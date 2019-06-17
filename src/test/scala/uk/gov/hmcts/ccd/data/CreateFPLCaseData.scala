package uk.gov.hmcts.ccd.data

import io.gatling.core.Predef._
import io.gatling.core.feeder.RecordSeqFeederBuilder
import io.gatling.http.Predef._
import uk.gov.hmcts.ccd.util.{CcdTokenGenerator, PerformanceTestsConfig}

import scala.concurrent.duration._
import scala.util.Random


object CreateFPLCaseData extends PerformanceTestsConfig {


  private val rng: Random = new Random()
  private def caseName(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def caseIDReference(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def thresholdDetails(): String = rng.alphanumeric.take(10).mkString
  private def type_GiveReason(): String = rng.alphanumeric.take(10).mkString
  private def documentTitle(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def applicantName(): String = rng.alphanumeric.take(10).mkString
  private def applicantpersonToContact(): String = rng.alphanumeric.take(10).mkString
  private def appellantAddress(): Int = 1000 + rng.nextInt(9999 - 1000) + 1
  private def applicantMobile(): Int = 100000000 + rng.nextInt(999999999 - 100000000) + 1
  private def applicantPhone(): Int = 10000000 + rng.nextInt(99999999 - 10000000) + 1
  private def solReference(): String = rng.alphanumeric.take(20).mkString
  private def dob(): Int = 10 + rng.nextInt(100 - 10) + 1
  private def caseSummaryText(): String = rng.alphanumeric.take(20).mkString
  private def caseDescriptionText(): String = rng.alphanumeric.take(30).mkString


  val DocStoreBashURL = config.getString("docStoreBashURL")

  println("DocStoreBashURL url - Creates a list of Stored Documents by uploading a list of binary/text files : " + DocStoreBashURL)

  val fileProviderSeq: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").queue

  val fileProviderRand: RecordSeqFeederBuilder[String] = csv("listoffiles.csv").random

  val randcaseType = new Random(System.currentTimeMillis())

  val caseEventTypeValue = Array("openCase")

  val caseTypeValue_random_index = randcaseType.nextInt(caseEventTypeValue.length)

  val EventId = caseEventTypeValue(caseTypeValue_random_index)

  def PickCaseType(): String = EventId


  println("caseTypeText Value   " + EventId)
  println("caseTypeText Value   " + PickCaseType())

  var CreateCaseUrl = caseDataUrl(config.getString("createFPLCaseAppURL"))

  println("create case url: " + CreateCaseUrl)

  val caseTypeValue = Array("CARE_SUPERVISION_EPO")

  val jurisdictionsValue = Array("PUBLICLAW")


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


  val EventBodyMain =StringBody("""{"data": {"caseName": "KJ Endley Council v Smith/Tate/Jones ${CaseName}","caseLocalAuthority": "HN","caseIDReference": "${CaseIDReference}","EPO_REASONING_SHOW": ["SHOW_FIELD"],"groundsForEPO": {"reason": ["URGENT_ACCESS_TO_CHILD_IS_OBSTRUCTED","HARM_IF_KEPT_IN_CURRENT_ACCOMMODATION","HARM_IF_NOT_MOVED_TO_NEW_ACCOMMODATION"]},"grounds": {"thresholdReason": ["beyondControl","noCare"],"thresholdDetails": "Details ${ThresholdDetails}"},"hearing": {"timeFrame": "Within 18 days","reason": null,"reason2Days": null,"reason7Days": null,"reason12Days": null,"type": "Standard case management hearing","type_GiveReason": "Reason ${Type_GiveReason}","withoutNotice": "No","withoutNoticeReason": null,"reducedNotice": "No","reducedNoticeReason": null,"respondentsAware": "No","respondentsAwareReason": null},"internationalElement": {"possibleCarer": "No","possibleCarerReason": null,"significantEvents": "No","significantEventsReason": null,"issues": "No","issuesReason": null,"proceedings": "No","proceedingsReason": null,"internationalAuthorityInvolvement": "No","internationalAuthorityInvolvementDetails": null},"factorsParenting": {"alcoholDrugAbuse": "No","alcoholDrugAbuseReason": null,"domesticViolence": "No","domesticViolenceReason": null,"anythingElse": "No","anythingElseReason": null},"documents_socialWorkChronology_document": {"documentStatus": "Attached","statusReason": null,"typeOfDocument": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}},"documents_socialWorkStatement_document": {"documentStatus": "Attached","statusReason": null,"typeOfDocument": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}},"documents_socialWorkAssessment_document": {"documentStatus": "Attached","statusReason": null,"typeOfDocument": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}},"documents_socialWorkCarePlan_document": {"documentStatus": "Attached","statusReason": null,"typeOfDocument": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}},"documents_socialWorkOther": [{"id": null,"value": {"documentTitle": "Document ${DocumentTitle}","typeOfDocument": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}}}],"courtBundle": {"document": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}},"applicant": {"name": "John ${ApplicantName}","personToContact": "Smith ${ApplicantpersonToContact}","jobTitle": "Mr","address": {"AddressLine1": "${AppellantAddress} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CO3 0RN","Country": "United Kingdom"},"mobile": "7${ApplicantMobile}","telephone": "020${ApplicantPhone}","email": "joe.bloggs@la.gov.uk"},"solicitor": {"name": "Solicitor Chris Len ${ApplicantName}","mobile": "7${ApplicantMobile}","telephone": "020 ${ApplicantPhone}","email": "joe.bloggs@la.gov.uk","dx": null,"reference": "SOLRef - ${SolReference}"},"risks": {"physicalHarm": "No","physicalHarmOccurrences": [],"emotionalHarm": "No","emotionalHarmOccurrences": [],"sexualAbuse": "No","sexualAbuseOccurrences": [],"neglect": "No","neglectOccurrences": []},"proceeding": {"onGoingProceeding": "No","proceedingStatus": null,"caseNumber": null,"started": null,"ended": null,"ordersMade": null,"judge": null,"children": null,"guardian": null,"sameGuardianNeeded": null,"sameGuardianDetails": null},"hearingPreferences": {"interpreter": "No","interpreterDetails": null,"intermediary": "No","intermediaryDetails": null,"litigation": "No","litigationDetails": null,"learningDisability": "No","learningDisabilityDetails": null,"welsh": "No","welshDetails": null,"extraSecurityMeasures": "No","extraSecurityMeasuresDetails": null},"allocationProposal": {"proposal": "High Court judge","proposalReason": "Reason ${Type_GiveReason}"},"others": {"firstOther": {"name": "Person 1 Stu Plummer ${ApplicantName}","DOB": "19${Dob}-02-12","gender": "Male","genderIdentification": null,"birthPlace": "London","address": {"AddressLine1": "${AppellantAddress} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CO3 0RN","Country": "United Kingdom"},"telephone": "020${ApplicantPhone}","childInformation": "person’s relationship carer","detailsHidden": "No","detailsHiddenReason": null,"litigationIssues": "NO","litigationIssuesDetails": null},"additionalOthers": []},"respondents": {"firstRespondent": {"name": "Respondent 1 Steve Kahoe ${ApplicantName}","dob": "19${Dob}-02-12","gender": "Male","genderIdentify": null,"placeOfBirth": "Newport","address": {"AddressLine1": "${AppellantAddress} Villa Road","AddressLine2": "","AddressLine3": "Stanway","PostTown": "Colchester","County": "","PostCode": "CO3 0RN","Country": "United Kingdom"},"telephone": "029${ApplicantPhone}","relationshipToChild": "respondent’s relationship - ${CaseSummaryText}","contactDetailsHidden": "No","contactDetailsHiddenReason": null,"litigationIssues": "NO","litigationIssuesDetails": null},"additional": []},"orders": {"orderType": ["OTHER","EMERGENCY_PROTECTION_ORDER","EDUCATION_SUPERVISION_ORDER","INTERIM_SUPERVISION_ORDER","SUPERVISION_ORDER","INTERIM_CARE_ORDER","CARE_ORDER"],"emergencyProtectionOrders": ["SEARCH_FOR_CHILD","ENTRY_PREMISES","CHILD_WHEREABOUTS"],"emergencyProtectionOrderDetails": null,"emergencyProtectionOrderDirections": ["EXCLUSION_REQUIREMENT","MEDICAL_PRACTITIONER","CHILD_MEDICAL_ASSESSMENT","CONTACT_WITH_NAMED_PERSON"],"emergencyProtectionOrderDirectionDetails": null,"otherOrder": "Which order do you need - ${CaseSummaryText}","directions": "No","directionDetails": null},"submissionConsentLabel": "Evidence attached","submissionConsent": ["agree"],"submittedForm": {"document_url": "http://${DMURL}/${Document_ID}","document_binary_url": "http://${DMURL}/${Document_ID}/binary","document_filename": "${filename}"}},"event": {"id": "openCase","summary": "Performance Test Case Summary - ${CaseSummaryText}","description": "Performance Test Case Description - ${CaseDescriptionText}"},"event_token": """" + "${eventToken}" + """","ignore_warning": false,"draft_id": null}""")


  val token = CcdTokenGenerator.generateGatewayS2SToken()
  val userToken = CcdTokenGenerator.generateWebUserToken()


  val   CreateFPLCaseDataSCN = scenario("Create FPL Case Data").during(TotalRunDuration minutes) {
    exec(
      _.setAll(
        ("CaseName", caseName()),
        ("CaseIDReference", caseIDReference()),
        ("ThresholdDetails", thresholdDetails()),
        ("Type_GiveReason", type_GiveReason()),
        ("DocumentTitle",documentTitle()),
        ("ApplicantName",applicantName()),
        ("ApplicantpersonToContact",applicantpersonToContact()),
        ("AppellantAddress",appellantAddress()),
        ("ApplicantMobile",applicantMobile()),
        ("ApplicantPhone",applicantPhone()),
        ("SolReference",solReference()),
        ("Dob",dob()),
        ("CaseSummaryText",caseSummaryText()),
        ("CaseDescriptionText",caseDescriptionText())
      )
    ) .feed(fileProviderRand)
      .exec(
      http("TX01_CCD_CreateCaseEndpoint_FPL_createcase_eventtoken")
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
      http("TX02_CCD_CreateCaseEndpoint_FPL_createcasedata")
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

