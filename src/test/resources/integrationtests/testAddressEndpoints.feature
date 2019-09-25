#Author: andreiw.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Address Endpoints
#Scenario: Get a list of addresses by valid postcode
## (Comments)
Feature: Test Contact centre Address Endpoints
  I want to verify that all address endpoints in CC-SERVICE work correctly

  Scenario: I want to verify that address search by postcode works
    Given I have a valid Postcode
    When I Search Addresses By Postcode
    Then A list of addresses for my postcode is returned

