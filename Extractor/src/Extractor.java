import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import gate.Gate;
import gate.LanguageAnalyser;
import gate.Controller;
import gate.CorpusController;
import gate.Document;
import gate.util.GateException;
import gate.util.ant.packager.GazetteerLists;
import gate.util.persistence.PersistenceManager;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.Plugin;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
//import gate.gui.MainFrame;
import gate.creole.SerialController;
//import gate.creole.metadata.CreoleResource;

import org.apache.log4j.BasicConfigurator;
import com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer;
//import com.jpetrak.gate.stringannotation.extendedgazetteer.FeatureGazetteer;

public class Extractor {
	private gate.Corpus corpus;
	
	public Extractor(String[] files) throws Exception {
		BasicConfigurator.configure();
		System.out.println("\n----Initializing----");
		//prepare the library
		Gate.init();
		//show the main window
		//MainFrame.getInstance().setVisible(true);
		System.out.println("\n--Completed");
		
		System.out.println("\n----Loading Plugins----");
		//Load the ANNIE plugin
		Plugin anniePlugin = new Plugin.Maven("uk.ac.gate.plugins", "annie", gate.Main.version); 
		Gate.getCreoleRegister().registerPlugin(anniePlugin); 
		
		Plugin GzPlugin = new Plugin.Maven("uk.ac.gate.plugins", "stringannotation", "4.0"); 
		Gate.getCreoleRegister().registerPlugin(GzPlugin); 
		System.out.println("\n--Completed");
		
		// add files to a corpus
		System.out.println("\n----Setting  Documents----");
		//load saved state;
		File doc = new File("log/SoSSec.log");
		File log = new File("log/SoSSec.xgapp");
	    File gz = new File("log/gazetteer/sossec.def");
	    System.out.println("\n"+files);
	    System.out.println("\n--Completed");
	    
		// setting corpus to process Documents
		System.out.println("\n----Initializing Controller and Corpus----");
		corpus = Factory.newCorpus("Processing Corpus");
		SerialAnalyserController sac = (SerialAnalyserController)Factory.createResource("gate.creole.SerialAnalyserController");
		
		//String[] processingResources = {"gate.creole.tokeniser.DefaultTokeniser",
				//"gate.creole.splitter.SentenceSplitter","gate.creole.gazetteer.DefaultGazetteer"};
		//runProcessingResources(processingResources);
		// featuremaps for .jape files, specifying location of .jape files 
		System.out.println("\n--Completed");
		
		System.out.println("\n----Loading Jape files----");
		FeatureMap agentFeature = Factory.newFeatureMap();
		agentFeature.put("grammarURL", new File("log/jape/AgentRule.jape").toURI().toURL());
		FeatureMap behaviourFeature = Factory.newFeatureMap();
		behaviourFeature.put("grammarURL", new File("log/jape/BehaviourRule.jape").toURI().toURL());
		FeatureMap conditionFeature = Factory.newFeatureMap();
		conditionFeature.put("grammarURL", new File("log/jape/ConditionRule.jape").toURI().toURL());
		FeatureMap messageFeature = Factory.newFeatureMap();
		messageFeature.put("grammarURL", new File("log/jape/MessageRule.jape").toURI().toURL());
		FeatureMap transmissionFeature = Factory.newFeatureMap();
		transmissionFeature.put("grammarURL", new File("log/jape/TransmissionRule.jape").toURI().toURL());
		FeatureMap vulnerabilityFeature = Factory.newFeatureMap();
		vulnerabilityFeature.put("grammarURL", new File("log/jape/VulnerabilityRule.jape").toURI().toURL());
		
		// load JAPE language resources with specified features 
		LanguageAnalyser agentTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", agentFeature);
		LanguageAnalyser behaviourTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", behaviourFeature);
		LanguageAnalyser conditionTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", conditionFeature);
		LanguageAnalyser messageTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", messageFeature);
		LanguageAnalyser transTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", transmissionFeature);
		LanguageAnalyser vulTagJape = (LanguageAnalyser) Factory.createResource("gate.creole.Transducer", vulnerabilityFeature);
		System.out.println("\n--Completed");
		
		//Loading Gazetteer;
		System.out.println("\n----Loading ExtendedGazetteer----");
		FeatureMap parms = Factory.newFeatureMap();
		URL gazURL = gz.toURI().toURL();
		parms.put("configFileURL", gazURL);
		ExtendedGazetteer eg = (ExtendedGazetteer)Factory.createResource(
            "com.jpetrak.gate.stringannotation.extendedgazetteer.ExtendedGazetteer", parms);
			  
		FeatureMap localtxt = Factory.newFeatureMap();
		URL docURL = doc.toURI().toURL();
		localtxt.put("sourceUrl",docURL);
		Document Fdoc = (Document) Factory.createResource("gate.corpora.DocumentImpl", localtxt);
		//Controller controller = (Controller)PersistenceManager.loadObjectFromFile(log);
		System.out.println("\n--Completed");
		
		System.out.println("\n----Processing documents----");
		sac.setCorpus(corpus);
		sac.setDocument(Fdoc);
		//eg.setDocument(Fdoc);
		sac.add(agentTagJape);
		sac.add(behaviourTagJape);
		sac.add(conditionTagJape);
		sac.add(messageTagJape);
		sac.add(transTagJape);
		sac.add(vulTagJape);
		sac.add(eg);
		//sac.setDocument(Fdoc);
		sac.execute();
		System.out.println("\n--Completed");
		//controller.setCorpus(corpus);
		//controller.execute();
		//eg.execute();
		

		//System.out.println("\n----Displaying Document Features----");
		//displayDocumentFeatures();
		
		System.out.println("\n--Session Finished");
	}
		
	/*private void createCorpus(String[] files) throws GateException{
		corpus = Factory.newCorpus("Transient GateCorpus");
		
		for(int file = 0; file < files.length; file++) {
			System.out.print("\t" + (file+1) + files[file]);
			try{
				corpus.add(Factory.newDocument(new File(files[file]).toURL()));
				System.out.println(" --> Success");
			} catch (gate.creole.ResourceInstantiationException e) {
				System.out.println(" --> Failed: (" + e.getMessage() + ")");
			} catch (Exception e) {
				System.out.println("--> Problem:" + e.getMessage());
			}
		}
	}
		ArrayList<Document> listGazes = new ArrayList<>();
		listGazes.add("log/gazetteer");
		ArrayList<Document> listDocument = new ArrayList<Document>();
		for(int i=0;i<listGazes.size();i++) {
            //Document doc = Factory.newDocument(listGazes.get(i).getText());
            FeatureMap params = Factory.newFeatureMap();
            params.put(Document.DOCUMENT_STRING_CONTENT_PARAMETER_NAME,listGazes.get(i));
            Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params);

            Long start=gate.Utils.start(doc);
            Long end = gate.Utils.end(doc);
            doc.getAnnotations("Key").add(start, end, " ", Factory.newFeatureMap());
            listDocument.add(doc);
            corpus.add(listDocument.get(i));
		}
	*/
	
	/*private void runProcessingResources(String[] processingResource) throws GateException{

		SerialAnalyserController pipeline = (SerialAnalyserController)Factory.createResource("gate.creole.SerialAnalyserController");

		for(int pr = 0; pr < processingResource.length; pr++) {
			System.out.print("\t* Loading" + processingResource[pr] + "...\n");
			pipeline.add((gate.LanguageAnalyser)Factory.createResource(processingResource[pr]));
			System.out.println("Completed");
		}
		
		System.out.print("Creating corpus from documents...");
		pipeline.setCorpus(corpus);
		System.out.println("Completed");

		System.out.print("Processing resources over corpus...");
		pipeline.execute();
		System.out.println("Completed");
	}*/
	
	/*private void displayDocumentFeatures() {
		Iterator documentIterator = corpus.iterator();

		while(documentIterator.hasNext()){
			Document currDoc = (Document)documentIterator.next();
			System.out.println("The features of document \"" + currDoc.getSourceUrl().getFile() + "\" are:");
			gate.FeatureMap documentFeatures = currDoc.getFeatures();

			Iterator featureIterator = documentFeatures.keySet().iterator();
			while(featureIterator.hasNext()){
				String key = (String)featureIterator.next();
				System.out.println("\t*) " + key + "-->" + documentFeatures.get(key));
			}
			System.out.println();
		}	
	}*/
	
	public static void main(String[] args) {

		if(args.length == 0)
			System.err.println("Usage: java OriginalLogFiles...");
		else try {
			new Extractor(args);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}