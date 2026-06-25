package mission;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "classpath:features",
        glue = {"stepDefination"},
        plugin = {"pretty", "html:target/cucumber-report.html"},
        monochrome = true
)
public class RunnerTest extends AbstractTestNGCucumberTests {
}
