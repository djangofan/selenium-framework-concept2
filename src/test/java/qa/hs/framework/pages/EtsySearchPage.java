package qa.hs.framework.pages;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

import qa.hs.framework.SeBuilder;
import qa.hs.framework.WebDriverHelper;

public class EtsySearchPage {
	
	private WebDriver driver;
	private WebDriverHelper helper;
	
	private By lazyLoadedSuggestionList = By.cssSelector("div.nav-search-text div#search-suggestions ul li");
	private By searchField = By.xpath(".//*[@id='search-query']");
	private By searchButton = By.xpath(".//*[@id='search_submit']");

	public EtsySearchPage( SeBuilder se ) {
		this.driver = se.getDriver();
		this.helper = se.getHelper();
		driver.navigate().to( se.getAppUrl().toExternalForm() );
		helper.waitTimer( 1, 1000 );
		Reporter.log( "EtsySearchPage constructor loaded...", true );
	}

	public void clickSearchButton() {
		WebElement clicker = helper.getElementByLocator( searchButton );
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
	
	/**
	 * Method: clickSearchButton
	 */
	public void clickSearchField() {
		Reporter.log("Clicking search field...");
		WebElement clicker = helper.getElementByLocator( searchField );
		try {
		    clicker.click();
		} catch ( StaleElementReferenceException e ) {
			Reporter.log( "Element not visible exception clicking search button.\n" + e.getMessage() );
			e.printStackTrace();
		}
	}

	/**
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void setSearchString( String sstr ) {
		helper.clearAndType( searchField, sstr );
	}
	
	/**
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void clickEtsyLogo() {
		Reporter.log("Click Etsy logo...");
		WebElement logo = null;
		By locator = By.cssSelector( "h1#etsy a" );
		logo = helper.getElementByLocator( locator );
		logo.click();
		helper.waitTimer(2, 1000);
	}

	/**
	 * Method: selectInEtsyDropdown
	 * Selects element in dropdown using keydowns method (just for fun)
	 * as long as you typed a string first.  The thread sleeps and the 
	 * key arrow down are safe to comment out within the below block.
	 * @return	void
	 * @throws	StaleElementReferenceException
	 */
	public void selectInEtsyDropdown( String match ) {
		Reporter.log("Selecting \"" + match + "\" from Etsy dynamic dropdown.");
		List<WebElement> allSuggestions = driver.findElements( lazyLoadedSuggestionList );  
		WebElement searcher = driver.findElement( searchField );
		try {
			for ( WebElement suggestion : allSuggestions ) {
				Thread.sleep(600);
				searcher.sendKeys( Keys.ARROW_DOWN); // show effect of selecting item with keyboard arrow down
				if ( suggestion.getText().contains( match ) ) {
					suggestion.click();
					Reporter.log("Found item and clicked it.");
					Thread.sleep(2000); // just to slow it down so human eyes can see it
					break;
				}
			}
		} catch ( StaleElementReferenceException see ) {
			Reporter.log("Error while iterating dropdown list:\n" + see.getMessage() );
		} catch ( InterruptedException ie ) {
			ie.printStackTrace();
		}
		Reporter.log("Finished select in Etsy dropdown.");
	}

}

