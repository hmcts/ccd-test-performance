package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._

class GetJurisdictionsSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDefinitionUrl")
  private val requestRate: Double = RequestRateSec.getOrElse(1)

  println(s"request per second:$requestRate")

  val scenarios = List(
      GetJurisdictions.scenarios.inject(
//        atOnceUsers(1)
        rampUsersPerSec(1) to 200 during(30)
        //constantUsersPerSec(requestRate) during(1 minutes)
      )
  )

  setup()
}
