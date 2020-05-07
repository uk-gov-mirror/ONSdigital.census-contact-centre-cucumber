package uk.gov.ons.ctp.integration.contcencucumber.main.repository.impl;

import com.godaddy.logging.Logger;
import com.godaddy.logging.LoggerFactory;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.error.CTPException.Fault;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CachedCase;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CloudDataStore;
import uk.gov.ons.ctp.integration.contcencucumber.main.repository.CaseDataRepository;

@Service
public class CaseDataRepositoryImpl implements CaseDataRepository {

  private static final Logger log = LoggerFactory.getLogger(CaseDataRepositoryImpl.class);

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
  public Optional<CachedCase> readCachedCaseByUPRN(final UniquePropertyReferenceNumber uprn)
      throws CTPException {
    log.info("Entering readCachedCaseByUPRN");
    log.with(gcpProject).info("gcpProject");
    log.with(caseSchemaName).info("caseSchemaName");
    log.with(caseSchema).info("caseSchema");
    String key = String.valueOf(uprn.getValue());
    String[] searchByUprnPath = new String[] {"uprn"};
    List<CachedCase> results =
        cloudDataStore.search(CachedCase.class, caseSchema, searchByUprnPath, key);

    if (results.isEmpty()) {
      return Optional.empty();
    } else if (results.size() > 1) {
      log.with("uprn", key).error("More than one cached skeleton case for UPRN");
      throw new CTPException(
          Fault.SYSTEM_ERROR, "More than one cached skeleton case for UPRN: " + key);
    } else {
      return Optional.ofNullable(results.get(0));
    }
  }

  @Override
  public void deleteCachedCase(String key) throws CTPException {

    cloudDataStore.deleteObject(caseSchema, key);
  }
}
