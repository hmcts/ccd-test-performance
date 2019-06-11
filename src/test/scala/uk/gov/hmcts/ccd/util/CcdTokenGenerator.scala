package uk.gov.hmcts.ccd.util

import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator
import io.restassured.RestAssured._
import io.restassured.builder._
import io.restassured.specification._
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig
import io.restassured.http.ContentType
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification


object CcdTokenGenerator extends PerformanceTestsConfig with SpringApplicationContext {

  val TOKEN_LEASE_URL = s"$UserAuthUrl/testing-support/lease"
  val USERTOKEN_SidAM_URL = s"$UserAuthUrl/authorizationCode"
  val CCDRedirectURL = s"$CcdCaseMgmtUrl/oauth2redirect"

  var dataStoreS2STokenGenerator = applicationContext.getBean("dataStoreS2STokenGenerator").asInstanceOf[AuthTokenGenerator]
  var gatewayS2STokenGenerator = applicationContext.getBean("gatewayS2STokenGenerator").asInstanceOf[AuthTokenGenerator]

  def generateWebUserToken(url: String): String = generateUserToken(UserCcdId, roleFor(url))

  def generateImportUserToken: String = generateUserToken(UserImportId, "ccd-import")

  protected def generateUserToken(id: String, role: String): String = {
    val url = TOKEN_LEASE_URL + String.format("?id=%s&role=%s", id, role)
    val restTemplate = new RestTemplate
    val response = restTemplate.exchange(url, HttpMethod.POST, null, classOf[String])
    val token = response.getBody
    println(s"generated user token $token")
    "Bearer " + token
  }

  def generateSIDAMUserToken() : String = {
    return generateSIDAMUserToken("kapil.jain@hmcts.net")
  }

  def generateSIDAMUserToken(userName : String) : String = {

    val authCodeRequest = RestAssured.given().log().all().config(RestAssured.config()
      .encoderConfig(EncoderConfig.encoderConfig()
        .encodeContentTypeAs("x-www-form-urlencoded",
          ContentType.URLENC)))
      .contentType("application/x-www-form-urlencoded; charset=UTF-8")
      .formParam("username", userName)
      .formParam("password", "Password12")
      .formParam("client_id", "test-public-service")
      .formParam("redirect_uri", "https://test-public-service.com")
      .header("accept", "application/json")
      .request()

    val response = authCodeRequest.post(USERTOKEN_SidAM_URL)

    val statusCode = response.getStatusCode()

    val getheader = response.getHeader("Location")

    val codeIndexStart = getheader.indexOf("=")

    val codeIndexEnd = getheader.indexOf("&")

    val authCode =  getheader.substring(codeIndexStart + 1,codeIndexEnd)

    val userTokenRequest = RestAssured.given().log().all().config(RestAssured.config()
      .encoderConfig(EncoderConfig.encoderConfig()
        .encodeContentTypeAs("x-www-form-urlencoded",
          ContentType.URLENC)))
      .contentType("application/x-www-form-urlencoded; charset=UTF-8")
      .formParam("code", authCode)
      .formParam("grant_type", "authorization_code")
      .formParam("redirect_uri", "https://test-public-service.com")
      .header("accept", "application/json")
      .request()

    val userTokenresponse = userTokenRequest.post(UserAuthUrl + "/oauth2/token?client_id=test-public-service&client_secret=test-public-service");

    val userTokenStatusCode = userTokenresponse.getStatusCode()

    val tokenStr = userTokenresponse.asString()

    val tokenIndexStart = tokenStr.indexOf(":")

    val tokenIndexEnd = tokenStr.indexOf(",")

    val token =  tokenStr.substring(tokenIndexStart+2,tokenIndexEnd -1 )

    println(s"token------------------------------------------------------------- $token")
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
    val result = "caseworker,caseworker-divorce,caseworker-probate,caseworker-probate-issuer,caseworker-autotest1,caseworker-sscs,caseworker-divorce-courtadmin,caseworker-loa1,caseworker-divorce-loa1,caseworker-autotest1-loa1,caseworker-sscs-loa1,caseworker-divorce-courtadmin-loa1" + parseJurisdiction(url).map(j => s"-$j").getOrElse("")

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
