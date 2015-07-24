package com.hhh.platform.ops;

import java.util.List;
import java.util.Map;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.hhh.platform.cache.CacheService;
import com.hhh.platform.cache.CacheServiceUtil;
import com.hhh.platform.ops.util.OPSAppConstants;
import com.hhh.platform.ops.util.OPSUtil;
import com.hhh.platform.ops.util.PlatformHelper;
import com.hhh.platform.ops.util.SystemSettingHelper;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.hhh.platform.ops"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private String latestVersion = "201506031206";

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		if (OPSUtil.checkIsUpdate(latestVersion)) {
			initializeAdmin();
			SystemSettingHelper.initializeSystemSetting();
		}
		List list = SystemSettingHelper.selectSystemConfigByCustomerID(OPSAppConstants.CUSTOMER_ID);
		CacheService cache = CacheServiceUtil.getCacheService();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			if (map.get("parameter_id").equals(SystemSettingHelper.OPS_DM_TYPE_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_DM_TYPE_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_YW_TYPE_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_YW_TYPE_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_PERFORMANCE_LEVEL_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_PERFORMANCE_LEVEL_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_RUN_LEVEL_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_RUN_LEVEL_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_SERVER_HDD_LEVEL_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_SERVER_HDD_LEVEL_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_SERVER_CPU_LEVEL_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_SERVER_CPU_LEVEL_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_SERVER_RAM_LEVEL_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_SERVER_RAM_LEVEL_KEY, map.get("value"));
				}
			}else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_SERVER_HDD_IGNORE_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_SERVER_HDD_IGNORE_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_SERVER_CPU_IGNORE_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_SERVER_CPU_IGNORE_KEY, map.get("value"));
				}
			} else if (map.get("parameter_id").equals(SystemSettingHelper.OPS_SERVER_RAM_IGNORE_KEY)) {
				if (map.get("value") != null && map.get("value").toString().length() > 0) {
					cache.put(SystemSettingHelper.OPS_SERVER_RAM_IGNORE_KEY, map.get("value"));
				}
			}
		}

	}

	private void initializeAdmin() throws Exception {
		initializeApps();
		initializeCustomerInfo();
		initializeOrgization();
		initializeServices();
		initializeOrgServices();
	}

	private void initializeCustomerInfo() {
		PlatformHelper.addCustomerInfo();
	}

	private void initializeOrgization() {
		PlatformHelper.addOrgnization(OPSAppConstants.ORGNIZATION_CUSTOMER_ID, OPSAppConstants.ORGNIZATION_NAME, "0", OPSAppConstants.ORGNIZATION_CUSTOMER_ID);
	}

	private void initializeApps() throws Exception {
		// 添加default应用
		PlatformHelper.addApps(OPSAppConstants.APP_NAME_DEFAULT, OPSAppConstants.APP_URLS_DEFAULT, OPSAppConstants.APP_TYPE_BUILDIN);
		PlatformHelper.addApps(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.APP_URLS_OPS, OPSAppConstants.APP_TYPE_BUILDIN);
	}

	private void initializeServices() throws Exception {
		// 添加建设工程质量管理信息服务平台服务
		PlatformHelper
				.addServices(OPSAppConstants.APP_NAME_DEFAULT, OPSAppConstants.PLATFORM_UUID, OPSAppConstants.PLATFORM_REAL_ID, "0", OPSAppConstants.PLATFORM_LABEL, OPSAppConstants.PLATFORM_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.PLATFORM_OPS_UUID, OPSAppConstants.PLATFORM_OPS_REAL_ID, OPSAppConstants.PLATFORM_UUID,
				OPSAppConstants.PLATFORM_OPS_LABEL, OPSAppConstants.PLATFORM_OPS_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_HOME_UUID, OPSAppConstants.OPS_MENU_HOME_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_DEFAULT_LABEL, OPSAppConstants.OPS_MENU_DEFAULT_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_UUID, OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_LABEL, OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_SERVER_REGISTER_UUID, OPSAppConstants.OPS_MENU_SERVER_REGISTER_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_SERVER_REGISTER_LABEL, OPSAppConstants.OPS_MENU_SERVER_REGISTER_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_SERVER_RUN_UUID, OPSAppConstants.OPS_MENU_SERVER_RUN_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_SERVER_RUN_LABEL, OPSAppConstants.OPS_MENU_SERVER_RUN_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_DEPLOY_UUID, OPSAppConstants.OPS_MENU_DEPLOY_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_DEPLOY_LABEL, OPSAppConstants.OPS_MENU_DEPLOY_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_PERFORMANCE_UUID, OPSAppConstants.OPS_MENU_PERFORMANCE_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_PERFORMANCE_LABEL, OPSAppConstants.OPS_MENU_PERFORMANCE_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_EXCEPTION_UUID, OPSAppConstants.OPS_MENU_EXCEPTION_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_EXCEPTION_LABEL, OPSAppConstants.OPS_MENU_EXCEPTION_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_RUN_UUID, OPSAppConstants.OPS_MENU_RUN_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_RUN_LABEL, OPSAppConstants.OPS_MENU_RUN_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_SYSMGT_ORG_UUID, OPSAppConstants.OPS_MENU_SYSMGT_ORG_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_SYSMGT_ORG_LABEL, OPSAppConstants.OPS_MENU_SYSMGT_ORG_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_MENU_SYSMGT_SETTING_UUID, OPSAppConstants.OPS_MENU_SYSMGT_SETTING_REAL_ID, OPSAppConstants.PLATFORM_OPS_UUID,
				OPSAppConstants.OPS_MENU_SYSMGT_SETTING_LABEL, OPSAppConstants.OPS_MENU_SYSMGT_SETTING_SEQ);

		// 产品登记
		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_REGISTER_ADD_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_ADD_REAL_ID,
				OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_ADD_LABEL, OPSAppConstants.OPS_TOOLBAR_REGISTER_ADD_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_REGISTER_UPDATE_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_UPDATE_REAL_ID,
				OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_UPDATE_LABEL, OPSAppConstants.OPS_TOOLBAR_REGISTER_UPDATE_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_REGISTER_READ_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_READ_REAL_ID,
				OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_READ_LABEL, OPSAppConstants.OPS_TOOLBAR_REGISTER_READ_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_REGISTER_DELETE_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_DELETE_REAL_ID,
				OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_UUID, OPSAppConstants.OPS_TOOLBAR_REGISTER_DELETE_LABEL, OPSAppConstants.OPS_TOOLBAR_REGISTER_DELETE_SEQ);
		//产品部署
		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_DEPLOY_ADD_UUID, OPSAppConstants.OPS_TOOLBAR_DEPLOY_ADD_REAL_ID, OPSAppConstants.OPS_MENU_DEPLOY_UUID,
				OPSAppConstants.OPS_TOOLBAR_DEPLOY_ADD_LABEL, OPSAppConstants.OPS_TOOLBAR_DEPLOY_ADD_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_DEPLOY_UPDATE_UUID, OPSAppConstants.OPS_TOOLBAR_DEPLOY_UPDATE_REAL_ID,
				OPSAppConstants.OPS_MENU_DEPLOY_UUID, OPSAppConstants.OPS_TOOLBAR_DEPLOY_UPDATE_LABEL, OPSAppConstants.OPS_TOOLBAR_DEPLOY_UPDATE_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_DEPLOY_READ_UUID, OPSAppConstants.OPS_TOOLBAR_DEPLOY_READ_REAL_ID, OPSAppConstants.OPS_MENU_DEPLOY_UUID,
				OPSAppConstants.OPS_TOOLBAR_DEPLOY_READ_LABEL, OPSAppConstants.OPS_TOOLBAR_DEPLOY_READ_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_DEPLOY_DELETE_UUID, OPSAppConstants.OPS_TOOLBAR_DEPLOY_DELETE_REAL_ID,
				OPSAppConstants.OPS_MENU_DEPLOY_UUID, OPSAppConstants.OPS_TOOLBAR_DEPLOY_DELETE_LABEL, OPSAppConstants.OPS_TOOLBAR_DEPLOY_DELETE_SEQ);
		//服务器登记
		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_SERVER_ADD_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_ADD_REAL_ID, OPSAppConstants.OPS_MENU_SERVER_REGISTER_UUID,
				OPSAppConstants.OPS_TOOLBAR_SERVER_ADD_LABEL, OPSAppConstants.OPS_TOOLBAR_SERVER_ADD_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_SERVER_UPDATE_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_UPDATE_REAL_ID,
				OPSAppConstants.OPS_MENU_SERVER_REGISTER_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_UPDATE_LABEL, OPSAppConstants.OPS_TOOLBAR_SERVER_UPDATE_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_SERVER_READ_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_READ_REAL_ID, OPSAppConstants.OPS_MENU_SERVER_REGISTER_UUID,
				OPSAppConstants.OPS_TOOLBAR_SERVER_READ_LABEL, OPSAppConstants.OPS_TOOLBAR_SERVER_READ_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_SERVER_DELETE_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_DELETE_REAL_ID,
				OPSAppConstants.OPS_MENU_SERVER_REGISTER_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_DELETE_LABEL, OPSAppConstants.OPS_TOOLBAR_SERVER_DELETE_SEQ);
		
		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_SERVER_PRODUCT_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_PRODUCT_REAL_ID,
				OPSAppConstants.OPS_MENU_SERVER_REGISTER_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_PRODUCT_LABEL, OPSAppConstants.OPS_TOOLBAR_SERVER_PRODUCT_SEQ);

		//性能日志
		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_READ_UUID, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_READ_REAL_ID,
				OPSAppConstants.OPS_MENU_PERFORMANCE_UUID, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_READ_LABEL, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_READ_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_DEAL_UUID, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_DEAL_REAL_ID,
				OPSAppConstants.OPS_MENU_PERFORMANCE_UUID, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_DEAL_LABEL, OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_DEAL_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_READ_UUID, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_READ_REAL_ID,
				OPSAppConstants.OPS_MENU_EXCEPTION_UUID, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_READ_LABEL, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_READ_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_DEAL_UUID, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_DEAL_REAL_ID,
				OPSAppConstants.OPS_MENU_EXCEPTION_UUID, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_DEAL_LABEL, OPSAppConstants.OPS_TOOLBAR_EXCEPTION_DEAL_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_RUN_READ_UUID, OPSAppConstants.OPS_TOOLBAR_RUN_READ_REAL_ID, OPSAppConstants.OPS_MENU_RUN_UUID,
				OPSAppConstants.OPS_TOOLBAR_RUN_READ_LABEL, OPSAppConstants.OPS_TOOLBAR_RUN_READ_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_RUN_DEAL_UUID, OPSAppConstants.OPS_TOOLBAR_RUN_DEAL_REAL_ID, OPSAppConstants.OPS_MENU_RUN_UUID,
				OPSAppConstants.OPS_TOOLBAR_RUN_DEAL_LABEL, OPSAppConstants.OPS_TOOLBAR_RUN_DEAL_SEQ);

		PlatformHelper.addServices(OPSAppConstants.APP_NAME_OPS, OPSAppConstants.OPS_TOOLBAR_SERVER_RUN_READ_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_RUN_READ_REAL_ID,
				OPSAppConstants.OPS_MENU_SERVER_RUN_UUID, OPSAppConstants.OPS_TOOLBAR_SERVER_RUN_READ_LABEL, OPSAppConstants.OPS_TOOLBAR_SERVER_RUN_READ_SEQ);

	}

	private void initializeOrgServices() {
		addOrgService(OPSAppConstants.PLATFORM_OPS_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_HOME_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_PRODUCT_REGISTER_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_SERVER_REGISTER_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_DEPLOY_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_PERFORMANCE_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_EXCEPTION_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_RUN_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_SYSMGT_ORG_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_SYSMGT_SETTING_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_MENU_SERVER_RUN_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		
		addOrgService(OPSAppConstants.OPS_TOOLBAR_REGISTER_ADD_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_REGISTER_UPDATE_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_REGISTER_READ_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_REGISTER_DELETE_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_DEPLOY_ADD_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_DEPLOY_UPDATE_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_DEPLOY_READ_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_DEPLOY_DELETE_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_SERVER_ADD_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_SERVER_UPDATE_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_SERVER_READ_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_SERVER_DELETE_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_READ_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_PERFORMANCE_DEAL_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_EXCEPTION_READ_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_EXCEPTION_DEAL_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_RUN_READ_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_RUN_DEAL_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_SERVER_RUN_READ_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		addOrgService(OPSAppConstants.OPS_TOOLBAR_SERVER_PRODUCT_REAL_ID, OPSAppConstants.APP_NAME_OPS);
		
	}

	private void addOrgService(String real_id, String appname) {
		String serviceId = PlatformHelper.getId(real_id, appname);
		if (serviceId != null) {
			PlatformHelper.addOrgSer(OPSAppConstants.ORGNIZATION_UUID, OPSAppConstants.ORGNIZATION_CUSTOMER_ID, serviceId);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
