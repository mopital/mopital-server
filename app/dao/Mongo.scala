package dao

import play.api.Logger
import reactivemongo.api.{MongoConnection, MongoDriver}
import reactivemongo.bson.{BSONDocument, BSONObjectID, BSONRegex}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Try}

/**
  * Created by ahmetkucuk on 05/02/15.
 */

object Mongo {

  val driver = new MongoDriver

//  val connection = driver.connection( List( "localhost" ) )
//  val db = connection("mopital")

  val connection = driver.connection( List( "localhost" ) )
  val db = connection("mopital")


// HEROKU Databse Connection
//  val uri = "mongodb://mopital:mopital@ds043991.mongolab.com:43991/heroku_app34029761"
//
//  val connection: Try[MongoConnection] =
//    MongoConnection.parseURI(uri).map { parsedUri =>
//      driver.connection(parsedUri)
//    }
//  val db = connection.get.db("heroku_app34029761")


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
