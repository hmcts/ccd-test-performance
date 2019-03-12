package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object CrossCaseTypeAliasfuzzyYesOrNo extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")
  println("Elastic Search - Cross Case Type Fuzzy Search on YesORNo field PT - URL: " + url)

  val CrossCaseTypeAliasFuzzyYesOrNoPayload = StringBody(
                              """
                               {
                                "_source":["alias.aText", "alias.aNumber", "alias.aPhoneUK", "alias.aEmail", "alias.aMoneyGBP", "alias.aTextArea", "alias.aDcoument","alias.aYesOrNo"],
                                "query": {
                                "fuzzy": {
                                "alias.aYesOrNo" : "Yes"
                                  }
                                 },
                                "size":20
                                }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)

    http("TX21_CCD_ElasticSearchEndpoint_CrossCaseType_FuzzyQuery_YesORNoField")
      .post(url)
      .queryParam("ctid", "ATCASETYPE1,ATCASETYPE2,ATCASETYPE3,ATCASETYPE4,AAT,CASETYPE2,CASETYPE3,CASETYPE4")
      .body(
        CrossCaseTypeAliasFuzzyYesOrNoPayload).asJSON
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("CrossCaseTypeAliasfuzzyYesOrNo: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val CrossCaseTypeAliasfuzzyYesOrNoSCN = scenario("Elastic Search CrossCaseTypeAliasfuzzyYesOrNo Fuzzy search on YesORNo Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
