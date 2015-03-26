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

case class AddBeaconRequest (beaconUUID: String, major: Int, minor: Int) {}

object AddBeaconRequest {

  implicit val addBeaconRequest = Json.format[AddBeaconRequest]
}


case class AddBloodSugarMonitoringRequest (patientId: String, urineGlucose: String, bloodGlucose: String) {}

object AddBloodSugarMonitoringRequest {

  implicit val requestFormat = Json.format[AddBloodSugarMonitoringRequest]
}

case class AddPeriodicMonitoringRequest (patientId: String, tension: Double, fever: Double, pulse: Int, respiration: String, pain: String) {}

object AddPeriodicMonitoringRequest {

  implicit val requestFormat = Json.format[AddPeriodicMonitoringRequest]
}
