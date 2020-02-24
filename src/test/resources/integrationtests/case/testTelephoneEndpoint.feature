#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact Centre Telephone Capture Endpoint
#Scenario: Launch EQ for a household
#Scenario: Launch EQ for an Individual
## (Comments)
Feature: Test Contact Centre Telephone Capture Endpoint

  Scenario Outline:   I want to verify that the telephone capture endpoint in CC-SERVICE works correctly

    Given confirmed CaseType <caseId> <individual>
    Then EQ is launched <caseType> <caseId> <individual>

    Examples:
      | caseId                                  | individual   | caseType  |
      | "3305e937-6fb1-4ce1-9d4c-077f147789bb"  | "false"      | "HH"      |
      | "3305e937-6fb1-4ce1-9d4c-077f147789bb"  | "true"       | "HI"      |
