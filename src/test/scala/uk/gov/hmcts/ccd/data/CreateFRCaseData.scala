package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.core.feeder.SourceFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object CreateFRCaseData extends PerformanceTestsConfig {


  private val rng: Random = new Random()
  private def solicitorName(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def solicitorFirm(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def solicitorReference(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def solicitorAddress(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def solicitorPhone(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def solicitorEmail(): String = rng.alphanumeric.take(10).mkString
  private def divorceCaseNumber(): Int = 10000 + rng.nextInt(99999 - 10000) + 1
  private def divorceDecreeAbsoluteDate(): Int = 10 + rng.nextInt(99 - 10) + 1
  private def applicantFMName(): String = rng.alphanumeric.take(10).mkString
  private def applicantLName(): String = rng.alphanumeric.take(10).mkString
  private def appRespondentFMName(): String = rng.alphanumeric.take(10).mkString
  private def appRespondentLName(): String = rng.alphanumeric.take(10).mkString
  private def respondentAddress(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def respondentPhone(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def respondentEmail(): String = rng.alphanumeric.take(10).mkString
  private def natureOfApplication3a(): String = rng.alphanumeric.take(10).mkString
  private def natureOfApplication3b(): String = rng.alphanumeric.take(10).mkString
  private def authorisationName(): String = rng.alphanumeric.take(10).mkString
  private def authorisationFirm(): String = rng.alphanumeric.take(10).mkString
  private def authorisation3(): Int = 10 + rng.nextInt(99 - 10) + 1
  private def PBANumber(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def PBAreference(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: SourceFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: SourceFeederBuilder[String] = csv("listoffiles.csv").random

  val randcaseType = new Random(System.currentTimeMillis())

  val caseEventTypeValue = Array("FR_solicitorCreate")

  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)

  val EventId = caseEventTypeValue(caseTypeValue_random_index)

  def PickCaseType(): String = EventId


  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createFRCaseAppURL"))

  println("create case url: " + CreateCaseUrl)

  val caseTypeValue = Array("FinancialRemedyMVP2")
  val jurisdictionsValue = Array("DIVORCE")

  val rand = new Random(System.currentTimeMillis())
  val caseType_random_index = rand.nextInt(caseTypeValue.length)

  val jurisdictions_random_index = rand.nextInt(jurisdictionsValue.length)

  if(!CreateCaseUrl.contains(":casetype_reference"))
    {
      CreateCaseUrl
    }
  else{
    CreateCaseUrl = CreateCaseUrl.replace(":casetype_reference", caseTypeValue(caseType_random_index))
  }

  println("create case url: " + CreateCaseUrl)

  if(!CreateCaseUrl.contains(":jurisdictions_reference"))
  {
    CreateCaseUrl
  }
  else{
    CreateCaseUrl = CreateCaseUrl.replace(":jurisdictions_reference", jurisdictionsValue(jurisdictions_random_index))
  }

  println("create case url: " + CreateCaseUrl)
  val CreateCaseTokenUrl = s"${CreateCaseUrl.replaceAll("cases", "")}event-triggers/$EventId/token"


  println("create case url: " + CreateCaseUrl)
  println("create case token url: " + CreateCaseTokenUrl)


  val EventBodyMain =StringBody("""{"data": {  "solicitorName": "Solicitor Name ${SolicitorName}",  "solicitorFirm": "Solicitor Firm ${SolicitorFirm}",  "solicitorReference": "SOLICITOR REF:${SolicitorReference}",  "solicitorAddress": {    "AddressLine1": "${SolicitorAddress}",    "AddressLine2": "Villa Road",    "AddressLine3": "Stanway",    "PostTown": "Colchester",    "County": "",    "PostCode": "CO3 0RN",    "Country": "United Kingdom"  },  "solicitorPhone": "78${SolicitorPhone}",  "solicitorEmail": "John.Smith.${SolicitorEmail}@gmail.com",  "solicitorDXnumber": null,  "solicitorAgreeToReceiveEmails": "No",  "divorceCaseNumber": "LV18D${DivorceCaseNumber}",  "divorceStageReached": "Decree Absolute",  "divorceDecreeAbsoluteDate": "19${DivorceDecreeAbsoluteDate}-12-12",  "divorceUploadEvidence2": {    "document_url": "http://${DMURL}/${Document_ID}",    "document_binary_url": "http://${DMURL}/${Document_ID}/binary",    "document_filename": "${filename}"  },  "applicantFMName": "John ${ApplicantFMName}",  "applicantLName": "Smith ${ApplicantLName}",  "appRespondentFMName": "Steve  ${AppRespondentFMName}",  "appRespondentLName": "Kahoe  ${AppRespondentLName}",  "appRespondentRep": "No",  "respondentAddress": {    "AddressLine1": "${RespondentAddress} Villa Road",    "AddressLine2": "",    "AddressLine3": "Stanway",    "PostTown": "Colchester",    "County": "",    "Country": "United Kingdom",    "PostCode": "CO3 0RN"  },  "respondentPhone": "71${RespondentPhone}",  "respondentEmail": "Steve.Kahoe.${RespondentEmail}@confirmation.com",  "natureOfApplication2": [    "Property Adjustment  Order",    "A settlement or a transfer of property",    "Pension Compensation Attachment Order",    "Pension Compensation Sharing Order",    "Periodical Payment Order",    "Lump Sum Order",    "Pension Sharing Order",    "Pension Attachment Order"  ],  "natureOfApplication3a": "102 PF, London ${NatureOfApplication3a}",  "natureOfApplication3b": "Mortgage Detail ${NatureOfApplication3b}",  "orderForChildrenQuestion1": "No",  "authorisationName": "Solicitor Fransis ${AuthorisationName}",  "authorisationFirm": "Fransis & Son ${AuthorisationFirm} Ltd",  "authorisation2b": "Advisor",  "authorisation3": "19${Authorisation3}-01-01",  "consentOrder": {    "document_url": "http://${DMURL}/${Document_ID}",    "document_binary_url": "http://${DMURL}/${Document_ID}/binary",    "document_filename": "${filename}"  },  "consentOrderText": {    "document_url": "http://${DMURL}/${Document_ID}",    "document_binary_url": "http://${DMURL}/${Document_ID}/binary",    "document_filename": "${filename}"  },  "d81Question": "Yes",  "d81Joint": {    "document_url": "http://${DMURL}/${Document_ID}",    "document_binary_url": "http://${DMURL}/${Document_ID}/binary",    "document_filename": "${filename}"  },  "pensionCollection": [    {      "id": null,      "value": {        "typeOfDocument": "Form P1",        "uploadedDocument": {          "document_url": "http://${DMURL}/${Document_ID}",          "document_binary_url": "http://${DMURL}/${Document_ID}/binary",          "document_filename": "${filename}"        }      }    }  ],  "otherCollection": [    {      "id": null,      "value": {        "typeOfDocument": "Letter",        "uploadedDocument": {          "document_url": "http://${DMURL}/${Document_ID}",          "document_binary_url": "http://${DMURL}/${Document_ID}/binary",          "document_filename": "${filename}"        }      }    }  ],  "helpWithFeesQuestion": "No",  "PBANumber": "PBA${PBANUMBER}",  "PBAreference": "PAYREF:${PBAReference}"},"event": {  "id": "FR_solicitorCreate",  "summary": "Performance Test Financial Case Summary - ${CaseSummaryText}",  "description": "Performance Test Financial Case Description - ${CaseDescriptionText}"},"event_token": """" + "${eventToken}" + """","ignore_warning": false,"draft_id": null}""")


  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateWebUserToken()


  val   CreateFRCaseDataSCN = scenario("Create Financial remedy Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("SolicitorName", solicitorName()),
        ("SolicitorFirm", solicitorFirm()),
        ("SolicitorReference", solicitorReference()),
        ("SolicitorAddress", solicitorAddress()),
        ("SolicitorPhone",solicitorPhone()),
        ("SolicitorEmail",solicitorEmail()),
        ("DivorceCaseNumber",divorceCaseNumber()),
        ("DivorceDecreeAbsoluteDate",divorceDecreeAbsoluteDate()),
        ("ApplicantFMName",applicantFMName()),
        ("ApplicantLName",applicantLName()),
        ("AppRespondentFMName",appRespondentFMName()),
        ("AppRespondentLName",appRespondentLName()),
        ("RespondentAddress",respondentAddress()),
        ("RespondentPhone",respondentPhone()),
        ("RespondentEmail",respondentEmail()),
        ("NatureOfApplication3a",natureOfApplication3a()),
        ("NatureOfApplication3b",natureOfApplication3b()),
        ("AuthorisationName",authorisationName()),
        ("AuthorisationFirm",authorisationFirm()),
        ("Authorisation3",authorisation3()),
        ("PBANUMBER",PBANumber()),
        ("PBAReference",PBAreference()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText())
      )
    ) .feed(fileProviderRand)
      .exec(
      http("TX01_CCD_CreateCaseEndpoint_FR_createcase_eventtoken")
        .get(CreateCaseTokenUrl)
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
          .header("Accept","application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-start-case-trigger.v2+json;charset=UTF-8")
        .header("Content-Type","application/json")
        .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
    ).exec(http("TX20_DocStore_PostDocument_${filename}")
      .post(DocStoreBashURL +"/documents")
      .header("Authorization", userToken)
      .header("ServiceAuthorization", token)
      .header("user-id", "giri.benadikar@hmcts.net")
      .bodyPart(
        RawFileBodyPart("files", "${filename}")
          .contentType("application/pdf")
          .fileName("${filename}")).asMultipartForm
      .formParam("classification", "PUBLIC")
      .check(status is 200, jsonPath("$._embedded.documents[0]._links.binary.href").saveAs("fileId"), regex("""http://(.+)/""").saveAs("DMURL"),regex("""/documents/(.+)"""").saveAs("Document_ID")))
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
      .exec(
      http("TX02_CCD_CreateCaseEndpoint_FR_createcasedata")
        .post(CreateCaseUrl)
        .body(EventBodyMain).asJson
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }


  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

