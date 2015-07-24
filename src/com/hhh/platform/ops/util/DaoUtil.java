package com.hhh.platform.ops.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;

import com.hhh.platform.advisors.framework.binding.BindingHelper;
import com.hhh.platform.orm.mybatis.SQLSessionService;
import com.hhh.platform.orm.mybatis.SQLSessionServiceUtil;
import com.hhh.platform.util.UUIDUitil;

public class DaoUtil {
	private static Log log = LogFactory.getLog(DaoUtil.class);

	public static void insert(String sql, Map parameters, String uuidKey) throws Exception {
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);
			parameters.put(uuidKey, UUIDUitil.getUUID());
			session.insert(sql, parameters);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行插入语句"+sql+"错误:"+e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
	}
	
	public static void insert(String sql, Map parameters) throws Exception {
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);
			session.insert(sql, parameters);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行插入语句"+sql+"错误:"+e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
	}
	
	public static void delete(String sql, Map parameters) throws Exception {
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);
			session.delete(sql, parameters);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行删除语句"+sql+"错误:"+e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
	}

	public static Map selectOne(String sql, Map<String, Object> parameters) throws Exception {
		Map resultMap = new HashMap();
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main");
			resultMap = (Map) session.selectOne(sql, parameters);
			if (resultMap!=null) {
				resultMap = BindingHelper.transferKeyToLowerCase((HashMap<String, Object>) resultMap);
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
		return resultMap;
	}

	public static List selectList(String sql, Map<String, Object> parameters) throws Exception {
		List resultList = new ArrayList();
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main");
			resultList = session.selectList(sql, parameters);
			if (resultList!=null) {
				resultList = BindingHelper.transferKeyToLowerCase(resultList);
			}
		} catch (Exception e) {
			log.error("执行查询语句"+sql+"错误:"+e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
		return resultList;
	}

	public static void update(String sql, Map parameters) throws Exception {
		SqlSession session = null;
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);
			session.update(sql, parameters);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行修改语句"+sql+"错误:"+e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
	}
	
	public static void update(String[] sqls, Map[] parameters) throws Exception {
		SqlSession session = null;
		String sql= "";
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);
			
			for (int i = 0; i < sqls.length; i++) {
				sql = sqls[i];
				session.update(sql, parameters[i]);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行修改语句"+sql+"错误:"+e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
	}
	
	public static void delete(String[] sqls, Map[] parameters) throws Exception {
		SqlSession session = null;
		String sql= "";
		try {
			SQLSessionService sessionService = SQLSessionServiceUtil.getSQLSessionService();
			session = sessionService.getSession("main", false);
			
			for (int i = 0; i < sqls.length; i++) {
				sql = sqls[i];
				session.delete(sql, parameters[i]);
			}
			session.commit();
		} catch (Exception e) {
			session.rollback();
			log.error("执行修改语句"+sql+"错误:"+e.getMessage());
			throw new Exception(e);
		} finally {
			if (session != null) {
				try {
					session.close();
					session = null;
				} catch (Exception e) {
				}
			}
		}
	}

}
