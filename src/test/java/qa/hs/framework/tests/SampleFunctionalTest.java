package qa.hs.framework.tests;

import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import qa.hs.framework.AutomationTest;
import qa.hs.framework.Browser;
import qa.hs.framework.Config;

@Config(
    url = "http://www.etsy.com/browse/men?ref=hp_so_h",
    browser = Browser.CHROME,
    hub = "",
    osType = "WIN8",
    browserVersion = "26",
    resolution = "1280x1024"
)
public class SampleFunctionalTest extends AutomationTest 
{
    
    @Test
    public void test1() {
    	navigateTo( baseUrl )
        .click( props.get( "autocomplete" ) )
        .enterTextIntoField( props.get( "autocomplete" ), "buttons" )
        .sleep( 1000 )
        .selectItemByText( props.get( "autocomplete" ), props.get( "suggest" ), "large buttons" )
        .sleep( 1000 )
        .click( props.get( "search" ) )
        .sleep( 1000 );        
    }
    
    @Test
    public void test2() {
    	navigateTo( baseUrl )
        .click( props.get( "autocomplete" ) )
        .enterTextIntoField( props.get( "autocomplete" ), "zippers" )
        .sleep( 1000 )
        .selectItemByText( props.get( "autocomplete" ), props.get( "suggest" ), "metal zippers" )
        .sleep( 1000 )
        .click( props.get( "search" ) )
        .sleep( 1000 );
    }
    
    @AfterClass
    public void cleanUp() {
    	closeAllWindows();
    	
    }
    
}
