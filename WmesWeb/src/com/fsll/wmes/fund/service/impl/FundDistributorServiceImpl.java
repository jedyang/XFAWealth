/**
 * 
 */
package com.fsll.wmes.fund.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fsll.common.base.service.BaseService;
import com.fsll.wmes.entity.FundInfo;
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
     
	//@Transactional(readOnly = true)
	public JsonPaging findAll(JsonPaging jsonPaging,String fundId,String companyName){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" From FundDistributor l ");
		hql.append(" where l.fund.id= ? ");				
		params.add(fundId);
		
		if(companyName != null && !"".equals(companyName)){
			hql.append(" and l.distributor.companyName like ?  ");
			params.add("%"+companyName+"%");
		}
				
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}*/

	/**
	 * 修改状态
	 * @param ids 主键
	 * @param status 状态  0/1
	 
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
	}*/

	/**
	 * 根据id查找实体内容
	 * @param id
	 * @return
	 
	@Override
	public FundDistributor findById(String id) {
		// TODO Auto-generated method stub
		String hql = "from FundDistributor where id=? ";
		List params=new ArrayList();
		params.add(id);
		List<FundDistributor> list = this.baseDao.find(hql, params.toArray(), false);
		
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		
		return null;
	}*/
	
	/**
	 * 新增或修改
	 * @param id
	 * @return
	 
	@Override
	public FundDistributor saveOrUpdate(FundDistributor obj) {
		// TODO Auto-generated method stub
		if(null == obj.getId() || "".equals(obj.getId())){
			obj.setId(null);
			obj = (FundDistributor)baseDao.create(obj);
		}else{
			obj = (FundDistributor)baseDao.update(obj);
		}
		return obj;
	}*/
	
	/***
	 * 查询所有的代理公司方法
	 * @date 2016-7-06
	 * @return list
	
	//@Transactional(readOnly=true)
	@Override
	public List<MemberDistributor> findAllDistributors(){
		
		String hql="from MemberDistributor ";
		List<MemberDistributor> list=this.baseDao.find(hql, null, false);

		return list;
	} */

	private List<ProductDistributor> getProductDistributorList(String productId, String distributorId){
		String hql = " FROM  ProductDistributor r "
			+ " WHERE r.product.id=? AND r.distributor.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		params.add(distributorId);
		List<ProductDistributor> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 删除基金、代理商关系
	 */
	@Override
	public boolean delFundDistributor(String fundId, String distributorId) {
		if(StringUtils.isBlank(fundId) || StringUtils.isBlank(distributorId)){
			return false;
		}else{
			FundInfo fund = (FundInfo)this.baseDao.get(FundInfo.class, fundId);
			String productId = fund.getProduct().getId();
			List<ProductDistributor> list = this.getProductDistributorList(productId, distributorId);
			if(!list.isEmpty()){
				for (int i = 0; i < list.size(); i++) {
					this.baseDao.delete(list.get(i));
				}
			}
			return true;
		}
	}
	

}
