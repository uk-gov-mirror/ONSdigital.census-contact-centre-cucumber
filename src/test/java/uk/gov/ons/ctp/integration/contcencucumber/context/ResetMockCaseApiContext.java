package uk.gov.ons.ctp.integration.contcencucumber.context;

import static io.cucumber.spring.CucumberTestContext.SCOPE_CUCUMBER_GLUE;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.caseapiclient.caseservice.model.CaseContainerDTO;
import uk.gov.ons.ctp.integration.contcencucumber.cucSteps.YamlPropertySourceFactory;

@Component
@EnableConfigurationProperties
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:cases.yml")
@ConfigurationProperties("casedata")
@Data
@NoArgsConstructor
@Scope(SCOPE_CUCUMBER_GLUE)
public class ResetMockCaseApiContext {

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

  @Value("${keystore}")
  private String keyStore;

  private List<CaseContainerDTO> caseList;

  private static final Logger log = LoggerFactory.getLogger(ResetMockCaseApiContext.class);

  public void setCases(final String cases) throws IOException {
    System.out.println("Resetting Mock Case API Data");
    log.info("Resetting Mock Case API Data");
    resetData();

    final ObjectMapper objectMapper = new ObjectMapper();
    caseList = objectMapper.readValue(cases, new TypeReference<List<CaseContainerDTO>>() {});
    final boolean failTest = false;
    postCasesToMockService(caseList, failTest);
  }

  public RestTemplate getRestTemplate() {
    return getRestTemplate(ccUsername, ccPassword);
  }

  public RestTemplate getRestTemplate(final String username, final String password) {
    return new RestTemplateBuilder().basicAuthentication(username, password).build();
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

  public CaseContainerDTO getCase(String caseId) {
    return caseList
        .stream()
        .filter(c -> c.getId().toString().equals(caseId))
        .findFirst()
        .orElse(null);
  }

  public RestTemplate getAuthenticationFreeRestTemplate() {
    return new RestTemplateBuilder().build();
  }

  public void postCasesToMockService(final List<CaseContainerDTO> caseList, boolean failTest) {
    UriComponentsBuilder builder =
        UriComponentsBuilder.fromHttpUrl(mcsBaseUrl)
            .port(mcsBasePort)
            .pathSegment("cases")
            .pathSegment("data")
            .pathSegment("cases")
            .pathSegment("save");
    for (CaseContainerDTO caseContainer : caseList) {
      final List<CaseContainerDTO> postCaseList = Collections.singletonList(caseContainer);
      try {
        getAuthenticationFreeRestTemplate()
            .postForObject(builder.build().encode().toUri(), postCaseList, HashMap.class);
      } catch (HttpClientErrorException mockDuplicateCaseException) {
        final String mockDuplicateCaseErrorMessage = "Posted duplicate case - exception thrown by mock case service - case: "
            + caseContainer.getId() + " - " + mockDuplicateCaseException.getMessage();
        if (failTest) {
          log.error(mockDuplicateCaseErrorMessage);
          throw new RuntimeException(mockDuplicateCaseException);
        }
        else {
          log.warn(mockDuplicateCaseErrorMessage);
        }
      }
    }
  }
}
