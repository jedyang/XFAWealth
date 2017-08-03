package com.fsll.wmes.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.entity.MemberRole;
import com.fsll.wmes.entity.MemberRoleMenu;
import com.fsll.wmes.role.service.MemberRoleService;
import com.fsll.wmes.role.vo.MemberRoleVO;

@Service("memberRoleService")
//@Transactional
public class MemberRoleServiceImpl extends BaseService implements MemberRoleService {
	

	@Override
	public JsonPaging findRoleByPage(JsonPaging jsonPaging,MemberRoleVO role) {
		String hql = "select r,s.nickName from MemberRole r left join SysAdmin s on r.createBy=s.id  where 1=1 ";
		List<String> params = new ArrayList<String>();
		if (null != role && StringUtils.isNoneBlank(role.getKeyword()) ) {
				hql += " and r.name like ? or r.code like ? ";
				params.add("%"+role.getKeyword()+"%");
				params.add("%"+role.getKeyword()+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		Iterator iterator = jsonPaging.getList().iterator();
		List<MemberRoleVO> list = new ArrayList<MemberRoleVO>();
		while (iterator.hasNext()) {
			Object[] objs = (Object[]) iterator.next();
			MemberRole mrole = (MemberRole) objs[0];
			MemberRoleVO vo = new MemberRoleVO(mrole);
			vo.setCreateNickName(objs[1]==null?"":objs[1].toString());
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public MemberRole findById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		Object obj = this.baseDao.get(MemberRole.class, id);
		if(obj != null){
			MemberRole role = (MemberRole) obj;
			return role;
		}
		return null;
	}

	@Override
	public MemberRole saveOrUpdate(MemberRole role) {
		role = (MemberRole) this.baseDao.saveOrUpdate(role);
		return role;
	}

	@Override
	public boolean deleteRole(String roleId) {
		MemberRole role = findById(roleId);
		if(role!=null){
			//删除该角色菜单权限
			String hql = " delete MemberRoleMenu rm where rm.role='"+roleId+"' ";
			baseDao.updateHql(hql, null);
			baseDao.delete(role);
			return true;
		}
		return false;
	}

	//modify by rqwang 20170518
	@Override
	public List<MenuTreeVO> getMenuTree(String roleId, String langCode) {
		MemberRole memRole = (MemberRole)this.baseDao.get(MemberRole.class, roleId);
		String roleCode = null==memRole?"":memRole.getCode();
		//所有前台菜单
		StringBuffer hql =new StringBuffer(" from MemberMenu m where m.isValid='1' ");
		if("investor".equalsIgnoreCase(roleCode)){
			hql.append(" and m.code like 'qta_%' ");
		}else if("IFA".equalsIgnoreCase(roleCode)||"ifa_supervisor".equalsIgnoreCase(roleCode)){
			hql.append(" and m.code like 'qtb_%' ");
		}else if("IFA_FIRM".equalsIgnoreCase(roleCode)){
			//hql.append(" and m.code like 'csa_%' ");
		}else if("distributor".equalsIgnoreCase(roleCode)){
			//hql.append(" and m.code like 'csb_%' ");
		}else{//访客可查定义所有菜单
			
		}
		hql.append(" order by m.orderBy ");
		List<MemberMenu> allMenu = baseDao.find(hql.toString(),null, false);
		//角色菜单
		hql = new StringBuffer(" from MemberRoleMenu rm where rm.role.id=?");
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		List<MemberRoleMenu> roleMenu = baseDao.find(hql.toString(),params.toArray(), false);
		Map<String,String> roleMap =new HashMap<String, String>();
		if(roleMenu!=null && !roleMenu.isEmpty()){
			for (MemberRoleMenu rm : roleMenu) {
				if(null==rm||null==rm.getMenu())continue;
				roleMap.put(rm.getMenu().getId(), "0");
			}
		}
		//封装数据
		List<MenuTreeVO> listVo=new ArrayList<MenuTreeVO>();
		MenuTreeVO topMenu = new MenuTreeVO();
		MenuTreeVO qtaMenu = new MenuTreeVO();
		MenuTreeVO qtbMenu = new MenuTreeVO();
		//MenuTreeVO csaMenu = new MenuTreeVO();
		//MenuTreeVO csbMenu = new MenuTreeVO();
		
		topMenu.setId("addTop");
		qtaMenu.setId("addInvestor");
		qtaMenu.setpId("addTop");
		qtbMenu.setId("addIfa");
		qtbMenu.setpId("addTop");
		
		if(langCode.endsWith("sc")){
			topMenu.setName("前台菜单项");
			qtaMenu.setName("投资人菜单项");
			qtbMenu.setName("ifa菜单项");
		}else if(langCode.endsWith("tc")){
			topMenu.setName("前臺菜單項");
			qtaMenu.setName("投資人菜單項");
			qtbMenu.setName("ifa菜單項");
		}else{
			topMenu.setName("Front menu");
			qtaMenu.setName("Investor menu");
			qtbMenu.setName("ifa menu");
		}

		/*csaMenu.setName("ifaFirm菜单项");
		csaMenu.setId("addIfaFirm");
		csaMenu.setpId("addTop");
		csbMenu.setName("代理商菜单项");
		csbMenu.setId("addDistributor");
		csbMenu.setpId("addTop");*/
		
		listVo.add(topMenu);
		listVo.add(qtaMenu);
		listVo.add(qtbMenu);
		/*listVo.add(csaMenu);
		listVo.add(csbMenu);*/
		
		if(null!=allMenu && !allMenu.isEmpty()){
			Iterator<MemberMenu> it=allMenu.iterator();
			while (it.hasNext()) {
				MemberMenu menu = it.next();
				MenuTreeVO vo=new MenuTreeVO();
				vo.setId(menu.getId());
				vo.setName(menu.getMenuName(langCode));
				if(menu.getParent()!=null){
					vo.setpId(menu.getParent().getId());
				}else{
					if(menu.getCode().contains("qta_")){
						vo.setpId("addInvestor");
					}else if(menu.getCode().contains("qtb_")){
						vo.setpId("addIfa");
					}else if(menu.getCode().contains("csa_")){
						//vo.setpId("addIfaFirm");
					}else if(menu.getCode().contains("csb_")){
						//vo.setpId("addDistributor");
					}
				}
				if(roleMap.containsKey(vo.getId())){
					vo.setChecked(true);
				}
				
				listVo.add(vo);
			}
		}
		return listVo;
	}

	@Override
	public boolean saveRoleMenu(String[] menuIds, String roleId) {
		MemberRole role = findById(roleId);
		if(role == null){
			return false;
		}
		//删除角色关联的菜单
		String hql =" delete from MemberRoleMenu rm where rm.role.id=?";
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		baseDao.updateHql(hql, params.toArray());
		//保存新角色菜单
		for (String menuId : menuIds) {
			MemberRoleMenu roleMenu = new MemberRoleMenu();
			MemberMenu menu = new MemberMenu();
			menu.setId(menuId);
			roleMenu.setRole(role);
			roleMenu.setMenu(menu);
			baseDao.saveOrUpdate(roleMenu, true);
		}
		return true;
	}

	/**
	 * 保存用户角色菜单
	 * @author wwluo
	 * @date 2017-05-31
	 * @param memberMenuId 用户菜单ID
	 * @param oldRoleCode 角色编码 member_role.code
	 * @param roleCode 角色编码 member_role.code
	 */
	@Override
	public void saveMemberRoleMenu(String memberMenuId, String oldRoleCode, String roleCode) {
		String findMemberRoleHql =" FROM MemberRole r WHERE r.code=?";
		MemberRole role = null;
		List<MemberRole> roles = baseDao.find(findMemberRoleHql, new Object[]{oldRoleCode}, false);
		if(roles != null && !roles.isEmpty()){
			role = roles.get(0);
			String hql =" DELETE FROM MemberRoleMenu rm WHERE rm.menu.id=? AND rm.role.id=?";
			List<String> params = new ArrayList<String>();
			params.add(memberMenuId);
			params.add(role.getId());
			baseDao.updateHql(hql, params.toArray());
		}
		MemberMenu menu = new MemberMenu();
		menu.setId(memberMenuId);
		roles = baseDao.find(findMemberRoleHql, new Object[]{roleCode}, false);
		if(roles != null && !roles.isEmpty()){
			role = roles.get(0);
		}
		MemberRoleMenu roleMenu = new MemberRoleMenu();
		roleMenu.setRole(role);
		roleMenu.setMenu(menu);
		baseDao.saveOrUpdate(roleMenu, true);
	}

}
