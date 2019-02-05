package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.casedefinition.scenarios._
import uk.gov.hmcts.ccd.corecasedata.scenarios._


import scala.concurrent.duration._



class CCDPTSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")


  /*
   val scenarios = List(
      GetCaseData.scenarios.inject(
        atOnceUsers(1)),
      PostCaseData.createCaseData.inject(
        atOnceUsers(1)),
      SearchCases.searchCases.inject(
        atOnceUsers(10)),
      PostEvent.saveEventData.inject(
        atOnceUsers(1)),
      GetUserProfile.scenarios.inject(
        atOnceUsers(1))
    )
   */


  /*
  User Concurrency Stress Test
   minthinktime = 2
  maxthinktime = 3
  minWaitForNextIteration = 4
  maxWaitForNextIteration = 5
  totalDuration = 1000



  val scenarios = List(
    GetCaseData.scenarios.inject(splitUsers(1000) into(rampUsers(10) over(5 minutes)) separatedBy(5 minutes)),
    PostCaseData.createCaseData.inject(splitUsers(1000) into(rampUsers(10) over(5 minutes)) separatedBy(5 minutes)),
    SearchCases.searchCases.inject(splitUsers(1000) into(rampUsers(10) over(5 minutes)) separatedBy(5 minutes)),
    PostEvent.saveEventData.inject(splitUsers(1000) into(rampUsers(10) over(5 minutes)) separatedBy(5 minutes)),
    GetUserProfile.scenarios.inject(splitUsers(1000) into(rampUsers(10) over(5 minutes)) separatedBy(5 minutes))
  )
*/



  /* Single User Test
   minthinktime = 2
    maxthinktime = 3
    minWaitForNextIteration = 4
    maxWaitForNextIteration = 5
    totalDuration = 2


*/

  val scenarios = List(
    ESMatchAllCases.ESMatchAll_Return50Cases.inject(atOnceUsers(1))
  )



  /*
  CCD Stress Test - RPS

  jurisdiction = "autotest1"
  maxSimulationDurationMinutes = 99999
  maxResponseTime = 20000
  meanResponseTime = 1000
  minthinktime = 1
  maxthinktime = 2
  minWaitForNextIteration = 1
  maxWaitForNextIteration = 2
  totalDuration = 1000
  reachRPSTarget = 150
  reachRPSDuration = 10
  reachRPSHoldForDuration = 60
  jumptoRPSTarget = 200
  jumptoRPSDuration = 900

  Data Volume
  reachRPSTarget = 25
  jumptoRPSTarget = 50



  val scenarios = List(
   // GetCaseData.scenarios.inject(rampUsers(2) over(1 minutes)),
    CreateDocument.createDocumentData.inject(rampUsers(10) over(5 minutes))
   // SearchCases.searchCases.inject(rampUsers(2) over(1 minutes)),
  //  PostEvent.saveEventData.inject(rampUsers(2) over(1 minutes)),
   // GetUserProfile.scenarios.inject(rampUsers(2) over(1 minutes))
  )

*/
  setup()
}
