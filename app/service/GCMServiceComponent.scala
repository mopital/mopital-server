package service

import dao.DaoComponent
import models._
import play.api.Logger
import play.api.libs.json.{Json, JsArray, JsObject}
import play.api.libs.ws.WS

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.Play.current

/**
 * Created by ahmetkucuk on 24/04/15.
 */
trait GCMServiceComponent {

  val gcmService: GCMService

  trait GCMService {

    def add(addGCMRequest: AddGCMRequest): Future[Boolean]
    def notifyUser(notifyUserRequest: NotifyUserRequest): Future[Boolean]

  }
}

trait GCMServiceComponentImpl extends GCMServiceComponent {

  this: DaoComponent =>

  val gcmService: GCMService = new GCMServiceImpl

  class GCMServiceImpl extends GCMService {

    def add(addGCMRequest: AddGCMRequest): Future[Boolean] = {
      gcmDao.add(new GCMHolder(addGCMRequest))
    }

    def notifyUser(notifyUserRequest: NotifyUserRequest): Future[Boolean] = {
      gcmDao.getGCMIdByUserId(notifyUserRequest.userId).map( gcmHolder =>
        gcmHolder match {
          case Some(gcm) =>
            sendNotificationPost(gcm.gcmId, notifyUserRequest.messageToSend)
            true
          case _ =>
            false
        }
      )
    }

    def sendNotificationPost(gcmId: String, answer: String) = {

      Logger.debug(s"in sendNotificationPost gcmId: $gcmId answer: $answer")

      val url = "https://android.googleapis.com/gcm/send"

      val regIds = JsObject(Seq("registration_ids" -> JsArray(Seq(Json.toJson(gcmId))), "data" -> JsObject(Seq("answer" -> Json.toJson(answer)))))
      val res = WS.url(url)
        .withHeaders("Authorization" -> "key=AIzaSyCJyPltXLMHLzfMMWVPnmXAm6gHFi9aNUE", "Content-Type" -> "application/json")
        .post(regIds)
      res.map(response => Logger.debug("[UserServiceComponent-sendNotificationPost] notification Response: " + response.status + " " + response.body))
    }

  }
}

