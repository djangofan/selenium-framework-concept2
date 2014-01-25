package qa.hs.framework;

import java.text.DecimalFormat;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;

public class WebDriverHelper {

	protected static SeHelper se;

	public WebDriverHelper( SeHelper se ) {
		WebDriverHelper.se = se;
	}

	public static void clearAndType( WebElement field, String text ) {
		field.submit();
		field.clear(); 
		field.sendKeys( text ); 
	}

	public static WebElement getElementByLocator( final By locator ) {
		Reporter.log( "Get element by locator: " + locator.toString(), true );                
		final long startTime = System.currentTimeMillis();
		WebElement we = null;
		int tries = 0;
		while ( (System.currentTimeMillis() - startTime) < 91000 ) {
			Reporter.log( "Searching for element. Try number " + (tries++), true );  
			try {
				we = se.driver.findElement( locator );
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

	public static void waitTimer( int units, int mills ) {
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

}
