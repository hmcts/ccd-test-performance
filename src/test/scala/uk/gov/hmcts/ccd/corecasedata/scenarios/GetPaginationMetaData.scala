package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import scala.concurrent.duration._

object GetPaginationMetaData extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("getPaginationMetadataUrl")
  println("Retrieving pagination metadata: " + url)

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)
    http("TX06_CCD_GetPaginationMetadataData")
      .get(url)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("GetCaseData: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val getPaginationMetaData = scenario("Get Pagination Metadata").during(TotalRunDuration minutes) {
    exec(httpRequest())
  }
}
