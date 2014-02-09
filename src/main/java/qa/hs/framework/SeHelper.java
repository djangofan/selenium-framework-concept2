package qa.hs.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.json.simple.JSONArray;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Reporter;

public final class SeHelper
{	
	private final String browser;
	private final String testName;
	private String appUrl;	
	private String sauceUser;
	private String sauceKey;
	private String sessionId;
	private String hubUrl;
	private SeUtil util;
	private WebDriver driver;
	private DesiredCapabilities abilities;

	private SeHelper( SeBuilder builder ) 
	{
		Reporter.log( "Creating new SeHelper object from a SeBuilder object...", true );
		this.testName = builder.testName;
		this.browser = builder.browser;
		this.appUrl = builder.appUrl;
		this.sauceUser = builder.sauceUser;
		this.sauceKey = builder.sauceKey;
		this.sessionId = builder.sessionId;
		this.hubUrl = builder.hubUrl;
		this.driver = builder.driver;
		this.util = builder.util;
		this.abilities = builder.abilities;
	}	
	
	public void navigateToStart() {
		Reporter.log( "Navigating to start URL: " + appUrl, true );
		if ( !(driver == null) ) {
			driver.navigate().to( appUrl );
		} else {
			throw new NullPointerException("Driver is not yet loaded or is null.");
		}		
	}

	public String getAppUrl() {
		if ( appUrl == null || appUrl.isEmpty() ) {
			throw new NullPointerException("The app url was not yet set in the SeBuilder object.");
		} else {
		    return appUrl;
		}
	}

	public WebDriver getDriver() {
		if ( this.driver == null ) {
			throw new IllegalStateException( "The driver is not yet loaded. Cannot return it." );
		} else {
		    return this.driver;
		}
	}

	public String getTestName() {
		return testName;
	}

	public String getHubUrl() {
		return hubUrl;
	}
	
	public SeUtil getUtil() {
		if ( this.util == null ) {
			return new SeUtil( driver );
		} else {
		   return this.util;
		}
	}

	public String getSauceKey() {
		return sauceKey;
	}

	public String getSauceUser() {
		return sauceUser;
	}

	public void setDriver( WebDriver driver ) {
		this.driver = driver;
	}

	public void setHubUrl( String hubUrl ) {
        this.hubUrl = hubUrl;
	}
	
	public void setUtil( SeUtil util ) {
		this.util = util;
	}
	
	public String getBrowser() {
		return browser;
	}
	
	public void loadDriver() {
		try {
			this.driver = new RemoteWebDriver( parseUrl( appUrl ), this.abilities );
		} catch ( Exception e ) {
			Reporter.log( "\nThere was a problem loading the driver:", true );
			e.printStackTrace();
		}
		this.sessionId = ((RemoteWebDriver)this.driver).getSessionId().toString();
	}
	
	private URL parseUrl( String url ) {
		URL formalUrl = null;
		try { 
			formalUrl = new URL( url );
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
		return formalUrl;
	}
	
	public static class SeBuilder 
	{
		private static final File CHROMEDRIVER = new File("chromedriver.exe");
		private static final File CHROMEDRIVERZIP = new File("chromedriver_win32.zip");
		private final String browser;
		private final String testName; // if you do not use SauceLabs, we require this anyway
		private String appUrl;
		private SeUtil util;
		private WebDriver driver;		
		private String hubUrl;
		private String sessionId;
		private String sauceUser;
		private String sauceKey;
		private DesiredCapabilities abilities;
		
		public SeBuilder( String testName, String browser ) {
			this.testName = testName;
			this.browser = browser;
		}
		
		public SeBuilder hubUrl( String hubUrl ) {
			this.hubUrl = hubUrl;
			return this;
		}
		
		public SeBuilder appUrl( String appUrl ) {
			this.appUrl = appUrl;
			return this;
		}
		
		public SeBuilder sauceUser( String sauceUser ) {
			this.sauceUser = sauceUser;
			return this;
		}
		
		public SeBuilder sauceKey( String sauceKey ) {
			this.sauceKey = sauceKey;
			return this;
		}
		
		public SeHelper build() {
			this.loadCapabilities();
			this.util = new SeUtil( this.driver );
			return new SeHelper( this );
		}
		
		@SuppressWarnings("unchecked") // JSONArray using legacy API
		public void loadCapabilities() {
			System.out.println("Loading WebDriver '" + this.browser + "' instance...");
			switch ( browser ) {
			case "chrome":
				getLatestWindowsChromeDriver();
				System.setProperty( "webdriver.chrome.driver", CHROMEDRIVER.getAbsolutePath() );
				this.abilities = DesiredCapabilities.chrome();
				driver = new ChromeDriver( abilities );
				break;
			case "firefox":
				this.abilities = DesiredCapabilities.firefox();
				driver = new FirefoxDriver( abilities );
				this.abilities.setCapability( CapabilityType.SUPPORTS_JAVASCRIPT, true );
				break;
			case "ie":
				System.setProperty("webdriver.ie.driver","IEDriverServer.exe");
				this.abilities = DesiredCapabilities.internetExplorer();
				driver = new InternetExplorerDriver( abilities );
				break;
			case "safari":
				this.abilities = DesiredCapabilities.safari();
				driver = new SafariDriver( abilities );
				break;
			case "phantomjs":
				// not yet
				break;
			case "gridchrome31":
				if ( hubUrl.isEmpty() ) {
					throw new IllegalStateException( "Please set the grid hub URL before calling loadDriver()" );
				} else if ( hubUrl.contains("User") && hubUrl.contains("Key") ) {
					hubUrl = hubUrl.replace("User", this.sauceUser );
					hubUrl = hubUrl.replace("Key", this.sauceKey );
					Reporter.log("Using Sauce Labs grid hub url: " + hubUrl, true );
				} else {
					Reporter.log("Using raw hub url to connect to Selenium grid hub...", true );
				}
				this.abilities = DesiredCapabilities.chrome();
				if ( testName.isEmpty() ) {
					throw new IllegalArgumentException( "Selenium grid tests require that the test name capability be set." );
				} else {
					this.abilities.setCapability( "name", this.testName );
				}
				JSONArray tags = new JSONArray(); 
				    tags.add( this.browser ); 
				    tags.add("Windows 8"); 
				    tags.add("1280x1024"); 
				this.abilities.setCapability( "tags", tags );
				this.abilities.setCapability( "platform", "Windows 8" );
				this.abilities.setCapability( "version", "31" );
				this.abilities.setCapability( "screen-resolution", "1280x1024" );
				Reporter.log("Default application url: " + appUrl, true );
				break;
			case "gridfirefox26":
				// not yet
				break;
			case "gridie10":
				// not yet
				break;
			case "gridsafari7":
				// not yet
				break;
			default:
				throw new IllegalStateException( "Unknown browser string '" + browser + "'." );
			}
			Reporter.log( "Finished setting up driver.", true );
			
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
						Reporter.log( "Finished downloading Chrome driver zip archive: " + CHROMEDRIVERZIP.getAbsolutePath(), true );
					} else {
						Reporter.log( "Failure to download the Chrome driver zip archive.", true );
					}				
				}
				if ( CHROMEDRIVERZIP.exists() ) {
					unzip( CHROMEDRIVERZIP.getAbsolutePath(), CHROMEDRIVER.getAbsolutePath(), "" );
					//CHROMEDRIVERZIP.delete();
				} else {
					throw new IllegalStateException( "Could not unzip Chrome driver.");
				}
			} else {
				Reporter.log("Chrome driver was found located at: " + CHROMEDRIVER.getAbsolutePath(), true );
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
		
	}
	
	public boolean uploadResultToSauceLabs( String testName, String build, Boolean pass ) {
		Reporter.log("Uploading sauce result for '" + build + "' : " + pass, true );
		Map<String, Object> updates = new HashMap<String, Object>();
		if ( !testName.isEmpty() ) {
			Reporter.log( "Updating SauceLabs test name to '" + testName + "'.", true );
			updates.put( "name", testName );
		}
		updates.put( "passed", pass.toString() );
		updates.put( "build", build );
		SauceREST client;
		try {
			client = new SauceREST( this.getSauceUser(), this.getSauceKey() );
			client.updateJobInfo( this.sessionId, updates );
		} catch ( Exception e ) {
			return false;
		}		
		String jobInfo = client.getJobInfo( this.sessionId );
		Reporter.log( "Job info: " + jobInfo, true );
		return true;
	}
	
}
