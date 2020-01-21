#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact Centre Telephone Capture Endpoint
#Scenario: Launch EQ for a household
#Scenario: Launch EQ for an Individual
## (Comments)
Feature: Test Contact Centre Telephone Capture Endpoint
  I want to verify that the telephone capture endpoint in CC-SERVICE works correctly

  @SetUpRHEngScot @TearDown
  Scenario: Launch EQ for a household
    Given the CC advisor has the respondent address
    And the respondent case type is a household
    When the CC advisor confirms the address
    Then a HH EQ is launched

  @SetUpRHEngScot @TearDown
  Scenario: Launch EQ for an Individual
    Given the CC advisor has the respondent address
    And the respondent case type is a household individual or a CE individual
    When the CC advisor confirms the address
    Then a HH EQ is launched
    
 
