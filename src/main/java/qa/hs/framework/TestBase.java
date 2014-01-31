package qa.hs.framework;

import java.io.File;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;

public abstract class TestBase {

	public static final String directory = "data";
	public static File dataFile;	
	
	@BeforeSuite 
	public static void setup() {
		dataFile = new File( directory );
	      if ( !new File(directory).exists() ) {
	          new File(directory).mkdir();
	      }
	}
	
	@AfterTest
	public void afterTest() {
		/* TODO for SauceLabs job update

		String jobID = ((RemoteWebDriver) driver).getSessionId().toString();
		String user = "jausten";
		String accessKey = "";
		Map<String, Object> updates = new HashMap<String, Object>();
		updates.put("name", "this job has a name");
		//updates.put( "passed", true );
		updates.put("build", "c234");
		SauceREST client = new SauceREST( user, accessKey );
		client.updateJobInfo( jobID, updates );
		String jobInfo = client.getJobInfo(args[2]);
		System.out.println("Job info: " + jobInfo);
		client.jobPassed( jobID );
		*/
	}
	
}
