package third.webcore.services

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import third.webcore.dao.WebCoreDaoComponent
import third.webcore.models.{SessionStatus, User}
import utils.WebCoreConstants

import scala.collection.JavaConversions._
import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 8/6/14.
 */
trait SessionServiceComponent {

  val sessionService: SessionService

  trait SessionService {


    def createSession(user: User): Future[String]
    def checkValidityAndUpdateIfValid(userId: String, sessionId: String): Future[Boolean]
//    def isSessionValid(sessionId: String): Boolean

  }
}

trait SessionServiceComponentImpl extends SessionServiceComponent {

  this: WebCoreDaoComponent =>


  val sessionService: SessionService = new MongoSessionServiceImpl

  class MongoSessionServiceImpl extends SessionService {


    def createSession(user: User): Future[String] = {

      sessionDao.getSessionByEmail(user.email).map{
        case Some(session) if session.isValid =>

          val updatedSession = session.copy(validUntil = System.currentTimeMillis() + WebCoreConstants.SESSION_PERIOD_IN_MILLIS)
          sessionDao.updateSessionByEmail(user.email, updatedSession).map{ updateResult =>

            if(!updateResult) {
              Logger.error("[SessionServiceComponent-createSession] error while updating session")
            }
          }
          updatedSession.sessionId
        case Some(session) =>

          val updatedSession = session.copy(sessionId = UUID.randomUUID().toString, validUntil = System.currentTimeMillis() + WebCoreConstants.SESSION_PERIOD_IN_MILLIS)
          sessionDao.updateSessionByEmail(user.email, updatedSession).map{ updateResult =>

            if(!updateResult) {
              Logger.error("[SessionServiceComponent-createSession] error while updating and changing sessionId")
            }
          }
          Logger.error("[SessionServiceComponent-createSession] session already exist")
          updatedSession.sessionId
        case _ =>
          val session = new SessionStatus(user.email, UUID.randomUUID().toString(), System.currentTimeMillis() + WebCoreConstants.SESSION_PERIOD_IN_MILLIS) // ten minutes
          sessionDao.createNewSession(session)
          session.sessionId
      }
    }

    def checkValidityAndUpdateIfValid(email: String, sessionId: String): Future[Boolean] = {

      sessionDao.getSession(email, sessionId).map {
        case Some(session) if session.isValid =>
          val newSession = new SessionStatus(session.email, session.sessionId, System.currentTimeMillis() + WebCoreConstants.SESSION_PERIOD_IN_MILLIS)
          sessionDao.updateSessionByEmail(email, newSession).map{ updateResult =>

            if(!updateResult) {
              Logger.error("[SessionServiceComponent-checkValidityAndUpdateIfValid] error while updating session")
            }
          }
          Logger.info("[SessionServiceComponent-checkValidityAndUpdateIfValid] session is validated")
          true
        case Some(session) =>
          val currentTime: Long = System.currentTimeMillis()
          val sessionTime: Long = session.validUntil
          Logger.info(s"[SessionServiceComponent-checkValidityAndUpdateIfValid] currentTime: $currentTime sessionTime: $sessionTime")
          sessionDao.removeSessionByEmail(email)
          false
        case _ =>
          Logger.info("[SessionServiceComponent-checkValidityAndUpdateIfValid] session couldn't validated")
          false
      }
    }
  }

  class SessionServiceImpl extends SessionService {


    private val sessions: collection.concurrent.Map[String, SessionStatus] = new ConcurrentHashMap[String, SessionStatus]()


    def createSession(user: User) = {

        validSessionIdOf(user.email) match {
          case Some(sessionId) =>
            Logger.info(s"[already-created-session-returned] UserId: ${user.email} with session id: $sessionId")
            Future.successful(sessionId)
          case None =>
            val session = new SessionStatus(user.email, UUID.randomUUID().toString(), System.currentTimeMillis() + WebCoreConstants.SESSION_PERIOD_IN_MILLIS) // ten minutes
            Logger.info(s"[new-session-created] UserId: ${user.email} ${session.sessionId}")
            sessions.put(user.email, session)
            Future.successful(session.sessionId)
        }
    }

    def checkValidityAndUpdateIfValid(userId: String, sessionId: String): Future[Boolean] = {

      validSessionOf(userId) match {
        case Some(session) =>
          Logger.info(s"[session-validated] UserId: $userId SessionId: $sessionId")
          sessions(userId) = SessionStatus(session.email, sessionId, System.currentTimeMillis() + WebCoreConstants.SESSION_PERIOD_IN_MILLIS)
          Future.successful(true)
        case _ =>
          Future.successful(false)
      }
    }

    def isSessionValid(sessionId: String) = {

      val iterator = sessions.entrySet().iterator()
      while(iterator.hasNext) {
        val mapEntry = iterator.next()
        Logger.debug("Sessions: " + mapEntry.getValue.sessionId)
        if(mapEntry.getValue.sessionId.equalsIgnoreCase(sessionId) && mapEntry.getValue.isValid) {
          true
        }
      }
      false
    }

    def validSessionIdOf(userId: String): Option[String] = {

      validSessionOf(userId) match {
        case Some(SessionStatus(_, sessionId, _)) =>
          Some(sessionId)
        case _ =>
          None
      }
    }

    def validSessionOf(userId: String): Option[SessionStatus] = {
      sessions.get(userId) match {
        case Some(sessionStatus) if sessionStatus.isValid =>
          Some(sessionStatus)
        case _ =>
          None
      }
    }

    def userOf(userId: String) = {

      validSessionOf(userId) match {
        case Some(SessionStatus(email, _, _)) =>
          Some(email)
        case _ =>
          None
      }
    }
  }
}
