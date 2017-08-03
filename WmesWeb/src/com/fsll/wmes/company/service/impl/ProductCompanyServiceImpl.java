package com.fsll.wmes.company.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.company.service.ProductCompanyService;
import com.fsll.wmes.company.vo.ProductCompanyVO;
import com.fsll.wmes.entity.CompanyInfo;
import com.fsll.wmes.entity.CompanyInfoEn;
import com.fsll.wmes.entity.CompanyInfoSc;
import com.fsll.wmes.entity.CompanyInfoTc;
import com.fsll.wmes.entity.ProductCompany;

/**
 * 产品运营企业关系管理
 * @author Yan
 * @date 2017-01-18
 */
@Service("productCompanyService")
//@Transactional
public class ProductCompanyServiceImpl extends BaseService implements ProductCompanyService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	@Override
	public ProductCompany findById(String id) {
		ProductCompany info = (ProductCompany)this.baseDao.get(ProductCompany.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */	
	@Override
	public ProductCompanyVO findVoById(String id, String langCode) {
		ProductCompanyVO infoVo = new ProductCompanyVO();
		ProductCompany info = (ProductCompany) baseDao.get(ProductCompany.class, id);
		CompanyInfo cInfo = info.getCompany();
		infoVo.setId(id);
		infoVo.setCompany(cInfo);
		infoVo.setProduct(info.getProduct());
		infoVo.setCreateTime(info.getCreateTime());
		if("sc".equals(langCode) && !"".equals(cInfo) && null!=cInfo){
			CompanyInfoSc infoSc = (CompanyInfoSc)this.baseDao.get(CompanyInfoSc.class, cInfo.getId());
			infoVo.setCompanyName(infoSc.getName());
		}
		if("tc".equals(langCode) && !"".equals(cInfo) && null!=cInfo){
			CompanyInfoTc infoTc = (CompanyInfoTc)this.baseDao.get(CompanyInfoTc.class, cInfo.getId());
			infoVo.setCompanyName(infoTc.getName());
		}
		if("en".equals(langCode) && !"".equals(cInfo) && null!=cInfo){
			CompanyInfoEn infoEn = (CompanyInfoEn)this.baseDao.get(CompanyInfoEn.class, cInfo.getId());
			infoVo.setCompanyName(infoEn.getName());
		}
		return infoVo;
	}
	
	/**
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param infoVo 查询参数
	 * @return
     */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging,ProductCompanyVO infoVo, String type, String langCode) {
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
		String hql=" FROM ProductCompany r "
			+ " LEFT JOIN " + this.getLangString("CompanyInfo", langCode) + " l ON r.company.id=l.id "
			+ " INNER JOIN " + typeMap + " m ON r.product.id=m.product.id "
			+ " LEFT JOIN " + this.getLangString(typeMap, langCode)	+ " ll ON m.id=ll.id "
//			+ " INNER JOIN MemberDistributor d ON r.distributor.id=d.id "
			+ " WHERE 1=1 ";
		List<Object> params=new ArrayList<Object>();
		//名字查询筛选
		if(!"".equals(infoVo.getCompanyName()) && null!=infoVo.getCompanyName()){
			hql += " AND l.name LIKE ? ";
			params.add("%" + infoVo.getCompanyName() + "%");
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
	 * 更新/保存方法
	 * @param info
	 * @param isAdd
	 * @return
	 */
	@Override
	public ProductCompany save(ProductCompany info, boolean isAdd){
		ProductCompany infoTemp = (ProductCompany)this.baseDao.saveOrUpdate(info,isAdd);		
		return infoTemp;
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
			ProductCompany info = findById(id);
			baseDao.delete(info);
			return true;
		}
	}
}
