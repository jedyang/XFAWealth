/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */

package com.fsll.common.persistence.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.fsll.common.persistence.SpringJdbcQueryManager;
import com.fsll.common.util.JsonPaging;

/**
 * 
 * Jdbc的特有查询接口
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
@Repository("springJdbcQueryManager")
public class SpringJdbcQueryManagerImpl implements SpringJdbcQueryManager {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	/**
	 * @param sql  SQL命令，没有参数
	 * @return List<Map>
	 */
	public List springJdbcQueryForList(String sql) {
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * @param sql  SQL命令，带有参数?。例如：select * from user where age > ? and gender = ?
	 * @param args  参数数组，与sql中?出现的顺序一致。
	 * @return List<Map>
	 */
	public List springJdbcQueryForList(String sql, Object[] args) {
		return jdbcTemplate.queryForList(sql, args);
	}

	/**
	 * @param sql SQL命令，带有参数?。例如：select name from user where id=?
	 * @param args参数数组，与sql中?出现的顺序一致。
	 * @param requiredType返回对象的类型
	 * @return 查询结果对象。 
	 */
	public Object springJdbcQueryForObject(String sql, Object[] args,Class requiredType) {
		return jdbcTemplate.queryForObject(sql, args, requiredType);
	}
	
	/**
	 * 执行sql
	 * @param SQL
	 */
	public void execute(String sql) {
		jdbcTemplate.execute(sql);
	}
	
	/**
	 * @param SQL SQL命令，带有参数?。例如：select name from user where id=?
	 * @param obj 参数数组，与sql中?出现的顺序一致。
	 * 
	 */
	public void update(String sql,Object[] obj) {
		jdbcTemplate.update(sql, obj);
	}
	
	/**
	 * 获得分页记录
	 * @param sql
	 * @param args
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging springJdbcQueryForPaging(String sql, Object[] args,JsonPaging jsonPaging) {
		int pageSize = jsonPaging.getRows();
		int curPage = jsonPaging.getPage();
		String order = jsonPaging.getOrderStr();
		String sqlTmp = sql + order + " limit " + (curPage-1)*pageSize + " , " + pageSize;
		List list = jdbcTemplate.queryForList(sqlTmp, args);
		jsonPaging.setList((ArrayList) list);
		//jsonPaging.setTotal( list == null?0: list.size());
		jsonPaging.setTotal(Integer.parseInt((springJdbcQueryForTotal(sql,args)).toString()));
		return jsonPaging;
	}
	
	/**
	 * 计算分页记录数
	 * @param sql
	 * @param args
	 * @param jsonPaging
	 * @return
	 */
	public Long springJdbcQueryForTotal(String sql, Object[] args) {
		String sqlTmp = "select count(*) as totalNum "+ sql;
		//modified by michael start
		if (sql.toUpperCase().indexOf("SELECT")>=0) 
			sqlTmp = "select count(*) as totalNum " + sql.substring(sql.toUpperCase().indexOf("FROM"));
		//modified by michael end
		List list = jdbcTemplate.queryForList(sqlTmp, args);
		Map map = (HashMap)list.get(0);
		return Long.parseLong(map.get("totalNum").toString());
	}
	
}
