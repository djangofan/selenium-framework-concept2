package qa.hs.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class Browser 
{

	public final File CHROMEDRIVER = new File("chromedriver.exe");
	public final File CHROMEDRIVERZIP = new File("chromedriver_win32.zip");
	
    String moniker;
    
    Browser( String moniker ) {
        this.moniker = moniker;
    }
    
	public void loadNewBrowser( URL url )
	{
		DesiredCapabilities abilities = null;

		switch ( moniker ) {
		case CHROME:
			getLatestWindowsChromeDriver();
			System.setProperty("webdriver.chrome.driver", CHROMEDRIVER.getAbsolutePath() );
			abilities = DesiredCapabilities.chrome();
			abilities.setCapability( "platform", osType );
			abilities.setCapability( "version", browserVersion );
			abilities.setCapability( "screen-resolution", "1024x768" );
			if ( isLocal ) driver = new ChromeDriver( abilities );
			break;
		case FIREFOX:
			abilities = DesiredCapabilities.firefox();
			abilities.setCapability( "platform", osType );
			abilities.setCapability( "version", browserVersion );
			abilities.setCapability( "screen-resolution", "1024x768" );
			if ( isLocal ) driver = new FirefoxDriver( abilities );
			break;
		case INTERNET_EXPLORER:
			System.setProperty("webdriver.ie.driver","IEDriverServer.exe");
			abilities = DesiredCapabilities.internetExplorer();
			abilities.setCapability( "platform", osType );
			abilities.setCapability( "version", browserVersion );
			abilities.setCapability( "screen-resolution", resolution );
			if ( isLocal ) driver = new InternetExplorerDriver( abilities );
			break;
		case SAFARI:
			abilities = DesiredCapabilities.safari();
			abilities.setCapability( "platform", osType );
			abilities.setCapability( "version", browserVersion );
			abilities.setCapability( "screen-resolution", "1024x768" );
			if ( isLocal ) driver = new SafariDriver( abilities );
			break;
		default:
			System.err.println("Unknown browser: " + browser );
			return;
		}

		//driver.manage().timeouts().pageLoadTimeout( 4, TimeUnit.SECONDS );

		if ( !isLocal ) {
			System.out.println( "Configured hub url is '" + hubUrl + "'" );
			try {
				driver = new RemoteWebDriver( new URL( hubUrl ), abilities );
			} catch ( Exception x ) {
				System.err.println( "Couldn't connect to hub: " + hubUrl );
				return;
			}
		} else {
			System.out.println("You are using a local web browser.");
		}

		// load the properties.
		Properties properties = new Properties();
		try {
			properties.load( getClass().getResourceAsStream( getClass().getSimpleName().concat(".properties") ) );            
			for ( String key : properties.stringPropertyNames() ) {
				props.put( key, By.cssSelector( properties.getProperty( key ) ) ); //hard coded as cssSelector but you can change it
			}
		} catch ( Exception x ) {
			System.err.println("WARN: No css properties file for this test was found.  You can create one under /src/tests/resources/<package(s)>/" +
					getClass().getSimpleName().concat(".properties") );
		}

		actions = new Actions( driver );
		driver.navigate().to( appUrl );
		//TODO augmenter
	}

	private void getLatestWindowsChromeDriver() {
		if ( !CHROMEDRIVER.exists() ) {	
			FileOutputStream fos = null;
			InputStream in = null;
			try {
				URL downloadUrl = new URL("http://chromedriver.storage.googleapis.com/index.html?path=2.8/chromedriver_win32.zip");
				URLConnection conn = downloadUrl.openConnection();
				in = conn.getInputStream();
				fos = new FileOutputStream( CHROMEDRIVERZIP.getAbsoluteFile() );
				byte[] b = new byte[1024];
				int count;
				while ( ( count = in.read(b) ) != -1 ) {
					fos.write(b, 0, count);
				}				
			} catch ( FileNotFoundException e ) {
				e.printStackTrace();
			} catch ( IOException e ) {
				e.printStackTrace();
			} finally {
				try {
					fos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if ( CHROMEDRIVERZIP.exists() ) {
					System.out.println( "Finished downloading Chrome driver zip archive: " + CHROMEDRIVERZIP.getAbsolutePath() );
				} else {
					System.out.println( "Failure to download the Chrome driver zip archive." );
				}				
			}
			if ( CHROMEDRIVERZIP.exists() ) {
				unzip( CHROMEDRIVERZIP.getAbsolutePath(), CHROMEDRIVER.getAbsolutePath(), "" );
				//CHROMEDRIVERZIP.delete();
			} else {
				throw new IllegalStateException( "Could not unzip Chrome driver.");
			}
		} else {
			System.out.println("Chrome driver was found located at: " + CHROMEDRIVER.getAbsolutePath() );
		}
	}

	/**
	 * Unzip a zip file
	 * @param source
	 * @param destination
	 * @param password
	 */
	public static void unzip( String source, String destination, String password ) {
		try {
			ZipFile zipFile = new ZipFile( source );
			if ( zipFile.isEncrypted() ) {
				zipFile.setPassword( password );
			}
			zipFile.extractAll( destination );
		} catch ( ZipException e ) {
			e.printStackTrace();
		}
	}
    
}
