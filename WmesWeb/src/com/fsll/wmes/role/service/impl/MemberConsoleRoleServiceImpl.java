package com.fsll.wmes.role.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.vo.MenuTreeVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberConsoleRole;
import com.fsll.wmes.entity.MemberConsoleRoleMember;
import com.fsll.wmes.entity.MemberConsoleRoleMenu;
import com.fsll.wmes.entity.MemberMenu;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.MemberAdminVO;
import com.fsll.wmes.role.service.MemberConsoleRoleService;

/***
 * 业务接口实现类：控制台角色
 * @author mqzou
 * @date 2016-11-11
 */
@Service("memberConsoleRoleService")
//@Transactional
public class MemberConsoleRoleServiceImpl extends BaseService implements MemberConsoleRoleService {
	
	@Autowired
	private MemberBaseService baseService;
	
	/**
	 *  分页获取控制台角色列表
	 * @author qgfeng
	 * @date 2016-12-8
	 * @param role 条件查询
	 * @param MemberAdmin 当前登入人权限
	 */
	@Override
	public JsonPaging findRoleByPage(JsonPaging jsonPaging, MemberConsoleRole role,MemberAdmin admin) {
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
		//投资公司ifaFirm
		if(admin.getType().equals(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA) && admin.getIfafirm()!=null){
			hql += " and r.type = '1' and r.relateId = ?";
			params.add(admin.getIfafirm().getId());
			jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		}else if(admin.getType().equals(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR) && admin.getDistributor()!=null){
			//投资公司ifaFirm
			hql += " and r.type = '2' and r.relateId = ?";
			params.add(admin.getDistributor().getId());
			jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		}
		return jsonPaging;
	}
	/**
	 * 根据id查找MemberConsoleRole
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	@Override
	public MemberConsoleRole findRoleById(String id) {
		if(StringUtils.isBlank(id)){
			return null;
		}
		MemberConsoleRole role = (MemberConsoleRole) this.baseDao.get(MemberConsoleRole.class, id);
		return role;
	}
	/**
	 * 保存更新 MemberConsoleRole
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	@Override
	public MemberConsoleRole saveOrUpdateRole(MemberConsoleRole role) {
		role = (MemberConsoleRole) this.baseDao.saveOrUpdate(role);
		return role;
	}
	/**
	 * 删除角色(物理删除) 级联删除角色下的菜单权限
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	@Override
	public boolean deleteRole(String roleId) {
		MemberConsoleRole role = findRoleById(roleId);
		if(role!=null){
			//删除该角色菜单权限
			String hql = " delete MemberConsoleRoleMenu rm where rm.role='"+roleId+"' ";
			baseDao.updateHql(hql, null);
			baseDao.delete(role);
			return true;
		}
		return false;
	}
	/**
	 * 根据角色查找菜单
	 * @author qgfeng
	 * @date 2016-12-8
	 */
	@Override
	public List<MenuTreeVO> getMenuTree(String roleId,MemberAdmin admin,String langCode) {

		String roleCode = "";
		StringBuffer menuHql = new StringBuffer(""
				+ " SELECT"
				+ " m"
				+ " FROM"
				+ " MemberMenu m"
				+ " LEFT JOIN"
				+ " MemberRoleMenu r"
				+ " ON"
				+ " m.id=r.menu.id"
				+ " LEFT JOIN"
				+ " MemberRole a"
				+ " ON"
				+ " a.id=r.role.id"
				+ " WHERE"
				+ " a.code=? AND m.isValid=? and m.code is null"
				+ " order by m.orderBy "
				+ ""); 
		if(admin.getType()!=null){
//			投资公司ifaFirm菜单
			if(admin.getType().equals(CommonConstantsWeb.MEMBERADMIN_TYPE_IFA)){
				roleCode="IFA_FIRM";
			}
//			代理商菜单
			if(admin.getType().equals(CommonConstantsWeb.MEMBERADMIN_TYPE_DISTRIBUTOR)){
				roleCode="distributor";
			}
		}
		List<MemberMenu> allMenu = baseDao.find(menuHql.toString(),new String[]{roleCode,"1"}, false);
		//已选菜单
		String hql = " from MemberConsoleRoleMenu rm where rm.role.id='"+roleId+"' ";
		List<MemberConsoleRoleMenu> roleMenu = baseDao.find(hql,null, false);
		Map<String,String> roleMap =new HashMap<String, String>();
		if(roleMenu!=null && !roleMenu.isEmpty()){
			for (MemberConsoleRoleMenu rm : roleMenu) {
				roleMap.put(rm.getMenu().getId(), "0");
			}
		}
		//封装数据
		List<MenuTreeVO> listVo=new ArrayList<MenuTreeVO>();
		if(null!=allMenu && !allMenu.isEmpty()){
			Iterator<MemberMenu> it= allMenu.iterator();
			while (it.hasNext()) {
				MemberMenu menu = it.next();
				MenuTreeVO vo=new MenuTreeVO();
				vo.setId(menu.getId());
				vo.setName(getMenuName(menu, langCode));
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
	 * @date 2016-11-22
	 * @param MenuIds 菜单id集合
	 * @param roleId 角色id
	 */
	@Override
	public boolean saveRoleMenu(String[] menuIds,String roleId) {
		MemberConsoleRole role = findRoleById(roleId);
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

	/**
	 *  获取角色下的所有MemberBase列表（分页）
	 * @author qgfeng
	 * @date 2016-12-8
	 * @param consoleRoleId 角色id
	 * @param keyWord 关键字（账号、昵称）
	 */
	@Override
	public JsonPaging findMemberInRole(JsonPaging jsonPaging,MemberAdminVO memberAdmin) {
		List<String> params = new ArrayList<String>();
		String hql = " select rm.member from MemberConsoleRoleMember rm where rm.role.id=? and rm.member.isValid='1' ";
		params.add(memberAdmin.getConsoleRoleId());
		if (StringUtils.isNoneBlank(memberAdmin.getKeyword()) ) {
			hql += " and (rm.member.loginCode like ? or rm.member.nickName like ? )";
			params.add("%"+memberAdmin.getKeyword()+"%");
			params.add("%"+memberAdmin.getKeyword()+"%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	/**
	 * 删除角色成员
	 * @author qgfeng
	 * @date 2016-11-22
	 */
	@Override
	public void deleteRoleMember(MemberConsoleRoleMember roleMember) {
		baseDao.delete(roleMember);
	}
	/**
	 * 获取不在某个角色下、并所属某个ifafrim或代理商的所有成员列表
	 * @author qgfeng
	 * @date 2016-11-23
	 * @param consoleRoleId 角色id
	 * @param type:1 是firm 2是代理商
	 * @param keyWord 关键字（账号、昵称）
	 */
	@Override
	public JsonPaging findMemberAdminNotInRole(JsonPaging jsonPaging,MemberAdminVO memberAdmin){
		//没有权限
		if(memberAdmin==null || StringUtils.isBlank(memberAdmin.getType())){
			//jsonPaging.setList(null);
			return jsonPaging;
		}
		List<Object> params = new ArrayList<Object>();
		String hql = " select m.member from MemberAdmin m where m.member.isValid='1' ";
		hql +=" and  m.member.id not in (select r.member.id from MemberConsoleRoleMember r where r.role.id=?) ";
		params.add(memberAdmin.getConsoleRoleId());
		//ifaFrim or distutor
		if("1".equals(memberAdmin.getType())){
			hql +=" and m.type='1' and m.ifafirm.id =? ";
			params.add(memberAdmin.getIfafirmId());
		}else if("2".equals(memberAdmin.getType())){
			hql +=" and m.type='1' and  m.distributor.id=? ";
			params.add(memberAdmin.getDistributorId());
		}
		if (StringUtils.isNoneBlank(memberAdmin.getKeyword())) {
			hql += " and (m.member.loginCode like ? or m.member.nickName like ?) ";
			params.add("%"+memberAdmin.getKeyword()+"%");
			params.add("%"+memberAdmin.getKeyword()+"%");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	@Override
	public boolean saveRoleMember(String roldId, String[] memberIds) {
		MemberConsoleRole role = findRoleById(roldId);
		if(role==null){
			return false;
		}
		if(memberIds != null && memberIds.length >0 ){
			for (int i = 0; i < memberIds.length; i++) {
				String memberId = memberIds[i];
				MemberBase member = baseService.findById(memberId);
				if(member != null ){
					MemberConsoleRoleMember rm = new MemberConsoleRoleMember();
					rm.setRole(role);
					rm.setMember(member);
					baseDao.saveOrUpdate(rm, true);
				}
			}
		}
		return true;
	}

	/**
	 * 获取角色中的所有成员
	 * @author mqzou
     * @date 2016-11-11
	 * @param roleId
	 * @return
	 */
	@Override
	public List<MemberConsoleRoleMember> findConsoleRoleMembers(String roleId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from MemberConsoleRoleMember r where r.role.id=?");
		List params=new ArrayList();
		params.add(roleId);
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<MemberConsoleRoleMember> list=new ArrayList<MemberConsoleRoleMember>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				MemberConsoleRoleMember object = (MemberConsoleRoleMember) it.next();
				list.add(object);
			}
		}
		return list;
	}

	/**
	   * 获取成员角色关系
	   * @author mqzou
	     * @date 2016-11-17 
	   * @param userId
	   * @param roleId
	   * @return
	   */
	@Override
	public MemberConsoleRoleMember findConsoleRoleMemberById(String userId, String roleId) {
		MemberConsoleRoleMember vo=new MemberConsoleRoleMember();
		String hql=" from MemberConsoleRoleMember r where r.role.id=? and r.member.id=?";
		List<String> params=new ArrayList<String>();
		params.add(roleId);
		params.add(userId);
		List<MemberConsoleRoleMember> resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=resultList){
			vo=(MemberConsoleRoleMember)resultList.get(0);
		}
		return vo;
	}
	
	/**
	 * 用于根据语言编码返回菜单名称
	 * @param lang
	 * @return
	 */
	public String getMenuName(MemberMenu menu,String lang){
		if (null!=lang){
			if (CommonConstants.LANG_CODE_EN.equalsIgnoreCase(lang))
				return menu.getNameEn();
			else if (CommonConstants.LANG_CODE_TC.equalsIgnoreCase(lang))
				return menu.getNameTc();
			else {
				return menu.getNameSc();
			}
		}
		return "";
	}

}
