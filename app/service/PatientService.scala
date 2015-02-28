package service

import dao.DaoComponent
import models.{Treatment, Patient}
import play.api.Logger
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 11/02/15.
 */
trait PatientServiceComponent {

  val patientService: PatientService

  trait PatientService {

    def add(bedNumber: Int, name: String, age: Int, weight: Double, height:Double, bloodType: Option[String], fileNo: Option[String], admissionDate: Option[String]): Future[Boolean]
    def get(id: String): Future[Option[Patient]]
    def getAll(): Future[List[Patient]]
    def getPatientByBeaconNumber(beaconNumber: String): Future[Option[Patient]]
  }
}

trait PatientServiceComponentImpl extends PatientServiceComponent {

  this: DaoComponent =>

  val patientService: PatientService = new PatientServiceImpl

  class PatientServiceImpl extends PatientService {

    def add(bedNumber: Int, name: String, age: Int, weight: Double, height:Double, bloodType: Option[String], fileNo: Option[String], admissionDate: Option[String]): Future[Boolean] = {
      patientDao.add(new Patient(Some(BSONObjectID.generate), bedNumber, name, age, weight, height, bloodType, fileNo, admissionDate, List()))
    }

    def getAll(): Future[List[Patient]] = {
      patientDao.getAll()
    }

    def get(id: String): Future[Option[Patient]] = {
      patientDao.get(id)
    }

    def getPatientByBeaconNumber(beaconNumber: String): Future[Option[Patient]] = {
      bedDao.getByBeaconNumber(beaconNumber).flatMap {
        case Some(bed) =>
          //check result and generate appropriate response
          patientDao.getPatientByBedNumber(bed.bed_number).map(result => result)
        case _ =>
          Logger.debug("bed_number11")
          null
      }
    }
  }
}
