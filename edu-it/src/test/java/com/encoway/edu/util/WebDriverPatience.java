package com.encoway.edu.util;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Functions;
import com.google.common.base.Predicates;

/**
 * 
 */
public class WebDriverPatience {
	
    public static void wait(WebDriver driver, int duration, TimeUnit timeUnit) {
        final WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        Wait<WebDriver> wait = webDriverWait.pollingEvery(duration, timeUnit);
        wait.until(Functions.forPredicate(Predicates.alwaysTrue()));
    }

}
