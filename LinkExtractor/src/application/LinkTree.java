package application;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeNode;

import sossec.cve.CVEItem;

public class LinkTree extends JScrollPane implements TreeExpansionListener, TreeWillExpandListener {
	public JTree tree;
	
	public DefaultMutableTreeNode root;
	public DefaultMutableTreeNode directNode;
	public DefaultMutableTreeNode indirectNode;
	
	public TreeNode rootNode;
	private Controller controller;

	public LinkTree() {
		rootNode = createNodes();
		tree = new JTree(rootNode);
		tree.addTreeExpansionListener(this);
		tree.addTreeWillExpandListener(this);
		tree.putClientProperty("JTree.lineStyle", "Angled");
		
		// Change the default JTree icons
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        Icon closedIcon = UIManager.getIcon("Tree.leafIcon");;
        Icon openIcon = UIManager.getIcon("Tree.leafIcon");;
        Icon leafIcon = UIManager.getIcon("Tree.leafIcon");;
        renderer.setClosedIcon(closedIcon);
        renderer.setOpenIcon(openIcon);
        renderer.setLeafIcon(leafIcon);
        renderer.setTextSelectionColor(Color.black);
        renderer.setBackgroundSelectionColor(Color.white);
        renderer.setBorderSelectionColor(Color.LIGHT_GRAY);
        
		setViewportView(tree);		
	}

	private TreeNode createNodes() {
		root = new DefaultMutableTreeNode(null);
		
		root.setUserObject("CVE");
		
		return root;
	}
	
	public void setController(Controller controller) {
		this.controller = controller;
	}

	//Required by TreeWillExpandListener interface.
	public void treeWillExpand(TreeExpansionEvent e) throws ExpandVetoException {
		System.out.println("Tree-will-expand event detected");
	}

	//Required by TreeWillExpandListener interface.
	public void treeWillCollapse(TreeExpansionEvent e) {
		System.out.println("Tree-will-collapse event detected");
	}

	// Required by TreeExpansionListener interface.
	public void treeExpanded(TreeExpansionEvent e) {
		System.out.println("Tree-expanded event detected");
	}

	// Required by TreeExpansionListener interface.
	public void treeCollapsed(TreeExpansionEvent e) {
		System.out.println("Tree-collapsed event detected");
	}
}
