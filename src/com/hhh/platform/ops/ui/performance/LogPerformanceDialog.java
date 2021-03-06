package com.hhh.platform.ops.ui.performance;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.ActionBindedScrolledDialog;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.ControlsCreator;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.ops.entity.LogPerformanceEntity;
import com.hhh.platform.ops.logic.performance.LogPerformanceDialogBindingHandler;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ui.binding.XMLModelControlsCreator;

public class LogPerformanceDialog extends ActionBindedScrolledDialog {

	protected DefaultBindingHandler bindingHandler;

	protected ControlsCreator controlsCreator;

	private String operationId;

	public LogPerformanceDialog(String operationId, LogPerformanceEntity entity) {
		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(
				"com/hhh/platform/ops/ui/performance/LogPerformanceDialogCreator.xml");
		this.controlsCreator = new XMLModelControlsCreator(xmlis);
		this.bindingHandler = new LogPerformanceDialogBindingHandler(operationId, entity);
		this.operationId = operationId;
		this.setSize(new Point(600, 530));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		if (!this.operationId.equals(OPSConstants.READ_PRODUCT_REGISTER)) {
			createButton(parent, BindingHandler.CONFIRM_ACTION, "确定", false);
		}
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
