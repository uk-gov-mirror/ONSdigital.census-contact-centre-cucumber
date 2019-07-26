package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.authentication.AuthenticationScheme;
import com.jayway.restassured.authentication.PreemptiveBasicAuthScheme;
import com.jayway.restassured.builder.RequestSpecBuilder;
import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import uk.gov.ons.ctp.common.event.model.CollectionCaseResponse;
import uk.gov.ons.ctp.integration.contcencucumber.client.generatorService.GeneratorServiceClientServiceImpl;

public class TestEndpoints {
  
  private static final Logger log = LoggerFactory.getLogger(AccessTheCensus.class);
  private WebDriver driver = null;
  private String versionUrl;
  private String generatorUrl;
  private GeneratorServiceClientServiceImpl generatorServiceClientServiceImpl;
  private CollectionCaseResponse collectionCaseResponse = null;
  private JSONObject requestJsonObject;
  private JSONObject contextsJsonObject1;
  private JSONObject contextsJsonObject2;
  private Response resp;
  
  @Before("@SetUpTestEndpoints")
  public void setup() {
      System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver/geckodriver");
      driver = new FirefoxDriver();
      versionUrl = "https://contactcentre-whitelodge.census-gcp.onsdigital.uk/version";
      generatorUrl = "http://localhost:8171/generate";
      generatorServiceClientServiceImpl = new GeneratorServiceClientServiceImpl();
  }
  
  @After("@TearDownTestEndpoints")
  public void deleteDriver() {
      driver.close();
  }

  @Given("I go to the version endpoint for the Contact Centre")
  public void i_go_to_the_version_endpoint_for_the_Contact_Centre() {
      driver.get(versionUrl);
  }

  @Given("I receive a Rest response")
  public void i_receive_a_Rest_response() {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
  }
  
  @Given("I post a request to the endpoint for the UAC Generator")
  public void i_post_a_request_to_the_endpoint_for_the_UAC_Generator() {
//    collectionCaseResponse = generatorServiceClientServiceImpl.postGenerateCaseCreated(generatorUrl);
    
      //Arrange
      RequestSpecBuilder builder = new RequestSpecBuilder();
//      builder.setBaseUri(generatorUrl);
//      builder.setContentType(ContentType.JSON);
      
      //set body
      requestJsonObject = new JSONObject();
      requestJsonObject.put("eventType", "CASE_CREATED");
      requestJsonObject.put("source", "RESPONDENT_HOME");
      requestJsonObject.put("channel", "RH");
      
      contextsJsonObject1 = new JSONObject();
      contextsJsonObject1.put("caseRef", "hello");
      contextsJsonObject1.put("id", "#uuid");
      
      contextsJsonObject2 = new JSONObject();
      contextsJsonObject2.put("caseRef", "bar");
      contextsJsonObject2.put("id", "#uuid");
      
      List<JSONObject> listForRequest = new ArrayList<JSONObject>();
      listForRequest.add(contextsJsonObject1);
      listForRequest.add(contextsJsonObject2);
      
      requestJsonObject.put("contexts", listForRequest);
       
      builder.setBody(requestJsonObject.toString());
      builder.setContentType("application/json; charset=UTF-8");
      
      PreemptiveBasicAuthScheme authScheme = new PreemptiveBasicAuthScheme();
      authScheme.setUserName("generator");
      authScheme.setPassword("hitmeup");
      
      builder.setAuth(authScheme);
      
      RequestSpecification requestSpec = builder.build();
      resp = null;
      resp = given().spec(requestSpec).when().post(generatorUrl);
      
//      assertTrue(resp.getBody().jsonPath().get("caseRef").equals("hello"));
      
  }

  @Given("I receive a Rest response that is not null")
  public void i_receive_a_Rest_response_that_is_not_null() {
      assertNotNull(resp);
  }


  @Then("the response should contain caseRefs {string} and {string}")
  public void the_response_should_contain_caseRefs_and(String caseRef0, String caseRef1) {
//      String firstCaseRef = "";
      assertNotNull(resp.getBody());
      assertNotNull(resp.getBody().jsonPath());
      log.info("The json path is: " + resp.getBody().jsonPath().toString());
      log.info("The payload is: " + resp.getBody().jsonPath().get("payloads"));
//      assertNotNull(resp.getBody().jsonPath().get("caseRef"));
  }

  @Then("the response should contain the following api version number {string}")
  public void the_response_should_contain_the_following_api_version_number(String string) {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
  }

  @Then("the response should contain the following data version number {string}")
  public void the_response_should_contain_the_following_data_version_number(String string) {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
  }

  @Then("the client receives status code of {int}")
  public void the_client_receives_status_code_of(Integer int1) {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
  }

  @Then("the client receives server version {double}")
  public void the_client_receives_server_version(Double double1) {
      // Write code here that turns the phrase above into concrete actions
      throw new cucumber.api.PendingException();
  }


}
