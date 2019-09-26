package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.apache.commons.codec.binary.Base64;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressQueryResponseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.PostcodeQueryRequestDTO;
import uk.gov.ons.ctp.integration.contcencucumber.main.SpringIntegrationTest;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;

public class TestAddressEndpoints extends SpringIntegrationTest {

    String postcode = "";
    final static String POSTCODE_URL = "http://localhost:8171/addresses/postcode";
    AddressQueryResponseDTO addressQueryResponseDTO;

    @Given("I have a valid Postcode {string}")
    public void i_have_a_valid_Postcode(final String postcode) {
        this.postcode = postcode;
    }

    @When("I Search Addresses By Postcode")
    public void i_Search_Addresses_By_Postcode() {
        RestTemplate restTemplate = new RestTemplateBuilder().basicAuthentication("serco_cks", "temporary").build();
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(POSTCODE_URL)
                .queryParam("postcode", postcode);
        addressQueryResponseDTO = restTemplate.getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
    }

    @Then("A list of addresses for my postcode is returned")
    public void a_list_of_addresses_for_my_postcode_is_returned() {
        Assert.assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
        Assert.assertNotEquals("", 0, addressQueryResponseDTO.getAddresses().size());
        addressQueryResponseDTO.getAddresses().forEach( e -> {
            Assert.assertTrue("Address must contain test postcode: " + postcode, e.getFormattedAddress().contains(postcode));
        });
    }
}
