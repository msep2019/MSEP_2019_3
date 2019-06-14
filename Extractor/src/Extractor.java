import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;

import gate.creole.SerialAnalyserController;
import gate.util.GateException;

public class Extractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException {

		long startTime = System.currentTimeMillis();

		BasicConfigurator.configure();

		// get location of all resources
		File log = new File("log/txt/Original simulation Log Files.txt");
		System.out.println("Number of processing files:" + 1);

		// identify structs for annotation types
		ArrayList<SoSSecAgent> agents = new ArrayList<SoSSecAgent>();

		// call document analysing
		agents = DocumentAnalyser.processDocs(log);

		// call file writer to write the XML file
		//FileOutput.atWriter(agents);
		//ExcelReaderXmlWriter.writeXml();

		long endTime = System.currentTimeMillis();
		System.out.println("Total time in processing" + " SoSSec is" + (endTime - startTime) + "ms.");
	}
}
