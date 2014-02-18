package qa.hs.framework.data;

import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.xml.XmlTest;

import qa.hs.framework.SeHelper;

public class TestData {
	
	@DataProvider(name = "testdata")
	public static Object[][] getTestData( ITestContext context ) 
	{
		List<XmlTest> tests = context.getSuite().getXmlSuite().getTests();
		Map<String, String> suiteParams = context.getSuite().getXmlSuite().getAllParameters();
		Object[][] testData = new Object[tests.size()][2];
		int i = 0;
		for ( XmlTest thisTest : tests ) {
			SeHelper se = new SeHelper.SeBuilder( thisTest.getName(), suiteParams.get( "browser" ) )
			    .appUrl( suiteParams.get( "appUrl" ) ).sauceUser( suiteParams.get( "sauceUser" ) )
			    .sauceKey( suiteParams.get( "sauceKey" ) ).hubUrl( suiteParams.get( "localHubUrl" ) )
			    .sauce( Boolean.parseBoolean( suiteParams.get( "isSauce" ) ) )
			    .grid( Boolean.parseBoolean( suiteParams.get( "isGrid" ) ) )
			    .proxy( Boolean.parseBoolean( thisTest.getTestParameters().get( "proxy" ) ) )
			    .build();
			testData[i][0] = se;
			testData[i][1] = thisTest;
			i++;
			Reporter.log( i +  ": Added test: " + thisTest.getName(), true );
		}
	    return testData;
	}

}
