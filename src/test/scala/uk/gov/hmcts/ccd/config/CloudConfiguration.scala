package uk.gov.hmcts.ccd.config

import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.http.HttpMessageConvertersAutoConfiguration
import org.springframework.cloud.openfeign.FeignAutoConfiguration
import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import uk.gov.hmcts.ccd.util.PerformanceTestsConfig
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGeneratorFactory

@Configuration
@ImportAutoConfiguration(Array(classOf[FeignAutoConfiguration], classOf[HttpMessageConvertersAutoConfiguration]))
@ComponentScan(basePackageClasses = Array(classOf[ServiceAuthorisationApi]))
class CloudConfiguration extends PerformanceTestsConfig {

  @Bean
  def dataStoreS2STokenGenerator(serviceAuthorisationApi: ServiceAuthorisationApi): AuthTokenGenerator =
    AuthTokenGeneratorFactory.createDefaultGenerator(config.getString("dataStoreS2STokenGeneratorSecret"), "ccd_data", serviceAuthorisationApi)

  @Bean
  def gatewayS2STokenGenerator(serviceAuthorisationApi: ServiceAuthorisationApi): AuthTokenGenerator =
    AuthTokenGeneratorFactory.createDefaultGenerator(config.getString("gatewayS2STokenGeneratorSecret"), "ccd_gw", serviceAuthorisationApi)

}
