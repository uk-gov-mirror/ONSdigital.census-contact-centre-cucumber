package uk.gov.ons.ctp.integration.contcencucumber.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class CucTestContext {
  @Value("${contact-centre.host}")
  protected String ccBaseUrl;

  @Value("${contact-centre.port}")
  protected String ccBasePort;

  @Value("${contact-centre.username}")
  private String ccUsername;

  @Value("${contact-centre.password}")
  private String ccPassword;

  @Value("${mock-case-service.host}")
  protected String mcsBaseUrl;

  @Value("${mock-case-service.port}")
  protected String mcsBasePort;

  // @Before(order = 1)
  public void init() {}

  public RestTemplate getRestTemplate() {
    return new RestTemplateBuilder().basicAuthentication(ccUsername, ccPassword).build();
  }
}
