/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.investor.service;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.crm.vo.InvestorAccountDistributorVO;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountBank;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountContactAddr;
import com.fsll.wmes.entity.InvestorAccountDeclaration;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderReturn;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.investor.vo.IndividualDataVO;
import com.fsll.wmes.member.vo.BasicInfoCombVo;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.rpq.vo.RpqListVO;
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
	 * 通过会员ID查询好友
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list 好友列表
	 */
	public List<WebFriend> findWebFriends(String fromMemberId);

	/**
	 * 通过会员ID查询IFA好友
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaFriends(String fromMemberId);
	
	/**
	 * 通过会员ID查询IFA的客户列表
	 * @author michael
	 * @param memberId IFA会员ID
	 * @param clientType 客户类型：Buddy， Client， Advisor， Prospect
	 * @return list IFA好友列表
	 */
	public List<MemberIndividual> findClientByIFA(String memberId, String clientType);
	
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
	public List<DocListVO> findContactDocList(String memberId,String distrubuteId, String contactId,String langCode);

	/**
	 * 从模板复制doclist到invest
	 * @params distrubuteId 代理商
	 * @params clientType 开户类型
	 * @params loginLangFlag 当前语言
	 * @params memberId 当前登录用户
	 * @params mainContactId 联系人id
	 * */
	public void copyDocListfromTemp(String distrubuteId,String clientType, String loginLangFlag, String memberId,String mainContactId);

	/**
	 * 搜索提交的文档列表
	 * @param moduleType
	 * @param relateId
	 * @return
	 */
	public List<AccessoryFile> findSubmitDocList(String moduleType, String relateId);
	
	/**
	 * 生成todo记录
	 * @param todo
	 * @return
	 *//*
	public WebToDo saveWebToDo(WebToDo todo);
	
	*//**
	 * 生成todo记录
	 * @param todo
	 * @param titleEn
	 * @param titleSc
	 * @param titleTc
	 * @return
	 *//*
	public WebToDo saveWebToDo(WebToDo todo, String titleEn, String titleSc, String titleTc);*/
	
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
	public List<InvestorAccount> findAllAccountList(InvestorAccount account);
	
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
	public List findAccountIfafirm(InvestorAccount account);
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
	public List<OrderReturn> findAccountOrderReturn(String langCode,String accountId) ;
	
	/**
	 * 获取投资人列表
	 * @author mqzou
	 * @date 2016-09-26
	 * @param account
	 * @return
	 */
	public List<MemberBase> findInvestorList(MemberBase memberBase,String noIfa);

	/**
	 * 获取文档审核记录
	 * @author scshi
	 * @date 20160928
	 * @param docId
	 * */
	public List<InvestorDocCheck> findDocCheckList(String docId);

	/**
	 * 获取文档详细信息
	 * @author scshi_u330p
	 * @date 20161009
	 * @param docId 文档id
	 * @param langCode 语言设置
	 * */
	public InvestorDoc findDocInfoById(String docId, String langCode);
	
	/**
	 * 获取Ifa管理的某个客户的投资账号信息
	 * @author wwluo
	 * @date 2016-10-14
	 */
	public List<InvestorAccount> findInvestorAccounts(String ifaMemberId,String customerMemberId);
}
