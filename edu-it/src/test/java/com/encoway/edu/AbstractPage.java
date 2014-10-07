package com.encoway.edu;

import static org.hamcrest.Matchers.endsWith;

import com.encoway.edu.util.WebDriverPatience;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.LoadableComponent;

/**
 * Abstract page representation.
 * 
 * @param <T> type of the page
 */
public abstract class AbstractPage<T extends LoadableComponent<T>> extends LoadableComponent<T> {

    protected final WebDriver driver;

    private String path;

    /**
     * Initializes an {@link AbstractPage}.
     * 
     * @param driver used to interact with this page
     * @param path path relative to the site root
     */
    public AbstractPage(WebDriver driver, String path) {
        this.driver = driver;
        this.path = path;
    }

    @Override
    protected void load() {
        final String url = System.getProperty("test.server.url");
        driver.get(url + path);
    }

    @Override
    protected void isLoaded() throws Error {
        Assert.assertThat("page not loaded", driver.getCurrentUrl(), endsWith(path));
    }

    /**
     * Performs a click on {@code trigger} and waits.
     * 
     * @param trigger the element to perform {@link WebElement#click()} on.
     */
    protected void clickAndWait(WebElement trigger) {
        trigger.click();

        WebDriverPatience.wait(driver, 2, TimeUnit.SECONDS);
    }

}