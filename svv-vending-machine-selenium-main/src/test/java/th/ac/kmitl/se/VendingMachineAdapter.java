package th.ac.kmitl.se;

import java.time.Duration;
import java.util.List;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


@Model(file  = "VendingMachineV2.json")
public class VendingMachineAdapter extends ExecutionContext {
    WebDriver driver;
    WebDriverWait wait;
    static final float PRICE_TUM_THAI = 100.0f;
    static final float PRICE_TUM_POO = 120.0f;

    @BeforeExecution
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("https://fekmitl.pythonanywhere.com/kratai-bin");
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterExecution
    public void tearDown() {
        driver.quit();
    }

    @Vertex()
    public void WELCOME() {
        System.out.println("Vertex WELCOME");
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.elementToBeClickable(By.id("start")));
    }

    @Edge()
    public void start() {
        System.out.println("Edge start");
        driver.findElement(By.id("start")).click();
    }

    @Vertex()
    public void ORDERING() {
        System.out.println("Vertex ORDERING");
        // Wait for the check-out button to be clickable.
        // Your code here ...
//        new WebDriverWait(driver, Duration.ofSeconds(20)).until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_check_out"))));

        // Check that the number of orders is as expected.
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...
        String numTumThai = driver.findElement(By.id("txt_tum_thai")).getAttribute("value");
        String numTumPoo = driver.findElement(By.id("txt_tum_poo")).getAttribute("value");

        assertEquals(numTumThaiExpected, Integer.parseInt(numTumThai));
        assertEquals(numTumPooExpected, Integer.parseInt(numTumPoo));

    }

    @Edge()
    public void addTumThai() {
        System.out.println("Edge addTumThai");
        // Click add tum thai
        driver.findElement(By.id("add_tum_thai")).click();
    }

    @Edge()
    public void addTumPoo() {
        System.out.println("Edge addTumPoo");
        // Click add tum poo
        driver.findElement(By.id("add_tum_poo")).click();
    }

    @Vertex()
    public void ERROR_ORDER() {
        System.out.println("Vertex ERROR_ORDERING");
        // Wait for the alert dialog to be visible.
        wait.until(ExpectedConditions.alertIsPresent());
    }

    @Edge()
    public void ack() {
        System.out.println("Edge ack");
        // Click OK on the alert dialog
        driver.switchTo().alert().accept();
    }

    @Edge()
    public void cancel() {
        System.out.println("Edge cancel");
        // Click cancel button
        // Your code here ...
        driver.findElement(By.id("btn_cancel")).click();
    }

    @Edge()
    public void checkOut() {
        System.out.println("Edge checkOut");
        // Click check out button and wait for the confirm button to be clickable.
        // Your code here ...
        driver.findElement(By.id("btn_check_out")).click();

    }

    @Vertex()
    public void CONFIRMING() {
        System.out.println("Vertex CONFIRMING");
        // Wait for the confirm button to be clickable
        // Your code here ...
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_confirm"))));

        // Check that the information shown is as expected.
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...

        String numTumThai = driver.findElement(By.id("msg_num_tum_thai")).getText();
        String numTumPoo = driver.findElement(By.id("msg_num_tum_poo")).getText();

        System.out.printf("\n### Poo %s %d\n", numTumPoo, numTumPooExpected);
        System.out.printf("\n### Thai %s %d\n", numTumThai, numTumThaiExpected);

        assertEquals(numTumThaiExpected, Integer.parseInt(numTumThai));
        assertEquals(numTumPooExpected, Integer.parseInt(numTumPoo));

//        driver.findElement(By.id("btn_confirm")).click();

    }

    @Edge()
    public void change() {
        System.out.println("Edge change");
        // Click change button
        // Your code here ...
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_change"))));

        driver.findElement(By.id("btn_change")).click();
    }

    @Edge()
    public void pay() {
        System.out.println("Edge pay");
        // Click Confirm button
        // Your code here ...
        driver.findElement(By.id("btn_confirm")).click();

    }

    @Vertex()
    public void PAYING() {
        System.out.println("Vertex PAYING");
        // Wait for the pay button to be clickable.
        // Your code here ...
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_pay"))));

        // Check that the total amount is as expected.
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...

        String totalText = driver.findElement(By.id("msg_grand_total")).getText();

        float totalExpected = numTumPooExpected * PRICE_TUM_POO + numTumThaiExpected * PRICE_TUM_THAI;

        assertEquals(Float.parseFloat(totalText), totalExpected);
        // Check that payment error message is properly shown
        // Hint: Use getLastElement().getName() to get the name of the last visited edge.
        // Your code here ...
        System.out.printf("\n\n-------- %s\n\n", getLastElement().getName());
        if (getLastElement().getName() == "payRetry") {
            assertTrue(driver.findElement(By.id("msg_error")).isDisplayed());
        }
    }

    @Edge()
    public void paid() {
        System.out.println("Edge paid");
        // Submit valid payment details
        // Your code here ...
        driver.findElement(By.name("txt_credit_card_num")).sendKeys("123456789");
        driver.findElement(By.name("txt_name_on_card")).sendKeys("FirstName LastName");
        driver.findElement(By.id("btn_pay")).click();
    }

    @Edge()
    public void payError() {
        System.out.println("Edge payError");
        // Submit blank payment details to simulate payment error
        // Your code here ...
        driver.findElement(By.name("txt_credit_card_num")).sendKeys("");
        driver.findElement(By.name("txt_name_on_card")).sendKeys("");
        driver.findElement(By.id("btn_pay")).click();
    }

    @Vertex()
    public void ERROR_PAY() {
        System.out.println("Vertex ERROR_PAY");
    }

    @Edge()
    public void payRetry() {
        System.out.println("Edge payRetry");
    }

    @Vertex()
    public void COLLECTING() {
        System.out.println("Vertex COLLECTING");
        // Wait for images to be clickable
        // Your code here ...
//        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("ImgTumThai"))));
//        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.className("ImgTumPoo"))));

        // Check that the number of items shown is correct
        int numTumThaiExpected = getAttribute("numTumThai").asInt();
        int numTumPooExpected = getAttribute("numTumPoo").asInt();
        // Your code here ...
        assertEquals(numTumThaiExpected, driver.findElements(By.className("ImgTumThai")).size());
        assertEquals(numTumPooExpected, driver.findElements(By.className("ImgTumPoo")).size());

    }

    @Edge()
    public void collected() {
        System.out.println("Edge collected");
        // Click on each image to collect all dishes
        // Your code here ...
        for(WebElement elem: driver.findElements(By.className("ImgTumThai"))) {
            elem.click();
        }for(WebElement elem: driver.findElements(By.className("ImgTumPoo"))) {
            elem.click();
        }

    }

    @Edge()
    public void collectError() {
        System.out.println("Edge collectError");
        // Wait until the clearing page is shown
        // Your code here ...
        wait.until(ExpectedConditions.urlContains("https://fekmitl.pythonanywhere.com/kratai-bin/check_payment"));


    }

    @Vertex()
    public void ERROR_COLLECT() {
        System.out.println("Vertex ERROR_COLLECT");
    }

    @Edge()
    public void cleared() {
        System.out.println("Edge cleared");
        // Wait until redirection to the welcome page
        // Your code here ...
        wait.until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin"));

    }
}
