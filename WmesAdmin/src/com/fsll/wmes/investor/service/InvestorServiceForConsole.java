package com.fsll.wmes.investor.service;



import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountCurrency;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.member.vo.DocListVO;

public interface InvestorServiceForConsole {

	/**
	 * 获取待审批的投资人开户列表
	 * @author mqzou
     * @date 2016-08-03
	 * @param jsonPaging
	 * @param investorAccount
	 * @return
	 */
	public JsonPaging findInvestorAccountForApproval(JsonPaging jsonPaging,InvestorAccount investorAccount);
	
	/**
	 * 获取已开户的投资账户列表
	 * @author zxtan
	 * @date 2016-08-23
	 * @param jsonPaging
	 * @param adminType
	 * @param adminMemberId
	 * @return
	 */
	public JsonPaging findInvestorAccount(JsonPaging jsonPaging,InvestorAccountVO info,String langCode);

	/**
	 * 获取待审批列表（Ifafirm,Distributor）
	 * @author zxtan
	 * @date 2016-08-23
	 * @param jsonPaging
	 * @param adminType
	 * @param adminMemberId
	 * @param curUserId 当前用户Id modify by mqzou 2016-09-05
	 * @return
	 */
	public JsonPaging findInvestorAccountForApproval(JsonPaging jsonPaging,String adminType,String adminMemberId,String curUserId);
	

	/**
	 * 获取投资账户信息
	 * @author zxtan
	 * @date 2016-08-23
	 * @param id
	 * @return
	 */
	public InvestorAccount findInvestorAccountById(String id);
	
	/**
	 * 投资账户流程审批（Ifafirm,Distributor审批），更新状态
	 * @author zxtan
	 * @date 2016-08-24
	 * @param id
	 * @param status
	 * @return
	 */
	public Boolean updateFlowStatus(String id,String status);
	
	/**
	 * 获取投资人开户列表
	 * @author mqzou
     * @date 2016-08-23
	 * @param jsonPaging
	 * @param investorAccount
	 * @param langCode
	 * @return
	 */
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging,InvestorAccount investorAccount,String langCode);
	
	/**
	 * 获取账户信息
	 * @author mqzou
     * @date 2016-08-23
	 * @param id
	 * @return
	 */
	public InvestorAccount findById(String id);
	
	/**
	 * 获取开户的审批历史记录
	 * @author mqzou
	 * @date 2016-09-06
	 * @param accountId
	 * @return
	 */
	public List findApproveHis(String accountId);
	
	/**
	 * 获取开户的附件列表
	 * @author mqzou
	 * @date 2016-09-07
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	public List findAccountFileList(String accountId,String langCode,String moduleType);
	
	/**
	 * 获取账户详情
	 * @author mqzou 2017-05-16
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	public InvestorAccountVO findAccountDetail(String accountId,String langCode,String currency);
	
	/**
	 * 获取账户的资产信息
	 * @author mqzou
	 * @date 2016-11-24
	 * @param accountId
	 * @param currency
	 * @return
	 */
	public InvestorAccountCurrencyVO findAccountCurrency(String accountNo, String currency);
	
	/**
	 * 获取账户的资产列表
	 * @author mqzou  2017-04-25
	 * @param accountId
	 * @return
	 */
	public List<InvestorAccountCurrency> findAccountCurrency(String accountId);
	/**
	 * 通过账号ID获取该账号所持有的PortfolioHoldProduct列表
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<PortfolioHoldProduct> findPortfolioHoldProductByAccountId(String accountId);
	
	/**
	 * 获取投资人背景调查
	 * @author wwlin
	 * @date 2016-09-18
	 */
	public List<InvestorBackground> findInvestorbackground(String memberId);
	
	/**
	 * 获取投资人培训信息
	 * @author wwlin
	 * @date 2016-09-18
	 */
	public List<InvestorTraining> findInvestorTraining( String memberId);
	
	/***
     * IFA问卷列表查询的方法
     * @author 林文伟
     * @date 2016-09-25
     */
	public List<RpqExam> findRpqExamByMemberId(String langCode,String memberId) ;
	
	/***
	 * 获取账户关联联系人id
	 * @param accountId 账户
	 * @param contactType 联系人类型M--主联系人，J--关联联系人
	 * @return contactId 
	 * */
	public String getAccountContactId(String accountId, String contactType);
	
	/***
	 * 获取需要检查的文档
	 * @param memberId 账号
	 * @param distrubuteId 代理商
	 * @param contactId 联系人
	 * */
	public List<DocListVO> findContactDocList(String memberId,String distrubuteId, String contactId,String langCode,String accountId);
	
	/**
	 * 从模板复制doclist到invest
	 * @params distrubuteId 代理商
	 * @params clientType 开户类型
	 * @params loginLangFlag 当前语言
	 * @params memberId 当前登录用户
	 * @params mainContactId 联系人id
	 * */
	public void copyDocListfromTemp(String distrubuteId,String clientType, String loginLangFlag, String memberId,String mainContactId,InvestorAccount account);
	
	/**
	 * 判断文档是否有更新
	 * @author scshi_u330p 
	 * @date 20170113
	 * @param vo 获取 memberId，distrubuteId
	 * @param contactId 关联联系人id，导入账户该参数为空
	 * @param clientType 适用客户    Individual
	 * */
	public void checkTemplateUpdate(InvestorAccountVO vo,String contactId,String clientType,String accountId);
}
