package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
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
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;

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

	@Override
	public JsonPaging findAllRole(JsonPaging jsonPaging, SysRole sysRole) {
		String hql = " from SysRole r where r.isValid='1' ";
		List params = new ArrayList();
		if (null != sysRole) {
			if (null != sysRole.getName() && !"".equals(sysRole.getName())) {
				hql += " and (r.name like '%" + sysRole.getName() + "%')";
			}
			if(null!=sysRole.getCode() && !"".equals(sysRole.getCode())){
				hql += " and  r.code like '%" + sysRole.getCode() + "%'";
			}

			if (null != sysRole.getType() && !"".equals(sysRole.getType())) {
				String type = sysRole.getType();
				hql += " and r.type='" + type + "'";
				/*
				 * if (!CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(type))
				 * { hql += " and r.relateId='" + sysRole.getRelateId() + "'"; }
				 */
			}

		}

		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		Iterator it = (Iterator) jsonPaging.getList().iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			SysRole type = (SysRole) it.next();
			SysRoleVO vo = new SysRoleVO();
			vo.setId(type.getId());
			vo.setCode(type.getCode());
			vo.setName(type.getName());
			vo.setCreateTime(type.getCreateTime());
			vo.setCreateBy(null == type.getCreateBy() ? "" : type.getCreateBy().getNickName());
			vo.setType(type.getType());
			//vo.setRelateId(type.getRelateId());
			//if (null != type.getRelateId() && !"".equals(type.getRelateId())) {
				if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(type.getType())) {
					vo.setCompanyName("");
				} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(type.getType())) {
					//MemberIfafirm ifafirm = ifafirmService.findById(type.getRelateId());
					//if (null != ifafirm) {
						//String companyName = ifafirm.getCompanyName();
						//vo.setCompanyName(companyName);
					//}

				} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(type.getType())) {
					//MemberDistributor distributor = distributorService.findDistributorById(type.getRelateId());
					//if (null != distributor) {
					//	String companyName = distributor.getCompanyName();
					//	vo.setCompanyName(companyName);
					//}

				}
			//}else {
			//	vo.setCompanyName("");
			//}

			list.add(vo);

		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public SysRole findRoleById(String id) {
		SysRole sysRole = (SysRole) this.baseDao.get(SysRole.class, id);
		// TODO Auto-generated method stub
		return sysRole;
	}

	@Override
	public SysRole saveRole(SysRole sysRole) {
		if (null == sysRole.getId() || "".equals(sysRole.getId())) {
			sysRole = (SysRole) this.baseDao.create(sysRole);
		} else {
			sysRole = (SysRole) this.baseDao.update(sysRole);
		}
		return sysRole;
	}

	@Override
	public SysRole deleteRole(SysRole sysRole) {
		if (null != sysRole) {
			sysRole.setIsValid("0");
			sysRole = (SysRole) this.baseDao.update(sysRole);
		}
		// TODO Auto-generated method stub
		return sysRole;
	}

	@Override
	public JsonPaging findUserInRole(JsonPaging jsonPaging, SysRole sysRole) {
		String hql = " from MemberBase r inner join SysRoleMember s on r.id=s.member.id where s.role.id='" + sysRole.getId() + "' and r.isValid='1'";
		List params = new ArrayList();
		if (null != sysRole && null != sysRole.getName() && !"".equals(sysRole.getName())) {
			hql += " and (r.loginCode like '%" + sysRole.getName() + "%' or r.nickName like '%" + sysRole.getName() + "%')";
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);

		return jsonPaging;
	}

	@Override
	public JsonPaging findUserNotInRole(JsonPaging jsonPaging, SysRole sysRole) {
		String hql = " from MemberBase r where r.id not in (select s.member.id from SysRoleMember s where s.role.id='" + sysRole.getId() + "') and r.isValid='1' ";

		List params = new ArrayList();
		if (null != sysRole && null != sysRole.getName() && !"".equals(sysRole.getName())) {
			hql += " and (r.loginCode like '%" + sysRole.getName() + "%' or r.nickName like '%" + sysRole.getName() + "%')";
		}
		if (null != sysRole && null != sysRole.getType() && !"".equals(sysRole.getType())) {
			String type = sysRole.getType();
			if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(type)) {
				//hql += "and r.id in (select m.member.id from  MemberAdmin m where m.ifafirm.id='" + sysRole.getRelateId() + "')";
			} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(type)) {
				//hql += "and r.id in (select m.member.id from  MemberAdmin m where m.distributor.id='" + sysRole.getRelateId() + "')";
			} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(type)) {
				hql += "and r.id in (select m.member.id from  MemberAdmin m where m.type='" + type + "')";
			}
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);

		return jsonPaging;
	}

	//@Transactional
	@Override
	public boolean saveRoleMember(List list) {
		if (null == list || list.isEmpty()) {
			return false;
		}
		Iterator it = list.iterator();
		while (it.hasNext()) {
			SysRoleAdmin vo = (SysRoleAdmin) it.next();
			vo = (SysRoleAdmin) this.baseDao.create(vo);
			if (null == vo)
				break;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean deleteRoleUser(String roleId, String memberId) {
		try {
			String hql = " delete from SysRoleMember r where 1=1 ";
			List param = new ArrayList();
			hql += " and r.role.id=?";
			param.add(roleId);
			hql += " and r.member.id=?";
			param.add(memberId);
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
					MemberIfafirm ifafirm = ifafirmService.findById(relateId);
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
	
	

	@Override
	public List getMenuTree(String roleId, String parentRoleId,String langCode) {
		String sql="SELECT DISTINCT m.id,m.parent_id,m.name_"+langCode+" name, case when r.`id` IS NULL  THEN 0 ELSE 1 END powerfull "+
		           " FROM sys_menu m  LEFT JOIN (SELECT  *  FROM `sys_role_menu`  WHERE role_id = '"+roleId+"') r  ON m.id = r.`menu_id`"+
		            " LEFT JOIN   sys_role_menu a ON m.id=a.menu_id";
		if(null!=parentRoleId && !"".equals(parentRoleId)){
			sql+="  WHERE LOCATE(a.role_id,'"+parentRoleId+"')";
		}
		List datalist=springJdbcQueryManager.springJdbcQueryForList(sql);
		List list=new ArrayList();
		MenuTreeVO allMenu=new MenuTreeVO();
		if(CommonConstants.LANG_CODE_EN.equals(langCode)){
			allMenu.setName("All");
		}else {
			allMenu.setName("全部");
		}
		allMenu.setId("");
		allMenu.setpId(null);
		
		if(null!=datalist && !datalist.isEmpty()){
			Iterator it=(Iterator)datalist.iterator();
			while (it.hasNext()) {
				Map object = (Map) it.next();
				String id=getMapObject(object, "id");
				String pid=getMapObject(object, "parent_id");
				String name=getMapObject(object, "name");
				String checked=getMapObject(object, "powerfull");
				
				MenuTreeVO vo=new MenuTreeVO();
				vo.setId(id);
				vo.setpId(pid);
				vo.setName(name);
				if("1".equals(checked)){
					vo.setChecked(true);
					allMenu.setChecked(true);
				}
				
				else {
					vo.setChecked(false);
				}
				list.add(vo);
				
			}
			list.add(allMenu);
		}
		return list;
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
	public List findAutoNameList(String field,String keyWork) {
		String sql="select "+field+" from sys_role ";
		if(null!=keyWork && !"".equals(keyWork)){
			sql+=" where "+field+" like '%"+keyWork+"%'";
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
