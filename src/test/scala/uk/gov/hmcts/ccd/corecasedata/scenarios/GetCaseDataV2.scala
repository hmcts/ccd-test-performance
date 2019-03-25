package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import scala.concurrent.duration._

object GetCaseDataV2 extends PerformanceTestsConfig {

  private val getCaseUrl = caseDataUrl(config.getString("getCaseUrlV2"))
  println("Retrieving case with base url: " + getCaseUrl)

  def getCaseDataHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(getCaseUrl)
    //http("get case data")
    http("TX01_CCD_GetCaseDataEndpoint_getcasedataV2")
      .get(_ => url())
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .header("Accept","application/vnd.uk.gov.hmcts.ccd-data-store-api.ui-case-view.v2+json")
      .header("Experimental","true")
      .check(status in  (200))
  }

  def url(): String = {
    val result: String = if(!getCaseUrl.contains(":case_reference")) {
      getCaseUrl
    } else {
      resolveGetCaseUrl()
    }
//    println("Retrieving case: " + result)
    result
  }

  private def resolveGetCaseUrl(): String = {
    getCaseUrl.replace(":case_reference", pickRandomReference().replaceAll("-", ""))
  }

  println("GetCaseData: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)
  val GetCaseDataV2Scenarios = scenario("Get Case Data V2").during(TotalRunDuration minutes) {
      exec(
        getCaseDataHttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
