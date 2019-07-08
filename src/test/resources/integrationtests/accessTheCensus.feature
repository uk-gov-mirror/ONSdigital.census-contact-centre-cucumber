#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : RH EQ
#Feature: Access the Census Questionnaire
#Scenario: Access the Census Questionnaire - the Happiest Path!
## (Comments)
Feature: Access the Census Questionnaire
  I want to complete my Census Questionnaire

  #@SetUpAccessTheCensus
  #@TearDownAccessTheCensus
  #Scenario: The Happiest Path - Access the Census Questionnaire
    #Given I am on the RH Start Page
    #And I enter UAC "hdv3" "frz4" "hj3k" "lmnz" into the text box
    #And I click the “Start now” button
    #Then I am presented with a page to confirm my address - method 1
    #And the address confirmation page contains two options - method 1
    #Given I select the “Yes, this address is correct” option - method 1
    #And I click the “Save and continue” button
    #Then I am presented with the Census Questionnaire
    
  @SetUpAccessTheCensus
  @TearDownAccessTheCensus
  Scenario: Unhappy path - No UAC entered
    Given I am on the RH Start Page
    And I click the “Start now” button
    Then an error message “Please enter your access code.” appears on the same page
    And underneath the message is a list of telephone numbers to call for advise

  @SetUpAccessTheCensus
  @TearDownAccessTheCensus
  Scenario: Unhappy path – Invalid UAC entered
    Given I am on the RH Start Page
    And I enter invalid UAC "aaaa" "aaaa" "aaaa" "aaaa" into the text box
    And I click the “Start now” button
    Then an error message “Please re-enter your access code and try again.” appears on the same page
    And underneath the message is a list of telephone numbers to call for advise
