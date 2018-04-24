package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._

import scala.concurrent.duration._

class GetCasesSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")
  private val requestRate: Double = RequestRateSec.getOrElse(1)

  println(s"request per second:$requestRate")

  val scenarios = List(
      GetCaseData.scenarios.inject(
//        rampUsersPerSec(1) to 10 during(5 seconds)
        constantUsersPerSec(requestRate) during(1 minutes)
      )
  )

  setup()
}
