package com.ptw.qe;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;

public class TestiOSSikuliAppium {
	
	public static AppiumDriver driver;
	//	public TouchAction touch;

	public TouchAction touch;
	WebDriverWait webDriverWait;
	DesiredCapabilities capabilities = new DesiredCapabilities();
	String udid = "";
	File appDir = new File("app/");	
  
	PTW_Support support = new PTW_Support();
	
	@Test
  public void f() {
		System.out.println("Inside Test");
  }
 
	@BeforeClass
  public void beforeClass() throws IOException {
	  

		//initialize the appiumdriver with appName and the device UDID - added by Jeberson on 20/July/15
			//		File appDir = new File("app/");		
//			File app = new File(appDir, appName);
			capabilities.setCapability("platformVersion",support.getdeviceInfo("platformVersion"));
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("deviceName", support.getdeviceInfo("deviceName"));
			capabilities.setCapability("udid", support.getdeviceInfo("udid"));
//			capabilities.setCapability("app", app.getAbsolutePath());
//			capabilities.setCapability("automationName", "Selendroid");
			capabilities.setCapability("app", "settings");
			capabilities.setCapability("showIOSLog",true);
			driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);


			webDriverWait = new WebDriverWait(driver, 10);	
			touch = new TouchAction(driver);
  }

  @AfterClass
  public void afterClass() {
	  driver.quit();
  }

}
