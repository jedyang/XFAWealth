package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.service.SysCountryService;
/***
 * 业务接口实现类：国家管理接口类
 * @author michael
 * @date 2016-7-11
 */
@Service("sysCountryService")
//@Transactional
public class SysCountryServiceImpl extends BaseService implements SysCountryService {
    /***
     * 查找所有
     * @author michael
     * @date 2016-07-11
	 * @param code 国家编码
	 * @param name 国家名称
	 * @return
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,String code, String name) {
		String hql=" from SysCountry where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(null!=name && !"".equals(name)){
			hql+=" and (nameSc like ? or nameTc like ? or nameEn like ?)";
			params.add("%"+name+"%");
			params.add("%"+name+"%");
			params.add("%"+name+"%");
		}
		if(null!=code && !"".equals(code)){
			hql+=" and (isoCode2 like ? or isoCode3 like ?)";
			params.add("%"+code+"%");
			params.add("%"+code+"%");
		}

		jsonpaging.setOrder("orderBy");
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);

		return jsonpaging;
	}
	
	 /***
     * 查询所有有效的记录
     * @author michael
     * @date 2016-07-11
     */
	@Override
	//@Transactional(readOnly=true)
	public List<SysCountry> findAll() {
		String hql=" from SysCountry t order by t.orderBy";
		List<SysCountry> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			return list;
		}else {
			return null;
		}	
	}
	
	/***
	 * 根据编码查找国家
	 * @author michael
	 * @date 2016-7-11
	 * @param code
	 * @return
	 */
	public SysCountry findByCode(String code){
		String hql="from SysCountry where 1=1 ";
		List params=new ArrayList();
		params.add(code);
		if(null!=code && !"".equals(code)){
			hql+=" and (isoCode2 = ? or isoCode3 = ?)";
			params.add(code);
			params.add(code);
		}
		List<SysCountry> listuser=this.baseDao.find(hql, params.toArray(), false);
		if (null!=listuser&&!listuser.isEmpty()) {
			return listuser.get(0);
		}
		return null;
	}

	/***
	 * 删除国家对象的方法
	 * @author michael
	 * @date 2016-7-11
	 * @param ids
	 * @return
	 */
	public boolean deleteObject(String ids){
		if (!"".equals(ids)) {
			String tmpStr = "";
			String[] arr = null;
			if(ids.endsWith(",")){
				tmpStr = ids.substring(0,ids.length()-1);
				arr = tmpStr.split(",");
		    }else{
		    	arr = new String[]{ids};
			}
			for (String id : arr) {
				if(!"".equals(id)){
					deleteById(id);
				}
			}
		}
		return false;
	}
	
	/***
	 * 删除根据id删除对象的方法
	 * @author michael
	 * @date 2016-7-11
	 * @param id
	 * @return
	 */
	private boolean deleteById(String id){
		if(null!=id){
			SysCountry site=new SysCountry();
//			site.setId(id);
			site=findBycountryId(id);
			if(null!=site){
				//删除该指定站点信息
				this.baseDao.delete(site);
			}
		}else{
			return false;
		}
		return true;
		
	}
	 
	@Override
	public SysCountry saveOrUpdate(SysCountry country, boolean isAdd) {
		country=(SysCountry)this.baseDao.saveOrUpdate(country,isAdd);
		return country;
	}
     
	 /***
     * 通过地区编码查国家信息
     * @author michael
     * @date 2016-07-11
	 * @param code
     */
	public List<SysCountry> findByAreaCode(String code){
		List params=new ArrayList();
		String hql=" from SysCountry t where t.areaCode=? order by t.orderBy";
		params.add(code);
		List<SysCountry> list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	
	/***
     * 通过id获取Country对象
     * @author wwluo
     * @date 2016-08-22
	 * @param countryId
     */
	public SysCountry findBycountryId(String countryId){
		SysCountry sysCountry = new SysCountry();
		if(StringUtils.isNotBlank(countryId)){
			sysCountry = (SysCountry) this.baseDao.get(SysCountry.class, countryId);
		}
		return sysCountry;
	}
	
	
}
