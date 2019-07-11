package uk.gov.ons.ctp.integration.contcencucumber.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
//import uk.gov.ons.ctp.common.rest.RestClient;
//import uk.gov.ons.ctp.common.rest.RestClientConfig;
import uk.gov.ons.ctp.integration.contcencucumber.config.AppConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {
  
    private AppConfig appConfig;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    
    /**
     * Constructor for ContactCentreSvcApplication
     *
     * @param appConfig contains the configuration for the current deployment.
     */
    @Autowired
    public Application(final AppConfig appConfig) {
      this.appConfig = appConfig;
    }

//    @Bean
//    @Qualifier("addressIndexClient")
//    public RestClient addressIndexClient() {
//      RestClientConfig clientConfig = appConfig.getAddressIndexSettings().getRestClientConfig();
//      RestClient restHelper = new RestClient(clientConfig, httpErrorMapping, defaultHttpStatus);
//      return restHelper;
//    }
}
