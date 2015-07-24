package com.hhh.platform.ops.entity;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.widgets.Composite;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.CommonEntity;
import com.hhh.platform.advisors.framework.binding.EntityViewPart;
import com.hhh.platform.advisors.table.CheckboxTableViewerWithDoubleClick;
import com.hhh.platform.advisors.table.TableModel;
import com.hhh.platform.advisors.table.TableRowModel;
import com.hhh.platform.ops.logic.deploy.ProductDeployViewerBindingHandler;
import com.hhh.platform.ops.ui.deploy.ProductDeployDialog;
import com.hhh.platform.ops.ui.deploy.ProductDeployViewerCreator;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.util.OPSUtil;
import com.hhh.platform.ops.view.ProductDeployView;

public class ProductDeployEntity extends CommonEntity {

	private static Log log = LogFactory.getLog(ProductDeployEntity.class);

	private HashMap<String, BindingHandler> viewerHandlers = new HashMap<String, BindingHandler>();

	public ProductDeployEntity(String entityID) {
		super(entityID);
	}

	/** 增删改操作 */
	@Override
	public Object execute(ExecutionEvent event, EntityViewPart view, String operationID) {
		if (operationID.equals(OPSConstants.ADD_DEPLOY)) {
			ProductDeployDialog dialog = new ProductDeployDialog(operationID, this);
			dialog.setTitleText("新增部署信息");
			dialog.open();
		} else if (operationID.equals(OPSConstants.UPDATE_DEPLOY)) {
			if (!this.isOnlyOneSelected(view)) {
				return null;
			}
			
			TableRowModel model = (TableRowModel) this.getSelectedElements()[0];
			String addUser = (String) model.getData().get("a.dp_user");
			HttpSession session = RWT.getUISession().getHttpSession();
			boolean is_admin = (Boolean) session.getAttribute(FrameworkConstants.IS_ADMIN);
			if (!is_admin) {
				String account = OPSUtil.getUserNameAndAccount();
				if (account != null && !account.equals(addUser)) {
					MessageUtil.showMessageDialog("抱歉，您不是该信息的部署人，不能修改或删除。", MessageUtil.MESSAGE_DIALOG_TYPE_INFO);
					return null;
				}
			}
			
			ProductDeployDialog dialog = new ProductDeployDialog(operationID, this);
			dialog.setTitleText("修改部署信息");
			dialog.open();
		} else if (operationID.equals(OPSConstants.READ_DEPLOY)) {
			if (!this.isOnlyOneSelected(view)) {
				return null;
			}
			ProductDeployDialog dialog = new ProductDeployDialog(operationID, this);
			dialog.setTitleText("查看部署信息");
			dialog.open();
		} else if (operationID.equals(OPSConstants.DELETE_DEPLOY)) {
			if (!this.isOnlyOneSelected(view)) {
				return null;
			}
			
			TableRowModel model = (TableRowModel) this.getSelectedElements()[0];
			String addUser = (String) model.getData().get("a.dp_user");
			HttpSession session = RWT.getUISession().getHttpSession();
			boolean is_admin = (Boolean) session.getAttribute(FrameworkConstants.IS_ADMIN);
			if (!is_admin) {
				String account = OPSUtil.getUserNameAndAccount();
				if (account != null && !account.equals(addUser)) {
					MessageUtil.showMessageDialog("抱歉，您不是该信息的部署人，您不能修改或删除。", MessageUtil.MESSAGE_DIALOG_TYPE_INFO);
					return null;
				}
			}
			
			boolean result = MessageDialog.openConfirm(view.getSite().getShell(), "系统提示", "删除部署信息会影响监控的数据获取，确定删除部署信息？");
			if (!result) {
				return null;
			}
			deleteProductDeploy(view);
		}
		return null;
	}

	private void deleteProductDeploy(EntityViewPart view) {
		try {
			String[] ids = this.getSelectedIDs();
			for (int i = 0; i < ids.length; i++) {
				Map parameters = new HashMap();
				parameters.put("dp_id", ids[i]);
				String[] sqls = new String[] { "com.hhh.platform.ops.sql.ops_pd_deployMapper.deleteDeploy",
						"com.hhh.platform.ops.sql.ops_log_exceptionMapper.deleteLog",
						"com.hhh.platform.ops.sql.ops_log_performanceMapper.deleteLog",
						"com.hhh.platform.ops.sql.ops_log_runMapper.deleteLog" };
				DaoUtil.delete(sqls, new Map[] { parameters, parameters, parameters, parameters });
				StructuredViewer viewer = this.getViewer();
				TableViewer viewer1 = (CheckboxTableViewerWithDoubleClick) viewer;
				TableModel tableModel = (TableModel) viewer1.getInput();

				TableRowModel updateRow = (TableRowModel) this.getSelectedElements()[i];
				tableModel.removeTableRows(new Object[] { updateRow });
				viewer1.refresh(tableModel, true);
			}
		} catch (Exception e) {
			MessageUtil.showErrorDialog(e, log, "删除的过程中发生异常", MessageUtil.ERROR_TYPE_OPERATION);
		}
	}

	/** 初始化viewer */
	@Override
	public void open(Composite top, EntityViewPart view) {
		String viewID = view.getSite().getId();
		if (viewID.equals(ProductDeployView.ID)) {
			ProductDeployViewerCreator viewerCreator = new ProductDeployViewerCreator();
			ProductDeployViewerBindingHandler handler = new ProductDeployViewerBindingHandler();
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
			if (viewID.equals(ProductDeployView.ID)) {
				handler = (ProductDeployViewerBindingHandler) viewerHandlers.get(viewID);
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
