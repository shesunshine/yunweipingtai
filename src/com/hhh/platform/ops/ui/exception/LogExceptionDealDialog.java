package com.hhh.platform.ops.ui.exception;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.ActionBindedScrolledDialog;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.ControlsCreator;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.ops.entity.LogExceptionEntity;
import com.hhh.platform.ops.logic.exception.LogExceptionDealDialogBindingHandler;
import com.hhh.platform.ui.binding.XMLModelControlsCreator;

public class LogExceptionDealDialog extends ActionBindedScrolledDialog {

	protected DefaultBindingHandler bindingHandler;

	protected ControlsCreator controlsCreator;

	private String operationId;

	public LogExceptionDealDialog(String operationId, LogExceptionEntity entity) {
		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(
				"com/hhh/platform/ops/ui/exception/DealDialogCreator.xml");
		this.controlsCreator = new XMLModelControlsCreator(xmlis);
		this.bindingHandler = new LogExceptionDealDialogBindingHandler(operationId, entity);
		this.operationId = operationId;
		this.setSize(new Point(600, 260));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, BindingHandler.CONFIRM_ACTION, "确定", false);
		createButton(parent, BindingHandler.CANCEL_ACTION, "返回", false);
	}

	@Override
	protected BindingHandler getBindingHandler() {
		return this.bindingHandler;
	}

	@Override
	protected ControlsCreator getControlsCreator() {
		return this.controlsCreator;
	}

	@Override
	protected int getShellStyle() {
		int newShellStyle = 0;
		newShellStyle = SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL | getDefaultOrientation();
		return newShellStyle;
	}
}
