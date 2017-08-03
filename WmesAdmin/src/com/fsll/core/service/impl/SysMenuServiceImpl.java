package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysMenu;
import com.fsll.core.service.SysMenuService;
import com.fsll.wmes.entity.MemberMenu;
/***
 * 业务接口实现类：语言管理接口类
 * @author tan
 * @date 2016-07-25
 */
@Service("sysMenuService")
//@Transactional
public class SysMenuServiceImpl extends BaseService implements SysMenuService {
    /***
     * 查询菜单
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findAll(JsonPaging jsonpaging,SysMenu menu) {
		String hql=" from SysMenu where 1=1 ";
		List<Object> params=new ArrayList<Object>();
		if(null!=menu.getNameSc()&&!"".equals(menu.getNameSc())){
			hql+=" and ( code like ? or nameSc like ?  or nameTc like ? or nameEn like ? or webUrl like ? )";
			
			params.add("%"+menu.getNameSc()+"%");
			params.add("%"+menu.getNameSc()+"%");
			params.add("%"+menu.getNameSc()+"%");
			params.add("%"+menu.getNameSc()+"%");
			params.add("%"+menu.getNameSc()+"%");			
		}
		
		hql+=" order by orderBy ";
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		if(null==menu.getNameSc() || "".equals(menu.getNameSc())){		
			List<SysMenu> list = getTreeMenu(jsonpaging.getList());
			jsonpaging.setList(list);
		}
		else {			
			
			List<SysMenu> list = jsonpaging.getList();
			if(!list.isEmpty()){
				//查找当前搜索结果的根节点
				String hqlParent = " from SysMenu where parent is NULL ";
				String hqlParentWhere = "";
				
				for (SysMenu sysMenu : list) {
					hqlParentWhere += " OR FIND_IN_SET(id,fn_queryParentInfo('"+sysMenu.getId()+"'))>0 ";
				}
				hqlParentWhere = " AND ( 1=2 "+hqlParentWhere+" ) ";
				hqlParent = hqlParent + hqlParentWhere; 
				List<SysMenu> parentMenus = this.baseDao.find(hqlParent, null, false);
				if(null != parentMenus && !parentMenus.isEmpty()){
					//根据 根节点查找所有的子节点
					String hqlQuery = " from SysMenu where 1=2 ";
					String hqlQueryWhere = "";
					for (SysMenu sysMenu : parentMenus) {
						hqlQueryWhere += " OR FIND_IN_SET(id,fn_queryChildrenInfo('"+sysMenu.getId()+"'))>0 ";
					}
					hqlQuery = hqlQuery + hqlQueryWhere + " order by orderBy ";
					List<SysMenu> queryList = this.baseDao.find(hqlQuery, null, false);
					queryList = getTreeMenu(queryList);
					jsonpaging.setList(queryList);
				}				
			}
		}

		return jsonpaging;
	}
	
	/***
     * 查询所有有效的类型
     */
	@Override
	//@Transactional(readOnly=true)
	public List<SysMenu> findAllMenu() {
		String hql=" from SysMenu where isValid=1 order by nameEn asc";
		List<SysMenu> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			return list;
		}else {
			return null;
		}	
	}
	


	
	List<SysMenu> treeList;
	public List<SysMenu> getTreeMenu(List<SysMenu> allList) {
		treeList = new ArrayList<SysMenu>();
//		List<SysMenu> list = new ArrayList<SysMenu>();
//		List<SysMenu> allList = findAllMenu();
		if (null!=allList && !allList.isEmpty())
		for (SysMenu sysMenu : allList) {
//			String id = sysMenu.getId();  
            if (sysMenu.getParent() == null) {  
                treeList.add(sysMenu);
                build(allList, sysMenu);  
            } 
		}
//		if(treeList.isEmpty())
//			return allList;
		
		return treeList;
	}
	
	private void build(List<SysMenu> allNodes,SysMenu node){  
        List<SysMenu> children = getChildren(allNodes,node);  
        if (!children.isEmpty()) {   
            for (SysMenu child : children) {  
            	treeList.add(child);  
                build(allNodes,child);  
            }  
        }   
    }
	
    private List<SysMenu> getChildren(List<SysMenu> nodes, SysMenu node){  
        List<SysMenu> children = new ArrayList<SysMenu>();  
        String id = node.getId();  
        for (SysMenu child : nodes) {  
            if (null != child.getParent() && id.equals(child.getParent().getId())) {  
                children.add(child);  
            }  
        }  
        return children;  
    }
	
	

	
	 
	/***
	 * 根据id查询对象的方法 modify qgfeng
	 * @param menu
	 * @return
	 */
	//@Transactional(readOnly=true)
	public SysMenu findMenuById(String id){
		if(StringUtils.isNotBlank(id)){
			SysMenu sysMenu = (SysMenu) this.baseDao.get(SysMenu.class, id);
			return sysMenu;
		}
		return null;
	}
	/***
	 * 删除对象的方法
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
	 * 删除根据id删除用户对象的方法
	 * @param id
	 * @return
	 */
	private boolean deleteById(String id){
		SysMenu menu = findMenuById(id);
		if(null != menu){
			//删除该指定站点信息
			this.baseDao.delete(menu);
			return true;
		}
		return false;
		
	}
	 
	@Override
	public SysMenu saveOrUpdate(SysMenu menu, boolean isAdd) {
		menu = (SysMenu)this.baseDao.saveOrUpdate(menu,isAdd);
		return menu;
	}

     
    /***
     * 检查是否存在子节点
     * @param menu
     * @return
     */
	@Override
	public boolean checkChildrenExist(SysMenu menu) {
    	String hql="FROM SysMenu t where t.parent.id=?";
    	List param=new ArrayList();
    	param.add(menu.getId());
    	List<SysMenu> list = this.baseDao.find(hql, param.toArray(), false);
    	if(null!=list&&!list.isEmpty()){
    		return true;
    	}
    	return false;    	
    }

	/**
	 * 删除菜单项（逻辑删除）
	 */
	@Override
	public boolean deleteMenu(String id) {
		if(StringUtils.isBlank(id)){
			return false;
		}
		SysMenu menu = (SysMenu) baseDao.get(SysMenu.class, id);
		if(menu == null){
			return false;
		}
		menu.setIsValid("0");
		baseDao.update(menu);
		return true;
	}

	/**
	 * 获取系统菜单集合
	 * @author wwluo
	 * @data 2017-06-16
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging findSysMenu(JsonPaging jsonPaging, String keyWord) {
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " m"
				+ " FROM"
				+ " SysMenu m"
				+ " WHERE"
				+ " (m.parent IS NULL"
				+ " OR"
				+ " m.parent='')"
				+ "");
		List<Object> params = new ArrayList<Object>();
		if (StringUtils.isNotBlank(keyWord)) {
			hql.append(" AND ( m.nameSc like ? OR m.nameTc like ? OR m.nameEn like ? )");
			params.add("%" + keyWord + "%");
			params.add("%" + keyWord + "%");
			params.add("%" + keyWord + "%");
		}
		hql.append(" ORDER BY m.orderBy");
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			iterationChildSysMenus(jsonPaging.getList());
		}
		return jsonPaging;
	}
	
	/**
	 *  子菜单拼装
	 * @author wwluo
	 * @date 2017-05-26
	 * @param menus
	 */
	private void iterationChildSysMenus(List<SysMenu> menus){
		for (SysMenu sysMenu : menus) {
			if(sysMenu.getParent() != null){
				sysMenu.setParentId(sysMenu.getParent().getId());
			}
			if(sysMenu.getChildSet() != null && !sysMenu.getChildSet().isEmpty()){
				sysMenu.setChilds(new ArrayList<SysMenu>(sysMenu.getChildSet()));
				iterationChildSysMenus(sysMenu.getChilds());
			}
			sysMenu.setParent(null);
		}
	}

	/**
	 * 删除菜单（物理删除，包含子菜单）
	 * @author wwluo
	 * @date 2017-06-16
	 * @param id sys_menu.id
	 */
	@Override
	public Boolean deleteSysMenu(String id) {
		Boolean flag = false;
		if (StringUtils.isNotBlank(id)) {
			StringBuffer hql = new StringBuffer(""
					+ " FROM"
					+ " SysMenu s"
					+ " WHERE"
					+ " s.id=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(id);
			List<SysMenu> sysMenus = baseDao.find(hql.toString(), params.toArray(), false);
			if(sysMenus != null && !sysMenus.isEmpty()){
				for (SysMenu sysMenu : sysMenus) {
					deleteChildSysMenu(sysMenu.getId());
					deleteRoleMenu(id);
					baseDao.delete(sysMenu);
				}
			}
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 物理删除子菜单
	 * @author wwluo
	 * @date 2017-06-16
	 * @param id sys_menu.id
	 */
	private void deleteChildSysMenu(String id){
		StringBuffer hql = new StringBuffer(""
				+ " FROM"
				+ " SysMenu s"
				+ " WHERE"
				+ " s.parent.id=?"
				+ "");
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		List<SysMenu> sysMenus = baseDao.find(hql.toString(), params.toArray(), false);
		if(sysMenus != null && !sysMenus.isEmpty()){
			for (SysMenu sysMenu : sysMenus) {
				deleteChildSysMenu(sysMenu.getId());
				deleteRoleMenu(id);
				baseDao.delete(sysMenu);
			}
		}
	}
	
	/**
	 * 删除角色菜单
	 * @author wwluo
	 * @date 2017-06-16
	 * @param id sys_menu.id
	 */
	private Integer deleteRoleMenu(String id){
		StringBuffer hql = new StringBuffer(""
				+ " DELETE"
				+ " FROM"
				+ " SysRoleMenu s"
				+ " WHERE"
				+ " s.menu.id=?"
				+ "");
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		return baseDao.updateHql(hql.toString(), params.toArray());
	}
}
