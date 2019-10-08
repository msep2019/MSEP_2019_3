package sossec.keywordmatching;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer;

import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.Plugin;
import gate.creole.SerialAnalyserController;
import gate.util.GateException;

public class KeywordMatchingResourcesLoader {
	public static SerialAnalyserController getResources(File fileKeywordsDef, String pathJape)
			throws GateException, IOException, URISyntaxException {

		Gate.init();

		// Load the ANNIE plugin
		Plugin anniePlugin = new Plugin.Maven("uk.ac.gate.plugins", "annie", gate.Main.version);
		Gate.getCreoleRegister().registerPlugin(anniePlugin);
		
		Plugin toolsPlugin = new Plugin.Maven("uk.ac.gate.plugins", "tools", gate.Main.version);
		Gate.getCreoleRegister().registerPlugin(toolsPlugin);

		// Setting up searialAnalyserController
		SerialAnalyserController sac = (SerialAnalyserController) Factory
				.createResource("gate.creole.SerialAnalyserController");

		// Setting up processing resources, only tokeniser needed
		ProcessingResource documentReset = (ProcessingResource) Factory
				.createResource("gate.creole.annotdelete.AnnotationDeletePR");
		ProcessingResource defaultTokeniser = (ProcessingResource) Factory
				.createResource("gate.creole.tokeniser.DefaultTokeniser");
		ProcessingResource sentenceSplitter = (ProcessingResource) Factory
				.createResource("gate.creole.splitter.SentenceSplitter");
		ProcessingResource posTagger = (ProcessingResource) Factory
				.createResource("gate.creole.POSTagger");
		
		// Loading Gazetteer;
		System.out.println("\n----Loading ExtendedGazetteer----");
		FeatureMap parms = Factory.newFeatureMap();
		URL gazURL = fileKeywordsDef.toURI().toURL();
		parms.put("configFileURL", gazURL);
		ExtendedGazetteer eg = (ExtendedGazetteer) Factory
				.createResource("com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer", parms);
		eg.setLongestMatchOnly(false);
		eg.setMatchAtWordStartOnly(false);
		eg.setMatchAtWordEndOnly(false);
		
		FeatureMap paramsASTransfer = Factory.newFeatureMap(); 
		List<String> annoList = new ArrayList<String>(); 
		annoList.add("Lookup");
        
		paramsASTransfer.put("annotationTypes", annoList);
		paramsASTransfer.put("copyAnnotations", true);
		paramsASTransfer.put("outputASName", "Original markups");
		paramsASTransfer.put("tagASName", "Original markups");
		paramsASTransfer.put("textTagName", "Weakness");
		ProcessingResource annoTransfer = (ProcessingResource) Factory
				.createResource("gate.creole.annotransfer.AnnotationSetTransfer", paramsASTransfer);

		// Specifying location of JAPE files
		FeatureMap japeFeature = Factory.newFeatureMap();
		japeFeature.put("grammarURL", new File(pathJape).toURI().toURL());
		japeFeature.put("inputASName", "Original markups");

		// Loading JAPE language resources with specified features
		LanguageAnalyser tagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				japeFeature, parms, "JAPE Transducer");

		sac.add(documentReset);
		sac.add(sentenceSplitter);
		sac.add(defaultTokeniser);
		sac.add(posTagger);
		sac.add(eg);
		sac.add(annoTransfer);
		
		sac.add(tagJape);

		return sac;
	}
}
