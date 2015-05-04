package controllers

import dao._
import models.{SetBeaconToEquipment, SetBeaconToBedRequest, AddEquipmentRequest, AddBedRequest}
import play.api
import play.api.{data, Logger}
import play.api.libs.json.{JsArray, JsObject}
import play.api.mvc.Action
import play.api.mvc.BodyParsers.parse
import play.api.mvc.Controller
import service.{EquipmentServiceComponentImpl, EquipmentServiceComponent, BedServiceComponent}
import third.webcore.models.ResponseBase
import utils.{Constants, ControllerHelperFunctions}

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 28/03/15.
 */
trait EquipmentController extends Controller with DaoComponent with EquipmentServiceComponent with ControllerHelperFunctions{


  def add() = Action.async(parse.json) { request =>

    request.body.validate[AddEquipmentRequest].fold(
      valid = { addEquipmentRequest: AddEquipmentRequest =>

        equipmentService.add(addEquipmentRequest).map { result =>
          getResponseFromResult(result)
        }
      },
      invalid = { e => Logger.error(s"Add Equipment Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def setBeaconToEquipment() = Action.async(parse.json) { request =>

    request.body.validate[SetBeaconToEquipment].fold(
      valid = { setBeaconToEquipment: SetBeaconToEquipment =>

        equipmentService.setBeacon(setBeaconToEquipment).map { result =>
          getResponseFromResult(result)
        }
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def getAll() = Action.async {
    equipmentService.getAll().map(equipments => AllowRemoteResult(Ok(ResponseListEquipment(ResponseBase.success(), equipments).toJson)))
  }

  def get(id: String) = Action.async {
    equipmentService.get(id).map {
      case Some(equipment) =>
        AllowRemoteResult(Ok(ResponseBaseModel(ResponseBase.success(), equipment).toJson))
      case _ => AllowRemoteResult(Ok(ResponseBase.error().toJson))
    }
  }

  def getEquipmentLastPosition(id: String) = Action.async {
    equipmentService.getLastPosition(id).map {
      result => AllowRemoteResult(Ok(ResponseBaseModel(ResponseBase.success(), result).toJson))
    }
  }
}

object EquipmentController extends EquipmentController with DaoComponentImpl with EquipmentServiceComponentImpl
