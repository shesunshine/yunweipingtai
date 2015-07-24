package com.hhh.platform.ops.ui.run;

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

public class LogRunViewerCreator extends EntityWithTableViewerCreatorEditable {
	private String[] columnFieldNames = new String[0];

	public LogRunViewerCreator() {
		this.fix_first_column = false;
	}

	@Override
	public Set<String> getBindedFieldsNames() {
		Set<String> bindedFieldNames = super.getBindedFieldsNames();
		bindedFieldNames.add("a.log_id");
		bindedFieldNames.add("c.re_id");
		bindedFieldNames.add("b.dp_id");
		return bindedFieldNames;
	}

	@Override
	protected String[] getColumnFieldNames() {
		columnFieldNames = new String[] { "", "b.pd_code", "b.dp_code", "c.name", "b.dp_name", "a.write_date", "a.content" };
		return columnFieldNames;

	}

	@Override
	protected String[] getColumnProperties() {
		String[] properties = new String[] { "", "产品编号", "部署编号", "产品名称", "网站名", "记录日期", "内容" };
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

			tableColumn.setAlignment(SWT.LEFT);
			if (fieldName.equals("b.pd_code")) {
				tableColumn.setWidth(0);
			} else if (fieldName.equals("b.dp_code")) {
				tableColumn.setWidth(0);
			} else if (fieldName.equals("c.name")) {
				tableColumn.setWidth(250);
			} else if (fieldName.equals("b.dp_name")) {
				tableColumn.setWidth(250);
			} else if (fieldName.equals("a.write_date")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.content")) {
				tableColumn.setWidth(450);
			} else if (fieldName.equals("a.deal_date")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("a.deal_status")) {
				tableColumn.setWidth(80);
				// tvc.setEditingSupport(new EditorSupportIntenal(viewer,
				// columnFieldNames[i], this.getColumnStyleMap()));
			} else if (fieldName.equals("a.mark")) {
				tableColumn.setWidth(300);
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

		String modelFilePath = "com/hhh/platform/ops/ui/run/LogRunViewSearcher.xml";

		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(modelFilePath);
		XMLModelControlsCreator controlsCreator = new XMLModelControlsCreator(xmlis);

		controlsCreator.createContents(mainSearch);
		bindedControls.putAll(controlsCreator.getBindedControls());
	}
}
