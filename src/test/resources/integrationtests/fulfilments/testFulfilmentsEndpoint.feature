#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CC CONTACT CENTRE SERVICE
#Feature: Test Contact centre Fulfilments Endpoints
#Scenario: Get fulfilments for various cases
## (Comments)
@CC
Feature: Test Contact centre Fulfilments Endpoints
  I want to verify that all endpoints in CC-SERVICE fulfilments work correctly

  Scenario Outline: I want to verify that the get Fulfilments endpoint works
    When I Search fulfilments <caseType> <region> <individual>
    Then A list of fulfilments is returned of the correct products <caseType> <region> <individual>

    Examples:
      | caseType | region | individual |
      | "HH"     | "E"    | "true"     |
      | "HH"     | "N"    | "true"     |
      | "HH"     | "W"    | "true"     |
      | "CE"     | "E"    | "true"     |
      | "CE"     | "N"    | "true"     |
      | "CE"     | "W"    | "true"     |
      | "SPG"    | "E"    | "true"     |
      | "SPG"    | "N"    | "true"     |
      | "SPG"    | "W"    | "true"     |
      | "HH"     | "E"    | "false"    |
      | "HH"     | "N"    | "false"    |
      | "HH"     | "W"    | "false"    |
      | "CE"     | "E"    | "false"    |
      | "CE"     | "N"    | "false"    |
      | "CE"     | "W"    | "false"    |
      | "SPG"    | "E"    | "false"    |
      | "SPG"    | "N"    | "false"    |
      | "SPG"    | "W"    | "false"    |

	@FulfilmentsEndToEnd
  Scenario Outline: I want to verify that Fulfilments work end to end
    Given I have a valid address search String <address>
    When I Search Addresses By Address Search String
    Then A list of addresses for my search is returned containing the address I require
    Given I have a valid UPRN from my found address <uprn>
    And cached cases for the UPRN do not already exist
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>
    Given I have a valid case from my search UPRN
    When I Search fulfilments
    Then the correct fulfilments are returned for my case

    Examples:
      | address               | uprn           | case_ids                               |
      | "70, Magdalen Street" | "100040222798" | "3305e937-6fb1-4ce1-9d4c-077f147789de" |
      | "33 Serge Court"      | "100041131297" | "03f58cb5-9af4-4d40-9d60-c124c5bddfff" |

  @SetUp
  Scenario: [CR-T142] I want to request an UAC for a HH Respondent in NI via POST
    Given the CC advisor has provided a valid UPRN "1710030095"
    Then the Case endpoint returns a case, associated with UPRN "1710030095", which has caseType "HH"
    Given a list of available fulfilment product codes is presented for a HH caseType where individual flag = "false" and region = "N"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "UAC" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "HH" and individual = "false"

  @SetUp
  Scenario: [CR-T292] I want to request an UAC for a HI Respondent in Wales via Post
    Given the CC advisor has provided a valid UPRN "1347459992"
    When the Case endpoint returns a case, associated with UPRN "1347459992", which has caseType "HH"
    Given a list of available fulfilment product codes is presented for a HH caseType where individual flag = "true" and region = "W"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "UAC" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "HH" and individual = "true"

  @SetUp
  Scenario: [CR-T301] I want to request a Welsh Paper Questionnaire for a CE Individual Respondent in Wales
    Given the CC advisor has provided a valid UPRN "1347459993"
    Then the Case endpoint returns a case, associated with UPRN "1347459993", which has caseType "CE"
    Given a list of available fulfilment product codes is presented for a caseType = "CE" where individual flag = "true" and region = "W"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "QUESTIONNAIRE" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "CE" and individual = "true"

  @SetUp
  Scenario: [CR-T334] I want to request an UAC for a SPG Respondent in Wales via Post
    Given the CC advisor has provided a valid UPRN "1347459996"
    Then the Case endpoint returns a case, associated with UPRN "1347459996", which has caseType "SPG"
    Given a list of available fulfilment product codes is presented for a caseType = "SPG" where individual flag = "false" and region = "W"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "QUESTIONNAIRE" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "SPG" and individual = "false"

  @SetUp
  Scenario Outline: [CR-T269, CR-T273, CR-T293, CR-T306, CR-T319, CR-T322] I want to request a Paper Questionnaire for SMS delivery channel
    Given the CC advisor has provided a valid UPRN "<uprn>"
    Then the Case endpoint returns a case, associated with UPRN "<uprn>", which has caseType "<case_type>" and addressLevel "U" and handDelivery "false"
    Given a list of available fulfilment product codes is presented for a caseType = "<case_type>" where individual flag = "<individual>" and region = "<region>"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "UAC" deliveryChannel "<delivery_channel>"
    And Requests a fulfilment for the case and delivery channel "<delivery_channel>"
    Then a fulfilment request event is emitted to RM for addressType = "<delivery_channel>" and individual = "<individual>"

    Examples:
      | uprn         | case_type | region | delivery_channel | individual |
      |100140222798  | HH        | E      | SMS              | false      |
      |100240222798  | CE        | E      | SMS              | true       |
      |100340222798  | HH        | W      | SMS              | true       |
      |100340222798  | HH        | W      | SMS              | true       |

  @SetUp
  Scenario Outline: [CR-T375] I want to check that EMPTY title, forename or surname items are not fulfilled for an individual POSTAL request
    Given the CC advisor has provided a valid UPRN "<uprn>"
    Then the Case endpoint returns a case, associated with UPRN "<uprn>", which has caseType "<case_type>" and addressLevel "U" and handDelivery "false"
    Given a list of available fulfilment product codes is presented for a caseType = "<case_type>" where individual flag = "true" and region = "W"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "QUESTIONNAIRE" deliveryChannel "POST"
    And Requests a fulfilment for the case and title "<title>" forename "<forename>" surname "<surname>"
    Then an exception is thrown stating "The fulfilment is for an individual so none of the following fields can be empty: 'title', 'forename' and 'surname'"

    Examples:
      | uprn         | case_type | title | forename | surname |
      | 1347459993   | CE        |       |          |         |
      | 1347459993   | CE        |   Mr  |          |         |
      | 1347459993   | CE        |       |   A      |         |
      | 1347459993   | CE        |       |          |   J     |
      | 1347459993   | CE        |   Mr  |   A      |         |
      | 1347459993   | CE        |   Mr  |          |   J     |
      | 1347459993   | CE        |       |   A      |   J     |

#### the following scenarios are still PENDING due to lack of products covering these options #####

  @SetUp
  Scenario Outline: [CR-T269, CR-T273, CR-T293, CR-T306, CR-T319, CR-T322] PENDING - I want to request a Paper Questionnaire for SMS delivery channel
    Given the CC advisor has provided a valid UPRN "<uprn>"
    Then the Case endpoint returns a case, associated with UPRN "<uprn>", which has caseType "<case_type>" and addressLevel "U" and handDelivery "false"
    Given a list of available fulfilment product codes is presented for a caseType = "<case_type>" where individual flag = "<individual>" and region = "<region>"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "UAC" deliveryChannel "<delivery_channel>"
    And Requests a fulfilment for the case and delivery channel "<delivery_channel>"
    Then a fulfilment request event is emitted to RM for addressType = "<delivery_channel>" and individual = "<individual>"

    Examples:
      | uprn         | case_type | region | delivery_channel | individual |
      |100440222798  | CE        | W      | SMS              | false      |
      |100540222798  | SPG       | N      | SMS              | false      |
      |100640222798  | SPG       | N      | SMS              | true       |
      |100440222798  | CE        | W      | SMS              | false      |

  @SetUp
  Scenario: [CR-T302] PENDING - I want to request an UAC for a CE Individual Respondent in Wales via Post
    Given the CC advisor has provided a valid UPRN "1347459993"
    Then the Case endpoint returns a case, associated with UPRN "1347459993", which has caseType "CE"
    Given a list of available fulfilment product codes is presented for a caseType = "CE" where individual flag = "true" and region = "W"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "UAC" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "CE" and individual = "true" 

  @SetUp
  Scenario: [CR-T304] PENDING - I want to request a Welsh Paper Questionnaire for a CE Manager in Wales
    Given the CC advisor has provided a valid UPRN "1347459993"
    Then the Case endpoint returns a case, associated with UPRN "1347459993", which has caseType "CE"
    Given a list of available fulfilment product codes is presented for a caseType = "CE" where individual flag = "false" and region = "W"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "QUESTIONNAIRE" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "CE" and individual = "false"

  @SetUp
  Scenario: [CR-T313] PENDING - I want to request an UAC for a CE Individual Respondent in NI via Post
    Given the CC advisor has provided a valid UPRN "1347459997"
    Then the Case endpoint returns a case, associated with UPRN "1347459997", which has caseType "CE"
    Given a list of available fulfilment product codes is presented for a caseType = "CE" where individual flag = "true" and region = "N"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "UAC" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "CE" and individual = "true"

  @SetUp
  Scenario: [CR-T316] PENDING - I want to request an UAC for a CE Manager in NI via Post
    Given the CC advisor has provided a valid UPRN "1347459993"
    Then the Case endpoint returns a case, associated with UPRN "1347459993", which has caseType "CE"
    Given a list of available fulfilment product codes is presented for a caseType = "CE" where individual flag = "false" and region = "N"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "UAC" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "CE" and individual = "false"

  @SetUp
  Scenario: [CR-T323] PENDING - I want to request a Paper Questionnaire for a SPG Individual Respondent in NI
    Given the CC advisor has provided a valid UPRN "1710030110"
    Then the Case endpoint returns a case, associated with UPRN "1710030110", which has caseType "SPG" and addressLevel "U" and handDelivery "false"
    Given a list of available fulfilment product codes is presented for a caseType = "SPG" where individual flag = "true" and region = "N"
    And an empty queue exists for sending Fulfilment Requested events
    When CC Advisor selects the product code for productGroup "QUESTIONNAIRE" deliveryChannel "POST"
    And Requests a fulfilment for the case and delivery channel "POST"
    Then a fulfilment request event is emitted to RM for addressType = "SPG" and individual = "true"

