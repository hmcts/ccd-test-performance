package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.data._


import scala.concurrent.duration._



class CCDPTSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")


  val scenarios = List(
    //GetCaseDataV2.GetCaseDataV2Scenarios.inject(atOnceUsers(1))
    //PostCaseData.createCaseData.inject(atOnceUsers(1))
    //CreateEthosCaseData.CreateEthosCaseDataSCN.inject(atOnceUsers(1))
   CreateIACaseData.CreateIACaseDataSCN.inject(atOnceUsers(1))
   //CreateProbateGrantCaseData.CreateProbateGrantCaseDataSCN.inject(atOnceUsers(1))
    //CreateEthosCaseData.CreateEthosCaseDataSCN.inject(rampUsers(1) over(5 minutes)),
   //CreateCMCCaseData.CreateCMCCaseDataSCN.inject(atOnceUsers(1))
    //CreateSSCSCaseData.CreateSSCSCaseDataSCN.inject(atOnceUsers(1))
    //CreateDIVCaseData.CreateDIVCaseDataSCN.inject(atOnceUsers(1))
    //CreateFPLCaseData.CreateFPLCaseDataSCN.inject(atOnceUsers(1))
    //CreateProbateGrantMECaseData.CreateProbateGrantMECaseDataSCN.inject(rampUsers(1) over(5 minutes)),
    //CreateProbateCaveatCaseData.CreateProbateCaveatCaseDataSCN.inject(atOnceUsers(1))
  )


  setup()
}