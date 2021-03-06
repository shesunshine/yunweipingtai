package com.hhh.platform.ops;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import com.hhh.platform.advisors.framework.CommonWorkbenchAdvisor;
import com.hhh.platform.ops.perspective.HomePerspective;
import com.hhh.platform.ops.perspective.ProductRegisterPerspective;
import com.hhh.platform.ops.workbentch.OPSBaseHead;
import com.hhh.platform.ops.workbentch.OPSMenuManager;

public class OPSDataWorkbenchAdvisor extends CommonWorkbenchAdvisor {

	@Override
	public String getInitialWindowPerspectiveId() {
		return ProductRegisterPerspective.ID;
	}

	@Override
	public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
			IWorkbenchWindowConfigurer configurer) {
		return new OPSDataWorkbenchWindowAdvisor(configurer, null, null);
	}

}
