package qa.hs.framework.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import qa.hs.framework.SeUtil;

public class MavenSearchPage {
	
	private WebDriver driver;
	private SeUtil util;
	
	public By testInputField = By.xpath(".//*[@id='query']");
	public By testInputButton = By.xpath(".//*[@id='queryButton']");
	public By resultListHyperLinks = By.xpath( ".//*[@id='artifactId']");
	
	// Advanced search fields
	public By artifactIdField = By.xpath(".//*[@id='artifactId']");
	public By asButton = By.xpath(".//*[@id='gavSearchButton']");

	public MavenSearchPage( WebDriver drv ) {
		this.driver = drv;	
		util = new SeUtil( driver );
		util.waitTimer( 3, 1000 );
		Reporter.log( "MavenSearchPage constructor loaded...", true );
	}

	public void clickSearchButton() {
		WebElement clicker = util.getElementByLocator( testInputButton );
		    try {
				clicker.click();
			} catch ( ElementNotVisibleException e ) {
				Reporter.log( "Element not visible exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			} catch ( Exception e ) {
				Reporter.log( "Exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			}
	}
	
	public void clickAdvancedSearchButton() {
		WebElement clicker = util.getElementByLocator( asButton );
		    try {
				clicker.click();
			} catch ( ElementNotVisibleException e ) {
				Reporter.log( "Element not visible exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			} catch ( Exception e ) {
				Reporter.log( "Exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			}
	}
	
	public void clickSearchField() {
		Reporter.log("Clicking test input field...");
		WebElement el = util.getElementByLocator( testInputField );
		try {
		    el.click();
		} catch ( StaleElementReferenceException e ) {
			Reporter.log( "Stale element exception clicking test input field.\n" + e.getMessage() );
			e.printStackTrace();
		}
	}
	
	public void clickArtifactField() {
		Reporter.log("Clicking advanced search artifact field...");
		WebElement el = util.getElementByLocator( artifactIdField );
		try {
		    el.click();
		} catch ( StaleElementReferenceException e ) {
			Reporter.log( "Stale element exception clicking artifact field.\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	public void setSearchFieldValue( String sstr ) {
		util.getElementByLocator( this.driver, testInputField ).sendKeys( sstr );
	}	
	
	public void setArtifactFieldValue( String sstr ) {
		util.getElementByLocator( this.driver, artifactIdField ).sendKeys( sstr );
	}

}

