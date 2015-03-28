package models

import models.BeaconType.BeaconType
import play.api.libs.json.{Json, JsObject, JsValue}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}
import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 18/02/15.
 */
case class Beacon(val id: Option[BSONObjectID], val uuid: String, val major: Int, val minor: Int, beaconType: String, position: String) extends BaseModel{

  def this() {
    this(Option(BSONObjectID.generate), "no_uuid", 100, 100, BeaconType.LocationBeacon.toString, "Unknown")
  }

  def this(addBeaconRequest: AddBeaconRequest) {
    this(Option(BSONObjectID.generate),
      addBeaconRequest.beaconUUID,
      addBeaconRequest.major,
      addBeaconRequest.minor,
      addBeaconRequest.beaconType,
      addBeaconRequest.position
    )
  }


  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "uuid" -> Json.toJson(uuid),
      "major" -> Json.toJson(major),
      "minor" -> Json.toJson(minor),
      "beaconType" -> Json.toJson(beaconType),
      "position" -> Json.toJson(position)
    ))
  }
}

object Beacon {

  implicit val beaconFormat = Json.format[Beacon]

  implicit object BeaconBSONWriter extends BSONDocumentWriter[Beacon] {
    def write(beacon: Beacon): BSONDocument =
      BSONDocument(
        "_id" -> beacon.id.getOrElse(BSONObjectID.generate),
        "uuid" -> beacon.uuid,
        "major" -> beacon.major,
        "minor" -> beacon.minor,
        "beacon_type" -> beacon.beaconType,
        "position" -> beacon.position
      )
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BeaconBSONReader extends BSONDocumentReader[Beacon] {
    def read(doc: BSONDocument): Beacon =
      Beacon(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("uuid").get,
        doc.getAs[Int]("major").get,
        doc.getAs[Int]("minor").get,
        doc.getAs[String]("beacon_type").get,
        doc.getAs[String]("position").get
    )
  }

}
