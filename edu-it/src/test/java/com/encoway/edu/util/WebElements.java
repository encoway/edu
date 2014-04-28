package com.encoway.edu.util;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public abstract class WebElements {
	
	public static String getValue(WebDriver driver, WebElement element) {
		final JavascriptExecutor executor = (JavascriptExecutor) driver;
		return (String) executor.executeScript("return document.getElementById('" + element.getAttribute("id") + "').value");
	}

}
