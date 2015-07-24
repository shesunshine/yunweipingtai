package com.hhh.platform.ops.logic.deploy;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.custom.CCombo;

import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreator;
import com.hhh.platform.advisors.table.CheckboxTableViewerWithDoubleClick;
import com.hhh.platform.ops.util.ContainPagingControlsCreatorBindingHandler;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.SystemSettingHelper;

public class ProductSelectDialogBindingHandler extends ContainPagingControlsCreatorBindingHandler {

	private static Log log = LogFactory.getLog(ProductSelectDialogBindingHandler.class);

	public ProductSelectDialogBindingHandler(EntityWithTableViewerCreator controlsCreator, String operationMessage,
			Map conditionMap) {
		super(controlsCreator, operationMessage, conditionMap);
	}

	@Override
	protected String getSelectMapperString() {
		return "com.hhh.platform.ops.sql.ops_pd_registerMapper.selectAllProduct";
	}

	@Override
	protected String getCountMapperString() {
		return "com.hhh.platform.ops.sql.ops_pd_registerMapper.countAllProduct";
	}

	@Override
	public void doSearch(HashMap<String, Object> paremeters) throws Exception {
		CCombo cmbo = (CCombo) this.allBindedControls.get("yw_type");
		if (cmbo.getSelectionIndex() == 0) {
			paremeters.put("yw_type", "");
		}

		CCombo cmbo2 = (CCombo) this.allBindedControls.get("dm_type");
		if (cmbo2.getSelectionIndex() == 0) {
			paremeters.put("dm_type", "");
		}
		super.doSearch(paremeters);
	}

	@Override
	protected void initCreatorControls() {
		try {
			initCombo();
		} catch (Exception e) {
			MessageUtil.showErrorDialog(e, log, "初始化下拉列表错误", MessageUtil.ERROR_TYPE_OPERATION);
		}
		((CheckboxTableViewerWithDoubleClick) this.controlsCreator.getViewer()).setSingleChecked(true);
		this.allBindedControls.get("check_all").setVisible(false);
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
}
