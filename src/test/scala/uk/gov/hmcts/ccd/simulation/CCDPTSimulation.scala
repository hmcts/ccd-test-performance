package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.data._


import scala.concurrent.duration._



class CCDPTSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")


  val scenarios = List(
    //CreateEthosCaseData.CreateEthosCaseDataSCN.inject(rampUsers(1) over(5 minutes))
   //CreateIACaseData.CreateIACaseDataSCN.inject(rampUsers(1) over(5 minutes)),
   //CreateProbateGrantCaseData.CreateProbateGrantCaseDataSCN.inject(rampUsers(1) over(5 minutes)),
    //CreateEthosCaseData.CreateEthosCaseDataSCN.inject(rampUsers(1) over(5 minutes)),
   //CreateCMCCaseData.CreateCMCCaseDataSCN.inject(rampUsers(1) over(5 minutes)),
    //CreateSSCSCaseData.CreateSSCSCaseDataSCN.inject(rampUsers(1) over(1 minutes)),
    CreateDIVCaseData.CreateDIVCaseDataSCN.inject(rampUsers(1) over(1 minutes)),
    //CreateFPLCaseData.CreateFPLCaseDataSCN.inject(rampUsers(1) over(5 minutes))
    //CreateProbateGrantMECaseData.CreateProbateGrantMECaseDataSCN.inject(rampUsers(1) over(5 minutes)),
   // CreateProbateCaveatCaseData.CreateProbateCaveatCaseDataSCN.inject(rampUsers(1) over(1 minutes))
  )


  setup()
}