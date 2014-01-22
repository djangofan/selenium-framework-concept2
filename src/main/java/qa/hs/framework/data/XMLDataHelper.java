package qa.hs.framework.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import qa.hs.framework.data.def.SuiteData;
import qa.hs.framework.data.def.TestArguments;
import qa.hs.framework.data.def.TestRow;
import com.thoughtworks.xstream.XStream;

public class XMLDataHelper {
  
  private SuiteData testSuite;
  
  public XMLDataHelper() {
	  System.out.println("XMLDataHelper object ready for you to call the createSuiteFile method...");
  }

  public XMLDataHelper( File testXML ) {
    if ( !testXML.exists() ) {
      System.out.println("Generating new default test data in xml file '" + testXML.getAbsolutePath() + "'.");
      try {
        testXML.createNewFile();
        createDefaultSuiteFile( testXML );
      }
      catch ( IOException e ) {
        e.printStackTrace();
      }
    }
    else {
      System.out.println("Loading test data from xml file '" + testXML.getAbsolutePath() + "'.");
      XStream xStream = new XStream();
      xStream.alias("suite", SuiteData.class);
      xStream.autodetectAnnotations(true);
      Object readObject = xStream.fromXML(testXML);
      testSuite = (SuiteData)readObject;
    }
  }

  public void createDefaultSuiteFile( File testXML ) {
    System.out.println("Calling createDefaultSuiteFile()...");
    SuiteData mySuite = new SuiteData("Suite 1",
        "http://username-string:access-key-string@ondemand.saucelabs.com:80/wd/hub");

    TestArguments tArgs1 = new TestArguments("true", "Test 1", "portal1", "Grid", "Firefox");
    tArgs1.setTestArgument("url", "java.lang.String", "http://google.com");
    TestRow tCase1 = new TestRow( tArgs1 );
    mySuite.add( tCase1 );

    TestArguments tArgs2 = new TestArguments("true", "Test 2", "portal2", "Local", "Chrome");
    tArgs2.setTestArgument("url", "java.lang.String", "http://yelp.com");
    TestRow tCase2 = new TestRow( tArgs2 );
    mySuite.add( tCase2 );

    XStream xstream = new XStream();
    xstream.autodetectAnnotations(true);
    String xml = xstream.toXML(mySuite);
    System.out.println(xml);
    FileWriter fw;
    try {
      fw = new FileWriter( testXML );
      fw.append(xml);
      fw.close();
    } catch ( IOException ioe ) {
      ioe.printStackTrace();
    }
    testSuite = mySuite;
  }
  
  public void createSuiteFile( File testXML, String suiteName, String suiteUrl, TestRow... args ) {
	    System.out.println("Calling createDefaultSuiteFile()...");
	    SuiteData mySuite = new SuiteData( suiteName, suiteUrl );
	    for ( TestRow tr : args ) {
	        mySuite.add( tr );
	    }
	    XStream xstream = new XStream();
	    xstream.autodetectAnnotations(true);
	    String xml = xstream.toXML(mySuite);
	    System.out.println(xml);
	    FileWriter fw;
	    try {
	      fw = new FileWriter( testXML );
	      fw.append(xml);
	      fw.close();
	    } catch ( IOException ioe ) {
	      ioe.printStackTrace();
	    }
	    testSuite = mySuite;
	  }

  public String getBrowserByIndex(int idx) {
    return this.getTestArgsByIndex(idx).getBrowser();
  }

  public Boolean getEnabledByIndex(int idx) {
    return this.getTestArgsByIndex(idx).getEnabled();
  }

  public String getEnvironmentByIndex(int idx) {
    return this.getTestArgsByIndex(idx).getEnvironment();
  }

  public String getTestLocaleByIndex(int idx) {
    return this.getTestArgsByIndex(idx).getTestLocale();
  }

  public String getTestNameByIndex(int idx) {
    return this.getTestArgsByIndex(idx).getTestName();
  }

  /**
   * This is a TestNG DataProvider implementation
   * Should return an Object[][] or Iterator<Object[]> data type
   * 
   * @return
   */
  public Object[][] getArgumentsArrays() {
    Object[][] array = new Object[testSuite.size()][1];
    int testCount = 0;
    for (TestRow ta : testSuite) {
      array[testCount][0] = ta.getTestArgs();
      testCount++;
    }
    return array;
  }

  // TODO method might be redundant
  private TestArguments getTestArgsByIndex(int testIdx) {
    return testSuite.getAllTests().get(testIdx).getTestArgs();
  }

}
