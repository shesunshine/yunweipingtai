package com.hhh.platform.ops.ui.server;

import java.util.Map;

import org.eclipse.swt.graphics.Image;

import com.hhh.platform.advisors.table.TableLabelProvider;
import com.hhh.platform.advisors.table.TableRowModel;
import com.hhh.platform.cache.CacheService;
import com.hhh.platform.cache.CacheServiceUtil;
import com.hhh.platform.ops.util.OPSConstants;
import com.hhh.platform.ops.util.SystemSettingHelper;
import com.hhh.platform.util.ImageUtil;

public class ServerRunViewerTableLabelProvider extends TableLabelProvider {
	public ServerRunViewerTableLabelProvider(String[] columnFieldNames) {
		super(columnFieldNames);
	}

	public Image getColumnImage(Object element, int columnIndex) {
		
		return null;
	}
}
