package com.fsll.wmes.web.service;

import java.util.List;
import java.util.Map;

import com.fsll.core.entity.SysParamConfig;

/***
 * 业务接口类：网站管理
 * @author wwluo
 * @date 2016-08-01
 */
public interface WebSiteService {

	/**
	 * 根据关联类型查询基础数据
	 * @author wwluo
     * @date 2016-08-01
	 */
	public List<Object> findByCode(String typeCode) ;
	
	/***
     * 更新设置
     * @author wwluo
     * @date 2016-08-01
     */
	public int update(List<Map> list,String lang);
}
