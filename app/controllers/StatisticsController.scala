package controllers

import dao.{ResponseStatistic, ResponseListBeacon, DaoComponentImpl, DaoComponent}
import play.api.mvc.{Action, Controller}
import service._
import third.webcore.models.ResponseBase
import utils.ControllerHelperFunctions

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 03/05/15.
 */
trait StatisticsController extends Controller with DaoComponent with StatisticServiceComponent with ControllerHelperFunctions {

  def get() = Action.async {
    statisticService.get().map(statistics => AllowRemoteResult(Ok(ResponseStatistic(ResponseBase.success(), statistics).toJson)))
  }

}

object StatisticsController extends StatisticsController
with DaoComponentImpl
with StatisticServiceComponentImpl