import java.util.concurrent.TimeUnit

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import play.api.test.FakeApplication
import play.api.test.Helpers._
import service.PatientServiceComponentImpl

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * Created by ahmetkucuk on 18/02/15.
 */
class PatientTest extends Specification {

  class PatientServiceComponentTest extends PatientServiceComponentImpl with MockDaoComponent

  val patientServiceComponentTest = new PatientServiceComponentTest

  "PatientServiceComponent #getUsers" should {
    "return user" in {

      val fake = FakeApplication()

      running(fake) {
        val result = Await.result(patientServiceComponentTest.patientService.get("53f45227ac843639ae552cd4"), Duration(2, TimeUnit.SECONDS))
        val expected = "ahmet"

        result.head.name must equalTo(expected)
      }
    }
  }

}
