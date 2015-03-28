package service

import dao.DaoComponent
import models._

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 28/03/15.
 */
trait EquipmentServiceComponent {

  val equipmentService: EquipmentService

  trait EquipmentService {

    def add(addEquipmentRequest: AddEquipmentRequest): Future[Boolean]
    def getAll(): Future[List[Equipment]]
    def get(id: String): Future[Option[Equipment]]
    def setBeacon(setBeaconToEquipment: SetBeaconToEquipment): Future[Boolean]
    def getLastPosition(id: String): Future[BeaconPosition]

  }
}

trait EquipmentServiceComponentImpl extends EquipmentServiceComponent {

  this: DaoComponent =>

  val equipmentService: EquipmentService = new EquipmentServiceImpl

  class EquipmentServiceImpl extends EquipmentService {

    def add(addEquipmentRequest: AddEquipmentRequest): Future[Boolean] = {
      equipmentDao.add(new Equipment(addEquipmentRequest))
    }

    def getAll(): Future[List[Equipment]] = {equipmentDao.getAll() }

    def get(id: String): Future[Option[Equipment]] = {equipmentDao.get(id) }

    def setBeacon(setBeaconToEquipment: SetBeaconToEquipment): Future[Boolean] = {
      beaconDao.get(setBeaconToEquipment.beaconId).flatMap( result =>

          result match {
            case Some(beacon) =>
              equipmentDao.setBeacon(setBeaconToEquipment.equipmentId, beacon)
            case _ =>
            Future.successful(false)
          }
      )
    }

    def getLastPosition(id: String): Future[BeaconPosition] = {
      Future.successful(new BeaconPosition("Enterence of Hospital"))
    }
  }
}
