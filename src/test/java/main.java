import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class main {

    private WebDriver driver;
    private static String purchaseEmail;
    private static String purchaseOrderNumber;

    @BeforeMethod
    public void testInit() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod
    public void testCleanup() {
        driver.quit();
    }

    @Test(priority=1)
    public void completePurchaseSuccessfully_whenNewClient() throws InterruptedException {

        driver.navigate().to("http://demos.bellatrix.solutions/");

        var addToCartFalcon9 = driver.findElement(By.cssSelector("[data-product_id*='28']"));
        addToCartFalcon9.click();
        Thread.sleep(5000);

        var viewCartButton = driver.findElement(By.cssSelector("[class*='added_to_cart wc-forward']"));
        viewCartButton.click();

        var couponCodeTextField = driver.findElement(By.id("coupon_code"));
        couponCodeTextField.clear();
        couponCodeTextField.sendKeys("happybirthday");

        var applyCouponButton = driver.findElement(By.cssSelector("[value*='Apply coupon']"));
        applyCouponButton.click();
        Thread.sleep(5000);

        var messageAlert = driver.findElement(By.cssSelector("[class*='woocommerce-message']"));
        Assert.assertEquals(messageAlert.getText(), "Coupon code applied successfully.");

        var quantityBox = driver.findElement(By.cssSelector("[class*='input-text qty text']"));
        quantityBox.clear();
        Thread.sleep(500);
        quantityBox.sendKeys("2");
        Thread.sleep(5000);

        var updateCart = driver.findElement(By.cssSelector("[value*='Update cart']"));
        updateCart.click();
        Thread.sleep(5000);

        var totalSpan = driver.findElement(By.xpath("//*[@class='order-total']//span"));
        Assert.assertEquals("114.00€", totalSpan.getText());

        var proceedToCheckout = driver.findElement(By.cssSelector("[class*='checkout-button button alt wc-forward']"));
        proceedToCheckout.click();

        var billingFirstName = driver.findElement(By.id("billing_first_name"));
        billingFirstName.sendKeys("Anton");

        var billingLastName = driver.findElement(By.id("billing_last_name"));
        billingLastName.sendKeys("Angelov");

        var billingCompany = driver.findElement(By.id("billing_company"));
        billingCompany.sendKeys("Space Flowers");

        var billingCountryWrapper = driver.findElement(By.id("select2-billing_country-container"));
        billingCountryWrapper.click();

        var billingCountryFilter = driver.findElement(By.className("select2-search__field"));
        billingCountryFilter.sendKeys("Germany");

        var germanyOption = driver.findElement(By.xpath("//*[contains(text(),'Germany')]"));
        germanyOption.click();

        var billingAddress1 = driver.findElement(By.id("billing_address_1"));
        billingAddress1.sendKeys("1 Willi Brandt Avenue Tiergarten");

        var billingAddress2 = driver.findElement(By.id("billing_address_2"));
        billingAddress2.sendKeys("Lotzowplatz 17");

        var billingCity = driver.findElement(By.id("billing_city"));
        billingCity.sendKeys("Berlin");

        var billingZip = driver.findElement(By.id("billing_postcode"));
        billingZip.clear();
        billingZip.sendKeys("10115");

        var billingPhone = driver.findElement(By.id("billing_phone"));
        billingPhone.sendKeys("+00498888999281");

        var billingEmail = driver.findElement(By.id("billing_email"));
        billingEmail.sendKeys("info@berlinspaceflowers.com");
        purchaseEmail = "info@berlinspaceflowers.com";
        Thread.sleep(5000);

        var placeOrderButton = driver.findElement(By.id("place_order"));
        placeOrderButton.click();
        Thread.sleep(10000);

        var receivedMessage = driver.findElement(By.xpath("/html/body/div[1]/div/div/div/main/div/header/h1"));
        Assert.assertEquals(receivedMessage.getText(), "Order received");
    }
}
