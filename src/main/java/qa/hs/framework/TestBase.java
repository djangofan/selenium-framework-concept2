package qa.hs.framework;

import java.io.File;

import org.openqa.selenium.WebDriver;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;

public abstract class TestBase {
	
	protected WebDriver driver;
	protected SeUtil util;

	public static final String directory = "data";
	public static File dataFile;	
	
	@BeforeSuite 
	public static void setUp() {
		Reporter.log( "Calling BeforeSuite setUp method." );
		dataFile = new File( directory );
	      if ( !new File(directory).exists() ) {
	    	  Reporter.log( "Creating data directory." );
	          new File(directory).mkdir();
	      }
	}
	
	@AfterMethod
	public void afterTest() {
		Reporter.log( "Called afterTest() method...", true );
	}
	
}
