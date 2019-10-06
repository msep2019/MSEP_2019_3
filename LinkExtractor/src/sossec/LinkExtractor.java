package sossec;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;
import gate.util.GateException;

import sossec.cve.CVEHelper;
import sossec.cwe.CWEHelper;
import sossec.capec.CAPECHelper;
import sossec.keyword.Keyword;
import sossec.keywordmatching.KeywordMatching;

public class LinkExtractor {
	public static void main(String[] args) throws GateException, IOException, URISyntaxException {
		ArrayList<String> cveKeywords = new ArrayList<>();
		File fileCVEKeywordDef = null;
		
		CVEHelper cveHelper = new CVEHelper();
		CWEHelper cweHelper = new CWEHelper();
		CAPECHelper capecHelper = new CAPECHelper();

		String result = cveHelper.getItemContent(args[0]);

		System.out.println(result);

		BasicConfigurator.configure();

		if (!result.isEmpty()) {
			cveKeywords = Keyword.processDocs(result);
		}
		
		if (cveKeywords.size() > 0) {
			fileCVEKeywordDef = cveHelper.generateCVEGazetteer(cveKeywords);
		}
		
		if (fileCVEKeywordDef != null) {
			KeywordMatching.processDocs(cweHelper.xmlFiles, fileCVEKeywordDef);
		}
	}
}
