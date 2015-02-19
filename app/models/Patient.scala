package models

import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}

import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 05/02/15.
 */
case class Patient(id: Option[BSONObjectID], name: String) {

  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "name" -> Json.toJson(name)
    ))
  }

}

object Patient {

  implicit val patientFormat = Json.format[Patient]

  implicit object PatientBSONWriter extends BSONDocumentWriter[Patient] {
    def write(patient: Patient): BSONDocument =
      BSONDocument(
        "_id" -> patient.id.getOrElse(BSONObjectID.generate),
        "name" -> patient.name)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object PatientBSONReader extends BSONDocumentReader[Patient] {
    def read(doc: BSONDocument): Patient =
      Patient(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("name").get)
  }

}
