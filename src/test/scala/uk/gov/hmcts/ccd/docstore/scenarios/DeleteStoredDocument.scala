package uk.gov.hmcts.ccd.docstore.scenarios

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.collection.mutable
import scala.concurrent.duration._
import scala.util.Random

object DeleteStoredDocument extends PerformanceTestsConfig {


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DeleteStoredDocument url - Deletes a Stored Document. : " + DocStoreBashURL)

  val DeleteDocumentProviderSeq: RecordSeqFeederBuilder[String] = csv("DELETE_listofcases.csv").circular;

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)

  def DeleteStoredDocumentHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(DocStoreBashURL)
    feed(DeleteDocumentProviderSeq)
      .exec(http("TX25_DocStore_DeleteStoredDocument")
       // .post("/documents")
        //.delete("/api/files/${DELETE_UUID}")
        .delete(DocStoreBashURL +"/documents/${DELETE_UUID}")
       // .header("Authorization", userToken)
        .header("ServiceAuthorization", s2sToken)
        .header("user-id", "auto.test.cnp@gmail.com")
        .header("Content-Type","application/json")
        .header("user-roles", "caseworker")
       // .queryParam("permanent", "true")
        .check(status in  (200)))
    /*  .exec { session =>
        println("fileId --------> " + session.get("fileId"))
        ids += session.get("fileId").as[String]
        session.remove("fileId")
      }*/
      .pause(MinThinkTime seconds,MaxThinkTime seconds)
  }


  println("DeleteStoredDocument - Deletes a Stored Document : Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val deleteStoredDocument = scenario("Delete Store Document").during(TotalRunDuration minutes) {
      exec(
        DeleteStoredDocumentHttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

}

