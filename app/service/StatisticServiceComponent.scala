package service

import dao.DaoComponent
import models.{Beacon, UpdateBeaconRequest, AddBeaconRequest, MopitalStatistics}
import third.webcore.models.InternalResponse
import views.html.play20.book

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

  this: DaoComponent =>
  val statisticService: StatisticService = new StatisticServiceImpl

  class StatisticServiceImpl extends StatisticService {

    def get(): Future[MopitalStatistics] = {

      val numberOfBed = bedDao.count()

      for {
        nOfBed <- numberOfBed
      } yield {
        (nOfBed) match {
          case (nOfBed) =>
            new MopitalStatistics(nOfBed, 232, 23, 433, 43, 12, 43)
          case _=>
            new MopitalStatistics(23, 232, 23, 433, 43, 12, 43)
        }
      }
    }
  }

}
