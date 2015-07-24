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

import com.hhh.platform.advisors.framework.binding.EntityWithTableViewerCreator;
import com.hhh.platform.advisors.table.TableLabelProviderEditable;
import com.hhh.platform.ui.binding.XMLModelControlsCreator;

public class ServerProductDialogCreator extends EntityWithTableViewerCreator {

	private String[] columnFieldNames = new String[0];

	@Override
	public Set<String> getBindedFieldsNames() {
		Set<String> bindedFieldNames = super.getBindedFieldsNames();
		bindedFieldNames.add("a.re_id");
		return bindedFieldNames;
	}

	@Override
	protected String[] getColumnFieldNames() {
		columnFieldNames = new String[] { "", "a.code", "a.name", "a.yw_type", "a.dm_type", "a.version", "a.mark" };
		return columnFieldNames;

	}

	@Override
	protected String[] getColumnProperties() {
		String[] properties = new String[] { "", "产品编号", "产品名称", "业务类型", "代码类型", "当前版本", "备注" };
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
			if (fieldName.equals("a.code")) {
				tableColumn.setWidth(0);
			} else if (fieldName.equals("a.name")) {
				tableColumn.setWidth(350);
			} else if (fieldName.equals("a.yw_type")) {
				tableColumn.setWidth(150);
			} else if (fieldName.equals("a.dm_type")) {
				tableColumn.setWidth(150);
			} else if (fieldName.equals("a.version")) {
				tableColumn.setWidth(150);
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

		String modelFilePath = "com/hhh/platform/ops/ui/register/ProductRegisterViewSearcher.xml";

		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(modelFilePath);
		XMLModelControlsCreator controlsCreator = new XMLModelControlsCreator(xmlis);

		controlsCreator.createContents(mainSearch);
		bindedControls.putAll(controlsCreator.getBindedControls());
	}
}
