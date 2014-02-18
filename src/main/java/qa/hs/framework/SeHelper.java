package qa.hs.framework;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import net.lightbody.bmp.core.har.Har;
import net.lightbody.bmp.core.har.HarEntry;
import net.lightbody.bmp.proxy.ProxyServer;

import org.json.simple.JSONArray;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Point;
import org.openqa.selenium.Proxy;
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
	private boolean isSauce;
	private boolean isGrid;
	private ProxyServer server;
	private int proxyPort;

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
		this.abilities = builder.abilities;
		this.isSauce = builder.isSauce;
		this.isGrid = builder.isGrid;
		this.util = new SeUtil( this.driver );
		this.server = builder.server;
		this.setProxyPort(builder.proxyPort);
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
	
	public boolean uploadResultToSauceLabs( String testName, String build, Boolean pass ) {
		if ( !isSauce ) throw new IllegalStateException( "This is not a SauceLabs test.  Cannot upload result." );
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

	public DesiredCapabilities getAbilities() {
		return abilities;
	}

	public void setAbilities(DesiredCapabilities abilities) {
		this.abilities = abilities;
	}

	public boolean isSauceTest() {
		return isSauce;
	}
	
	public boolean isGrid() {
		return isGrid;
	}

	public void setGrid(boolean isGrid) {
		this.isGrid = isGrid;
	}
	
	public void setHar( String label ) {
        server.newHar( label );
    }
	
	public void getHar() {
		//https://dvcs.w3.org/hg/webperf/raw-file/tip/specs/HAR/Overview.html
        Har har = server.getHar();
        if (har == null) return;
        // File harFile = new File("C:\\localdev\\bla.har");
        // har.writeTo(harFile);
        for ( HarEntry entry : har.getLog().getEntries() ) {
            // Check for any 4XX and 5XX HTTP status codes
            if ( ( String.valueOf(entry.getResponse().getStatus() ).startsWith("4") )
                    || ( String.valueOf( entry.getResponse().getStatus() ).startsWith("5") ) ) {
                Reporter.log( String.format("%s %d %s", entry.getRequest().getUrl(), entry.getResponse().getStatus(),
                        entry.getResponse().getStatusText() ), true );
                //throw new UnsupportedOperationException("Not implemented");
            }
        }
    }	
	
	public void setWindowPosition( int width, int height, int fleft, int ftop ) {
		Reporter.log( "Resizing window to: " + width + "x" + height + " at " + fleft + "x" + ftop, true );
		this.driver.manage().window().setPosition( new Point(fleft, ftop) );
		this.driver.manage().window().setSize( new Dimension( width, height) );
	}	

	public void maximizeWindow() {
		Reporter.log( "Maximize window is not yet implemented.", true );		
	}

	public int getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	/*
	 * SeBuilder inner class.  Using Builder design pattern.	
	 */
	public static class SeBuilder 
	{
		private final String browser; // final to make it a required option
		private final String testName; // final to make it a required option
		private String appUrl;
		private WebDriver driver;		
		private String hubUrl;
		private String sessionId;
		private String sauceUser;
		private String sauceKey;
		private DesiredCapabilities abilities;
		private boolean isGrid;
		private boolean isSauce;
		private ProxyServer server;
		private int proxyPort;
		
		public SeBuilder( String testName, String browser ) {
			this.setIsSauce( false ); //sets a default if 'sauce' method is not used
			this.setIsGrid( false ); //sets a default if 'grid' method is not used
			this.testName = testName;
			this.browser = browser;
		}
		
		public SeBuilder hubUrl( String hubUrl ) {
			if ( getIsSauce() ) {
				this.hubUrl = "http://User:Key@ondemand.saucelabs.com:80/wd/hub";
			} else {
			    this.hubUrl = hubUrl;
			}
			return this;
		}
		
		public SeBuilder appUrl( String appUrl ) {
			this.appUrl = appUrl;
			return this;
		}
		
		public SeBuilder sauce( boolean is ) {
			this.isSauce = is;
			return this;
		}
		
		public SeBuilder grid( boolean is ) {
			this.isGrid = is;
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
		
		public SeBuilder proxy( boolean enable ) {
			proxyPort = getNextProxyPort( 42000, 42999 );
			server = new ProxyServer( proxyPort );
			if ( enable ) {
		        try {
					server.start();
			        server.setCaptureHeaders(true);
			        server.blacklistRequests("https?://.*\\.google-analytics\\.com/.*", 200);
			        server.whitelistRequests(new String[]{"https?://.*\\.test.eesti\\.ee/.*"}, 200);	 
	                Proxy proxy = server.seleniumProxy();
	                this.abilities.setCapability( CapabilityType.PROXY, proxy );
				} catch ( Exception e ) {
					e.printStackTrace();
				}
		        Reporter.log( String.format( "Browser mob proxy started, running at port: %d", proxyPort ), true );		        
			}
			return this;
		}
		
		private int getNextProxyPort( int min, int max ) {
			Random rand = new Random();
		    int randomNum = rand.nextInt((max - min) + 1) + min;
		    return randomNum;
		}

		public SeHelper build() {
			this.setCapabilities();
			if ( this.isSauce || this.isGrid ) {
				this.loadGridDriver();
				this.sessionId = ((RemoteWebDriver)this.driver).getSessionId().toString();
			} else {
				this.loadLocalDriver();
			}
			return new SeHelper( this );
		}
		
		private URL asURL( String url ) {
			URL formalUrl = null;
			try { 
				formalUrl = new URL( url );
			} catch ( MalformedURLException e ) {
				e.printStackTrace();
			}
			return formalUrl;
		}
		
		public void loadGridDriver() {
			if ( this.isSauce && this.isGrid ) {
				Reporter.log( "Loading SauceLabs grid driver.", true );
			} else {
				Reporter.log( "Loading standard grid driver.", true );
			}
			try {
				this.driver = new RemoteWebDriver( asURL( hubUrl ), this.abilities );				
			} catch ( Exception e ) {
				Reporter.log( "\nThere was a problem loading the driver:", true );
				e.printStackTrace();
			}
	    	if ( this.isSauce && this.isGrid ) {
				Reporter.log("Finished loading SauceLabs grid driver.");
			} else {
				Reporter.log("Finished loading standard grid driver.");
			}
		}

		public void loadLocalDriver() {
			Reporter.log( "Loading local WebDriver '" + this.browser + "' instance...", true );
			switch ( browser ) {
			case "chrome":
				this.driver = new ChromeDriver( abilities );
				break;
			case "firefox":
				this.abilities.setCapability( CapabilityType.SUPPORTS_JAVASCRIPT, true );
				this.driver = new FirefoxDriver( abilities );
				break;
			case "ie":
				this.driver = new InternetExplorerDriver( abilities );
				break;
			case "safari":
				this.driver = new SafariDriver( abilities );
				break;
			default:
				throw new IllegalStateException( "No local browser support for '" + browser + "'." );
			}
	    	Reporter.log( "Finished loading local WebDriver.", true );
		}
		
		@SuppressWarnings("unchecked") // JSONArray using legacy API
		public void setCapabilities() {
			Reporter.log( "Loading WebDriver capabilities for '" + this.browser + "' instance...", true );
			switch ( browser ) {
			case "chrome":
				System.setProperty( "webdriver.chrome.driver", DownloadDriver.CHROMEDRIVER.getAbsolutePath() );
				this.abilities = DesiredCapabilities.chrome();
				break;
			case "firefox":
				this.abilities = DesiredCapabilities.firefox();
				this.abilities.setCapability( CapabilityType.SUPPORTS_JAVASCRIPT, true );
				break;
			case "ie":
				System.setProperty("webdriver.ie.driver","IEDriverServer.exe");
				this.abilities = DesiredCapabilities.internetExplorer();
				break;
			case "safari":
				this.abilities = DesiredCapabilities.safari();
				break;
			case "saucegridchrome31":
				if ( hubUrl.isEmpty() || !hubUrl.contains("@") ) {
					throw new IllegalStateException( "This configuration requires that a SauceLabs formatted hub URL is defined." );
				} else if ( hubUrl.contains("User") && hubUrl.contains("Key") ) {
					hubUrl = hubUrl.replace("User", this.sauceUser );
					hubUrl = hubUrl.replace("Key", this.sauceKey );
					Reporter.log("Using Sauce Labs grid hub url at '" + hubUrl + "'.", true );
				} else {
					Reporter.log("Using raw hub url to connect to SauceLabs hub at '" + hubUrl + "'.", true );
				}
				this.setIsSauce( true );
				this.setIsGrid( true );
				this.abilities = DesiredCapabilities.chrome();
				if ( testName.isEmpty() ) {
					throw new IllegalArgumentException( "SauceLabs tests require that the test name capability be set." );
				} else {
					this.abilities.setCapability( "name", this.testName );
				}
				JSONArray tags = new JSONArray(); 
				    tags.add( this.browser ); 
				    tags.add("Windows 8"); 
				    tags.add("1280x1024"); 
				this.abilities.setCapability( "tags", tags );
				this.abilities.setCapability( "platform", Platform.WIN8 );
				this.abilities.setCapability( "version", "31" );
				this.abilities.setCapability( "screen-resolution", "1280x1024" );
				this.abilities.setCapability( "driver", "ALL" );
				break;
			case "localgridchrome":
				if ( hubUrl.isEmpty() ) {
					throw new IllegalStateException( "The hubUrl must be set to a valid Selenium grid hub." );
				} else {
					Reporter.log("Using raw hub url to connect to Selenium grid hub at '" + hubUrl + "'.", true );
				}
				this.setIsGrid( true );
				this.abilities = DesiredCapabilities.chrome();
				break;
			case "localgridfirefox":
				if ( hubUrl.isEmpty() ) {
					throw new IllegalStateException( "The hubUrl must be set to a valid Selenium grid hub." );
				} else {
					Reporter.log("Using raw hub url to connect to Selenium grid hub at '" + hubUrl + "'.", true );
				}
				this.setIsGrid( true );
				this.abilities = DesiredCapabilities.firefox();
				break;
			default:
				throw new IllegalStateException( "Unsupported browser string '" + browser + "'." );
			}
			Reporter.log("Default application url: " + appUrl, true );
			Reporter.log( "Finished setting up driver capabilities.", true );			
		}
		
		public void setIsGrid( boolean is ) {
			this.isGrid = is;
		}
		
		public void setIsSauce( boolean is ) {
			this.isSauce = is;
		}

		public boolean getIsSauce() {
			return isSauce;
		}		
		
	}
	
}
