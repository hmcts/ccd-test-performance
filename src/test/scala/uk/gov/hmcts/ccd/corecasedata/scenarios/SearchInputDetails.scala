package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object SearchInputDetails extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("searchInputDetails")
  println("Retrieving searchInputDetails URL : " + url)

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)
    http("TX07_CCD_SearchInputDetails_searchInputDetails")
      .get(url)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("SearchInputDetails: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val getSearchInputDetails = scenario("Search Input Details").during(TotalRunDuration minutes) {
    exec(
      httpRequest()
    )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }
}
