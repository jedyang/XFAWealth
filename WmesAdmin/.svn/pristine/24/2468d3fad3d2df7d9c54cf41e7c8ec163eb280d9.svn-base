package com.fsll.wmes.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberConsoleRole;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
import com.fsll.wmes.entity.MemberConsoleRoleMenu;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.member.vo.MemberBaseVO;
import com.fsll.wmes.role.service.ConsoleRoleService;
@Service("consoleRoleService")
//@Transactional
public class ConsoleRoleServiceImpl extends BaseService implements ConsoleRoleService {

	/**
	 * 分页获取工作台角色列表
	 * @author qgfeng
	 * @date 2016-11-21
	 */
	@Override
	public JsonPaging findRoleByPage(JsonPaging jsonPaging, MemberConsoleRole role) {
		String hql = " from MemberConsoleRole r where 1=1 ";
		List<String> params = new ArrayList<String>();
		if (null != role ) {
			if(StringUtils.isNoneBlank(role.getIsValid())){
				hql += " and r.isValid  = ?";
				params.add(role.getIsValid());
			}
			if(StringUtils.isNoneBlank(role.getName())){
				hql += " and r.name like ? ";
				params.add("%"+role.getName()+"%");
			}
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}

	@Override
	public MemberConsoleRole findById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		Object obj = this.baseDao.get(MemberConsoleRole.class, id);
		if(obj==null)return null;
		return (MemberConsoleRole)obj;
	}

	//modify by rqwang 20170518
	@Override
	public List<MenuTreeVO> getMenuTree(String roleId,String langCode) {
		MemberConsoleRole memRole = (MemberConsoleRole)this.baseDao.get(MemberConsoleRole.class, roleId);
		String roleType = memRole.getType();//1--firm,2--distributor
		//角色相关菜单
		StringBuffer hql =new StringBuffer(" from MemberMenu m where m.isValid='1' ");
		if("1".equalsIgnoreCase(roleType)){
			hql.append(" and m.code like 'csa_%' ");
		}else if("2".equalsIgnoreCase(roleType)){
			hql.append(" and m.code like 'csb_%' ");
		}else{//访客可查定义所有菜单
			
		}
		hql.append("order by m.orderBy,m.id ");
		List<MemberMenu> allMenu = baseDao.find(hql.toString(),null, false);
		//角色菜单
		hql = new StringBuffer(" from MemberConsoleRoleMenu rm where rm.role.id=? ");
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		List<MemberConsoleRoleMenu> roleMenu = baseDao.find(hql.toString(),params.toArray(), false);
		Map<String,String> roleMap =new HashMap<String, String>();
		if(roleMenu!=null && !roleMenu.isEmpty()){
			for (MemberConsoleRoleMenu rm : roleMenu) {
				roleMap.put(rm.getMenu().getId(), "0");
			}
		}
		//封装数据
		List<MenuTreeVO> listVo=new ArrayList<MenuTreeVO>();
		MenuTreeVO topMenu = new MenuTreeVO();
		//MenuTreeVO qtaMenu = new MenuTreeVO();
		//MenuTreeVO qtbMenu = new MenuTreeVO();
		MenuTreeVO csaMenu = new MenuTreeVO();
		MenuTreeVO csbMenu = new MenuTreeVO();
		
		if(langCode.endsWith("sc")){
			topMenu.setName("工作台菜单项");
			csaMenu.setName("ifaFirm菜单项");
			csbMenu.setName("代理商菜单项");
		}else if(langCode.endsWith("tc")){
			topMenu.setName("工作臺菜單項");
			csaMenu.setName("ifaFirm菜單項");
			csbMenu.setName("代理商菜單項");
		}else{
			topMenu.setName("Workbench menu");
			csaMenu.setName("IfaFirm menu");
			csbMenu.setName("Distirbutor menu");
		}
		
		
		topMenu.setId("addTop");
		csaMenu.setId("addIfaFirm");
		csaMenu.setpId("addTop");
		csbMenu.setId("addDistributor");
		csbMenu.setpId("addTop");
		
		/*qtaMenu.setName("投资人菜单项");
		qtaMenu.setId("addInvestor");
		qtaMenu.setpId("addTop");
		qtbMenu.setName("ifa菜单项");
		qtbMenu.setId("addIfa");
		qtbMenu.setpId("addTop");*/
		/*listVo.add(qtaMenu);
		listVo.add(qtbMenu);*/
		
		listVo.add(topMenu);
		listVo.add(csaMenu);
		listVo.add(csbMenu);
		
		if(null!=allMenu && !allMenu.isEmpty()){
			Iterator<MemberMenu> it= allMenu.iterator();
			while (it.hasNext()) {
				MemberMenu menu = it.next();
				MenuTreeVO vo=new MenuTreeVO();
				vo.setId(menu.getId());
				vo.setName(menu.getMenuName(langCode));
				if(menu.getParent()!=null){
					vo.setpId(menu.getParent().getId());
				}else{
					if(menu.getCode().contains("qta_")){
						//vo.setpId("addInvestor");
					}else if(menu.getCode().contains("qtb_")){
						//vo.setpId("addIfa");
					}else if(menu.getCode().contains("csa_")){
						vo.setpId("addIfaFirm");
					}else if(menu.getCode().contains("csb_")){
						vo.setpId("addDistributor");
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
	public boolean saveRoleMenu(String[] menuIds,String roleId) {
		MemberConsoleRole role = findById(roleId);
		if(role == null){
			return false;
		}
		//删除角色关联的菜单
		String hql =" delete from MemberConsoleRoleMenu rm where rm.role.id=?";
		List<String> params = new ArrayList<String>();
		params.add(roleId);
		baseDao.updateHql(hql, params.toArray());
		//保存新角色菜单
		for (String menuId : menuIds) {
			if(StringUtils.isNotBlank(menuId)){
				MemberConsoleRoleMenu roleMenu = new MemberConsoleRoleMenu();
				MemberMenu menu = new MemberMenu();
				menu.setId(menuId);
				roleMenu.setRole(role);
				roleMenu.setMenu(menu);
				baseDao.saveOrUpdate(roleMenu, true);
			}
		}
		return true;
	}

	@Override
	public JsonPaging findMemberInRole(JsonPaging jsonPaging,MemberAdminVO memberAdmin) {
		String hql = " from MemberConsoleRoleMember rm where rm.role.id=? and rm.member.isValid='1' ";
		List<String> params = new ArrayList<String>();
		params.add(memberAdmin.getConsoleRoleId());
		if (StringUtils.isNoneBlank(memberAdmin.getKeyword()) ) {
			hql += " and (rm.member.loginCode like ? or rm.member.nickName like ? )";
			params.add("%"+memberAdmin.getKeyword()+"%");
			params.add("%"+memberAdmin.getKeyword()+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<MemberConsoleRoleMember> list = jsonPaging.getList();
		List<MemberBaseVO> listVo = new ArrayList<MemberBaseVO>();
		if(list!=null && !list.isEmpty()){
			for (MemberConsoleRoleMember rm : list) {
				MemberBaseVO vo = new MemberBaseVO(rm.getMember());
				vo.setConsoleRoleMemberId(rm.getId());
				listVo.add(vo);
			}
		}
		jsonPaging.setList(listVo);
		return jsonPaging;
	}

	/**
	 * 删除角色
	 * @author Yan
	 * @date 2017-02-14
	 */
	@Override
	public void deleteConsoleRole(MemberConsoleRole info) {
		this.baseDao.delete(info);
		
	}
	
	@Override
	public void deleteRoleMember(MemberConsoleRoleMember roleMember) {
		baseDao.delete(roleMember);
	}

	@Override
	public JsonPaging findMemberAdminNotInRole(JsonPaging jsonPaging,MemberAdminVO memberAdmin) {
		String hql = " select m.member from MemberAdmin m where m.member.isValid='1' ";
		hql +=" and  m.member.id not in (select r.member.id from MemberConsoleRoleMember r where r.role.id=?) ";
		List<Object> params = new ArrayList<Object>();
		params.add(memberAdmin.getConsoleRoleId());
		if (StringUtils.isNoneBlank(memberAdmin.getKeyword())) {
			hql += " and (m.member.loginCode like ? or m.member.nickName like ?) ";
			params.add("%"+memberAdmin.getKeyword()+"%");
			params.add("%"+memberAdmin.getKeyword()+"%");
		}
		if (StringUtils.isNoneBlank(memberAdmin.getType())) {
			hql += " and m.type = ? ";
			params.add(memberAdmin.getType());
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}

	/**
	 * 添加角色
	 * @author Yan
	 * @date 2017-02-14
	 */
	@Override
	public MemberConsoleRole saveConsoleRole(MemberConsoleRole info) {
		info = (MemberConsoleRole)this.baseDao.saveOrUpdate(info);
		return info;
	}
	
	@Override
	public boolean saveRoleMember(String roldId, String[] memberIds) {
		MemberConsoleRole role = findById(roldId);
		if(role==null){
			return false;
		}
		if(memberIds!=null){
			for (int i = 0; i < memberIds.length; i++) {
				String memberId = memberIds[i];
				if(StringUtils.isNoneBlank(memberId)){
					MemberBase base = new MemberBase();
					base.setId(memberId);
					MemberConsoleRoleMember rm = new MemberConsoleRoleMember();
					rm.setRole(role);
					rm.setMember(base);
					baseDao.saveOrUpdate(rm, true);
				}
			}
		}
		return true;
	}
}
