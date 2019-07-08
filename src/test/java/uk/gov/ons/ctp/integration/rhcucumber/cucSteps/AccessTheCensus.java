package uk.gov.ons.ctp.integration.rhcucumber.cucSteps;

import uk.gov.ons.ctp.integration.rhcucumber.selenium.pageobject.Start;
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

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;

public class AccessTheCensus {
	
	private static final Logger log = LoggerFactory.getLogger(AccessTheCensus.class);
	private WebDriver driver = null;
	private String baseUrl;
	Start startPage = null;
	private WebElement option1 = null;
	
	@Before("@SetUpAccessTheCensus")
	public void setup() {
		System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver/geckodriver");
		driver = new FirefoxDriver();
		baseUrl = "https://whitelodge.census-gcp.onsdigital.uk/start/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		startPage = new Start(driver);
	}
	
	@After("@TearDownAccessTheCensus")
	public void deleteDriver() {
		driver.close();
	}
	
	@Given("I am on the RH Start Page")
	public void i_am_on_the_RH_Start_Page() {
		driver.get(baseUrl);
	}
	
	@Given("I enter UAC {string} {string} {string} {string} into the text box")
	public void i_enter_UAC_into_the_text_box(String uac1, String uac2, String uac3, String uac4) {

		startPage.enterUac(uac1, uac2, uac3, uac4);		
	}

	@Given("I click the “Start now” button")
	public void i_click_the_Start_now_button() {
		
//		try {
//			Thread.sleep(5000); //give the button time to appear
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/div/div/div/form/button")).click();
	}

	@Then("I am presented with a page to confirm my address - method {int}")
	public void i_am_presented_with_a_page_to_confirm_my_address_method(Integer int1) {
		
		try {
			Thread.sleep(5000); // give the title time to appear
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
		WebElement confirmAddressTitle = driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/div/div/div/h2"));
		
		assertEquals("address confirmation title has incorrect text", "Is your address correct?", confirmAddressTitle.getText());
	}

	@Then("the address confirmation page contains two options - method {int}")
	public void the_address_confirmation_page_contains_two_options_method(Integer int1) {
		
		option1 = driver.findElement(By.id("address-check-answer-0"));
		WebElement option2 = driver.findElement(By.id("address-check-answer-1"));
		
		assertEquals("first address confirmation option has incorrect value", "Yes", option1.getAttribute("value"));
		assertEquals("second address confirmation option has incorrect value", "No", option2.getAttribute("value"));
	}

	@Given("I select the “Yes, this address is correct” option - method {int}")
	public void i_select_the_Yes_this_address_is_correct_option_method(Integer int1) {
		
		option1.click();
	}
	
	@Given("I click the “Save and continue” button")
	public void i_click_the_Save_and_continue_button() {
		
		driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/div/div/div/form/button")).click();
	}

	@Then("I am presented with the first page of the Census Questionnaire")
	public void i_am_presented_with_the_first_page_of_the_Census_Questionnaire() {
		
//		 WebElement firstQuestion = driver.findElement(By.xpath("/html/body/div/div/form/div/div/div/main/div[2]/div/fieldset/legend/h1"));
//		 assertEquals("first question has incorrect text", "What is your name?", firstQuestion.getText());
		 
	}
	
	@Then("I am presented with the Census Questionnaire")
	public void i_am_presented_with_the_Census_Questionnaire() {
	    
		driver.findElement(By.xpath("/html/body/div/div/form/header/div[2]/div/div/div[1]/img"));
	}

	@Then("an error message “Please enter your access code.” appears on the same page")
	public void an_error_message_Please_enter_your_access_code_appears_on_the_same_page() {
	   
		driver.findElement(By.linkText("Please enter your access code."));
	}

	@Then("underneath the message is a list of telephone numbers to call for advise")
	public void underneath_the_message_is_a_list_of_telephone_numbers_to_call_for_advise() {
	   
		WebElement phoneNumber1 = driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/div/div/div[1]/div/div[2]/ul[2]/li[1]"));
		assertEquals("phone number 1 has incorrect text", "0800 141 2021 England", phoneNumber1.getText());
		
		WebElement phoneNumber4 = driver.findElement(By.xpath("/html/body/div/div[1]/div[3]/div/div/div/div[1]/div/div[2]/ul[2]/li[4]"));
		assertEquals("phone number 4 has incorrect text", "0800 587 2021 Translation", phoneNumber4.getText());
		
	}

	@Then("an error message “Please re-enter your access code and try again.” appears on the same page")
	public void an_error_message_Please_re_enter_your_access_code_and_try_again_appears_on_the_same_page() {
	    
		driver.findElement(By.linkText("Please re-enter your access code and try again."));
	}
	
	@Given("I enter invalid UAC {string} {string} {string} {string} into the text box")
	public void i_enter_invalid_UAC_into_the_text_box(String uac1, String uac2, String uac3, String uac4) {
	   
		startPage.enterUac(uac1, uac2, uac3, uac4);		
	}

}
