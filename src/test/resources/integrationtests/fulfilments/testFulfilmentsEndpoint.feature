#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Fulfilments Endpoints
#Scenario: Get fulfilments for various cases
## (Comments)
Feature: Test Contact centre Fulfilments Endpoints
  I want to verify that all endpoints in CC-SERVICE fulfilments work correctly

  Scenario Outline: I want to verify that the get Fulfilments endpoint works
    Given I have a valid case Type <caseType> and region <region>
    When I Search fulfilments
    Then A list of fulfilments is returned of size <size> <caseType> <region>

    Examples:
      | caseType  | region        | size |
      | "HH"      | "E"           | 7    |
      | "HH"      | "N"           | 5    |
      | "HH"      | "W"           | 10   |
      | "HI"      | "E"           | 2    |
      | "HI"      | "N"           | 2    |
      | "HI"      | "W"           | 4    |
      | "CE"      | "E"           | 0    |
      | "CE"      | "N"           | 0    |
      | "CE"      | "W"           | 0    |
      | "CI"      | "E"           | 0    |
      | "CI"      | "N"           | 0    |
      | "CI"      | "W"           | 0    |

  Scenario Outline: I want to verify that Fulfilments work end to end
    Given I have a valid address search String <address>
    When I Search Addresses By Address Search String
    Then A list of addresses for my search is returned containing the address I require <list_size>
    Given I have a valid UPRN from my found address <uprn>
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>
    Given I have a valid case from my search UPRN
    When I Search fulfilments
    Then the correct fulfilments are returned for my case <case_type> <region> <languages>


    Examples:
      | address                                   | list_size       | uprn           | case_ids                                 | case_type | region  | languages |
      | "8A, Okehampton Road"                     | 1               |"100041141569"  | "3305e937-6fb1-4ce1-9d4c-077f147789bb"   | "HH"      | "E"     |  "eng,ben,som,tur,vie"      |
      | "Magdalen House, Magdalen Street"         | 1               |"10013041069"   | ""                                       | "HH"      | "E"     |  ""      |
      | "70, Magdalen Street"                     | 1               |"100040222798"  | "3305e937-6fb1-4ce1-9d4c-077f147789de"   | "HH"      | "E"     |  "eng,ben,som,tur,vie"      |
      | "33 Serge Court"                          | 1               |"100041131297"  | "03f58cb5-9af4-4d40-9d60-c124c5bddfff"   | "HH"      | "W"     |  "ben,eng,som,wel,tur,vie"     |
