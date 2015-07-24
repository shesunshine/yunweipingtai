/*
 * ContainPagingControlsCreatorBindingHandler.java
 * com.hhh.platform.jc.core.ti.logic
 *
 * @author			3hhf
 * @date			2011-9-2 下午05:39:56 
 * @version			V1.0
 *
 * 版权所有 © 2011 广州粤建三和软件有限公司
 */

package com.hhh.platform.ops.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;

import com.hhh.platform.advisors.framework.FrameWorkAdvisor;
import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.framework.binding.BindingHandler;
import com.hhh.platform.advisors.framework.binding.EntityViewerBindingHandler;
import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreator;
import com.hhh.platform.advisors.page.PagePartionUtil;
import com.hhh.platform.advisors.table.TableRowModel;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class ContainPagingControlsCreatorBindingHandler extends
		EntityViewerBindingHandler {

	private static Log log = LogFactory
			.getLog(ContainPagingControlsCreatorBindingHandler.class);

	protected String operationMessage;

	protected EntityWithTableViewerCreator controlsCreator;

	protected Map selectedMap = new HashMap();

	protected List selectedList = new ArrayList();

	protected Map conditionMap;

	protected PagePartionUtil pagePartionUtil;

	/**
	 * 是否只能最多check一个
	 */
	private boolean singleChecked = true;

	public ContainPagingControlsCreatorBindingHandler(
			EntityWithTableViewerCreator controlsCreator,
			String operationMessage, Map conditionMap) {
		this.controlsCreator = controlsCreator;
		this.operationMessage = operationMessage;
		this.conditionMap = conditionMap;
	}

	@Override
	public boolean initControls() {
		initCreatorControls();
		try {
			pagePartionUtil = new PagePartionUtil(
					this.controlsCreator.getViewer(),
					this.controlsCreator.getPagePartitionArea(),
					FrameworkConstants.PAGE_SIZE, getCountMapperString(),
					getSelectMapperString(),
					(Button) this.allBindedControls.get("check_all"),
					controlsCreator.getBindedFieldsNames());
			this.doSearch((HashMap<String, Object>) conditionMap);
			return true;
		} catch (Exception e) {
			String message = "初始化" + operationMessage + "过程中发生异常";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return false;
		}

	}

	@Override
	public void doSearch(HashMap<String, Object> paremeters) throws Exception {
		pagePartionUtil.doSearch(paremeters);
	}

	@Override
	public boolean perform(String action) {
		if (action.equals(BindingHandler.CONFIRM_ACTION)) {

			Object[] checkedElements = ((CheckboxTableViewer) this.controlsCreator
					.getViewer()).getCheckedElements();
			// 必须选择一条数据
			if (checkedElements == null || checkedElements.length == 0) {
				MessageDialog.openWarning(
						Display.getCurrent().getActiveShell(), "提示", "请选择信息");
				return false;
			} else {
				// 如果是单选的话，只能选一条
				if (singleChecked) {
					if (checkedElements.length == 1) {
						TableRowModel rowModel = (TableRowModel) checkedElements[0];
						selectedMap = (HashMap<String, Object>) rowModel
								.getData();
					} else {
						MessageDialog.openWarning(Display.getCurrent()
								.getActiveShell(), "提示", operationMessage
								+ "只能单选");
						return false;
					}
				} 
				// 否则可以多选
				else {
					for (int i = 0; i < checkedElements.length; i++) {
						TableRowModel rowModel = (TableRowModel) checkedElements[i];
						Map map = (HashMap<String, Object>) rowModel.getData();
						selectedList.add(map);
					}
				}
			}
		} else {
			super.perform(action);
		}
		return true;
	}

	/**
	 * 查询复核条件的信息的语句映射 子类必须重写
	 * 
	 * @return
	 */
	protected String getSelectMapperString() {
		return "";
	}

	/**
	 * 查询复核条件的信息的数量的语句映射 子类必须重写
	 * 
	 * @return
	 */
	protected String getCountMapperString() {
		return "";
	}

	/**
	 * 可初始化控件
	 */
	protected void initCreatorControls() {
		this.allBindedControls.get("check_all").setVisible(false);
	}

	public EntityWithTableViewerCreator getControlsCreator() {
		return controlsCreator;
	}

	public String getOperationMessage() {
		return operationMessage;
	}

	public Map getSelectedMap() {
		return selectedMap;
	}

	public List getSelectedList() {
		return selectedList;
	}

	public boolean isSingleChecked() {
		return singleChecked;
	}

	public void setSingleChecked(boolean singleChecked) {
		this.singleChecked = singleChecked;
	}

}
