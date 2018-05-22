package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import scala.concurrent.duration._

object PostEvent extends PerformanceTestsConfig {

 // val EventId = "updateContactDetails"
  val EventId = "UPDATE"
  val SaveEventUrl = caseDataUrl(config.getString("saveEventUrl"))
  val SaveEventTokenUrl = s"${SaveEventUrl.replaceAll("events", "")}event-triggers/$EventId/token"
  println("save event url: " + SaveEventUrl)
  println("save event token url: " + SaveEventTokenUrl)

  val EventBody = StringBody(s"""
                           {
                             "event": {
                                 "description": "test update case",
                                 "id": "${EventId}",
                                 "summary": "Performance testing"
                             },
                             "event_token": """"  + "${eventToken}" +   """",
                             "data": {}
                           }
                         """)


  def saveEventDataHttp() = {
    val token = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(SaveEventUrl)
    exec(
         //http("save event token")
           http("TX04_CCD_SaveEventEndpoint_saveeventtoken")
           .get(SaveEventTokenUrl)
           .header("ServiceAuthorization", token)
           .header("Authorization", userToken)
           .header("Content-Type","application/json")
           .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
      ).exec(
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

  val saveEventData = scenario("Save event").during(TotalRunDuration minutes) {
      exec(
          saveEventDataHttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

