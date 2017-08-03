package com.fsll.wmes.ifafirm.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.core.entity.SysMenu;
import com.fsll.wmes.distributor.vo.MemberDistributorVO;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifa.vo.MemberIfaVO;
import com.fsll.wmes.ifafirm.service.MyFirmService;

/***
 * 业务接口实现类：我的公司管理接口实现类
 * 
 * @author wwluo
 * @version 1.0
 * @date 2016-08-30
 */
@Service("myFirmService")
//@Transactional
public class MyFirmServiceImpl extends BaseService implements MyFirmService{

	/***
     * 组装组织架构菜单
     * @author wwluo
     * @date 2016-08-30
     */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List assemblyOrgMenu(String ifaFirmId) {
		List<IfafirmTeam> list = new ArrayList<IfafirmTeam>();
		StringBuffer hql = new StringBuffer(" from IfafirmTeam where isValid=1 and parent is null");
		hql.append(" and ifafirm_id=?");
		List params = new ArrayList();		
		params.add(ifaFirmId);
		hql.append(" order by order_by");
		list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list;
		} else {
			return null;
		}		
	}

	/**
	 * 获取组织架构成员
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public JsonPaging getTeamMember(JsonPaging jsonPaging, String teamId) {
		if(StringUtils.isNotBlank(teamId)){
			StringBuffer hql = new StringBuffer("from IfafirmTeamIfa where team_id=?");
			List params = new ArrayList();
			params.add(teamId);
			jsonPaging = 
				this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
			if(null != jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
				List list = new ArrayList();
				for (Object object : jsonPaging.getList()) {
					IfafirmTeamIfa teamIfa = (IfafirmTeamIfa) object;
					MemberIfa ifa = teamIfa.getIfa();
					MemberIfaVO vo = new MemberIfaVO();
					BeanUtils.copyProperties(ifa, vo);
					if(null != ifa.getMember()){
						vo.setEmail(ifa.getMember().getEmail());
					}
					if(null != ifa.getMember()){
						vo.setMobileNumber(ifa.getMember().getMobileNumber());
					}
					vo.setIsSupervisor(teamIfa.getIsSupervisor());
					list.add(vo);
				}
				jsonPaging.getList().clear();
				jsonPaging.getList().addAll(list);
			}
		}
		return jsonPaging;
	}
	
	/**
	 * 增加、修改组织机构
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@Override
	public IfafirmTeam saveAndUpdateOrg(IfafirmTeam team, String ifaFirmId,
			String parentid) {
		if(StringUtils.isNotBlank(ifaFirmId)){
			MemberIfafirm memberIfafirm =  (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, ifaFirmId);
			team.setIfafirm(memberIfafirm);
		}
		if(StringUtils.isNotBlank(parentid)){
			IfafirmTeam ifafirmTeam = (IfafirmTeam) this.baseDao.get(IfafirmTeam.class, parentid);
			team.setParent(ifafirmTeam);
			if(null != ifafirmTeam){
				team.setClassLayer(ifafirmTeam.getClassLayer()+1);
			}
		}else{
			Integer classLayer = team.getClassLayer();
			if(null == classLayer){
				team.setClassLayer(1);
			}
		}
		team = (IfafirmTeam) this.baseDao.saveOrUpdate(team);
		return team;
	}

	/**
	 * 删除组织机构
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@Override
	public boolean deleteOrg(String teamId) {
		boolean falg = false;
		if(StringUtils.isNotBlank(teamId)){
			IfafirmTeam team =  (IfafirmTeam) this.baseDao.get(IfafirmTeam.class, teamId);
			team.setIsValid("0");//逻辑删除
			this.baseDao.update(team);
			falg = true;
		}
		return falg;
	}

	/**
	 * 根据id获取ifafirm实体
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@Override
	public MemberIfafirm getIfafirmById(String ifaFirmId) {
		if(StringUtils.isNotBlank(ifaFirmId)){
			MemberIfafirm memberIfafirm = (MemberIfafirm) this.baseDao.get(MemberIfafirm.class, ifaFirmId);
			return memberIfafirm;
		}else
			return null;
	}
	
	/**
	 * ifafirm删除ifa
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@Override
	public boolean deleteIfa(String ifaId) {
		boolean flag = false;
		if(StringUtils.isNotBlank(ifaId)){
			MemberIfa memberIfa = (MemberIfa) this.baseDao.get(MemberIfa.class, ifaId);
			memberIfa.setCompanyType(null);
			memberIfa.setIfafirm(null);
			memberIfa.setIfafirmStatus("0");
			this.baseDao.update(memberIfa);
			flag = true;
		}else
			return flag;
		return flag;
	}

	/**
	 * 设置管理员
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@SuppressWarnings({ "unchecked", "unused", "rawtypes" })
	@Override
	public boolean setLeader(String ifaId, String teamId,String isSupervisor) {
		boolean flag = false;
		if(StringUtils.isNotBlank(ifaId) && 
				StringUtils.isNotBlank(teamId) && 
					StringUtils.isNotBlank(isSupervisor)){
			StringBuffer hql = new StringBuffer("from IfafirmTeamIfa where team_id=? and ifa_id=?");
			List params = new ArrayList();
			params.add(teamId);
			params.add(ifaId);
			List<IfafirmTeamIfa> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				IfafirmTeamIfa ifafirmTeamIfa = list.get(0);
				ifafirmTeamIfa.setIsSupervisor(isSupervisor);//1是，0否
				this.baseDao.update(ifafirmTeamIfa);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * ifa管理员设置（设置&移除）
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@Override
	public boolean setIfaAdmin(String ifaId, String isAdmin) {
		boolean flag = false;
		if(StringUtils.isNotBlank(ifaId) && 
				StringUtils.isNotBlank(isAdmin)){
			MemberIfa memberIfa = (MemberIfa) this.baseDao.get(MemberIfa.class, ifaId);
			memberIfa.setIsAdmin(isAdmin);
			this.baseDao.update(memberIfa);
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取该公司所有ifa
	 * @author wwluo
	 * @date 2016-09-02 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public JsonPaging findIfaByIfaFirm(JsonPaging jsonPaging, String ifafirmId,
			String langCode,String isAdminAccount) {
		if(StringUtils.isNotBlank(ifafirmId)){
			StringBuffer hql = new StringBuffer(
					" from MemberIfa m" +
					" where" +
					" m.member.isValid=1 and m.ifafirmStatus=1 and company_ifafirm_id=?");
			List params = new ArrayList();
			params.add(ifafirmId);
			if(StringUtils.isNotBlank(isAdminAccount)){
				hql.append(" and m.isAdmin=?");
				params.add(isAdminAccount);
			}
			jsonPaging = 
				this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
			if(null != jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
				List list = new ArrayList();
				for (Object object : jsonPaging.getList()) {
					MemberIfa memberIfa = (MemberIfa) object;
					MemberIfaVO vo = new MemberIfaVO(); 
					BeanUtils.copyProperties(memberIfa, vo);
					vo.setEmail(memberIfa.getMember().getEmail());
					list.add(vo);
				}
				jsonPaging.getList().clear();
				jsonPaging.getList().addAll(list);
			}
		}
		return jsonPaging;
	}

	/**
	 * 获取所有代理商    distributor
	 * @author wwluo
	 * @date 2016-09-05 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes"})
	@Override
	public JsonPaging getDistributorJson(JsonPaging jsonPaging,String langCode,
			String companyName, String entityType) {
		StringBuffer hql = new StringBuffer(
				" from MemberDistributor");
		List params = new ArrayList();
		if(StringUtils.isNotBlank(companyName)){
			hql.append(" where companyName=?");
			params.add(companyName);
		}
		if(StringUtils.isNotBlank(entityType)){
			if(hql.toString().toLowerCase().indexOf("where") > -1){
				hql.append(" and entityType=?");
			}else{
				hql.append(" where entityType=?");
			}
			params.add(entityType);
		}
		jsonPaging = 
			this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<MemberDistributorVO> list = new ArrayList<MemberDistributorVO>();
		for (Object object : jsonPaging.getList()) {
			MemberDistributorVO vo = new MemberDistributorVO();
			MemberDistributor memberDistributor = (MemberDistributor) object;
			BeanUtils.copyProperties(memberDistributor, vo);
			String countryId = memberDistributor.getCountry();
			SysCountry sysCountry = (SysCountry) this.baseDao.get(SysCountry.class, countryId);
			if("en".equals(langCode)){
				vo.setCountryName(sysCountry.getNameEn());
			}else if("tc".equals(langCode)){
				vo.setCountryName(sysCountry.getNameTc());
			}else{
				vo.setCountryName(sysCountry.getNameSc());
			}
			list.add(vo);
		}
		jsonPaging.getList().clear();
		jsonPaging.getList().addAll(list);
		return jsonPaging;
	}
}



























