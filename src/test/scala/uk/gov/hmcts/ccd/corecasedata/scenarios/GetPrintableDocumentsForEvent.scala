package uk.gov.hmcts.ccd.corecasedata.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._

object GetPrintableDocumentsForEvent extends PerformanceTestsConfig {

  private val url: String = config.getString("caseDataUrl") + "/" + config.getString("getPrintableDocumentsForEvent")
  println("Retrieving getPrintableDocumentsForEvent URL : " + url)

  def httpRequest() = {
    val s2sToken = CcdTokenGenerator.generateGatewayS2SToken()
    val userToken = CcdTokenGenerator.generateWebUserToken(url)
    http("TX09_CCD_GetPrintableDocumentsForEvent_getDocumentsForEvent")
      .get(url)
      .header("ServiceAuthorization", s2sToken)
      .header("Authorization", userToken)
      .header("Content-Type","application/json")
      .check(status in  (200))
  }

  println("WorkBasketInputDetails: Minimum think time " + MinThinkTime + " Maximum think time " + MaxThinkTime)

  val getPrintableDocumentForEvents = scenario("Get Printable Document for Events").during(TotalRunDuration minutes) {
    exec(httpRequest())
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }
}
