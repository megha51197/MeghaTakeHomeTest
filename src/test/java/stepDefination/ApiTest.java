package stepDefination;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.cucumber.java.en.*;
import org.openqa.selenium.By;
import org.testng.Assert;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static mission.BasePage.driver;

public class ApiTest {

    private Response response;

    @Given("I get the default list of users for on 1st page")
    public void defaultListOfUsers()
    {
        RestAssured.baseURI = "https://reqres.in/";
        response = given().when().get("/users?page=1");
    }

    @When("I get the list of all users within every page")
    public void allUsers()
    {
        int totalPages = response.jsonPath().getInt("total_pages");
        for (int i = 1; i <= totalPages; i++)
        {
            response = given().when().get("/users?page=" + i);
        }
    }

    @Then("I should see total users count equals the number of user ids")
    public void totalUsersCount()
    {
        int total = response.jsonPath().getInt("total");
        List<Integer> ids = response.jsonPath().getList("data.id");
        Assert.assertEquals(ids.size(), total);
    }

    @Given("I make a search for user {int}")
    public void searchUser(int userId)
    {
        response = given().when().get("/users/" + userId);
    }

    @Then("I should see the following user data")
    public void verifyUserData() {
        String actualFirstName = driver.findElement(By.id("first-name")).getText();
        String actualEmail = driver.findElement(By.id("email")).getText();
        String expectedFirstName = "Emma";
        String expectedEmail = "emma.wong@reqres.in";
        Assert.assertEquals(actualFirstName, expectedFirstName, "First name does not match!");
        Assert.assertEquals(actualEmail, expectedEmail, "Email does not match!");
    }


    @Then("I receive error code {int} in response")
    public void errorCode(int code)
    {
        Assert.assertEquals(response.getStatusCode(), code);
    }

    @Given("I create a user with following {string} {string}")
    public void createUser(String name, String job) {
        driver.findElement(By.id("name")).sendKeys(name);
        driver.findElement(By.id("job")).sendKeys(job);
        driver.findElement(By.id("create-user")).click();
    }

    @Then("response should contain the following data")
    public void checkCreatedUser() {
        String actualName = driver.findElement(By.id("created-name")).getText();
        String actualJob = driver.findElement(By.id("created-job")).getText();
        String actualId = driver.findElement(By.id("created-id")).getText();
        String actualCreatedAt = driver.findElement(By.id("created-at")).getText();

        Assert.assertNotNull(actualId, "User ID should not be null");
        Assert.assertNotNull(actualCreatedAt, "CreatedAt timestamp should not be null");
        Assert.assertEquals(actualName, driver.findElement(By.id("name")).getAttribute("value"));
        Assert.assertEquals(actualJob, driver.findElement(By.id("job")).getAttribute("value"));
    }


    @Given("I login unsuccessfully with the following data")
    public void login(io.cucumber.datatable.DataTable dataTable)
    {
        Map<String, String> credentials = dataTable.asMaps().get(0);
        response = given()
                .contentType("application/json")
                .body(credentials)
                .when()
                .post("/login");
    }

    @Then("I should get a response code of {int}")
    public void verifyLoginResponseCode(int code)
    {
        Assert.assertEquals(response.getStatusCode(), code);
    }

    @And("I should see the following response message:")
    public void errorMessage() {
        String actualMessage = driver.findElement(By.cssSelector(".error-message-container")).getText();
        String expectedMessage = "Missing password";
        Assert.assertTrue(actualMessage.contains(expectedMessage),
                "Expected error message not found! Actual: " + actualMessage);
    }


    @Given("I wait for the user list to load")
    public void waitForUserList()
    {
        response = given().when().get("/users?delay=3");
    }

    @Then("I should see that every user has a unique id")
    public void userHasUniqueIds()
    {
        List<Integer> ids = response.jsonPath().getList("data.id");
        Assert.assertEquals(ids.size(), ids.stream().distinct().count());
    }
}