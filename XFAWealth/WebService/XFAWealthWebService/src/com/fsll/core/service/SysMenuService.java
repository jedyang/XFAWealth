package com.fsll.core.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysMenu;
/***
 * 业务接口类：系统菜单管理
 * @author tan
 * @date 2016-7-125
 */
public interface SysMenuService {
	/***
	 * 列表查询的方法
	 * @param menu
	 * @param jsonpaging
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging,SysMenu menu);
	/***
	 * 根据id查询用户对象的方法
	 * @param menu 菜单项
	 * @return
	 */
	public SysMenu findMenuById(SysMenu menu);
	 
	/***
	 * 保存对象的方法
	 * @param menu 菜单对象
	 * @return
	 */
	public SysMenu saveOrUpdate(SysMenu menu,boolean isAdd);
	/***
	 * 删除对象的方法
	 * @param ids id
	 * @return
	 */
	public boolean deleteObject(String ids);
    /***
     * 检查是否存在子节点
     * @param menu
     * @return
     */
    public boolean checkChildrenExist(SysMenu menu);

	
	 /***
     * 查询所有有效的菜单
     */
    public List<SysMenu> findAllMenu();


	  

}
