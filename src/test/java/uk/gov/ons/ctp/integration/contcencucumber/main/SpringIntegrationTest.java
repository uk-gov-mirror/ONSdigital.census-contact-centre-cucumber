package uk.gov.ons.ctp.integration.contcencucumber.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CloudDataStore;
import uk.gov.ons.ctp.integration.contcencucumber.main.repository.CaseDataRepository;
import uk.gov.ons.ctp.integration.contcencucumber.main.service.ProductService;

@ContextConfiguration(
    classes = {
      SpringIntegrationTest.class,
      ProductService.class,
      CaseDataRepository.class,
      CloudDataStore.class,
      AppConfig.class
    },
    loader = SpringBootContextLoader.class,
    initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
@SpringBootTest
@Import({
  uk.gov.ons.ctp.integration.contcencucumber.main.service.impl.ProductServiceImpl.class,
  uk.gov.ons.ctp.integration.common.product.ProductReference.class,
  uk.gov.ons.ctp.integration.contcencucumber.main.repository.impl.CaseDataRepositoryImpl.class,
  uk.gov.ons.ctp.integration.contcencucumber.cloud.FirestoreDataStore.class
})
public class SpringIntegrationTest {

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

  protected RestTemplate getRestTemplate() {
    return new RestTemplateBuilder().basicAuthentication(ccUsername, ccPassword).build();
  }

  protected RestTemplate getAuthenticationFreeRestTemplate() {
    return new RestTemplateBuilder().build();
  }
}
