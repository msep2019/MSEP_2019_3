package application;

import sossec.cve.CVEItem;

public class Application {
	public static void main(String[] args) {
		// Assemble all the pieces of the MVC
		CVEItem m = new CVEItem();
		MainView v = new MainView("SoSSec Link Extractor");
		Controller c = new Controller(m, v);
		c.initController();
	}
}
