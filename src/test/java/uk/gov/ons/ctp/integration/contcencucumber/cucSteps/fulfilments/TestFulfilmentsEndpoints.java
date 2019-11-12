package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.fulfilments;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.*;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpoints;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TestFulfilmentsEndpoints extends TestEndpoints {

    private List<FulfilmentDTO> fulfilmentDTOList;
    private Exception exception;
    private String caseType;
    private String region;
    private AddressQueryResponseDTO addressQueryResponseDTO;
    private AddressUpdateRequestDTO addressUpdateRequestDTO;
    private String postcode = "";
    private String addressSearchString = "";
    private String uprn;
    private List<CaseDTO> caseDTOList;
    private CaseDTO caseDTO;


    @Given("I have a valid case Type {string} and region {string}")
    public void i_have_a_valid_case_Type_and_region(String caseType, String region) {
        this.caseType = caseType;
        this.region = region;
    }

    @When("I Search fulfilments")
    public void i_Search_fulfilments() {
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(ccBaseUrl)
                .port(ccBasePort)
                .pathSegment("/fulfilments")
                .queryParam("caseType", caseType)
                .queryParam("region", region);
        try {
            ResponseEntity<List<FulfilmentDTO>> fulfilmentResponse =
                    getRestTemplate().exchange(builder.build().encode().toUri(),
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<FulfilmentDTO>>() {
                            });
            fulfilmentDTOList = fulfilmentResponse.getBody();
        }
        catch (HttpClientErrorException httpClientErrorException) {
            this.exception = httpClientErrorException;
        }
    }

    @Then("A list of fulfilments is returned of size {int} {string} {string}")
    public void a_list_of_fulfilments_is_returned_of_size(Integer expectedSize, String caseType,  String region) {
       assertEquals("Fulfilments list size should be " + expectedSize,
               Integer.valueOf(expectedSize), Integer.valueOf(fulfilmentDTOList.size()));
        fulfilmentDTOList.forEach( fulfilment -> {
            assertEquals("Fulfilment should be of correct caseType", caseType, fulfilment.getCaseType().name());
            assertTrue("Fulfilment should be of correct caseType", fulfilment.getRegions().contains(Region.valueOf(region)));
        });
    }

    @Given("I have a valid address search String {string}")
    public void i_have_a_valid_address_search_String(String addressSearchString) {
        this.addressSearchString = addressSearchString;
    }

    @When("I Search Addresses By Address Search String")
    public void i_Search_Addresses_By_Address_Search_String() {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(ccBaseUrl)
                .port(ccBasePort)
                .pathSegment("addresses")
                .queryParam("input", addressSearchString);
        addressQueryResponseDTO = getRestTemplate().getForObject(builder.build().encode().toUri(), AddressQueryResponseDTO.class);
    }

    @Then("A list of addresses for my search is returned containing the address I require")
    public void a_list_of_addresses_for_my_search_is_returned_containing_the_address_I_require() {
        assertNotNull("Address Query Response must not be null", addressQueryResponseDTO);
        assertEquals("Address list size must  be 1 ", 1, addressQueryResponseDTO.getAddresses().size());
    }

    @Given("I have a valid UPRN from my found address {string}")
    public void i_have_a_valid_UPRN_from_my_found_address(String expectedUPRN) {
        this.uprn = addressQueryResponseDTO.getAddresses().get(0).getUprn();
        assertEquals("Should have returned the correct UPRN", expectedUPRN, this.uprn);
    }

    @When("I Search cases By UPRN")
    public void i_Search_cases_By_UPRN() {
        final UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(ccBaseUrl)
                .port(ccBasePort)
                .pathSegment("cases")
                .pathSegment("uprn")
                .pathSegment(uprn);
        try {
            ResponseEntity<List<CaseDTO>> caseResponse =
                    getRestTemplate().exchange(builder.build().encode().toUri(),
                            HttpMethod.GET, null, new ParameterizedTypeReference<List<CaseDTO>>() {
                            });
            caseDTOList = caseResponse.getBody();
        }
        catch (HttpClientErrorException httpClientErrorException) {
            this.exception = httpClientErrorException;
        }
    }

    @Then("the correct cases for my UPRN are returned {string}")
    public void the_correct_cases_for_my_UPRN_are_returned(String caseIds) {
        List caseIdList = Arrays.stream(caseIds.split(",")).collect(Collectors.toList());
        caseDTOList.forEach( caseDetails -> {
            assertEquals("Cases must have the correct UPRN", uprn, Long.toString(caseDetails.getUprn().getValue()));
            assertTrue("Cases must have the correct ID", caseIdList.contains(caseDetails.getId().toString()));
        });
    }

    @Given("I have a valid case from my search UPRN")
    public void i_have_a_valid_case_from_my_search_UPRN() {
       caseDTO =  caseDTOList.get(0);
       caseType = caseDTO.getCaseType();
       region = caseDTO.getRegion();
    }

    @Then("the correct fulfilments are returned for my case {string}")
    public void the_correct_fulfilments_are_returned_for_my_case(String fulfilmentCodes) {
        List fulfilmentCodeList = Arrays.stream(fulfilmentCodes.split(",")).collect(Collectors.toList());
        int expectedSize = fulfilmentCodeList.size();
        assertEquals("Fulfilments list size should be " + expectedSize,
                Integer.valueOf(expectedSize), Integer.valueOf(fulfilmentDTOList.size()));
        fulfilmentDTOList.forEach( fulfilment -> {
            assertEquals("Fulfilment should be of correct caseType", caseType, fulfilment.getCaseType().name());
            assertTrue("Fulfilment should be of correct region", fulfilment.getRegions().contains(Region.valueOf(region)));
            assertTrue("Fulfilment code list must contain the fulfilment code", fulfilmentCodeList.contains(fulfilment.getFulfilmentCode()));
        });
    }
}