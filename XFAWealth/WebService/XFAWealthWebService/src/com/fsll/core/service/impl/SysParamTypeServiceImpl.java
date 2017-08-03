package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysParamConfig;
import com.fsll.core.entity.SysParamType;
import com.fsll.core.service.SysParamTypeService;
/***
 * 业务接口实现类：语言管理接口类
 * @author simon
 * @date 2016-4-11
 */
@Service("sysParamTypeService")
//@Transactional
public class SysParamTypeServiceImpl extends BaseService implements SysParamTypeService {
    /***
     * 查询app管理
     * @author simon
     * @date 2016-03-17
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,SysParamType paramType) {
		String hql=" from SysParamType where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(null!=paramType.getName()&&!"".equals(paramType.getName())){
			hql+=" and (name like ? or typeCode like ?)";
			params.add("%"+paramType.getName()+"%");
			params.add("%"+paramType.getName()+"%");
		}
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = new ArrayList();
		Iterator it = jsonpaging.getList().iterator();
		Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
		Integer index = 0;
    	while(it.hasNext()){
			index++;
			SysParamType obj = (SysParamType)it.next();
			obj.setXh(Integer.toString(indexNumber+index));
			list.add(obj);
		}
    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
	   /***
     * 查询所有有效的类型
     * @author tan
     * @date 2016-07-08
     */
	@Override
	//@Transactional(readOnly=true)
	public List<SysParamType> findAllParamType() {
		String hql=" from SysParamType where isValid=1";
		List<SysParamType> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			return list;
		}else {
			return null;
		}	
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
	public SysParamType findParamTypeById(SysParamType paramType){
		String hql="from SysParamType where id=? ";
		List params=new ArrayList();
		params.add(paramType.getId());
		List<SysParamType> listuser=this.baseDao.find(hql, params.toArray(), false);
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
	public SysParamType findByCode(String code){
		String hql="from SysParamType where typeCode=?";
		List params=new ArrayList();
		params.add(code);
		List<SysParamType> listuser=this.baseDao.find(hql, params.toArray(), false);
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
	public SysParamType findByParamTypeName(String name){
		String hql="from SysParamType where name=?";
		List params=new ArrayList();
		params.add(name);
		List<SysParamType> listuser=this.baseDao.find(hql, params.toArray(), false);
		if (null!=listuser&&!listuser.isEmpty()) {
			return listuser.get(0);
		}
		return null;
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
			SysParamType site=new SysParamType();
			site.setId(id);
			site=findById(site);
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
	public SysParamType saveOrUpdate(SysParamType paramType, boolean isAdd) {
		paramType=(SysParamType)this.baseDao.saveOrUpdate(paramType,isAdd);
		return paramType;
	}

	@Override
	public SysParamType findById(SysParamType paramType) {
		String hql="from SysParamType where id=?";
		List params=new ArrayList();
		params.add(paramType.getId());
		List<SysParamType> listuser=this.baseDao.find(hql, params.toArray(), false);
		if (null!=listuser&&!listuser.isEmpty()) {
			return listuser.get(0);
		}
		return null;
	}
     
    /***
     * 检查SysParamType是否存在子节点
     * @author simon
     * @date 2016-4-12
     * @param type
     * @return
     */
	@Override
	public boolean checkChildrenExist(SysParamType paramType) {
    	String hql="FROM SysParamType t where t.parent.id=?";
    	List param=new ArrayList();
    	param.add(paramType.getId());
    	List<SysParamConfig> listtype=this.baseDao.find(hql, param.toArray(), false);
    	if(null!=listtype&&!listtype.isEmpty()){
    		return true;
    	}
    	return false;    	
    }
	
	 /***
     * 获取子节点
     * @author simon
     * @date 2016-4-12
     * @param type
     * @return
     */
    public List<SysParamConfig> getChildren(String typeCode){
    	String hql = " From SysParamConfig c "
    		+ " WHERE c.isValid=1 ";
    	List<Object> params = new ArrayList<Object>();
    	if(StringUtils.isNotBlank(typeCode)){
    		hql += " AND c.type.id=(SELECT t.id FROM SysParamType t WHERE t.typeCode=? )";
    		params.add(typeCode);
    	}
    	List<SysParamConfig> list = this.baseDao.find(hql, params.toArray(), false);
    	return list;
    }
	
    /***
     * 
     */
    @Override
	public boolean saveStatus(String ids, String status) {
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
				String hql = "update SysParamType v set v.status = ? where v.id =  ? ";
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
    
}
