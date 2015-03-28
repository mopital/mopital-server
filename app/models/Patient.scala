package models

import play.api.libs.json.{JsArray, JsObject, JsValue, Json}
import reactivemongo.bson.{BSONDocumentReader, BSONDocument, BSONDocumentWriter, BSONObjectID}

import play.modules.reactivemongo.json.BSONFormats._
/**
 * Created by ahmetkucuk on 05/02/15.
 */
case class Patient(id: Option[BSONObjectID], bedNumber: Int, name: String, age: Int, weight: Double, height:Double, bloodType: String, fileNo: String, admissionDate: String, treatments: List[Treatment], nurseRecords: NurseRecords) extends BaseModel{

  def this(addPatientRequest: AddPatientRequest) {
    this(Option(BSONObjectID.generate),
      addPatientRequest.bedNumber,
      addPatientRequest.name,
      addPatientRequest.age,
      addPatientRequest.weight,
      addPatientRequest.height,
      addPatientRequest.bloodType,
      addPatientRequest.fileNo,
      addPatientRequest.admissionDate,
      List(),
      new NurseRecords(addPatientRequest)
    )
  }

  def toJson(): JsValue = {
    JsObject(Seq("id" -> Json.toJson(id.get.stringify),
      "bedNumber" -> Json.toJson(bedNumber),
      "name" -> Json.toJson(name),
      "age" -> Json.toJson(age),
      "weight" -> Json.toJson(weight),
      "height" -> Json.toJson(height),
      "bloodType" -> Json.toJson(bloodType),
      "fileNo" -> Json.toJson(fileNo),
      "admissionDate" -> Json.toJson(admissionDate),
      "treatments" -> JsArray(treatments.map(p => p.toJson())),
      "nurseRecords" -> nurseRecords.toJson()
    ))
  }

}

object Patient {

  implicit val patientFormat = Json.format[Patient]

  implicit object PatientBSONWriter extends BSONDocumentWriter[Patient] {
    def write(patient: Patient): BSONDocument =
      BSONDocument(
        "_id" -> patient.id.getOrElse(BSONObjectID.generate),
        "bed_number" -> patient.bedNumber,
        "name" -> patient.name,
        "age" -> patient.age,
        "weight" -> patient.weight,
        "height" -> patient.height,
        "blood_type" -> patient.bloodType,
        "file_no" -> patient.fileNo,
        "admission_date" -> patient.admissionDate,
        "treatments" -> patient.treatments,
        "nurse_records" -> patient.nurseRecords
      )
  }

  /** deserialize a Celebrity from a BSON */
  implicit object PatientBSONReader extends BSONDocumentReader[Patient] {
    def read(doc: BSONDocument): Patient =
      Patient(
        doc.getAs[BSONObjectID]("_id"),
        doc.getAs[Int]("bed_number").get,
        doc.getAs[String]("name").get,
        doc.getAs[Int]("age").get,
        doc.getAs[Double]("weight").get,
        doc.getAs[Double]("height").get,
        doc.getAs[String]("blood_type").get,
        doc.getAs[String]("file_no").get,
        doc.getAs[String]("admission_date").get,
        doc.getAs[List[Treatment]]("treatments").get,
        doc.getAs[NurseRecords]("nurse_records").get
      )
  }

}
