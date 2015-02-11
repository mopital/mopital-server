package models

import play.api.libs.json.Json
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Created by ahmetkucuk on 05/02/15.
 */
case class Patient(id: Option[BSONObjectID], name: String) {

}

object Patient {

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
