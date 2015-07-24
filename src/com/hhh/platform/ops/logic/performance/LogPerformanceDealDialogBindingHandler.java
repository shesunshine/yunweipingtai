package com.hhh.platform.ops.logic.performance;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Control;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.advisors.table.CheckboxTableViewerWithDoubleClick;
import com.hhh.platform.advisors.table.TableModel;
import com.hhh.platform.advisors.table.TableRowModel;
import com.hhh.platform.ops.entity.LogPerformanceEntity;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.util.OPSUtil;

public class LogPerformanceDealDialogBindingHandler extends DefaultBindingHandler {
	private static Log log = LogFactory.getLog(LogPerformanceDealDialogBindingHandler.class);

	private String operationId;
	private LogPerformanceEntity entity;
	private HashMap<String, Object> savedValues = new HashMap<String, Object>();

	public LogPerformanceDealDialogBindingHandler(String operationId, LogPerformanceEntity entity) {
		this.operationId = operationId;
		this.entity = entity;
	}

	@Override
	public boolean initControls() {
		initStatusCombo();
		BindingHelper.setValue(this.allBindedControls.get("a.deal_user"), OPSUtil.getUserNameAndAccount());
		return true;
	}

	private void initStatusCombo() {
		CCombo cmbo_status = (CCombo) this.allBindedControls.get("a.deal_status");
		if (cmbo_status != null) {
			cmbo_status.setItems(OPSConstants.STATUS_ARRAY);
			cmbo_status.select(0);
			cmbo_status.setEditable(false);
		}
	}

	@Override
	public boolean perform(String action) {
		if (action.equals(BindingHandler.CONFIRM_ACTION) && this.validate(true)) {
			if (this.operationId.equals(OPSConstants.DEAL_LOG_PERFORMANCE)) {
				try {
					updateLog();
					return true;
				} catch (Exception e) {
					String message = "修改性能日志失败";
					MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				}
			}
		} else if (action.equals(BindingHandler.CANCEL_ACTION)) {
			return true;
		}
		return false;
	}

	private Map updateLog() throws Exception {
		HashMap<String, Object> newValues = BindingHelper.getLatestBindedValues(this.allBindedControls);
		HashMap<String, Object> updatedValues = BindingHelper.filterUnchanged(savedValues, newValues);
		filterSpecialValues(updatedValues);

		String updateValues = BindingHelper.buildUpdateValues(updatedValues, "a");
		String[] logIds = this.entity.getSelectedIDs();
		for (int selecedIndex = 0; selecedIndex < logIds.length; selecedIndex++) {

			Map parametersForApp = new HashMap();
			parametersForApp.put("log_id", logIds[selecedIndex]);
			parametersForApp.put(FrameworkConstants.UPDATEVALUES, updateValues);
			Map parametersForRecord = new HashMap();
			String sql = "com.hhh.platform.ops.sql.ops_log_performanceMapper.updateLog";
			DaoUtil.update(sql, parametersForApp);

			StructuredViewer viewer = entity.getViewer();
			TableViewer viewer1 = (CheckboxTableViewerWithDoubleClick) viewer;
			TableModel tableModel = (TableModel) viewer1.getInput();

			TableRowModel updateRow = (TableRowModel) this.entity.getSelectedElements()[selecedIndex];
			savedValues.putAll(updatedValues);
			updateRow.setData(savedValues);
			tableModel.updateTableRow(updateRow);
			viewer1.refresh(tableModel, true);
		}
		return updatedValues;
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
		if (fieldName.equals("a.mark")) {
			return BindingHelper.checkNotEmpty(ctl, "原因或处理方法不能为空");
		}
		return true;
	}

}
