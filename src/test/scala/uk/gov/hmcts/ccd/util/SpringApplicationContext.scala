package uk.gov.hmcts.ccd.util

import org.springframework.boot.SpringApplication
import uk.gov.hmcts.ccd.config.PerformanceTestsApplication

trait SpringApplicationContext {

  def S2SAuthUrl(): String

  //  val applicationContext = SpringApplication.run(classOf[CCDPerformanceTestsApplication], s"--idam.s2s-auth.url=${s2sAuthUrl()}", "--logging.level.org.springframework=DEBUG");
  val applicationContext = SpringApplication.run(classOf[PerformanceTestsApplication], s"--idam.s2s-auth.url=$S2SAuthUrl")

}
