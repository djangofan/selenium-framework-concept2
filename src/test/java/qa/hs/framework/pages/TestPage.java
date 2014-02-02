package qa.hs.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import qa.hs.framework.SeUtil;

public class TestPage {
	
	private WebDriver driver;
	private SeUtil util;
	
	public By testInputField = By.xpath(".//*[@id='textFieldTestInputControlID']");
	public By testInputButton = By.xpath(".//*[@id='textFieldTestProcessButtonID']");
	public By testOutputField = By.xpath( ".//*[@id='textFieldTestOutputControlID']");

	public TestPage( WebDriver drv ) {
		this.driver = drv;	
		util = new SeUtil( driver );
		util.waitTimer( 3, 1000 );
		Reporter.log( "TestPage constructor loaded...", true );
	}

	public void clickProcessButton() {
		WebElement clicker = util.getElementByLocator( testInputButton );
		    try {
				clicker.click();
			} catch ( ElementNotVisibleException e ) {
				Reporter.log( "Element not visible exception clicking process button.\n" + e.getMessage() );
				e.printStackTrace();
			} catch ( Exception e ) {
				Reporter.log( "Exception clicking process button.\n" + e.getMessage() );
				e.printStackTrace();
			}
	}
	
	public void clickTestInputField() {
		Reporter.log("Clicking test input field...");
		WebElement el = util.getElementByLocator( testInputField );
		try {
		    el.click();
		} catch ( StaleElementReferenceException e ) {
			Reporter.log( "Stale element exception clicking test input field.\n" + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void clickTestOutputField() {
		Reporter.log("Clicking test output field...");
		WebElement el = util.getElementByLocator( testOutputField );
		try {
		    el.click();
		} catch ( StaleElementReferenceException e ) {
			Reporter.log( "Stale element exception clicking test output field.\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	public void setInputFieldString( String sstr ) {
		util.getElementByLocator( this.driver, testInputField ).sendKeys( sstr );
	}	

}

