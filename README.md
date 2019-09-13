# census-contact-centre-cucumber
Cucumber integration tests for Census Contact Centre Service

This branch (CR-221) has been about proving that rest endpoints can be called from within cucumber tests. I have found that this can be done using the RestAssured framework. The code changes will not be merged but instead we will keep the CR-221 branch in the census-contact-centre-cucumber repo until the code changes are required at some future date. NB. The branch contains the following scenario, which runs successfully but requires RabbitMQ (from the census-rh-service docker image) and the census-int-event-generator (for the endpoint to post to) to both be running too:

```
@SetUpTestEndpoints @TearDownTestEndpoints
Scenario: Test the UAC Generator
Given I post a request to the endpoint for the UAC Generator
And I receive a Rest response that is not null   
Then the response should contain caseRefs "hello" and "bar"
```