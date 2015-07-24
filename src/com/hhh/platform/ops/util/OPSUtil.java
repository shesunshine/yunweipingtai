package com.hhh.platform.ops.util;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.rap.rwt.RWT;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHelper;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class OPSUtil {

	private static Log log = LogFactory.getLog(OPSUtil.class);

	private static final String APPNAME = "default";

	private static final String SERVICE_UPDATE_ID = "ops.serivce";

	// public static String GetPlatformServiceID() {
	// String platformServiceID = null;
	// try {
	// ServiceDao serviceDao = new ServiceDao();
	// Map parametersMap = new HashMap();
	// parametersMap.put(FrameworkConstants.CURRENT_APPNAME, APPNAME);
	// Map resultMap =
	// serviceDao.selectPlatformServiceIDByAppName(parametersMap);
	// if (resultMap != null) {
	// platformServiceID = resultMap.get("id").toString();
	// }
	// } catch (Exception e) {
	// log.error("获取platformserviceID错误", e);
	// throw new RuntimeException("获取platformserviceID错误;" + e.getMessage(), e);
	// }
	//
	// return platformServiceID;
	// }

	public static boolean checkIsUpdate(String latestVersion) {
		HashMap resultMap = new HashMap();
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		boolean isUpdate = false;
		Calendar currDate = Calendar.getInstance();
		String year = Integer.toString(currDate.get(Calendar.YEAR));
		String month = Integer.toString(currDate.get(Calendar.MONTH) + 1);
		String day = Integer.toString(currDate.get(Calendar.DAY_OF_MONTH));
		String hour = Integer.toString(currDate.get(Calendar.HOUR_OF_DAY));
		String minute = Integer.toString(currDate.get(Calendar.MINUTE));

		if (month.length() == 1)
			month = "0" + month;
		if (day.length() == 1)
			day = "0" + day;
		if (hour.length() == 1)
			hour = "0" + hour;
		if (minute.length() == 1)
			minute = "0" + minute;

		try {
			parameters.put("id", SERVICE_UPDATE_ID);
			String sql = "com.hhh.platform.ops.sql.opsMapper.selectUpdateRecord";
			resultMap = (HashMap) DaoUtil.selectOne(sql, parameters);
			if (resultMap != null) {
				resultMap = BindingHelper.transferKeyToLowerCase(resultMap);
				if (subtract(latestVersion, (String) resultMap.get("latestversion")) > 0) {
					parameters.put("latestversion", year + month + day + hour + minute);
					parameters.put("update_date", new Date());
					sql = "com.hhh.platform.ops.sql.opsMapper.updateVersionRecord";
					DaoUtil.update(sql, parameters);
					isUpdate = true;
				}
			} else {
				parameters.put("latestversion", year + month + day + hour + minute);
				parameters.put("update_date", new Date());
				sql = "com.hhh.platform.ops.sql.opsMapper.insertUpdateRecord";
				DaoUtil.insert(sql, parameters);
				isUpdate = true;
			}
		} catch (Exception e) {
			log.error("校验是否初始化运维平台时错误", e);
		}
		return isUpdate;
	}

	/**
	 * 减法运算。
	 * 
	 * @param v1
	 * @param v2
	 * @return 两个参数的差
	 */
	public static int subtract(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).intValue();
	}

	public static String getUserNameAndAccount() {
		HttpSession session = RWT.getUISession().getHttpSession();
		if (session.getAttribute(FrameworkConstants.AUTHENTICATED_PROPERTY) != null
				&& session.getAttribute(FrameworkConstants.AUTHENTICATED_PROPERTY).equals(true)) {
			String userName = (String) session.getAttribute(FrameworkConstants.USER_NAME);

			String userAccount = (String) session.getAttribute(FrameworkConstants.USER_ACCOUNT);

			String adminId = (String) session.getAttribute(FrameworkConstants.ADMIN_ID);

			String adminName = (String) session.getAttribute(FrameworkConstants.ADMIN_NAME);

			String headString = (userName == null || userName.equals("") ? adminName : userName) + "("
					+ (adminId == null || adminId.equals("") ? userAccount : adminId) + ")";
			return headString;
		}
		return "";
	}
}
