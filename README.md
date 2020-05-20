# census-contact-centre-cucumber
Cucumber integration tests for Census Contact Centre Service


This project tests the functionality of the Contact Centre Service
It currently tests the Address and case endpoints.
It uses Spring Boot to create a restTemplate - mapping Json Objects to POJOs
It also uses Scenario Outlines to utilize tabulated data in tests
```
  Scenario Outline: I want to verify that address search by postcode works
    Given I have a valid Postcode <postcode>
    When I Search Addresses By Postcode
    Then A list of addresses for my postcode is returned

  Scenario Outline: I want to verify that address search by invalid postcode works
    Given I have an invalid Postcode <postcode>
    When I Search Addresses By Invalid Postcode
    Then An empty list of addresses for my postcode is returned

  Scenario Outline: I want to verify that address search by address works
    Given I have a valid address <address>
    When I Search Addresses By Address Search
    Then A list of addresses for my search is returned

  Scenario Outline: I want to verify that invalid address search by address works
    Given I have an invalid address <address>
    When I Search invalid Addresses By Address Search
    Then An empty list of addresses for my search is returned

  Scenario Outline: I want to verify that the case search by case ID works
    Given I have a valid case ID <caseId>
    When I Search cases By case ID <caseEvents>
    Then the correct case for my case ID is returned <uprn>
    And the correct number of events are returned <caseEvents> <noCaseEvents>

  Scenario Outline: I want to verify that the case search by invalid case ID works
    Given I have an invalid case ID <caseId>
    When I Search for cases By case ID
    Then An error is thrown and no case is returned <httpError>

  Scenario Outline: I want to verify that the case search by case UPRN works
    Given I have a valid UPRN <uprn>
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>

  Scenario Outline: I want to verify that the case search by invalid case UPRN works
    Given I have an invalid UPRN <uprn>
    When I Search cases By invalid UPRN
    Then no cases for my UPRN are returned <httpError>

  Scenario Outline: I want to verify that the get Fulfilments endpoint works
    Given I have a valid case Type <caseType> and region <region>
    When I Search fulfilments
    Then A list of fulfilments is returned of size <size> <caseType> <region>

  Scenario Outline: I want to verify that Fulfilments work end to end
    Given I have a valid address search String <address>
    When I Search Addresses By Address Search String
    Then A list of addresses for my search is returned containing the address I require
    Given I have a valid UPRN from my found address <uprn>
    When I Search cases By UPRN
    Then the correct cases for my UPRN are returned <case_ids>
    Given I have a valid case from my search UPRN
    When I Search fulfilments
    Then the correct fulfilments are returned for my case <fulfilments>
```
A new travis.yml contains a script which runs the maven build, populates the local maven repo and copies the maven 
settings.xml file. Tests are skipped as this would run the cucumber.

```
script:
  - travis_wait mvn install -DskipTests -Dmaven.repo.local=local-maven-repo/repository
  - cp $HOME/.m2/settings.xml m2
```

A smokeTests.feature has now been added. The smoke tests will run before the other tests during a normal run. However, the smoke tests are tagged with @smoke so that, if preferred, they can be run separately from the other tests as follows:

```
mvn test -Dcucumber.options="--tags @smoke"
```
The advantage of running the smoke tests separately is that, if they find an error (such as that one of the services is not running), then the tests will fail faster than if the whole set of cucumber tests is run (otherwise the other tests would continue to run).

To run all the CCCUC tests locally do as follows:

Make sure that your local IP address is whitelisted for access to AI e.g. try accessing this link: https://rh-dev-ai-api.ai.census-gcp.onsdigital.uk/addresses/rh/uprn/10034869241?addresstype=paf&verbose=true

Run rabbitmq locally e.g. go into the census-rh-service repo, in the terminal, and enter this command: 

```
docker-compose up -d
```
Run the following command to make sure that you have permission to access Firestore (no error occurs otherwise - it just freezes and fails): 

```
gcloud auth application-default login
```
Run the mock case service locally (either using eclipse or mvn spring-boot:run)

Make sure that you have set the following environment variables NB. the GOOGLE_CLOUD_PROJECT variable needs to be set in order for the Firestore collection to be given the correct name when the CCSVC runs:

```
export ADDRESS_INDEX_SETTINGS_REST_CLIENT_CONFIG_SCHEME=https
export ADDRESS_INDEX_SETTINGS_REST_CLIENT_CONFIG_HOST=rh-dev-ai-api.ai.census-gcp.onsdigital.uk
export ADDRESS_INDEX_SETTINGS_REST_CLIENT_CONFIG_PORT=443
export ADDRESS_INDEX_SETTINGS_REST_CLIENT_CONFIG_USERNAME=rhuser
export ADDRESS_INDEX_SETTINGS_REST_CLIENT_CONFIG_PASSWORD=<password>
export GOOGLE_CLOUD_PROJECT=<name of your project>
```
Obviously replace <password> with the rhuser password. The CCCUC tests should then pass when run locally against the CCSVC.
And replace <name of your project> with something like census-rh-ellacook1

Also, if there's a problem with using the AI in dev then an alternative is to point it at Whitelodge:

```
export ADDRESS_INDEX_SETTINGS_REST_CLIENT_CONFIG_HOST=whitelodge-ai-api.ai.census-gcp.onsdigital.uk
```
NB. You can test access to whitelodge AI using this link: https://whitelodge-ai-api.ai.census-gcp.onsdigital.uk/addresses/rh/uprn/10034869241?addresstype=paf&verbose=true

Run the contact centre service locally (either using eclipse or mvn spring-boot:run)

In the cccuc repo, in the terminal, set the GOOGLE_CLOUD_PROJECT environment variable to the same value as for the CCSVC above: 

```
export GOOGLE_CLOUD_PROJECT=<name of your project>
```
Now you can run cccuc in the terminal using this command: 

```
mvn clean install
```

