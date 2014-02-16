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

public class SampleTest3 extends TestBase {
	
    @Test(dataProvider = "testdata", dataProviderClass = TestData.class)
    public void test3( SeHelper se, XmlTest testArgs ) {
    	Reporter.log( "Start of test3.", true );
    	setHelper( se );
		se.navigateToStart();
    	Map<String, String> params = testArgs.getAllParameters();
    	MavenSearchPage ep = new MavenSearchPage( se.getDriver() );
    	getUtil().waitTimer(1,  1000);
    	Reporter.log( "Page title: " + se.getDriver().getTitle(), true );
    	ep.clickArtifactField();;
    	ep.setArtifactFieldValue( params.get( "textString1" ) );
    	ep.clickAdvancedSearchButton();
    	getUtil().waitTimer( 3, 1000 );
    }
    
	@AfterTest
	public void afterTest() {
		Reporter.log( "Called overridden afterTest() method...", true );
    	if ( getHelper().isSauceTest() ) getHelper().uploadResultToSauceLabs("", "build3", true );
    	getHelper().getDriver().quit();
	}
	
}
