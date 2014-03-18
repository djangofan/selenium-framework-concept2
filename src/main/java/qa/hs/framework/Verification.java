package qa.hs.framework;

import java.util.List;
import java.util.Map;

import org.testng.asserts.Assertion;
import org.testng.asserts.IAssert;
import org.testng.collections.Lists;
import org.testng.collections.Maps;

/**
 * A class to generate soft assertions during a test.
 * @author austenjt
 *
 */
public class Verification extends Assertion {

	private Map<AssertionError, IAssert> collectedErrors = Maps.newHashMap();
	private List<String> messages = Lists.newArrayList();

	public void assertAll() {
		if ( !collectedErrors.isEmpty() ) {
			StringBuilder sb = new StringBuilder( "The following assertions failed:\n ");
			boolean first = true;
			for ( Map.Entry<AssertionError, IAssert> ae : collectedErrors.entrySet() ) {
				if ( first ) {
					first = false;
				} else {
					sb.append(", ");
				}
				sb.append( ae.getValue().getMessage() );
			}
			throw new AssertionError( sb.toString() );
		}
	}

	@Override
	public void executeAssert( IAssert a ) {
		try {
			a.doAssert();
		} catch ( AssertionError ae ) {
			collectedErrors.put( ae, a ); // gracefully add error to collection
		}
	}

	public List<String> getMessages() {
		return messages;
	}

	@Override
	public void onBeforeAssert( IAssert ass ) {
		messages.add( "Assertion: " + ass.getMessage() );
	}

}
