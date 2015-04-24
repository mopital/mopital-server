package models

import play.api.libs.json.{Json, JsValue, JsObject}
import reactivemongo.bson.{BSONDocumentReader, BSONObjectID, BSONDocument, BSONDocumentWriter}

/**
 * Created by ahmetkucuk on 24/04/15.
 */
case class GCMHolder(userId: String, gcmId: String) {

  def this(addGCMRequest: AddGCMRequest) = {
    this(addGCMRequest.userId,
      addGCMRequest.gcmId
    )
  }


  def toJson(): JsValue = {
    JsObject(Seq("userId" -> Json.toJson(userId),
      "gcmId" -> Json.toJson(gcmId)
    ))
  }

}


object GCMHolder {

  implicit val gcmFormat = Json.format[GCMHolder]

  implicit object BedBSONWriter extends BSONDocumentWriter[GCMHolder] {
    def write(gCMHolder: GCMHolder): BSONDocument =
      BSONDocument(
        "user_id" -> gCMHolder.userId,
        "gcm_id" -> gCMHolder.gcmId)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object BedBSONReader extends BSONDocumentReader[GCMHolder] {
    def read(doc: BSONDocument): GCMHolder =
      GCMHolder(
        doc.getAs[String]("user_id").get,
        doc.getAs[String]("gcm_id").get
      )
  }

}
