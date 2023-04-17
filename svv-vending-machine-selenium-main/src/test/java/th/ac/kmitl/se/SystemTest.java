package th.ac.kmitl.se;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SystemTest {
    WebDriver driver;
    @BeforeAll
    void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        driver.get("https://fekmitl.pythonanywhere.com/kratai-bin");
    }

    @AfterAll
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void test1() throws java.io.IOException {
//        FluentWait wait = new FluentWait(driver).withTimeout(Duration.ofSeconds(20));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        //start
        driver.findElement(By.id("start")).click();
        wait.until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin/order"));
        //cancel
        driver.findElement(By.id("btn_cancel")).click();
        wait.until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin"));

        driver.findElement(By.id("start")).click();
        wait.until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin/order"));

        driver.findElement(By.id("add_tum_thai")).click();
        driver.findElement(By.id("add_tum_thai")).click();
        driver.findElement(By.id("add_tum_thai")).click();
        driver.findElement(By.id("add_tum_poo")).click();
        driver.findElement(By.id("add_tum_poo")).click();
        driver.findElement(By.id("add_tum_poo")).click();

        driver.findElement(By.id("add_tum_thai")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        driver.findElement(By.id("add_tum_poo")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        driver.switchTo().alert().accept();

        driver.findElement(By.id("btn_check_out")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_change"))));

        driver.findElement(By.id("btn_change")).click();
        wait.until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin/order"));

        driver.findElement(By.id("btn_check_out")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_confirm"))));

        driver.findElement(By.id("btn_confirm")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_pay"))));

        driver.findElement(By.id("btn_pay")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.id("msg_error"))));

        driver.findElement(By.name("txt_credit_card_num")).sendKeys("123456789");
        driver.findElement(By.name("txt_name_on_card")).sendKeys("FirstName LastName");
        driver.findElement(By.id("btn_pay")).click();
        wait.until(ExpectedConditions.urlContains("https://fekmitl.pythonanywhere.com/kratai-bin/check_payment"));

        //cleared
        wait.until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin"));
        driver.findElement(By.id("start")).click();
        driver.findElement(By.id("add_tum_thai")).click();

        wait.until(ExpectedConditions.urlToBe("https://fekmitl.pythonanywhere.com/kratai-bin/order"));
        driver.findElement(By.id("btn_check_out")).click();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_confirm"))));
        driver.findElement(By.id("btn_confirm")).click();

        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("btn_pay"))));
        driver.findElement(By.name("txt_credit_card_num")).sendKeys("123456789");
        driver.findElement(By.name("txt_name_on_card")).sendKeys("FirstName LastName");
        driver.findElement(By.id("btn_pay")).click();

        wait.until(ExpectedConditions.urlContains("https://fekmitl.pythonanywhere.com/kratai-bin/check_payment"));
        driver.findElement(By.className("ImgTumThai")).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id("start"))));
    }
}