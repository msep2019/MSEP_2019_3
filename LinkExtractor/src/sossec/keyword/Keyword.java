package sossec.keyword;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Iterator;

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
			AnnotationSet annotAgent = defaultSet.get("Keyword");

			// Get Agents
			for (Annotation annot : annotAgent) {
				FeatureMap featureAgent = annot.getFeatures();
				String agentName = featureAgent.get("string").toString();

				if (!keywords.contains(agentName)) {
					keywords.add(agentName);
				}
			}
		}
		
		keywords.forEach((agent) -> {
			System.out.println(agent);

		});
		
		corpus.clear();

		// Delete each document resource
		for (Iterator<Document> docResIterator = documentResList.iterator(); docResIterator.hasNext();) {
			Factory.deleteResource((Resource) docResIterator.next());
		}
		System.out.println("All docs are removed from LR and corpus is cleared!");

		// Clear list of document resources
		documentResList.clear();
		System.out.println("Document resource list cleared!");
		System.gc();

		return keywords;
	}
}