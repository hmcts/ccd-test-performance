package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.CcdTokenGenerator

object SearchCasesAPIGateway {

  def SearchCasesAPIGatewayhttp() = {

    http("get case data")
      .get("/aggregated/caseworkers/538/jurisdictions/TEST/case-types/Benefit/cases")
      .header("Authorization", CcdTokenGenerator.generateWebUserToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  val searchCasesAPIGateway = scenario("Search cases via API Gateway")
                .exec(SearchCasesAPIGatewayhttp())
}
