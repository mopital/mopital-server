package service

import dao.DaoComponent
import models.Patient
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 11/02/15.
 */
trait PatientServiceComponent {

  val patientService: PatientService

  trait PatientService {

    def add(name: String): Future[Boolean]

  }
}

trait PatientServiceComponentImpl extends PatientServiceComponent {

  this: DaoComponent =>

  val patientService: PatientService = new PatientServiceImpl

  class PatientServiceImpl extends PatientService {

    def add(name: String): Future[Boolean] = {
      patientDao.add(new Patient(Some(BSONObjectID.generate), name))
    }

  }

}
