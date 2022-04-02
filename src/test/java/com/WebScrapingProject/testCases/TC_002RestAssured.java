package com.WebScrapingProject.testCases;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.Test;

import com.WebScrapingProject.pageObjects.ScrapingPage;
//import com.WebScrapingProject.pageObjects.ScrapingPageRest;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TC_002RestAssured {

	public static WebDriver driver;
	public String keyword="RestAssured";
	public String excelPath="C:\\Users\\sethu\\eclipse-workspace\\WebScrapingProject\\src\\test\\java\\com\\WebScrapingProject\\testData\\RestAssured.xlsx";

	@Test
	public void ScrapingApiTest() throws InterruptedException, IOException
	{
		ChromeOptions chromeOptions = new ChromeOptions();


		chromeOptions.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.DISMISS);
		chromeOptions.setPageLoadStrategy(PageLoadStrategy.EAGER);
		chromeOptions.addArguments("--enable-automation");
		//chromeOptions.addArguments("disable-infobars");
		chromeOptions.addExtensions(new File("C://Users//sethu//eclipse-workspace//WebScrapingProject//1.3.1_0.crx"));
		
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver(chromeOptions);//chromeOptions
		driver.get("https://www.indeed.com");
		Thread.sleep(3000);
		driver.manage().window().maximize();
		Thread.sleep(4000);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

		ScrapingPage sp=new ScrapingPage(driver);
		sp.setKeyword(keyword);
		Thread.sleep(3000);
		
		sp.setLocation();
		Thread.sleep(3000);
		
		sp.clickButton();
		Thread.sleep(3000);
		
		sp.setDatePosted();
		Thread.sleep(3000);
		
		sp.getAlert();
		Thread.sleep(3000);
		
		sp.setExcelHeader(excelPath);
		
		sp.setPagination();
		
		
		
	


	}

}
