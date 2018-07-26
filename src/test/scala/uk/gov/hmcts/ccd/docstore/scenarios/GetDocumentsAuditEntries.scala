package uk.gov.hmcts.ccd.docstore.scenarios

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object GetDocumentsAuditEntries extends PerformanceTestsConfig {


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Retrieves audits related to a Stored Document: " + DocStoreBashURL)

  val documentProviderSeq: RecordSeqFeederBuilder[String] = csv("listofcases").circular;

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)

  def GetDocumentsAuditEntryDataByDocumentIDHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(DocStoreBashURL)
    //http("get case data")
    feed(documentProviderSeq)
      .exec(http("TX23_DocStore_GetDocumentsAuditEntriesByDocumentID")
        .get(DocStoreBashURL +"/documents/${id}/auditEntries")
        .header("ServiceAuthorization", s2sToken)
        //.header("Authorization", userToken)
        .header("Content-Type","application/json")
        .header("user-roles", "caseworker")
        .check(status in  (200)))
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  println("Get Document's Audit Entry Data by Document ID - Retrieves audits related to a Stored Document : Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val getDocumentsAuditEntryDataByDocumentID = scenario("Get Documents Audit Entry Data by Document ID").during(TotalRunDuration minutes) {
    exec(
      GetDocumentsAuditEntryDataByDocumentIDHttp()
    )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

}

