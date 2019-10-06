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
import gate.util.GateException;

public class ResourcesLoader {
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

		// Loading Gazetteer;
		System.out.println("\n----Loading ExtendedGazetteer----");
		File gz = new File("log/gazetteer/sossec.def");
		FeatureMap parms = Factory.newFeatureMap();
		URL gazURL = gz.toURI().toURL();
		parms.put("configFileURL", gazURL);
		ExtendedGazetteer eg = (ExtendedGazetteer) Factory
				.createResource("com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer", parms);
		eg.setLongestMatchOnly(false);
		eg.setMatchAtWordStartOnly(false);
		eg.setMatchAtWordEndOnly(false);

		// Specifying location of JAPE files
		FeatureMap agentFeature = Factory.newFeatureMap();
		agentFeature.put("grammarURL", new File("log/jape/AgentRule.jape").toURI().toURL());

		FeatureMap behaviourFeature = Factory.newFeatureMap();
		behaviourFeature.put("grammarURL", new File("log/jape/BehaviourRule.jape").toURI().toURL());

		FeatureMap vulnerabilityFeature = Factory.newFeatureMap();
		vulnerabilityFeature.put("grammarURL", new File("log/jape/VulnerabilityRule.jape").toURI().toURL());

		FeatureMap preconditionFeature = Factory.newFeatureMap();
		preconditionFeature.put("grammarURL", new File("log/jape/PreConditionRule.jape").toURI().toURL());

		FeatureMap postconditionFeature = Factory.newFeatureMap();
		postconditionFeature.put("grammarURL", new File("log/jape/PostConditionRule.jape").toURI().toURL());

		FeatureMap messageFeature = Factory.newFeatureMap();
		messageFeature.put("grammarURL", new File("log/jape/MessageRule.jape").toURI().toURL());

		// Loading JAPE language resources with specified features
		LanguageAnalyser agentTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				agentFeature, parms, "JAPE Transducer - Agent");
		LanguageAnalyser behaviourTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				behaviourFeature, parms, "JAPE Transducer - Behaviour");
		LanguageAnalyser vulTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				vulnerabilityFeature, parms, "JAPE Transducer - Vulnerability");
		LanguageAnalyser postconTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				postconditionFeature, parms, "JAPE Transducer - PostCondition");
		LanguageAnalyser preconTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				preconditionFeature, parms, "JAPE Transducer - PreCondition");
		LanguageAnalyser messageTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer",
				messageFeature, parms, "JAPE Transducer - Message");

		sac.add(documentReset);
		sac.add(defaultTokeniser);
		sac.add(sentenceSplitter);
		sac.add(eg);

		sac.add(agentTagJape);
		sac.add(behaviourTagJape);
		sac.add(vulTagJape);
		sac.add(preconTagJape);
		sac.add(postconTagJape);
		sac.add(messageTagJape);

		return sac;
	}
}
