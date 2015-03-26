package models

import play.api.libs.json.{JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter}
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Created by ahmetkucuk on 22/03/15.
 */
case class BloodSugarMonitoring(recordedAt: Long, urineGlucose: String, bloodGlucose: String ) {

  def this(addBloodSugarMonitoringRequest: AddBloodSugarMonitoringRequest) {
    this(System.currentTimeMillis(), addBloodSugarMonitoringRequest.urineGlucose, addBloodSugarMonitoringRequest.bloodGlucose)
  }

  def toJson(): JsValue = {
    JsObject(Seq("recordedAt" -> Json.toJson(recordedAt),
      "urineGlucose" -> Json.toJson(urineGlucose),
      "bloodGlucose" -> Json.toJson(bloodGlucose)))
  }
}

object BloodSugarMonitoring {

  implicit val bloodSugarFormat = Json.format[BloodSugarMonitoring]

  implicit object BloodSugarPainBSONWriter extends BSONDocumentWriter[BloodSugarMonitoring] {
    def write(patientPain: BloodSugarMonitoring): BSONDocument =
      BSONDocument(
        "recordedAt" -> patientPain.recordedAt,
        "urineGlucose" -> patientPain.urineGlucose,
        "bloodGlucose" -> patientPain.bloodGlucose)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BloodSugarBSONReader extends BSONDocumentReader[BloodSugarMonitoring] {
    def read(doc: BSONDocument): BloodSugarMonitoring =
      BloodSugarMonitoring(
        doc.getAs[Long]("recordedAt").get,
        doc.getAs[String]("urineGlucose").get,
        doc.getAs[String]("bloodGlucose").get
      )
  }
}


