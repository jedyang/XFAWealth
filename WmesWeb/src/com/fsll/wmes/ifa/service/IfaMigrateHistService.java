package com.fsll.wmes.ifa.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.IfaMigrateHist;



/**
 * IFA客户数据迁移接口
 * @author michael
 * @date 2017-3-1
 */
public interface IfaMigrateHistService {
	/**
	 * 增加或者修改一条数据
	 * @param hist 帐号信息
	 * @return 
	 */
	public IfaMigrateHist saveOrUpdate(IfaMigrateHist hist);

	/**
	 * 删除多条数据
	 * @param ids
	 */
	public boolean delete(String ids);
	
	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public IfaMigrateHist findById(String id);
	
	 /***
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param hist 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, IfaMigrateHist hist);
	
}
