/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.investor.service;

import java.io.FileNotFoundException;
import java.util.List;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.community.vo.CommunityNewsListVO;
import com.fsll.wmes.community.vo.FrontCommunityTopicVO;
import com.fsll.wmes.community.vo.TopicDetailVO;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.InvestorAccountDistributorVO;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountBank;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountContactAddr;
import com.fsll.wmes.entity.InvestorAccountCurrency;
import com.fsll.wmes.entity.InvestorAccountDeclaration;
import com.fsll.wmes.entity.InvestorAccountStream;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorReport;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.MemberAccountSso;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MyAsset;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.fund.vo.ChartDataVO;
import com.fsll.wmes.fund.vo.FundAnnoVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.ifa.vo.IfaClientAccountVO;
import com.fsll.wmes.ifa.vo.StrategyInfoVO;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.IndividualDataVO;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.investor.vo.InvestorHomeMostSelectedFundsVO;
import com.fsll.wmes.investor.vo.InvestorHomeTopPerformanceFundsVO;
import com.fsll.wmes.investor.vo.InvestorHomeTopPerformancePortfolioVO;
import com.fsll.wmes.investor.vo.InvestorMyPortfolioVO;
import com.fsll.wmes.investor.vo.InvestorMyPortfolios;
import com.fsll.wmes.investor.vo.MyAssetChartDataVO;
import com.fsll.wmes.investor.vo.MyAssetsVO;
import com.fsll.wmes.investor.vo.PortfolioAssetsVO;
import com.fsll.wmes.investor.vo.WebFriendVO;
import com.fsll.wmes.member.vo.BasicInfoCombVo;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.news.vo.NewsInfoVO;
import com.fsll.wmes.notice.vo.NoticeVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;
import com.fsll.wmes.rpq.vo.RpqListVO;
import com.fsll.wmes.strategy.vo.CharDataVO;
/***
 * 业务接口类：开户管理
 * @author michael
 * @date 2016-7-7
 */
public interface InvestorService {
	
	// saveOrUpdate -------------
	public InvestorDoc saveOrUpdateFileToInvestorDoc(InvestorDoc investorDoc);
	
	public AccessoryFile saveOrUpdateFileToAccessoryFile(AccessoryFile accessoryFile);
	
	public InvestorAccountDeclaration saveOrUpdateInvestorAccountDeclarationAgeas( InvestorAccountDeclaration ageas,String accountId );
	
	/**
	 * 保存开户基本信息
	 * @author michael
	 * @param account 开户基本信息实体
	 * @return InvestorAccount
	 */
	public InvestorAccount saveOrUpdateInvestorAccount(InvestorAccount account);
	// saveOrUpdate -------------
	
	
	// delete -------------
	public void deleteAccessoryFile( AccessoryFile accessoryFile );
	
	public void deleteInvestorDoc( InvestorDoc investorDoc );	
	// delete -------------
	
	
	// find list -------------
	public List<RpqPageLevel> findRpqPageLevel( String pageId, int score, String status, String isValid,String langCode );
	
	public List<DocListVO> findDocumentCheckList( String distributorId, String clientType, String langCode, MemberBase loginUser );
	
	public InvestorAccountDeclaration findInvestorAccountDeclarationAgeasForAccount( String accountId );
	
	/**
	 * 通过会员ID查询IFA的客户列表(通过webfriend表关联)
	 * @author michael
	 * @param memberId IFA会员ID
	 * @param clientType 客户类型：Buddy， Client， Advisor， Prospect
	 * @return list IFA好友列表
	 */
	public List<MemberIndividual> findClientByIFA(String memberId, String clientType);
	
	/**
	 * 通过会员ID查询IFA的客户列表(通过CrmCustomer表关联)
	 * 
	 * @author michael
	 * @param ifaId IFA会员ID
	 * @return list IFA客户列表
	 */
	public List<MemberIndividual> findCustomerByIFA(String ifaId);
	
	/**
	 * 通过IFA ID查询IFA关联的代理商
	 * @author michael
	 * @param ifaId IFA会员ID
	 * @return list IFA关联的代理商列表
	 */
	public List<MemberDistributor> findIfaDistributors(String ifaId);

	/**
	 * 通过会员ID查询IFA的同事
	 * @author michael
	 * @param memberId 排除当前会员ID
	 * @param ifaFirmId IFA公司ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaColleagues(String memberId, String ifaFirmId);
	
	/**
	 * 通过会员ID查询IFA的组员
	 * @author michael
	 * @param memberId 会员ID
	 * @param ifaFirmId IFA公司ID
	 * @param ifaTeamId IFA组ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaTeamColleagues(String memberId, String ifaFirmId, String ifaTeamId);
	
	public List<RpqQuestItem> findRpqQuestItemList(String questId);
	
	public List<RpqPageQuest> findRpqPageQuest( String pageId );
	
	public List<RpqPage> findRpqPage( String type, String distributorId, String clientType, String langCode, String isDef, String pageType );
	
	public List<RpqListVO> findRpqQuestList( String type, String distributorId, String clientType, String langCode, String isDef, String pageType );
	// find list -------------
	
	
	// get entity -------------
	public RpqPage getRpqPage(String id);
	
	public RpqPageLevel getRpqPageLevel(String id);
	
	public RpqQuest getRpqQuest(String id);
	
	public MemberDistributor getMemberDistributor(String id);
	
	public DocTemplate getDocTemplate(String id);
	
	public InvestorAccountDeclaration getInvestorAccountDeclarationAgeas( String id );
	
	public AccessoryFile getAccessoryFile(String id);
	
	public InvestorDoc getInvestorDoc(String id);
	// get entity -------------	

	/***
	 * 保存开户step-3基本信息
	 * @author scshi
	 * @param basicVo 基本信息总类VO
	 * @date 20160714
	 * */
	public void saveOrUpdateBasicInfo(BasicInfoCombVo basicVo);

	/**
	 * 根据id获取InvestorAccount实体
	 * @param accountId
	 * @return
	 */
	public InvestorAccount findInvestorAccountById(String accountId);
	
	/**
	 * 获取账户详情
	 * @author mqzou 2017-05-16
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	public InvestorAccountVO findAccountDetail(String accountId,String langCode,String currency);
	
	/**
	 * 获取账户关联联系人
	 * @author scshi
	 * @param account 账户
	 * @param contactType 联系人类型,M:帐户或者主联系人信息,J:联名帐户联系人信息
	 * */
	public InvestorAccountContact findIContactByType(InvestorAccount account, String contactType,String langCode);
	
	/**
	 * 获取联系人地址信息
	 * @author scshi
	 * @param contact 联系人
	 * @param addrType R:Residential居住地址 P:Permanent永久地址,C:Correspondence通信地址
	 * */
	public InvestorAccountContactAddr findIContactAddr(InvestorAccountContact iContact, String addrType);
	
	/**
	 * 获取账户关联银行信息
	 * @author scshi
	 * @param account 账户
	 * */
	public InvestorAccountBank findBankAgeasByAccid(InvestorAccount iAccount);

	/**
	 * 通过memberId获取信息
	 * @author michael
	 * @param memberId 成员ID
	 * @return MemberIfa
	 */
	public MemberIfa findIfaByMemberId(String memberId);	

	 /***
     * 分页查询开户申请列表
     * @author michael
     * @param jsonPaging 分页参数
     * @param loginUser 当前登录用户
	 * @return
     */
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging,MemberBase loginUser, String queryType);
	/**
	 * 账号信息保存
	 * @author scshi
	 * @param account 账号信息
	 * */
	public void saveInvestorAccount(InvestorAccount account);

	/**
	 * 基本信息保存
	 * @author scshi
	 * @param iContact 主要联系人信息
	 * @param accountId 关联账号Id
	 * @return iContactId 主要联系人id
	 * */
	public String saveOrUpdateMainContact(InvestorAccountContact iContact,String accountId);
/**
 * 联系人地址保存
 * @author scshi
 * @param iContactAddr 地址实体
 * @param iContactId	关联联系人
 * @param accountId	关联账号
 * */
	public String saveOrUpdateIaddr(InvestorAccountContactAddr iContactAddr, String iContactId,String accountId);
	/**
	 * 联系人地址保存
	 * @author scshi
	 * @param iBank 银行信息实体
	 * @param iContactId	关联联系人
	 * */
	public String saveOrUpdateIbank(InvestorAccountBank iBank, String accountId);
	
	/**
	 * 通过memberId获取信息
	 * @author michael
	 * @param memberId 成员ID
	 * @return MemberIndividual
	 */
	public MemberIndividual findIndividualByMemberId(String memberId);
	
	/**
	 * 通过memberId获取信息
	 * @author michael
	 * @param memberId 成员ID
	 * @return IndividualDataVO
	 */
	public IndividualDataVO findIndividualFullInfoByMemberId(String memberId);
	
	 /**
	  * 分页查询开户申请列表
	  * @param jsonPaging 分页参数
	  * @param loginUser 当前登录用户
	  * @param submitSts 提交方：invest投资人提交,ifa提交
	  * @param openSts 开户状态:-2待推送,-1已推送等待结果,1成功开户,0失败
	  * @param finishSts 完成状态：0草稿,1已提交
	  * @param flowSts 流程状态：-1还未执行,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
	  * @return
	  */
	//@Transactional(readOnly = true)
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging,MemberBase loginUser, String queryType, String submitSts, String openSts, String finishSts, String flowSts);

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
	 * 搜索提交的文档列表
	 * @param moduleType
	 * @param relateId
	 * @return
	 */
	public List<AccessoryFile> findSubmitDocList(String moduleType, String relateId);
	
	
	/**
	 * 获取Ifa管理的投资账户
	 * @author zxtan
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	public JsonPaging findInvestorAccountListForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId);
	
	
	/**
	 * 获取Ifa管理的某个客户的投资账号代理商列表
	 * @author zxtan
	 * @date 2016-09-09
	 */
	public List<InvestorAccountDistributorVO> findInvestorDistributor(String ifaMemberId,String customerMemberId);
	
	
	/**
	 * 获取Ifa管理的某个客户的投资账号列表
	 * @author zxtan
	 * @date 2016-09-09
	 */
	public List<InvestorAccount> findInvestorAccountListForIfa(String ifaMemberId, String customerMemberId,String distributorId);
	
	
	/**
	 * 获取Ifa管理的某个客户的投资账号文档
	 * @author zxtan
	 * @date 2016-09-09
	 */
	public InvestorDoc findInvestorDocForDistributor(String distributorId,String customerMemberId);
	
	/**
	 * 获取投资人的所有账户
	 * @author mqzou
	 * @date 2016-09-12
	 * @param account
	 * @return
	 */
	public List<AccountVO> findAllAccountList(InvestorAccount account);
	
	/**
	 * 获取投资人的账户所属的distributor
	 * @author mqzou
	 * @date 2016-09-12
	 * @param account
	 * @return
	 */
	public List findInvestorDistributor(InvestorAccount account);
	
	/**
	 * 获取主张号下的所有子帐号列表
	 * @author mqzou
	 * @date 2016-09-20
	 * @param account
	 * @return
	 */
	public List<InvestorAccount> findSubAccountList(InvestorAccount account);
	
	/**
	 * 获取投资人的账户所属的ifafirm
	 * @author mqzou
	 * @date 2016-09-14
	 * @param account
	 * @return
	 */
	public List findAccountIfafirm(InvestorAccount account,String langCode);
	/**
	 * 获取投资人的账户所属的用户
	 * @author mqzou
	 * @date 2016-09-18
	 * @param account
	 * @return
	 */
	public List findAccountMember(InvestorAccount account);
	
	/*******************************投资人背景调查、培训*****************************************/
	/**
	 * 获取投资人背景调查
	 * @author wwlin
	 * @date 2016-09-18
	 */
	public List<InvestorBackground> findInvestorbackground(String memberId);
	
	/**
	 * 保存投资人背景调查
	 * @author wwlin
	 * @param account 背景调查基本信息实体
	 * @return InvestorAccount
	 */
	public InvestorBackground saveOrUpdateInvestorBackground(InvestorBackground ib);
	
	/***
     * 通过ID获取背景信息
     * @author 林文伟
     * @date 2016-09-19
     */
	public InvestorBackground getInvestorBackground(String id);
	
	/***
     * 通过ID获取培训信息
     * @author 林文伟
     * @date 2016-09-19
     */
	public InvestorTraining getInvestorTraining(String id) ;
	
	/***
     * 删除背景信息
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean delInvestorBackground(String id);
	
	/**
	 * 获取投资人培训信息
	 * @author wwlin
	 * @date 2016-09-18
	 */
	public List<InvestorTraining> findInvestorTraining( String memberId);
	
	/**
	 * 保存投资人培训信息
	 * @author wwlin
	 * @param account 培训信息基本信息实体
	 * @return InvestorAccount
	 */
	public InvestorTraining saveOrUpdateInvestorTraining(InvestorTraining model);
	
	public AccessoryFile saveOrUpdateAccessoryFile(AccessoryFile model);
	
	/**
	 * 获取帐号开户的审批记录
	 * @author mqzou
	 * @date 2016-09-21
	 * @param accountId
	 * @return
	 */
	public List<WfProcedureInstanseTodo> findAccountWfHistory(String accountId);

	
	/***
     * 删除投资人培训信息
     * @author 林文伟
     * @date 2016-09-18
     */
	public Boolean delInvestorTraining(String id);
	
	/***
     * IFA问卷列表查询的方法
     * @author 林文伟
     * @date 2016-09-25
     */
	public List<RpqExam> findRpqExamByMemberId(String langCode,String memberId) ;
	
	/***
     * 账号的交易历右记录
     * @author 林文伟
     * @date 2016-09-27
     */
	public JsonPaging findAccountOrderReturn(JsonPaging jsonPaging,String langCode,String accountId,String transactionType) ;
	
	/**
	 * 获取投资人列表
	 * @author mqzou
	 * @date 2016-09-26
	 * @param account
	 * @return
	 */
	public List<MemberBase> findInvestorList(MemberBase memberBase,String noIfa);

	
	/**
	 * 获取Ifa管理的某个客户的投资账号信息
	 * @author wwluo
	 * @date 2016-10-14
	 */
	public List<InvestorAccount> findInvestorAccounts(String ifaMemberId,String customerMemberId);
	
	/**
	 * 获取投资人账户列表（IFA）
	 * @author mqzou 2016-11-10
	 * @param jsonPaging
	 * @param accountVO
	 * @return
	 */
	public JsonPaging findAccountList(JsonPaging jsonPaging,AccountVO accountVO,String langCode);
	
	/**
	 * 获取帐号的收益图表数据
	 * @author mqzou 2016-11-10
	 * @param accountNo
	 * @param currency
	 * @return
	 */
	public List<CharDataVO> findAccountCurrency(String accountNo,String subAccountNo,String currency);
	
	/**
	 * Accounts Wait for Process
	 * @author wwluo
	 * @date 2016-10-14
	 */
	public JsonPaging getAccountsWaitforProcess(JsonPaging jsonPaging,
			MemberBase loginUser ,String distributorId,String ifaFirmId ,String keyWord,String lang);
	
	/**
	 * 获取用户的资产信息
	 * @author mqzou
	 * @date 2016-11-16
	 * @param memberId
	 * @return
	 */
	public InvestorAccountCurrencyVO findInvestorCurrency(String memberId,String currency,String ifaId);
	
	/**
	 * 获取账户的资产信息
	 * @author mqzou
	 * @date 2016-11-24
	 * @param accountId
	 * @param currency
	 * @return
	 */
	public InvestorAccountCurrencyVO findAccountCurrency(String accountNo,String currency);
	
	/**
	 * 获取账户的所有资产列表
	 * @author mqzou
	 * @date 2016-11-24
	 * @param accountNo
	 * @return
	 */
	public List<InvestorAccountCurrencyVO> findAccountCurrencyList(String accountNo );
	
	/**
	 * 获取投资人rpq中的最高风险系数
	 * @author mqzou
	 * @date 2016-12-16
	 * @param memberId
	 * @return
	 */
	public String findInvestorMaxRiskLevel(String memberId);
	
	/**
	 * 检查投资人是否FACA或CIES
	 * @author mqzou
	 * @date 2016-12-16
	 * @param memberId
	 * @return
	 */
	public boolean checkFacaOrCies(String memberId,String type) ;
	
	

	/**
	 * 获取账户信息
	 * @author wwluo
	 * @date 2016-12-13
	 * @param 
	 * @return
	 */
	public List<InvestorAccountVO> findInvestorByMember(MemberBase member,
			MemberIfa memberIfa,String langCode,String toCurrency);
	
	/**
	 * 获取ifa的客户的账户列表
	 * @author mqzou
	 * @date 2016-12-27
	 * @param ifaMemberId
	 * @return
	 */
	public JsonPaging findCrmAccountList(JsonPaging jsonPaging,String ifaMemberId,String distributorId,String keyWord,String currency,String langCode);
	
	/**
	 * 根据投资人id获取它的所有InvestorAccount账号下的ifa数据
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<MemberIfa> findInvestorAccountListByInvestorId(String investorId);
	
	/**
	 * 根据投资人accountNo获取它的所有InvestorAccount账号
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<InvestorAccount> findInvestorAccountListByAccountNo(String accountNo);
	
	/**
	 * Investor投资首页，Most Selected Funds 从portfolio_hold_product跟portfolio_arena_produc中进行产品统计
	 * @author mqzou
	 * @date 2016-11-26
	 * @param ifa
	 * @return
	 */
	public List<InvestorHomeMostSelectedFundsVO> getMostSelectedFunds(String langCode);
	
	/**
	 * Top Performance Portfolio
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<InvestorHomeTopPerformancePortfolioVO> findTopPerformancePortfolio(String langCode,String periodType, String displayColor,String memberId);
	
	/**
	 * Top Performance Funds
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<InvestorHomeTopPerformanceFundsVO> findTopPerformanceFunds(String langCode,String periodType, String displayColor);
	
	/**
	 * My zone My All Portfolio Detail 
	 * author:林文伟
	 * modify by mqzou 2017-01-10
	 * @param investorId
	 * @return
	 */
	public List<InvestorMyPortfolioVO> findMyAllPortfolio(String memberId,String langCode,String currency) ;
	
	/**
	 * 登录成功后获取SSO实体
	 * author:林文伟
	 * @param accountCode
	 * @return
	 */
	public  MemberAccountSso  getMemberAccountSso(String accountCode);
	
	/**
	 * 根据投资人id获取它的所有InvestorAccount账号
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<InvestorAccount> findInvestorAccountListByMemberId(String memberId);
	
	/**
	 * 获取审批角色下的所有MemberBase列表
	 * @author michael
	 * @date 2016-12-30
	 * @param companyId 运营公司id
	 * @param accountId 账户id
	 * @param roleType 审批角色类型（表member_console_check_role）
	 */
	public List findMemberByRole(String companyId, String accountId, String roleType);
	
	/**
	 * 根据holdId获取它的所有PortfolioHoldCumperf data
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<PortfolioHoldCumperf> findPortfolioHoldCumperfListByHoldId(String holdId);

	/**
	 * PortfolioHoldAccount
	 * @author wwluo
	 * @date 2017-01-08
	 * @param accountNoId
	 * @param ifaId
	 * @return
	 */
	public PortfolioHoldAccount findPortfolioHoldAccount(String accountNo, String ifaId,
			String langCode, Object toCurrency);

	/**
	 * InvestorAccountVO
	 * @author wwluo
	 * @date 2017-01-08
	 * @param investorAccount
	 * @return
	 */
	public InvestorAccountVO findPortfolioHoldAccount(
			InvestorAccount investorAccount, String langCode);

	/**
	 * InvestorAccountVO
	 * @author wwluo
	 * @date 2017-01-08
	 * @param 
	 * @return
	 */
	public InvestorAccount findPortfolioHoldAccount(String distributorId, String ifaId);
	/**
	 * GET IFA AE CODE
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public IfaDistributor findIfaDistributorByIfaId(String ifaId) ;
	
	/**
	 * 获取投资人的持仓账户
	 * @author mqzou
	 * @date 2017-01-09
	 */
	public List<String> findInvestorHoldAccount(String memberId,String portfolioHoldId);
	
	/**
	 * 通过账号ID获取该账号所持有的PortfolioHoldProduct列表
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	public List<PortfolioHoldProduct> findPortfolioHoldProductByAccountId(String accountId);
	
	/**
	 * 获取投资人的ifa
	 * @author mqzou
	 * @date 2017-01-11
	 * @param memberId
	 * @return
	 */
	public List<MemberIfa> findInvestorIfa(String memberId);
	
	/**
	 * 获取投资人的好友
	 * @author mqzou
	 * @date 2017-01-11
	 * @param memberId
	 * @param count
	 * @return
	 */
	public List<WebFriendVO> findInvestorBuddyList(String memberId,int count);
	
	/**
	 * 根据条件搜索代理商
	 * @param searchVo
	 * @return
	 */
	public List findDistributorByCriteria(ClientSearchVO searchVo);
	
	/**
	 * 检查是否已有账户
	 * @param distributorId
	 * @param memberId
	 * @return
	 */
	public boolean checkAccount(String distributorId, String memberId);

	/**
	 * 检查是否已有账户号
	 * @param memberId
	 * @param accountNo
	 * @return
	 */
	public boolean checkAccountNo(String memberId, String accountNo);
	
	/**
	 * 获取InvestorAccount
	 * @param distributorId
	 * @param memberId
	 * @return
	 */
	public List<InvestorAccount> findInvestorAccountsByDistributorMember(String distributorId, String memberId);

	/**
	 * 复制IFA的客户投资账户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaCustomerAccount(String fromMemberId,String toMemberId,MemberBase createBy);
	
	/**
	 * 获取Ifa管理的投资账户
	 * @author zxtan
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param customerMemberId
	 * @return
	 */
	public JsonPaging findInvestorListForIfa(JsonPaging jsonPaging,String ifaMemberId);
	
	/**
	 * 根据查询条件获取投资人的投资账户
	 * @author michael
	 * @param memberId
	 * @param criterias
	 * @return
	 */
	public List<InvestorAccount> findInvestorAccountList(String memberId, InvestorAccount criterias);

	/**
	 * 获取我的IFA信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<IfaVO> getMyIfas(String memberId,String langCode);

	/**
	 * 获取推荐的组合
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<PortfolioArenaVO> getRecommendedPortfolios(String memberId, String isPublic, String langCode);

	/**
	 * 获取arean浏览记录
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<PortfolioArenaVO> getInvestorVisitArena(String memberId, String langCode);

	/**
	 * 获取推荐的策略
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<StrategyInfoVO> getRecommendedStrategies(String memberId,
			String isPublic, String langCode);

	/**
	 * 获取strategy浏览记录
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<StrategyInfoVO> getInvestorVisitStrategies(String memberId, String langCode);

	/**
	 * 获取推荐的基金
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<FundInfoDataVO> getRecommendedFunds(String memberId, String langCode);

	/**
	 * 获取fund浏览记录
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<FundInfoDataVO> getInvestorVisitFunds(String memberId, String langCode);

	/**
	 * 获取持仓基金公告
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	public List<FundAnnoVO> getHoldFundNotice(MemberBase loginUser, String langCode, Integer maxResults);

	/**
	 * 获取指定时间内，指定好友类型发布的insight
	 * @author wwluo
	 * @date 2017-03-28
	 * @param type 关系类型：Buddy Client Advisor Prospect
	 * @param 时间类型 天：D，周：W，月：M，年：Y
	 * @param period
	 * @param memberId
	 * @param langCode
	 * @param maxResults 获取数量
	 * @return
	 */
	public List<FrontCommunityTopicVO> getRecommendedInsights(MemberBase loginUser, String type,
			String periodType, Integer period, String memberId, String langCode, Integer maxResults);

	/**
	 * 获取Hot Topic
	 * @author wwluo
	 * @date 2017-03-28
	 * @param rule
	 * @param maxResults 获取数量
	 * @return
	 */
	public List<FrontCommunityTopicVO> getHotTopics(MemberBase loginUser,
			String rule, Integer maxResults, String langCode);

	/**
	 * 获取投资者账单信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param type 报告类型,w周报,m月报,q季报,h半年报,y年报
	 * @param maxResults 获取数量
	 * @return
	 */
	public List<InvestorReport> getInvestorReports(String memberId, String type, Integer maxResults);

	/**
	 * 获取我的组合资产信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param currencyCode 货币编码
	 * @param periodType 周期类型,'M':月，'Y':年,'YTD':年初至今
	 * @param period
	 * @return
	 */
	public List<PortfolioAssetsVO> getMyPortfolioTotalAssets(String memberId,
			String currencyCode, String periodType, String period);

	/**
	 * 获取我的资产信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param currencyCode
	 * @return
	 */
	public MyAsset getMyAsset(String memberId, String currencyCode);

	/**
	 * 计算全部持仓组合资产信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param currencyCode
	 * @return
	 */
	public InvestorMyPortfolios getAllPortfolioAssets(String memberId,
			String currencyCode);
	
	/**
	 * My Favourites News
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param maxResults
	 * @return
	 */
	public List<NewsInfoVO> getMyFavouritesNews(String memberId, String langCode, Integer maxResults);

	/**
	 * My Favourites Topics
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param maxResults
	 * @return
	 */
	public List<TopicDetailVO> getMyFavouritesTopics(String memberId, String langCode, Integer maxResults);

	/**
	 * 删除我的收藏信息（逻辑删除）
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param maxResults
	 * @return
	 */
	public Boolean deleteMyWebFollow(String followId);

	/**
	 * 获取 Statement
	 * @author wwluo
	 * @data 2017-03-16
	 * @param jsonPaging
	 * @param memberId
	 * @param type 报告类型,w周报,m月报,q季报,h半年报,y年报
	 * @return
	 */	
	public JsonPaging getInvestorReports(JsonPaging jsonPaging, String memberId,
			String type);

	/**
	 * 获取 推荐的基金公告
	 * @author wwluo
	 * @data 2017-03-16
	 * @param memberId
	 * @param langCode
	 * @param maxResults
	 * @return
	 */	
	public List<FundAnnoVO> getRecommendedFundAnnos(MemberBase loginUser, String langCode,
			Integer maxResults);

	/**
	 * 获取新闻
	 * @author wwluo
	 * @data 2017-03-16
	 * @param maxResults
	 * @return
	 */	
	public List<CommunityNewsListVO> getNews(Integer maxResults);
	
	/**
	 * 获取账户的资产列表
	 * @author mqzou  2017-04-25
	 * @param accountId
	 * @return
	 */
	public List<InvestorAccountCurrency> findAccountCurrency(String accountId);
	
	/**
	 * 删除投资帐号
	 * @author mqzou 2017-05-16
	 * @param account
	 */
	public void deleteAccount(InvestorAccount account);
	
	/**
	 * 获取投资人的资产收益图表数据
	 * @author mqzou  2017-06-06
	 * @param memberId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
    public List<MyAssetChartDataVO> findAssetCumperfsData(String memberId,String beginDate,String endDate,String dateFormat,String currency);
}
