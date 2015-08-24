
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

//import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;


public class TestAppium {

	private AppiumDriver driver;
	private static WebDriverWait driverWait;
	private AppiumDriver driver2;
	
	@BeforeMethod
	public void setUp() throws MalformedURLException {
		DesiredCapabilities caps = new DesiredCapabilities();
		final File appDir = new File("/Users/ptw/Desktop/TempFolder/Payload/");

		final File app = new File(appDir, "empires2.app");

		caps.setCapability("automationName", "Appium");

		caps.setCapability("platformName", "iOS");
		caps.setCapability("platformVersion", "8.0"); // Replace this with your iOS version
		//System.out.println(driver.getOrientation().toString());
		caps.setCapability("deviceName", getdeviceInfo("deviceName"));
		caps.setCapability("udid",getdeviceInfo("udid") );
		caps.setCapability("app", "settings");
		caps.setCapability("app", app.getAbsolutePath());
		//caps.setCapability("app", driver.lockScreen(1));

		//caps.setCapability("app", driver.get);
	
			
		driver = (AppiumDriver) new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);
		driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
		driverWait = new WebDriverWait(driver, 180);
	}
	
	@Test
	public void closeApp() {
	System.out.println("Inside Test");
	driver.lockScreen(0);
	//System.out.println(driver.getOrientation().toString());
	//System.out.println(driver.findElementByName("Update"));

	}
	
	@AfterMethod
	public void tearDown() {
		//driver.executeScript("UIALogger.debug(\"Inside Teardown\")");
		System.out.println("Inside after");
		driver.quit();
		//System.out.println("Inside after");
		
		//driver.executeScript("UIALogger.debug(\"\")");

	}

}
