package third.webcore.services

import play.api.Logger
import play.api.libs.Crypto
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.i18n.Messages
import third.webcore.dao.WebCoreDaoComponent
import third.webcore.models._

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 8/6/14.
 */
trait UserServiceComponent {

  val userService: UserService

  trait UserService{
    def getUserByEmail(email: String): Future[Option[User]]
    def getUsers(): Future[List[User]]
    def register(registerRequest: RegisterRequest): Future[InternalResponse]
    def login(username: String, password: String): Future[Option[(String, User)]]
    def changeUserRole(changeUserRoleRequest: ChangeUserRoleRequest): Future[Boolean]
    def deleteUser(email: String): Future[Boolean]
    def changeUserPassword(email: String, changePasswordRequest: ChangePasswordRequest): Future[InternalResponse]
    def forgotPassword(email: String): Future[InternalResponse]
    def renewForgotPassword(renewPasswordRequest: RenewPasswordRequest): Future[InternalResponse]
  }
}

trait UserServiceComponentImpl extends UserServiceComponent {

  this: WebCoreDaoComponent with SessionServiceComponent =>

  val userService: UserService = new UserServiceImpl

  class UserServiceImpl extends UserService {

    def getUsers() = {
      userDao.getUsers()
    }


    def register(registerRequest: RegisterRequest): Future[InternalResponse] = {

      val passwordHash = Crypto.sign(registerRequest.email + registerRequest.password)
      if(!checkPasswordStrength(registerRequest.password)) {
        Logger.debug(s"[UserServiceComponent-register]: Password is not strong. Password: " + registerRequest.password)
        Future.successful(InternalResponse(false, Messages.get("password_not_strong")))
      } else {
        isThereUserWithEmail(registerRequest.email).flatMap(result =>

        if(!result) {
            Logger.debug(s"[UserServiceComponent-register]: Password and email are available email: " + registerRequest.email)
            val user = User(registerRequest.name, registerRequest.department, registerRequest.email, passwordHash, "USER")
            userDao.register(user).map( r => InternalResponse(r, Messages.get("success")))
          } else {
            Logger.debug(s"[UserServiceComponent-register]: Email is not available.  Email: " + registerRequest.email)
            Future.successful(InternalResponse(false, Messages.get("user_already_exists")))
          }
        )
      }
    }

    def isThereUserWithEmail(email:String): Future[Boolean] = {

      getUserByEmail(email).map{
        case Some(user) =>
          true
        case _ =>
          false
      }
  }

    def getUserByEmail(email: String) = {
      userDao.getUserByEmail(email)
    }

    override def login(email: String, password: String) = {
      val passwordHash = Crypto.sign(email + password)
      userDao.getUserByEmailAndPasswordHash(email, passwordHash).flatMap {
          case Some(user) =>
            sessionService.createSession(user).map { sessionId => Some(sessionId, user)}
          case None =>
            Future.successful(None)
        }
    }

    def changeUserRole(changeUserRoleRequest: ChangeUserRoleRequest): Future[Boolean] = {
      userDao.getUserByEmail(changeUserRoleRequest.userEmail).flatMap {
        case Some(user) =>
          val updatedUser: User = user.copy(role = changeUserRoleRequest.role)
          userDao.updateUserByEmail(changeUserRoleRequest.userEmail, updatedUser).map(result => result)
        case _ =>
          Future.successful(false)
      }
    }


    def changeUserPassword(email: String, changePasswordRequest: ChangePasswordRequest): Future[InternalResponse] = {
      userDao.getUserByEmail(email).flatMap {
          case Some(user) =>

            val passwordHash = Crypto.sign(email + changePasswordRequest.passwordOld)
            if (user.password.equalsIgnoreCase(passwordHash)) {

              if (checkPasswordStrength(changePasswordRequest.passwordNew)) {

                val newPasswordHash = Crypto.sign(email + changePasswordRequest.passwordNew)
                val updatedUser: User = user.copy(password = newPasswordHash)

                userDao.updateUserByEmail(email, updatedUser).map(result => InternalResponse(result, Messages.get("success")))
              } else {
                Future.successful(InternalResponse(false, Messages.get("password_not_strong")))
              }

            } else {
              Future.successful(InternalResponse(false, Messages.get("password_not_match")))
            }
          case _ =>
            Future.successful(InternalResponse(false, Messages.get("user_not_found")))
        }
    }

    def forgotPassword(email: String): Future[InternalResponse] = {

      userDao.getUserByEmail(email).map {
        case Some(user) =>
          //email service
          //notifyUserForPassword(email, user.password)
          InternalResponse(true, Messages.get("success"))
        case _ =>
          InternalResponse(false, Messages.get("user_not_found"))
      }
    }


    def renewForgotPassword(renewPasswordRequest: RenewPasswordRequest): Future[InternalResponse] = {

      userDao.getUserByEmailAndPasswordHash(renewPasswordRequest.email, renewPasswordRequest.hash).flatMap {
        case Some(user) =>
          if(checkPasswordStrength(renewPasswordRequest.newPassword)) {

            val newPasswordHash: String = Crypto.sign(renewPasswordRequest.email + renewPasswordRequest.newPassword)
            val updatedUser: User = user.copy(password = newPasswordHash)
            userDao.updateUserByEmail(renewPasswordRequest.email, updatedUser).map(result =>
              if (result) {
                Logger.debug(s"[UserServiceComponent-forgotPassword]: new password created ${renewPasswordRequest.newPassword}")
                InternalResponse(true, Messages.get("success"))
              } else {
                InternalResponse(false, Messages.get("update_error"))
              }
            )
          } else {
            Future.successful(InternalResponse(false, Messages.get("password_not_strong")))
          }
        case _ =>
          Future.successful(InternalResponse(false, Messages.get("user_not_found")))
      }
    }

    def deleteUser(email: String): Future[Boolean] = {
      userDao.delete(email)
    }

    def randomAlphaNumericString(length: Int): String = {
      val chars = ('a' to 'z') ++ ('A' to 'Z') ++ ('0' to '9')
      randomStringFromCharList(length, chars)
    }

    def randomStringFromCharList(length: Int, chars: Seq[Char]): String = {
      val sb = new StringBuilder
      for (i <- 1 to length) {
        val randomNum = scala.util.Random.nextInt(chars.length)
        sb.append(chars(randomNum))
      }
      sb.toString
    }

      /*
      return true if password is strong
       */
      def checkPasswordStrength(password: String): Boolean = {
        if(password.length < 8)
          false
        val isThereUpperCase: Int = if((password.toList intersect (('A' to 'Z').toList)).size > 0)  1 else 0
        val isThereLowerCase: Int = if((password.toList intersect (('a' to 'z').toList)).size > 0)  1 else 0
        val isThereDigit: Int = if((password.toList. intersect ((0 to 9).toList)).size > 0)  1 else 0
        val isThereSpecialChar: Int = if((password.toList intersect List('.', '!', ',', '+', '-', '*', '?', '_', '@')).size > 0)  1 else 0
        val total = isThereUpperCase + isThereLowerCase + isThereDigit + isThereSpecialChar;
        if(total < 3 )
          return false
        true
      }
    }

}
