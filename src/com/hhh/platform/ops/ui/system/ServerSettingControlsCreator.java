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
import com.hhh.platform.widget.HHHLabel;

public class ServerSettingControlsCreator {

	private HashMap<String, Control> allBindedControls = new LinkedHashMap<String, Control>();

	private Scale scale_hdd, scale_cpu, scale_ram;
	private Spinner spinner_hdd, spinner_cpu, spinner_ram;
	private Text txt_hdd, txt_cpu, txt_ram;

	private final int HDD_MIN = 0;
	private final int HDD_MAX = 100;

	private final int CPU_MIN = 0;
	private final int CPU_MAX = 100;

	private final int RAM_MIN = 0;
	private final int RAM_MAX = 100;

	public ServerSettingControlsCreator(HashMap<String, Control> allBindedControls) {
		this.allBindedControls = allBindedControls;
	}

	public void initCommonTabItem() {
		Composite server_c = (Composite) this.allBindedControls.get("server_c");
		server_c.setLayout(new FormLayout());

		Composite topC = new Composite(server_c, SWT.NONE);
		FormData f = new FormData();
		f.top = new FormAttachment(0, 10);
		f.bottom = new FormAttachment(100, 0);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(100, 0);
		topC.setLayoutData(f);
		createControlsInTopC(topC);
	}

	private void createControlsInTopC(Composite topC) {
		topC.setLayout(new FormLayout());

		HHHLabel cpu_label = new HHHLabel(topC, SWT.NONE);
		cpu_label.setText("CPU使用率阀值");
		cpu_label.setLabelAlignment(SWT.RIGHT);
		FormData f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(0, 40);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(15, 0);
		cpu_label.setLayoutData(f);

		scale_cpu = new Scale(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(cpu_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(cpu_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(cpu_label, 5, SWT.RIGHT);
		f.right = new FormAttachment(35, 0);
		scale_cpu.setLayoutData(f);
		this.allBindedControls.put("scale_cpu", scale_cpu);

		spinner_cpu = new Spinner(topC, SWT.BORDER);
		f = new FormData();
		f.top = new FormAttachment(cpu_label, 5, SWT.TOP);
		f.bottom = new FormAttachment(cpu_label, -5, SWT.BOTTOM);
		f.left = new FormAttachment(scale_cpu, 2, SWT.RIGHT);
		f.right = new FormAttachment(45, 0);
		spinner_cpu.setLayoutData(f);
		this.allBindedControls.put("m.spinner_cpu", spinner_cpu);

		HHHLabel s_lable = new HHHLabel(topC, SWT.NONE);
		s_lable.setText("%");
		f = new FormData();
		f.top = new FormAttachment(cpu_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(cpu_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(spinner_cpu, 0, SWT.RIGHT);
		f.right = new FormAttachment(50, 0);
		s_lable.setLayoutData(f);

		HHHLabel cpu_label2 = new HHHLabel(topC, SWT.NONE);
		cpu_label2.setText("CPU使用率曲线图描述点可忽略值为≤");
		cpu_label2.setLabelAlignment(SWT.RIGHT);
		f = new FormData();
		f.top = new FormAttachment(0, 0);
		f.bottom = new FormAttachment(0, 40);
		f.left = new FormAttachment(50, 0);
		f.right = new FormAttachment(85, 0);
		cpu_label2.setLayoutData(f);

		txt_cpu = new Text(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(cpu_label2, 5, SWT.TOP);
		f.bottom = new FormAttachment(cpu_label2, -5, SWT.BOTTOM);
		f.left = new FormAttachment(cpu_label2, 5, SWT.RIGHT);
		f.right = new FormAttachment(95, 0);
		txt_cpu.setLayoutData(f);
		this.allBindedControls.put("m.ignore_cpu", txt_cpu);

		HHHLabel s_lable2 = new HHHLabel(topC, SWT.NONE);
		s_lable2.setText("%");
		f = new FormData();
		f.top = new FormAttachment(cpu_label2, 0, SWT.TOP);
		f.bottom = new FormAttachment(cpu_label2, 0, SWT.BOTTOM);
		f.left = new FormAttachment(txt_cpu, 0, SWT.RIGHT);
		f.right = new FormAttachment(100, 0);
		s_lable2.setLayoutData(f);

		HHHLabel ram_label = new HHHLabel(topC, SWT.NONE);
		ram_label.setText("RAM使用率阀值");
		ram_label.setLabelAlignment(SWT.RIGHT);
		f = new FormData();
		f.top = new FormAttachment(cpu_label, 10, SWT.BOTTOM);
		f.bottom = new FormAttachment(cpu_label, 50, SWT.BOTTOM);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(15, 0);
		ram_label.setLayoutData(f);

		scale_ram = new Scale(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(ram_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(ram_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(ram_label, 5, SWT.RIGHT);
		f.right = new FormAttachment(35, 0);
		scale_ram.setLayoutData(f);
		this.allBindedControls.put("scale_ram", scale_ram);

		spinner_ram = new Spinner(topC, SWT.BORDER);
		f = new FormData();
		f.top = new FormAttachment(ram_label, 5, SWT.TOP);
		f.bottom = new FormAttachment(ram_label, -5, SWT.BOTTOM);
		f.left = new FormAttachment(scale_cpu, 2, SWT.RIGHT);
		f.right = new FormAttachment(45, 0);
		spinner_ram.setLayoutData(f);
		this.allBindedControls.put("m.spinner_ram", spinner_ram);

		HHHLabel r_label = new HHHLabel(topC, SWT.NONE);
		r_label.setText("%");
		f = new FormData();
		f.top = new FormAttachment(ram_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(ram_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(spinner_ram, 0, SWT.RIGHT);
		f.right = new FormAttachment(50, 0);
		r_label.setLayoutData(f);

		HHHLabel ram_label2 = new HHHLabel(topC, SWT.NONE);
		ram_label2.setText("RAM使用率曲线图描述点可忽略值为≤");
		ram_label2.setLabelAlignment(SWT.RIGHT);
		f = new FormData();
		f.top = new FormAttachment(r_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(r_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(50, 0);
		f.right = new FormAttachment(85, 0);
		ram_label2.setLayoutData(f);

		txt_ram = new Text(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(ram_label2, 5, SWT.TOP);
		f.bottom = new FormAttachment(ram_label2, -5, SWT.BOTTOM);
		f.left = new FormAttachment(ram_label2, 5, SWT.RIGHT);
		f.right = new FormAttachment(95, 0);
		txt_ram.setLayoutData(f);
		this.allBindedControls.put("m.ignore_ram", txt_ram);

		HHHLabel r_lable2 = new HHHLabel(topC, SWT.NONE);
		r_lable2.setText("%");
		f = new FormData();
		f.top = new FormAttachment(ram_label2, 0, SWT.TOP);
		f.bottom = new FormAttachment(ram_label2, 0, SWT.BOTTOM);
		f.left = new FormAttachment(txt_ram, 0, SWT.RIGHT);
		f.right = new FormAttachment(100, 0);
		r_lable2.setLayoutData(f);

		HHHLabel hdd_label = new HHHLabel(topC, SWT.NONE);
		hdd_label.setText("硬盘使用率阀值");
		hdd_label.setLabelAlignment(SWT.RIGHT);
		f = new FormData();
		f.top = new FormAttachment(ram_label, 10, SWT.BOTTOM);
		f.bottom = new FormAttachment(ram_label, 50, SWT.BOTTOM);
		f.left = new FormAttachment(0, 0);
		f.right = new FormAttachment(15, 0);
		hdd_label.setLayoutData(f);

		scale_hdd = new Scale(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(hdd_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(hdd_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(hdd_label, 5, SWT.RIGHT);
		f.right = new FormAttachment(35, 0);
		scale_hdd.setLayoutData(f);
		this.allBindedControls.put("scale_hdd", scale_hdd);

		spinner_hdd = new Spinner(topC, SWT.BORDER);
		f = new FormData();
		f.top = new FormAttachment(hdd_label, 5, SWT.TOP);
		f.bottom = new FormAttachment(hdd_label, -5, SWT.BOTTOM);
		f.left = new FormAttachment(scale_hdd, 2, SWT.RIGHT);
		f.right = new FormAttachment(45, 0);
		spinner_hdd.setLayoutData(f);
		this.allBindedControls.put("m.spinner_hdd", spinner_hdd);

		HHHLabel h_label = new HHHLabel(topC, SWT.NONE);
		h_label.setText("%");
		f = new FormData();
		f.top = new FormAttachment(hdd_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(hdd_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(spinner_hdd, 0, SWT.RIGHT);
		f.right = new FormAttachment(50, 0);
		h_label.setLayoutData(f);
		
		HHHLabel hdd_label2 = new HHHLabel(topC, SWT.NONE);
		hdd_label2.setText("硬盘使用率曲线图描述点可忽略值为≤");
		hdd_label2.setLabelAlignment(SWT.RIGHT);
		f = new FormData();
		f.top = new FormAttachment(h_label, 0, SWT.TOP);
		f.bottom = new FormAttachment(h_label, 0, SWT.BOTTOM);
		f.left = new FormAttachment(50, 0);
		f.right = new FormAttachment(85, 0);
		hdd_label2.setLayoutData(f);

		txt_hdd = new Text(topC, SWT.NONE);
		f = new FormData();
		f.top = new FormAttachment(hdd_label2, 5, SWT.TOP);
		f.bottom = new FormAttachment(hdd_label2, -5, SWT.BOTTOM);
		f.left = new FormAttachment(hdd_label2, 5, SWT.RIGHT);
		f.right = new FormAttachment(95, 0);
		txt_hdd.setLayoutData(f);
		this.allBindedControls.put("m.ignore_hdd", txt_hdd);

		HHHLabel h_lable2 = new HHHLabel(topC, SWT.NONE);
		h_lable2.setText("%");
		f = new FormData();
		f.top = new FormAttachment(hdd_label2, 0, SWT.TOP);
		f.bottom = new FormAttachment(hdd_label2, 0, SWT.BOTTOM);
		f.left = new FormAttachment(txt_hdd, 0, SWT.RIGHT);
		f.right = new FormAttachment(100, 0);
		h_lable2.setLayoutData(f);

		initControlsInTopC();
	}

	private void initControlsInTopC() {
		this.scale_hdd.setMinimum(this.HDD_MIN);
		this.scale_hdd.setMaximum(this.HDD_MAX);
		this.scale_hdd.setIncrement(5);
		this.scale_hdd.setPageIncrement(5);
		this.scale_hdd.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinner_hdd.setSelection(scale_hdd.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				spinner_hdd.setSelection(scale_hdd.getSelection());
			}
		});

		this.scale_cpu.setMinimum(this.CPU_MIN);
		this.scale_cpu.setMaximum(this.CPU_MAX);
		this.scale_cpu.setIncrement(5);
		this.scale_cpu.setPageIncrement(5);
		this.scale_cpu.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinner_cpu.setSelection(scale_cpu.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				spinner_cpu.setSelection(scale_cpu.getSelection());
			}
		});

		this.scale_ram.setMinimum(this.RAM_MIN);
		this.scale_ram.setMaximum(this.RAM_MAX);
		this.scale_ram.setIncrement(5);
		this.scale_ram.setPageIncrement(5);
		this.scale_ram.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				spinner_ram.setSelection(scale_ram.getSelection());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				spinner_ram.setSelection(scale_ram.getSelection());
			}
		});

		this.spinner_hdd.setMinimum(this.HDD_MIN);
		this.spinner_hdd.setMaximum(this.HDD_MAX);
		this.spinner_hdd.setIncrement(5);
		this.spinner_hdd.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				int selection = spinner_hdd.getSelection();
				scale_hdd.setSelection(selection);
			}
		});

		this.spinner_cpu.setMinimum(this.CPU_MIN);
		this.spinner_cpu.setMaximum(this.CPU_MAX);
		this.spinner_cpu.setIncrement(5);
		this.spinner_cpu.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				int selection = spinner_cpu.getSelection();
				scale_cpu.setSelection(selection);
			}
		});

		this.spinner_ram.setMinimum(this.RAM_MIN);
		this.spinner_ram.setMaximum(this.RAM_MAX);
		this.spinner_ram.setIncrement(5);
		this.spinner_ram.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent event) {
				int selection = spinner_ram.getSelection();
				scale_ram.setSelection(selection);
			}
		});
	}
}
