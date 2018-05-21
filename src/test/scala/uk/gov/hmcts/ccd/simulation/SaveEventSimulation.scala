package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._

class SaveEventSimulation extends CCDSimulation {

  val baseHttpUrl = config.getString("caseDataUrl")

  val scenarios = List(
     PostEvent.saveEventData.inject(
       atOnceUsers(1)
//       constantUsersPerSec(10) during(60 minutes) randomized
      )
  )

  setup()
}
