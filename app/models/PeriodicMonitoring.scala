package models

import play.api.libs.json.{JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter}
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Created by ahmetkucuk on 22/03/15.
 */
case class PeriodicMonitoring(recordedAt: Long, tension: Double, fever: Double, pulse: Int, respiration: String, pain: String) {

  def this(addPeriodicMonitoringRequest: AddPeriodicMonitoringRequest) {

    this(System.currentTimeMillis(),
      addPeriodicMonitoringRequest.tension,
      addPeriodicMonitoringRequest.fever,
      addPeriodicMonitoringRequest.pulse,
      addPeriodicMonitoringRequest.respiration,
      addPeriodicMonitoringRequest.pain
    )
  }

  def toJson(): JsValue = {
    JsObject(Seq("recordedAt" -> Json.toJson(recordedAt),
      "tension" -> Json.toJson(tension),
      "fever" -> Json.toJson(fever),
      "pulse" -> Json.toJson(pulse),
      "respiration" -> Json.toJson(respiration),
      "pain" -> Json.toJson(pain)
    ))
  }

}


object PeriodicMonitoring {

  implicit val periodicMonitoringFormat = Json.format[PeriodicMonitoring]

  implicit object PeriodicMonitoringBSONWriter extends BSONDocumentWriter[PeriodicMonitoring] {
    def write(periodicMonitoring: PeriodicMonitoring): BSONDocument =
      BSONDocument(
        "recordedAt" -> periodicMonitoring.recordedAt,
        "tension" -> periodicMonitoring.tension,
        "fever" -> periodicMonitoring.fever,
        "pulse" -> periodicMonitoring.pulse,
        "respiration" -> periodicMonitoring.respiration,
        "pain" -> periodicMonitoring.pain)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object PeriodicMonitoringBSONReader extends BSONDocumentReader[PeriodicMonitoring] {
    def read(doc: BSONDocument): PeriodicMonitoring =
      PeriodicMonitoring(
        doc.getAs[Long]("recordedAt").get,
        doc.getAs[Double]("tension").get,
        doc.getAs[Double]("fever").get,
        doc.getAs[Int]("pulse").get,
        doc.getAs[String]("respiration").get,
        doc.getAs[String]("pain").get)
  }

}


