package com.hhh.platform.ops.logic.server;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Control;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.advisors.table.CheckboxTableViewerWithDoubleClick;
import com.hhh.platform.advisors.table.TableModel;
import com.hhh.platform.advisors.table.TableRowModel;
import com.hhh.platform.ops.entity.ServerRegisterEntity;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.util.OPSUtil;
import com.hhh.platform.ops.util.SystemSettingHelper;

public class ServerRegisterDialogBindingHandler extends DefaultBindingHandler {
	private static Log log = LogFactory.getLog(ServerRegisterDialogBindingHandler.class);

	private String operationId;
	private ServerRegisterEntity entity;
	private String currentRegisterId;
	private HashMap<String, Object> savedValues = new HashMap<String, Object>();

	public ServerRegisterDialogBindingHandler(String operationId, ServerRegisterEntity entity) {
		this.operationId = operationId;
		this.entity = entity;
	}
	
	public ServerRegisterDialogBindingHandler(String operationId, String server_id) {
		this.operationId = operationId;
		this.currentRegisterId = server_id;
	}

	@Override
	public boolean initControls() {
		try {
			initCombo();
			BindingHelper.setValue(this.allBindedControls.get("a.add_user"), OPSUtil.getUserNameAndAccount());
			if (this.operationId.equals(OPSConstants.ADD_SERVER_REGISTER)) {
			} else if (this.operationId.equals(OPSConstants.UPDATE_SERVER_REGISTER) || this.operationId.equals(OPSConstants.READ_SERVER_REGISTER)) {
				if (this.entity != null) {
					currentRegisterId = this.entity.getSelectedIDs()[0];
				}
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("server_id", currentRegisterId);

				String selectColumns = BindingHelper.buildSelectColumns(this.allBindedControls.keySet(), "a");
				parameters.put(FrameworkConstants.SELECTCOLUMNS, selectColumns);

				String sql = "com.hhh.platform.ops.sql.ops_server_registerMapper.selectServer";
				Map appMap = DaoUtil.selectOne(sql, parameters);

				if (appMap.size() > 0) {
					appMap = BindingHelper.getResultsWithTableInfo((HashMap<String, Object>) appMap, "a");
					appMap.put("server_id", currentRegisterId);
					BindingHelper.setBindedValues(this.allBindedControls, (HashMap<String, Object>) appMap);
					this.savedValues.putAll(appMap);
					this.savedValues.put("a.server_id", currentRegisterId);
					
					String is_watch = (String) appMap.get("a.is_watch");
					if (OPSConstants.YES.equals(is_watch)) {
						((Button)this.allBindedControls.get("is_watch_select")).setSelection(true);
					}else {
						((Button)this.allBindedControls.get("is_watch_select")).setSelection(false);
					}
				}

				if (this.operationId.equals(OPSConstants.READ_SERVER_REGISTER)) {
					this.disableAll();
				}

			}
		} catch (Exception e) {
			String message = "初始化服务器登记信息发生异常";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return false;
		}
		return true;
	}

	@Override
	public boolean perform(String action) {
		if (action.equals(BindingHandler.CONFIRM_ACTION) && this.validate(true)) {
			if (this.operationId.equals(OPSConstants.ADD_SERVER_REGISTER)) {
				try {
					String server_id = insertServer();
					return true;
				} catch (Exception e) {
					String message = "新增服务器登记信息失败";
					MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				}
			} else if (this.operationId.equals(OPSConstants.UPDATE_SERVER_REGISTER)) {
				try {
					HashMap<String, Object> oldValues = new HashMap<String, Object>();
					oldValues.putAll(savedValues);
					Map updateValues = updateServer();
					return true;
				} catch (Exception e) {
					String message = "修改服务器登记信息失败";
					MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				}
			}
		} else if (action.equals(BindingHandler.CANCEL_ACTION)) {
			return true;
		}
		return false;
	}

	private void initCombo() throws Exception {
		CCombo cmbo_use_type = (CCombo) this.allBindedControls.get("a.use_type");
		String[] use_type = new String[] { SystemSettingHelper.OPS_SERVER_USE_TYPE_WEB, SystemSettingHelper.OPS_SERVER_USE_TYPE_DB, SystemSettingHelper.OPS_SERVER_USE_TYPE_WEB_DB };
		if (cmbo_use_type != null) {
			cmbo_use_type.setItems(use_type);
			cmbo_use_type.select(0);
			cmbo_use_type.setEditable(false);
		}

		CCombo cmbo_p_type = (CCombo) this.allBindedControls.get("a.p_type");
		String[] p_type = new String[] { SystemSettingHelper.OPS_SERVER_P_TYPE_1, SystemSettingHelper.OPS_SERVER_P_TYPE_2, SystemSettingHelper.OPS_SERVER_P_TYPE_3,
				SystemSettingHelper.OPS_SERVER_P_TYPE_4 };
		if (cmbo_p_type != null) {
			cmbo_p_type.setItems(p_type);
			cmbo_p_type.select(1);
			cmbo_p_type.setEditable(false);
		}

		CCombo cmbo_p_structure = (CCombo) this.allBindedControls.get("a.p_structure");
		String[] p_structure = new String[] { "1U", "2U", "3U", "4U", "5U", "6U", "7U" };
		if (cmbo_p_structure != null) {
			cmbo_p_structure.setItems(p_structure);
			cmbo_p_structure.select(1);
			cmbo_p_structure.setEditable(false);
		}

		CCombo cmbo_cpu_core_num = (CCombo) this.allBindedControls.get("a.cpu_core_num");
		String[] cpu_core_num = new String[] { "双核", "四核", "六核", "八核", "十核", "十二核" };
		if (cmbo_cpu_core_num != null) {
			cmbo_cpu_core_num.setItems(cpu_core_num);
			cmbo_cpu_core_num.select(1);
			cmbo_cpu_core_num.setEditable(false);
		}

		CCombo cmbo_ram_type = (CCombo) this.allBindedControls.get("a.ram_type");
		String[] ram_type = new String[] { "DD2", "DD3", "DD4" };
		if (cmbo_ram_type != null) {
			cmbo_ram_type.setItems(ram_type);
			cmbo_ram_type.select(1);
			cmbo_ram_type.setEditable(true);
		}

		CCombo cmbo_hdd_port = (CCombo) this.allBindedControls.get("a.hdd_port");
		String[] hdd_port = new String[] { "IDE", "SATA", "SCSI", "FC", "SAS" };
		if (cmbo_hdd_port != null) {
			cmbo_hdd_port.setItems(hdd_port);
			cmbo_hdd_port.select(1);
			cmbo_hdd_port.setEditable(false);
		}

		CCombo cmbo_os_type = (CCombo) this.allBindedControls.get("a.os_type");
		String[] os_type = new String[] { SystemSettingHelper.OPS_SERVER_OS_TYPE_WINDOWS, SystemSettingHelper.OPS_SERVER_OS_TYPE_LINUX, SystemSettingHelper.OPS_SERVER_OS_TYPE_UNIX };
		if (cmbo_os_type != null) {
			cmbo_os_type.setItems(os_type);
			cmbo_os_type.select(0);
			cmbo_os_type.setEditable(false);
		}
	}

	private String insertServer() throws Exception {
		HashMap<String, Object> insertValues = BindingHelper.getLatestBindedValues(this.allBindedControls);
		String[] insertColumnsAndValues = BindingHelper.buildInsertColumnsAndValues(insertValues, "a");
		Map parameters = new HashMap();
		parameters.put(FrameworkConstants.INSERTCOLUMNS, insertColumnsAndValues[0]);
		parameters.put(FrameworkConstants.INSERTVALUES, insertColumnsAndValues[1]);

		String sql = "com.hhh.platform.ops.sql.ops_server_registerMapper.insertServer";
		DaoUtil.insert(sql, parameters, "server_id");
		insertValues.put("a.server_id", parameters.get("server_id"));
		StructuredViewer viewer = this.entity.getViewer();
		if (viewer instanceof CheckboxTableViewerWithDoubleClick) {
			TableViewer viewer1 = (CheckboxTableViewerWithDoubleClick) viewer;
			TableRowModel newrow = new TableRowModel(insertValues);
			TableModel input = (TableModel) viewer1.getInput();
			if (input == null) {
				input = new TableModel(null);
				viewer1.setInput(input);
			}
			input.addTableRow(newrow);
			viewer.refresh();
			entity.setSelection(newrow);
		}
		return (String) parameters.get("server_id");
	}

	private Map updateServer() throws Exception {
		HashMap<String, Object> newValues = BindingHelper.getLatestBindedValues(this.allBindedControls);
		HashMap<String, Object> updatedValues = BindingHelper.filterUnchanged(savedValues, newValues);
		filterSpecialValues(updatedValues);
		if (updatedValues.size() > 0) {
			String updateValues = BindingHelper.buildUpdateValues(updatedValues, "a");
			Map parametersForApp = new HashMap();
			parametersForApp.put("server_id", this.currentRegisterId);
			parametersForApp.put(FrameworkConstants.UPDATEVALUES, updateValues);

			Map parametersForRecord = new HashMap();
			if (updatedValues.containsKey("a.name")) {
				String updateValuesForRecord = BindingHelper.buildUpdateValues((HashMap<String, Object>) parametersForRecord, "a");
				parametersForRecord.put("server_id", this.currentRegisterId);
				parametersForRecord.put(FrameworkConstants.UPDATEVALUES, updateValuesForRecord);
				String[] sqls = { "com.hhh.platform.ops.sql.ops_server_registerMapper.updateServer"
				// ,"com.hhh.platform.ops.sql.dc_app_using_recordMapper.updateRecord"
				};
				Map[] parameters = { parametersForApp
				// , parametersForRecord
				};
				DaoUtil.update(sqls, parameters);
			} else {
				String sql = "com.hhh.platform.ops.sql.ops_server_registerMapper.updateServer";
				DaoUtil.update(sql, parametersForApp);
			}

			StructuredViewer viewer = entity.getViewer();
			if (viewer instanceof CheckboxTableViewerWithDoubleClick) {
				TableViewer viewer1 = (CheckboxTableViewerWithDoubleClick) viewer;
				TableModel tableModel = (TableModel) viewer1.getInput();
				int index = -1;

				String[] ids = this.entity.getSelectedIDs();
				for (int i = 0; i < ids.length; i++) {
					String id = ids[i];
					String entityId = (String) savedValues.get(this.entity.getEntityId());
					if (entityId.equals(id)) {
						index = i;
					}
				}
				if (index != -1) {
					TableRowModel updateRow = (TableRowModel) this.entity.getSelectedElements()[index];
					savedValues.putAll(updatedValues);
					updateRow.setData(savedValues);
					tableModel.updateTableRow(updateRow);
					viewer1.refresh(tableModel, true);
				}
			}
		}
		return updatedValues;
	}

	@Override
	public void focusLost(String fieldName, Control ctl) {
	}

	@Override
	public void widgetSelected(String fieldName, Control ctl) {
		if (fieldName.equals("is_watch_select")) {
			boolean flag = ((Button)ctl).getSelection();
			if (flag) {
				BindingHelper.setValue(this.allBindedControls.get("a.is_watch"), "是");
			}else {
				BindingHelper.setValue(this.allBindedControls.get("a.is_watch"), "否");
			}
		}
	}

	@Override
	protected void filterSpecialValues(HashMap<String, Object> updateValues) {
		updateValues.remove("is_watch_select");
	}
}
