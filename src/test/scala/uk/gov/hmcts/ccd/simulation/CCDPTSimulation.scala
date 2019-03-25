package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.data._


import scala.concurrent.duration._



class CCDPTSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")


  val scenarios = List(
   //CreateCMCCaseData.CreateCMCCaseDataSCN.inject(rampUsers(100) over(5 minutes)),
   //CreateSSCSCaseData.CreateSSCSCaseDataSCN.inject(rampUsers(1) over(1 minutes)),
  CreateDIVCaseData.CreateDIVCaseDataSCN.inject(rampUsers(1) over(1 minutes))
   //ProdSSCSSearchWorkBasketIssue.ProdSSCSSearchWorkBasketIssueSCN.inject(rampUsers(300) over(5 minutes))
  )


  setup()
}