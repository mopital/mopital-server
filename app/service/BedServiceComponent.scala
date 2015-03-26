package service

import dao.DaoComponent
import models.{SetBeaconToBedRequest, AddBedRequest, Beacon, Bed}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BedServiceComponent {

  val bedService: BedService

  trait BedService {
    def add(addBedRequest: AddBedRequest): Future[Boolean]
    def getAll(): Future[List[Bed]]
    def setBeaconToBed(setBeaconToBedRequest: SetBeaconToBedRequest): Future[Boolean]
  }

}

trait BedServiceComponentImpl extends BedServiceComponent {

  this: DaoComponent with BeaconServiceComponent =>

  val bedService: BedService = new BedServiceImpl

  class BedServiceImpl extends BedService {

    def add(addBedRequest: AddBedRequest): Future[Boolean] = {

      bedDao.add(new Bed(addBedRequest))

    }
    def getAll(): Future[List[Bed]] = {
      bedDao.getAll()
    }

    def setBeaconToBed(setBeaconToBedRequest: SetBeaconToBedRequest): Future[Boolean] = {
      beaconService.get(setBeaconToBedRequest.beaconId).flatMap {
        case Some(beacon) =>
          //check result and generate appropriate response
          bedDao.updateBedBeacon(setBeaconToBedRequest.bedId, beacon).map(result => result)
        case _ =>
          Future.successful(false);
      }
    }
  }

}
