package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.CcdTokenGenerator

object SearchCasesAPIGateway {

  val url = "/aggregated/caseworkers/538/jurisdictions/TEST/case-types/Benefit/cases"
  val userToken = CcdTokenGenerator.generateWebUserToken(url)

  def SearchCasesAPIGatewayhttp() = {
    http("get case data")
      .get(url)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  val searchCasesAPIGateway = scenario("Search cases via API Gateway")
                .exec(SearchCasesAPIGatewayhttp())
}
