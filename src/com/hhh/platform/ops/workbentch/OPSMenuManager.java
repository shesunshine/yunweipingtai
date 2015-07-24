package com.hhh.platform.ops.workbentch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.hhh.platform.advisors.framework.CommonMenuManager;

@SuppressWarnings("serial")
public class OPSMenuManager extends CommonMenuManager {

	private List<ToolItem> toolSubMenuBarItemList = new ArrayList<ToolItem>();
	private Composite subMenuBarArea;

	/**
	 * 清空工具栏的所有元素
	 */
	private void disposeSubToolItems() {
		int size = toolSubMenuBarItemList.size();
		for (int i = 0; i < size; i++) {
			toolSubMenuBarItemList.remove(0);
		}
		Control[] ctls = subMenuBarArea.getChildren();
		for (int i = 0; i < ctls.length; i++) {
			ctls[i].dispose();
		}
		subMenuBarArea.layout(true, true);

	}

/*	protected void createMenu(final MenuManager manager, final ToolItem toolItem) {
		final Menu menu = new Menu(menuBarArea);
		toolItem.setData(menu);
		toolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				MakeSubMenu(manager, toolItem);
				final SelectionAdapter selectionAdapter = this;
				toolItem.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent event) {
						toolItem.removeDisposeListener(this);
						toolItem.removeSelectionListener(selectionAdapter);
					}

				});
			}

			private void MakeSubMenu(final MenuManager manager, ToolItem toolItem) {
				disposeSubToolItems();
				toolSubMenuBar = new ToolBar(subMenuBarArea, SWT.FLAT | SWT.HORIZONTAL | SWT.WRAP);
				toolSubMenuBar.setData(RWT.CUSTOM_VARIANT, "subMenu");
				IContributionItem[] items = manager.getItems();
				if (items.length > 0) {
					for (int i = 0; i < items.length; i++) {
						IContributionItem item = items[i];
						if (item.isVisible()) {
							IContributionItem tempItem = null;
							if (item instanceof SubContributionItem) {
								SubContributionItem subItem = (SubContributionItem) item;
								tempItem = subItem.getInnerItem();
							} else if (item instanceof MenuManager) {
								tempItem = item;
							}
							if (tempItem != null && tempItem instanceof MenuManager) {
								final MenuManager menumanager = (MenuManager) tempItem;
								final ToolItem newItem = new ToolItem(toolSubMenuBar, SWT.PUSH);
								newItem.setText(menumanager.getMenuText());
								newItem.setData(RWT.CUSTOM_VARIANT, "subMenu");
								createSubMenu(menumanager, newItem);
								toolSubMenuBarItemList.add(newItem);
							} else {
//								CommandContributionItemParameter contributionParameters = ((CommandContributionItem)item).getData();
//								contributionParameters.style = SWT.RADIO;
//								CommandContributionItem newItem = new CommandContributionItem(contributionParameters);
								item.fill(toolSubMenuBar, -1);
								toolSubMenuBar.getItem(toolSubMenuBar.getItemCount() - 1).setData(
										RWT.CUSTOM_VARIANT, "subMenu");
								toolSubMenuBarItemList.add(toolSubMenuBar.getItem(toolSubMenuBar
										.getItemCount() - 1));
							}
						}
					}
					manager.update(true);

					RowLayout layout = resetToolSubMenuBarLocation(toolItem);
					toolSubMenuBar.setLayout(layout);
					subMenuBarArea.layout(true, true);

				}
			}

			private RowLayout resetToolSubMenuBarLocation(ToolItem toolItem) {
				Rectangle bounds = toolItem.getBounds();
				int leftIndent = bounds.x -2;
				RowLayout layout = (RowLayout) subMenuBarArea.getLayout();
				layout.marginLeft = leftIndent;
				return layout;
			}
		});
	}

	protected void createSubMenu(final MenuManager manager, final ToolItem toolItem) {
		final Menu menu = new Menu(subMenuBarArea);
		toolItem.setData(menu);
		toolItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(final SelectionEvent e) {
				MenuItem[] menuItems = menu.getItems();
				for (int i = 0; i < menuItems.length; i++) {
					menuItems[i].dispose();
				}
				fillMenu(manager, menu);
				Display display = toolItem.getDisplay();
				Rectangle bounds = toolItem.getBounds();
				int leftIndent = bounds.x;
				int topIndent = bounds.y + bounds.height;
				Point indent = new Point(leftIndent, topIndent);
				Point menuLocation = display.map(toolSubMenuBar, toolSubMenuBar.getShell(), indent);
				menu.setLocation(menuLocation);
				menu.setVisible(true);
				menu.setData(RWT.CUSTOM_VARIANT, "thirdSubMenu");
				menuItems = menu.getItems();
				for (int i = 0; i < menuItems.length; i++) {
					menuItems[i].setData(RWT.CUSTOM_VARIANT, "thirdSubMenu");
				}
				final SelectionAdapter selectionAdapter = this;
				toolItem.addDisposeListener(new DisposeListener() {
					public void widgetDisposed(DisposeEvent event) {
						toolItem.removeDisposeListener(this);
						toolItem.removeSelectionListener(selectionAdapter);
					}

				});
			}

			private void fillMenu(final MenuManager manager, final Menu menu) {
				IContributionItem[] contribItems = manager.getItems();
				if (contribItems != null && contribItems.length > 0) {
					for (int i = 0; i < contribItems.length; i++) {
						if (i > 0 || !(contribItems[i] instanceof Separator)) {
							contribItems[i].fill(menu, -1);
						}
					}
				}
			};

		});
	}*/

	@Override
	public void fill(Composite menuBarArea) {
		menuBarArea.setLayout(new FormLayout());

		this.menuBarArea = new Composite(menuBarArea, SWT.NONE);
		FormData fd = new FormData();
		fd.top = new FormAttachment(0, 0);
		fd.left = new FormAttachment(0, 20);
		fd.right = new FormAttachment(100, 0);
		fd.bottom = new FormAttachment(100, 0);
		this.menuBarArea.setLayoutData(fd);
		this.menuBarArea.setData(RWT.CUSTOM_VARIANT, "menuBar");

		RowLayout layout = new RowLayout();
		layout.marginLeft = 10;
		layout.marginRight = 10;
		layout.marginTop = 0;
		layout.marginBottom = 0;
		this.menuBarArea.setLayout(layout);
		
//		FillLayout layout = new FillLayout(SWT.HORIZONTAL);
		this.menuBarArea.setLayout(layout);

		toolMenuBar = new ToolBar(this.menuBarArea, SWT.FLAT | SWT.HORIZONTAL | SWT.WRAP);
		toolMenuBar.setData(RWT.CUSTOM_VARIANT, "mainMenuToolBar");
		toolMenuBar.setFont(new Font(Display.getCurrent(), "微软雅黑",16, SWT.BOLD));
		update(false, false);
	}
}
