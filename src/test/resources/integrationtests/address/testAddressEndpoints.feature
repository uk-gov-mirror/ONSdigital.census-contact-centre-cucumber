#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Address Endpoints
#Scenario: Get a list of addresses by valid postcode
## (Comments)
Feature: Test Contact centre Address Endpoints
  I want to verify that all address endpoints in CC-SERVICE work correctly

  Scenario Outline: [CR-T129] I want to verify that address search by postcode works
    Given I have a valid Postcode <postcode>
    When I Search Addresses By Postcode
    Then A list of addresses for my postcode is returned

    Examples: 
      | postcode  |
      | "EX4 1EH" |
      | "EX41EH"  |

  Scenario Outline: [CR-T130] I want to verify that address search by invalid postcode works
    Given I have an invalid Postcode <postcode>
    When I Search Addresses By Invalid Postcode
    Then An empty list of addresses for my postcode is returned

    Examples: 
      | postcode   |
      | "ZZ99 9ZZ" |
      | "XXX SSS"  |

  Scenario Outline: [CR-T131] I want to verify that address search by address works
    Given I have a valid address <address>
    When I Search Addresses By Address Search
    Then A list of addresses for my search is returned

    Examples: 
      | address          |
      | "Chelmsford"     |
      | "Bristol Street" |
      | "Plymouth"       |

  Scenario Outline: [CR-T132] I want to verify that invalid address search by address works
    Given I have an invalid address <address>
    When I Search invalid Addresses By Address Search
    Then An empty list of addresses for my search is returned

    Examples: 
      | address              |
      | "Chimpanzee"         |
      | "Boaty McBoat Face " |
      | "Strawberry Laces"   |

  Scenario: [CR-T147] AIMS endpoint to provide region code, address type and establishment type
    Given the respondent calls the CC with a fulfilment request
    And the respondent address exists in AIMS
    When the CC agent searches for the address
    Then the CC SVC returns address attributes with region code, address type and establishment type