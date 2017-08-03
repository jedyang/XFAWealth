/**
 * 
 */
package com.fsll.wmes.fund.service;

/**
 * @author tan
 * 基金经销商信息接口
 * @date 20160706
 */
public interface FundDistributorService {

	/**
	 * 根据id查找英文实体内容
	 * @param id
	 * @return
	 
	public MemberDistributor findById(String id); */
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     
	public JsonPaging findAll(JsonPaging jsonpaging,String fundId,String companyName);*/

	/**
	 * 修改状态
	 * @param ids
	 * @param status
	 * @return
	 
	public Boolean saveStatus(String ids, String status);*/
	
	/**
	 * 创建或更新
	 * @param obj 实体对象
	 * @return
	 
	public FundDistributor saveOrUpdate(FundDistributor obj); */
	
	/***
	 * 查询所有的代理公司方法
	 * @date 2016-7-06
	 * @return list
	 
	public List<MemberDistributor> findAllDistributors();*/

	/**
	 * 删除基金、代理商关系
	 */
	public boolean delFundDistributor(String fundId, String distributorId);
}
