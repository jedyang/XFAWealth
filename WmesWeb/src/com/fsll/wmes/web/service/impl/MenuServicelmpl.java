package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.core.entity.SysMenu;
import com.fsll.core.entity.SysParamType;
import com.fsll.wmes.web.service.MenuService;

/***
 * 业务接口实现类：后台菜单管理接口类
 * 
 * @author wwluo
 * @version 1.0
 * @date 2016-07-22
 */
@Service("menuService")
//@Transactional
public class MenuServicelmpl extends BaseService implements MenuService {

	/***
	 * 获取菜单
	 * 
	 * @author wwluo
	 * @date 2016-07-22
	 */
	@SuppressWarnings("unchecked")
	@Override
	//@Transactional(readOnly = true)
	public List<SysMenu> findMenu() {
		List<SysMenu> list = new ArrayList<SysMenu>();
		String hql = " from SysMenu where isValid=1 and parent is null order by order_by";
		list = this.baseDao.find(hql, null, false);
		if (null != list && !list.isEmpty()) {
			return list;
		} else {
			return null;
		}
	}

	@Override
	public List<SysMenu> findMenuForPower(String memberId) {
		List<SysMenu> list = new ArrayList<SysMenu>();
		
		/*StringBuilder sql=new StringBuilder();
		sql.append(" SELECT * FROM (");
		sql.append(" SELECT r.`member_id`,s.* FROM sys_role_member r LEFT JOIN `sys_role_menu` m ON r.`role_id`=m.`role_id`");
		sql.append(" LEFT JOIN `sys_menu` s ON m.`menu_id`=s.`id` WHERE r.`member_id`='"+memberId+"'");
		sql.append(" UNION ");
		sql.append(" SELECT u.`member_id`,s.* FROM `sys_usergroup_member` u LEFT JOIN `sys_role_usergroup` r ON u.`group_id`=r.`group_id`");
		sql.append(" LEFT JOIN `sys_role_menu` m ON r.`role_id`=m.`role_id` LEFT JOIN `sys_menu` s ON m.`menu_id`=s.`id`");
		sql.append(" WHERE u.`member_id`='"+memberId+"')a  WHERE a.is_valid='1' ORDER BY a.order_by;");*/
		
		/*String hql = " from SysMenu r inner join  SysRoleMenu s on r.id=s.menu.id  inner join SysRole i on s.role.id=i.id ";
		hql += " inner join SysRoleMember m on i.id=m.role.id where m.member.id='" + memberId + "' and r.isValid='1' order by r.orderBy";

		List resultList = this.baseDao.find(hql, null, false);

		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			SysMenu vo = (SysMenu) obj[0];
			if (!list.contains(vo))
				list.add(vo);
		}*/

		String hql = " from SysMenu r inner join  SysRoleMenu s on r.id=s.menu.id  inner join SysRole i on s.role.id=i.id ";
		hql += " inner join SysRoleUsergroup u on i.id=u.role.id inner join SysUsergroup g on u.group.id=g.id ";
		hql += " inner join SysUsergroupAdmin  m on g.id=m.group.id where m.admin.id='" + memberId + "' and r.isValid='1' order by r.orderBy";
		List resultList = this.baseDao.find(hql, null, false);
		

		Iterator iterator = resultList.iterator();
		while (iterator.hasNext()) {
			Object[] obj = (Object[]) iterator.next();
			SysMenu vo = (SysMenu) obj[0];
			if (!list.contains(vo))
				list.add(vo);
		}
		
		return list;
	}
	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}

}
