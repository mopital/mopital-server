package controllers

import dao._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc._
import service.{PatientServiceComponent, PatientServiceComponentImpl}

/**
 * Created by ahmetkucuk on 04/02/15.
 */
trait PatientController extends Controller with DaoComponent with PatientServiceComponent{

  def addPatient(name: String) = Action.async { request =>

      patientService.add(name).map(result =>

        result match {
          case true => Ok(ResponseBase.success().toJson)
          case _ => Ok(ResponseBase.error().toJson)
        }
      )
  }

  def allPatients() = Action.async {
      patientService.getAll().map(patients => Ok(ResponseListPatient(ResponseBase.success(), patients).toJson))
  }

}

object PatientController extends PatientController
                         with DaoComponentImpl
                         with PatientServiceComponentImpl