package uk.gov.ons.ctp.integration.contcencucumber.main.repository;

import java.util.Optional;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CachedCase;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.common.model.UniquePropertyReferenceNumber;

/** Repository for Case Data */
public interface CaseDataRepository {

  /**
   * Read a Case for an address by Unique Property Reference Number
   *
   * @param uprn of case to read
   * @return Optional containing case for UPRN if available
   * @throws CTPException error reading case
   */
  Optional<CachedCase> readCachedCaseByUPRN(final UniquePropertyReferenceNumber uprn)
      throws CTPException;
}
