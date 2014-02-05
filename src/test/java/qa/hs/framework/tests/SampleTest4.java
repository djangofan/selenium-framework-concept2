package qa.hs.framework.tests;

import java.util.Map;

import org.testng.Reporter;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import qa.hs.framework.TestBase;
import qa.hs.framework.SeHelper;
import qa.hs.framework.data.TestData;
import qa.hs.framework.pages.TestPage;

public class SampleTest4 extends TestBase {
	
	@Test(dataProvider = "testdata", dataProviderClass = TestData.class)
    public void test4( SeHelper se, XmlTest testArgs ) {
    	se.loadNewBrowser();    	
    	this.util = se.getUtil();
    	util.setWindowPosition( se.getDriver(), 800, 600, 40, 200 );
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
    	se.uploadResultToSauceLabs("", "build4", true );
    }

}
