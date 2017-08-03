package com.fsll.wmes.crm.service;

import java.util.Date;
import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.crm.vo.ClientRemindVO;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.ClientsBasicVO;
import com.fsll.wmes.crm.vo.CrmClientForIfaVO;
import com.fsll.wmes.crm.vo.CrmCustomerVO;
import com.fsll.wmes.crm.vo.CrmExistingCustomerVO;
import com.fsll.wmes.crm.vo.CrmProspectCustomerVO;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.CrmCustomerGroup;
import com.fsll.wmes.entity.CrmCustomerGroupRelationship;
import com.fsll.wmes.entity.CrmMemo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.member.vo.IfaVO;

public interface CrmCustomerService {
	/**
	 * 获取客户列表（条件：本ifafrim、ifa）
	 * @author qgfeng
	 * @date 2016-12-14
	 */
    public JsonPaging findInvestorByIfa(JsonPaging jsonPaging,IfaVO ifaVO,String langCode);
    
    
    /**
     * 获取客户的投资组合列表
     * @author mqzou
     * @date 2016-08-23
     * @param jsonPaging
     * @param portfolioInfo
     * @return
     */
    public JsonPaging findPortfolioByCustomer(JsonPaging jsonPaging,PortfolioInfo portfolioInfo);
    
    /**
     * 获取投资组合详细信息
     * @author mqzou
     * @date 2016-08-24
     * @param id
     * @return
     */
    public PortfolioInfo findPortfolioInfo(String id);
    
	/**
	 * 获取客户信息
	 * @author zxtan
	 * @date 2016-09-13
	 * @param langCode
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	public CrmCustomerVO findCustomerDetailForIfa(String customerId,String langCode);
	
	/**
	 * 获取ifa的所有客户
	 * @author mqzou
	 * @date 2016-09-23
	 * @param ifaId
	 * @return
	 */
	public JsonPaging findCustomerByIfa(JsonPaging jsonPaging,MemberIfa memberIfa);
    
	/**
	 * 获取客户列表
	 * @author zxtan
	 * @date 2016-09-26
	 * modify by mqzou 2016-10-25 增加搜索条件
	 */
	public JsonPaging findCustomerListForIfa(JsonPaging jsonPaging,String ifaMemberId,String areaId,String period,String saleStageId,String keyword,String character);
	
	/**
	 * 获取客户生日提醒
	 * @author zxtan
	 * @date 2016-09-26
	 */
	public Long findCustomerBirthdayRemindForIfa(String ifaMemberId,String clientType,String period);

	/**
	 * 获取客户日程提醒
	 * @author zxtan
	 * @date 2016-09-26
	 */
	public Long findCustomerAppointmentRemindForIfa(String ifaMemberId,String clientType,String period);

	/**
	 * 获取客户投资组合提醒
	 * @author zxtan
	 * @date 2016-09-29
	 */
	public Long findCustomerPortfolioRemindForIfa(String ifaMemberId,String clientType);


	/**
	 * 获取客户KYC提醒
	 * @author zxtan
	 * @date 2016-09-29
	 */
	public Long findCustomerKYCRemindForIfa(String ifaMemberId,String clientType,String period);
	
	/**
	 * 获取客户信息
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	public CrmCustomer findCustomerById(String id);
	
	/**
	 * 保存客户信息
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	public CrmCustomer saveCustomer(CrmCustomer obj);
	
	/**
	 * 更新客户重要性
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	public Boolean updateImportant(String id,String isImportant);
	
	/**
	 * 更新客户信息备注
	 * @author zxtan
	 * @date 2016-10-08
	 * @param id
	 * @param remark
	 * @return
	 */
	public Boolean updateRemark(String id,String remark);
	
	/**
	 * 删除客户信息
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	public void deleteCustomer(String id,String saleStageId);
	
	/**
	 * 获取已存在的客户
	 * @author zxtan
	 * @date 2016-09-27
	 * @param obj
	 * @return
	 */
	public JsonPaging findExistingCustomerListForIfa(JsonPaging jsonPaging,String ifaMemberId,String areaId,String period,String clientStatus,String keyword,String character);
	
	/**
	 * 获取非ifa客户的所有投资人
	 * @author mqzou
	 * @date 2016-10-25
	 * @param ifaMember
	 * @return
	 */
	public JsonPaging findInverstorNotInCrm(JsonPaging jsonPaging, MemberBase ifaMember,String langCode,String invStyle,String intrest,String noIfa,String region);
	
	
	/**
	 * 保存CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	public CrmCustomerGroup saveGroup(CrmCustomerGroup obj);
	
	
	/**
	 * 删除CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	public Boolean deleteGroup(String id);
	
	/**
	 * 通过Id 获得CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	public CrmCustomerGroup findGroupById(String id);
	
	/**
	 * 获取Ifa的CRM 用户分组信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	public JsonPaging findGroupList(JsonPaging jsonPaging,String ifaId);
	
	/**
	 * 保存CRM 用户分组关系信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	public CrmCustomerGroupRelationship saveGroupRelationship(CrmCustomerGroupRelationship obj);
	
	/**
	 * 删除CRM 用户分组关系信息
	 * @author zxtan
	 * @date 2016-11-14
	 */
	public Boolean deleteGroupRelationship(String id);	

	/**
	 * 通过CustomerId获得 CRM 用户分组关系信息
	 * @author zxtan
	 * @date 2016-11-14
	 */	
	public CrmCustomerGroupRelationship findGroupRelationshipByCustomerId(String customerId);
	
	/**
	 * 获取是否重要客户
	 * @author mqzou
	 * @date 2016-11-21
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfImportantCrm(String ifaMemberId,String memberId);


	public List<CrmProspectCustomerVO> findPropectCustomerList(String langCode,String ifaMemberId, String saleStageId, String keyword);
	
	public List<CrmExistingCustomerVO> findExistingCustomerList(String langCode, String ifaMemberId,String clientStatus,String keyword,String toCurrency);
	
	/**
	 * 获取IFA的客户列表
	 * @author mqzou
	 * @date 2016-12-15
	 * @param jsonPaging
	 * @param clientSearchVO
	 * @return
	 */
	public JsonPaging findClentsByIFA(JsonPaging jsonPaging,ClientSearchVO clientSearchVO);
	
	/**
	 * 获取时间段内客户信息提醒
	 * @author mqzou
	 * @date 2016-12-16
	 * @param ifaId
	 * @param ifaMemberId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ClientRemindVO findClientRemind(String ifaId,String ifaMemberId,String startDate,String endDate);
	
	/**
	 * 获取一段时间内需要进行KYC的客户
	 * @author mqzou
	 * @date 2016-12-16
	 * @param ifaId
	 * @param memberId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List findRpqPre(String ifaId,String memberId,Date startDate,Date endDate);
	
	/**
	 * 检查客户是是否生日
	 * @author mqzou
	 * @date 2016-12-16
	 * @param ifaId
	 * @param memberId
	 * @param days
	 * @return
	 */
	public List checkBirthDayCustomer(String ifaId,String memberId,int days);
	
	/**
	 * 检查客户是否有日程
	 * @author mqzou
	 * @date 2016-12-16
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	public List checkSchedultCustomer(String ifaId,String memberId,Date startDate,Date endDate);
	
	/**
	 * 更新潜在客户状态
	 * @author zxtan
	 * @date 2016-12-19
	 * @param obj
	 * @return
	 */
	public Boolean updateStageId(String id,String stageId);
	
	/**
	 * 获取ifa的客户列表（ifa详情）
	 * @author mqzou 2016-12-26
	 * @param ifaMemberId
	 * @param keyWord
	 * @param type 客户类型
	 * @return
	 */
	public JsonPaging findClientForIfa(JsonPaging jsonPaging, String ifaMemberId,String keyWord,String type,String langCode);
	
	/**
	 * 根据ifaId和memberId获取实体
	 * @author mqzou 2016-12-29
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	public CrmCustomer findByIfaAndMember(String ifaId,String memberId);
	
	/**
	 * 根据ifaId获取客户实体列表
	 * @author mqzou 2016-12-29
	 * @param ifaId
	 * @param memberId
	 * @return
	 */
	public List<CrmCustomer> findCustomerByIfaId(String ifaId);

	/**
	 * 根据ifa的memberId查找客户
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<CrmCustomer> findCustomerByIfa(String memberId);
	
	/**
	 * 检测是否存在客户
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistCrm(String ifaMemberId, String memberId);
	
	/**
	 * 复制IFA的客户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaCustomer(String fromMemberId,String toMemberId,MemberBase createBy);
	
	/**
	 * 根据客户的memberId查找客户
	 * @author michael
	 * @param memberId 客户id
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<CrmCustomer> findCustomerByMember(String memberId);

	/**
	 * 获取备注记录
	 * @author wwluo
	 * @param ifaMemberId 
	 * @param memberId 
	 * @return
	 */
	public List<CrmMemo> findCrmMemo(String ifaMemberId, String memberId);
}
