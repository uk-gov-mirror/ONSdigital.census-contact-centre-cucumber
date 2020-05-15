package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases;


import io.swagger.client.model.RefusalRequestDTO;
import java.util.Date;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.common.event.model.AddressCompact;
import uk.gov.ons.ctp.common.event.model.Contact;
import uk.gov.ons.ctp.common.model.UniquePropertyReferenceNumber;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RefusalFixture {

  public static final String A_REASON = "HARD";
  public static final String AN_AGENT_ID = "123";
  public static final String SOME_NOTES = "Description of refusal";
  public static final String A_TITLE = "Mr";
  public static final String A_FORENAME = "Steve";
  public static final String A_SURNAME = "Jones";
  public static final String A_TEL_NO = "07968583119";
  public static final String AN_ADDR_LINE_1 = "1 High Street";
  public static final String AN_ADDR_LINE_2 = "Delph";
  public static final String AN_ADDR_LINE_3 = "Oldham";
  public static final String A_TOWN = "Manchester";
  public static final String A_POSTCODE = "OL3 5DJ";
  public static final String A_REGION = "E";
  public static final String A_UPRN_STR = "1234";
  public static final UniquePropertyReferenceNumber A_UPRN =
      new UniquePropertyReferenceNumber(A_UPRN_STR);

  public static RefusalRequestDTO createRequest(String caseId, String agentId, String reason) {
    Date dateTime = new Date();

    RefusalRequestDTO refusal =
        new RefusalRequestDTO();
            refusal.setCaseId(caseId);
            refusal.setAgentId(agentId);
            refusal.setNotes(SOME_NOTES);
            refusal.setTitle(A_TITLE);
            refusal.setForename(A_FORENAME);
            refusal.setSurname(A_SURNAME);
            refusal.setTelNo(A_TEL_NO);
            refusal.setAddressLine1(AN_ADDR_LINE_1);
            refusal.setAddressLine2(AN_ADDR_LINE_2);
            refusal.setAddressLine3(AN_ADDR_LINE_3);
            refusal.setTownName(A_TOWN);
            refusal.setPostcode(A_POSTCODE);
            refusal.setUprn(Long.getLong(A_UPRN_STR);
            refusal.setRegion(A_REGION);
    refusal.setReason(reason)
            .dateTime(dateTime)
            .build();
    return refusal;
  }

  // to match details in the request DTO
  public static AddressCompact compactAddress() {
    AddressCompact addr = new AddressCompact();
    addr.setAddressLine1(AN_ADDR_LINE_1);
    addr.setAddressLine2(AN_ADDR_LINE_2);
    addr.setAddressLine3(AN_ADDR_LINE_3);
    addr.setTownName(A_TOWN);
    addr.setPostcode(A_POSTCODE);
    addr.setRegion(A_REGION.name());
    addr.setUprn(A_UPRN_STR);
    return addr;
  }

  // to match details in the request DTO
  public static Contact contact() {
    Contact contact = new Contact();
    contact.setTitle(A_TITLE);
    contact.setForename(A_FORENAME);
    contact.setSurname(A_SURNAME);
    contact.setTelNo(A_TEL_NO);
    return contact;
  }
}
