package qa.hs.framework.tests;

import java.util.List;
import java.util.Map;

import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

import qa.hs.framework.TestBase;
import qa.hs.framework.SeHelper;
import qa.hs.framework.pages.EtsySearchPage;

public class SampleFunctionalTest extends TestBase {
	
    @Test(dataProvider = "testdata")
    public void test1( SeHelper se, XmlTest testArgs ) {
    	Map<String, String> params = testArgs.getAllParameters();
    	EtsySearchPage ep = new EtsySearchPage( se ); //activates SeHelper object
    	ep.setSearchString( params.get( "searchString" ) );
    	ep.selectInEtsyDropdown( params.get( "searchMatch" ) );
        //navigateTo( appUrl )
        //.click( props.get( "autocomplete" ) )
        //.enterTextIntoField( props.get( "autocomplete" ), "buttons" )
        // .sleep( 1000 )
        //.selectFromDropdownByText( props.get( "autocomplete" ), props.get( "suggest" ), "large buttons" )
        //.sleep( 1000 )
        //.click( props.get( "search" ) )
        //.sleep( 1000 );        
    }
    
    @Test(dataProvider = "testdata")
    public void test2( SeHelper se, XmlTest testArgs ) {
    	Map<String, String> params = testArgs.getAllParameters();
    	EtsySearchPage ep = new EtsySearchPage( se ); //activates SeHelper object
    	ep.setSearchString( params.get( "searchString" ) );
    	ep.selectInEtsyDropdown( params.get( "searchMatch" ) );
        //navigateTo( appUrl )
        //.click( props.get( "autocomplete" ) )
        //.enterTextIntoField( props.get( "autocomplete" ), "zippers" )
        //.sleep( 1000 )
        //.selectFromDropdownByText( props.get( "autocomplete" ), props.get( "suggest" ), "metal zippers" )
        //.sleep( 1000 )
        //.click( props.get( "search" ) )
        //.sleep( 1000 );
    }
    
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
			se.setHubUrl( suiteParams.get( "hubUrl" ) );
			se.setSauceUsername( suiteParams.get( "sauceUsername" ) );
			se.setSauceKey( suiteParams.get( "sauceKey" ) );
			se.setBrowser( suiteParams.get( "browser" ) );
			se.loadNewBrowser();
			testData[i][0] = se;
			testData[i][1] = thisTest;
			i++;
			System.out.println("Added data: " + i );
		}
	    return testData;
	}
    
}
