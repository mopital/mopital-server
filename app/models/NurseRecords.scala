package models

import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}
import play.modules.reactivemongo.json.BSONFormats._

/**
 * Created by ahmetkucuk on 22/03/15.
 */
case class NurseRecords(id: Option[BSONObjectID], recordedAt: Long, diagnoses: String, allergy: String, bloodType: String, nurse: String, patientPain: PatientPain, bloodSugarMonitoringRecords: List[BloodSugarMonitoring], periodicMonitoringRecords: List[PeriodicMonitoring]) {

  def this(addPatientRequest: AddPatientRequest) {
    this(Option(BSONObjectID.generate),
      System.currentTimeMillis(),
      addPatientRequest.diagnoses,
      addPatientRequest.allergy,
      addPatientRequest.bloodType,
      addPatientRequest.nurse,
      PatientPain(addPatientRequest.painRegion, addPatientRequest.painType, addPatientRequest.painDuration),
      List(),
      List()
    )
  }

  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "recordedAt" -> Json.toJson(recordedAt),
      "diagnoses" -> Json.toJson(diagnoses),
      "allergy" -> Json.toJson(allergy),
      "bloodType" -> Json.toJson(bloodType),
      "nurse" -> Json.toJson(nurse),
      "patientPain" -> Json.toJson(patientPain),
      "bloodSugarMonitoringRecords" -> JsArray(bloodSugarMonitoringRecords.map(p => p.toJson())),
      "periodicMonitoringRecords" -> JsArray(periodicMonitoringRecords.map(p => p.toJson()))
    ))
  }
}


object NurseRecords {

  implicit val nurseRecordsFormat = Json.format[NurseRecords]

  implicit object NurseRecordsPainBSONWriter extends BSONDocumentWriter[NurseRecords] {
    def write(nurseRecords: NurseRecords): BSONDocument =
      BSONDocument(
        "_id" -> nurseRecords.id.getOrElse(BSONObjectID.generate),
        "recordedAt" -> nurseRecords.recordedAt,
        "diagnoses" -> nurseRecords.diagnoses,
        "allergy" -> nurseRecords.allergy,
        "blood_type" -> nurseRecords.bloodType,
        "nurse" -> nurseRecords.nurse,
        "patient_pain" -> nurseRecords.patientPain,
        "blood_sugar_monitoring_records" -> nurseRecords.bloodSugarMonitoringRecords,
        "periodic_monitoring_records" -> nurseRecords.periodicMonitoringRecords
      )
  }

  /** deserialize a Celebrity from a BSON */
  implicit object NurseRecordsBSONReader extends BSONDocumentReader[NurseRecords] {
    def read(doc: BSONDocument): NurseRecords =
      NurseRecords(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Long]("recordedAt").get,
        doc.getAs[String]("diagnoses").get,
        doc.getAs[String]("allergy").get,
        doc.getAs[String]("blood_type").get,
        doc.getAs[String]("nurse").get,
        doc.getAs[PatientPain]("patient_pain").get,
        doc.getAs[List[BloodSugarMonitoring]]("blood_sugar_monitoring_records").get,
        doc.getAs[List[PeriodicMonitoring]]("periodic_monitoring_records").get
      )
  }
}


