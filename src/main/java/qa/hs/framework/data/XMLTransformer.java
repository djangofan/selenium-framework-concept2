package qa.hs.framework.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLTransformer {
	
	private static final File directory = new File("data"); // subdir where transformations occur
	
	public XMLTransformer() {
		// does nothing
	}

	public static void generateHtmlFromInputXML( String testFile, String styleSheetFile, String outHtmlFile ) {
		try {
			TransformerFactory tFactory = TransformerFactory.newInstance();
			Source xslDoc = new StreamSource( new File ( styleSheetFile ) );
			Source xmlDoc = new StreamSource( new File( directory + File.separator + testFile ) );
			File outFile = new File( directory + File.separator + outHtmlFile );
			if ( outFile.exists() ) outFile.delete(); // delete before regenerating
			OutputStream htmlFile = new FileOutputStream( outFile );
			Transformer tranzform = tFactory.newTransformer( xslDoc );
			tranzform.transform( xmlDoc, new StreamResult( htmlFile ) );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
