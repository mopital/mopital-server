package models

import play.api.libs.json.{Json, JsObject, JsValue}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}

import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 28/02/15.
 */
case class Treatment(id: Option[BSONObjectID], date: Option[String], time: Option[String], tension: Option[String], temperature: Option[String], pulse: Option[String], respiration: Option[String], pain: Option[String], definition: Option[String]) {


  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "date" -> Json.toJson(date),
      "tension" -> Json.toJson(tension),
      "temperature" -> Json.toJson(temperature),
      "pulse" -> Json.toJson(pulse),
      "respiration" -> Json.toJson(respiration),
      "pain" -> Json.toJson(pain),
      "definition" -> Json.toJson(definition)
    ))
  }
}

object Treatment {

  implicit val treatmentFormat = Json.format[Treatment]

  implicit object TreatmentBSONWriter extends BSONDocumentWriter[Treatment] {
    def write(treatment: Treatment): BSONDocument =
      BSONDocument(
        "_id" -> treatment.id.getOrElse(BSONObjectID.generate),
        "date" -> treatment.date,
        "tension" -> treatment.tension,
        "temperature" -> treatment.temperature,
        "pulse" -> treatment.pulse,
        "respiration" -> treatment.respiration,
        "pain" -> treatment.pain,
        "definition" -> treatment.definition)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object TreatmentBSONReader extends BSONDocumentReader[Treatment] {
    def read(doc: BSONDocument): Treatment =
      Treatment(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[String]("date"),
        doc.getAs[String]("time"),
        doc.getAs[String]("tension"),
        doc.getAs[String]("temperature"),
        doc.getAs[String]("pulse"),
        doc.getAs[String]("respiration"),
        doc.getAs[String]("pain"),
        doc.getAs[String]("definition")
      )
  }
}
