package service

import dao.DaoComponent
import models.{Beacon, Bed}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BedServiceComponent {

  val bedService: BedService

  trait BedService {
    def add(number: Int): Future[Boolean]
    def getAll(): Future[List[Bed]]
    def updateBeacon(bedId: String, beaconId: String): Future[Boolean]
  }

}

trait BedServiceComponentImpl extends BedServiceComponent {

  this: DaoComponent with BeaconServiceComponent =>

  val bedService: BedService = new BedServiceImpl

  class BedServiceImpl extends BedService {

    def add(number: Int): Future[Boolean] = {

      bedDao.add(new Bed(Some(BSONObjectID.generate), number, new Beacon()))

    }
    def getAll(): Future[List[Bed]] = {
      bedDao.getAll()
    }

    def updateBeacon(bedId: String, beaconId: String): Future[Boolean] = {
      beaconService.get(beaconId).flatMap {
        case Some(beacon) =>
          //check result and generate appropriate response
          bedDao.updateBedBeacon(bedId, beacon).map(result => result)
        case _ =>
          Future.successful(false);
      }
    }
  }

}
