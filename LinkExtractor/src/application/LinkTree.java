package application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
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
import sossec.cwe.CWEItem;

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
		CustomIconRenderer renderer = null;
		try {
			renderer = new CustomIconRenderer();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		renderer.setTextSelectionColor(Color.black);
		renderer.setBackgroundSelectionColor(Color.white);
		renderer.setBorderSelectionColor(Color.LIGHT_GRAY);
		
        Icon leafIcon = UIManager.getIcon("Tree.leafIcon");
        renderer.setLeafIcon(leafIcon);
        renderer.setClosedIcon(leafIcon);
        renderer.setOpenIcon(leafIcon);


		tree.setCellRenderer(renderer);
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

class CustomIconRenderer extends DefaultTreeCellRenderer {

    /**
     *
     */
    private static final long serialVersionUID = 967937360839244309L;
    ImageIcon iconDirect = null;
    ImageIcon iconIndirect = null;

    public CustomIconRenderer() throws MalformedURLException {
    	try {
    		URL imageUrl;
    		BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);;
    		
    		imageUrl = getClass().getResource("/assets/images/link_go.png");
			image = ImageIO.read(imageUrl);
			iconDirect = new ImageIcon(image);
			
			imageUrl = getClass().getResource("/assets/images/link.png");
			image = ImageIO.read(imageUrl);
			iconIndirect = new ImageIcon(image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
		
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean sel, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                row, hasFocus);

        if (value instanceof CustomTreeNode) {
        	if (((CustomTreeNode)value).type == CustomTreeNode.DIRECT) {
        		setIcon(iconDirect);
            } else if (((CustomTreeNode)value).type == CustomTreeNode.INDIRECT) {
            	setIcon(iconIndirect);
            }
        }
        
        return this;
    }
}
