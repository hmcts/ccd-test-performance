package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESSeachONFixedList extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search on fixed list field PT - URL: " + url)

  val ESSeachONFixedListReqPayload = StringBody(
                              """
                                {
                                   "query":{
                                      "bool":{
                                         "filter":{
                                            "term":{
                                               "data.FixedListField":"value1"
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

    http("TX16_CCD_ElasticSearchEndpoint_SearchONFixedListField")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ESSeachONFixedListReqPayload).asJSON
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESSeachONFixedList: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESSeachONFixedListSCN = scenario("Elastic Search on Fixedlist Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
