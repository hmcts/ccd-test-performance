package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object CrossCaseTypeAliasMatchYesOrNo extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("ESSearch")

  println("Elastic Search - Cross Case Type - Match Query on YesORNo field PT - URL: " + url)

  val CrossCaseTypeAliasMatchYesOrNoPayload = StringBody(
                              """
                               {
                                "_source":["alias.aText", "alias.aNumber", "alias.aPhoneUK", "alias.aEmail", "alias.aMoneyGBP", "alias.aTextArea", "alias.aDcoument","alias.aYesOrNo"],
                                "query": {
                                "match": {
                                "alias.aYesOrNo" : "Yes"
                                  }
                                 },
                                "size":20
                                }
                              """
                              )

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()

    http("TX22_CCD_ElasticSearchEndpoint_CrossCaseType_MatchQuery_YesORNoField")
      .post(url)
      .queryParam("ctid", "ATCASETYPE1,ATCASETYPE2,ATCASETYPE3,ATCASETYPE4,AAT,CASETYPE2,CASETYPE3,CASETYPE4")
      .body(
        CrossCaseTypeAliasMatchYesOrNoPayload).asJson
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("CrossCaseTypeAliasMatchYesOrNo: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val CrossCaseTypeAliasMatchYesOrNoSCN = scenario("Elastic Search CrossCaseTypeAliasMatchYesOrNo Match Query on YesORNo Field").during(TotalRunDuration minutes) {
      exec(
          httpRequest()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}
