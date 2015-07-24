package com.hhh.platform.ops.ui.system;

import java.io.InputStream;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

import com.hhh.platform.advisors.framework.binding.ActionBindedScrolledDialog;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.ControlsCreator;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.ops.logic.system.SystemSettingDialogBindingHandler;
import com.hhh.platform.ui.binding.XMLModelControlsCreator;

public class SystemSettingDialog extends ActionBindedScrolledDialog {

	protected DefaultBindingHandler bindingHandler;

	protected ControlsCreator controlsCreator;

	public SystemSettingDialog() {
		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(
				"com/hhh/platform/ops/ui/system/SystemSettingDialogCreator.xml");
		this.controlsCreator = new XMLModelControlsCreator(xmlis);
		this.bindingHandler = new SystemSettingDialogBindingHandler();
		this.setSize(new Point(800, 600));
		this.setTitleText("系统设置");
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

	@Override
	protected Point getInitialLocation(Point initialSize) {
		if (getShell().getSize().y > Display.getCurrent().getClientArea().height) {
			return super.getInitialLocation(initialSize);
		} else {
			return new Point(Display.getCurrent().getClientArea().width / 2 - getShell().getSize().x / 2, Display
					.getCurrent().getClientArea().height / 2 - getShell().getSize().y / 2);
		}

	}
}
