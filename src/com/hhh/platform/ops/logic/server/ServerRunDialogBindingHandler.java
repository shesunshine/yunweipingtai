package com.hhh.platform.ops.logic.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.xml.crypto.dsig.keyinfo.KeyName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.rap.rwt.RWT;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.advisors.framework.binding.DefaultBindingHandler;
import com.hhh.platform.advisors.page.TableViewerCustomizer;
import com.hhh.platform.advisors.table.TableContentProvider;
import com.hhh.platform.advisors.table.TableLabelProvider;
import com.hhh.platform.advisors.table.TableModel;
import com.hhh.platform.advisors.tree.LazyTreeContentProvider;
import com.hhh.platform.advisors.tree.TreeLabelProvider;
import com.hhh.platform.cache.CacheService;
import com.hhh.platform.cache.CacheServiceUtil;
import com.hhh.platform.ops.entity.ServerRunEntity;
import com.hhh.platform.ops.ui.server.ServerRunViewerTableLabelProvider;
import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.ops.util.MessageUtil;
import com.hhh.platform.ops.util.ServerRunDialogViewerCustomizer;
import com.hhh.platform.ops.util.SystemSettingHelper;
import com.hhh.platform.util.ObjectCache;

public class ServerRunDialogBindingHandler extends DefaultBindingHandler {
	private static Log log = LogFactory.getLog(ServerRunDialogBindingHandler.class);

	private String operationId;
	private ServerRunEntity entity;
	private String currentServerId;
	private HashMap<String, Object> savedValues = new HashMap<String, Object>();

	private TableViewer hddViewer;
	private TreeViewer dateTimeViewer;
	private Browser cpuBrowser;
	private Browser ramBrowser;
	private Browser hddBrowser;

	public ServerRunDialogBindingHandler(String operationId, ServerRunEntity entity) {
		this.operationId = operationId;
		this.entity = entity;
	}

	@Override
	public boolean initControls() {
		currentServerId = this.entity.getSelectedIDs()[0];
		changeXMFolderBottom();

		initDateTime();
		initTabForCPU();
		initTabForRAM();
		initTabForHDD();
		initDateTimeTree();
		initRunView();

		addListenerToFolder();

		this.defaultListener.bindControls(this.allBindedControls);
		// 绘制cpu的图
		this.drawCPUPicture();

		return true;
	}

	private void addListenerToFolder() {
		CTabFolder folder = (CTabFolder) allBindedControls.get("serverFolder");
		folder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				CTabItem item = (CTabItem) event.item;
				String key = (String) item.getData("binding");
				if (key != null) {
					if (key.equals("cpu_item")) {
						drawCPUPicture();
					} else if (key.equals("ram_item")) {
						drawRAMPicture();
					} else if (key.equals("hdd_item")) {
						drawHDDPicture();
					} else if (key.equals("table_item")) {
						buildTree();
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
	}

	private void initTabForCPU() {
		Composite cpu_c = (Composite) this.allBindedControls.get("cpu_c");
		cpu_c.setLayout(new FillLayout());
		log.info(cpu_c.getSize());
		cpuBrowser = new Browser(cpu_c, SWT.NONE);

	}

	private void initTabForRAM() {
		Composite ram_c = (Composite) this.allBindedControls.get("ram_c");
		ram_c.setLayout(new FillLayout());
		log.info(ram_c.getSize());
		ramBrowser = new Browser(ram_c, SWT.NONE);

	}

	private void initTabForHDD() {
		Composite hdd_c = (Composite) this.allBindedControls.get("hdd_c");
		hdd_c.setLayout(new FillLayout());
		log.info(hdd_c.getSize());
		hddBrowser = new Browser(hdd_c, SWT.NONE);
	}

	private void drawCPUPicture() {
		try {
			String chartData = getCPUChartData();
			ObjectCache.getInstance().loadObjToCache("cpu_c", chartData);
			cpuBrowser.setUrl("/highchart?objID=" + "cpu_c");
			// cpuBrowser.redraw();
		} catch (Exception e) {
			String message = "读取CPU图表配置文件错误";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
		}
	}

	private void drawRAMPicture() {
		try {
			String chartData = getRAMChartData();
			ObjectCache.getInstance().loadObjToCache("ram_c", chartData);
			ramBrowser.setUrl("/highchart?objID=" + "ram_c");
			ramBrowser.redraw();
		} catch (Exception e) {
			String message = "读取RAM图表配置文件错误";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
		}
	}

	private void drawHDDPicture() {
		try {
			String chartData = getHDDChartData();
			ObjectCache.getInstance().loadObjToCache("hdd_c", chartData);
			hddBrowser.setUrl("/highchart?objID=" + "hdd_c");
			hddBrowser.redraw();
		} catch (Exception e) {
			String message = "读取HDD图表配置文件错误";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
		}
	}

	private boolean isIgnore(String sourcePercent, String ignorePercent) {
		double sourcePercentD = 0.0;
		double ignorePercentD = 0.0;
		try {
			sourcePercentD = Double.parseDouble(sourcePercent);
			ignorePercentD = Double.parseDouble(ignorePercent);
		} catch (NumberFormatException e) {
			return false;
		}
		if (sourcePercentD <= ignorePercentD) {
			return true;
		}
		return false;
	}

	private String getCPUChartData() throws Exception {
		CacheService cache = CacheServiceUtil.getCacheService();
		String ops_server_cpu_level = (String) cache.get(SystemSettingHelper.OPS_SERVER_CPU_LEVEL_KEY);
		String ops_server_cpu_ignore = (String) cache.get(SystemSettingHelper.OPS_SERVER_CPU_IGNORE_KEY);

		List resultList = getServerRunByDate("CPU", "CPU", false);
		List ignoredList = new ArrayList();
		for (int i = 0; i < resultList.size(); i++) {
			Map map = (Map) resultList.get(i);
			String use_percent = (String) map.get("use_percent");
			if (use_percent == null)
				continue;

			if (isIgnore(use_percent, ops_server_cpu_ignore)) {
				continue;
			}
			ignoredList.add(map);
		}
		List<String> xArr = new ArrayList<String>();
		List<String> yArr = new ArrayList<String>();
		for (int i = 0; i < ignoredList.size(); i++) {
			Map map = (Map) ignoredList.get(i);
			String add_date_time = (String) map.get("add_date_time");
			String use_percent = (String) map.get("use_percent");
			if (use_percent == null)
				continue;
			if (isIgnore(use_percent, ops_server_cpu_ignore)) {
				continue;
			}

			xArr.add(add_date_time);
			yArr.add(use_percent);
		}

		StringBuilder sb = new StringBuilder();
		// 基本设置，type：曲线类型 credits：版权
		sb.append("chart: {type: 'spline'},title: {text: 'CPU使用率（%）'},credits: {text: '3H运维平台',href: ''},");
		// 横坐标
		sb.append("xAxis: {categories: [");
		// 现画图还是用顺序
		for (int i = 0; i < xArr.size(); i++) {
			sb.append("'" + xArr.get(i) + "'");
			if (i != xArr.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("],tickmarkPlacement: 'on',tickLength:7,tickPositions: [0, " + ignoredList.size() / 2 + "]},");
		// 纵坐标
		sb.append("yAxis: {title: {text: '使用率（%）'},tickPositions: [0,20,40,60,80,100],");
		// 纵坐标基准线
		sb.append("plotLines: [{value:" + ops_server_cpu_level + ",color: 'red',width: 2,label: {text: 'CPU阀值:" + ops_server_cpu_level + "%',align: 'center',style: {color: 'gray'}}}]},");
		// tooltip为鼠标放到点上的提示，legend为图例说明
		sb.append("tooltip: {valueSuffix: '%'},legend: {layout: 'horizontal',align: 'center',verticalAlign: 'bottom',borderWidth: 0},");
		// 点的属性
		sb.append("plotOptions: {spline: {lineWidth: 2,marker: {enabled: false,	states:{hover:{radius:4}}},}},");
		// Y轴数据
		sb.append("series: [{name: 'CPU使用率',data: [");
		// 现画图还是用顺序
		for (int i = 0; i < yArr.size(); i++) {
			sb.append(yArr.get(i));
			if (i != yArr.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]}]");
		return sb.toString();
	}

	private String getRAMChartData() throws Exception {
		CacheService cache = CacheServiceUtil.getCacheService();
		String ops_server_ram_level = (String) cache.get(SystemSettingHelper.OPS_SERVER_RAM_LEVEL_KEY);
		String ops_server_ram_ignore = (String) cache.get(SystemSettingHelper.OPS_SERVER_RAM_IGNORE_KEY);

		List resultList = getServerRunByDate("RAM", "RAM", false);
		List ignoredList = new ArrayList();
		for (int i = 0; i < resultList.size(); i++) {
			Map map = (Map) resultList.get(i);
			String use_percent = (String) map.get("use_percent");
			if (use_percent == null)
				continue;

			if (isIgnore(use_percent, ops_server_ram_ignore)) {
				continue;
			}
			ignoredList.add(map);
		}
		List<String> xArr = new ArrayList<String>();
		List<String> yArr = new ArrayList<String>();
		for (int i = 0; i < ignoredList.size(); i++) {
			Map map = (Map) ignoredList.get(i);
			String add_date_time = (String) map.get("add_date_time");
			String use_percent = (String) map.get("use_percent");
			xArr.add(add_date_time);
			yArr.add(use_percent);
		}

		StringBuilder sb = new StringBuilder();
		// 基本设置，type：曲线类型 credits：版权
		sb.append("chart: {type: 'spline'},title: {text: 'RAM使用率（%）'},credits: {text: '3H运维平台',href: ''},");
		// 横坐标
		sb.append("xAxis: {categories: [");
		// 现画图还是用顺序
		for (int i = 0; i < xArr.size(); i++) {
			sb.append("'" + xArr.get(i) + "'");
			if (i != xArr.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("],tickmarkPlacement: 'on',tickLength:7,tickPositions: [0, " + ignoredList.size() / 2 + "]},");
		// 纵坐标
		sb.append("yAxis: {title: {text: '使用率（%）'},tickPositions: [0,20,40,60,80,100],");
		// 纵坐标基准线
		sb.append("plotLines: [{value:" + ops_server_ram_level + ",color: 'red',width: 2,label: {text: 'RAM阀值:" + ops_server_ram_level + "%',align: 'center',style: {color: 'gray'}}}]},");
		// tooltip为鼠标放到点上的提示，legend为图例说明
		sb.append("tooltip: {valueSuffix: '%'},legend: {layout: 'horizontal',align: 'center',verticalAlign: 'bottom',borderWidth: 0},");
		// 点的属性
		sb.append("plotOptions: {spline: {lineWidth: 2,marker: {enabled: false,	states:{hover:{radius:4}}},}},");
		// Y轴数据
		sb.append("series: [{name: 'RAM使用率',data: [");
		// 现画图还是用顺序
		for (int i = 0; i < yArr.size(); i++) {
			sb.append(yArr.get(i));
			if (i != yArr.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]}]");
		return sb.toString();
	}

	private String getHDDChartData() throws Exception {
		CacheService cache = CacheServiceUtil.getCacheService();
		String ops_server_hdd_level = (String) cache.get(SystemSettingHelper.OPS_SERVER_HDD_LEVEL_KEY);
		String ops_server_hdd_ignore = (String) cache.get(SystemSettingHelper.OPS_SERVER_HDD_IGNORE_KEY);
		// 全部硬盘的使用率
		List resultList = getServerRunByDate(null, "HDD", false);
		List ignoredList = new ArrayList();
		for (int i = 0; i < resultList.size(); i++) {
			Map map = (Map) resultList.get(i);
			String use_percent = (String) map.get("use_percent");
			if (use_percent == null)
				continue;

			if (isIgnore(use_percent, ops_server_hdd_ignore)) {
				continue;
			}
			ignoredList.add(map);
		}
		// 盘符种类
		List<String> keyList = new ArrayList<String>();
		// 硬盘使用率对应的时间
		List<String> dateList = new ArrayList<String>();
		for (int i = 0; i < ignoredList.size(); i++) {
			Map map = (Map) ignoredList.get(i);
			String keyName = (String) map.get("key_name");
			String add_date_time = (String) map.get("add_date_time");

			// 如果该数据的时间没有被记载，则记载
			if (!dateList.contains(add_date_time)) {
				dateList.add(add_date_time);
			}

			if (keyName != null) {
				if (!keyList.contains(keyName)) {
					keyList.add(keyName);
				}
			}
		}

		// 不同硬盘的使用率，key为硬盘符，value为使用率，如果该盘符在该时间点没有数据的话，用0来代替
		Map<String, String[]> percentMap = new LinkedHashMap<String, String[]>();

		for (int i = 0; i < keyList.size(); i++) {
			String keyName = keyList.get(i);
			// 创建时间轴长度的数组，初始化都是null
			String[] percent = new String[dateList.size()];
			for (int j = 0; j < dateList.size(); j++) {
				String add_date_time = dateList.get(j);
				for (int k = 0; k < ignoredList.size(); k++) {
					Map map = (Map) ignoredList.get(k);
					String keyNameTemp = (String) map.get("key_name");
					String add_date_timeTemp = (String) map.get("add_date_time");
					String use_percent = (String) map.get("use_percent");
					if (use_percent == null)
						use_percent = "0";
					// 将时间符合的转为目标值
					if (keyName.equals(keyNameTemp) && add_date_time.equals(add_date_timeTemp)) {
						percent[j] = use_percent;
					}
				}
			}

			percentMap.put(keyName, percent);
		}

		List<String> xArr = new ArrayList<String>();
		for (int i = 0; i < dateList.size(); i++) {
			String add_date_time = dateList.get(i);
			xArr.add(add_date_time);
		}

		StringBuilder sb = new StringBuilder();
		// 基本设置，type：曲线类型 credits：版权
		sb.append("chart: {type: 'spline'},title: {text: 'HDD使用率（%）'},credits: {text: '3H运维平台',href: ''},");
		// 横坐标
		sb.append("xAxis: {categories: [");
		// 现画图还是用顺序
		for (int i = 0; i < xArr.size(); i++) {
			sb.append("'" + xArr.get(i) + "'");
			if (i != xArr.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("],tickmarkPlacement: 'on',tickLength:7,tickPositions: [0, " + dateList.size() / 2 + "]},");
		// 纵坐标
		sb.append("yAxis: {title: {text: '使用率（%）'},tickPositions: [0,20,40,60,80,100],");
		// 纵坐标基准线
		sb.append("plotLines: [{value:" + ops_server_hdd_level + ",color: 'red',width: 2,label: {text: 'HDD阀值:" + ops_server_hdd_level + "%',align: 'center',style: {color: 'gray'}}}]},");
		// tooltip为鼠标放到点上的提示，legend为图例说明
		sb.append("tooltip: {valueSuffix: '%'},legend: {layout: 'horizontal',align: 'center',verticalAlign: 'bottom',borderWidth: 0},");
		// 点的属性
		sb.append("plotOptions: {spline: {lineWidth: 2,marker: {enabled: false,states:{hover:{radius:4}}},}},");
		// Y轴数据
		if (percentMap.size() > 0) {
			sb.append("series: [");
			for (Entry<String, String[]> entry : percentMap.entrySet()) {
				String key = entry.getKey();
				String[] percentArr = entry.getValue();

				// 此处有个细节，key是类似"C:\"这样的数据，后面的斜杠要转义一次，才能正常显示整个图片
				// 这点非常重要，算是个bug吧。
				sb.append("{name: '" + key + "\\',data: [");
				// 现画图还是用顺序
				for (int i = 0; i < percentArr.length; i++) {
					if (percentArr[i] == null) {
						sb.append("0");
					} else {
						sb.append(percentArr[i]);
					}
					if (i != percentArr.length - 1) {
						sb.append(",");
					}
				}
				sb.append("]},");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("]");
		} else {
			sb.append("series: [{name: '无数据',data: [");
			sb.append("]}]");
		}
		return sb.toString();
	}

	/**
	 * 从cpu_chart.txt文件中读取json配置 用来测试用
	 * 
	 * @return
	 * @throws IOException
	 */
	private String getChartDataFromFile() throws IOException {
		StringBuffer strBuffer = new StringBuffer();

		String modelFilePath = "com/hhh/platform/ops/logic/server/cpu_chart.txt";
		InputStream xmlis = getClass().getClassLoader().getResourceAsStream(modelFilePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(xmlis));

		String line = null;
		while ((line = br.readLine()) != null) {
			strBuffer.append(line.trim());
		}
		return strBuffer.toString();
	}

	// 以日期为一级节点，时间为二级节点的树
	private void buildTree() {
		dateTimeViewer.setInput(null);
		hddViewer.setInput(null);

		TreeMap<String, List<String>> timeMap = new TreeMap<String, List<String>>();
		List resultList = new ArrayList();
		;
		try {
			resultList = getServerRunByDate(null, null, true);
		} catch (Exception e) {
			String message = "获取监控信息失败";
			MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			return;
		}
		List<String> dateList = new ArrayList<String>();
		if (resultList != null) {
			for (int i = 0; i < resultList.size(); i++) {
				Map resultMap = (Map) resultList.get(i);
				String date = (String) resultMap.get("add_date");
				String time = (String) resultMap.get("add_time");

				if (!dateList.contains(date)) {
					dateList.add(date);
					List<String> timeList = new ArrayList<String>();
					timeList.add(time);
					timeMap.put(date, timeList);
				} else {
					List<String> timeList = timeMap.get(date);
					if (!timeList.contains(time)) {
						timeList.add(time);
					}
				}
			}

			for (String itor : timeMap.descendingKeySet()) {
				List<String> timeList = timeMap.get(itor);

				TreeItem item = new TreeItem(dateTimeViewer.getTree(), SWT.NONE);
				item.setText(itor);
				for (int i = 0; i < timeList.size(); i++) {
					TreeItem subItem = new TreeItem(item, SWT.NONE);
					subItem.setText(timeList.get(i));
				}
			}
		}

		this.dateTimeViewer.getTree().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TreeItem item = (TreeItem) e.item;
				if (item.getParentItem() != null) {
					String date = item.getParentItem().getText();
					String time = item.getText();
					try {
						List list = getServerRunByTime(date, time);
						TableModel tableItemList = new TableModel(list);
						hddViewer.setInput(tableItemList);

						TableViewerCustomizer customizer = new ServerRunDialogViewerCustomizer();
						customizer.customize(hddViewer);

					} catch (Exception e1) {
						String message = "查询服务器监控信息发生异常";
						MessageUtil.showErrorDialog(e1, log, message, MessageUtil.ERROR_TYPE_OPERATION);
					}
				} else {
					item.setExpanded(!item.getExpanded());
				}
			}
		});
	}

	private List getServerRunByTime(String date, String time) throws Exception {
		Map parameter = new HashMap();
		parameter.put("add_date", date);
		parameter.put("add_time", time);
		parameter.put("server_id", currentServerId);

		String sql = "com.hhh.platform.ops.sql.ops_server_runMapper.selectServerByTime";
		List resultList = DaoUtil.selectList(sql, parameter);
		return resultList;
	}

	// 按时间分组查找日志
	private List getServerRunByDate(String keyName, String type, boolean timeOrderDesc) throws Exception {
		Map parameter = new HashMap();
		Date date_start = (Date) BindingHelper.getValue(this.allBindedControls.get("date_start"));
		Date date_end = (Date) BindingHelper.getValue(this.allBindedControls.get("date_end"));
		parameter.put("date_start", date_start);
		parameter.put("date_end", date_end);
		parameter.put("server_id", currentServerId);

		parameter.put("key_name", keyName);
		parameter.put("type", type);
		if (timeOrderDesc) {
			parameter.put("order", "desc");
		}

		String sql = "com.hhh.platform.ops.sql.ops_server_runMapper.selectServerByDate";
		List resultList = DaoUtil.selectList(sql, parameter);
		return resultList;
	}

	private void initDateTimeTree() {
		Composite date_time_c = (Composite) this.allBindedControls.get("date_time_c");
		date_time_c.setLayout(new FillLayout());

		dateTimeViewer = new TreeViewer(date_time_c, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE);
		TreeColumn col1 = new TreeColumn(dateTimeViewer.getTree(), SWT.BORDER);
		col1.setText("监控时间");
		col1.setResizable(false);
		col1.setMoveable(false);
		col1.setWidth(180);
		dateTimeViewer.setContentProvider(new LazyTreeContentProvider());
		dateTimeViewer.setLabelProvider(new TreeLabelProvider());
		dateTimeViewer.setUseHashlookup(true);
		dateTimeViewer.getTree().setHeaderVisible(true);
		dateTimeViewer.setInput(null);

		date_time_c.layout(true, true);
		date_time_c.getParent().layout(true, true);
	}

	private void initRunView() {
		Composite hdd_view_c = (Composite) this.allBindedControls.get("hdd_view");
		hdd_view_c.setLayout(new FillLayout());

		hddViewer = new TableViewer(hdd_view_c, SWT.NONE);
		hddViewer.setContentProvider(new TableContentProvider());
		TableLabelProvider tableLabelProvider = new ServerRunViewerTableLabelProvider(getColumnFieldNames());
		tableLabelProvider.setColumnStyleMap(getColumnStyleMap());
		hddViewer.setLabelProvider(tableLabelProvider);
		String[] columnProperties = getColumnProperties();
		setColumnsWidth(columnProperties);
		hddViewer.setColumnProperties(columnProperties);
		hddViewer.getTable().setHeaderVisible(true);
		hddViewer.getTable().getParent().getParent().layout(true);
		hddViewer.getTable().setData(RWT.MARKUP_ENABLED, true);

	}

	private void initDateTime() {
		// 日期初始化
		Control date_start_ctl = allBindedControls.get("date_start");
		Control date_end_ctl = allBindedControls.get("date_end");
		if (date_start_ctl instanceof DateTime && date_end_ctl instanceof DateTime) {
			Calendar lastDay = Calendar.getInstance();
			lastDay.add(Calendar.DAY_OF_YEAR, -1);
			((DateTime) date_start_ctl).setDay(lastDay.get(Calendar.DAY_OF_MONTH));
			((DateTime) date_start_ctl).setMonth(lastDay.get(Calendar.MONTH));
			((DateTime) date_start_ctl).setYear(lastDay.get(Calendar.YEAR));
			((DateTime) date_start_ctl).setHours(0);
			((DateTime) date_start_ctl).setMinutes(0);
			((DateTime) date_start_ctl).setSeconds(0);
			((DateTime) date_end_ctl).setHours(23);
			((DateTime) date_end_ctl).setMinutes(59);
			((DateTime) date_end_ctl).setSeconds(59);
		}
	}

	private String[] getColumnProperties() {
		String[] properties = new String[] { "", "报警", "内容", "总空间", "剩余空间", "使用率", };
		return properties;
	}

	private Map<String, String> getColumnStyleMap() {
		return Collections.EMPTY_MAP;
	}

	private String[] getColumnFieldNames() {
		String[] columnFieldNames = new String[] { "", "alarm", "key_name", "total_store", "rest_store", "use_percent" };
		return columnFieldNames;
	}

	private void setColumnsWidth(String[] columnProperties) {
		for (int i = 0; i < columnProperties.length; i++) {
			TableColumn tableColumn = new TableColumn(hddViewer.getTable(), SWT.NONE);
			tableColumn.setText(columnProperties[i]);
			tableColumn.setAlignment(SWT.CENTER);
			String fieldName = getColumnFieldNames()[i];
			if (fieldName.equals("key_name")) {
				tableColumn.setWidth(80);
			} else if (fieldName.equals("total_store")) {
				tableColumn.setWidth(120);
			} else if (fieldName.equals("rest_store")) {
				tableColumn.setWidth(120);
			} else if (fieldName.equals("use_percent")) {
				tableColumn.setWidth(120);
			} else if (fieldName.equals("alarm")) {
				tableColumn.setWidth(50);
				tableColumn.setResizable(false);
			} else {
				tableColumn.setWidth(0);
			}
		}
	}

	@Override
	public boolean perform(String action) {
		return true;
	}

	@Override
	public void focusLost(String fieldName, Control ctl) {
	}

	@Override
	public void widgetSelected(String fieldName, Control ctl) {
		if (fieldName.equals("search_b")) {
			try {
				CTabFolder folder = (CTabFolder) allBindedControls.get("serverFolder");
				String key = (String) folder.getSelection().getData("binding");
				if (key != null) {
					if (key.equals("cpu_item")) {
						drawCPUPicture();
					} else if (key.equals("ram_item")) {
						drawRAMPicture();
					} else if (key.equals("hdd_item")) {
						drawHDDPicture();
					} else if (key.equals("table_item")) {
						buildTree();
					}
				}
			} catch (Exception e) {
				String message = "初始化服务器监控日志信息发生异常";
				MessageUtil.showErrorDialog(e, log, message, MessageUtil.ERROR_TYPE_OPERATION);
			}
		}
	}

	@Override
	public boolean veryfyField(String fieldName, Control ctl, boolean force) {
		return true;
	}

	private void changeXMFolderBottom() {
		CTabFolder folder = (CTabFolder) allBindedControls.get("serverFolder");
		if (null != folder) {
			FormData formData = (FormData) folder.getLayoutData();
			formData.bottom = new FormAttachment(100, 0);
		}
	}
}
