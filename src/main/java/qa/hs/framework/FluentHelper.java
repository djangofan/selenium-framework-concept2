package qa.hs.framework;

import java.net.URL;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.UnexpectedTagNameException;
import org.testng.Assert;

public class FluentHelper 
{    
	public Actions actions; 
	public Matcher m; 
	public Pattern p;
	protected static SeHelper se;
	
	public FluentHelper( SeHelper se ) {
		FluentHelper.se = se;
	}

	/**
	 * Check a checkbox, or radio button.
	 * @param by The element to check.
	 * @return
	 */
	public FluentHelper check( By by ) {
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
	public FluentHelper click( By by ) {
		waitForElement( by ).click();
		return this;
	}

	/**
	 * Close all open windows and stop command chain.
	 * @return
	 */
	public void closeAllWindows() {
		Set<String> windows = se.driver.getWindowHandles();
		if ( windows.size() > 0 ) {
			for ( String window : windows ) {
				try {
					se.driver.switchTo().window( window );
					se.driver.close();
				} catch ( NoSuchWindowException e ) {
					Assert.fail( "Cannot close a window that doesn't exist: " + window );
				}
			}
			se.driver.quit();
		}
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

		if ( e.getTagName().equalsIgnoreCase("input") || e.getTagName().equalsIgnoreCase("select") )
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
	public FluentHelper goBack() {
		se.driver.navigate().back();
		return this;
	}

	/**
	 * Hover over an element.
	 * @param by The element to hover over.
	 * @return
	 */
	public FluentHelper hoverOver( By by ) {
		actions.moveToElement( se.driver.findElement( by ) ).perform();
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
		if ( se.driver.findElements( by ).size() > 0 ) return true;
		return false;
	}

	/**
	 * Navigates to an absolute or relative Url.
	 * @param url Use cases are:<br>
	 * @return this
	 */
	public FluentHelper navigateTo( URL url ) {
		se.driver.navigate().to( url );
		return this;
	}

	/**
	 * Selects an option from a dropdown ({@literal <select> tag}) based on the text displayed.
	 * @param by
	 * @param text The text that is displaying.
	 * @see #selectOptionByValue(By, String)
	 * @return
	 */
	public FluentHelper selectOptionByText( By by, String text ) {
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
	public FluentHelper selectOptionByValue( By by, String value ) {
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
	public FluentHelper setText( By by, String text ) {
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
	public FluentHelper sleep( long milliseconds ) {
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
	public FluentHelper switchToDefaultContent() {
		se.driver.switchTo().defaultContent();
		return this;
	}

	/**
	 * Switches to a frame or iframe.
	 * @param idOrName The id or name of the frame.
	 * @return
	 */
	public FluentHelper switchToFrame( String idOrName ) {
		try {
			se.driver.switchTo().frame( idOrName );
		} catch ( Exception x ) {
			Assert.fail("Couldn't switch to frame with id or name [" + idOrName + "]");
		}
		return this;
	}

	/**
	 * Switch's to a window that is already in existance.
	 * @param regex Regex enabled. Url of the window, or title.
	 * @return
	 */
	public FluentHelper switchToWindow( String regex ) {
		Set<String> windows = se.driver.getWindowHandles();
		for ( String window : windows ) {
			se.driver.switchTo().window(window);
			System.out.println( String.format("#switchToWindow() : title=%s ; url=%s", se.driver.getTitle(), se.driver.getCurrentUrl() ) );
			p = Pattern.compile( regex );
			m = p.matcher( se.driver.getTitle() );
			if ( m.find() ) {
				return this;
			} else {
				m = p.matcher(se.driver.getCurrentUrl());
				if (m.find()) return this;
			}
		}
		Assert.fail( "Could not switch to window with title / url: " + regex );
		return this;
	}

	/**
	 * Uncheck a checkbox, or radio button.
	 * @param by The element to uncheck.
	 * @return
	 */
	public FluentHelper uncheck( By by ) {
		//TODO make sure its a checkbox element or throw exception
		if ( isChecked( by ) ) {
			waitForElement( by ).click();
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
	public FluentHelper validateAttribute(By by, String attr, String regex) {
		String actual = null;
		try {
			actual = se.driver.findElement(by).getAttribute(attr);
			if (actual.equals(regex)) return this; // test passes.
		} catch (NoSuchElementException e) {
			Assert.fail("No such element [" + by.toString() + "] exists.");
		} catch (Exception x) {
			Assert.fail("Cannot validate an attribute if an element doesn't have it!");
		}
		p = Pattern.compile(regex);
		m = p.matcher(actual);
		Assert.assertTrue( m.find(), String.format("Attribute doesn't match! [Selector: %s] [Attribute: %s] [Desired value: %s] [Actual value: %s]", 
				by.toString(), attr, regex,	actual ) );
		return this;
	}

	/**
	 * Validate that a checkbox or a radio button is checked.
	 * @param by
	 * @return
	 */
	public FluentHelper validateChecked( By by ) {
		Assert.assertTrue( isChecked(by), by.toString() + " is not checked!" );
		return this;
	}

	/**
	 * Validates that an element is not present.
	 * @param by
	 * @return
	 */
	public FluentHelper validateNotPresent( By by ) {
		Assert.assertFalse( isPresent(by), "Element " + by.toString() + " exists!" );
		return this;
	}

	/**
	 * Validates that an element is present.
	 * @param by
	 * @return
	 */
	public FluentHelper validatePresent(By by) {
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
	public FluentHelper validateText( By by, String text ) {
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
	public FluentHelper validateTextNot( By by, String text ) {
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
	public FluentHelper validateUnchecked( By by ) {
		Assert.assertFalse( isChecked( by ), by.toString() + " is not unchecked!" );
		return this;
	}

	/**
	 * Validate the Url
	 * @param regex Regular expression to match
	 * @return
	 */
	public FluentHelper validateUrl( String regex ) {
		p = Pattern.compile( regex );
		m = p.matcher( se.driver.getCurrentUrl() );
		Assert.assertTrue( m.find(), "Url does not match regex [" + regex + "] (actual is: \"" + se.driver.getCurrentUrl() + "\")" );
		return this;
	}

	/**
	 * Private method that acts as an arbiter of implicit timeouts of sorts.. sort of like a Wait For Ajax method.
	 */
	private WebElement waitForElement( By by ) {
		int attempts = 0;
		int size = se.driver.findElements( by ).size();        
		while ( size == 0 ) {
			size = se.driver.findElements(by).size();
			if ( attempts > 4 ) Assert.fail( String.format("Could not find %s after %d seconds", by.toString(), 5 ) );
			attempts++;
			sleep(1000);
		}        
		if (size > 1) System.err.println("WARN: There are more than 1 " + by.toString() + " 's!");        
		return se.driver.findElement( by );
	}

	public FluentHelper enterTextIntoField( By locator, String string ) {
		WebElement we = waitForElement( locator );
		if ( !we.getTagName().equals("input") ) throw new IllegalArgumentException("The method enterTextIntoField only takes an 'input' element as an argument.");
		we.sendKeys( string );
		return this;
	}

	public FluentHelper clickElementContainingText( By xpath, String string ) {
		WebElement we = waitForElement( xpath );
		if ( we.getText().contains( string ) ) {
			we.click();
		} else {
			Assert.fail("Couldn't click element.");
		}
		return this;
	}

	public FluentHelper selectFromDropdownByText( By locator, By sublocator, String string ) {
		WebElement dropdown = waitForElement( locator );
		List<WebElement> els = se.driver.findElements( sublocator ); // probably a set of div elements
		System.out.println( "Size: " + els.size() );
		for ( WebElement we : els ) {
			if ( we.getTagName().contains("option") ) {
				// for static option dropdowns use selectOptionByText method instead
				// this should work for the Etsy.com or Google.com dropdowns
				throw new IllegalArgumentException("The method selectFromDropdownByText operates only on non-option div dropdowns.");
			}
			System.out.println("Element: " + we.getText() );
			dropdown.sendKeys( Keys.DOWN);
			sleep(250);			
			if ( we.getText().equalsIgnoreCase( string ) ) {
				we.click();
				break;
			}
		}
		return this;
	}
	
}
