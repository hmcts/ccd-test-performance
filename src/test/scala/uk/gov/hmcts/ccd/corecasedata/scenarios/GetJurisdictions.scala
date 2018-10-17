package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object GetJurisdictions extends PerformanceTestsConfig {

  private val getJurisdictionsUrl = caseDefinitionUrl(config.getString("getJurisdictionsUrl"))
  private val endpointUrl = caseDataUrl(config.getString("getUserProfileUrl"))
  println("Retrieving case with base url: " + getJurisdictionsUrl)

  def getCaseDataHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(endpointUrl)
    //http("get case data")
    http("TX01_CCD_GetCaseDataEndpoint_getcasedata")
      .get(getJurisdictionsUrl)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("GetJurisdictions: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)
  val scenarios = scenario("Get Jurisdictions").during(TotalRunDuration minutes) {
      exec(
        getCaseDataHttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
