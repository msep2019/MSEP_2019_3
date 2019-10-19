package application;

import javax.swing.tree.DefaultMutableTreeNode;

public class CustomTreeNode extends DefaultMutableTreeNode {
	final static int DIRECT = 0;
	final static int INDIRECT = 1;
	
	public int type = -1;
	
	public CustomTreeNode(Object userObject, int type) {
		this.setUserObject(userObject);
		this.type = type;
	}
}
