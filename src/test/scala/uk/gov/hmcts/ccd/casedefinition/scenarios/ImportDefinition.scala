package uk.gov.hmcts.ccd.casedefinition.scenarios

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

object ImportDefinition extends PerformanceTestsConfig {

  def importDefinitionhttp() = {
    val token = CcdTokenGenerator.generateDataStoreS2SToken()
    exec(
        http("import a definition")
          .post(s"$CaseDefinitionUrl/import")
            .bodyPart(RawFileBodyPart("file", config.getString("importSpreadsheet")))
          .header("ServiceAuthorization", token)
          .header("Authorization", CcdTokenGenerator.generateImportUserToken)
          .check(status is 201)
    )
  }


  val importDefinition = scenario("Import a Definition")
    .exec(importDefinitionhttp())

}

