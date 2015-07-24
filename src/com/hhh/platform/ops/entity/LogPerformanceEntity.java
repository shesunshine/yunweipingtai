package com.hhh.platform.ops.entity;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.CommonEntity;
import com.hhh.platform.advisors.framework.binding.EntityViewPart;
import com.hhh.platform.ops.logic.performance.LogPerformanceViewerBindingHandler;
import com.hhh.platform.ops.ui.performance.LogPerformanceDealDialog;
import com.hhh.platform.ops.ui.performance.LogPerformanceDialog;
import com.hhh.platform.ops.ui.performance.LogPerformanceViewerCreator;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.view.LogPerformanceView;

public class LogPerformanceEntity extends CommonEntity{

	private static Log log = LogFactory.getLog(LogPerformanceEntity.class);

	private HashMap<String, BindingHandler> viewerHandlers = new HashMap<String, BindingHandler>();

	public LogPerformanceEntity(String entityID) {
		super(entityID);
	}

	@Override
	public Object execute(ExecutionEvent event, EntityViewPart view, String operationID) {
		if (operationID.equals(OPSConstants.READ_LOG_PERFORMANCE)) {
			if (!this.isOnlyOneSelected(view)) {
				return null;
			}
			LogPerformanceDialog dialog = new LogPerformanceDialog(operationID, this);
			dialog.setTitleText("处理性能日志");
			dialog.open();
		} else if (operationID.equals(OPSConstants.DEAL_LOG_PERFORMANCE)) {
			if (!this.isNotEmpty(view)) {
				return null;
			}
			LogPerformanceDealDialog dialog = new LogPerformanceDealDialog(operationID, this);
			dialog.setTitleText("处理性能日志");
			dialog.open();
		}
		return null;
	}

	/** 初始化viewer */
	@Override
	public void open(Composite top, EntityViewPart view) {
		String viewID = view.getSite().getId();
		if (viewID.equals(LogPerformanceView.ID)) {
			LogPerformanceViewerCreator viewerCreator = new LogPerformanceViewerCreator();
			LogPerformanceViewerBindingHandler handler = new LogPerformanceViewerBindingHandler();
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
			if (viewID.equals(LogPerformanceView.ID)) {
				handler = (LogPerformanceViewerBindingHandler) viewerHandlers.get(viewID);
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
