package qa.hs.framework.data;

import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlTest;

import qa.hs.framework.SeHelper;

public class TestData {
	
	@DataProvider(name = "testdata")
	public static Object[][] getTestData( ITestContext context ) {
		List<XmlTest> tests = context.getSuite().getXmlSuite().getTests();
		Map<String, String> suiteParams = context.getSuite().getXmlSuite().getAllParameters();
		Object[][] testData = new Object[tests.size()][2];
		int i = 0;
		for ( XmlTest thisTest : tests ) {
			SeHelper se = new SeHelper();
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
