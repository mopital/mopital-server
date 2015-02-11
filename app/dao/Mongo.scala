package dao

import play.api.Logger
import reactivemongo.api.MongoDriver
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONRegex}

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * Created by ahmetkucuk on 05/02/15.
 */

object Mongo {

  val driver = new MongoDriver
  val connection = driver.connection( List( "localhost" ) )
  val db = connection("mopital")
  //  db.collection[BSONCollection]("sessions").indexesManager.ensuring(true, "asd")

}
trait MongoOps {

  def callSafelyAndGet(func: () => Any, errorMsg: => String): (Boolean, Any) = {
    try {
      (true, func())
    } catch {
      case e: Exception =>
        Logger.error(errorMsg, e)
        (false, e)
    }
  }

  def regex(keyword: String): BSONRegex = {
    BSONRegex(keyword + ".*", "i")
  }

  def byId(id: String): BSONDocument = {
    BSONDocument("_id" -> BSONObjectID.apply(id))
  }

  def byId(id: BSONObjectID): BSONDocument = {
    BSONDocument("_id" -> id)
  }
}
