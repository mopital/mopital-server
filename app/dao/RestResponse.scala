package dao

import models.{Bed, Beacon, Patient}
import play.Logger
import play.i18n.Messages
import reactivemongo.core.commands.LastError
import third.webcore.models.ResponseBase
import utils.Constants
import play.api.libs.json.{JsValue, JsArray, Json, JsObject}


import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 16/02/15.
 */
class RestResponse {

}
case class ResponseListPatient(result: ResponseBase, data:List[Patient]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsArray(data.map( patient => patient.toJson))))
  }
}
object ResponseListBook




case class ResponseListBeacon(result: ResponseBase, data:List[Beacon]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsArray(data.map( beacon => beacon.toJson))))
  }
}
object ResponseListBeacon




case class ResponseListBed(result: ResponseBase, data:List[Bed]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsArray(data.map( bed => bed.toJson))))
  }
}
object ResponseListBed


case class ResponsePatient(result: ResponseBase, data:Patient) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> data.toJson()))
  }
}
object ResponsePatient
