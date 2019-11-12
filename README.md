# census-contact-centre-cucumber
Cucumber integration tests for Census Contact Centre Service

This project tests the functionality of the Contact Centre Service
It currently tests the Address and case endpoints.
It uses Spring Boot to create a restTemplate - mapping Json Objects to POJOs
It also uses Scenario Outlines to utilize tabulated data in tests
```
  Scenario Outline: I want to verify that address search by postcode works
    Given I have a valid Postcode <postcode>
    When I Search Addresses By Postcode
    Then A list of addresses for my postcode is returned

  Scenario Outline: I want to verify that address search by invalid postcode works
    Given I have an invalid Postcode <postcode>
    When I Search Addresses By Invalid Postcode
    Then An empty list of addresses for my postcode is returned

  Scenario Outline: I want to verify that address search by address works
    Given I have a valid address <address>
    When I Search Addresses By Address Search
    Then A list of addresses for my search is returned

  Scenario Outline: I want to verify that invalid address search by address works
    Given I have an invalid address <address>
    When I Search invalid Addresses By Address Search
    Then An empty list of addresses for my search is returned

  Scenario Outline: I want to verify that the case search by case ID works
    Given I have a valid case ID <caseId>
    When I Search cases By case ID <caseEvents>
    Then the correct case for my case ID is returned <uprn>
    And the correct number of events are returned <caseEvents> <noCaseEvents>

  Scenario Outline: I want to verify that the case search by invalid case ID works
    Given I have an invalid case ID <caseId>
    When I Search for cases By case ID
    Then An error is thrown and no case is returned <httpError>

  Scenario Outline: I want to verify that the case search by case UPRN works
    Given I have a valid UPRN <uprn>
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>

  Scenario Outline: I want to verify that the case search by invalid case UPRN works
    Given I have an invalid UPRN <uprn>
    When I Search cases By invalid UPRN
    Then no cases for my UPRN are returned <httpError>

  Scenario Outline: I want to verify that the get Fulfilments endpoint works
    Given I have a valid case Type <caseType> and region <region>
    When I Search fulfilments
    Then A list of fulfilments is returned of size <size> <caseType> <region>

  Scenario Outline: I want to verify that Fulfilments work end to end
    Given I have a valid address search String <address>
    When I Search Addresses By Address Search String
    Then A list of addresses for my search is returned containing the address I require
    Given I have a valid UPRN from my found address <uprn>
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>
    Given I have a valid case from my search UPRN
    When I Search fulfilments
    Then the correct fulfilments are returned for my case <fulfilments>
```

To run these tests - the following services need to be running:-

contact-centre-service
census-mock-case-api-service