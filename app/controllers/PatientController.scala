package controllers

import dao._
import models._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsObject, JsValue, Json}
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import service.{GCMServiceComponentImpl, GCMServiceComponent, PatientServiceComponent, PatientServiceComponentImpl}
import third.webcore.models.ResponseBase
import utils.ControllerHelperFunctions

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 04/02/15.
 */
trait PatientController extends Controller with DaoComponent with PatientServiceComponent with GCMServiceComponent with ControllerHelperFunctions{

  def addPatient() = Action.async(parse.json) { request =>
    request.body.validate[AddPatientRequest].fold(
      valid = { addPatientRequest =>

        patientService.add(addPatientRequest).map { result =>
          getResponseFromResult(result)
        }

      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def get(id: String) = Action.async {
    patientService.get(id).map {
      case Some(patient) =>
        AllowRemoteResult(Ok(ResponseBaseModel(ResponseBase.success(), patient).toJson))
      case _ => AllowRemoteResult(Ok(ResponseBase.error().toJson))
    }
  }


  def getPatientBeaconMap() = Action.async {
    patientService.getPatientBeaconMap().map(result => Ok(JsObject(Seq("result" -> ResponseBase.success().toJson, "data" -> JsObject(Seq("patientBeaconMapList" -> result))))))
  }

  def allPatients() = Action.async {
      patientService.getAll().map(patients => AllowRemoteResult(Ok(ResponseListPatient(ResponseBase.success(), patients).toJson)))
  }


  def getPatientByBeaconMinor(minor: Int) = Action.async {

    patientService.getPatientByBeaconMinor(minor).map {
      case Some(patient) =>
        AllowRemoteResult(Ok(ResponseBaseModel(ResponseBase.success(), patient).toJson))
      case _ => AllowRemoteResult(Ok(ResponseBase.error().toJson))
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
          Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
        }
      )
  }

  def addBloodSugarMonitoringData() = Action.async(parse.json) { request =>

    request.body.validate[AddBloodSugarMonitoringRequest].fold(
      valid = { addBloodSugarMonitoringRequest: AddBloodSugarMonitoringRequest =>

        patientService.addBloodSugarMonitoring(addBloodSugarMonitoringRequest).map { result =>
          getResponseFromResult(result)
        }
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def addPeriodicMonitoringData() = Action.async(parse.json) { request =>

    request.body.validate[AddPeriodicMonitoringRequest].fold(
      valid = { addPeriodicMonitoringRequest: AddPeriodicMonitoringRequest =>

        patientService.addPeriodicMonitoring(addPeriodicMonitoringRequest).map { result =>
          getResponseFromResult(result)
        }
      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }
    )
  }

  def addGCM() = Action.async(parse.json) { request =>
    request.body.validate[AddGCMRequest].fold(
      valid = { addGCMRequest: AddGCMRequest =>

        gcmService.add(addGCMRequest).map { result =>
          getResponseFromResult(result)
        }

      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }

    )
  }

  def notifyUser() = Action.async(parse.json) { request =>
    request.body.validate[NotifyUserRequest].fold(
      valid = { notifyUserRequest: NotifyUserRequest =>

        gcmService.notifyUser(notifyUserRequest).map { result =>
          getResponseFromResult(result)
        }

      },
      invalid = { e => Logger.error(s"Add Patient Controller] $e");
        Future.successful(AllowRemoteResult(Ok(ResponseBase.error("invalid json fields.").toResultJson)))
      }

    )
  }

  def notifyUserGet(userId: String, messageToSend: String) = Action.async {
    val notifyUserRequest: NotifyUserRequest = new NotifyUserRequest(userId, messageToSend)
    gcmService.notifyUser(notifyUserRequest).map { result =>
      getResponseFromResult(result)
    }
  }

}

object PatientController extends PatientController
                         with DaoComponentImpl
                         with GCMServiceComponentImpl
                         with PatientServiceComponentImpl