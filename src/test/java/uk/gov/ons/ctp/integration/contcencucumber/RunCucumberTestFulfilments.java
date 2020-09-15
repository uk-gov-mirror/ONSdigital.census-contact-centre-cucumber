package uk.gov.ons.ctp.integration.contcencucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    plugin = {"pretty", "html:target/cucumber"},
    features = {"src/test/resources/integrationtests/fulfilments"},
    glue = {
      "uk.gov.ons.ctp.integration.contcencucumber.cucSteps.fulfilments",
      "uk.gov.ons.ctp.integration.contcencucumber.main"
    })
public class RunCucumberTestFulfilments {}
