package uk.gov.hmcts.ccd.util

import java.net.{URL, URLDecoder}
import java.util
import spray.json._
import spray.json.DefaultJsonProtocol._

import org.springframework.http.{HttpEntity, HttpHeaders, MediaType}
import org.springframework.web.client.RestTemplate
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

object CcdTokenGenerator extends PerformanceTestsConfig with SpringApplicationContext {

  val AUTHORISATION_CLIENT_ID = s"ccd_gateway"
  val AUTH_CODE_URL = s"$UserAuthUrl/authorizationCode"
  val AUTH_TOKEN_URL = s"$UserAuthUrl/oauth2/token?client_id=$AUTHORISATION_CLIENT_ID&client_secret=$OAuth2ClientSecret"
  val restTemplate = new RestTemplate

  var dataStoreS2STokenGenerator = applicationContext.getBean("dataStoreS2STokenGenerator").asInstanceOf[AuthTokenGenerator]
  var gatewayS2STokenGenerator = applicationContext.getBean("gatewayS2STokenGenerator").asInstanceOf[AuthTokenGenerator]

  def generateWebUserToken(): String = generateUserToken(UserCcdId, UserCcdPassword, RedirectUriForAuthToken)

  def generateImportUserToken: String = generateUserToken(UserImportId, UserImportPassword, RedirectUriForAuthToken)

  private def generateUserToken(id: String, password: String, redirectUri: String): String = {
    val authCode = generateUserAuthCode(id, password, redirectUri)
    val token = generateUserAuthToken(authCode, redirectUri)

    token
  }

  private def generateUserAuthCode(userId: String, password: String, redirectUri: String): String = {
    // retrieve authorisation code for user name and password
    val headers = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)
    headers.setAccept(util.Arrays.asList(MediaType.APPLICATION_JSON_UTF8))

    val formParameters = new LinkedMultiValueMap[String, String]
    formParameters.add("username", userId)
    formParameters.add("password", password)
    formParameters.add("client_id", AUTHORISATION_CLIENT_ID)
    formParameters.add("redirect_uri", redirectUri)

    val request = new HttpEntity[MultiValueMap[String, String]](formParameters, headers)

    val response = restTemplate.postForEntity(AUTH_CODE_URL, request, classOf[String])

    // extract authorisation code from the headers
    val locationHeader = response.getHeaders.get("Location").toString.replaceAll("\\[", "").replaceAll("\\]", "")
    val query = new URL(locationHeader).getQuery
    val queryParameters = parseUrlParameters(query)

    queryParameters.getOrElse("code", "")
  }

  private def generateUserAuthToken(authCode: String, redirectUri: String): String = {
    // retrieve SIDAM authorisation code for user name and password
    val headers = new HttpHeaders
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED)
    headers.setAccept(util.Arrays.asList(MediaType.APPLICATION_JSON_UTF8))

    val formParameters = new LinkedMultiValueMap[String, String]
    formParameters.add("code", authCode)
    formParameters.add("grant_type", "authorization_code")
    formParameters.add("redirect_uri", redirectUri)

    val request = new HttpEntity[MultiValueMap[String, String]](formParameters, headers)

    val response = restTemplate.postForEntity(AUTH_TOKEN_URL, request, classOf[String])

    // extract access token from the response body
    val responseProperties = response.getBody.parseJson.convertTo[Map[String, JsValue]]
    val accessToken= responseProperties("access_token").convertTo[String]

    println(s"accessToken: $accessToken")

    accessToken.toString
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

  private def parseUrlParameters(url: String) = {
    url.split("&").map(v => {
      val m = v.split("=", 2).map(s => URLDecoder.decode(s, "UTF-8"))
      m(0) -> m(1)
    }).toMap
  }

}
