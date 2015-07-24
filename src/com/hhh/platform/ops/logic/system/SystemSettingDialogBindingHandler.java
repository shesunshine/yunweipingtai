package com.hhh.platform.ops.logic.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Control;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.cache.CacheService;
import com.hhh.platform.cache.CacheServiceUtil;
import com.hhh.platform.ops.ui.system.CommonSettingControlsCreator;
import com.hhh.platform.ops.ui.system.ServerSettingControlsCreator;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSAppConstants;
import com.hhh.platform.ops.util.SystemSettingHelper;
import com.hhh.platform.orm.mybatis.SQLSessionService;
import com.hhh.platform.orm.mybatis.SQLSessionServiceUtil;

public class SystemSettingDialogBindingHandler extends DefaultBindingHandler {
	private static Log log = LogFactory.getLog(SystemSettingDialogBindingHandler.class);

	private HashMap<String, Object> savedValues = new HashMap<String, Object>();

	public SystemSettingDialogBindingHandler() {
	}

	@Override
	public boolean initControls() {
		changeXMFolderBottom();

		CommonSettingControlsCreator common_c = new CommonSettingControlsCreator(this.allBindedControls);
		common_c.initCommonTabItem();

		ServerSettingControlsCreator server_c = new ServerSettingControlsCreator(this.allBindedControls);
		server_c.initCommonTabItem();

		this.defaultListener.bindControls(this.allBindedControls);
		try {
			List resultList = SystemSettingHelper.selectSystemConfigByCustomerID(OPSAppConstants.CUSTOMER_ID);
			HashMap<String, Object> valuesInDB = SystemSettingHelper.changeToMap(resultList);
			if (valuesInDB != null && valuesInDB.size() > 0) {
				BindingHelper.setBindedValues(allBindedControls, valuesInDB);
				savedValues = new HashMap<String, Object>();
				savedValues.putAll(BindingHelper.getLatestBindedValues(valuesInDB.keySet(), this.allBindedControls));

				// afterSetBindededValues(savedValues);
				common_c.afterSetBindededValues(savedValues);
			} else {
				savedValues = new HashMap<String, Object>();
			}
		} catch (Exception e) {
			MessageUtil.showErrorDialog(e, log, "获取系统设置错误", MessageUtil.ERROR_TYPE_OPERATION);
		}
		return true;
	}

	private void changeXMFolderBottom() {
		CTabFolder messageFolder = (CTabFolder) allBindedControls.get("approveFolder");
		if (null != messageFolder) {
			FormData formData = (FormData) messageFolder.getLayoutData();
			formData.bottom = new FormAttachment(100, 0);
		}
	}

	@Override
	public boolean perform(String action) {
		if (action.equals(CONFIRM_ACTION)) {
			if (validate(false)) {

				HashMap<String, Object> newValues = BindingHelper.getLatestBindedValues(allBindedControls);
				HashMap<String, Object> updateValues = BindingHelper.filterUnchanged(savedValues, newValues);
				filterSpecialValues(updateValues);

				if (updateValues.size() == 0) {
					return true;
				}
				try {
					updateCurrent(updateValues);
				} catch (Exception e) {
					MessageUtil.showErrorDialog(e, log, "修改系统设置异常", MessageUtil.ERROR_TYPE_OPERATION);
					return false;
				}
				savedValues.clear();
				savedValues.putAll(newValues);
				BindingHelper.setBindedValues(this.allBindedControls, updateValues);
			}
		}
		return true;
	}

	protected void updateCurrent(HashMap<String, Object> updateValues) throws Exception {
		HashMap<String, Object> parametersMap = new HashMap<String, Object>();
		updateSystemSetting(parametersMap, updateValues);
	}

	public int updateSystemSetting(HashMap<String, Object> parameters, HashMap<String, Object> bindedValues) throws Exception {
		int effect = 0;
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);

			parameters.put(FrameworkConstants.CUSTOMER_ID, RWT.getUISession().getHttpSession().getAttribute(FrameworkConstants.CUSTOMER_ID));

			CacheService cache = CacheServiceUtil.getCacheService();
			Set<String> fieldNames = bindedValues.keySet();
			Iterator<String> itor = fieldNames.iterator();
			while (itor.hasNext()) {
				String fieldName = itor.next();
				if (fieldName.equals("m.spinner_p")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_PERFORMANCE_LEVEL_KEY);
					parameters.put("value", bindedValues.get("m.spinner_p"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.spinner_r")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_RUN_LEVEL_KEY);
					parameters.put("value", bindedValues.get("m.spinner_r"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.ops_dm_type")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_DM_TYPE_KEY);
					parameters.put("value", bindedValues.get("m.ops_dm_type"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.ops_yw_type")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_YW_TYPE_KEY);
					parameters.put("value", bindedValues.get("m.ops_yw_type"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.spinner_hdd")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_SERVER_HDD_LEVEL_KEY);
					parameters.put("value", bindedValues.get("m.spinner_hdd"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.spinner_cpu")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_SERVER_CPU_LEVEL_KEY);
					parameters.put("value", bindedValues.get("m.spinner_cpu"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.spinner_ram")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_SERVER_RAM_LEVEL_KEY);
					parameters.put("value", bindedValues.get("m.spinner_ram"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.ignore_cpu")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_SERVER_CPU_IGNORE_KEY);
					parameters.put("value", bindedValues.get("m.ignore_cpu"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.ignore_ram")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_SERVER_RAM_IGNORE_KEY);
					parameters.put("value", bindedValues.get("m.ignore_ram"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				} else if (fieldName.equals("m.ignore_hdd")) {
					parameters.put("parameter_id", SystemSettingHelper.OPS_SERVER_HDD_IGNORE_KEY);
					parameters.put("value", bindedValues.get("m.ignore_hdd"));
					effect = session.update("com.hhh.platform.ops.sql.opsMapper.updateSystemSettingById", parameters);
				}
			}
			session.commit();

			Iterator<String> itor2 = fieldNames.iterator();
			while (itor2.hasNext()) {
				String fieldName = itor2.next();
				if (fieldName.equals("m.spinner_p")) {
					cache.put(SystemSettingHelper.OPS_PERFORMANCE_LEVEL_KEY, bindedValues.get("m.spinner_p"));
				} else if (fieldName.equals("m.spinner_r")) {
					cache.put(SystemSettingHelper.OPS_RUN_LEVEL_KEY, bindedValues.get("m.spinner_r"));
				} else if (fieldName.equals("m.ops_dm_type")) {
					cache.put(SystemSettingHelper.OPS_DM_TYPE_KEY, bindedValues.get("m.ops_dm_type"));
				} else if (fieldName.equals("m.ops_yw_type")) {
					cache.put(SystemSettingHelper.OPS_YW_TYPE_KEY, bindedValues.get("m.ops_yw_type"));
				} else if (fieldName.equals("m.spinner_hdd")) {
					cache.put(SystemSettingHelper.OPS_SERVER_HDD_LEVEL_KEY, bindedValues.get("m.spinner_hdd"));
				} else if (fieldName.equals("m.spinner_cpu")) {
					cache.put(SystemSettingHelper.OPS_SERVER_CPU_LEVEL_KEY, bindedValues.get("m.spinner_cpu"));
				} else if (fieldName.equals("m.spinner_ram")) {
					cache.put(SystemSettingHelper.OPS_SERVER_RAM_LEVEL_KEY, bindedValues.get("m.spinner_ram"));
				} else if (fieldName.equals("m.ignore_hdd")) {
					cache.put(SystemSettingHelper.OPS_SERVER_HDD_IGNORE_KEY, bindedValues.get("m.ignore_hdd"));
				} else if (fieldName.equals("m.ignore_cpu")) {
					cache.put(SystemSettingHelper.OPS_SERVER_CPU_IGNORE_KEY, bindedValues.get("m.ignore_cpu"));
				} else if (fieldName.equals("m.ignore_ram")) {
					cache.put(SystemSettingHelper.OPS_SERVER_RAM_IGNORE_KEY, bindedValues.get("m.ignore_ram"));
				}
			}

		} catch (Exception e) {
			session.rollback();
			log.error("修改系统配置信息错误：", e);
			throw new Exception(e.getMessage() + ";修改系统项配置信息错误", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
		return effect;
	}

	@Override
	public void focusLost(String fieldName, Control ctl) {
	}

	@Override
	public void widgetSelected(String fieldName, Control ctl) {
	}

	@Override
	public boolean veryfyField(String fieldName, Control ctl, boolean force) {
		if (!force) {
			return true;
		}
		return true;
	}

	public static void main(String[] args) {
		ArrayList<String> a = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			a.add("1234");
		}

		System.out.println(a.toString().replace("[", "").replace("]", ""));
	}
}
