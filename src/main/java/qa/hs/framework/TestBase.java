package qa.hs.framework;

import java.io.File;

import org.testng.Reporter;
import org.testng.annotations.BeforeSuite;

public abstract class TestBase {	

	private final String directory = "data";
	private SeHelper se;
	private File dataFile;	
	
	@BeforeSuite 
	public void setUp() {
		Reporter.log( "Calling BeforeSuite setUp method." );
		setDataFile( new File( directory ) );
	      if ( !new File(directory).exists() ) {
	    	  Reporter.log( "Creating data directory." );
	          new File( directory ).mkdir();
	      }
	}
	
	public String getDirectory() {
		return this.directory;
	}
	
	public void setHelper( SeHelper se ) {
		this.se = se;		
	}	

	public SeHelper getHelper() {
		return se;
	}
	
	public SeUtil getUtil() {
		return se.getUtil();
	}

	public File getDataFile() {
		return this.dataFile;
	}

	public void setDataFile(File dataFile) {
		this.dataFile = dataFile;
	}
	
}
