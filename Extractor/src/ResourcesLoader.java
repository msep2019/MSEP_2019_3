import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer;

import gate.Controller;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.ProcessingResource;
import gate.creole.ANNIEConstants;
import gate.creole.ConditionalSerialAnalyserController;
import gate.creole.Plugin;
import gate.creole.ResourceReference;
import gate.creole.SerialAnalyserController;
import gate.gui.MainFrame;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

public class ResourcesLoader {
	public static SerialAnalyserController getResources() throws GateException, IOException, URISyntaxException{
		
		Gate.init();
		MainFrame.getInstance().setVisible(true);
		//Load the ANNIE plugin
		Plugin anniePlugin = new Plugin.Maven("uk.ac.gate.plugins", "annie", gate.Main.version); 
		Gate.getCreoleRegister().registerPlugin(anniePlugin); 
		
		Plugin GzPlugin = new Plugin.Maven("uk.ac.gate.plugins", "stringannotation", "4.0"); 
		Gate.getCreoleRegister().registerPlugin(GzPlugin); 
		
		// setting up searialAnalyserController


		SerialAnalyserController sac = (SerialAnalyserController)Factory.createResource("gate.creole.SerialAnalyserController");
		//ProcessingResource annie = (ProcessingResource)Factory.createResource("gate.creole.morph.Morph");
		//SerialAnalyserController sac = (SerialAnalyserController) PersistenceManager.loadObjectFromUrl(new ResourceReference(anniePlugin,ANNIEConstants.DEFAULT_FILE).toURL());
		
		// setting up processing resources, only tokeniser needed
		ProcessingResource defaultTokeniser = (ProcessingResource) Factory.createResource("gate.creole.tokeniser.DefaultTokeniser");
		ProcessingResource sentenceSplitter = (ProcessingResource) Factory.createResource("gate.creole.splitter.SentenceSplitter");
		ProcessingResource documentReset = (ProcessingResource) Factory.createResource("gate.creole.annotdelete.AnnotationDeletePR");
		
		//Loading Gazetteer;
		System.out.println("\n----Loading ExtendedGazetteer----");
		File gz = new File("log/gazetteer/sossec.def");
		FeatureMap parms = Factory.newFeatureMap();
		URL gazURL = gz.toURI().toURL();
		parms.put("configFileURL", gazURL);
		ExtendedGazetteer eg = (ExtendedGazetteer)Factory.createResource(
            "com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer", parms);
		eg.setLongestMatchOnly(true);	
		eg.setMatchAtWordStartOnly(false);
		eg.setMatchAtWordEndOnly(false);
		//eg.setSpaceAnnotationType(SpaceToken);
		//eg.setSplitAnnotationType(Split);
		//eg.setWordAnnotationType(Token);
		
		//specifying location of jape files
		FeatureMap agentFeature = Factory.newFeatureMap();
		agentFeature.put("grammarURL", new File("log/jape/AgentRule.jape").toURI().toURL());
		FeatureMap behaviourFeature = Factory.newFeatureMap();
		behaviourFeature.put("grammarURL", new File("log/jape/BehaviourRule.jape").toURI().toURL());
		//FeatureMap conditionFeature = Factory.newFeatureMap();
		//conditionFeature.put("grammarURL", new File("log/jape/ConditionRule.jape").toURI().toURL());
		FeatureMap messageFeature = Factory.newFeatureMap();
		messageFeature.put("grammarURL", new File("log/jape/MessageRule.jape").toURI().toURL());
		FeatureMap postconditionFeature = Factory.newFeatureMap();
		postconditionFeature.put("grammarURL", new File("log/jape/PostConditionRule.jape").toURI().toURL());
		FeatureMap preconditionFeature = Factory.newFeatureMap();
		preconditionFeature.put("grammarURL", new File("log/jape/PreConditionRule.jape").toURI().toURL());
		//FeatureMap transmissionFeature = Factory.newFeatureMap();
		//transmissionFeature.put("grammarURL", new File("log/jape/TransmissionRule.jape").toURI().toURL());
		FeatureMap vulnerabilityFeature = Factory.newFeatureMap();
		vulnerabilityFeature.put("grammarURL", new File("log/jape/VulnerabilityRule.jape").toURI().toURL());
		
		//loading JAPE language resources with specified features
		LanguageAnalyser agentTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", agentFeature,parms,"JAPE Transducer - Agent");
		LanguageAnalyser behaviourTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", behaviourFeature,parms,"JAPE Transducer - Behaviour");
		//LanguageAnalyser conditionTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", conditionFeature,parms,"JAPE Transducer - Condition");
		LanguageAnalyser messageTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", messageFeature,parms,"JAPE Transducer - Message");
		LanguageAnalyser postconTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", postconditionFeature,parms,"JAPE Transducer - PostCondition");
		LanguageAnalyser preconTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", preconditionFeature,parms,"JAPE Transducer - PreCondition");
		//LanguageAnalyser transTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", transmissionFeature,parms,"JAPE Transducer - Transmission");
		LanguageAnalyser vulTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", vulnerabilityFeature,parms,"JAPE Transducer - Vulnerability");
		
		sac.add(documentReset);
		sac.add(defaultTokeniser);
		sac.add(sentenceSplitter);
		sac.add(eg);

		sac.add(agentTagJape);
		sac.add(behaviourTagJape);	
		//sac.add(conditionTagJape);
		sac.add(vulTagJape);
		sac.add(preconTagJape);
		sac.add(postconTagJape);
		sac.add(messageTagJape);
		//sac.add(transTagJape);
		
		return sac;
	}
}
