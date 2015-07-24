package com.hhh.platform.ops.logic.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.EntityViewerBindingHandler;
import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreator;
import com.hhh.platform.advisors.page.PagePartionUtil;
import com.hhh.platform.ops.ui.server.ServerProductDialogCreator;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.SystemSettingHelper;

public class ServerProductDialogBindingHandler extends EntityViewerBindingHandler {
	private static Log log = LogFactory
			.getLog(ServerProductDialogBindingHandler.class);

	protected String operationMessage;

	protected EntityWithTableViewerCreator controlsCreator;

	protected Map conditionMap;

	protected PagePartionUtil pagePartionUtil;
	
	public ServerProductDialogBindingHandler(Map conditionMap) {
		this.conditionMap = conditionMap;
	}
	
	@Override
	public boolean initControls() {
		this.controlsCreator = (ServerProductDialogCreator) controlsCreators.get(ServerProductDialogCreator.class.getName());
		try {
			initCombo();
			pagePartionUtil = new PagePartionUtil(
					this.controlsCreator.getViewer(),
					this.controlsCreator.getPagePartitionArea(),
					FrameworkConstants.PAGE_SIZE, getCountMapperString(),
					getSelectMapperString(),
					(Button) this.allBindedControls.get("check_all"),
					controlsCreator.getBindedFieldsNames());
			this.doSearch(new HashMap<String, Object>());
			return true;
		} catch (Exception e) {
			String message = "初始化" + operationMessage + "过程中发生异常";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return false;
		}
	}
	
	protected String getSelectMapperString() {
		return "com.hhh.platform.ops.sql.ops_server_productMapper.selectAllProduct";
	}

	protected String getCountMapperString() {
		return "com.hhh.platform.ops.sql.ops_server_productMapper.countAllProduct";
	}

	@Override
	public void doSearch(HashMap<String, Object> paremeters) throws Exception {
		paremeters.putAll(conditionMap);
		CCombo cmbo = (CCombo) this.allBindedControls.get("yw_type");
		if (cmbo.getSelectionIndex() == 0) {
			paremeters.put("yw_type", "");
		}

		CCombo cmbo2 = (CCombo) this.allBindedControls.get("dm_type");
		if (cmbo2.getSelectionIndex() == 0) {
			paremeters.put("dm_type", "");
		}
		
		pagePartionUtil.doSearch(paremeters);
	}
	
	private void initCombo() throws Exception {
		CCombo cmbo_yw_type = (CCombo) this.allBindedControls.get("yw_type");
		if (cmbo_yw_type != null) {
			String[] yw_type = SystemSettingHelper.getYwType();
			String[] temp = new String[yw_type.length + 1];
			temp[0] = "";
			for (int i = 0; i < yw_type.length; i++) {
				temp[i + 1] = yw_type[i];
			}
			cmbo_yw_type.setItems(temp);
			cmbo_yw_type.select(0);
			cmbo_yw_type.setEditable(false);
		}

		CCombo cmbo_dm_type = (CCombo) this.allBindedControls.get("dm_type");
		if (cmbo_dm_type != null) {
			String[] dm_type = SystemSettingHelper.getDmType();
			String[] temp = new String[dm_type.length + 1];
			temp[0] = "";
			for (int i = 0; i < dm_type.length; i++) {
				temp[i + 1] = dm_type[i];
			}
			cmbo_dm_type.setItems(temp);
			cmbo_dm_type.select(0);
			cmbo_dm_type.setEditable(false);
		}
	}

	@Override
	public boolean perform(String action) {
		if (action.equals(BindingHandler.RETURN_ACTION)) {
			return true;
		}
		return super.perform(action);
	}
}
