package com.ptw.qe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.TouchAction;
import io.appium.java_client.ios.IOSDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.ScreenOrientation;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

public class PTW_Support {

	public static AppiumDriver driver;
	//	public TouchAction touch;

	public TouchAction touch;
	WebDriverWait webDriverWait;
	DesiredCapabilities capabilities = new DesiredCapabilities();
	String udid = "";
	File appDir = new File("app/");	

	//initialize the appiumdriver with appName and the device UDID - added by Jeberson on 20/July/15
	public AppiumDriver initialize(String appName) throws IOException {
		//		File appDir = new File("app/");		
		File app = new File(appDir, appName);
		capabilities.setCapability("platformVersion", getdeviceInfo("platformVersion"));
		capabilities.setCapability("platformName", "iOS");
		capabilities.setCapability("deviceName", getdeviceInfo("deviceName"));
		capabilities.setCapability("udid", getdeviceInfo("udid"));
		capabilities.setCapability("app", app.getAbsolutePath());
//		capabilities.setCapability("automationName", "Selendroid");
		capabilities.setCapability("showIOSLog",true);
		assertAppSize(appName);
		driver = new IOSDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);


		webDriverWait = new WebDriverWait(driver, 10);	
		touch = new TouchAction(driver);
		return driver;

	}

	public AppiumDriver getDriver(){
		return driver;
	}

	//assert the size of App to be less than 100MB - added by Jeberson on 20/July/15
	public void assertAppSize(String appName) {	
		File file = new File("app/"+appName);
		if (!file.exists() || !file.isFile()) {
			System.out.println("File doesn\'t exist");
		}
		long fileSize = file.length();
		//		System.out.println(fileSize);

		assert fileSize <= 100000000;
	}


	//get the device UDID - added bu Jeberson on 16/July/2015
	public String getUDID() throws IOException {
		Process proc = Runtime.getRuntime().exec("instruments -s devices");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTerminalOutput = "";
		String sTemp = null;
		while ((sTemp = stdInput.readLine()) != null) {
			if(!sTemp.contains("Simulator") && !sTemp.contains("iMac")) {
				//				System.out.println(sTemp);
				sTerminalOutput = sTemp;
			}

		}

		sTerminalOutput = sTerminalOutput.replaceAll("[\\[\\]]", "");
		String sUDID = sTerminalOutput.split("\\) ")[1];
		//		System.out.println(sUDID);
		return sUDID;
	}



	//	public void launchTime() {
	//		Date d = new Date();
	//		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
	//	}


	//get the deviceInfo like UDID, deviceName and platformVersion - added by Jeberson on 20/July/15
	public String getdeviceInfo(String sParam) throws IOException {
		Process proc = Runtime.getRuntime().exec("instruments -s devices");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTerminalOutput = "";
		String sTemp = null;
		while ((sTemp = stdInput.readLine()) != null) {
			if(!sTemp.contains("Simulator") && !sTemp.contains("iMac")) {
				System.out.println(sTemp);
				sTerminalOutput = sTemp;
			}

		}

		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(proc.getErrorStream()));

		System.out.println("Here is the standard error of the command (if any):\n");
		while ((sTemp = stdError.readLine()) != null) {
			//			System.out.println(sTemp);
			sTerminalOutput = sTemp;
		}

		sTerminalOutput = sTerminalOutput.replaceAll("[\\[\\]]", "");


		if(sParam.contains("deviceName")) {
			//			System.out.println(sTerminalOutput.split("\\(")[0].trim());
			return sTerminalOutput.split("\\(")[0].trim();
		}

		if(sParam.contains("udid")) {
			//			System.out.println(sTerminalOutput.split("\\) ")[1]);
			return sTerminalOutput.split("\\) ")[1];
		}

		if(sParam.contains("platformVersion")) {
			//			System.out.println(sTerminalOutput.split("\\(")[1].split("\\)")[0]);
			//			return sTerminalOutput.split("\\(")[1].split("\\)")[0];
			proc = Runtime.getRuntime().exec("/usr/local/bin/ideviceinfo");
			stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			while ((sTemp = stdInput.readLine()) != null) {
				if(sTemp.contains("ProductVersion")) {
					System.out.println(sTemp);
					sTerminalOutput = sTemp;
				}
			}
			return sTerminalOutput.split(": ")[1];
		}

		return null;
	}


	//uninstall the app using ideviceInstaller - added by Jeberson on 20/July/15
	public void uninstallViaIdeviceInstaller() throws IOException {
		udid = getdeviceInfo("udid");
		System.out.println(udid);
		//		System.out.println("/usr/local/bin/ideviceinstaller -u \""+udid+"\" --uninstall \"*\"");
		System.out.println("/usr/local/bin/ideviceinstaller --udid \""+udid+"\" --uninstall \"*\"");
		//		Runtime.getRuntime().exec("cd /usr/local/bin/ideviceinstaller");
		//		Process proc = Runtime.getRuntime().exec("/usr/local/bin/ideviceinstaller -u \""+udid+"\" --uninstall \"*\"");
		Process proc = Runtime.getRuntime().exec("/usr/local/bin/ideviceinstaller --udid \""+udid+"\" --uninstall \"*\"");
		//		Process proc = Runtime.getRuntime().exec("/usr/local/bin/ideviceinstaller --uninstall \"*\"");

		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTemp = null;
		while ((sTemp = stdInput.readLine()) != null) {
			System.out.println(sTemp);
		}

		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(proc.getErrorStream()));


		while ((sTemp = stdError.readLine()) != null) {
			//			System.out.println("Here is the standard error of the command (if any):\n");
			System.out.println("Here is the standard error of the command (if any):\n"+sTemp);
		}

	}



	//uninstall an app through shell script - using this as Appium fails to uninstall the app - added by Jeberson on 20/July/15
	public void uninstallViaShellScript() throws IOException {
		//		if(driver.isAppInstalled("*")) {
		Runtime.getRuntime().exec("chmod go+rx /Users/qe/Desktop/uninstallApp.sh");

		//		Runtime.getRuntime().exec("cd /Users/qe/Desktop");
		Process proc = Runtime.getRuntime().exec("sh uninstallApp.sh");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTemp = null;
		while ((sTemp = stdInput.readLine()) != null) {
			System.out.println(sTemp);
		}

		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(proc.getErrorStream()));


		while ((sTemp = stdError.readLine()) != null) {
			//				System.out.println("Here is the standard error of the command (if any):\n");
			System.out.println("Here is the standard error of the command (if any):\n"+sTemp);
		}

	}
	//	}


	//get the connected device info(iPhone/iPad/iPod) - added by Jeberson on 20/July/15
	public String getConnectedDeviceInfo() throws IOException {
		Process proc = Runtime.getRuntime().exec("system_profiler SPUSBDataType");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTerminalOutput = "";
		String sTemp = null;
		while ((sTemp = stdInput.readLine()) != null) {
			if(sTemp.contains("iPhone") || sTemp.contains("iPad") || sTemp.contains("iPod"))
				sTerminalOutput = sTemp;
		}

		//		System.out.println(sTerminalOutput.trim().replace(":", ""));
		String sDevice = sTerminalOutput.trim().replace(":", "");
		return sDevice;

	}


	//assert app can be loaded on device successfully - added by Jeberson on 20/July/15
	public void assertAppLoadingOnDevice() throws IOException {
		String sDevice = getConnectedDeviceInfo();
		assert driver.isAppInstalled("*") == true;
		assert driver.findElements(By.xpath("//UIAApplication[1]/UIAWindow[1]")).size() > 0;
		System.out.println("App Loaded successfully on "+sDevice);

	}

	//assert the texts beta, demo, trial or test is not displayed on loading screen - added by Jeberson on 21/July/15
	//assert no placeholder or debug text displayed on screen - added by Jeberson on 21/July/15
	public void assertDemoTextsNotDisplayedOnLoadingScreen() {
		assert driver.findElements(By.xpath("//*[text()='beta']")).size() == 0;
		assert driver.findElements(By.xpath("//*[text()='demo']")).size() == 0;
		assert driver.findElements(By.xpath("//*[text()='trial']")).size() == 0;
		assert driver.findElements(By.xpath("//*[text()='test']")).size() == 0;
		assert driver.findElements(By.xpath("//*[text()='debug']")).size() == 0;
		assert driver.findElements(By.xpath("//*[text()='Placeholder']")).size() == 0;
		assert driver.findElements(By.xpath("//*[text()='{}']")).size() == 0;


	}

	//include this if Developer Settings is not displayed - added by Jeberson on 21/July/15
	public void openXcodeToEnableDeveloperSettings() throws IOException {
		Process proc = Runtime.getRuntime().exec("ps aux | grep Xcode");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTemp = null;
		while ((sTemp = stdInput.readLine()) != null) {
			if(sTemp.contains("Xcode"))
				Runtime.getRuntime().exec("killall Xcode");	
		}

		Runtime.getRuntime().exec("open -a Xcode");
	}


	//turnOff Wifi on iPhone 6+ - added by Jeberson on 21/July/15
	public void turnOffWifiLandscape() {
		//		if(driver.findElements(By.xpath("//UIAElement[contains(@name, 'Wi-Fi')]")).size() > 0) {
		//			System.out.println(driver.manage().window().getSize());
		//			int height = driver.manage().window().getSize().height;
		//			int width = driver.manage().window().getSize().width;
		//		driver.swipe(500, 410, 500, 300, 500);
		//			driver.swipe(width/2, height, width/2, height/2, 500);
		//			driver.swipe(width/2, height, width/2, 0, 500);
		if(driver.findElements(By.xpath("//UIAElement[contains(@name, 'Wi-Fi')]")).size() == 0) {
			System.out.println("Not Connected to WiFi");
		}
		else {
			swipeFromBottomLandscape();
			driver.tap(1, 61, 133, 500);
			//			driver.swipe(width/2, 0, width/2, height, 500);
			swipeFromTopLandscape();
		}
		//		}
	}

	//turn On Airplane Mode works on iPhone 6+ - added by Jeberson on 21/July/15
	public void turnOnAirplaneModeLandscape() {
		swipeFromBottomLandscape();
		driver.tap(1, 60, 64, 500);
		swipeFromTopLandscape();

	}

	//get the Screen Height - added by Jeberson on 21/July/15
	public int  getScreenHeight() {
		return driver.manage().window().getSize().height;
	}

	//get the Screen Width - added by Jeberson on 21/July/15
	public int getScreenWidth() {
		return driver.manage().window().getSize().width;
	}

	//swipe From Bottom works on iphone 6+ - added by Jeberson on 21/July/15
	public void swipeFromBottomLandscape() {
		int height = getScreenHeight();
		int width = getScreenWidth();
		driver.swipe(width/2, height, width/2, height/2, 500);
		driver.swipe(width/2, height, width/2, 0, 500);
	}

	//swipe From Top works on iphone 6+ - added by Jeberson on 21/July/15
	public void swipeFromTopLandscape() {
		int height = getScreenHeight();
		int width = getScreenWidth();
		driver.swipe(width/2, 0, width/2, height, 500);
	}

	//assert Info.plist file is present in ipa file - added by Jeberson on 21/July/15
	public void assertPlistFile(String appName) throws IOException {
		System.out.println("unzip "+appDir+"/"+appName+"");
		String sLocation = System.getProperty("user.dir");
		System.out.println("unzip -o "+appDir+"/"+appName+" -d "+sLocation+"");
		Process proc = Runtime.getRuntime().exec("unzip -o "+appDir+"/"+appName+" -d "+sLocation+"");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTemp = null;
		String sTerminalOutput = "";
		while ((sTemp = stdInput.readLine()) != null) {
			//			System.out.println(sTemp);
			//			if(sTemp.contains("Info.plist"))
			//				Runtime.getRuntime().exec("killall Xcode");
			//			assert sTemp.contains("Info.plist") == true;
			if(sTemp.contains("Info.plist")){
				System.out.println(sTemp);
				sTerminalOutput = sTemp.trim();
				break;
			}
		}
		System.out.println(sTerminalOutput.split(": ")[1]);
		File file = new File(sTerminalOutput.split(": ")[1]);
		FileReader fr = new FileReader(file); 
		BufferedReader br = new BufferedReader(fr); 
		String s; 
		FileWriter fileWriter = new FileWriter(sLocation+"/iOS_guidelines.txt");
		while((s = br.readLine()) != null) { 
			if(s.contains("<key>CFBundleIconFiles</key>") || s.contains("<array>") || s.contains("<string>icon_57x57.png</string>") || s.contains("<string>icon_72x72.png</string>") || s.contains("<string>icon_76x76.png</string>")
					|| s.contains("<string>icon_114x114.png</string>") || s.contains("<string>icon_120x120.png</string>") || s.contains("<string>icon_144x144.png</string>") || s.contains("<string>icon_152x152.png</string>")
					|| s.contains("</array>")) {
				fileWriter.write(s);

			}
		}
		fr.close(); 
		fileWriter.close();

		file = new File(sTerminalOutput.split(": ")[1]);
		fr = new FileReader(file); 
		br = new BufferedReader(fr); 
		while((s = br.readLine()) != null){
			if(s.contains("CFBundleIconFiles"))
				Assert.assertEquals(s.trim(), "<key>CFBundleIconFiles</key>"); 
			if(s.contains("<array>"))
				Assert.assertEquals(s.trim(), "<array>");
			if(s.contains("<string>icon_57x57.png</string>"))
				Assert.assertEquals(s.trim(), "<string>icon_57x57.png</string>");
			if(s.contains("<string>icon_72x72.png</string>"))
				Assert.assertEquals(s.trim(), "<string>icon_72x72.png</string>");
			if(s.contains("<string>icon_76x76.png</string>"))
				Assert.assertEquals(s.trim(), "<string>icon_76x76.png</string>");
			if(s.contains("<string>icon_114x114.png</string>"))
				Assert.assertEquals(s.trim(), "<string>icon_114x114.png</string>");
			if(s.contains("<string>icon_120x120.png</string>"))
				Assert.assertEquals(s.trim(), "<string>icon_120x120.png</string>");
			if(s.contains("<string>icon_144x144.png</string>"))
				Assert.assertEquals(s.trim(), "<string>icon_144x144.png</string>");
			if(s.contains("144"))
				Assert.assertEquals(s.trim(), "<string>icon_152x152.png</string>");
			if(s.contains("</array>"))
				Assert.assertEquals(s.trim(), "</array>");
			else
				Assert.fail("Info.plist file is not as expected");
		}


		//			System.out.println(s); 
		//			if(s.contains("CFBundleIconFiles"))
		//				Assert.assertEquals(s.trim(), "<key>CFBundleIconFiles</key>"); 
		//			if(s.contains("<array>"))
		//				Assert.assertEquals(s.trim(), "<array>");
		//			if(s.contains("<string>icon_57x57.png</string>"))
		//				Assert.assertEquals(s.trim(), "<string>icon_57x57.png</string>");
		//			if(s.contains("<string>icon_72x72.png</string>"))
		//				Assert.assertEquals(s.trim(), "<string>icon_72x72.png</string>");
		//			if(s.contains("<string>icon_76x76.png</string>"))
		//				Assert.assertEquals(s.trim(), "<string>icon_76x76.png</string>");
		//			if(s.contains("<string>icon_114x114.png</string>"))
		//				Assert.assertEquals(s.trim(), "<string>icon_114x114.png</string>");
		//			if(s.contains("<string>icon_120x120.png</string>"))
		//				Assert.assertEquals(s.trim(), "<string>icon_120x120.png</string>");
		//			if(s.contains("<string>icon_144x144.png</string>"))
		//				Assert.assertEquals(s.trim(), "<string>icon_144x144.png</string>");
		//			if(s.contains("144"))
		//				Assert.assertEquals(s.trim(), "<string>icon_152x152.png</string>");
		//			if(s.contains("</array>"))
		//				Assert.assertEquals(s.trim(), "</array>");
		//			
		//			}



		//				int i = 0;
		//				fileWriter.write(s);

		//				String sLine = "";
		//				while(((sLine = br.readLine()) != null)) {
		//                    System.out.println(sLine);
		//                    fileWriter.write(sLine);
		//                    i++;
		//                }   
		//			}






		//		fileWriter.close();



		BufferedReader stdError = new BufferedReader(new 
				InputStreamReader(proc.getErrorStream()));


		while ((sTemp = stdError.readLine()) != null) {
			//			System.out.println("Here is the standard error of the command (if any):\n");
			System.out.println("Here is the standard error of the command (if any):\n"+sTemp);
		}


	}

	//assert ScreenSize for iPhone 6Plus
	public void assertScreenSize() {
		int height = getScreenHeight();
		System.out.println(height);
		int width = getScreenWidth();
		System.out.println(width);
		assert height == 414;
		assert width == 736;
	}


	//assert metadata files doesnt has texts Google, Kindle, Windows - added by Jeberson on 22/July/15
	public void assertMetadataFile(String appName) throws IOException {
		System.out.println("unzip "+appDir+"/"+appName+"");
		String sLocation = System.getProperty("user.dir");
		System.out.println("unzip -o "+appDir+"/"+appName+" -d "+sLocation+"");
		Process proc = Runtime.getRuntime().exec("unzip -o "+appDir+"/"+appName+" -d "+sLocation+"");
		BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));

		String sTemp = null;
		String sTerminalOutput = "";
		while ((sTemp = stdInput.readLine()) != null) {
			if(sTemp.contains("Metadata.plist")){
				System.out.println(sTemp);
				sTerminalOutput = sTemp.trim();
				break;
			}
			//			assert !sTemp.contains("Google");
			//			assert !sTemp.contains("Nexus");
			//			assert !sTemp.contains("Google");
		}

		File file = new File(sTerminalOutput.split(": ")[1]);
		FileReader fileReader = new FileReader(file); 
		BufferedReader br = new BufferedReader(fileReader); 
		String sLine; 
		//		FileWriter fileWriter = new FileWriter(sLocation+"/iOS_guidelines.txt");
		while((sLine = br.readLine()) != null) { 
			//			System.out.println(s); 
			assert !sLine.contains("Google");
			assert !sLine.contains("Kindle");
			assert !sLine.contains("Windows");
		}
	}

	//setAlarm to interrupt application - added by Jeberson on 28/July/15
	public void setAlarmLanscape() throws ParseException {
		System.out.println("SetAlarm");
		//		driver.tap(1, 34, 60, 500);
		//		touch.press(702, 33).perform();

		swipeFromBottomLandscape();
		driver.tap(1, getScreenWidth() - 60, getScreenHeight()/2 -20  , 500);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("Alarm")));
		//		driver.tap(1, 312, getScreenHeight() - 30, 500);
		driver.tap(1, getScreenWidth()/2 -50, getScreenHeight() - 30, 500);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//UIANavigationBar[@name='Alarm']/UIAButton[@name='Add']")));
		driver.tap(1, getScreenWidth() - 30, getScreenHeight() - 391, 500);
		
//		driver.tap(1, getScreenWidth() - 30, getScreenHeight() - 30, 500);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//UIANavigationBar[@name='Add Alarm']/UIAButton[@name='Save']")));
		//		driver.findElement(By.xpath("//UIANavigationBar[@name='Add Alarm']/UIAButton[@name='Save']")).click();
		String sHour = driver.findElement(By.xpath("//UIAPicker[1]/UIAPickerWheel[1]")).getAttribute("value");
		sHour = sHour.split(" o")[0];
		System.out.println(sHour);

		String sMinutes = driver.findElement(By.xpath("//UIAPicker[1]/UIAPickerWheel[2]")).getAttribute("value");
		sMinutes = sMinutes.split(" ")[0];
		System.out.println(sMinutes);

		String sMeridiem = driver.findElement(By.xpath("//UIAPicker[1]/UIAPickerWheel[3]")).getAttribute("value").trim();

		String sTime = sHour + ":" + sMinutes + " " + sMeridiem;
		System.out.println(sTime);

		SimpleDateFormat ft = new SimpleDateFormat("h:mm a");
		Date date = ft.parse(sTime);
		System.out.println(ft.format(date));
		Date afterAddingTwoMinutes = new Date(date.getTime() + (2 * 60000));
		String sAfterAddingTwoMinutes = ft.format(afterAddingTwoMinutes);
		System.out.println(sAfterAddingTwoMinutes);

		String sHourAfterAddingTwoMinutes = sAfterAddingTwoMinutes.split(":")[0];
		System.out.println(sHourAfterAddingTwoMinutes);
		if(!sHourAfterAddingTwoMinutes.equals(sHour))
			driver.swipe(getScreenWidth()/2 - 70 , getScreenHeight()/2 - 60, getScreenWidth()/2 - 70, getScreenHeight()/2 - 110, 500);

		driver.swipe(getScreenWidth()/2 + 3 , getScreenHeight()/2 - 30, getScreenWidth()/2 + 3, getScreenHeight()/2 - 95, 500);

		String sMeridiemAfterAddingTwoMinutes = sAfterAddingTwoMinutes.split(" ")[1].toLowerCase();
		System.out.println(sMeridiemAfterAddingTwoMinutes);

		if(!sMeridiemAfterAddingTwoMinutes.equals(sMeridiem) && sMeridiem.equals("am"))
			driver.swipe(getScreenWidth()/2 + 58 , getScreenHeight()/2 - 60, getScreenWidth()/2 + 58, getScreenHeight()/2 - 110, 500);

		if(!sMeridiemAfterAddingTwoMinutes.equals(sMeridiem) && sMeridiem.equals("pm"))
			driver.swipe(getScreenWidth()/2 + 58 , getScreenHeight()/2 - 110, getScreenWidth()/2 + 58, getScreenHeight()/2 - 60, 500);

		driver.tap(1, getScreenWidth()/2 + 173, getScreenHeight()/2 - 163, 500);
		
		driver.closeApp();
		driver.launchApp();
		webDriverWait = new WebDriverWait(driver, 120);	
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//UIAAlert[@name='Alarm']")));
		
		driver.tap(1, getScreenWidth()/2 + 60, getScreenHeight()/2 + 30, 500);
		
//		driver.tap(1, 30, 30, 500);
//		driver.tap(1, getScreenWidth()/2, getScreenHeight() - 130, 500);

		//		driver.swipe(getScreenWidth()/2 + 3 , getScreenHeight()/2 - 60, getScreenWidth()/2 + 3, getScreenHeight()/2 - 110, 500);

		//		driver.findElement(By.name("Alarm")).click();
		//		driver.findElementByName("Alarm").click();
		//		driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATabBar[1]/UIAButton[2]")).click();
//		TouchAction touch = new TouchAction(driver).press(getScreenWidth()/2, getScreenHeight() - 130).perform().release().press(0,0).perform();
	}
	
	
	public void setAlarmPotrait() throws ParseException {
		System.out.println("SetAlarm");
		//		driver.tap(1, 34, 60, 500);
		//		touch.press(702, 33).perform();

		swipeFromBottomLandscape();
		driver.tap(1, getScreenWidth() - 60, getScreenHeight()/2 -20  , 500);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.name("Alarm")));
		//		driver.tap(1, 312, getScreenHeight() - 30, 500);
		driver.tap(1, getScreenWidth()/2 -50, getScreenHeight() - 30, 500);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//UIANavigationBar[@name='Alarm']/UIAButton[@name='Add']")));
//		driver.tap(1, getScreenWidth() - 30, getScreenHeight() - 391, 500);
		driver.tap(1, getScreenWidth() - 30, 30, 500);
		driver.rotate(ScreenOrientation.LANDSCAPE);
		webDriverWait.until(ExpectedConditions.elementToBeClickable(By.xpath("//UIANavigationBar[@name='Add Alarm']/UIAButton[@name='Save']")));
		//		driver.findElement(By.xpath("//UIANavigationBar[@name='Add Alarm']/UIAButton[@name='Save']")).click();
		String sHour = driver.findElement(By.xpath("//UIAPicker[1]/UIAPickerWheel[1]")).getAttribute("value");
		sHour = sHour.split(" o")[0];
		System.out.println(sHour);

		String sMinutes = driver.findElement(By.xpath("//UIAPicker[1]/UIAPickerWheel[2]")).getAttribute("value");
		sMinutes = sMinutes.split(" ")[0];
		System.out.println(sMinutes);

		String sMeridiem = driver.findElement(By.xpath("//UIAPicker[1]/UIAPickerWheel[3]")).getAttribute("value").trim();

		String sTime = sHour + ":" + sMinutes + " " + sMeridiem;
		System.out.println(sTime);

		SimpleDateFormat ft = new SimpleDateFormat("h:mm a");
		Date date = ft.parse(sTime);
		System.out.println(ft.format(date));
		Date afterAddingTwoMinutes = new Date(date.getTime() + (2 * 60000));
		String sAfterAddingTwoMinutes = ft.format(afterAddingTwoMinutes);
		System.out.println(sAfterAddingTwoMinutes);

		String sHourAfterAddingTwoMinutes = sAfterAddingTwoMinutes.split(":")[0];
		System.out.println(sHourAfterAddingTwoMinutes);
		if(!sHourAfterAddingTwoMinutes.equals(sHour))
			driver.swipe(getScreenWidth()/2 - 70 , getScreenHeight()/2 - 60, getScreenWidth()/2 - 70, getScreenHeight()/2 - 110, 500);

		//driver.swipe(getScreenWidth()/2 + 10 , getScreenHeight()/2, getScreenWidth()/2, getScreenHeight()/2 + 30, 500);
		driver.swipe(getScreenWidth()/2 + 10 , getScreenHeight()/2 + 10, getScreenWidth()/2, getScreenHeight()/2 - 50, 500);
		

		String sMeridiemAfterAddingTwoMinutes = sAfterAddingTwoMinutes.split(" ")[1].toLowerCase();
		System.out.println(sMeridiemAfterAddingTwoMinutes);

		if(!sMeridiemAfterAddingTwoMinutes.equals(sMeridiem) && sMeridiem.equals("am"))
			driver.swipe(getScreenWidth()/2 + 58 , getScreenHeight()/2 - 60, getScreenWidth()/2 + 58, getScreenHeight()/2 - 110, 500);

		if(!sMeridiemAfterAddingTwoMinutes.equals(sMeridiem) && sMeridiem.equals("pm"))
			driver.swipe(getScreenWidth()/2 + 58 , getScreenHeight()/2 - 110, getScreenWidth()/2 + 58, getScreenHeight()/2 - 60, 500);

		driver.tap(1, getScreenWidth()/2 + 173, getScreenHeight()/2 - 163, 500);
		
		driver.closeApp();
		driver.launchApp();
		webDriverWait = new WebDriverWait(driver, 120);	
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//UIAAlert[@name='Alarm']")));
		
		driver.tap(1, getScreenWidth()/2 + 60, getScreenHeight()/2 + 30, 500);
		
//		driver.tap(1, 30, 30, 500);
//		driver.tap(1, getScreenWidth()/2, getScreenHeight() - 130, 500);

		//		driver.swipe(getScreenWidth()/2 + 3 , getScreenHeight()/2 - 60, getScreenWidth()/2 + 3, getScreenHeight()/2 - 110, 500);

		//		driver.findElement(By.name("Alarm")).click();
		//		driver.findElementByName("Alarm").click();
		//		driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]/UIATabBar[1]/UIAButton[2]")).click();
//		TouchAction touch = new TouchAction(driver).press(getScreenWidth()/2, getScreenHeight() - 130).perform().release().press(0,0).perform();
	}
	
	//put the phone to sleep and assert the screen on wake - added by Sampath on 22/July/15
	public void sleepInterrupt() throws InterruptedException {
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//UIAApplication[1]/UIAWindow[1]")));
		driver.lockScreen(3);
		Assert.assertTrue(driver.findElement(By.xpath("//UIAApplication[1]/UIAWindow[1]")).isDisplayed());

	}
	
	//assert the time taken to load the app - added by Sampath on 22/July/15
	public void initialLaunchTime() {
		long start = System.currentTimeMillis();
		// String source_main = driver.getPageSource();
		webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//UIAApplication[1]/UIAWindow[1]")));
		long end = System.currentTimeMillis();
		long diff = end - start;
		int seconds = (int) ((diff / 1000) % 60);
		System.out.println("Time taken by the app to open in MilliSeconds "+ diff);
		System.out.println("Time taken by the app to open in Seconds "+ seconds);

	}
	
	
	//test gameplay Tutorial
	public void testGameplayTutorial() {
		System.out.println("Gameplay Tutorial");
	}
	
	
	


}
