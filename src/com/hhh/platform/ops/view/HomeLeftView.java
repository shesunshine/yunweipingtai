package com.hhh.platform.ops.view;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.client.service.BrowserNavigation;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.hhh.platform.ops.statistic.DataSummaryHelper;

public class HomeLeftView extends ViewPart {

	public static final String ID = "com.hhh.platform.ops.view.home.left";

	protected Composite partParent;
	private Tree tree;

	/**
	 * 所在透视图的ID
	 */
	protected String perspectiveId;

	public void createPartControl(Composite parent) {
		this.perspectiveId = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getPerspective().getId();
		this.partParent = parent;
		parent.setLayout(new FormLayout());
		tree = new Tree(parent, SWT.FULL_SELECTION);
		tree.setLayoutData(createLayoutDataForTree());
		fillTree(parent);
	}

	private void fillTree(Composite parent) {
		final BrowserNavigation navigation = RWT.getClient().getService(BrowserNavigation.class);
		TreeItem item = new TreeItem(tree, SWT.NONE);
		item.setText("统计1");
		tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				try {
					DataSummaryHelper.statisticLogExeption();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private FormData createLayoutDataForTree() {
		FormData layoutData = new FormData();
		layoutData.top = new FormAttachment(0, 0);
		layoutData.left = new FormAttachment(0, 0);
		layoutData.bottom = new FormAttachment(100, 0);
		layoutData.width = 190;
		return layoutData;
	}

	public void cleanAllControls() {
		IViewSite site = (IViewSite) this.getSite();
		final IActionBars actionbars = site.getActionBars();
		final ToolBarManager toolManager = (ToolBarManager) actionbars.getToolBarManager();
		toolManager.removeAll();
		actionbars.updateActionBars();
		this.setPartName("");
	}

	public String getPerspectiveId() {
		return perspectiveId;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}
