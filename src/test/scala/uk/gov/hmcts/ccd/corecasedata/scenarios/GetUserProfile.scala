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
    val userToken = CcdTokenGenerator.generateWebUserToken(endpointUrl)
    //http("get user profile")
    http("TX05_CCD_GetUserProfileEndpoint_getuserprofile")
      .get(_ => endpointUrl)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("GetUserProfile: Minimum think time " + minThinkTime + " Maximum think time " + maxThinkTime)

  val scenarios = scenario("Get user profile").during(totalRunDuration minutes) {
      exec(
        call()
      )
      .pause(minThinkTime seconds, maxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
