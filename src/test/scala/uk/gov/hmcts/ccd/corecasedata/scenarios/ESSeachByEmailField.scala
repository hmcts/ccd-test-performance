package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESSeachByEmailField extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search by Email field PT - URL: " + url)

  val ESSeachByEmailFieldReqPayload = StringBody(
                              """
                                {
                                   "query":{
                                      "bool":{
                                         "filter":{
                                            "term":{
                                               "data.EmailField":"confirmation@confirmation.com"
                                            }
                                         }
                                      }
                                   }
                                }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()

    http("TX17_CCD_ElasticSearchEndpoint_SearchEmailField")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ESSeachByEmailFieldReqPayload).asJson
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESSeachByEmailField: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESSeachByEmailFieldSCN = scenario("Elastic Search by email Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
