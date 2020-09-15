package uk.gov.ons.ctp.integration.contcencucumber.main;

import io.cucumber.java.Before;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import uk.gov.ons.ctp.common.cloud.FirestoreDataStore;
import uk.gov.ons.ctp.common.cloud.TestCloudDataStore;

import uk.gov.ons.ctp.integration.contcencucumber.context.CucTestContext;
import uk.gov.ons.ctp.integration.contcencucumber.context.ResetMockCaseApiContext;
import uk.gov.ons.ctp.integration.contcencucumber.main.service.ProductService;

@ContextConfiguration(
    classes = {
      SpringIntegrationTest.class,
      ProductService.class,
      TestCloudDataStore.class,
      FirestoreDataStore.class,
      CucTestContext.class,
      ResetMockCaseApiContext.class
    },
    loader = SpringBootContextLoader.class,
    initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
@SpringBootTest
@Import({
  uk.gov.ons.ctp.integration.contcencucumber.main.service.impl.ProductServiceImpl.class,
  uk.gov.ons.ctp.integration.common.product.ProductReference.class,
  uk.gov.ons.ctp.integration.contcencucumber.main.repository.CaseDataRepository.class
})
public class SpringIntegrationTest {

    @Before(order = 0)
    public void init() {}
}
