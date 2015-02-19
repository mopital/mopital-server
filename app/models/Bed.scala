package models

import play.api.libs.json.{Json, JsObject, JsValue}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}
import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 18/02/15.
 */
case class Bed(id: Option[BSONObjectID], bed_no: Int, beacon: Beacon) {

  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "bed_no" -> Json.toJson(bed_no),
      "beacon" -> Json.toJson(beacon)
    ))
  }
}

object Bed {

  implicit val bedFormat = Json.format[Bed]

  implicit object BedBSONWriter extends BSONDocumentWriter[Bed] {
    def write(bed: Bed): BSONDocument =
      BSONDocument(
        "_id" -> bed.id.getOrElse(BSONObjectID.generate),
        "number" -> bed.bed_no,
        "beacon" -> bed.beacon)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BedBSONReader extends BSONDocumentReader[Bed] {
    def read(doc: BSONDocument): Bed =
      Bed(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Int]("number").get,
        doc.getAs[Beacon]("beacon").get
      )
  }

}

