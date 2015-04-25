package controllers

import dao._
import models.{SetBeaconToBedRequest, AddBedRequest, AddBeaconRequest, AddPeriodicMonitoringRequest}
import play.api.Logger
import play.api.mvc.{Result, Action, Controller}
import service.{BedServiceComponentImpl, BedServiceComponent, BeaconServiceComponentImpl, BeaconServiceComponent}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import third.webcore.models.ResponseBase
import utils.ControllerHelperFunctions

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */

trait BedController extends Controller with DaoComponent with BedServiceComponent with ControllerHelperFunctions{

  def add() = Action.async(parse.json) { request =>

    request.body.validate[AddBedRequest].fold(
      valid = { addBedRequest: AddBedRequest=>

        bedService.add(addBedRequest).map { result =>
          getResponseFromResult(result)
        }
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def setBeaconToBed() = Action.async(parse.json) { request =>

    request.body.validate[SetBeaconToBedRequest].fold(
      valid = { setBeaconToBedRequest: SetBeaconToBedRequest =>

        bedService.setBeaconToBed(setBeaconToBedRequest).map { result =>
          getResponseFromResult(result)
        }
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )

  }

  def getAll() = Action.async {
    bedService.getAll().map(beds => AllowRemoteResult(Ok(ResponseListBed(ResponseBase.success(), beds).toJson)))
  }

}

object BedController extends BedController
                      with DaoComponentImpl
                      with BedServiceComponentImpl
                      with BeaconServiceComponentImpl
