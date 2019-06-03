package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import scala.concurrent.duration._

object PostCaseData extends PerformanceTestsConfig {

  // val EventId = "applyForGrant"
  val EventId = "appealCreated"
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
                                "event_token": """" + "${eventToken}" + """",
                                "data": {
                                	"appeal": {
                                		"receivedVia": "Online",
                                		"mrnDetails": {
                                			"dwpIssuingOffice": "DWP101010",
                                			"mrnDate": "2010-12-10",
                                			"mrnLateReason": "Not known",
                                			"mrnMissingReason": "No Reason"
                                		},
                                		"appellant": {
                                			"name": {
                                				"title": "Mr",
                                				"firstName": "Jooles",
                                				"middleName": "Rodriguez",
                                				"lastName": "Fernandez"
                                			},
                                			"identity": {
                                				"dob": "2011-11-11",
                                				"nino": "SGD83241"
                                			},
                                			"address": {
                                				"line1": "14 Avenue Road",
                                				"line2": "Revenue Lane",
                                				"line3": null,
                                				"town": "Chiswick",
                                				"county": "Middlesex",
                                				"postcode": "W4 5XR",
                                				"country": "UK"
                                			},
                                			"contact": {
                                				"phone": "7788990022",
                                				"mobile": "8899008899",
                                				"email": "noreason@everyreason.com"
                                			},
                                			"isAppointee": "Yes",
                                			"appointee": {
                                				"name": {
                                					"title": "Mrs",
                                					"firstName": "Davis",
                                					"middleName": "Novis",
                                					"lastName": "Data"
                                				},
                                				"identity": {
                                					"dob": "1990-01-01",
                                					"nino": "SH656437"
                                				},
                                				"address": {
                                					"line1": "14 Avenue Road",
                                					"line2": "Revenue Lane",
                                					"line3": null,
                                					"town": "Chiswick",
                                					"county": "Middlesex",
                                					"postcode": "W4 5XR",
                                					"country": "UK"
                                				},
                                				"contact": {
                                					"phone": "0909090909",
                                					"mobile": "0808080808",
                                					"email": "revenue@jas.com"
                                				}
                                			},
                                			"isAddressSameAsAppointee": "Yes"
                                		},
                                		"benefitType": {
                                			"code": "B0120202",
                                			"description": "Benefit"
                                		},
                                		"hearingType": "domiciliary",
                                		"hearingOptions": {
                                			"wantsToAttend": "Yes",
                                			"wantsSupport": "Yes",
                                			"languageInterpreter": "No",
                                			"arrangements": [
                                				"signLanguageInterpreter"
                                			],
                                			"scheduleHearing": "No",
                                			"other": "Other information",
                                			"signLanguageType": "wer"
                                		},
                                		"appealReasons": {
                                			"reasons": [],
                                			"otherReasons": null
                                		},
                                		"supporter": {
                                			"name": {
                                				"title": "Mr",
                                				"firstName": "Data",
                                				"middleName": "less",
                                				"lastName": "last name"
                                			},
                                			"contact": {
                                				"phone": "0909090909",
                                				"mobile": "070707070707",
                                				"email": "house@revenue.com"
                                			}
                                		},
                                		"rep": {
                                			"hasRepresentative": "No"
                                		},
                                		"signer": null
                                	},
                                	"regionalProcessingCenter": {
                                		"name": null,
                                		"address1": "14 Avenue Road",
                                		"address2": "Revenue Road",
                                		"address3": null,
                                		"address4": null,
                                		"postcode": "W4 5XR",
                                		"city": "Chiswick",
                                		"phoneNumber": "0902340982",
                                		"faxNumber": "203480284"
                                	},
                                	"panel": {
                                		"assignedTo": "Mr Judge",
                                		"medicalMember": "Medico",
                                		"disabilityQualifiedMember": "none"
                                	},
                                	"caseReference": "100000000",
                                	"caseCreated": "2010-11-10",
                                	"region": "London",
                                	"generatedNino": "S123456789",
                                	"generatedSurname": "Bakerloo",
                                	"generatedEmail": "bakerrloo@bakerloo.com",
                                	"generatedMobile": "0909090909",
                                	"generatedDOB": "1990-10-10",
                                	"evidencePresent": "Yes"
                                }
                         }""")


  def createCaseDatahttp() = {
    val token = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken()
    exec(
      //http("get create case event token")
      http("TX02_CCD_CreateCaseEndpoint_createcase_eventtoken")
        .get(CreateCaseTokenUrl)
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type", "application/json")
        .check(status.is(200), jsonPath("$.token").saveAs("eventToken"))
    ).exec(
      //http("create case data")
      http("TX02_CCD_CreateCaseEndpoint_createcasedata")
        .post(CreateCaseUrl)
        .body(EventBody).asJson
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type", "application/json")
        .check(status is 201)
    )
  }


  println("PostCaseData: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val createCaseData = scenario("Create Case Data").during(TotalRunDuration minutes) {
    exec(
      createCaseDatahttp()
    )
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }

  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

