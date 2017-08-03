/**
 * 
 */
package com.fsll.wmes.product.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.vo.ProductCompanyVO;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductDistributorFee;
import com.fsll.wmes.entity.ProductDistributorFee;
import com.fsll.wmes.product.vo.ProductDistributorVO;

/**
 * 产品代理费用接口
 * @author Yan
 * @date 2016-12-30
 */
public interface ProductDistributorService {
	
	/**
	 * 通过ID查找实体
	 * @param id
	 * @return
	 */
	public ProductDistributor findById(String id);
	
	/**
	 * 通过VO查找实体
	 * @param id
	 * @return
	 */
	public ProductDistributorVO findVoById(String id, String langCode);
	
	/**
	 * 通过productId和distributorId查找productDistributor
	 * @param productId
	 * @param distributorId
	 * @return
	 */
	public List<ProductDistributor> findProductDistributor(String productId, String distributorId);
	
	/**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param infoVo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, ProductDistributorVO infoVo, String type, String langCode);
	
	/**
	 * 更新实体信息
	 * @param info
	 * @return
	 */
	public ProductDistributor saveOrUpdate(ProductDistributor info); 
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
}
