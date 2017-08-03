package com.fsll.wmes.ifafirm.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.distributor.vo.DistributorSimpleVO;
import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.IfafirmTeamIfa;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.WfProcedureInfo;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.member.vo.MemberVO;

public interface IfafirmManageService {
	/***
	 * ifafirm列表查询的方法
	 * @author 林文伟
	 * @date 2016-07-01
	 * @param jsonpaging
	 * @param emailLog
	 * @return
	 */
	public JsonPaging findAll(JsonPaging jsonpaging, MemberIfafirm list ,MemberAdmin admin);
	
	/***
     * ifafirm的团队数据列表查询的方法
     * @author 林文伟
     * @date 2016-06-29
     */

	public List<IfafirmTeam> findTeamAll( IfafirmTeam team);
	
	/***
	 * 保存对象的方法
	 * @author  
	 * @date 2016-3-23
	 * @param userinfo app用户对象
	 * @return
	 */
	public MemberIfafirm saveOrUpdate(MemberIfafirm memberIfafirm,boolean isAdd);
	
	/***
     * 通过输入一个KEY获取IFA数据，用于团队成员选择窗口
     * @author 林文伟
     * @date 2016-06-20
     */
	public List<MemberIfa> getIFAKeyList(String ifafirmId,String keyword);
	
	/***
     * 通过ID获取团队信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeam getIfafirmTeam(String teamId) ;
	
	/***
     * 批量保存团队信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeam saveOrUpdateTeam(IfafirmTeam memberIfafirm, boolean isAdd);
	
	/***
     * 通过公司与团队ID获取其所有成员数据
     * @author 林文伟
     * @date 2016-06-20
     */
	public List<MemberIfa> getTeamMemberIfaByTeamId(String ifafirmId,String teamId);
	
	/***
     * 保存团队成员
     * @author 林文伟
     * @date 2016-06-20
     */
	public Boolean delTeamMemberIfa(String teamId,String ifafirmId);
	
	/***
     * 
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeamIfa saveOrUpdateTeamMemberifa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd);
	
	/***
     * 通过公司ID,团队ID获取旗下所有的团队成员
     * @author 林文伟
     * @date 2016-06-29
     */
	public JsonPaging findAllTeamIfa(JsonPaging jsonpaging, String ifafirmId ,String teamId,String keyword,String ifaid);
	
	/***
     * 给团队添加成员
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeamIfa saveOrUpdateTeamIfa(IfafirmTeamIfa ifafirmTeamIfa, boolean isAdd);
	
	/***
     * 判断该成员是否存在在该团队中，如果已存在，则返回该成员实体
     * @author 林文伟
     * @date 2016-06-20
     */
	public IfafirmTeamIfa checkIfaIsExistInTeam(String ifafirmId,String teamId,String ifaId) ;
	/***
     * 删除团队数据，注意同时要删除子团队跟所有团队的成员
     * @author 林文伟
     * @date 2016-06-20
     */
	public Boolean delIfafirmTeamInfo(String teamId);
	
	/***
     * 启动与取消团队成员的sv操作
     * @author 林文伟
     * @date 2016-06-20
     */
	public void dealSupervisor(String id);
	
	/***
     * ifafirm的代理商列表查询的方法
     * @author 林文伟
     * @date 2016-07-12
     */
	public JsonPaging findIfafirmDistributorList(JsonPaging jsonpaging, String ifafirmId) ;
	
	/***
     * 删除公司与代理商的关联关系
     * @author 林文伟
     * @date 2016-06-20
     */
	public void delIfafirmDistributorid(String id);
	
	/***
     * 添加公司与代理商的关联关系
     * @author 林文伟
     * @date 2016-06-20
     */
	public void addIfafirmDistributorid(String distributorId,String ifafirmId);
	
	/**
	 * 根据id获取ifafirm
	 * @author mqzou
     * @date 2016-07-19
	 * @param id
	 * @return
	 */
	public MemberIfafirm findById(String id);
	
	/**
	 * 获取ifafirm 列表数据
	 * @author wwluo
	 * @date 2016-08-22
	 */
	public JsonPaging getIfaFirmJson(JsonPaging jsonPaging,String companyName ,String registerNo ,String entityType,String lang);
	
	/**
	 * 删除ifafirm
	 * @author wwluo
	 * @date 2016-08-22
	 */
	public boolean deleteById(String ifafirmId);
	
	/**
	 * 根据id获取firm多语言公司名称
	 * @author wwluo
	 * @date 2016-08-22
	 */
	public Object findCompanyNameById(String langFlag,String id);
	
	/**
	 * 根据id获取ifafirm关联的公司（ifafirm -ifafirm）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	public List findIfafirmIfafirmByid(String ifafirmId);
	
	/**
	 * 保存公司与公司关系（add childFirm）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	public boolean saveChildFirm(String parentId, String childId);
	
	/**
	 * 移除公司与公司关系（remove childFirm）
	 * @author wwluo
	 * @date 2016-08-23
	 */
	public boolean removeChildFirm(String parentId, String childId);
	
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
	
	/**
	 * 获取ifafirm的简单信息
	 * @author mqzou
	 * @date 2016-11-24
	 * @param ifafirmId
	 * @param langCode
	 * @return
	 */
	public MemberIfafirmBaseVO findIfafirmBase(String ifafirmId,String langCode);

	public JsonPaging findAllTeamIfa(JsonPaging jsonpaging, String ifafirmId,String teamId, String keywords);

	public Boolean delIfafirmTeamIfa(String id);

	public JsonPaging getIFAListForSelect(JsonPaging jsonpaging, String ifafirmId, String teamId,	String keyword);

	public JsonPaging findFirmCustomerList(JsonPaging jsonpaging, String ifafirmId,String keyword);

	public JsonPaging findFirmAccountList(JsonPaging jsonpaging, String ifafirmId,
			String keyword, String status);
	
	/**
	 * 获取ifa的ifafirm
	 * @author mqzou
	 * @date 2016-11-26
	 * @param ifa
	 * @return
	 */
	public MemberIfafirm findIfafirmByIfa(MemberIfa ifa,String langCode);
	
	/***
     * 逻辑删除团队数据，注意同时要删除子团队跟所有团队的成员
     * @author michael
     * @date 2017-02-08
     */
	public Boolean delIfafirmTeamInfoLogical(String teamId);
	
	/**
	 * 检查团队编码是否已被使用
	 * @param code
	 * @return
	 */
	public boolean checkTeamIfExist(String code);

	/**
	 * 通过团队编码获取记录
	 * @param code
	 * @return
	 */
	public IfafirmTeam findTeamByCode(String code);
	
	/**
	 * 获取该member所有IFA
	 * @date 2016-11-26
	 * @author wwluo
	 * @param memberId
	 * @return
	 */
	public List<MemberIfa> getAllIfabyMember(String memberId);

	/**
	 * 获取该公司所有IFA
	 * @date 2017-03-17
	 * @author wwluo
	 * @param ifafirmId
	 * @return
	 */
	public List<MemberVO> getClientsByIfafirm(String ifafirmId, String langCode);

	/**
	 * 获取该公司所有客户
	 * @date 2017-03-17
	 * @author wwluo
	 * @param memberId
	 * @return
	 */
	public List<MemberVO> getIfasByIfafirm(String ifafirmId, String langCode);
	
	/**
	 * 获取ifafirm下所有有效的ifa
	 * @author michael
	 * @date 2016-12-13
	 */
	public List findIfaByIfaFirm(IfaVO ifaVo, String langCode);

	/**
	 * ifafirm的代理商列表查询的方法(新)
	 * @author rqwang
	 * @date 2017-06-12
	 */
	public JsonPaging findIfafirmDisList(JsonPaging jsonPaging,
			String ifafirmId, MemberDistributor disInfo,HttpServletRequest request);
	
	/**
	 *  获取与ifafirm关联的所有代理商
	 *  @author mqzou	2017-06-15
	 * @param ifafirmId
	 * @param langCode
	 * @return
	 */
	public List<DistributorSimpleVO> findAllDistributorList(String ifafirmId);
	
}


