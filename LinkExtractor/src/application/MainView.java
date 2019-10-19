package application;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class MainView {
	// View uses Swing framework to display UI to user
	public JFrame frame;
	public LinkTree linkTree;
	public JLabel lblCVE;
	public JTextField txtCVE;
	public JButton btnSearch;
	public JComboBox<String> selectType;

	JPanel detailView;
	CardLayout detailViewLayout;
	public CVEOptionPanel panelCVE;
	public CWEOptionPanel panelCWE;
	public CAPECOptionPanel panelCAPEC;

	final String CVE_OPTION_PANEL = "CVE";
	final String CWE_OPTION_PANEL = "CWE";
	final String CAPEC_OPTION_PANEL = "CAPEC";

	String[] linkTypes = { "Both", "Direct", "Indirect" };

	public MainView(String title) {
		frame = new JFrame(title);
		Container pane = frame.getContentPane();
		pane.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width, screenSize.height);
		frame.setLocationRelativeTo(null);
		frame.setMinimumSize(new Dimension(800, 600));

		// Create UI elements
		lblCVE = new JLabel("CVE ID");
		txtCVE = new JTextField();
		btnSearch = new JButton("Run");

		JPanel panelTop = new JPanel();
		GroupLayout layout = new GroupLayout(panelTop);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblCVE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(txtCVE))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnSearch)));

		layout.setVerticalGroup(
				layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lblCVE).addComponent(txtCVE).addComponent(btnSearch)));

		panelTop.setLayout(layout);
		pane.add(panelTop, BorderLayout.NORTH);

		// Create a tree that allows one selection at a time.
		linkTree = new LinkTree();

		JPanel panelType = new JPanel(new FlowLayout());
		selectType = new JComboBox<String>(linkTypes);
		selectType.setSelectedIndex(0);
		panelType.add(new JLabel("Link Type"));
		panelType.add(selectType);
		
		JPanel panelLeft = new JPanel();
		panelLeft.setLayout(new BorderLayout());
		
		panelLeft.add(panelType, BorderLayout.PAGE_START);
		panelLeft.add(linkTree, BorderLayout.CENTER);
		
		// Listen for when the selection changes.
		// tree.addTreeSelectionListener(this);

		panelCVE = new CVEOptionPanel();
		panelCWE = new CWEOptionPanel();
		panelCAPEC = new CAPECOptionPanel();

		detailViewLayout = new CardLayout();
		detailView = new JPanel(detailViewLayout);
		detailView.add(panelCVE, CVE_OPTION_PANEL);
		detailView.add(panelCWE, CWE_OPTION_PANEL);
		detailView.add(panelCAPEC, CAPEC_OPTION_PANEL);
		
		Border padding = BorderFactory.createEmptyBorder(21, 0, 0, 0);
		detailView.setBorder(padding);

		// Add the scroll panes to a split pane.
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setLeftComponent(panelLeft);
		splitPane.setRightComponent(detailView);
		splitPane.setResizeWeight(0.5);
		splitPane.setBorder(null);

		// Add UI element to frame
		pane.add(splitPane, BorderLayout.CENTER);

		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}
}