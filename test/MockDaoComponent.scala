import dao.{DaoComponentImpl, DaoComponent}
import models.Patient
import org.specs2.mock.Mockito
import reactivemongo.bson.BSONObjectID

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 18/02/15.
 */
trait MockDaoComponent extends DaoComponent with Mockito{


  val em = mock[DaoComponentImpl]


  val patientDao: PatientDao = mock[PatientDao]
  val bedDao: BedDao = mock[BedDao]
  val beaconDao: BeaconDao = mock[BeaconDao]

  val patient1 = new Patient(Option(BSONObjectID.apply("53f45227ac843639ae552cd4")), "ahmet")

  patientDao.get("53f45227ac843639ae552cd4") returns Future.successful(Option(patient1))

  def expect(f: (DaoComponentImpl) => Any) {
    f(em)
  }

}
