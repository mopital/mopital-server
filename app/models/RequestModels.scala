package models

import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID

/**
 * Created by ahmetkucuk on 02/03/15.
 */
class RequestModels{}
//Patient


case class AddPatientRequest (bedNumber: Int, name: String, age: Int, weight: Double, height:Double, bloodType: String, fileNo: String, admissionDate: String, diagnoses: String, allergy: String, nurse: String, painRegion: String, painType: String, painDuration: String) {}

object AddPatientRequest {

  implicit val addPatientRequest = Json.format[AddPatientRequest]
}

case class AddTreatmentRequest(patientId: String, date: Option[String], time: Option[String], tension: Option[String], temperature: Option[String], pulse: Option[String], respiration: Option[String], pain: Option[String], definition: Option[String]) {

}

object AddTreatmentRequest {

  implicit val addTreatmentRequest = Json.format[AddTreatmentRequest]
}

case class AddBeaconRequest (beaconUUID: String, major: Int, minor: Int, beaconType: String, position: String) {
  def this() = this("", 10, 10, "", "")
}

object AddBeaconRequest {

  implicit val addBeaconRequest = Json.format[AddBeaconRequest]
}

case class UpdateBeaconRequest (id: String, beaconUUID: String, major: Int, minor: Int, beaconType: String, position: String) {
  def this() = this("", "", 10, 10, "", "")
}

object UpdateBeaconRequest {

  implicit val request = Json.format[UpdateBeaconRequest]
}

case class AddBedRequest (bedNo: Int) {}

object AddBedRequest {

  implicit val requestFormat = Json.format[AddBedRequest]
}

case class SetBeaconToBedRequest (bedId: String, beaconId: String) {}

object SetBeaconToBedRequest {

  implicit val requestFormat = Json.format[SetBeaconToBedRequest]
}


case class AddBloodSugarMonitoringRequest (patientId: String, urineGlucose: String, bloodGlucose: String) {}

object AddBloodSugarMonitoringRequest {

  implicit val requestFormat = Json.format[AddBloodSugarMonitoringRequest]
}

case class AddPeriodicMonitoringRequest (patientId: String, tension: Double, fever: Double, pulse: Int, respiration: String, pain: String) {}

object AddPeriodicMonitoringRequest {

  implicit val requestFormat = Json.format[AddPeriodicMonitoringRequest]
}

case class AddEquipmentRequest(recordId: String, name: String, typeOfEquipment: String) {}

object AddEquipmentRequest{

  implicit val requestFormat = Json.format[AddEquipmentRequest]
}


case class SetBeaconToEquipment(equipmentId: String, beaconId: String) {}

object SetBeaconToEquipment{

  implicit val requestFormat = Json.format[SetBeaconToEquipment]
}

case class GetEquipmentRequest(equipmentId: String) {}

object GetEquipmentRequest{

  implicit val requestFormat = Json.format[GetEquipmentRequest]
}

case class GetEquipmentLastPositionRequest(equipmentId: String) {}

object GetEquipmentLastPositionRequest {

  implicit val requestFormat = Json.format[GetEquipmentLastPositionRequest]
}

case class AddGCMRequest(userId: String, gcmId: String) {}

object AddGCMRequest {

  implicit val requestFormat = Json.format[AddGCMRequest]
}

case class NotifyUserRequest(userId: String, messageToSend: String) {}

object NotifyUserRequest {

  implicit val requestFormat = Json.format[NotifyUserRequest]
}

