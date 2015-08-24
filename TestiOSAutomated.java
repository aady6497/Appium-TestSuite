package com.ptw.qe;

import java.io.IOException;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TestiOSAutomated {
	public AppiumDriver driver;
	public TouchAction touch;
	WebDriverWait webDriverWait;
	DesiredCapabilities capabilities = new DesiredCapabilities();
	// String appName = "CastleMobileQEDailyBuild-1878_resigned.ipa";
	// String appName = "CEA_iOS_1.0.0.13.resigned1.ipa";
	String appName = "Empires-resigned.ipa";
	// Screen s = new Screen();
	// String appName = "Castle Mobile QE Daily Build-1886-resigned.ipa";

	// String udid = "fa1006f5bc0910e77ed8eb876798c39a9a446abf";

	PTW_Support support = new PTW_Support();

	// IPhone6Plus iphone6plus = new IPhone6Plus();

	// working on iPhone 6 without source code, only on ipa
	// Follow the steps in the link https://github.com/talk-to/resign-ipa to
	// resign an ipa

	@BeforeClass
	public void setUp() throws Exception {
		// support.openXcodeToEnableDeveloperSettings();
		driver = support.initialize(appName);
		// System.out.println(driver.manage().window().getSize());
		System.out.println(driver.executeScript("UIATarget.localTarget().frontMostApp().mainWindow().rect()"));

	}

	@AfterClass
	public void tearDown() throws Exception {
		driver.closeApp();
		driver.quit();
		support.uninstallViaShellScript(); // working
	}

	@Test
	public void testAppLodingOnDevice() throws Exception {

		System.out.println("ipa launched successfully");
		support.assertAppLoadingOnDevice();
	}

	@Test
	public void testPlatformDetailsMetadata() throws IOException {
		support.assertMetadataFile(appName);
	}

	@Test
	public void testScreenSize() throws IOException {
		support.assertScreenSize();
	}


	@Test
	public void testInfoPlistFileInIpa() throws IOException {
		support.assertPlistFile(appName);
		System.out.println(System.getProperty("user.dir"));
		String sLocation = System.getProperty("user.dir") + "/app";
		System.out.println(sLocation);

	}
	
	@Test
	public void testTutorial() {
		support.testGameplayTutorial();
	}
	
	

}
