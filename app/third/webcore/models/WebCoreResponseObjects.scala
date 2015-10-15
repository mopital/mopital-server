package third.webcore.models

import play.api.Logger
import play.api.libs.json.{Json, JsArray, JsObject, JsValue}
import reactivemongo.core.commands.LastError
import utils.WebCoreConstants

/**
 * Created by ahmetkucuk on 07/03/15.
 */
class WebCoreResponseObjects {

}


case class ResponseListUser(result: ResponseBase, data:List[User]) {
  def toJson: JsObject = {
    JsObject(Seq(WebCoreConstants.RESULT -> result.toJson, WebCoreConstants.DATA -> JsObject(Seq("userList" -> JsArray(data.map( user => user.toJson))))))
  }
}
object ResponseListUser


case class ResponseUser(result: ResponseBase, data:User) {
  def toJson: JsObject = {
    JsObject(Seq(WebCoreConstants.RESULT -> result.toJson, WebCoreConstants.DATA -> data.toJson))
  }
}
object ResponseUser


case class InternalResponse(status: Boolean, msg: String) {

  def getResponse(): JsValue = {
    if(status) {
      ResponseBase.success(msg).toResultJson
    } else {
      ResponseBase.error(msg).toResultJson
    }
  }
}

case class ResponseBase(status: String, statusCode: Int, msg: String) {

  def toJson: JsValue = {
    JsObject(Seq(WebCoreConstants.STATUS -> Json.toJson(status), WebCoreConstants.STATUS_CODE -> Json.toJson(statusCode), WebCoreConstants.MSG -> Json.toJson(msg)))
  }

  def toResultJson: JsValue = {
    JsObject(Seq(WebCoreConstants.RESULT -> (JsObject(Seq(WebCoreConstants.STATUS -> Json.toJson(status), WebCoreConstants.STATUS_CODE-> Json.toJson(statusCode), WebCoreConstants.MSG -> Json.toJson(msg))))))
  }
}

object ResponseBase {

  implicit val responseBaseFormat = Json.format[ResponseBase]

  def success(): ResponseBase = {
    ResponseBase(WebCoreConstants.SUCCESS, WebCoreConstants.SUCCESS_CODE, "success")
  }

  def success(msg: String): ResponseBase = {
    ResponseBase(WebCoreConstants.SUCCESS, WebCoreConstants.SUCCESS_CODE, msg)
  }


  def error(message: String): ResponseBase = {
    ResponseBase(WebCoreConstants.ERROR, WebCoreConstants.GENERAL_ERROR_CODE, message)
  }


  def error(): ResponseBase = {
    ResponseBase(WebCoreConstants.ERROR, WebCoreConstants.GENERAL_ERROR_CODE, "error")
  }

  def response(result:Boolean): JsValue = {
    if(result) {
      ResponseBase.success().toResultJson
    } else {
      ResponseBase.error().toResultJson
    }
  }

  def response(internalResponse: InternalResponse): JsValue = {
    if(internalResponse.status) {
      ResponseBase.success(internalResponse.msg).toResultJson
    } else {
      ResponseBase.error(internalResponse.msg).toResultJson
    }
  }

};


object InternalResponse {

  def response(lastError: LastError): InternalResponse = {
    if(lastError.inError) {
      Logger.error("[Internal Response Error: ", lastError)
      InternalResponse(false, lastError.err.getOrElse(""))
    } else {
      InternalResponse(true, lastError.err.getOrElse(""))
    }
  }

  def response(result: Boolean): InternalResponse = {
    if(result) {
      InternalResponse(true, "")
    } else {
      InternalResponse(false, "")
    }
  }

}
