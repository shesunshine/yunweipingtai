package com.hhh.platform.ops;

import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.WorkbenchAdvisor;


public class OPSEntryPoint implements EntryPoint {

	public OPSEntryPoint() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int createUI() {
		Display display = PlatformUI.createDisplay();
		WorkbenchAdvisor advisor = new OPSWorkbenchAdvisor();
		return PlatformUI.createAndRunWorkbench( display, advisor );
	}

}
