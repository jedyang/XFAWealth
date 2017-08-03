package com.fsll.core.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysSite;
/***
 * 业务接口类：语言设置管理
 * @author simon
 * @date 2016-3-17
 */
public interface SysSiteService {
	/***
	 * 语言管理列表查询的方法
	 * @author simon
	 * @date 2016-3-17
	 * @param site
	 * @param jsonpaging
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging,SysSite site);
	/***
	 * 根据app用户的id查询用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 * @param jsonpaging
	 * @return
	 */
	public SysSite findSiteById(SysSite site);
	 
	/***
	 * 保存用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public SysSite saveOrUpdate(SysSite site,boolean isAdd);
	/***
	 * 删除用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public boolean deleteObject(String ids);
	/***
	 * 根据域名查询站点对象
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysSite findBydomain(String domain);
	/***
	 * 根据站点名称查询站点对象
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysSite findBysiteName(String siteName);
	/***
	 * 获取站点信息
	 * @author wwluo
	 * @date 2016-12-20
	 * @param
	 * @return
	 */
	public SysSite findSysSiteById(String id);
}
