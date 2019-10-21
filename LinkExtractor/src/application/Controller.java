package application;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import sossec.cve.CVEItem;
import sossec.cwe.CWEItem;
import sossec.capec.CAPECItem;

public class Controller {
	private CVEItem cve;
	private MainView view;
	boolean loaded;
	
	DefaultTreeModel model;
	DefaultMutableTreeNode root;

	public Controller(MainView v) {
		view = v;
		initView();
	}

	public void initView() {
		view.linkTree.setController(this);
	}

	public void initController() {
		view.linkTree.tree.getSelectionModel().addTreeSelectionListener(e -> selectItem(e));
		view.btnSearch.addActionListener(e -> searchCWE());
		view.panelCVE.btnApply.addActionListener(e -> reloadCWEList());
		view.panelCWE.btnApply.addActionListener(e -> reloadCAPECList());
		
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
				cwe.loadedChildren = false;
				
				model.nodeStructureChanged(node);
				view.linkTree.tree.expandPath(new TreePath(node.getPath()));
				
				loadCAPECList();
			} else {
				view.panelCWE.setCWE(cwe);
			}
			
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

	private void searchCWE() {
		cve = new CVEItem();
		
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

		loadCWEList();
		System.out.println(model);
		
		view.linkTree.tree.setSelectionPath(new TreePath(root.getPath()));
	}
	

	private void setChildren(DefaultMutableTreeNode node, List<CustomTreeNode> list) {
		node.removeAllChildren();
        node.setAllowsChildren(list.size() > 0);
        for (MutableTreeNode child : list) {
            node.add(child);
        }
    }
	
	public void loadCWEList() {
		System.out.println("loadCWEList");
		if (loaded) {
            return;
        }
		
		model = (DefaultTreeModel) view.linkTree.tree.getModel();
		root = (DefaultMutableTreeNode) model.getRoot();
		
        SwingWorker<List<CustomTreeNode>, Void> worker = new SwingWorker<List<CustomTreeNode>, Void>() {
        	@Override
            protected List<CustomTreeNode> doInBackground() throws Exception {
        		List<CustomTreeNode> children = new ArrayList<CustomTreeNode>();
        		
        		cve.getDirectCWEList();
        		
        		System.out.println(cve.directCWE);
        		
        		if (cve.directCWE.size() > 0) {
        			for (CWEItem itemCWE : cve.directCWE) {
        				CustomTreeNode child = new CustomTreeNode(itemCWE, CustomTreeNode.DIRECT) ;

        				children.add(child);
        			}
        		}
        		
        		cve.getIndirectCWEList();
        		
        		System.out.println(cve.indirectCWE);

        		if (cve.indirectCWE.size() > 0) {
        			int maxMatching = cve.indirectCWE.get(0).matching;
        			if (cve.maxMatching != maxMatching) {
	        			if (maxMatching >= 4) {
	        				cve.minMatching = 4;
	        			} else {
	        				cve.minMatching = maxMatching;
	        			}
        			}
        			
        			cve.maxMatching = maxMatching;
        			
        			view.panelCVE.setSimilarity(cve.maxMatching, cve.minMatching);
        			
        			for (CWEItem itemCWE : cve.indirectCWE) {
        				if (itemCWE.matching >= cve.minMatching) {
        					CustomTreeNode child = new CustomTreeNode(itemCWE, CustomTreeNode.INDIRECT) ;
                			//view.panelCVE.cboSimilarity
            				children.add(child);
        				}
        			}
        		} else {
        			cve.minMatching = 0;
        			cve.maxMatching = 0;
        			view.panelCVE.setSimilarity(cve.maxMatching, cve.minMatching);
        		}
        		
                return children;
            }

            @Override
            protected void done() {
                try {
                    setChildren(root, get());
                    loaded = true;
                    model.nodeStructureChanged(root);
                    view.linkTree.tree.setSelectionPath(new TreePath(root.getPath()));
                } catch (Exception e) {
                    e.printStackTrace();
                    // Notify user of error.
                }
                super.done();
            }
        };
        worker.execute();
    }
	
	public void reloadCWEList() {
		model = (DefaultTreeModel) view.linkTree.tree.getModel();
		root = (DefaultMutableTreeNode) model.getRoot();
		
		loaded = false;
		cve.indirectCWE = new ArrayList<>();
		root.removeAllChildren();
		root.add(new DefaultMutableTreeNode("Loading...", false));
		model.nodeStructureChanged(root);
		loadCWEList();
	}
	
	public void loadCAPECList() {
		System.out.println("loadCAPECList");
		
		CustomTreeNode node = (CustomTreeNode)view.linkTree.tree.getLastSelectedPathComponent();
		model = (DefaultTreeModel) view.linkTree.tree.getModel();
		CWEItem cwe = (CWEItem) node.getUserObject();
		
		if (cwe.loadedChildren) {
            return;
        }
		
        SwingWorker<List<CustomTreeNode>, Void> worker = new SwingWorker<List<CustomTreeNode>, Void>() {
        	@Override
            protected List<CustomTreeNode> doInBackground() throws Exception {
        		
        		List<CustomTreeNode> children = new ArrayList<CustomTreeNode>();
        		
        		
        		
        		cwe.getDirectCAPECList();
    			
        		if (cwe.directCAPEC.size() > 0) {
    				for (CAPECItem itemCAPEC : cwe.directCAPEC) {
    					CustomTreeNode child = new CustomTreeNode(itemCAPEC, CustomTreeNode.DIRECT);

    					children.add(child);
    				}
    			}
    			
        		cwe.getIndirectCAPECList();
    			
    			if (cwe.indirectCAPEC.size() > 0) {
    				int maxMatching = cwe.indirectCAPEC.get(0).matching;
    				
    				if (cwe.minMatching == -1) {
            			if (maxMatching >= 4) {
            				cwe.minMatching = 4;
            			} else {
            				cwe.minMatching = maxMatching;
            			}
    				}
        			
    				view.panelCWE.setSimilarity(maxMatching, cwe.minMatching);
    				
    				for (CAPECItem itemCAPEC : cwe.indirectCAPEC) {
    					if (itemCAPEC.matching >= cwe.minMatching) {
	    					CustomTreeNode child = new CustomTreeNode(itemCAPEC, CustomTreeNode.INDIRECT);
	
	    					children.add(child);
    					}
    				}
    			}
    			
    			view.linkTree.tree.expandPath(new TreePath(node.getPath()));
        		
                return children;
            }

            @Override
            protected void done() {
                try {
                    setChildren(node, get());
                    cwe.loadedChildren = true;
                    model.nodeStructureChanged(node);
                    view.panelCWE.setCWE(cwe);
                } catch (Exception e) {
                    e.printStackTrace();
                    // Notify user of error.
                }
                super.done();
            }
        };
        worker.execute();
    }
	
	public void reloadCAPECList() {
		System.out.println("reloadCAPECList");
		CustomTreeNode node = (CustomTreeNode)view.linkTree.tree.getLastSelectedPathComponent();
		CWEItem cwe = (CWEItem) node.getUserObject();
		cwe.loadedChildren = false;
		loadCAPECList();
	}
}