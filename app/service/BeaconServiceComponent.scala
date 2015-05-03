package service

import dao.DaoComponent
import models.{UpdateBeaconRequest, AddBeaconRequest, Beacon}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BeaconServiceComponent {

  val beaconService: BeaconService

  trait BeaconService {

    def add(addBeaconRequest: AddBeaconRequest): Future[Boolean]
    def update(updateBeacon: UpdateBeaconRequest): Future[Boolean]
    def getAll(): Future[List[Beacon]]
    def get(id: String): Future[Option[Beacon]]
  }

}

trait BeaconServiceComponentImpl extends BeaconServiceComponent {

  this: DaoComponent =>

  val beaconService: BeaconService = new BeaconServiceImpl

  class BeaconServiceImpl extends BeaconService {

    def add(addBeaconRequest: AddBeaconRequest): Future[Boolean] = {
      val beacon = new Beacon(addBeaconRequest)
      beaconDao.add(beacon)
    }

    def update(updateBeacon: UpdateBeaconRequest): Future[Boolean] = {
      val beacon = new Beacon(updateBeacon)
      beaconDao.update(beacon)
    }
    def getAll(): Future[List[Beacon]] = {
      beaconDao.getAll()
    }

    def get(id: String): Future[Option[Beacon]] = {
      beaconDao.get(id)
    }

  }

}