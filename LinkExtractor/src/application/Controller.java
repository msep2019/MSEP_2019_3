package application;

import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import sossec.cve.CVEItem;
import sossec.cwe.CWEItem;
import sossec.capec.CAPECItem;
import sossec.keywordmatching.Item;

public class Controller {
	private CVEItem cve;
	private MainView view;

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
		DefaultTreeModel model = (DefaultTreeModel) view.linkTree.tree.getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

		cve.id = view.txtCVE.getText();

		if (view.txtCVE.getText().isEmpty()) {
			root.setUserObject("CVE");
		} else {
			root.setUserObject(cve);
		}
		model.nodeChanged(root);

		root.removeAllChildren(); 
		
		cve.getIndirectCWEList();

		view.linkTree.directNode = new DefaultMutableTreeNode("Direct Links");
		root.add(view.linkTree.directNode);

		view.linkTree.indirectNode = new DefaultMutableTreeNode("Indirect Links");
		root.add(view.linkTree.indirectNode);

		if (cve.indirectCWE.size() > 0) {
			for (CWEItem itemCWE : cve.indirectCWE) {
				DefaultMutableTreeNode child = new DefaultMutableTreeNode(itemCWE) ;

				view.linkTree.indirectNode.add(child);
			}

			view.linkTree.tree.expandPath(new TreePath(view.linkTree.indirectNode.getPath()));
			
			view.panelCVE.setCVE(cve);
		}

	}

	private void selectItem(TreeSelectionEvent e) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

		if (node.getUserObject() instanceof CVEItem) {
			CVEItem cve = (CVEItem) node.getUserObject();
			
			view.panelCVE.setCVE(cve);
		}
		
		if (node.getUserObject() instanceof CWEItem) {
			CWEItem cwe = (CWEItem) node.getUserObject();
			cwe.getIndirectCAPECList();
			
			if (cwe.indirectCAPEC.size() > 0) {
				for (CAPECItem itemCAPEC : cwe.indirectCAPEC) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(itemCAPEC);

					node.add(child);
				}

				view.linkTree.tree.expandPath(new TreePath(node.getPath()));
			}
		} else if (node.getUserObject() instanceof CAPECItem) {
			
		}
	}
}