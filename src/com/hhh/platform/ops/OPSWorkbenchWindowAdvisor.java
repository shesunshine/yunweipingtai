package com.hhh.platform.ops;

import java.lang.reflect.Method;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;

import com.hhh.platform.advisors.framework.CommonHeader;
import com.hhh.platform.advisors.framework.CommonMenuManager;
import com.hhh.platform.advisors.framework.CommonWorkbenchWindowAdvisor;
import com.hhh.platform.advisors.framework.FrameworkConstants;

public class OPSWorkbenchWindowAdvisor extends CommonWorkbenchWindowAdvisor {

	private static final String SHELLID = "com.hhh.platform.ops.shell";

	public OPSWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer, CommonHeader head,
			CommonMenuManager menuManager) {
		super(configurer, head, menuManager, null, true);
	}

	@Override
	protected String getShellID() {
		return SHELLID;
	}

	public void postWindowCreate() {
		Shell shell = getWindowConfigurer().getWindow().getShell();
		shell.setText("Platform OPS");
		shell.setData("user.toolbar.add.show","true");
		shell.setData("user.toolbar.update.show","true");
		shell.setData("user.toolbar.delete.show","true");
		shell.setData("user.toolbar.reset.show","true");
		shell.setData("user.toolbar.authorization.show","true");
		shell.setData("orgnization.toolbar.authorization.show","true");
		
		shell.setData("com.hhh.platform.ops.toolbar.changepassword","true");
		shell.setData("com.hhh.platform.ops.toolbar.logout","true");
		super.postWindowCreate();
	}
	
	

	// 创建窗口显示内容，包括划分区域
	public void createWindowContents(Shell shell) {
		shell.setData(RWT.CUSTOM_VARIANT, "main");
		shell.setData(SHELL_ID, getShellID());
		ApplicationWindow window = ((ApplicationWindow) this.getWindowConfigurer().getWindow());

		try {
			Method m = window.getClass().getDeclaredMethod("largeUpdateStart", new Class[0]);
			m.invoke(window, new Object[0]);
		} catch (Exception e) {
		}
		FormLayout shellLayout = new FormLayout();
		shellLayout.marginWidth = 0;
		shellLayout.marginHeight = 0;
		shellLayout.marginBottom = 0;
		shellLayout.marginTop = 0;
		shell.setLayout(shellLayout);

		// 头部区域，固定高度75
		Composite headerArea = new Composite(shell, SWT.NONE);
		headerArea.setData(RWT.CUSTOM_VARIANT, "headerArea");
		// headerArea.setBackgroundMode(SWT.INHERIT_FORCE);
		FormData headerAreaData = new FormData();
		headerAreaData.left = new FormAttachment(0, 0);
		headerAreaData.right = new FormAttachment(100, 0);
		headerAreaData.top = new FormAttachment(0, 0);
		headerAreaData.bottom = new FormAttachment(0, 60);
		headerArea.setLayoutData(headerAreaData);
		commonHeader.createHeader(headerArea, this.getWindowConfigurer(), this.useCommonBackground);

//		final IMenuService menuService = (IMenuService) this.getWindowConfigurer().getWindow()
//				.getService(IMenuService.class);
//
//		menuService.populateContributionManager((ContributionManager) mainMenuManager, MenuUtil.MAIN_MENU);
//		// MENU权限控制
//		IContributionItem[] items = mainMenuManager.getItems();
//		if (items.length != 0) {
//			this.authzMenu(items);
//		}
//		boolean hasMenubar = false;
//		if (mainMenuManager.getItems().length > 0) {
//			hasMenubar = true;
//		}
//		Composite menuBarArea = null;
//		if (hasMenubar) {
//			// 菜单栏
//			menuBarArea = new Composite(shell, SWT.NONE);
//			// menuBarArea.setData(RWT.CUSTOM_VARIANT, "menuBar");
//			// menuBarArea.setBackgroundMode(SWT.INHERIT_FORCE);
//			FormData menuBarAreaData = new FormData();
//			menuBarAreaData.top = new FormAttachment(headerArea, 0, SWT.BOTTOM);
//			menuBarAreaData.left = new FormAttachment(0, 0);
//			menuBarAreaData.right = new FormAttachment(100, 0);
//			menuBarAreaData.bottom = new FormAttachment(headerArea, 100, SWT.BOTTOM);
//
//			menuBarArea.setLayoutData(menuBarAreaData);
//
//			mainMenuManager.fill(menuBarArea);
//		}
		// line info bar
		/*
		 * Composite lineInfoArea = new Composite(shell, SWT.NONE);
		 * lineInfoArea.setBackground(new Color(Display.getCurrent(), 244, 248,
		 * 255)); FormData lineInfoAreaData = new FormData();
		 * lineInfoAreaData.top = new FormAttachment(vipInfoArea, 0,
		 * SWT.BOTTOM); lineInfoAreaData.bottom = new
		 * FormAttachment(vipInfoArea, 30, SWT.BOTTOM); lineInfoAreaData.left =
		 * new FormAttachment(0, 0); lineInfoAreaData.right = new
		 * FormAttachment(100, 0); lineInfoArea.setLayoutData(lineInfoAreaData);
		 */

		// 当前设置footArea的大小为0
		Composite footerArea = new Composite(shell, SWT.NONE);
		footerArea.setData(RWT.CUSTOM_VARIANT, "footerArea");
		footerArea.setBackgroundMode(SWT.INHERIT_FORCE);
		FormData footerAreaData = new FormData();
		footerAreaData.left = new FormAttachment(0, 0);
		footerAreaData.right = new FormAttachment(100, 0);
		footerAreaData.top = new FormAttachment(100, 0);
		footerAreaData.bottom = new FormAttachment(100, 0);
		footerArea.setLayoutData(footerAreaData);
		commonFooter.createFootler(footerArea, this.getWindowConfigurer(), this.useCommonBackground);

		// 中间显示的部分
		Composite pageParent = new Composite(shell, SWT.NONE);
		pageParent.setData(RWT.CUSTOM_VARIANT, "pageArea");
		FormData pageParentData = new FormData();
		pageParentData.left = new FormAttachment(0, 0);
		pageParentData.top = new FormAttachment(headerArea, 0, SWT.BOTTOM);
		pageParentData.right = new FormAttachment(100, 0);
		pageParentData.bottom = new FormAttachment(footerArea, 0, SWT.TOP);
		pageParent.setLayoutData(pageParentData);
		pageParent.setLayout(new FillLayout());
		Control pageArea = this.getWindowConfigurer().createPageComposite(pageParent);
		pageArea.setVisible(true);
		try {
			Method m = window.getClass().getDeclaredMethod("largeUpdateEnd", new Class[0]);
			m.invoke(window, new Object[0]);
		} catch (Exception e) {
		}
	}

	@Override
	public String getLoginUrl() {
		String loginUrl = "";
		String requestURL = RWT.getRequest().getRequestURL().toString();
		String contextPath = RWT.getRequest().getContextPath();
		int servletBeginPossition = 0;
		if (contextPath.length() > 0) {
			servletBeginPossition = requestURL.indexOf(contextPath) + contextPath.length();
		} else {
			servletBeginPossition = requestURL.lastIndexOf(RWT.getRequest().getRequestURI());
		}
		loginUrl = requestURL.substring(0, servletBeginPossition) + "/ops/" + FrameworkConstants.LOGIN;
		return loginUrl;
	}
}
