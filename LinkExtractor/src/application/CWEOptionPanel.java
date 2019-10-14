package application;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CWEOptionPanel extends JPanel{
	public JLabel lblKeywords;

	public CWEOptionPanel() {

		lblKeywords = new JLabel("Keywords");

		JPanel panelTop = new JPanel();
		GroupLayout layout = new GroupLayout(panelTop);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblKeywords)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(lblKeywords)));

		panelTop.setLayout(layout);
	}
}
