package controllers

import play.api.mvc._

/**
 * Created by ahmetkucuk on 04/02/15.
 */
object PatientController extends Controller {

  def getUserInfoByBeaconId(id: String) = Action {
      Ok("ok")
  }

}
