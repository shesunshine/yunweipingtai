package com.hhh.platform.ops.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.hhh.platform.advisors.framework.binding.CommonEntityHandler;
import com.hhh.platform.advisors.framework.binding.EntityViewPart;

public class OPSEntityHandler extends CommonEntityHandler {
	private static Log log = LogFactory.getLog(OPSEntityHandler.class);

	public Object execute1(ExecutionEvent event) {
		writeLog(event);
		return super.execute1(event);
	}

	private void writeLog(ExecutionEvent event) {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if (part instanceof EntityViewPart && (event.getTrigger() instanceof Event)) {
			EntityViewPart view = (EntityViewPart) part;

			String viewName = view.getPartName();

			Event trigger = (Event) event.getTrigger();
			ToolItem item = (ToolItem) trigger.widget;
			String toolItemName = item.getText();

			String content = "在“" + viewName + "”视图中，进行“" + toolItemName + "”操作";

//			LogHelper.writeLog(content);
		}
	}
}
