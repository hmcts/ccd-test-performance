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
    // val result = "caseworker-cmc,caseworker,caseworker-cmc-loa1,caseworker-loa1" + parseJurisdiction(url).map(j => s"-$j").getOrElse("")
    val result = "caseworker,caseworker-divorce,caseworker-probate,caseworker-autotest1,caseworker-sscs,caseworker-divorce-courtadmin,caseworker-loa1,caseworker-divorce-loa1,caseworker-autotest1-loa1,caseworker-sscs-loa1,caseworker-divorce-courtadmin-loa1" + parseJurisdiction(url).map(j => s"-$j").getOrElse("")
   //val result = "caseworker,caseworker-autotest1,caseworker-autotest1-junior,caseworker-autotest1-senior,caseworker-autotest1-manager,caseworker-autotest2,caseworker-loa1,caseworker-autotest1-loa1,caseworker-autotest1-junior-loa1,caseworker-autotest1-senior-loa1,caseworker-autotest1-manager-loa1,caseworker-autotest2-loa1" + parseJurisdiction(url).map(j => s"-$j").getOrElse("")
    //val result = "caseworker,caseworker-autotest1,caseworker-autotest1-solicitor,,caseworker-autotest2,caseworker-autotest2-solicitor,caseworker,caseworker-loa1,caseworker-autotest1-loa1,caseworker-autotest1-solicitor-loa1,caseworker-loa1,caseworker,caseworker-autotest1,caseworker-autotest1-solicitor,,caseworker-autotest2,caseworker-autotest2-solicitor,caseworker,caseworker-loa1,caseworker-autotest1-loa1,caseworker-autotest1-solicitor-loa1,caseworker-loa1-loa0" + parseJurisdiction(url).map(j => s"-$j").getOrElse("")

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
