package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.smoke;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.FulfilmentDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpointsFFData;

public class TestServicesAreRunning extends TestEndpointsFFData {

	private static final Logger log = LoggerFactory.getLogger(TestServicesAreRunning.class);
	private Exception exception;
	private HttpStatus ccResponse = null;
	
	@Given("I access the Fulfilments endpoint")
	public void i_access_the_Fulfilments_endpoint() {
		log.info("Check that the Contact Centre service is running");
		checkContCenSvc_SmokeTest();
	}

	@Then("I receive a response with a status of {int}")
	public void i_receive_a_response_with_a_status_of(Integer int1) {
		assertEquals("The Contact Centre Service may not be running - it does not give a response code of 200", HttpStatus.OK, ccResponse);
	}

	private void checkContCenSvc_SmokeTest() {
		log.info("Using the following endpoint to check that the contact centre service is running: http://localhost:8171/fulfilments");
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ccBaseUrl).port(ccBasePort)
				.pathSegment("/fulfilments");
		
		try {
			ResponseEntity<List<FulfilmentDTO>> fulfilmentResponse = getRestTemplate().exchange(
					builder.build().encode().toUri(), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<FulfilmentDTO>>() {
					});
			ccResponse = fulfilmentResponse.getStatusCode();
			log.with(ccResponse).info("Smoke Test: The response from http://localhost:8171/fulfilments");
		} catch (HttpClientErrorException httpClientErrorException) {
			this.exception = httpClientErrorException;
			
		}

	}
}
