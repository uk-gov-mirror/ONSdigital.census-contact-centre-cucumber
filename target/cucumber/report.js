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
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "I am on the RH Start Page",
  "keyword": "Given "
});
formatter.match({
  "location": "AccessTheCensus.i_am_on_the_RH_Start_Page()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I click the “Start now” button",
  "keyword": "And "
});
formatter.match({
  "location": "AccessTheCensus.i_click_the_Start_now_button()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "an error message “Enter your access code.” appears on the same page",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTheCensus.an_error_message_Enter_your_access_code_appears_on_the_same_page()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
formatter.uri("file:src/test/resources/integrationtests/testEndpoints.feature");
formatter.feature({
  "name": "Test the Version Endpoint",
  "description": "  I want to find out what version of the Contact Centre I\u0027m using",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Test the Version Endpoint",
  "description": "",
  "keyword": "Scenario"
});
formatter.step({
  "name": "I go to the version endpoint for the Contact Centre",
  "keyword": "Given "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "I receive a Rest response",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the response should contain the following api version number \"4.0.2\"",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the response should contain the following data version number \"60\"",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
});