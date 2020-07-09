package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESExactMatchONDate extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search exact match on date field PT - URL: " + url)

  val ESExactMatchONDateReqPayload = StringBody(
                              """
                                {
                                   "query":{
                                      "bool":{
                                         "filter":{
                                            "term":{
                                               "data.DateField":"2009-12-12"
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

    http("TX15_CCD_ElasticSearchEndpoint_ExactMatchONDateField")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ESExactMatchONDateReqPayload).asJson
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESExactMatchONDate: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESExactMatchONDateSCN = scenario("Elastic Search Exact Match ON Date Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
