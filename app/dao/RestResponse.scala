package dao

import models._
import play.Logger
import play.i18n.Messages
import reactivemongo.core.commands.LastError
import third.webcore.models.ResponseBase
import utils.Constants
import play.api.libs.json.{JsValue, JsArray, Json, JsObject}


import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.util.parsing.json.JSONArray

/**
 * Created by ahmetkucuk on 16/02/15.
 */
class RestResponse {

}
case class ResponseListPatient(result: ResponseBase, data:List[Patient]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsObject(Seq("patientList" -> JsArray(data.map( patient => patient.toJson))))))
  }
}
object ResponseListBook




case class ResponseListBeacon(result: ResponseBase, data:List[Beacon]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsObject(Seq("beaconList" -> JsArray(data.map( beacon => beacon.toJson))))))
  }
}
object ResponseListBeacon

object ResponseStatistic

case class ResponseStatistic(result: ResponseBase, data:MopitalStatistics) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> data.toJson()))
  }
}




case class ResponseListBed(result: ResponseBase, data:List[Bed]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsObject(Seq("bedList" -> JsArray(data.map( bed => bed.toJson))))))
  }
}
object ResponseListBed


case class ResponseBaseModel(result: ResponseBase, data:BaseModel) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> data.toJson()))
  }
}
object ResponsePatient

case class ResponseListEquipment(result: ResponseBase, data:List[Equipment]) {

  def toJson: JsObject = {
    JsObject(Seq(Constants.RESULT -> result.toJson, Constants.DATA -> JsObject(Seq("equipmentList" -> JsArray(data.map( equipment => equipment.toJson))))))
  }
}
object ResponseListEquipment
