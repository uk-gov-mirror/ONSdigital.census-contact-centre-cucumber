package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.fulfilments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.model.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressQueryResponseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseType;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.Region;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.ResetMockCaseApiAndPostCasesBase;
import uk.gov.ons.ctp.integration.contcencucumber.main.service.ProductService;

public class TestFulfilmentsEndpoints extends ResetMockCaseApiAndPostCasesBase {

  private List<FulfilmentDTO> fulfilmentDTOList;
  private AddressQueryResponseDTO addressQueryResponseDTO;
  private String addressSearchString;
  private String uprn;
  private List<CaseDTO> caseDTOList;
  private CaseDTO caseDTO;
  private String requestChannel = "";
  private String caseForUprnUrl;
  private List<CaseDTO> listOfCasesWithUprn;

  @Autowired private ProductService productService;

  @Given("I Search fulfilments")
  public void i_Search_fulfilments() {
    searchFulfillments(caseDTO.getCaseType(), caseDTO.getRegion(), "true");
  }

  @Given("I Search fulfilments {string} {string} {string}")
  public void i_Search_fulfilments(String caseType, String region, String individual) {
    searchFulfillments(caseType, region, individual);
  }

  private void searchFulfillments(String caseType, String region, String individual) {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("/fulfilments")
            .queryParam("caseType", caseType)
            .queryParam("region", region)
            .queryParam("individual", individual);

    try {
      ResponseEntity<List<FulfilmentDTO>> fulfilmentResponse =
          getRestTemplate()
              .exchange(
                  builder.build().encode().toUri(),
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<FulfilmentDTO>>() {});
      fulfilmentDTOList = fulfilmentResponse.getBody();
    } catch (HttpClientErrorException httpClientErrorException) {
      fail(httpClientErrorException.getMessage());
    }
  }

  @Then("A list of fulfilments is returned of the correct products {string} {string} {string}")
  public void a_list_of_fulfilments_is_returned_of_the_correct_products(
      String caseType, String region, String individual) throws CTPException {

    this.requestChannel = "CC";
    List<Product> expectedProducts = getExpectedProducts(caseType, region, individual);

    assertEquals(
        "Fulfilments list size should be " + expectedProducts.size(),
        Integer.valueOf(expectedProducts.size()),
        Integer.valueOf(fulfilmentDTOList.size()));
    fulfilmentDTOList.forEach(
        fulfilment -> {
          assertTrue(
              "Fulfilment should be of correct caseType",
              fulfilmentContainsCaseType(fulfilment, caseType));
          assertTrue(
              "Fulfilment should be of correct region",
              fulfilment.getRegions().contains(Region.valueOf(region)));
        });
  }

  private boolean fulfilmentContainsCaseType(final FulfilmentDTO dto, final String caseType) {
    boolean containsCaseType = false;
    for (CaseType caseType1 : dto.getCaseTypes()) {
      if (caseType1.name().equalsIgnoreCase(caseType)) {
        containsCaseType = true;
      }
    }
    return containsCaseType;
  }

  @Given("I have a valid address search String {string}")
  public void i_have_a_valid_address_search_String(String addressSearchString) {
    this.addressSearchString = addressSearchString;
  }

  @When("I Search Addresses By Address Search String")
  public void i_Search_Addresses_By_Address_Search_String() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("addresses")
            .queryParam("input", addressSearchString);
    addressQueryResponseDTO =
        getRestTemplate()
            .getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
  }

  @Then("A list of addresses for my search is returned containing the address I require")
  public void a_list_of_addresses_for_my_search_is_returned_containing_the_address_I_require() {
    assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
    assertTrue("Address list size must be > 0", addressQueryResponseDTO.getAddresses().size() > 0);
  }

  @Given("I have a valid UPRN from my found address {string}")
  public void i_have_a_valid_UPRN_from_my_found_address(final String expectedUPRN) {

    List<AddressDTO> addressList =
        addressQueryResponseDTO
            .getAddresses()
            .stream()
            .filter(aq -> aq.getUprn().equals(expectedUPRN))
            .collect(Collectors.toList());
    if (addressList.isEmpty()) {
      fail(
          "i_have_a_valid_UPRN_from_my_found_address - filtered address list must not be empty: expected UPRN "
              + expectedUPRN);
    } else {
      this.uprn = addressList.get(0).getUprn();
      assertEquals("Should have returned the correct UPRN", expectedUPRN, this.uprn);
    }
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
      fail(httpClientErrorException.getMessage());
    }
  }

  @Then("the correct cases for my UPRN are returned {string}")
  public void the_correct_cases_for_my_UPRN_are_returned(String caseIds) {
    if (caseIds.isEmpty()) {
      assertNull(caseDTOList);
      caseDTOList = new ArrayList<>();
    } else {
      List caseIdList =
          Arrays.stream(caseIds.split(","))
              .filter(item -> !item.isEmpty())
              .collect(Collectors.toList());
      try {
        caseDTOList.forEach(
            caseDetails -> {
              assertEquals(
                  "Cases must have the correct UPRN",
                  uprn,
                  Long.toString(caseDetails.getUprn().getValue()));
              assertTrue(
                  "Cases must have the correct ID" + caseIds,
                  caseIdList.contains(caseDetails.getId().toString()));
            });
      } catch (NullPointerException npe) {
        fail("Null pointer exception on case list for UPRN: " + uprn);
      }
    }
  }

  @Given("I have a valid case from my search UPRN")
  public void i_have_a_valid_case_from_my_search_UPRN() {
    caseDTO = caseDTOList.isEmpty() ? null : caseDTOList.get(0);
    requestChannel = "CC";
  }

  @Then("the correct fulfilments are returned for my case")
  public void the_correct_fulfilments_are_returned_for_my_case() throws CTPException {
    List<Product> expectedProducts =
        getExpectedProducts(caseDTO.getCaseType(), caseDTO.getRegion(), "true");
    List<String> expectedCodes =
        expectedProducts.stream().map(ex -> ex.getFulfilmentCode()).collect(Collectors.toList());

    if (caseDTO != null) {
      assertEquals(
          "Fulfilments list size should be " + expectedProducts.size(),
          Integer.valueOf(expectedProducts.size()),
          Integer.valueOf(fulfilmentDTOList.size()));
      fulfilmentDTOList.forEach(
          fulfilment -> {
            assertTrue(
                "Case: " + caseDTO + " Fulfilment should be of correct code ",
                expectedCodes.contains(fulfilment.getFulfilmentCode()));
          });
    }
  }

  private List<Product> getExpectedProducts(
      final String caseType, final String region, final String individual) throws CTPException {

    return productService
        .getProducts()
        .stream()
        .filter(p1 -> (containsCaseType(p1, caseType)))
        .filter(p2 -> (containsRegion(p2, region)))
        .filter(p3 -> containsChannel(p3))
        .filter(p4 -> p4.getIndividual().equals(Boolean.parseBoolean(individual)))
        .collect(Collectors.toList());
  }

  private boolean containsCaseType(final Product product, final String caseType) {
    boolean containsCaseType = false;
    for (Product.CaseType pCaseType : product.getCaseTypes()) {
      if (pCaseType.name().equalsIgnoreCase(caseType)) {
        containsCaseType = true;
      }
    }
    return containsCaseType;
  }

  private boolean containsRegion(final Product product, final String region) {
    boolean containsRegion = false;
    for (Product.Region pRegion : product.getRegions()) {
      if (pRegion.name().equalsIgnoreCase(region)) {
        containsRegion = true;
      }
    }
    return containsRegion;
  }

  private boolean containsChannel(final Product product) {
    boolean containsChannel = false;
    for (Product.RequestChannel pRequestChannel : product.getRequestChannels()) {
      if (pRequestChannel.name().equalsIgnoreCase(requestChannel)) {
        containsChannel = true;
      }
    }
    return containsChannel;
  }

  @Given("the CC advisor has provided a valid UPRN with caseType HH")
  public void the_CC_advisor_has_provided_a_valid_UPRN_with_caseType_HH() {
    try {
      ResponseEntity<List<CaseDTO>> caseUprnResponse = getCaseForUprn("1347459991");
      listOfCasesWithUprn = caseUprnResponse.getBody();
      HttpStatus contactCentreStatus = caseUprnResponse.getStatusCode();
      log.with(contactCentreStatus).info("GET CASE BY UPRN: The response from " + caseForUprnUrl);
      assertEquals(
          "GET CASE BY UPRN HAS FAILED -  the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("GET CASE BY UPRN HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("GET CASE BY UPRN HAS FAILED: An unexpected error has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Then("the Case endpoint returns a case associated with the UPRN")
  public void the_Case_endpoint_returns_a_case_associated_with_the_UPRN() {
    assertEquals(
        "The case id found is not the expected one",
        "3305e937-6fb1-4ce1-9d4c-077f147789aa",
        listOfCasesWithUprn.get(0).getId().toString());

    UniquePropertyReferenceNumber expectedUprn = new UniquePropertyReferenceNumber("1347459991");
    assertEquals(
        "The uprn found is not the expected one",
        expectedUprn,
        listOfCasesWithUprn.get(0).getUprn());
  }

  @Given(
      "a list of available fulfilment product codes is presented for a HH caseType where individual flag = {string} and region = {string}")
  public void
      a_list_of_available_fulfilment_product_codes_is_presented_for_a_HH_caseType_where_individual_flag_and_region(
          String string, String string2) {
    // Write code here that turns the phrase above into concrete actions
    // throw new cucumber.api.PendingException();
  }

  @When("CC Advisor select the product code for HH UAC via Post")
  public void cc_Advisor_select_the_product_code_for_HH_UAC_via_Post() {
    // Write code here that turns the phrase above into concrete actions
    // throw new cucumber.api.PendingException();
  }

  @Then(
      "an event is emitted to RM with a fulfilment request for a HH UAC where delivery channel = Post")
  public void
      an_event_is_emitted_to_RM_with_a_fulfilment_request_for_a_HH_UAC_where_delivery_channel_Post() {
    // Write code here that turns the phrase above into concrete actions
    // throw new cucumber.api.PendingException();
  }

  private ResponseEntity<List<CaseDTO>> getCaseForUprn(String uprn) {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprn);

    ResponseEntity<List<CaseDTO>> caseResponse = null;

    try {
      caseResponse =
          getRestTemplate()
              .exchange(
                  builder.build().encode().toUri(),
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<CaseDTO>>() {});
      caseDTOList = caseResponse.getBody();
    } catch (HttpClientErrorException httpClientErrorException) {
      log.debug(
          "A HttpClientErrorException has occurred when trying to get list of cases using getCaseByUprn endpoint in contact centre: "
              + httpClientErrorException.getMessage());
    }
    return caseResponse;
  }
}
