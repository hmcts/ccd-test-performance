package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.data._


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

ES Load Testing
 minthinktime = 7
  maxthinktime = 9
  minWaitForNextIteration = 10
  maxWaitForNextIteration = 12
  totalDuration = 60

  ES Case count in CCD - 200685

*/

  val scenarios = List(
    //GetCaseDataV2.GetCaseDataV2Scenarios.inject(rampUsers(500) over(15 minutes))
    CreateProbateGrantCaseData.CreateProbateGrantCaseDataSCN.inject(rampUsers(1) over(1 minutes))
   //CreateCMCCaseData.CreateCMCCaseDataSCN.inject(rampUsers(100) over(5 minutes)),
   //CreateSSCSCaseData.CreateSSCSCaseDataSCN.inject(rampUsers(1) over(1 minutes)),
   //CreateDIVCaseData.CreateDIVCaseDataSCN.inject(rampUsers(1) over(1 minutes)),
    //ProdDIVORCESearchD8caseReference.ProdDIVORCESearchD8caseReferenceSCN.inject(rampUsers(300) over(5 minutes)),
    //ProdDIVORCESearchD8caseReferencePagination.ProdDIVORCESearchD8caseReferencePaginationSCN.inject(rampUsers(300) over(5 minutes)),
    //ProdSSCSSearchPagination.ProdSSCSSearchPaginationSCN.inject(rampUsers(300) over(5 minutes)),
   //ProdSSCSSearchWorkBasketIssue.ProdSSCSSearchWorkBasketIssueSCN.inject(rampUsers(300) over(5 minutes))
    //  PostCaseData.createCaseData.inject(rampUsers(1) over(1 minutes)),
   //   CrossCaseTypeAliasfuzzyYesOrNo.CrossCaseTypeAliasfuzzyYesOrNoSCN.inject(rampUsers(1) over(1 minutes)),
    //  CrossCaseTypeAliasMatchYesOrNo.CrossCaseTypeAliasMatchYesOrNoSCN.inject(rampUsers(1) over(1 minutes)),
    //  CrossCaseTypeAliasMatchOnTextArea.CrossCaseTypeAliasMatchOnTextAreaSCN.inject(rampUsers(1) over(1 minutes)),
    //  CrossCaseTypeAliasMatchOnText.CrossCaseTypeAliasMatchOnTextSCN.inject(rampUsers(1) over(1 minutes)),
   // CrossCaseTypeAliasFuzzySearchOnText.CrossCaseTypeAliasFuzzySearchOnTextSCN.inject(rampUsers(1) over(1 minutes))
    //  ESMatchAllCases.ESMatchAll_Return50Cases.inject(rampUsers(1) over(1 minutes)),
    //  ESExactMatchYesOrNo.ESExactMatchYesOrNoSCN.inject(rampUsers(1) over(1 minutes)),
     // ESSeachONTextArea.ESSeachONTextAreaSCN.inject(rampUsers(1) over(1 minutes)),
     // ESSeachONReferenceMetaData.ESSeachONReferenceMetaDataSCN.inject(rampUsers(1) over(1 minutes))
   // ESStdTest.ESStdTEST.inject(rampUsers(1) over(5 minutes))
   // GetCaseData.scenarios.inject(rampUsers(1) over(1 minutes)),
  //  PostCaseData.createCaseData.inject(rampUsers(1) over(1 minutes)),
  //  SearchCases.searchCases.inject(rampUsers(1) over(15 minutes)),
  //  SearchInputDetails.getSearchInputDetails.inject(rampUsers(1) over(1 minutes)),
   // WorkBasketInputDetails.getWorkbasketInputDetails.inject(rampUsers(1) over(15 minutes)),
   // GetPaginationMetaData.getPaginationMetaData.inject(rampUsers(1) over(1 minutes)),
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