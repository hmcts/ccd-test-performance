package uk.gov.hmcts.ccd.userprofile.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.util.Random


object PostUserProfile extends PerformanceTestsConfig {

  val url = s"$UserProfileUrl/user-profile/users"
  val userEmailprefix = Iterator.continually(
    Map("userEmailprefix" -> Random.nextInt(Integer.MAX_VALUE))
  )

  def createUsersHttp() = {
    val token = CcdTokenGenerator.generateDataStoreS2SToken()

    exec(
        http("create a user profile")
        .post(url)
        .body(StringBody("""{ "id":""""  + "${userEmailprefix}" +
                         """@perftest.com", "jurisdictions":[{"id":"DIVORCE"}],
                           |"work_basket_default_case_type":"Case1", "work_basket_default_jurisdiction":"DIVORCE","work_basket_default_state":"state1" }"""
                           .stripMargin)).asJson
        .header("ServiceAuthorization", token)
        .header("Authorization", CcdTokenGenerator.generateWebUserToken(url))
        .check(status is 201)
    )
  }


  val createUsers = scenario("Create A User Profile")
    .feed(userEmailprefix)
    .exec(createUsersHttp())

}

