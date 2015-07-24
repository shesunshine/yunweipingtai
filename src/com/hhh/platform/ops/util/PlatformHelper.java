package com.hhh.platform.ops.util;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.orm.mybatis.SQLSessionService;
import com.hhh.platform.orm.mybatis.SQLSessionServiceUtil;
import com.hhh.platform.util.MD5Util;

public class PlatformHelper {

	private static Log log = LogFactory.getLog(PlatformHelper.class);
	@SuppressWarnings("unchecked")
	public static void addApps(String name, String urls, String type) {
		HashMap<String, Object> resultForApps = new HashMap<String, Object>();
		HashMap<String, Object> parametersForApps = new HashMap<String, Object>();
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForApps.put("name", name);
			resultForApps = (HashMap<String, Object>) session.selectOne(
					"com.hhh.platform.ops.sql.opsMapper.selectByName", 
					parametersForApps);
			if (resultForApps == null) {
				parametersForApps.put("urls", urls);
				parametersForApps.put("type", type);
				session.insert(
						"com.hhh.platform.ops.sql.opsMapper.addApps",
						parametersForApps);
			}
		} catch (Exception e) {
			log.error("添加" + name + "应用错误，初始化ss_apps表失败!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 添加三和管理员账号
	 */
	@SuppressWarnings("unchecked")
	public static void addCustomerInfo() {
		HashMap<String, Object> resultForCustomerInfo = new HashMap<String, Object>();
		HashMap<String, Object> parametersForCustomerInfo = new HashMap<String, Object>();
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForCustomerInfo.put("customer_id", OPSAppConstants.CUSTOMER_ID);
			resultForCustomerInfo = (HashMap<String, Object>) session.selectOne(
					"com.hhh.platform.ops.sql.opsMapper.selectCustomerByCustomerId",
					parametersForCustomerInfo);
			if (resultForCustomerInfo == null) {
				parametersForCustomerInfo.put("customer_serial_id", OPSAppConstants.SUPERADMIN_UUID);
				parametersForCustomerInfo.put("admin_id", OPSAppConstants.ADMIN_ID);
				parametersForCustomerInfo.put("admin_name", OPSAppConstants.ADMIN_NAME);
				parametersForCustomerInfo.put("admin_pass", MD5Util.getMD5(OPSAppConstants.ADMIN_PASS));
				parametersForCustomerInfo.put("org_name", OPSAppConstants.ORG_NAME);
				parametersForCustomerInfo.put("org_province", OPSAppConstants.ORG_PROVINCE);
				parametersForCustomerInfo.put("org_city", OPSAppConstants.ORG_CITY);
				parametersForCustomerInfo.put("org_address", OPSAppConstants.ORG_ADDRESS);
				parametersForCustomerInfo.put("org_tel", OPSAppConstants.ORG_TEL);
				parametersForCustomerInfo.put("org_code", OPSAppConstants.ORG_CODE);
				parametersForCustomerInfo.put("status", OPSAppConstants.STATUS);				
				session.insert(
						"com.hhh.platform.ops.sql.opsMapper.initializeCustomerInfo",
						parametersForCustomerInfo);
			}
		} catch (Exception e) {
			log.error("添加三和管理员账号错误，初始化平台管理中心ss_customer_info表失败!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}

	/**
	 * 添加三和管理员顶级组织机构
	 * @param customer_id 组织机构ID
	 * @param label 组织机构名称
	 * @param pid 上一级ID
	 * @param orgcode 组织机构代号
	 */
	@SuppressWarnings("unchecked")
	public static void addOrgnization(String customer_id, String label, String pid, String orgcode) {
		HashMap<String, Object> resultForOrgnization = new HashMap<String, Object>();
		HashMap<String, Object> parametersForOrgnization = new HashMap<String, Object>();
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForOrgnization.put("customer_id", customer_id);
			resultForOrgnization = (HashMap<String, Object>) session.selectOne(
					"com.hhh.platform.admin.sql.orgnizationMapper.selectOrgIdByCustomerId",
					parametersForOrgnization);
			if (resultForOrgnization == null) {
				parametersForOrgnization.put("id", OPSAppConstants.ORGNIZATION_UUID);
				parametersForOrgnization.put("label", label);
				parametersForOrgnization.put("pid", pid);
				parametersForOrgnization.put("orgcode", orgcode);
				session.insert("com.hhh.platform.admin.sql.orgnizationMapper.addOrgnization",
								parametersForOrgnization);
			}
		} catch (Exception e) {
			log.error("添加"+label+"组织机构错误，初始化平台管理中心ss_orgnization表失败!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	/**
	 * 添加服务
	 * @param appname 应用名称
	 * @param id 服务ID
	 * @param real_id 服务real_id
	 * @param pid 上级服务ID
	 * @param label 服务名称
	 * @param seq 排序号
	 */
	@SuppressWarnings("unchecked")
	public static void addServices(String appname, String id, String real_id, String pid, String label, String seq) {
		HashMap<String, Object> resultForServices = new HashMap<String, Object>();
		HashMap<String, Object> parametersForService = new HashMap<String, Object>();
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForService.put("customer_id", "000000");
			parametersForService.put("appname", appname);
			parametersForService.put("real_id", real_id);
			resultForServices = (HashMap<String, Object>) session.selectOne(
					"com.hhh.platform.admin.sql.serviceMapper.selectServiceByRealId",
					parametersForService);
			if (resultForServices == null) {
				parametersForService.put("id", id);
				parametersForService.put("label", label);
				parametersForService.put("pid", pid);
				parametersForService.put("seq", seq);
				session.insert("com.hhh.platform.admin.sql.serviceMapper.addServiceSort",
								parametersForService);
			}
		} catch (Exception e) {
			log.error("添加"+label+"服务错误，初始化平台管理中心ss_service表失败!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void addOrgSer(String orgnization_id, String customer_id,
			String service_id) {
		HashMap<String, Object> resultForOrgSer = new HashMap<String, Object>();
		HashMap<String, Object> parametersForOrgSer = new HashMap<String, Object>();
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil
					.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForOrgSer.put("orgnization_id",orgnization_id);
			parametersForOrgSer.put("customer_id", customer_id);
			parametersForOrgSer.put("service_id", service_id);
			resultForOrgSer = (HashMap<String, Object>) session.selectOne(
					"com.hhh.platform.admin.sql.org_serMapper.selectOrgSerByCustomerId",
					parametersForOrgSer);
			if (resultForOrgSer == null) {
				parametersForOrgSer.put("service_id", service_id);
				parametersForOrgSer.put("istoporgnization", 0);
				parametersForOrgSer.put("ser_status", "已激活");
				parametersForOrgSer.put("ser_remark", "");
				session.insert("com.hhh.platform.admin.sql.org_serMapper.addOrg_Ser",parametersForOrgSer);
			}
		} catch (Exception e) {
			log.error("添加"+orgnization_id+"与"+service_id+"关联失败，初始化平台管理中心ss_org_ser表失败!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static String getServiceId(String real_id,String appname) {
		HashMap<String, Object> resultForService = new HashMap<String, Object>();
		HashMap<String, Object> parametersForService = new HashMap<String, Object>();
		String pid = null;
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil
					.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForService.put("real_id", real_id);
			parametersForService.put("appname", appname);
			resultForService = (HashMap<String, Object>) session
			.selectOne(
					"com.hhh.platform.admin.sql.serviceMapper.selectServiceByRealId",
					parametersForService);
			if(resultForService != null) {
				resultForService = BindingHelper.transferKeyToLowerCase(resultForService);
				pid =  resultForService.get("id").toString();
			}
		} catch (Exception e) {
			log.error("获取PID错误!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
		return pid;
	}
	
	
	@SuppressWarnings("unchecked")
	public static String getTopOrgnizationid() {
		HashMap<String, Object> resultForService = new HashMap<String, Object>();
		HashMap<String, Object> parametersForService = new HashMap<String, Object>();
		String pid = null;
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil
					.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForService.put("customer_id", "100000");
			parametersForService.put("pid", "0");
			resultForService = (HashMap<String, Object>) session
			.selectOne(
					"com.hhh.platform.admin.sql.orgnizationMapper.selectOrgIdByCustomerId",
					parametersForService);
			if(resultForService != null) {
				resultForService = BindingHelper.transferKeyToLowerCase(resultForService);
				pid =  resultForService.get("id").toString();
			}
		} catch (Exception e) {
			log.error("获取顶级机构id错误!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
		return pid;
	}
	
	@SuppressWarnings("unchecked")
	public static String getId(String real_id,String appname) {
		HashMap<String, Object> resultForService = new HashMap<String, Object>();
		HashMap<String, Object> parametersForService = new HashMap<String, Object>();
		String id = null;
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil
					.getSQLSessionService();
			session = sessionService.getSession("main", true);
			parametersForService.put("real_id", real_id);
			parametersForService.put("appname", appname);
			resultForService = (HashMap<String, Object>) session
			.selectOne(
					"com.hhh.platform.admin.sql.serviceMapper.selectServiceByRealId",
					parametersForService);
			if(resultForService != null) {
				resultForService = BindingHelper.transferKeyToLowerCase(resultForService);
				id =  resultForService.get("id").toString();
			}
		} catch (Exception e) {
			log.error("获取PID错误!", e);
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
				}
			}
		}
		return id;
	}

}
