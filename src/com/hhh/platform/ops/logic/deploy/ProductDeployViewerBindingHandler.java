package com.hhh.platform.ops.logic.deploy;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.EntityViewerBindingHandler;
import com.hhh.platform.advisors.page.PagePartionUtil;
import com.hhh.platform.advisors.paging.PagingPartionUtil;
import com.hhh.platform.ops.ui.deploy.ProductDeployViewerCreator;

public class ProductDeployViewerBindingHandler extends EntityViewerBindingHandler {

	private PagingPartionUtil pagingPartionUtil;

	private ProductDeployViewerCreator controlsCreator;

	private static String[] condition = new String[] { "产品名称", "网站名", "产品编号", "部署编号", "部署人" };

	private static String[] field = new String[] { "pd_name", "dp_name", "pd_code", "dp_code", "dp_user" };

	public boolean initControls() {
		initCombo();
		initDeployDate();
		initPagePart();
		return true;
	}

	private void initCombo() {
		CCombo cmbo_status = (CCombo) this.allBindedControls.get("con_select");
		if (cmbo_status != null) {
			cmbo_status.setItems(condition);
			cmbo_status.select(0);
			cmbo_status.setEditable(false);
		}
	}

	private void initDeployDate() {
		Calendar lastMonthDay = Calendar.getInstance();
		lastMonthDay.add(Calendar.MONTH, -3);
		Control wt_date_start_ctl = allBindedControls.get("dp_date_start");
		Control wt_date_end_ctl = allBindedControls.get("dp_date_end");
		if (wt_date_start_ctl instanceof DateTime && wt_date_end_ctl instanceof DateTime) {
			((DateTime) wt_date_start_ctl).setDay(lastMonthDay.get(Calendar.DAY_OF_MONTH));
			((DateTime) wt_date_start_ctl).setMonth(lastMonthDay.get(Calendar.MONTH));
			((DateTime) wt_date_start_ctl).setYear(lastMonthDay.get(Calendar.YEAR));
			((DateTime) wt_date_start_ctl).setHours(0);
			((DateTime) wt_date_start_ctl).setMinutes(0);
			((DateTime) wt_date_start_ctl).setSeconds(0);
			((DateTime) wt_date_end_ctl).setHours(23);
			((DateTime) wt_date_end_ctl).setMinutes(59);
			((DateTime) wt_date_end_ctl).setSeconds(59);
		}
	}

	private void initPagePart() {
		this.controlsCreator = (ProductDeployViewerCreator) controlsCreators.get(ProductDeployViewerCreator.class
				.getName());
		pagingPartionUtil = new PagingPartionUtil(this.controlsCreator.getViewer(),
				this.controlsCreator.getPagePartitionArea(), FrameworkConstants.PAGE_SIZE,
				"com.hhh.platform.ops.sql.ops_pd_deployMapper.countAllDeploy",
				"com.hhh.platform.ops.sql.ops_pd_deployMapper.selectAllDeploy",
				(Button) this.allBindedControls.get("check_all"), controlsCreator.getBindedFieldsNames());
		this.perform(SEARCH_ACTION);
	}

	@Override
	public void doSearch(HashMap<String, Object> paremeters) throws Exception {
		CCombo cmbo = (CCombo) this.allBindedControls.get("con_select");
		Text t = (Text) this.allBindedControls.get("con_t");
		int index = cmbo.getSelectionIndex();
		paremeters.put(field[index], t.getText());

		Map map = BindingHelper.getFiledsGroupByTable(controlsCreator.getBindedFieldsNames());
		Set<String> a = (Set<String>) map.get("a");
		Set<String> b = (Set<String>) map.get("b");

		String selectCulomnsA = BindingHelper.buildSelectColumnsWithPrefix(a, "a");
		String selectColumnsB = BindingHelper.buildSelectColumnsWithPrefix(b, "b");
		paremeters.put(FrameworkConstants.SELECTCOLUMNS, selectCulomnsA + "," + selectColumnsB);
		pagingPartionUtil.doSearch(paremeters);
	}

	public ProductDeployViewerCreator getControlsCreator() {
		return controlsCreator;
	}
}
