package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._
import uk.gov.hmcts.ccd.casedefinition.scenarios.ImportDefinition
import uk.gov.hmcts.ccd.util.Headers

class ImportCaseDefinitionSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDefinitionUrl")

  val scenarios = List(

     ImportDefinition.importDefinition.inject(
        atOnceUsers(1)
       //concurrent imports are not supported at the moment and will cause errors
//        rampUsersPerSec(1) to 1 during(3 seconds)
      )

  )

  setup()
}