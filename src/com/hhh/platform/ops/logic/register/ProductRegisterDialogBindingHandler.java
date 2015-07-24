package com.hhh.platform.ops.logic.register;

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
import com.hhh.platform.ops.entity.ProductRegisterEntity;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.util.OPSUtil;
import com.hhh.platform.ops.util.SystemSettingHelper;

public class ProductRegisterDialogBindingHandler extends DefaultBindingHandler {
	private static Log log = LogFactory.getLog(ProductRegisterDialogBindingHandler.class);

	private String operationId;
	private ProductRegisterEntity entity;
	private String currentRegisterId;
	private HashMap<String, Object> savedValues = new HashMap<String, Object>();

	public ProductRegisterDialogBindingHandler(String operationId, ProductRegisterEntity entity) {
		this.operationId = operationId;
		this.entity = entity;
	}

	@Override
	public boolean initControls() {
		try {
			initCombo();
			BindingHelper.setValue(this.allBindedControls.get("a.add_user"), OPSUtil.getUserNameAndAccount());
			if (this.operationId.equals(OPSConstants.ADD_PRODUCT_REGISTER)) {
			} else if (this.operationId.equals(OPSConstants.UPDATE_PRODUCT_REGISTER)
					|| this.operationId.equals(OPSConstants.READ_PRODUCT_REGISTER)) {

				this.disableControl("a.code");

				currentRegisterId = this.entity.getSelectedIDs()[0];
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("re_id", currentRegisterId);

				String selectColumns = BindingHelper.buildSelectColumns(this.allBindedControls.keySet(), "a");
				parameters.put(FrameworkConstants.SELECTCOLUMNS, selectColumns);

				String sql = "com.hhh.platform.ops.sql.ops_pd_registerMapper.selectProduct";
				Map appMap = DaoUtil.selectOne(sql, parameters);

				if (appMap.size() > 0) {
					appMap = BindingHelper.getResultsWithTableInfo((HashMap<String, Object>) appMap, "a");
					BindingHelper.setBindedValues(this.allBindedControls, (HashMap<String, Object>) appMap);
					this.savedValues.putAll(appMap);
					this.savedValues.put("a.re_id", currentRegisterId);
				}

				if (this.operationId.equals(OPSConstants.READ_PRODUCT_REGISTER)) {
					this.disableAll();
				}

			}
		} catch (Exception e) {
			String message = "初始化产品登记信息发生异常";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return false;
		}
		return true;
	}

	@Override
	public boolean perform(String action) {
		if (action.equals(BindingHandler.CONFIRM_ACTION) && this.validate(true) && checkCode() && checkName()) {
			if (this.operationId.equals(OPSConstants.ADD_PRODUCT_REGISTER)) {
				try {
					String re_id = insertApp();
					String content = "新增产品登记信息，唯一编号为:" + re_id;
					return true;
				} catch (Exception e) {
					String message = "新增产品登记信息失败";
					MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				}
			} else if (this.operationId.equals(OPSConstants.UPDATE_PRODUCT_REGISTER)) {
				try {
					HashMap<String, Object> oldValues = new HashMap<String, Object>();
					oldValues.putAll(savedValues);
					Map updateValues = updateApp();
					return true;
				} catch (Exception e) {
					String message = "修改产品登记信息失败";
					MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				}
			}
		} else if (action.equals(BindingHandler.CANCEL_ACTION)) {
			return true;
		}
		return false;
	}

	private void initCombo() throws Exception {
		CCombo cmbo_yw_type = (CCombo) this.allBindedControls.get("a.yw_type");
		if (cmbo_yw_type != null) {
			cmbo_yw_type.setItems(SystemSettingHelper.getYwType());
			cmbo_yw_type.select(0);
			cmbo_yw_type.setEditable(false);
		}

		CCombo cmbo_dm_type = (CCombo) this.allBindedControls.get("a.dm_type");
		if (cmbo_dm_type != null) {
			cmbo_dm_type.setItems(SystemSettingHelper.getDmType());
			cmbo_dm_type.select(0);
			cmbo_dm_type.setEditable(false);
		}
	}

	private String insertApp() throws Exception {

		HashMap<String, Object> insertValues = BindingHelper.getLatestBindedValues(this.allBindedControls);
		
		//TODO:特殊处理
		insertValues.remove("a.code");
		
		String[] insertColumnsAndValues = BindingHelper.buildInsertColumnsAndValues(insertValues, "a");
		Map parameters = new HashMap();
		parameters.put(FrameworkConstants.INSERTCOLUMNS, insertColumnsAndValues[0]);
		parameters.put(FrameworkConstants.INSERTVALUES, insertColumnsAndValues[1]);

		String sql = "com.hhh.platform.ops.sql.ops_pd_registerMapper.insertProduct";
		DaoUtil.insert(sql, parameters, "re_id");

		insertValues.put("a.re_id", parameters.get("re_id"));
		insertValues.put("a.code", parameters.get("re_id"));
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
		return (String) parameters.get("re_id");
	}

	private Map updateApp() throws Exception {
		HashMap<String, Object> newValues = BindingHelper.getLatestBindedValues(this.allBindedControls);
		HashMap<String, Object> updatedValues = BindingHelper.filterUnchanged(savedValues, newValues);
		filterSpecialValues(updatedValues);
		if (updatedValues.size() > 0) {
			String updateValues = BindingHelper.buildUpdateValues(updatedValues, "a");
			Map parametersForApp = new HashMap();
			parametersForApp.put("re_id", this.currentRegisterId);
			parametersForApp.put(FrameworkConstants.UPDATEVALUES, updateValues);

			Map parametersForRecord = new HashMap();
			if (updatedValues.containsKey("a.name")) {
				parametersForRecord.put("a.app_name", updatedValues.get("a.name"));
				String updateValuesForRecord = BindingHelper.buildUpdateValues(
						(HashMap<String, Object>) parametersForRecord, "a");
				parametersForRecord.put("re_id", this.currentRegisterId);
				parametersForRecord.put(FrameworkConstants.UPDATEVALUES, updateValuesForRecord);
				String[] sqls = { "com.hhh.platform.ops.sql.ops_pd_registerMapper.updateProduct"
				// ,"com.hhh.platform.ops.sql.dc_app_using_recordMapper.updateRecord"
				};
				Map[] parameters = { parametersForApp
				// , parametersForRecord
				};
				DaoUtil.update(sqls, parameters);
			} else {
				String sql = "com.hhh.platform.ops.sql.ops_pd_registerMapper.updateProduct";
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
	}

	private boolean checkCode() {
		String code = (String) BindingHelper.getValue(this.allBindedControls.get("a.code"));

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("code", code);
		try {
			Map<String, String> map = DaoUtil.selectOne(
					"com.hhh.platform.ops.sql.ops_pd_registerMapper.selectProductByCode", parameters);
			if (this.operationId.equals(OPSConstants.ADD_PRODUCT_REGISTER)) {
				if (map != null && !map.isEmpty()) {
					MessageUtil.showMessageDialog("产品编号已经被占用", MessageUtil.MESSAGE_DIALOG_TYPE_INFO);
					return false;
				}
			} else if (this.operationId.equals(OPSConstants.UPDATE_PRODUCT_REGISTER)) {
				String code_old = (String) this.savedValues.get("a.code");

				if (!code_old.equals(code)) {
					if (map != null && !map.isEmpty()) {
						MessageUtil.showMessageDialog("产品编号已经被占用", MessageUtil.MESSAGE_DIALOG_TYPE_INFO);
						return false;
					}
				}
			}

		} catch (Exception e) {
			String message = "根据编号查询应用失败";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return false;
		}
		return true;
	}

	private boolean checkName() {
		String name = (String) BindingHelper.getValue(this.allBindedControls.get("a.name"));

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("name", (String) BindingHelper.getValue(this.allBindedControls.get("a.name")));
		try {
			Map<String, String> map = DaoUtil.selectOne(
					"com.hhh.platform.ops.sql.ops_pd_registerMapper.selectProductByName", parameters);
			if (this.operationId.equals(OPSConstants.ADD_PRODUCT_REGISTER)) {
				if (map != null && !map.isEmpty()) {
					MessageUtil.showMessageDialog("产品名称已经被占用", MessageUtil.MESSAGE_DIALOG_TYPE_INFO);
					return false;
				}
			} else if (this.operationId.equals(OPSConstants.UPDATE_PRODUCT_REGISTER)) {
				String name_old = (String) this.savedValues.get("a.name");

				if (!name_old.equals(name)) {
					if (map != null && !map.isEmpty()) {
						MessageUtil.showMessageDialog("产品名称已经被占用", MessageUtil.MESSAGE_DIALOG_TYPE_INFO);
						return false;
					}
				}
			}
		} catch (Exception e) {
			String message = "根据名称查询应用失败";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return false;
		}
		return true;
	}

}
