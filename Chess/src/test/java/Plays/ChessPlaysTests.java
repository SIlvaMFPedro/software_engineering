/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Plays;

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
public class ChessPlaysTests {
    
    @Test
    public void ChessPlaysTests() throws Exception {
        String bodyText, statsText;
        String findStr;
        int lastIndex;
        int count;
        
        //WebDriver driver = new FirefoxDriver();
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();
        driver.get("http://localhost:8080/Chess/");
        driver.findElement(By.linkText("latest moves")).click();
        bodyText = driver.findElement(By.tagName("body")).getText();
        driver.get("http://localhost:8080/Chess/MovesFrequencyServlet");        
        driver.manage().window().maximize();
        statsText = driver.findElement(By.tagName("body")).getText();
        System.out.println(statsText);
        //Assert.assertTrue("Text not found!", bodyText.contains(bodyText));
        //driver.findElement(By.id("textarea")).sendKeys("hahaha");
        driver.quit();
        
        String[] array = bodyText.split("\n");
        
        for(int i=2; i<array.length; i++)
        {
            findStr = array[i];
            lastIndex = 0;
            count = 0;

            while(lastIndex != -1){

                lastIndex = bodyText.indexOf(findStr,lastIndex);

                if(lastIndex != -1){
                    count ++;
                    lastIndex += findStr.length();
                }
            }

            //System.out.println(findStr+" - "+count+" ocurrences");
            
            assert(statsText.contains(findStr+" was played "+count));
//            if(statsText.contains(findStr+" was played "+count))
//                 System.out.println("Success");
//            else
//                System.out.println("Failure");    
        
        }
        System.out.println("Success");
    }
    
}
