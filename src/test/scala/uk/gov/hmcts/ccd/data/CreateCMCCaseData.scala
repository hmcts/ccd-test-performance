package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object CreateCMCCaseData extends PerformanceTestsConfig {

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

 // val EventId = "CREATE"
  val randcaseType = new Random(System.currentTimeMillis())
  val caseEventTypeValue = Array("IssueClaim")
  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)
  val EventId = caseEventTypeValue(caseTypeValue_random_index)
  def PickCaseType(): String = EventId
 //def PickCaseType(): String = caseEventTypeValue(randcaseType.nextInt(caseEventTypeValue.length))

  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createCaseCMCUrl"))

  println("create case url: " + CreateCaseUrl)

    val caseTypeValue = Array("MoneyClaimCase")
  //val caseTypeValue = Array("ATCASETYPE1","ATCASETYPE2","ATCASETYPE3","ATCASETYPE4")
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
  val userToken = CcdTokenGenerator.generateWebUserToken(CreateCaseUrl)

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


  val EventBodyMain =StringBody("""{"data": {"submitterId": "SubmitterID${SubmitterId}","externalId": "External${ExternalId}","referenceNumber": "${ClaimNum1}MC${ClaimNum2}","feeAmountInPennies": "${FeeAmountInPennies}","reason": "A strong sense of entitlement that would explain my reasons of the claim, that the Roof work and leaks that followed were done below ${CaseSummaryText} by the council inspecto","sotSignerName": "Statement of truth Signer Name ${SotSignerName}","sotSignerRole": "Statement of truth Signer Role ${SotSignerRole}","feeAccountNumber": "${FeeAccountNumber}","externalReferenceNumber": null,"preferredCourt": "London","feeCode": "FEE${FeeCode}","amountType": "BREAK_DOWN","amountLowerValue": "20","amountHigherValue": "50","amountBreakDown": [{"id": null,"value": {"reason": "Roof Fix & repairs to leak","amount": "${AmountBreakDown}"}}],"interestType": "NO_INTEREST","interestRate": "2","interestReason": "Reason for claiming interest 001","interestBreakDownAmount": "${FeeCode}","interestBreakDownExplanation": "25","interestSpecificDailyAmount": "25","interestDateType": "SUBMISSION","interestClaimStartDate": "2019-03-20","interestStartDateReason": "Reason for selecting the start date 001","interestEndDateType": "SUBMISSION","totalAmount": "${TotalAmount}","timeline": [{"id": null,"value": {"date": "01/10/2019","description": "The day the first bill was issued ${CaseDescriptionText}"}},{"id": null,"value": {"date": "26/03/2019","description": "A historic day with enormous importance ${CaseDescriptionText}"}},{"id": null,"value": {"date": "20/06/2019","description": "line to explain what happened and when ${CaseDescriptionText}"}}],"evidence": [],"housingDisrepairOtherDamages": "No Other Damages","housingDisrepairCostOfRepairDamages": "200","personalInjuryGeneralDamages": "100","issuedOn": "2019-03-20","submittedOn": "2019-03-20T11:20:32.000","submitterEmail": "Submitter${SubmitterId}@confirmation.com","id": null,"features": null,"defendants": [{"id": null,"value": {"partyName": "Mr Jon Clark ${DefendantsPartyName}","directionsQuestionnaireDeadline": "2019-02-20","responseSubmittedOn": "2019-02-20T11:30:33.000","responseDeadline": "2019-04-24","courtDetermination": {"courtIntention": {"paymentOption": "IMMEDIATELY","paymentDate": "2019-03-20","instalmentAmount": "150","firstPaymentDate": "2019-03-20","paymentLength": "12","completionDate": "2020-03-20","paymentSchedule": "EVERY_MONTH"},"courtDecision": {"paymentOption": "IMMEDIATELY","paymentDate": "2019-03-20","instalmentAmount": "150","firstPaymentDate": "2019-03-20","paymentLength": "12","completionDate": "2020-03-20","paymentSchedule": "EVERY_MONTH"},"rejectionReason": "Rejection Reason ${RejectionReason}","disposableIncome": null,"decisionType": "COURT"},"responseType": "FULL_ADMISSION","responseAmount": "${ResponseAmount}","paymentDeclarationPaidDate": "2019-03-20","paymentDeclarationExplanation": "Explanation 001234","defendantPaymentIntention": {"paymentOption": "IMMEDIATELY","paymentDate": "2019-03-20","instalmentAmount": "150","firstPaymentDate": "2019-03-20","paymentLength": "12","completionDate": "2020-03-20","paymentSchedule": "EVERY_MONTH"},"defendantTimeLineEvents": []}}],"claimants": [{"id": null,"value": {"partyType": "INDIVIDUAL","partyName": "Mary Richards ${ClaimantsPartyName}","partyPhone": "790010010","partyEmail": "Claimant${ClaimantsPartyEmail}@confirmation.com","partyAddress": {"AddressLine1": null,"AddressLine2": null,"AddressLine3": null,"PostTown": null,"County": null,"PostCode": null,"Country": null},"partyCorrespondenceAddress": {"AddressLine1": null,"AddressLine2": null,"AddressLine3": null,"PostTown": null,"County": null,"PostCode": null,"Country": null},"representativeOrganisationName": "representative Organisation Name Test Org ${RepresentativeOrganisationName}","representativeOrganisationAddress": {"AddressLine1": null,"AddressLine2": null,"AddressLine3": null,"PostTown": null,"County": null,"PostCode": null,"Country": null},"representativeOrganisationPhone": "790010010","representativeOrganisationDxAddress": null,"representativeOrganisationEmail": "Org${SubmitterId}@confirmation.com","partyTitle": "Mr","partyDateOfBirth": "2002-03-20","partyBusinessName": "Test Org ${PartyBusinessName}","partyContactPerson": "Test KJ ${PartyContactPerson}","partyCompaniesHouseNumber": "${PartyCompaniesHouseNumber}"}}],"subjectName": "Subject Name ${SubjectName}","subjectType": "CLAIMANT","paymentId": "${PaymentId}","paymentAmount": "${PaymentAmount}","paymentReference": "RC-${PaymentReference1}-${PaymentReference2}-${PaymentReference3}-${PaymentReference4}","paymentStatus": "Success","paymentDateCreated": "2019-03-20"},"event": {"id": "IssueClaim","summary": "4th Page Check your answers - Event summary ${CaseSummaryText}","description": "4th Page Check your answers - Event description ${CaseDescriptionText}"},"event_token": """"+ "${eventToken}" + """","ignore_warning": false,"draft_id": null}""")




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
    ).exec(
      //http("get create case event token")
      http("TX01_CCD_CreateCaseEndpoint_CMC_createcase_eventtoken")
        .get(CreateCaseTokenUrl)
        .header("ServiceAuthorization", token)
        .header("Authorization", userToken)
        .header("Content-Type","application/json")
        .check(status.is(200),jsonPath("$.token").saveAs("eventToken"))
    ).exec(
      //http("create case data")
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

