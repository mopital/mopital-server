package service

import dao.DaoComponent
import models._
import reactivemongo.bson.BSONObjectID

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BeaconServiceComponent {

  val beaconService: BeaconService

  trait BeaconService {

    def add(addBeaconRequest: AddBeaconRequest): Future[Boolean]
    def update(updateBeacon: UpdateBeaconRequest): Future[Boolean]
    def addBeaconLog(addBeaconLogRequest: AddBeaconLogRequest): Future[Boolean]
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

    def addBeaconLog(addBeaconLogRequest: AddBeaconLogRequest): Future[Boolean] = {
      beaconDao.getByMinor(addBeaconLogRequest.minor).flatMap ( {
        case Some(beacon) =>
          beaconLogDao.add(new BeaconLog(addBeaconLogRequest, beacon))
        case _ =>
          Future.successful(false)
      }
      )
    }
    def getAll(): Future[List[Beacon]] = {
      beaconDao.getAll()
    }

    def get(id: String): Future[Option[Beacon]] = {
      beaconDao.get(id)
    }

  }

}