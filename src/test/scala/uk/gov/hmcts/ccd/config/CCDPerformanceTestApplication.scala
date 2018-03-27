package uk.gov.hmcts.ccd.config

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.netflix.feign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import uk.gov.hmcts.ccd.util.PerformanceTestsConfig
import uk.gov.hmcts.reform.authorisation.ServiceAuthorisationApi
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGenerator
import uk.gov.hmcts.reform.authorisation.generators.AuthTokenGeneratorFactory


@SpringBootApplication
@Configuration
@EnableFeignClients(basePackageClasses = Array(classOf[ServiceAuthorisationApi]))
class PerformanceTestsApplication extends PerformanceTestsConfig {

  @Bean
  def dataStoreS2STokenGenerator(serviceAuthorisationApi: ServiceAuthorisationApi): AuthTokenGenerator =
    AuthTokenGeneratorFactory.createDefaultGenerator(config.getString("dataStoreS2STokenGeneratorSecret"), "ccd_data", serviceAuthorisationApi)

  @Bean
  def gatewayS2STokenGenerator(serviceAuthorisationApi: ServiceAuthorisationApi): AuthTokenGenerator =
    AuthTokenGeneratorFactory.createDefaultGenerator(config.getString("gatewayS2STokenGeneratorSecret"), "ccd_gw", serviceAuthorisationApi)
}