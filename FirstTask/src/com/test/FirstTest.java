package com.test;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class FirstTest {

	WebDriver driver;

	@Test(priority = 1)
	public void testHeader() {

		String expHeader = "Your Shopping Cart";

		WebElement actualHeader = driver
				.findElement(By.xpath("//*[@id='shopping-cart-v2-root']/div/div[2]/div[1]/div[1]/h1"));
		String header = actualHeader.getText();
		System.out.println(header);

		Assert.assertEquals(header, expHeader);

	}

	@Test(priority = 2)
	public void testDisplay() {

		WebElement displayText = driver.findElement(By.xpath("//*[contains(text(), 'Your shopping cart is empty.' )]"));

		Assert.assertTrue(displayText.isDisplayed());

	}

	@Test(priority = 3)
	public void linkactive() {

		WebElement activeLink = driver.findElement(By.xpath("//*[@class='checkout-links']/a"));
		try {
			Actions actions = new Actions(driver);
			actions.moveToElement(activeLink).click().build().perform();
		}

		catch (AssertionError e) {
			System.out.println("Element " + activeLink.toString() + " not found on page");
			return;
		}
	}

	@Test(priority = 4)
	public void modalDisplay() {

		linkactive();

		WebElement displayText = driver.findElement(By.cssSelector("div#passwordReset"));
		boolean appear = displayText.isDisplayed();


		WebElement closeModal = driver.findElement(By.xpath(
				"//*[@class='gwt-submit-cancel-dialog-button-panel gwt-submit-cancel-dialog-button-panel-right-align']/button[1]"));

		Actions actions = new Actions(driver);
		actions.moveToElement(closeModal).click().build().perform();

		Assert.assertTrue(appear);
	}

	public String getDate(int rows, int colums) throws Exception {

		String path = System.getProperty("user.dir");
		String excelpath = path + "\\Excel\\Test.xlsx"; 

		File file = new File(excelpath);
		Sheet sheet = workbook.getSheetAt(0);

		int row = sheet.getLastRowNum();
		int col = sheet.getRow(0).getLastCellNum();

		Object data[][] = new Object[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				data[i][j] = sheet.getRow(i).getCell(j).toString();

			}

		}
		return data[rows][colums].toString();

	}
	
	
	@Test(priority = 5)
	public void errorTest() throws Exception {

		String inEmail = getDate(3,0);
		String pass = getDate(3, 1);

		WebElement email = driver.findElement(By.xpath("//input[@id='gwt-sign-in-modal']"));

		WebElement password = driver.findElement(By.xpath("//*[@id='passwordReset']"));

		WebElement singIn = driver.findElement(By.xpath("//*[@id='logonButton']"));

		Actions actions = new Actions(driver);

		email.sendKeys(inEmail);
		password.sendKeys(pass);
		actions.moveToElement(singIn).click().build().perform();
		
		
		WebElement errorMsg = driver.findElement(By.xpath("//*[@class='error-panel']/div"));
		
		System.out.println(errorMsg.getText());
		System.out.println(errorMsg.toString());
		actions.moveToElement(errorMsg).build().perform();
		boolean appear = errorMsg.isDisplayed();
		Assert.assertTrue(appear);
		
	}
	
	

	@Test(priority = 6)
	public void logInTest() throws Exception {
		
		driver.navigate().refresh();

		String inEmail = getDate(1, 0);
		String pass = getDate(1, 1);
		
		System.out.println("email : "+inEmail + "pass : "+pass);

		WebElement email = driver.findElement(By.xpath("//input[@id='gwt-sign-in-modal']"));
		WebElement password = driver.findElement(By.xpath("//*[@id='passwordReset']"));
		WebElement singIn = driver.findElement(By.xpath("//*[@id='logonButton']"));
		
		email.clear();
		password.clear();

		Actions actions = new Actions(driver);

		email.sendKeys(inEmail);
		password.sendKeys(pass);
		actions.moveToElement(singIn).click().build().perform();
		
		WebElement welcome = driver.findElement(By.xpath("//*[@id='welcome']/a"));
		boolean appear = welcome.isDisplayed();
		Assert.assertTrue(appear);
		
	}
	
	

	@BeforeMethod
	public void ads() {

		try {
			WebElement ads = driver.findElement(By.xpath("//*[@id='fcopt_form_35642']img"));

			if (ads.isDisplayed()) {
				WebElement X = driver.findElement(By.xpath("//*[@id='Close3-Item112']button"));
				Actions actions = new Actions(driver);
				actions.moveToElement(X).click().build().perform();
			}
		} catch (Exception e) {
			System.out.println("No ads");
		}

	}
	
	
	@AfterMethod
	public void adss() {

		try {
			WebElement ads = driver.findElement(By.xpath("//*[@id='fcopt_form_35642']img"));

			if (ads.isDisplayed()) {
				WebElement X = driver.findElement(By.xpath("//*[@id='Close3-Item112']button"));
				Actions actions = new Actions(driver);
				actions.moveToElement(X).click().build().perform();
			}
		} catch (Exception e) {
			System.out.println("No ads");
		}

	}
	

	@BeforeClass
	public void setUp() {
		String path = System.getProperty("user.dir"); // return project folder path
		String driverpath = path + "\\Chromedriver\\chromedriver.exe"; // return driver folder path
		System.setProperty("webdriver.chrome.driver", driverpath);
		
		driver = new ChromeDriver();

		driver.get("https://www.garnethill.com/ShoppingCartView");
		driver.manage().window().maximize();
		//driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
	}

	@AfterClass
	public void tearnDown() {
		driver.quit();
	}
}
