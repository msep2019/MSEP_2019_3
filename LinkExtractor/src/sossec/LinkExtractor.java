package sossec;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.log4j.BasicConfigurator;

import gate.util.GateException;
import sossec.cve.CVEHelper;
import sossec.keyword.KeywordDocumentAnalyser;

public class LinkExtractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException {

		CVEHelper helper = new CVEHelper();
		
		String result = helper.getCVEContent(args[0]);
		
		System.out.println(result);
		
		
		BasicConfigurator.configure();
		
		ArrayList<String> agents = KeywordDocumentAnalyser.processDocs(result);

		/*
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
		*/
	}
}
