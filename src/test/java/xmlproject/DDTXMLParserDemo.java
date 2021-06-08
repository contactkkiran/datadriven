package xmlproject;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class DDTXMLParserDemo {

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

	@Test(dataProvider="dp1")
	public void login(String data) {
		String users[] = data.split(",");		
		String username = users[0];
		String password = users[1];
		driver.get("https://demo.nopcommerce.com/login");
		driver.findElement(By.id("Email")).sendKeys(username);
		driver.findElement(By.id("Password")).sendKeys(password);
		driver.findElement(By.xpath("//input[@class-'button-1 login-button']")).click();
		Assert.assertEquals("nopCommerce demo store", driver.getTitle());
		

	}
	// This is the data proider that reads testdata json file

    @DataProvider(name = "dp1")
	public  String[] readxml() {
		String[] arr = null;
		try {
			File inputFile = new File(".\\testng.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			NodeList nList = doc.getElementsByTagName("userlogin");
			System.out.println("Total Students nodes found: " + nList.getLength());
			// create a java array
			arr = new String[nList.getLength()];
			for (int i = 0; i < nList.getLength(); i++) {
				Node nNode = nList.item(i);
				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					System.out.println("Student roll no : " + eElement.getAttribute("rollno"));
					System.out.println(
							"username : " + eElement.getElementsByTagName("username").item(0).getTextContent());
					String user = eElement.getElementsByTagName("username").item(0).getTextContent();
					System.out.println(
							"password : " + eElement.getElementsByTagName("password").item(0).getTextContent());
					String pwd= eElement.getElementsByTagName("password").item(0).getTextContent();
					arr[i] = user + "," + pwd;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arr;
	}
}