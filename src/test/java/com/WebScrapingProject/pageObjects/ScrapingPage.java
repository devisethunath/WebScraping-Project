package com.WebScrapingProject.pageObjects;

import java.io.IOException;
//import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.WebScrapingProject.utilities.WaitHelper;
import com.WebScrapingProject.utilities.XLUtils;

//import com.RecipeScrapingHackathon.utilities.WaitHelper;

public class ScrapingPage {

	public WebElement nextButton;
	public WaitHelper waithelper;
	public int count=1;
	public WebElement jobs_count;
	public WebElement jobCompanyName;
	public int excelCount=1;
	public String category;
	WebDriver ldriver;
	public ScrapingPage(WebDriver rdriver)
	{
		ldriver=rdriver;
		PageFactory.initElements(rdriver, this);
		waithelper=new WaitHelper(ldriver);
	}

	@FindBy(css="#text-input-what")
	WebElement txtSearchWhat;

	@FindBy(name="l")
	WebElement txtSearchWhere;

	@FindBy(css="button[type='submit']")
	WebElement btnFindJob;

	@FindBy(css="#filter-dateposted")
	WebElement listDatePosted;

	@FindBy(xpath="//a[contains(text(),'Last 24 hours')]")
	WebElement listItemhours;

	@FindBy(css=".pagination-list")
	WebElement pagination;

	@FindBy(css="#vjs-container-iframe")
	WebElement iframe;

	@FindBy(xpath="//body/div[@id='viewJobSSRRoot']/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[1]/div[2]/div[1]/h1[1]")
	WebElement jobTitle;

	/*@FindBy(xpath="//body/div[@id='viewJobSSRRoot']/div[1]/div[1]/div[3]/div[1]/div[1]/div[1]/div[1]/div[3]/div[1]/div[2]/div[1]/div[1]/div[1]/div[1]")
	WebElement jobCompanyName;*/

	@FindBy(xpath="//div[@class='jobsearch-CompanyInfoContainer']/div/div/div/div[2]/div")
	WebElement jobLocation;

	@FindBy(css=".jobsearch-HiringInsights-entry--text")
	WebElement jobPostedTime;

	@FindBy(css="#jobDescriptionText")
	WebElement jobDescription;

	public void setKeyword(String ekeyword)
	{
		category=ekeyword;
		waithelper.WaitForElement(txtSearchWhat, 30);
		txtSearchWhat.clear();
		txtSearchWhat.sendKeys(ekeyword);
	}

	public void setLocation()
	{
		waithelper.WaitForElement(txtSearchWhere, 30);
		txtSearchWhere.sendKeys(Keys.chord(Keys.CONTROL,"a",Keys.DELETE));
		txtSearchWhere.sendKeys("Remote");
	}

	public void clickButton() throws InterruptedException
	{
		Thread.sleep(3000);
		JavascriptExecutor js=(JavascriptExecutor)ldriver;
		js.executeScript("arguments[0].click()", btnFindJob);
	}

	public void setDatePosted()
	{

		JavascriptExecutor js=(JavascriptExecutor)ldriver;
		js.executeScript("arguments[0].click()", listDatePosted);
		js.executeScript("arguments[0].click()", listItemhours);
	}
	public void getAlert() throws InterruptedException
	{
		//Thread.sleep(15000);
		WebElement btnClose=ldriver.findElement(By.xpath("//*[@id='popover-x']"));
		waithelper.WaitForElement(btnClose, 30);
		btnClose.click();
	}

	public void setPagination() throws InterruptedException
	{
		int c=1;
		while(c!= 0)
		{
			try
			{
				//Scraping
				scraping();

				JavascriptExecutor js=(JavascriptExecutor)ldriver;
				js.executeScript("window.scrollTo(0,document.body.scrollHeight)");
				WebElement arrow=ldriver.findElement(By.xpath("//a[@aria-label='Next']"));
				waithelper.WaitForElement(arrow, 30);
				//Thread.sleep(5000);
				js.executeScript("arguments[0].click()", arrow);
				Thread.sleep(6000);
				count++;
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
				System.out.println(	"Count is"+count++ );
				break;
			}
			Thread.sleep(5000);
		}}

	public void scraping() throws InterruptedException, IOException
	{

		WebDriverWait mywait=new WebDriverWait(ldriver,Duration.ofSeconds(30));

		JavascriptExecutor js=(JavascriptExecutor)ldriver;
		js.executeScript("window.scrollTo(0,document.body.scrollHeight)");

		try
		{
			ldriver.findElement(By.cssSelector("b[aria-label='"+count+"']")).click();
			Thread.sleep(3000);
		}
		catch(Exception e)
		{
			System.out.println("Only one Page is available");
		}

		WebElement jobs=ldriver.findElement(By.id("mosaic-provider-jobcards"));
		List<WebElement>jobs_count=jobs.findElements(By.xpath("//div[@id='mosaic-zone-jobcards']/div/a"));   ////a[@data-hiring-event='false']
		System.out.println(jobs_count.size()+"jobs count on "+ count +" page");


		for(int i=0;i<jobs_count.size();i++)
		{
			List<WebElement>jobsNo=jobs.findElements(By.xpath("//div[@id='mosaic-zone-jobcards']/div/a"));    ////a[@data-hiring-event='false']

			System.out.println(count+" st page"+ (i+1) +"loop now");

			//New Code

			List <WebElement>closeButton=ldriver.findElements(By.xpath("//button[@class='icl-CloseButton icl-Modal-close']"));
			if(closeButton.size()>0)
			{
				closeButton.get(0).click();
			}

			String href=jobsNo.get(i).getAttribute("href");
			jobsNo.get(i).click();
			ldriver.switchTo().frame(iframe);

			Thread.sleep(3000);

			WebElement jobTitle=mywait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h1")));
			XLUtils.setCellData("Sheet1", i+excelCount , 0, jobTitle.getText());
			System.out.println(	 "Job Title"+ jobTitle.getText());


			System.out.println("Job Ctegory "+category);
			XLUtils.setCellData("Sheet1", i+excelCount , 1, category);

			WebElement jobCompanyName=mywait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='jobsearch-CompanyInfoContainer']/div/div/div/div/div[2]/div")));

			System.out.println("Job Company Name: "+jobCompanyName.getText());
			XLUtils.setCellData("Sheet1",  i+excelCount , 2, jobCompanyName.getText());

			waithelper.WaitForElement(jobLocation, 30);
			System.out.println("Job Location: "+jobLocation.getText());
			XLUtils.setCellData("Sheet1",  i+excelCount , 3, jobLocation.getText());

			waithelper.WaitForElement(jobPostedTime, 30);
			String jobpostedDate=getDate(jobPostedTime.getText());
			System.out.println("Job Posted Time: "+jobpostedDate);
			XLUtils.setCellData("Sheet1",  i+excelCount , 4, jobpostedDate);

			waithelper.WaitForElement(jobDescription, 30);
			System.out.println("Job Description: "+jobDescription.getText());
			XLUtils.setCellData("Sheet1",  i+excelCount , 5, jobDescription.getText());

			System.out.println("Position Id: "+"Null");
			XLUtils.setCellData("Sheet1",  i+excelCount , 6, "Null");


			System.out.println("Job Link: "+ href);
			XLUtils.setCellData("Sheet1",  i+excelCount , 7, href);

			DateFormat df=new SimpleDateFormat("dd/MM/yy HH:mm:ss");
			Date dateobj=new Date();
			System.out.println("Date Time Scrapped: "+df.format(dateobj));
			XLUtils.setCellData("Sheet1",  i+excelCount, 8, df.format(dateobj));

			XLUtils.setCellData("Sheet1",  i+excelCount, 9, "Null");
			XLUtils.setCellData("Sheet1",  i+excelCount, 10, "Null");

			ldriver.switchTo().defaultContent();

		}
		excelCount=excelCount+jobs_count.size();
	}

	public void setExcelHeader(String excelpath) throws IOException
	{

		//String path=excelpath;
		XLUtils xlutil=new XLUtils(excelpath);

		/*Setting Excel Header*/
		XLUtils.setCellData("Sheet1", 0, 0, "Job Title");
		XLUtils.setCellData("Sheet1", 0, 1, "Job Category");
		XLUtils.setCellData("Sheet1", 0, 2, "Job Company Name");
		XLUtils.setCellData("Sheet1", 0, 3, "Job Location");
		XLUtils.setCellData("Sheet1", 0, 4, "Job Posted Date");
		XLUtils.setCellData("Sheet1", 0, 5, "Job Description");
		XLUtils.setCellData("Sheet1", 0, 6, "Position ID");
		XLUtils.setCellData("Sheet1", 0, 7, "Job Link");
		XLUtils.setCellData("Sheet1", 0, 8, "Date Time Scraped");
		XLUtils.setCellData("Sheet1", 0, 9, "Email");
		XLUtils.setCellData("Sheet1", 0, 10, "Contact No:");

	}

	public String getDate(String ejobPostedTime)
	{
		Calendar cal=Calendar.getInstance();

		String todayDate="Posted today";
		String yesterdayDate="Posted 1 day ago";

		if (ejobPostedTime.equalsIgnoreCase(todayDate))

			return cal.getTime().toString();

		else if (ejobPostedTime.equalsIgnoreCase(yesterdayDate))

			cal.add(Calendar.DATE, -1);
		return cal.getTime().toString();

	}

}


















