package sossec.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class JDialogOutput extends JDialog {

	public JDialogOutput(){
		super(new MyJFrame(), "SoSSec Output", true);
		Container container = this.getContentPane();
		JLabel jl = new JLabel("These are the results.");
		//Container.add(JLabel);
		this.setSize(1280, 720);
		
	}
	
	public static void main(String[] args){
		
		new JDialogOutput().setVisible(true);

	}
	
}
class MyJFrame extends JFrame{
	public void CreateJFrame(){
		
		this.setVisible(true);
		this.setSize(1280,720);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	
		Container container = this.getContentPane();
		container.setLayout(null);
	
		JButton jb = new JButton("Open dialog");
		jb.setBounds(30,30,200,50);
		jb.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0){
				new JDialogOutput().setVisible(true);
			}

		});
		container.add(jb);
	}
}

