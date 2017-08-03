/**
 * 
 */
package com.fsll.wmes.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.ProductDistributorFeeSpec;
import com.fsll.wmes.product.service.ProductDistributorFeeSpecService;

/**
 * 产品代理费用接口实现
 * @author Yan
 * @date 2016-11-28
 */
@Service("productDistributorFeeSpecService")
//@Transactional
public class ProductDistributorFeeSpecServiceImpl extends BaseService implements ProductDistributorFeeSpecService {

	/**
	 * 通过ID查找一条产品基本信息
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public ProductDistributorFeeSpec findById(String id){
		ProductDistributorFeeSpec info = (ProductDistributorFeeSpec) baseDao.get(ProductDistributorFeeSpec.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条信息
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging getList(JsonPaging jsonPaging, String keyword, String langCode){
		String hql = " FROM ProductDistributorFeeSpec f "
			+ " LEFT JOIN SysParamConfig c ON f.feeType=c.id "
			+ " LEFT JOIN ProductInfo p ON f.product.id=p.id "
			+ " LEFT JOIN FundInfo ff ON p.id=ff.product.id "
			+ " LEFT JOIN " + this.getLangString("FundInfo", langCode) + " fl ON ff.id=fl.id "
			+ " LEFT JOIN BondInfo b ON p.id=b.product.id "
			+ " LEFT JOIN " + this.getLangString("BondInfo", langCode) + " bl ON b.id=bl.id "
			+ " LEFT JOIN StockInfo s ON p.id=s.product.id "
			+ " LEFT JOIN " + this.getLangString("StockInfo", langCode) + " sl ON s.id=sl.id "
			+ " WHERE p.isValid = 1 ";
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(keyword)){
			hql += " AND fl.fundName LIKE ? ";
			params.add("%"+keyword+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}	
	
	/**
	 * 保存信息
	 * @param info
	 * @return
	 */
	public ProductDistributorFeeSpec saveOrUpdate(ProductDistributorFeeSpec info, boolean isAdd){
		if(isAdd){
			return (ProductDistributorFeeSpec)this.baseDao.create(info);
		}else{
			return (ProductDistributorFeeSpec)this.baseDao.saveOrUpdate(info);
		}
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			ProductDistributorFeeSpec info = findById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}

}
