package qa.hs.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

public class SeHelper 
{
    private WebDriver driver;
	public final File CHROMEDRIVER = new File("chromedriver.exe");
	public final File CHROMEDRIVERZIP = new File("chromedriver_win32.zip");
    private Browser browser;
	private URL appUrl;
	private URL hubUrl;
    
	public SeHelper( Browser moniker ) {
        this.browser = moniker;
    }
    
	public void loadNewBrowser( String appUrl )
	{
		this.setAppUrl( appUrl );
		DesiredCapabilities abilities = null;

		switch ( browser.getMoniker() ) {
		case "CHROME":
			getLatestWindowsChromeDriver();
			System.setProperty( "webdriver.chrome.driver", CHROMEDRIVER.getAbsolutePath() );
			abilities = DesiredCapabilities.chrome();
			driver = new ChromeDriver( abilities );
			break;
		case "FIREFOX":
			abilities = DesiredCapabilities.firefox();
			driver = new FirefoxDriver( abilities );
			break;
		case "IE":
			System.setProperty("webdriver.ie.driver","IEDriverServer.exe");
			abilities = DesiredCapabilities.internetExplorer();
			driver = new InternetExplorerDriver( abilities );
			break;
		case "SAFARI":
			abilities = DesiredCapabilities.safari();
			driver = new SafariDriver( abilities );
			break;
		case "PHANTOMJS":
			// not yet
			break;
		case "GRIDCHROME32":
			if ( hubUrl.toExternalForm().isEmpty() ) {
				throw new IllegalStateException( "Please set the Selenium Grid hub URL before calling loadNewBrowser()");
			}
			abilities = DesiredCapabilities.chrome();
			abilities.setCapability( "platform", "Windows 8" );
			abilities.setCapability( "version", "32" );
			abilities.setCapability( "screen-resolution", "1280x1024" );
			driver = new RemoteWebDriver( hubUrl, abilities );
			break;
		case "GRIDFIREFOX26":
			// not yet
			break;
		case "GRIDIE10":
			// not yet
			break;
		case "GRIDSAFARI7":
			// not yet
			break;
		default:
			System.err.println("Unknown browser: " + browser.getMoniker() );
			return;
		}

		driver.navigate().to( appUrl );
		//TODO augmenter
	}

	/*
	 * Download latest Chrome WebDriver binary if necessary.
	 */
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
	 * Unzip a zip file.
	 * @param source
	 * @param destination
	 * @param password
	 */
	private static void unzip( String source, String destination, String password ) {
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

	public URL getAppUrl() {
		return appUrl;
	}

	public void setAppUrl( String appUrl ) {
		try {
			this.appUrl = new URL( appUrl );
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
	}
	
    public URL getHubUrl() {
		return hubUrl;
	}

	public void setHubUrl( String hubUrl ) {
		try {
			this.hubUrl = new URL( hubUrl );
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
	}
    
}
