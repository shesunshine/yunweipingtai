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

public class OPSDataWorkbenchWindowAdvisor extends CommonWorkbenchWindowAdvisor {

	private static final String SHELLID = "com.hhh.platform.ops.shell.data";

	public OPSDataWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer, CommonHeader head,
			CommonMenuManager menuManager) {
		super(configurer, head, menuManager, null, true);
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

		Composite pageParent = new Composite(shell, SWT.NONE);
		pageParent.setData(RWT.CUSTOM_VARIANT, "pageArea");
		FormData pageParentData = new FormData();
		pageParentData.left = new FormAttachment(0, 0);
		pageParentData.top = new FormAttachment(0, 0);
		pageParentData.right = new FormAttachment(100, 0);
		pageParentData.bottom = new FormAttachment(100, 0);
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
}
