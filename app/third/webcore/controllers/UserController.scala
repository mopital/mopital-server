package third.webcore.controllers

import play.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import play.i18n.Messages
import third.webcore.dao.WebCoreDaoComponentImpl
import third.webcore.models._
import third.webcore.services._
import utils.{WebCoreConstants, ControllerHelperFunctions}

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 8/6/14.
 */

trait UserController extends Controller with UserServiceComponent with SessionServiceComponent with AuthenticationHelper with ControllerHelperFunctions{

  def get(email: String) = Authenticated {

    val result = userService.getUserByEmail(email)
    result.map {
      case None =>
        AllowRemoteResult(Ok(ResponseBase.error(Messages.get("user_not_found")).toResultJson))
      case Some(user) =>
        AllowRemoteResult(Ok(ResponseUser(ResponseBase.success(), user).toJson))
    }
  }

  def getUser = Authenticated(parse.json) { (request, email, _) =>

      userService.getUserByEmail(email).map {
        case None =>
          AllowRemoteResult(Ok(ResponseBase.error(Messages.get("user_not_found")).toResultJson))
        case Some(user) =>
          AllowRemoteResult(Ok(ResponseUser(ResponseBase.success(), user).toJson))
      }
  }

  def index = Action.async {
      userService.getUsers().map{users =>
        AllowRemoteResult(Ok(ResponseListUser(ResponseBase.success(), users).toJson))
      }
  }

  def login() = Action.async(parse.json) { request =>

      request.body.validate[LoginRequest].fold (
        valid = { loginRequest =>
          userService.login(loginRequest.email, loginRequest.password).map{
            case Some((sessionId, user)) =>
              Logger.info("Login Success With email: " + loginRequest.email + " password: " + loginRequest.password)
              if(user.role.equalsIgnoreCase(WebCoreConstants.ROLE_ADMIN)) {
                AllowRemoteResult(Ok(ResponseUser(ResponseBase.success(), user).toJson).withSession(WebCoreConstants.SESSION_ID -> sessionId, WebCoreConstants.EMAIL -> user.email, WebCoreConstants.ROLE -> WebCoreConstants.ROLE_ADMIN))
              } else {
                AllowRemoteResult(Ok(ResponseUser(ResponseBase.success(), user).toJson).withSession(WebCoreConstants.SESSION_ID -> sessionId, WebCoreConstants.EMAIL -> user.email, WebCoreConstants.ROLE -> WebCoreConstants.ROLE_USER))
              }
            case None =>
              Logger.info("Login Fail with email: " + loginRequest.email + " password: " + loginRequest.password)
              AllowRemoteResult(Ok(ResponseBase.error().toResultJson))
          }
        },
        invalid = { e =>
          Logger.error(s"[UserController-login] error: $e")
          Future.successful(AllowRemoteResult(Ok(ResponseBase.error().toResultJson)))
        }
      )
  }

  def register() = Action.async(parse.json) { request =>

      request.body.validate[RegisterRequest].fold (
        valid = { registerRequest =>
          userService.register(registerRequest).map{ internalResponse =>
            AllowRemoteResult(Ok(ResponseBase.response(internalResponse)))
          }
        },
        invalid = {e => Logger.error(s"[UserController-register] $e");
          Future.successful(AllowRemoteResult(Ok(ResponseBase.error().toResultJson)))
        }
      )
  }

  def changeUserRole() = Authenticated(parse.json) { (request, _, _) =>

      request.body.validate[ChangeUserRoleRequest].fold(
        valid = {changeUserRoleRequest =>
          userService.changeUserRole(changeUserRoleRequest).map { result =>
            AllowRemoteResult(Ok(ResponseBase.response(result)))
          }
        },
        invalid = {e => Logger.error(s"[changeUserRole] error: $e")
          Future.successful(AllowRemoteResult(Ok(ResponseBase.error(Messages.get("json_field_error")).toResultJson)))
        }
      )
  }

  def changePassword() = Authenticated(parse.json) { (request, _, _) =>

      request.body.validate[ChangePasswordRequest].fold(
        valid = {changePasswordRequest =>
          val email: String = request.session.get("email").get
          userService.changeUserPassword(email, changePasswordRequest).map(internalResponse =>
            AllowRemoteResult(Ok(internalResponse.getResponse()))
          )
        },
        invalid = {e => Logger.debug(s"[ChangePassword] error: $e")
          Future.successful(AllowRemoteResult(Ok(ResponseBase.error(Messages.get("json_field_error")).toResultJson)))
        }
      )
  }

  def forgotPassword = Action.async(parse.json) { request =>

      request.body.\("email").asOpt[String] match {
        case Some(email) =>
          userService.forgotPassword(email).map( internalResponse => AllowRemoteResult(Ok(internalResponse.getResponse())))
        case _ =>
          Future.successful(AllowRemoteResult(Ok(ResponseBase.error(Messages.get("json_field_error")).toResultJson)))
      }
  }

  def renewPassword = Action.async(parse.json) { request =>

      request.body.validate[RenewPasswordRequest].fold(

        valid = {renewPasswordRequest =>
          userService.renewForgotPassword(renewPasswordRequest).map(internalResponse => AllowRemoteResult(Ok(internalResponse.getResponse())))
        },
        invalid = {e => Logger.error(s"[UserController - RenewPassword] $e")
          Future.successful(AllowRemoteResult(Ok(ResponseBase.error(Messages.get("json_field_error")).toResultJson)))
        }
      )
  }

  def deleteUser(email: String) = Authenticated { (request, _, _) =>
      if(request.session.get("role").get.equalsIgnoreCase(WebCoreConstants.ROLE_ADMIN)) {
        userService.deleteUser(email).map(result =>
          AllowRemoteResult(Ok(ResponseBase.response(result)))
        )
      } else {
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error(Messages.get("remove_user_permission_denied")).toResultJson)))
      }
  }
}
object UserController extends UserController
                      with UserServiceComponentImpl
                      with WebCoreDaoComponentImpl
                      with SessionServiceComponentImpl