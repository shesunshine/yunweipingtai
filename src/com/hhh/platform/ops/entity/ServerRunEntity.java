package com.hhh.platform.ops.entity;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.CommonEntity;
import com.hhh.platform.advisors.framework.binding.EntityViewPart;
import com.hhh.platform.ops.logic.server.ServerRunViewerBindingHandler;
import com.hhh.platform.ops.ui.server.ServerRunDialog;
import com.hhh.platform.ops.ui.server.ServerRunViewerCreator;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.view.ServerRunView;

public class ServerRunEntity extends CommonEntity {

	private static Log log = LogFactory.getLog(ServerRunEntity.class);

	private HashMap<String, BindingHandler> viewerHandlers = new HashMap<String, BindingHandler>();

	public ServerRunEntity(String entityID) {
		super(entityID);
	}

	/** 增删改操作 */
	@Override
	public Object execute(ExecutionEvent event, EntityViewPart view, String operationID) {
		if (operationID.equals(OPSConstants.READ_SERVER_RUN)) {
			if (!this.isOnlyOneSelected(view)) {
				return null;
			}
			ServerRunDialog dialog = new ServerRunDialog(operationID, this);
			dialog.setTitleText("查看服务器监控");
			dialog.open();
		} 
		return null;
	}

	/** 初始化viewer */
	@Override
	public void open(Composite top, EntityViewPart view) {
		String viewID = view.getSite().getId();
		if (viewID.equals(ServerRunView.ID)) {
			ServerRunViewerCreator viewerCreator = new ServerRunViewerCreator();
			ServerRunViewerBindingHandler handler = new ServerRunViewerBindingHandler();
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
			if (viewID.equals(ServerRunView.ID)) {
				handler = (ServerRunViewerBindingHandler) viewerHandlers.get(viewID);
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
