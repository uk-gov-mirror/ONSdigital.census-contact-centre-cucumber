package uk.gov.ons.ctp.integration.contcencucumber.cucSteps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import uk.gov.ons.ctp.integration.caseapiclient.caseservice.model.CaseContainerDTO;
import uk.gov.ons.ctp.integration.contcencucumber.main.SpringIntegrationTest;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
@EnableConfigurationProperties
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:cases.yml")
@ConfigurationProperties("casedata")
public class TestEndpointsFFData extends SpringIntegrationTest {

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
    @Value("${mock-case-service.username}")
    private String mcsUsername;
    @Value("${mock-case-service.password}")
    private String mcsPassword;

    private static final Logger log = LoggerFactory.getLogger(TestEndpointsFFData.class);

    public void setCases(final String cases) throws IOException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final List<CaseContainerDTO> caseList =
                objectMapper.readValue(cases, new TypeReference<List<CaseContainerDTO>>() {});
        postCasesToMockService(caseList);
    }

    private void postCasesToMockService(final List<CaseContainerDTO> caseList) {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(mcsBaseUrl)
                .port(mcsBasePort)
                .pathSegment("cases")
                .pathSegment("data")
                .pathSegment("cases")
                .pathSegment("add");
        for (CaseContainerDTO caseContainer : caseList) {
            final List<CaseContainerDTO> postCaseList = Arrays.asList(caseContainer);
            try {
                getRestTemplate().postForObject(builder.build().encode().toUri(), postCaseList, HashMap.class);
            }
            catch (HttpClientErrorException ex) {
                log.warn("Posted duplicate cases - exception thrown by mock case service - case: " + caseContainer.getId());
            }
        }
    }

    protected RestTemplate getRestTemplate() {
        return new RestTemplateBuilder().basicAuthentication(ccUsername, ccPassword).build();
    }
}
