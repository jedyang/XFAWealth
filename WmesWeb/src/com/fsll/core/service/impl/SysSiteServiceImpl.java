package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysSite;
import com.fsll.core.service.SysSiteService;
/***
 * 业务接口实现类：语言管理接口类
 * @author simon
 * @date 2016-4-11
 */
@Service("sysSiteService")
//@Transactional
public class SysSiteServiceImpl extends BaseService implements SysSiteService {
    /***
     * 查询app管理
     * @author simon
     * @date 2016-03-17
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,SysSite site) {
		String hql=" from SysSite where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(null!=site.getSiteName()&&!"".equals(site.getSiteName())){
			hql+=" and (siteName like ? or shortName like ? or domain like ?)";
			params.add("%"+site.getSiteName()+"%");
			params.add("%"+site.getSiteName()+"%");
			params.add("%"+site.getSiteName()+"%");
		} 
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		Integer index = 0;
    	while(it.hasNext()){
			index++;
			SysSite obj = (SysSite)it.next();
			obj.setXh(Integer.toString(indexNumber+index));
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	 
	/***
	 * 根据app用户的id查询用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo
	 * @param jsonpaging
	 * @return
	 */
	//@Transactional(readOnly=true)
	public SysSite findSiteById(SysSite userinfo){
		String hql="from SysSite where id=?";
		List params=new ArrayList();
		params.add(userinfo.getId());
		List<SysSite> listuser=this.baseDao.find(hql, params.toArray(), false);
		if (null!=listuser&&!listuser.isEmpty()) {
			return listuser.get(0);
		}
		return null;
	}
	/***
	 * 根据域名查询站点对象
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysSite findBydomain(String domain){
		String hql="from SysSite where domain=?";
		List params=new ArrayList();
		params.add(domain);
		List<SysSite> listuser=this.baseDao.find(hql, params.toArray(), false);
		if (null!=listuser&&!listuser.isEmpty()) {
			return listuser.get(0);
		}
		return null;
	}
	/***
	 * 根据站点名称查询站点对象
	 * @author simon
	 * @date 2016-4-11
	 * @param domain
	 * @return
	 */
	public SysSite findBysiteName(String siteName){
		String hql="from SysSite where siteName=?";
		List params=new ArrayList();
		params.add(siteName);
		List<SysSite> listuser=this.baseDao.find(hql, params.toArray(), false);
		if (null!=listuser&&!listuser.isEmpty()) {
			return listuser.get(0);
		}
		return null;
	}
	/***
	 * 验证用户是否已经 存在的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param username
	 * @param loginCode
	 * @return
	 */
	//@Transactional(readOnly=true)
	public SysSite findByName(String siteName){
		String hql="from SysSite where siteName=? ";
		List params=new ArrayList();
		if(null!=siteName&&!"".endsWith(siteName)){
			params.add(siteName);
		}
		List<SysSite> listuser=this.baseDao.find(hql, params.toArray(), true);
		if (null!=listuser&&!listuser.isEmpty()) {
			return listuser.get(0);
		}
		return null;
	}
	/***
	 * 保存用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public SysSite saveOrUpdate(SysSite site,boolean isAdd){
		//1.保存站点信息
		site=(SysSite)this.baseDao.saveOrUpdate(site,isAdd);
		return site;
	}
	      
	/***
	 * 删除用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo app用户对象
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
	 * 删除根据id删除用户对象的方法
	 * @author simon
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	private boolean deleteById(String id){
		if(null!=id){
			SysSite site=new SysSite();
			site.setId(id);
			site=findSiteById(site);
			//删除该指定站点信息
			this.baseDao.delete(site);
		}else{
			return false;
		}
		return true;		
	}
	
	/***
	 * 获取站点信息
	 * @author wwluo
	 * @date 2016-12-20
	 * @param
	 * @return
	 */
	@Override
	public SysSite findSysSiteById(String id){
		return (SysSite)this.baseDao.get(SysSite.class, id);	
	}
	
}
