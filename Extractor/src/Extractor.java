import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;

import gate.util.GateException;

public class Extractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException {

		long startTime = System.currentTimeMillis();

		BasicConfigurator.configure();

		// get location of all resources
		File log = new File("log/txt/Original simulation Log Files.txt");
		System.out.println("Number of processing files: " + 1);

		// identify structs for annotation types
		HashMap<String, SoSSecAgent> agents = new HashMap<String, SoSSecAgent>();

		// call document analysing
		agents = DocumentAnalyser.processDocs(log);

		// call file writer to write the XML file
		//FileOutput.atWriter(agents);
		//ExcelReaderXmlWriter.writeXml();
		Exporter export = new Exporter();
		export.exportXMI(agents);

		long endTime = System.currentTimeMillis();
		System.out.println("Total time in processing" + " SoSSec is" + (endTime - startTime) + "ms.");
	}
}
