#Author: eleanor.cook@ons.gov.uk
#Keywords Summary : RH EQ
#Feature: Access the Census Questionnaire
#Scenario: Access the Census Questionnaire - the Happiest Path!
## (Comments)
Feature: Access the Census Questionnaire
  I want to complete my Census Questionnaire
    
  @SetUpAccessTheCensus
  @TearDownAccessTheCensus
  Scenario: Unhappy path - No UAC entered
    Given I am on the RH Start Page
    And I click the “Start now” button
    Then an error message “Enter your access code.” appears on the same page

  #@SetUpAccessTheCensus
  #@TearDownAccessTheCensus
  #Scenario: Unhappy path – Invalid UAC entered
    #Given I am on the RH Start Page
    #And I enter invalid UAC "aaaaaaaaaaaaaaaa" into the text box
    #And I click the “Start now” button
    #Then an error message “Please re-enter your access code and try again.” appears on the same page
    #And underneath the message is a list of telephone numbers to call for advise
