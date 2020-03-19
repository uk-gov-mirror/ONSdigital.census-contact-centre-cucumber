#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Fulfilments Endpoints
#Scenario: Get fulfilments for various cases
## (Comments)
Feature: Test Contact centre Fulfilments Endpoints
  I want to verify that all endpoints in CC-SERVICE fulfilments work correctly

  Scenario Outline: I want to verify that the get Fulfilments endpoint works
    When I Search fulfilments <caseType> <region> <individual>
    Then A list of fulfilments is returned of the correct products <caseType> <region> <individual>

    Examples: 
      | caseType | region | individual |
      | "HH"     | "E"    | "true"     |
      | "HH"     | "N"    | "true"     |
      | "HH"     | "W"    | "true"     |
      | "CE"     | "E"    | "true"     |
      | "CE"     | "N"    | "true"     |
      | "CE"     | "W"    | "true"     |
      | "SPG"    | "E"    | "true"     |
      | "SPG"    | "N"    | "true"     |
      | "SPG"    | "W"    | "true"     |
      | "HH"     | "E"    | "false"    |
      | "HH"     | "N"    | "false"    |
      | "HH"     | "W"    | "false"    |
      | "CE"     | "E"    | "false"    |
      | "CE"     | "N"    | "false"    |
      | "CE"     | "W"    | "false"    |
      | "SPG"    | "E"    | "false"    |
      | "SPG"    | "N"    | "false"    |
      | "SPG"    | "W"    | "false"    |

  Scenario Outline: I want to verify that Fulfilments work end to end
    Given I have a valid address search String <address>
    When I Search Addresses By Address Search String
    Then A list of addresses for my search is returned containing the address I require
    Given I have a valid UPRN from my found address <uprn>
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>
    Given I have a valid case from my search UPRN
    When I Search fulfilments
    Then the correct fulfilments are returned for my case

    Examples: 
      | address               | uprn           | case_ids                               |
      | "70, Magdalen Street" | "100040222798" | "3305e937-6fb1-4ce1-9d4c-077f147789de" |
      | "33 Serge Court"      | "100041131297" | "03f58cb5-9af4-4d40-9d60-c124c5bddfff" |

  @SetUp
  Scenario: [CR-T142] I want to request an UAC for a HH Respondent in NI via POST
    Given the CC advisor has provided a valid UPRN with caseType HH
    Then the Case endpoint returns a case associated with the UPRN
    Given a list of available fulfilment product codes is presented for a HH caseType where individual flag = "false" and region = "N"
    When CC Advisor select the product code for HH UAC via Post
    Then an event is emitted to RM with a fulfilment request for a HH UAC where delivery channel = Post
