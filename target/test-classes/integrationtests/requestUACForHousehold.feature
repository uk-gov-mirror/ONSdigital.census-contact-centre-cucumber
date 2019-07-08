#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : Household UAC
#Feature: Request a Household UAC
#Scenario: Submit UAC Request to RM
## (Comments)
Feature: Request a Household UAC
  I want to get a new UAC for completing my Household Census Questionnaire

  Scenario: Submit UAC Request to RM
    Given I am on the RH Start Page  - method 2
    And I select the “Request UAC” option
    Then I am presented with a form for entering my postcode
    Given I enter a valid UK postcode
    And I select the “Find Address” option
    Then I am presented with the AI list of available addresses for the postcode
    Given I select my address from the AI list
    And I select “Continue” - method 1
    Then I am presented with a page to confirm my address - method 2
    And the address confirmation page contains two options - method 2
    Given I select the "Yes, this address is correct" option - method 2
    And the address is one of the addresses that’s in the rehearsal
    And there is exactly 1 HH case that’s associated with the UPRN
    Then I am presented with a form for entering a mobile no.
    Given I enter my UK mobile no.
    And I select “Continue” - method 2
    Then I am presented with a page to confirm my mobile no.
    And the mobile no. confirmation page contains two options
    Given I select the "Yes, send the text now" option
    And the region associated with the HH case is NOT Wales
    Then my UAC request is submitted
    And I am presented with a page containing a confirmation message
