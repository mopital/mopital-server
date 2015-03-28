package utils

import java.lang.reflect.Constructor

import models._
import play.api.libs.json.{JsArray, Json, JsObject, JsValue}

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 28/03/15.
 */
object ApiGenerator {

  def classToAttributeString(inputClass: Object): String = {
    var s: String = ""
    inputClass.getClass.getDeclaredFields.foreach(field => s += field.getName + " : " + cutOption(field.getGenericType.toString) +  ", ")
    cutFromLastPoint(inputClass.getClass.getName) + "\t----->" + s
  }

  def cutFromLastPoint(s: String) = { s.substring(s.lastIndexOf(".")+1)}

  def cutOption(s: String) = {
    if(!s.contains("Option")) {
      cutFromLastPoint(s)
    } else {
      "Option[" + s.substring(s.lastIndexOf(".")+1, s.length-1) + "]"
    }
  }

  def getRequestClassAttributes(): JsValue = {
    val classList = List(new AddBeaconRequest, new AddBedRequest(10), new AddBloodSugarMonitoringRequest("","",""), AddEquipmentRequest("", "", ""), AddPatientRequest(10, "", 11, 1.2, 1.2, "", "", "", "", "", "", "", "", ""), new AddPeriodicMonitoringRequest("", 1.3, 1.2, 10, "", ""), new AddTreatmentRequest("", None, None, None, None, None, None, None, None), new GetEquipmentLastPositionRequest(""), new GetEquipmentRequest(""), new SetBeaconToBedRequest("",""), new SetBeaconToEquipment("", "") )
    JsArray(classList.map(o => Json.toJson(classToAttributeString(o))))
  }

  def getRouteElements(): JsValue = {
    JsArray(play.api.Play.current.routes.toList(0).documentation.map( a => JsObject(Seq("method" -> Json.toJson(a._1), "path" -> Json.toJson(a._2)))))
  }

  def getApiJson(): JsValue = {
    JsObject(Seq("api" -> JsObject(Seq("requestObjects" -> getRequestClassAttributes(), "methodsAndPaths" -> getRouteElements()))))
  }

}
