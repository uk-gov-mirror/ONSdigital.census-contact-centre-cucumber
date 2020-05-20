package uk.gov.ons.ctp.integration.contcencucumber.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import uk.gov.ons.ctp.integration.contcencucumber.main.service.ProductService;

@ContextConfiguration(
    classes = {SpringIntegrationTest.class, ProductService.class},
    loader = SpringBootContextLoader.class,
    initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
@SpringBootTest
@Import({
  uk.gov.ons.ctp.integration.contcencucumber.main.service.impl.ProductServiceImpl.class,
  uk.gov.ons.ctp.integration.common.product.ProductReference.class
})
public class SpringIntegrationTest {

  protected RestTemplate getAuthenticationFreeRestTemplate() {
    return new RestTemplateBuilder().build();
  }

  protected RestTemplate getRestTemplate(final String username, final String password) {
    return new RestTemplateBuilder().basicAuthentication(username, password).build();
  }

  public static String getDateAsString() {
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
    Date today = Calendar.getInstance().getTime();
    return sdf.format(today);
  }
}
