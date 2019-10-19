package application;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;

import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.swing.IconFontSwing;

public class KeywordPanel extends JPanel {
	public JList<String> listEnabledKeywords;
	public JList<String> listDisabledKeywords;
	public JButton btnEnable;
	public JButton btnDisable;

	public JTextField txtKeyword;
	public JButton btnAdd;
	
	JScrollPane paneEnabledKeywords;
	JScrollPane paneDisabledKeywords;

	public ArrayList<String> enabledKeywords;
	public ArrayList<String> disabkedKeywords;

	public KeywordPanel(ArrayList<String> enabledKeywords, ArrayList<String> disabkedKeywords) {
		this.enabledKeywords = enabledKeywords;
		this.disabkedKeywords = disabkedKeywords;

		init();
	}

	private void init() {
		IconFontSwing.register(FontAwesome.getIconFont());

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1.0;

		c.gridx = 0;
		c.gridy = 0;
		this.add(new JLabel("Enabled Keywords"), c);

		c.gridx = 2;
		c.gridy = 0;
		this.add(new JLabel("Disabled Keywords"), c);

		DefaultListModel<String> modelEnabledKeywords = new DefaultListModel<>();
		for (String item : enabledKeywords) {
			modelEnabledKeywords.addElement(item);
		}

		listEnabledKeywords = new JList<>();
		listEnabledKeywords.setModel(modelEnabledKeywords);
		listEnabledKeywords.setPreferredSize(new Dimension(100, 200));
		listEnabledKeywords.setLayoutOrientation(JList.VERTICAL);
		listEnabledKeywords.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		paneEnabledKeywords = new JScrollPane(listEnabledKeywords, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		paneEnabledKeywords.setPreferredSize(new Dimension(0, 300));

		c.gridx = 0;
		c.gridy = 1;
		this.add(paneEnabledKeywords, c);

		DefaultListModel<String> modelDisabledKeywords = new DefaultListModel<>();
		for (String item : enabledKeywords) {
			modelDisabledKeywords.addElement(item);
		}

		listDisabledKeywords = new JList<>();
		listDisabledKeywords.setPreferredSize(new Dimension(100, 200));
		listDisabledKeywords.setModel(modelDisabledKeywords);
		listDisabledKeywords.setLayoutOrientation(JList.VERTICAL);
		listDisabledKeywords.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		paneDisabledKeywords = new JScrollPane(listDisabledKeywords,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		paneDisabledKeywords.setPreferredSize(new Dimension(0, 300));

		c.gridx = 2;
		c.gridy = 1;
		this.add(paneDisabledKeywords, c);

		JPanel panelArrows = new JPanel();
		panelArrows.setLayout(new BoxLayout(panelArrows, BoxLayout.Y_AXIS));

		Icon icon1 = IconFontSwing.buildIcon(FontAwesome.ANGLE_LEFT, 20);
		Icon icon2 = IconFontSwing.buildIcon(FontAwesome.ANGLE_RIGHT, 20);
		btnEnable = new JButton(icon1);
		btnDisable = new JButton(icon2);

		panelArrows.add(btnDisable);
		panelArrows.add(btnEnable);
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 1;
		this.add(panelArrows, c);

		JPanel panelAddKeyword = new JPanel();
		panelAddKeyword.setLayout(new BoxLayout(panelAddKeyword, BoxLayout.X_AXIS));
		panelAddKeyword.setAlignmentX(Component.LEFT_ALIGNMENT);

		txtKeyword = new JTextField();
		panelAddKeyword.add(txtKeyword);

		btnAdd = new JButton("Add");
		panelAddKeyword.add(btnAdd);
		c.weightx = 1;
		c.gridx = 0;
		c.gridy = 2;
		this.add(panelAddKeyword, c);
	}
}
