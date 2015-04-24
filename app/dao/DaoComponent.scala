package dao

import java.awt.print.Book

import models._
import play.api.Logger
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 05/02/15.
 */
trait DaoComponent {

  val patientDao: PatientDao
  val beaconDao: BeaconDao
  val bedDao: BedDao
  val equipmentDao: EquipmentDao
  val gcmDao: GCMDao

  trait PatientDao {

    def get(id: String): Future[Option[Patient]]
    def add(patient: Patient): Future[Boolean]
    def getAll(): Future[List[Patient]]
    def getPatientByBedNumber(bedNumber: Int): Future[Option[Patient]]
    def insertTreatment(id: String, treatment: Treatment): Future[Boolean]
    def insertBloodSugarMonitoring(id: String, bloodSugarMonitoring: BloodSugarMonitoring): Future[Boolean]
    def insertPeriodicMonitoring(id: String, periodicMonitoring: PeriodicMonitoring): Future[Boolean]

  }

  trait BeaconDao {
    def add(beacon: Beacon): Future[Boolean]
    def getAll(): Future[List[Beacon]]
    def get(id: String): Future[Option[Beacon]]
  }

  trait BedDao {

    def add(bed: Bed): Future[Boolean]
    def getAll(): Future[List[Bed]]
    def updateBed(id:String, bed:Bed): Future[Boolean]
    def updateBedBeacon(id:String, beacon:Beacon): Future[Boolean]
    def getByBeaconNumber(beaconNumber: String):Future[Option[Bed]]
  }

  trait EquipmentDao {
    def add(equipment: Equipment): Future[Boolean]
    def getAll(): Future[List[Equipment]]
    def get(id: String): Future[Option[Equipment]]
    def setBeacon(id: String, beacon: Beacon): Future[Boolean]
  }

  trait GCMDao {

    def add(gcmHolder: GCMHolder): Future[Boolean]

    def getGCMIdByUserId(userId: String): Future[Option[GCMHolder]]

  }
}

trait DaoComponentImpl extends DaoComponent {

  val patientDao = new PatientDaoImpl
  val beaconDao = new BeaconDaoImpl
  val bedDao = new BedDaoImpl
  val equipmentDao = new EquipmentDaoImpl
  val gcmDao = new GCMDaoImpl

  class PatientDaoImpl extends PatientDao with MongoOps{

    def patientCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("patient")

    def get(id: String): Future[Option[Patient]] = {
      patientCollection.find(byId(id), BSONDocument()).cursor[Patient].headOption
    }

    def add(patient: Patient): Future[Boolean] = {
      Logger.debug("add-patient")
      val result = patientCollection.insert(patient)
      result.map(lastError => !lastError.inError)

    }

    def getAll(): Future[List[Patient]] = {
        patientCollection.find(BSONDocument(), BSONDocument()).cursor[Patient].collect[List](Int.MaxValue, true)
    }


    def getPatientByBedNumber(bedNumber: Int): Future[Option[Patient]] = {
      patientCollection.find(BSONDocument("bed_number" -> bedNumber), BSONDocument()).cursor[Patient].headOption
    }


    def insertTreatment(id: String, treatment: Treatment): Future[Boolean] = {
      val pushQuery = BSONDocument("$push" -> BSONDocument("treatments" -> treatment))

      patientCollection.update(byId(id), pushQuery).map(lastError => !lastError.inError)

    }

    def insertBloodSugarMonitoring(id: String, bloodSugarMonitoring: BloodSugarMonitoring): Future[Boolean] = {

      val pushQuery = BSONDocument("$push" -> BSONDocument("nurse_records.blood_sugar_monitoring_records" -> bloodSugarMonitoring))

      patientCollection.update(byId(id), pushQuery).map(lastError => !lastError.inError)
    }

    def insertPeriodicMonitoring(id: String, periodicMonitoring: PeriodicMonitoring): Future[Boolean] = {

      val pushQuery = BSONDocument("$push" -> BSONDocument("nurse_records.periodic_monitoring_records" -> periodicMonitoring))

      patientCollection.update(byId(id), pushQuery).map(lastError => !lastError.inError)
    }

  }

  class BeaconDaoImpl extends BeaconDao with MongoOps{

    def beaconCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("beacon")

    def add(beacon: Beacon): Future[Boolean] = {
      beaconCollection.insert(beacon).map(lastError => !lastError.inError)
    }

    def getAll(): Future[List[Beacon]] = {
      beaconCollection.find(BSONDocument(), BSONDocument()).cursor[Beacon].collect[List](Int.MaxValue, true)
    }
    def get(id: String): Future[Option[Beacon]] = {
      beaconCollection.find(byId(id), BSONDocument()).cursor[Beacon].headOption
    }
  }


  class BedDaoImpl extends BedDao with MongoOps{

    def bedCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("bed")

    def add(bed: Bed): Future[Boolean] = {
      bedCollection.insert(bed).map(lastError => !lastError.inError)
    }

    def getAll(): Future[List[Bed]] = {
      bedCollection.find(BSONDocument(), BSONDocument()).cursor[Bed].collect[List](Int.MaxValue, true)
    }

    def updateBed(id:String, bed:Bed): Future[Boolean] = {
      bedCollection.update(byId(id), bed).map(lastError => !lastError.inError)
    }

    def updateBedBeacon(id:String, beacon:Beacon): Future[Boolean] = {
      bedCollection.update(byId(id), BSONDocument("$set" -> BSONDocument("beacon" -> beacon))).map(lastError => !lastError.inError)
    }

    def getByBeaconNumber(beaconNumber: String):Future[Option[Bed]] = {
      val query = BSONDocument( "beacon.uuid" -> beaconNumber)
      bedCollection.find(query, BSONDocument()).cursor[Bed].headOption
    }
  }

  class EquipmentDaoImpl extends EquipmentDao with MongoOps{

    def equipmentCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("equipment")

    def add(equipment: Equipment): Future[Boolean] = {
      equipmentCollection.insert(equipment).map(lastError => !lastError.inError)
    }

    def getAll(): Future[List[Equipment]] = {
      equipmentCollection.find(BSONDocument(), BSONDocument()).cursor[Equipment].collect[List](Int.MaxValue, true)
    }
    def get(id: String): Future[Option[Equipment]] = {
      equipmentCollection.find(byId(id), BSONDocument()).cursor[Equipment].headOption
    }

    def setBeacon(id:String, beacon:Beacon): Future[Boolean] = {
      equipmentCollection.update(byId(id), BSONDocument("$set" -> BSONDocument("beacon" -> beacon))).map(lastError => !lastError.inError)
    }
  }

  class GCMDaoImpl extends GCMDao with MongoOps {

    def gcmCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("gcm")

    def add(gcmHolder: GCMHolder): Future[Boolean] = {
      gcmCollection.insert(gcmHolder).map(lastError => !lastError.inError)
    }

    def getGCMIdByUserId(userId: String): Future[Option[GCMHolder]] = {
      gcmCollection.find(BSONDocument("user_id" -> userId)).cursor[GCMHolder].headOption
    }
  }

}
