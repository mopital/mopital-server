package controllers

import dao.{ResponseListBeacon, DaoComponentImpl, DaoComponent}
import models.{UpdateBeaconRequest, AddBeaconRequest}
import play.api.Logger
import play.api.mvc.{Result, Action, Controller}
import service.{BeaconServiceComponentImpl, BeaconServiceComponent}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import third.webcore.models.ResponseBase
import utils.ControllerHelperFunctions

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BeaconController extends Controller with DaoComponent with BeaconServiceComponent with ControllerHelperFunctions{


  def add() = Action.async(parse.json) { request =>

    request.body.validate[AddBeaconRequest].fold (

      valid = { addBeaconRequest =>
        beaconService.add(addBeaconRequest).map ( result => getResponseFromResult(result))
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def update() = Action.async(parse.json) { request =>

    request.body.validate[UpdateBeaconRequest].fold (

      valid = { updateBeaconRequest =>
        beaconService.update(updateBeaconRequest).map ( result => getResponseFromResult(result))
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def addGET(beaconUUID: String, major: Int, minor: Int, beaconType: String, position: String) = Action.async {
    val addBeaconRequest = new AddBeaconRequest(beaconUUID, major, minor, beaconType, position)
    beaconService.add(addBeaconRequest).map ( result => getResponseFromResult(result))
  }

  def updateGET(id: String, beaconUUID: String, major: Int, minor: Int, beaconType: String, position: String) = Action.async {
    val updateBeaconRequest = new UpdateBeaconRequest(id, beaconUUID, major, minor, beaconType, position)
    beaconService.update(updateBeaconRequest).map ( result => getResponseFromResult(result))
  }

  def getAll() = Action.async {
    beaconService.getAll().map(beacons => AllowRemoteResult(Ok(ResponseListBeacon(ResponseBase.success(), beacons).toJson)))
  }


}

object BeaconController extends BeaconController
                        with DaoComponentImpl
                        with BeaconServiceComponentImpl
