/**
 * 
 */
package com.fsll.wmes.product.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.product.vo.ProductVO;

/**
 * 产品信息查询服务接口
 * @author Yan
 * @date 2016-11-28
 */
public interface ProductInfoService {
	
	/**
	 * 通过ID查找一条信息
	 * @param id
	 * @return
	 */
	public ProductInfo findProductInfoById(String id);
	
	/**
	 * 获取产品列表（基金）
	 * @return
	 */
	public List<ProductVO> getProductFundList(String distributorId, String langCode);
	
	/**
	 * 查找产品VO列表
	 * @param id
	 * @return
	 */
	public JsonPaging getProductVOList(JsonPaging jsonPaging, String loginId, String keyword, String type, String langCode);
	
	/**
	 * 保存信息
	 * @param info
	 * @return
	 */
	public ProductInfo saveOrUpdate(ProductInfo info, boolean isAdd);
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
	
	/**
	 * 通过产品ID查找基金产品
	 * @param productId
	 * @param langCode
	 * @return
	 */
	public ProductVO findFundInfoByProductId(String productId, String langCode);
	
	/**
	 * 通过产品ID查找债劵产品
	 * @param productId
	 * @param langCode
	 * @return
	 */
	public ProductVO findBondInfoByProductId(String productId, String langCode);
	
	/**
	 * 通过产品ID查找股票产品
	 * @param productId
	 * @param langCode
	 * @return
	 */
	public ProductVO findStockInfoByProductId(String productId, String langCode);
}
