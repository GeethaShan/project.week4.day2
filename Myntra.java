package project.week4.day2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Myntra {

	public static void main(String[] args) throws InterruptedException, IOException {

		WebDriverManager.chromedriver().setup();
		ChromeDriver driver = new ChromeDriver();

		// 1) Open https://www.myntra.com/
		driver.get("https://www.myntra.com/");
		Thread.sleep(3000);
		System.out.println("Opened Myntra site");

		// 2) Mouse hover on MeN
		WebElement menElement = driver.findElement(By.xpath("//a[text()='Men']"));
		Thread.sleep(2000);
		Actions actionBuilder = new Actions(driver);
		actionBuilder.moveToElement(menElement).perform();
		Thread.sleep(2000);

		// 3) Click Jackets
		driver.findElement(By.xpath("//a[text()='Jackets' and @href='/men-jackets']")).click();
		System.out.println("Clicked Men->Jackets");

		// 4) Find the total count of item
		String totalCountTop = driver.findElement(By.xpath("//span[@class='title-count']")).getText();
		String totalCount = totalCountTop.replaceAll("[^0-9]", "");
		System.out.println("Total count of item is " + totalCount);

		// 5) Validate the sum of categories count matches
		List<WebElement> categoriesCount = driver.findElements(By.xpath("//ul[@class='categories-list']//span"));
		int count = 0;
		for (WebElement catElement : categoriesCount) {
			String eachVal = catElement.getText().replaceAll("[^0-9]", "");
			System.out.println(eachVal);
			int parseInt = Integer.parseInt(eachVal);
			count = count + parseInt;
		}
		if (Integer.toString(count).equals(totalCount)) {
			System.out.println("Pass - Count match");
		} else
			System.out.println(
					"Fail - Count doesnt match.Expected : " + totalCount + " , Actual : " + Integer.toString(count));

		Thread.sleep(3000);

		// 6) Check jackets
		driver.findElement(By.xpath("//input[@value='Jackets']/following::div")).click();
		System.out.println("Checked Jackets checkbox");

		// 7) Click + More option under BRAND
		driver.findElement(By.xpath("//div[text()='+ ']")).click();
		System.out.println("Clicked More+ option under brand");

		// 8) Type Duke and click checkbox
		driver.findElement(By.xpath("//input[@placeholder='Search brand']")).sendKeys("Duke");
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@value='Duke']/following::div[1]")).click();
		System.out.println("Selected Duke");

		// 9) Close the pop-up x
		driver.findElement(By.xpath("//div[@class='FilterDirectory-titleBar']/span")).click();
		Thread.sleep(3000);
		System.out.println("Popup closed");

		// 10) Confirm all the Coats are of brand Duke
		List<WebElement> dukeElements = driver.findElements(By.tagName("h3"));
		// printing size-1 as total count, as last h3 tag in the page is a video
		System.out.println("Duke coats count : " + (dukeElements.size() - 1));
		boolean duke = true;
		for (int i = 0; i < dukeElements.size() - 1; i++) {
			String text = dukeElements.get(i).getText();
			if (!text.equals("Duke")) {
				System.out.println((i + 1) + "-Product is not a Duke. Actual : " + text);
				duke = false;
			}
		}
		if (duke) {
			System.out.println("All the coats are of brand Duke");
		}

		// 11) Sort by Better Discount
		driver.findElement(By.xpath("//div[text()='Sort by : ']")).click();
		driver.findElement(By.xpath("//ul[@class='sort-list']//label[text()='Better Discount']")).click();
		System.out.println("Products sorted by better discount");

		// 12) Find the price of first displayed item
		String firstItemPrice = driver
				.findElement(By.xpath("//ul[@class='results-base']/li[1]//span[@class='product-discountedPrice']"))
				.getText();
		System.out.println("Price of first displayed item is : " + firstItemPrice);

		// 13) Click on the first product
		driver.findElement(By.xpath("//ul[@class='results-base']/li[1]//img")).click();
		System.out.println("Clicked first product");

		Set<String> windowHandles = driver.getWindowHandles();
		List<String> windowHandlesList = new ArrayList<String>(windowHandles);
		driver.switchTo().window(windowHandlesList.get(1));

		// 14) Take a screen shot
		File src = driver.getScreenshotAs(OutputType.FILE);
		File dest = new File("./snaps/myntraFirstItem.png");
		FileUtils.copyFile(src, dest);
		System.out.println("Screenshot taken");

		// 15) Click on WishList Now
		driver.findElement(By.xpath("//span[text()='WISHLIST']")).click();
		System.out.println("Added to wishlist");

		// 16) Close Browser
		driver.quit();
		System.out.println("Browser closed");
	}

}
