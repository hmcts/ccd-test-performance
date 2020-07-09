package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESMatchAllCases extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search Match All & return 50 PT - URL: " + url)

  val ReqPayload = StringBody(
                              """
                              {
                                  "query": {
                                      "match_all": {}
                                  },
                                  "size": 50
                              }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()

    http("TX11_CCD_ElasticSearchEndpoint_MatchAll_Return50Cases")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ReqPayload).asJson
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESMatchAllCases: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESMatchAll_Return50Cases = scenario("Elastic Search Match all and return 50 cases").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
