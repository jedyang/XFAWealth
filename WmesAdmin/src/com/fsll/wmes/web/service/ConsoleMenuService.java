/**
 * 
 */
package com.fsll.wmes.web.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberMenu;

/**
 * @author scshi_u330p
 *前台菜单接口
 */
public interface ConsoleMenuService {
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
}
