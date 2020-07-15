package uk.gov.ons.ctp.integration.contcencucumber.main.repository;

import java.util.List;
import uk.gov.ons.ctp.common.domain.UniquePropertyReferenceNumber;
import uk.gov.ons.ctp.common.error.CTPException;
import uk.gov.ons.ctp.integration.contcencucumber.cloud.CachedCase;

/** Repository for Case Data */
public interface CaseDataRepository {

  /**
   * Get all Cached cases for an address by Unique Property Reference Number.
   *
   * @param uprn UPRN of the case to read
   * @return list of cached cases found that match the given UPRN
   * @throws CTPException on error
   */
  List<CachedCase> readCachedCasesByUprn(final UniquePropertyReferenceNumber uprn)
      throws CTPException;

  /**
   * Delete a case from Firestore
   *
   * @param key of case to delete
   * @throws CTPException
   */
  void deleteCachedCase(String key) throws CTPException;
}
