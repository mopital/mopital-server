package utils

import play.api.libs.json.JsObject
import play.api.mvc._
import reactivemongo.core.nodeset.Authenticated
import third.webcore.models.ResponseBase

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by ahmetkucuk on 26/03/15.
 */
trait ControllerHelperFunctions {

  self:Controller =>

  def getResponseFromResult(result: Boolean): Result = {
    if(result) {
      AllowRemoteResult(Ok(ResponseBase.success().toResultJson))
    } else {
      AllowRemoteResult(Ok(ResponseBase.error().toResultJson))
    }
  }

  def AllowRemoteResult(result: Result): Result = {
    result.withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Content-Type, X-Requested-With, Accept",
      // cache access control response for one day
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString
    )
  }


}
