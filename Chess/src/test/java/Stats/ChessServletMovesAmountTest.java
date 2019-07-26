/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Stats;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author franciscoteixeira
 */
public class ChessServletMovesAmountTest {
    
    @Test
    public void ChessServletMovesAmountTest() throws Exception {
        
        String bodyText;
        
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/Chess/");
        driver.findElement(By.linkText("latest moves")).click();
        bodyText = driver.findElement(By.tagName("body")).getText();
        String[] array = bodyText.split("\n");
        assert(array.length==12);
        
        //Close the browser
        driver.quit();
        
        System.out.println("Success");
    }
    
}
