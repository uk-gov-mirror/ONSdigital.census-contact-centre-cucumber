package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.smoke;

import static org.junit.Assert.*;

import cucumber.api.java.Before;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;

import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.common.product.model.Product;
import uk.gov.ons.ctp.integration.contactcentresvc.representation.*;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.TestEndpointsFFData;
import uk.gov.ons.ctp.integration.contcencucumber.main.service.ProductService;

public class TestServicesAreRunning extends TestEndpointsFFData {

	private static final Logger log = LoggerFactory.getLogger(TestServicesAreRunning.class);
	private Exception exception;
	
	@Given("I access the Fulfilments endpoint")
	public void i_access_the_Fulfilments_endpoint() {
		log.info("Check that the Contact Centre service is running and has a fulfilments endpoint");
		checkContCenSvc_SmokeTest();
	}

	@Then("I receive a response with a status of {int}")
	public void i_receive_a_response_with_a_status_of(Integer int1) {
		log.warn("hello there 7");
		log.info("hello there 8");
	}

	private void checkContCenSvc_SmokeTest() {
		log.info("Using the following endpoint to check that the contact centre service is running: http://localhost:8171/fulfilments");
		final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(ccBaseUrl).port(ccBasePort)
				.pathSegment("/fulfilments");
		
		String response = "no response";
		
		try {
			ResponseEntity<List<FulfilmentDTO>> fulfilmentResponse = getRestTemplate().exchange(
					builder.build().encode().toUri(), HttpMethod.GET, null,
					new ParameterizedTypeReference<List<FulfilmentDTO>>() {
					});
			response = fulfilmentResponse.getStatusCode().toString();
			log.with(response).info("Smoke Test: The response from http://localhost:8171/fulfilments");
//			fulfilmentDTOList = fulfilmentResponse.getBody();
		} catch (HttpClientErrorException httpClientErrorException) {
			this.exception = httpClientErrorException;
			
		}

	}
}
