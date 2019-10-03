package uk.gov.ons.ctp.integration.contcencucumber.main;

import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@ContextConfiguration(
        classes = SpringIntegrationTest.class,
        loader = SpringBootContextLoader.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@WebAppConfiguration
@SpringBootTest
@ActiveProfiles("test")
public class SpringIntegrationTest {

}


