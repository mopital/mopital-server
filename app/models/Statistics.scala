package models

import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by ahmetkucuk on 03/05/15.
 */
case class MopitalStatistics(numberOfBeds: Int, totalTreatment: Int, numberOfBeacons: Int, numberOfDoctor: Int, numberOfEquipment: Int, numberOfPatients: Int, numberOfEmergencyCall: Int) {

  def toJson(): JsValue = {
    JsObject(Seq("numberOfBeds" -> Json.toJson(numberOfBeds),
    "totalTreatment" -> Json.toJson(totalTreatment),
    "numberOfBeacons" -> Json.toJson(numberOfBeacons),
    "numberOfDoctor" -> Json.toJson(numberOfDoctor),
    "numberOfEquipment" -> Json.toJson(numberOfEquipment),
    "numberOfPatients" -> Json.toJson(numberOfPatients),
    "numberOfEmergencyCall" -> Json.toJson(numberOfEmergencyCall)
    ))
  }
}

