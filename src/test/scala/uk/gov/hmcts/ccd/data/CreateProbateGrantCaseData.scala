package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.core.feeder.SourceFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random

object CreateProbateGrantCaseData extends PerformanceTestsConfig {


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

  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: SourceFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: SourceFeederBuilder[String] = csv("listoffiles.csv").random

  val randcaseType = new Random(System.currentTimeMillis())
  val caseEventTypeValue = Array("applyForGrant")
  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)
  val EventId = caseEventTypeValue(caseTypeValue_random_index)
  def PickCaseType(): String = EventId

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

  val CreateProbateGrantCaseDataEventBodyMain =StringBody("""{"data": {"registryLocation": "London","applicationType": "Personal","applicationSubmittedDate": "2018-12-12","caseType": "gop","extraCopiesOfGrant": "1","outsideUKGrantCopies": "1","applicationFeePaperForm": "${ApplicationFeePaperForm}","feeForCopiesPaperForm": "${FeeForCopiesPaperForm}","totalFeePaperForm": "${TotalFeePaperForm}","paperPaymentMethod": "debitOrCredit","paymentReferenceNumberPaperform": "PAYREF:${PaymentReferenceNumberPaperform}","primaryApplicantForenames": "PA application First Name ${PrimaryApplicantForenamesTXT} ${PrimaryApplicantForenames}","primaryApplicantSurname": "PA application Last Name ${PrimaryApplicantSurnameTXT} ${PrimaryApplicantSurname}","primaryApplicantPhoneNumber": "02920${PrimaryApplicantPhoneNumber}","primaryApplicantSecondPhoneNumber": "02020${PrimaryApplicantSecondPhoneNumber}","primaryApplicantEmailAddress": "PAApplicant${PrimaryApplicantEmailAddress}@confirmation.com","primaryApplicantAddress": {"AddressLine1": "${PrimaryApplicantAddress} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CO3 0RN","Country": "United Kingdom"},"primaryApplicantRelationshipToDeceased": "partner","primaryApplicantHasAlias": "No","primaryApplicantIsApplying": "Yes","otherExecutorExists": "No","notifiedApplicants": "No","adopted": "No","boDeceasedTitle": "Mr","deceasedForenames": "Deceased FirstName ${DeceasedForenames}","deceasedSurname": "Deceased Last ${DeceasedSurname}","boDeceasedHonours": "OBE","deceasedAddress": {"AddressLine1": "${DeceasedAddress} Landmark Place","AddressLine2": "Churchill Way","AddressLine3": "","PostTown": "Cardiff","County": "","PostCode": "CF10 2HT","Country": "United Kingdom"},"deceasedDateOfBirth": "1945-03-21","deceasedDateOfDeath": "2016-06-30","deceasedAnyOtherNames": "No","deceasedMartialStatus": "marriedCivilPartnership","foreignAsset": "No","willExists": "No","deceasedEnterMarriageOrCP": "No","dateOfDivorcedCPJudicially": "2018-12-12","courtOfDecree": "London","willGiftUnderEighteen": "Yes","spouseOrPartner": "Yes","applyingAsAnAttorney": "No","deceasedDomicileInEngWales": "No","domicilityCountry": "UK","ukEstate": [{"id": null,"value": {"item": "House","value": "${UkEstateValue}"}}],"domicilityIHTCert": "No","ihtFormCompletedOnline": "No","ihtFormId": "IHT205","ihtGrossValue": "${IhtGrossValue}","ihtNetValue": "${IhtGrossValue}","boDocumentsUploaded": [{"value": {"DocumentType": "deathCertificate","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"},"Comment": "${filename}"}},{"value": {"DocumentType": "deathCertificate","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"},"Comment": "${filename}"}},{"value": {"DocumentType": "deathCertificate","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"},"Comment": "${filename}"}}]},"event": {"id": "applyForGrant","summary": "Performance Testing Event summary ${CaseSummaryText}","description": "Performance Testing Event description ${CaseDescriptionText}"},"event_token": """"+ "${eventToken}" + """","ignore_warning": false,"draft_id": null}""")

  val token = CcdTokenGenerator.generateGatewayS2SToken()

  val userToken = CcdTokenGenerator.generateWebUserToken()


  val   CreateProbateGrantCaseDataSCN = scenario("Create Probate Grant Application Case Data").during(TotalRunDuration minutes) {
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
      http("TX02_CCD_CreateCaseEndpoint_Probate_ApplyGrantApplication_createcasedata")
        .post(CreateCaseUrl)
        .body(CreateProbateGrantCaseDataEventBodyMain).asJson
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }


  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

