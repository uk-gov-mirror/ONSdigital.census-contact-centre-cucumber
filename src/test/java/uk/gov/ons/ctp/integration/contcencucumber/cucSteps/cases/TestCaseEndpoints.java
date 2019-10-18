package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpoints;

import java.io.IOException;

import static org.junit.Assert.*;

public class TestCaseEndpoints extends TestEndpoints {

    private String caseId;
    private CaseDTO caseDTO;
    private HttpClientErrorException httpClientErrorException;

    @Given("I have a valid case ID {string}")
    public void i_have_a_valid_case_ID(String caseId) {
       this.caseId = caseId;
    }

    @When("I Search cases By case ID")
    public void i_Search_cases_By_case_ID() throws IOException {
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(ccBaseUrl)
                .port(ccBasePort)
                .pathSegment("cases")
                .pathSegment(caseId);
        caseDTO = getRestTemplate().getForObject(builder.build().encode().toUri(), CaseDTO.class);
    }

    @Then("the correct case for my case ID is returned {int}")
    public void the_correct_case_for_my_case_ID_is_returned(Integer uprn) {
        assertNotNull("Case Query Response must not be null", caseDTO );
        assertEquals("Case Query Response UPRN must match", caseDTO.getUprn().getValue(), uprn.longValue());
    }

    @Given("I have an invalid case ID {string}")
    public void i_have_an_invalid_case_ID(String string) {
        this.caseId = caseId;
    }

    @When("I Search for cases By case ID")
    public void i_Search_for_cases_By_case_ID() {
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(ccBaseUrl)
                .port(ccBasePort)
                .pathSegment("cases")
                .pathSegment(caseId);
        try {
            caseDTO = getRestTemplate().getForObject(builder.build().encode().toUri(), CaseDTO.class);
        }
        catch (HttpClientErrorException httpClientErrorException) {
            this.httpClientErrorException = httpClientErrorException;
        }
    }

    @Then("An error is thrown and no case is returned {string}")
    public void an_error_is_thrown_and_no_case_is_returned(String httpError) {
        assertTrue("The correct http error must be returned", httpClientErrorException.getMessage().trim().contains(httpError));


    }


}
