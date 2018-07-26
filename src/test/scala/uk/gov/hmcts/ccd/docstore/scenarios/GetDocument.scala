package uk.gov.hmcts.ccd.docstore.scenarios

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.util.Random
import io.gatling.core.structure.ScenarioBuilder

object GetDocument extends PerformanceTestsConfig {


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Retrieves JSON representation of a Stored Document : " + DocStoreBashURL)

  val documentProviderSeq: RecordSeqFeederBuilder[String] = csv("listofcases").circular;

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)

  def getDocumentDataByDocumentIDHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(DocStoreBashURL)
    //http("get case data")
    feed(documentProviderSeq)
      .exec(http("TX21_DocStore_GetDocumentByDocumentID")
        .get(DocStoreBashURL +"/documents/${id}")
        .header("ServiceAuthorization", s2sToken)
        //.header("Authorization", userToken)
        .header("user-id", "auto.test.cnp@gmail.com")
        .header("Content-Type","application/json")
        .header("user-roles", "caseworker")
        .check(status in  (200)))
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  println("Get Document by Document ID - Retrieves JSON representation of a Stored Document : Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val getDocumentDataByDocumentID = scenario("Get Document by Document ID").during(TotalRunDuration minutes) {
    exec(
      getDocumentDataByDocumentIDHttp()
    )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

}

