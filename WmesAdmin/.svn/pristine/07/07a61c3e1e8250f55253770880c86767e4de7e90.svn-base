/**
 * 
 */
package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.web.service.ConsoleMenuService;

/**
 * @author scshi_u330p
 *
 */
@Service("consoleMenuService")
//@Transactional
public class ConsoleMenuServiceImpl  extends BaseService implements ConsoleMenuService {

	@Override
	public JsonPaging findAll(JsonPaging jsonPaging, MemberMenu frontMenu) {
		String hql=" from MemberMenu where (code like 'csa_%' or code like 'csb_%') ";
		List<Object> params=new ArrayList<Object>();
		if(null!=frontMenu.getNameSc()&&!"".equals(frontMenu.getNameSc())){
			hql+=" and ( code like ? or nameSc like ?  or nameTc like ? or nameEn like ? or webUrl like ?  )";
			params.add("%"+frontMenu.getNameSc()+"%");
			params.add("%"+frontMenu.getNameSc()+"%");
			params.add("%"+frontMenu.getNameSc()+"%");
			params.add("%"+frontMenu.getNameSc()+"%");
			params.add("%"+frontMenu.getNameSc()+"%");			
		}
		
		hql+=" order by orderBy ";
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		if(null==frontMenu.getNameSc() || "".equals(frontMenu.getNameSc())){		
			List<MemberMenu> list = getTreeMenu(jsonPaging.getList());
			jsonPaging.setList(list);
		}
		else {			
			List<MemberMenu> list = jsonPaging.getList();
			if(!list.isEmpty()){
				//查找当前搜索结果的根节点
				String hqlParent = " from MemberMenu where parent is NULL ";
				String hqlParentWhere = "";
				
				for (MemberMenu menuVo : list) {
					hqlParentWhere += " OR FIND_IN_SET(id,fn_queryMemberMenuParent('"+menuVo.getId()+"'))>0 ";
				}
				hqlParentWhere = " AND ( 1=2 "+hqlParentWhere+" ) ";
				hqlParent = hqlParent + hqlParentWhere; 
				List<MemberMenu> parentMenus = this.baseDao.find(hqlParent, null, false);
				if(null != parentMenus && !parentMenus.isEmpty()){
					//根据 根节点查找所有的子节点
					String hqlQuery = " from MemberMenu where 1=2 ";
					String hqlQueryWhere = "";
					for (MemberMenu menuVo : parentMenus) {
						hqlQueryWhere += " OR FIND_IN_SET(id,fn_queryMemberMenuChildren('"+menuVo.getId()+"'))>0 ";
					}
					hqlQuery = hqlQuery + hqlQueryWhere + " order by orderBy ";
					List<MemberMenu> queryList = this.baseDao.find(hqlQuery, null, false);
					queryList = getTreeMenu(queryList);
					jsonPaging.setList(queryList);
				}				
			}
		}
		return jsonPaging;
	}

	List<MemberMenu> treeList;
	public List<MemberMenu> getTreeMenu(List<MemberMenu> allList) {
		treeList = new ArrayList<MemberMenu>();
		for (MemberMenu menuVo : allList) {
            if (menuVo.getParent() == null) {  
                treeList.add(menuVo);
                build(allList, menuVo);  
            } 
		}
		return treeList;
	}
	
	private void build(List<MemberMenu> allNodes,MemberMenu node){  
        List<MemberMenu> children = getChildren(allNodes,node);  
        if (!children.isEmpty()) {   
            for (MemberMenu child : children) {  
            	treeList.add(child);  
                build(allNodes,child);  
            }  
        }   
    }
	
    private List<MemberMenu> getChildren(List<MemberMenu> nodes, MemberMenu node){  
        List<MemberMenu> children = new ArrayList<MemberMenu>();  
        String id = node.getId();  
        for (MemberMenu child : nodes) {  
            if (null != child.getParent() && id.equals(child.getParent().getId())) {  
                children.add(child);  
            }  
        }  
        return children;  
    }

    /***
     * 查询所有有效的菜单
     */
	@Override
	public List<MemberMenu> findAllMenu() {
		String hql=" from MemberMenu where isValid=1 order by orderBy asc";
		List<MemberMenu> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			return list;
		}else {
			return null;
		}	
	}
	
	/***
	 * 根据id查询用户对象的方法
	 * @param menu 菜单项
	 * @return
	 */
	@Override
	public MemberMenu findMenuById(MemberMenu parentMenu) {
		String hql="from MemberMenu where id=? ";
		List params=new ArrayList();
		params.add(parentMenu.getId());
		List<MemberMenu> list = this.baseDao.find(hql, params.toArray(), false);
		if (null!=list&&!list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 保存菜单
	 * @author scshi_u330p
	 * @date 20161103
	 * */
	public MemberMenu saveOrUpdate(MemberMenu menu, boolean isAdd){
		menu = (MemberMenu)this.baseDao.saveOrUpdate(menu,isAdd);
		return menu;
	}
	
	/**
	 * 删除叶子菜单
	 * @author scshi_u330p
	 * @date 20161103
	 * */
	public boolean deleteMenuInfo(String menuId){
		if(null!=menuId){
			MemberMenu menu=new MemberMenu();
			menu.setId(menuId);
			menu= findMenuById(menu);
			if(null!=menu){
				//删除该指定站点信息
				this.baseDao.delete(menu);
			}
		}else{
			return false;
		}
		return true;
	}
	
}
