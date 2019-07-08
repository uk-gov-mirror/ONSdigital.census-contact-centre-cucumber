package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RequestAHouseholdUAC {
	
	private static final Logger log = LoggerFactory.getLogger(RequestAHouseholdUAC.class);
	WebDriver driver = null;
	
	@Given("I am on the RH Start Page  - method {int}")
	public void i_am_on_the_RH_Start_Page_method(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I select the “Request UAC” option")
	public void i_select_the_Request_UAC_option() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("I am presented with a form for entering my postcode")
	public void i_am_presented_with_a_form_for_entering_my_postcode() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I enter a valid UK postcode")
	public void i_enter_a_valid_UK_postcode() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I select the “Find Address” option")
	public void i_select_the_Find_Address_option() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("I am presented with the AI list of available addresses for the postcode")
	public void i_am_presented_with_the_AI_list_of_available_addresses_for_the_postcode() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I select my address from the AI list")
	public void i_select_my_address_from_the_AI_list() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I select “Continue”")
	public void i_select_Continue() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("I am presented with a page to confirm my address")
	public void i_am_presented_with_a_page_to_confirm_my_address() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("the address confirmation page contains two options")
	public void the_address_confirmation_page_contains_two_options() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I select the {string} option")
	public void i_select_the_option(String string) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("the address is one of the addresses that’s in the rehearsal")
	public void the_address_is_one_of_the_addresses_that_s_in_the_rehearsal() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("there is exactly {int} HH case that’s associated with the UPRN")
	public void there_is_exactly_HH_case_that_s_associated_with_the_UPRN(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("I am presented with a form for entering a mobile no.")
	public void i_am_presented_with_a_form_for_entering_a_mobile_no() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I enter my UK mobile no.")
	public void i_enter_my_UK_mobile_no() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("I am presented with a page to confirm my mobile no.")
	public void i_am_presented_with_a_page_to_confirm_my_mobile_no() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("the mobile no. confirmation page contains two options")
	public void the_mobile_no_confirmation_page_contains_two_options() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("the region associated with the HH case is NOT Wales")
	public void the_region_associated_with_the_HH_case_is_NOT_Wales() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("my UAC request is submitted")
	public void my_UAC_request_is_submitted() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("I am presented with a page containing a confirmation message")
	public void i_am_presented_with_a_page_containing_a_confirmation_message() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I select “Continue” - method {int}")
	public void i_select_Continue_method(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I select the {string} option - method {int}")
	public void i_select_the_option_method(String string, Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}
}
