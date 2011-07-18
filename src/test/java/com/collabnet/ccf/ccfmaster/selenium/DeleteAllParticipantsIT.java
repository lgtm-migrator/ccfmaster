package com.collabnet.ccf.ccfmaster.selenium;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverBackedSelenium;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.selenium.SeleneseTestBase;
import com.thoughtworks.selenium.Selenium;

public class DeleteAllParticipantsIT extends SeleneseTestBase {
//	private Selenium selenium;
	private static final Logger log = LoggerFactory.getLogger(DeleteAllParticipantsIT.class);
	static WebDriver browser;
	static Selenium selenium;

	@BeforeClass
	public static void launchBrowser() throws URISyntaxException {
		String baseUrl = System.getProperty("ccf.baseUrl","http://localhost:8080/");
		// not interested in path.
		URI uri = new URI(baseUrl);
		String port = uri.getPort() == -1 ? "" : ":" + uri.getPort();
		baseUrl = String.format("%s://%s%s/", uri.getScheme(), uri.getHost(), port);
		log.info("Launching Firefox with baseUrl: {}", baseUrl);
		browser = new FirefoxDriver();
		selenium = new WebDriverBackedSelenium(browser, baseUrl);
	}
	
	@Before
	public void login() {
		try {
			selenium.open("/CCFMaster/login");
//			selenium.click("j_username");
			selenium.type("j_username", "admin");
			selenium.type("j_password", "admin");
			selenium.click("proceed");
			selenium.waitForPageToLoad("30000");
		} catch (Exception e) {
			log.error("Error loggin in. Base64 screenshot\n:{}", selenium.captureScreenshotToString());
		}
	}

	@Test
	public void testDeleteAllParticipantsIT() throws Exception {
		selenium.open("/CCFMaster/participants");
		while(!selenium.isTextPresent("No Participant found.")) {
			selenium.click("//input[@value='Delete Participant']");
			assertTrue(selenium.getConfirmation().matches("^Are you sure want to delete this item[\\s\\S]$"));
		}
		verifyFalse(selenium.isElementPresent("//input[@value='Delete Participant']"));
	}

	@After
	public void logout() {
		selenium.open("/CCFMaster/resources/j_spring_security_logout");
	}
	
	@AfterClass
	public static void stopBrowser() throws Exception {
		log.info("Stopping browser.");
		selenium.stop();
	}
}
