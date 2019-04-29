package phase1;

import gate.Gate;
import gate.Document;
import gate.util.GateException;
import gate.Factory;
import gate.creole.SerialAnalyserController;

import java.util.Iterator;
import java.io.File;

public class TextExtractPhase1 {
	private gate.Corpus corpus;
	
	public TextExtractPhase1(String[] files) throws Exception {
		Gate.init();
		Gate.getCreoleRegister().registerDirectories(
				new File(System.getProperty("user.dir")).toURL());
		// add files to a corpus
		System.out.println("\n----Getting Documents----");
		createCorpus(files);
		
		// utilizing Gate to process Documents
		System.out.println("\n----Processing Documents by Gate----");
		String[] processingResources = {"gate.creole.tokeniser.Tokeniser00018","gate.creole.gazetteer.ExtendedGazetteer00014","gate.creole.splitter.SentenceSplitter00015","gate.prs.SoSSec"};
		runProcessingResources(processingResources);
		
		System.out.println("\n----Displaying Document Features----");
		displayDocumentFeatures();
		
		System.out.println("\n--Session Finished");
	}
	
	private void createCorpus(String[] files) throws GateException{
		corpus = Factory.newCorpus("TestGateCorpus");
		
		for(int file = 0; file < files.length; file++) {
			System.out.print("\t" + (file+1) + files[file]);
			try{
				corpus.add(Factory.newDocument(new File(files[file]).toURL()));
				System.out.println(" --> Success");
			}
			catch(gate.creole.ResourceInstantiationException e){
				System.out.println(" --> Failed: (" + e.getMessage() + ")");
			}
			catch(Exception e){
				System.out.println("--> Problem:" + e.getMessage());
			}
		}
	}
	
	private void runProcessingResources(String[] processingResource) throws GateException{
		SerialAnalyserController pipeline = (SerialAnalyserController)Factory.createResource("gate.creole.SerialAnalyserController");
		for(int pr = 0; pr < processingResource.length; pr++) {
			System.out.print("\t* Loading" + processingResource[pr] + "...");
			pipeline.add((gate.LanguageAnalyser)Factory.createResource(processingResource[pr]));
			System.out.println("Completed");
		}
		
		System.out.print("Creating corpus from documents...");
		pipeline.setCorpus(corpus);
		System.out.println("Completed");

		System.out.print("Processing resources over corpus..");
		pipeline.execute();
		System.out.println("Completed");
	}

	private void displayDocumentFeatures() {
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
	}
	
	public static void main(String[] args) {

		if(args.length == 0)
			System.err.println("Usage: java OriginalLogFiles...");
		else try {
			new TextExtractPhase1(args);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}