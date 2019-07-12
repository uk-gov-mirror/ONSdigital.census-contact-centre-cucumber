package uk.gov.ons.ctp.integration.contcencucumber.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/** Application Config bean */
@Component
@Configuration
@ConfigurationProperties
@Data
public class AppConfig {
    private EventGeneratorSettings eventGeneratorSettings;
    private Logging logging;
    
//  // private Rabbitmq rabbitmq;
//  private CaseServiceSettings caseServiceSettings;
  
}
