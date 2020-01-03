#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Smoke Tests for Contact Centre and Mock Case Api Services
#Scenario: I want to check that I can connect to the contact centre service
#Scenario: I want to check that I can connect to the mock case api service
## (Comments)
Feature: Smoke Tests for Contact Centre and Mock Case Api Services
  I want to verify that the contact centre and mock case api services are running

  @smoke
  Scenario: I want to check that I can connect to the contact centre service
    Given I am about to do a smoke test by going to a contact centre endpoint
    Then I do the smoke test and receive a response of OK from the contact centre service

  @smoke
  Scenario: I want to check that I can connect to the mock case api service
    Given I am about to do a smoke test by going to a mock case api endpoint
    Then I do the smoke test and receive a response of OK from the mock case api service
   
