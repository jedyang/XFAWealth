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
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.product.service.ProductInfoService;
import com.fsll.wmes.product.vo.ProductVO;

/**
 * 基金信息查询服务接口实现
 * @author Yan
 * @date 2016-11-28
 */
@Service("productInfoService")
//@Transactional
public class ProductInfoServiceImpl extends BaseService implements ProductInfoService {

	/**
	 * 通过ID查找一条产品基本信息
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public ProductInfo findProductInfoById(String id){
		ProductInfo info = (ProductInfo) baseDao.get(ProductInfo.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条产品VO基本信息
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging getProductVOList(JsonPaging jsonPaging, ProductVO vo, String langCode){
		String hql = " FROM ProductInfo p "
			+ " LEFT JOIN FundInfo f ON p.id=f.product.id "
			+ " LEFT JOIN " + this.getLangString("FundInfo", langCode) + " fl ON f.id=fl.id "
			+ " LEFT JOIN BondInfo b ON p.id=b.product.id "
			+ " LEFT JOIN " + this.getLangString("BondInfo", langCode) + " bl ON b.id=bl.id "
			+ " LEFT JOIN StockInfo s ON p.id=s.product.id "
			+ " LEFT JOIN " + this.getLangString("StockInfo", langCode) + " sl ON s.id=sl.id "			
			+ " LEFT JOIN SysParamConfig c ON c.configCode=fl.fundCurrencyCode " //OR c.configCode=b.currency " //OR c.configCode=s.currency 
			+ " LEFT JOIN ProductDistributor pd ON p.id=pd.product.id "
			+ " LEFT JOIN MemberDistributor md ON pd.distributor.id=md.id "
			+ " WHERE p.isValid = 1 "
			+ " AND c.type.id=(SELECT t.id FROM SysParamType t WHERE t.typeCode='currency_type') ";
		List<Object> params = new ArrayList<Object>();
		if(null!=vo && StringUtils.isNotBlank(vo.getType())){
			hql += " AND p.type=? ";
			params.add(vo.getType());
		}
		if(null!=vo && StringUtils.isNotBlank(vo.getName())){
			hql += " AND (fl.fundName LIKE ? OR bl.bondName LIKE ? )";
			params.add("%" + vo.getName() + "%");
			params.add("%" + vo.getName() + "%");
		}
		if(null!=vo && StringUtils.isNotBlank(vo.getDistributorId())){
			hql += " AND md.id = ? ";
			params.add(vo.getDistributorId());
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}	
	
	/**
	 * 保存信息
	 * @param info
	 * @return
	 */
	public ProductInfo saveOrUpdate(ProductInfo info, boolean isAdd){
		if(isAdd){
			return (ProductInfo)this.baseDao.create(info);
		}else{
			return (ProductInfo)this.baseDao.saveOrUpdate(info);
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
			ProductInfo info = findProductInfoById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * 通过ID更改一条记录状态
	 * @param id
	 * @return
	 */
	@Override
	public boolean updateIsValid(String id){
		if (id == null) {
			return false;
		}else{
			ProductInfo info = findProductInfoById(id);
			if(info != null){
				info.setIsValid("0");
				baseDao.update(info);
				return true;
			}else{
				return false;
			}
		}
	}
}
