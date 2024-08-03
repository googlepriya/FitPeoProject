package com.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.interactions.Actions;

public class FitPeoAutomation {
	public static WebDriver driver;
	public static Properties properties;
	
	public Properties loadPropertyFile() throws IOException {
		FileInputStream fileInputStream = new FileInputStream("config.properties");
		properties = new Properties();
		properties.load(fileInputStream);
		return properties;
	}
	
	@BeforeSuite
	public void lauchBrowser() throws IOException {
		loadPropertyFile();
		String browser = properties.getProperty("browser");
		String URL = "https://www.fitpeo.com/";
		String driverLocation = properties.getProperty("driverLocation");
		
		System.setProperty("webdriver.chrome.driver", driverLocation);
		driver = new ChromeDriver();
	    driver.manage().window().maximize();
	    
	 //1.   Navigate to the FitPeo Homepage:
	    driver.get(URL);
	}
	
    //2.Navigate to Revenue Calculator Page:
    @Test(priority = 1)
	public void navigateToRevenueCalculatorPage() {
		WebElement revenueCalculatorLink = driver.findElement(By.xpath("//div[text()='Revenue Calculator']"));
	    revenueCalculatorLink.click(); 
	}
	
	//3. Scroll Down to the Slider section:
    @Test(priority = 2)
	public void scrollDownSlider() {
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		WebElement revenueCalculatorSlider = driver.findElement(By.xpath("//h4[text()='Medicare Eligible Patients']"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].scrollIntoView(true);", revenueCalculatorSlider);
		
	}
	
	// 4.Adjust the Slider:
    @Test(priority = 3)
	public void adjustSlider() {
		WebElement slider = driver.findElement(By.xpath("//input[@data-index=\"0\"]"));
	//	JavascriptExecutor js = (JavascriptExecutor) driver;
		//js.executeScript("arguments[0].value = arguments[1];", slider, 520); 
		int sliderWidth = slider.getSize().getWidth();
        int desiredValue = 520;  // Example desired value
        int maxValue = 2000;  // Example maximum value of the slider
        int offset = (int) ((desiredValue / (double) maxValue) * sliderWidth);
		Actions actions = new Actions(driver);
        actions.dragAndDropBy(slider, offset, 0).perform();  // Adjust the offset as needed

		
	}
	
    //5. Update the Text Field:
    @Test(priority = 4)
	public void updateTextField() {
	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	WebElement SliderInput = driver.findElement(By.xpath("(//input[@min='0'])[2]"));
	SliderInput.sendKeys(Keys.CONTROL + "a");
	SliderInput.sendKeys(Keys.CONTROL + "x");
	SliderInput.sendKeys("560");
	}
    
	//6.Validate Slider Value:
    @Test(priority = 5)
	public void validateSliderValue() {
    	WebElement SliderInput = driver.findElement(By.xpath("(//input[@min='0'])[2]"));
    	String expectedValue = "560";
    	String actualValue = SliderInput.getAttribute("value");
        Assert.assertEquals(actualValue, expectedValue, "the slider's position is updated to reflect the value 560");
	}
	
    //7.Select CPT Codes:
    @Test(priority = 6)
	public void selectCPTCodes() {
    	//Initialize JavascriptExecutor to scroll into view the element 
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Find & Scroll down into View CPT-99453
    	WebElement CPT1Element1 = driver.findElement(By.xpath("//p[text()='CPT-99091']"));
    	js.executeScript("arguments[0].scrollIntoView(true);", CPT1Element1);
    	
    	// Find & Click the CPT-99453
    	driver.findElement(By.xpath("(//input[@type='checkbox'])[1]")).click();
    	
    	// Find & Click the CPT-99454
    	driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();
    	
    	// Find & Scroll down into View CPT-99453
    	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    	WebElement CPT1Element4 = driver.findElement(By.xpath("(//p[text()='CPT-99454'])[1]"));
    	
    	// Scroll down into View CPT-99474
		js.executeScript("arguments[0].scrollIntoView(true);", CPT1Element4);
		
		// Find & Click the CPT-99453
    	driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    	driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).click();
	}
	
    @Test(priority = 7)
	public void validateTotalRecurringReimbursement() {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    	WebElement SliderInput = driver.findElement(By.xpath("(//p[text()='67200'])[1]"));
    	String expectedValue = "$67200";
    	String actualValue = SliderInput.getText();
    	System.out.println(actualValue);
    	Assert.assertEquals(actualValue, expectedValue, "Total Recurring Reimbursement shows the value $67200");
	}
	
    @Test(priority = 8)
	public void verifyDisplayValue() {
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    	WebElement SliderInput = driver.findElement(By.xpath("(//p[text()='67200'])[1]"));
    	boolean isDisplayed = SliderInput.isDisplayed();
    	Assert.assertTrue(isDisplayed,"Total Recurring Reimbursement for all Patients Per Month: shows the value $67200");
	}

	
	@AfterSuite
	public void tearDown() {
		driver.close();
		
	}

}
