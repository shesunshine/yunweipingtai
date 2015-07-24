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

public class ServerRunViewerCustomizer extends TableViewerCustomizer {

	private Log log = LogFactory.getLog(ServerRunViewerCustomizer.class);

	/**
	 * 默认构造函数
	 */
	public ServerRunViewerCustomizer() {
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

		String[] server_ids = new String[tv.getTable().getItems().length];
		for (int i = 0; i < tv.getTable().getItems().length; i++) {
			TableRowModel tableRowModel = (TableRowModel) tv.getTable().getItem(i).getData();

			TableModel tableModel = (TableModel) tv.getInput();
			Map data = tableRowModel.getData();
			String id = (String) data.get("a.server_id");
			
			server_ids[i] = id;
		}
		Map parameters = new HashMap();
		parameters.put("array", server_ids);
		
		List list = new ArrayList();
		try {
			list = DaoUtil.selectList("com.hhh.platform.ops.sql.ops_server_runMapper.selectServerRunNewest2", parameters);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for (int i = 0; i < tv.getTable().getItems().length; i++) {
			TableRowModel tableRowModel = (TableRowModel) tv.getTable().getItem(i).getData();

			TableModel tableModel = (TableModel) tv.getInput();
			Map data = tableRowModel.getData();

			String id = (String) data.get("a.server_id");
			try {
				for (int j = 0; j < list.size(); j++) {
					Map map = (Map) list.get(j);
					String server_id = (String) map.get("server_id");
					String type = (String) map.get("type");
					String use_percent = (String) map.get("use_percent");

					if (server_id.equals(id)) {
						if (OPSConstants.SERVER_RUN_HDD.equals(type)) {
							if (Integer.parseInt(use_percent) >= Integer.parseInt(ops_server_hdd_level)) {
								data.put("alarm", "<img src=\"/ops/images/alarm.gif\" width=\"16\" height=\"16\" />");
								break;
							}
						} else if (OPSConstants.SERVER_RUN_CPU.equals(type)) {
							if (Integer.parseInt(use_percent) >= Integer.parseInt(ops_server_cpu_level)) {
								data.put("alarm", "<img src=\"/ops/images/alarm.gif\" width=\"16\" height=\"16\" />");
								break;
							}
						} else if (OPSConstants.SERVER_RUN_RAM.equals(type)) {
							if (Integer.parseInt(use_percent) >= Integer.parseInt(ops_server_ram_level)) {
								data.put("alarm", "<img src=\"/ops/images/alarm.gif\" width=\"16\" height=\"16\" />");
								break;
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("获取id为" + id + "的服务器最新运行数据错误：" + e.getMessage());
				continue;
			}

			tableRowModel.setData(data);
			tableModel.updateTableRow(tableRowModel);
			tv.refresh(tableModel, true);
		}
	}
}
