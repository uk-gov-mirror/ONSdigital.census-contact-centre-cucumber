package uk.gov.ons.ctp.integration.contcencucumber.main.repository.impl;

import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CachedCase;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CloudDataStore;
import uk.gov.ons.ctp.integration.contcencucumber.main.repository.CaseDataRepository;

@Service
public class CaseDataRepositoryImpl implements CaseDataRepository {
  private static String[] SEARCH_BY_UPRN_PATH = new String[] {"uprn"};

  @Value("${google-cloud-project}")
  private String gcpProject;

  @Value("${cloud-storage.case-schema-name}")
  private String caseSchemaName;

  private String caseSchema;

  @Autowired private CloudDataStore cloudDataStore;

  @PostConstruct
  public void init() {
    caseSchema = gcpProject + "-" + caseSchemaName.toLowerCase();
    this.cloudDataStore.connect();
  }

  @Override
  public List<CachedCase> readCachedCasesByUprn(UniquePropertyReferenceNumber uprn)
      throws CTPException {
    String key = String.valueOf(uprn.getValue());
    return cloudDataStore.search(CachedCase.class, caseSchema, SEARCH_BY_UPRN_PATH, key);
  }

  @Override
  public void deleteCachedCase(String key) throws CTPException {
    cloudDataStore.deleteObject(caseSchema, key);
  }
}
