package controllers

import dao._
import play.api.mvc.{Action, Controller}
import service.{BedServiceComponentImpl, BedServiceComponent, BeaconServiceComponentImpl, BeaconServiceComponent}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

/**
 * Created by ahmetkucuk on 18/02/15.
 */

trait BedController extends Controller with DaoComponent with BedServiceComponent{

  def add() = Action.async(parse.json) { request =>

    val bed_no = request.body.\("bed_no").as[Int]

    bedService.add(bed_no).map( result =>
      result match {
        case true => Ok(ResponseBase.success().toJson)
        case _ => Ok(ResponseBase.error().toJson)
      }
    )
  }

  def setBeaconToBed() = Action.async(parse.json) { request =>

    val bedId = request.body.\("bed_id").as[String]
    val beaconId = request.body.\("beacon_id").as[String]

    bedService.updateBeacon(bedId, beaconId).map { result =>
      result match {
        case true => Ok(ResponseBase.success().toJson)
        case _ => Ok(ResponseBase.error().toJson)
      }
    }

  }

  def getAll() = Action.async {
    bedService.getAll().map(beds => Ok(ResponseListBed(ResponseBase.success(), beds).toJson))
  }

}

object BedController extends BedController
                      with DaoComponentImpl
                      with BedServiceComponentImpl
                      with BeaconServiceComponentImpl
