package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressQueryResponseDTO;
import uk.gov.ons.ctp.integration.contcencucumber.main.SpringIntegrationTest;

import static org.junit.Assert.*;

public class TestAddressEndpoints extends SpringIntegrationTest {

    @Value("${contact-centre.host}")
    private String ccBaseUrl;
    @Value("${contact-centre.port}")
    private String ccBasePort;
    @Value("${contact-centre.username}")
    private String ccUsername;
    @Value("${contact-centre.password}")
    private String ccPassword;

    private AddressQueryResponseDTO addressQueryResponseDTO;
    private String postcode = "";

    @Given("I have a valid Postcode {string}")
    public void i_have_a_valid_Postcode(final String postcode) {
        this.postcode = postcode;
    }

    @When("I Search Addresses By Postcode")
    public void i_Search_Addresses_By_Postcode() {
        final String postcodeUrl = ccBaseUrl + ":" + ccBasePort + "/addresses/postcode";
        RestTemplate restTemplate = new RestTemplateBuilder().basicAuthentication(ccUsername, ccPassword).build();
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(postcodeUrl)
                .queryParam("postcode", postcode);
        addressQueryResponseDTO = restTemplate.getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
    }

    @Then("A list of addresses for my postcode is returned")
    public void a_list_of_addresses_for_my_postcode_is_returned() {
        assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
        assertNotEquals("Address list size must not be zero", 0, addressQueryResponseDTO.getAddresses().size());
    }

    @Given("I have an invalid Postcode {string}")
    public void i_have_an_invalid_Postcode(String postcode) {
        this.postcode = postcode;
    }

    @When("I Search Addresses By Invalid Postcode")
    public void i_Search_Addresses_By_Invalid_Postcode() {
        final String postcodeUrl = ccBaseUrl + ":" + ccBasePort + "/addresses/postcode";
        RestTemplate restTemplate = new RestTemplateBuilder().basicAuthentication("serco_cks", "temporary").build();
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(postcodeUrl)
                .queryParam("postcode", postcode);
            try {
                addressQueryResponseDTO = restTemplate.getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
            }
            catch (HttpClientErrorException hcee) {
              assertNull(" Invalid format Address Query Response must be null", addressQueryResponseDTO);
            }
    }

    @Then("An empty list of addresses for my postcode is returned")
    public void an_empty_list_of_addresses_for_my_postcode_is_returned() {
        if (addressQueryResponseDTO != null) {
            assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
            assertEquals("Address list size must be zero", 0, addressQueryResponseDTO.getAddresses().size());
        }
    }
}
