package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object ESStdTest extends PerformanceTestsConfig {

 // val EventId = "applyForGrant"
  private val rng: Random = new Random()
  private def firstpageText(): String = rng.alphanumeric.take(10).mkString
  private def firstpageNumberField(): Int = rng.nextInt(999999999)
  private def firstpageEmailRandNumber(): Int = rng.nextInt(999999)
  private def firstpageMoneyField(): Int = rng.nextInt(9999999)
  private def secondpageText(): String = rng.alphanumeric.take(15).mkString
  private def thirdpageText(): String = rng.alphanumeric.take(15).mkString
  private def thirdpageNestedNumberField(): Int = rng.nextInt(99999999)
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString

 // val EventId = "CREATE"
  val randcaseType = new Random(System.currentTimeMillis())
  val caseEventTypeValue = Array("CREATE","CREATEASPROG","CREATEASDONE")
  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)
  val EventId = caseEventTypeValue(caseTypeValue_random_index)
  def PickCaseType(): String = EventId
 //def PickCaseType(): String = caseEventTypeValue(randcaseType.nextInt(caseEventTypeValue.length))

  println("ES caseTypeText Value   " + EventId)
  println("ES caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createCaseUrl"))

  println("ES create case url: " + CreateCaseUrl)

    //val caseTypeValue = Array("AAT","CASETYPE2","CASETYPE3","CASETYPE4")
  //val caseTypeValue = Array("ATCASETYPE1","ATCASETYPE2","ATCASETYPE3","ATCASETYPE4")
  val caseTypeValue = Array("AAT")
  val jurisdictionsValue = Array("AUTOTEST1")

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



  //val EventBodyMain = StringBody("""{"data": {"TextField": "Performance Testing First Page Text ${FirstpageText}","NumberField": "${FirstpageNumberField}","YesOrNoField": "Yes","PhoneUKField": "02020002002","EmailField": "confirmation${FirstpageEmailRandNumber}@confirmation.com","MoneyGBPField": "${FirstpageMoneyField}","DateField": "2018-06-12","TextAreaField": "Performance Testing Second page TextArea ${SecondpageText}","FixedListField": "","MultiSelectListField": [],"ComplexField": {"ComplexTextField": "Performance Testing Third page Text - Third page Text ${ThirdpageText}","ComplexFixedListField": "","ComplexNestedField": {"NestedNumberField": "${ThirdpageNestedNumberField}","NestedCollectionTextField": []}}},"event": {"id": "CREATE","summary": "4th Page Check your answers - Performance Testing Event summary ${CaseSummaryText}","description": "4th Page Check your answers - Performance Testing Event description ${CaseDescriptionText}"},"event_token": """"  + "${eventToken}" +   """","ignore_warning": false}""")
  val EventBodyMain = StringBody("""{"data": {"TextField": "Performance Testing First Page Text ${FirstpageText}","NumberField": "${FirstpageNumberField}","YesOrNoField": "Yes","PhoneUKField": "02020002002","EmailField": "confirmation${FirstpageEmailRandNumber}@confirmation.com","MoneyGBPField": "${FirstpageMoneyField}","DateField": "2018-06-12","TextAreaField": "Performance Testing Second page TextArea ${SecondpageText}","FixedListField": "","MultiSelectListField": [],"ComplexField": {"ComplexTextField": "Performance Testing Third page Text - Third page Text ${ThirdpageText}","ComplexFixedListField": "","ComplexNestedField": {"NestedNumberField": "${ThirdpageNestedNumberField}","NestedCollectionTextField": []}}},"event": {"id": "${PickCaseEventType}","summary": "4th Page Check your answers - Performance Testing Event summary ${CaseSummaryText}","description": "4th Page Check your answers - Performance Testing Event description ${CaseDescriptionText}"},"event_token": """"  + "${eventToken}" +   """","ignore_warning": false}""")



  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateWebUserToken(CreateCaseUrl)


 /* def createCaseDatahttp() = {
    val token = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(CreateCaseUrl)
    exec(
         //http("get create case event token")
           http("TX02_CCD_CreateCaseEndpoint_createcase_eventtoken")
           .get(CreateCaseTokenUrl)
           .header("ServiceAuthorization", token)
           .header("Authorization", userToken)
           .header("Content-Type","application/json")
           .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
      ).exec(
        //http("create case data")
        http("TX02_CCD_CreateCaseEndpoint_createcasedata")
        .post(CreateCaseUrl)
        /*.body(
          EventBody).asJson*/
          .body(EventBodyMain).asJson
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )
  }


  println("PostCaseData: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val   createCaseData = scenario("Create Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("FirstpageText", firstpageText()),
        ("FirstpageNumberField", firstpageNumberField()),
        ("FirstpageEmailRandNumber", firstpageEmailRandNumber()),
        ("FirstpageMoneyField",firstpageMoneyField()),
        ("SecondpageText",secondpageText()),
        ("ThirdpageText",thirdpageText()),
        ("ThirdpageNestedNumberField",thirdpageNestedNumberField()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText()),
        ("PickCaseEventType",PickCaseType())
      )
    ).exec(
          createCaseDatahttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }
*/
 private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search on reference metadata PT - URL: " + url)

  val ESSearchONReferenceMetaDataReqPayload = StringBody(
                            """
                                {
                                   "query":{
                                      "bool":{
                                         "filter":{
                                            "match":{
                                               "reference":"${ESNew_Case_Id}"
                                            }
                                         }
                                      }
                                   }
                                }
                              """
                            )

  val   ESStdTEST = scenario("CCD Create Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("FirstpageText", firstpageText()),
        ("FirstpageNumberField", firstpageNumberField()),
        ("FirstpageEmailRandNumber", firstpageEmailRandNumber()),
        ("FirstpageMoneyField",firstpageMoneyField()),
        ("SecondpageText",secondpageText()),
        ("ThirdpageText",thirdpageText()),
        ("ThirdpageNestedNumberField",thirdpageNestedNumberField()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText()),
        ("PickCaseEventType",PickCaseType())
      )
    ).exec(
      //http("get create case event token")
      http("TX02_CCD_CreateCaseEndpoint_createcase_eventtoken")
        .get(CreateCaseTokenUrl)
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
    ).exec(
      //http("create case data")
      http("TX02_CCD_CreateCaseEndpoint_createcasedata")
        .post(CreateCaseUrl)
        .body(EventBodyMain).asJson
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(jsonPath("$.id").saveAs("ESNew_Case_Id"))
        .check(status is 201)
    )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)

      .exec(
        http("TX13_CCD_ElasticSearchEndpoint_ReferenceMetaDataSearch")
          .post(url)
          .queryParam("ctid", "AAT")
          .body(
            ESSearchONReferenceMetaDataReqPayload).asJson
         // .body(StringBody("""{"query": {"bool": {"filter": {"wildcard": {"reference": """"  + "${ESNew_Case_Id}" +   """""}}}}""")).asJson
          .header("ServiceAuthorization", token)
          .header("Authorization", userToken)
          .header("Content-Type","application/json")
          .check(status in  (200))
      )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }


  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

