#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : Webchat
#Feature: Webchat
#Scenario: Start a Webchat Session
## (Comments)
Feature: Webchat
  I want to chat with a Contact Centre agent

  Scenario: Start a Webchat Session
    Given I want to chat with a contact centre agent
    And I am on the RH Start Page - method 3
    And I have selected the Webchat option
    And the current time is in opening hours
    Then I am presented with a form to enter required information
    Given I enter all the required information
    When I select the “Start Chat” button
    And the current time is in opening hours
    Then the information is submitted to Serco
    And my Webchat session starts
    
