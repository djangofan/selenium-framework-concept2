package qa.hs.framework.tests;

import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import qa.hs.framework.TestBase;
import qa.hs.framework.SeHelper;
import qa.hs.framework.data.TestData;
import qa.hs.framework.pages.TestPage;

public class SampleTest2 extends TestBase {
	
	@Test(dataProvider = "testdata", dataProviderClass = TestData.class)
    public void test2( SeHelper se, XmlTest testArgs ) {
		Reporter.log( "Start of test2." );
		se.loadDriver();
		se.navigateToStart();
        this.util = se.getUtil();
    	util.setWindowPosition( se.getDriver(), 800, 600, 20, 20 );
    	Map<String, String> params = testArgs.getAllParameters();
    	TestPage ep = new TestPage( se.getDriver() );
    	util.waitTimer(1,  1000);
    	Reporter.log( "Page title: " + se.getDriver().getTitle() );
    	ep.clickTestInputField();
    	ep.clickTestOutputField();
    	util.waitTimer(5,  1000);
    	ep.setInputFieldString( params.get( "textString2" ) );
    	ep.clickProcessButton();
    	util.waitTimer( 5, 1000 );
    	se.getDriver().quit();
    	se.uploadResultToSauceLabs("", "build2", true );
    }
    
}
