package models

import play.api.libs.json.{Json, JsObject, JsArray, JsValue}

/**
 * Created by ahmetkucuk on 28/03/15.
 */
case class BeaconPosition(position: String) extends BaseModel{

  def toJson(): JsValue = {
    JsObject(Seq("position" -> Json.toJson(position)))
  }

}
