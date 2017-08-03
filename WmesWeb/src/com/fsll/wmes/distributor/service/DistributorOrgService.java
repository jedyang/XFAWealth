/**
 * 
 */
package com.fsll.wmes.distributor.service;


import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.MemberDistributor;

/**
 * @author michael
 * 经销商组织架构信息接口
 * @date 20160711
 */
public interface DistributorOrgService {

	/**
	 * 根据id查找实体内容
	 * @param id
	 * @return
	 */
	public DistributorOrg findById(String id); 
	

	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,String fundId,String companyName);

	/**
	 * 修改状态
	 * @param ids
	 * @param status
	 * @return
	 */
	public Boolean saveStatus(String ids, String status);

	
	
	/**
	 * 创建或更新
	 * @param obj 实体对象
	 * @return
	 */
	public DistributorOrg saveOrUpdate(DistributorOrg obj); 
	
	/***
	 * 查询所有的代理公司方法
	 * @date 2016-7-06
	 * @return list
	 */
	public List<MemberDistributor> findAllDistributors();


}
