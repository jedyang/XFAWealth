/**
 * 
 */
package com.fsll.wmes.distributor.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.distributor.service.DistributorOrgService;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.MemberDistributor;

/**
 * @author michael
 * 经销商组织架构信息接口实现
 * @date 20160711
 */
@Service("distributorOrgService")
//@Transactional
public class DistributorOrgServiceImpl extends BaseService implements DistributorOrgService {

	
	 /***
     * 分页查询记录
     * @param jsonpaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	//@Transactional(readOnly = true)
	public JsonPaging findAll(JsonPaging jsonPaging,String fundId,String companyName){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder(" From DistributorOrg l ");
		hql.append(" where l.fund.id= ? ");				
		params.add(fundId);
		
		if(companyName != null && !"".equals(companyName)){
			hql.append(" and l.distributor.companyName like ?  ");
			params.add("%"+companyName+"%");
		}
				
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
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
				String hql = "update DistributorOrg v set v.isValid = ? where v.id =  ? ";
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
	 * 根据id查找实体内容
	 * @param id
	 * @return
	 */
	@Override
	public DistributorOrg findById(String id) {
		// TODO Auto-generated method stub
		String hql = "from DistributorOrg where id=? ";
		List params=new ArrayList();
		params.add(id);
		List<DistributorOrg> list = this.baseDao.find(hql, params.toArray(), false);
		
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
	public DistributorOrg saveOrUpdate(DistributorOrg obj) {
		// TODO Auto-generated method stub
		if(null == obj.getId() || "".equals(obj.getId())){
			obj.setId(null);
			obj = (DistributorOrg)baseDao.create(obj);
		}else{
			obj = (DistributorOrg)baseDao.update(obj);
		}
		return obj;
	}
	
	/***
	 * 查询所有的代理公司方法
	 * @date 2016-7-06
	 * @return list
	 */
	//@Transactional(readOnly=true)
	@Override
	public List<MemberDistributor> findAllDistributors(){
		
		String hql="from MemberDistributor ";
		List<MemberDistributor> list=this.baseDao.find(hql, null, false);

		return list;
	}


	

}
