package models

import models.AddBeaconLogRequest
import play.api.libs.json.{JsValue, JsObject, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by ahmetkucuk on 04/05/15.
 */
case class BeaconLog(recordedAt: Long, email: String, minor: Int) {

  def this(addBeaconLogRequest: AddBeaconLogRequest) = {
    this(System.currentTimeMillis(), addBeaconLogRequest.email, addBeaconLogRequest.minor)
  }

  def toJson(): JsValue = {
    JsObject(Seq("recordedAt" -> Json.toJson(recordedAt), "email" -> Json.toJson(email), "minor" -> Json.toJson(minor)))
  }

}


object BeaconLog {

  implicit val beaconFormat = Json.format[BeaconLog]

  implicit object BeaconLogBSONWriter extends BSONDocumentWriter[BeaconLog] {
    def write(beaconLog: BeaconLog): BSONDocument =
      BSONDocument(
        "recordedAt" -> beaconLog.recordedAt,
        "email" -> beaconLog.email,
        "minor" -> beaconLog.minor
      )
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BeaconLogBSONReader extends BSONDocumentReader[BeaconLog] {
    def read(doc: BSONDocument): BeaconLog =
      BeaconLog(
        doc.getAs[Long]("recordedAt").get,
        doc.getAs[String]("email").get,
        doc.getAs[Int]("minor").get
      )
  }

}
