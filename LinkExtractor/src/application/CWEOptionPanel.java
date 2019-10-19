package application;

import java.awt.BorderLayout;
import java.awt.Panel;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

import sossec.cwe.CWEItem;

public class CWEOptionPanel extends Panel {
	public JButton btnApply;
	public KeywordPanel panelKeyword;
	
	public CWEItem cwe;

	public CWEOptionPanel() {
		cwe = new CWEItem(); 
		init();
		
		panelKeyword.btnEnable.addActionListener(e -> enableKeywords());
		panelKeyword.btnDisable.addActionListener(e -> disableKeywords());
		panelKeyword.btnAdd.addActionListener(e -> addKeyword());
	}

	public void init() {

		setLayout(new BorderLayout());

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
		add(panelBottom, BorderLayout.SOUTH);

		JPanel panelBody = new JPanel();
		panelBody.setLayout(new BoxLayout(panelBody, BoxLayout.Y_AXIS));
		
		panelKeyword = new KeywordPanel(cwe.keywords, cwe.disabledKeywords); 
		panelBody.add(panelKeyword);

		add(panelBody, BorderLayout.PAGE_START);
	}
	
	public void setCWE(CWEItem cwe) {
		this.cwe = cwe;
		updateKeywordLists();
	}
	
	public void updateKeywordLists() {
		DefaultListModel<String> modelEnabledKeywords = new DefaultListModel<>();
		
		for (String item : cwe.keywords) {
			modelEnabledKeywords.addElement(item);
		}
		panelKeyword.listEnabledKeywords.setModel(modelEnabledKeywords);
		
		DefaultListModel<String> modelDisabledKeywords = new DefaultListModel<>();
		
		for (String item : cwe.disabledKeywords) {
			modelDisabledKeywords.addElement(item);
		}
		panelKeyword.listDisabledKeywords.setModel(modelDisabledKeywords);
		panelKeyword.paneEnabledKeywords.revalidate();
		panelKeyword.paneDisabledKeywords.revalidate();
	}
	
	public void enableKeywords() {
		List<String> selectedItems = panelKeyword.listDisabledKeywords.getSelectedValuesList();
		
		for (String item : selectedItems) {
			if (cwe.disabledKeywords.contains(item)) {
				cwe.disabledKeywords.remove(item);
			}
			
			if (!cwe.keywords.contains(item)) {
				cwe.keywords.add(item);
			}
		}
		
		Collections.sort(cwe.keywords, String.CASE_INSENSITIVE_ORDER);
		
		updateKeywordLists();
	}
	
	public void disableKeywords() {
		List<String> selectedItems = panelKeyword.listEnabledKeywords.getSelectedValuesList();
		
		System.out.println(selectedItems);
		System.out.println(cwe.keywords);
		
		for (String item : selectedItems) {
			if (cwe.keywords.contains(item)) {
				cwe.keywords.remove(item);
			}
			
			if (!cwe.disabledKeywords.contains(item)) {
				cwe.disabledKeywords.add(item);
			}
		}
		
		Collections.sort(cwe.disabledKeywords, String.CASE_INSENSITIVE_ORDER);
		
		updateKeywordLists();
	}
	
	public void addKeyword() {
		if (!cwe.keywords.contains(panelKeyword.txtKeyword.getText())) {
			cwe.keywords.add(panelKeyword.txtKeyword.getText());
		}
			
		Collections.sort(cwe.keywords, String.CASE_INSENSITIVE_ORDER);
		
		updateKeywordLists();
	}
}
