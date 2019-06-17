package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object CrossCaseTypeAliasFuzzySearchOnText extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")

  println("Corss Case Type Elastic Search - Cross Case Type - Match Query on Text field PT - URL: " + url)

  val CrossCaseTypeAliasFuzzySearchOnTextPayload = StringBody(
                              """
                               {
                                "_source":["alias.aText", "alias.aNumber", "alias.aPhoneUK", "alias.aEmail", "alias.aMoneyGBP", "alias.aTextArea", "alias.aDcoument","alias.aYesOrNo"],
                                "query": {
                                "fuzzy": {
                                "alias.aText": "performance"
                                    }
                                },
                                "size":20
                                }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()

    http("TX25_CCD_ElasticSearchEndpoint_CrossCaseType_FuzzyQuery_Text")
      .post(url)
      .queryParam("ctid", "ATCASETYPE1,ATCASETYPE2,ATCASETYPE3,ATCASETYPE4,AAT,CASETYPE2,CASETYPE3,CASETYPE4")
      .body(
        CrossCaseTypeAliasFuzzySearchOnTextPayload ).asJson
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("CrossCaseTypeAliasFuzzySearchOnText: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val CrossCaseTypeAliasFuzzySearchOnTextSCN = scenario("Elastic Search CrossCaseTypeAliasFuzzySearchOnText Match Query on Text Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
