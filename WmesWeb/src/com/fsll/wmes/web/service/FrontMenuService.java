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
	public MemberMenu findMenuById(MemberMenu parentMenu);
	
	/**
	 * 保存菜单
	 * @author scshi_u330p
	 * @date 20161103
	 * */
	public MemberMenu saveOrUpdate(MemberMenu updateparam, boolean isAdd);

	/**
	 * 删除叶子菜单
	 * @author scshi_u330p
	 * @date 20161103
	 * */
	public boolean deleteMenuInfo(String menuId);
	
    /***
     * 通过角色查询前台用户有效的菜单
     * @param role 角色编码：investor,IFA,IFA_FIRM,distributor,guest,sys (参照CoreConstants.FRONT_USER_ROLE_*)
     * @return list
     */
	public List<MemberMenu> findFrontMenuByRole(String role);
	
    /***
     * 通过角色查询工作台用户有效的菜单
     * @param type 角色类型：1 - ifafirm创建的角色,2 - distributor创建的角色'
     * @param memberId 用户id
     * @return list
     */
	public List<MemberMenu> findConsoleMenuByRole(String type, String memberId);
	
	/**
	 *  前台菜单列表查询
	 * @author wwluo
	 * @date 2017-05-26
	 * @param jsonPaging
	 * @param companyId 运营公司 company_info.id
	 * @param roleCode 菜单编码 member_role.code
	 * @param keyWord 关键字查询（匹配sys_menu.name_sc、sys_menu.name_tc、sys_menu.name_en）
	 * @return JsonPaging
	 */
	public JsonPaging getFrontMenus(JsonPaging jsonPaging, String companyId, String roleCode,
			String keyWord,String isValid);
	
	/**
	 *  前台菜单列表查询
	 * @author wwluo
	 * @date 2017-05-26
	 * @param companyId 运营公司 company_info.id
	 * @param roleCode 菜单编码 member_role.code
	 * @param keyWord 关键字查询（匹配sys_menu.name_sc、sys_menu.name_tc、sys_menu.name_en）
	 * @return List<MemberMenu>
	 */
	public List<MemberMenu> getFrontMenus(String companyId, String roleCode,
			String keyWord, String isValid);
	
}
