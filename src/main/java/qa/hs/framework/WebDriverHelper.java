package qa.hs.framework;

import java.text.DecimalFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class WebDriverHelper {

	private WebDriver driver;

	public WebDriverHelper( WebDriver driver ) {
		this.driver = driver;
	}

	public void clearAndType( By field, String text ) {
		if ( elementExists( field ) ) {
		    WebElement el = driver.findElement( field );
		    el.submit();
		    el.clear(); 
		    el.sendKeys( text ); 
		} else {
			Reporter.log("Element did not exist to type text into: " + text );
		}
	}

	public WebElement getElementByLocator( By locator ) {
		Reporter.log( "Get element by locator: " + locator.toString(), true );                
		WebElement we = null;
		int tries = 0;
		while ( true ) {
			tries++;
			Reporter.log( "Searching for element. Try number: " + tries, true );  
			try {
				we = driver.findElement( locator );
				break;
			} catch ( StaleElementReferenceException sere ) {   
				if ( tries < 5 ) {
				    Reporter.log( "Ignoring a StaleElementReferenceException!", true );
				} else {
					Reporter.log( sere.getLocalizedMessage() );
				}
			} catch ( NoSuchElementException nse ) { 
				if ( tries < 5 ) {
				    Reporter.log( "Ignoring a NoSuchElementException!", true );
				} else {
					Reporter.log( nse.getLocalizedMessage() );
				}
			}
			waitTimer( 5, 1000 );
		}
		Reporter.log( "Finished getting element: " + locator, true );
		return we;
	}

	public void waitTimer( int units, int mills ) {
		DecimalFormat df = new DecimalFormat("###.##");
		double totalSeconds = ((double)units*mills)/1000;
		Reporter.log( "Explicit pause for " + df.format(totalSeconds) + " seconds divided by " + units + " units of time: ", true );
		try {
			Thread.currentThread();                
			int x = 0;
			while( x < units ) {
				Thread.sleep( mills );
				//Reporter.log( ".", true );
				x = x + 1;
			}
		} catch ( InterruptedException ex ) {
			ex.printStackTrace();
		}        
	}

	public boolean elementExists( By locator ) {
		waitTimer( 1, 500 );
		return driver.findElements( locator ).size() != 0;
	}

}
