package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._

import scala.concurrent.duration._

class GetUserProfileSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")

  val scenarios = List(
      GetUserProfile.scenarios.inject(
        constantUsersPerSec(RequestRateSec.getOrElse(1)) during(1 minutes)
      )
  )

  setup()
}
