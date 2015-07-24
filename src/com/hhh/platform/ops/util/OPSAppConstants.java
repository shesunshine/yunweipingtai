package com.hhh.platform.ops.util;

import com.hhh.platform.advisors.framework.FrameworkConstants;

public class OPSAppConstants extends FrameworkConstants{
	
	/**
	 * 平台信息的前缀
	 */
	public static String PLATFOMINFO_PREFIX = "m";
	
	/**
	 * 平台管理员ID 
	 */
	public static final String ADMIN_ID = "admin";
	
	/**
	 * 平台管理员姓名
	 */
	public static final String ADMIN_NAME = "三和管理员";
	
	/**
	 * 平台管理员密码
	 */
	public static final String ADMIN_PASS = "3hsaasadmin";
	
	/**
	 * 平台管理中心组织ID
	 */
	public static final String CUSTOMER_ID = "100000";
	
	/**
	 * 平台管理中心组织机构名称
	 */
	public static final String ORG_NAME = "广州粤建三和软件有限公司";
	
	/**
	 * 平台管理中心组织机构联系方式
	 */
	public static final String ORG_TEL = "020-61072700";
	
	/**
	 * 平台管理中心组织机构代号
	 */
	public static final String ORG_CODE = "510640";
	
	/**
	 * 平台管理中心组织机构所在省份
	 */
	public static final String ORG_PROVINCE = "广东省";
	
	/**
	 * 平台管理中心组织机构所在城市
	 */
	public static final String ORG_CITY = "广州市";
	
	/**
	 * 平台管理中心组织机构地址
	 */
	public static final String ORG_ADDRESS = "广州市天河区五山金颍路1号金颍大厦6层";
	
	/**
	 * 状态
	 */
	public static final String STATUS = "已激活";

	public static final String APP_URLS_DEFAULT = "http://localhost:8080/default";
	public static final String APP_URLS_OPS = "http://localhost:8080/ops";
	public static final String APP_NAME_DEFAULT = "default";
	public static final String APP_NAME_OPS = "ops";
	public static final String APP_TYPE_BUILDIN = "buildin";

	/**
	 * 三和管理员顶级组织机构UUID
	 */
	public static final String ORGNIZATION_UUID = "343acf0e5e71432ca5e8161abdda66d6";
	public static final String ORGNIZATION_NAME = "平台管理工具";
	public static final String ORGNIZATION_CUSTOMER_ID = "100000";

	/**
	 * 100000管理员账号UUID
	 */
	public static final String SUPERADMIN_UUID = "cd714e77220b4bafbf3ec71fe8ae5898";

	public static final String PLATFORM_REAL_ID = "com.hhh.platform";
	public static final String PLATFORM_UUID = "c5a061204d964c88b883d721863d4c41";
	public static final String PLATFORM_LABEL = "建设工程质量管理信息服务平台";
	public static final String PLATFORM_SEQ = "0";

	public static final String PLATFORM_OPS_REAL_ID = "com.hhh.platform.ops";
	public static final String PLATFORM_OPS_UUID = "c9c4e13625dc4e4baeaae8971064adf7";
	public static final String PLATFORM_OPS_LABEL = "运维平台";
	public static final String PLATFORM_OPS_SEQ = "0";

	public static final String OPS_MENU_HOME_REAL_ID = "com.hhh.platform.ops.menu.home";
	public static final String OPS_MENU_HOME_UUID = "1766461fefd04dbcb12d13ff09350909";
	public static final String OPS_MENU_DEFAULT_LABEL = "首页";
	public static final String OPS_MENU_DEFAULT_SEQ = "0";

	public static final String OPS_MENU_PRODUCT_REGISTER_REAL_ID = "com.hhh.platform.ops.menu.product.register";
	public static final String OPS_MENU_PRODUCT_REGISTER_UUID = "5d9e9d7ea53444b596c2513a95de0302";
	public static final String OPS_MENU_PRODUCT_REGISTER_LABEL = "产品登记";
	public static final String OPS_MENU_PRODUCT_REGISTER_SEQ = "0";
	
	public static final String OPS_MENU_SERVER_REGISTER_REAL_ID = "com.hhh.platform.ops.menu.server.register";
	public static final String OPS_MENU_SERVER_REGISTER_UUID = "bf07aa5482b54463b159c47304719b9f";
	public static final String OPS_MENU_SERVER_REGISTER_LABEL = "服务器登记";
	public static final String OPS_MENU_SERVER_REGISTER_SEQ = "0";
	
	public static final String OPS_MENU_SERVER_RUN_REAL_ID = "com.hhh.platform.ops.menu.server.run";
	public static final String OPS_MENU_SERVER_RUN_UUID = "4597b9898dc142ddbd693d9dbf22602d";
	public static final String OPS_MENU_SERVER_RUN_LABEL = "服务器监控";
	public static final String OPS_MENU_SERVER_RUN_SEQ = "0";

	public static final String OPS_MENU_DEPLOY_REAL_ID = "com.hhh.platform.ops.menu.product.deploy";
	public static final String OPS_MENU_DEPLOY_UUID = "d1674f34cacc4e49809c6bb511d23ff7";
	public static final String OPS_MENU_DEPLOY_LABEL = "产品部署";
	public static final String OPS_MENU_DEPLOY_SEQ = "0";

	public static final String OPS_MENU_PERFORMANCE_REAL_ID = "com.hhh.platform.ops.menu.log.performance";
	public static final String OPS_MENU_PERFORMANCE_UUID = "532fb20eb19a4cf1bf2db8b0df3e850a";
	public static final String OPS_MENU_PERFORMANCE_LABEL = "性能日志";
	public static final String OPS_MENU_PERFORMANCE_SEQ = "0";

	public static final String OPS_MENU_EXCEPTION_REAL_ID = "com.hhh.platform.ops.menu.log.exception";
	public static final String OPS_MENU_EXCEPTION_UUID = "b08f8a50bf714856857599e16d12dc50";
	public static final String OPS_MENU_EXCEPTION_LABEL = "异常日志";
	public static final String OPS_MENU_EXCEPTION_SEQ = "0";

	public static final String OPS_MENU_RUN_REAL_ID = "com.hhh.platform.ops.menu.log.run";
	public static final String OPS_MENU_RUN_UUID = "d6f0f7a56231429eb92a2b149ac3ad34";
	public static final String OPS_MENU_RUN_LABEL = "运行监督";
	public static final String OPS_MENU_RUN_SEQ = "0";

	public static final String OPS_MENU_SYSMGT_ORG_REAL_ID = "com.hhh.platform.ops.menu.user.manager";
	public static final String OPS_MENU_SYSMGT_ORG_UUID = "4a61139caf594c099ed760fd669d3a83";
	public static final String OPS_MENU_SYSMGT_ORG_LABEL = "用户管理";
	public static final String OPS_MENU_SYSMGT_ORG_SEQ = "0";

	public static final String OPS_MENU_SYSMGT_SETTING_REAL_ID = "com.hhh.platform.ops.menu.system.setting";
	public static final String OPS_MENU_SYSMGT_SETTING_UUID = "bb5ca1368fa94565a3140c12a94b7c55";
	public static final String OPS_MENU_SYSMGT_SETTING_LABEL = "系统设置";
	public static final String OPS_MENU_SYSMGT_SETTING_SEQ = "0";
	
	/**
	 * 工具栏
	 * 产品登记
	 */
	public static final String OPS_TOOLBAR_REGISTER_ADD_REAL_ID = "com.hhh.platform.ops.view.product.register.toolbar.add";
	public static final String OPS_TOOLBAR_REGISTER_ADD_UUID = "9a1ae2dbe42d41ba874e7bb0680f1097";
	public static final String OPS_TOOLBAR_REGISTER_ADD_LABEL = "新增";
	public static final String OPS_TOOLBAR_REGISTER_ADD_SEQ = "0";
	
	public static final String OPS_TOOLBAR_REGISTER_UPDATE_REAL_ID = "com.hhh.platform.ops.view.product.register.toolbar.update";
	public static final String OPS_TOOLBAR_REGISTER_UPDATE_UUID = "323af2435dff4a8da93eef1a4bf9d8da";
	public static final String OPS_TOOLBAR_REGISTER_UPDATE_LABEL = "修改";
	public static final String OPS_TOOLBAR_REGISTER_UPDATE_SEQ = "0";
	
	public static final String OPS_TOOLBAR_REGISTER_READ_REAL_ID = "com.hhh.platform.ops.view.product.register.toolbar.read";
	public static final String OPS_TOOLBAR_REGISTER_READ_UUID = "5af8d15443604ce6a50f35bc8bf6e293";
	public static final String OPS_TOOLBAR_REGISTER_READ_LABEL = "查看";
	public static final String OPS_TOOLBAR_REGISTER_READ_SEQ = "0";
	
	public static final String OPS_TOOLBAR_REGISTER_DELETE_REAL_ID = "com.hhh.platform.ops.view.product.register.toolbar.delete";
	public static final String OPS_TOOLBAR_REGISTER_DELETE_UUID = "b4645d61ee7e4977a92d8d16200e381d";
	public static final String OPS_TOOLBAR_REGISTER_DELETE_LABEL = "删除";
	public static final String OPS_TOOLBAR_REGISTER_DELETE_SEQ = "0";
	
	/**
	 * 产品部署
	 */
	public static final String OPS_TOOLBAR_DEPLOY_ADD_REAL_ID = "com.hhh.platform.ops.view.product.deploy.toolbar.add";
	public static final String OPS_TOOLBAR_DEPLOY_ADD_UUID = "ebe5815d415e4cb3baf40069fb133252";
	public static final String OPS_TOOLBAR_DEPLOY_ADD_LABEL = "新增";
	public static final String OPS_TOOLBAR_DEPLOY_ADD_SEQ = "0";
	
	public static final String OPS_TOOLBAR_DEPLOY_UPDATE_REAL_ID = "com.hhh.platform.ops.view.product.deploy.toolbar.update";
	public static final String OPS_TOOLBAR_DEPLOY_UPDATE_UUID = "6b36c35c7c444f2cbc28af7d4f30fe43";
	public static final String OPS_TOOLBAR_DEPLOY_UPDATE_LABEL = "修改";
	public static final String OPS_TOOLBAR_DEPLOY_UPDATE_SEQ = "0";
	
	public static final String OPS_TOOLBAR_DEPLOY_READ_REAL_ID = "com.hhh.platform.ops.view.product.deploy.toolbar.read";
	public static final String OPS_TOOLBAR_DEPLOY_READ_UUID = "31d3db8011434af398df38c25d2c2741";
	public static final String OPS_TOOLBAR_DEPLOY_READ_LABEL = "查看";
	public static final String OPS_TOOLBAR_DEPLOY_READ_SEQ = "0";
	
	public static final String OPS_TOOLBAR_DEPLOY_DELETE_REAL_ID = "com.hhh.platform.ops.view.product.deploy.toolbar.delete";
	public static final String OPS_TOOLBAR_DEPLOY_DELETE_UUID = "1a101049a41a4fcb8a612dd3a6e8d944";
	public static final String OPS_TOOLBAR_DEPLOY_DELETE_LABEL = "删除";
	public static final String OPS_TOOLBAR_DEPLOY_DELETE_SEQ = "0";
	
	/**
	 * 服务器登记
	 */
	public static final String OPS_TOOLBAR_SERVER_ADD_REAL_ID = "com.hhh.platform.ops.view.server.register.toolbar.add";
	public static final String OPS_TOOLBAR_SERVER_ADD_UUID = "39b10a7fd1e84f0d8b6999b49e3d8db7";
	public static final String OPS_TOOLBAR_SERVER_ADD_LABEL = "新增";
	public static final String OPS_TOOLBAR_SERVER_ADD_SEQ = "0";
	
	public static final String OPS_TOOLBAR_SERVER_UPDATE_REAL_ID = "com.hhh.platform.ops.view.server.register.toolbar.update";
	public static final String OPS_TOOLBAR_SERVER_UPDATE_UUID = "c124c601333c4d84b9f53dd9c725a67d";
	public static final String OPS_TOOLBAR_SERVER_UPDATE_LABEL = "修改";
	public static final String OPS_TOOLBAR_SERVER_UPDATE_SEQ = "0";
	
	public static final String OPS_TOOLBAR_SERVER_READ_REAL_ID = "com.hhh.platform.ops.view.server.register.toolbar.read";
	public static final String OPS_TOOLBAR_SERVER_READ_UUID = "404e3a357d954532a09d977882d2b285";
	public static final String OPS_TOOLBAR_SERVER_READ_LABEL = "查看";
	public static final String OPS_TOOLBAR_SERVER_READ_SEQ = "0";
	
	public static final String OPS_TOOLBAR_SERVER_DELETE_REAL_ID = "com.hhh.platform.ops.view.server.register.toolbar.delete";
	public static final String OPS_TOOLBAR_SERVER_DELETE_UUID = "ca32e888fc8c4353a306d9d384725059";
	public static final String OPS_TOOLBAR_SERVER_DELETE_LABEL = "删除";
	public static final String OPS_TOOLBAR_SERVER_DELETE_SEQ = "0";
	
	public static final String OPS_TOOLBAR_SERVER_PRODUCT_REAL_ID = "com.hhh.platform.ops.view.server.register.toolbar.product";
	public static final String OPS_TOOLBAR_SERVER_PRODUCT_UUID = "6020b979184b41648929c0c3d437f965";
	public static final String OPS_TOOLBAR_SERVER_PRODUCT_LABEL = "产品列表";
	public static final String OPS_TOOLBAR_SERVER_PRODUCT_SEQ = "0";
	
	/**
	 * 性能日志
	 */
	public static final String OPS_TOOLBAR_PERFORMANCE_READ_REAL_ID = "com.hhh.platform.ops.view.log.performance.toolbar.read";
	public static final String OPS_TOOLBAR_PERFORMANCE_READ_UUID = "b3bd526f862140ed928078daae5d7ed7";
	public static final String OPS_TOOLBAR_PERFORMANCE_READ_LABEL = "查看";
	public static final String OPS_TOOLBAR_PERFORMANCE_READ_SEQ = "0";
	
	public static final String OPS_TOOLBAR_PERFORMANCE_DEAL_REAL_ID = "com.hhh.platform.ops.view.log.performance.toolbar.deal";
	public static final String OPS_TOOLBAR_PERFORMANCE_DEAL_UUID = "a83e33843c0243b2bc172f9271980ed0";
	public static final String OPS_TOOLBAR_PERFORMANCE_DEAL_LABEL = "处理";
	public static final String OPS_TOOLBAR_PERFORMANCE_DEAL_SEQ = "0";
	/**
	 * 异常日志
	 */
	public static final String OPS_TOOLBAR_EXCEPTION_READ_REAL_ID = "com.hhh.platform.ops.view.log.exception.toolbar.read";
	public static final String OPS_TOOLBAR_EXCEPTION_READ_UUID = "3ebe7bb2197a460db0ffba927996fcf7";
	public static final String OPS_TOOLBAR_EXCEPTION_READ_LABEL = "查看";
	public static final String OPS_TOOLBAR_EXCEPTION_READ_SEQ = "0";
	
	public static final String OPS_TOOLBAR_EXCEPTION_DEAL_REAL_ID = "com.hhh.platform.ops.view.log.exception.toolbar.deal";
	public static final String OPS_TOOLBAR_EXCEPTION_DEAL_UUID = "e0e0d2435d724dcd8cb1363ba4af9e98";
	public static final String OPS_TOOLBAR_EXCEPTION_DEAL_LABEL = "处理";
	public static final String OPS_TOOLBAR_EXCEPTION_DEAL_SEQ = "0";
	
	/**
	 * 运行日志
	 */
	public static final String OPS_TOOLBAR_RUN_READ_REAL_ID = "com.hhh.platform.ops.view.log.run.toolbar.read";
	public static final String OPS_TOOLBAR_RUN_READ_UUID = "cae51be8817749a5820f09eca9f0f5d8";
	public static final String OPS_TOOLBAR_RUN_READ_LABEL = "查看";
	public static final String OPS_TOOLBAR_RUN_READ_SEQ = "0";
	
	public static final String OPS_TOOLBAR_RUN_DEAL_REAL_ID = "com.hhh.platform.ops.view.log.run.toolbar.deal";
	public static final String OPS_TOOLBAR_RUN_DEAL_UUID = "e5d4471484844c9da76be1ac72104974";
	public static final String OPS_TOOLBAR_RUN_DEAL_LABEL = "处理";
	public static final String OPS_TOOLBAR_RUN_DEAL_SEQ = "0";
	
	/**
	 * 服务器运行日志
	 */
	public static final String OPS_TOOLBAR_SERVER_RUN_READ_REAL_ID = "com.hhh.platform.ops.view.server.run.toolbar.read";
	public static final String OPS_TOOLBAR_SERVER_RUN_READ_UUID = "3f74c9ec5e3248799872e8b89f849ec5";
	public static final String OPS_TOOLBAR_SERVER_RUN_READ_LABEL = "查看";
	public static final String OPS_TOOLBAR_SERVER_RUN_READ_SEQ = "0";
}
