package controllers

import java.io.File

import akka.routing.Router
import play.Play
import play.api._
import play.api.libs.json.{Json, JsArray}
import play.api.mvc._
import third.webcore.models.ResponseBase
import utils.ApiGenerator

object Application extends Controller {

  def index(any: String) = Action {
    Ok(views.html.index()).withHeaders(("Access-Control-Allow-Origin","*"), ("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT"))
  }

  def api() = Action {
    Ok(views.html.main())
  }

  def apiData() = Action {
    Ok(ApiGenerator.getApiJson())
  }

  def user(id: String) = Action {
    Ok(views.html.patient())
  }

  def options(url: String) = Action {
    Ok("").withHeaders(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Content-Type, X-Requested-With, Accept, *",
      // cache access control response for one day
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString
    )
  }

  def error(any: String) = Action {
    Ok(ResponseBase.error("authentication").toJson)
  }

  /** resolve "any" into the corresponding HTML page URI */
  def getURI(any: String): String = any match {
    case "patient-list" => "/public/html/patient-list.html"
    case _ => "error"
  }

  /** load an HTML page from public/html */
  def loadPublicHTML(any: String) = Action {
    val projectRoot = Play.application().path()
    var file = new File(projectRoot + getURI(any))
    if (file.exists())
      Ok(scala.io.Source.fromFile(file.getCanonicalPath()).mkString).as("text/html");
    else
      NotFound
  }

}