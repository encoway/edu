package com.encoway.edu.util;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

/**
 * 
 */
public class WebDriverPatience {
	
	public static void wait(WebDriver driver, int duration, TimeUnit timeUnit) {
        final WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        Wait<WebDriver> wait = webDriverWait.pollingEvery(duration, timeUnit);
        wait.until(new AskMeAgain(1));
    }

	private static class AskMeAgain implements Function<WebDriver, Boolean> {
		
		private int invokationsUntilTrue;
	
		public AskMeAgain(int invokationsUntilTrue) {
			this.invokationsUntilTrue = invokationsUntilTrue;
		}
	
		@Override
		public Boolean apply(WebDriver input) {
			return invokationsUntilTrue-- <= 0;
		}
		
	}

}
