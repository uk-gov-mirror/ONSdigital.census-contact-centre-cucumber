package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.address;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressQueryResponseDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestBase;

public class TestAddressEndpoints extends TestBase {

  private AddressQueryResponseDTO addressQueryResponseDTO;
  //  private AddressUpdateRequestDTO addressUpdateRequestDTO;
  private String postcode = "";
  private String addressSearchString = "";
  private String uprn;

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

  //  @Given("I have a new uprn and address {string} {string}")
  //  public void i_have_a_new_uprn_and_address(String uprn, String address) {
  //    this.uprn = uprn;
  //    addressUpdateRequestDTO = populateAddress(address);
  //  }

  //  @When("I post the new address")
  //  public void i_post_the_new_address() {
  //    UriComponentsBuilder builder =
  //        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
  //            .port(ccBasePort)
  //            .pathSegment("addresses")
  //            .pathSegment(uprn);
  //    getRestTemplate()
  //        .postForObject(builder.build().encode().toUri(), addressUpdateRequestDTO, String.class);
  //  }

  @Then("The new address is posted successfully")
  public void the_new_address_is_posted_successfully() {
    // Write code here that turns the phrase above into concrete actions
    throw new cucumber.api.PendingException();
  }

  //  private AddressUpdateRequestDTO populateAddress(final String address) {
  //    final String[] addressParamater = address.split(",");
  //    return new AddressUpdateRequestDTO(
  //        addressParamater[0], // address line 1
  //        addressParamater[1], // address line 2
  //        addressParamater[2], // address line 3
  //        addressParamater[3], // town name
  //        addressParamater[4], // region
  //        addressParamater[5], // postcode
  //        AddressUpdateRequestDTO.Category.valueOf(addressParamater[6]), // category
  //        AddressUpdateRequestDTO.Type.valueOf(addressParamater[7]), // type
  //        addressParamater[8], // title
  //        addressParamater[9], // forename
  //        addressParamater[10], // surname
  //        new Date()); // date time
  //  }
}
