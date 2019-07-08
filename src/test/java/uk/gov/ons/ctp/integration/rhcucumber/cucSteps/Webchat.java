package uk.gov.ons.ctp.integration.rhcucumber.cucSteps;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class Webchat {
	
	private static final Logger log = LoggerFactory.getLogger(Webchat.class);
	WebDriver driver = null;
	
	@Given("I am on the RH Start Page - method {int}")
	public void i_am_on_the_RH_Start_Page_method(Integer int1) {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}
	
	@Given("I want to chat with a contact centre agent")
	public void i_want_to_chat_with_a_contact_centre_agent() {
	   //nothing to do here
	}

	@Given("I have selected the Webchat option")
	public void i_have_selected_the_Webchat_option() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("the current time is in opening hours")
	public void the_current_time_is_in_opening_hours() {
	    // The contact centre opening times are given in CR-216 and are as follows:
		// weekdays: 8am - 7pm
		// Saturdays: 8am - 1pm
		// Sundays and bank holidays: closed
		// Census weekend: 8am - 4pm
		
		
	    
	}

	@Then("I am presented with a form to enter required information")
	public void i_am_presented_with_a_form_to_enter_required_information() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Given("I enter all the required information")
	public void i_enter_all_the_required_information() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@When("I select the “Start Chat” button")
	public void i_select_the_Start_Chat_button() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("the information is submitted to Serco")
	public void the_information_is_submitted_to_Serco() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

	@Then("my Webchat session starts")
	public void my_Webchat_session_starts() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new cucumber.api.PendingException();
	}

}
