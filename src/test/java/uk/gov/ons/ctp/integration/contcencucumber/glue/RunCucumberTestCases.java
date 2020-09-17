package uk.gov.ons.ctp.integration.contcencucumber.glue;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = {
      "src/test/resources/integrationtests/case/smokeTests.feature",
      "src/test/resources/integrationtests/case/testCaseEndpoints.feature",
      "src/test/resources/integrationtests/case/testTelephoneEndpoint.feature"
    },
    glue = {
      "uk.gov.ons.ctp.integration.contcencucumber.cucSteps.cases",
      "uk.gov.ons.ctp.integration.contcencucumber.main"
    })
public class RunCucumberTestCases {}
