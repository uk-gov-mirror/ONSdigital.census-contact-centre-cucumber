package uk.gov.ons.ctp.integration.contcencucumber.config;

import lombok.Data;
import uk.gov.ons.ctp.common.rest.RestClientConfig;

@Data
public class EventGeneratorSettings {
  private String generatorPath;
  private RestClientConfig restClientConfig;
}
