package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object CreateSSCSCaseDataKJ extends PerformanceTestsConfig {

 // val EventId = "applyForGrant"
  private val rng: Random = new Random()
  private def generatedSurname(): String = rng.alphanumeric.take(10).mkString
  private def generatedNino(): Int = rng.nextInt(99999999)
  private def caseReference(): Int = rng.nextInt(99999999)
  private def generatedEmail(): String = rng.alphanumeric.take(10).mkString
  private def generatedMobile(): Int = rng.nextInt(99999999)
  private def generatedDOB():  Int = rng.nextInt(99)
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString
  private def appellantfirstName(): String = rng.alphanumeric.take(10).mkString
  private def appellantmiddleName(): String = rng.alphanumeric.take(10).mkString
  private def appellantlastName(): String = rng.alphanumeric.take(10).mkString
  private def appointeefirstName(): String = rng.alphanumeric.take(10).mkString
  private def appointeemiddleName(): String = rng.alphanumeric.take(10).mkString
  private def appointeelastName(): String = rng.alphanumeric.take(10).mkString
  private def addressNumber():  Int = rng.nextInt(99)


  // val EventId = "CREATE"
  val randcaseType = new Random(System.currentTimeMillis())
  val caseEventTypeValue = Array("appealCreated")
  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)
  val EventId = caseEventTypeValue(caseTypeValue_random_index)
  def PickCaseType(): String = EventId
 //def PickCaseType(): String = caseEventTypeValue(randcaseType.nextInt(caseEventTypeValue.length))

  println("CreateSSCSCaseData caseTypeText Value   " + EventId)
  println("CreateSSCSCaseData caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createCaseSSCSUrl"))

  println("CreateSSCSCaseData create case url: " + CreateCaseUrl)

    val caseTypeValue = Array("Benefit")
  //val caseTypeValue = Array("ATCASETYPE1","ATCASETYPE2","ATCASETYPE3","ATCASETYPE4")
  val jurisdictionsValue = Array("SSCS")

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

  println("CreateSSCSCaseData create case url: " + CreateCaseUrl)

  if(!CreateCaseUrl.contains(":jurisdictions_reference"))
  {
    CreateCaseUrl
  }
  else{
    CreateCaseUrl = CreateCaseUrl.replace(":jurisdictions_reference", jurisdictionsValue(jurisdictions_random_index))
  }

  println("create case url: " + CreateCaseUrl)
  val CreateCaseTokenUrl = s"${CreateCaseUrl.replaceAll("cases", "")}event-triggers/$EventId/token"


  println("CreateSSCSCaseData create case url: " + CreateCaseUrl)
  println("CreateSSCSCaseData create case token url: " + CreateCaseTokenUrl)

  val EventBodyMain =StringBody("""{"data":{"appeal":{"receivedVia":"Online","mrnDetails":{"dwpIssuingOffice":"DWP101010","mrnDate":"2010-12-10","mrnLateReason":"Notknown","mrnMissingReason":"NoReason"},"appellant":{"name":{"title":"Mr","firstName":"Jooles ${AppellantfirstName}","middleName":"Rodriguez ${AppellantmiddleName}","lastName":"Fernandez ${AppellantlastName}"},"identity":{"dob":"19${GeneratedDOB}-11-11","nino":"SGD83241"},"address":{"line1":"${AddressNumber} AvenueRoad","line2":"RevenueLane","line3":null,"town":"Chiswick","county":"Middlesex","postcode":"W45XR","country":"UK"},"contact":{"phone":"77${GeneratedMobile}","mobile":"88${GeneratedMobile}","email":"noreason_${GeneratedEmail}@everyreason.com"},"isAppointee":"Yes","appointee":{"name":{"title":"Mrs","firstName":"Davis ${AppointeefirstName}","middleName":"Novis ${AppointeemiddleName}","lastName":"Data ${AppointeelastName}"},"identity":{"dob":"19${GeneratedDOB}-01-01","nino":"SH656437"},"address":{"line1":"${AddressNumber} AvenueRoad","line2":"RevenueLane","line3":null,"town":"Chiswick","county":"Middlesex","postcode":"W45XR","country":"UK"},"contact":{"phone":"09${GeneratedMobile}","mobile":"08${GeneratedMobile}","email":"revenue_${GeneratedEmail}@jas.com"}},"isAddressSameAsAppointee":"Yes"},"benefitType":{"code":"B0120202","description":"Benefit"},"hearingType":"domiciliary","hearingOptions":{"wantsToAttend":"Yes","wantsSupport":"Yes","languageInterpreter":"No","arrangements":["signLanguageInterpreter"],"scheduleHearing":"No","other":"Otherinformation","signLanguageType":"wer"},"appealReasons":{"reasons":[],"otherReasons":null},"supporter":{"name":{"title":"Mr","firstName":"Data","middleName":"less","lastName":"lastname"},"contact":{"phone":"09${GeneratedMobile}","mobile":"07${GeneratedMobile}","email":"house_${GeneratedEmail}@revenue.com"}},"rep":{"hasRepresentative":"No"},"signer":null},"regionalProcessingCenter":{"name":null,"address1":"${AddressNumber}AvenueRoad","address2":"RevenueRoad","address3":null,"address4":null,"postcode":"W45XR","city":"Chiswick","phoneNumber":"0902340982","faxNumber":"203480284"},"panel":{"assignedTo":"MrJudge","medicalMember":"Medico","disabilityQualifiedMember":"none"},"caseReference":"1${CaseReference}","caseCreated":"19${GeneratedDOB}-11-10","region":"London","generatedNino":"S1${GeneratedNino}","generatedSurname":"Bakerloo ${GeneratedSurname}","generatedEmail":"bakerrloo_${GeneratedEmail}@bakerloo.com","generatedMobile":"07${GeneratedMobile}","generatedDOB":"19${GeneratedDOB}-10-10","evidencePresent":"Yes"},"event":{"id":"appealCreated","summary":"Performance Testing SSCS summary ${CaseSummaryText}","description":"Performance Testing SSCS Event description ${CaseDescriptionText}"},"event_token":""""+"${eventToken}"+"""","ignore_warning":false,"draft_id":null}""")

  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateWebUserToken()


  val   CreateSSCSCaseDataSCN = scenario("Create SSCS Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("GeneratedSurname", generatedSurname()),
        ("GeneratedNino", generatedNino()),
        ("CaseReference", caseReference()),
        ("GeneratedEmail",generatedEmail()),
        ("GeneratedMobile",generatedMobile()),
        ("GeneratedDOB",generatedDOB()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText()),
        ("AppellantfirstName", appellantfirstName()),
        ("AppellantmiddleName", appellantmiddleName()),
        ("AppellantlastName", appellantlastName()),
        ("AppointeefirstName", appointeefirstName()),
        ("AppointeemiddleName", appointeemiddleName()),
        ("AppointeelastName", appointeelastName()),
        ("AddressNumber",addressNumber()),
        ("PickCaseEventType",PickCaseType())
      )
    ).exec(
      //http("get create case event token")
      http("TX21_CCD_CreateCaseEndpoint_SSCS_createcase_eventtoken")
        .get(CreateCaseTokenUrl)
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
    ).exec(
      //http("create case data")
      http("TX22_CCD_CreateCaseEndpoint_SSCS_createcasedata")
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

