package application;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import org.jdom2.Element;

import sossec.cve.CVEItem;
import sossec.cwe.CWEItem;
import sossec.mitigation.Mitigation;
import sossec.mitigation.SecurityPattern;
import sossec.capec.CAPECHelper;
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
		view.listMitigations.addListSelectionListener(e -> selectMitigation(e));
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

			if (node.getChildCount() == 0 || !((DefaultMutableTreeNode) node.getFirstChild()).getUserObject().toString()
					.equals("Loading...")) {
				node.add(new DefaultMutableTreeNode("Loading...", false));

				if (cwe.indirectCAPEC.size() <= 0) {
					cwe.loadedChildren = false;

					model.nodeStructureChanged(node);
					view.linkTree.tree.expandPath(new TreePath(node.getPath()));
					loadCAPECList(node);
				} else {
					view.panelCWE.setCWE(cwe);
				}

				System.out.println("View: " + view.CWE_OPTION_PANEL);
				System.out.println(view.detailView);
				CardLayout cardLayout = (CardLayout) view.detailView.getLayout();
				cardLayout.show(view.detailView, view.CWE_OPTION_PANEL);
			}

		} else if (node.getUserObject() instanceof CAPECItem) {
			System.out.println("View: " + view.CAPEC_OPTION_PANEL);
			System.out.println(view.detailView);
			CardLayout cardLayout = (CardLayout) view.detailView.getLayout();
			cardLayout.show(view.detailView, view.CAPEC_OPTION_PANEL);
		}
	}

	private void searchCWE() {
		cve = new CVEItem();

		System.out.println("searchCWE");
		model = (DefaultTreeModel) view.linkTree.tree.getModel();
		root = (DefaultMutableTreeNode) model.getRoot();

		cve.id = view.txtCVE.getText().trim();

		DefaultMutableTreeNode loadingNode = new DefaultMutableTreeNode("Loading...");
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
						CustomTreeNode child = new CustomTreeNode(itemCWE, CustomTreeNode.DIRECT);

						children.add(child);
					}
				}

				System.out.println("cve.isChangedKeywords" + cve.isChangedKeywords);
				if (cve.isChangedKeywords) {
					cve.getIndirectCWEList();
					cve.isChangedKeywords = false;
				}

				System.out.println(cve.indirectCWE);

				if (cve.indirectCWE.size() > 0) {
					int maxMatching = cve.indirectCWE.get(0).matching;
					int cboMatchingValue;
					if (view.panelCVE.cboSimilarity.getSelectedItem() != null) {
						cboMatchingValue = Integer.parseInt(view.panelCVE.cboSimilarity.getSelectedItem().toString());
					} else {
						cboMatchingValue = -1;
					}

					if (cboMatchingValue > 0) {
						cve.minMatching = cboMatchingValue;
					}

					if (cve.maxMatching != maxMatching) {
						if (cboMatchingValue < 0 || cboMatchingValue > maxMatching) {
							if (maxMatching >= 4) {
								cve.minMatching = 4;
							} else {
								cve.minMatching = maxMatching;
							}
						}
					}

					cve.maxMatching = maxMatching;
					System.out.println("cve.maxMatching : " + cve.maxMatching);
					System.out.println("cve.minMatching : " + cve.minMatching);
					view.panelCVE.setSimilarity(cve.maxMatching, cve.minMatching);

					for (CWEItem itemCWE : cve.indirectCWE) {
						if (itemCWE.matching >= cve.minMatching) {
							CustomTreeNode child = new CustomTreeNode(itemCWE, CustomTreeNode.INDIRECT);
							// view.panelCVE.cboSimilarity
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
		if (cve.isChangedKeywords) {
			cve.indirectCWE = new ArrayList<>();
		}
		root.removeAllChildren();
		root.add(new DefaultMutableTreeNode("Loading...", false));
		model.nodeStructureChanged(root);
		loadCWEList();
	}

	public void loadCAPECList(DefaultMutableTreeNode node) {
		System.out.println("loadCAPECList");

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

				System.out.println("cwe.isChangedKeywords" + cwe.isChangedKeywords);
				if (cwe.isChangedKeywords) {
					cwe.getIndirectCAPECList();
					cwe.isChangedKeywords = false;
				}

				if (cwe.indirectCAPEC.size() > 0) {
					int maxMatching = cwe.indirectCAPEC.get(0).matching;
					int cboMatchingValue;
					if (view.panelCWE.cboSimilarity.getSelectedItem() != null) {
						cboMatchingValue = Integer.parseInt(view.panelCWE.cboSimilarity.getSelectedItem().toString());
					} else {
						cboMatchingValue = -1;
					}

					if (cboMatchingValue > 0) {
						cwe.minMatching = cboMatchingValue;
					}

					if (cwe.maxMatching != maxMatching) {
						if (cboMatchingValue < 0 || cboMatchingValue > maxMatching) {
							if (maxMatching >= 4) {
								cwe.minMatching = 4;
							} else {
								cwe.minMatching = maxMatching;
							}
						}
					}

					cwe.maxMatching = maxMatching;
					System.out.println("cwe.maxMatching : " + cwe.maxMatching);
					System.out.println("cwe.minMatching : " + cwe.minMatching);
					view.panelCWE.setSimilarity(cwe.maxMatching, cwe.minMatching);

					for (CAPECItem itemCAPEC : cwe.indirectCAPEC) {
						System.out.println(itemCAPEC.name + " " + itemCAPEC.matching);
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

					loadMitigationList(cwe);
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
		CustomTreeNode node = (CustomTreeNode) view.linkTree.tree.getLastSelectedPathComponent();
		System.out.println(node.getFirstChild());

		if (!((DefaultMutableTreeNode) node.getFirstChild()).getUserObject().toString().equals("Loading...")) {
			CWEItem cwe = (CWEItem) node.getUserObject();
			cwe.loadedChildren = false;
			if (cwe.isChangedKeywords) {
				cwe.indirectCAPEC = new ArrayList<>();
			}
			node.removeAllChildren();
			node.add(new DefaultMutableTreeNode("Loading...", false));
			model.nodeStructureChanged(node);

			loadCAPECList(node);
		}
	}

	public void loadMitigationList(CWEItem cwe) {
		System.out.println("loadMitigationList");

		CAPECHelper helperCAPEC = new CAPECHelper();
		
		DefaultListModel<Mitigation> modelMitigations = new DefaultListModel<>();

		if (cwe.mitigations == null || cwe.mitigations.size() <= 0) {
			cwe.getMitigations();
			
			for (Mitigation mitigation : cwe.mitigations) {
				modelMitigations.addElement(mitigation);
			}
		}
		
		for (CAPECItem itemCAPEC : cwe.directCAPEC) {
			if (itemCAPEC.mitigations.size() <= 0) {
				List<Element> mitigations = helperCAPEC.getMitigations(itemCAPEC.id);
				System.out.println(mitigations);
				for (Element mitigation : mitigations) {
					if (!mitigation.getValue().trim().isEmpty()) {
						Mitigation mitigationItem = new Mitigation();
						mitigationItem.description = mitigation.getValue();
						itemCAPEC.mitigations.add(mitigationItem);
					}
				}
			}
			
			System.out.println("itemCAPEC.matching: " + itemCAPEC.matching);
			System.out.println("cwe.minMatching " + cwe.minMatching);
			for (Mitigation mitigation : itemCAPEC.mitigations) {
				modelMitigations.addElement(mitigation);
			}
		}

		for (CAPECItem itemCAPEC : cwe.indirectCAPEC) {
			if (itemCAPEC.mitigations.size() <= 0) {
				List<Element> mitigations = helperCAPEC.getMitigations(itemCAPEC.id);
				for (Element mitigation : mitigations) {
					if (!mitigation.getValue().trim().isEmpty()) {
						Mitigation mitigationItem = new Mitigation();
						mitigationItem.description = mitigation.getValue();
						itemCAPEC.mitigations.add(mitigationItem);
					}
				}
			}
			
			if (itemCAPEC.matching >= cwe.minMatching) {
				for (Mitigation mitigation : itemCAPEC.mitigations) {
					modelMitigations.addElement(mitigation);
				}
			}
		}
		
		view.listMitigations.setModel(modelMitigations);
		view.listMitigations.revalidate();
	}
	
	public void selectMitigation(ListSelectionEvent e) {
		// Return if the changes are still being made
		if (e.getValueIsAdjusting()) {
			return;
		}
		
		System.out.println("selectMitigation");
		
		loadSecurityPatterns(view.listMitigations.getSelectedValue());
		
		CardLayout cardLayout = (CardLayout) view.detailView.getLayout();
		cardLayout.show(view.detailView, view.MITIGATION_OPTION_PANEL);
	}
	
	public void loadSecurityPatterns(Mitigation mitigation) {
		System.out.println("loadSecurityPatterns");
//		ArrayList<SecurityPattern> listPattern = mitigation.getSecurityPatterns();
//		System.out.println(listPattern);
		
		DefaultListModel<SecurityPattern> modelPatterns = new DefaultListModel<>();
		
		
		SwingWorker<ArrayList<SecurityPattern>, Void> worker = new SwingWorker<ArrayList<SecurityPattern>, Void>() {
			@Override
			protected ArrayList<SecurityPattern> doInBackground() throws Exception {

				ArrayList<SecurityPattern> children = new ArrayList<SecurityPattern>();

				System.out.println("mitigation.isChangedKeywords" + mitigation.isChangedKeywords);
				if (mitigation.isChangedKeywords) {
					mitigation.getSecurityPatterns();
					mitigation.isChangedKeywords = false;
				}

				if (mitigation.securityPatterns.size() > 0) {
					int maxMatching = mitigation.securityPatterns.get(0).matching;
					int cboMatchingValue;
					if (view.panelMitigation.cboSimilarity.getSelectedItem() != null) {
						cboMatchingValue = Integer.parseInt(view.panelMitigation.cboSimilarity.getSelectedItem().toString());
					} else {
						cboMatchingValue = -1;
					}

					if (cboMatchingValue > 0) {
						mitigation.minMatching = cboMatchingValue;
					}

					if (mitigation.maxMatching != maxMatching) {
						if (cboMatchingValue < 0 || cboMatchingValue > maxMatching) {
							if (maxMatching >= 4) {
								mitigation.minMatching = 4;
							} else {
								mitigation.minMatching = maxMatching;
							}
						}
					}

					mitigation.maxMatching = maxMatching;
					System.out.println("cwe.maxMatching : " + mitigation.maxMatching);
					System.out.println("cwe.minMatching : " + mitigation.minMatching);
					view.panelCWE.setSimilarity(mitigation.maxMatching, mitigation.minMatching);

					for (SecurityPattern pattern : mitigation.securityPatterns) {
						System.out.println(pattern.name + " " + pattern.matching);
						if (pattern.matching >= mitigation.minMatching) {

							children.add(pattern);
						}
					}
				}

				return children;
			}

			@Override
			protected void done() {
				try {
					for (SecurityPattern pattern : get()) {
						modelPatterns.addElement(pattern);
					}
					
					view.panelMitigation.setMitigation(mitigation);
					view.panelMitigation.listPatterns.setModel(modelPatterns);
					view.panelMitigation.listPatterns.revalidate();
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