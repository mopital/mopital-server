package controllers

import dao.{ResponseListBeacon, ResponseBase, DaoComponentImpl, DaoComponent}
import models.AddBeaconRequest
import play.api.Logger
import play.api.mvc.{Result, Action, Controller}
import service.{BeaconServiceComponentImpl, BeaconServiceComponent}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BeaconController extends Controller with DaoComponent with BeaconServiceComponent{


  def add() = Action.async(parse.json) { request =>

    request.body.validate[AddBeaconRequest].fold(

      valid = { addBeaconRequest =>
        beaconService.add(addBeaconRequest).map( result => getResponseFromResult(result))
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(Ok(ResponseBase.error("invalid json fields.").toResultJson))
      }

    )
  }

  def getAll() = Action.async {
    beaconService.getAll().map(beacons => Ok(ResponseListBeacon(ResponseBase.success(), beacons).toJson))
  }

  def getResponseFromResult(result: Boolean): Result = {
    if(result) {
      Ok(ResponseBase.success().toResultJson)
    } else {
      Ok(ResponseBase.error().toResultJson)
    }
  }

}

object BeaconController extends BeaconController
                        with DaoComponentImpl
                        with BeaconServiceComponentImpl
