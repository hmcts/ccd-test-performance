package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random

object CreateProbateCaveatCaseData extends PerformanceTestsConfig {


  private val rng: Random = new Random()

  private def deceasedForenames(): String = rng.alphanumeric.take(10).mkString
  private def deceasedSurname(): String = rng.alphanumeric.take(10).mkString
  private def fullAliasName(): String = rng.alphanumeric.take(10).mkString
  private def deceasedAddress(): Int = rng.nextInt(9999)
  private def caveatorForenames(): String = rng.alphanumeric.take(10).mkString
  private def caveatorSurname(): String = rng.alphanumeric.take(10).mkString
  private def caveatorEmailAddress(): Int = rng.nextInt(9999999)
  private def caveatorAddress(): Int = rng.nextInt(9999)
  private def messageContent(): String = rng.alphanumeric.take(10).mkString
  private def documentsUploadedComment(): String = rng.alphanumeric.take(10).mkString
  private def documentsGeneratedFile(): String = rng.alphanumeric.take(10).mkString
  private def documentsGeneratedComment(): String = rng.alphanumeric.take(10).mkString
  private def caseMatchesFullName(): String = rng.alphanumeric.take(10).mkString
  private def aliases(): String = rng.alphanumeric.take(10).mkString
  private def comment(): String = rng.alphanumeric.take(10).mkString

  private def caseReference(): Int = rng.nextInt(99999999)
  private def recordId(): Int = rng.nextInt(99999999)
  private def legacyCaseViewUrl(): String = rng.alphanumeric.take(10).mkString

  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString

  private def deceasedDateOfBirth(): Int = 10 + rng.nextInt(100 - 10) + 1
  private def deceasedDateOfDeath(): Int = 10 + rng.nextInt(20 - 13) + 1
  private def caseMatchesDOB(): Int = 10 + rng.nextInt(100 - 10) + 1
  private def caseMatchesDOD(): Int = 10 + rng.nextInt(20 - 13) + 1

  val randcaseType = new Random(System.currentTimeMillis())
  val caseEventTypeValue = Array("applyForCaveat")
  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)
  val EventId = caseEventTypeValue(caseTypeValue_random_index)
  def PickCaseType(): String = EventId

  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createCaseProbateCaveatApplicationUrl"))

  println("create case url: " + CreateCaseUrl)

  val caseTypeValue = Array("Caveat")

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

  val CreateProbateCaveatCaseDataEventBodyMain =StringBody("""{"data": {"applicationType": "Personal","registryLocation": "Oxford","deceasedForenames": "Caveat First Name ${DeceasedForenames}","deceasedSurname": "Caveat Surname Name ${DeceasedSurname}","deceasedDateOfDeath": "2014-03-30","deceasedDateOfBirth": "19${DeceasedDateOfBirth}-06-30","deceasedAnyOtherNames": "No","deceasedFullAliasNameList": [{"id": null,"value": {"FullAliasName": "Deceased alias names ${FullAliasName}"}}],"deceasedAddress": {"AddressLine1": "${DeceasedAddress} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CF10 2HT","Country": "United Kingdom"},"caveatorForenames": "${CaveatorForenames}","caveatorSurname": "${CaveatorSurname}","caveatorEmailAddress": "confirmation${CaveatorEmailAddress}@confirmation.com","caveatorAddress": {"AddressLine1": "${CaveatorAddress} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CO3 0RN","Country": "United Kingdom"},"expiryDate": "2019-06-30","messageContent": "Message Content ${MessageContent}","documentsUploaded": [{"id": null,"value": {"DocumentType": "deathCertificate","Comment": "Death Certificate Attached ${DocumentsUploadedComment}","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "Death Certificate Attached ${filename}"}}},{"id": null,"value": {"DocumentType": "deathCertificate","Comment": "Death Certificate Attached ${DocumentsUploadedComment}","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "Death Certificate Attached ${filename}"}}}],"documentsGenerated": [{"id": null,"value": {"DocumentType": "digitalGrant","DocumentDateAdded": "1920-06-30","DocumentFileName": "Digital Grant Attached ${DocumentsGeneratedFile}","DocumentGeneratedBy": "John Smith ${DocumentsGeneratedComment}","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "Digital Grant Attached ${filename}"}}}],"caseMatches": [{"id": null,"value": {"type": "Mr","fullName": "${CaseMatchesFullName}","dob": "12/12/19${CaseMatchesDOB}","dod": "12/12/20${CaseMatchesDOD}","aliases": "test Aliases ${Aliases}","postcode": "CO3 0RN","comment": "test Aliases ${Comment}","caseLink": {"CaseReference": "12345678${CaseReference}"},"recordId": "12${RecordId}","legacyCaseViewUrl": "testURL${LegacyCaseViewUrl}","valid": "No","doImport": "No"}}],"recordId": "1234567890654321","legacyId": "1234567890123456","legacyType": "Legacy case type 1234568890","legacyCaseViewUrl": "Legacy case link 1234567890"},"event": {"id": "applyForCaveat","summary": "4th Page Check your answers - Event summary ${CaseSummaryText}","description": "Event description ${CaseDescriptionText}"},"event_token": """"+ "${eventToken}" + """","ignore_warning": false,"draft_id": null}""")


  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateWebUserToken()


  val   CreateProbateCaveatCaseDataSCN = scenario("Create Probate Caveat Application Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("DeceasedForenames", deceasedForenames()),
        ("DeceasedSurname", deceasedSurname()),
        ("FullAliasName", fullAliasName()),
        ("DeceasedAddress",deceasedAddress()),
        ("CaveatorForenames",caveatorForenames()),
        ("CaveatorSurname",caveatorSurname()),
        ("CaveatorEmailAddress",caveatorEmailAddress()),
        ("CaveatorAddress",caveatorAddress()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText()),
        ("MessageContent", messageContent()),
        ("DocumentsUploadedComment",documentsUploadedComment()),
        ("DocumentsGeneratedFile",documentsGeneratedFile()),
        ("DocumentsGeneratedComment",documentsGeneratedComment()),
        ("PickCaseEventType",PickCaseType()),
        ("CaseMatchesFullName", caseMatchesFullName()),
        ("Aliases", aliases()),
        ("Comment", comment()),
        ("CaseReference", caseReference()),
        ("RecordId", recordId()),
        ("LegacyCaseViewUrl", legacyCaseViewUrl()),
        ("DeceasedDateOfBirth", deceasedDateOfBirth()),
        ("deceasedDateOfDeath", deceasedDateOfBirth()),
        ("CaseMatchesDOB", caseMatchesDOB()),
        ("CaseMatchesDOD", caseMatchesDOD())
      )
    ).feed(fileProviderRand)
      .exec(
      http("TX01_CCD_CreateCaseEndpoint_Probate_ApplyCaveatApplication_eventtoken")
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
      http("TX02_CCD_CreateCaseEndpoint_Probate_ApplyCaveatApplicationME_createcasedata")
        .post(CreateCaseUrl)
        .body(CreateProbateCaveatCaseDataEventBodyMain).asJSON
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }


  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

