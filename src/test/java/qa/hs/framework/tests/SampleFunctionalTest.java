package qa.hs.framework.tests;

import java.io.File;

import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import qa.hs.framework.AutomationTest;
import qa.hs.framework.SeHelper;
import qa.hs.framework.TestArguments;

public class SampleFunctionalTest extends AutomationTest 
{
	private static final String directory = "data";
	private static File dataFile;
	
	@BeforeClass 
	public static void setup() {
		dataFile = new File( directory );
	      if ( !new File(directory).exists() ) {
	          new File(directory).mkdir();
	      }
	}
	
    @Test(dataProvider = "testdata1")
    public void test1( SeHelper se, TestArguments testArgs ) {
    	navigateTo( appUrl )
        .click( props.get( "autocomplete" ) )
        .enterTextIntoField( props.get( "autocomplete" ), "buttons" )
        .sleep( 1000 )
        .selectFromDropdownByText( props.get( "autocomplete" ), props.get( "suggest" ), "large buttons" )
        .sleep( 1000 )
        .click( props.get( "search" ) )
        .sleep( 1000 );        
    }
    
    @Test(dataProvider = "testdata1")
    public void test2( SeHelper se, TestArguments testArgs ) {
    	navigateTo( appUrl )
        .click( props.get( "autocomplete" ) )
        .enterTextIntoField( props.get( "autocomplete" ), "zippers" )
        .sleep( 1000 )
        .selectFromDropdownByText( props.get( "autocomplete" ), props.get( "suggest" ), "metal zippers" )
        .sleep( 1000 )
        .click( props.get( "search" ) )
        .sleep( 1000 );
    }
    
    @AfterClass
    public void cleanUp() {
    	closeAllWindows();
    }
    
	@DataProvider(name = "testdata1")
	public Object[][] getTestData1( ITestContext context ) {
		System.out.println("Calling TestNG data provider method: testdata1");
		String testParam = context.getCurrentXmlTest().getParameter("test_param");
	    return new Object[][] {{ testParam }};
	}
    
}
