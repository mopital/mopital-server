package service

import dao.DaoComponent
import models.{Beacon, UpdateBeaconRequest, AddBeaconRequest, MopitalStatistics}
import third.webcore.dao.WebCoreDaoComponent
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

  this: DaoComponent with WebCoreDaoComponent =>
  val statisticService: StatisticService = new StatisticServiceImpl

  class StatisticServiceImpl extends StatisticService {

    def get(): Future[MopitalStatistics] = {

      val numberOfBed = bedDao.count()
      val numberOfPatient = patientDao.count()
      val numberOfEquipment = equipmentDao.count()
      val numberOfBeacon = beaconDao.count()
      val numberOfDoctor = userDao.count()

      for {
        nOfBed <- numberOfBed
        nOfPatient <- numberOfPatient
        nOfEquipment <- numberOfEquipment
        nOfBeacon <- numberOfBeacon
        nOfDoctor <- numberOfDoctor
      } yield {
        new MopitalStatistics(nOfBed, 232, nOfBeacon, nOfDoctor, nOfEquipment, nOfPatient, 43)
      }
    }
  }

}
