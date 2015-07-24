package com.hhh.platform.ops.logic.register;

import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.EntityViewerBindingHandler;
import com.hhh.platform.advisors.page.PagePartionUtil;
import com.hhh.platform.advisors.paging.PagingPartionUtil;
import com.hhh.platform.ops.ui.register.ProductRegisterViewerCreator;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.SystemSettingHelper;

public class ProductRegisterViewerBindingHandler extends EntityViewerBindingHandler {

	private static Log log = LogFactory.getLog(ProductRegisterViewerBindingHandler.class);

	private PagingPartionUtil pagingPartionUtil;

	private ProductRegisterViewerCreator controlsCreator;

	private HashMap<String, Object> parameterMap;

	public boolean initControls() {
		try {
			initCombo();
			initPagePart();
		} catch (Exception e) {
			MessageUtil.showErrorDialog(e, log, "初始化异常", MessageUtil.ERROR_TYPE_OPERATION);
		}
		return true;
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

	private void initPagePart() {
		this.controlsCreator = (ProductRegisterViewerCreator) controlsCreators.get(ProductRegisterViewerCreator.class
				.getName());
		pagingPartionUtil = new PagingPartionUtil(this.controlsCreator.getViewer(),
				this.controlsCreator.getPagePartitionArea(), FrameworkConstants.PAGE_SIZE,
				"com.hhh.platform.ops.sql.ops_pd_registerMapper.countAllProduct",
				"com.hhh.platform.ops.sql.ops_pd_registerMapper.selectAllProduct",
				(Button) this.allBindedControls.get("check_all"), controlsCreator.getBindedFieldsNames());
		this.perform(SEARCH_ACTION);
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
		
		pagingPartionUtil.doSearch(paremeters);
		this.parameterMap = new HashMap<String, Object>(paremeters);
	}

	public HashMap<String, Object> getParameterMap() {
		return parameterMap;
	}

	public ProductRegisterViewerCreator getControlsCreator() {
		return controlsCreator;
	}
}
