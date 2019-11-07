#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Case Endpoints
## (Comments)
Feature: Test Contact centre Case Endpoints
I want to verify that all endpoints in CC-SERVICE work correctly

  Scenario Outline: I want to verify that the case search by case ID works
    Given I have a valid case ID <caseId>
    When I Search cases By case ID <caseEvents>
    Then the correct case for my case ID is returned <uprn>
    And the correct number of events are returned <caseEvents> <noCaseEvents>

    Examples:
      | caseId                                  | uprn        | caseEvents  | noCaseEvents  |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab"  | 1347459999  | "true"      | 2             |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ac"  | 1347459999  | "true"      | 1             |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ab"  | 1347459999  | "false"     | 0             |
      | "3305e937-6fb1-4ce1-9d4c-077f147789ac"  | 1347459999  | "false"     | 0             |
      | "03f58cb5-9af4-4d40-9d60-c124c5bddf09"  | 1347459999  | "true"      | 0             |

  Scenario Outline: I want to verify that the case search by invalid case ID works
    Given I have an invalid case ID <caseId>
    When I Search for cases By case ID
    Then An error is thrown and no case is returned <httpError>

    Examples:
      | caseId                                  | httpError   |
      | "40074ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "500"       |
      | "40174ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "500"       |
      | "40474ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "404"       |
      | "50074ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "500"       |

  Scenario Outline: I want to verify that the case search by case UPRN works
    Given I have a valid UPRN <uprn>
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>

    Examples:
      | uprn          | case_ids                                                                                                           |
      | "1347459999"  | "3305e937-6fb1-4ce1-9d4c-077f147789ab,3305e937-6fb1-4ce1-9d4c-077f147789ac,03f58cb5-9af4-4d40-9d60-c124c5bddf09"   |

  Scenario Outline: I want to verify that the case search by invalid case UPRN works
    Given I have an invalid UPRN <uprn>
    When I Search cases By invalid UPRN
    Then no cases for my UPRN are returned <httpError>

    Examples:
      | uprn          | httpError   |
      | "1347459998"  | "404"       |
      | "abcdefghik"  | "400"       |
      | "1111111111"  | "404"       |
