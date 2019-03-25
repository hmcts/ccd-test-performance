package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ProdDIVORCESearchD8caseReference extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("prodDIVORCED8caseReference")
  println("url: " + url)

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)
    //http("search cases")
    http("TX03_CCD_DIVORCE_SearchCaseEndpoint_D8caseReference")
      .get(url)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ProdDIVORCESearchD8caseReference: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ProdDIVORCESearchD8caseReferenceSCN = scenario("ProdDIVORCESearchD8caseReference search cases").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
