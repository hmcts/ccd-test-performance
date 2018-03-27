package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

object SearchCases extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("searchCases")
  println("url: " + url)

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken
    http("search cases")
      .get(url)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  val searchCases = scenario("search cases")
    .exec(httpRequest())
}
