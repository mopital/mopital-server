package utils

import play.api.mvc._
import third.webcore.models.ResponseBase

/**
 * Created by ahmetkucuk on 26/03/15.
 */
trait ControllerHelperFunctions {

  self:Controller =>

  def getResponseFromResult(result: Boolean): Result = {
    if(result) {
      Ok(ResponseBase.success().toResultJson)
    } else {
      Ok(ResponseBase.error().toResultJson)
    }
  }

}
