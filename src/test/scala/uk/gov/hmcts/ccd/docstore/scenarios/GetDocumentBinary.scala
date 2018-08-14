package uk.gov.hmcts.ccd.docstore.scenarios

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object GetDocumentBinary extends PerformanceTestsConfig {


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Streams contents of the most recent Document Content Version associated with the Stored Document: " + DocStoreBashURL)

  val documentProviderSeq: RecordSeqFeederBuilder[String] = csv("listofcases").circular;

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)

  def getDocumentBinaryDataByDocumentIDHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(DocStoreBashURL)
    //http("get case data")
    feed(documentProviderSeq)
      .exec(http("TX22_DocStore_GetDocumentBinaryByDocumentID")
        .get(DocStoreBashURL +"/documents/${id}/binary")
        .header("ServiceAuthorization", s2sToken)
        //.header("Authorization", userToken)
        .header("Content-Type","application/json")
        .header("user-roles", "caseworker")
        .check(status in  (200)))
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  println("Get Document Binary  - Streams contents of the most recent Document Content Version associated with the Stored Document : Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val getDocumentBinaryDataByDocumentID = scenario("Get Document Binary by Document ID").during(TotalRunDuration minutes) {
    exec(
      getDocumentBinaryDataByDocumentIDHttp()
    )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

}

