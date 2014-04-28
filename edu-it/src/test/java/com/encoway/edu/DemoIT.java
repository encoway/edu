package com.encoway.edu;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

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
	public void thatValuesAreUpdated() {
		String url = System.getProperty("test.server.url") + "demo.xhtml";
		driver.get(url);
		assertThat("unexpected URL", driver.getCurrentUrl(), is(url));
		final DemoPage demoPage = PageFactory.initElements(driver, DemoPage.class);
		assertThat("unexpected initial input value", demoPage.getInputText().getAttribute("value"), is("Old Value"));
		assertThat("unexpected initial output value", demoPage.getOutputText().getText(), is("Old Value"));
		demoPage.updateStringValue();
		assertThat("unexpected updated input value", demoPage.getInputText().getAttribute("value"), is("New Value"));
		assertThat("unexpected updated output value", demoPage.getOutputText().getText(), is("New Value"));
	}

}
