package uk.gov.hmcts.ccd.util

import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator

object CcdTokenGenerator extends PerformanceTestsConfig with SpringApplicationContext {

  val TOKEN_LEASE_URL = s"$UserAuthUrl/testing-support/lease"

  var dataStoreS2STokenGenerator = applicationContext.getBean("dataStoreS2STokenGenerator").asInstanceOf[AuthTokenGenerator]
  var gatewayS2STokenGenerator = applicationContext.getBean("gatewayS2STokenGenerator").asInstanceOf[AuthTokenGenerator]

  def generateWebUserToken(url: String): String = generateUserToken(UserCcdId, roleFor(url))

  def generateImportUserToken: String = generateUserToken(UserImportId, "ccd-import")

  protected def generateUserToken(id: Integer, role: String): String = {
    val url = TOKEN_LEASE_URL + String.format("?id=%s&role=%s", id, role)
    val restTemplate = new RestTemplate
    val response = restTemplate.exchange(url, HttpMethod.POST, null, classOf[String])
    val token = response.getBody
    println(s"generated user token $token")
    "Bearer " + token
  }

  def generateDataStoreS2SToken(): String = {
    val token = dataStoreS2STokenGenerator.generate()
    println(s"generated s2s datastore token: $token")
    token
  }

  def generateGatewayS2SToken(): String = {
    val token = gatewayS2STokenGenerator.generate()
    println(s"generated s2s gateway token: $token")
    token
  }

  private def roleFor(url: String) = {
    /*val result = "caseworker,caseworker-divorce,caseworker-divorce-courtadmin,caseworker-divorce-financialremedy,caseworker-divorce-financialremedy-courtadmin,caseworker-probate,caseworker-probate-issuer,caseworker-probate-examiner,caseworker-probate-authoriser,caseworker-sscs,caseworker-cmc,caseworker-test,caseworker-test-manager,caseworker-test-junior,caseworker-test-senior,caseworker-test-public,caseworker-test-private,caseworker-test-protected,caseworker-reference-data,caseworker-publiclaw,caseworker-publiclaw-cafcass,caseworker-privatelaw,caseworker-privatelaw-courtadmin,caseworker-privatelaw-casecreator,caseworker-privatelaw-cafcass,caseworker,payments,caseworker-loa1,caseworker-divorce-loa1,caseworker-divorce-courtadmin-loa1,caseworker-divorce-financialremedy-loa1,caseworker-divorce-financialremedy-courtadmin-loa1,caseworker-probate-loa1,caseworker-probate-issuer-loa1,caseworker-probate-examiner-loa1,caseworker-probate-authoriser-loa1,caseworker-sscs-loa1,caseworker-cmc-loa1,caseworker-test-loa1,caseworker-test-manager-loa1,caseworker-test-junior-loa1,caseworker-test-senior-loa1,caseworker-test-public-loa1,caseworker-test-private-loa1,caseworker-test-protected-loa1,caseworker-reference-data-loa1,caseworker-publiclaw-loa1,caseworker-publiclaw-cafcass-loa1,caseworker-privatelaw-loa1,caseworker-privatelaw-courtadmin-loa1,caseworker-privatelaw-casecreator-loa1,caseworker-privatelaw-cafcass-loa1,caseworker-loa1,payments-loa1"
    */
    val result = "caseworker" + parseJurisdiction(url).map(j => s"-$j").getOrElse("")
    println(s"role used for user token generation: $result")
    result
  }

  private def parseJurisdiction(url: String) = {
    if (!url.contains("jurisdictions")) {
      None
    } else {
      val jurisdiction = url.split("jurisdictions/")(1).split("/")(0)
      Some(jurisdiction.toLowerCase)
    }
  }

}
