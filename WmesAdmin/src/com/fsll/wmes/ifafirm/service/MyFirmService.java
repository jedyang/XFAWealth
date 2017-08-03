package com.fsll.wmes.ifafirm.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.MemberIfafirm;

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
	public boolean setLeader(String ifaId, String teamId,String isSupervisor);

	/**
	 * ifa管理员设置（设置&移除）
	 * @author wwluo
	 * @date 2016-08-30 
	 */
	public boolean setIfaAdmin(String ifaId, String isAdmin);
	
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

}
