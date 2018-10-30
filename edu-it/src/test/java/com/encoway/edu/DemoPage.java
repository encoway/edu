package com.encoway.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page representing the demo for EDU.
 */
public class DemoPage extends AbstractPage<DemoPage> {

    @FindBy(xpath = "//form//input[@type='submit' and contains(@value, 'String')]")
    WebElement stringButton;

    @FindBy(xpath = "//form//input[@type='submit' and contains(@value, 'Integer')]")
    WebElement integerButton;

    @FindBy(xpath = "//form//a[contains(text(), 'Reset')]")
    WebElement resetLink;

    /**
     * Initializes a {@link DemoPage}.
     * 
     * @param driver the driver used when interacting with this page
     */
    public DemoPage(WebDriver driver) {
        super(driver, "demo.xhtml");
    }

    /**
     * Clicks the "Update String" button.
     */
    public void updateStringValue() {
        clickAndWait(stringButton);
    }

    public void updateStringValue(String value) {
        getStringInput().click();
        getStringInput().sendKeys(value, Keys.TAB);
        getStringOutputText().click();
        wait(2);
    }

    /**
     * Clicks the "Update Integer" button.
     */
    public void updateIntegerValue() {
        clickAndWait(integerButton);
    }

    public void updateIntegerValue(Integer value) {
        getIntegerInput().click();
        getIntegerInput().sendKeys(value.toString(), Keys.TAB);
        getIntegerOutputText().click();
        wait(2);
    }

    /**
     * Clicks the reset link.
     */
    public void reset() {
        clickAndWait(resetLink);
    }

    public WebElement getStringOutputText() {
        return driver.findElement(By.id("hybridStringOutput"));
    }

    public WebElement getIntegerOutputText() {
        return driver.findElement(By.id("hybridIntOutput"));
    }

    public WebElement getStringInput() {
        return driver.findElement(By.xpath("//form//input[@type='text']"));
    }

    public WebElement getIntegerInput() {
        return driver.findElement(By.xpath("//form//input[@type='number']"));
    }

}
