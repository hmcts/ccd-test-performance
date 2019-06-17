package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object CreateEthosCaseDataWithDocs extends PerformanceTestsConfig {

  private val rng: Random = new Random()
  private def ethosCaseReference(): Int = 1000000 + rng.nextInt(9999999 - 1000000) + 1
  private def feeGroupReference(): Int = 1000000 + rng.nextInt(9999999 - 1000000) + 1
  private def claimant_first_names(): String = rng.alphanumeric.take(10).mkString
  private def claimant_last_name(): String = rng.alphanumeric.take(10).mkString
  private def claimant_date_of_birth(): Int = 10 + rng.nextInt(100 - 10) + 1
  private def claimant_addressUK(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def claimant_phone_number(): Int = 100000 + rng.nextInt(999999 - 100000) + 1
  private def claimant_mobile_number(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def claimant_email_address(): Int = 100000 + rng.nextInt(999999 - 100000) + 1
  private def respondent_name(): String = rng.alphanumeric.take(10).mkString
  private def respondent_address(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def respondent_phone1(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def claimant_work_address(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def claimant_work_phone_number(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def add_respondent_name(): String = rng.alphanumeric.take(10).mkString
  private def add_respondent_address(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def add_respondent_phone1(): Int = 100000 + rng.nextInt(999999 - 100000) + 1
  private def add_respondent_phone2(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def add_respondent_email(): Int = 100000 + rng.nextInt(999999 - 100000) + 1
  private def claimant_occupation(): String = rng.alphanumeric.take(10).mkString
  private def claimant_employed_from(): Int = 10 + rng.nextInt(100 - 10) + 1
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

  val randcaseType = new Random(System.currentTimeMillis())

  val caseEventTypeValue = Array("initiateCase")

  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)

  val EventId = caseEventTypeValue(caseTypeValue_random_index)

  def PickCaseType(): String = EventId


  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createETHOSCaseAppURL"))

  println("create case url: " + CreateCaseUrl)

    val caseTypeValue = Array("TRIB_MVP_3_TYPE_Manc")
  //val caseTypeValue = Array("ATCASETYPE1","ATCASETYPE2","ATCASETYPE3","ATCASETYPE4")
  val jurisdictionsValue = Array("PUBLICLAW")


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

  val EventBodyMain =StringBody("""{"data": {"ethosCaseReference": "CaseRef:123${EthosCaseReference}","receiptDate": "19${Claimant_date_of_birth}-02-01","feeGroupReference": "${FeeGroupReference}89012","claimant_TypeOfClaimant": "Individual","claimantIndType": {"claimant_title1": "Dr","claimant_title_other": null,"claimant_first_names": "John Claimant ${Claimant_first_names}","claimant_last_name": "Smith ${Claimant_last_name}","claimant_date_of_birth": "19${Claimant_date_of_birth}-02-01","claimant_gender": "Male"},"claimantType": {"claimant_addressUK": {"AddressLine1": "${Claimant_addressUK} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","Country": "United Kingdom","PostCode": "CO3 0RN"},"claimant_phone_number": "02920${Claimant_phone_number}","claimant_mobile_number": "070${Claimant_mobile_number}","claimant_email_address": "Ethos_${Claimant_email_address}@gmail.com","claimant_contact_preference": "Email"},"respondentSumType": {"respondent_name": "Steve K Respondent ${Respondent_name}","respondent_ACAS_question": "No","respondent_ACAS": null,"respondent_ACAS_no": null,"respondent_address": {"AddressLine1": "${Respondent_address} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","Country": "United Kingdom","PostCode": "CO3 0RN"},"respondent_phone2": null,"respondent_email": null,"respondent_contact_preference": null,"respondent_phone1": "079${Respondent_phone1}"},"claimantWorkAddress": {"claimant_work_address": {"AddressLine1": "${Claimant_work_address} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","Country": "United Kingdom","PostCode": "CO3 0RN"},"claimant_work_phone_number": "079${Claimant_work_phone_number}"},"respondentCollection": [{"id": null,"value": {"respondent_name": "Additional Respondent Steve R ${Add_respondent_name}","respondent_ACAS_question": "No","respondent_ACAS": null,"respondent_ACAS_no": "Unfair Dismissal","respondent_address": {"AddressLine1": "${Add_respondent_address} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CO3 0RN","Country": "United Kingdom"},"respondent_phone1": "02920${Add_respondent_phone1}","respondent_phone2": "079${Add_respondent_phone2}","respondent_email": "AdditionalResp.Steve.K.${Add_respondent_email}@confirmation.com","respondent_contact_preference": "Email"}}],"claimantOtherType": {"claimant_occupation": "Builder ${Claimant_occupation}","claimant_employed_from": "19${Claimant_employed_from}-01-01","claimant_employed_currently": "No","claimant_employed_to": null,"claimant_employed_notice_period": null,"claimant_disabled": "No","claimant_disabled_details": null},"claimantRepresentedQuestion": "No"},"event": {"id": "initiateCase","summary":"Ethos Performance Case Summary ${CaseSummaryText}","description":"Ethos Performance Case Description ${CaseDescriptionText}"},"event_token": """" + "${eventToken}" + """","ignore_warning": false,"draft_id": null}""")


  val createToken = CcdTokenGenerator.generateGatewayS2SToken()
  val createUserToken = CcdTokenGenerator.generateWebUserToken()

  val   CreateEthosCaseDataSCN = scenario("Create ETHOS Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("EthosCaseReference", ethosCaseReference()),
        ("FeeGroupReference", feeGroupReference()),
        ("Claimant_first_names", claimant_first_names()),
        ("Claimant_last_name", claimant_last_name()),
        ("Claimant_date_of_birth", claimant_date_of_birth()),
        ("Claimant_addressUK", claimant_addressUK()),
        ("Claimant_phone_number", claimant_phone_number()),
        ("Claimant_mobile_number", claimant_mobile_number()),
        ("Claimant_email_address", claimant_email_address()),
        ("Respondent_name", respondent_name()),
        ("Respondent_address", respondent_address()),
        ("Respondent_phone1", respondent_phone1()),
        ("Claimant_work_address", claimant_work_address()),
        ("Claimant_work_phone_number", claimant_work_phone_number()),
        ("Add_respondent_name", add_respondent_name()),
        ("Add_respondent_address", add_respondent_address()),
        ("Add_respondent_phone1", add_respondent_phone1()),
        ("Add_respondent_phone2", add_respondent_phone2()),
        ("Add_respondent_email", add_respondent_email()),
        ("Claimant_occupation", claimant_occupation()),
        ("Claimant_employed_from", claimant_employed_from()),
        ("CaseSummaryText", caseSummaryText()),
        ("CaseDescriptionText", caseDescriptionText())
      )
    ).feed(fileProviderRand)
      .exec(
        //http("get create case event token")
        http("TX01_CCD_CreateCaseEndpoint_ETHOS_createcase_eventtoken")
          .get(CreateCaseTokenUrl)
          .header("ServiceAuthorization", createToken)
          .header("Authorization", createUserToken)
          .header("Content-Type", "application/json")
          .check(status.is(200), jsonPath("$.token").saveAs("eventToken"))
      ).exec(http("TX20_DocStore_PostDocument_${filename}")
      // .post("/documents")
      .post(DocStoreBashURL + "/documents")
      .header("Authorization", createUserToken)
      .header("ServiceAuthorization", createToken)
      .header("user-id", "giri.benadikar@hmcts.net")
      .bodyPart(
        RawFileBodyPart("files", "${filename}")
          .contentType("application/pdf")
          .fileName("${filename}")).asMultipartForm
      .formParam("classification", "PUBLIC")
      .check(status is 200, jsonPath("$._embedded.documents[0]._links.binary.href").saveAs("fileId"), regex("""http://(.+)/""").saveAs("DMURL"), regex("""/documents/(.+)"""").saveAs("Document_ID")))
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
      .exec(
        //http("create case data")
        http("TX02_CCD_CreateCaseEndpoint_ETHOS_createcasedata")
          .post(CreateCaseUrl)
          .body(EventBodyMain).asJSON
          .header("ServiceAuthorization", createToken)
          .header("Authorization", createUserToken)
          .header("Content-Type", "application/json")
          .check(status is 201)
          .check(jsonPath("$.id").saveAs("New_Case_Id"))
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)

   // var SaveEventUrl = CreateCaseUrl + "${New_Case_Id}/events"

   // println("Update save event url: " + SaveEventUrl)
  }
  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

