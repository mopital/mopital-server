package controllers

import dao._
import models.{AddTreatmentRequest, AddPatientRequest, Treatment, Patient}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import service.{PatientServiceComponent, PatientServiceComponentImpl}
import third.webcore.models.ResponseBase

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 04/02/15.
 */
trait PatientController extends Controller with DaoComponent with PatientServiceComponent{

  def addPatient() = Action.async(parse.json) { request =>
    request.body.validate[AddPatientRequest].fold(
      valid = { addPatientRequest =>

        patientService.add(addPatientRequest).map { result =>
          getResponseFromResult(result)
        }

      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(Ok(ResponseBase.error("invalid json fields.").toResultJson))
      }

    )
  }

  def get(id: String) = Action.async {
    patientService.get(id).map {
      case Some(patient) =>
        Ok(ResponsePatient(ResponseBase.success(), patient).toJson)
      case _ => Ok(ResponseBase.error().toJson)
    }
  }

  def allPatients() = Action.async {
      patientService.getAll().map(patients => Ok(ResponseListPatient(ResponseBase.success(), patients).toJson))
  }


  def getPatientByBeaconUUID(uuid: String) = Action.async {

    patientService.getPatientByBeaconUUID(uuid).map {
      case Some(patient) =>
        Ok(ResponsePatient(ResponseBase.success(), patient).toJson)
      case _ => Ok(ResponseBase.error().toJson)
    }
  }

  def addTreatment() = Action.async(parse.json) { request =>

      request.body.validate[AddTreatmentRequest].fold(
        valid = { addTreatmentRequest =>

          patientService.addTreatment(addTreatmentRequest).map { result =>
            getResponseFromResult(result)
          }
        },
        invalid = { e => Logger.error(s"Add Patient Controller] $e");
          Future.successful(Ok(ResponseBase.error("invalid json fields.").toResultJson))
        }

      )
  }

  def getResponseFromResult(result: Boolean): Result = {
    if(result) {
      Ok(ResponseBase.success().toResultJson)
    } else {
      Ok(ResponseBase.error().toResultJson)
    }
  }

}

object PatientController extends PatientController
                         with DaoComponentImpl
                         with PatientServiceComponentImpl