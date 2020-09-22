#Author: andrew.johnys@ext.ons.gov.uk
#Keywords Summary : CONTACT CENTRE, ASSISTED DIGITAL SERVICE
#Feature: Test Contact Centre, Assisted Digital case endpoints
## (Comments)
Feature: Test Contact Centre, Assisted Digital case endpoints
  I want to verify that all endpoints in CC/AD service work correctly

  @CC @CR-T381
  Scenario Outline: CR-T381 Get latest case from Cache (GetCaseByUPRN)
    Given the case does not exist in the cache "<uprn>"
    And an empty queue exists for sending "ADDRESS_MODIFIED" events
    And the case exists in RM "<case_id>"
    When the case address details are modified by a member of CC staff
    And the case modified even is sent to RM but RM does not immediately action it
    When the call is made to fetch the case again by UPRN "<uprn>"
    Then the modified case is returned from the cache

    Examples:
      | uprn         | case_id                                  |
      | 1347459999   | 3305e937-6fb1-4ce1-9d4c-077f147789ac     |

  @CC @CR-T385
  Scenario Outline: CR-T385 Get latest case from Cache (GetCaseByID)
    Given the case does not exist in the cache "<uprn>"
    And an empty queue exists for sending "ADDRESS_MODIFIED" events
    And the case exists in RM "<case_id>"
    When the case address details are modified by a member of CC staff
    And the case modified even is sent to RM but RM does not immediately action it
    When the call is made to fetch the case again by case ID "<case_id>"
    Then the modified case is returned from the cache

    Examples:
      | uprn         | case_id                                  |
      | 1347459999   | 3305e937-6fb1-4ce1-9d4c-077f147789ac     |

