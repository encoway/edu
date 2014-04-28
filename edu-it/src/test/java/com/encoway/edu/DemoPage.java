package com.encoway.edu;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.encoway.edu.util.WebDriverPatience;

public class DemoPage {
	
	private static final String UPDATED_STRING_CLASS_CONSTRAINT = "contains(@class, 'updated-string-value')";
	
	@FindBy(xpath="//form/input[@type='submit']")
	WebElement submitButton;
	
	private final WebDriver driver;
	
	public DemoPage(WebDriver driver) {
		this.driver = driver;
	}
	
	public void updateStringValue() {
		submitButton.click();
		WebDriverPatience.wait(driver, 50, TimeUnit.MILLISECONDS);
	}
	
	public WebElement getOutputText() {
		return driver.findElement(By.xpath("//span[" + UPDATED_STRING_CLASS_CONSTRAINT + "]"));
	}
	
	public WebElement getInputText() {
		return driver.findElement(By.xpath("//form/input[" + UPDATED_STRING_CLASS_CONSTRAINT + "]"));
	}

}
