package com.encoway.edu;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DemoPage extends AbstractPage<DemoPage> {

    @FindBy(xpath = "//form//input[@type='submit' and contains(@value, 'String')]")
    WebElement stringButton;

    @FindBy(xpath = "//form//input[@type='submit' and contains(@value, 'Integer')]")
    WebElement integerButton;

    @FindBy(xpath = "//form//a[contains(text(), 'Reset')]")
    WebElement resetLink;

    public DemoPage(WebDriver driver) {
        super(driver, "demo.xhtml");
    }

    public void updateStringValue() {
        clickAndWait(stringButton);
    }

    public void updateIntegerValue() {
        clickAndWait(integerButton);
    }

    public void reset() {
        clickAndWait(resetLink);
    }

    public WebElement getOutputText() {
        return driver.findElement(By.xpath("//p[preceding-sibling::h1]/span"));
    }

    public WebElement getStringInput() {
        return driver.findElement(By.xpath("//form//input[@type='text']"));
    }

    public WebElement getIntegerInput() {
        return driver.findElement(By.xpath("//form//input[@type='number']"));
    }

}
