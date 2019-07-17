package uk.gov.ons.ctp.integration.contcencucumber.client.generatorService;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import uk.gov.ons.ctp.common.rest.RestClient;
//import uk.gov.ons.ctp.integration.contactcentresvc.client.caseservice.model.CaseContainerDTO;
import uk.gov.ons.ctp.integration.contcencucumber.config.AppConfig;
import uk.gov.ons.ctp.integration.contcencucumber.model.GeneratorRequest;
import uk.gov.ons.ctp.common.event.model.CollectionCaseResponse;
import uk.gov.ons.ctp.common.event.EventPublisher;
import uk.gov.ons.ctp.common.event.EventPublisher.EventType;


/** This class is responsible for communications with the Case Service. */
//@Service
//@Validated
public class GeneratorServiceClientServiceImpl {
  private static final Logger log = LoggerFactory.getLogger(GeneratorServiceClientServiceImpl.class);

  @Autowired private AppConfig appConfig;

  @Inject
  @Qualifier("eventGeneratorClient")
  private RestClient eventGeneratorClient;
//  
  public CollectionCaseResponse postGenerateCaseCreated() {
    log.debug("postGenerateCaseCreated");
    
//  Build map for query params
    Map<String, String> queryParams = new HashMap<String, String>();
    queryParams.put("caseRef", "hello");
    queryParams.put("id", "#uuid");
    
    List<Map<String, String>> listForRequest = new ArrayList<Map<String, String>>();
    listForRequest.add(queryParams);
    
    //Ask Generator to generate a CaseCreated event
    String path = appConfig.getEventGeneratorSettings().getGeneratorPath();
    
    GeneratorRequest generatorRequest = new GeneratorRequest();
    generatorRequest.setEventType(EventPublisher.EventType.CASE_CREATED);
    generatorRequest.setSource(EventPublisher.Source.RESPONDENT_HOME);
    generatorRequest.setChannel(EventPublisher.Channel.RH);
    generatorRequest.setContexts(listForRequest);
    
    CollectionCaseResponse collectionCases = eventGeneratorClient.postResource(path, generatorRequest, CollectionCaseResponse.class, null);
    
    return collectionCases;
    
  }

//  public CaseContainerDTO getCaseById(UUID caseId, Boolean listCaseEvents) {
//    log.debug("getCaseById. Calling Case Service to find case details by ID: " + caseId);
//
//    // Build map for query params
//    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//    queryParams.add("caseEvents", Boolean.toString(listCaseEvents));
//
//    // Ask Case Service to find case details
//    String path = appConfig.getCaseServiceSettings().getCaseByIdQueryPath();
//    CaseContainerDTO caseDetails =
//        caseServiceClient.getResource(
//            path, CaseContainerDTO.class, null, queryParams, caseId.toString());
//    log.debug("getCaseById. Found details for case: " + caseId);
//
//    return caseDetails;
//  }

//  public List<CaseContainerDTO> getCaseByUprn(Long uprn, Boolean listCaseEvents) {
//    log.debug("getCaseByUprn. Calling Case Service to find case details by Uprn: " + uprn);
//
//    // Build map for query params
//    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
//    queryParams.add("caseEvents", Boolean.toString(listCaseEvents));
//
//    // Ask Case Service to find case details
//    String path = appConfig.getCaseServiceSettings().getCaseByUprnQueryPath();
//    List<CaseContainerDTO> cases =
//        caseServiceClient.getResources(
//            path, CaseContainerDTO[].class, null, queryParams, Long.toString(uprn));
//
//    log.debug("getCaseByUprn. Found details for Uprn" + uprn);
//
//    return cases;
//  }

  
}
