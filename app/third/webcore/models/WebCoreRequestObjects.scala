package third.webcore.models

import play.api.libs.json.Json

/**
 * Created by ahmetkucuk on 07/03/15.
 */
class WebCoreRequestObjects {

}

//User
object LoginRequest {

  implicit val loginRequestFormat = Json.format[LoginRequest]
}

case class LoginRequest(email:String, password:String)


object RegisterRequest {

  implicit val registerRequestFormat = Json.format[RegisterRequest]
}

case class RegisterRequest(name: String, department:String, email:String, password:String)



case class ChangePasswordRequest(passwordOld: String, passwordNew: String)

object ChangePasswordRequest {

  implicit val changePasswordRequestFormat = Json.format[ChangePasswordRequest]
}


case class ChangeUserRoleRequest(userEmail: String, role: String)

object ChangeUserRoleRequest {

  implicit val changeUserRoleRequestFormat = Json.format[ChangeUserRoleRequest]
}


case class RenewPasswordRequest(email: String, hash: String, newPassword: String)

object RenewPasswordRequest {

  implicit  val renewPasswordRequest = Json.format[RenewPasswordRequest]
}

