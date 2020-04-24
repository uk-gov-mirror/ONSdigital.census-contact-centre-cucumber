package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.address;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressQueryResponseDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestBase;

public class TestAddressEndpoints extends TestBase {

  private static final Logger log = LoggerFactory.getLogger(TestAddressEndpoints.class);
  private AddressQueryResponseDTO addressQueryResponseDTO;
  private String postcode = "";
  private String addressSearchString = "";
  private String addressEndpointUrl;
  private String aimsEndpointBody;
  private String uprnStr;
  private String mcsUprnEndpointUrl;
  private String ccUprnEndpointUrl;
  private String status = "";

  @Given("I have a valid Postcode {string}")
  public void i_have_a_valid_Postcode(final String postcode) {
    this.postcode = postcode;
  }

  @When("I Search Addresses By Postcode")
  public void i_Search_Addresses_By_Postcode() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .pathSegment("postcode")
            .queryParam("postcode", postcode);
    addressQueryResponseDTO =
        getRestTemplate()
            .getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
  }

  @Then("A list of addresses for my postcode is returned")
  public void a_list_of_addresses_for_my_postcode_is_returned() {
    assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
    assertNotEquals(
        "Address list size must not be zero", 0, addressQueryResponseDTO.getAddresses().size());
  }

  @Given("I have an invalid Postcode {string}")
  public void i_have_an_invalid_Postcode(String postcode) {
    this.postcode = postcode;
  }

  @When("I Search Addresses By Invalid Postcode")
  public void i_Search_Addresses_By_Invalid_Postcode() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .pathSegment("postcode")
            .queryParam("postcode", postcode);
    try {
      addressQueryResponseDTO =
          getRestTemplate()
              .getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
    } catch (HttpClientErrorException hcee) {
      assertNull(" Invalid format Address Query Response must be null", addressQueryResponseDTO);
    }
  }

  @Then("An empty list of addresses for my postcode is returned")
  public void an_empty_list_of_addresses_for_my_postcode_is_returned() {
    if (addressQueryResponseDTO != null) {
      assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
      assertEquals(
          "Address list size must be zero", 0, addressQueryResponseDTO.getAddresses().size());
    }
  }

  @Given("I have a valid address {string}")
  public void i_have_a_valid_address(String addressSearchString) {
    this.addressSearchString = addressSearchString;
  }

  @When("I Search Addresses By Address Search")
  public void i_Search_Addresses_By_Address_Search() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .queryParam("input", addressSearchString);
    addressQueryResponseDTO =
        getRestTemplate()
            .getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
  }

  @Then("A list of addresses for my search is returned")
  public void a_list_of_addresses_for_my_search_is_returned() {
    assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
    assertNotEquals(
        "Address list size must not be zero", 0, addressQueryResponseDTO.getAddresses().size());
  }

  @Given("I have an invalid address {string}")
  public void i_have_an_invalid_address(String addressSearchString) {
    this.addressSearchString = addressSearchString;
  }

  @When("I Search invalid Addresses By Address Search")
  public void i_Search_invalid_Addresses_By_Address_Search() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .queryParam("input", addressSearchString);
    try {
      addressQueryResponseDTO =
          getRestTemplate()
              .getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
    } catch (HttpClientErrorException hcee) {
      assertNull(" Invalid format Address Query Response must be null", addressQueryResponseDTO);
    }
  }

  @Then("An empty list of addresses for my search is returned")
  public void an_empty_list_of_addresses_for_my_search_is_returned() {
    if (addressQueryResponseDTO != null) {
      assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
      assertEquals(
          "Address list size must be zero", 0, addressQueryResponseDTO.getAddresses().size());
    }
  }

  @Given("the respondent calls the CC with a fulfilment request")
  public void the_respondent_calls_the_CC_with_a_fulfilment_request() {
    log.info("Nothing to do here: the respondent calls the CC with a fulfilment request");
  }

  @Given("the respondent address exists in AIMS")
  public void the_respondent_address_exists_in_AIMS() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .queryParam("input", "1, West Grove Road, Exeter, EX2 4LU");
    addressEndpointUrl = builder.build().encode().toUri().toString();

    log.info("Using the following endpoint to check address exists in AIMS: " + addressEndpointUrl);

    ResponseEntity<String> aimsEndpointResponse =
        getRestTemplate().getForEntity(builder.build().encode().toUri(), String.class);
    aimsEndpointBody = aimsEndpointResponse.getBody();
    log.with(aimsEndpointBody).info("The response body");
    log.with(aimsEndpointResponse.getStatusCode()).info("The response status");
    assertEquals(
        "THE ADDRESS MAY NOT EXIST IN AIMS - AIMS does not give a response code of 200",
        HttpStatus.OK,
        aimsEndpointResponse.getStatusCode());
  }

  @When("the CC agent searches for the address")
  public void the_CC_agent_searches_for_the_address() {
    log.info("Nothing to do here: the CC agent searches for the address");
  }

  @Then(
      "the CC SVC returns address attributes with region code, address type and establishment type")
  public void
      the_CC_SVC_returns_address_attributes_with_region_code_address_type_and_establishment_type() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .queryParam("input", "1, West Grove Road, Exeter, EX2 4LU");

    ResponseEntity<AddressQueryResponseDTO> addressQueryResponse =
        getRestTemplate()
            .exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AddressQueryResponseDTO>() {});

    addressEndpointUrl = builder.build().encode().toUri().toString();

    log.info(
        "Using the following endpoint to check CCSVC returns expected values: "
            + addressEndpointUrl);

    log.with(addressQueryResponse).info("The address query response here");

    AddressQueryResponseDTO addressQueryBody = addressQueryResponse.getBody();

    List<AddressDTO> addressesFound = addressQueryBody.getAddresses();

    int i = 0;
    boolean addressExists = false;
    String addressToFind = "1 West Grove Road, Exeter, EX2 4LU";
    String addressFound = "";
    int indexFound = 500;
    log.info(
        "The indexFound value defaults to 500 as that will cause an exception if it does not get reset in the while loop");
    while ((i < addressesFound.size()) && (addressExists == false)) {
      addressFound = addressesFound.get(i).getFormattedAddress();
      log.with(addressFound).info("This is the address that was found in AIMS where i is: " + i);
      if (addressFound.equals(addressToFind)) {
        log.with(addressFound).info("This is the address that was found in AIMS");
        addressExists = true;
        indexFound = i;
      }
      i++;
    }
    log.info("The value of i: " + i);
    assertEquals(
        "The address query response does not contain the correct address",
        addressToFind,
        addressFound);

    String regionCode = null;
    regionCode = addressesFound.get(indexFound).getRegion();
    log.with(regionCode).info("This is the region code that was found in AIMS");
    assertNotNull(regionCode);

    String addressType = null;
    addressType = addressesFound.get(indexFound).getAddressType();
    log.with(addressType).info("This is the address type that was found in AIMS");
    assertNotNull(addressType);

    String estabType = null;
    estabType = addressesFound.get(indexFound).getEstabType();
    log.with(estabType).info("This is the establishment type that was found in AIMS");
    assertNotNull(estabType);
  }

  @Given("the CC agent has confirmed the respondent address")
  public void the_CC_agent_has_confirmed_the_respondent_address() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .queryParam("input", "1, West Grove Road, Exeter, EX2 4LU");

    ResponseEntity<AddressQueryResponseDTO> addressQueryResponse =
        getRestTemplate()
            .exchange(
                builder.build().encode().toUri(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AddressQueryResponseDTO>() {});

    log.with(addressQueryResponse).info("The address query response here");

    AddressQueryResponseDTO addressQueryBody = addressQueryResponse.getBody();

    List<AddressDTO> addressesFound = addressQueryBody.getAddresses();

    int i = 0;
    boolean addressExists = false;
    String addressToFind = "1 West Grove Road, Exeter, EX2 4LU";
    String addressFound = "";
    int indexFound = 500;
    log.info(
        "The indexFound value defaults to 500 as that will cause an exception if it does not get reset in the while loop");
    while ((i < addressesFound.size()) && (addressExists == false)) {
      addressFound = addressesFound.get(i).getFormattedAddress();

      if (addressFound.equals(addressToFind)) {
        log.with(addressFound).info("This is the address that was found in AIMS");
        addressExists = true;
        indexFound = i;
      }
      i++;
    }
    assertEquals(
        "The address query response does not contain the correct address",
        addressToFind,
        addressFound);

    uprnStr = addressesFound.get(indexFound).getUprn();
  }

  @Given("the case service does not have any case created for the address in question")
  public void the_case_service_does_not_have_any_case_created_for_the_address_in_question() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(mcsBaseUrl)
            .port(mcsBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprnStr);
    mcsUprnEndpointUrl = builder.build().encode().toUri().toString();

    log.info(
        "Using the following mock case service endpoint to check case does not exist for uprn in question: "
            + mcsUprnEndpointUrl);

    try {
      getRestTemplate().getForEntity(builder.build().encode().toUri(), String.class);
    } catch (RestClientException e) {
      log.with(e.getMessage())
          .info("catching the error returned by the mock case service endpoint");
      status = e.getMessage();
    }

    log.info("The response status: " + status);
  }

  @When("Get\\/Case API returns a {int} error because there is no case found")
  public void get_Case_API_returns_a_error_because_there_is_no_case_found(Integer int1) {
    assertEquals(
        "THE CASE SHOULD NOT EXIST - the mock case service endpoint should give a response code of 404",
        "404 Not Found",
        status);
  }
  
  @When("CC SVC creates a fake Case with the address details from AIMS")
  public void cc_SVC_creates_a_fake_Case_with_the_address_details_from_AIMS() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprnStr); 
    ccUprnEndpointUrl = builder.build().encode().toUri().toString();
    
    log.info("As the case does not exist in the case service the endpoint {} should cause a new fake case to be created", ccUprnEndpointUrl);
    
//    getRestTemplate().getForEntity(builder.build().encode().toUri(), String.class);
  }

}
