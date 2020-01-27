package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases;

import static org.junit.Assert.*;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpoints;

public class TestCaseEndpoints extends TestEndpoints {

  private String caseId;
  private String uprn;
  private CaseDTO caseDTO;
  private List<CaseDTO> caseDTOList;
  private Exception exception;
  private static final Logger log = LoggerFactory.getLogger(TestCaseEndpoints.class);
  private String ccSmokeTestUrl;
  private String mockCaseSvcSmokeTestUrl;
  private String telephoneEndpointUrl;

  @Given("I am about to do a smoke test by going to a contact centre endpoint")
  public void i_am_about_to_do_a_smoke_test_by_going_to_a_contact_centre_endpoint() {
    log.info("About to check that the Contact Centre service is running...");
  }

  @Then("I do the smoke test and receive a response of OK from the contact centre service")
  public void i_do_the_smoke_test_and_receive_a_response_of_OK_from_the_contact_centre_service() {
    try {
      HttpStatus contactCentreStatus = checkContactCentreRunning();
      log.with(contactCentreStatus).info("Smoke Test: The response from " + ccSmokeTestUrl);
      assertEquals(
          "THE CONTACT CENTRE SERVICE MAY NOT BE RUNNING - it does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error(
          "THE CONTACT CENTRE SERVICE MAY NOT BE RUNNING: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("THE CONTACT CENTRE SERVICE MAY NOT BE RUNNING: An unexpected has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Given("I am about to do a smoke test by going to a mock case api endpoint")
  public void i_am_about_to_do_a_smoke_test_by_going_to_a_mock_case_api_endpoint() {
    log.info("About to check that the mock case api service is running...");
  }

  @Then("I do the smoke test and receive a response of OK from the mock case api service")
  public void i_do_the_smoke_test_and_receive_a_response_of_OK_from_the_mock_case_api_service() {
    try {
      HttpStatus mockCaseApiStatus = checkMockCaseApiRunning();
      log.with(mockCaseApiStatus).info("Smoke Test: The response from " + mockCaseSvcSmokeTestUrl);
      assertEquals(
          "THE MOCK CASE API SERVICE MAY NOT BE RUNNING - it does not give a response code of 200",
          HttpStatus.OK,
          mockCaseApiStatus);
    } catch (ResourceAccessException e) {
      log.error(
          "THE MOCK CASE API SERVICE MAY NOT BE RUNNING: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("THE MOCK CASE API SERVICE MAY NOT BE RUNNING: An unexpected has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Given("I have a valid case ID {string}")
  public void i_have_a_valid_case_ID(String caseId) {
    this.caseId = caseId;
  }

  @When("I Search cases By case ID {string}")
  public void i_Search_cases_By_case_ID(String showCaseEvents) {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment(caseId)
            .queryParam("caseEvents", showCaseEvents);
    caseDTO = getRestTemplate().getForObject(builder.build().encode().toUri(), CaseDTO.class);
  }

  @Then("the correct case for my case ID is returned {int}")
  public void the_correct_case_for_my_case_ID_is_returned(Integer uprn) {
    assertNotNull("Case Query Response must not be null", caseDTO);
    assertEquals(
        "Case Query Response UPRN must match", caseDTO.getUprn().getValue(), uprn.longValue());
  }

  @Then("the correct number of events are returned {string} {int}")
  public void the_correct_number_of_events_are_returned(
      String showCaseEvents, Integer expectedCaseEvents) {
    if (!Boolean.parseBoolean(showCaseEvents)) {
      assertNull("Events must be null", caseDTO.getCaseEvents());
    } else {
      assertEquals(
          "Must have the correct number of case events",
          Long.valueOf(expectedCaseEvents),
          Long.valueOf(caseDTO.getCaseEvents().size()));
    }
  }

  @Given("I have an invalid case ID {string}")
  public void i_have_an_invalid_case_ID(String caseId) {
    this.caseId = caseId;
  }

  @When("I Search for cases By case ID")
  public void i_Search_for_cases_By_case_ID() {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment(caseId);
    try {
      caseDTO = getRestTemplate().getForObject(builder.build().encode().toUri(), CaseDTO.class);
    } catch (HttpClientErrorException httpClientErrorException) {
      this.exception = httpClientErrorException;
    } catch (HttpServerErrorException httpServerErrorException) {
      this.exception = httpServerErrorException;
    }
  }

  @Then("An error is thrown and no case is returned {string}")
  public void an_error_is_thrown_and_no_case_is_returned(String httpError) {
    assertTrue(
        "The correct http status must be returned " + httpError,
        exception.getMessage().trim().contains(httpError));
  }

  @Given("I have a valid UPRN {string}")
  public void i_have_a_valid_UPRN(String uprn) {
    this.uprn = uprn;
  }

  @When("I Search cases By UPRN")
  public void i_Search_cases_By_UPRN() {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprn);
    try {
      ResponseEntity<List<CaseDTO>> caseResponse =
          getRestTemplate()
              .exchange(
                  builder.build().encode().toUri(),
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<CaseDTO>>() {});
      caseDTOList = caseResponse.getBody();
    } catch (HttpClientErrorException httpClientErrorException) {
      this.exception = httpClientErrorException;
    }
  }

  @Then("the correct cases for my UPRN are returned {string}")
  public void the_correct_cases_for_my_UPRN_are_returned(String caseIds) {
    final List<String> caseIdList = Arrays.stream(caseIds.split(",")).collect(Collectors.toList());
    caseDTOList.forEach(
        caseDetails -> {
          String caseID = caseDetails.getId().toString().trim();
          assertTrue("case ID must be in case list - ", caseIdList.contains(caseID));
        });
  }

  @Given("I have an invalid UPRN {string}")
  public void i_have_an_invalid_UPRN(String uprn) {
    this.uprn = uprn;
  }

  @When("I Search cases By invalid UPRN")
  public void i_Search_cases_By_invalid_UPRN() {
    exception = null;
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprn);
    try {
      ResponseEntity<List<CaseDTO>> caseResponse =
          getRestTemplate()
              .exchange(
                  builder.build().encode().toUri(),
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<CaseDTO>>() {});
      caseDTOList = caseResponse.getBody();
    } catch (HttpClientErrorException httpClientErrorException) {
      exception = httpClientErrorException;
    }
  }

  @Then("no cases for my UPRN are returned {string}")
  public void no_cases_for_my_UPRN_are_returned(String httpError) {
    assertNotNull("Should throw an exception", exception);
    assertTrue(
        "Invalid UPRN causes http status " + httpError,
        exception.getMessage() != null && exception.getMessage().contains(httpError));

    assertNull("UPRN response must be null", caseDTOList);
  }

  @Given("the CC advisor has the respondent address")
  public void the_CC_advisor_has_the_respondent_address() {
    log.info(
        "nothing to do here - we can assume that the CC advisor has the respondent's address and its UPRN");
  }

  @Given("the respondent case type is a household")
  public void the_respondent_case_type_is_a_household() {
    log.info("nothing to do here - we can assume that the case type is a household");
  }

  @When("the CC advisor confirms the address")
  public void the_CC_advisor_confirms_the_address() {
    log.info("nothing to do here - the CC advisor clicks a button to confirm the address");
  }

  @When("confirms the CaseType=HH")
  public void confirms_the_CaseType_HH() {
    log.info(
        "The CC advisor clicks a button to confirm that the case type is HH and then launch EQ...");

    try {
      HttpStatus contactCentreStatus = getEqTokenForHH();
      log.with(contactCentreStatus)
          .info("Launch EQ for HH: The response from " + telephoneEndpointUrl);
      assertEquals(
          "LAUNCHING EQ FOR HH HAS FAILED -  the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("LAUNCHING EQ FOR HH HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("LAUNCHING EQ FOR HH HAS FAILED: An unexpected has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  private HttpStatus checkContactCentreRunning() {
    log.info("Entering checkContactCentreRunning method");
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl).port(ccBasePort).pathSegment("fulfilments");

    ccSmokeTestUrl = builder.build().encode().toUri().toString();

    log.info(
        "Using the following endpoint to check that the contact centre service is running: "
            + ccSmokeTestUrl);

    ResponseEntity<List<FulfilmentDTO>> fulfilmentResponse =
        getRestTemplate()
            .exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<FulfilmentDTO>>() {});

    return fulfilmentResponse.getStatusCode();
  }

  private HttpStatus checkMockCaseApiRunning() {
    log.info("Entering checkMockCaseApiRunning method");
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(mcsBaseUrl)
            .port(mcsBasePort)
            .pathSegment("cases")
            .pathSegment("info");

    RestTemplate restTemplate = getAuthenticationFreeRestTemplate();

    mockCaseSvcSmokeTestUrl = builder.build().encode().toUri().toString();

    log.info(
        "Using the following endpoint to check that the mock case api service is running: "
            + mockCaseSvcSmokeTestUrl);

    ResponseEntity<String> mockCaseApiResponse =
        restTemplate.getForEntity(builder.build().encode().toUri(), String.class);

    return mockCaseApiResponse.getStatusCode();
  }

  private HttpStatus getEqTokenForHH() {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("3305e937-6fb1-4ce1-9d4c-077f147789ab")
            .queryParam("agentId", 1)
            .queryParam("individual", false);

    telephoneEndpointUrl = builder.build().encode().toUri().toString();

    log.info("Using the following endpoint to launch EQ for HH: " + telephoneEndpointUrl);

    ResponseEntity<String> ccLaunchEqResponse =
        getRestTemplate().getForEntity(builder.build().encode().toUri(), String.class);

    return ccLaunchEqResponse.getStatusCode();
  }
}
