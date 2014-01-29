package qa.hs.framework.tests;

import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import qa.hs.framework.SeHelper;

public class TestClass1 {

	@DataProvider(name = "testdata")
	public Object[][] getTestData( ITestContext context ) {
		// returns 2 args per test contained in the testng.xml file: a configured SeHelper object and a XmlTest object
		List<XmlTest> tests = context.getSuite().getXmlSuite().getTests();
		Map<String, String> suiteParams = context.getSuite().getXmlSuite().getAllParameters();
		Object[][] testData = new Object[tests.size()][2];
		int i = 0;
		for ( XmlTest thisTest : tests ) {
			SeHelper se = new SeHelper();
			se.setAppUrl( suiteParams.get( "appUrl" ) );
			testData[i][0] = se;
			testData[i][1] = thisTest;
			i++;
			System.out.println("Added data: " + i );
		}
	    return testData;
	}

	@Test(dataProvider="testdata")
	public void test1( SeHelper se, XmlTest testArgs )	{
		System.out.println( "Chrome binary: " + se.CHROMEDRIVERZIP.getAbsolutePath() );
		System.out.println( "Test Param: " + testArgs.getAllParameters().get("searchString") );
		System.out.println( "Suite Param: " + se.getAppUrl() );
	}
	
}
