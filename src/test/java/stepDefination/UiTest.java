package stepDefination;

import io.cucumber.java.en.*;
import mission.BrowserSetup;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class UiTest {

    private WebDriver driver;

    @Given("I am on the home page")
    public void homePage() {
        driver = BrowserSetup.driver;
        driver.get("https://www.saucedemo.com/");
    }

    @And("I login in with the following details")
    public void loginData() {
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();
    }

    @And("I add the following items to the basket")
    @Test(dataProvider = "basketItems")
    public void addItemsToBasket(String itemName) {
        // Locate item by its name and click "Add to cart"
        driver.findElement(By.xpath("//div[text()='" + itemName + "']/ancestor::div[@class='inventory_item']//button"))
                .click();
    }
    public Object[][] basketItems() {
        return new Object[][]{
                {"Sauce Labs Backpack"},
                {"Sauce Labs Fleece Jacket"},
                {"Sauce Labs Bolt T-Shirt"},
                {"Sauce Labs Onesie"}
        };
    }

    @And("I  should see {int} items added to the shopping cart")
    public void itemsInCart(int expectedCount) {
        String count = driver.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(Integer.parseInt(count), expectedCount);
    }

    @And("I click on the shopping cart")
    public void clickShoppingCart() {
        driver.findElement(By.className("shopping_cart_link")).click();
    }

    @And("I verify that the QTY count for each item should be {int}")
    public void countQuantityOfItems(int expectedQty) {
        List<WebElement> qtyElements = driver.findElements(By.className("cart_quantity"));
        for (WebElement qty : qtyElements) {
            Assert.assertEquals(Integer.parseInt(qty.getText()), expectedQty);
        }
    }

    @And("I remove the following item:")
    public void removeItem(io.cucumber.datatable.DataTable dataTable) {
        String item = dataTable.asList().get(0);
        driver.findElement(By.xpath("//div[text()='" + item + "']/ancestor::div[@class='cart_item']//button")).click();
    }

    @And("I click on the CHECKOUT button")
    public void clickCheckout() {
        driver.findElement(By.id("checkout")).click();
    }

    @And("I type {string} for First Name")
    public void enterFirstName(String firstName) {
        driver.findElement(By.id("first-name")).sendKeys("FirstName");
    }

    @And("I type {string} for Last Name")
    public void enterLastName(String lastName) {
        driver.findElement(By.id("last-name")).sendKeys("LastName");
    }

    @And("I type {string} for ZIP/Postal Code")
    public void enterZip(String zip) {
        driver.findElement(By.id("postal-code")).sendKeys("EC1A 9JU");
    }

    @When("I click on the CONTINUE button")
    public void clickOnContinue() {
        driver.findElement(By.id("continue")).click();
    }

    @Then("Item total will be equal to the total of items on the list")
    public void itemTotal() {
        List<WebElement> prices = driver.findElements(By.className("inventory_item_price"));
        double sum = 0;
        for (WebElement price : prices) {
            sum = sum + Double.parseDouble(price.getText().replace("$", ""));
        }
        double itemTotal = Double.parseDouble(driver.findElement(By.className("summary_subtotal_label"))
                .getText().replace("Item total: $", ""));
        Assert.assertEquals(itemTotal, sum);
    }

    @And("a Tax rate of {int} % is applied to the total")
    public void taxRate(int taxRate) {
        double itemTotal = Double.parseDouble(driver.findElement(By.className("summary_subtotal_label"))
                .getText().replace("Item total: $", ""));
        double tax = Double.parseDouble(driver.findElement(By.className("summary_tax_label"))
                .getText().replace("Tax: $", ""));
        double expectedTax = itemTotal * taxRate / 100.0;
        Assert.assertEquals(tax, expectedTax, 0.01);
    }
}
