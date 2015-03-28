package service

import dao.DaoComponent
import models._
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

    def add(addPatientRequest: AddPatientRequest): Future[Boolean]
    def get(id: String): Future[Option[Patient]]
    def getAll(): Future[List[Patient]]
    def getPatientByBeaconUUID(uuid: String): Future[Option[Patient]]
    def addTreatment(addTreatmentRequest: AddTreatmentRequest): Future[Boolean]
    def addBloodSugarMonitoring(addBloodSugarMonitoringRequest: AddBloodSugarMonitoringRequest): Future[Boolean]
    def addPeriodicMonitoring(addPeriodicMonitoringRequest: AddPeriodicMonitoringRequest): Future[Boolean]
  }
}

trait PatientServiceComponentImpl extends PatientServiceComponent {

  this: DaoComponent =>

  val patientService: PatientService = new PatientServiceImpl

  class PatientServiceImpl extends PatientService {

    def add(addPatientRequest: AddPatientRequest): Future[Boolean] = {
      patientDao.add(new Patient(addPatientRequest))
    }

    def getAll(): Future[List[Patient]] = {
      patientDao.getAll()
    }

    def get(id: String): Future[Option[Patient]] = {
      patientDao.get(id)
    }

    def getPatientByBeaconUUID(uuid: String): Future[Option[Patient]] = {
      bedDao.getByBeaconNumber(uuid).flatMap {
        case Some(bed) =>
          //check result and generate appropriate response
          patientDao.getPatientByBedNumber(bed.bed_number).map(result => result)
        case _ =>
          Logger.debug("bed_number11")
          null
      }
    }


    def addTreatment(addTreatmentRequest: AddTreatmentRequest): Future[Boolean] = {
      patientDao.insertTreatment(addTreatmentRequest.patientId, new Treatment(addTreatmentRequest))
    }

    def addBloodSugarMonitoring(addBloodSugarMonitoringRequest: AddBloodSugarMonitoringRequest): Future[Boolean] = {
      patientDao.insertBloodSugarMonitoring(addBloodSugarMonitoringRequest.patientId, new BloodSugarMonitoring(addBloodSugarMonitoringRequest))
    }

    def addPeriodicMonitoring(addPeriodicMonitoringRequest: AddPeriodicMonitoringRequest): Future[Boolean] = {
      patientDao.insertPeriodicMonitoring(addPeriodicMonitoringRequest.patientId, new PeriodicMonitoring(addPeriodicMonitoringRequest))
    }
  }
}
