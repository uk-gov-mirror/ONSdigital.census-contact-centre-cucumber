package uk.gov.ons.ctp.integration.contcencucumber.data;

import io.swagger.client.model.CaseType;
import io.swagger.client.model.EstabType;
import io.swagger.client.model.ModifyCaseRequestDTO;
import io.swagger.client.model.NewCaseRequestDTO;
import io.swagger.client.model.Region;
import java.util.UUID;

public class ExampleData {
  public static ModifyCaseRequestDTO createModifyCaseRequest(final UUID caseId) {
    final ModifyCaseRequestDTO modifyCaseRequest = new ModifyCaseRequestDTO();
    modifyCaseRequest.setAddressLine1("33 Some Road");
    modifyCaseRequest.setAddressLine2("Some Small Area");
    modifyCaseRequest.setAddressLine3("Some Village");
    modifyCaseRequest.setCeOrgName("Some Organisation");
    modifyCaseRequest.setDateTime("2020-08-20T16:50:26.564+01:00");
    modifyCaseRequest.setCaseId(caseId);
    modifyCaseRequest.setEstabType(EstabType.OTHER);
    modifyCaseRequest.setCaseType(CaseType.CE);
    return modifyCaseRequest;
  }

  public static NewCaseRequestDTO createNewCaseRequestDTO() {
    NewCaseRequestDTO newCaseRequest = new NewCaseRequestDTO();
    newCaseRequest.setCaseType(CaseType.SPG);
    newCaseRequest.setAddressLine1("12 Newlands Terrace");
    newCaseRequest.setAddressLine2("Flatfield");
    newCaseRequest.setAddressLine3("Brumble");
    newCaseRequest.setCeOrgName("Claringdon House");
    newCaseRequest.setCeUsualResidents(13);
    newCaseRequest.setEstabType(EstabType.ROYAL_HOUSEHOLD);
    newCaseRequest.setDateTime("2016-11-09T11:44:44.797");
    newCaseRequest.setUprn("3333334");
    newCaseRequest.setRegion(Region.E);
    newCaseRequest.setPostcode("EX2 5WH");
    newCaseRequest.setTownName("Exeter");
    return newCaseRequest;
  }
}
