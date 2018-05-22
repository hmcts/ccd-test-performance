package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._

import scala.concurrent.duration._



class CCDPTSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")


  val scenarios = List(
    GetCaseData.scenarios.inject(rampUsers(2) over(1 minutes)),
    PostCaseData.createCaseData.inject(rampUsers(50) over(5 minutes)),
    SearchCases.searchCases.inject(rampUsers(2) over(1 minutes)),
    PostEvent.saveEventData.inject(rampUsers(2) over(1 minutes)),
    GetUserProfile.scenarios.inject(rampUsers(2) over(1 minutes))
  )

  setup()
}
