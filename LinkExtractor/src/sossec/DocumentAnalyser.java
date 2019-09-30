package sossec;
import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import sossec.elements.SoSSecAgent;
import sossec.elements.SoSSecBehaviour;
import sossec.elements.SoSSecCondition;
import sossec.elements.SoSSecVulnerability;

public class DocumentAnalyser {
	public static HashMap<String, SoSSecAgent> processDocs(File log)
			throws ResourceInstantiationException, ExecutionException, MalformedURLException {
		HashMap<String, SoSSecAgent> agents = new HashMap<>();

		SerialAnalyserController sac;

		try {
			sac = ResourcesLoader.getResources();

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
		
		// feature map for creating documents
		FeatureMap params = Factory.newFeatureMap();
		params.put(Document.DOCUMENT_URL_PARAMETER_NAME, log.toURI().toURL());
		params.put(Document.DOCUMENT_ENCODING_PARAMETER_NAME, "UTF-8");

		FeatureMap features = Factory.newFeatureMap();
		features.put("createdOn", new Date());
		name++;// to mark each doc and corpus

		// creating document
		Document doc = (Document) Factory.createResource("gate.corpora.DocumentImpl", params, features,
				log.getName() + name);

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
			AnnotationSet annotAgent = defaultSet.get("Agent");
			AnnotationSet annotBehaviour = defaultSet.get("Behaviour");
			AnnotationSet annotCondition = defaultSet.get("Condition");
			AnnotationSet annotVulner = defaultSet.get("Vulnerability");
			AnnotationSet annotMessage = defaultSet.get("Message");

			Set<String> set1 = defaultSet.getAllTypes();
			Iterator<String> it1 = set1.iterator();
			// Set<String> set2 = conditionType.getAllTypes();
			// Iterator<String> it2 = set2.iterator();

			System.out.println(set1.size());
			while (it1.hasNext()) {
				String str = it1.next();
				System.out.println(str);
			}
			
			// Get Agents
			for (Annotation annot : annotAgent) {
				FeatureMap featureAgent = annot.getFeatures();
				String agentName = featureAgent.get("string").toString();

				if (!agents.containsKey(agentName)) {
					agents.put(agentName, new SoSSecAgent(agentName));
				}
			}

			// Get Behaviours
			for (Annotation annot : annotBehaviour) {
				FeatureMap featureBehaviour = annot.getFeatures();
				String behaviourName = featureBehaviour.get("string").toString();
				String behaviourAgent = featureBehaviour.get("Agent").toString();

				SoSSecAgent agent = agents.get(behaviourAgent);

				if (agent != null) {
					agent.addBehaviour(new SoSSecBehaviour(behaviourName));
				}
			}

			// Get Vulnerabilities
			for (Annotation annot : annotVulner) {
				FeatureMap featureVulner = annot.getFeatures();
				String vulnerName = featureVulner.get("string").toString();
				String vulnerBehaviour = featureVulner.get("Behaviour").toString();

				agents.forEach((agentName, agent) -> {
					SoSSecBehaviour behaviour = agent.getBehaviour(vulnerBehaviour);

					if (behaviour != null) {
						behaviour.addVulnerability(new SoSSecVulnerability(vulnerName));
					}
				});
			}

			// Get Conditions
			for (Annotation annot : annotCondition) {
				FeatureMap featureCondition = annot.getFeatures();
				String conditionName = featureCondition.get("string").toString();
				String conditionType = featureCondition.get("type").toString();
				String conditionVulner = featureCondition.get("Vulnerability").toString();

				agents.forEach((agentName, agent) -> {
					agent.getBehaviours().forEach((behaviourName, behaviour) -> {
						SoSSecVulnerability vulner = behaviour.getVulnerability(conditionVulner);

						if (vulner != null) {
							SoSSecCondition cond = new SoSSecCondition(conditionName);
							cond.setType(conditionType);
							vulner.addCondition(cond);
						}
					});
				});
			}

			// Get Messages
			for (Annotation annot : annotMessage) {
				FeatureMap featureMessage = annot.getFeatures();
				String strSender = featureMessage.get("sender").toString();
				String strReceiver = featureMessage.get("receiver").toString();

				agents.forEach((agentName, agent) -> {
					SoSSecBehaviour sender = agent.getBehaviour(strSender);

					if (sender != null) {
						agents.forEach((agentReceiverName, agentReceiver) -> {
							SoSSecBehaviour receiver = agentReceiver.getBehaviour(strReceiver);

							if (receiver != null) {
								sender.addReceiver(receiver);
							}
						});
					}
				});
			}
		}
		
		agents.forEach((agentName, agent) -> {
			System.out.println(agent.getName());

			agent.getBehaviours().forEach((behaviourName, behaviour) -> {
				System.out.println("└─" + behaviour.getName());

				behaviour.getVulnerabilities().forEach((vulnerName, vulner) -> {
					System.out.println("  └─" + vulner.getName());

					vulner.getConditions().forEach((conditionName, condition) -> {
						System.out.println("    └─" + condition.getName());
					});
				});

				behaviour.getReceivers().forEach((receiverName, receiver) -> {
					System.out.println("  └─Receiver: " + receiver.getName());

				});
			});
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

		return agents;
	}
}