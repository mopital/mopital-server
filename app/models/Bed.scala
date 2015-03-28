package models

import play.api.libs.json.{Json, JsObject, JsValue}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}
import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 18/02/15.
 */
case class Bed(id: Option[BSONObjectID], bed_number: Int, beacon: Beacon) extends BaseModel{

  def this(addBedRequest: AddBedRequest) = {
    this(Option(BSONObjectID.generate), addBedRequest.bedNo, new Beacon())
  }

  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "bed_number" -> Json.toJson(bed_number),
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
        "bed_number" -> bed.bed_number,
        "beacon" -> bed.beacon)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BedBSONReader extends BSONDocumentReader[Bed] {
    def read(doc: BSONDocument): Bed =
      Bed(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Int]("bed_number").get,
        doc.getAs[Beacon]("beacon").get
      )
  }

}

