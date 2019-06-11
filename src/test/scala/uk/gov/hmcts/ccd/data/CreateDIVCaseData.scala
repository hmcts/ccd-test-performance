package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}
import io.gatling.core.feeder.RecordSeqFeederBuilder

import scala.concurrent.duration._
import scala.util.Random


object CreateDIVCaseData extends PerformanceTestsConfig {


  private val rng: Random = new Random()
  private def d8MarriagePetitionerName(): String = rng.alphanumeric.take(10).mkString
  private def d8MarriageRespondentName(): String = rng.alphanumeric.take(10).mkString
  private def d8PetitionerEmail(): String = rng.alphanumeric.take(10).mkString
  private def d8caseReference(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def d8PetitionerPhoneNumber(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def d8PetitionerFirstName(): String = rng.alphanumeric.take(10).mkString
  private def d8PetitionerLastName(): String = rng.alphanumeric.take(10).mkString
  private def d8RespondentFirstName(): String = rng.alphanumeric.take(10).mkString
  private def d8RespondentLastName(): String = rng.alphanumeric.take(10).mkString
  private def d8DerivedPetitionerCurrentFullName(): String = rng.alphanumeric.take(10).mkString
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString
  private def burnid(): Int = rng.nextInt(99999999)
  private def d8MarriageDate(): Int = 10 + rng.nextInt(100 - 10) + 1

  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

  val randcaseType = new Random(System.currentTimeMillis())

  val caseEventTypeValue = Array("CREATE")

  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)

  val EventId = caseEventTypeValue(caseTypeValue_random_index)

  def PickCaseType(): String = EventId


  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createCaseDIVUrl"))

  println("create case url: " + CreateCaseUrl)

  val caseTypeValue = Array("DIVORCE")

  val jurisdictionsValue = Array("DIVORCE")


  val rand = new Random(System.currentTimeMillis())
  val caseType_random_index = rand.nextInt(caseTypeValue.length)

  val jurisdictions_random_index = rand.nextInt(jurisdictionsValue.length)

  if(!CreateCaseUrl.contains(":casetype_reference"))
    {
      CreateCaseUrl
    }
  else{
    CreateCaseUrl = CreateCaseUrl.replace(":casetype_reference", caseTypeValue(caseType_random_index))
  }

  println("create case url: " + CreateCaseUrl)

  if(!CreateCaseUrl.contains(":jurisdictions_reference"))
  {
    CreateCaseUrl
  }
  else{
    CreateCaseUrl = CreateCaseUrl.replace(":jurisdictions_reference", jurisdictionsValue(jurisdictions_random_index))
  }

  println("create case url: " + CreateCaseUrl)
  val CreateCaseTokenUrl = s"${CreateCaseUrl.replaceAll("cases", "")}event-triggers/$EventId/token"


  println("create case url: " + CreateCaseUrl)
  println("create case token url: " + CreateCaseTokenUrl)

  val EventBodyMain =StringBody("""{"data":{"D8legalProcess":"divorce","createdDate":"2018-10-10","D8ScreenHasMarriageBroken":"No","D8ScreenHasRespondentAddress":"Yes","D8ScreenHasMarriageCert":"No","D8ScreenHasPrinter":"Yes","D8DivorceWho":"husband","D8MarriageIsSameSexCouple":"No","D8InferredPetitionerGender":"male","D8InferredRespondentGender":"female","D8MarriedInUk":"Yes","D8CertificateInEnglish":"Yes","D8CertifiedTranslation":"No","D8MarriagePlaceOfMarriage":"London","D8CountryName":"UK","D8PetitionerNameDifferentToMarriageCert":"No","D8PetitionerPhoneNumber":"07${D8PetitionerPhoneNumber}","D8DerivedPetitionerCurrentFullName":"Robert Del monte  ${D8DerivedPetitionerCurrentFullName}","D8PetitionerNameChangedHow":["marriageCertificate","deedPoll"],"D8PetitionerNameChangedHowOtherDetails":"Namechangedwithnoreason","D8PetitionerContactDetailsConfidential":"keep","D8PetitionerHomeAddress":{"AddressLine1":"14AvenueRoad","AddressLine2":"RevenueLane","AddressLine3":null,"PostCode":"W45XR","PostTown":"Chiswick","County":"Middlesex","Country":"UK"},"D8DerivedPetitionerHomeAddress":"Sameasabove","D8PetitionerCorrespondenceAddress":{"AddressLine1":null,"AddressLine2":null,"AddressLine3":null,"PostCode":null,"PostTown":null,"County":null,"Country":null},"D8DerivedPetitionerCorrespondenceAddr":"Sameasabove","D8PetitionerCorrespondenceUseHomeAddress":"Yes","D8RespondentNameAsOnMarriageCertificate":"Yes","D8DerivedRespondentCurrentName":"Dimarta","D8DerivedRespondentSolicitorDetails":"NotKnown","D8RespondentHomeAddress":{"AddressLine1":"15AvenueRoad","AddressLine2":"RevenueLane","AddressLine3":null,"PostCode":"W45XR","PostTown":"Chiswick","County":"Middlesex","Country":"UK"},"D8DerivedRespondentHomeAddress":"sameasabove","D8RespondentCorrespondenceAddress":{"AddressLine1":null,"AddressLine2":null,"AddressLine3":null,"PostCode":null,"PostTown":null,"County":null,"Country":null},"D8DerivedRespondentCorrespondenceAddr":"sameasabove","D8RespondentSolicitorName":"RickSick","D8RespondentSolicitorCompany":"DivorcedSolicitors","D8RespondentCorrespondenceSendToSol":"Yes","D8RespondentSolicitorAddress":{"AddressLine1":"16AvenueRoad","AddressLine2":"RevenueLane","AddressLine3":null,"PostCode":"W45XR","PostTown":"Chiswick","County":"Middlesex","Country":"UK"},"D8DerivedRespondentSolicitorAddr":"sameasabove","D8RespondentCorrespondenceUseHomeAddress":"Yes","D8RespondentKnowsHomeAddress":"Yes","D8RespondentLivesAtLastAddress":"No","D8LivingArrangementsTogetherSeparated":"Yes","D8LivingArrangementsLastLivedTogether":"No","D8LivingArrangementsLiveTogether":"Yes","D8LivingArrangementsLastLivedTogethAddr":{"AddressLine1":null,"AddressLine2":null,"AddressLine3":null,"PostCode":null,"PostTown":null,"County":null,"Country":null},"D8DerivedLivingArrangementsLastLivedAddr":"sameasabove","D8LegalProceedings":"No","D8LegalProceedingsRelated":["property","marriage"],"D8LegalProceedingsDetails":null,"D8ReasonForDivorce":"separation-2-years","D8DerivedStatementOfCase":null,"D8ReasonForDivorceBehaviourDetails":null,"D8ReasonForDivorceDesertionDate":"2019-12-12","D8ReasonForDivorceDesertionAgreed":"No","D8ReasonForDivorceDesertionDetails":null,"D8ReasonForDivorceSeperationDate":"2017-10-10","D8ReasonForDivorceAdultery3rdPartyFName":null,"D8ReasonForDivorceAdultery3rdPartyLName":null,"D8DerivedReasonForDivorceAdultery3dPtyNm":null,"D8ReasonForDivorceAdulteryDetails":"notknown","D8ReasonForDivorceAdulteryKnowWhen":"Yes","D8ReasonForDivorceAdulteryWishToName":"No","D8ReasonForDivorceAdulteryKnowWhere":"Yes","D8ReasonForDivorceAdulteryWhereDetails":"notknown","D8ReasonForDivorceAdulteryWhenDetails":"notknown","D8ReasonForDivorceAdulteryIsNamed":"Yes","D8ReasonForDivorceAdultery3rdAddress":{"AddressLine1":"17AvenueRoad","AddressLine2":"RevenueLane","AddressLine3":null,"PostCode":"W45XR","PostTown":"Chiswick","County":"Middlesex","Country":"UK"},"D8DerivedReasonForDivorceAdultery3rdAddr":"sameasabove","D8FinancialOrder":"No","D8FinancialOrderFor":["petitioner"],"D8HelpWithFeesAppliedForFees":"Yes","D8HelpWithFeesReferenceNumber":null,"D8PaymentMethod":"card","D8DivorceCostsClaim":"No","D8DivorceIsNamed":"No","D8DivorceClaimFrom":["respondent","correspondent"],"D8JurisdictionConfidentLegal":"Yes","D8JurisdictionConnection":["A","C","E","F"],"D8JurisdictionLastTwelveMonths":"Yes","D8JurisdictionPetitionerDomicile":"No","D8JurisdictionPetitionerResidence":"Yes","D8JurisdictionRespondentDomicile":"No","D8JurisdictionRespondentResidence":"Yes","D8JurisdictionHabituallyResLast6Months":"No","Payments":[],"D8DocumentsUploaded": [{"id": null,"value": {"DocumentType": "marriageCert","DocumentEmailContent": "Marriage Certificate Evidence","DocumentDateAdded": "2018-12-12","DocumentComment": "Comment ${D8PetitionerPhoneNumber}","DocumentFileName": "Marriage Certificate ${D8PetitionerPhoneNumber}","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}},{"id": null,"value": {"DocumentType": "marriageCert","DocumentEmailContent": "Marriage Certificate Evidence","DocumentDateAdded": "2018-12-12","DocumentComment": "Comment ${D8PetitionerPhoneNumber}","DocumentFileName": "Marriage Certificate ${D8PetitionerPhoneNumber}","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}},{"id": null,"value": {"DocumentType": "marriageCert","DocumentEmailContent": "Marriage Certificate Evidence","DocumentDateAdded": "2018-12-12","DocumentComment": "Comment ${D8PetitionerPhoneNumber}","DocumentFileName": "Marriage Certificate ${D8PetitionerPhoneNumber}","DocumentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}}],"D8DocumentsGenerated": [{"id": null,"value": {"DocumentType": "marriageCertTranslation","DocumentEmailContent": "Documents generated","DocumentDateAdded": "2018-12-12","DocumentComment": null,"DocumentFileName": "Documents generated file name ${D8PetitionerPhoneNumber}","DocumentLink": {"document_url": "https://${DMURL}/${Document_ID}","document_binary_url": "https://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}},{"id": null,"value": {"DocumentType": "marriageCertTranslation","DocumentEmailContent": "Documents generated","DocumentDateAdded": "2018-12-12","DocumentComment": null,"DocumentFileName": "Documents generated file name ${D8PetitionerPhoneNumber}","DocumentLink": {"document_url": "https://${DMURL}/${Document_ID}","document_binary_url": "https://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}},{"id": null,"value": {"DocumentType": "marriageCertTranslation","DocumentEmailContent": "Documents generated","DocumentDateAdded": "2018-12-12","DocumentComment": null,"DocumentFileName": "Documents generated file name ${D8PetitionerPhoneNumber}","DocumentLink": {"document_url": "https://${DMURL}/${Document_ID}","document_binary_url": "https://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}}],"D8StatementOfTruth":"No","D8Cohort":"onlineSubmissionPrivateBeta","AosLetterHolderId":null,"D8caseReference":"EZ12D8${D8caseReference}","D8DivorceUnit":"eastMidlands","D8MarriageDate":"19${D8MarriageDate}-10-10","D8MarriagePetitionerName":"Francis Dela Cruz ${D8MarriagePetitionerName}","D8MarriageRespondentName":"Cruz Dela Francis ${D8MarriageRespondentName}","D8PetitionerFirstName":"Della ${D8PetitionerFirstName}","D8PetitionerLastName":"Francis ${D8PetitionerLastName}","D8PetitionerEmail":"dela_${D8PetitionerEmail}@francis.com","D8RespondentFirstName":"Dimello ${D8RespondentFirstName}","D8RespondentLastName":"Best ${D8RespondentLastName}","D8HelpWithFeesNeedHelp":"Yes","dueDate":"2019-10-10","SolPaymentHowToPay":"feePayByAccount","burnID":"10${burnID}","receivedDate":"2019-01-10"},"event":{"id":"create","summary": "Performance Testing Event summary ${CaseSummaryText}","description": "Performance Testing Event description ${CaseDescriptionText}"},"event_token": """"  + "${eventToken}" +   """","ignore_warning":false,"draft_id":null}""")


  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateSIDAMUserToken()


  val   CreateDIVCaseDataSCN = scenario("Create DIV Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("D8MarriagePetitionerName", d8MarriagePetitionerName()),
        ("D8MarriageRespondentName", d8MarriageRespondentName()),
        ("D8caseReference", d8caseReference()),
        ("D8PetitionerPhoneNumber",d8PetitionerPhoneNumber()),
        ("D8PetitionerFirstName",d8PetitionerFirstName()),
        ("D8PetitionerLastName",d8PetitionerLastName()),
        ("D8DerivedPetitionerCurrentFullName",d8DerivedPetitionerCurrentFullName()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText()),
        ("D8PetitionerEmail", d8PetitionerEmail()),
        ("burnID",burnid()),
        ("D8RespondentFirstName",d8RespondentFirstName()),
        ("D8RespondentLastName",d8RespondentLastName()),
        ("PickCaseEventType",PickCaseType()),
        ("D8MarriageDate", d8MarriageDate())
      )
    ) .feed(fileProviderRand)
      .exec(
      http("TX01_CCD_CreateCaseEndpoint_Divorce_createcase_eventtoken")
        .get(CreateCaseTokenUrl)
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
    ).exec(http("TX20_DocStore_PostDocument_${filename}")
      .post(DocStoreBashURL +"/documents")
      .header("Authorization", userToken)
      .header("ServiceAuthorization", token)
      .header("user-id", "giri.benadikar@hmcts.net")
      .bodyPart(
        RawFileBodyPart("files", "${filename}")
          .contentType("application/pdf")
          .fileName("${filename}")).asMultipartForm
      .formParam("classification", "PUBLIC")
      .check(status is 200, jsonPath("$._embedded.documents[0]._links.binary.href").saveAs("fileId"), regex("""http://(.+)/""").saveAs("DMURL"),regex("""/documents/(.+)"""").saveAs("Document_ID")))
      .pause(MinThinkTime seconds, MaxThinkTime seconds)
      .exec(
      http("TX02_CCD_CreateCaseEndpoint_Divorce_createcasedata")
        .post(CreateCaseUrl)
        .body(EventBodyMain).asJSON
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status is 201)
    )

      .pause(MinThinkTime seconds, MaxThinkTime seconds)
  }


  val waitForNextIteration = pace(MinWaitForNextIteration seconds, MaxWaitForNextIteration seconds)
}

