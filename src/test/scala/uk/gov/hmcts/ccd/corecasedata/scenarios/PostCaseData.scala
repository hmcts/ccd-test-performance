package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import scala.concurrent.duration._

object PostCaseData extends PerformanceTestsConfig {

 // val EventId = "applyForGrant"
  val EventId = "CREATE"
  val CreateCaseUrl = caseDataUrl(config.getString("createCaseUrl"))
  val CreateCaseTokenUrl = s"${CreateCaseUrl.replaceAll("cases", "")}event-triggers/$EventId/token"
  println("create case url: " + CreateCaseUrl)
  println("create case token url: " + CreateCaseTokenUrl)

  val EventBody = StringBody(s"""
                           {
                             "event": {
                                 "description": "case automatically created by performance test",
                                 "id": "${EventId}",
                                 "summary": "Performance testing"
                             },
                             "event_token": """"  + "${eventToken}" +   """",
                             "data": {
                               "willDate": "2013-01-01",
                               "amountPaid": "120000",
                               "willExists": "Yes",
                               "declaration": "Declaration text does here and it is long and long that means it goes on ffor a very long time and then im going to cop and paste a few times to make it long. Hope ok with youDeclaration text does here and it is long and long that means it goes on ffor a very long time and then im going to cop and paste a few times to make it long. Hope ok with youDeclaration text does here and it is long and long that means it goes on ffor a very long time and then im going to cop and paste a few times to make it long. Hope ok with youDeclaration text does here and it is long and long that means it goes on ffor a very long time and then im going to cop and paste a few times to make it long. Hope ok with you",
                               "ihtNetValue": "120000",
                               "ihtGrossValue": "120000",
                               "legalStatement": "Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.   Legal Statement is just as long and again I am going to copy and paste a number time so I dont have ti type  too much.",
                               "deceasedAddress": "5 Death Road\nFake Town",
                               "deceasedSurname": "Plummer",
                               "willHasCodicils": "No",
                               "willIsAnOriginal": "Yes",
                                 "deceasedForenames": "Bella",
                               "numberOfExecutors": "3",
                               "extraCopiesOfGrant": "0",
                               "ihtReferenceNumber": "250",
                               "numberOfApplicants": "0",
                               "deceasedDateOfBirth": "1970-01-01",
                               "deceasedDateOfDeath": "2018-02-02",
                               "executorsNotApplying": [
                                 {
                                   "id": "ef68c99a-ea40-4000-af62-974081f2cd02",
                                   "value": {
                                     "notApplyingExecutorName": "Mr Other Exec Plummer",
                                     "notApplyingExecutorReason": "DiedBefore",
                                     "notApplyingExecutorNotified": "No",
                                     "notApplyingExecutorNameOnWill": "Mr Other Exec Plummer",
                                     "notApplyingExecutorNameDifferenceComment": "None"
                                   }
                                 },
                                 {
                                   "id": "35110c98-7fcf-4d4c-8c57-b8810407443d",
                                   "value": {
                                     "notApplyingExecutorName": "Mr Third Exec Plummer",
                                     "notApplyingExecutorReason": "DiedAfter",
                                     "notApplyingExecutorNotified": "Yes",
                                     "notApplyingExecutorNameOnWill": "Mr Third Exec Plummer",
                                     "notApplyingExecutorNameDifferenceComment": "None"
                                   }
                                 }
                               ],
                               "outsideUKGrantCopies": "0",
                               "willNumberOfCodicils": "1",
                               "deceasedAliasNameList": [
                                 {
                                   "id": "8c46923a-8b1a-407d-aae1-ee85c297d0ca",
                                   "value": {
                                     "LastName": "Plummer",
                                     "Forenames": "Alias Bella",
                                     "AppearOnGrant": "Yes"
                                   }
                                 },
                                 {
                                   "id": "282c0413-4247-4f30-840c-06fc44a261ed",
                                   "value": {
                                     "LastName": "Plummer",
                                     "Forenames": "B",
                                     "AppearOnGrant": "No"
                                   }
                                 }
                               ],
                               "willLatestCodicilDate": "2016-01-01",
                               "ihtFormCompletedOnline": "Yes",
                               "paymentReferenceNumber": "PaymentRef1111111",
                               "primaryApplicantAddress": "5 Fake road,\nFake Town",
                               "primaryApplicantSurname": "Plummer",
                               "willLatestCodicilHasDate": "No",
                               "primaryApplicantForenames": "Rex",
                               "deceasedDomicileInEngWales": "",
                               "primaryApplicantIsExecutor": "Yes",
                               "primaryApplicantPhoneNumber": "080909090909",
                               "primaryApplicantEmailAddress": "fake@gmail.com"
                             }
                           }
                         """)


  def createCaseDatahttp() = {
    val token = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(CreateCaseUrl)
    exec(
         //http("get create case event token")
           http("TX02_CCD_CreateCaseEndpoint_createcase_eventtoken")
           .get(CreateCaseTokenUrl)
           .header("ServiceAuthorization", token)
           .header("Authorization", userToken)
           .header("Content-Type","application/json")
           .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
      ).exec(
        //http("create case data")
        http("TX02_CCD_CreateCaseEndpoint_createcasedata")
        .post(CreateCaseUrl)
        .body(
          EventBody).asJSON
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )
  }


  println("PostCaseData: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val   createCaseData = scenario("Create Case Data").during(TotalRunDuration minutes) {
      exec(
          createCaseDatahttp()
      )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

