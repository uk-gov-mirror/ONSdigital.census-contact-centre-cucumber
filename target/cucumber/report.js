$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("file:src/test/resources/integrationtests/accessTheCensus.feature");
formatter.feature({
  "name": "Access the Census Questionnaire",
  "description": "  I want to complete my Census Questionnaire",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Unhappy path - No UAC entered",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@SetUpAccessTheCensus"
    },
    {
      "name": "@TearDownAccessTheCensus"
    }
  ]
});
formatter.step({
  "name": "I am on the RH Start Page",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I click the “Start now” button",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "an error message “Please enter your access code.” appears on the same page",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "underneath the message is a list of telephone numbers to call for advise",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.scenario({
  "name": "Unhappy path – Invalid UAC entered",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@SetUpAccessTheCensus"
    },
    {
      "name": "@TearDownAccessTheCensus"
    }
  ]
});
formatter.step({
  "name": "I am on the RH Start Page",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I enter invalid UAC \"aaaa\" \"aaaa\" \"aaaa\" \"aaaa\" into the text box",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I click the “Start now” button",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "an error message “Please re-enter your access code and try again.” appears on the same page",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "underneath the message is a list of telephone numbers to call for advise",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.uri("file:src/test/resources/integrationtests/requestUACForHousehold.feature");
formatter.feature({
  "name": "Request a Household UAC",
  "description": "  I want to get a new UAC for completing my Household Census Questionnaire",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Submit UAC Request to RM",
  "description": "",
  "keyword": "Scenario"
});
formatter.step({
  "name": "I am on the RH Start Page  - method 2",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select the “Request UAC” option",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am presented with a form for entering my postcode",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I enter a valid UK postcode",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select the “Find Address” option",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am presented with the AI list of available addresses for the postcode",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select my address from the AI list",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select “Continue” - method 1",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am presented with a page to confirm my address - method 2",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the address confirmation page contains two options - method 2",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select the \"Yes, this address is correct\" option - method 2",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the address is one of the addresses that’s in the rehearsal",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "there is exactly 1 HH case that’s associated with the UPRN",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am presented with a form for entering a mobile no.",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I enter my UK mobile no.",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select “Continue” - method 2",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am presented with a page to confirm my mobile no.",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the mobile no. confirmation page contains two options",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select the \"Yes, send the text now\" option",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the region associated with the HH case is NOT Wales",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "my UAC request is submitted",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am presented with a page containing a confirmation message",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.uri("file:src/test/resources/integrationtests/webchat.feature");
formatter.feature({
  "name": "Webchat",
  "description": "  I want to chat with a Contact Centre agent",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Start a Webchat Session",
  "description": "",
  "keyword": "Scenario"
});
formatter.step({
  "name": "I want to chat with a contact centre agent",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am on the RH Start Page - method 3",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I have selected the Webchat option",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the current time is in opening hours",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I am presented with a form to enter required information",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I enter all the required information",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I select the “Start Chat” button",
  "keyword": "When "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the current time is in opening hours",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the information is submitted to Serco",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "my Webchat session starts",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
});