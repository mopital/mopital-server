package models

import play.api.libs.json.{Json, JsObject, JsValue}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}
import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 18/02/15.
 */
case class Beacon(val id: Option[BSONObjectID], val number: String, val major: Int, val minor: Int) {

  def this() {
    this(Option(BSONObjectID.generate), "no_number", 100, 100)
  }


  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "number" -> Json.toJson(number),
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
        "number" -> beacon.number,
        "major" -> beacon.major,
        "minor" -> beacon.minor)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BeaconBSONReader extends BSONDocumentReader[Beacon] {
    def read(doc: BSONDocument): Beacon =
      Beacon(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("number").get,
        doc.getAs[Int]("major").get,
        doc.getAs[Int]("minor").get
      )
  }

}
