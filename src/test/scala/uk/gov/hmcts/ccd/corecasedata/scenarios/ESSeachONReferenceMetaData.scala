package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESSeachONReferenceMetaData extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search on reference metadata PT - URL: " + url)

  val ESSearchONReferenceMetaDataReqPayload = StringBody(
                              """
                                {
                                   "query":{
                                      "bool":{
                                         "filter":{
                                            "wildcard":{
                                               "reference":"1537*"
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

    http("TX13_CCD_ElasticSearchEndpoint_ReferenceMetaDataSearch")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ESSearchONReferenceMetaDataReqPayload).asJSON
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESSeachONReferenceMetaData: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESSeachONReferenceMetaDataSCN = scenario("Elastic Search on Reference Metadata Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
