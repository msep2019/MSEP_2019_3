import gate.Gate;
import gate.Document;
import gate.util.GateException;
import gate.Factory;
import gate.creole.Plugin;
import gate.creole.SerialAnalyserController;

import java.util.Iterator;

import org.apache.log4j.BasicConfigurator;

import java.io.File;

public class Extractor {

	private gate.Corpus corpus;

	public Extractor(String[] files) throws Exception {
		
		BasicConfigurator.configure();
		
		Gate.init();
		// Gate.getCreoleRegister().registerDirectories(new File(System.getProperty("user.dir")).toURL());
		// load the ANNIE plugin 
		Plugin anniePlugin = new Plugin.Maven("uk.ac.gate.plugins", "annie", gate.Main.version); 
		Gate.getCreoleRegister().registerPlugin(anniePlugin); 

		// add files to a corpus
		System.out.println("\n== OBTAINING DOCUMENTS ==");
		createCorpus(files);

		System.out.println("\n== USING GATE TO PROCESS THE DOCUMENTS ==");
		String[] processingResources = { "gate.creole.tokeniser.DefaultTokeniser",
				"gate.creole.splitter.SentenceSplitter" };
		runProcessingResources(processingResources);

		System.out.println("\n== DOCUMENT FEATURES ==");
		displayDocumentFeatures();

		System.out.println("\nDemo done... :)");
	}

	private void createCorpus(String[] files) throws GateException {
		corpus = Factory.newCorpus("Transient Gate Corpus");

		for (int file = 0; file < files.length; file++) {
			System.out.print("\t " + (file + 1) + ") " + files[file]);
			try {
				corpus.add(Factory.newDocument(new File(files[file]).toURL()));
				System.out.println(" -- success");
			} catch (gate.creole.ResourceInstantiationException e) {
				System.out.println(" -- failed (" + e.getMessage() + ")");
			} catch (Exception e) {
				System.out.println(" -- " + e.getMessage());
			}
		}
	}

	private void runProcessingResources(String[] processingResource) throws GateException {
		SerialAnalyserController pipeline = (SerialAnalyserController) Factory
				.createResource("gate.creole.SerialAnalyserController");

		for (int pr = 0; pr < processingResource.length; pr++) {
			System.out.print("\t* Loading " + processingResource[pr] + " ... ");
			pipeline.add((gate.LanguageAnalyser) Factory.createResource(processingResource[pr]));
			System.out.println("done");
		}

		System.out.print("Creating corpus from documents obtained...");
		pipeline.setCorpus(corpus);
		System.out.println("done");

		System.out.print("Running processing resources over corpus...");
		pipeline.execute();
		System.out.println("done");
	}

	private void displayDocumentFeatures() {
		Iterator documentIterator = corpus.iterator();

		while (documentIterator.hasNext()) {
			Document currDoc = (Document) documentIterator.next();
			System.out.println("The features of document \"" + currDoc.getSourceUrl().getFile() + "\" are:");
			gate.FeatureMap documentFeatures = currDoc.getFeatures();

			Iterator featureIterator = documentFeatures.keySet().iterator();
			while (featureIterator.hasNext()) {
				String key = (String) featureIterator.next();
				System.out.println("\t*) " + key + " --> " + documentFeatures.get(key));
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		if (args.length == 0)
			System.err.println("USAGE: java Extractor <file1> <file2> ...");
		else
			try {
				new Extractor(args);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
}