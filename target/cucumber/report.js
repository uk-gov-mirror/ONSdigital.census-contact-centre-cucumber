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
  "name": "an error message “Please enter your access code.” appears on the same page",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTheCensus.an_error_message_Please_enter_your_access_code_appears_on_the_same_page()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "underneath the message is a list of telephone numbers to call for advise",
  "keyword": "And "
});
formatter.match({
  "location": "AccessTheCensus.underneath_the_message_is_a_list_of_telephone_numbers_to_call_for_advise()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
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
  "name": "I enter invalid UAC \"aaaa\" \"aaaa\" \"aaaa\" \"aaaa\" into the text box",
  "keyword": "And "
});
formatter.match({
  "location": "AccessTheCensus.i_enter_invalid_UAC_into_the_text_box(String,String,String,String)"
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
  "name": "an error message “Please re-enter your access code and try again.” appears on the same page",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTheCensus.an_error_message_Please_re_enter_your_access_code_and_try_again_appears_on_the_same_page()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "underneath the message is a list of telephone numbers to call for advise",
  "keyword": "And "
});
formatter.match({
  "location": "AccessTheCensus.underneath_the_message_is_a_list_of_telephone_numbers_to_call_for_advise()"
});
formatter.result({
  "status": "passed"
});
formatter.after({
  "status": "passed"
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
formatter.match({
  "location": "RequestAHouseholdUAC.i_am_on_the_RH_Start_Page_method(Integer)"
});
formatter.result({
  "error_message": "cucumber.api.PendingException: TODO: implement me\n\tat uk.gov.ons.ctp.integration.rhcucumber.cucSteps.RequestAHouseholdUAC.i_am_on_the_RH_Start_Page_method(RequestAHouseholdUAC.java:23)\n\tat ✽.I am on the RH Start Page  - method 2(file:src/test/resources/integrationtests/requestUACForHousehold.feature:10)\n",
  "status": "pending"
});
formatter.step({
  "name": "I select the “Request UAC” option",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_select_the_Request_UAC_option()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I am presented with a form for entering my postcode",
  "keyword": "Then "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_am_presented_with_a_form_for_entering_my_postcode()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I enter a valid UK postcode",
  "keyword": "Given "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_enter_a_valid_UK_postcode()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I select the “Find Address” option",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_select_the_Find_Address_option()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I am presented with the AI list of available addresses for the postcode",
  "keyword": "Then "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_am_presented_with_the_AI_list_of_available_addresses_for_the_postcode()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I select my address from the AI list",
  "keyword": "Given "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_select_my_address_from_the_AI_list()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I select “Continue” - method 1",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_select_Continue_method(Integer)"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I am presented with a page to confirm my address - method 2",
  "keyword": "Then "
});
formatter.match({
  "location": "AccessTheCensus.i_am_presented_with_a_page_to_confirm_my_address_method(Integer)"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "the address confirmation page contains two options - method 2",
  "keyword": "And "
});
formatter.match({
  "location": "AccessTheCensus.the_address_confirmation_page_contains_two_options_method(Integer)"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I select the \"Yes, this address is correct\" option - method 2",
  "keyword": "Given "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_select_the_option_method(String,Integer)"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "the address is one of the addresses that’s in the rehearsal",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.the_address_is_one_of_the_addresses_that_s_in_the_rehearsal()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "there is exactly 1 HH case that’s associated with the UPRN",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.there_is_exactly_HH_case_that_s_associated_with_the_UPRN(Integer)"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I am presented with a form for entering a mobile no.",
  "keyword": "Then "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_am_presented_with_a_form_for_entering_a_mobile_no()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I enter my UK mobile no.",
  "keyword": "Given "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_enter_my_UK_mobile_no()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I select “Continue” - method 2",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_select_Continue_method(Integer)"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I am presented with a page to confirm my mobile no.",
  "keyword": "Then "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_am_presented_with_a_page_to_confirm_my_mobile_no()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "the mobile no. confirmation page contains two options",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.the_mobile_no_confirmation_page_contains_two_options()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I select the \"Yes, send the text now\" option",
  "keyword": "Given "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_select_the_option(String)"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "the region associated with the HH case is NOT Wales",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.the_region_associated_with_the_HH_case_is_NOT_Wales()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "my UAC request is submitted",
  "keyword": "Then "
});
formatter.match({
  "location": "RequestAHouseholdUAC.my_UAC_request_is_submitted()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I am presented with a page containing a confirmation message",
  "keyword": "And "
});
formatter.match({
  "location": "RequestAHouseholdUAC.i_am_presented_with_a_page_containing_a_confirmation_message()"
});
formatter.result({
  "status": "skipped"
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
formatter.match({
  "location": "Webchat.i_want_to_chat_with_a_contact_centre_agent()"
});
formatter.result({
  "status": "passed"
});
formatter.step({
  "name": "I am on the RH Start Page - method 3",
  "keyword": "And "
});
formatter.match({
  "location": "Webchat.i_am_on_the_RH_Start_Page_method(Integer)"
});
formatter.result({
  "error_message": "cucumber.api.PendingException: TODO: implement me\n\tat uk.gov.ons.ctp.integration.rhcucumber.cucSteps.Webchat.i_am_on_the_RH_Start_Page_method(Webchat.java:23)\n\tat ✽.I am on the RH Start Page - method 3(file:src/test/resources/integrationtests/webchat.feature:11)\n",
  "status": "pending"
});
formatter.step({
  "name": "I have selected the Webchat option",
  "keyword": "And "
});
formatter.match({
  "location": "Webchat.i_have_selected_the_Webchat_option()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "the current time is in opening hours",
  "keyword": "And "
});
formatter.match({
  "location": "Webchat.the_current_time_is_in_opening_hours()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I am presented with a form to enter required information",
  "keyword": "Then "
});
formatter.match({
  "location": "Webchat.i_am_presented_with_a_form_to_enter_required_information()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I enter all the required information",
  "keyword": "Given "
});
formatter.match({
  "location": "Webchat.i_enter_all_the_required_information()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "I select the “Start Chat” button",
  "keyword": "When "
});
formatter.match({
  "location": "Webchat.i_select_the_Start_Chat_button()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "the current time is in opening hours",
  "keyword": "And "
});
formatter.match({
  "location": "Webchat.the_current_time_is_in_opening_hours()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "the information is submitted to Serco",
  "keyword": "Then "
});
formatter.match({
  "location": "Webchat.the_information_is_submitted_to_Serco()"
});
formatter.result({
  "status": "skipped"
});
formatter.step({
  "name": "my Webchat session starts",
  "keyword": "And "
});
formatter.match({
  "location": "Webchat.my_Webchat_session_starts()"
});
formatter.result({
  "status": "skipped"
});
});