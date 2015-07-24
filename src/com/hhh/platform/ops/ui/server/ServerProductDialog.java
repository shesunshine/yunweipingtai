/*
 * ContainPagingDialog.java
 * com.hhh.platform.jc.core.ti.ui
 *
 * @author			3hhf
 * @date			2011-9-2 下午05:32:00 
 * @version			V1.0
 *
 * 版权所有 © 2011 广州粤建三和软件有限公司
 */

package com.hhh.platform.ops.ui.server;

import java.util.Map;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.ActionBindedScrolledDialog;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.ControlsCreator;
import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreator;
import com.hhh.platform.ops.logic.server.ServerProductDialogBindingHandler;

@SuppressWarnings("serial")
public class ServerProductDialog extends ActionBindedScrolledDialog{

	protected BindingHandler bindingHandler;

	protected EntityWithTableViewerCreator controlsCreator;
	
	public ServerProductDialog(Map conditionMap) {
		this.bindingHandler = new ServerProductDialogBindingHandler(conditionMap);
		this.controlsCreator = new ServerProductDialogCreator();
		this.setTitleText("产品列表");
		this.setSize(new Point(1000, 680));
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, BindingHandler.RETURN_ACTION, "返回", false);
	}
	@Override
	protected BindingHandler getBindingHandler() {
		return this.bindingHandler;
	}

	@Override
	protected ControlsCreator getControlsCreator() {
		return this.controlsCreator;
	}
	
}

