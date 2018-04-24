package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

object GetUserProfile extends PerformanceTestsConfig {

  private val endpointUrl = caseDataUrl(config.getString("getUserProfileUrl"))
  println("endpoint url: " + endpointUrl)

  def call() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(endpointUrl)
    http("get user profile")
      .get(_ => endpointUrl)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  val scenarios = scenario("Get user profile")
    .exec(call())
}
