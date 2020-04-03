package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.caseapiclient.caseservice.model.CaseContainerDTO;
import uk.gov.ons.ctp.integration.contcencucumber.main.SpringIntegrationTest;

@Component
@EnableConfigurationProperties
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:cases.yml")
@ConfigurationProperties("casedata")
public class ResetMockCaseApiAndPostCasesBase extends SpringIntegrationTest {

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

  private static final Logger log = LoggerFactory.getLogger(ResetMockCaseApiAndPostCasesBase.class);

  public void setCases(final String cases) throws IOException {
    log.info("Resetting Mock Case API Data");
    resetData();

    final ObjectMapper objectMapper = new ObjectMapper();
    final List<CaseContainerDTO> caseList =
        objectMapper.readValue(cases, new TypeReference<List<CaseContainerDTO>>() {});
    postCasesToMockService(caseList);
  }

  private void postCasesToMockService(final List<CaseContainerDTO> caseList) {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(mcsBaseUrl)
            .port(mcsBasePort)
            .pathSegment("cases")
            .pathSegment("data")
            .pathSegment("cases")
            .pathSegment("add");
    for (CaseContainerDTO caseContainer : caseList) {
      final List<CaseContainerDTO> postCaseList = Arrays.asList(caseContainer);
      try {
        getAuthenticationFreeRestTemplate()
            .postForObject(builder.build().encode().toUri(), postCaseList, HashMap.class);
      } catch (HttpClientErrorException ex) {
        log.warn(
            "Posted duplicate cases - exception thrown by mock case service - case: "
                + caseContainer.getId());
      }
    }
  }

  protected RestTemplate getRestTemplate() {
    return getRestTemplate(ccUsername, ccPassword);
  }

  private void resetData() {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(mcsBaseUrl)
            .port(mcsBasePort)
            .pathSegment("cases")
            .pathSegment("data")
            .pathSegment("cases")
            .pathSegment("reset");
    try {
      getAuthenticationFreeRestTemplate()
          .getForObject(builder.build().encode().toUri(), HashMap.class);
    } catch (HttpClientErrorException ex) {
      fail("Unable to RESET Mock case api service: ");
    }
  }
}
