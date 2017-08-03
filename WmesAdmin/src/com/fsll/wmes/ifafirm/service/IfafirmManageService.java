package com.fsll.wmes.ifafirm.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.ifa.vo.AutoCompleteVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;

public interface IfafirmManageService {
	
	/**
	 * 获取ifafirm 列表数据
	 * @author qgfeng
	 * @date 2016-11-10
	 */
	public JsonPaging getIfaFirmJson(JsonPaging jsonPaging,MemberIfafirmVO vo,String langCode);
	
	/**
	 * 获取ifafirm列表 (不分页)
	 * @author qgfeng
	 * @date 2016-12-5
	 * @param langCode
	 * @return
	 */
	public List<MemberIfafirmVO> getIfafirmlist(String langCode);
	
	/**
	 * 根据id获取ifafirm
	 * @author qgfeng
     * @date2016-11-10
	 */
	public MemberIfafirm findById(String id,String langCode);
	/**
	 * 获取ifafirm 简体中文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	public MemberIfafirmSc findIfafirmScById(String id);
	
	/**
	 * 获取ifafirm 繁体中文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	public MemberIfafirmTc findIfafirmTcById(String id);
	
	/**
	 * 获取ifafirm 英文表
	 * @author wwluo
	 * @date 2016-08-26
	 */
	public MemberIfafirmEn findIfafirmEnById(String id);
	
	/***
	 * 保存ifafirm
	 * @date 2016-3-23
	 */
	public MemberIfafirm saveOrUpdate(MemberIfafirm memberIfafirm,boolean isAdd);
	
	/**
	 * 删除ifafirm
	 * @author wwluo
	 * @date 2016-08-22
	 */
	public boolean deleteById(String ifafirmId);
	
	/**
	 * 保存ifafirm 简体中文表信息
	 * @author wwluo
	 * @date 2016-08-26
	 */
	public MemberIfafirmSc saveOrUpdateFirmSc(MemberIfafirmSc memberIfafirmSc);

	/**
	 * 保存ifafirm 繁体中文表信息
	 * @author wwluo
	 * @date 2016-08-26
	 */
	public MemberIfafirmTc saveOrUpdateFirmTc(MemberIfafirmTc memberIfafirmTc);

	/**
	 * 保存ifafirm 英文表信息
	 * @author wwluo
	 * @date 2016-08-26
	 */
	public MemberIfafirmEn saveOrUpdateFirmEn(MemberIfafirmEn memberIfafirmEn);
	
	//-----end MemberIfafirm action----
	
	/***
     * IFAFirmTeam 团队数据列表查询的方法
     * @author 林文伟
     * @date 2016-06-29
     */
	public List<IfafirmTeam> findTeamAll( IfafirmTeam team);
	
	/***
     * IFAFirmTeam 通过ID获取团队信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeam getIfafirmTeam(String teamId) ;
	
	/***
     * 保存IfafirmTeam
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeam saveOrUpdateTeam(IfafirmTeam team, boolean isAdd);
	
	/***
     * IFAFirmTeam 删除团队数据，注意同时要删除子团队跟所有团队的成员
     * @author 林文伟
     * @date 2016-06-20
     */
	public Boolean delIfafirmTeam(String teamId);
	
	// -----end IFAFirmTeam action----
	
	/**
	 * IfafirmTeamIfa 通过公司与团队ID获取其所有成员数据
	 * @author qgfeng
	 * @date 2017-1-12
	 */
	public JsonPaging findAllTeamIfa(JsonPaging jsonpaging, String ifafirmId ,String teamId,String keywords);
	
	/**
	 * IfafirmTeamIfa 删除团队成员团
	 * @author qgfeng
	 * @date 2017-1-12
	 */
	public Boolean delIfafirmTeamIfa(String ifafirmTeamIfaId);
	
	/***
     * IfafirmTeamIfa 启动与取消团队成员的sv操作
     * @author 林文伟
     * @date 2016-06-20
     */
	public void mergeSupervisor(String id);
	
	/***
     * IfafirmTeamIfa 判断该成员是否存在在该团队中，如果已存在，则返回该成员实体
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeamIfa getIfaIsExistInTeam(String ifafirmId,String teamId,String ifaId) ;
	
	/***
     * IfafirmTeamIfa 给团队添加成员
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeamIfa saveOrUpdateTeamIfa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd);
	/***
     * IfafirmTeamIfa 通过公司与团队ID获取其所有成员数据
	 * @author qgfeng
	 * @date 2017-1-12
     */
	public JsonPaging getIFAListForSelect(JsonPaging jsonPaging,String ifafirmId, String teamId,String keyword);
	
	/***
     * IfafirmTeamIfa 通过输入一个KEY获取IFA数据，用于团队成员选择窗口
     * @author 林文伟
     * @date 2016-06-20
     */
	public List<MemberIfa> getIFAKeyList(String ifafirmId,String keywords);
	
	/***
     * 
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeamIfa saveOrUpdateTeamMemberifa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd);
	
	
	//-----end IfafirmTeamIfa action----
	
	/***
     * IfafirmDistributor 代理商列表查询的方法
     * @author 林文伟
     * @date 2016-07-12
     */
	public JsonPaging findIfafirmDistributorList(JsonPaging jsonpaging, String ifafirmId,HttpServletRequest request) ;
	
	/***
     * IfafirmDistributor 删除公司与代理商的关联关系
     * @author 林文伟
     * @date 2016-06-20
     */
	public void delIfafirmDistributorid(String id);
	
	/***
     * 添加公司与代理商的关联关系(IfafirmDistributor)
     * @author 林文伟
     * @date 2016-06-20
     */
	public void addIfafirmDistributor(String distributorId,String ifafirmId);
	
	//-----end IfafirmDistributor action----
	
	/**
	 * IfafirmIfafirm 根据id获取ifafirm关联的公司（子公司）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	public List findIfafirmIfafirmByid(String ifafirmId,String langCode);
	
	/**
	 * IfafirmIfafirm 保存公司与公司关系（子公司）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	public boolean saveChildFirm(String parentId, String childId);
	
	/**
	 * IfafirmIfafirm 移除公司与公司关系（子公司）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	public boolean removeChildFirm(String parentId, String childId);
	
	//-----end IfafirmIfafirm action----
	
	/**
	 * 查询条件自动填充IFAFirm
	 * @author qgfeng
	 * @date 2016-11-10
	 */
    public List<AutoCompleteVO> findAutoIfafirm(String keyWord,String langCode);

    /**
	 * 获取IFAFirm 列表数据
	 * @author wwluo
	 * @date 2017-06-06
	 */
	public List<MemberIfafirmVO> getIfaFirms(String langCode);
	
	 /**
     * 获取代理商的ifaFirm列表
 	 * @author rqwang
	 * @date 2017-06-08
     */
	public JsonPaging getdisIfaFirmJson(JsonPaging jsonPaging, String id,
			MemberIfafirmVO vo, String langFlag);
	
}


