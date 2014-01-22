package qa.hs.framework;

import java.util.List;
import java.util.Map;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
//import org.testng.reporters.TestHTMLReporter;
import org.testng.xml.XmlSuite;

public class CustomReportListener implements IReporter {

	@Override
	public void generateReport( List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory ) {
		System.out.println();
		//Iterating over each suite included in the test
		for ( ISuite suite : suites ) {
			//Following code gets the suite name
			String suiteName = suite.getName();
			//Getting the results for the said suite
			Map<String, ISuiteResult> suiteResults = suite.getResults();
			for ( ISuiteResult sr : suiteResults.values() ) {
				ITestContext tc = sr.getTestContext();
				System.out.println( "Passed tests for suite '" + suiteName + "' is:" + 
				    tc.getPassedTests().getAllResults().size() );
				System.out.println( "Failed tests for suite '" + suiteName + "' is:" + 
				    tc.getFailedTests().getAllResults().size() );
				System.out.println( "Skipped tests for suite '" + suiteName + "' is:" + 
				    tc.getSkippedTests().getAllResults().size() );
			}
		}
		CustomReport cr = new CustomReport();
		cr.generateReport( xmlSuites, suites, outputDirectory );
		System.out.println();
	}

}

