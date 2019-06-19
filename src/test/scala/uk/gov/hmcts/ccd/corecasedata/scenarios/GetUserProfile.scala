package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import scala.concurrent.duration._

object GetUserProfile extends PerformanceTestsConfig {

  private val endpointUrl = caseDataUrl(config.getString("getUserProfileUrl"))
  println("endpoint url: " + endpointUrl)

  def call() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()
    //println("s2sToken:  " + s2sToken + " userToken " + userToken)
    //http("get user profile")
    http("TX05_CCD_GetUserProfileEndpoint_getuserprofile")
      .get(_ => endpointUrl)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .header("Accept"," application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
      .check(status in  (200))
  }

  println("GetUserProfile: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val scenarios = scenario("Get user profile").during(TotalRunDuration minutes) {
      exec(
        call()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
