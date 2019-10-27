package application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import sossec.mitigation.Mitigation;
import sossec.mitigation.SecurityPattern;

public class MitigationOptionPanel extends Panel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton btnApply;
	public KeywordPanel panelKeyword;

	public JComboBox<Integer> cboSimilarity;

	public Mitigation mitigation;
	
	public JList<SecurityPattern> listPatterns;
	
	public JFrame frame;
	

	public MitigationOptionPanel() {
		mitigation = new Mitigation();
		init();

		panelKeyword.btnEnable.addActionListener(e -> enableKeywords());
		panelKeyword.btnDisable.addActionListener(e -> disableKeywords());
		panelKeyword.btnAdd.addActionListener(e -> addKeyword());
		listPatterns.addListSelectionListener(e -> selectSecurityPattern(e));
	}

	public void init() {

		setLayout(new BorderLayout());
		
		JPanel panelOPtion = new JPanel();
		panelOPtion.setLayout(new BorderLayout());

		btnApply = new JButton("Apply");

		JPanel panelBottom = new JPanel();
		GroupLayout layoutBottom = new GroupLayout(panelBottom);
		layoutBottom.setAutoCreateGaps(true);
		layoutBottom.setAutoCreateContainerGaps(true);
		layoutBottom.setHorizontalGroup(layoutBottom.createSequentialGroup()
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(layoutBottom.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(btnApply)));

		layoutBottom.setVerticalGroup(layoutBottom.createSequentialGroup()
				.addGroup(layoutBottom.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnApply)));

		panelBottom.setLayout(layoutBottom);
		panelOPtion.add(panelBottom, BorderLayout.SOUTH);

		JPanel panelBody = new JPanel();
		panelBody.setLayout(new BoxLayout(panelBody, BoxLayout.Y_AXIS));

		panelKeyword = new KeywordPanel(mitigation.keywords, mitigation.disabledKeywords);
		panelBody.add(panelKeyword);

		JPanel panelSimilarity = new JPanel(new FlowLayout(FlowLayout.LEFT));
		cboSimilarity = new JComboBox<Integer>();
		panelSimilarity.add(new JLabel("Minimun keyword similarity"));
		panelSimilarity.add(cboSimilarity);
		panelBody.add(panelSimilarity);

		panelOPtion.add(panelBody, BorderLayout.PAGE_START);
		
		listPatterns = new JList<>();
		listPatterns.setLayoutOrientation(JList.VERTICAL);
		listPatterns.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane panelSecurityPattern = new JScrollPane(listPatterns,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelSecurityPattern.setPreferredSize(new Dimension(200, 300));
		
		JSplitPane splitPaneLeft = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPaneLeft.setLeftComponent(panelOPtion);
		splitPaneLeft.setRightComponent(panelSecurityPattern);
		splitPaneLeft.setResizeWeight(0.5);
		splitPaneLeft.setBorder(null);
		
		this.add(splitPaneLeft, BorderLayout.CENTER);

	}

	public void setMitigation(Mitigation mitigation) {
		this.mitigation = mitigation;
		updateKeywordLists();
		this.setSimilarity(mitigation.maxMatching, mitigation.minMatching);
	}

	public void updateKeywordLists() {
		DefaultListModel<String> modelEnabledKeywords = new DefaultListModel<>();

		for (String item : mitigation.keywords) {
			modelEnabledKeywords.addElement(item);
		}
		panelKeyword.listEnabledKeywords.setModel(modelEnabledKeywords);

		DefaultListModel<String> modelDisabledKeywords = new DefaultListModel<>();

		for (String item : mitigation.disabledKeywords) {
			modelDisabledKeywords.addElement(item);
		}
		panelKeyword.listDisabledKeywords.setModel(modelDisabledKeywords);
		panelKeyword.paneEnabledKeywords.revalidate();
		panelKeyword.paneDisabledKeywords.revalidate();
	}

	public void enableKeywords() {
		List<String> selectedItems = panelKeyword.listDisabledKeywords.getSelectedValuesList();

		for (String item : selectedItems) {
			if (mitigation.disabledKeywords.contains(item)) {
				mitigation.disabledKeywords.remove(item);
			}

			if (!mitigation.keywords.contains(item)) {
				mitigation.keywords.add(item);
			}
		}

		Collections.sort(mitigation.keywords, String.CASE_INSENSITIVE_ORDER);

		updateKeywordLists();
		mitigation.isChangedKeywords = true;
	}

	public void disableKeywords() {
		List<String> selectedItems = panelKeyword.listEnabledKeywords.getSelectedValuesList();

		System.out.println(selectedItems);
		System.out.println(mitigation.keywords);

		for (String item : selectedItems) {
			if (mitigation.keywords.contains(item)) {
				mitigation.keywords.remove(item);
			}

			if (!mitigation.disabledKeywords.contains(item)) {
				mitigation.disabledKeywords.add(item);
			}
		}

		Collections.sort(mitigation.disabledKeywords, String.CASE_INSENSITIVE_ORDER);

		updateKeywordLists();
		mitigation.isChangedKeywords = true;
	}

	public void addKeyword() {
		if (!mitigation.keywords.contains(panelKeyword.txtKeyword.getText())) {
			mitigation.keywords.add(panelKeyword.txtKeyword.getText());
		}

		Collections.sort(mitigation.keywords, String.CASE_INSENSITIVE_ORDER);

		updateKeywordLists();
		mitigation.isChangedKeywords = true;
	}

	public void setSimilarity(int max, int min) {
		Integer[] numbers = new Integer[max];

		for (int i = 0; i < max; i++) {
			numbers[i] = max - i;
		}

		ComboBoxModel<Integer> model = new DefaultComboBoxModel<Integer>(numbers);
		cboSimilarity.setModel(model);

		if (max != 0 && min != 0 && max >= min) {
			cboSimilarity.setSelectedIndex(max - min);
		}
	}
	
	public void selectSecurityPattern(ListSelectionEvent e) {
		// Return if the changes are still being made
		if (e.getValueIsAdjusting()) {
			return;
		}
		
		if (frame != null) {
			frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING)); 
		}
		
		System.out.println("selectMitigation");
		
		SecurityPattern pattern = listPatterns.getSelectedValue();
		
		String content = pattern.getContent();
		
		JEditorPane editor = new JEditorPane();
		editor.setEditable(false);
		
		HTMLEditorKit kit = new HTMLEditorKit();
		editor.setEditorKit(kit);
		
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("h1{margin-bottom:0px;padding-botton:0px}");
		
		Document doc = kit.createDefaultDocument();
		editor.setDocument(doc);
		editor.setText(content);
		
		frame = new JFrame("Security Pattern: " + pattern.name);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		frame.getContentPane().add(new JScrollPane(editor), BorderLayout.CENTER);
		frame.setPreferredSize(new Dimension(600, 800));
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
	}
}
