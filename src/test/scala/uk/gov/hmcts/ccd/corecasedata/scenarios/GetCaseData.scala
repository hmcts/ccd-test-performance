package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

object GetCaseData extends PerformanceTestsConfig {

  private val getCaseUrl = caseDataUrl(config.getString("getCaseUrl"))
  println("Retrieving case with base url: " + getCaseUrl)

  def getCaseDataHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(getCaseUrl)
    http("get case data")
      .get(_ => url())
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
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

  val scenarios = scenario("Get Case Data")
    .exec(getCaseDataHttp())
}
