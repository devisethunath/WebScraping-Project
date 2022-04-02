package com.WebScrapingProject.utilities;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitHelper {
	
public WebDriver driver;
	
	public  WaitHelper (WebDriver driver)
	{
		this.driver=driver;
	}
	/*Method to set up explicit wait for element*/
	public void WaitForElement(WebElement element,long timeOutInSeconds)
	{
		WebDriverWait wait= new WebDriverWait(driver,Duration.ofSeconds(30));
		wait.until(ExpectedConditions.visibilityOf(element));
	}


}
