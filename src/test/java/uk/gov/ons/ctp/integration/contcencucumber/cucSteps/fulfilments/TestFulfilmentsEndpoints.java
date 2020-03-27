package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.fulfilments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.event.EventPublisher.EventType;
import uk.gov.ons.ctp.common.event.model.FulfilmentPayload;
import uk.gov.ons.ctp.common.event.model.FulfilmentRequestedEvent;
import uk.gov.ons.ctp.common.event.model.Header;
import uk.gov.ons.ctp.common.model.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.rabbit.RabbitHelper;
import uk.gov.ons.ctp.common.util.TimeoutParser;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.AddressQueryResponseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseType;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.PostalFulfilmentRequestDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.Region;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.ResponseDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.ResetMockCaseApiAndPostCasesBase;
import uk.gov.ons.ctp.integration.contcencucumber.main.service.ProductService;

// import uk.gov.ons.ctp.integration.rhcucumber.utils.TimeoutParser;

public class TestFulfilmentsEndpoints extends ResetMockCaseApiAndPostCasesBase {

  private List<FulfilmentDTO> fulfilmentDTOList;
  private AddressQueryResponseDTO addressQueryResponseDTO;
  private String addressSearchString;
  private String uprn;
  private List<CaseDTO> caseDTOList;
  private CaseDTO caseDTO;
  private String requestChannel = "";
  private URI caseForUprnUrl;
  private URI productsUrl;
  private List<CaseDTO> listOfCasesWithUprn;
  private List<Product> listOfProducts;
  private RabbitHelper rabbit;
  private String queueName;
  private FulfilmentRequestedEvent fulfilmentRequestedEvent;
  private Header fulfilmentRequestedHeader;
  private FulfilmentPayload fulfilmentPayload;

  @Autowired private ProductService productService;
  private URI fulfilmentByPostUrl;

  private static final String RABBIT_EXCHANGE = "events";

  @Before("@SetUp")
  public void setup() throws CTPException {
    rabbit = RabbitHelper.instance(RABBIT_EXCHANGE);
    fulfilmentRequestedEvent = null;
  }

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
      log.with(contactCentreStatus)
          .info("GET CASE BY UPRN: The response from " + caseForUprnUrl.toString());
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

  @Given("the CC advisor has provided a valid UPRN {string}")
  public void the_CC_advisor_has_provided_a_valid_UPRN(String strUprn) {
    try {
      ResponseEntity<List<CaseDTO>> caseUprnResponse = getCaseForUprn(strUprn);
      listOfCasesWithUprn = caseUprnResponse.getBody();
      HttpStatus contactCentreStatus = caseUprnResponse.getStatusCode();
      log.with(contactCentreStatus)
          .info("GET CASE BY UPRN: The response from " + caseForUprnUrl.toString());
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

  @When(
      "the Case endpoint returns a case, associated with UPRN {string}, which has caseType {string}")
  public void the_Case_endpoint_returns_a_case_associated_with_UPRN_which_has_caseType(
      String strUprn, String strCaseType) {
    log.with(listOfCasesWithUprn.get(0).getId().toString())
        .debug("The case id returned by getCasesWithUprn endpoint");

    UniquePropertyReferenceNumber expectedUprn = new UniquePropertyReferenceNumber(strUprn);
    assertEquals(
        "The uprn found is not the expected one",
        expectedUprn,
        listOfCasesWithUprn.get(0).getUprn());
    assertEquals(
        "The caseType found is not the expected one",
        strCaseType,
        listOfCasesWithUprn.get(0).getCaseType());
  }

  @Then(
      "the Case endpoint returns a case, associated with UPRN {string}, which has caseType {string} and addressLevel {string} and handDelivery {string}")
  public void
      the_Case_endpoint_returns_a_case_associated_with_UPRN_which_has_caseType_and_addressLevel_and_handDelivery(
          String strUprn, String strCaseType, String strAddressLevel, String strHandDelivery) {
    log.with(listOfCasesWithUprn.get(0).getId().toString())
        .debug("The case id returned by getCasesWithUprn endpoint");

    UniquePropertyReferenceNumber expectedUprn = new UniquePropertyReferenceNumber(strUprn);
    assertEquals(
        "The uprn found is not the expected one",
        expectedUprn,
        listOfCasesWithUprn.get(0).getUprn());
    assertEquals(
        "The caseType found is not the expected one",
        strCaseType,
        listOfCasesWithUprn.get(0).getCaseType());
    log.with(strAddressLevel)
        .info(
            "We cannot assert that the case has this addressLevel - because the addressLevel field is not shown in the CaseDTO representation to Serco.");
    log.with(strHandDelivery)
        .info(
            "We cannot assert the the case has this value of handDelivery - because the isHandDelivery result is deliberately hidden in the CaseDTO representation to Serco.");
  }

  @Given(
      "a list of available fulfilment product codes is presented for a HH caseType where individual flag = {string} and region = {string}")
  public void
      a_list_of_available_fulfilment_product_codes_is_presented_for_a_HH_caseType_where_individual_flag_and_region(
          String individual, String region) throws CTPException {
    try {
      ResponseEntity<List<Product>> productsResponse = getProducts("HH", region, individual);
      listOfProducts = productsResponse.getBody();
      HttpStatus contactCentreStatus = productsResponse.getStatusCode();
      log.with(contactCentreStatus)
          .info("GET PRODUCTS: The response from " + productsUrl.toString());
      assertEquals(
          "GET PRODUCTS HAS FAILED -  the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("GET PRODUCTS HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("GET PRODUCTS HAS FAILED: An unexpected error has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Given(
      "a list of available fulfilment product codes is presented for a caseType = {string} where individual flag = {string} and region = {string}")
  public void
      a_list_of_available_fulfilment_product_codes_is_presented_for_a_caseType_where_individual_flag_and_region(
          String caseType, String individual, String region) {
    try {
      ResponseEntity<List<Product>> productsResponse = getProducts(caseType, region, individual);
      listOfProducts = productsResponse.getBody();
      HttpStatus contactCentreStatus = productsResponse.getStatusCode();
      log.with(contactCentreStatus)
          .info("GET PRODUCTS: The response from " + productsUrl.toString());
      assertEquals(
          "GET PRODUCTS HAS FAILED -  the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("GET PRODUCTS HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("GET PRODUCTS HAS FAILED: An unexpected error has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Given("an empty queue exists for sending Fulfilment Requested events")
  public void an_empty_queue_exists_for_sending_Fulfilment_Requested_events() throws CTPException {
    String eventTypeAsString = "FULFILMENT_REQUESTED";
    log.info("Creating queue for events of type: '" + eventTypeAsString + "'");
    EventType eventType = EventType.valueOf(eventTypeAsString);
    queueName = rabbit.createQueue(eventType);
    log.info("Flushing queue: '" + queueName + "'");

    rabbit.flushQueue(queueName);
  }

//  @When("CC Advisor select the product code for HH UAC via Post")
//  public void cc_Advisor_select_the_product_code_for_HH_UAC_via_Post() {
//    String productCodeSelected = null;
//    for (Product p : listOfProducts) {
//      Product.ProductGroup productGroup = p.getProductGroup();
//      if (productGroup == Product.ProductGroup.UAC) {
//        productCodeSelected = p.getFulfilmentCode();
//      }
//    }
//    log.info("The product code selected is: " + productCodeSelected);
//
//    try {
//      ResponseEntity<ResponseDTO> fulfilmentRequestResponse =
//          requestFulfilmentByPost("3305e937-6fb1-4ce1-9d4c-077f147789aa", productCodeSelected);
//      HttpStatus contactCentreStatus = fulfilmentRequestResponse.getStatusCode();
//      log.with(contactCentreStatus)
//          .info("REQUEST FULFILMENT: The response from " + productsUrl.toString());
//      assertEquals(
//          "REQUEST FULFILMENT HAS FAILED - the contact centre does not give a response code of 200",
//          HttpStatus.OK,
//          contactCentreStatus);
//    } catch (ResourceAccessException e) {
//      log.error("REQUEST FULFILMENT HAS FAILED: A ResourceAccessException has occurred.");
//      log.error(e.getMessage());
//      fail();
//      System.exit(0);
//    } catch (Exception e) {
//      log.error("REQUEST FULFILMENT HAS FAILED: An unexpected error has occurred.");
//      log.error(e.getMessage());
//      fail();
//      System.exit(0);
//    }
//  }

  @When("CC Advisor selects the product code for productGroup {string},  language {string}, deliveryChannel {string}")
  public void cc_Advisor_selects_the_product_code_for_productGroup_language_deliveryChannel(String strProductGroup, String strLanguage, String strDeliveryChannel) {
    String productCodeSelected = null;
    for (Product p : listOfProducts) {
      String productGroup = p.getProductGroup().toString().toUpperCase();
      String language = p.getLanguage();
      String deliveryChannel = p.getDeliveryChannel().toString().toUpperCase();
      if (productGroup.equals(strProductGroup) && language.equals(strLanguage) && deliveryChannel.equals(strDeliveryChannel)){
        productCodeSelected = p.getFulfilmentCode();
      }
    }
    log.info("The product code selected is: " + productCodeSelected);
    if (productCodeSelected == null) {
      throw new cucumber.api.PendingException("Not able to test until Product Reference Service is updated");
    }

    try {
      ResponseEntity<ResponseDTO> fulfilmentRequestResponse =
          requestFulfilmentByPost("3305e937-6fb1-4ce1-9d4c-077f147789aa", productCodeSelected);
      HttpStatus contactCentreStatus = fulfilmentRequestResponse.getStatusCode();
      log.with(contactCentreStatus)
          .info("REQUEST FULFILMENT: The response from " + productsUrl.toString());
      assertEquals(
          "REQUEST FULFILMENT HAS FAILED - the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("REQUEST FULFILMENT HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("REQUEST FULFILMENT HAS FAILED: An unexpected error has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }
  
//  @When("CC Advisor select the product code for UAC in welsh language via Post")
//  public void cc_Advisor_select_the_product_code_for_UAC_in_welsh_language_via_Post() {
//    String descriptionToFind =
//        ""; // TODO update the descriptionToFind, with the product description, when the product
//    // reference library contains the required product.
//    String productCodeSelected = null;
//    for (Product p : listOfProducts) {
//      String productDescription = p.getDescription();
//      if (descriptionToFind.equals("")) {
//        throw new cucumber.api.PendingException(
//            "Not able to test until Product Reference Service is updated");
//      } else {
//        if (productDescription.equals(descriptionToFind)) {
//          productCodeSelected = p.getFulfilmentCode();
//        }
//        try {
//          ResponseEntity<ResponseDTO> fulfilmentRequestResponse =
//              requestFulfilmentByPost("3305e937-6fb1-4ce1-9d4c-077f147789aa", productCodeSelected);
//          HttpStatus contactCentreStatus = fulfilmentRequestResponse.getStatusCode();
//          log.with(contactCentreStatus)
//              .info("REQUEST FULFILMENT: The response from " + productsUrl.toString());
//          assertEquals(
//              "REQUEST FULFILMENT HAS FAILED - the contact centre does not give a response code of 200",
//              HttpStatus.OK,
//              contactCentreStatus);
//        } catch (ResourceAccessException e) {
//          log.error("REQUEST FULFILMENT HAS FAILED: A ResourceAccessException has occurred.");
//          log.error(e.getMessage());
//          fail();
//          System.exit(0);
//        } catch (Exception e) {
//          log.error("REQUEST FULFILMENT HAS FAILED: An unexpected error has occurred.");
//          log.error(e.getMessage());
//          fail();
//          System.exit(0);
//        }
//      }
//    }
//  }

  @When("CC Advisor select the product code for Individual Paper Questionnaire in welsh language")
  public void
      cc_Advisor_select_the_product_code_for_Individual_Paper_Questionnaire_in_welsh_language() {
    String productCodeSelected = null;
    for (Product p : listOfProducts) {
      String productDescription = p.getDescription();
      if (productDescription.equals("Individual Questionnaire for Wales (in Welsh)")) {
        productCodeSelected = p.getFulfilmentCode();
      }
    }
    assertEquals("An incorrect fulfilment code was selected", "P_OR_I2W", productCodeSelected);

    try {
      ResponseEntity<ResponseDTO> fulfilmentRequestResponse =
          requestFulfilmentByPost("3305e937-6fb1-4ce1-9d4c-077f147722aa", productCodeSelected);
      HttpStatus contactCentreStatus = fulfilmentRequestResponse.getStatusCode();
      log.with(contactCentreStatus)
          .info("REQUEST FULFILMENT: The response from " + productsUrl.toString());
      assertEquals(
          "REQUEST FULFILMENT HAS FAILED - the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("REQUEST FULFILMENT HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("REQUEST FULFILMENT HAS FAILED: An unexpected error has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @When("CC Advisor select the product code for SPG Paper Questionnaire \\(english)")
  public void cc_Advisor_select_the_product_code_for_SPG_Paper_Questionnaire_english() {
    String productCodeSelected = null;
    for (Product p : listOfProducts) {
      String productDescription = p.getDescription();
      if (productDescription.equals("Individual Questionnaire for Northern Ireland (in English)")) {
        productCodeSelected = p.getFulfilmentCode();
      }
    }
    assertEquals("An incorrect fulfilment code was selected", "P_OR_I4", productCodeSelected);

    try {
      ResponseEntity<ResponseDTO> fulfilmentRequestResponse =
          requestFulfilmentByPost("3305e937-6fb1-4ce1-9d4c-077f147733aa", productCodeSelected);
      HttpStatus contactCentreStatus = fulfilmentRequestResponse.getStatusCode();
      log.with(contactCentreStatus)
          .info("REQUEST FULFILMENT: The response from " + productsUrl.toString());
      assertEquals(
          "REQUEST FULFILMENT HAS FAILED - the contact centre does not give a response code of 200",
          HttpStatus.OK,
          contactCentreStatus);
    } catch (ResourceAccessException e) {
      log.error("REQUEST FULFILMENT HAS FAILED: A ResourceAccessException has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    } catch (Exception e) {
      log.error("REQUEST FULFILMENT HAS FAILED: An unexpected error has occurred.");
      log.error(e.getMessage());
      fail();
      System.exit(0);
    }
  }

  @Then(
      "an event is emitted to RM with a fulfilment request for a HH UAC where delivery channel = Post")
  public void
      an_event_is_emitted_to_RM_with_a_fulfilment_request_for_a_HH_UAC_where_delivery_channel_Post()
          throws CTPException {
    log.info(
        "Check that a FULFILMENT_REQUESTED event has now been put on the empty queue, named "
            + queueName
            + ", ready to be picked up by RM");

    String clazzName = "FulfilmentRequestedEvent.class";
    String timeout = "2000ms";

    log.info(
        "Getting from queue: '"
            + queueName
            + "' and converting to an object of type '"
            + clazzName
            + "', with timeout of '"
            + timeout
            + "'");

    fulfilmentRequestedEvent =
        (FulfilmentRequestedEvent)
            rabbit.getMessage(
                queueName,
                FulfilmentRequestedEvent.class,
                TimeoutParser.parseTimeoutString(timeout));

    assertNotNull(fulfilmentRequestedEvent);
    fulfilmentRequestedHeader = fulfilmentRequestedEvent.getEvent();
    assertNotNull(fulfilmentRequestedHeader);
    fulfilmentPayload = fulfilmentRequestedEvent.getPayload();
    assertNotNull(fulfilmentPayload);
  }

  @Then(
      "an event with a {string} is emitted to RM with a fulfilment request for an individual UAC in welsh where delivery channel = Post")
  public void
      an_event_with_a_is_emitted_to_RM_with_a_fulfilment_request_for_an_individual_UAC_in_welsh_where_delivery_channel_Post(
          String string) throws CTPException {
    log.info(
        "Check that a FULFILMENT_REQUESTED event has now been put on the empty queue, named "
            + queueName
            + ", ready to be picked up by RM");

    String clazzName = "FulfilmentRequestedEvent.class";
    String timeout = "2000ms";

    log.info(
        "Getting from queue: '"
            + queueName
            + "' and converting to an object of type '"
            + clazzName
            + "', with timeout of '"
            + timeout
            + "'");

    fulfilmentRequestedEvent =
        (FulfilmentRequestedEvent)
            rabbit.getMessage(
                queueName,
                FulfilmentRequestedEvent.class,
                TimeoutParser.parseTimeoutString(timeout));

    assertNotNull(fulfilmentRequestedEvent);
    fulfilmentRequestedHeader = fulfilmentRequestedEvent.getEvent();
    assertNotNull(fulfilmentRequestedHeader);
    fulfilmentPayload = fulfilmentRequestedEvent.getPayload();
    assertNotNull(fulfilmentPayload);
  }

  @Then(
      "an event with the {string} is emitted to RM with a fulfilment request for an individual Paper Questionnaire in welsh")
  public void
      an_event_with_the_is_emitted_to_RM_with_a_fulfilment_request_for_an_individual_Paper_Questionnaire_in_welsh(
          String string) throws CTPException {
    log.info(
        "Check that a FULFILMENT_REQUESTED event has now been put on the empty queue, named "
            + queueName
            + ", ready to be picked up by RM");

    String clazzName = "FulfilmentRequestedEvent.class";
    String timeout = "2000ms";

    log.info(
        "Getting from queue: '"
            + queueName
            + "' and converting to an object of type '"
            + clazzName
            + "', with timeout of '"
            + timeout
            + "'");

    fulfilmentRequestedEvent =
        (FulfilmentRequestedEvent)
            rabbit.getMessage(
                queueName,
                FulfilmentRequestedEvent.class,
                TimeoutParser.parseTimeoutString(timeout));

    assertNotNull(fulfilmentRequestedEvent);
    fulfilmentRequestedHeader = fulfilmentRequestedEvent.getEvent();
    assertNotNull(fulfilmentRequestedHeader);
    fulfilmentPayload = fulfilmentRequestedEvent.getPayload();
    assertNotNull(fulfilmentPayload);
  }

  @Then(
      "an event is emitted with the {string} to RM with a fulfilment request for an Individual Paper Questionnaire \\(english)")
  public void
      an_event_is_emitted_with_the_to_RM_with_a_fulfilment_request_for_an_Individual_Paper_Questionnaire_english(
          String string) throws CTPException {
    log.info(
        "Check that a FULFILMENT_REQUESTED event has now been put on the empty queue, named "
            + queueName
            + ", ready to be picked up by RM");

    String clazzName = "FulfilmentRequestedEvent.class";
    String timeout = "2000ms";

    log.info(
        "Getting from queue: '"
            + queueName
            + "' and converting to an object of type '"
            + clazzName
            + "', with timeout of '"
            + timeout
            + "'");

    fulfilmentRequestedEvent =
        (FulfilmentRequestedEvent)
            rabbit.getMessage(
                queueName,
                FulfilmentRequestedEvent.class,
                TimeoutParser.parseTimeoutString(timeout));

    assertNotNull(fulfilmentRequestedEvent);
    fulfilmentRequestedHeader = fulfilmentRequestedEvent.getEvent();
    assertNotNull(fulfilmentRequestedHeader);
    fulfilmentPayload = fulfilmentRequestedEvent.getPayload();
    assertNotNull(fulfilmentPayload);
  }

  private ResponseEntity<List<CaseDTO>> getCaseForUprn(String uprn) {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment("uprn")
            .pathSegment(uprn);

    ResponseEntity<List<CaseDTO>> caseResponse = null;
    caseForUprnUrl = builder.build().encode().toUri();

    try {
      caseResponse =
          getRestTemplate()
              .exchange(
                  caseForUprnUrl,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<CaseDTO>>() {});
    } catch (HttpClientErrorException httpClientErrorException) {
      log.debug(
          "A HttpClientErrorException has occurred when trying to get list of cases using getCaseByUprn endpoint in contact centre: "
              + httpClientErrorException.getMessage());
    }
    return caseResponse;
  }

  private ResponseEntity<List<Product>> getProducts(
      String caseType, String region, String individual) {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("fulfilments")
            .queryParam("caseType", "HH")
            .queryParam("region", region)
            .queryParam("individual", individual);

    ResponseEntity<List<Product>> productsResponse = null;
    productsUrl = builder.build().encode().toUri();

    try {
      productsResponse =
          getRestTemplate()
              .exchange(
                  productsUrl,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<List<Product>>() {});
    } catch (HttpClientErrorException httpClientErrorException) {
      log.debug(
          "A HttpClientErrorException has occurred when trying to get list of cases using getCaseByUprn endpoint in contact centre: "
              + httpClientErrorException.getMessage());
    }
    return productsResponse;
  }

  private ResponseEntity<ResponseDTO> requestFulfilmentByPost(String caseId, String productCode) {
    final UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(ccBaseUrl)
            .port(ccBasePort)
            .pathSegment("cases")
            .pathSegment(caseId)
            .pathSegment("fulfilment")
            .pathSegment("post");

    ResponseEntity<ResponseDTO> requestFulfilmentByPostResponse = null;
    fulfilmentByPostUrl = builder.build().encode().toUri();

    PostalFulfilmentRequestDTO postalFulfilmentRequest = new PostalFulfilmentRequestDTO();
    postalFulfilmentRequest.setCaseId(UUID.fromString(caseId));
    postalFulfilmentRequest.setTitle("Mrs");
    postalFulfilmentRequest.setForename("Joanna");
    postalFulfilmentRequest.setSurname("Bloggs");
    postalFulfilmentRequest.setFulfilmentCode(productCode);
    postalFulfilmentRequest.setDateTime(new Date());

    HttpEntity<PostalFulfilmentRequestDTO> requestEntity =
        new HttpEntity<>(postalFulfilmentRequest);

    try {
      requestFulfilmentByPostResponse =
          getRestTemplate()
              .exchange(fulfilmentByPostUrl, HttpMethod.POST, requestEntity, ResponseDTO.class);
    } catch (HttpClientErrorException httpClientErrorException) {
      log.debug(
          "A HttpClientErrorException has occurred when trying to post to fulfilmentRequestByPost endpoint in contact centre: "
              + httpClientErrorException.getMessage());
    }
    return requestFulfilmentByPostResponse;
  }
}
