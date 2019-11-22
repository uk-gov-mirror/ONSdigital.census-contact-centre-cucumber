package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.ctp.integration.contcencucumber.main.SpringIntegrationTest;

public class TestEndpoints extends SpringIntegrationTest {

  @Value("${contact-centre.host}")
  protected String ccBaseUrl;

  @Value("${contact-centre.port}")
  protected String ccBasePort;

  @Value("${contact-centre.username}")
  private String ccUsername;

  @Value("${contact-centre.password}")
  private String ccPassword;

  protected RestTemplate getRestTemplate() {
    return new RestTemplateBuilder().basicAuthentication(ccUsername, ccPassword).build();
  }
}
