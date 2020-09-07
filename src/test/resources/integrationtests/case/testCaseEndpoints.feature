#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CONTACT CENTRE, ASSISTED DIGITAL SERVICE
#Feature: Test Contact Centre, Assisted Digital case endpoints
## (Comments)
Feature: Test Contact Centre, Assisted Digital case endpoints
  I want to verify that all endpoints in CC/AD service work correctly

  @CC @TestCaseEndpointsT134 @SetUpT134
  Scenario Outline: [CR-T134] I want to verify that the case search by case ID works
    Given I have a valid case ID <caseId>
    When I Search cases By case ID <caseEvents>
    Then the correct case for my case ID is returned <uprn>
    And the correct number of events are returned <caseEvents> <noCaseEvents>
    And the establishment UPRN is <estabUprn>
    And the secure establishment is set to <secure>

    Examples:
      | caseId                                 | uprn       | caseEvents | noCaseEvents | estabUprn      | secure  |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab" | 1710030106 | "true"     |            2 | "334111111111" | "true"  |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ac" | 1710030106 | "true"     |            1 | ""             | "false" |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab" | 1710030106 | "false"    |            0 | "334111111111" | "true"  |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ac" | 1710030106 | "false"    |            0 | ""             | "false" |
      | "03f58cb5-9af4-4d40-9d60-c124c5bddf09" | 1710030106 | "true"     |            0 | ""             | "false" |

  @CC
  Scenario Outline: [CR-T135] I want to verify that the case search by invalid case ID works
    Given I have an invalid case ID <caseId>
    When I Search for cases By case ID
    Then An error is thrown and no case is returned <httpError>

    Examples:
      | caseId                                 | httpError |
      | "40074ef9-2a0c-4a5c-bb69-d3fc5bfa10dc" | "500"     |
      | "40174ef9-2a0c-4a5c-bb69-d3fc5bfa10dc" | "500"     |
      | "40474ef9-2a0c-4a5c-bb69-d3fc5bfa10dc" | "404"     |
      | "50074ef9-2a0c-4a5c-bb69-d3fc5bfa10dc" | "500"     |

  @CC @AD
  Scenario Outline: [CR-T136] I want to verify that the case search by case UPRN works
    Given I have a valid UPRN <uprn>
    And cached cases for the UPRN do not already exist
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>

    Examples:
      | uprn         | case_ids                                                                                                         |
      | "1710030106" | "3305e937-6fb1-4ce1-9d4c-077f147789ab,3305e937-6fb1-4ce1-9d4c-077f147789ac,03f58cb5-9af4-4d40-9d60-c124c5bddf09" |

  @CC @AD
  Scenario Outline: [CR-T137] I want to verify that the case search by invalid case UPRN works
    Given I have an invalid UPRN <uprn>
    When I Search cases By invalid UPRN
    Then no cases for my UPRN are returned <httpError>

    Examples:
      | uprn         | httpError |
      | "1347459998" | "404"     |
      | "abcdefghik" | "400"     |
      | "1111111111" | "404"     |

  @CC @ValidRefusalIsAccepted
  Scenario Outline: I want to verify that a valid Refusal is accepted
    Given I have a valid case ID <caseId>
    And I supply the Refusal information
    When I Refuse a case
    Then the call succeeded and responded with the supplied case ID

    Examples:
      | caseId                                 |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab" |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ac" |

  @CC @RefusalReasonAcceptedAndEventPosted
  Scenario Outline: I want to verify that a valid reason for Refusal is accepted and event posted
    Given I have a valid case ID <caseId>
    And an empty queue exists for sending Refusal events
    And I supply a <reason> reason for Refusal
    And I supply the Refusal information
    When I Refuse a case
    Then the call succeeded and responded with the supplied case ID
    And a Refusal event is sent with type <type>

    Examples:
      | caseId                                 | reason          | type                    |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab" | "HARD"          | "HARD_REFUSAL"          |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab" | "EXTRAORDINARY" | "EXTRAORDINARY_REFUSAL" |

  @CC
  Scenario Outline: I want to verify that a Refusal without a reason is rejected
    Given I have a valid case ID <caseId>
    And I supply a <reason> reason for Refusal
    And I supply the Refusal information
    When I Refuse a case
    Then An error is thrown and no case is returned <httpError>

    Examples:
      | caseId                                 | reason | httpError |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab" | ""     | "400"     |

  @CC
  Scenario Outline: I want to verify that a Refusal without a valid agentId is rejected
    Given I have a valid case ID <caseId>
    And I supply an <agentId> agentId for Refusal
    And I supply the Refusal information
    When I Refuse a case
    Then An error is thrown and no case is returned <httpError>

    Examples:
      | caseId                                 | agentId       | httpError |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab" | ""            | "400"     |

  @CC
  Scenario Outline: I want to verify that an invalid Case ID for Refusal is rejected
    Given I have an invalid case ID <caseId>
    And I supply the Refusal information
    When I Refuse a case
    Then An error is thrown and no case is returned <httpError>

    Examples:
      | caseId                                 | httpError |
      | "NOTKNOWN"                             | "400"     |
      | "XX474ef9-2a0c-4a5c-bb69-d3fc5bfa10dc" | "400"     |

  @CC @InvalidateCase @SetUp
  Scenario Outline: [CR-T357] Invalid Address
    Given the CC advisor has provided a valid UPRN <uprn>
    Then the Case endpoint returns a case associated with UPRN <uprn>
    Given an empty queue exists for sending AddressNotValid events
    When CC Advisor selects the address status change <status>
    Then an AddressNotValid event is emitted to RM, which contains the <status> change

    Examples:
      | uprn         | status               |
      | "1710030095" | "DERELICT"           |
      | "1710030095" | "DEMOLISHED"         |
      | "1710030095" | "NON_RESIDENTIAL"    |
      | "1710030095" | "UNDER_CONSTRUCTION" |
      | "1710030095" | "SPLIT_ADDRESS"      |
      | "1710030095" | "MERGED"             |
      | "1710030095" | "DUPLICATE"          |
      | "1710030095" | "DOES_NOT_EXIST"     |

  @CC @TestCaseEndpointsT379 @SetUp
  Scenario Outline: [CR-T379] No Invalid Address Event for CE
    Given the CC advisor has provided a valid UPRN "1710030113"
    And the Case endpoint returns a CE case associated with UPRN "1710030113"
    When CC Advisor selects the CE address status change <status>
    Then a "400 Bad Request" error is returned along with the message about CE addresses

    Examples:
      | status               |
      | "DERELICT"           |
      | "DEMOLISHED"         |
      | "NON_RESIDENTIAL"    |
      | "UNDER_CONSTRUCTION" |
      | "SPLIT_ADDRESS"      |
      | "MERGED"             |
      | "DUPLICATE"          |
      | "DOES_NOT_EXIST"     |

  @CC @AD @CaseTestT148
  Scenario: [CR-T148] Publish a new address event to RM
    Given the agent has confirmed the respondent address
    And the case service does not have any case created for the address in question
    And Get/Case API returns a "404" error because there is no case found
    And an empty queue exists for sending NewAddressReported events
    And cached cases for the UPRN do not already exist
    Given the service creates a fake Case with the address details from AIMS
    Then the service must publish a new address event to RM with the fake CaseID

  @CC @AD
  Scenario: [CR-T377] AddressType Not Applicable
    Given the CC agent has selected an address that is not of addressType CE, HH, or SPG
    And the case service does not have any case created for the address in question
    When Get/Case API returns a "404" error because there is no case found
    Then the CC SVC must also return a "404 Not Found" error

  @AD @CR-T383
  Scenario Outline: [CR-T383]  AD advisor wants to get a new UAC for the respondent
    Given the AD advisor has the <caseId> for a case with <caseType>, <region> and <addressLevel>
    And the AD advisor requests a new UAC for <caseId> <individual>  
    Then the AD advisor receives a <httpResponse> with new UAC and QID if successful

    Examples:
      | caseId                                  | individual   | caseType  | region | addressLevel | httpResponse |  
      | "03f58cb5-9af4-4d40-9d60-c124c5bddfff"  | "false"      | "HH"      | "W"    | "E"          | 200          |
      | "3305e937-6fb1-4ce1-9d4c-077f147789bb"  | "true"       | "HH"      | "E"    | "E"          | 200          |
      | "3305e937-6fb1-4ce1-9d4c-077f147789aa"  | "false"      | "HH"      | "N"    | "E"          | 200          |
      | "3305e937-6fb3-4ce1-9d4c-077f147789de"  | "false"      | "CE"      | "E"    | "E"          | 200          |
      | "cb46a66a-494f-45ea-ba46-8186069bbb6f"  | "true"       | "CE"      | "N"    | "E"          | 200          |
      | "cb46a66a-494f-45ea-ba46-8186069bbb6f"  | "false"      | "CE"      | "N"    | "E"          | 400          |
      | "3305e937-6fb1-4ce1-9d4c-077f147722aa"  | "false"      | "CE"      | "W"    | "U"          | 400          |    
      | "3305e937-6fb1-4ce1-9d4c-077f147789dd"  | "true"       | "HI"      | "E"    | "E"          | 400          |
      | "3305e937-6fb1-4ce1-9d4c-770f147711aa"  | "false"      | "SPG"     | "W"    | "E"          | 200          |
      | "3305e937-6fb1-4ce1-9d4c-077f147733aa"  | "true"       | "SPG"     | "N"    | "U"          | 200          |

  @CC @TestCaseEndpointsT382
  Scenario Outline: [CR-T382, CR-T384] Get latest case from RM
    Given the case with id "5a54ee1f-3552-4a46-adcc-0940f0998f90" and uprn "1710030112" does not exist in the cache
    And an empty queue exists for sending "ADDRESS_MODIFIED" events
    And the case exists in RM and can be fetched using <endpoint>
    When the case address details are modified by a member of CC staff
    And the case modified event is sent to RM and RM does immediately action it
    And the call is made to fetch the case again from <endpoint>
    Then <endpoint> gets the modified case from RM
    
    Examples: 
      | endpoint        | 
      | "GetCaseByUPRN" |
      #| "GetCaseByID"   |