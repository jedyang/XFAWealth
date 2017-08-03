/**
 * 
 */
package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysMenu;
import com.fsll.wmes.entity.MemberMenu;

/**
 * @author scshi_u330p
 *前台菜单接口
 */
public interface FrontMenuService {
	/**
	 * 前台菜单列表
	 * @author scshi_u330p
	 * @date 20161102
	 * */
	public JsonPaging findAll(JsonPaging jsonPaging, MemberMenu frontMenu);
	
	 /***
     * 查询所有有效的菜单
     */
	public List<MemberMenu> findAllMenu();
	
	/***
	 * 根据id查询用户对象的方法
	 * @param menu 菜单项
	 * @return
	 */
	public MemberMenu findMenuById(String id);
	
	/**
	 * 保存菜单
	 * @author scshi_u330p
	 * @date 20161103
	 * */
	public MemberMenu saveOrUpdate(MemberMenu updateparam, boolean isAdd);

	/**
	 * 删除菜单(逻辑删除)
	 * @author scshi_u330p
	 * @date 20161103
	 * */
	public boolean deleteMenuInfo(String menuId);

	/**
	 *  前台菜单列表查询
	 * @author wwluo
	 * @date 2017-05-26
	 * @param roleCode jsonPaging
	 * @param roleCode 菜单编码 member_role.code
	 * @param keyWord 关键字查询（匹配sys_menu.name_sc、sys_menu.name_tc、sys_menu.name_en）
	 * @return JsonPaging
	 */
	public JsonPaging getFrontMenus(JsonPaging jsonPaging, String roleCode,
			String keyWord, String isValid);

	/**
	 *  按角色获取有效的前台菜单集合
	 * @author wwluo
	 * @date 2017-05-26
	 * @param roleCode 菜单编码 member_role.code
	 * @return List<MemberMenu>
	 */
	public List<MemberMenu> getFrontMenus(String roleCode);
			
	/**
	 * 删除菜单（物理删除，包含子菜单）
	 * @author wwluo
	 * @date 2017-06-16
	 */
	public Boolean deleteMemberMenu(String id);

}
