package uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases;

import io.swagger.client.model.RefusalRequestDTO;
import io.swagger.client.model.RefusalRequestDTO.ReasonEnum;
import io.swagger.client.model.Region;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import uk.gov.ons.ctp.common.event.model.AddressCompact;

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
  public static final Region A_REGION = Region.E;
  public static final String A_UPRN_STR = "1234";

  public static RefusalRequestDTO createRequest(UUID caseId, String agentId, ReasonEnum reason) {

    RefusalRequestDTO refusal = new RefusalRequestDTO();
    refusal
        .caseId(caseId)
        .agentId(StringUtils.isBlank(agentId) ? null : Integer.valueOf(agentId))
        .callId(A_CALL_ID)
        .title(A_TITLE)
        .forename(A_FORENAME)
        .surname(A_SURNAME)
        .addressLine1(AN_ADDR_LINE_1)
        .addressLine2(AN_ADDR_LINE_2)
        .addressLine3(AN_ADDR_LINE_3)
        .townName(A_TOWN)
        .postcode(A_POSTCODE)
        .uprn(A_UPRN_STR)
        .region(A_REGION)
        .reason(reason)
        .isHouseholder(true)
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
}
