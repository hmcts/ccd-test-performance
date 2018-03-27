package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

object PostEvent extends PerformanceTestsConfig {

  val EventId = "updateContactDetails"
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
    val userToken = CcdTokenGenerator.generateWebUserToken
    exec(
         http("save event token")
           .get(SaveEventTokenUrl)
           .header("ServiceAuthorization", token)
           .header("Authorization", userToken)
           .header("Content-Type","application/json")
           .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
      ).exec(
        http("save event")
        .post(SaveEventUrl)
        .body(
          EventBody).asJSON
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )
  }


  val saveEventData = scenario("Save event")
    .exec(saveEventDataHttp())

}

