package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESSeachONTextArea extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search on text area field PT - URL: " + url)

  val ESSeachONTextAreaReqPayload = StringBody(
                              """
                                {
                                   "query":{
                                      "match_phrase_prefix":{
                                         "data.TextAreaField":"Performance Testing "
                                      }
                                   },
                                   "sort":[
                                      {
                                         "last_modified":"desc"
                                      },
                                      "_score"
                                   ]
                                }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)

    http("TX14_CCD_ElasticSearchEndpoint_SearchONTextAreaField")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ESSeachONTextAreaReqPayload).asJson
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESSeachONTextArea: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESSeachONTextAreaSCN = scenario("Elastic Search on TextArea Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
