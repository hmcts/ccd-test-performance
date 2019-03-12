package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.data._


import scala.concurrent.duration._



class CCDPTSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")


  val scenarios = List(
    //GetCaseDataV2.GetCaseDataV2Scenarios.inject(rampUsers(500) over(15 minutes))
   CreateCMCCaseData.CreateCMCCaseDataSCN.inject(rampUsers(1) over(1 minutes))
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