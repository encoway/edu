package com.encoway.edu;

import com.encoway.edu.util.WebElements;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.PageFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.is;

/**
 * Demo Page Integration Test.
 */
public class DemoIT {

    private static final String OLD_VALUE = "Old Value";
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
    public void thatStringsAreUpdated() {
        final DemoPage demoPage = PageFactory.initElements(driver, DemoPage.class).get();

        assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getStringInput()), is(OLD_VALUE));
        assertThat("unexpected initial output value", demoPage.getStringOutputText().getText(), containsString(OLD_VALUE));

        demoPage.updateStringValue();

        String newValue = OLD_VALUE + updateString();

        assertThat("unexpected updated input value", WebElements.getValue(driver, demoPage.getStringInput()), is(newValue));
        assertThat("unexpected updated output value", demoPage.getStringOutputText().getText(), containsString(newValue));

        demoPage.reset();

        assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getStringInput()), is(OLD_VALUE));
        assertThat("unexpected initial output value", demoPage.getStringOutputText().getText(), containsString(OLD_VALUE));

        demoPage.updateStringValue("test");

        assertThat("unexpected manual input value", WebElements.getValue(driver, demoPage.getStringInput()), endsWith("test"));
        assertThat("unexpected manual output value", demoPage.getStringOutputText().getText(), endsWith("test"));
    }

    @Test
    public void thatIntegersAreUpdated() {
        final DemoPage demoPage = PageFactory.initElements(driver, DemoPage.class).get();

        assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getIntegerInput()), is("0"));
        assertThat("unexpected initial output value", demoPage.getIntegerOutputText().getText(), containsString("0"));

        demoPage.updateIntegerValue();

        assertThat("unexpected updated input value", WebElements.getValue(driver, demoPage.getIntegerInput()), is("1"));
        assertThat("unexpected updated output value", demoPage.getIntegerOutputText().getText(), containsString("1"));

        demoPage.reset();

        assertThat("unexpected initial input value", WebElements.getValue(driver, demoPage.getIntegerInput()), is("0"));
        assertThat("unexpected initial output value", demoPage.getIntegerOutputText().getText(), containsString("0"));

        demoPage.updateIntegerValue(2);

        assertThat("unexpected manual input value", WebElements.getValue(driver, demoPage.getIntegerInput()), endsWith("2"));
        assertThat("unexpected manual output value", demoPage.getIntegerOutputText().getText(), endsWith("2"));
    }

    private String updateString() {
        return " (upd. " + new SimpleDateFormat("hh:mm").format(new Date()) + ")";
    }

}
