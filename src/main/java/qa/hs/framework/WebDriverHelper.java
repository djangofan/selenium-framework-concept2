package qa.hs.framework;

import java.text.DecimalFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class WebDriverHelper {

	protected static WebDriver wd;

	public WebDriverHelper( WebDriver driver ) {
		WebDriverHelper.wd = driver;
	}

	public void clearAndType( By field, String text ) {
		WebElement el = wd.findElement( field );
		el.submit();
		el.clear(); 
		el.sendKeys( text ); 
	}

	public WebElement getElementByLocator( final By locator ) {
		Reporter.log( "Get element by locator: " + locator.toString(), true );                
		final long startTime = System.currentTimeMillis();
		WebElement we = null;
		int tries = 0;
		while ( (System.currentTimeMillis() - startTime) < 91000 ) {
			Reporter.log( "Searching for element. Try number " + (tries++), true );  
			try {
				we = wd.findElement( locator );
				waitTimer( 5, 1000 );
				break;
			} catch ( StaleElementReferenceException e ) {                                                
				Reporter.log( "Stale element exception.", true );
			}
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		Reporter.log( "Searched for " + totalTime + " milliseconds.", true );
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
				Reporter.log( ".", true );
				x = x + 1;
			}
		} catch ( InterruptedException ex ) {
			ex.printStackTrace();
		}        
	}

	public boolean elementExists( By locator ) {
		waitTimer( 1, 500 );
		return wd.findElements( locator ).size() != 0;
	}

}
