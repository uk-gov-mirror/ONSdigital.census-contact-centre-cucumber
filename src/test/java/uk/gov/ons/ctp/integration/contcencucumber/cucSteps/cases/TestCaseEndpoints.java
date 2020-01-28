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
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.CaseDTO;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpoints;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.Codec;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.EQJOSEProvider;
import uk.gov.ons.ctp.integration.eqlaunch.crypto.KeyStore;

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
  private String telephoneEndpointBody;

  private static final String SIGNING_PUBLIC_SHA1 = "57db285d00430f8c9dbaa3e1fb281f7053acd977";

  private static final String ENCRYPTION_PRIVATE_SHA1 = "1fd9125153420767a7259ee3dada222e74812f82";

  // @Value("${keystore.keys}")
  // private String cryptoKeys;

  private static final String SIGNING_PUBLIC_VALUE =
      "-----BEGIN PUBLIC KEY-----\nMIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAuTeQiTUPhOTEh/tIYx1R\nHjw0gLrfL2yFPh2bmWPirhLFiEZCIl8Nci7as8ta6HykUajUgvl0"
          + "rt5O3t/RaPdm\nUB4uLDZXF1CWHoP1L6oeplwG3mnmp15cBi0in/iMNcbeTrVPm6m5liIZ91p80Z41\n/zwIO8EyvMRpMQO6LuLsSMLmIbXS8gleZOeV1tIegp2Q8WGmx5+U/PMoPmN61cjH"
          + "\nMgCyFJY6/U3GAVBj2ZIQsKqcJ1yJHRx+mvA4yQ+ElpPh4BN+xvNUfuQ+NHfvI3le\nl/Wma5TxMlimS+2ONj9bSe0xSh9qAjFWx6L6A4IZqLnX70eW9fnY8hO7IphNFsMC\nZH2zp1KJls"
          + "8Z0nCRGdCumAExHJ7ddOG5TJ5BfiMboOh7dlFSOaBEWWSa+S96oDXc\nf8zzhaBiAp1sBpU+WTyKyg4Pixjwy5ygbUjkPQWN4jSas5k/fyZIsYKyoBs7vT52\n+gxBZdXVVsa0jHOrZbvNSQ"
          + "v6cK2b+BLwPqTTGsAlKTT68c7OvGYxGU/bpoKDXGtG\nuX/05K0L/PHPuXq8TxgPbfAlvxReQLIvBdlxQTr9uRGagN8She1U2wi+7UD31nDh\nGMERIX+gKKME0C3FFYyyDtCRBd93KKbA/g"
          + "F6avcbUV7z/L70WAX4r7KxjLWUYdBB\nxWHJrYaak598jLk/YZ1E5EsCAwEAAQ==\n-----END PUBLIC KEY-----";

  private static final String ENCRYPTION_PRIVATE_VALUE =
      "-----BEGIN RSA PRIVATE KEY-----\nMIIJKgIBAAKCAgEA2uj63sibvXlXz9O3AU0PCjBf2O4S5OJ9dEGQbufc+kjQb1mN\nTkv2k6Hweol4JjudUhTBtSY6IQJj1+ZnN03zdY9BvvCB8R4A"
          + "AcPaS9WeP8MxY5FX\n9aKIiEaFoVHdLJm2E0s/uBqY2OfEVL3qsfphaogX7c+UpisInDVDRExcmJU1pjqN\nWq7KE9tbt1OMy2lOfIkKGzntbSEeX6bV98frGvZvFFInomTrE4IUpxdlQSlTN"
          + "+PD\nZNhr3FXnLLhF6YalxGWykF6+ceslfP7/lq7R5d4S/hqPeJbkFofM9IzI/E14I+ci\nf7rDoJN4dkRcTyE8yvefLHLp7GM3kv38Fuu8TSCojS/mDOIr/glpSvOgbCEteISG\njLm0MWdM"
          + "4m2P2NEYcn5OAwqo5kbtSKUSMkdQtRrqTMZtisvXkdlEO3OVbqARTEvi\nKw0pGJK6rDyZv4lUnib5r+mMqzTN6hYUgTYwrvf+v3ZennCQF6THfHelUdqSanXG\nokQs7TdE7SpP0rBAb3sAU"
          + "GZxNSXnxKu3tQFfYaUVUbktYEytEwNtr1RgVb1jV/xJ\nhqE1AUKtPfeImZ1cQM78BMMbcm8rF7p0qdBNsu/4gLKHy6cXjnx7uv+N2MJxVqDd\ndXoZsIcPj161/cDXYLQ4VsjsHgGwJk+s4I"
          + "VSIzDZVQ9mhgHvu6Ob0fQu3ksCAwEA\nAQKCAgEAitBuNNOVHqNRsbIZPDP3M4jVXqQpSwbscsoYoptZH0UJF3L8DghRhfCM\no8eEbwoiiqgq5YomFkSDt6adEQ7XMVb0Y/XzEEjrzDK7vJb"
          + "rUJegO/kwg8zbMz81\nAcT9sYiPUtDGySHkhrcBYpNDvjR9jaZbNiwJoQ1s2lFUJH0fwonriUtb04M/FTRm\nhmvuvhY69PGZfQZQroDv18vdptAZH/HhIkuuYmYIQ0ibfBFz3cwsvyYpA0vR"
          + "qdi3\nTT5U5u7BMzch8jaCIY7xKMAypwSYiWE3Pfbtv5Q/cqIOr4yO7p7ZbjrlP4XZA3xA\nSQ60UmFLQZGgV/lOqN2JoiZ4srgtoLai4hg11LoO6v4SKxMhAcpgOVQ9i3C9S+t4\nWc1zr/s"
          + "8g2Hllh2+jZRSKjurtQ8gU3p+BJ2I5wiK4GENwJLLTKA7yu9lW5/VOayB\n8kl3t1ktn3Bbg2mH914AFw3v0HenqgEYoBiqyUm8chNi/xhPgJUy4TSVxMlHajD4\nmTKNbLlS/B6tGfQ763+N"
          + "urvN+tDxY8Obmgzvxq5yb4GfLVdBdhA59v464rlbIeOq\nly/WhTWuiIsmNKq0TZqgzXNs0lkU/+FarGO7yyXgopaJ15St7d5vwcBlF1h+Jqlu\nnna2gN1ziCEGBFQWga8D6cgCJvesWKCg+"
          + "4jSn6Y3bD0GM5VFnTECggEBAPJEMiFZ\nkqv8N+iuxaD3/b3TjKd3Ya7Tu2AfW5EabSkhw2Fsw2zNQMPZovVH0Lbg18aFSdD/\nR7zIVouvG7+xabqCFkdpa3lzIPrllRDUV18SLa5eyAFaTF"
          + "60yVJs5vbZcBLtPbi/\nRChlbBjDYu/Q5Fj/gp/y5IsIC7VX+NfN24oKAQLzhaE9Tm5vMsjlkFG2azcbSJcJ\nTATcCwtt3m5l066HlQf+GVrnvSLWOu2iDq5i5tJ1W3DWC56IokhCAwRztdi"
          + "mjKcA\nSYj1I01mZEUPDoKIu/DcsB+HE+E6G3VIsDDydB0xVr+754DBT1RwH+5Romyq/9GO\nOKPMCL/6Ag4pw9cCggEBAOdR1YMfTY1Hoa3GTdmDejtGFnayvpYtDR4N/eMdXBcQ\nQb6Wg7"
          + "bQgEAknvX9s8ibtUvou0hvNQRtgw5RXYvgLccdIbgJKJxwx8SbKAdNsxDi\n6uV45DRdF1Sax07BhYc6W2lFlsaJbFC7FnyY0CTSUMVy4hKe9y2H9m/iUjHRi60Y\nX7L+iKb7c7lMkqpBN8C"
          + "GZnmcXinhCRFd8vZrCDza7GUfsxWx/G7cTrb69O3yrxRa\n2gTpUnieIRoY16koAiNqKkqeJH9i/5A1N2IDGrB1Fzk7wGxSxno9EO2BImsT/BCQ\nMFwMy2p80iWU6QS/tZOkjlZM1ixGg+Q2"
          + "e/Nyy3RF6q0CggEBAOCMZQqUQ2Y1eHyq\nwXLZaM6xbgUqmde5WEtXxT9RByzApp9+eAQ4mnbtGiXOUp2u+VB/6lNUyeEor075\nFvcuMZk1SgEWrQ78ruzeLrRSnEj6SGz+XNMkJvUBDDjhx"
          + "5G1KDPiwPqkDUCEARVS\nxDkTnRMAgMpQD5Tt+Cw1ReiAXaQscZOSs6EnebLSTnf2jzWtpuKE31teup8mdPY6\n95fLudBiyreFxWqEsMHBXw0yz/jCor8fF4oWWt6uNfxTcwiFV19fv65qjb"
          + "3ruLTc\n2sNHmWNT6R7u7i/Ku5peiWTD4sr7brRALu+38nNcuXLCGlaCQ9IFDOfmsojfbJzD\n1XPbMjECggEBAKU7OjVQPdgmC6ISXXd6x+vhE5iz8xlYkt8eBXWeejJrRVr7Lph+\ndK3cu"
          + "sNh9nLcbnMLigOilA9Wnl+y1QxeFLT+5SANQroEgV25qq0U3ky4mitR4Ehb\nGpgveyCRcWz3zWXz9FBax0kfal+T1FII/PMdLck5TbxzOyAOXMIDOS9qkqlL0Zbd\nVHtO7BC7RzS5jCdf/4"
          + "8QlKaYIj3MlHTLuEI/k29y1KrnqT5hImV5jYAPqV4KwAMy\niNiYWCwXTPe5FP552f44W+7JZIcZSo4zBWS3fqqhse6NAjN0o34zJ0E5jaGi7gZh\npMZ9iLkAq5YL3DRe7DlyITH5jS6HmDa"
          + "03n0CggEAIJ3FyRVmGckDSPGAQ7rJ9ltC\n+Y28ETsYN/Ve+JTxzjO1m8YI0gW5lgVnf83kIXHMt8mbq+gMpSdULaRBnjdY8P2z\nvJUTeQlD3qaUML7HqXSoTyGRlrVVuCAomlsykEWd2E4E"
          + "vgk0/eGWiRE3kmyiSCpG\n5lf1zHC4TQKzQibqOCO6PwheAHMu0pv3NaYpK/wmdl+S/wgCTxJ/w9O4v7+JCll7\nJUUNOwN4x+A0bFcgLEBFForwLHtkLlCE6FY9vkbaFhNChnsEopPKd/qeI"
          + "iNwZPs0\n8MheFykrH6jeckzcso9mALrobJlRDUrPmYYNDO6jn6NAacDAoXvp7aPa1RIJ8Q==\n-----END RSA PRIVATE KEY-----";

  private static final String JWTKEYS_DECRYPTION =
      "{\"keys\": {\""
          + SIGNING_PUBLIC_SHA1
          + "\": "
          + "{\"purpose\": \"decryption\", "
          + "\"type\": \"public\""
          + ", \"value\": \""
          + SIGNING_PUBLIC_VALUE
          + "\"}, \""
          + ENCRYPTION_PRIVATE_SHA1
          + "\": "
          + "{\"purpose\": \"decryption\", "
          + "\"type\": \"private\""
          + ", \"value\": \""
          + ENCRYPTION_PRIVATE_VALUE
          + "\"}}}";

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

  @Then("a HH EQ is launched")
  public void a_HH_EQ_is_launched() throws CTPException {
    String hhEqToken;

    log.info(
        "Create a substring that removes the first part of the telephoneEndpointBody to leave just the EQ token value");

    hhEqToken = telephoneEndpointBody.substring(37);

    log.info("The EQ token is: " + hhEqToken);

    EQJOSEProvider coderDecoder = new Codec();

    KeyStore keyStoreDecryption = new KeyStore(JWTKEYS_DECRYPTION);

    // JWEHelper jweHelper = new JWEHelper();

    // String kid = jweHelper.getKid(hhEqToken);
    //
    // log.info("The kid is: " + kid);

    String decryptedEqToken = coderDecoder.decrypt(hhEqToken, keyStoreDecryption);

    log.info("The decrypted String is: " + decryptedEqToken);
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
            .pathSegment("launch")
            .queryParam("agentId", 1)
            .queryParam("individual", false);

    telephoneEndpointUrl = builder.build().encode().toUri().toString();

    log.info("Using the following endpoint to launch EQ for HH: " + telephoneEndpointUrl);

    ResponseEntity<String> ccLaunchEqResponse =
        getRestTemplate().getForEntity(builder.build().encode().toUri(), String.class);

    telephoneEndpointBody = ccLaunchEqResponse.getBody();

    return ccLaunchEqResponse.getStatusCode();
  }
}
