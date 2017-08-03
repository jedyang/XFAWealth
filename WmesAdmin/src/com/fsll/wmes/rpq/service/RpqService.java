/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.rpq.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.fsll.core.entity.AccessoryFile;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountDeclaration;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqExamAnswer;
import com.fsll.wmes.entity.RpqExamEn;
import com.fsll.wmes.entity.RpqExamQuest;
import com.fsll.wmes.entity.RpqExamQuestEn;
import com.fsll.wmes.entity.RpqExamQuestItem;
import com.fsll.wmes.entity.RpqExamQuestItemEn;
import com.fsll.wmes.entity.RpqExamQuestItemSc;
import com.fsll.wmes.entity.RpqExamQuestItemTc;
import com.fsll.wmes.entity.RpqExamQuestSc;
import com.fsll.wmes.entity.RpqExamQuestTc;
import com.fsll.wmes.entity.RpqExamSc;
import com.fsll.wmes.entity.RpqExamTc;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageEn;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqPageSc;
import com.fsll.wmes.entity.RpqPageTc;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.RpqQuestItemEn;
import com.fsll.wmes.entity.RpqQuestItemSc;
import com.fsll.wmes.entity.RpqQuestItemTc;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.rpq.vo.RpqListVO;
/***
 * 业务接口类：开户管理
 * @author tejay zhu
 * @date 2016-7-7
 */
public interface RpqService {
	
	// saveOrUpdate -------------
	public InvestorDoc saveOrUpdateFileToInvestorDoc(InvestorDoc investorDoc);
	public AccessoryFile saveOrUpdateFileToAccessoryFile(AccessoryFile accessoryFile);
	public InvestorAccountDeclaration saveOrUpdateInvestorAccountDeclarationAgeas( InvestorAccountDeclaration ageas );
	public RpqExam saveOrUpdateRpqExam(RpqExam obj);
	public RpqExamAnswer saveOrUpdateRpqExamAnswer(RpqExamAnswer obj);
	public RpqExamQuest saveOrUpdateRpqExamQuest(RpqExamQuest obj);
	public RpqExamQuestItem saveOrUpdateRpqExamQuestItem(RpqExamQuestItem obj);
	// saveOrUpdate -------------
	
	// delete -------------
	public void deleteAccessoryFile( AccessoryFile accessoryFile );
	public void deleteInvestorDoc( InvestorDoc investorDoc );	
	// delete -------------
	
	// find list -------------
	public RpqPageLevel findRpqPageLevel( String pageId, int score, String status, String isValid,String langCode );
	//查询等于少于指定分数的问卷评级
	public List<RpqPageLevel> findRpqPageLevelByScore( String pageId, int score, String status, String isValid,int riskLevel,String langCode );
	public List<DocListVO> findDocumentCheckList( String distributorId, String clientType, String langCode, MemberBase loginUser );
	public InvestorAccountDeclaration findInvestorAccountDeclarationAgeasForAccount( String accountId );
	public List<RpqQuestItem> findRpqQuestItemList(String questId,String landCode);
	public List<RpqPageQuest> findRpqPageQuest( String pageId);
	public List<RpqPage> findRpqPage( String type, String distributorId, String clientType, String langCode, String isDef, String pageType );
	public List<RpqListVO> findRpqQuestList( String type, String distributorId, String clientType, String langCode, String isDef, String pageType );
	//public List<RpqExam> findRpqExam( String id, String memberId, String title, String type, String fromMemberId, String distributorId, int totalScore, int riskLevel, String remark, String status, String isValid );
	public List<RpqExamAnswer> findRpqExamAnswer(String id, String examId, String questId, String itemId);
	public List<RpqExamQuest> findRpqExamQuest(String id, String exam, String title, String remark);
	public List<RpqExamQuestItem> findRpqExamQuestItem(String id, String questId, String type, String title, int scoreValue, String remark);
	// find list -------------
	
	
	// get entity -------------
	public InvestorAccount getInvestorAccount(String id);
	public RpqPage getRpqPage(String id);
	public RpqPageEn getRpqPageEn(String id);
	public RpqPageSc getRpqPageSc(String id);
	public RpqPageTc getRpqPageTc(String id);
	public RpqPageLevel getRpqPageLevel(String id);
	public RpqQuest getRpqQuest(String id);
	public RpqQuestItem getRpqQuestItem(String id);
	public MemberDistributor getMemberDistributor(String id);
	public DocTemplate getDocTemplate(String id);
	public InvestorAccountDeclaration getInvestorAccountDeclarationAgeas( String id );
	public AccessoryFile getAccessoryFile(String id);
	public InvestorDoc getInvestorDoc(String id);
	public RpqExam getRpqExam(String id);
	//返回Exam问卷风险等级
	public RpqExam getRpqExamRiskLevel(String id,String loginLangFlag);
	public RpqExamAnswer getRpqExamAnswer(String id);
	public RpqExamQuest getRpqExamQuest(String id);
	public RpqExamQuestItem getRpqExamQuestItem(String id);
	public RpqQuest loadRpqQuestionById(String questionId,String langCode);
	public RpqQuestItemEn getRpqQuestItemEn(String id);
	public RpqQuestItemTc getRpqQuestItemTc(String id);
	public RpqQuestItemSc getRpqQuestItemSc(String id);
	// get entity -------------
	
//saveORupdate entity
	public RpqExamEn saveOrUpdateExamEn(RpqExamEn examEn);
	public RpqExamTc saveOrUpdateExamTc(RpqExamTc examTc);
	public RpqExamSc saveOrUpdateExamSc(RpqExamSc examSc);
	public RpqExamQuestSc saveOrUpdateRpqExamQuestSc(RpqExamQuestSc examQuestionSc);
	public RpqExamQuestTc saveOrUpdateRpqExamQuestTc(RpqExamQuestTc examQuestionTc);
	public RpqExamQuestEn saveOrUpdateExamQuestSc(RpqExamQuestEn examQuestionEn);
	public RpqExamQuestItemEn saveOrUpdateRpqExamQuestItemEn(
			RpqExamQuestItemEn examQuestItemEn);
	public RpqExamQuestItemTc saveOrUpdateRpqExamQuestItemTc(
			RpqExamQuestItemTc examQuestItemTc);
	public RpqExamQuestItemSc saveOrUpdateRpqExamQuestItemSc(
			RpqExamQuestItemSc examQuestItemSc);
	public List<RpqListVO> findExamList(String examId, String loginLangFlag);
//用户风险评级（提交后）
	public RpqPageLevel loadRpqLevelMsg(String examId, String loginLangFlag);
	/**根据问卷和问题，返回原答案
	 * @param examId
	 * @param questionId
	 * @return
	 */
	public RpqExamAnswer getRpqExamAns(String examId, String questionId);
	
	/**
	 * 获取已填写的RPQExam
	 * @author scshi
	 * @date 20160811
	 * @param accountId 当前账户
	 * @param moduleType 对应模块,O开户,I投资方案,K文档审查的rpq,其他类型自行扩展
	 * */
	public List<RpqListVO> findRpqExamList(String accountId, String moduleType,String langCode);
	
	/**
	 * 根据投资人id,代理商id获取问卷
	 * @author qgfeng
	 * @param accountId 投资人id
	 * @param distributorId 代理商id
	 * @param module_type 类型  ：O:开户rpq I:投资方案的rpq,K:KYC rpq
	 * @param langCode 
	 */
	public RpqListVO getRpqExamByMember(String accountId,String distributorId,String moduleType,String langCode);

	/**
	 * 从模板复制问卷到rpqExam
	 * @author scshi
	 * @param distributorId 代理商
	 * @param clientType 适用客户,Individual,Corporate,FI多个之间用,分隔'
	 * @param pageType '此问题适用类型, O:开户rpq I:投资方案的rpq,K:KYC中使用的rpq,
	 * */
	public List<RpqListVO> copyExamFromPage(String distributorId,String clientType, 
			String pageType,HttpServletRequest request,String accountId);
	/**
	 * 评级结果多语言保存
	 * @author scshi
	 * */
	public void saveOrUpdateRpqExamLanguage(RpqPageLevel levelSc,
											RpqPageLevel levelTc, 
											RpqPageLevel levelEn,
											String examId);
	/**
	 * 保存投资人答题得分及投资风险等级
	 * @param levelId 分值等级
	 * @param userLevelId 用户选择等级
	 * @param examId 
	 * @param score 总分
	 * @param langCode 语音
	 */
	public RpqExam saveCompleteExam(String levelId,String userLevelId,String examId,int score,String langCode);


	/**
	 * 根据风险等级获取结果
	 * @param rpqExamId 答卷id
	 * @param langCode
	 * @param rpqLevel
	 */
	public String getRiskLevelResult(String rpqExamId,Integer riskLevel, String langCode);


}
