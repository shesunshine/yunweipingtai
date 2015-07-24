package com.hhh.platform.ops.entity;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.CommonEntity;
import com.hhh.platform.advisors.framework.binding.EntityViewPart;
import com.hhh.platform.ops.logic.exception.LogExceptionViewerBindingHandler;
import com.hhh.platform.ops.ui.exception.LogExceptionDealDialog;
import com.hhh.platform.ops.ui.exception.LogExceptionDialog;
import com.hhh.platform.ops.ui.exception.LogExceptionViewerCreator;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.view.LogExceptionView;

public class LogExceptionEntity extends CommonEntity{

	private static Log log = LogFactory.getLog(LogExceptionEntity.class);

	private HashMap<String, BindingHandler> viewerHandlers = new HashMap<String, BindingHandler>();

	public LogExceptionEntity(String entityID) {
		super(entityID);
	}

	@Override
	public Object execute(ExecutionEvent event, EntityViewPart view, String operationID) {
		if (operationID.equals(OPSConstants.READ_LOG_EXCEPTION)) {
			if (!this.isOnlyOneSelected(view)) {
				return null;
			}
			LogExceptionDialog dialog = new LogExceptionDialog(operationID, this);
			dialog.setTitleText("查看异常日志");
			dialog.open();
		} else if (operationID.equals(OPSConstants.DEAL_LOG_EXCEPTION)) {
			if (!this.isNotEmpty(view)) {
				return null;
			}
			LogExceptionDealDialog dialog = new LogExceptionDealDialog(operationID, this);
			dialog.setTitleText("处理异常日志");
			dialog.open();
		} 
		return null;
	}

	/** 初始化viewer */
	@Override
	public void open(Composite top, EntityViewPart view) {
		String viewID = view.getSite().getId();
		if (viewID.equals(LogExceptionView.ID)) {
			LogExceptionViewerCreator viewerCreator = new LogExceptionViewerCreator();
			LogExceptionViewerBindingHandler handler = new LogExceptionViewerBindingHandler();
			if (handler.initContents(viewerCreator, top) && handler.initControls()) {
				if (viewerCreator.getBindedControls() != null && viewerCreator.getBindedControls().size() > 0) {
					handler.attachListeners();
					viewerHandlers.put(viewID, handler);
					viewer = viewerCreator.getViewer();
				}
			}
		}
	}

	/** 关闭模型 */
	@Override
	public void close(Composite top, EntityViewPart view) {
		if (!top.isDisposed()) {
			BindingHandler handler = null;
			String viewID = view.getSite().getId();
			// 用户视图
			if (viewID.equals(LogExceptionView.ID)) {
				handler = (LogExceptionViewerBindingHandler) viewerHandlers.get(viewID);
			}
			handler.detachListeners();
			handler.destroy();
			top.dispose();
		}
	}

	public HashMap<String, BindingHandler> getViewerHandlers() {
		return viewerHandlers;
	}


}
