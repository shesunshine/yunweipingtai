package com.hhh.platform.ops.workbentch;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.eclipse.jface.action.ContributionManager;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.rap.rwt.widgets.BrowserCallback;
import org.eclipse.rap.rwt.widgets.BrowserUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.menus.MenuUtil;

import com.hhh.platform.advisors.framework.AuthExtensionsTracker;
import com.hhh.platform.advisors.framework.CommonHeader;
import com.hhh.platform.advisors.framework.CommonMenuManager;
import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.authz.AuthzInt;
import com.hhh.platform.advisors.framework.authz.AuthzUtil;
import com.hhh.platform.util.ImageUtil;
import com.hhh.platform.widget.HHHLabel;

public class OPSBaseHead extends CommonHeader {

	protected CommonMenuManager mainMenuManager;

	public OPSBaseHead(OPSMenuManager menuManager) {
		this.mainMenuManager = menuManager;
	}

	public void createHeader(Composite headerArea, Object workbenchWindowConfigurer, boolean useCommonBackground) {
		headerArea.setLayout(new FormLayout());
		// 隐藏的browser，用于执行一些java script脚本
		hiddenBrowser = new Browser(headerArea, SWT.NONE);
		FormData browserAreaData = new FormData();
		browserAreaData.left = new FormAttachment(0, 0);
		browserAreaData.right = new FormAttachment(0, 1);
		browserAreaData.top = new FormAttachment(0, 0);
		browserAreaData.bottom = new FormAttachment(0, 1);
		hiddenBrowser.setLayoutData(browserAreaData);
		BrowserCallback cb = new BrowserCallback() {
			private static final long serialVersionUID = 1L;

			public void evaluationSucceeded(Object result) {
			}

			public void evaluationFailed(Exception exception) {
			}
		};
		BrowserUtil.evaluate(hiddenBrowser, "return window.top.document.location", cb);

		// 头部左边的区域,固定长度550
		Composite leftArea = new Composite(headerArea, SWT.NONE);
		leftArea.setData(RWT.CUSTOM_VARIANT, "leftHeaderArea");
		FormData leftAreaData = new FormData();
		leftAreaData.left = new FormAttachment(0, 20);
		leftAreaData.right = new FormAttachment(0, 200);
		leftAreaData.top = new FormAttachment(0, 0);
		leftAreaData.bottom = new FormAttachment(100, 0);
		leftArea.setLayoutData(leftAreaData);
		fillLeft(leftArea);

		// 头部右边的区域,剩下的区域
		final Composite rightArea = new Composite(headerArea, SWT.NONE);
		rightArea.setData(RWT.CUSTOM_VARIANT, "rightHeaderArea");
		FormData rightAreaData = new FormData();
		rightAreaData.left = new FormAttachment(100, -180);
		rightAreaData.right = new FormAttachment(100, 0);
		rightAreaData.top = new FormAttachment(0, 0);
		rightAreaData.bottom = new FormAttachment(100, 0);
		rightArea.setLayoutData(rightAreaData);

		// 中间可伸缩区域
		Composite centerArea = new Composite(headerArea, SWT.NONE);
		centerArea.setData(RWT.CUSTOM_VARIANT, "centerHeaderArea");
		FormData centerAreaData = new FormData();
		centerAreaData.left = new FormAttachment(leftArea, 0, SWT.RIGHT);
		centerAreaData.right = new FormAttachment(rightArea, 0, SWT.LEFT);
		centerAreaData.top = new FormAttachment(0, 0);
		centerAreaData.bottom = new FormAttachment(100, 0);
		centerArea.setLayoutData(centerAreaData);

		rightArea.setLayout(new FormLayout());
		
		Composite label_c = new Composite(rightArea,SWT.NONE);
		FormData f = new FormData();
		f.left = new FormAttachment(0,0);
		f.right = new FormAttachment(0,60);
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		label_c.setLayoutData(f);
		
		label_c.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite bar_c = new Composite(rightArea,SWT.NONE);
		 f = new FormData();
		f.left = new FormAttachment(0,60);
		f.right = new FormAttachment(100,0);
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		bar_c.setLayoutData(f);
		
		bar_c.setLayout(new FillLayout(SWT.HORIZONTAL));

		HHHLabel loginfo = new HHHLabel(label_c, SWT.NONE);
		loginfo.setData(RWT.CUSTOM_VARIANT, "loginfo");
		loginfo.setText(this.getLoginfo());
		loginfo.setAlignment(SWT.CENTER);
		loginfo.setFont(new Font(Display.getCurrent(), "微软雅黑", 12, SWT.BOLD));
		loginfo.setForeground(new Color(Display.getCurrent(), new RGB(255, 255, 255)));
		// 按钮区
		final Composite mainCoolbarArea = new Composite(bar_c, SWT.NONE);

		RowLayout mainCoolBarLayout = new RowLayout();
		mainCoolBarLayout.center = true;
		mainCoolBarLayout.justify = true;
		mainCoolBarLayout.fill = true;
		mainCoolBarLayout.marginTop = 0;
		mainCoolBarLayout.marginLeft = -10;
		mainCoolBarLayout.marginBottom = 0;
		mainCoolBarLayout.marginRight = 0;
		mainCoolbarArea.setLayout(mainCoolBarLayout);
		if (workbenchWindowConfigurer != null) {
			IWorkbenchWindowConfigurer configer = (IWorkbenchWindowConfigurer) workbenchWindowConfigurer;
			ApplicationWindow window = ((ApplicationWindow) configer.getWindow());
			window.getCoolBarManager().createControl(mainCoolbarArea);
			final CoolBar coolbar = window.getCoolBarManager().getControl();
			final ControlListener resizeListener = new ControlListener() {
				public void controlMoved(ControlEvent e) {
					regreshCoolbar();
				}

				public void controlResized(ControlEvent e) {
					regreshCoolbar();
				}

				void regreshCoolbar() {
					if (coolbar != null && !coolbar.isDisposed()) {
						CoolItem[] coolItems = coolbar.getItems();
						for (int i = 0; i < coolItems.length; i++) {
							ToolBar toolbar = (ToolBar) coolItems[i].getControl();
							toolbar.setData(RWT.CUSTOM_VARIANT, "mainToolBar");
							ToolItem[] toolItems = toolbar.getItems();
							for (int j = 0; j < toolItems.length; j++) {
								toolItems[j].setData(RWT.CUSTOM_VARIANT, "mainToolItem");
							}
							toolbar.layout(true, true);
						}
						coolbar.setLocked(true);
						
						mainCoolbarArea.layout(true, true);
					}
				}
			};
			coolbar.addControlListener(resizeListener);
			coolbar.addDisposeListener(new DisposeListener() {
				public void widgetDisposed(DisposeEvent event) {
					coolbar.removeDisposeListener(this);
					coolbar.removeControlListener(resizeListener);
				}
			});

			final IMenuService menuService = (IMenuService) ((IWorkbenchWindowConfigurer) workbenchWindowConfigurer)
					.getWindow().getService(IMenuService.class);

			menuService.populateContributionManager((ContributionManager) mainMenuManager, MenuUtil.MAIN_MENU);
			// MENU权限控制
			IContributionItem[] items = mainMenuManager.getItems();
			if (items.length != 0) {
				this.authzMenu(items);
			}
			boolean hasMenubar = false;
			if (mainMenuManager.getItems().length > 0) {
				hasMenubar = true;
			}
			Composite menuBarArea = null;
			if (hasMenubar) {
				mainMenuManager.fill(centerArea);
			}
		} else {
			fillRight(rightArea);
		}

		// GridData coolbarLayoutData = new GridData();
		// coolbarLayoutData.grabExcessHorizontalSpace = true;
		// coolbarLayoutData.horizontalAlignment = SWT.END;
		// coolbarLayoutData.verticalAlignment = SWT.TOP;
		// mainCoolbarArea.setLayoutData(coolbarLayoutData);
		// rightArea.pack(true);
	}

	/**
	 * 填充头部左边的区域，包括单位名称，应用服务
	 * 
	 * @param leftArea
	 */
	protected void fillLeft(Composite leftArea) {
		HttpSession session = RWT.getUISession().getHttpSession();
		Boolean authenticated = (Boolean) session.getAttribute(FrameworkConstants.AUTHENTICATED_PROPERTY);
		String customer_id = (String) session.getAttribute(FrameworkConstants.CUSTOMER_ID);
		leftArea.setLayout(new FormLayout());
		if (authenticated != null && authenticated.equals(true)) {
			String fileRepository = System.getProperty(FrameworkConstants.FILE_REPOSITORY);
			String logo_path = fileRepository + "/" + customer_id + "/logo/logo.jpg";
			File logoFile = new File(logo_path);
			if (logoFile.exists()) {
				Image logo = new Image(PlatformUI.getWorkbench().getDisplay(), logo_path);
				leftArea.setBackgroundImage(logo);
			} else {
				Composite leftImgComposite = new Composite(leftArea, SWT.NONE);
				leftImgComposite.setLayout(new FormLayout());
				FormData formData = new FormData();
				formData.top = new FormAttachment(0, 0);
				formData.bottom = new FormAttachment(100, 0);
				formData.left = new FormAttachment(0, 0);
				formData.right = new FormAttachment(0, 61);
				leftImgComposite.setLayoutData(formData);

				HHHLabel imgInfo = new HHHLabel(leftImgComposite, SWT.NONE);
				imgInfo.setImage(ImageUtil.loadImage("icons/3H_ICON2.png", OPSBaseHead.class.getClassLoader()));
				imgInfo.setAlignment(SWT.RIGHT);
				formData = new FormData();
				formData.top = new FormAttachment(0, 0);
				formData.bottom = new FormAttachment(100, 0);
				formData.left = new FormAttachment(0, 0);
				formData.right = new FormAttachment(100, 0);
				imgInfo.setLayoutData(formData);

				Composite labelComposite = new Composite(leftArea, SWT.NONE);
				labelComposite.setLayout(new FillLayout());
				formData = new FormData();
				formData.top = new FormAttachment(0, 0);
				formData.bottom = new FormAttachment(100, 0);
				formData.left = new FormAttachment(leftImgComposite, 0, SWT.RIGHT);
				formData.right = new FormAttachment(100, 0);
				labelComposite.setLayoutData(formData);

				GridLayout glayout = new GridLayout(1, false);
				glayout.horizontalSpacing = 0;
				glayout.marginHeight = 0;
				glayout.marginWidth = 0;
				labelComposite.setLayout(glayout);

				HHHLabel orgNameLabel = new HHHLabel(labelComposite, SWT.NONE);
				orgNameLabel.setLabelData(RWT.CUSTOM_VARIANT, "orgInfo");
				orgNameLabel.setAlignment(SWT.LEFT);
				orgNameLabel.setFont(new Font(Display.getCurrent(), "微软雅黑", 24, SWT.BOLD));
				orgNameLabel.setForeground(new Color(Display.getCurrent(), new RGB(255, 255, 255)));
				String label = (String) session.getAttribute(FrameworkConstants.SERVICE_LABEL);

				orgNameLabel.setText(label);
				GridData gdata = new GridData(SWT.LEFT, SWT.CENTER, true, true);
				orgNameLabel.setLayoutData(gdata);
			}
		} else {
			Composite leftImgComposite = new Composite(leftArea, SWT.NONE);
			leftImgComposite.setLayout(new FormLayout());
			FormData formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.left = new FormAttachment(0, 0);
			formData.right = new FormAttachment(0, 61);
			leftImgComposite.setLayoutData(formData);

			HHHLabel imgInfo = new HHHLabel(leftImgComposite, SWT.NONE);
			imgInfo.setImage(ImageUtil.loadImage("icons/3H_ICON2.png", OPSBaseHead.class.getClassLoader()));
			imgInfo.setAlignment(SWT.RIGHT);
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.left = new FormAttachment(0, 0);
			formData.right = new FormAttachment(100, 0);
			imgInfo.setLayoutData(formData);

			Composite labelComposite = new Composite(leftArea, SWT.NONE);
			labelComposite.setLayout(new FillLayout());
			formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.bottom = new FormAttachment(100, 0);
			formData.left = new FormAttachment(leftImgComposite, 0, SWT.RIGHT);
			formData.right = new FormAttachment(100, 0);
			labelComposite.setLayoutData(formData);

			GridLayout glayout = new GridLayout(1, false);
			glayout.horizontalSpacing = 0;
			glayout.marginHeight = 0;
			glayout.marginWidth = 0;
			labelComposite.setLayout(glayout);

			HHHLabel orgNameLabel = new HHHLabel(labelComposite, SWT.NONE);
			orgNameLabel.setLabelData(RWT.CUSTOM_VARIANT, "orgInfo");
			orgNameLabel.setAlignment(SWT.LEFT);
			orgNameLabel.setText("运维平台");
			orgNameLabel.setFont(new Font(Display.getCurrent(), "微软雅黑", 24, SWT.NORMAL));
			orgNameLabel.setForeground(new Color(Display.getCurrent(), new RGB(255, 255, 255)));
			GridData gdata = new GridData(SWT.LEFT, SWT.CENTER, true, true);
			orgNameLabel.setLayoutData(gdata);
			return;
		}
	}

	public void authzMenu(IContributionItem[] items) {
		String servletPath = RWT.getRequest().getServletPath();
		String servletName = null;
		if (servletPath.length() > 0) {
			servletName = servletPath.substring(1);
		}
		// 如果不需要进行认证（只有认证之后，才有可能授权)
		if (servletName != null && (!AuthExtensionsTracker.getInstance().getAuthenticationRequired(servletPath))) {
			return;
		}
		// 如果不需要授权
		if (servletName != null && (!AuthExtensionsTracker.getInstance().getAuthorizationRequired(servletPath))) {
			return;
		}

		AuthzInt authzInt = AuthzUtil.getAuthzIntService();
		for (int i = 0; i < items.length; i++) {
			if (!authzInt.doCheck(items[i].getId())) {
				mainMenuManager.remove(items[i]);
			} else if (items[i] instanceof IMenuManager) {
				authzSubMenu(items[i]);
			}
		}
	}

	public void authzSubMenu(IContributionItem items) {
		AuthzInt authzInt = AuthzUtil.getAuthzIntService();
		IContributionItem[] sub_items = ((IMenuManager) items).getItems();
		for (int i = 0; i < sub_items.length; i++) {
			if (!authzInt.doCheck(sub_items[i].getId())) {
				((IMenuManager) items).remove(sub_items[i]);
			} else if (sub_items[i] instanceof IMenuManager) {
				authzSubMenu(sub_items[i]);
			}
		}
	}

	private String getLoginfo() {
		String loginStatus = "未登录";
		HttpServletRequest request = RWT.getRequest();
		String servletPath = request.getServletPath();

		if (servletPath.equalsIgnoreCase("/" + FrameworkConstants.REGISTER)
				|| servletPath.equalsIgnoreCase("/" + FrameworkConstants.ZRZT_REGISTER)) {
			loginStatus = "亲爱的用户，欢迎您注册";
		}

		HttpSession session = RWT.getUISession().getHttpSession();
		if (session.getAttribute(FrameworkConstants.AUTHENTICATED_PROPERTY) != null
				&& session.getAttribute(FrameworkConstants.AUTHENTICATED_PROPERTY).equals(true)) {
			loginStatus = "";
		} else {
			return loginStatus;
		}

		String userName = (String) session.getAttribute(FrameworkConstants.USER_NAME);

		String userAccount = (String) session.getAttribute(FrameworkConstants.USER_ACCOUNT);

		String adminId = (String) session.getAttribute(FrameworkConstants.ADMIN_ID);

		String adminName = (String) session.getAttribute(FrameworkConstants.ADMIN_NAME);

		String headString = loginStatus + (userName == null || userName.equals("") ? adminName : userName) + "\n"
				+ "("+(adminId == null || adminId.equals("") ? userAccount : adminId)+")";

		return headString;
	}
}
