package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases;

import io.swagger.client.model.RefusalRequestDTO;
import io.swagger.client.model.RefusalRequestDTO.ReasonEnum;
import io.swagger.client.model.RefusalRequestDTO.RegionEnum;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import uk.gov.ons.ctp.common.event.model.AddressCompact;
import uk.gov.ons.ctp.common.event.model.Contact;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RefusalFixture {

  public static final ReasonEnum A_REASON = ReasonEnum.HARD;
  public static final String AN_AGENT_ID = "123";
  public static final String A_CALL_ID = "292-CALLME";
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
  public static final RegionEnum A_REGION = RegionEnum.E;
  public static final String A_UPRN_STR = "1234";

  public static RefusalRequestDTO createRequest(String caseId, String agentId, ReasonEnum reason) {

    RefusalRequestDTO refusal = new RefusalRequestDTO();
    refusal
        .caseId(caseId)
        .agentId(agentId)
        .callId(A_CALL_ID)
        .notes(SOME_NOTES)
        .title(A_TITLE)
        .forename(A_FORENAME)
        .surname(A_SURNAME)
        .telNo(A_TEL_NO)
        .addressLine1(AN_ADDR_LINE_1)
        .addressLine2(AN_ADDR_LINE_2)
        .addressLine3(AN_ADDR_LINE_3)
        .townName(A_TOWN)
        .postcode(A_POSTCODE)
        .uprn(A_UPRN_STR)
        .region(A_REGION)
        .reason(reason)
        .dateTime(OffsetDateTime.now(ZoneId.of("Z")).withNano(0).toString());

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
