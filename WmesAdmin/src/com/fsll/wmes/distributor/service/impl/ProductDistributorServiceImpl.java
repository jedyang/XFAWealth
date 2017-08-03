package com.fsll.wmes.distributor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.distributor.service.ProductDistributorService;
import com.fsll.wmes.distributor.vo.ProductDistributorVO;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.fund.service.FundInfoService;

/**
 * 产品企业代理商关系管理
 * @author Yan
 * @date 2016-12-07
 */
@Service("productDistributorService")
//@Transactional
public class ProductDistributorServiceImpl extends BaseService implements ProductDistributorService {

	/**
	 * 通过ID查找一条数据
	 * @param id
	 * @return
	 */
	@Override
	public ProductDistributor findById(String id) {
		ProductDistributor info = (ProductDistributor)this.baseDao.get(ProductDistributor.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条详细数据
	 * @param id
	 * @return
	 */	
	@Override
	public ProductDistributorVO findVoById(String id, String langCode) {
		ProductDistributorVO infoVo = new ProductDistributorVO();
		ProductDistributor info = (ProductDistributor) baseDao.get(ProductDistributor.class, id);
		//获取代理商信息
		if(null!=info && null!=info.getDistributor()){
			infoVo.setDistributor(info.getDistributor());
			infoVo.setDistributorName(info.getDistributor().getCompanyName());
		}
		//获取产品信息
		if(null!=info && null!=info.getProduct()){
			infoVo.setProduct(info.getProduct());
			List<Object> params = new ArrayList<Object>();
			if("fund".equals(info.getProduct().getType())){	//获取基金名称
				String hql = " FROM FundInfo f WHERE f.product.id=? ";
				params.add(info.getProduct().getId());
				List<Object> list = this.baseDao.find(hql, params.toArray(), false);
				FundInfo fund = null;
				if(null!=list){
					fund = (FundInfo)list.get(0);
				}
				if("sc".equals(langCode)&&null!=fund){
					FundInfoSc sc = (FundInfoSc)this.baseDao.get(FundInfoSc.class, fund.getId());
					if(null!=sc) infoVo.setProductName(sc.getFundName());
				} else if("tc".equals(langCode)&&null!=fund){
					FundInfoTc tc = (FundInfoTc)this.baseDao.get(FundInfoTc.class, fund.getId());
					if(null!=tc) infoVo.setProductName(tc.getFundName());
				} else if("en".equals(langCode)&&null!=fund){
					FundInfoEn en = (FundInfoEn)this.baseDao.get(FundInfoEn.class, fund.getId());
					if(null!=en) infoVo.setProductName(en.getFundName());
				}
			} else if("bond".equals(info.getProduct().getType())){	//获取债券名称
				String hql = " FROM BondInfo b WHERE b.product.id=? ";
				params.add(info.getProduct().getId());
				List<Object> list = this.baseDao.find(hql, params.toArray(), false);
				BondInfo bond = null;
				if(null!=list){
					bond = (BondInfo)list.get(0);
				}
				if("sc".equals(langCode)&&null!=bond){
					BondInfoSc sc = (BondInfoSc)this.baseDao.get(FundInfoSc.class, bond.getId());
					if(null!=sc) infoVo.setProductName(sc.getBondName());
				} else if("tc".equals(langCode)&&null!=bond){
					BondInfoTc tc = (BondInfoTc)this.baseDao.get(FundInfoTc.class, bond.getId());
					if(null!=tc) infoVo.setProductName(tc.getBondName());
				} else if("en".equals(langCode)&&null!=bond){
					BondInfoEn en = (BondInfoEn)this.baseDao.get(FundInfoEn.class, bond.getId());
					if(null!=en) infoVo.setProductName(en.getBondName());
				}
			}
		}
		BeanUtils.copyProperties(info, infoVo);
		
		return infoVo;
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
//		if(!"".equals(infoVo.getCompanyName()) && null!=infoVo.getCompanyName()){
//			hql += " AND l.name LIKE ? ";
//			params.add("%" + infoVo.getCompanyName() + "%");
//		}
		if(!"".equals(infoVo.getDistributorName()) && null!=infoVo.getDistributorName()){
			hql += " AND d.companyName LIKE ? ";
			params.add("%" + infoVo.getDistributorName() + "%");
		}
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
	public ProductDistributor save(ProductDistributor info, boolean isAdd){
		ProductDistributor infoTemp = (ProductDistributor)this.baseDao.saveOrUpdate(info,isAdd);		
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
			ProductDistributor info = findById(id);
			baseDao.delete(info);
			return true;
		}
	}
}
