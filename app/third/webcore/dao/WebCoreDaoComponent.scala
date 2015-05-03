package third.webcore.dao

import dao.{MongoOps}
import play.api.Logger
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONObjectID, BSONArray, BSONDocument}
import reactivemongo.core.commands.Count
import third.webcore.models.{SessionStatus, User}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 07/03/15.
 */

trait WebCoreDaoComponent {

  val userDao: UserDao
  val sessionDao: SessionDao

  trait UserDao {

    def getUserByEmail(email: String): Future[Option[User]]
    def getUsers(): Future[List[User]]
    def count(): Future[Int]
    def getUserByEmailAndPasswordHash(username: String, passwordHash: String): Future[Option[User]]
    def register(user: User): Future[Boolean]
    def updateUserByEmail(email: String, user: User): Future[Boolean]
    def delete(email: String): Future[Boolean]
  }

  trait SessionDao {
    def createNewSession(session:SessionStatus): Future[Boolean]
    def getSession(email:String, sessionId: String): Future[Option[SessionStatus]]
    def getSessionByEmail(email:String): Future[Option[SessionStatus]]
    def updateSessionByEmail(email:String, session:SessionStatus): Future[Boolean]
    def removeSessionByEmail(email:String): Future[Boolean]
  }
}

trait WebCoreDaoComponentImpl extends WebCoreDaoComponent {

  val userDao: UserDao = new UserDaoImpl
  val sessionDao: SessionDao = new SessionDaoImpl

  class UserDaoImpl extends UserDao with MongoOps {

    def userCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("users")

    def updateUserByEmail(email: String, user: User) = {

      Logger.debug(s"[DaoComponent-updateUserByEmail]: Executing with email: $email")
      val future = userCollection.update(BSONDocument("email" -> email), user)
      future.map(lastError => !lastError.inError)
    }

    def getUsers() = {

      Logger.debug("[UserDaoImpl]: executing getUsers")
      userCollection.find(BSONDocument(), BSONDocument()).cursor[User].toList()
    }

    def count(): Future[Int] = {
      userCollection.db.command(Count(userCollection.name))
    }

    def register(user: User) = {

      val future = userCollection.insert(user)
      future.map(lastError => !lastError.inError)
    }

    def getUserByEmail(email: String) = {

      val query = BSONDocument("email" -> email)
      val filter = BSONDocument()
      // Get a cursor of BSONDocuments
      userCollection.find(query, filter).cursor[User].headOption

    }

    override def getUserByEmailAndPasswordHash(username:String, passwordHash: String) = {

      val query = BSONDocument("email" -> username, "password" -> passwordHash)
      userCollection.find(query, BSONDocument()).cursor[User].headOption
    }

    def delete(email: String): Future[Boolean] = {

      val query = BSONDocument("email" -> email)
      userCollection.remove(query).map( lastError => !lastError.inError)
    }
  }

  class SessionDaoImpl extends SessionDao {

    def sessionCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("sessions")

    def createNewSession(session: SessionStatus): Future[Boolean] = {

      sessionCollection.insert(session).map( lastError => !lastError.inError)
    }

    def getSession(email:String, sessionId: String): Future[Option[SessionStatus]] = {

      val query = BSONDocument("email" -> email, "sessionId" -> sessionId)
      sessionCollection.find(query, BSONDocument()).cursor[SessionStatus].headOption
    }

    def getSessionByEmail(email:String): Future[Option[SessionStatus]] = {
      val query = BSONDocument("email" -> email)
      sessionCollection.find(query, BSONDocument()).cursor[SessionStatus].headOption
    }

    def updateSessionByEmail(email:String, session:SessionStatus): Future[Boolean] = {

      Logger.debug(s"[DaoComponent-updateSessionByEmail]: Executing with email: $email")

      val future = sessionCollection.update(BSONDocument("email" -> email), session)
      future.map( lastError => !lastError.inError)
    }

    def removeSessionByEmail(email:String): Future[Boolean] = {

      Logger.debug(s"[DaoComponent-removeSessionByEmail]: Executing with email: $email")
      sessionCollection.remove(BSONDocument("email" -> email)).map( lastError => !lastError.inError)
    }

  }
}
