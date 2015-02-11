package dao

import models.Patient
import play.api.Logger
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 05/02/15.
 */
trait DaoComponent {

  val patientDao: PatientDao

  trait PatientDao {

    def get(id: String): Future[Option[Patient]]
    def add(patient: Patient): Future[Boolean]

  }
}

trait DaoComponentImpl extends DaoComponent {

  val patientDao = new PatientDaoImpl

  class PatientDaoImpl extends PatientDao with MongoOps{

    def patientCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("patient")

    def get(id: String): Future[Option[Patient]] = {
      Logger.debug("get-patient")
      Future(Some(new Patient(Some(BSONObjectID.generate), "ahmet")))

      //        bookCollection.find(byId(bookId), BSONDocument()).cursor[Book].headOption
    }

    def add(patient: Patient): Future[Boolean] = {
      Logger.debug("add-patient")
      val result = patientCollection.insert(patient)
      result.map(lastError => !lastError.inError)

    }

  }
}
