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
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.MemberCompanyIfafirm;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductCompany;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.entity.ProductDistributorFee;
import com.fsll.wmes.entity.ProductIfafirmDistributor;
import com.fsll.wmes.product.service.ProductDistributorService;
import com.fsll.wmes.product.service.ProductIfafirmDistributorService;
import com.fsll.wmes.product.vo.ProductDistributorVO;

/**
 * 代理商与投资顾问公司的基金关系接口实现
 * @author rqwang
 * @date 2017-06-16
 */
@Service("ProductIfafirmDistributorService")
public class ProductIfafirmDistributorServiceImpl extends BaseService implements ProductIfafirmDistributorService {

	
	/**
	 * 添加
	 */
	@Override
	public ProductIfafirmDistributor save(ProductIfafirmDistributor info) {
		return (ProductIfafirmDistributor)this.baseDao.create(info);
	}

	/**
	 * 删除根据id
	 */
	@Override
	public boolean deleteById(String id) {
		if (id == null) {
			return false;
		}else{
			ProductIfafirmDistributor info = findById(id);
			if(info != null){
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}

	/**
	 * 查找
	 */
	@Override
	public ProductIfafirmDistributor findById(String id) {
		ProductIfafirmDistributor info = (ProductIfafirmDistributor)this.baseDao.get(ProductIfafirmDistributor.class, id);
		return info;
	}


	/**
	 * 根据条件查找出基金与代理商和投资顾问公司的关系
	 */
	@Override
	public ProductIfafirmDistributor findProductIfafirmDistributorById(
			String disId, String ifaFirmId, String productId) {
		List<Object> params = new ArrayList<Object>();
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from ProductIfafirmDistributor t");
		hql.append("  where");
		hql.append("  t.distributor.id=?");
		hql.append("  and ");
		hql.append("  t.ifafirm.id=?");
		hql.append("  and");
		hql.append("  t.product.id=?");
		
		params.add(disId);
		params.add(ifaFirmId);
		params.add(productId);
		
		ProductIfafirmDistributor pid = (ProductIfafirmDistributor) this.baseDao.getUniqueResult(hql.toString(), params.toArray(), false);
		
		return pid;
	}
	
}
