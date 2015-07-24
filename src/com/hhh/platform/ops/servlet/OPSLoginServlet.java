package com.hhh.platform.ops.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hhh.platform.advisors.framework.AppInfo;
import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.OPSAppConstants;
import com.hhh.platform.util.MD5Util;

public class OPSLoginServlet extends HttpServlet {
	private static final long serialVersionUID = 3293524791763698003L;
	private static final Log log = LogFactory.getLog(OPSLoginServlet.class);
	private static final int MAX_INACTIVE = 60 * 60;// 单位：秒

	private static final String APP_LABEL = "运维平台";

	@Override
	public void init() throws ServletException {
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.doPost(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		session.invalidate();
		req.getSession(true);
		try {
			initUrls(req);
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			resp.setContentType("text/html; charset=UTF-8");
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			ceateHtmlForFailure(e.getMessage(), req, resp);
			return;
		}
		dologin(req, resp);
	}

	/**
	 * 计算平台管理中心登录、首页的URL,保存在请求的属性中，供后续使用
	 * 
	 * @param req
	 * 
	 * @throws Exception
	 */
	private void initUrls(HttpServletRequest req) throws Exception {
		String loginUrl = req.getRequestURL().toString();
		String opsUrl = req.getRequestURL().toString();
		String requestURL = req.getRequestURL().toString();
		String contextPath = req.getContextPath();
		// TODO:处理jSessionID
		int servletBeginPossition = 0;
		if (contextPath.length() > 0) {
			servletBeginPossition = requestURL.indexOf(contextPath) + contextPath.length();
		} else {
			servletBeginPossition = requestURL.lastIndexOf(req.getRequestURI());
		}
		loginUrl = requestURL.substring(0, servletBeginPossition) + "/ops/" + FrameworkConstants.LOGIN;
		opsUrl = requestURL.substring(0, servletBeginPossition) + "/ops";
		req.setAttribute(FrameworkConstants.LOGIN, loginUrl);
		req.setAttribute("ops", opsUrl);
	}

	private void ceateHtmlForFailure(String message, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		StringBuilder htmlStringBuilder = new StringBuilder();
		createLoginHtml(req, htmlStringBuilder);
		htmlStringBuilder.append("<script language='javascript' type='text/javascript'>");
		htmlStringBuilder.append("alert('" + message + "');");
		htmlStringBuilder.append("</script>");
		htmlStringBuilder.append("</body>");
		htmlStringBuilder.append("</html>");
		resp.getWriter().println(htmlStringBuilder);
		resp.getWriter().flush();
		htmlStringBuilder = null;
	}

	private void dologin(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		Map userMap = null;
		Map adminMap = null;

		if (req.getParameter("userAccountOrAdminId") != null && req.getParameter("password") != null) {
			String userAccountOrAdminId = req.getParameter("userAccountOrAdminId");
			String password = MD5Util.getMD5(req.getParameter("password"));
			try {
				adminMap = getAdminMap();
				if (adminMap == null) {
					throw new Exception("平台管理中心初始化数据不正确");
				}
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				String message = "访问数据库出错";
				ceateHtmlForFailure(message, req, resp);
				return;
			}
			try {
				userMap = getUserMap(userAccountOrAdminId);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				String message = "访问数据库出错";
				ceateHtmlForFailure(message, req, resp);
				return;
			}
			if (userMap != null) {
				String user_password = (String) userMap.get("user_pass");
				if (user_password != null && user_password.equals(password)) {
					userMap.put(FrameworkConstants.ORG_NAME, adminMap.get(FrameworkConstants.ORG_NAME));
					this.dealWhenSuccess(req, resp, userMap, false);
					return;
				} else {
					String message = "用户名或密码错误,请重新输入";
					ceateHtmlForFailure(message, req, resp);
					return;
				}
			} else {
				if (adminMap != null) {
					String admin_password = (String) adminMap.get("admin_pass");
					String admin_id = (String) adminMap.get("admin_id");
					if (admin_password != null && admin_password.equals(password) && admin_id != null
							&& admin_id.equals(userAccountOrAdminId)) {
						this.dealWhenSuccess(req, resp, adminMap, true);
						return;
					} else {
						String message = "用户名或密码错误,请重新输入";
						ceateHtmlForFailure(message, req, resp);
						return;
					}
				}
			}
		} else {
			StringBuilder htmlStringBuilder = new StringBuilder();
			createLoginHtml(req, htmlStringBuilder);
			htmlStringBuilder.append("</body>");
			htmlStringBuilder.append("</html>");
			resp.getWriter().println(htmlStringBuilder);
			resp.getWriter().flush();
			htmlStringBuilder = null;
		}

	}

	private Map getAdminMap() throws Exception {
		Map parametersMap = new HashMap();
		Map resultMap = null;
		parametersMap.put(FrameworkConstants.CUSTOMER_ID, "100000");
		String sql = "com.hhh.platform.ops.sql.opsMapper.selectCustomerByCustomerId";
		resultMap = DaoUtil.selectOne(sql, parametersMap);
		return resultMap;
	}

	/**
	 * 登录成功后的业务处理
	 * 
	 * @param resp
	 * 
	 * @param map
	 * @param isAdmin
	 * @param htmlStringBuilder
	 * @throws Exception
	 */
	private void dealWhenSuccess(HttpServletRequest req, HttpServletResponse resp, Map map, boolean isAdmin)
			throws IOException {
		String customerId = map.get(FrameworkConstants.CUSTOMER_ID).toString();
		String userAccount = map.get(FrameworkConstants.USER_ACCOUNT) == null ? "" : map.get(
				FrameworkConstants.USER_ACCOUNT).toString();
		String adminId = map.get(FrameworkConstants.ADMIN_ID) == null ? "" : map.get(FrameworkConstants.ADMIN_ID)
				.toString();
		String adminName = map.get(FrameworkConstants.ADMIN_NAME) == null ? "" : map.get(FrameworkConstants.ADMIN_NAME)
				.toString();
		String orgName = map.get(FrameworkConstants.ORG_NAME) == null ? "" : map.get(FrameworkConstants.ORG_NAME)
				.toString();
		String userName = map.get(FrameworkConstants.USER_NAME) == null ? "" : map.get(FrameworkConstants.USER_NAME)
				.toString();

		String appUrl = (String) req.getAttribute(OPSAppConstants.APP_NAME_OPS);
		HttpSession session = req.getSession(false);
		List<String> serviceIDList = new ArrayList<String>();
		List list = new ArrayList();
		try {
			if (!isAdmin) {
				Map parametersMap = new HashMap();
				parametersMap.put(FrameworkConstants.USER_ACCOUNT, userAccount);
				parametersMap.put(FrameworkConstants.CUSTOMER_ID, customerId);
				parametersMap.put(FrameworkConstants.CURRENT_APPNAME, OPSAppConstants.APP_NAME_OPS);
				parametersMap.put("user_ser_status", "已激活");

				String sql = "com.hhh.platform.ops.sql.opsMapper.selectByUserAccount_CustomerId_UserSerStatus";
				list = DaoUtil.selectList(sql, parametersMap);
			} else {
				Map parametersMap = new HashMap();
				parametersMap.put(FrameworkConstants.ADMIN_ID, adminId);
				parametersMap.put(FrameworkConstants.CUSTOMER_ID, customerId);
				parametersMap.put("ser_status", "已激活");
				parametersMap.put(FrameworkConstants.CURRENT_APPNAME, OPSAppConstants.APP_NAME_OPS);

				String sql = "com.hhh.platform.ops.sql.opsMapper.selectTopOrgSerByCustomerId_SerStatus";
				list = DaoUtil.selectList(sql, parametersMap);
			}
		} catch (Exception e) {
			String message = "访问数据库出错";
			ceateHtmlForFailure(message, req, resp);
			return;
		}

		for (int i = 0; i < list.size(); i++) {
			Map service = (Map) list.get(i);
			String realId = service.get("real_id").toString();
			serviceIDList.add(realId);
		}
		AppInfo app = new AppInfo();
		app.setAppname(OPSAppConstants.APP_NAME_OPS);
		app.setLabel(APP_LABEL);
		app.setServiceRealIdList(serviceIDList);

		Map<String, AppInfo> appMap = new HashMap<String, AppInfo>();
		appMap.put(OPSAppConstants.APP_NAME_OPS, app);
		session.setAttribute(FrameworkConstants.APPS, appMap);

		session.setAttribute(FrameworkConstants.SERVLET_NAME, OPSAppConstants.APP_NAME_OPS);
		session.setAttribute(FrameworkConstants.CURRENT_APPNAME, OPSAppConstants.APP_NAME_OPS);
		session.setAttribute(FrameworkConstants.APPNAMES, new String[] { OPSAppConstants.APP_NAME_OPS });
		session.setAttribute(FrameworkConstants.ADMIN_NAME, adminName);
		session.setAttribute(FrameworkConstants.ADMIN_ID, adminId);
		session.setAttribute(FrameworkConstants.IS_ADMIN, isAdmin);
		session.setAttribute(FrameworkConstants.CUSTOMER_ID, customerId);
		session.setAttribute(FrameworkConstants.USER_ACCOUNT, userAccount);
		session.setAttribute(FrameworkConstants.USER_NAME, userName);
		session.setAttribute(FrameworkConstants.ORG_NAME, orgName);
		session.setAttribute(FrameworkConstants.SERVICE_LABEL, APP_LABEL);
		session.setAttribute(FrameworkConstants.LABELS, new String[] { APP_LABEL });
		session.setAttribute(FrameworkConstants.AUTHENTICATED_PROPERTY, true);
		session.setMaxInactiveInterval(MAX_INACTIVE);
		createHtmlToApps(appUrl, resp);
	}

	private Map getUserMap(String userAccount) throws Exception {
		Map parametersMap = new HashMap();
		parametersMap.put(FrameworkConstants.USER_ACCOUNT, userAccount);
		parametersMap.put(FrameworkConstants.CUSTOMER_ID, "100000");
		String sql = "com.hhh.platform.ops.sql.opsMapper.selectUserByUserAccountAndCustomerId";
		Map userMap = DaoUtil.selectOne(sql, parametersMap);
		return userMap;
	}

	/**
	 * 登录成功后，直接跳到应用的跳转页面
	 * 
	 * @param htmlStringBuilder
	 * @param tid
	 * @param urls
	 * @param resp
	 * @throws IOException
	 */
	private void createHtmlToApps(String urls, HttpServletResponse resp) throws IOException {
		StringBuilder htmlStringBuilder = new StringBuilder();
		htmlStringBuilder.append("<html>\n");
		htmlStringBuilder.append("<head>\n");
		htmlStringBuilder.append("<script language='javascript' type='text/javascript'>\n");
		htmlStringBuilder.append("function redirect(url){\n");
		htmlStringBuilder.append("var form = document.getElementById('form1');\n");
		htmlStringBuilder.append("form.action = url;\n");
		htmlStringBuilder.append("form.submit();\n");
		htmlStringBuilder.append("}\n");
		htmlStringBuilder.append("</script>\n");
		htmlStringBuilder.append("<title>登录成功</title>\n");
		htmlStringBuilder.append("</head>\n");
		htmlStringBuilder.append("<body onload= \"redirect('" + urls + "')\">\n");
		htmlStringBuilder.append("<form id='form1' action='" + urls + "' method='get'>\n");
		htmlStringBuilder.append("<table width='100%' height='100%' border='0' cellpadding='0' cellspacing='0'>\n");
		htmlStringBuilder.append("<tr>\n");
		htmlStringBuilder.append("<td style='height:50px' bgcolor='#87CEFA'>&nbsp;</td>\n");
		htmlStringBuilder.append("</tr>\n");
		htmlStringBuilder.append("<tr>\n");
		htmlStringBuilder.append("<td align='center' bgcolor='#F0F8FF' valign = 'top'>\n");
		htmlStringBuilder.append("</br>\n");
		htmlStringBuilder.append("</br>\n");
		htmlStringBuilder.append("</br>\n");
		htmlStringBuilder.append("</br>\n");
		htmlStringBuilder.append("</br>\n");
		htmlStringBuilder.append("<h1>登录成功，请稍后……</h1>\n");
		htmlStringBuilder.append("<input type='hidden' name='tid' value = '" + System.currentTimeMillis() + "' />\n");
		htmlStringBuilder.append("</td>\n");
		htmlStringBuilder.append("</tr>\n");
		htmlStringBuilder.append("<tr>\n");
		htmlStringBuilder.append("<td style='height:30px' bgcolor='#87CEFA'>&nbsp;</td>\n");
		htmlStringBuilder.append("</tr>\n");
		htmlStringBuilder.append("</table>\n");
		htmlStringBuilder.append("</form>\n");
		htmlStringBuilder.append("</body>");
		htmlStringBuilder.append("</html>");
		resp.getWriter().println(htmlStringBuilder);
		resp.getWriter().flush();
		htmlStringBuilder = null;
	}

	/**
	 * 初始化登录界面
	 * 
	 * @param htmlStringBuilder
	 * @param portalUrl
	 */
	private void createLoginHtml(HttpServletRequest req, StringBuilder htmlStringBuilder) {
		htmlStringBuilder.append("<html>\n");
		htmlStringBuilder.append("<head>\n");
		htmlStringBuilder.append("<title>运维平台</title>\n");
		htmlStringBuilder.append("<link href='/ops/images/login.css' rel='stylesheet' type='text/css'/>");
		htmlStringBuilder.append("<script language='javascript' type='text/javascript'>\n");
		htmlStringBuilder.append("function dologin(){\n");
		htmlStringBuilder
				.append("if(document.getElementById('userAccountOrAdminId').value==''){alert('用户账号不能为空'); return false;}\n");
		htmlStringBuilder
				.append("if(document.getElementById('password').value==''){alert('用户密码不能为空'); return false;}\n");
		htmlStringBuilder.append("document.getElementById('form1').submit();return true;}\n");
		htmlStringBuilder.append("</script>\n");

		htmlStringBuilder.append("</head>\n");

		htmlStringBuilder.append("<body>\n");
		htmlStringBuilder.append("<div id=\"base\">\n");
		htmlStringBuilder.append("<form id='form1' action='" + req.getAttribute(FrameworkConstants.LOGIN)
				+ "' method='post'>\n");
		htmlStringBuilder.append("<div id=\"form\">\n");
		htmlStringBuilder
				.append("<input  type='hidden' name='orgNameOrCustomerId' id='orgNameOrCustomerId' disabled='true' value='100000'/>\n");
		htmlStringBuilder
				.append("<input class=\"account\" type=\"text\" name='userAccountOrAdminId' id='userAccountOrAdminId'/>\n");
		htmlStringBuilder.append("<input class=\"password\" type=\"password\" name='password' id='password'/>\n");
		htmlStringBuilder.append("<input class=\"loginbtn\" type=\"submit\" value='' onClick='return dologin()'/>\n");
		htmlStringBuilder.append("</div>\n");
		htmlStringBuilder.append("</form>\n");
		htmlStringBuilder.append("</div>\n");
	}
}
