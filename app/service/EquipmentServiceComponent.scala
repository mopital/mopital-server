package service

import dao.DaoComponent
import models._
import play.api.Logger
import play.api.libs.json.{Json, JsObject, JsArray}

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.util.{Failure, Success}

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
//
//      val f1 = equipmentDao.get(id)
//      val f2 = {equipment: Equipment => beaconLogDao.findLastLog(equipment.beacon)}
//      val f3 = {beaconLog: BeaconLog => beaconLogDao.nearestLog(beaconLog.recordedAt)}
//
//      f1.onSuccess({
//          case Some(equipment) =>
//            f2{equipment}.onSuccess( {
//              case Some(beaconLog) =>
//                f3(beaconLog).onSuccess({
//                  case Some(bLog) =>
//                    new BeaconPosition(bLog.beacon.position)
//                  case _ =>
//                    Future.successful(new BeaconPosition("Unknown"))
//
//                })
//              case _ =>
//                Future.successful(new BeaconPosition("Unknown"))
//            }
//            )
//          case _ =>
//            Future.successful(new BeaconPosition("Unknown"))
//        }
//      )

//      Await.result(f1, Duration.I
// nf)

//      Await.result(f2, Duration.Inf)
//      val f3 = f2.flatMap({
//        case Some(beaconLog) => beaconLogDao.nearestLog(beaconLog.recordedAt)
//      })
//      Await.result(f3, Duration.Inf)
//      val f4 = f3.map({
//        case Some(bLog) =>
//          new BeaconPosition(bLog.beacon.position)
//        case _ =>
//          new BeaconPosition("NoooooNNN")
//      })
//      Await.result(f4, Duration.Inf)

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
