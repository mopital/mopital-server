package models

import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by ahmetkucuk on 03/05/15.
 */
case class MopitalStatistics(numberOfBeds: Int, totalTreatment: Int) {

  def toJson(): JsValue = {
    JsObject(Seq("numberOfBeds" -> Json.toJson(numberOfBeds),
    "totalTreatment" -> Json.toJson(totalTreatment)
    ))
  }
}

