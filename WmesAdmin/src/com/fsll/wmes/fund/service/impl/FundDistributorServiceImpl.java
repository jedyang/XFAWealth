/**
 * 
 */
package com.fsll.wmes.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.CompanyDistributor;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.ProductCompany;
import com.fsll.wmes.entity.ProductDistributor;
import com.fsll.wmes.fund.service.FundDistributorService;

/**
 * @author tan
 *基金经销商信息接口实现
 * @date 20160706
 */
@Service("fundDistributorService")
//@Transactional
public class FundDistributorServiceImpl extends BaseService implements FundDistributorService {
	/***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	//@Transactional(readOnly = true)
	public JsonPaging findAll(JsonPaging jsonPaging,String productId,String companyName){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" From ProductDistributor l ");
		hql.append(" where l.product.id= ? ");				
		params.add(productId);
		
		if(companyName != null && !"".equals(companyName)){
			hql.append(" and l.distributor.companyName like ?  ");
			params.add("%"+companyName+"%");
		}
				
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}
	
	/**
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	@Override
	public JsonPaging findAllIncludeDistributorName(JsonPaging jsonpaging,
			String productId, String distributorName, String langCode) {
		String hql = "SELECT r From ProductDistributor r "
//			+ " LEFT JOIN ProductCompany c ON r.product.id=c.product.id "
//			+ " INNER JOIN " + this.getLangString("CompanyInfo", langCode) 
//			+ " l ON l.id=c.company.id "
			+ " WHERE r.product.id= ? ";
		List<Object> params = new ArrayList<Object>();				
		params.add(productId);
		
		if(distributorName != null && !"".equals(distributorName)){
			hql += " AND r.distributor.companyName LIKE ? ";
			params.add("%" + distributorName + "%");
		}
				
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonpaging , false);

		return jsonpaging;
	}

	/**
	 * 修改状态
	 * @param ids 主键
	 * @param status 状态  0/1
	 */
	@Override
	public Boolean saveStatus(String ids, String status) {
		if (!"".equals(ids)) {
			String tmpStr = "";
			String[] arr =null;
			if(ids.endsWith(",")){
				tmpStr = ids.substring(0,ids.length()-1);
				arr =tmpStr.split(",");
			}else{
				arr =new String[]{ids};
			}			
			for (String id : arr) {				 
				String hql = "update FundDistributor v set v.isValid = ? where v.id =  ? ";
				List<Object> params = new ArrayList<Object>();
				params.add(status);
				if(!"".equals(id)){
					params.add(id); 
					this.baseDao.updateHql(hql, params.toArray());
				}
			}
		}
		return true;
	}


	/**
	 * 根据id查找ProductDistributor
	 * @param productId
	 * @param distributorId
	 * @return
	 */
	public ProductDistributor findProductDistributor(String productId, String distributorId) {
		String hql = "from ProductDistributor pc where pc.product.id=? and pc.distributor.id=? ";
		List params=new ArrayList();
		params.add(productId);
		params.add(distributorId);
		List<ProductDistributor> list = this.baseDao.find(hql, params.toArray(), false);
		
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}

	/**
	 * 根据id查找实体内容
	 * @param id
	 * @return
	 */
	@Override
	public ProductDistributor findById(String id) {
		// TODO Auto-generated method stub
		String hql = "from ProductDistributor where id=? ";
		List params=new ArrayList();
		params.add(id);
		List<ProductDistributor> list = this.baseDao.find(hql, params.toArray(), false);
		
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * 新增或修改
	 * @param id
	 * @return
	 */
	@Override
	public ProductDistributor saveOrUpdate(ProductDistributor obj) {
		// TODO Auto-generated method stub
		if(null == obj.getId() || "".equals(obj.getId())){
			obj.setId(null);
			obj = (ProductDistributor)baseDao.create(obj);
		}else{
			obj = (ProductDistributor)baseDao.update(obj);
		}
		return obj;
	}
	
	/***
	 * 查询所有的代理公司方法
	 * @date 2016-7-06
	 * @return list
	 */
	@Override
	public List<MemberDistributor> findAllDistributors(){
		String hql="from MemberDistributor ";
		List<MemberDistributor> list=this.baseDao.find(hql, null, false);

		return list;
	}

	/***
	 * 查询产品的代理公司
	 * @Auth Michael
	 * @date 2017-3-1
	 * @param productId
	 * @return list
	 */
	public List<MemberDistributor> findDistributorsByProduct(String productId){
		if (StrUtils.isEmpty(productId)) return null;
		String hql="SELECT d from MemberDistributor d left join ProductDistributor pd on d.id=pd.distributor.id where pd.product.id=?";
		List params=new ArrayList();
		params.add(productId);
		List<MemberDistributor> list=this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	/***
	 * 查询基金的代理公司
	 * @Auth Michael
	 * @date 2017-3-1
	 * @param fundId
	 * @return list
	 */
	public List<MemberDistributor> findDistributorsByFund(String fundId){
		if (StrUtils.isEmpty(fundId)) return null;
		String hql="SELECT d from MemberDistributor d left join ProductDistributor pd on d.id=pd.distributor.id " +
				" where pd.product.id is not null and pd.product.id in (select f.product.id from FundInfo f where f.id=?)";
		List params=new ArrayList();
		params.add(fundId);
		List<MemberDistributor> list=this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	/**
	 * 删除ProductDistributor信息
	 * @param id
	 * @author Yan
	 */
	@Override
	public boolean deleteProductDistributor(String id) {
		boolean flag = false;
		if(StringUtils.isNotBlank(id)){
			ProductDistributor info = this.findById(id);
			if(!"".equals(info)&&null!=info){
				this.baseDao.delete(info);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 根据id查找ProductCompany
	 * @param productId
	 * @param companyId
	 * @return
	 */
	public ProductCompany findProductCompany(String productId, String companyId) {
		if (StrUtils.isEmpty(productId) && StrUtils.isEmpty(companyId)) return null;
		String hql = "from ProductCompany pc where pc.product.id=? and pc.company.id=? ";
		List params=new ArrayList();
		params.add(productId);
		params.add(companyId);
		List<ProductCompany> list = this.baseDao.find(hql, params.toArray(), false);
		
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * 新增或修改
	 * @param obj
	 * @return
	 */
	public ProductCompany saveOrUpdate(ProductCompany obj) {
		// TODO Auto-generated method stub
		if(null == obj.getId() || "".equals(obj.getId())){
			obj.setId(null);
			obj = (ProductCompany)baseDao.create(obj);
		}else{
			obj = (ProductCompany)baseDao.update(obj);
		}
		return obj;
	}
	
	/**
	 * 根据id查找CompanyDistributor
	 * @param companyId
	 * @param distributorId
	 * @return
	 */
	public CompanyDistributor findCompanyDistributor(String companyId, String distributorId) {
		if (StrUtils.isEmpty(distributorId) && StrUtils.isEmpty(companyId)) return null;
		String hql = "from CompanyDistributor cd where cd.company.id=? and cd.distributor.id=? ";
		List params=new ArrayList();
		params.add(companyId);
		params.add(distributorId);
		List<CompanyDistributor> list = this.baseDao.find(hql, params.toArray(), false);
		
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}
	
	/**
	 * 新增或修改
	 * @param obj
	 * @return
	 */
	public CompanyDistributor saveOrUpdate(CompanyDistributor obj) {
		// TODO Auto-generated method stub
		if(null == obj.getId() || "".equals(obj.getId())){
			obj.setId(null);
			obj = (CompanyDistributor)baseDao.create(obj);
		}else{
			obj = (CompanyDistributor)baseDao.update(obj);
		}
		return obj;
	}
	
	/**
	 * 根据symbolCode查找实体内容
	 * @param code
	 * @return
	 */
	public ProductDistributor findBySymbolCode(String code) {
		// TODO Auto-generated method stub
		String hql = "from ProductDistributor where symbolCode=? ";
		List params=new ArrayList();
		params.add(code);
		List<ProductDistributor> list = this.baseDao.find(hql, params.toArray(), false);
		
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}
}
