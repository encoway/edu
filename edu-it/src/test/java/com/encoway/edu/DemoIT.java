package com.encoway.edu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

import com.encoway.edu.util.WebElements;

public class DemoIT {
	
	private WebDriver driver;
	
	@Before
	public void setupWebDriver() {
		driver = new HtmlUnitDriver(true);  
	}
	
	@After
	public void closeWebDriver() {
		if (driver != null) {			
			driver.close();
		}
	}
	
	@Test
	public void thatStingsAreUpdated() {
		final DemoPage demoPage = PageFactory.initElements(driver, DemoPage.class).get();
		
		assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getStringInput()), is("Old Value"));
		assertThat("unexpected initial output value", demoPage.getOutputText().getText(), containsString("Old Value"));
		
		demoPage.updateStringValue();
		
		assertThat("unexpected updated input value", WebElements.getValue(driver, demoPage.getStringInput()), is("Old Value+"));
		assertThat("unexpected updated output value", demoPage.getOutputText().getText(), containsString("Old Value+"));
		
		demoPage.reset();
		
		assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getStringInput()), is("Old Value"));
		assertThat("unexpected initial output value", demoPage.getOutputText().getText(), containsString("Old Value"));
	}
	
	@Test
	public void thatIntegersAreUpdated() {
		final DemoPage demoPage = PageFactory.initElements(driver, DemoPage.class).get();
		
		assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getIntegerInput()), is("0"));
		assertThat("unexpected initial output value", demoPage.getOutputText().getText(), containsString("0"));
		
		demoPage.updateIntegerValue();
		
		assertThat("unexpected updated input value", WebElements.getValue(driver, demoPage.getIntegerInput()), is("1"));
		assertThat("unexpected updated output value", demoPage.getOutputText().getText(), containsString("1"));
		
		demoPage.reset();
		
		assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getIntegerInput()), is("0"));
		assertThat("unexpected initial output value", demoPage.getOutputText().getText(), containsString("0"));
	}

}
