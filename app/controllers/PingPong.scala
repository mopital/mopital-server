package controllers

import controllers.Application._
import play.api.mvc.Action
import play.mvc.Controller

/**
 * Created by ahmetkucuk on 02/03/15.
 */
object PingPong extends Controller{

  def ping() = Action {
    Ok("pong")
  }

}
