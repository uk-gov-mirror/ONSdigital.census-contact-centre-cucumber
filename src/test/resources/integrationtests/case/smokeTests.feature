#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : CONTACT CENTRE, ASSISTED DIGITAL SERVICE
#Feature: Smoke Tests for Contact Centre, Assisted Digital and Mock Case API Services
#Scenario: I want to check that I can connect to the contact centre, assisted digital service
#Scenario: I want to check that I can connect to the mock case api service
## (Comments)
@CC @AD @smoke
Feature: Smoke Tests for Contact Centre, Assisted Digital and Mock Case Api Services
  I want to verify that the contact centre, assisted digital and mock case api services are running

  Scenario: I want to check that I can connect to the service
    Given I am about to do a smoke test by going to an endpoint
    Then I do the smoke test and receive a response of OK from the service

  Scenario: I want to check that I can connect to the mock case api service
    Given I am about to do a smoke test by going to a mock case api endpoint
    Then I do the smoke test and receive a response of OK from the mock case api service
   
