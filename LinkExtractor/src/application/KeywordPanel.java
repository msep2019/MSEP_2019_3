package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

public class KeywordPanel extends JPanel{
	public JList<String> listEnabledKeywords;
	public JList<String> listDisabledKeywords;
	public JButton btnEnable;
	public JButton btnDisable;
	
	public JTextField txtKeyword;
	public JButton btnAdd;
	
	public ArrayList<String> enabledKeywords;
	public ArrayList<String> disabkedKeywords;
	
	public KeywordPanel(ArrayList<String> enabledKeywords, ArrayList<String> disabkedKeywords) {
		this.enabledKeywords = enabledKeywords;
		this.disabkedKeywords = disabkedKeywords;
		
		init();
	}
	
	private void init() {
		IconFontSwing.register(FontAwesome.getIconFont());

		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		JPanel panelKeywordLists = new JPanel();
		panelKeywordLists.setLayout(new BoxLayout(panelKeywordLists, BoxLayout.X_AXIS));
		panelKeywordLists.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		JPanel panelEnabledKeywords = new JPanel();
		panelEnabledKeywords.setLayout(new BoxLayout(panelEnabledKeywords, BoxLayout.PAGE_AXIS));
		
		JLabel label1 = new JLabel("Enabled Keywords");
		label1.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelEnabledKeywords.add(label1);
		
		DefaultListModel<String> modelEnabledKeywords = new DefaultListModel<>();
		for (String item : enabledKeywords) {
			modelEnabledKeywords.addElement(item);
		}
		
		listEnabledKeywords = new JList<>();
		listEnabledKeywords.setModel(modelEnabledKeywords);
		listEnabledKeywords.setPreferredSize(new Dimension(100, 200));
		listEnabledKeywords.setLayoutOrientation(JList.VERTICAL);
		listEnabledKeywords.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane paneEnabledKeywords = new JScrollPane(listEnabledKeywords);
		paneEnabledKeywords.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panelEnabledKeywords.add(new JScrollPane(listEnabledKeywords));
		
		JPanel panelDisabledKeywords = new JPanel();
		panelDisabledKeywords.setLayout(new BoxLayout(panelDisabledKeywords, BoxLayout.Y_AXIS));
		
		JLabel label2 = new JLabel("Disabled Keywords");
		label2.setAlignmentX(Component.LEFT_ALIGNMENT);
		panelDisabledKeywords.add(label2);
		
		DefaultListModel<String> modelDisabledKeywords = new DefaultListModel<>();
		for (String item : enabledKeywords) {
			modelDisabledKeywords.addElement(item);
		}
		
		listDisabledKeywords = new JList<>();
		listDisabledKeywords.setPreferredSize(new Dimension(100, 200));
		listDisabledKeywords.setModel(modelDisabledKeywords);
		listDisabledKeywords.setLayoutOrientation(JList.VERTICAL);
		listDisabledKeywords.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JScrollPane paneDisabledKeywords = new JScrollPane(listDisabledKeywords);
		paneDisabledKeywords.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		panelDisabledKeywords.add(paneDisabledKeywords);
		
		JPanel panelArrows = new JPanel();
		panelArrows.setLayout(new BoxLayout(panelArrows, BoxLayout.Y_AXIS));
		
		Icon icon1 = IconFontSwing.buildIcon(FontAwesome.ANGLE_LEFT, 20);
		Icon icon2 = IconFontSwing.buildIcon(FontAwesome.ANGLE_RIGHT, 20);
		btnEnable = new JButton(icon1);
		btnDisable = new JButton(icon2);
		
		
		panelArrows.add(new JLabel(" "));
		panelArrows.add(btnDisable);
		panelArrows.add(btnEnable);
		
		panelKeywordLists.add(panelEnabledKeywords);
		panelKeywordLists.add(panelArrows);
		panelKeywordLists.add(panelDisabledKeywords);
			
		this.add(panelKeywordLists);
		
		JPanel panelAddKeyword = new JPanel();
		panelAddKeyword.setLayout(new BoxLayout(panelAddKeyword, BoxLayout.X_AXIS));
		panelAddKeyword.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		txtKeyword = new JTextField();
		panelAddKeyword.add(txtKeyword);
		
		btnAdd = new JButton("Add");
		panelAddKeyword.add(btnAdd);
		
		this.add(panelAddKeyword);
		
	}
}
