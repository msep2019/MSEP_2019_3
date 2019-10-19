package application;

public class Application {
	public static void main(String[] args) {
		// Assemble all the pieces of the MVC
		MainView v = new MainView("SoSSec Link Extractor");
		Controller c = new Controller(v);
		c.initController();
	}
}
