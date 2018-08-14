package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.corecasedata.scenarios._
//import uk.gov.hmcts.ccd.corecasedata.scenarios._
import uk.gov.hmcts.ccd.docstore.scenarios._

import scala.concurrent.duration._



class CCDPTSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("caseDataUrl")




  val scenarios = List(

    CreateDocument.createDocumentData.inject(rampUsers(1) over(1 minutes)),
    GetDocument.getDocumentDataByDocumentID.inject(rampUsers(1) over(1 minutes)),
    GetDocumentBinary.getDocumentBinaryDataByDocumentID.inject(rampUsers(1) over(1 minutes)),
    GetDocumentsAuditEntries.getDocumentsAuditEntryDataByDocumentID.inject(rampUsers(1) over(1 minutes)),
    GetDocumentsThumbnail.getDocumentThumbnailDataByDocumentID.inject(rampUsers(1) over(1 minutes)),
   // DeleteStoredDocument.deleteStoredDocument.inject(rampUsers(1) over(1 minutes))
  )

  setup()
}