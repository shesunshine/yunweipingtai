package com.hhh.platform.ops.util;

import org.apache.commons.logging.Log;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;

import com.hhh.platform.advisors.framework.FrameWorkAdvisor;

public class MessageUtil {
	public static final String ERROR_TYPE_OPERATION = "数据库交互发生异常";

	public static final String MESSAGE_DIALOG_TYPE_INFO = "info";

	public static final String MESSAGE_DIALOG_TYPE_ERROR = "error";

	public static final String MESSAGE_DIALOG_TYPE_CONFIRM = "confirm";

	public static void showErrorDialog(Exception e, Log log, String message, String erroType) {
		int code = (int) System.currentTimeMillis();
		log.error(message + "(" + code + ")", e);
		IStatus status = new Status(IStatus.ERROR, FrameWorkAdvisor.PLUGIN_ID, code, message, e);
		ErrorDialog.openError(Display.getCurrent().getActiveShell(), erroType, null, status);
	}

	public static boolean showMessageDialog(String message, String messageDialogType) {
		if (messageDialogType.equals(MESSAGE_DIALOG_TYPE_INFO)) {
			MessageDialog.openInformation(Display.getCurrent().getActiveShell(), "系统提示", message);
			return true;
		} else if (messageDialogType.equals(MESSAGE_DIALOG_TYPE_ERROR)) {
			MessageDialog.openError(Display.getCurrent().getActiveShell(), "系统提示", message);
			return true;
		} else if (messageDialogType.equals(MESSAGE_DIALOG_TYPE_CONFIRM)) {
			return MessageDialog.openConfirm(Display.getCurrent().getActiveShell(), "系统提示", message);
		} else {
			return true;
		}

	}
}
