# census-contact-centre-cucumber
Cucumber integration tests for Census Contact Centre Service

This project tests teh functionality of the RH Service
It currently tests the Address endpoints and validates postcodes returning addresses.
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

```