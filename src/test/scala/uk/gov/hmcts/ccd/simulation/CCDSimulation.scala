package uk.gov.hmcts.ccd.simulation

import io.gatling.core.Predef.{Simulation, _}
import io.gatling.core.structure.PopulationBuilder
import io.gatling.http.Predef.{Proxy, http}
import io.gatling.http.protocol.HttpProtocolBuilder
import uk.gov.hmcts.ccd.config.Implicits.RichConfig
import uk.gov.hmcts.ccd.util.{Headers, PerformanceTestsConfig}

import scala.concurrent.duration.DurationInt

abstract class CCDSimulation extends Simulation with PerformanceTestsConfig {

  val baseHttpUrl: String

  val httpConf = http.baseUrl(baseHttpUrl).headers(Headers.commonHeaders)

  def getProxiedHttpConf(): Option[HttpProtocolBuilder] = {
    val proxyHostOptional = config.getOptionalString("httpProxyHost")
    val proxyPortOptional = config.getOptionalString("httpProxyPort")

    for {
      host <- proxyHostOptional
      port <- proxyPortOptional
    } yield httpConf.proxy(Proxy(host, port.toInt))
  }

  def getHttpConf() = getProxiedHttpConf().getOrElse(httpConf)

  def scenarios(): List[PopulationBuilder]

  def setup() = setUp(scenarios())
    .protocols(getHttpConf())
   /* .throttle(
      reachRps(ReachRPSTarget) in (ReachRPSDuration seconds),
      holdFor(ReachRPSHoldForDuration minutes),
      jumpToRps(JumptoRPSTarget),
      holdFor(JumptoRPSDuration minutes)
    )*/
    .assertions(
      global.responseTime.mean.lt(config.getInt("meanResponseTime"))

    ).maxDuration(MaxSimulationDuration minutes)
}


