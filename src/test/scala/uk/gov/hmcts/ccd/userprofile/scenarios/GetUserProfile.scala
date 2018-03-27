package uk.gov.hmcts.ccd.userprofile.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

object GetUserProfile extends PerformanceTestsConfig {

  def getAllUsersHttp() = {
    val token = CcdTokenGenerator.generateDataStoreS2SToken()

    http("get a user profile")
      .get(s"$UserProfileUrl/user-profile/users?uid=ccdwebdomain@gmail.com")
      .header("ServiceAuthorization", token)
      .header("Authorization", CcdTokenGenerator.generateWebUserToken)
      .check(status is 200)
  }

  val getAllUsers = scenario("Get A User Profile")
                .exec(getAllUsersHttp())
}
