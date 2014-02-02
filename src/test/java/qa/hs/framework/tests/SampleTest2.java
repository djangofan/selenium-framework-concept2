package qa.hs.framework.tests;

import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import qa.hs.framework.TestBase;
import qa.hs.framework.SeBuilder;
import qa.hs.framework.pages.TestPage;

public class SampleTest2 extends TestBase {
	
    @Test(dataProvider = "testdata2")
    public void test2( SeBuilder se, XmlTest testArgs ) {
    	se.loadNewBrowser();    	
    	this.util = se.getUtil();
    	util.setWindowPosition( se.getDriver(), 800, 600, 20, 20 );
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
    
    @Test(dataProvider = "testdata2")
    public void test4( SeBuilder se, XmlTest testArgs ) {
    	se.loadNewBrowser();    	
    	this.util = se.getUtil();
    	util.setWindowPosition( se.getDriver(), 800, 600, 500, 200 );
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
    
	@DataProvider(name = "testdata2")
	public Object[][] getTestData( ITestContext context ) {
		List<XmlTest> tests = context.getSuite().getXmlSuite().getTests();
		Map<String, String> suiteParams = context.getSuite().getXmlSuite().getAllParameters();
		Object[][] testData = new Object[tests.size()][2];
		int i = 0;
		for ( XmlTest thisTest : tests ) {
			SeBuilder se = new SeBuilder();
			se.setAppUrl( suiteParams.get( "appUrl" ) );
			se.setHubUrl( suiteParams.get( "hubUrl" ) );
			se.setSauceUsername( suiteParams.get( "sauceUsername" ) );
			se.setSauceKey( suiteParams.get( "sauceKey" ) );
			se.setBrowser( suiteParams.get( "browser" ) );
			testData[i][0] = se;
			testData[i][1] = thisTest;
			i++;
			System.out.println("Added data: " + i );
		}
	    return testData;
	}
    
}
