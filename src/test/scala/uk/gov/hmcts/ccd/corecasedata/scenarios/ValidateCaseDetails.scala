package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ValidateCaseDetails extends PerformanceTestsConfig {

 // val EventId = "applyForGrant"
  val EventId = "CREATE"
  val CreateCaseUrl = caseDataUrl(config.getString("validateCaseUrl"))
  val CreateCaseTokenUrl = s"${CreateCaseUrl.replaceAll("cases", "")}event-triggers/$EventId/token"
  println("create case url: " + CreateCaseUrl)
  println("create case token url: " + CreateCaseTokenUrl)

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("validateCaseDetails")
  println("Retrieving validateCaseDetails URL : " + url)

  val ReqPayload = StringBody("""
                               {
                                "data": {
                                    "TextField": "First Page",
                                    "NumberField": "123",
                                    "YesOrNoField": "Yes",
                                    "PhoneUKField": "02920001002",
                                    "EmailField": "confirmation@confirmation.com"
                                    },
                                "event": {
                                    "id": "CREATE",
                                    "summary": "",
                                    "description": ""
                                    },
                                "event_token": "${eventToken}",
                                "ignore_warning": false
                                }
                              """)


  def validateCaseDatahttp() = {
    val token = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()
    exec(
         //http("get create case event token")
           http("TX10_CCD_ValidateCaseDataEndpoint_validatecasedata_eventtoken")
           .get(CreateCaseTokenUrl)
           .header("ServiceAuthorization", token)
           .header("Authorization", userToken)
           .header("Content-Type","application/json")
           .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
      ).exec(
        //http("create case data")
        http("TX11_CCD_CCD_ValidateCaseDataEndpoint_validatecasedata")
        .post(url)
        .body(
          ReqPayload).asJson
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 200)
    )
  }


  println("ValidateCaseData: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val   validateCaseData = scenario("Validate Case Data").during(TotalRunDuration minutes) {
      exec(
        validateCaseDatahttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

