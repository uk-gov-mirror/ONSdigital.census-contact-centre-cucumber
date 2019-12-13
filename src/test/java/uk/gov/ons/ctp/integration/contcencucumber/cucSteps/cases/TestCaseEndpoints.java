package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases;

import static org.junit.Assert.*;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpoints;

public class TestCaseEndpoints extends TestEndpoints {

  private String caseId;
  private String uprn;
  private CaseDTO caseDTO;
  private List<CaseDTO> caseDTOList;
  private Exception exception;

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
}
