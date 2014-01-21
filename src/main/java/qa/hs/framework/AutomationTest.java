package qa.hs.framework;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;

public class AutomationTest 
{    
	public Actions actions; 
	public String baseUrl;
	public Browser browser;
	public String browserVersion;
	public String osType;
	public WebDriver driver;
	public String hub;  
	private int attempts = 0;
	private Config configuration;
	private Matcher m;    
	private final int MAX_ATTEMPTS = 5;  
	private final File CHROMEDRIVER = new File("chromedriver.exe");
	private final File CHROMEDRIVERZIP = new File("chromedriver_win32.zip");

	private Pattern p;

	public Map<String, By> props = new HashMap<String, By>();
	private String resolution;

	public AutomationTest() 
	{
		configuration = getClass().getAnnotation( Config.class );
		baseUrl = configuration.url();
		osType = configuration.osType();
		browser = configuration.browser();
		browserVersion = configuration.browserVersion();    
		resolution = configuration.resolution();
		hub = configuration.hub();

		if ( browser == null ) throw new IllegalStateException( "Problem getting Browser object from properties.");
		boolean isLocal = hub.trim().isEmpty() ? true : false;        

		DesiredCapabilities abilities = null;

		switch ( browser ) {
		case CHROME:
			getLatestWindowsChromeDriver();
			System.setProperty("webdriver.chrome.driver", CHROMEDRIVER.getAbsolutePath() );
			abilities = DesiredCapabilities.chrome();
			//ChromeOptions options = new ChromeOptions();
			//options.addExtensions( new File( "Adblock-Plus_v1.4.1.crx" ) );
			//abilities.setCapability( ChromeOptions.CAPABILITY, options );
			abilities.setCapability( "platform", osType );
			abilities.setCapability( "version", browserVersion );
			if ( isLocal ) driver = new ChromeDriver( abilities );
			break;
		case FIREFOX:
			abilities = DesiredCapabilities.firefox();
			abilities.setCapability( "platform", osType );
			abilities.setCapability( "version", browserVersion );
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
			if ( isLocal ) driver = new SafariDriver( abilities );
			break;
		default:
			System.err.println("Unknown browser: " + configuration.browser());
			return;
		}

		//driver.manage().timeouts().pageLoadTimeout( 4, TimeUnit.SECONDS );

		if ( !isLocal ) {
			System.out.println( "Configured hub url is '" + hub + "'" );
			try {
				driver = new RemoteWebDriver( new URL( configuration.hub() ), abilities );
			} catch ( Exception x ) {
				System.err.println( "Couldn't connect to hub: " + configuration.hub() );
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
		driver.navigate().to( baseUrl );
		//TODO augmenter
	}

	private void getLatestWindowsChromeDriver() {
		if ( !CHROMEDRIVER.exists() ) {	
			FileOutputStream fos;
			InputStream in;
			try {
				URL downloadUrl = new URL("http://chromedriver.storage.googleapis.com/index.html?path=2.8/chromedriver_win32.zip");
				URLConnection conn = downloadUrl.openConnection();
				in = conn.getInputStream();
				fos = new FileOutputStream( CHROMEDRIVERZIP.getAbsoluteFile() );
				byte[] b = new byte[1024];
				int count;
				while ( ( count = in.read(b) ) >= 0 ) {
					fos.write(b, 0, count);
				}
				fos.flush();
				fos.close();
				in.close();
			} catch ( FileNotFoundException e ) {
				e.printStackTrace();
			} catch ( IOException e ) {
				e.printStackTrace();
			} finally {
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

	public static void unzip( String source, String destination, String password ) {
		//String source = "some/compressed/file.zip";
		//String destination = "some/destination/folder";
		//String password = "password";
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

	/**
	 * Check a checkbox, or radio button.
	 * @param by The element to check.
	 * @return
	 */
	public AutomationTest check( By by ) {
		if ( !isChecked( by ) ) {
			waitForElement(by).click();
			Assert.assertTrue( isChecked( by ), "Locator '" + by.toString() + "' did not check!" );
		}
		return this;
	}

	/**
	 * Click an element.
	 * @param by The element to click.
	 * @return
	 */
	public AutomationTest click( By by ) {
		waitForElement( by ).click();
		return this;
	}

	/**
	 * Close all open windows and stop command chain.
	 * @return
	 */
	public void closeAllWindows() {
		Set<String> windows = driver.getWindowHandles();
		if ( windows.size() > 0 ) {
			for ( String window : windows ) {
				try {
					driver.switchTo().window( window );
					driver.close();
				} catch ( NoSuchWindowException e ) {
					Assert.fail( "Cannot close a window that doesn't exist: " + window );
				}
			}
			driver.quit();
		}
	}

	/**
	 * Closes the current active window.  Calling this method will return the context back to the initial window.
	 * @return
	 */
	public AutomationTest closeWindow() {
		return closeWindow( null );
	}

	/**
	 * Close an open window.
	 * <br>
	 * If you have opened only 1 external window, then when you call this method, the context will switch back to 
	 * the window you were using before.<br>
	 * <br>
	 * If you had more than 2 windows displaying, then you will need to call {@link #switchToWindow(String)} to 
	 * switch back context.
	 * @param regex The title of the window to close (regex enabled). You may specify <code>null</code> to close 
	 * the active window. If you specify <code>null</code> then the context will switch back to the initial window.
	 * @return
	 */
	public AutomationTest closeWindow( String regex ) {
		if ( regex == null ) {
			driver.close();    		
			if ( driver.getWindowHandles().size() == 1 ) {
				driver.switchTo().window( driver.getWindowHandles().iterator().next() );
			}
			return this;
		}

		Set<String> windows = driver.getWindowHandles();

		for ( String window : windows ) {
			try {
				driver.switchTo().window(window);

				p = Pattern.compile(regex);
				m = p.matcher( driver.getTitle() );

				if ( m.find() ) {
					switchToWindow(regex); // switch to the window, then close it.
					driver.close();

					if ( windows.size() == 2 ) { // just default back to the first window.
						driver.switchTo().window( windows.iterator().next() );
					}
				} else {
					m = p.matcher( driver.getCurrentUrl() );
					if ( m.find() ) {
						switchToWindow(regex);
						driver.close();

						if (windows.size() == 2) driver.switchTo().window( windows.iterator().next() );
					}
				}

			} catch ( NoSuchWindowException e ) {
				Assert.fail( "Cannot close a window that doesn't exist. [" + regex + "]" );
			}
		}
		return this;
	}

	/**
	 * Get the text of an element.
	 * <blockquote>This is a consolidated method that works on input's, as select boxes, and fetches the value 
	 * rather than the innerHTMl.</blockquote>
	 * @param by
	 * @return
	 */
	public String getText(By by) {
		String text = null;
		WebElement e = waitForElement(by);

		if (e.getTagName().equalsIgnoreCase("input") || e.getTagName().equalsIgnoreCase("select"))
			text = e.getAttribute("value");
		else
			text = e.getText();

		return text;
	}

	/**
	 * Navigates the browser back one page.
	 * Same as <code>driver.navigate().back()</navigate>
	 * @return
	 */
	public AutomationTest goBack() {
		driver.navigate().back();
		return this;
	}

	/**
	 * Hover over an element.
	 * @param by The element to hover over.
	 * @return
	 */
	public AutomationTest hoverOver( By by ) {
		actions.moveToElement( driver.findElement( by ) ).perform();
		return this;
	}

	/**
	 * Checks if the element is checked or not.
	 * @param by
	 * @return <i>this method is not meant to be used fluently.</i><br><br>
	 * Returns <code>true</code> if the element is checked. and <code>false</code> if it's not.
	 */
	public boolean isChecked( By by ) {
		return waitForElement( by ).isSelected();
	}

	/**
	 * Checks if the element is present or not.<br>
	 * @param by
	 * @return <i>this method is not meant to be used fluently.</i><br><br.
	 * Returns <code>true</code> if the element is present. and <code>false</code> if it's not.
	 */
	public boolean isPresent( By by ) {
		if ( driver.findElements( by ).size() > 0 ) return true;
		return false;
	}

	/**
	 * Log something to 'out'
	 * @param object What to log.
	 * @return
	 */
	public AutomationTest log( Object object ) {
		System.out.println( object );
		return this;
	}

	/**
	 * Navigates to an absolute or relative Url.
	 * @param url Use cases are:<br>
	 * <blockquote>
	 * <code>navigateTo("/login") // navigate to a relative url. slash meaning start fresh from the base url.</code><br><br>
	 * <code>navigateTo("path") // navigate to a relative url. will simply append "path" to the current url.</code><br><br>
	 * <code>navigateTo("http://google.com") // navigates to an absolute url.</code>
	 * </blockquote>
	 * @return
	 */
	public AutomationTest navigateTo( String url ) {
		if ( url.contains("://") ) {
			driver.navigate().to( url );
		} else if ( url.startsWith("/") ) {
			driver.navigate().to( baseUrl.concat( url ) );
		} else {
			driver.navigate().to( driver.getCurrentUrl().concat( url ) );
		}        
		return this;
	}

	/**
	 * Selects an option from a dropdown ({@literal <select> tag}) based on the text displayed.
	 * @param by
	 * @param text The text that is displaying.
	 * @see #selectOptionByValue(By, String)
	 * @return
	 */
	public AutomationTest selectOptionByText( By by, String text ) {
		try {
			Select box = new Select( waitForElement( by ) );
			box.selectByVisibleText( text );
		} catch ( UnexpectedTagNameException tne ) {
			System.out.println("The non-standard select box you passed to this method might not contain 'option' elements.");
			tne.printStackTrace();
		}
		return this;
	}

	/**
	 * Selects an option from a dropdown ({@literal <select> tag}) based on the value.
	 * @param by
	 * @param value The <code>value</code> attribute of the option.
	 * @see #selectOptionByText(By, String)
	 * @return
	 */
	public AutomationTest selectOptionByValue( By by, String value ) {
		try {
			Select box = new Select( waitForElement( by ) );
			box.selectByValue( value );
		} catch ( UnexpectedTagNameException tne ) {
			System.out.println("The non-standard select box you passed to this method might not contain 'option' elements.");
			tne.printStackTrace();
		}
		return this;
	}

	/**
	 * Clears the text from a text field, and sets it.
	 * @param by The element to set the text of.
	 * @param text The text that the element will have.
	 * @return
	 */
	public AutomationTest setText( By by, String text ) {
		System.out.println( by.toString() );
		WebElement element = waitForElement( by );
		element.clear();
		element.sendKeys( text );
		return this;
	}

	/**
	 * Validate the Url
	 * @param regex Regular expression to match
	 * @return
	 */
	public AutomationTest sleep( long milliseconds ) {
		if ( milliseconds > 900000 ) throw new IllegalArgumentException("Method 'sleep()' refuses to wait longer than 15 minutes.");
		try {
			Thread.sleep( milliseconds );
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		return this;
	}

	/**
	 * Switch back to the default content (the first window / frame that you were on before switching)
	 * @return
	 */
	public AutomationTest switchToDefaultContent() {
		driver.switchTo().defaultContent();
		return this;
	}

	/**
	 * Switches to a frame or iframe.
	 * @param idOrName The id or name of the frame.
	 * @return
	 */
	public AutomationTest switchToFrame(String idOrName) {
		try {
			driver.switchTo().frame(idOrName);
		} catch (Exception x) {
			Assert.fail("Couldn't switch to frame with id or name [" + idOrName + "]");
		}
		return this;
	}

	/**
	 * Switch's to a window that is already in existance.
	 * @param regex Regex enabled. Url of the window, or title.
	 * @return
	 */
	public AutomationTest switchToWindow(String regex) {
		Set<String> windows = driver.getWindowHandles();

		for (String window : windows) {
			driver.switchTo().window(window);
			System.out.println(String.format("#switchToWindow() : title=%s ; url=%s",
					driver.getTitle(),
					driver.getCurrentUrl()));

			p = Pattern.compile(regex);
			m = p.matcher(driver.getTitle());

			if (m.find()) return this;
			else {
				m = p.matcher(driver.getCurrentUrl());
				if (m.find()) return this;
			}
		}

		Assert.fail("Could not switch to window with title / url: " + regex);
		return this;
	}

	/* ************************ */

	/* Validation Functions for Testing */

	@AfterTest
	public void teardown() {
		driver.quit();
	}

	/**
	 * Uncheck a checkbox, or radio button.
	 * @param by The element to uncheck.
	 * @return
	 */
	public AutomationTest uncheck(By by) {
		if (isChecked(by)) {
			waitForElement(by).click();
			Assert.assertFalse( isChecked(by), by.toString() + " did not uncheck!");
		}
		return this;
	}

	/**
	 * Validates an attribute of an element.<br><br>
	 * Example:<br>
	 * <blockquote>
	 * {@literal <input type="text" id="test" />}
	 * <br><br>
	 * <code>.validateAttribute(css("input#test"), "type", "text") // validates that the "type" attribute equals "test"</code>
	 * </blockquote>
	 * @param by The element
	 * @param attr The attribute you'd like to validate
	 * @param regex What the attribute <b>should</b> be.  (this method supports regex)
	 * @return
	 */
	public AutomationTest validateAttribute(By by, String attr, String regex) {
		String actual = null;
		try {
			actual = driver.findElement(by).getAttribute(attr);
			if (actual.equals(regex)) return this; // test passes.
		} catch (NoSuchElementException e) {
			Assert.fail("No such element [" + by.toString() + "] exists.");
		} catch (Exception x) {
			Assert.fail("Cannot validate an attribute if an element doesn't have it!");
		}

		p = Pattern.compile(regex);
		m = p.matcher(actual);

		Assert.assertTrue( m.find(), String.format("Attribute doesn't match! [Selector: %s] [Attribute: %s] [Desired value: %s] [Actual value: %s]", 
				by.toString(),
				attr,
				regex,
				actual
				) );

		return this;
	}

	/**
	 * Validate that a checkbox or a radio button is checked.
	 * @param by
	 * @return
	 */
	public AutomationTest validateChecked(By by) {
		Assert.assertTrue( isChecked(by), by.toString() + " is not checked!" );
		return this;
	}

	/**
	 * Validates that an element is not present.
	 * @param by
	 * @return
	 */
	public AutomationTest validateNotPresent(By by) {
		Assert.assertFalse( isPresent(by), "Element " + by.toString() + " exists!");
		return this;
	}

	/**
	 * Validates that an element is present.
	 * @param by
	 * @return
	 */
	public AutomationTest validatePresent(By by) {
		waitForElement(by);
		Assert.assertTrue( isPresent(by), "Element " + by.toString() + " does not exist!" );
		return this;
	}

	/**
	 * Validate that the text of an element is correct.
	 * @param by The element to validate the text of.
	 * @param text The text to validate.
	 * @return
	 */
	public AutomationTest validateText(By by, String text) {
		String actual = getText(by);

		Assert.assertTrue( text.equals(actual), String.format("Text does not match! [expected: %s] [actual: %s]", text, actual) );
		return this;
	}

	/**
	 * Validate that the text of an element is not matching text.
	 * @param by The element to validate the text of.
	 * @param text The text to validate.
	 * @return
	 */
	public AutomationTest validateTextNot(By by, String text) {
		String actual = getText(by);

		Assert.assertFalse( text.equals(actual), String.format("Text matches! [expected: %s] [actual: %s]", text, actual) );
		return this;
	}

	/* ================================ */

	/**
	 * Validate that a checkbox or a radio button is unchecked.
	 * @param by
	 * @return
	 */
	public AutomationTest validateUnchecked(By by) {
		Assert.assertFalse( isChecked(by), by.toString() + " is not unchecked!" );
		return this;
	}

	/**
	 * Validate the Url
	 * @param regex Regular expression to match
	 * @return
	 */
	public AutomationTest validateUrl(String regex) {
		p = Pattern.compile(regex);
		m = p.matcher(driver.getCurrentUrl());

		Assert.assertTrue( m.find(), "Url does not match regex [" + regex + "] (actual is: \""+driver.getCurrentUrl()+"\")" );
		return this;
	}

	/**
	 * Private method that acts as an arbiter of implicit timeouts of sorts.. sort of like a Wait For Ajax method.
	 */
	private WebElement waitForElement(By by) {
		int attempts = 0;
		int size = driver.findElements(by).size();        
		while ( size == 0 ) {
			size = driver.findElements(by).size();
			if ( attempts == MAX_ATTEMPTS ) Assert.fail( String.format("Could not find %s after %d seconds", by.toString(), MAX_ATTEMPTS ) );
			attempts++;
			sleep(1000);
		}        
		if (size > 1) System.err.println("WARN: There are more than 1 " + by.toString() + " 's!");        
		return driver.findElement(by);
	}

	/**
	 * Waits for a window to appear, then switches to it.
	 * @param regex Regex enabled. Url of the window, or title.
	 * @return
	 */
	public AutomationTest waitForWindow(String regex) {
		Set<String> windows = driver.getWindowHandles();

		for (String window : windows) {
			try {
				driver.switchTo().window(window);

				p = Pattern.compile(regex);
				m = p.matcher(driver.getCurrentUrl());

				if (m.find()) {
					attempts = 0;
					return switchToWindow(regex);
				}
				else {
					// try for title
					m = p.matcher(driver.getTitle());

					if (m.find()) {
						attempts = 0;
						return switchToWindow(regex);
					}
				}
			} catch ( NoSuchWindowException e) {
				if ( attempts <= MAX_ATTEMPTS ) {
					attempts++;
					sleep(1000);
					return waitForWindow( regex );
				} else {
					Assert.fail("Window with url|title: " + regex + " did not appear after " + MAX_ATTEMPTS + " tries. Exiting.");
				}
			}
		}

		// when we reach this point, that means no window exists with that title..
		if (attempts == MAX_ATTEMPTS) {
			Assert.fail("Window with title: " + regex + " did not appear after 5 tries. Exiting.");
			return this;
		} else {
			System.out.println("#waitForWindow() : Window doesn't exist yet. [" + regex + "] Trying again. " + attempts + "/" + MAX_ATTEMPTS);
			attempts++;
			return waitForWindow(regex);
		}
	}

	public AutomationTest enterTextIntoField( By locator, String string ) {
		WebElement we = waitForElement( locator );
		if ( !we.getTagName().equals("input") ) throw new IllegalArgumentException("The method enterTextIntoField only takes an 'input' element as an argument.");
		we.sendKeys( string );
		return this;
	}

	public AutomationTest clickElementContainingText( By xpath, String string ) {
		WebElement we = waitForElement( xpath );
		if ( we.getText().contains( string ) ) {
			we.click();
		} else {
			Assert.fail("Couldn't click element.");
		}
		return this;
	}

	public AutomationTest selectItemByText( By locator, By sublocator, String string ) {
		List<WebElement> els = waitForElement( locator ).findElements( sublocator );
		for ( WebElement we : els ) {
			if ( we.getText().equals( string ) ) we.click();
		}
		return this;
	}
}
