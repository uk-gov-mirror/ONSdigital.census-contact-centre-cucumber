#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Case Endpoints
## (Comments)
Feature: Test Contact centre Case Endpoints
I want to verify that all endpoints in CC-SERVICE work correctly

  Scenario Outline: I want to verify that the case search by case ID works
    Given I have a valid case ID <caseId>
    When I Search cases By case ID
    Then the correct case for my case ID is returned <uprn>

    Examples:
      | caseId                                  | uprn        |
      | "8fb74ef4-2a0c-4a5c-bb69-d3fc5bfa10dc"  | 1347459999  |

  Scenario Outline: I want to verify that the case search by invalid case ID works
    Given I have an invalid case ID <caseId>
    When I Search for cases By case ID
    Then An error is thrown and no case is returned <httpError>

    Examples:
      | caseId                                  | httpError   |
      | "40074ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "404"       |
      | "40174ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "404"       |
      | "40474ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "404"       |
      | "50074ef9-2a0c-4a5c-bb69-d3fc5bfa10dc"  | "404"       |


