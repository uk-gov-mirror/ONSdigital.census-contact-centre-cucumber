#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Smoke Tests for Contact Centre and Mock Case Api Services
#Scenario: I want to check that contact centre service is running
## (Comments)
Feature: Smoke Tests for Contact Centre and Mock Case Api Services
  I want to verify that the contact centre and mock case api services are running
  
  @smoke
  Scenario: I want to check that contact centre service is running
  	Given I access the Fulfilments endpoint
  	Then I receive a response from the contact centre service with a status of 200
  	
  #Scenario: I want to check that mock case api service is running
  #	Given I access the mock case api service info endpoint
  #	Then I receive a response from the mock case api service with a status of 200
