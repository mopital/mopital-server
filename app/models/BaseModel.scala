package models

import play.api.libs.json.JsValue

/**
 * Created by ahmetkucuk on 28/03/15.
 */
trait BaseModel {

  def toJson(): JsValue

}
