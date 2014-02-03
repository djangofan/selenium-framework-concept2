package qa.hs.framework.tests;

import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import qa.hs.framework.TestBase;
import qa.hs.framework.SeBuilder;
import qa.hs.framework.data.TestData;
import qa.hs.framework.pages.TestPage;

public class SampleTest3 extends TestBase {
	
    @Test(dataProvider = "testdata", dataProviderClass = TestData.class)
    public void test5( SeBuilder se, XmlTest testArgs ) {
    	se.loadNewBrowser();    	
    	this.util = se.getUtil();
    	util.setWindowPosition( se.getDriver(), 800, 600, 20, 100 );
    	Map<String, String> params = testArgs.getAllParameters();
    	TestPage ep = new TestPage( se.getDriver() );
    	util.waitTimer(1,  1000);
    	Reporter.log( "Page title: " + se.getDriver().getTitle() );
    	ep.clickTestInputField();
    	ep.clickTestOutputField();
    	util.waitTimer(5,  1000);
    	ep.setInputFieldString( params.get( "textString1" ) );
    	ep.clickProcessButton();
    	util.waitTimer( 5, 1000 );
    	se.getDriver().quit();
    }
    
    @Test(dataProvider = "testdata", dataProviderClass = TestData.class)
    public void test7( SeBuilder se, XmlTest testArgs ) {
    	se.loadNewBrowser();    	
    	this.util = se.getUtil();
    	util.setWindowPosition( se.getDriver(), 800, 600, 540, 100 );
    	Map<String, String> params = testArgs.getAllParameters();
    	TestPage ep = new TestPage( se.getDriver() );
    	util.waitTimer(1,  1000);
    	Reporter.log( "Page title: " + se.getDriver().getTitle() );
    	ep.clickTestInputField();
    	ep.clickTestOutputField();
    	util.waitTimer(5,  1000);
    	ep.setInputFieldString( params.get( "textString1" ) );
    	ep.clickProcessButton();
    	util.waitTimer( 5, 1000 );
    	se.getDriver().quit();
    }
    
}
