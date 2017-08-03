package com.fsll.wmes.distributor.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.distributor.vo.ProductDistributorVO;
import com.fsll.wmes.entity.ProductDistributor;

/**
 * 产品企业代理商关系管理
 * @author Yan
 * @date 2016-12-07
 */
public interface ProductDistributorService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	public ProductDistributor findById(String id);
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */
	public ProductDistributorVO findVoById(String id, String langCode);
	
	/**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param infoVo 查询参数
	 * @return
     */
	public JsonPaging findAll(JsonPaging jsonPaging, ProductDistributorVO infoVo, String type, String langCode);

	/**
	 * 更新/保存方法
	 * @param info
	 * @param isAdd
	 * @return
	 */
	public ProductDistributor save(ProductDistributor info, boolean isAdd);
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);
}
