package uk.gov.ons.ctp.integration.contcencucumber.main;

import org.springframework.boot.SpringApplication;
import uk.gov.ons.ctp.integration.contcencucumber.config.AppConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
  
    private AppConfig appConfig;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
