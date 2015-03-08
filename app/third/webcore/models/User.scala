package third.webcore.models

import play.api.libs.json._
import reactivemongo.bson.Producer.nameValue2Producer
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter, BSONStringHandler}

/**
 * Created by ahmetkucuk on 04/08/14.
 */


case class User(name: String, department: String, email: String, password: String, role: String) {

  def toJson: JsValue = {

    JsObject(Seq("name" -> Json.toJson(name),
      "department" -> Json.toJson(department),
      "email" -> Json.toJson(email),
      "password" -> Json.toJson(password),
      "role" -> Json.toJson(role)
    ))
  }

  override def toString: String = {

    "name: " + name.toString + " Department: " + department  + " Email: " + email
  }
}

object User {

  /** serialize a User into a BSON */
  implicit object UserBSONWriter extends BSONDocumentWriter[User] {
    def write(user: User): BSONDocument =
      BSONDocument(
        "name" -> user.name,
        "department" -> user.department,
        "email" -> user.email,
        "password" -> user.password,
        "role" -> user.role)
  }

  /** deserialize a Celebrity from a BSON */
  implicit object UserBSONReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User =
      User(
        doc.getAs[String]("name").get,
        doc.getAs[String]("department").get,
        doc.getAs[String]("email").get,
        doc.getAs[String]("password").get,
        doc.getAs[String]("role").get)
  }

}
