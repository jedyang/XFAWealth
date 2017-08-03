package com.fsll.wmes.distributor.service;

import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.distributor.vo.AccountFitlerVO;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;

public interface DistributorService {
	
	/**
	 * 通过id获取代理商主数据
	 * @param id
	 * @return
	 */
	public MemberDistributor findDistributorById(String id);
	
	/**
	 * 查找代理商管理的IFAFirm
	 * @author qgfeng
	 * @date 2016-12-16
	 */
	public JsonPaging findReleIfafirm(JsonPaging jsonPaging,MemberIfafirmVO firmVo,String langCode);
	/****
	 * 移除ifa公司与代理商的关联关系
	 * @author qgfeng
	 * @date 2016-12-16
	 */
	public boolean delReleIfafirm(String distributorId,String ifaFrimId);
	
	/***
     * 保存代理商信息
     * @author 林文伟
     * @date 2016-06-20
     */
	public MemberDistributor saveOrUpdate(MemberDistributor memberDistributor, boolean isAdd);
	
	/***
     * 获取管理员
     * @author 林文伟
     * @date 2016-06-29
     */
	public MemberAdmin findDistributorMemberAdmin(String memberId);
	
	/**
	 * 获取所有的代理商
     * @author mqzou
     * @date 2016-07-19
	 * @return
	 */
	public List findAllDistributor();
	
	/***
     * 获取代理商的组织架构数据
     * @author 林文伟
     * @date 2016-06-29
     */

	public List<DistributorOrg> findDistributorOrgAll(String distributorId) ;
	
	/**
	 * 保存代理商信息
	 * @param model
	 * @return
	 */
	public MemberDistributor saveOrUpdateDistributor(MemberDistributor model);
	
	/**
	 * 通过IFA firm ID查询关联的代理商
	 * 
	 * @author michael
	 * @param  ifafirmId 顾问公司ID
	 * @return list 代理商列表
	 */
	public List<MemberDistributor> findDistributorByIfafirm(String ifafirmId);
	
	/**
	 * 获取distributor的所有ifafirm
	 * @author mqzou 2017-06-07
	 * @param distributorId
	 * @param langCode
	 * @return
	 */
	public List<MemberIfafirmBaseVO> findAllIfafirm(String distributorId,String langCode);
	
	/**
	 * 获取客户账户列表
	 * @author mqzou 2017-06-08
	 * @param jsonPaging
	 * @param vo
	 * @return
	 */
	public JsonPaging findAccountList(JsonPaging jsonPaging,AccountFitlerVO vo );
	
	/**
	 * 查找代理商的MemberAdmin
	 * @author wwlin
	 * @date 2017-5-28
	 */
	public MemberAdmin getDistributorById(String memberId) ;
	
	/**
	 * 获取基金列表(Fund Screener页面)
	 * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	public JsonPaging findFundInfoList(JsonPaging jsonPaging, FundScreenerDataVO filters);

}


