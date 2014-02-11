package qa.hs.framework.tests;

import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import qa.hs.framework.TestBase;
import qa.hs.framework.SeHelper;
import qa.hs.framework.data.TestData;
import qa.hs.framework.pages.MavenSearchPage;

public class SampleTest1 extends TestBase {
	
	@Test(dataProvider = "testdata", dataProviderClass = TestData.class)
    public void test1( SeHelper se, XmlTest testArgs ) {
		Reporter.log( "Start of test1." );
		se.navigateToStart();
        this.util = se.getUtil();
    	Map<String, String> params = testArgs.getAllParameters();
    	MavenSearchPage ep = new MavenSearchPage( se.getDriver() );
    	util.waitTimer(1,  1000);
    	Reporter.log( "Page title: " + se.getDriver().getTitle() );
    	ep.clickSearchField();
    	ep.setSearchFieldValue( params.get( "textString1" ) );
    	ep.clickSearchButton();
    	util.waitTimer( 5, 1000 );
    	se.getDriver().quit();
    	se.uploadResultToSauceLabs( "", "build1", new Boolean(true) );
    }
	
	@Override
	@AfterTest
	public void afterTest() {
		Reporter.log( "Called overridden afterTest() method...", true );
		
	}
    
}
