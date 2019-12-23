#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Smoke Tests for Contact Centre and Mock Case Api Services
#Scenario: I want to check that contact centre service is running
## (Comments)
Feature: Smoke Tests for Contact Centre and Mock Case Api Services
  I want to verify that the contact centre and mock case api services are running
  
  Scenario: I want to check that contact centre service is running
  	Given I access the Fulfilments endpoint
  	Then I receive a response with a status of 200
