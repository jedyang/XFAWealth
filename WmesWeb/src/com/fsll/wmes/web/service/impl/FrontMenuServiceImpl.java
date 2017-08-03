/**
 * 
 */
package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.web.service.FrontMenuService;

/**
 * @author scshi_u330p
 *
 */
@Service("frontMenuService")
//@Transactional
public class FrontMenuServiceImpl  extends BaseService implements FrontMenuService {

	/**
	 * 根据条件获取用户菜单
	 */
	@Override
	public JsonPaging findAll(JsonPaging jsonPaging, MemberMenu frontMenu) {
		String hql=" from MemberMenu where isValid='1'";
		List<Object> params=new ArrayList<Object>();
		if(null!=frontMenu.getCode()&&!"".equals(frontMenu.getCode())){
			hql+=" and code like ? ";
			params.add("%"+frontMenu.getCode()+"%");
		}
		if(null!=frontMenu.getWebUrl()&&!"".equals(frontMenu.getWebUrl())){
			hql+=" and webUrl like ? ";
			params.add("%"+frontMenu.getWebUrl()+"%");
		}
		if(null!=frontMenu.getNameSc()&&!"".equals(frontMenu.getNameSc())){
			hql+=" and ( nameSc like ?  or nameTc like ? or nameEn like ? )";
			
			params.add("%"+frontMenu.getNameSc()+"%");
			params.add("%"+frontMenu.getNameSc()+"%");
			params.add("%"+frontMenu.getNameSc()+"%");
		}
		
		if(null!=frontMenu.getKeyWord()&&!"".equals(frontMenu.getKeyWord())){
			hql+=" and ( code like ? or nameSc like ?  or nameTc like ? or nameEn like ? or webUrl like ? )";
			
			params.add("%"+frontMenu.getKeyWord()+"%");
			params.add("%"+frontMenu.getKeyWord()+"%");
			params.add("%"+frontMenu.getKeyWord()+"%");
			params.add("%"+frontMenu.getKeyWord()+"%");
			params.add("%"+frontMenu.getKeyWord()+"%");			
		}
		hql+=" order by orderBy ";
		
		jsonPaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<MemberMenu> list = getTreeMenu(jsonPaging.getList());
		jsonPaging.setList(list);

		return jsonPaging;
	}

	List<MemberMenu> treeList;
	public List<MemberMenu> getTreeMenu(List<MemberMenu> allList) {
		treeList = new ArrayList<MemberMenu>();
		if (null!=allList && !allList.isEmpty())
		for (MemberMenu menuVo : allList) {
            if (menuVo.getParent() == null) {  
                treeList.add(menuVo);
                build(allList, menuVo);  
            } 
		}
		return treeList;
	}
	
	/**
	 * 递归获取子节点集
	 */
	private void build(List<MemberMenu> allNodes,MemberMenu node){  
        List<MemberMenu> children = getChildren(allNodes,node);  
        node.setChilds(children);
        if (null!=children && !children.isEmpty()) {   
            for (MemberMenu child : children) {  
//            	treeList.add(child);  
                build(allNodes,child);  
            }  
        }   
    }
	
	/**
	 * 通过结果集获取指定节点的子节点集
	 */
    private List<MemberMenu> getChildren(List<MemberMenu> nodes, MemberMenu node){  
        List<MemberMenu> children = new ArrayList<MemberMenu>();  
        String id = node.getId();  
        if (null!=nodes && !nodes.isEmpty())
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
		String hql=" from MemberMenu where isValid='1' order by orderBy asc";
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
	
    /***
     * 通过角色查询前台用户有效的菜单
     * @param role 角色编码：investor,IFA,IFA_FIRM,distributor,guest,sys (参照CoreConstants.FRONT_USER_ROLE_*)
     * @return list
     */
	public List<MemberMenu> findFrontMenuByRole(String role) {
		String hql="select m from MemberMenu m left join MemberRoleMenu rm on m.id=rm.menu.id " +
				"left join MemberRole r on rm.role.id=r.id ";
		hql += " where isValid='1' and code = '"+role+"' ";
		hql += " order by orderBy asc";
		List<MemberMenu> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			return list;
		}else {
			return null;
		}	
	}
	
    /***
     * 通过角色查询工作台用户有效的菜单
     * @param type 角色类型：1 - ifafirm创建的角色,2 - distributor创建的角色'
     * @param memberId 用户id
     * @return list
     */
	public List<MemberMenu> findConsoleMenuByRole(String type, String memberId) {
		String hql="select m from MemberMenu m left join MemberConsoleRoleMenu rm on m.id=rm.menu.id " +
				"left join MemberConsoleRole r on rm.role.id=r.id " +
				"left join MemberConsoleRoleMember b on r.role.id=b.member.id ";
		hql += " where isValid='1' and b.member.id = '"+memberId+"' ";
		hql += " order by orderBy asc";
		List<MemberMenu> list = this.baseDao.find(hql, null, false);
		if(null != list && !list.isEmpty()){
			return list;
		}else {
			return null;
		}
	}
	
	/**
	 *  前台菜单列表查询
	 * @author wwluo
	 * @date 2017-05-26
	 * @param jsonPaging
	 * @param companyId 运营公司 company_info.id
	 * @param roleCode 菜单编码 member_role.code
	 * @param keyword 关键字查询（匹配sys_menu.name_sc、sys_menu.name_tc、sys_menu.name_en）
	 * @return JsonPaging
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getFrontMenus(JsonPaging jsonPaging, String companyId, String roleCode,
			String keyWord, String isValid) {
		Object[] object = getFrontMenuSql(companyId, roleCode, keyWord, isValid);
		if(object != null){
			jsonPaging = this.baseDao.selectJsonPaging((String) object[0], (Object[]) object[1], jsonPaging, false);
			if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
				iterationFrontChildMenus(jsonPaging.getList(), isValid);
			}
		}
		return jsonPaging;
	}
	
	/**
	 *  前台菜单列表查询
	 * @author wwluo
	 * @date 2017-05-26
	 * @param companyId 运营公司 company_info.id
	 * @param roleCode 菜单编码 member_role.code
	 * @param keyword 关键字查询（匹配sys_menu.name_sc、sys_menu.name_tc、sys_menu.name_en）
	 * @return List<MemberMenu>
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MemberMenu> getFrontMenus(String companyId, String roleCode,
			String keyWord, String isValid) {
		List<MemberMenu> menus = null;
		Object[] object = getFrontMenuSql(companyId, roleCode, keyWord, isValid);
		if(object != null){
			menus = this.baseDao.find((String) object[0], (Object[]) object[1], false);
			if(menus != null && !menus.isEmpty()){
				iterationFrontChildMenus(menus, isValid);
			}
		}
		return menus;
	}
	
	/**
	 *  前台子菜单拼装
	 * @author wwluo
	 * @date 2017-05-26
	 * @param menus
	 */
	private void iterationFrontChildMenus(List<MemberMenu> menus, String isValid){
		for (MemberMenu memberMenu : menus) {
			if(memberMenu.getParent() != null){
				memberMenu.setParentId(memberMenu.getParent().getId());
			}
			if(memberMenu.getChildSet() != null && !memberMenu.getChildSet().isEmpty()){
				List<MemberMenu> temp = new ArrayList<MemberMenu>(memberMenu.getChildSet());
				for (MemberMenu menu : temp) {
					if (StringUtils.isNotBlank(isValid) && !isValid.equals(menu.getIsValid())) {
						memberMenu.getChildSet().remove(menu);
					}
				}
				memberMenu.setChilds(new ArrayList<MemberMenu>(memberMenu.getChildSet()));
				iterationFrontChildMenus(memberMenu.getChilds(), isValid);
			}
		}
	}
	
	/**
	 *  菜单列表查询，返回查询hql、hql参数
	 * @author wwluo
	 * @date 2017-05-26
	 * @param companyId 运营公司 company_info.id
	 * @param roleCode 菜单编码 member_role.code
	 * @param keyWord 关键字查询（匹配sys_menu.name_sc、sys_menu.name_tc、sys_menu.name_en）
	 * @return Object[] {hql字符串，params参数数组}
	 */
	private Object[] getFrontMenuSql(String companyId, String roleCode,
			String keyWord, String isValid) {
		Object[] object = new Object[2];
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " m"
				+ " FROM"
				+ " MemberMenu m"
				+ " LEFT JOIN"
				+ " MemberRoleMenu r"
				+ " ON"
				+ " m.id=r.menu.id"
				+ " LEFT JOIN"
				+ " MemberRole a"
				+ " ON"
				+ " a.id=r.role.id"
				+ " LEFT JOIN"
				+ " MemberCompanyMenu b"
				+ " ON"
				+ " b.menu.id=m.id"
				+ " WHERE"
				+ " m.parent IS NULL"
				+ "");
		List<Object> params = new ArrayList<Object>();
		if (StringUtils.isNotBlank(companyId)) {
			hql.append(" AND b.company.id=?");
			params.add(companyId);
		}
		if (StringUtils.isNotBlank(isValid)) {
			hql.append(" AND m.isValid=?");
			params.add(isValid);
		}
		if (StringUtils.isNotBlank(roleCode)) {
			hql.append(" AND a.code=?");
			params.add(roleCode);
		}
		if (StringUtils.isNotBlank(keyWord)) {
			hql.append(" AND ( m.nameSc like ? OR m.nameTc like ? OR m.nameEn like ? )");
			params.add("%" + keyWord + "%");
			params.add("%" + keyWord + "%");
			params.add("%" + keyWord + "%");
		}
		hql.append(" ORDER BY m.orderBy");
		object[0] = hql.toString();
		object[1] = params.toArray();
		return object;
	}
}
