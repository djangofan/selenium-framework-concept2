package qa.hs.framework.tests;

import java.io.File;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import qa.hs.framework.AutomationTest;
import qa.hs.framework.data.XMLDataHelper;
import qa.hs.framework.data.def.TestArguments;
import static qa.hs.framework.data.XMLTransformer.generateHtmlFromInputXML;

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
    public void test1( TestArguments testArgs ) {
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
    public void test2( TestArguments testArgs ) {
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
	    if ( dataFile.exists() ) {
	        generateHtmlFromInputXML( "dataProvider1.xml", "src/test/resources/default.xsl", "dataProvider1.html" );
	    } else {
	        throw new IllegalStateException("The data directory for tests is missing.");
	    }    	
    }
    
	@DataProvider(name = "testdata1")
	public Object[][] getTestData1() {
		System.out.println("Calling TestNG data provider method: testdata1");
		XMLDataHelper dp = new XMLDataHelper( new File( directory + File.separator + "dataProvider1.xml" ) );
		return dp.getArgumentsArrays();
	}
    
}
