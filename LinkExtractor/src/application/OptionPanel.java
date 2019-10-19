package application;

import java.awt.BorderLayout;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;

public class OptionPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JButton btnApply;
	public CVEOptionPanel panelCVEOption;
	
	public OptionPanel() {
		init();
	}
	
	
	public void init() {
		setLayout(new BorderLayout());
		
		btnApply = new JButton("Apply");
		
		JPanel panelTop = new JPanel();
		GroupLayout layout = new GroupLayout(panelTop);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
		                   GroupLayout.PREFERRED_SIZE, Short.MAX_VALUE)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(btnApply)));

		layout.setVerticalGroup(
				layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(btnApply)));

		panelTop.setLayout(layout);
		add(panelTop, BorderLayout.SOUTH);
		
//		panelCVEOption = new CVEOptionPanel();
//		
//		add(panelCVEOption, BorderLayout.CENTER);
	}
}
