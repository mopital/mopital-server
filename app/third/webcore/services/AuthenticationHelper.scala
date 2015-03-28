package third.webcore.services

import controllers.routes
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import utils.Constants

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 8/12/14.
 */


trait AuthenticationHelper {
  self:Controller with SessionServiceComponent =>

  def Authenticated[T](bp: BodyParser[T])(f: (Request[T], String, String) => Future[Result]): Action[T] = Action.async(bp) { request =>

    val emailOpt = request.session.get(Constants.EMAIL)
    val sessionIdOpt = request.session.get(Constants.SESSION_ID)

    (emailOpt, sessionIdOpt) match {

      case (Some(email), Some(sessionId)) =>
        Logger.debug(s"[UserController-Authenticate] In Authentication email: $email sessionId: $sessionId")
        sessionService.checkValidityAndUpdateIfValid(email, sessionId).flatMap { sessionResult =>

          if(sessionResult) {

              //This section is written for admin authentication controller
              //Currently we don't need to use this
//            val adminActionPathList = List("/api/books/acceptReturn", "/api/books/edit", "/api/user/role")
//            if(request.path.contains(Constants.PATH_ADMIN) || adminActionPathList.contains(request.path)) {
//
//              request.session.get(Constants.ROLE) match {
//
//                case Some(role) if role.equalsIgnoreCase(Constants.ROLE_ADMIN) =>
//                  f(request, email, sessionId)
//                case _ =>
//                  Redirect(routes.Application.error("authorization"))
//              }
//            } else {
//              f(request, email, sessionId)
//            }

            f(request, email, sessionId)
          } else {
            Future.successful(Redirect(routes.Application.error()))
          }
        }
      case _ =>
        Future.successful(Redirect(routes.Application.index("none")))
    }

  }

  def Authenticated(f: (Request[AnyContent], String, String) => Future[Result]):Action[AnyContent] = Authenticated(parse.anyContent)(f)
  def Authenticated(f: => Future[Result]):Action[AnyContent]  = Authenticated(parse.anyContent)((_, _, _) => f)

}