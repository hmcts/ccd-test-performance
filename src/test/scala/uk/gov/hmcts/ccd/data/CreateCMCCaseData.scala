package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.core.feeder.*
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object CreateCMCCaseData extends PerformanceTestsConfig {

  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue
  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

  // val EventId = "applyForGrant"
  private val rng: Random = new Random()
  private def submitterId(): Int = rng.nextInt(99999)
  private def externalId(): Int = rng.nextInt(99999)
  private def feeAmountInPennies(): Int = rng.nextInt(6)
  private def sotSignerName(): Int = rng.nextInt(99999)
  private def sotSignerRole(): Int = rng.nextInt(9999)

  private def claimNum1(): Int = rng.nextInt(999)
  private def claimNum2(): Int = rng.nextInt(999)

  private def feeCode(): Int = rng.nextInt(999)
  private def amountBreakDown(): Int = rng.nextInt(9999)
  private def totalAmount(): Int = rng.nextInt(99999)
  private def feeAccountNumber(): Int = rng.nextInt(99999)
  private def defendantsPartyName(): String = rng.alphanumeric.take(10).mkString
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString
  private def rejectionReason(): Int = rng.nextInt(99999999)
  private def responseAmount(): Int = rng.nextInt(99999)
  private def claimantsPartyName(): String = rng.alphanumeric.take(10).mkString
  private def claimantsPartyEmail(): Int = rng.nextInt(99999)
  private def representativeOrganisationName(): String = rng.alphanumeric.take(10).mkString
  private def partyBusinessName(): String = rng.alphanumeric.take(10).mkString
  private def partyContactPerson(): String = rng.alphanumeric.take(10).mkString
  private def partyCompaniesHouseNumber(): Int = rng.nextInt(9999999)
  private def subjectName(): String = rng.alphanumeric.take(10).mkString
  private def paymentId(): Int = rng.nextInt(9999999)
  private def paymentAmount(): Int = rng.nextInt(9999999)
  private def paymentReference1(): Int = rng.nextInt(9999)
  private def paymentReference2(): Int = rng.nextInt(9999)
  private def paymentReference3(): Int = rng.nextInt(9999)
  private def paymentReference4(): Int = rng.nextInt(9999)

  val randcaseType = new Random(System.currentTimeMillis())
  val caseEventTypeValue = Array("IssueClaim")
  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)
  val EventId = caseEventTypeValue(caseTypeValue_random_index)
  def PickCaseType(): String = EventId

  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createCaseCMCUrl"))

  println("create case url: " + CreateCaseUrl)

    val caseTypeValue = Array("MoneyClaimCase")

  val jurisdictionsValue = Array("CMC")

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

  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateWebUserToken()

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




  val EventBodyMain =StringBody("""{"data": {"submitterId": "SubmitterID ${SubmitterId}","caseName": "Endley Council v Smith/Tate/Jones ${SubmitterId}","externalId": "External ${ExternalId}","referenceNumber": "${ClaimNum1}MC${ClaimNum2}","feeAmountInPennies": "${FeeAmountInPennies}","reason": "A strong sense of entitlement that would explain my reasons of the claim, that the Roof work and leaks that followed were done below standards set by the council inspecto","sotSignerName": "Statement of truth Signer Name ${SotSignerName}","sotSignerRole": "Statement of truth Signer Role ${SotSignerRole}","feeAccountNumber": "${FeeAccountNumber}","externalReferenceNumber": "test123","preferredCourt": "London","feeCode": "FEE${FeeCode}","amountType": "RANGE","amountLowerValue": "123","amountHigherValue": "500","interestType": "NO_INTEREST","interestRate": "12","interestReason": "123","interestBreakDownAmount": "1232","interestBreakDownExplanation": "test","interestSpecificDailyAmount": "12","interestDateType": "SUBMISSION","interestClaimStartDate": "2009-12-12","interestStartDateReason": "NA","interestEndDateType": "SUBMISSION","currentInterestAmount": "12300","totalAmount": "1234","timeline": [{"id": null,"value": {"date": "01/10/2019","description": "The day the first bill was issued"}}],"evidence": [{"id": null,"value": {"type": "RECEIPTS","description": "Evidence Receipt ${SubmitterId}"}}],"housingDisrepairOtherDamages": "123","housingDisrepairCostOfRepairDamages": "12","personalInjuryGeneralDamages": "12","issuedOn": "2018-12-12","submittedOn": "2018-12-23T12:23:23.000","submitterEmail": "confirmation_${SubmitterId}@confirmation.com","id": "771379906304249","caseDocuments": [{"id": null,"value": {"documentName": "Claim Document ${SubmitterId}","documentType": "CLAIM_ISSUE_RECEIPT","authoredDatetime": null,"createdDatetime": null,"createdBy": "User Kapil Jain ${SubmitterId}","documentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}},{"id": null,"value": {"documentName": "Cliam Document 2 ${SubmitterId}","documentType": "DEFENDANT_PIN_LETTER","authoredDatetime": null,"createdDatetime": null,"createdBy": "User Kapil Jain ${SubmitterId}","documentLink": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}}],"features": null,"respondents": [{"id": null,"value": {"partyName": "Defendant Name ${DefendantsPartyName}","directionsQuestionnaireDeadline": null,"responseSubmittedOn": null,"servedDate": null,"responseDeadline": null,"courtDetermination": {"courtIntention": {"paymentOption": "IMMEDIATELY","paymentDate": null,"instalmentAmount": "122","firstPaymentDate": null,"paymentLength": "12","completionDate": null,"paymentSchedule": "EVERY_MONTH"},"courtDecision": {"paymentOption": "IMMEDIATELY","paymentDate": null,"instalmentAmount": "12","firstPaymentDate": null,"paymentLength": "12","completionDate": null,"paymentSchedule": "EVERY_MONTH"},"rejectionReason": null,"disposableIncome": null,"decisionType": "COURT"},"responseType": "PART_ADMISSION","responseAmount": null,"paymentDeclarationPaidAmount": null,"paymentDeclarationPaidDate": null,"paymentDeclarationExplanation": null,"defendantPaymentIntention": {"paymentOption": "IMMEDIATELY","paymentDate": null,"instalmentAmount": null,"firstPaymentDate": null,"paymentLength": null,"completionDate": null,"paymentSchedule": "EVERY_MONTH"},"defendantTimeLineEvents": [{"id": null,"value": {"date": "12/12/2018","description": "Timeline description ${SubmitterId}"}}],"defendantTimeLineComment": "test ${SubmitterId}","responseEvidenceRows": [{"id": null,"value": {"type": "RECEIPTS","description": "Receipt Attached ${SubmitterId}"}}],"responseEvidenceComment": "Response Evidence attached comments","responseSubject": "Res_DEFENDANT","responseDefenceType": "ALREADY_PAID","responseDefence": "test ${SubmitterId}","responseFreeMediationOption": "No","responseMediationPhoneNumber": {"telephoneNumber": "02020002003","telephoneUsageType": "Landline","contactDirection": "test ${SubmitterId}"},"responseMediationContactPerson": "test${SubmitterId}","responseMoreTimeNeededOption": "No","responseDefendantSOTSignerName": "Test ${SubmitterId}","responseDefendantSOTSignerRole": "Test ${SubmitterId}","paidInFullDate": "2018-12-12","claimantResponse": {"mediationPhoneNumber": {"telephoneNumber": "02920001002","telephoneUsageType": "Landline","contactDirection": "Test ${SubmitterId}"},"mediationContactPerson": "Test ${SubmitterId}","claimantResponseType": "ACCEPTATION","amountPaid": "123","formaliseOption": "REFER_TO_JUDGE","courtDetermination": {"courtIntention": {"paymentOption": "IMMEDIATELY","paymentDate": null,"instalmentAmount": "123","firstPaymentDate": null,"paymentLength": "12","completionDate": null,"paymentSchedule": null},"courtDecision": {"paymentOption": "INSTALMENTS","paymentDate": null,"instalmentAmount": "212","firstPaymentDate": null,"paymentLength": "12","completionDate": null,"paymentSchedule": "EVERY_MONTH"},"rejectionReason": null,"disposableIncome": null,"decisionType": "COURT"},"claimantPaymentIntention": {"paymentOption": "IMMEDIATELY","paymentDate": null,"instalmentAmount": null,"firstPaymentDate": null,"paymentLength": null,"completionDate": null,"paymentSchedule": "EVERY_MONTH"},"freeMediationOption": null,"reason": null,"paymentReceived": null,"settleForAmount": null,"submittedOn": null},"settlementPartyStatements": [],"settlementReachedAt": null,"countyCourtJudgementRequest": {"type": "ADMISSIONS","requestedDate": null,"defendantDateOfBirth": null,"paymentOption": "IMMEDIATELY","paidAmount": "123","repaymentPlanInstalmentAmount": "12","repaymentPlanFirstPaymentDate": null,"repaymentPlanPaymentLength": null,"repaymentPlanCompletionDate": null,"repaymentPlanPaymentSchedule": "EVERY_MONTH","payBySetDate": null,"statementOfTruthSignerName": null,"statementOfTruthSignerRole": null},"countyCourtJudgmentRequest": {"type": "ADMISSIONS","requestedDate": null,"defendantDateOfBirth": null,"paymentOption": "IMMEDIATELY","paidAmount": null,"repaymentPlanInstalmentAmount": null,"repaymentPlanFirstPaymentDate": null,"repaymentPlanPaymentLength": null,"repaymentPlanCompletionDate": null,"repaymentPlanPaymentSchedule": "EVERY_TWO_WEEKS","payBySetDate": null,"statementOfTruthSignerName": null,"statementOfTruthSignerRole": null},"redeterminationExplanation": null,"redeterminationRequestedDate": null,"redeterminationMadeBy": "COURT","defendantId": null,"letterHolderId": null,"partyDetail": {"partyId": null,"idamId": null,"type": null,"title": null,"firstName": null,"lastName": null,"dateOfBirth": null,"primaryAddress": {"AddressLine1": "3${ClaimNum1}7 Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","Country": "United Kingdom","PostCode": "CO3 0RN"},"emailAddress": null,"telephoneNumber": {"telephoneNumber": "02920002002","telephoneUsageType": "Landline","contactDirection": "Test 123"},"correspondenceAddress": {"AddressLine1": "4${ClaimNum1}1 Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","Country": "United Kingdom","PostCode": "CO3 0RN"},"businessName": null,"contactPerson": null,"companiesHouseNumber": null},"representativeOrganisationName": null,"representativeOrganisationAddress": {"AddressLine1": "6${ClaimNum1}1 Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CO3 0RN","Country": "United Kingdom"},"representativeOrganisationPhone": null,"representativeOrganisationDxAddress": null,"representativeOrganisationEmail": null,"statementOfMeans": {"reason": "test 123","residenceType": "PRIVATE_RENTAL","residenceOtherDetail": "test${ClaimNum1}","noOfMaintainedChildren": "2","dependantChildren": [],"anyDisabledChildren": null,"numberOfOtherDependants": null,"otherDependantDetails": null,"otherDependantAnyDisabled": null,"employers": [],"taxPaymentsReason": null,"taxYouOwe": null,"selfEmploymentJobTitle": null,"selfEmploymentAnnualTurnover": null,"unEmployedNoOfYears": null,"unEmployedNoOfMonths": null,"retired": null,"employmentDetails": null,"bankAccounts": [],"debts": [],"incomes": [],"expenses": [],"courtOrders": [],"priorityDebts": [],"carer": null,"disabilityStatus": null,"livingPartner": {"disability": null,"over18": null,"pensioner": null}},"claimantProvidedDetail": {"partyId": null,"idamId": null,"type": null,"title": null,"firstName": null,"lastName": null,"dateOfBirth": null,"primaryAddress": {"AddressLine1": null,"AddressLine2": null,"AddressLine3": null,"PostTown": null,"County": null,"Country": null,"PostCode": null},"emailAddress": null,"telephoneNumber": {"telephoneNumber": null,"telephoneUsageType": null,"contactDirection": null},"correspondenceAddress": {"AddressLine1": null,"AddressLine2": null,"AddressLine3": null,"PostTown": null,"County": null,"Country": null,"PostCode": null},"businessName": null,"contactPerson": null,"companiesHouseNumber": null},"claimantProvidedPartyName": null,"claimantProvidedRepresentativeOrganisationName": null,"claimantProvidedRepresentativeOrganisationAddress": {"AddressLine1": null,"AddressLine2": null,"AddressLine3": null,"PostTown": null,"County": null,"PostCode": null,"Country": null},"claimantProvidedRepresentativeOrganisationPhone": null,"claimantProvidedRepresentativeOrganisationDxAddress": null,"claimantProvidedRepresentativeOrganisationEmail": null}}],"applicants": [],"subjectName": null,"subjectType": null,"paymentId": null,"paymentAmount": null,"paymentReference": null,"paymentStatus": null,"paymentDateCreated": null,"migratedFromClaimStore": "No"},"event": {"id": "IssueClaim","summary": "4th Page Check your answers - Event summary ${CaseSummaryText}","description": "4th Page Check your answers - Event description ${CaseDescriptionText}"},"event_token": """"+ "${eventToken}" + """","ignore_warning": false,"draft_id": null}""")



  val   CreateCMCCaseDataSCN = scenario("Create CMC Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("SubmitterId", submitterId()),
        ("ExternalId", externalId()),
        ("FeeAmountInPennies", feeAmountInPennies()),
        ("SotSignerName",sotSignerName()),
        ("SotSignerRole",sotSignerRole()),
        ("ClaimNum1",claimNum1()),
        ("ClaimNum2",claimNum2()),
        ("FeeCode",feeCode()),
        ("FeeAccountNumber",feeAccountNumber()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText()),
        ("AmountBreakDown", amountBreakDown()),
        ("TotalAmount",totalAmount()),
        ("DefendantsPartyName",defendantsPartyName()),
        ("RejectionReason",rejectionReason()),
        ("ResponseAmount",responseAmount()),
        ("ClaimantsPartyName", claimantsPartyName()),
        ("ClaimantsPartyEmail", claimantsPartyEmail()),
        ("RepresentativeOrganisationName", representativeOrganisationName()),
        ("PartyBusinessName", partyBusinessName()),
        ("PartyContactPerson", partyContactPerson()),
        ("PartyCompaniesHouseNumber", partyCompaniesHouseNumber()),
        ("SubjectName", subjectName()),
        ("PaymentId", paymentId()),
        ("PaymentAmount", paymentAmount()),
        ("PaymentReference1", paymentReference1()),
        ("PaymentReference2", paymentReference2()),
        ("PaymentReference3", paymentReference3()),
        ("PaymentReference4", paymentReference4())
      )
    )
      .feed(fileProviderRand)
      .exec(
      http("TX01_CCD_CreateCaseEndpoint_CMC_createcase_eventtoken")
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
      http("TX02_CCD_CreateCaseEndpoint_CMC_createcasedata")
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

