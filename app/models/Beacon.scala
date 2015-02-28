package models

import play.api.libs.json.{Json, JsObject, JsValue}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}
import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 18/02/15.
 */
case class Beacon(val id: Option[BSONObjectID], val uuid: String, val major: Int, val minor: Int) {

  def this() {
    this(Option(BSONObjectID.generate), "no_uuid", 100, 100)
  }


  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "uuid" -> Json.toJson(uuid),
      "major" -> Json.toJson(major),
      "minor" -> Json.toJson(minor)
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
        "minor" -> beacon.minor)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BeaconBSONReader extends BSONDocumentReader[Beacon] {
    def read(doc: BSONDocument): Beacon =
      Beacon(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("uuid").get,
        doc.getAs[Int]("major").get,
        doc.getAs[Int]("minor").get
      )
  }

}
