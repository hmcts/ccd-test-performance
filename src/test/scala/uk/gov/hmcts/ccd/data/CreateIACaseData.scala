package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object CreateIACaseData extends PerformanceTestsConfig {


  private val rng: Random = new Random()
  private def homeOfficeReferenceNumber(): Int = 1000000 + rng.nextInt(9999999 - 1000000) + 1
  private def appellantGivenNames(): String = rng.alphanumeric.take(10).mkString
  private def appellantFamilyName(): String = rng.alphanumeric.take(10).mkString
  private def appellantDateOfBirth(): Int = 10 + rng.nextInt(100 - 10) + 1
  private def appellantAddress(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def newMatters():  String = rng.alphanumeric.take(10).mkString
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

  val randcaseType = new Random(System.currentTimeMillis())

  val caseEventTypeValue = Array("startAppeal")

  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)

  val EventId = caseEventTypeValue(caseTypeValue_random_index)

  def PickCaseType(): String = EventId


  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createIACaseAppURL"))

  println("create case url: " + CreateCaseUrl)

  val caseTypeValue = Array("Asylum")
  val jurisdictionsValue = Array("IA")


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


  val EventBodyMain =StringBody("""{"data":{"checklist":{"checklist1":["isAdult"],"checklist2":["isNotDetained"],"checklist3":["isNotFamilyAppeal"],"checklist4":["isWithinPostcode"],"checklist5":["isResidingInUK"],"checklist6":["isNotStateless"]},"homeOfficeReferenceNumber":"A${HomeOfficeReferenceNumber}/001","homeOfficeDecisionDate":"2019-12-12","appellantTitle":"Mr","appellantGivenNames":"Basic-GivenName-${AppellantGivenNames}","appellantFamilyName":"Basic-FamilyName-${AppellantFamilyName}","appellantDateOfBirth":"19${AppellantDateOfBirth}-06-12","appellantNationalities":[{"id":null,"value":{"code":"GB"}}],"appellantHasFixedAddress":"Yes","appellantAddress":{"AddressLine1":"${AppellantAddress} LandmarkPlace","AddressLine2":"ChurchillWay","AddressLine3":"","PostTown":"Cardiff","County":"","PostCode":"CF102HT","Country":"UnitedKingdom"},"appealType":"protection","appealGroundsProtection":{"values":["protectionRefugeeConvention","protectionHumanitarianProtection"]},"appealGroundsHumanRights":{"values":["protectionHumanRights"]},"hasNewMatters":"Yes","newMatters":"New matters reason ${NewMatters}","hasOtherAppeals":"No","legalRepReferenceNumber":null},"event":{"id":"startAppeal","summary":"${CaseSummaryText}","description":"${CaseDescriptionText}"},"event_token": """"  + "${eventToken}" +   """","ignore_warning":false,"draft_id":null}""")


  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateWebUserToken()


  val   CreateIACaseDataSCN = scenario("Create IA Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("HomeOfficeReferenceNumber", homeOfficeReferenceNumber()),
        ("AppellantGivenNames", appellantGivenNames()),
        ("AppellantFamilyName", appellantFamilyName()),
        ("AppellantDateOfBirth",appellantDateOfBirth()),
        ("AppellantAddress",appellantAddress()),
        ("NewMatters",newMatters()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText())
      )
    ) .feed(fileProviderRand)
      .exec(
      http("TX01_CCD_CreateCaseEndpoint_IA_createcase_eventtoken")
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
      http("TX02_CCD_CreateCaseEndpoint_IA_createcasedata")
        .post(CreateCaseUrl)
        .body(EventBodyMain).asJSON
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }


  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

