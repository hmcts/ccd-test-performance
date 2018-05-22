package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios.{GetPaginationMetaData, _}

import scala.concurrent.duration._

class SearchCasesSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")

  val scenarios = List(
//    GetPaginationMetaData.getPaginationMetaData.inject(
//        constantUsersPerSec(1) during(1 minute)
//    )
      SearchCases.searchCases.inject(
        atOnceUsers(10)
        //constantUsersPerSec(10) during(1 minute)
      )
  )

  setup()
}
