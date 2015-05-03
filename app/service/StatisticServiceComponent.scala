package service

import models.{Beacon, UpdateBeaconRequest, AddBeaconRequest, MopitalStatistics}

import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits.defaultContext

/**
 * Created by ahmetkucuk on 03/05/15.
 */
trait StatisticServiceComponent {

  val statisticService: StatisticService

  trait StatisticService {
    def get(): Future[MopitalStatistics]
  }
}

trait StatisticServiceComponentImpl extends StatisticServiceComponent {

  val statisticService: StatisticService = new StatisticServiceImpl

  class StatisticServiceImpl extends StatisticService {

    def get(): Future[MopitalStatistics] = {
      Future.successful(new MopitalStatistics(23, 232))
    }
  }

}
