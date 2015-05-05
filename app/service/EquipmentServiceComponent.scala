package service

import dao.DaoComponent
import models._
import play.api.Logger
import play.api.libs.json.{Json, JsObject, JsArray}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

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
      equipmentDao.get(id).flatMap(
        {
          case Some(equipment) =>
            Logger.debug("equipment" + equipment.toJson().toString())
            Await.ready(beaconLogDao.findLastLog(equipment.beacon), Duration.Inf).value.get.get match {
              case Some(beaconLog) =>
                Logger.debug("beaconLog" + beaconLog.toJson().toString())
                Await.ready(beaconLogDao.nearestLog(beaconLog.recordedAt), Duration.Inf).value.get.get match {
                  case Some(bLog) =>
                    Logger.debug("bLog" + bLog.toJson().toString())
                    new BeaconPosition(bLog.beacon.position)
                  case _ =>
                    Logger.debug("bLogbos")
                    Future.successful(new BeaconPosition("Unknown"))
                }
              case _ =>
                Future.successful(new BeaconPosition("Unknown"))
            }
            Future.successful(new BeaconPosition("Unknown"))
          case _ =>
            Future.successful(new BeaconPosition("Unknown"))
        }
      )
    }
  }
}
