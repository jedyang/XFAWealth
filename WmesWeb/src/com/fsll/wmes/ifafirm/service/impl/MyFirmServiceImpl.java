package com.fsll.wmes.ifafirm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.distributor.vo.MemberDistributorVO;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifa.vo.MemberIfaVO;
import com.fsll.wmes.ifafirm.service.MyFirmService;
import com.fsll.wmes.ifafirm.vo.IfafirmTeamIfaVO;
import com.fsll.wmes.ifafirm.vo.MemberIfaIfafirmVO;
import com.fsll.wmes.member.vo.MemberAdminVO;

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
			IfafirmTeam team = (IfafirmTeam) this.baseDao.get(IfafirmTeam.class, teamId);
			if(team != null){
				team.setIsValid("0");//逻辑删除
				this.baseDao.update(team);
				falg = true;
			}
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
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean saveLeader(String ifaId, String teamId,String isSupervisor) {
		boolean flag = false;
		if(StringUtils.isNotBlank(ifaId) && 
				StringUtils.isNotBlank(teamId) && 
					StringUtils.isNotBlank(isSupervisor)){
			StringBuffer hql = new StringBuffer("from IfafirmTeamIfa i where i.team.id=? and i.ifa.id=?");
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
	public boolean saveIfaAdmin(String ifaId, String isAdmin) {
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
	
	//-------- firmAdmin --------
	/**
	 * 获取ifaFirm管理员历表
	 * @author qgfeng
	 * @date 2016-12-2
	 * 
	 */
	@Override
	public JsonPaging findFirmMember(JsonPaging jsonPaging,MemberAdminVO adminVo) {
		if(adminVo==null){
			return jsonPaging;
		}
		String hql = " from MemberAdmin r where r.type='1' and r.ifafirm.id=? ";
		List<String> params = new ArrayList<String>();
		params.add(adminVo.getIfafirmId());
		if(StringUtils.isNotBlank(adminVo.getKeyword())){
			hql += " and (r.member.loginCode like ? or r.member.nickName like ?)";
			params.add("%"+adminVo.getKeyword()+"%");
			params.add("%"+adminVo.getKeyword()+"%");
		}
		if(StringUtils.isNotBlank(adminVo.getIsValid())){
			hql += " and r.member.isValid = ? ";
			params.add(adminVo.getIsValid());
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<MemberAdminVO> list = new ArrayList<MemberAdminVO>();
		if (null!=jsonPaging.getList() && !jsonPaging.getList().isEmpty()){
			Iterator<MemberAdmin> it = (Iterator) jsonPaging.getList().iterator();
			while (it.hasNext()) {
				MemberAdmin entity = (MemberAdmin) it.next();
				if(entity.getMember()!=null){
					MemberAdminVO vo = new MemberAdminVO();
					vo.setId(entity.getId());
					vo.setParentId(null!=entity.getParent()?entity.getParent().getId():"");
					vo.setParentName(null!=entity.getParent()?entity.getParent().getMember().getNickName():"");
					vo.setLoginCode(entity.getMember().getLoginCode());
					vo.setMemberId(entity.getMember().getId());
					vo.setType(entity.getType());
					vo.setIsValid(entity.getMember().getIsValid());
					vo.setNickName(entity.getMember().getNickName());
					vo.setCreateTime(DateUtil.getDateStr(entity.getMember().getCreateTime()));
					vo.setIsSystem("0");
					if(StringUtils.isNotBlank(entity.getMember().getIsSystem())){
						vo.setIsSystem(entity.getMember().getIsSystem());
					}
					list.add(vo);
				}
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	@Override
	public MemberBase getFirmMemberById(String id) {
		String hql = "select r.member from MemberAdmin r where r.id=?";
		List<String> params = new ArrayList<String>();
		params.add(id);
		MemberBase base = (MemberBase) baseDao.getUniqueResult(hql, params.toArray(), false);
		return base;
	}
	/**
	 * 根据memberBase ID 及firmId查找
	 * @author qgfeng
	 * @date 2016-12-28
	 * @param firmId 代理公司id
	 * @param memberId 人员id
	 */
	@Override
	public MemberAdmin getFirmMemberByMid(String firmId, String memberId) {
		if(StringUtils.isBlank(firmId) || StringUtils.isBlank(memberId)){
			return null;
		}
		String hql = " from MemberAdmin r where r.ifafirm.id=? and r.member.id=?";
		List<String> params = new ArrayList<String>();
		params.add(firmId);
		params.add(memberId);
		List<MemberAdmin> list = baseDao.find(hql, params.toArray(),false);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 保存firm Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	@Override
	public MemberAdmin saveOrUpdateFirmMember(MemberAdmin firmMember) {
		if(firmMember==null || firmMember.getMember()==null || firmMember.getIfafirm()==null){
			return null;
		}
		MemberBase member = (MemberBase) baseDao.saveOrUpdate(firmMember.getMember());
		if(member != null ){
			firmMember.setMember(member);
			firmMember = (MemberAdmin) baseDao.saveOrUpdate(firmMember);
			return firmMember;
		}
		return null;
	}

	@Override
	public boolean deleteFirmMemberByIds(String ids) {
		if(StringUtils.isBlank(ids)){
			return false;
		}
		String[] idArry = ids.split(",");
		if(idArry != null && idArry.length>0){
			for(String id : idArry){
				if(StringUtils.isBlank(id)) continue;
				MemberAdmin admin = (MemberAdmin) baseDao.get(MemberAdmin.class, id);
				if(admin!=null){
					if(admin.getMember() != null){
						baseDao.delete(admin.getMember());
					}
					baseDao.delete(admin);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 获取该公司下的组织架构
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param ifaFirmId 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<IfafirmTeam> getOrg(String ifaFirmId) {
		List<IfafirmTeam> teams = null;
		if (StringUtils.isNotBlank(ifaFirmId)) {
			StringBuffer hql = new StringBuffer(""
					+ " FROM "
					+ " IfafirmTeam i"
					+ " WHERE"
					+ " i.isValid=1"
					+ " AND"
					+ " i.ifafirm.id=?"
					/*+ " AND("
					+ " i.parent IS NULL"
					+ " OR"
					+ " i.parent='')"*/
					+ " ORDER BY"
					+ " i.orderBy"
					+ " ASC"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(ifaFirmId);
			teams = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(teams != null && !teams.isEmpty()){
				for (IfafirmTeam team : teams) {
					if(team.getParent() != null){
						team.setParentId(team.getParent().getId());
					}
				}
			}
		}
		return teams;
	}

	/**
	 * 获取该公司下团队成员
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param ifaFirmId
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getIfafirmTeamIfa(String ifaFirmId, String teamId, String keyWord, JsonPaging jsonPaging) {
		if (StringUtils.isNotBlank(ifaFirmId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " ti,i"
					+ " FROM"
					+ " IfafirmTeamIfa ti"
					+ " LEFT JOIN"
					+ " MemberIfa i"
					+ " ON"
					+ " i.id=ti.ifa.id"
					+ " LEFT JOIN"
					+ " MemberBase m"
					+ " ON"
					+ " m.id=i.member.id"
					+ " WHERE"
					+ " ti.ifafirm.id=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(ifaFirmId);
			if (StringUtils.isNotBlank(teamId)) {
				hql.append(" AND ti.team.id=?");
				params.add(teamId);
			}
			if (StringUtils.isNotBlank(keyWord)) {
				hql.append(" AND("
						+ "	TRIM(i.firstName)||' '||TRIM(i.lastName) LIKE ? OR"
						+ "	i.firstName LIKE ? OR"
						+ "	i.lastName LIKE ? OR"
						+ "	i.nameChn LIKE ? OR"
						+ "	m.nickName LIKE ? OR"
						+ "	m.loginCode LIKE ? OR"
						+ "	m.email LIKE ? OR"
						+ "	i.telephone LIKE ?"
						+ ")");
				params.add("%" + keyWord + "%");
				params.add("%" + keyWord + "%");
				params.add("%" + keyWord + "%");
				params.add("%" + keyWord + "%");
				params.add("%" + keyWord + "%");
				params.add("%" + keyWord + "%");
				params.add("%" + keyWord + "%");
				params.add("%" + keyWord + "%");
			}
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
			if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
				List<IfafirmTeamIfaVO> vos = new ArrayList<IfafirmTeamIfaVO>();;
				List<Object[]> objects = jsonPaging.getList();
				for (Object[] objs : objects) {
					IfafirmTeamIfa teamIfa = (IfafirmTeamIfa) objs[0];
					MemberIfa ifa = (MemberIfa) objs[1];
					IfafirmTeamIfaVO vo = new IfafirmTeamIfaVO();
					BeanUtils.copyProperties(teamIfa, vo);
					vo.setTeamId(teamIfa.getTeam().getId());
					vo.setMemberId(ifa.getMember().getId());
					vo.setIfaId(ifa.getId());
					vo.setIfaNameChn(ifa.getNameChn());
					if (StringUtils.isNotBlank(ifa.getFirstName()) && StringUtils.isNotBlank(ifa.getLastName())) {
						vo.setIfaName(ifa.getFirstName() + " " + ifa.getLastName());
					}else if(StringUtils.isNotBlank(ifa.getFirstName())){
						vo.setIfaName(ifa.getFirstName());
					}else if(StringUtils.isNotBlank(ifa.getLastName())){
						vo.setIfaName(ifa.getLastName());
					}
					vo.setMoblePhone(ifa.getTelephone());
					vo.setEmail(ifa.getMember().getEmail());
					vo.setLoginCode(ifa.getMember().getLoginCode());
					vos.add(vo);
				}
				jsonPaging.getList().clear();
				jsonPaging.getList().addAll(vos);
			}
		}
		return jsonPaging;
	}

	/**
	 * 团队移除ifa
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@Override
	public Boolean delIfa(String ifaId, String teamId) {
		Boolean flag = null;
		StringBuffer hql = new StringBuffer(" DELETE FROM IfafirmTeamIfa i WHERE i.team.id=? AND i.ifa.id=?");
		List<Object> params = new ArrayList<Object>();
		params.add(teamId);
		params.add(ifaId);
		Integer count = baseDao.updateHql(hql.toString(), params.toArray());
		if(count != null && count > 0){
			flag = true;	
		}
		return flag;
	}

	/**
	 * 获取待审批的IFA
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public JsonPaging getMemberIfaIfafirm(String ifaFirmId,
			MemberIfaIfafirmVO memberIfaIfafirmVO, String dateFomat, String langCode, JsonPaging jsonPaging) {
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " m"
				+ " FROM "
				+ " MemberIfaIfafirm m"
				+ " LEFT JOIN"
				+ " MemberIfa i"
				+ " ON"
				+ " m.ifa.id=i.id"
				+ " LEFT JOIN"
				+ " MemberBase b"
				+ " ON"
				+ " i.member.id=b.id"
				+ " WHERE"
				+ " m.isValid=1"
				+ " AND"
				+ " m.checkStatus<>1"
				+ " AND"
				+ " m.ifafirm.id=?"
				+ "");
		List<Object> params = new ArrayList<Object>();
		params.add(ifaFirmId);
		if (StringUtils.isNotBlank(memberIfaIfafirmVO.getFilterStatus())) {
			hql.append(" AND m.checkStatus=?");
			params.add(memberIfaIfafirmVO.getFilterStatus());
		}
		if (StringUtils.isNotBlank(memberIfaIfafirmVO.getFilterKeyWord())) {
			hql.append(" AND("
					+ "	TRIM(i.firstName)||' '||TRIM(i.lastName) LIKE ? OR"
					+ "	i.firstName LIKE ? OR"
					+ "	i.lastName LIKE ? OR"
					+ "	i.nameChn LIKE ? OR"
					+ "	b.nickName LIKE ? OR"
					+ "	b.loginCode LIKE ? OR"
					+ "	b.email LIKE ? OR"
					+ "	i.telephone LIKE ?"
					+ ")");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
			params.add("%" + memberIfaIfafirmVO.getFilterKeyWord() + "%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(jsonPaging.getList() != null && !jsonPaging.getList().isEmpty()){
			List<MemberIfaIfafirmVO> vos = new ArrayList<MemberIfaIfafirmVO>(); 
			List<MemberIfaIfafirm> ifafirms = jsonPaging.getList();
			for (MemberIfaIfafirm memberIfaIfafirm : ifafirms) {
				MemberIfaIfafirmVO vo = new MemberIfaIfafirmVO();
				MemberIfa ifa = memberIfaIfafirm.getIfa();
				BeanUtils.copyProperties(memberIfaIfafirm, vo);
				vo.setMemberId(ifa.getMember().getId());
				vo.setIfaId(ifa.getId());
				vo.setIfaNameChn(ifa.getNameChn());
				if (StringUtils.isNotBlank(ifa.getFirstName()) && StringUtils.isNotBlank(ifa.getLastName())) {
					vo.setIfaName(ifa.getFirstName() + " " + ifa.getLastName());
				}else if(StringUtils.isNotBlank(ifa.getFirstName())){
					vo.setIfaName(ifa.getFirstName());
				}else if(StringUtils.isNotBlank(ifa.getLastName())){
					vo.setIfaName(ifa.getLastName());
				}
				vo.setName(getCommonMemberName(ifa.getMember().getId(), langCode, "2"));
				vo.setMoblePhone(ifa.getTelephone());
				vo.setEmail(ifa.getMember().getEmail());
				vo.setLoginCode(ifa.getMember().getLoginCode());
				vo.setRegisterTimeStr(DateUtil.dateToDateString(ifa.getMember().getCreateTime(), dateFomat));
				vos.add(vo);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(vos);
		}
		return jsonPaging;
	}

	/**
	 * IFA审批操作
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param memberAdmin
	 * @param status
	 * @param opinion
	 * @return MemberIfaIfafirm
	 */
	@SuppressWarnings("unchecked")
	@Override
	public MemberIfaIfafirm saveMemberIfaIfafirm(MemberAdmin memberAdmin, String ifaId, String status,
			String opinion) {
		MemberIfaIfafirm ifaIfafirm = null;
		StringBuffer hql = new StringBuffer(""
				+ " FROM"
				+ " MemberIfaIfafirm mi"
				+ " WHERE"
				+ " mi.ifafirm.id=?"
				+ " AND"
				+ " mi.ifa.id=?"
				+ "");
		List<Object> params = new ArrayList<Object>();
		params.add(memberAdmin.getIfafirm().getId());
		params.add(ifaId);
		List<MemberIfaIfafirm> ifaIfafirms = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(ifaIfafirms != null && !ifaIfafirms.isEmpty()){
			ifaIfafirm = ifaIfafirms.get(0);
			ifaIfafirm.setCheckStatus(status);
			ifaIfafirm.setCheckDate(new Date());
			ifaIfafirm.setCheckUser(memberAdmin.getMember());
			ifaIfafirm.setCheckOpinion(opinion);
			ifaIfafirm = (MemberIfaIfafirm) baseDao.saveOrUpdate(ifaIfafirm);
		}
		return ifaIfafirm;
	}

	/**
	 * IFA团队更换
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param ifaId
	 * @param oldTeamId
	 * @param newTeamId
	 */
	@Override
	public Integer updateIfaTeam(String ifaFirmId, String ifaId, String oldTeamId,
			String newTeamId) {
		Integer count = null;
		if (StringUtils.isNotBlank(ifaFirmId) 
				&& StringUtils.isNotBlank(ifaId) 
				&& StringUtils.isNotBlank(oldTeamId) 
				&& StringUtils.isNotBlank(newTeamId)) {
			StringBuffer hql = new StringBuffer(""
					+ " UPDATE"
					+ " IfafirmTeamIfa i"
					+ " SET"
					+ " i.team.id=?"
					+ " WHERE"
					+ " i.team.id=?"
					+ " AND"
					+ " i.ifa.id=?"
					+ " AND"
					+ " i.ifafirm.id=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(newTeamId);
			params.add(oldTeamId);
			params.add(ifaId);
			params.add(ifaFirmId);
			count = this.baseDao.updateHql(hql.toString(), params.toArray());
		}
		return count;
	}
}






