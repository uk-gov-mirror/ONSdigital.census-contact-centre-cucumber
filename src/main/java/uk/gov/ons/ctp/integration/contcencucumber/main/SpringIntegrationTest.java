package uk.gov.ons.ctp.integration.contcencucumber.main;

import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

@ContextConfiguration(
        classes = SpringIntegrationTest.class,
        loader = SpringBootContextLoader.class)
@WebAppConfiguration
@SpringBootTest
public class SpringIntegrationTest {

}

