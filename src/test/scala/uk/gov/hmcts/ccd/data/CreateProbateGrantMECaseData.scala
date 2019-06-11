package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random
import io.gatling.core.feeder.RecordSeqFeederBuilder

object CreateProbateGrantMECaseData extends PerformanceTestsConfig {


  private val rng: Random = new Random()
  private def applicationFeePaperForm(): Int = rng.nextInt(9999999)
  private def feeForCopiesPaperForm(): Int = rng.nextInt(99999)
  private def totalFeePaperForm(): Int = rng.nextInt(99999999)
  private def paymentReferenceNumberPaperform(): Int = rng.nextInt(99999999)
  private def primaryApplicantForenamesTXT(): String = rng.alphanumeric.take(10).mkString
  private def primaryApplicantForenames(): Int = rng.nextInt(99999999)
  private def primaryApplicantSurnameTXT(): String = rng.alphanumeric.take(10).mkString
  private def primaryApplicantSurname(): Int = rng.nextInt(99999999)

  private def primaryApplicantPhoneNumber(): Int = rng.nextInt(999999)
  private def primaryApplicantSecondPhoneNumber(): Int = rng.nextInt(999999)
  private def primaryApplicantEmailAddress(): Int = rng.nextInt(999999)
  private def primaryApplicantAddress(): Int = rng.nextInt(9999)
  private def deceasedForenames(): Int = rng.nextInt(999999)
  private def deceasedSurname(): Int = rng.nextInt(999999)
  private def deceasedAddress(): Int = rng.nextInt(9999)
  private def ukEstateValue(): Int = rng.nextInt(99999999)
  private def ihtGrossValue(): Int = rng.nextInt(99999999)


  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString


  val randcaseType = new Random(System.currentTimeMillis())
  val caseEventTypeValue = Array("applyForGrant")
  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)
  val EventId = caseEventTypeValue(caseTypeValue_random_index)
  def PickCaseType(): String = EventId

  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createCaseProbateGrantApplicationUrl"))

  println("create case url: " + CreateCaseUrl)

  val caseTypeValue = Array("GrantOfRepresentation")

  val jurisdictionsValue = Array("PROBATE")

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

  val CreateProbateGrantMECaseDataEventBodyMain =StringBody("""{"event": {"id": "applyForGrant","description": "Description ${CaseDescriptionText}","summary": "Probate application ${CaseSummaryText}"},"ignore_warning": true,"event_token": """"+ "${eventToken}" + """","data": {"applicationID": 6194,"applicationSubmittedDate": "2019-03-13","ihtFormCompletedOnline": "No","ihtFormId": "IHT205","softStop": "No","registryLocation": "Manchester","applicationType": "Personal","outsideUKGrantCopies": 3,"extraCopiesOfGrant": 2,"deceasedAnyOtherNames": "Yes","numberOfExecutors": 7,"willHasCodicils": "Yes","willNumberOfCodicils": 5,"deceasedForenames": "greby ${DeceasedForenames}","numberOfApplicants": 3,"primaryApplicantEmailAddress": "woods${PrimaryApplicantEmailAddress}@test.com","primaryApplicantAliasReason": "other","primaryApplicantForenames": "helly ${PrimaryApplicantForenames}","primaryApplicantAlias": "welly choptor","primaryApplicantSurname": "coptor ${PrimaryApplicantSurname}","primaryApplicantOtherReason": "other_reason_for_name_change","ihtReferenceNumber": "Not applicable","primaryApplicantPhoneNumber": "02020${PrimaryApplicantPhoneNumber}","deceasedMarriedAfterWillOrCodicilDate": "No","primaryApplicantSameWillName": "No","deceasedSurname": "mebble ${DeceasedSurname}","deceasedDateOfDeath": "1979-01-12","deceasedDateOfBirth": "1955-01-01","executorsNotApplying": [{"value": {"notApplyingExecutorName": "exec2","notApplyingExecutorReason": "DiedBefore"}},{"value": {"notApplyingExecutorName": "exec4","notApplyingExecutorReason": "PowerReserved"}},{"value": {"notApplyingExecutorName": "exec5","notApplyingExecutorReason": "Renunciation"}},{"value": {"notApplyingExecutorName": "exec7","notApplyingExecutorReason": "DiedAfter"}}],"executorsApplying": [{"value": {"applyingExecutorName": "exec3","applyingExecutorPhoneNumber": "07773055642","applyingExecutorEmail": "douglas.rice@hmcts.net","applyingExecutorAddress": {"AddressLine1": "8 Exeter Road London N14 5JY"},"applyingExecutorOtherNames": "exec3 current name","applyingExecutorOtherNamesReason": "other","applyingExecutorOtherReason": "exec3 other name change reason"}},{"value": {"applyingExecutorName": "exec6","applyingExecutorPhoneNumber": "07773055642","applyingExecutorEmail": "douglas.rice@hmcts.net","applyingExecutorAddress": {"AddressLine1": "20 Exeter Road London N14 5JY"},"applyingExecutorOtherNames": "exec6 current name","applyingExecutorOtherNamesReason": "other","applyingExecutorOtherReason": "exec 6 other reason name change"}}],"totalFee": "21750","ihtGrossValue": "10000000","ihtNetValue": "5000000","deceasedAliasNameList": [{"value": {"Forenames": "grebby","LastName": "mantles"}}],"declaration": {"confirm": "We confirm that we will administer the estate of greby mebble, according to law. We will:","confirmItem1": "collect the whole estate","confirmItem2": "keep full details (an inventory) of the estate","confirmItem3": "keep a full account of how the estate has been administered","requests": "If the probate registry (court) asks us to do so, we will:","requestsItem1": "provide the full details of the estate and how it has been administered","requestsItem2": "return the grant of probate to the court","understand": "We understand that:","understandItem1": "our application will be rejected if we do not answer any questions about the information we have given","understandItem2": "criminal proceedings for fraud may be brought against us if we are found to have been deliberately untruthful or dishonest","accept": "I confirm that I will administer the estate of the person who died according to law, and that my application is truthful."},"legalStatement": {"applicant": "We, helly coptor of 24 Exeter Road London N14 5JY, exec3 current name of 8 Exeter Road London N14 5JY and exec6 current name of 20 Exeter Road London N14 5JY, make the following statement:","deceased": "greby mebble was born on 1 January 1955 and died on 12 January 1979, domiciled in England and Wales.","deceasedOtherNames": "They were also known as grebby mantles.","deceasedEstateValue": "The gross value for the estate amounts to £100000 and the net value for the estate amounts to £50000.","deceasedEstateLand": "To the best of our knowledge, information and belief, there was no land vested in greby mebble which was settled previously to the death (and not by the will) of greby mebble and which remained settled land notwithstanding such death.","executorsNotApplying": [{"value": {"executor": "exec2, an executor named in the will or codicils, is not making this application because they died before greby mebble died."}},{"value": {"executor": "exec4, an executor named in the will or codicils, is not making this application but reserves power to do so at a later date. They have been notified in writing."}},{"value": {"executor": "exec5, an executor named in the will or codicils, is not making this application now and gives up the right to do so in the future."}},{"value": {"executor": "exec7, an executor named in the will or codicils, is not making this application because they died after greby mebble died."}}],"executorsApplying": [{"value": {"name": "helly coptor, an executor named in the will or codicils as welly choptor, is applying for probate. Their name is different because helly coptor: other_reason_for_name_change.","sign": "helly coptor will send to the probate registry what they believe to be the true and original last will and testament and any codicils of greby mebble."}},{"value": {"name": "exec3 current name, an executor named in the will or codicils as exec3, is applying for probate. Their name is different because exec3 current name: exec3 other name change reason.","sign": ""}},{"value": {"name": "exec6 current name, an executor named in the will or codicils as exec6, is applying for probate. Their name is different because exec6 current name: exec 6 other reason name change.","sign": ""}}],"intro": "This statement is based on the information helly coptor has given in their application. It will be stored as a public record."},"deceasedAddress": {"AddressLine1": "${DeceasedAddress} Exeter Road London N14 5JY"},"primaryApplicantAddress": {"AddressLine1": "${PrimaryApplicantAddress} Exeter Road London N14 5JY"},"boDocumentsUploaded": [{"value": {"DocumentType": "deathCertificate","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"},"Comment": "${filename}"}},{"value": {"DocumentType": "deathCertificate","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"},"Comment": "${filename}"}},{"value": {"DocumentType": "deathCertificate","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"},"Comment": "${filename}"}}]}}""")


  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateSIDAMUserToken()

  val   CreateProbateGrantMECaseDataSCN = scenario("Create Probate Grant Application Multi Exe Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("ApplicationFeePaperForm", applicationFeePaperForm()),
        ("FeeForCopiesPaperForm", feeForCopiesPaperForm()),
        ("TotalFeePaperForm", totalFeePaperForm()),
        ("PaymentReferenceNumberPaperform",paymentReferenceNumberPaperform()),
        ("PrimaryApplicantForenamesTXT",primaryApplicantForenamesTXT()),
        ("PrimaryApplicantForenames",primaryApplicantForenames()),
        ("PrimaryApplicantSurnameTXT",primaryApplicantSurnameTXT()),
        ("PrimaryApplicantSurname",primaryApplicantSurname()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText()),
        ("PrimaryApplicantPhoneNumber", primaryApplicantPhoneNumber()),
        ("PrimaryApplicantSecondPhoneNumber",primaryApplicantSecondPhoneNumber()),
        ("PrimaryApplicantEmailAddress",primaryApplicantEmailAddress()),
        ("PrimaryApplicantAddress",primaryApplicantAddress()),
        ("PickCaseEventType",PickCaseType()),
        ("DeceasedForenames", deceasedForenames()),
        ("DeceasedSurname", deceasedSurname()),
        ("DeceasedAddress", deceasedAddress()),
        ("UkEstateValue", ukEstateValue()),
        ("IhtGrossValue", ihtGrossValue())
      )
    ).feed(fileProviderRand)
      .exec(
      http("TX01_CCD_CreateCaseEndpoint_Probate_ApplyGrantApplication_eventtoken")
        .get(CreateCaseTokenUrl)
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
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
      http("TX02_CCD_CreateCaseEndpoint_Probate_ApplyGrantApplicationME_createcasedata")
        .post(CreateCaseUrl)
        .body(CreateProbateGrantMECaseDataEventBodyMain).asJSON
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }


  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

