package com.hhh.platform.ops.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.internal.WorkbenchPage;
import org.eclipse.ui.menus.CommandContributionItem;

import com.hhh.platform.advisors.perspective.PerspectiveSwitcherHandler;

public class OPSPerspectiveSwitcherHandler extends PerspectiveSwitcherHandler {
	private static Log log = LogFactory.getLog(OPSPerspectiveSwitcherHandler.class);

	@Override
	protected Object execute1(ExecutionEvent event) {
		super.execute1(event);
		writeLog(event);
		return null;
	}

	private void writeLog(ExecutionEvent event) {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = workbench.getActiveWorkbenchWindow();
		WorkbenchPage page = (WorkbenchPage) workbench.getActiveWorkbenchWindow().getActivePage();

		IPerspectiveDescriptor perspectiveDescriptor = page.getPerspective();
		String perspectiveName = perspectiveDescriptor.getLabel();

		Event trigger = (Event) event.getTrigger();
		CommandContributionItem data = (CommandContributionItem) trigger.widget.getData();
		String commandName = data.getData().label;

		String content = "单击“" + commandName + "”菜单，进入" + perspectiveName;

//		LogHelper.writeLog(content);
	}
}
