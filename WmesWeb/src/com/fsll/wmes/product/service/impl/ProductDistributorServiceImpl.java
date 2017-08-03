package com.fsll.wmes.product.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.vo.ProductCompanyVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.MemberCompanyIfafirm;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductCompany;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.product.service.ProductDistributorService;
import com.fsll.wmes.product.vo.ProductDistributorVO;

/**
 * 产品代理费用接口实现
 * @author Yan
 * @date 2016-11-28
 */
@Service("productDistributorService")
//@Transactional
public class ProductDistributorServiceImpl extends BaseService implements ProductDistributorService {

	/**
	 * 通过ID查找实体
	 * @param id
	 * @return
	 */
	@Override
	public ProductDistributor findById(String id) {
		ProductDistributor info = (ProductDistributor)this.baseDao.get(ProductDistributor.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找实体
	 * @param id
	 * @return
	 */
	@Override
	public ProductDistributorVO findVoById(String id, String langCode) {
		ProductDistributorVO infoVo = new ProductDistributorVO();
		ProductDistributor info = (ProductDistributor) baseDao.get(ProductDistributor.class, id);
		MemberDistributor dInfo = info.getDistributor();
		BeanUtils.copyProperties(info, infoVo);
		return infoVo;
	}
	
	/**
	 * 通过productId和distributorId查找productDistributor
	 * @param productId
	 * @param distributorId
	 * @return
	 */
	@Override
	public List<ProductDistributor> findProductDistributor(String productId,
			String distributorId) {
		String hql = " FROM ProductDistributor r WHERE 1=1 "; 
		List<Object> params = new ArrayList<Object>();
		if(StringUtils.isNotBlank(productId)){
			hql += " AND r.product.id=? ";
			params.add(productId);
		}
		if(StringUtils.isNotBlank(distributorId)){
			hql += " AND r.distributor.id=? ";
			params.add(distributorId);
		}
		List<ProductDistributor> list = (List<ProductDistributor>)this.baseDao.find(hql, params.toArray(), false);
		return list;
	}

	/**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param infoVo 查询参数
	 * @return
     */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging,ProductDistributorVO infoVo, String type, String langCode) {
		String typeMap = "FundInfo";
		if(StringUtils.isNotBlank(type) && "fund".equals(type)){
			typeMap = "FundInfo";
		} else if(StringUtils.isNotBlank(type) && "bond".equals(type)){
			typeMap = "BondInfo";
		} else if(StringUtils.isNotBlank(type) && "stock".equals(type)){
			typeMap = "StockInfo";
		} else if(StringUtils.isNotBlank(type) && "futures".equals(type)){
			typeMap = "FundInfo";
		}
		String hql=" FROM ProductDistributor r "
//			+ " LEFT JOIN " + this.getLangString("CompanyInfo", langCode) + " l ON r.company.id=l.id "
			+ " INNER JOIN " + typeMap + " m ON r.product.id=m.product.id "
			+ " LEFT JOIN " + this.getLangString(typeMap, langCode)	+ " ll ON m.id=ll.id "
			+ " INNER JOIN MemberDistributor d ON r.distributor.id=d.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		//名字查询筛选
		if(!"".equals(infoVo.getDistributor()) && null!=infoVo.getDistributor()){
			hql += " AND d.id=? ";
			params.add(infoVo.getDistributor().getId());
		}
//		if(!"".equals(distributorName) && null!=distributorName){
//			hql += " AND d.companyName LIKE ? ";
//			params.add("%" + distributorName + "%");
//		}
		if(!"".equals(infoVo.getProductName()) && null!=infoVo.getProductName() && "fund".equals(type)){
			hql += " AND ll.fundName LIKE ? ";
			params.add("%" + infoVo.getProductName() + "%");
		} else if(!"".equals(infoVo.getProductName()) && null!=infoVo.getProductName() && "bond".equals(type)){
			hql += " AND ll.bondName LIKE ? ";
			params.add("%" + infoVo.getProductName() + "%");
		} else if(!"".equals(infoVo.getProductName()) && null!=infoVo.getProductName() && "stock".equals(type)){
			hql += " AND ll.stockName LIKE ? ";
			params.add("%" + infoVo.getProductName() + "%");
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		
		return jsonPaging;
	}
	
	/**
	 * 更新实体信息
	 * @param info
	 * @return
	 */
	@Override
	public ProductDistributor saveOrUpdate(ProductDistributor info) {
		return (ProductDistributor)this.baseDao.saveOrUpdate(info);
	}

	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			ProductDistributor info = findById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}
}
