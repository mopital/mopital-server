package third.webcore.models

import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

/**
 * Created by ahmetkucuk on 8/6/14.
 */
case class SessionStatus(val email: String, val sessionId: String, val validUntil: Long) {

  def isValid = System.currentTimeMillis() <= validUntil

}

object SessionStatus {

//  implicit val sessionFormat = Json.format[SessionStatus]

  /** serialize a User into a BSON */
  implicit object SessionStatusBSONWriter extends BSONDocumentWriter[SessionStatus] {
    def write(session: SessionStatus): BSONDocument =
      BSONDocument(
        "email" -> session.email,
        "sessionId" -> session.sessionId,
        "validUntil" -> session.validUntil)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object SessionStatusBSONReader extends BSONDocumentReader[SessionStatus] {
    def read(doc: BSONDocument): SessionStatus =
      SessionStatus(
        doc.getAs[String]("email").get,
        doc.getAs[String]("sessionId").get,
        doc.getAs[Long]("validUntil").get)//convert to Int
  }
}

