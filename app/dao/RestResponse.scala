package dao

import java.awt.print.Book

import models.{Patient}
import play.i18n.Messages
import utils.Constants
import play.api.libs.json.{JsValue, JsArray, Json, JsObject}


import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 16/02/15.
 */
class RestResponse {

}

case class ResponseBase(status: String, statusCode: Int, msg: String) {

  def toJson: JsValue = {
    JsObject(Seq(Constants.STATUS -> Json.toJson(status), Constants.STATUS_CODE -> Json.toJson(statusCode), Constants.MSG -> Json.toJson(msg)))
  }

  def toResultJson: JsValue = {
    JsObject(Seq(Constants.RESULT -> (JsObject(Seq(Constants.STATUS -> Json.toJson(status), Constants.STATUS_CODE-> Json.toJson(statusCode), Constants.MSG -> Json.toJson(msg))))))
  }
}


case class ResponseListPatient(result: ResponseBase, data:List[Patient]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsArray(data.map( patient => patient.toJson))))
  }
}
object ResponseListBook

object ResponseBase {
  implicit val responseBaseFormat = Json.format[ResponseBase]

  def success(): ResponseBase = {
    ResponseBase(Constants.SUCCESS, Constants.SUCCESS_CODE, Messages.get("success"))
  }

  def success(msg: String): ResponseBase = {
    ResponseBase(Constants.SUCCESS, Constants.SUCCESS_CODE, msg)
  }


  def error(message: String): ResponseBase = {
    ResponseBase(Constants.ERROR, Constants.GENERAL_ERROR_CODE, message)
  }


  def error(): ResponseBase = {
    ResponseBase(Constants.ERROR, Constants.GENERAL_ERROR_CODE, Messages.get("error"))
  }

  def response(result:Boolean): JsValue = {
    if(result) {
      ResponseBase.success().toResultJson
    } else {
      ResponseBase.error().toResultJson
    }
  }

//  def response(internalResponse: InternalResponse): JsValue = {
//    if(internalResponse.status) {
//      ResponseBase.success(internalResponse.msg).toResultJson
//    } else {
//      ResponseBase.error(internalResponse.msg).toResultJson
//    }
//  }

};
