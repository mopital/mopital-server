package service

import dao.DaoComponent
import models.{Beacon, Bed}
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BedServiceComponent {

  val bedService: BedService

  trait BedService {
    def add(number: Int): Future[Boolean]
    def getAll(): Future[List[Bed]]
  }

}

trait BedServiceComponentImpl extends BedServiceComponent {

  this: DaoComponent =>

  val bedService: BedService = new BedServiceImpl

  class BedServiceImpl extends BedService {


    def add(number: Int): Future[Boolean] = {

      bedDao.add(new Bed(Some(BSONObjectID.generate), number, new Beacon()))

    }
    def getAll(): Future[List[Bed]] = {
      bedDao.getAll()
    }
  }

}
