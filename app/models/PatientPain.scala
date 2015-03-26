package models

import play.api.libs.json.{JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Created by ahmetkucuk on 22/03/15.
 */
case class PatientPain(region: String, typeOfPain: String, duration: String) {

  def toJson(): JsValue = {
    JsObject(Seq("region" -> Json.toJson(region),
      "typeOfPain" -> Json.toJson(typeOfPain),
      "duration" -> Json.toJson(duration)
    ))
  }
}

object PatientPain {

  implicit val patientPainFormat = Json.format[PatientPain]

  implicit object PatientPainPainBSONWriter extends BSONDocumentWriter[PatientPain] {
    def write(patientPain: PatientPain): BSONDocument =
      BSONDocument(
        "region" -> patientPain.region,
        "type_of_pain" -> patientPain.typeOfPain,
        "duration" -> patientPain.duration)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object PatientPainBSONReader extends BSONDocumentReader[PatientPain] {
    def read(doc: BSONDocument): PatientPain =
      PatientPain(
        doc.getAs[String]("region").get,
        doc.getAs[String]("type_of_pain").get,
        doc.getAs[String]("duration").get
      )
  }
}