package com.hhh.platform.ops.logic.run;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Control;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.advisors.table.CheckboxTableViewerWithDoubleClick;
import com.hhh.platform.advisors.table.TableModel;
import com.hhh.platform.advisors.table.TableRowModel;
import com.hhh.platform.ops.entity.LogRunEntity;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSConstants;

public class LogRunDialogBindingHandler extends DefaultBindingHandler {
	private static Log log = LogFactory.getLog(LogRunDialogBindingHandler.class);

	private String operationId;
	private LogRunEntity entity;
	private String currentLogId;
	private HashMap<String, Object> savedValues = new HashMap<String, Object>();

	public LogRunDialogBindingHandler(String operationId, LogRunEntity entity) {
		this.operationId = operationId;
		this.entity = entity;
	}

	@Override
	public boolean initControls() {
		try {
			currentLogId = this.entity.getSelectedIDs()[0];
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("log_id", currentLogId);

			String selectColumns = BindingHelper.buildSelectColumns(this.allBindedControls.keySet(), "a");
			parameters.put(FrameworkConstants.SELECTCOLUMNS, selectColumns);

			String sql = "com.hhh.platform.ops.sql.ops_log_runMapper.selectLog";
			Map logMap = DaoUtil.selectOne(sql, parameters);

			if (logMap.size() > 0) {
				logMap = BindingHelper.getResultsWithTableInfo((HashMap<String, Object>) logMap, "a");
				
				String re_id = (String) logMap.get("a.re_id");
				String dp_id = (String) logMap.get("a.dp_id");
				parameters.put("re_id", re_id);
				parameters.put("dp_id", dp_id);
				Map product = DaoUtil.selectOne("com.hhh.platform.ops.sql.ops_pd_registerMapper.selectProduct2", parameters);
				logMap.put("a.pd_code", product.get("code"));
				logMap.put("a.pd_name", product.get("name"));
				Map deploy = DaoUtil.selectOne("com.hhh.platform.ops.sql.ops_pd_deployMapper.selectDeploy2", parameters);
				logMap.put("a.dp_name", deploy.get("dp_name"));

				
				BindingHelper.setBindedValues(this.allBindedControls, (HashMap<String, Object>) logMap);
				this.savedValues.putAll(logMap);
				this.savedValues.put("a.log_id", currentLogId);
			}

			this.disableAll();
		} catch (Exception e) {
			String message = "初始化运行监控日志信息发生异常";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return false;
		}
		return true;
	}

	@Override
	public boolean perform(String action) {
		return true;
	}



	@Override
	public void focusLost(String fieldName, Control ctl) {
	}

	@Override
	public void widgetSelected(String fieldName, Control ctl) {
	}

	@Override
	public boolean veryfyField(String fieldName, Control ctl, boolean force) {
		return true;
	}



}
