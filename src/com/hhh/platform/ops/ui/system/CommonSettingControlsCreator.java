package com.hhh.platform.ops.ui.system;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.ControlsCreator;
import com.hhh.platform.widget.HHHLabel;

public class CommonSettingControlsCreator {

	private HashMap<String, Control> allBindedControls = new LinkedHashMap<String, Control>();

	private Scale scale_p, scale_r;
	private Spinner spinner_p, spinner_r;
	private List ywList, dmList;
	private ListViewer ywViewer, dmViewer;

	private LinkedList<String> yw = new LinkedList<String>();
	private LinkedList<String> dm = new LinkedList<String>();
	private String dm_type = "", yw_type = "";

	private final int P_MIN = 3;
	private final int P_MAX = 30;

	private final int R_MIN = 1;
	private final int R_MAX = 12;

	private final String YW_PREFIX = "yw_";
	private final String DM_PREFIX = "dm_";

	public CommonSettingControlsCreator(HashMap<String, Control> allBindedControls) {
		this.allBindedControls = allBindedControls;
	}

	public void initCommonTabItem() {
		Composite common_c = (Composite) this.allBindedControls.get("common_c");
		common_c.setLayout(new FormLayout());

		Composite topC = new Composite(common_c, SWT.NONE);
		FormData f = new FormData();
		f.top = new FormAttachment(0, 10);
		f.bottom = new FormAttachment(0, 50);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		topC.setLayoutData(f);
		createControlsInTopC(topC);

		Composite bottomC = new Composite(common_c, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(topC, 30, SWT.BOTTOM);
		f.bottom = new FormAttachment(80, 0);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		bottomC.setLayoutData(f);
		createControlsInBottomC(bottomC);

	}

	private void createControlsInTopC(Composite topC) {
		topC.setLayout(new FormLayout());

		HHHLabel performance_label = new HHHLabel(topC, SWT.NONE);
		performance_label.setText("性能优劣阀值");
		performance_label.setLabelAlignment(SWT.RIGHT);
		FormData f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(15, 0);
		performance_label.setLayoutData(f);

		scale_p = new Scale(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(performance_label, 5, SWT.RIGHT);
		f.right = new FormAttachment(35, 0);
		scale_p.setLayoutData(f);
		this.allBindedControls.put("scale_p", scale_p);

		spinner_p = new Spinner(topC, SWT.BORDER);
		f = new FormData();
		f.top = new FormAttachment(0, 5);
		f.bottom = new FormAttachment(100, -5);
		f.left = new FormAttachment(scale_p, 2, SWT.RIGHT);
		f.right = new FormAttachment(45, 0);
		spinner_p.setLayoutData(f);
		this.allBindedControls.put("m.spinner_p", spinner_p);

		HHHLabel h_lable = new HHHLabel(topC, SWT.NONE);
		h_lable.setText("秒");
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(spinner_p, 0, SWT.RIGHT);
		f.right = new FormAttachment(50, 0);
		h_lable.setLayoutData(f);

		HHHLabel run_label = new HHHLabel(topC, SWT.NONE);
		run_label.setText("运行监控阀值");
		run_label.setLabelAlignment(SWT.RIGHT);
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(50, 0);
		f.right = new FormAttachment(65, 0);
		run_label.setLayoutData(f);

		scale_r = new Scale(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(run_label, 5, SWT.RIGHT);
		f.right = new FormAttachment(85, 0);
		scale_r.setLayoutData(f);
		this.allBindedControls.put("scale_r", scale_r);

		spinner_r = new Spinner(topC, SWT.BORDER);
		f = new FormData();
		f.top = new FormAttachment(0, 5);
		f.bottom = new FormAttachment(100, -5);
		f.left = new FormAttachment(scale_r, 2, SWT.RIGHT);
		f.right = new FormAttachment(95, 0);
		spinner_r.setLayoutData(f);
		this.allBindedControls.put("m.spinner_r", spinner_r);

		HHHLabel s_lable = new HHHLabel(topC, SWT.NONE);
		s_lable.setText("小时");
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(spinner_r, 0, SWT.RIGHT);
		f.right = new FormAttachment(100, 0);
		s_lable.setLayoutData(f);

		initControlsInTopC();
	}

	private void initControlsInTopC() {
		this.scale_p.setMinimum(this.P_MIN);
		this.scale_p.setMaximum(this.P_MAX);
		this.scale_p.setIncrement(1);
		this.scale_p.setPageIncrement(1);
		this.scale_p.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinner_p.setSelection(scale_p.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				spinner_p.setSelection(scale_p.getSelection());
			}
		});

		this.scale_r.setMinimum(this.R_MIN);
		this.scale_r.setMaximum(this.R_MAX);
		this.scale_r.setIncrement(1);
		this.scale_r.setPageIncrement(1);
		this.scale_r.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinner_r.setSelection(scale_r.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				spinner_r.setSelection(scale_r.getSelection());
			}
		});

		this.spinner_p.setMinimum(this.P_MIN);
		this.spinner_p.setMaximum(this.P_MAX);
		this.spinner_p.setIncrement(1);
		this.spinner_p.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				int selection = spinner_p.getSelection();
				scale_p.setSelection(selection);
			}
		});

		this.spinner_r.setMinimum(this.R_MIN);
		this.spinner_r.setMaximum(this.R_MAX);
		this.spinner_r.setIncrement(1);
		this.spinner_r.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				int selection = spinner_r.getSelection();
				scale_r.setSelection(selection);
			}
		});
	}

	private void createControlsInBottomC(Composite bottomC) {
		bottomC.setLayout(new FormLayout());

		Composite leftC = new Composite(bottomC, SWT.NONE);
		FormData f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(5, 0);
		f.right = new FormAttachment(50, 0);
		leftC.setLayoutData(f);

		leftC.setLayout(new FormLayout());

		HHHLabel yw_label = new HHHLabel(leftC, SWT.NONE);
		yw_label.setText("业务类型");
		yw_label.setLabelAlignment(SWT.LEFT);
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(0, 30);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		yw_label.setLayoutData(f);

		ywList = new List(leftC, SWT.BORDER | SWT.V_SCROLL);
		f = new FormData();
		f.top = new FormAttachment(yw_label, 0, SWT.BOTTOM);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(40, 0);
		ywList.setLayoutData(f);

		ywViewer = new ListViewer(ywList);
		ywViewer.setContentProvider(new ArrayContentProvider());
		ywViewer.setLabelProvider(new LabelProvider());
		ywViewer.setInput(yw);
		createControlsInC(leftC, "业务类型", YW_PREFIX, this.ywList, this.ywViewer, this.yw);

		Composite rightC = new Composite(bottomC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(55, 0);
		f.right = new FormAttachment(100, 0);
		rightC.setLayoutData(f);

		rightC.setLayout(new FormLayout());

		HHHLabel dm_label = new HHHLabel(rightC, SWT.NONE);
		dm_label.setText("代码类型");
		dm_label.setLabelAlignment(SWT.LEFT);
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(0, 30);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		dm_label.setLayoutData(f);

		dmList = new List(rightC, SWT.BORDER | SWT.V_SCROLL);
		f = new FormData();
		f.top = new FormAttachment(dm_label, 0, SWT.BOTTOM);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(40, 0);
		dmList.setLayoutData(f);

		dmViewer = new ListViewer(dmList);
		dmViewer.setContentProvider(new ArrayContentProvider());
		dmViewer.setLabelProvider(new ColumnLabelProvider());
		dmViewer.setInput(yw);
		createControlsInC(rightC, "代码类型", DM_PREFIX, this.dmList, this.dmViewer, this.dm);
	}

	private void createControlsInC(Composite c, String title, final String prefix, final List list, final ListViewer viewer, final LinkedList<String> link_list) {

		Button add_b = new Button(c, SWT.PUSH);
		FormData f = new FormData();
		f.top = new FormAttachment(20, 0);
		f.left = new FormAttachment(list, 5, SWT.RIGHT);
		f.width = 25;
		f.height = 25;
		add_b.setLayoutData(f);
		this.allBindedControls.put(prefix + "add_b", add_b);
		add_b.setData(RWT.CUSTOM_VARIANT, "add");

		Button update_b = new Button(c, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(add_b, 7, SWT.BOTTOM);
		f.left = new FormAttachment(add_b, 0, SWT.LEFT);
		f.width = 25;
		f.height = 25;
		update_b.setLayoutData(f);
		this.allBindedControls.put(prefix + "update_b", update_b);
		update_b.setData(RWT.CUSTOM_VARIANT, "update");

		Button delete_b = new Button(c, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(update_b, 7, SWT.BOTTOM);
		f.left = new FormAttachment(update_b, 0, SWT.LEFT);
		f.width = 25;
		f.height = 25;
		delete_b.setLayoutData(f);
		this.allBindedControls.put(prefix + "delete_b", delete_b);
		delete_b.setData(RWT.CUSTOM_VARIANT, "delete");

		Button up_b = new Button(c, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(delete_b, 7, SWT.BOTTOM);
		f.left = new FormAttachment(delete_b, 0, SWT.LEFT);
		f.width = 25;
		f.height = 25;
		up_b.setLayoutData(f);
		this.allBindedControls.put(prefix + "up_b", up_b);
		up_b.setData(RWT.CUSTOM_VARIANT, "up");

		Button down_b = new Button(c, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(up_b, 7, SWT.BOTTOM);
		f.left = new FormAttachment(up_b, 0, SWT.LEFT);
		f.width = 25;
		f.height = 25;
		down_b.setLayoutData(f);
		this.allBindedControls.put(prefix + "down_b", down_b);
		down_b.setData(RWT.CUSTOM_VARIANT, "down");

		final Composite addC = new Composite(c, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(add_b, -1, SWT.TOP);
		f.bottom = new FormAttachment(add_b, 3, SWT.BOTTOM);
		f.left = new FormAttachment(add_b, 2, SWT.RIGHT);
		f.right = new FormAttachment(100, -2);
		addC.setLayoutData(f);
		addC.setLayout(new FormLayout());
		this.allBindedControls.put(prefix + "addC", addC);
		addC.setVisible(false);

		final Text add_t = new Text(addC, SWT.BORDER);
		f = new FormData();
		f.top = new FormAttachment(add_b, 0, SWT.TOP);
		f.left = new FormAttachment(add_b, 2, SWT.RIGHT);
		f.right = new FormAttachment(100, -64);
		f.height = 23;
		add_t.setLayoutData(f);
		this.allBindedControls.put(prefix + "add_t", add_t);

		Button add_t_yes = new Button(addC, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(add_t, 1, SWT.TOP);
		f.left = new FormAttachment(add_t, 2, SWT.RIGHT);
		f.width = 25;
		f.height = 25;
		add_t_yes.setLayoutData(f);
		this.allBindedControls.put(prefix + "add_t_yes", add_t_yes);
		add_t_yes.setData(RWT.CUSTOM_VARIANT, "yes");

		Button add_t_no = new Button(addC, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(add_t_yes, 0, SWT.TOP);
		f.left = new FormAttachment(add_t_yes, 2, SWT.RIGHT);
		f.width = 25;
		f.height = 25;
		add_t_no.setLayoutData(f);
		this.allBindedControls.put(prefix + "add_t_no", add_t_no);
		add_t_no.setData(RWT.CUSTOM_VARIANT, "no");

		final Composite updateC = new Composite(c, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(update_b, -2, SWT.TOP);
		f.bottom = new FormAttachment(update_b, 3, SWT.BOTTOM);
		f.left = new FormAttachment(update_b, 2, SWT.RIGHT);
		f.right = new FormAttachment(100, -2);
		updateC.setLayoutData(f);
		updateC.setLayout(new FormLayout());
		this.allBindedControls.put(prefix + "updateC", updateC);
		updateC.setVisible(false);

		final Text update_t = new Text(updateC, SWT.BORDER);
		f = new FormData();
		f.top = new FormAttachment(update_b, 1, SWT.TOP);
		f.left = new FormAttachment(update_b, 2, SWT.RIGHT);
		f.right = new FormAttachment(100, -64);
		f.height = 23;
		update_t.setLayoutData(f);
		this.allBindedControls.put(prefix + "update_t", update_t);

		Button update_t_yes = new Button(updateC, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(update_t, 1, SWT.TOP);
		f.left = new FormAttachment(update_t, 2, SWT.RIGHT);
		f.width = 25;
		f.height = 25;
		update_t_yes.setLayoutData(f);
		this.allBindedControls.put(prefix + "update_t_yes", update_t_yes);
		update_t_yes.setData(RWT.CUSTOM_VARIANT, "yes");

		Button update_t_no = new Button(updateC, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(update_t_yes, 0, SWT.TOP);
		f.left = new FormAttachment(update_t_yes, 2, SWT.RIGHT);
		f.width = 25;
		f.height = 25;
		update_t_no.setLayoutData(f);
		this.allBindedControls.put(prefix + "update_t_no", update_t_no);
		update_t_no.setData(RWT.CUSTOM_VARIANT, "no");

		final Composite deleteC = new Composite(c, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(delete_b, -2, SWT.TOP);
		f.bottom = new FormAttachment(delete_b, 3, SWT.BOTTOM);
		f.left = new FormAttachment(delete_b, 2, SWT.RIGHT);
		f.right = new FormAttachment(100, -2);
		deleteC.setLayoutData(f);
		deleteC.setLayout(new FormLayout());
		this.allBindedControls.put(prefix + "deleteC", updateC);
		deleteC.setVisible(false);

		final HHHLabel delete_l = new HHHLabel(deleteC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(delete_b, 1, SWT.TOP);
		f.left = new FormAttachment(delete_b, 2, SWT.RIGHT);
		f.right = new FormAttachment(100, -64);
		f.height = 23;
		delete_l.setLayoutData(f);
		delete_l.setLabelAlignment(SWT.RIGHT);
		delete_l.setText("确定删除?");
		this.allBindedControls.put(prefix + "delete_l", delete_l);

		Button delete_l_yes = new Button(deleteC, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(delete_l, 1, SWT.TOP);
		f.left = new FormAttachment(delete_l, 2, SWT.RIGHT);
		f.width = 25;
		f.height = 25;
		delete_l_yes.setLayoutData(f);
		this.allBindedControls.put(prefix + "delete_l_yes", delete_l_yes);
		delete_l_yes.setData(RWT.CUSTOM_VARIANT, "yes");

		Button delete_l_no = new Button(deleteC, SWT.PUSH);
		f = new FormData();
		f.top = new FormAttachment(delete_l_yes, 0, SWT.TOP);
		f.left = new FormAttachment(delete_l_yes, 2, SWT.RIGHT);
		f.width = 25;
		f.height = 25;
		delete_l_no.setLayoutData(f);
		this.allBindedControls.put(prefix + "delete_l_no", delete_l_no);
		delete_l_no.setData(RWT.CUSTOM_VARIANT, "no");

		add_b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addC.setVisible(true);
				updateC.setVisible(false);
				deleteC.setVisible(false);
				add_t.setText("");
			}
		});
		add_t_yes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = add_t.getText().trim();
				if (!text.isEmpty()) {
					list.add(text);
					link_list.addLast(text);
					viewer.refresh(true, true);
					setType(prefix, link_list);
					list.select(link_list.size() - 1);
				}
			}
		});

		add_t_no.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addC.setVisible(false);
			}
		});

		update_b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = list.getSelectionIndex();
				addC.setVisible(false);
				deleteC.setVisible(false);
				if (index != -1) {
					updateC.setVisible(true);
					String selectE = list.getItem(index);
					update_t.setText(selectE);
				}
			}
		});
		update_t_yes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String text = update_t.getText().trim();
				list.setItem(list.getSelectionIndex(), text);
				link_list.set(list.getSelectionIndex(), text);
				viewer.refresh(true, true);
				setType(prefix, link_list);
			}
		});

		update_t_no.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateC.setVisible(false);
			}
		});

		delete_b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateC.setVisible(false);
				addC.setVisible(false);

				int index = list.getSelectionIndex();
				if (index != -1) {
					deleteC.setVisible(true);
				}
			}
		});

		delete_l_yes.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = list.getSelectionIndex();
				if (index != -1) {
					list.remove(index);
					link_list.remove(index);
					viewer.refresh(true, true);
					setType(prefix, link_list);
					list.select(index);
				}
			}
		});

		delete_l_no.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteC.setVisible(false);
			}
		});

		up_b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addC.setVisible(false);
				updateC.setVisible(false);
				deleteC.setVisible(false);
				int index = list.getSelectionIndex();
				if (index != -1) {
					if (index > 0) {
						String temp = link_list.get(index);
						link_list.set(index, link_list.get(index - 1));
						link_list.set(index - 1, temp);
						
						list.setSelection(index);
						setType(prefix, link_list);
						
						viewer.refresh();
					}
				}
			}
		});

		down_b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addC.setVisible(false);
				updateC.setVisible(false);
				deleteC.setVisible(false);
				int index = list.getSelectionIndex();
				if (index != -1) {
					if (index < link_list.size() - 1) {
						String temp = link_list.get(index);
						link_list.set(index, link_list.get(index + 1));
						link_list.set(index + 1, temp);
						list.setSelection(index);
						setType(prefix, link_list);
						viewer.refresh();
					}
				}
			}
		});
	}

	private void setType(final String prefix, final LinkedList<String> link_list) {
		if (prefix.equals(YW_PREFIX)) {
			yw_type = listToString(link_list);
			BindingHelper.setValue(this.allBindedControls.get("m.ops_yw_type"), yw_type);
		} else {
			dm_type = listToString(link_list);
			BindingHelper.setValue(this.allBindedControls.get("m.ops_dm_type"), dm_type);
		}
	}

	private String listToString(LinkedList<String> list) {
		if (list == null || list.isEmpty()) {
			return "";
		} else {
			return list.toString().replace("[", "").replace("]", "").replace(" ", "");
		}
	}

	public void afterSetBindededValues(HashMap<String, Object> savedValues) {
		String dm_type = (String) BindingHelper.getValue(this.allBindedControls.get("m.ops_dm_type"));
		if (dm_type != null && !dm_type.isEmpty()) {
			String[] types = dm_type.split(",");
			for (int i = 0; i < types.length; i++) {
				this.dm.addLast(types[i]);
			}
			this.dmViewer.setInput(this.dm);
			this.dmList.setSelection(dm_type.split(","));
		}
		String yw_type = (String) BindingHelper.getValue(this.allBindedControls.get("m.ops_yw_type"));
		if (yw_type != null && !yw_type.isEmpty()) {
			String[] types = yw_type.split(",");
			for (int i = 0; i < types.length; i++) {
				String type = types[i];
				this.yw.addLast(type);
			}
			this.ywViewer.setInput(this.yw);
			this.ywList.setSelection(yw_type.split(","));
		}

	}
}
