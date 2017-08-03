package com.fsll.wmes.ifafirm.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfaIfafirm;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.ifafirm.vo.MemberIfaIfafirmVO;
import com.fsll.wmes.member.vo.MemberAdminVO;

/***
 * 业务接口类：我的公司管理接口
 * @author wwluo
 * @date 2016-08-30
 */
public interface MyFirmService {

	/***
     * 组装组织架构菜单
     * @author wwluo
     * @date 2016-08-30
     */
	public List assemblyOrgMenu(String ifaFirmId);

	/**
	 * 获取组织架构成员
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public JsonPaging getTeamMember(JsonPaging jsonPaging, String teamId);

	/**
	 * 增加、修改组织机构
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public IfafirmTeam saveAndUpdateOrg(IfafirmTeam team, String ifaFirmId,
			String parentid);

	/**
	 * 删除组织机构
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public boolean deleteOrg(String teamId);

	/**
	 * 根据id获取firm实体
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public MemberIfafirm getIfafirmById(String ifaFirmId);

	/**
	 * ifafirm删除ifa
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public boolean deleteIfa(String ifaId);

	/**
	 * 设置团队负责人
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public boolean saveLeader(String ifaId, String teamId,String isSupervisor);

	/**
	 * ifa管理员设置（设置&移除）
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public boolean saveIfaAdmin(String ifaId, String isAdmin);
	
	/**
	 * 获取该公司所有ifa
	 * @author wwluo
	 * @date 2016-09-02 
	 */
	public JsonPaging findIfaByIfaFirm(JsonPaging jsonPaging, String ifafirmId,
			String langCode,String isAdminAccount);

	/**
	 * 获取代理商    distributor
	 * @author wwluo
	 * @date 2016-09-05 
	 */
	public JsonPaging getDistributorJson(JsonPaging jsonPaging,String langCode,
			String companyName, String entityType);
	
	//---------------
	/**
	 * 获取ifaFirm管理员历表
	 * @author qgfeng
	 * @date 2016-12-2
	 * 
	 */
	public JsonPaging findFirmMember(JsonPaging jsonPaging,MemberAdminVO adminVo);
	/**
	 * 查找 FirmMember
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	public MemberBase getFirmMemberById(String id);
	/**
	 * 根据memberBase ID 及firmId查找
	 * @author qgfeng
	 * @date 2016-12-28
	 * @param firmId 代理公司id
	 * @param memberId 人员id
	 */
	public MemberAdmin getFirmMemberByMid(String firmId,String memberId);
	/**
	 * 保存firm Member
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	public MemberAdmin saveOrUpdateFirmMember(MemberAdmin firmMember);
	/**
	 * 删除firm Member 级联删除MemberBase
	 * @author qgfeng
	 * @date 2016-12-28
	 */
	public boolean deleteFirmMemberByIds(String ids);

	/**
	 * 获取该公司下的组织架构
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param ifaFirmId 
	 */
	public List<IfafirmTeam> getOrg(String ifaFirmId);

	/**
	 * 获取该公司下团队成员
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param ifaFirmId
	 */
	public JsonPaging getIfafirmTeamIfa(String ifaFirmId, String teamId, String keyWord, JsonPaging jsonPaging);

	/**
	 * 团队移除ifa
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public Boolean delIfa(String ifaId, String teamId);

	/**
	 * 获取待审批的IFA
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public JsonPaging getMemberIfaIfafirm(String ifaFirmId,
			MemberIfaIfafirmVO memberIfaIfafirmVO, String dateFomat, String langCode, JsonPaging jsonPaging);

	/**
	 * IFA审批操作
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param memberAdmin
	 * @param ifaId
	 * @param status
	 * @param opinion
	 * @return MemberIfaIfafirm
	 */
	public MemberIfaIfafirm saveMemberIfaIfafirm(MemberAdmin memberAdmin, String ifaId, String status,
			String opinion);

	/**
	 * IFA团队更换
	 * @author wwluo
	 * @date 2016-08-30 
	 * @param ifaFirmId
	 * @param ifaId
	 * @param oldTeamId
	 * @param newTeamId
	 */
	public Integer updateIfaTeam(String ifaFirmId, String ifaId, String oldTeamId,
			String newTeamId);
	
	

}
