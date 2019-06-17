package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ProdSSCSSearchWorkBasketIssue extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("prodSSCSWorkBasket")
  println("url: " + url)

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()
    //http("search cases")
    http("TX01_CCD_SSCS_SearchCaseEndpoint_WorkBasket_searchcases")
      .get(url)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ProdSSCSSearchWorkBasketIssue: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ProdSSCSSearchWorkBasketIssueSCN = scenario("ProdSSCSSearchWorkBasketIssue search cases").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
