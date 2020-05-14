#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact Centre Telephone Capture Endpoint
#Scenario: Launch EQ for a household caseType with the Individual = false
#Scenario: Launch EQ for a household caseType with the Individual = true
## (Comments)
Feature: Test Contact Centre Telephone Capture Endpoint


  Scenario Outline: [CR-T144, CR-T145]  I want to verify that the telephone capture endpoint in CC-SERVICE works correctly
    Given confirmed CaseType <caseId> <individual>
    And an empty queue exists for sending SurveyLaunched events
    And EQ is launched <caseType> <caseId> <individual>
    When CC Advisor selects the survey launch
    Then a Survey Launched event is emitted to RM

    Examples:
      | caseId                                  | individual   | caseType  |
      | "3305e937-6fb1-4ce1-9d4c-077f147789bb"  | "false"      | "HH"      |
      | "3305e937-6fb1-4ce1-9d4c-077f147789bb"  | "true"       | "HI"      |

