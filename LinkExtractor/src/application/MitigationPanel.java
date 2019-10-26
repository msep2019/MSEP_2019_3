package application;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;

public class MitigationPanel extends JPanel {
	public JList<String> listMitigations;
	
	public MitigationPanel() {
		this.setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setBackground(Color.white);
	}
	public void setMitigations(List<String> mitigations) {
		
	}
}
