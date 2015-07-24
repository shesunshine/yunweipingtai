package com.hhh.platform.ops.ui.deploy;

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

public class ProductDeployViewerCreator extends EntityWithTableViewerCreatorEditable {
	private String[] columnFieldNames = new String[0];
	
	public ProductDeployViewerCreator(){
		this.fix_first_column = false;
	}

	@Override
	public Set<String> getBindedFieldsNames() {
		Set<String> bindedFieldNames = super.getBindedFieldsNames();
		bindedFieldNames.add("a.dp_id");
		bindedFieldNames.add("b.re_id");
		return bindedFieldNames;
	}

	@Override
	protected String[] getColumnFieldNames() {
		columnFieldNames = new String[] { "", "a.dp_code", "a.dp_name", "a.pd_code",
				"b.name","a.url", "a.organization", "a.dp_user","a.dp_date"};
		return columnFieldNames;

	}
	@Override
	protected String[] getColumnProperties() {
		String[] properties = new String[] { "", "部署编号", "网站名", "产品编号","产品名称", "访问网址", "使用单位",
				"部署人","部署日期"};
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
			if (fieldName.equals("a.dp_code")) {
				tableColumn.setWidth(0);
			}else if (fieldName.equals("a.dp_name")) {
				tableColumn.setWidth(200);
			}else if (fieldName.equals("a.pd_code")) {
				tableColumn.setWidth(0);
			}else if (fieldName.equals("b.name")) {
				tableColumn.setWidth(200);
			}else if (fieldName.equals("a.url")) {
				tableColumn.setWidth(200);
			}else if (fieldName.equals("a.organization")) {
				tableColumn.setWidth(200);
			}else if (fieldName.equals("a.dp_user")) {
				tableColumn.setWidth(200);
			}else if (fieldName.equals("a.dp_date")) {
				tableColumn.setWidth(200);
//				tvc.setEditingSupport(new EditorSupportIntenal(viewer, columnFieldNames[i], this
//						.getColumnStyleMap()));
			}else  {
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
		
		String modelFilePath= "com/hhh/platform/ops/ui/deploy/ProductDeployViewSearcher.xml";
		
		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(modelFilePath);
		XMLModelControlsCreator controlsCreator = new XMLModelControlsCreator(xmlis);

		controlsCreator.createContents(mainSearch);
		bindedControls.putAll(controlsCreator.getBindedControls());
	}
}
