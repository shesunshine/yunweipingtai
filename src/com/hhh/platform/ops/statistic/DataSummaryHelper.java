package com.hhh.platform.ops.statistic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hhh.platform.ops.util.DaoUtil;
import com.hhh.platform.util.UUIDUitil;

public class DataSummaryHelper {

	public static void statisticLogExeption() throws Exception {
		String sql = "com.hhh.platform.ops.sql.opsMapper.statisticLogExeption";
		List list = DaoUtil.selectList(sql, new HashMap());
		List insertList = new ArrayList();
		for (int i = 0; i < list.size(); i++) {
			Map map = (Map) list.get(i);
			String uuid = UUIDUitil.getUUID();
			map.put("e_dp_all_id", uuid);
			insertList.add(map);
		}

		Map parameters = new HashMap();
		parameters.put("list", insertList);

		String insertSql = "com.hhh.platform.ops.sql.ops_stat_e_dp_allMapper.insertExceptionStatistic";
		DaoUtil.insert(insertSql, parameters);

	}
}
