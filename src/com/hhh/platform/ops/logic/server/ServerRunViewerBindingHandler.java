package com.hhh.platform.ops.logic.server;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;

import com.hhh.platform.advisors.framework.binding.EntityViewerBindingHandler;
import com.hhh.platform.advisors.page.TableViewerCustomizer;
import com.hhh.platform.advisors.paging.PagingPartionUtil;
import com.hhh.platform.ops.ui.server.ServerRunViewerCreator;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.util.ServerRunViewerCustomizer;
import com.hhh.platform.ops.util.SystemSettingHelper;

public class ServerRunViewerBindingHandler extends EntityViewerBindingHandler {

	private static Log log = LogFactory.getLog(ServerRunViewerBindingHandler.class);

	private PagingPartionUtil pagingPartionUtil;

	private ServerRunViewerCreator controlsCreator;

	private HashMap<String, Object> parameterMap;

	private int pageSize = 20;

	public boolean initControls() {
		try {
			initPagePart();
			initCombo();
		} catch (Exception e) {
			MessageUtil.showErrorDialog(e, log, "初始化异常", MessageUtil.ERROR_TYPE_OPERATION);
		}
		return true;
	}

	private void initCombo() throws Exception {
		CCombo cmbo_use_type = (CCombo) this.allBindedControls.get("use_type");
		String[] use_type = new String[] { "", SystemSettingHelper.OPS_SERVER_USE_TYPE_WEB, SystemSettingHelper.OPS_SERVER_USE_TYPE_DB, SystemSettingHelper.OPS_SERVER_USE_TYPE_WEB_DB };
		if (cmbo_use_type != null) {
			cmbo_use_type.setItems(use_type);
			cmbo_use_type.select(0);
			cmbo_use_type.setEditable(false);
		}

		CCombo cmbo_p_type = (CCombo) this.allBindedControls.get("p_type");
		String[] p_type = new String[] { "", SystemSettingHelper.OPS_SERVER_P_TYPE_1, SystemSettingHelper.OPS_SERVER_P_TYPE_2, SystemSettingHelper.OPS_SERVER_P_TYPE_3,
				SystemSettingHelper.OPS_SERVER_P_TYPE_4 };
		if (cmbo_p_type != null) {
			cmbo_p_type.setItems(p_type);
			cmbo_p_type.select(0);
			cmbo_p_type.setEditable(false);
		}

		CCombo cmbo_os_type = (CCombo) this.allBindedControls.get("os_type");
		String[] os_type = new String[] { "", SystemSettingHelper.OPS_SERVER_OS_TYPE_WINDOWS, SystemSettingHelper.OPS_SERVER_OS_TYPE_LINUX, SystemSettingHelper.OPS_SERVER_OS_TYPE_UNIX };
		if (cmbo_os_type != null) {
			cmbo_os_type.setItems(os_type);
			cmbo_os_type.select(0);
			cmbo_os_type.setEditable(false);
		}

		CCombo cmbo_is_watch = (CCombo) this.allBindedControls.get("is_watch");
		String[] is_watch = new String[] { "", OPSConstants.YES, OPSConstants.NO };
		if (cmbo_is_watch != null) {
			cmbo_is_watch.setItems(is_watch);
			cmbo_is_watch.select(0);
			cmbo_is_watch.setEditable(false);
		}
	}

	private void initPagePart() {
		this.controlsCreator = (ServerRunViewerCreator) controlsCreators.get(ServerRunViewerCreator.class.getName());
		pagingPartionUtil = new PagingPartionUtil(this.controlsCreator.getViewer(), this.controlsCreator.getPagePartitionArea(), pageSize,
				"com.hhh.platform.ops.sql.ops_server_registerMapper.countAllServer", "com.hhh.platform.ops.sql.ops_server_registerMapper.selectAllServer",
				(Button) this.allBindedControls.get("check_all"), controlsCreator.getBindedFieldsNames());
		this.perform(SEARCH_ACTION);
	}

	@Override
	public void doSearch(HashMap<String, Object> paremeters) throws Exception {
		paremeters.put("is_watch", OPSConstants.YES);

		TableViewerCustomizer customizer = new ServerRunViewerCustomizer();
		pagingPartionUtil.setCustomizer(customizer);

		pagingPartionUtil.doSearch(paremeters);
		this.parameterMap = new HashMap<String, Object>(paremeters);
	}

	public HashMap<String, Object> getParameterMap() {
		return parameterMap;
	}

	public ServerRunViewerCreator getControlsCreator() {
		return controlsCreator;
	}
}
