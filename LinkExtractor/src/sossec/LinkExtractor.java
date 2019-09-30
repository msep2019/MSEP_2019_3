package sossec;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;
import gate.util.GateException;

import sossec.cve.CVEHelper;
import sossec.cwe.CWEHelper;
import sossec.capec.CAPECHelper;
import sossec.keyword.KeywordDocumentAnalyser;

public class LinkExtractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException {

		CVEHelper helperCVE = new CVEHelper();

		String result = helperCVE.getItemContent(args[0]);

		System.out.println(result);

		BasicConfigurator.configure();

		if (!result.isEmpty()) {
			ArrayList<String> keywords = KeywordDocumentAnalyser.processDocs(result);
		}
	}
}
