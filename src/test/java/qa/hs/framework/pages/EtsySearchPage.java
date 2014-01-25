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
import static qa.hs.framework.WebDriverHelper.*;

public class EtsySearchPage extends LoadableComponent<EtsySearchPage> {

	public static final String searchFieldName = "search_query";
	public static final String searchButtonName = "search_submit";
	public static final String suggestIons = "div.nav-search-text div#search-suggestions ul li";
	private SeHelper se;

	@FindBy(name = searchFieldName ) public WebElement searchField;
	@FindBy(name = searchButtonName ) public WebElement searchButton;

	public EtsySearchPage( SeHelper se ) {
		this.se = se;
		se.loadNewBrowser();
		this.get(); // SlowLoadableComponent.get()
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
		if ( !(searchField == null ) ) {
			try {
				if ( searchField.isDisplayed() ) {
					loaded = true;
				}
			} catch ( ElementNotVisibleException e ) {
				Reporter.log( "Element may not be displayed yet.", true );
			}
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
		PageFactory.initElements( se.driver, this ); // initialize WebElements on page
		//sleep( 1000 );
	}

	public void clickSearchButton() {
		if ( searchButton == null ) {
			searchButton = getElementByLocator( By.id( searchButtonName ) );
		} else {
		    try {
				searchButton.click();
			} catch ( ElementNotVisibleException e ) {
				Reporter.log( "Element not visible exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			} catch ( Exception e ) {
				Reporter.log( "Exception clicking search button.\n" + e.getMessage() );
				e.printStackTrace();
			}
		}
	}

	/**
	 * Because of how the page object is initialized, we are using getAttribute here
	 * @param	sstr
	 * @return	void
	 */
	public void setSearchString( String sstr ) {
		clearAndType( searchField, sstr );
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
		logo = getElementByLocator( locator );
		logo.click();
		waitTimer(2, 1000);
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
		List<WebElement> allSuggestions = se.driver.findElements( By.cssSelector( suggestIons ) );  
		try {
			for ( WebElement suggestion : allSuggestions ) {
				Thread.sleep(600);
				searchField.sendKeys( Keys.ARROW_DOWN); // show effect of selecting item with keyboard arrow down
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

