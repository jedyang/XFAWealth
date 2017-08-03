/**
 * 
 */
package com.fsll.wmes.product.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.ProductDistributorFeeSpec;

/**
 * 产品代理费用接口
 * @author Yan
 * @date 2016-12-19
 */
public interface ProductDistributorFeeSpecService {
	
	/**
	 * 通过ID查找一条信息
	 * @param id
	 * @return
	 */
	public ProductDistributorFeeSpec findById(String id);
	
	/**
	 * 通过ID查找一条产品VO基本信息
	 * @param id
	 * @return
	 */
	public JsonPaging getList(JsonPaging jsonPaging, String keyword, String langCode);
	
	/**
	 * 保存信息
	 * @param info
	 * @return
	 */
	public ProductDistributorFeeSpec saveOrUpdate(ProductDistributorFeeSpec info, boolean isAdd);
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
}
