package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class TestEndpoints {
  
  private static final Logger log = LoggerFactory.getLogger(AccessTheCensus.class);
  private WebDriver driver = null;
  private String versionUrl;
  
  @Before("@SetUpTestEndpoints")
  public void setup() {
      System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver/geckodriver");
      driver = new FirefoxDriver();
      versionUrl = "https://contactcentre-whitelodge.census-gcp.onsdigital.uk/version";
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
