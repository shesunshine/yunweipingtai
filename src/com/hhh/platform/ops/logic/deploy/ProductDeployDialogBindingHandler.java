package com.hhh.platform.ops.logic.deploy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.advisors.framework.binding.DefaultBindingListener;
import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreator;
import com.hhh.platform.advisors.table.CheckboxTableViewerWithDoubleClick;
import com.hhh.platform.advisors.table.TableContentProvider;
import com.hhh.platform.advisors.table.TableLabelProvider;
import com.hhh.platform.advisors.table.TableLabelProviderEditable;
import com.hhh.platform.advisors.table.TableModel;
import com.hhh.platform.advisors.table.TableRowModel;
import com.hhh.platform.ops.entity.ProductDeployEntity;
import com.hhh.platform.ops.logic.server.ServerSelectDialogBindingHandler;
import com.hhh.platform.ops.ui.deploy.ProductSelectDialogCreator;
import com.hhh.platform.ops.ui.server.ServerRegisterDialog;
import com.hhh.platform.ops.ui.server.ServerRunViewerTableLabelProvider;
import com.hhh.platform.ops.ui.server.ServerSelectDialogCreator;
import com.hhh.platform.ops.util.ContainPagingControlsCreatorBindingHandler;
import com.hhh.platform.ops.util.ContainPagingDialog;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.util.OPSUtil;
import com.hhh.platform.orm.mybatis.SQLSessionService;
import com.hhh.platform.orm.mybatis.SQLSessionServiceUtil;
import com.hhh.platform.util.UUIDUitil;

public class ProductDeployDialogBindingHandler extends DefaultBindingHandler {
	private static Log log = LogFactory.getLog(ProductDeployDialogBindingHandler.class);

	private String operationId;
	private ProductDeployEntity entity;
	private String currentDeployId;
	private HashMap<String, Object> savedValues = new HashMap<String, Object>();
	private TableViewer serverViewer;
	private boolean isChangedOfServerViewer = false;

	public ProductDeployDialogBindingHandler(String operationId, ProductDeployEntity entity) {
		this.operationId = operationId;
		this.entity = entity;
	}

	@Override
	public boolean initControls() {
		initServerViewer();
		initButtonBar();

		this.defaultListener.bindControls(this.allBindedControls);

		BindingHelper.setValue(this.allBindedControls.get("a.dp_user"), OPSUtil.getUserNameAndAccount());
		if (this.operationId.equals(OPSConstants.ADD_DEPLOY)) {
		} else if (this.operationId.equals(OPSConstants.UPDATE_DEPLOY) || this.operationId.equals(OPSConstants.READ_DEPLOY)) {
			try {
				this.disableControl("a.dp_code");
				this.disableControl("a.pd_code");
				this.disableControl("pd_select");
				
				bindValuesToControls();
				bindValuesToViewer();
				
				if (this.operationId.equals(OPSConstants.READ_DEPLOY)) {
					this.disableAll();
				}
			} catch (Exception e) {
				String message = "初始化产品部署信息发生异常";
				MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				return false;
			}

		}
		return true;
	}

	private void bindValuesToViewer() throws Exception {
		String sql = "com.hhh.platform.ops.sql.ops_server_productMapper.selectServerId";

		Map<String, Object> parameters = new HashMap();
		parameters.put("dp_id", this.currentDeployId);
		parameters.put("re_id", BindingHelper.getValue(this.allBindedControls.get("a.re_id")));

		List list = DaoUtil.selectList(sql, parameters);
		if (list != null && !list.isEmpty()) {
			List newList = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				newList.add(((Map)list.get(i)).get("server_id"));
			}
			String columns = BindingHelper.buildSelectColumns(this.getBindedFieldsNames());
			parameters = new HashMap();
			parameters.put(FrameworkConstants.SELECTCOLUMNS, columns);
			parameters.put("list", newList);

			sql = "com.hhh.platform.ops.sql.ops_server_registerMapper.selectServerByIds";
			List list2 = DaoUtil.selectList(sql, parameters);
			
			List resultList = new ArrayList();
			for (int i = 0; i < list2.size(); i++) {
				resultList.add(BindingHelper.getResultsWithTableInfo((HashMap) list2.get(i),
						"a"));
			}
			TableModel model = new TableModel(resultList);
			this.serverViewer.setInput(model);
		}
	}

	private void bindValuesToControls() throws Exception {
		currentDeployId = this.entity.getSelectedIDs()[0];
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dp_id", currentDeployId);

		String selectColumns = BindingHelper.buildSelectColumns(this.allBindedControls.keySet(), "a");
		parameters.put(FrameworkConstants.SELECTCOLUMNS, selectColumns);

		String sql = "com.hhh.platform.ops.sql.ops_pd_deployMapper.selectDeploy";
		Map appMap = DaoUtil.selectOne(sql, parameters);

		if (appMap.size() > 0) {
			appMap = BindingHelper.getResultsWithTableInfo((HashMap<String, Object>) appMap, "a");
			BindingHelper.setBindedValues(this.allBindedControls, (HashMap<String, Object>) appMap);
			this.savedValues.putAll(appMap);
			this.savedValues.put("a.dp_id", currentDeployId);
		}
	}

	private void initServerViewer() {
		Composite hdd_view_c = (Composite) this.allBindedControls.get("server_viewer");
		hdd_view_c.setLayout(new FillLayout());

		serverViewer = new TableViewer(hdd_view_c, SWT.BORDER);
		serverViewer.setContentProvider(new TableContentProvider());
		TableLabelProvider tableLabelProvider = new ServerRunViewerTableLabelProvider(getColumnFieldNames());
		tableLabelProvider.setColumnStyleMap(getColumnStyleMap());
		serverViewer.setLabelProvider(tableLabelProvider);
		String[] columnProperties = getColumnProperties();
		setColumnsWidth(columnProperties);
		serverViewer.setColumnProperties(columnProperties);
		serverViewer.getTable().setHeaderVisible(true);
		serverViewer.getTable().getParent().getParent().layout(true);
		serverViewer.getTable().setData(RWT.MARKUP_ENABLED, true);
	}

	protected String[] getColumnFieldNames() {
		String[] columnFieldNames = new String[] { "", "a.server_id", "a.server_model", "a.os_type", "a.site" };
		return columnFieldNames;

	}

	protected String[] getColumnProperties() {
		String[] properties = new String[] { "", "服务器编号", "品牌型号", "操作系统", "存放位置" };
		return properties;

	}

	protected void setColumnsWidth(String[] columnProperties) {
		for (int i = 0; i < columnProperties.length; i++) {
			String fieldName = getColumnFieldNames()[i];
			TableViewerColumn tvc = new TableViewerColumn(serverViewer, SWT.NONE);
			tvc.setLabelProvider(new TableLabelProviderEditable(getColumnFieldNames()[i]));
			TableColumn tableColumn = tvc.getColumn();
			tableColumn.setText(columnProperties[i]);

			tableColumn.setAlignment(SWT.CENTER);
			if (fieldName.equals("a.server_id")) {
				tableColumn.setWidth(0);
			} else if (fieldName.equals("a.server_model")) {
				tableColumn.setWidth(170);
				tableColumn.setAlignment(SWT.LEFT);
			} else if (fieldName.equals("a.p_type")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.os_type")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.cpu_model")) {
				tableColumn.setWidth(170);
				tableColumn.setAlignment(SWT.LEFT);
			} else if (fieldName.equals("a.cpu_hz")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.ram_gb")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.hdd_gb")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.is_watch")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.site")) {
				tableColumn.setWidth(250);
				tableColumn.setAlignment(SWT.LEFT);
			} else if (fieldName.equals("a.provider")) {
				tableColumn.setWidth(250);
				tableColumn.setAlignment(SWT.LEFT);
			} else if (fieldName.equals("alarm")) {
				tableColumn.setWidth(50);
			} else {
				tableColumn.setWidth(10);
			}
		}
	}

	private Map<String, String> getColumnStyleMap() {
		return Collections.EMPTY_MAP;
	}

	private void initButtonBar() {
		Composite c = (Composite) this.allBindedControls.get("button_bar");
		c.setLayout(new FormLayout());

		Button add_b = new Button(c, SWT.PUSH);
		FormData f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.left = new FormAttachment(0, 0);
		f.width = 25;
		f.height = 25;
		add_b.setLayoutData(f);
		this.allBindedControls.put("add_b", add_b);
		add_b.setData(RWT.CUSTOM_VARIANT, "add");

		Button update_b = new Button(c, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(add_b, 7, SWT.BOTTOM);
		f.left = new FormAttachment(add_b, 0, SWT.LEFT);
		f.width = 25;
		f.height = 25;
		update_b.setLayoutData(f);
		this.allBindedControls.put("update_b", update_b);
		update_b.setData(RWT.CUSTOM_VARIANT, "read2");

		Button delete_b = new Button(c, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(update_b, 7, SWT.BOTTOM);
		f.left = new FormAttachment(update_b, 0, SWT.LEFT);
		f.width = 25;
		f.height = 25;
		delete_b.setLayoutData(f);
		this.allBindedControls.put("delete_b", delete_b);
		delete_b.setData(RWT.CUSTOM_VARIANT, "delete");

	}

	@Override
	public boolean perform(String action) {
		if (action.equals(BindingHandler.CONFIRM_ACTION) && this.validate(true)
		// && checkCode() && checkName()
		) {
			if (this.operationId.equals(OPSConstants.ADD_DEPLOY)) {
				try {
					String dp_id = insertDeploy();
					return true;
				} catch (Exception e) {
					String message = "新增应用失败";
					MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				}
			} else if (this.operationId.equals(OPSConstants.UPDATE_DEPLOY)) {
				try {
					HashMap<String, Object> oldValues = new HashMap<String, Object>();
					oldValues.putAll(savedValues);
					Map updateValues = updateDeploy();
					if (updateValues != null && !updateValues.isEmpty()) {
						String content = "修改产品部署信息，唯一编号为:" + this.currentDeployId;
						// LogHelper.writeLog(content,
						// "ops_pd_deploy",this.allBindedControls,oldValues,
						// updateValues);
					}
					return true;
				} catch (Exception e) {
					String message = "修改产品部署信息失败";
					MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
				}
			}
		} else if (action.equals(BindingHandler.CANCEL_ACTION)) {
			return true;
		}
		return false;
	}

	private String insertDeploy() throws Exception {
		HashMap<String, Object> insertValues = BindingHelper.getLatestBindedValues(this.allBindedControls);
		insertValues.remove("a.dp_code");

		String[] insertColumnsAndValues = BindingHelper.buildInsertColumnsAndValues(insertValues, "a");
		Map parameters = new HashMap();
		parameters.put(FrameworkConstants.INSERTCOLUMNS, insertColumnsAndValues[0]);
		parameters.put(FrameworkConstants.INSERTVALUES, insertColumnsAndValues[1]);

		String sql = "com.hhh.platform.ops.sql.ops_pd_deployMapper.insertDeploy";

		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);
			String dp_id = UUIDUitil.getUUID();
			parameters.put("dp_id", dp_id);
			session.insert(sql, parameters);

			TableModel model = (TableModel) this.serverViewer.getInput();
			for (int i = 0; i < model.getRows().size(); i++) {
				Map data = model.getRows().get(i).getData();
				String server_id = (String) data.get("a.server_id");

				parameters = new HashMap();
				parameters.put("server_id", server_id);
				parameters.put("dp_id", dp_id);
				parameters.put("re_id", BindingHelper.getValue(this.allBindedControls.get("a.re_id")));

				sql = "com.hhh.platform.ops.sql.ops_server_productMapper.insertServerProduct";
				session.insert(sql, parameters);
			}

			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行插入语句" + sql + "错误:" + e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}

		insertValues.put("a.dp_id", parameters.get("dp_id"));
		insertValues.put("a.dp_code", parameters.get("dp_id"));
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
		return (String) parameters.get("dp_id");
	}

	private Map updateDeploy() throws Exception {
		HashMap<String, Object> newValues = BindingHelper.getLatestBindedValues(this.allBindedControls);
		HashMap<String, Object> updatedValues = BindingHelper.filterUnchanged(savedValues, newValues);
		filterSpecialValues(updatedValues);

		SqlSession session = null;
		String sql = "";
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);

			if (updatedValues.size() > 0) {
				String updateValues = BindingHelper.buildUpdateValues(updatedValues, "a");
				Map parametersForDeploy = new HashMap();
				parametersForDeploy.put("dp_id", this.currentDeployId);
				parametersForDeploy.put(FrameworkConstants.UPDATEVALUES, updateValues);

				Map parametersForRecord = new HashMap();
				if (updatedValues.containsKey("a.name")) {
					parametersForRecord.put("a.app_name", updatedValues.get("a.name"));
					String updateValuesForRecord = BindingHelper.buildUpdateValues((HashMap<String, Object>) parametersForRecord, "a");
					parametersForRecord.put("dp_id", this.currentDeployId);
					parametersForRecord.put(FrameworkConstants.UPDATEVALUES, updateValuesForRecord);
					String[] sqls = { "com.hhh.platform.ops.sql.ops_pd_deployMapper.updateDeploy", "com.hhh.platform.ops.sql.dc_app_using_recordMapper.updateRecord" };
					Map[] parameters = { parametersForDeploy, parametersForRecord };

					for (int i = 0; i < sqls.length; i++) {
						sql = sqls[i];
						session.update(sql, parameters[i]);
					}
				} else {
					sql = "com.hhh.platform.ops.sql.ops_pd_deployMapper.updateDeploy";
					session.update(sql, parametersForDeploy);
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

			if (isChangedOfServerViewer) {
				updateServerProduct(session);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行修改语句" + sql + "错误:" + e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
		return updatedValues;
	}

	private void updateServerProduct(SqlSession session) {
		String sql = "com.hhh.platform.ops.sql.ops_server_productMapper.deleteServerProduct";
		Map deleteParameter = new HashMap();
		deleteParameter.put("dp_id", this.currentDeployId);
		deleteParameter.put("re_id", BindingHelper.getValue(this.allBindedControls.get("a.re_id")));
		session.delete(sql, deleteParameter);

		TableModel model = (TableModel) this.serverViewer.getInput();
		for (int i = 0; i < model.getRows().size(); i++) {
			Map data = model.getRows().get(i).getData();
			String server_id = (String) data.get("a.server_id");

			Map addParameters = new HashMap();
			addParameters.put("server_id", server_id);
			addParameters.put("dp_id", this.currentDeployId);
			addParameters.put("re_id", BindingHelper.getValue(this.allBindedControls.get("a.re_id")));

			sql = "com.hhh.platform.ops.sql.ops_server_productMapper.insertServerProduct";
			session.insert(sql, addParameters);
		}
	}

	@Override
	public void focusLost(String fieldName, Control ctl) {
	}

	@Override
	public void widgetSelected(String fieldName, Control ctl) {
		if (fieldName.equals("pd_select")) {
			String operationMessage = "";
			Map parametersMap = new HashMap();
			EntityWithTableViewerCreator creator = new ProductSelectDialogCreator();
			ContainPagingControlsCreatorBindingHandler bindingHandler = new ProductSelectDialogBindingHandler(creator, operationMessage, parametersMap);
			ContainPagingDialog dialog = new ContainPagingDialog(bindingHandler);
			dialog.setSize(new Point(1000, 680));
			dialog.setTitleText("选择产品");
			dialog.open();

			Map selectedMap = bindingHandler.getSelectedMap();
			if (selectedMap != null && selectedMap.size() > 0) {

				String pd_code = (String) selectedMap.get("a.code");
				String pd_name = (String) selectedMap.get("a.name");
				String re_id = (String) selectedMap.get("a.re_id");

				setBindedValue(pd_code, "a.pd_code");
				setBindedValue(pd_name, "a.pd_name");
				setBindedValue(re_id, "a.re_id");
			}

		}
		if (fieldName.equals("add_b")) {
			String operationMessage = "";
			Map parametersMap = new HashMap();
			EntityWithTableViewerCreator creator = new ServerSelectDialogCreator();
			ContainPagingControlsCreatorBindingHandler bindingHandler = new ServerSelectDialogBindingHandler(creator, operationMessage, parametersMap);
			ContainPagingDialog dialog = new ContainPagingDialog(bindingHandler);
			dialog.setSize(new Point(1000, 680));
			dialog.setTitleText("添加服务器");
			dialog.open();

			List selectList = bindingHandler.getSelectedList();
			if (selectList != null && selectList.size() > 0) {
				TableModel model = (TableModel) serverViewer.getInput();
				for (int i = 0; i < selectList.size(); i++) {
					Map selectMap = (Map) selectList.get(i);

					if (model == null) {
						model = new TableModel(null);
						serverViewer.setInput(model);

						TableRowModel newrow = new TableRowModel(selectMap);
						model.addTableRow(newrow);
						isChangedOfServerViewer = true;
					} else {
						String server_id = (String) selectMap.get("a.server_id");

						boolean hasSelected = false;
						List<TableRowModel> list = model.getRows();
						for (int j = 0; j < list.size(); j++) {
							TableRowModel rowModel = list.get(j);
							String server_id_temp = (String) rowModel.getData().get("a.server_id");
							if (server_id.equals(server_id_temp)) {
								hasSelected = true;
								break;
							}
						}

						if (!hasSelected) {
							TableRowModel newrow = new TableRowModel(selectMap);
							model.addTableRow(newrow);
							isChangedOfServerViewer = true;
						}
					}
				}

				serverViewer.refresh();
			}
		}
		if (fieldName.equals("update_b")) {
			TableItem[] items = serverViewer.getTable().getSelection();
			if (items != null && items.length > 0) {
				TableRowModel rowModel = (TableRowModel) items[0].getData();
				String server_id = (String) rowModel.getData().get("a.server_id");

				ServerRegisterDialog dialog = new ServerRegisterDialog(OPSConstants.READ_SERVER_REGISTER, server_id);
				dialog.setTitleText("查看服务器");
				dialog.open();
			}
		}

		if (fieldName.equals("delete_b")) {
			TableModel model = (TableModel) serverViewer.getInput();
			TableItem[] items = serverViewer.getTable().getSelection();
			if (items != null && items.length > 0) {
				TableRowModel rowModel = (TableRowModel) items[0].getData();
				model.removeTableRows(new TableRowModel[] { rowModel });

				isChangedOfServerViewer = true;
				serverViewer.refresh();
			}
		}
	}

	private void setBindedValue(String value, String bindedFieldName) {
		BindingHelper.setValue(this.allBindedControls.get(bindedFieldName), value == null ? "" : value);
	}


	@Override
	protected void filterSpecialValues(HashMap<String, Object> updateValues) {
		updateValues.remove("pd_select");
		updateValues.remove("add_b");
		updateValues.remove("update_b");
		updateValues.remove("delete_b");
	}

	@Override
	public void attachListeners() {
		super.attachListeners();
		this.enableListeners("add_b", DefaultBindingListener.SELECTION);
		this.enableListeners("update_b", DefaultBindingListener.SELECTION);
		this.enableListeners("delete_b", DefaultBindingListener.SELECTION);
	}

	/**
	 * 
	 * @return 所有需要查询的绑定字段,包括那些不显示的
	 */
	public Set<String> getBindedFieldsNames() {
		Set<String> fields = new HashSet<String>();
		for (int i = 0; i < this.getColumnFieldNames().length; i++) {
			if (this.getColumnProperties()[i].trim().length() != 0) {
				fields.add(this.getColumnFieldNames()[i]);
			}
		}
		return fields;
	}

}
