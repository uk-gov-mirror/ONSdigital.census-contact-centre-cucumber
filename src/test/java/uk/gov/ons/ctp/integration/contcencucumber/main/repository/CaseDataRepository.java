package uk.gov.ons.ctp.integration.contcencucumber.main.repository;

import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.ons.ctp.common.cloud.TestCloudDataStore;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CachedCase;

@Service
public class CaseDataRepository {
  private static final String[] SEARCH_BY_UPRN_PATH = new String[] {"uprn"};

  @Value("${google-cloud-project}")
  private String gcpProject;

  @Value("${cloud-storage.case-schema-name}")
  private String caseSchemaName;

  private String caseSchema;

  @Autowired private TestCloudDataStore cloudDataStore;

  @PostConstruct
  public void init() {
    caseSchema = gcpProject + "-" + caseSchemaName.toLowerCase();
  }

  public List<CachedCase> readCachedCasesByUprn(UniquePropertyReferenceNumber uprn)
      throws CTPException {
    String key = String.valueOf(uprn.getValue());
    return cloudDataStore.search(CachedCase.class, caseSchema, SEARCH_BY_UPRN_PATH, key);
  }

  public void deleteCachedCase(String key) throws CTPException {
    cloudDataStore.deleteObject(caseSchema, key);
  }
}
