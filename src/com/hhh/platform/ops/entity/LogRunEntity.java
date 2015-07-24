package com.hhh.platform.ops.entity;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.CommonEntity;
import com.hhh.platform.advisors.framework.binding.EntityViewPart;
import com.hhh.platform.ops.logic.run.LogRunViewerBindingHandler;
import com.hhh.platform.ops.ui.run.LogRunDealDialog;
import com.hhh.platform.ops.ui.run.LogRunDialog;
import com.hhh.platform.ops.ui.run.LogRunViewerCreator;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.view.LogRunView;

public class LogRunEntity extends CommonEntity {

	private static Log log = LogFactory.getLog(LogRunEntity.class);

	private HashMap<String, BindingHandler> viewerHandlers = new HashMap<String, BindingHandler>();

	public LogRunEntity(String entityID) {
		super(entityID);
	}

	/** 增删改操作 */
	@Override
	public Object execute(ExecutionEvent event, EntityViewPart view, String operationID) {
		if (operationID.equals(OPSConstants.READ_LOG_RUN)) {
			if (!this.isOnlyOneSelected(view)) {
				return null;
			}
			LogRunDialog dialog = new LogRunDialog(operationID, this);
			dialog.setTitleText("运行性能日志");
			dialog.open();
		} else if (operationID.equals(OPSConstants.DEAL_LOG_RUN)) {
			if (!this.isNotEmpty(view)) {
				return null;
			}
			LogRunDealDialog dialog = new LogRunDealDialog(operationID, this);
			dialog.setTitleText("处理运行日志");
			dialog.open();
		}
		return null;
	}

	/** 初始化viewer */
	@Override
	public void open(Composite top, EntityViewPart view) {
		String viewID = view.getSite().getId();
		if (viewID.equals(LogRunView.ID)) {
			LogRunViewerCreator viewerCreator = new LogRunViewerCreator();
			LogRunViewerBindingHandler handler = new LogRunViewerBindingHandler();
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
			if (viewID.equals(LogRunView.ID)) {
				handler = (LogRunViewerBindingHandler) viewerHandlers.get(viewID);
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
