package uk.gov.hmcts.ccd.docstore.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.collection.mutable
import scala.util.Random
import io.gatling.core.feeder.RecordSeqFeederBuilder

object CreateDocument extends PerformanceTestsConfig {


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)


  val ids: mutable.MutableList[String] = mutable.MutableList[String]()
  val randomNum: Random.type = scala.util.Random
  val tempVal: String = ""
  private val times: Int = 1

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random
  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)

  def CreateDocumentHttp() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(DocStoreBashURL)
    feed(fileProviderRand)
      .exec(http("TX20_DocStore_PostDocument")
       // .post("/documents")
        .post(DocStoreBashURL +"/documents")
        //.header("Authorization", userToken)
        .header("ServiceAuthorization", s2sToken)
        .header("user-id", "auto.test.cnp@gmail.com")
        .bodyPart(
          RawFileBodyPart("files", "${filename}")
            .contentType("application/pdf")
            .fileName("${filename}")).asMultipartForm
        .formParam("classification", "PUBLIC")
        .check(status is 200, jsonPath("$._embedded.documents[0]._links.binary.href").saveAs("fileId")))
    /*  .exec { session =>
        println("fileId --------> " + session.get("fileId"))
        ids += session.get("fileId").as[String]
        session.remove("fileId")
      }*/
      .pause(MinThinkTime seconds,MaxThinkTime seconds)
  }


  println("CreateDocument - Creates a list of Stored Documents by uploading a list of binary/text files : Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val createDocumentData = scenario("Create Document").during(TotalRunDuration minutes) {
      exec(
        CreateDocumentHttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

}

