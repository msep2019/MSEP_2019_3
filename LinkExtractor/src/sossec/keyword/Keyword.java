package sossec.keyword;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.UUID;

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

public class Keyword {
	public static ArrayList<String> processDocs(String desc)
			throws ResourceInstantiationException, ExecutionException, MalformedURLException {
		ArrayList<String> keywords = new ArrayList<>();

		SerialAnalyserController sac;

		try {
			sac = KeywordResourcesLoader.getResources();

		} catch (Exception e) {
			System.out.println(e.toString());
			System.exit(0);
			return null;
		}

		// Creating corpus
		Corpus corpus = Factory.newCorpus("LOGText Corpus");

		// Arraylist to store files resources
		ArrayList<Document> documentResList = new ArrayList<Document>();

		int name = 0;
		
		/*
		// feature map for creating documents
		FeatureMap params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_URL_PARAMETER_NAME, log.toURI().toURL());
		params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");

		FeatureMap features = Factory.newFeatureMap();
		features.put("createdOn", new Date());
		name++;// to mark each doc and corpus
		*/

		// creating document
		Document doc = (Document) Factory.newDocument(desc);

		// add document in corpus
		corpus.add(doc);
		// add corpus to sac

		sac.setCorpus(corpus);
		// execute sac on corpus

		sac.execute();

		for (Iterator<Document> cIterator = corpus.iterator(); cIterator.hasNext();) {

			// get document from corpus
			Document corpDoc = cIterator.next();
			// get default set of annotations
			AnnotationSet defaultSet = corpDoc.getAnnotations();
			// get annotations of different types
			AnnotationSet annotKeyword = defaultSet.get("Keyword");

			// Get Agents
			for (Annotation annot : annotKeyword) {
				FeatureMap featureAgent = annot.getFeatures();
				String keywordName = featureAgent.get("string").toString();

				if (!keywords.contains(keywordName)) {
					keywords.add(keywordName);
				}
			}
		}
		
		corpus.clear();

		// Delete each document resource
		for (Iterator<Document> docResIterator = documentResList.iterator(); docResIterator.hasNext();) {
			Factory.deleteResource((Resource) docResIterator.next());
		}
		
		// Clear list of document resources
		documentResList.clear();
		
		System.gc();
		
		Collections.sort(keywords, String.CASE_INSENSITIVE_ORDER); 

		return keywords;
	}
	
	public static File generateGazetteer(String name, ArrayList<String> keywords) {

		File tempDir = new File("tmp");
		if (!tempDir.exists()) {
			if (tempDir.mkdir()) {
				System.out.println("Temp directory is created!");
			} else {
				System.out.println("Failed to create temp directory!");
				System.exit(1);
			}
		}

		File fileList = null;
		File fileDef = null;

		try {
			String fileListName = name + "_keywords" + UUID.randomUUID().toString() + ".lst";
			fileList = new File("tmp/" + fileListName);

			PrintStream fileListStream = new PrintStream(fileList);
			for (String keyword : keywords) {
				fileListStream.println(keyword);
			}

			fileListStream.close();

			String fileDefName = name + "_keywords" + UUID.randomUUID().toString() + ".def";
			fileDef = new File("tmp/" + fileDefName);

			PrintStream fileDefStream = new PrintStream(fileDef);

			fileDefStream.print(fileListName + ":" + name  + ":Keyword:en");

			fileDefStream.close();

			// fileList.deleteOnExit();
			// fileDef.deleteOnExit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileDef;
	}
}