package qa.hs.framework.pages;

import java.util.List;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.LoadableComponent;
import org.testng.Reporter;

import qa.hs.framework.SeHelper;

public class EtsySearchPage extends LoadableComponent<EtsySearchPage> {
	
	public static final By lazyLoadedSuggestionList = By.cssSelector("div.nav-search-text div#search-suggestions ul li");
	public static final By searchField = By.xpath(".//*[@id='search-query']");
	public static final By searchButton = By.xpath(".//*[@id='search_submit']");
	private SeHelper se;

	public EtsySearchPage( SeHelper se ) {
		this.se = se;
		this.get();
		Reporter.log( "EtsySearchPage constructor loaded...", true );
	}
	
	/**
	 * Method: isLoaded()
	 * Overidden method from the LoadableComponent class.
	 * This method must contain an Assert on visibility of an element in order
	 *  to trigger another call of load() if element is not found.
	 * @return	void
	 * @throws	null
	 */
	@Override
	protected void isLoaded() throws Error {    	
		Reporter.log( "EtsySearchPage.isLoaded()...", true );
		boolean loaded = false;
			try {
				if ( se.helper.elementExists( searchField ) ) {
					loaded = true;
				}
			} catch ( ElementNotVisibleException e ) {
				Reporter.log( "Element may not be displayed yet.", true );
			}
		Assert.assertTrue( "Etsy search field is not yet displayed.", loaded );
	}

	/**
	 * Method: load
	 * Overidden method from the LoadableComponent class.
	 * @return	void
	 * @throws	null
	 */
	@Override
	protected void load() {
		Reporter.log("EtsySearchPage.load()...", true );
		se.driver.navigate().to( se.getAppUrl() );
		se.helper.waitTimer( 1, 1000 );
	}

	@SuppressWarnings("null")
	public void clickSearchButton() {
		WebElement clicker = se.helper.getElementByLocator( searchButton );
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
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void setSearchString( String sstr ) {
		se.helper.clearAndType( searchField, sstr );
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
		logo = se.helper.getElementByLocator( locator );
		logo.click();
		se.helper.waitTimer(2, 1000);
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
		List<WebElement> allSuggestions = se.driver.findElements( lazyLoadedSuggestionList );  
		WebElement searcher = se.driver.findElement( searchField );
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

