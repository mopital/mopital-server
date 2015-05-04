package models

import play.api.libs.json.{JsValue, JsObject, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by ahmetkucuk on 04/05/15.
 */
case class BeaconLog(recordedAt: Long, email: String, beacon: Beacon) {

  def this(addBeaconLogRequest: AddBeaconLogRequest, beacon: Beacon) = {
    this(System.currentTimeMillis(), addBeaconLogRequest.email, beacon)
  }

  def toJson(): JsValue = {
    JsObject(Seq("recordedAt" -> Json.toJson(recordedAt), "email" -> Json.toJson(email), "beacon" -> Json.toJson(beacon)))
  }

}


object BeaconLog {

  implicit val beaconFormat = Json.format[BeaconLog]

  implicit object BeaconLogBSONWriter extends BSONDocumentWriter[BeaconLog] {
    def write(beaconLog: BeaconLog): BSONDocument =
      BSONDocument(
        "recordedAt" -> beaconLog.recordedAt,
        "email" -> beaconLog.email,
        "beacon" -> beaconLog.beacon
      )
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BeaconLogBSONReader extends BSONDocumentReader[BeaconLog] {
    def read(doc: BSONDocument): BeaconLog =
      BeaconLog(
        doc.getAs[Long]("recordedAt").get,
        doc.getAs[String]("email").get,
        doc.getAs[Beacon]("beacon").get
      )
  }

}
