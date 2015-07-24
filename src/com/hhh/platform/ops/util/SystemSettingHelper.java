package com.hhh.platform.ops.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hhh.platform.advisors.framework.FrameworkConstants;

public class SystemSettingHelper {
	private static Log log = LogFactory.getLog(SystemSettingHelper.class);

	public static final String OPS_YW_TYPE_KEY = "ops_yw_type";
	public static final String OPS_DM_TYPE_KEY = "ops_dm_type";
	public static final String OPS_PERFORMANCE_LEVEL_KEY = "ops_performance_level";
	public static final String OPS_RUN_LEVEL_KEY = "ops_run_level";

	public static final String OPS_SERVER_HDD_LEVEL_KEY = "ops_server_hdd_level";
	public static final String OPS_SERVER_CPU_LEVEL_KEY = "ops_server_cpu_level";
	public static final String OPS_SERVER_RAM_LEVEL_KEY = "ops_server_ram_level";
	public static final String OPS_SERVER_HDD_IGNORE_KEY = "ops_server_hdd_ignore";
	public static final String OPS_SERVER_CPU_IGNORE_KEY = "ops_server_cpu_ignore";
	public static final String OPS_SERVER_RAM_IGNORE_KEY = "ops_server_ram_ignore";
	
	public static final String OPS_SERVER_HDD_LEVEL_VALUE = "60";
	public static final String OPS_SERVER_CPU_LEVEL_VALUE = "60";
	public static final String OPS_SERVER_RAM_LEVEL_VALUE = "60";
	public static final String OPS_SERVER_HDD_IGNORE_VALUE = "30";
	public static final String OPS_SERVER_CPU_IGNORE_VALUE = "30";
	public static final String OPS_SERVER_RAM_IGNORE_VALUE = "30";

	public static final String OPS_YW_TYPE_VALUE = "检测";
	public static final String OPS_DM_TYPE_VALUE = "JAVA";
	public static final String OPS_PERFORMANCE_LEVEL_VALUE = "3";
	public static final String OPS_RUN_LEVEL_VALUE = "1";
	
	

	// 服务器用途
	public static final String OPS_SERVER_USE_TYPE_DB = "DB";
	public static final String OPS_SERVER_USE_TYPE_WEB = "WEB";
	public static final String OPS_SERVER_USE_TYPE_WEB_DB = "WEB+DB";
	// 服务器产品类型
	public static final String OPS_SERVER_P_TYPE_1 = "台式(塔式)";
	public static final String OPS_SERVER_P_TYPE_2 = "机架式";
	public static final String OPS_SERVER_P_TYPE_3 = "机柜式";
	public static final String OPS_SERVER_P_TYPE_4 = "刀片式";

	// 服务器产品类型
	public static final String OPS_SERVER_OS_TYPE_WINDOWS = "Windows";
	public static final String OPS_SERVER_OS_TYPE_LINUX = "Linux";
	public static final String OPS_SERVER_OS_TYPE_UNIX = "Unix";

	/**
	 * 初始化系统运行参数设置表
	 * 
	 * @param customer_id
	 * @param supportedStandards
	 */
	@SuppressWarnings("rawtypes")
	public static void initializeSystemSetting() throws Exception {
		try {
			long start_time = System.currentTimeMillis();
			List existedList = selectSystemConfigByCustomerID(OPSAppConstants.CUSTOMER_ID);

			if (!isExistedSystemSetting(existedList, OPS_PERFORMANCE_LEVEL_KEY)) {
				addSystemSetting(OPS_PERFORMANCE_LEVEL_KEY, OPS_PERFORMANCE_LEVEL_VALUE, "性能阀值", OPSAppConstants.CUSTOMER_ID);
			}

			if (!isExistedSystemSetting(existedList, OPS_RUN_LEVEL_KEY)) {
				addSystemSetting(OPS_RUN_LEVEL_KEY, OPS_RUN_LEVEL_VALUE, "运行阀值", OPSAppConstants.CUSTOMER_ID);
			}

			if (!isExistedSystemSetting(existedList, OPS_YW_TYPE_KEY)) {
				addSystemSetting(OPS_YW_TYPE_KEY, OPS_YW_TYPE_VALUE, "业务类型", OPSAppConstants.CUSTOMER_ID);
			}

			if (!isExistedSystemSetting(existedList, OPS_DM_TYPE_KEY)) {
				addSystemSetting(OPS_DM_TYPE_KEY, OPS_DM_TYPE_VALUE, "代码类型", OPSAppConstants.CUSTOMER_ID);
			}
			
			if (!isExistedSystemSetting(existedList, OPS_SERVER_HDD_LEVEL_KEY)) {
				addSystemSetting(OPS_SERVER_HDD_LEVEL_KEY, OPS_SERVER_HDD_LEVEL_VALUE, "服务器硬盘使用率阀值", OPSAppConstants.CUSTOMER_ID);
			}
			
			if (!isExistedSystemSetting(existedList, OPS_SERVER_CPU_LEVEL_KEY)) {
				addSystemSetting(OPS_SERVER_CPU_LEVEL_KEY, OPS_SERVER_CPU_LEVEL_VALUE, "服务器CPU使用率阀值", OPSAppConstants.CUSTOMER_ID);
			}
			
			if (!isExistedSystemSetting(existedList, OPS_SERVER_RAM_LEVEL_KEY)) {
				addSystemSetting(OPS_SERVER_RAM_LEVEL_KEY, OPS_SERVER_RAM_LEVEL_VALUE, "服务器RAM使用率阀值", OPSAppConstants.CUSTOMER_ID);
			}
			
			if (!isExistedSystemSetting(existedList, OPS_SERVER_HDD_IGNORE_KEY)) {
				addSystemSetting(OPS_SERVER_HDD_IGNORE_KEY, OPS_SERVER_HDD_IGNORE_VALUE, "服务器硬盘使用率忽略阀值", OPSAppConstants.CUSTOMER_ID);
			}
			
			if (!isExistedSystemSetting(existedList, OPS_SERVER_CPU_IGNORE_KEY)) {
				addSystemSetting(OPS_SERVER_CPU_IGNORE_KEY, OPS_SERVER_CPU_IGNORE_VALUE, "服务器CPU使用率忽略阀值", OPSAppConstants.CUSTOMER_ID);
			}
			
			if (!isExistedSystemSetting(existedList, OPS_SERVER_RAM_IGNORE_KEY)) {
				addSystemSetting(OPS_SERVER_RAM_IGNORE_KEY, OPS_SERVER_RAM_IGNORE_VALUE, "服务器RAM使用率忽略阀值", OPSAppConstants.CUSTOMER_ID);
			}

			long end_time = System.currentTimeMillis();
			log.info("初始化系统运行参数设置表一共用时:" + (end_time - start_time) + "毫秒");
		} catch (Exception e) {
			log.error("初始化系统运行参数设置表失败", e);
		}
	}

	private static boolean isExistedSystemSetting(List existedList, String parameter_id) {
		if (existedList.size() > 0) {
			for (int i = 0; i < existedList.size(); i++) {
				Map existedMap = (Map) existedList.get(i);
				if (parameter_id.equals(existedMap.get("parameter_id"))) {
					return true;
				}
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List selectSystemConfigByCustomerID(String customer_id) throws Exception {
		Map parammetersMap = new HashMap();
		parammetersMap.put(FrameworkConstants.CUSTOMER_ID, OPSAppConstants.CUSTOMER_ID);
		List resultList = DaoUtil.selectList("com.hhh.platform.ops.sql.opsMapper.selectByCustomerID", parammetersMap);
		return resultList;
	}

	/**
	 * 初始化系统运行参数设置表
	 * 
	 * @throws Exception
	 */
	public static void addSystemSetting(String parameter_id, String value, String beizhu, String customer_id) throws Exception {
		HashMap<String, Object> parametersSystemSetting = new HashMap<String, Object>();
		parametersSystemSetting.put("parameter_id", parameter_id);
		parametersSystemSetting.put(FrameworkConstants.CUSTOMER_ID, customer_id);
		parametersSystemSetting.put("value", value);
		parametersSystemSetting.put("beizhu", beizhu);
		DaoUtil.insert("com.hhh.platform.ops.sql.opsMapper.insertSystemSetting", parametersSystemSetting);
	}

	public static HashMap<String, Object> changeToMap(List<Map> list) {
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		if (list == null) {
			return new HashMap<String, Object>();
		} else {
			int length = list.size();
			for (int i = 0; i < length; i++) {
				if (list.get(i).get("parameter_id").equals(OPS_DM_TYPE_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.ops_dm_type", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_YW_TYPE_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.ops_yw_type", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_PERFORMANCE_LEVEL_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.spinner_p", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_RUN_LEVEL_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.spinner_r", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_SERVER_HDD_LEVEL_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.spinner_hdd", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_SERVER_CPU_LEVEL_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.spinner_cpu", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_SERVER_RAM_LEVEL_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.spinner_ram", list.get(i).get("value"));
					}
				}else if (list.get(i).get("parameter_id").equals(OPS_SERVER_HDD_IGNORE_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.ignore_hdd", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_SERVER_CPU_IGNORE_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.ignore_cpu", list.get(i).get("value"));
					}
				} else if (list.get(i).get("parameter_id").equals(OPS_SERVER_RAM_IGNORE_KEY)) {
					if (list.get(i).get("value") != null && list.get(i).get("value").toString().length() > 0) {
						resultMap.put("m.ignore_ram", list.get(i).get("value"));
					}
				}
			}
			return resultMap;
		}
	}

	public static String[] getDmType() throws Exception {
		String[] result = new String[0];
		java.util.List resultList = SystemSettingHelper.selectSystemConfigByCustomerID(OPSAppConstants.CUSTOMER_ID);
		HashMap<String, Object> valuesInDB = SystemSettingHelper.changeToMap(resultList);
		String dmType = (String) valuesInDB.get("m." + OPS_DM_TYPE_KEY);
		if (dmType != null && !dmType.isEmpty()) {
			result = dmType.split(",");
		}
		return result;
	}

	public static String[] getYwType() throws Exception {
		String[] result = new String[0];
		java.util.List resultList = SystemSettingHelper.selectSystemConfigByCustomerID(OPSAppConstants.CUSTOMER_ID);
		HashMap<String, Object> valuesInDB = SystemSettingHelper.changeToMap(resultList);
		String ywType = (String) valuesInDB.get("m." + OPS_YW_TYPE_KEY);
		if (ywType != null && !ywType.isEmpty()) {
			result = ywType.split(",");
		}
		return result;
	}
}
