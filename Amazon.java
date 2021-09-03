package project.week4.day2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Amazon {

	public ChromeDriver driverSetUp(String url) {
		WebDriverManager.chromedriver().setup();
		ChromeDriver driverElement = new ChromeDriver();
		driverElement.get(url);
		driverElement.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driverElement;

	}

	public static void main(String[] args) throws IOException, InterruptedException {

		// Load the uRL https://www.amazon.in/
		Amazon objAmazon = new Amazon();
		ChromeDriver driver = objAmazon.driverSetUp("https://www.amazon.in/");

		// search as oneplus 9 pro
		driver.findElement(By.id("twotabsearchtextbox")).sendKeys("oneplus 9 pro");
		driver.findElement(By.id("nav-search-submit-button")).click();

		/// Get the price of the first product
		String priceFirstProd = driver
				.findElement(By.xpath("//div[@data-cel-widget='search_result_1']//span[@class='a-price-whole']"))
				.getText();
		System.out.println("Price of the first product is " + priceFirstProd);

		// Print the number of customer ratings for the first displayed product
		String ratingFirstProd = driver
				.findElement(By.xpath("//div[@data-cel-widget='search_result_1']//span[@class='a-size-base']"))
				.getText();
		System.out.println("Number of Ratings for the first product is " + ratingFirstProd);
		Thread.sleep(3000);
		
		//Mouse Hover on the stars
		WebElement ratingElement = driver.findElement(By.xpath("//div[@data-cel-widget='search_result_1']//span[contains(@aria-label,'out of 5 stars')]"));
		ratingElement.click();
		Thread.sleep(3000);

		// Get the percentage of ratings for the 5 star.
		String percFiveStar = driver.findElement(By.xpath("//td[@class='a-text-right a-nowrap']//a[contains(@title,'have 5 stars')]")).getText();
		System.out.println("Percentage of 5 Star ratings is : "+percFiveStar);

		// Click the first text link of the first image
		driver.findElement(By.xpath("//div[@data-cel-widget='search_result_1']//h2[1]/a")).click();
		Thread.sleep(3000);
		System.out.println("First search result link clicked");

		// get the handle of the new window and switch to the new window handle
		Set<String> windowHandles = driver.getWindowHandles();
		List<String> windowHandlesList = new ArrayList<String>(windowHandles);
		driver.switchTo().window(windowHandlesList.get(1));
		Thread.sleep(3000);

		// Take a screen shot of the product displayed
		File src = driver.getScreenshotAs(OutputType.FILE);
		File dest = new File("./snaps/amazonFirstProd.png");
		FileUtils.copyFile(src, dest);
		System.out.println("Screenshot taken for the first product");

		// Click 'Add to Cart' button
		driver.findElement(By.id("add-to-cart-button")).click();
		Thread.sleep(3000);
		System.out.println("Clicked add to cart button");

		// Get the cart subtotal and verify if it is correct.
		WebElement cartEle = driver.findElement(By.xpath("//span[@id='attach-accessory-cart-total-string']"));
		String cartTotal = cartEle.getText();
		Thread.sleep(3000);
		//System.out.println(cartTotal);
		String replaceAll = cartTotal.replaceAll("[^0-9]", "");
		//System.out.println(replaceAll);
		int intCartTotal = Integer.parseInt(replaceAll);
		if (intCartTotal == 1) {
			System.out.println("Cart Subtotal is correct.Count : " + intCartTotal);
		} 
		else
			System.out.println("Cart Subtotal is wrong.Actual : " + intCartTotal + " ,Expected : 1");
		
		
		//Close the browser
		driver.quit();
	}

}
