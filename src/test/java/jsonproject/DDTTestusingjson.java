package jsonproject;

import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;

public class DDTTestusingjson {
	WebDriver driver;

	@BeforeClass
	public void setup() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.MILLISECONDS);
	}

	@AfterClass
	public void tearDown() {
		driver.close();
	}

	@Test(dataProvider="dp")
	public void login(String data) {
		String users[] = data.split(",");
		
		String username = users[0];
		String password = users[1];
		driver.get("https://demo.nopcommerce.com/login");
		driver.findElement(By.id("Email")).sendKeys(username);
		driver.findElement(By.id("Password")).sendKeys(password);
//		driver.findElement(By.xpath("//input[@class-'button-1 login-button']")).click();
		Assert.assertEquals("", "");
		

	}

	// This is the data proider that reads testdata json file
	@DataProvider(name = "dp")
	public  String[] readJson() {
		// Here we are going to use simple json jar

		// We need to create JSon praser object to read json data
		JSONParser jsonParser = new JSONParser();
		FileReader reader = null;
		try {
			 reader = new FileReader(".\\jsonfiles\\testdata.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] arr = null;
		try {
			Object obj = jsonParser.parse(reader);
			
			//Cast the object to JSOn Object
			JSONObject userloginsJsonobj = (JSONObject) obj;
			//Convert to user JSON Array
			JSONArray userLoginArray = (JSONArray)userloginsJsonobj.get("userlogins");
			
			//create a java array
			arr = new String[userLoginArray.size()];
			
//			Iterate through user Jsaon array
			for (int i = 0; i < userLoginArray.size(); i++) {
				JSONObject users = (JSONObject) userLoginArray.get(i);
				String user = (String) users.get("username");
				String pwd = (String) users.get("password");
				arr[i] = user + "," + pwd;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arr;
	}
}
