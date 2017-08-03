/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */

package com.fsll.common.persistence;

import java.util.List;

import com.fsll.common.util.JsonPaging;

/**
 * 
 * Jdbc的特有查询接口
 *  * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0
 * @date   2016年2月15日
 */
public interface SpringJdbcQueryManager {
	/**
	 * @param sql  SQL命令，没有参数
	 * @return List<Map>
	 */
	public List springJdbcQueryForList(String sql);

	/**
	 * @param sql  SQL命令，带有参数?。例如：select * from user where age > ? and gender = ?
	 * @param args  参数数组，与sql中?出现的顺序一致。
	 * @return List<Map>
	 */
	public List springJdbcQueryForList(String sql, Object[] args);

	/**
	 * @param sql SQL命令，带有参数?。例如：select name from user where id=?
	 * @param args参数数组，与sql中?出现的顺序一致。
	 * @param requiredType返回对象的类型
	 * @return 查询结果对象。 
	 */
	public Object springJdbcQueryForObject(String sql, Object[] args,Class requiredType);

	/**
	 * 执行sql
	 * @param SQL
	 */
	public void execute(String sql);

	/**
	 * @param SQL SQL命令，带有参数?。例如：select name from user where id=?
	 * @param obj 参数数组，与sql中?出现的顺序一致。
	 * 
	 */
	public void update(String sql, Object[] obj);
	
	/**
	 * 获得分页记录
	 * @param sql
	 * @param args
	 * @param jsonPaging
	 * @return
	 */
	public JsonPaging springJdbcQueryForPaging(String sql, Object[] args,JsonPaging jsonPaging);
	
	/**
	 * 计算分页记录数
	 * @param sql
	 * @param args
	 * @param jsonPaging
	 * @return
	 */
	public Long springJdbcQueryForTotal(String sql, Object[] args);

}
