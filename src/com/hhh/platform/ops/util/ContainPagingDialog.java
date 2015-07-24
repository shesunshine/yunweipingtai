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

package com.hhh.platform.ops.util;

import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.ActionBindedScrolledDialog;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.ControlsCreator;
import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreator;

@SuppressWarnings("serial")
public class ContainPagingDialog extends ActionBindedScrolledDialog{

	protected ContainPagingControlsCreatorBindingHandler bindingHandler;

	protected EntityWithTableViewerCreator controlsCreator;
	
	public ContainPagingDialog(ContainPagingControlsCreatorBindingHandler bindingHandler) {
		this.controlsCreator = bindingHandler.getControlsCreator();
		this.bindingHandler = bindingHandler;
		this.setTitleText(bindingHandler.getOperationMessage());
		
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, BindingHandler.CONFIRM_ACTION, "确定", false);
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

