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

import report.Report;
import sossec.cve.CVEItem;
import sossec.cwe.CWEItem;
import sossec.mitigation.Mitigation;
import sossec.mitigation.SecurityPattern;
import sossec.capec.CAPECItem;

public class Controller {
	private CVEItem cve;
	private MainView view;
	boolean loaded;

	DefaultTreeModel model;
	DefaultMutableTreeNode root;

	final String NO_MITIGATION = "No mitigations found.";
	final String NO_SECUIRITY_PATTERN = "No security patterns found.";
	final String LOADING = "Loading...";
	
	private long cweStartTime;
	private long cweEndTime;
	private long capecStartTime;
	private long capecEndTime;
	private long mitigationStartTime;
	private long mitigationEndTime;
	private long securityPatternStartTime;
	private long securityPatternEndTime;

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
		view.selectType.addActionListener(e -> reloadCWEList());
		view.menuReport.addActionListener(e -> (new Report()).printCVEReport(cve));
	}

	private void selectItem(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

		view.listMitigations.setModel(new DefaultListModel<>());
		view.listMitigations.revalidate();

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

			if (node.getChildCount() == 0
					|| !((DefaultMutableTreeNode) node.getFirstChild()).getUserObject().toString().equals(LOADING)) {
				node.add(new DefaultMutableTreeNode(LOADING, false));
			}
			
			if (cwe.directCAPEC.size() <= 0 && cwe.indirectCAPEC.size() <= 0) {
				DefaultListModel<Mitigation> modelMitigations = new DefaultListModel<>();

				if (modelMitigations.size() == 0) {
					modelMitigations.addElement(new Mitigation(LOADING));
				}

				view.listMitigations.setModel(modelMitigations);
				view.listMitigations.revalidate();

			}

			cwe.loadedChildren = false;

			model.nodeStructureChanged(node);
			view.linkTree.tree.expandPath(new TreePath(node.getPath()));
			loadCAPECList(node);

			view.panelCWE.setCWE(cwe);
			System.out.println("View: " + view.CWE_OPTION_PANEL);
			System.out.println(view.detailView);
			CardLayout cardLayout = (CardLayout) view.detailView.getLayout();
			cardLayout.show(view.detailView, view.CWE_OPTION_PANEL);

		} else if (node.getUserObject() instanceof CAPECItem) {
			System.out.println("View: " + view.CAPEC_OPTION_PANEL);
			System.out.println(view.detailView);

			reloadMitigationList((CAPECItem) node.getUserObject());
			CardLayout cardLayout = (CardLayout) view.detailView.getLayout();
			cardLayout.show(view.detailView, view.CAPEC_OPTION_PANEL);

		}
	}

	private void searchCWE() {
		cweStartTime = System.nanoTime();
		
		cve = new CVEItem();
		cve.id = view.txtCVE.getText().trim();
		
		if (cve.id.isEmpty()) {
			return;
		}
		
		System.out.println("searchCWE");
		
		model = (DefaultTreeModel) view.linkTree.tree.getModel();
		root = (DefaultMutableTreeNode) model.getRoot();

		DefaultMutableTreeNode loadingNode = new DefaultMutableTreeNode(LOADING);
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
		root.add(new DefaultMutableTreeNode(LOADING, false));

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

				if (!view.selectType.getSelectedItem().toString().equals("Indirect")) {
					if (cve.directCWE.size() <= 0) {
						cve.getDirectCWEList();
					}

					if (cve.directCWE.size() > 0) {
						for (CWEItem itemCWE : cve.directCWE) {
							CustomTreeNode child = new CustomTreeNode(itemCWE, CustomTreeNode.DIRECT);

							children.add(child);
						}
					}
				}

				// System.out.println(cve.directCWE);
				if (!view.selectType.getSelectedItem().toString().equals("Direct")) {
					System.out.println("cve.isChangedKeywords" + cve.isChangedKeywords);
					if (cve.isChangedKeywords) {
						cve.getIndirectCWEList();
						cve.isChangedKeywords = false;
					}

					// System.out.println(cve.indirectCWE);

					if (cve.indirectCWE.size() > 0) {
						int maxMatching = cve.indirectCWE.get(0).matching;
						int cboMatchingValue;

						if (view.panelCVE.cboSimilarity.getSelectedItem() != null) {
							cboMatchingValue = Integer
									.parseInt(view.panelCVE.cboSimilarity.getSelectedItem().toString());
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

							cve.maxMatching = maxMatching;
							view.panelCVE.setSimilarity(cve.maxMatching, cve.minMatching);
						}

						System.out.println("cboMatchingValue : " + cboMatchingValue);
						System.out.println("cve.maxMatching : " + cve.maxMatching);
						System.out.println("cve.minMatching : " + cve.minMatching);

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
					
					cweEndTime = System.nanoTime();
					long timeElapsed = cweEndTime - cweStartTime;
					System.out.println("Load CWE - Execution time in milliseconds : " + timeElapsed / 1000000);
					cve.metadata.put("processing-time", Long.toString(timeElapsed / 1000000));
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
		root.add(new DefaultMutableTreeNode(LOADING, false));

		// model.nodeStructureChanged(root);
		loadCWEList();

	}

	public void loadCAPECList(DefaultMutableTreeNode node) {
		capecStartTime = System.nanoTime();
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

				if (!view.selectType.getSelectedItem().toString().equals("Indirect")) {
					if (cwe.directCAPEC.size() <= 0) {
						cwe.getDirectCAPECList();
					}

					if (cwe.directCAPEC.size() > 0) {
						for (CAPECItem itemCAPEC : cwe.directCAPEC) {
							CustomTreeNode child = new CustomTreeNode(itemCAPEC, CustomTreeNode.DIRECT);

							children.add(child);
						}
					}
				}

				if (!view.selectType.getSelectedItem().toString().equals("Direct")) {
					System.out.println("cwe.isChangedKeywords" + cwe.isChangedKeywords);
					if (cwe.isChangedKeywords) {
						cwe.getIndirectCAPECList();
						cwe.isChangedKeywords = false;
					}

					if (cwe.indirectCAPEC.size() > 0) {
						int maxMatching = cwe.indirectCAPEC.get(0).matching;
						int cboMatchingValue;

						if (view.panelCWE.cboSimilarity.getSelectedItem() != null) {
							cboMatchingValue = Integer
									.parseInt(view.panelCWE.cboSimilarity.getSelectedItem().toString());
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
				}

				System.out.println("cwe.isChangedKeywords" + cwe.isChangedKeywords);
				if (cwe.isChangedKeywords) {
					cwe.getIndirectCAPECList();
					cwe.isChangedKeywords = false;
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
					
					capecEndTime = System.nanoTime();
					long timeElapsed = capecEndTime - capecStartTime;
					System.out.println("Load CAPEC - Execution time in milliseconds : " + timeElapsed / 1000000);
					cwe.metadata.put("processing-time", Long.toString(timeElapsed / 1000000));
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

		if (!((DefaultMutableTreeNode) node.getFirstChild()).getUserObject().toString().equals(LOADING)) {
			CWEItem cwe = (CWEItem) node.getUserObject();
			cwe.loadedChildren = false;
			if (cwe.isChangedKeywords) {
				cwe.indirectCAPEC = new ArrayList<>();
			}
			node.removeAllChildren();
			node.add(new DefaultMutableTreeNode(LOADING, false));
			model.nodeStructureChanged(node);

			loadCAPECList(node);
		}
	}

	public void loadMitigationList(CWEItem cwe) {
		mitigationStartTime = System.nanoTime();
		
		System.out.println("loadMitigationList");

		DefaultListModel<Mitigation> modelMitigations = new DefaultListModel<>();

		if (cwe.mitigations == null || cwe.mitigations.size() <= 0) {
			cwe.getMitigations();
		} 
		
		if (cwe.mitigations.size() > 0) {
			for (Mitigation mitigation : cwe.mitigations) {
				modelMitigations.addElement(mitigation);
			}
		}

		for (CAPECItem itemCAPEC : cwe.directCAPEC) {
			if (itemCAPEC.mitigations.size() <= 0) {
				itemCAPEC.getMitigations();
			}

			System.out.println("itemCAPEC.matching: " + itemCAPEC.matching);
			System.out.println("cwe.minMatching " + cwe.minMatching);
			for (Mitigation mitigation : itemCAPEC.mitigations) {
				modelMitigations.addElement(mitigation);
			}
		}

		for (CAPECItem itemCAPEC : cwe.indirectCAPEC) {
			if (itemCAPEC.mitigations.size() <= 0) {
				itemCAPEC.getMitigations();
			}

			if (itemCAPEC.matching >= cwe.minMatching) {
				for (Mitigation mitigation : itemCAPEC.mitigations) {
					modelMitigations.addElement(mitigation);
				}
			}
		}

		if (modelMitigations.size() == 0) {
			modelMitigations.addElement(new Mitigation(NO_MITIGATION));
		}

		view.listMitigations.setModel(modelMitigations);
		view.listMitigations.revalidate();
		
		mitigationEndTime = System.nanoTime();
		long timeElapsed = mitigationEndTime - mitigationStartTime;
		cwe.metadata.put("processing-time", Long.toString(timeElapsed / 1000000));
	}

	public void reloadMitigationList(CAPECItem capec) {
		DefaultListModel<Mitigation> modelMitigations = new DefaultListModel<>();

		for (Mitigation mitigation : capec.mitigations) {
			modelMitigations.addElement(mitigation);
		}

		if (modelMitigations.size() == 0) {
			modelMitigations.addElement(new Mitigation(NO_MITIGATION));
		}

		view.listMitigations.setModel(modelMitigations);
		view.listMitigations.revalidate();
	}

	public void selectMitigation(ListSelectionEvent e) {
		securityPatternStartTime = System.nanoTime();
		// Return if the changes are still being made
		if (e.getValueIsAdjusting()) {
			return;
		}

		/*
		if (view.listMitigations != null && view.listMitigations.getSelectedValue().description.equals(NO_MITIGATION)) {
			System.out.println("selectMitigation");
		}
		*/
			

		DefaultListModel<SecurityPattern> modelPatterns = new DefaultListModel<>();

		if (modelPatterns.size() == 0) {
			modelPatterns.addElement(new SecurityPattern(LOADING));
		}

		view.panelMitigation.listPatterns.setModel(modelPatterns);
		view.panelMitigation.listPatterns.revalidate();

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
						cboMatchingValue = Integer
								.parseInt(view.panelMitigation.cboSimilarity.getSelectedItem().toString());
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

					if (modelPatterns.size() == 0) {
						modelPatterns.addElement(new SecurityPattern(NO_SECUIRITY_PATTERN));
					}

					view.panelMitigation.setMitigation(mitigation);
					view.panelMitigation.listPatterns.setModel(modelPatterns);
					view.panelMitigation.listPatterns.revalidate();
					
					securityPatternEndTime = System.nanoTime();
					long timeElapsed = securityPatternEndTime - securityPatternStartTime;
					System.out.println("Load Security Pattern - Execution time in milliseconds : " + timeElapsed / 1000000);
					mitigation.metadata.put("processing-time", Long.toString(timeElapsed / 1000000));
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