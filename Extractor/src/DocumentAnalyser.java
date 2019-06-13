import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;


public class DocumentAnalyser {
	public static void processDocs(SerialAnalyserController sac, File log, SoSSecCondition[] conditionRules, SoSSecMessage[] messageRules, 	SoSSecBehaviour[] behaviourRules, SoSSexAgent[] agentRules, int fileClusterLength) throws ResourceInstantiationException, ExecutionException, MalformedURLException{
		
		//File log = new File("log/SoSSec.xgapp");
		//Controller controller = (Controller)PersistenceManager.loadObjectFromFile(log);
		
		//creating corpus
		Corpus corpus = Factory.newCorpus("LOGText Corpus");
		
		//Arraylist to store files resources
		ArrayList<Document> documentResList = new ArrayList<Document>();
		
		int name = 0;
		int rowCount = 0;
		
		//strings for temp arrangement
		String colFeature = "";
		String colTag = "";
		String decodedcolFeature = "";
		
		URL sourceUrl = log.toURI().toURL();
		// feature map for creating documents
		FeatureMap params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_URL_PARAMETER_NAME, sourceUrl);
		params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");
		
		FeatureMap features = Factory.newFeatureMap();
		features.put("createdOn", new Date());
		name++;// to mark each doc and corpus
		
		//creating document
		Document doc=(Document) Factory.createResource("gate.corpora.DocumentImpl", params, features, log.getName()+"-TestDoc"+name);
		//add document in corpus
		corpus.add(doc);
		// add corpus to sac
		sac.setCorpus(corpus);
		// execute sac on corpus
		sac.execute();
		
		for(Iterator<Document> cIterator = corpus.iterator(); cIterator.hasNext();) {

			// get document from corpus
			Document corpDoc = cIterator.next();
			// get default set of annotations
			AnnotationSet defaultSet = corpDoc.getAnnotations();
			// get annotations of different types
			AnnotationSet conditionType = defaultSet.get("Condition");
			AnnotationSet messageType = defaultSet.get("Message");
			AnnotationSet vulnerabilityType = defaultSet.get("Vulnerability");
			AnnotationSet behaviourType = defaultSet.get("Behaviour");
			
			Set<String> set1 = defaultSet.getAllTypes();
			Iterator<String> it1 = set1.iterator(); 
			//Set<String> set2 = conditionType.getAllTypes();
			//Iterator<String> it2 = set2.iterator(); 
			
			System.out.println(set1.size());
			while (it1.hasNext()) {  
				  String str = it1.next();  
				  System.out.println(str);  
			}		
			//System.out.println(set2.size());
			
			//get ConditionRule
			for(Annotation colAnnotation : conditionType){
				FeatureMap colFeatureMap = colAnnotation.getFeatures();
				//String colNamesString = colFeatureMap.toString();
				conditionRules[rowCount] = new SoSSecCondition();
				if (colFeatureMap.containsKey("Vulnerability")){
					colFeature = colFeatureMap.get("Vulnerability").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colFeature,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colFeature  = decodedcolFeature;
					conditionRules[rowCount].setVulnerability(colFeature);
				}
				if (colFeatureMap.containsKey("type")){
					colTag = colFeatureMap.get("type").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colTag,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colTag  = decodedcolFeature;
					conditionRules[rowCount].setType(colTag);
				}
				rowCount++;
			}
			
			rowCount = 0;
			//get MessageRule
			for(Annotation colAnnotation : messageType){
				FeatureMap colFeatureMap = colAnnotation.getFeatures();
				//String colNamesString = colFeatureMap.toString();
				messageRules[rowCount] = new SoSSecMessage();
				if (colFeatureMap.containsKey("receiver")){
					colFeature = colFeatureMap.get("receiver").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colFeature,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colFeature  = decodedcolFeature;
					messageRules[rowCount].setReceiver(colFeature);
				}
				if (colFeatureMap.containsKey("sender")){
					colTag = colFeatureMap.get("sender").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colTag,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colTag  = decodedcolFeature;
					messageRules[rowCount].setSender(colTag);
				}
				rowCount++;
			}
			
			rowCount = 0;
			//get BehaviourRule
			for(Annotation colAnnotation : vulnerabilityType){
				FeatureMap colFeatureMap = colAnnotation.getFeatures();
				//String colNamesString = colFeatureMap.toString();
				behaviourRules[rowCount] = new SoSSecBehaviour();
				if (colFeatureMap.containsKey("Behaviour")){
					colFeature = colFeatureMap.get("Behaviour").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colFeature,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colFeature  = decodedcolFeature;
					behaviourRules[rowCount].setBehaviour(colFeature);
				}
				if (colFeatureMap.containsKey("Vulnerability")){
					colFeature = colFeatureMap.get("Vulnerability").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colFeature,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colFeature  = decodedcolFeature;
					behaviourRules[rowCount].setVulnerability(colFeature);
				}
				rowCount++;
			}
			
			rowCount = 0;
			//get BehaviourRule
			for(Annotation colAnnotation : behaviourType){
				FeatureMap colFeatureMap = colAnnotation.getFeatures();
				agentRules[rowCount] = new SoSSexAgent();
				//String colNamesString = colFeatureMap.toString();
				if (colFeatureMap.containsKey("Agent")){
					colFeature = colFeatureMap.get("Agent").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colFeature,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colFeature  = decodedcolFeature;
					agentRules[rowCount].setAgent(colFeature);
				}
				if (colFeatureMap.containsKey("Behaviour")){
					colFeature = colFeatureMap.get("Behaviour").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colFeature,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colFeature  = decodedcolFeature;
					agentRules[rowCount].setBehaviour(colFeature);
				}
				rowCount++;
			}
		}
		
		System.out.println(fileClusterLength+" processed docs analysed!");
		corpus.clear();
				
		//delete each document resource
		for(Iterator<Document> docResIterator = documentResList.iterator(); docResIterator.hasNext();){
			Factory.deleteResource((Resource) docResIterator.next());
		}
		System.out.println("All docs are removed from LR and corpus is cleared!");
		
		//clear list of document resources
		documentResList.clear();
		System.out.println("Document resource list cleared!");
		System.gc();
	}
}