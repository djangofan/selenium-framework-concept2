package qa.hs.framework;

import java.io.File;

import org.testng.annotations.BeforeClass;

public abstract class TestBase {

	public static final String directory = "data";
	public static File dataFile;
	
	public String appUrl;
	public Browser browser;
	public String browserVersion;
	public String osType;
	public String resolution;
	protected SeHelper se;
	public String hubUrl;
	
	@BeforeClass 
	public static void setup() {
		dataFile = new File( directory );
	      if ( !new File(directory).exists() ) {
	          new File(directory).mkdir();
	      }
	}
	
}
