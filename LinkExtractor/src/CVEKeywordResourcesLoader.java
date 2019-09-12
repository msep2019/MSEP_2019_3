import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.Plugin;
import gate.creole.SerialAnalyserController;
//import gate.gui.MainFrame;
import gate.util.GateException;

public class CVEKeywordResourcesLoader {
	public static SerialAnalyserController getResources() throws GateException, IOException, URISyntaxException {

		Gate.init();

		// Load the ANNIE plugin
		Plugin anniePlugin = new Plugin.Maven("uk.ac.gate.plugins", "annie", gate.Main.version);
		Gate.getCreoleRegister().registerPlugin(anniePlugin);

		Plugin GzPlugin = new Plugin.Maven("uk.ac.gate.plugins", "stringannotation", "4.0");
		Gate.getCreoleRegister().registerPlugin(GzPlugin);

		// Setting up searialAnalyserController
		SerialAnalyserController sac = (SerialAnalyserController) Factory
				.createResource("gate.creole.SerialAnalyserController");

		// Setting up processing resources, only tokeniser needed
		ProcessingResource documentReset = (ProcessingResource) Factory
				.createResource("gate.creole.annotdelete.AnnotationDeletePR");
		ProcessingResource defaultTokeniser = (ProcessingResource) Factory
				.createResource("gate.creole.tokeniser.SimpleTokeniser");
		ProcessingResource sentenceSplitter = (ProcessingResource) Factory
				.createResource("gate.creole.splitter.SentenceSplitter");
		ProcessingResource posTagger = (ProcessingResource) Factory
				.createResource("gate.creole.POSTagger");
		
		FeatureMap parms = Factory.newFeatureMap();
		/*
		File gz = new File("log/gazetteer/sossec.def");
		URL gazURL = gz.toURI().toURL();
		parms.put("configFileURL", gazURL);
		*/

		// Specifying location of JAPE files
		FeatureMap cveKeywordFeature = Factory.newFeatureMap();
		cveKeywordFeature.put("grammarURL", new File("src/gate/jape/get-keywords.jape").toURI().toURL());

		// Loading JAPE language resources with specified features
		LanguageAnalyser cveKeywordTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				cveKeywordFeature, parms, "JAPE Transducer - CVE Keywords");

		sac.add(documentReset);
		sac.add(sentenceSplitter);
		sac.add(defaultTokeniser);
		sac.add(posTagger);

		sac.add(cveKeywordTagJape);

		return sac;
	}
}
