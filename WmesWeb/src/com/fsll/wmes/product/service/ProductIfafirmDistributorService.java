
package com.fsll.wmes.product.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.vo.ProductCompanyVO;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductDistributorFee;
import com.fsll.wmes.entity.ProductDistributorFee;
import com.fsll.wmes.entity.ProductIfafirmDistributor;
import com.fsll.wmes.product.vo.ProductDistributorVO;

/**
 * 代理商与投资顾问公司的基金关系接口
 * @author rqwang
 * @date 2017-06-16
 */
public interface ProductIfafirmDistributorService {
	
	/**
	 * 通过ID查找实体
	 * @param id
	 * @return
	 */
	public ProductIfafirmDistributor findById(String id);
	
	/**
	 * 更新实体信息
	 * @param info
	 * @return
	 */
	public ProductIfafirmDistributor save(ProductIfafirmDistributor info); 
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id);

	/**
	 * 获取基金
	 * @param disId 代理商
	 * @param ifaFirmId 投资顾问公司
	 * @param fundId 基金id
	 * @return
	 */
	public ProductIfafirmDistributor findProductIfafirmDistributorById(
			String disId, String ifaFirmId, String productId);
}
