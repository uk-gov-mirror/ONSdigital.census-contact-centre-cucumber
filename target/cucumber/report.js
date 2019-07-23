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
  "name": "Test Endpoints",
  "description": "  I want to call endpoints from my cucumber scenarios",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "Test the UAC Generator",
  "description": "",
  "keyword": "Scenario",
  "tags": [
    {
      "name": "@SetUpTestEndpoints"
    },
    {
      "name": "@TearDownTestEndpoints"
    }
  ]
});
formatter.before({
  "status": "passed"
});
formatter.step({
  "name": "I post a request to the endpoint for the UAC Generator",
  "keyword": "Given "
});
formatter.match({
  "location": "TestEndpoints.i_post_a_request_to_the_endpoint_for_the_UAC_Generator()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I receive a Rest response that is not null",
  "keyword": "And "
});
formatter.match({
  "location": "TestEndpoints.i_receive_a_Rest_response_that_is_not_null()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "the response should contain caseRefs \"hello\" and \"bar\"",
  "keyword": "Then "
});
formatter.match({
  "location": "TestEndpoints.the_response_should_contain_caseRefs_and(String,String)"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
});
});