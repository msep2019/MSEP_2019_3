package application;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;

import sossec.mitigation.Mitigation;

public class MitigationPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JList<Mitigation> listMitigations;
	
	public MitigationPanel() {
		this.setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setBackground(Color.white);
	}
	public void setMitigations(List<Mitigation> mitigations) {
		
	}
}