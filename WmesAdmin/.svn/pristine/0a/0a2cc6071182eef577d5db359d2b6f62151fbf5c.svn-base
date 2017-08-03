/**
 * 
 */
package com.fsll.wmes.product.service;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
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
	 * 通过ID查找一条产品VO基本信息
	 * @param id
	 * @return
	 */
	public JsonPaging getProductVOList(JsonPaging jsonPaging, ProductVO vo, String langCode);
	
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
	 * 通过ID更改一条记录状态
	 * @param id
	 * @return
	 */
	public boolean updateIsValid(String id);
}
