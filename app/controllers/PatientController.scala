package controllers

import dao._
import models.{Treatment, Patient}
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc._
import reactivemongo.bson.BSONObjectID
import service.{PatientServiceComponent, PatientServiceComponentImpl}

import scala.concurrent.Future

/**
 * Created by ahmetkucuk on 04/02/15.
 */
trait PatientController extends Controller with DaoComponent with PatientServiceComponent{

  def addPatient() = Action.async(parse.json) { request =>

    val bedNumber = request.body.\("bed_number").as[Int]
    val name = request.body.\("name").as[String]
    val age = request.body.\("age").as[Int]
    val weight = request.body.\("weight").as[Double]
    val height = request.body.\("height").as[Double]
    val fileNo = request.body.\("file_no").asOpt[String]
    val bloodType = request.body.\("blood_type").asOpt[String]
    val admissionDate = request.body.\("admission_date").asOpt[String]

    patientService.add(bedNumber, name, age, weight, height, bloodType, fileNo, admissionDate).map(result =>

      result match {
        case true => Ok(ResponseBase.success().toJson)
        case _ => Ok(ResponseBase.error().toJson)
      }
    )
  }

  def allPatients() = Action.async {
      patientService.getAll().map(patients => Ok(ResponseListPatient(ResponseBase.success(), patients).toJson))
  }


  def getPatientByBeaconNumber(uuid: String) = Action.async {
//    val patient = new Patient(Option(BSONObjectID.generate), 12, name = "alpercem", age = 12, weight = 71.2, height = 1.79, bloodType = Option("artı"), fileNo = Option("1232"), admissionDate = Option("dsfjd"), treatments = List())

    patientService.getPatientByBeaconUUID(uuid).map( result =>

        result match  {

          case Some(patient) =>
            Ok(ResponsePatient(ResponseBase.success(), patient).toJson)
          case _ => Ok(ResponseBase.error().toJson)
        }
    )
  }

}

object PatientController extends PatientController
                         with DaoComponentImpl
                         with PatientServiceComponentImpl