package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.entity.SysMenu;
import com.fsll.core.entity.SysRole;
import com.fsll.core.entity.SysRoleAdmin;
import com.fsll.core.entity.SysRoleMenu;
import com.fsll.core.entity.SysRoleUsergroup;
import com.fsll.core.entity.SysUsergroup;
import com.fsll.core.service.SysRoleService;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.core.vo.SysRoleVO;
import com.fsll.core.vo.SysUserGroupVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.web.service.DistributorService;

/***
 * 业务接口实现类：角色管理接口类
 * 
 * @author mqzou
 * @date 2016-07-11
 */
@Service("sysRoleService")
//@Transactional
public class SysRoleServiceImpl extends BaseService implements SysRoleService {

	@Autowired
	private IfafirmManageService ifafirmService;
	@Autowired
	private DistributorService distributorService;

	/**
	 * 获取角色列表
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	@Override
	public JsonPaging findAllRole(JsonPaging jsonPaging, SysRoleVO roleVO) {
		String hql = " from SysRole r where 1=1 ";
		List<String> params = new ArrayList<String>();
		if (null != roleVO) {
			if (StringUtils.isNotBlank(roleVO.getKeyWord())) {
				hql += " and ( r.name like ? or  r.code like ? ) ";
				params.add("%"+roleVO.getKeyWord()+"%");
				params.add("%"+roleVO.getKeyWord()+"%");
			}
			if (StringUtils.isNotBlank(roleVO.getType())) {
				hql += " and r.type=? ";
				params.add(roleVO.getType());
			}
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		Iterator it = (Iterator) jsonPaging.getList().iterator();
		List<SysRoleVO> list = new ArrayList<SysRoleVO>();
		while (it.hasNext()) {
			SysRole role = (SysRole) it.next();
			SysRoleVO vo = new SysRoleVO(role);
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 根据Id获取角色信息
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	@Override
	public SysRole findRoleById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		Object obj = this.baseDao.get(SysRole.class, id);
		if(obj != null){
			return (SysRole)obj;
		}
		return null;
	}

	/**
	 * 更新保存角色
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	@Override
	public SysRole saveOrUpdate(SysRole sysRole) {
		sysRole = (SysRole) baseDao.saveOrUpdate(sysRole);
		return sysRole;
	}

	@Override
	public boolean deleteRole(String roleId) {
		SysRole role = findRoleById(roleId);
		if(role!=null){
			baseDao.delete(role);
			return true;
		}
		return false;
	}

	/**
	 *  获取角色下的所有用户列表
	 * @author qgfeng
	 * @date 2016-11-23
	 * @param roleId 角色id
	 * @param keyWord 关键字（账号、昵称）
	 */
	@Override
	public JsonPaging findUserInRole(JsonPaging jsonPaging,String roleId,String keyWord) {
		String hql = " select admin from SysRoleAdmin rm where rm.role.id=? and rm.admin.isValid='1' ";
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		if(StringUtils.isNotBlank(keyWord)) {
			hql += " and (rm.admin.loginCode like ? or rm.member.nickName like ? )";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}

	/**
	 * 获取不在某个角色下的所有用户列表
	 * @author qgfeng
	 * @date 2016-12-2
	 */
	@Override
	public JsonPaging findUserNotInRole(JsonPaging jaonPaging,String roleId,String keyWord) {
		String hql = " from SysAdmin r where r.id not in (select s.admin.id from SysRoleAdmin s where s.role.id=?) and r.isValid='1' ";
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		if (StringUtils.isNotBlank(keyWord)) {
			hql += " and (r.loginCode like ? or r.nickName like ?)";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		jaonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jaonPaging, false);
		return jaonPaging;
	}

	/**
	 * 批量添加用户到角色
	 * @author qgfeng
	 * @date 2016-11-23
	 */
	//@Transactional
	@Override
	public boolean saveRoleMember(String roldId, String[] adminIds) {
		SysRole role = findRoleById(roldId);
		if(role==null){
			return false;
		}
		if(adminIds!=null){
			for (int i = 0; i < adminIds.length; i++) {
				String adminId = adminIds[i];
				if(StringUtils.isNotBlank(adminId)){
					SysAdmin admin = new SysAdmin();
					admin.setId(adminId);
					SysRoleAdmin rm = new SysRoleAdmin();
					rm.setRole(role);
					rm.setAdmin(admin);
					baseDao.saveOrUpdate(rm, true);
				}
			}
		}
		return true;
	}

	@Override
	public boolean deleteRoleUser(String roleId, String adminId) {
		try {
			String hql = " delete from SysRoleAdmin r where 1=1 and r.role.id=? and r.admin.id=? ";
			List<String> param = new ArrayList<String>();
			param.add(roleId);
			param.add(adminId);
			int count = this.baseDao.updateHql(hql, param.toArray());
			if (count > 0){
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean checkRoleCode(String roleId, String code) {
		String hql = " from SysRole r where 1=1";
		List params = new ArrayList();

		if (null != roleId && !"".equals(roleId)) {
			hql += " and r.id<>? ";
			params.add(roleId);
		}
		if (null != code && !"".equals(code)) {
			hql += " and r.code=?";
			params.add(code);
		}
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty())
			return false;
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public JsonPaging findGroupInRole(JsonPaging jsonPaging, SysRole sysRole) {

		String hql = " from SysUsergroup r where r.id in (select s.group.id from SysRoleUsergroup s where s.role.id='" + sysRole.getId() + "')";

		if(null!=sysRole.getName() && !"".equals(sysRole.getName())){
			hql+=" and r.name like '%"+sysRole.getName()+"'";
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql, null, jsonPaging, false);
		Iterator it = (Iterator) jsonPaging.getList().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			SysUsergroup usergroup = (SysUsergroup) it.next();
			SysUserGroupVO vo = new SysUserGroupVO();
			vo.setId(usergroup.getId());
			vo.setCreateBy(null != usergroup.getCreateBy() ? usergroup.getCreateBy().getNickName() : "");
			vo.setLastUpdate(usergroup.getLastUpdate().toString());
			//vo.setRelateId(usergroup.getRelateId());
			vo.setRemark(usergroup.getRemark());
			//vo.setType(usergroup.getType());
			vo.setName(usergroup.getName());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public JsonPaging findGroupNotInRole(JsonPaging jsonPaging, SysRole sysRole) {
		String hql = " from SysUsergroup r where r.id not in (select s.group.id from SysRoleUsergroup s where s.role.id='" + sysRole.getId() + "')";

		String type = sysRole.getType();
		if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(type)) {
			hql += " and r.type='" + type + "'";
		} else {
			//hql += " and r.relateId='" + sysRole.getRelateId() + "'";
		}

		jsonPaging = this.baseDao.selectJsonPaging(hql, null, jsonPaging, false);
		Iterator it = (Iterator) jsonPaging.getList().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			SysUsergroup usergroup = (SysUsergroup) it.next();
			SysUserGroupVO vo = new SysUserGroupVO();
			vo.setId(usergroup.getId());
			vo.setCreateBy(null != usergroup.getCreateBy() ? usergroup.getCreateBy().getNickName() : "");
			vo.setLastUpdate(usergroup.getLastUpdate().toString());
			//vo.setRelateId(usergroup.getRelateId());
			vo.setRemark(usergroup.getRemark());
			//vo.setType(usergroup.getType());
			vo.setName(usergroup.getName());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public boolean saveGroup(List list) {
		if (null == list || list.isEmpty()) {
			return false;
		}
		Iterator it = list.iterator();
		while (it.hasNext()) {
			SysRoleUsergroup vo = (SysRoleUsergroup) it.next();
			vo = (SysRoleUsergroup) this.baseDao.create(vo);
			if (null == vo)
				break;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean deleteGroup(String roleId, String groupId) {
		try {
			String hql = " delete from SysRoleUsergroup r where 1=1 ";
			List param = new ArrayList();
			hql += " and r.role.id=?";
			param.add(roleId);
			hql += " and r.group.id=?";
			param.add(groupId);
			int count = this.baseDao.updateHql(hql, param.toArray());
			if (count > 0)
				return true;
			/*
			 * if(null!=list && list.size()>0){ SysRoleMember
			 * sysRoleMember=(SysRoleMember)list.get(0);
			 * this.baseDao.delete(sysRoleMember); return true; }
			 */
			return false;

		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public JsonPaging findAllRolePro(JsonPaging jsonPaging, SysRole sysRole) {
		int pageIndex = (jsonPaging.getPage() - 1) * jsonPaging.getRows();
		int pageSize = jsonPaging.getRows();
		String keyWord = "";
		String id = "";

		if (null != sysRole) {
			if (null != sysRole.getCreateBy()) {
				id = sysRole.getCreateBy().getId();
			}
			if (null != sysRole.getName() && !"".equals(sysRole.getName())) {
				keyWord = sysRole.getName();
			}
		}
		String sql = "CALL  pro_getrolelist('" + id + "'," + pageIndex + "," + pageSize + ",'" + keyWord + "',@a)";

		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql);
		Iterator it = (Iterator) resultList.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Map type = (Map) it.next();
			SysRoleVO vo = new SysRoleVO();
			vo.setId(getMapObject(type, "id"));
			vo.setCode(getMapObject(type, "code"));
			vo.setName(getMapObject(type, "name"));
			// vo.setCreateTime(sdf.parse(getMapObject(type,"create_time")));
			vo.setCreateBy(getMapObject(type, "create_by_name"));
			String typeString = getMapObject(type, "type");
			vo.setType(typeString);
			String relateId = getMapObject(type, "relate_id");
			vo.setRelateId(relateId);

			if (null != relateId && !"".equals(relateId)) {
				if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(typeString)) {
					vo.setCompanyName("");
				} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(typeString)) {
					MemberIfafirm ifafirm = ifafirmService.findById(relateId,null);
					if (null != ifafirm) {
						String companyName = ifafirm.getCompanyName();
						vo.setCompanyName(companyName);
					}

				} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(typeString)) {
					MemberDistributor distributor = distributorService.findDistributorById(relateId);
					if (null != distributor) {
						String companyName = distributor.getCompanyName();
						vo.setCompanyName(companyName);
					}

				}
			}else {
				vo.setCompanyName("");
			}

			list.add(vo);

		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}
	

	/**
	 * 获取权限菜单树
	 * @author qgfeng
	 * @date 2016-12-1
	 */
	@Override
	public List<MenuTreeVO> getMenuTree(String roleId,String langCode) {
		//所有前台菜单
		String hql =" from SysMenu m where m.isValid='1' order by orderBy ";
		List<SysMenu> allMenu = baseDao.find(hql,null, false);
		//角色菜单
		hql = " from SysRoleMenu rm where rm.role.id=?";
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		List<SysRoleMenu> roleMenu = baseDao.find(hql,params.toArray(), false);
		Map<String,String> roleMap =new HashMap<String, String>();
		if(roleMenu!=null && !roleMenu.isEmpty()){
			for (SysRoleMenu rm : roleMenu) {
				roleMap.put(rm.getMenu().getId(), "0");
			}
		}
		//封装数据
		List<MenuTreeVO> listVo=new ArrayList<MenuTreeVO>();
		if(null!=allMenu && !allMenu.isEmpty()){
			Iterator<SysMenu> it=allMenu.iterator();
			while (it.hasNext()) {
				SysMenu menu = it.next();
				MenuTreeVO vo=new MenuTreeVO();
				vo.setId(menu.getId());
				vo.setName(menu.getMenuName(langCode));
				if(menu.getParent()!=null){
					vo.setpId(menu.getParent().getId());
				}
				if(roleMap.containsKey(vo.getId())){
					vo.setChecked(true);
				}
				listVo.add(vo);
			}
		}
		return listVo;
	}
	/**
	 * 保存角色菜单
	 * @author qgfeng
	 * @date 2016-12-1
	 * @param MenuIds 菜单id集合
	 * @param roleId 角色id
	 */
	@Override
	public boolean saveRoleMenu(String[] menuIds,String roleId) {
		SysRole role = findRoleById(roleId);
		if(role == null){
			return false;
		}
		//删除角色关联的菜单
		String hql =" delete from SysRoleMenu rm where rm.role.id=?";
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		baseDao.updateHql(hql, params.toArray());
		//保存新角色菜单
		for (String menuId : menuIds) {
			SysRoleMenu roleMenu = new SysRoleMenu();
			SysMenu menu = new SysMenu();
			menu.setId(menuId);
			roleMenu.setRole(role);
			roleMenu.setMenu(menu);
			baseDao.saveOrUpdate(roleMenu, true);
		}
		return true;
	}

	@Override
	public String getMemberRoleId(String memberId) {
		String sql="SELECT a.`role_id` FROM `sys_role_member` a WHERE a.`member_id`='"+memberId+"'  UNION"+
		            "  SELECT a.`role_id` FROM  `sys_role_usergroup` a LEFT JOIN `sys_usergroup_member` b ON a.`group_id`=b.`group_id` WHERE b.`member_id`='"+memberId+"' ;";
		List dataList=springJdbcQueryManager.springJdbcQueryForList(sql);
		String roleId="";
		if(null!=dataList && !dataList.isEmpty()){
			Iterator it=(Iterator)dataList.iterator();
			while (it.hasNext()) {
				Map object = (Map) it.next();
				String id=null!=object.get("role_id")?object.get("role_id").toString():"";
				if("".equals(id))
					continue;
			    roleId+=id+",";
			    
			}
		}
		if(roleId.lastIndexOf(",")>0){
			roleId=roleId.substring(0, roleId.length()-1);
			
		}
		return roleId;
	}
	

	//@Transactional
	@Override
	public boolean saveRoleMenu(List list,String roleId) {
		if (null == list ){
			list=new ArrayList();
		}
		String sql1=" call pro_saverolemenu('"+roleId+"')";
		springJdbcQueryManager.execute(sql1);
		
		String sql="delete from sys_role_menu where role_id='"+roleId+"'";
		springJdbcQueryManager.execute(sql);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			SysRoleMenu vo = (SysRoleMenu) it.next();
			vo = (SysRoleMenu) this.baseDao.create(vo);
			if (null == vo)
				break;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public List findAutoNameList(String field,String keyWord) {
		String sql="select "+field+" from sys_role ";
		if(null!=keyWord && !"".equals(keyWord)){
			sql+=" where "+field+" like '%"+keyWord+"%'";
		}
		sql+=" limit 0,6";
		List list=springJdbcQueryManager.springJdbcQueryForList(sql);// this.baseDao.find(sql, null, false);
		
		return list;
	}

	@Override
	public List findUserInRole(String roleId) {
		String hql=" FROM SysRoleMember r WHERE r.role.id='"+roleId+"'";
		List list=this.baseDao.find(hql, null, false);
		
		/*List reList=new ArrayList();
		Iterator it=list.iterator();
		while (it.hasNext()) {
			SysRoleAdmin object = (SysRoleAdmin) it.next();
			//reList.add(object.getMember());
		}*/
		
		return list;
	}
}
