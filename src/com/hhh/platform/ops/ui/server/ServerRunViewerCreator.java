package com.hhh.platform.ops.ui.server;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Set;

import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;

import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreatorEditable;
import com.hhh.platform.advisors.table.TableLabelProviderEditable;
import com.hhh.platform.ui.binding.XMLModelControlsCreator;

public class ServerRunViewerCreator extends EntityWithTableViewerCreatorEditable {
	private String[] columnFieldNames = new String[0];

	public ServerRunViewerCreator() {
		this.fix_first_column = false;
	}

	@Override
	public Set<String> getBindedFieldsNames() {
		Set<String> bindedFieldNames = super.getBindedFieldsNames();
		bindedFieldNames.add("a.server_id");
		bindedFieldNames.remove("alarm");
		return bindedFieldNames;
	}

	@Override
	protected String[] getColumnFieldNames() {
		columnFieldNames = new String[] { "","alarm", "a.server_model", "a.p_type", "a.os_type", "a.cpu_model", "a.cpu_hz", "a.ram_gb", "a.hdd_gb", "a.is_watch","a.site", "a.provider" };
		return columnFieldNames;

	}

	@Override
	protected String[] getColumnProperties() {
		String[] properties = new String[] { "","报警", "品牌型号", "产品类别", "操作系统", "CPU型号", "CPU频率(GHz)", "内存容量(GB)", "硬盘容量(GB)", "是否监控","存放位置", "供应商" };
		return properties;

	}

	@Override
	protected void setColumnsWidth(String[] columnProperties) {
		for (int i = 0; i < columnProperties.length; i++) {
			String fieldName = getColumnFieldNames()[i];
			TableViewerColumn tvc = new TableViewerColumn(viewer, SWT.NONE);
			tvc.setLabelProvider(new TableLabelProviderEditable(getColumnFieldNames()[i]));
			TableColumn tableColumn = tvc.getColumn();
			tableColumn.setText(columnProperties[i]);

			tableColumn.setAlignment(SWT.CENTER);
			if (fieldName.equals("a.server_model")) {
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
				tableColumn.setWidth(30);
			}
		}
	}

	@Override
	protected void createSearchControl(Composite searchComposite, HashMap<String, Control> bindedControls) {
		searchComposite.setLayout(new FormLayout());

		FormData searchCompositeData = (FormData) searchComposite.getLayoutData();
		searchCompositeData.bottom = new FormAttachment(0, 65);
		searchComposite.setLayoutData(searchCompositeData);

		Composite mainSearch = new Composite(searchComposite, SWT.NONE);
		FormData formData = new FormData();
		formData.left = new FormAttachment(0, 0);
		formData.top = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, 0);
		formData.bottom = new FormAttachment(100, 0);
		mainSearch.setLayoutData(formData);

		String modelFilePath = "com/hhh/platform/ops/ui/server/ServerRunViewSearcher.xml";

		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(modelFilePath);
		XMLModelControlsCreator controlsCreator = new XMLModelControlsCreator(xmlis);

		controlsCreator.createContents(mainSearch);
		bindedControls.putAll(controlsCreator.getBindedControls());
	}

	@Override
	protected boolean setMarkUp() {
		return true;
	}
	
	
}
