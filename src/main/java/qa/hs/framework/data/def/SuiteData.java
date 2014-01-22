package qa.hs.framework.data.def;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("suite")
public class SuiteData implements Iterable<TestRow> {

    private String suiteName;
    private String sauceURL;
    private List<TestRow> tests = new ArrayList<TestRow>();

    public SuiteData( String sName, String sauceUrl ) {
        this.suiteName = sName;
        this.sauceURL = sauceUrl;
    }

    public void add( TestRow test ) {
        tests.add( test );
    }

    public List<TestRow> getAllTests() {
        return tests;
    }

    public String getSauceUrl() {
		return sauceURL;
	}

    public String getSuiteName() {
		return suiteName;
	}

    public TestRow getTestByIndex( int idx ) {
    	if ( idx > tests.size() || idx < 0 ) {
    		throw new IndexOutOfBoundsException("Index " + idx + " was beyond the range of " + tests.size() + " test cases." );
    	}
    	return tests.get( idx );
    }

	public TestRow getTestByName( String name ) {
    	for ( TestRow tr: tests ) {
    		String testName = tr.getTestArgs().getTestName();
    		if ( testName.equalsIgnoreCase( name ) ) {
    			System.out.println("Found test by its name: " + testName );
        		return tr;
    		}
    	}
    	return null;
    }

	public int size() {
    return tests.size();
    }

	@Override
	public Iterator<TestRow> iterator() {
		Iterator<TestRow> itests = tests.iterator();
    return itests;

	}

}
