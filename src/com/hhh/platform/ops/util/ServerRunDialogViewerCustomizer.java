package com.hhh.platform.ops.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import com.hhh.platform.advisors.framework.FrameworkConstants;
import com.hhh.platform.advisors.page.TableViewerCustomizer;
import com.hhh.platform.advisors.table.TableLabelProvider;
import com.hhh.platform.advisors.table.TableModel;
import com.hhh.platform.advisors.table.TableRowModel;
import com.hhh.platform.cache.CacheService;
import com.hhh.platform.cache.CacheServiceUtil;
import com.hhh.platform.ops.ui.server.ServerRunViewerTableLabelProvider;
import com.hhh.platform.util.ImageUtil;

public class ServerRunDialogViewerCustomizer extends TableViewerCustomizer {

	private Log log = LogFactory.getLog(ServerRunDialogViewerCustomizer.class);

	/**
	 * 默认构造函数
	 */
	public ServerRunDialogViewerCustomizer() {
		super();
	}

	@Override
	public void customize(TableViewer tv) {
		customizeData(tv);
	}

	private void customizeData(TableViewer tv) {

		CacheService cache = CacheServiceUtil.getCacheService();

		String ops_server_hdd_level = (String) cache.get(SystemSettingHelper.OPS_SERVER_HDD_LEVEL_KEY);
		String ops_server_cpu_level = (String) cache.get(SystemSettingHelper.OPS_SERVER_CPU_LEVEL_KEY);
		String ops_server_ram_level = (String) cache.get(SystemSettingHelper.OPS_SERVER_RAM_LEVEL_KEY);

		Image image = ImageUtil.loadImage("icons/alarm.gif", ServerRunViewerTableLabelProvider.class.getClassLoader());

		for (int i = 0; i < tv.getTable().getItems().length; i++) {
			TableRowModel tableRowModel = (TableRowModel) tv.getTable().getItem(i).getData();

			TableModel tableModel = (TableModel) tv.getInput();
			Map data = tableRowModel.getData();

			String type = (String) data.get("type");
			String use_percent = (String) data.get("use_percent");

			if (OPSConstants.SERVER_RUN_HDD.equals(type)) {
				if (Integer.parseInt(use_percent) >= Integer.parseInt(ops_server_hdd_level)) {
					data.put("alarm", "<img src=\"/ops/images/alarm.gif\" width=\"16\" height=\"16\" />");
				}
			} else if (OPSConstants.SERVER_RUN_CPU.equals(type)) {
				if (Integer.parseInt(use_percent) >= Integer.parseInt(ops_server_cpu_level)) {
					data.put("alarm", "<img src=\"/ops/images/alarm.gif\" width=\"16\" height=\"16\" />");
				}
			} else if (OPSConstants.SERVER_RUN_RAM.equals(type)) {
				if (Integer.parseInt(use_percent) >= Integer.parseInt(ops_server_ram_level)) {
					data.put("alarm", "<img src=\"/ops/images/alarm.gif\" width=\"16\" height=\"16\" />");
				}
			}

			tableRowModel.setData(data);
			tableModel.updateTableRow(tableRowModel);
			tv.refresh(tableModel, true);

		}
	}
}
