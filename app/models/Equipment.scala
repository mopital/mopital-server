package models

import play.api.libs.json.{JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Created by ahmetkucuk on 28/03/15.
 */
case class Equipment(id: Option[BSONObjectID], recordId: String, name: String, typeOfEquipment: String, beacon: Beacon ) extends BaseModel{

  def this(addEquipmentRequest: AddEquipmentRequest) = {
    this(Option(BSONObjectID.generate),
      addEquipmentRequest.recordId,
      addEquipmentRequest.name,
      addEquipmentRequest.typeOfEquipment,
      new Beacon()
    )
  }


  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "recordId" -> Json.toJson(recordId),
      "name" -> Json.toJson(name),
      "typeOfEquipment" -> Json.toJson(typeOfEquipment),
      "beacon" -> Json.toJson(beacon)
    ))
  }
}

object Equipment {

  implicit val equipmentFormat = Json.format[Equipment]

  implicit object EquipmentBSONWriter extends BSONDocumentWriter[Equipment] {
    def write(equipment: Equipment): BSONDocument =
      BSONDocument(
        "_id" -> equipment.id.getOrElse(BSONObjectID.generate),
        "recordId" -> equipment.recordId,
        "name" -> equipment.name,
        "type_of_equipment" -> equipment.typeOfEquipment,
        "beacon" -> equipment.beacon)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object EquipmentBSONReader extends BSONDocumentReader[Equipment] {
    def read(doc: BSONDocument): Equipment =
      Equipment(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("recordId").get,
        doc.getAs[String]("name").get,
        doc.getAs[String]("type_of_equipment").get,
        doc.getAs[Beacon]("beacon").get
      )
  }

}


