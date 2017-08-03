/**
 * 
 */
package com.fsll.wmes.fund.service;


import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.CompanyDistributor;
import com.fsll.wmes.entity.FundFeesItem;
import com.fsll.wmes.entity.FundFeesItemEn;
import com.fsll.wmes.entity.FundFeesItemSc;
import com.fsll.wmes.entity.FundFeesItemTc;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductCompany;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.fund.vo.FundChargesDataVO;

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
	 */
	public ProductDistributor findById(String id); 
	
    /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonpaging,String productId,String companyName);
	
	/***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	public JsonPaging findAllIncludeDistributorName(JsonPaging jsonpaging,String productId,String companyName, String langCode);

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
	public ProductDistributor saveOrUpdate(ProductDistributor obj); 
	
	/***
	 * 查询所有的代理公司方法
	 * @date 2016-7-06
	 * @return list
	 */
	public List<MemberDistributor> findAllDistributors();

	/**
	 * 删除ProductDistributor信息
	 * @param id
	 * @author Yan
	 */
	public boolean deleteProductDistributor(String id);
	
	/**
	 * 根据id查找ProductDistributor
	 * @param productId
	 * @param distributorId
	 * @return
	 */
	public ProductDistributor findProductDistributor(String productId, String distributorId);
	
	/**
	 * 根据id查找ProductCompany
	 * @param productId
	 * @param companyId
	 * @return
	 */
	public ProductCompany findProductCompany(String productId, String companyId);
	
	/**
	 * 新增或修改
	 * @param obj
	 * @return
	 */
	public ProductCompany saveOrUpdate(ProductCompany obj);
	
	/**
	 * 根据id查找CompanyDistributor
	 * @param companyId
	 * @param distributorId
	 * @return
	 */
	public CompanyDistributor findCompanyDistributor(String companyId, String distributorId);
	
	/**
	 * 新增或修改
	 * @param obj
	 * @return
	 */
	public CompanyDistributor saveOrUpdate(CompanyDistributor obj);
	
	/***
	 * 查询产品的代理公司
	 * @Auth Michael
	 * @date 2017-3-1
	 * @param productId
	 * @return list
	 */
	public List<MemberDistributor> findDistributorsByProduct(String productId);
	
	/***
	 * 查询基金的代理公司
	 * @Auth Michael
	 * @date 2017-3-1
	 * @param fundId
	 * @return list
	 */
	public List<MemberDistributor> findDistributorsByFund(String fundId);
	
	/**
	 * 根据symbolCode查找实体内容
	 * @param code
	 * @return
	 */
	public ProductDistributor findBySymbolCode(String code);
}
