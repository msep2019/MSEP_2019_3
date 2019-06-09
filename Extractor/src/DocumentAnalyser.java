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
//import gate.creole.ConditionalSerialAnalyserController;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;


public class DocumentAnalyser {
	public static void processDocs(SerialAnalyserController sac, File log, ArrayList<String> anntTypes, ArrayList<String> checkColumn,int fileClusterLength) throws ResourceInstantiationException, ExecutionException, MalformedURLException{
		
		//File log = new File("log/SoSSec.xgapp");
		//Controller controller = (Controller)PersistenceManager.loadObjectFromFile(log);
		
		//creating corpus
		Corpus corpus = Factory.newCorpus("LOGText Corpus");
		
		//Arraylist to store files resources
		ArrayList<Document> documentResList = new ArrayList<Document>();
		
		int name = 0;
		//Integer uniqueColKey = 0;
		int rowCount = 0;
		int fileCount = 0;
		//int totalPF = 0;
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
			rowCount++;// add rowCount
			// get document from corpus
			Document corpDoc = cIterator.next();
			// get default set of annotations
			AnnotationSet defaultSet = corpDoc.getAnnotations();
			// get annotations of annotation type name and values
			AnnotationSet colAnnotationType = defaultSet.get("Lookup");
			
			Set<String> set1 = defaultSet.getAllTypes();
			Iterator<String> it1 = set1.iterator(); 
			Set<String> set2 = colAnnotationType.getAllTypes();
			Iterator<String> it2 = set2.iterator(); 
			
			System.out.println(set1.size());
			
			while (it1.hasNext()) {  
				  String str = it1.next();  
				  System.out.println(str);  
			}
			
			System.out.println(set2.size());
			
			for(Annotation colAnnotation : colAnnotationType){
				FeatureMap colFeatureMap = colAnnotation.getFeatures();
				String colNamesString = colFeatureMap.toString();
				if (colFeatureMap.containsKey("_string")){
					colFeature = colFeatureMap.get("_string").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colFeature,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colFeature  = decodedcolFeature;
					anntTypes.add(colFeature);
				}
				if (colFeatureMap.containsKey("minorType")){
					colTag = colFeatureMap.get("minorType").toString();
					//decodedcolFeature = colFeature ;
					try {
						decodedcolFeature = URLDecoder.decode(colTag,  "UTF-8");
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					colTag  = decodedcolFeature;
					checkColumn.add(colTag);
				}
				
			}
		}
		
		System.out.println(fileClusterLength+" processed docs analysed!");
		fileCount = 0;//reset count to 0
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