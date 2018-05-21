package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef._
import uk.gov.hmcts.ccd.userprofile.scenarios._

class UserProfileSimulation extends CCDSimulation {

  val baseHttpUrl: String = config.getString("userProfileUrl")

  val scenarios = List(
      GetUserProfile.getAllUsers.inject(
        atOnceUsers(1)
      )
//      PostUserProfile.createUsers.inject(
//          atOnceUsers(1),
//          rampUsersPerSec(1) to 5 during(5 seconds)
//      ),
//      PutUserProfiles.updateUsers.inject(
//         atOnceUsers(1),
//      )
  )

  setup()
}