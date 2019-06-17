package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import io.gatling.core.feeder.RecordSeqFeederBuilder
import scala.concurrent.duration._

object UpdateEthosPostEventDocumentAttached extends PerformanceTestsConfig {

  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

 // val EventId = "updateContactDetails"
  val EventId = "uploadDocument"
  val SaveEventUrl = caseDataUrl(config.getString("saveEventETHOSUploadDocsUrl"))
  val SaveEventTokenUrl = s"${SaveEventUrl.replaceAll("events", "")}event-triggers/$EventId/token"
  println("save event url: " + SaveEventUrl)
  println("save event token url: " + SaveEventTokenUrl)

  val EventBody = StringBody("""{"data": {"documentCollection": [{"id": null,"value": {"typeOfDocument": "Other","ownerDocument": "Case Document 123456 Attached","creationDate": "2018-02-01","shortDescription": "Case Document 123456 Attached for support","uploadedDocument": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}},{"id": null,"value": {"typeOfDocument": "Other","ownerDocument": "Case Document 123456 Attached","creationDate": "2018-02-01","shortDescription": "Case Document 123456 Attached for support","uploadedDocument": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}}]},"event": {"id": "uploadDocument","summary": "","description": ""},"event_token": """" + "${eventToken}" + """","ignore_warning": false}""")

  def saveEventDataHttp() = {

    val token = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()
    //val userToken = CcdTokenGenerator.generateWebUserToken(SaveEventUrl)
      feed(fileProviderRand)
      .exec(
         //http("save event token")
           http("TX04_CCD_SaveEventEndpoint_saveeventtoken")
           .get(SaveEventTokenUrl)
           .header("ServiceAuthorization", token)
           .header("Authorization", userToken)
           .header("Content-Type","application/json")
           .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
      )
      .exec(http("TX20_DocStore_PostDocument_${filename}")
      // .post("/documents")
      .post(DocStoreBashURL +"/documents")
      .header("Authorization", userToken)
      .header("ServiceAuthorization", token)
      .header("user-id", "giri.benadikar@hmcts.net")
      .bodyPart(
        RawFileBodyPart("files", "${filename}")
          .contentType("application/pdf")
          .fileName("${filename}")).asMultipartForm
      .formParam("classification", "PUBLIC")
      .check(status is 200, jsonPath("$._embedded.documents[0]._links.binary.href").saveAs("fileId"), regex("""http://(.+)/""").saveAs("DMURL"),regex("""/documents/(.+)"""").saveAs("Document_ID")))
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
      .exec(
       // http("save event")
         http("TX04_CCD_SaveEventEndpoint")
        .post(SaveEventUrl)
        .body(
          EventBody).asJSON
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )
  }

  println("PostEvent: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val UpdateEthosPostEventDocumentAttachedSCN = scenario("Save event").during(TotalRunDuration minutes) {
      exec(
          saveEventDataHttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

