package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESExactMatchYesOrNo extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search Exact Match on YesORNo field PT - URL: " + url)

  val ESExactMatchYesOrNoReqPayload = StringBody(
                              """
                                {
                                  "query":{
                                        "bool":{
                                         "filter":{
                                           "match":{
                                             "data.YesOrNoField":"Yes"
                                             }
                                           }
                                          }
                                      }
                                  }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)

    http("TX12_CCD_ElasticSearchEndpoint_ExactMatch_YesORNoField")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ESExactMatchYesOrNoReqPayload).asJSON
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESExactMatchYesOrNo: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESExactMatchYesOrNoSCN = scenario("Elastic Search Exact Match on YesORNo Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
