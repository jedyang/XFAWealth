package com.fsll.core.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysRoleAdmin;
import com.fsll.core.entity.SysUsergroup;
import com.fsll.core.entity.SysUsergroupAdmin;
import com.fsll.core.service.SysUserGroupService;
import com.fsll.core.vo.SysUserGroupVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;

/***
 * 业务接口实现类：用户管理接口类
 * 
 * @author mqzou
 * @date 2016-07-20
 */
@Service("sysUserGroupServiceImpl")
//@Transactional
public class SysUserGroupServiceImpl extends BaseService implements SysUserGroupService {

	@Autowired
	private IfafirmManageService ifafirmService;
	@Autowired
	private DistributorService distributorService;

	@Override
	public SysUsergroup findSysUsergroupById(String id) {
		SysUsergroup sysUsergroup = (SysUsergroup) this.baseDao.get(SysUsergroup.class, id);
		return sysUsergroup;
	}

	@Override
	public JsonPaging findAllUserGroup(JsonPaging jsonPaging, SysUsergroup sysUsergroup) {
		String hql = " from SysUsergroup r where r.isValid='1'";
		List params = new ArrayList();
		
			if (null != sysUsergroup && null != sysUsergroup.getName() && !"".equals(sysUsergroup.getName())) {
				hql += " and r.name like '%" + sysUsergroup.getName() + "%' ";
			}

			//if (null != sysUsergroup.getType()) {
			//	String type = sysUsergroup.getType();
			//	hql += " and r.type='" + type + "'";
			//}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
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
			//String type = usergroup.getType();
			//String relateId = usergroup.getRelateId();
			//if (null != relateId && !"".equals(relateId)) {
				//if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(type)) {
					vo.setRelateCompany("");
				//} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(type)) {
					//MemberIfafirm ifafirm = ifafirmService.findById(relateId);
					//if (null != ifafirm) {
					//	vo.setRelateCompany(ifafirm.getCompanyName() != null ? ifafirm.getCompanyName() : "");
					//} else {
					//	vo.setRelateCompany("");
					//}
				//} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(type)) {
					//MemberDistributor distributor = distributorService.findDistributorById(relateId);
					//if (null != distributor) {
					//	vo.setRelateCompany(null != distributor.getCompanyName() ? distributor.getCompanyName() : "");
					//} else {
					//	vo.setRelateCompany("");
					//}
				//}
			//}

			list.add(vo);
		}
		jsonPaging.setList(list);
		// TODO Auto-generated method stub
		return jsonPaging;
	}

	@Override
	public SysUsergroup saveUsergroup(SysUsergroup sysUsergroup) {
		if (null == sysUsergroup.getId() || "".equals(sysUsergroup.getId())) {
			sysUsergroup = (SysUsergroup) this.baseDao.create(sysUsergroup);
		} else {
			sysUsergroup = (SysUsergroup) this.baseDao.update(sysUsergroup);
		}
		return sysUsergroup;
	}

	//@Transactional
	@Override
	public boolean saveUsergroupMember(List list) {
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
	public boolean deleteUserGroup(SysUsergroup sysUsergroup) {
		if (null != sysUsergroup) {
			sysUsergroup.setIsValid("0");
			this.baseDao.update(sysUsergroup);
			return true;
		}
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JsonPaging findUserInGroup(JsonPaging jsonPaging, SysUsergroup sysUsergroup) {
		String hql = " from MemberBase r inner join SysUsergroupMember s on r.id=s.member.id where s.group.id='" + sysUsergroup.getId() + "' and r.isValid='1'";
		List params = new ArrayList();
		if (null != sysUsergroup && null != sysUsergroup.getName() && !"".equals(sysUsergroup.getName())) {
			hql += " and (r.loginCode like '%" + sysUsergroup.getName() + "%' or r.nickName like '%" + sysUsergroup.getName() + "%')";
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);

		return jsonPaging;
	}

	@Override
	public JsonPaging findUserNotInGroup(JsonPaging jsonPaging, SysUsergroup sysUsergroup) {
		String hql = " from MemberBase r where r.id not in (select s.member.id from SysUsergroupMember s where s.group.id='" + sysUsergroup.getId() + "') and r.isValid='1'";
		List params = new ArrayList();
		if (null != sysUsergroup && null != sysUsergroup.getName() && !"".equals(sysUsergroup.getName())) {
			hql += " and (r.loginCode like '%" + sysUsergroup.getName() + "%' or r.nickName like '%" + sysUsergroup.getName() + "%')";
		}
/*		if (null != sysUsergroup && null != sysUsergroup.getType() && !"".equals(sysUsergroup.getType())) {
			String type = sysUsergroup.getType();
			if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(type)) {
				hql += "and r.id in (select m.member.id from  MemberAdmin m where m.ifafirm.id='" + sysUsergroup.getRelateId() + "')";
			}

			else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(type)) {
				hql += "and r.id in (select m.member.id from  MemberAdmin m where m.distributor.id='" + sysUsergroup.getRelateId() + "')";
			} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(type)) {
				hql += "and r.id in (select m.member.id from  MemberAdmin m where m.type='" + type + "')";
			}
		}*/
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);

		return jsonPaging;
	}

	@Override
	public boolean saveGroupMember(List list) {
		if (null == list || list.isEmpty()) {
			return false;
		}
		Iterator it = list.iterator();
		while (it.hasNext()) {
			SysUsergroupAdmin vo = (SysUsergroupAdmin) it.next();
			vo = (SysUsergroupAdmin) this.baseDao.create(vo);
			if (null == vo)
				break;
		}
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean deleteGroupMember(String groupId, String memberId) {
		try {
			String hql = " delete from SysUsergroup r where 1=1 ";
			List param = new ArrayList();
			hql += " and r.group.id=?";
			param.add(groupId);
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
	public boolean checkGroupName(String id, String name) {
		String hql = " from SysUsergroup r where 1=1";
		List params = new ArrayList();

		if (null != id && !"".equals(id)) {
			hql += " and r.id<>? ";
			params.add(id);
		}
		if (null != name && !"".equals(name)) {
			hql += " and name=?";
			params.add(name);
		}
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty())
			return false;
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public JsonPaging findAllUserGroupPro(JsonPaging jsonPaging, SysUsergroup sysUsergroup) {
		int pageIndex = (jsonPaging.getPage() - 1) * jsonPaging.getRows();
		int pageSize = jsonPaging.getRows();
		String keyWord = "";
		String id = "";

		if (null != sysUsergroup) {
			if (null != sysUsergroup.getCreateBy()) {
				id = sysUsergroup.getCreateBy().getId();
			}
			if (null != sysUsergroup.getName() && !"".equals(sysUsergroup.getName())) {
				keyWord = sysUsergroup.getName();
			}
		}
		String sql = "CALL  pro_getusergrouplist('" + id + "'," + pageIndex + "," + pageSize + ",'" + keyWord + "')";

		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql);
		Iterator it = (Iterator) resultList.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			Map type = (Map) it.next();
			SysUserGroupVO vo = new SysUserGroupVO();
			vo.setId(getMapObject(type, "id"));
			vo.setName(getMapObject(type, "name"));
			// vo.setCreateTime(sdf.parse(getMapObject(type,"create_time")));
			vo.setCreateBy(getMapObject(type, "create_by_name"));
			String typeString = getMapObject(type, "type");
			vo.setType(typeString);
			String relateId = getMapObject(type, "relate_id");
			vo.setRelateId(relateId);
			if (null != relateId && !"".equals(relateId)) {
				if (CommonConstantsWeb.MEMBERADMIN_TYPE_SYSTEM.equals(typeString)) {
					vo.setRelateCompany("");
				} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_IFA.equals(typeString)) {
					MemberIfafirm ifafirm = ifafirmService.findById(relateId);
					if (null != ifafirm) {
						vo.setRelateCompany(ifafirm.getCompanyName() != null ? ifafirm.getCompanyName() : "");
					} else {
						vo.setRelateCompany("");
					}
				} else if (CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR.equals(typeString)) {
					MemberDistributor distributor = distributorService.findDistributorById(relateId);
					if (null != distributor) {
						vo.setRelateCompany(null != distributor.getCompanyName() ? distributor.getCompanyName() : "");
					} else {
						vo.setRelateCompany("");
					}
				}
			}
			list.add(vo);

		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}

}
