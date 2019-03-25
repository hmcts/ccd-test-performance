package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object ESSeachByPhoneUKField extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search by PhoneUK field PT - URL: " + url)

  val ESSeachByPhoneUKFieldReqPayload = StringBody(
                              """
                                {
                                  "query": {
                                    "bool" : {
                                      "filter": {
                                        "match" : { "data.PhoneUKField" : "02020002002" }
                                    }
                                    }
                                  }
                                }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)

    http("TX18_CCD_ElasticSearchEndpoint_SearchByPhoneUKField")
      .post(url)
      .queryParam("ctid", "AAT")
      .body(
        ESSeachByPhoneUKFieldReqPayload).asJSON
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("ESSeachByPhoneUKField: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val ESSeachByPhoneUKFieldSCN = scenario("Elastic Search by PhoneUK Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
