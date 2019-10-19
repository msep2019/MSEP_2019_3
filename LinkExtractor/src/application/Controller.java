package application;

import java.awt.CardLayout;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import application.TestJTree.MyTreeNode;
import sossec.cve.CVEItem;
import sossec.cwe.CWEItem;
import sossec.capec.CAPECItem;
import sossec.keywordmatching.Item;

public class Controller {
	private CVEItem cve;
	private MainView view;
	boolean loaded;
	
	DefaultTreeModel model;
	DefaultMutableTreeNode root;

	public Controller(CVEItem m, MainView v) {
		cve = m;
		view = v;
		initView();
	}

	public void initView() {
		view.linkTree.setController(this);
	}

	public void initController() {
		view.linkTree.tree.getSelectionModel().addTreeSelectionListener(e -> selectItem(e));
		view.btnSearch.addActionListener(e -> searchCWE());
		view.panelCVE.btnApply.addActionListener(e -> searchCWE());
	}

	private void searchCWE() {
		System.out.println("searchCWE");
		model = (DefaultTreeModel) view.linkTree.tree.getModel();
		root = (DefaultMutableTreeNode) model.getRoot();

		cve.id = view.txtCVE.getText().trim();
		
		DefaultMutableTreeNode loadingNode  = new DefaultMutableTreeNode("Loading...");
		root.add(loadingNode);
		
		model.nodeChanged(root);
		model.reload();

		if (view.txtCVE.getText().isEmpty()) {
			root.setUserObject("CVE");
		} else {
			root.setUserObject(cve);
		}
		
		System.out.println("Remove child node");
		root.removeAllChildren();
		
		model.nodeChanged(root);
		model.reload();
		
		loaded = false;
		root.add(new DefaultMutableTreeNode("Loading...", false));

		model.nodeStructureChanged(root);
		view.linkTree.tree.expandPath(new TreePath(root.getPath()));

		loadDirectCWEChildren(model, root);
		loadIndirectCWEChildren(model, root);
		System.out.println(model);
	}
	
	public void loadIndirectCWEChildren(final DefaultTreeModel model, DefaultMutableTreeNode node) {
		System.out.println("loadIndirectCWEChildren");
		if (loaded) {
            return;
        }
        SwingWorker<List<CustomTreeNode>, Void> worker = new SwingWorker<List<CustomTreeNode>, Void>() {
        	@Override
            protected List<CustomTreeNode> doInBackground() throws Exception {
        		List<CustomTreeNode> children = new ArrayList<CustomTreeNode>();
        		
        		cve.getIndirectCWEList();
        		
        		System.out.println(cve.indirectCWE);

        		if (cve.indirectCWE.size() > 0) {
        			for (CWEItem itemCWE : cve.indirectCWE) {
        				CustomTreeNode child = new CustomTreeNode(itemCWE, CustomTreeNode.INDIRECT) ;

        				children.add(child);
        			}
        		}
        		
                return children;
            }

            @Override
            protected void done() {
                try {
                    setChildren(node, get());
                    model.nodeStructureChanged(node);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Notify user of error.
                }
                super.done();
            }
        };
        worker.execute();
    }

	private void setChildren(DefaultMutableTreeNode node, List<CustomTreeNode> list) {
		node.removeAllChildren();
        node.setAllowsChildren(list.size() > 0);
        for (MutableTreeNode child : list) {
            node.add(child);
        }
        loaded = true;
        
    }
	
	private void selectItem(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

		System.out.println("Object: " + node.getUserObject());
		if (node.getUserObject() instanceof CVEItem) {
			CVEItem cve = (CVEItem) node.getUserObject();
			
			System.out.println("View: " + view.CVE_OPTION_PANEL);
			System.out.println(view.detailView);
			
			view.panelCVE.setCVE(cve);
			
			CardLayout cardLayout = (CardLayout) view.detailView.getLayout();
			cardLayout.show(view.detailView, view.CVE_OPTION_PANEL);
			


		}
		
		if (node.getUserObject() instanceof CWEItem) {
			CWEItem cwe = (CWEItem) node.getUserObject();
			
			model = (DefaultTreeModel) view.linkTree.tree.getModel();
			
			node.add(new DefaultMutableTreeNode("Loading...", false));
			
			if (cwe.indirectCAPEC.size() <= 0) {
				loaded = false;
				
				model.nodeStructureChanged(node);
				view.linkTree.tree.expandPath(new TreePath(node.getPath()));
				
				loadIndirectCAPECChildren(cwe, model, node);
			}
			
			view.panelCWE.setCWE(cwe);
			
			System.out.println("View: " + view.CWE_OPTION_PANEL);
			System.out.println(view.detailView);
			CardLayout cardLayout = (CardLayout) view.detailView.getLayout();
			cardLayout.show(view.detailView, view.CWE_OPTION_PANEL);
			
		} else if (node.getUserObject() instanceof CAPECItem) {
			System.out.println("View: " + view.CAPEC_OPTION_PANEL);
			System.out.println(view.detailView);
			view.detailViewLayout.show(view.detailView, view.CAPEC_OPTION_PANEL);
		}
	}
	
	public void loadIndirectCAPECChildren(CWEItem cwe, final DefaultTreeModel model, DefaultMutableTreeNode node) {
		System.out.println("loadIndirectCWEChildren");
		if (loaded) {
            return;
        }
        SwingWorker<List<CustomTreeNode>, Void> worker = new SwingWorker<List<CustomTreeNode>, Void>() {
        	@Override
            protected List<CustomTreeNode> doInBackground() throws Exception {
        		
        		List<CustomTreeNode> children = new ArrayList<CustomTreeNode>();
        		
        		cwe.getIndirectCAPECList();
    			
    			if (cwe.indirectCAPEC.size() > 0) {
    				for (CAPECItem itemCAPEC : cwe.indirectCAPEC) {
    					CustomTreeNode child = new CustomTreeNode(itemCAPEC, CustomTreeNode.INDIRECT);

    					children.add(child);
    				}

    				view.linkTree.tree.expandPath(new TreePath(node.getPath()));
    			}
        		
                return children;
            }

            @Override
            protected void done() {
                try {
                    setChildren(node, get());
                    model.nodeStructureChanged(node);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Notify user of error.
                }
                super.done();
            }
        };
        worker.execute();
    }
}