package com.hhh.platform.ops.logic.system;

import org.eclipse.core.commands.ExecutionEvent;

import com.hhh.platform.advisors.framework.binding.AbstractCommonHandler;
import com.hhh.platform.ops.ui.system.SystemSettingDialog;

public class SystemSettingHandler extends AbstractCommonHandler {

	@Override
	protected Object execute1(ExecutionEvent event) {
		SystemSettingDialog dialog = new SystemSettingDialog();
		dialog.open();
		return null;
	}
}
