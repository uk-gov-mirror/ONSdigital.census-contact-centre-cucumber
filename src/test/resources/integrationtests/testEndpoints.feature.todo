#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : CC
#Feature: Test Endpoints
#Scenario: Test the Version Endpoint
## (Comments)
Feature: Test Endpoints
  I want to call endpoints from my cucumber scenarios
    
  @SetUpTestEndpoints
  @TearDownTestEndpoints
  Scenario: Test the UAC Generator
    Given I post a request to the endpoint for the UAC Generator
   	And I receive a Rest response that is not null
    Then the response should contain caseRefs "hello" and "bar"
    
    
  #Scenario: Test the Version Endpoint
    #Given I go to the version endpoint for the Contact Centre
   #	And I receive a Rest response
    #Then the response should contain the following api version number "4.0.2"
    #And the response should contain the following data version number "60"