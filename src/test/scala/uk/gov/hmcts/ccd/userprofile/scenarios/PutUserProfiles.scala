package uk.gov.hmcts.ccd.userprofile.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}


object PutUserProfiles extends PerformanceTestsConfig {

  def updateUsersHttp() = {
    val token = CcdTokenGenerator.generateDataStoreS2SToken()

    http("update a user profile")
      .put(s"$UserProfileUrl/user-profile/users")
      .body(StringBody(
        """[{ "id": "3@perftest.com", "jurisdictions":[{"id":"DIVORCE"}],"work_basket_default_case_type":"Case1",
          |"work_basket_default_jurisdiction":"DIVORCE","work_basket_default_state":"state1" }]""".stripMargin)).asJson
      .header("ServiceAuthorization", token)
      .check(status is 201)
  }

  val updateUsers = scenario("Update A User Profile")
    .exec(updateUsersHttp())

}
