package dao

import models.Patient

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 05/02/15.
 */
trait DaoComponent {

  val patientDao: PatientDao

  trait PatientDao {

    def get(id: String): Future[Option[Patient]]
    def add(patient: Patient): Future[Boolean]

  }

  trait DaoComponentImpl extends DaoComponent {

    val patientDao = new PatientDaoImpl

    class PatientDaoImpl extends PatientDao with MongoOps{

      def get(id: String): Future[Option[Patient]] = {
        Future(Some(new Patient))

//        bookCollection.find(byId(bookId), BSONDocument()).cursor[Book].headOption
      }

      def add(patient: Patient): Future[Boolean] = {

//        val result = bookCollection.insert(book)
//        result.map(lastError => !lastError.inError)

        Future.successful(true)
      }

    }
  }
}
