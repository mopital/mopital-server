package controllers

import dao.{ResponseListBeacon, ResponseBase, DaoComponentImpl, DaoComponent}
import play.api.mvc.{Action, Controller}
import service.{BeaconServiceComponentImpl, BeaconServiceComponent}

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait BeaconController extends Controller with DaoComponent with BeaconServiceComponent{


  def add() = Action.async(parse.json) { request =>

    val number: String = request.body.\("number").as[String]
    val major: Int = request.body.\("major").as[Int]
    val minor: Int = request.body.\("minor").as[Int]

    beaconService.add(number, major, minor).map(result =>

        result match {

          case true =>
            Ok(ResponseBase.success().toJson)
          case _=>
            Ok(ResponseBase.error().toJson)
        }
    )
  }

  def getAll() = Action.async {
    beaconService.getAll().map(beacons => Ok(ResponseListBeacon(ResponseBase.success(), beacons).toJson))
  }

}

object BeaconController extends BeaconController
                        with DaoComponentImpl
                        with BeaconServiceComponentImpl
