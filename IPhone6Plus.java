package com.ptw.qe;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import io.appium.java_client.AppiumDriver;


public class IPhone6Plus {
	
	PTW_Support support = new PTW_Support();
	AppiumDriver driver = support.getDriver();
	
	
	public void setAlarm() {
		System.out.println("SetAlarm");
//		driver.tap(1, 34, 60, 500);
//		touch.press(702, 33).perform();
		support.swipeFromBottomLandscape();
		driver.tap(1, support.getScreenWidth() - 60, support.getScreenHeight()/2 -20  , 500);
	}
	
	
	//assert ScreenSize - added by Jeberson on 22/July/15
	public void assertScreenSize() throws IOException {
		int height = support.getScreenHeight();
		System.out.println(height);
		int width = support.getScreenWidth();
		System.out.println(width);
		assert height == 414;
		assert width == 736;
	}


}
