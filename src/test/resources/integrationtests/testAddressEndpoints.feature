#Author: andreiw.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Address Endpoints
#Scenario: Get a list of addresses by valid postcode
## (Comments)
Feature: Test Contact centre Address Endpoints
  I want to verify that all address endpoints in CC-SERVICE work correctly

  Scenario Outline: I want to verify that address search by postcode works
    Given I have a valid Postcode <postcode>
    When I Search Addresses By Postcode
    Then A list of addresses for my postcode is returned

    Examples:
    | postcode  |
    | "EX4 1EH" |
    | "EX41EH"  |
 #   | "EX4 1EH "|

  Scenario Outline: I want to verify that address search by invalid postcode works
    Given I have an invalid Postcode <postcode>
    When I Search Addresses By Invalid Postcode
    Then An empty list of addresses for my postcode is returned

    Examples:
      | postcode  |
      | "BD7 4PF" |
      | "XXX SSS" |

  Scenario Outline: I want to verify that address search by address works
    Given I have a valid address <address>
    When I Search Addresses By Address Search
    Then A list of addresses for my search is returned

    Examples:
      | address         |
      | "Chelmsford"    |
      | "Bristol Street"|
      | "Plymouth"      |

  Scenario Outline: I want to verify that invalid address search by address works
    Given I have an invalid address <address>
    When I Search invalid Addresses By Address Search
    Then An empty list of addresses for my search is returned

    Examples:
      | address                 |
      | "Chimpanzee"            |
      | "Boaty McBoat Face Ally"|
      | "Mike Tyson Avenue"     |

 #  Scenario Outline: I want to add a new address
 #    Given I have a new uprn and address <uprn> <address>
 #    When I post the new address
 #    Then The new address is posted successfully

 #    Examples:
 #      | uprn      | address                 |
 #      | "1234567" | "14 lawrence Drive,Horton Bank Top,,Bradford,West Yorkshire,BD7 4PF,CREATE,NEW_PROPERTY,Mr,Andrew,Johnys"            |
