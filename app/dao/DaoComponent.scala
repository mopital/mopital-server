package dao

import models.{Bed, Beacon, Patient}
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

  trait PatientDao {

    def get(id: String): Future[Option[Patient]]
    def add(patient: Patient): Future[Boolean]
    def getAll(): Future[List[Patient]]

  }

  trait BeaconDao {
    def add(beacon: Beacon): Future[Boolean]
    def getAll(): Future[List[Beacon]]
  }

  trait BedDao {

    def add(bed: Bed): Future[Boolean]
    def getAll(): Future[List[Bed]]
  }

}

trait DaoComponentImpl extends DaoComponent {

  val patientDao = new PatientDaoImpl
  val beaconDao = new BeaconDaoImpl
  val bedDao = new BedDaoImpl

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

    def getAll(): Future[List[Patient]] = {
        patientCollection.find(BSONDocument(), BSONDocument()).cursor[Patient].collect[List](Int.MaxValue, true)
    }
  }


  class BeaconDaoImpl extends BeaconDao {

    def beaconCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("beacon")

    def add(beacon: Beacon): Future[Boolean] = {
      beaconCollection.insert(beacon).map(lastError => !lastError.inError)
    }

    def getAll(): Future[List[Beacon]] = {
      beaconCollection.find(BSONDocument(), BSONDocument()).cursor[Beacon].collect[List](Int.MaxValue, true)
    }
  }


  class BedDaoImpl extends BedDao {

    def bedCollection: BSONCollection = dao.Mongo.db.collection[BSONCollection]("bed")

    def add(bed: Bed): Future[Boolean] = {
      bedCollection.insert(bed).map(lastError => !lastError.inError)
    }

    def getAll(): Future[List[Bed]] = {
      bedCollection.find(BSONDocument(), BSONDocument()).cursor[Bed].collect[List](Int.MaxValue, true)
    }
  }

}
