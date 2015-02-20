package service

import dao.DaoComponent
import models.Beacon
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BeaconServiceComponent {

  val beaconService: BeaconService

  trait BeaconService {

    def add(number:String, major: Int, minor: Int): Future[Boolean]
    def getAll(): Future[List[Beacon]]
  }

}

trait BeaconServiceComponentImpl extends BeaconServiceComponent {

  this: DaoComponent =>

  val beaconService: BeaconService = new BeaconServiceImpl

  class BeaconServiceImpl extends BeaconService {

    def add(number:String, major: Int, minor: Int): Future[Boolean] = {
      val beacon = new Beacon(Some(BSONObjectID.generate), number, major, minor)
      beaconDao.add(beacon)
    }
    def getAll(): Future[List[Beacon]] = {
      beaconDao.getAll()
    }

  }

}