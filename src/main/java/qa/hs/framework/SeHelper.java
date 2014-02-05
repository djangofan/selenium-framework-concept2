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
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.Reporter;

public final class SeHelper
{

	private SeUtil util;
	private WebDriver driver;
	
	private final File CHROMEDRIVER = new File("chromedriver.exe");
	private final File CHROMEDRIVERZIP = new File("chromedriver_win32.zip");
	private String browser;
	private URL appUrl;
	private URL hubUrl;
	private String sauceUser;
	private String sauceKey;
	private String sessionId;
	private String testName;

	public SeHelper( String testName ) {
		System.out.println("Created new SeBuilder object.");
		this.testName = testName;
	}

	public URL getAppUrl() {
		if ( appUrl == null || appUrl.toExternalForm().isEmpty() ) {
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

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public URL getHubUrl() {
		return hubUrl;
	}
	
	public SeUtil getUtil() {
		if ( this.util == null ) {
			return new SeUtil( driver );
		} else {
		   return this.util;
		}
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

	public String getSauceKey() {
		return sauceKey;
	}

	public String getSauceUser() {
		return sauceUser;
	}

	public SeHelper loadNewBrowser() {
		this.testName = testName;
		System.out.println("Loading WebDriver instance...");
		DesiredCapabilities abilities = null;
		switch ( browser ) {
		case "chrome":
			getLatestWindowsChromeDriver();
			System.setProperty( "webdriver.chrome.driver", CHROMEDRIVER.getAbsolutePath() );
			abilities = DesiredCapabilities.chrome();
			driver = new ChromeDriver( abilities );
			break;
		case "firefox":
			abilities = DesiredCapabilities.firefox();
			driver = new FirefoxDriver( abilities );
			break;
		case "ie":
			System.setProperty("webdriver.ie.driver","IEDriverServer.exe");
			abilities = DesiredCapabilities.internetExplorer();
			driver = new InternetExplorerDriver( abilities );
			break;
		case "safari":
			abilities = DesiredCapabilities.safari();
			driver = new SafariDriver( abilities );
			break;
		case "phantomjs":
			// not yet
			break;
		case "gridchrome31":
			String normalizedHubUrl = hubUrl.toExternalForm();
			if ( normalizedHubUrl.isEmpty() ) {
				throw new IllegalStateException( "Please set the grid hub URL before calling loadNewBrowser()");
			} else if ( normalizedHubUrl.contains("User") && normalizedHubUrl.contains("Key") ) {
				Reporter.log("Normalizing the Sauce Labs grig hub url...");
				normalizedHubUrl = normalizedHubUrl.replace("User", this.getSauceUser() );
				normalizedHubUrl = normalizedHubUrl.replace("Key", this.getSauceKey() );
				try {
					hubUrl = new URL( normalizedHubUrl );
				} catch ( MalformedURLException e ) {
					e.printStackTrace();
				}
			} else {
				Reporter.log("Using raw hub url to connect to Selenium grid hub...");
			}
			abilities = DesiredCapabilities.chrome();
			if ( testName.isEmpty() ) {
				abilities.setCapability( "name", "Local" );
			} else {
				abilities.setCapability( "name", this.testName );
			}
			JSONArray tags = new JSONArray(); 
			 tags.add("gridchrome31"); 
			 tags.add("WIN8"); 
			 tags.add("high-res"); 
			abilities.setCapability( "tags", tags );
			abilities.setCapability( "platform", "Windows 8" );
			abilities.setCapability( "version", "31" );
			abilities.setCapability( "screen-resolution", "1280x1024" );
			driver = new RemoteWebDriver( hubUrl, abilities );
			this.sessionId = ((RemoteWebDriver)driver).getSessionId().toString();
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
        initializeUtil();
        navigateTo( appUrl.toExternalForm() );
		return this;
	}
	
	public void initializeUtil() {
		if ( !(driver == null) ) {
			if ( util == null ) {
			    setUtil( new SeUtil( driver ) );
			} else {
				throw new IllegalStateException("Utility object already was initialized when you tried to initialize it.");
			}
		} else {
			throw new NullPointerException("Driver is not yet loaded or is null.");
		}
	}
	
	public void navigateTo( String url ) {
		if ( !(driver == null) ) {
			driver.navigate().to( url );
		} else {
			throw new NullPointerException("Driver is not yet loaded or is null.");
		}		
	}

	public void setAppUrl( String appUrl ) {
		try {
			this.appUrl = new URL( appUrl );
		} catch ( MalformedURLException e ) {
			e.printStackTrace();
		}
	}

	public void setBrowser( String bro ) {
		browser = bro;		
	}

	public void setDriver( WebDriver driver ) {
		this.driver = driver;
	}

	public void setHubUrl( String hubUrl ) {
		if ( !hubUrl.isEmpty() ) {
			try {
				this.hubUrl = new URL( hubUrl );
			} catch ( MalformedURLException e ) {
				e.printStackTrace();
			}
		}
	}

	public void setSauceKey(String sauceKey) {
		this.sauceKey = sauceKey;
	}

	public void setSauceUsername(String sauceUsername) {
		this.sauceUser = sauceUsername;
	}
	
	public void setUtil( SeUtil util ) {
		this.util = util;
	}
	
	public String getBrowser() {
		return browser;
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
	
	public static class SeBuilder {
	    //TODO Implement builder pattern.
		// http://www.javacodegeeks.com/2013/01/the-builder-pattern-in-practice.html
	}
	
	public boolean uploadResultToSauceLabs( String testName, String build, Boolean pass ) {
		Reporter.log("Uploading sauce result for '" + build + "' : " + pass, true );
		Map<String, Object> updates = new HashMap<String, Object>();
		if ( !testName.isEmpty() ) {
			Reporter.log( "Updating SauceLabs test name to '" + testName + "'." );
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
