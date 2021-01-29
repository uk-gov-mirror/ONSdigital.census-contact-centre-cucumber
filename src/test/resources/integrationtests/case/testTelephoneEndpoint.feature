#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact Centre Telephone Capture Endpoint
#Scenario: Launch EQ for a household caseType with the Individual = false
#Scenario: Launch EQ for a household caseType with the Individual = true
## (Comments)
@CC
Feature: Test Contact Centre Telephone Capture Endpoint


  @TeleEndpoint-CR-T144
  Scenario Outline: [CR-T144, CR-T145]  I want to verify that the telephone capture endpoint in CC-SERVICE works correctly
    Given confirmed CaseType <caseId> <individual>
    And an empty queue exists for sending "SURVEY_LAUNCHED" events
    And EQ is launched <caseType> <caseId> <individual>
    When CC Advisor selects the survey launch
    Then a Survey Launched event is emitted to RM

    Examples:
      | caseId                                  | individual   | caseType  |
      | "3305e937-6fb1-4ce1-9d4c-077f147789bb"  | "false"      | "HH"      |
      | "3305e937-6fb1-4ce1-9d4c-077f147789bb"  | "true"       | "HI"      |

  @TeleEndpoint-CR-T412
  Scenario Outline: CR-T412 - NI CE manager receives an error on EQ launch
    Given Case ID <caseId> is region "N" and for case type "CE"
    When getting URL for EQ Launch for Case ID <caseId> and Individual flag <individual>
    Then CC advisor receives error <message>

    Examples:
      | caseId                                 | individual | message |
      | "cb46a66a-494f-45ea-ba46-8186069bbb6f" | "false"    | "All Northern Ireland calls from CE Managers are to be escalated to the NI management team" |
