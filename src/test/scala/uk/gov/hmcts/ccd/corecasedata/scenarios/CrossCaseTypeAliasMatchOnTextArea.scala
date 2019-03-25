package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object CrossCaseTypeAliasMatchOnTextArea extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")

  println("Elastic Search - Cross Case Type - Match Query on TextArea field PT - URL: " + url)

  val CrossCaseTypeAliasMatchOnTextAreaPayload = StringBody(
                              """
                               {
                                "_source":["alias.aText", "alias.aNumber", "alias.aPhoneUK", "alias.aEmail", "alias.aMoneyGBP", "alias.aTextArea", "alias.aDcoument","alias.aYesOrNo"],
                                "query": {
                                "match": {
                                "alias.aTextArea" : "Performance Testing"
                                  }
                                 },
                                "size":20
                                }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)

    http("TX23_CCD_ElasticSearchEndpoint_CrossCaseType_MatchQuery_TextArea")
      .post(url)
      .queryParam("ctid", "ATCASETYPE1,ATCASETYPE2,ATCASETYPE3,ATCASETYPE4,AAT,CASETYPE2,CASETYPE3,CASETYPE4")
      .body(
        CrossCaseTypeAliasMatchOnTextAreaPayload ).asJSON
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("CrossCaseTypeAliasMatchOnTextArea: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val CrossCaseTypeAliasMatchOnTextAreaSCN = scenario("Elastic Search CrossCaseTypeAliasMatchOnTextArea Match Query on Text Area Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
