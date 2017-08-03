/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.rpq.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.service.CoreBaseService;
import com.fsll.wmes.entity.DocList;
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
import com.fsll.wmes.entity.RpqPageLevelEn;
import com.fsll.wmes.entity.RpqPageLevelSc;
import com.fsll.wmes.entity.RpqPageLevelTc;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqPageSc;
import com.fsll.wmes.entity.RpqPageTc;
import com.fsll.wmes.entity.RpqPageType;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.RpqQuestItemEn;
import com.fsll.wmes.entity.RpqQuestItemSc;
import com.fsll.wmes.entity.RpqQuestItemTc;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.rpq.service.RpqService;
import com.fsll.wmes.rpq.vo.ExamQuestionAndItemsVo;
import com.fsll.wmes.rpq.vo.RpqExamQuestionItemVo;
import com.fsll.wmes.rpq.vo.RpqListVO;
/***
 * 业务接口实现类：开户管理
 * @author tejay zhu 
 * @date 2016-7-7
 */
@Service("rpqService")
//@Transactional
public class RpqServiceImpl extends BaseService implements RpqService {
	
	@Autowired
	private CoreBaseService coreBaseService;

// saveOrUpdate -------------
	public RpqExam saveOrUpdateRpqExam(RpqExam obj) {
		if (null == obj.getId() || obj.getId().trim().length() < 1) {
			obj.setId(null);
			obj.setCreateTime(new Date());
			obj.setLastUpdate(new Date());
			obj = (RpqExam) baseDao.create(obj);
		} else {
			obj = (RpqExam) baseDao.update(obj);
		}
		return obj;
	}

	public RpqExamAnswer saveOrUpdateRpqExamAnswer(RpqExamAnswer obj) {
		if (null == obj.getId() || obj.getId().trim().length() < 1) {
			obj.setId(null);
			obj = (RpqExamAnswer) baseDao.create(obj);
		} else {
			obj = (RpqExamAnswer) baseDao.update(obj);
		}
		return obj;
	}

	public RpqExamQuest saveOrUpdateRpqExamQuest(RpqExamQuest obj) {
		if (null == obj.getId() || obj.getId().trim().length() < 1) {
			obj.setId(null);
			obj = (RpqExamQuest) baseDao.create(obj);
		} else {
			obj = (RpqExamQuest) baseDao.update(obj);
		}
		return obj;
	}

	public RpqExamQuestItem saveOrUpdateRpqExamQuestItem(RpqExamQuestItem obj) {
		if (null == obj.getId() || obj.getId().trim().length() < 1) {
			obj.setId(null);
			obj = (RpqExamQuestItem) baseDao.create(obj);
		} else {
			obj = (RpqExamQuestItem) baseDao.update(obj);
		}
		return obj;
	}
	
	
	/**
	 * @author tejay zhu
	 * 保存附件记录到 AccessoryFile表
	 */
	@Override
	public InvestorDoc saveOrUpdateFileToInvestorDoc(InvestorDoc investorDoc) {
		if ( null == investorDoc.getId() || investorDoc.getId().trim().length() < 1 ) {
			investorDoc.setId(null);
			investorDoc.setCreateTime(new Date());
			investorDoc.setSubmitDate(new Date());
			investorDoc.setLastUpdate(new Date());
			investorDoc = (InvestorDoc) baseDao.create(investorDoc);
		} else {
			investorDoc = (InvestorDoc) baseDao.update(investorDoc);
		}
		
		return investorDoc;
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	public AccessoryFile saveOrUpdateFileToAccessoryFile(AccessoryFile accessoryFile) {
		if ( null == accessoryFile.getId() || accessoryFile.getId().trim().length() < 1 ) {
			accessoryFile.setId(null);
			accessoryFile.setCreateTime(new Date());
			accessoryFile = (AccessoryFile) baseDao.create(accessoryFile);
		} else {
			accessoryFile = (AccessoryFile) baseDao.update(accessoryFile);
		}
		return accessoryFile;
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	public InvestorAccountDeclaration saveOrUpdateInvestorAccountDeclarationAgeas(InvestorAccountDeclaration ageas) {
		InvestorAccount investorAccount = new InvestorAccount();
		investorAccount.setId("tejay");
		
		ageas.setLastUpdate(new Date());
		ageas.setAccount(investorAccount);
		
		InvestorAccountDeclaration ageasPr = null;
		if ( null == ageas.getId() || ageas.getId().trim().length() < 1 ) {
			ageas.setId(null);
			ageas.setCreateTime(new Date());
			ageasPr = (InvestorAccountDeclaration)baseDao.create(ageas);
		}else{
			ageasPr = (InvestorAccountDeclaration)baseDao.update(ageas);
		}
		return ageasPr;
	}
	
	/**
	 * 评级结果多语言保存
	 * @author scshi
	 * */
	@Override
	public void saveOrUpdateRpqExamLanguage(RpqPageLevel levelSc,
			RpqPageLevel levelTc, 
			RpqPageLevel levelEn,
			String examId) {
		
		RpqExamSc examsc = (RpqExamSc)this.baseDao.get(RpqExamSc.class, examId);
		if(null!=levelSc && null!=examsc){
			examsc.setRiskResult(levelSc.getResult());
			this.baseDao.saveOrUpdate(examsc);
		}
		
		RpqExamTc examTc = (RpqExamTc)this.baseDao.get(RpqExamTc.class, examId);
		if(null!=levelTc && null!=examTc){
			examTc.setRiskResult(levelTc.getResult());
			this.baseDao.saveOrUpdate(examTc);
		}
		
		RpqExamEn examEn = (RpqExamEn)this.baseDao.get(RpqExamEn.class, examId);
		if(null!=levelEn && null!=examEn){
			examEn.setRiskResult(levelEn.getResult());
			this.baseDao.saveOrUpdate(examEn);
		}
		
		
	}	
// saveOrUpdate -------------
	
	

	
// delete -------------
	/**
	 * @author tejay zhu
	 */
	@Override
	public void deleteAccessoryFile(AccessoryFile accessoryFile) {
		this.baseDao.delete(accessoryFile);
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	public void deleteInvestorDoc(InvestorDoc investorDoc) {
		this.baseDao.delete(investorDoc);
	}	
	
	
// delete -------------
	

	
// find list -------------
	/**
	 * @author tejay zhu
	 * 总分范围在 0 ~ 100
	 */
	@Override
	public RpqPageLevel findRpqPageLevel(String pageId, Integer score, String status, String isValid,String loginLangFlag) {
		if(score==null){
			score = 0;
		}
		String hql ="select t.id,out.remark,out.result ";
		hql += " from RpqPageLevel t inner join "+this.getLangString("RpqPageLevel", loginLangFlag);
		hql += " out on t.id=out.id ";
		hql += " where "+score+" BETWEEN t.beginScore and t.endScore and t.page.id = ? and t.status = ? and t.isValid = ? ";
		List<String> params = new ArrayList<String>();
		params.add( pageId );
		params.add( status );
		params.add( isValid );
		List levels = this.baseDao.find(hql, params.toArray(), false);
		List<RpqPageLevel> voList = new ArrayList();
		if ( null != levels && !levels.isEmpty() ) {
			for(int x=0;x<levels.size();x++){
				Object[] objs = (Object[])levels.get(x);
				String levelID = objs[0]==null?"":objs[0].toString();
				if(!"".equals(levelID)){
					RpqPageLevel level = (RpqPageLevel)this.baseDao.get(RpqPageLevel.class, levelID);
					level.setRemark(objs[1]==null?"":objs[1].toString());
					level.setResult(objs[2]==null?"":objs[2].toString());
					voList.add(level);
				}
			}
			return voList.get(0);
		}else{
			return new RpqPageLevel();
		}
	}
	
	/**
	 * 查询等于少于指定分数的问卷评级
	 */
	@Override
	public List<RpqPageLevel> findRpqPageLevelByScore(String pageId, Integer score,
			String status, String isValid,Integer riskLevel, String langCode) {
		if(score==null){
			score = 0;
		}
		List<Object> params = new ArrayList<Object>();
		String hql ="select t.id,out.remark,out.result ";
		hql += " from RpqPageLevel t inner join "+this.getLangString("RpqPageLevel", langCode);
		hql += " out on t.id=out.id ";
		hql += " where 1=1 " ;
		hql += " AND "+score+" BETWEEN t.beginScore and t.endScore ";
		hql += " and t.page.id = ? and t.status = ? and t.isValid = ?";
		params.add( pageId );
		params.add( status );
		params.add( isValid );
		if(null != riskLevel){
			hql += " and  t.riskLevel <= ? ";
			params.add(riskLevel);
		}
		hql += " order by t.riskLevel desc ";
		List levels = this.baseDao.find(hql, params.toArray(), false);
		List<RpqPageLevel> voList = new ArrayList();
		if ( null != levels && !levels.isEmpty() ) {
			for(int x=0;x<levels.size();x++){
				Object[] objs = (Object[])levels.get(x);
				String levelID = objs[0]==null?"":objs[0].toString();
				if(!"".equals(levelID)){
					RpqPageLevel level = (RpqPageLevel)this.baseDao.get(RpqPageLevel.class, levelID);
					level.setRemark(objs[1]==null?"":objs[1].toString());
					level.setResult(objs[2]==null?"":objs[2].toString());
					voList.add(level);
				}
			}
			return voList;
		}else{
			return null;
		}
	}
	
	/**
	 * 查询等于少于指定风险等级的问卷评级
	 */
	@Override
	public List<RpqPageLevel> findRpqPageLevelList(String pageId, String status, String isValid
			, Integer riskLevel, String langCode) {
		List<Object> params = new ArrayList<Object>();
		String hql ="select t.id,out.remark,out.result ";
		hql += " from RpqPageLevel t inner join "+this.getLangString("RpqPageLevel", langCode);
		hql += " out on t.id=out.id ";
		hql += " where 1=1 " ;
		hql += " and t.page.id = ? and t.status = ? and t.isValid = ?";
		params.add( pageId );
		params.add( status );
		params.add( isValid );
		if(null != riskLevel){
			hql += " and  t.riskLevel <= ? ";
			params.add(riskLevel);
		}
		hql += " order by t.riskLevel desc ";
		List levels = this.baseDao.find(hql, params.toArray(), false);
		List<RpqPageLevel> voList = new ArrayList();
		if ( null != levels && !levels.isEmpty() ) {
			for(int x=0;x<levels.size();x++){
				Object[] objs = (Object[])levels.get(x);
				String levelID = objs[0]==null?"":objs[0].toString();
				if(!"".equals(levelID)){
					RpqPageLevel level = (RpqPageLevel)this.baseDao.get(RpqPageLevel.class, levelID);
					level.setRemark(objs[1]==null?"":objs[1].toString());
					level.setResult(objs[2]==null?"":objs[2].toString());
					voList.add(level);
				}
			}
			return voList;
		}else{
			return null;
		}
	}
	
	/**
	 * @author tejay zhu
	 * @editor scshi 160810
	 * @param distributorId
	 * @param clientType
	 * @return
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<DocListVO> findDocumentCheckList( String distributorId, String clientType, String langCode, MemberBase loginUser ) {
		List<DocListVO> docListVOList = new ArrayList<DocListVO>();
		List<DocList> docLists = new ArrayList<DocList>();
		List<InvestorDoc> investorDocList = new ArrayList<InvestorDoc>();
		
		String docTemplateHql = "from DocTemplate dt WHERE dt.isValid = 1 and dt.distributor.id = ? and dt.clientType = ? and dt.langCode = ? ";
		List<String> docTemplateParams = new ArrayList<String>();
		docTemplateParams.add(distributorId);
		docTemplateParams.add(clientType);
		docTemplateParams.add(langCode);
		List<DocTemplate> docTemplatesList = this.baseDao.find(docTemplateHql, docTemplateParams.toArray(), false);
		
		if ( null != docTemplatesList && !docTemplatesList.isEmpty() ) {
			String docListHql = "from DocList t where t.template.id = ? ORDER BY t.id";
			List<String> docListParams = new ArrayList<String>();
			docListParams.add( docTemplatesList.get(0).getId() );
			docLists = this.baseDao.find(docListHql, docListParams.toArray(), false);
			if ( null != docLists && !docLists.isEmpty() ) {
				
				// 转换是否为必须上传项显示内容为文字
				for (int i = 0; i < docLists.size(); i++) {
					
					//获取当前语言下文档名称与描述
					String docName = "";
					String docDesc = "";
					String hql = "select t.docName,t.remark from "+this.getLangString("DocList", langCode)+" where t.id=? ";
					List docListExt = this.baseDao.find(hql, new String[]{docLists.get(i).getId()}, false);
					if(!docListExt.isEmpty()){
						Object[] objs = (Object[])docListExt.get(0);
						docName = objs[0]==null?"":objs[0].toString();
						docDesc = objs[1]==null?"":objs[1].toString();
					}
					
					String investorDocHql = "from InvestorDoc t where t.distributor.id = ? and t.docTemplate.id = ? "
							+ "and t.docName = ? and t.isNecessary = ? and t.isValid = ? ";
					List<String> investorDocParams = new ArrayList<String>();
					investorDocParams.add(distributorId);
					investorDocParams.add( docLists.get(i).getTemplate().getId() );
					investorDocParams.add( docName );
					investorDocParams.add( docLists.get(i).getIsNecessary() );
					investorDocParams.add("1");
					investorDocList = this.baseDao.find(investorDocHql, investorDocParams.toArray(), false);
					DocListVO docListVO = new DocListVO();
					docListVO.setId( docLists.get(i).getId() );
					docListVO.setTemplateId(docLists.get(i).getTemplate().getId());
					docListVO.setDocName(docName);
					docListVO.setDesc( docDesc );
					docListVO.setUpdateCycle( docLists.get(i).getUpdateCycle() );
					docListVO.setEffectDate( docLists.get(i).getEffectDate() );
					docListVO.setCreateTime( docLists.get(i).getCreateTime() );
					docListVO.setIsValid( docLists.get(i).getIsValid() );
					if ( "1".equalsIgnoreCase( docLists.get(i).getIsNecessary() ) ) {
						docListVO.setIsNecessary("YES");
					}else{
						docListVO.setIsNecessary("NO");
					}
					if( null != investorDocList && !investorDocList.isEmpty() ){
						String accessoryFileHql = "from AccessoryFile t where t.moduleType = ? and t.relateId = ?";
						List<String> accessoryFileParams = new ArrayList<String>();
						accessoryFileParams.add( "investor_doc" );
						accessoryFileParams.add( investorDocList.get(0).getId() );
						List<AccessoryFile> files = this.baseDao.find(accessoryFileHql, accessoryFileParams.toArray(), false);
//						System.out.println( "** AccessoryFile files:"+files.size() + " <> investorDocList.get(0).getId():"+investorDocList.get(0).getId() );
						if ( null != files && !files.isEmpty() ) {
							docListVO.setAccessoryFileId( files.get(0).getId() );
							docListVO.setAccessoryFileName( files.get(0).getFileName() );
							docListVO.setAccessoryFileCreateTime( files.get(0).getCreateTime() );
						}else{
							docListVO.setAccessoryFileId("");
							docListVO.setAccessoryFileName("");
							docListVO.setAccessoryFileCreateTime(new Date());							
				}
					}else{
						docListVO.setAccessoryFileId("");
						docListVO.setAccessoryFileName("");
						docListVO.setAccessoryFileCreateTime(new Date());
			}
					docListVOList.add(docListVO);
		}
			}
		}
		
		return docListVOList;
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	//@Transactional(readOnly=true)
	public InvestorAccountDeclaration findInvestorAccountDeclarationAgeasForAccount(String accountId) {
		String hql = "from InvestorAccountDeclarationAgeas t where t.account.id = ?";
		List<String> params = new ArrayList<String>();
		params.add(accountId);
		List<InvestorAccountDeclaration> ageas = this.baseDao.find(hql, params.toArray(), false);
		if ( null != ageas && !ageas.isEmpty() ) {
			return ageas.get(0);
		}else{
			InvestorAccountDeclaration ageasForNew = new InvestorAccountDeclaration();
			ageasForNew.setId("");
			ageasForNew.setDeclarationFlag("0");
			ageasForNew.setInformationFlag("0");
			ageasForNew.setAimFlag("0");
			ageasForNew.setAdvisorFlag("0");
			ageasForNew.setQualifiedFlag("0");
			return ageasForNew;
		}
		
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<RpqQuestItem> findRpqQuestItemList(String questId,String langCode){
		String hql = "select t.id,out.title,out.remark ";
		hql += " from RpqQuestItem t inner join "+this.getLangString("RpqQuestItem", langCode);
		hql += " out on t.id=out.id ";
		hql += " where t.quest.id = ? order by t.orderBy ";
		 
		List<String> params = new ArrayList<String>();
		params.add(questId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		List<RpqQuestItem> voList = new ArrayList<RpqQuestItem>();
		if(!list.isEmpty()){
			for(int z=0;z<list.size();z++){
				Object[] objs = (Object[])list.get(z);
				RpqQuestItem questionItem = new RpqQuestItem();
				String questionItemId = objs[0]==null?"":objs[0].toString();
				if(!"".equals(questionItemId)){
					questionItem = (RpqQuestItem)this.baseDao.get(RpqQuestItem.class, questionItemId);
					questionItem.setTitle(objs[1]==null?"":objs[1].toString());
					questionItem.setRemark(objs[2]==null?"":objs[2].toString());
				}
				voList.add(questionItem);
			}
		}
		return voList;
	}
	
	public RpqQuest  loadRpqQuestionById(String questionId,String langCode){
		RpqQuest question = (RpqQuest)this.baseDao.get(RpqQuest.class, questionId);
		
		String hql = "select t.title,t.remark,t.id ";
		hql += " from "+this.getLangString("RpqQuest", langCode);
		hql += " t where t.id=? ";
		
		List<String> params = new ArrayList<String>();
		params.add(questionId);
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			Object[] objs = (Object[])voList.get(0);
			question.setTitle(objs[0]==null?"":objs[0].toString());
			question.setRemark(objs[1]==null?"":objs[1].toString());
			question.setId(objs[2]==null?"":objs[2].toString());
		}
		return question;
	}
	

	/**
	 * @author tejay zhu
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<RpqPageQuest> findRpqPageQuest(String pageId) {
		String hql = " from RpqPageQuest t where t.page.id = ? order by t.orderBy";
		List<String> params = new ArrayList<String>();
		params.add( pageId );
		List<RpqPageQuest> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	@Override
	public List<RpqQuest> findRpqQuestList(String pageId, String langCode) {
		List<Object> params = new ArrayList<Object>();
		String hql = "select q.id,lang.title,lang.remark";
		hql += " from RpqPageQuest t left join RpqQuest q on t.quest.id=q.id ";
		hql += " inner join "+this.getLangString("RpqQuest", langCode)+" lang on q.id=lang.id ";
		hql += " where t.page.id = ? order by t.orderBy ";
		params.add( pageId );
		List voList = this.baseDao.find(hql, params.toArray(), false);
		List<RpqQuest> list = new ArrayList<RpqQuest>();
		if(voList!=null && !voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				Object[] objs = (Object[])voList.get(x);
				RpqQuest quest = new RpqQuest();
				String questId = objs[0]==null?"":objs[0].toString();
				if(StringUtils.isNotBlank(questId)){
					quest = (RpqQuest)this.baseDao.get(RpqQuest.class, questId);
					quest.setTitle(objs[1]==null?"":objs[1].toString());
					quest.setRemark(objs[2]==null?"":objs[2].toString());
				}
				list.add(quest);
			}
		}
		return list;
	}
	
	/**
	 * 查询问卷题库
	 * @author qgfeng
	 * @param type 类型,1:代理商,2:ifa或者ifafirm,0:平台,sys_admin
	 * @param distributorId 代理商id
	 * @param clientType:适用客户,Individual,Corporate,FI多个之间用,分隔
	 * @param pageType :此问题适用类型, O:开户rpq I:投资方案的rpq,K:KYC中使用的rpq,其他类型自行扩展
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<RpqPage> findRpqPage( String type, String distributorId, String clientType, String langCode, String pageType ){
		List<Object> params = new ArrayList<Object>();
		String hql = "select p.id,lang.title,lang.remark ";
		hql += " from RpqPageType t left join RpqPage p on t.rpqPage.id = p.id ";
		hql += " inner join "+this.getLangString("RpqPage", langCode)+" lang on p.id=lang.id ";
		hql += "where p.isValid = '1' and t.distributor.id = ? and t.clientType = ? and t.pageType = ? ";
		params.add(distributorId);
		params.add(clientType);
		params.add(pageType);
		if(StringUtils.isNotBlank(type)){
			hql+=(" and t.type=? ");
			params.add(type);
		}
		List voList = this.baseDao.find(hql, params.toArray(), false);
		List<RpqPage> list = new ArrayList<RpqPage>();
		if(voList!=null && !voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				Object[] objs = (Object[])voList.get(x);
				RpqPage page = new RpqPage();
				String pageId = objs[0]==null?"":objs[0].toString();
				if(StringUtils.isNotBlank(pageId)){
					page = (RpqPage)this.baseDao.get(RpqPage.class, pageId);
					page = (RpqPage)this.baseDao.get(RpqPage.class, pageId);
					page.setTitle(objs[1]==null?"":objs[1].toString());
					page.setRemark(objs[2]==null?"":objs[2].toString());
				}
				list.add(page);
			}
		}
		return list;
	}
	
	/**
	 * @author tejay zhu
	 * @editor scshi 20161114
	 * 查出问卷中的问题及问题对应的选项
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<RpqListVO> findRpqQuestList(String type, String distributorId, String clientType, 
			String langCode, String pageType) {
		// 返回的结果list
		List<RpqListVO> rpqListVOs = new ArrayList<RpqListVO>();
		// 确定使用的问卷
		List<RpqPage> rpqPageList = this.findRpqPage(type, distributorId, clientType, langCode, pageType);
		RpqPage rpqPage = null;
		if( null != rpqPageList && !rpqPageList.isEmpty() ){
				rpqPage = rpqPageList.get(0);
				RpqListVO rpqExamVo = new RpqListVO();
				rpqExamVo.setRpqPageid( rpqPage.getId() );
				rpqExamVo.setRpqPageTitle( rpqPage.getTitle() );
				rpqExamVo.setRpqPageRemark( rpqPage.getRemark() );
				rpqExamVo.setType( rpqPage.getType() );
				rpqExamVo.setDistributorId( rpqPage.getDistributor().getId() );
				rpqExamVo.setIsValid( rpqPage.getIsValid() );
				// 确定问卷的问题列表
				List<RpqQuest> rpqQuestList = this.findRpqQuestList(rpqPage.getId(),langCode);
				List<ExamQuestionAndItemsVo> questVoList = null;
				if(rpqQuestList!=null && !rpqQuestList.isEmpty()){
					questVoList = new ArrayList<ExamQuestionAndItemsVo>();
					for (int i = 0; i < rpqQuestList.size(); i++) {
						RpqQuest quest = rpqQuestList.get(i);
						ExamQuestionAndItemsVo questVo = new ExamQuestionAndItemsVo();
						questVo.setQuestId(quest.getId());
						questVo.setQuestTitle(quest.getTitle());
						questVo.setQuestRemark(quest.getRemark());
						List<RpqQuestItem> questItemList = this.findRpqQuestItemList(questVo.getQuestId(), langCode);
						if(questItemList!=null && !questItemList.isEmpty()){
							List<RpqExamQuestionItemVo> questItemVoList = new ArrayList<RpqExamQuestionItemVo>();
							for(int k=0;k<questItemList.size();k++){
								RpqQuestItem item = questItemList.get(k);
								RpqExamQuestionItemVo itemVo = new RpqExamQuestionItemVo();
								itemVo.setId(item.getId());
								itemVo.setTitle(item.getTitle());
								itemVo.setRemark(item.getRemark());
								itemVo.setScoreValue(item.getScoreValue());
								questItemVoList.add(itemVo);
							}
							questVo.setRqpExamItemList(questItemVoList);
						}
						questVoList.add(questVo);
						rpqExamVo.setQuestionList(questVoList);
					}
					rpqListVOs.add(rpqExamVo);
				}	
		}
		return rpqListVOs;
	}
	
/**
 * 获取已填问卷
 * @author scshi
 * */
	//@Transactional(readOnly=true)
	public List<RpqListVO> findExamList(String examId,String langCode){
		// 返回的结果list
		List<RpqListVO> rpqListVOs = new ArrayList<RpqListVO>();
		// 确定原使用的问卷
		List<RpqExam> rpqExamList = this.findRpqExam(examId,langCode);
		
		if(null!=rpqExamList && !rpqExamList.isEmpty()){
			RpqExam examVo = rpqExamList.get(0);
			//查找问题列表
			List<RpqExamQuest> questionList = this.findRpqExamQuest(examId,langCode);
			for(int a=0;a<questionList.size();a++){
				RpqListVO rpqListVO = new RpqListVO();
				rpqListVO.setRpqPageid( examVo.getPageId() );
				rpqListVO.setRpqPageTitle( examVo.getTitle() );
				rpqListVO.setRpqPageRemark( examVo.getRemark() );
				rpqListVO.setType( examVo.getType() );
				rpqListVO.setDistributorId( examVo.getDistributor().getId() );
				
				RpqExamQuest question = questionList.get(a);
				
				rpqListVO.setQuestId( question.getId() );
				rpqListVO.setQuestOrder( question.getOrderBy() );
				rpqListVO.setQuestTitle( (a+1)+"."+ question.getTitle() );
				rpqListVO.setQuestRemark( question.getRemark() );
				
				rpqListVOs.add(rpqListVO);
			}
			
			// 获取各条问题的选项与已选
			RpqListVO rpqListVOForFillItem = null;
			if( null != questionList && !questionList.isEmpty() ){
				for (int i = 0; i < rpqListVOs.size(); i++) {
					rpqListVOForFillItem = rpqListVOs.get(i);
					rpqListVOs.get(i).setRqpExamItemList( this.findExemQuestItemList( rpqListVOForFillItem.getQuestId(),langCode,examId ) );
				}
			}
		}
		return rpqListVOs;
	}
	

	public List<RpqExam>  findRpqExam(String examId,String langCode){
		String hql = "select t.id,out.title,out.remark ";
			hql += " from RpqExam t inner join "+this.getLangString("RpqExam", langCode);
			hql += " out on t.id=out.id ";
			hql += " where t.id=? ";
			List<RpqExam> voList = new ArrayList<RpqExam>();
			List list = this.baseDao.find(hql, new String[]{examId}, false);
			if(!list.isEmpty()){
				for(int t=0;t<list.size();t++){
					Object[] objs = (Object[])list.get(t);
					RpqExam obj = (RpqExam)this.baseDao.get(RpqExam.class, examId);
					obj.setTitle(objs[1]==null?"":objs[1].toString());
					obj.setRemark(objs[2]==null?"":objs[2].toString());
					voList.add(obj);
				}
			}
			return voList;
	}
	
	public List<RpqExam> findRpqExam(String accountId,String moduleType,String langCode ){
		String hql = "select t.id,out.title,out.remark ";
		hql += " from RpqExam t inner join "+this.getLangString("RpqExam", langCode);
		hql += " out on t.id=out.id ";
		hql += " where t.relateId=? and t.moduleType=? order by t.orderBy ";
		
		List<RpqExam> voList = new ArrayList<RpqExam>();
		List list = this.baseDao.find(hql, new String[]{accountId,moduleType}, false);
		if(!list.isEmpty()){
			for(int t=0;t<list.size();t++){
				Object[] objs = (Object[])list.get(t);
				String examId = objs[0]==null?"":objs[0].toString();
				if(!"".equals(examId)){
					RpqExam obj = (RpqExam)this.baseDao.get(RpqExam.class, examId);
					obj.setTitle(objs[1]==null?"":objs[1].toString());
					obj.setRemark(objs[2]==null?"":objs[2].toString());
					voList.add(obj);
				}
			}
		}
		return voList;
	}
	
//查询答卷关联问题	
	public List<RpqExamQuest> findRpqExamQuest(String examId,String langCode) {
		String hql = "select t.id,out.title,out.remark ";
		hql += " from RpqExamQuest t inner join "+this.getLangString("RpqExamQuest", langCode);
		hql += " out on t.id=out.id ";
		hql += " where t.exam.id=? order by t.orderBy ";
		List<RpqExamQuest> voList = new ArrayList<RpqExamQuest>();
		List list = this.baseDao.find(hql, new String[]{examId}, false);
		if(!list.isEmpty()){
			for(int k=0;k<list.size();k++){
				Object[] objs = (Object[])list.get(k);
				String examQuestionId = objs[0]==null?"":objs[0].toString();
				if(!"".equals(examQuestionId)){
					RpqExamQuest question = (RpqExamQuest)this.baseDao.get(RpqExamQuest.class, examQuestionId);
					question.setTitle(objs[1]==null?"":objs[1].toString());
					question.setRemark(objs[2]==null?"":objs[2].toString());
					voList.add(question);
				}
			}
		}
		return voList;
	}
	
//	查询答卷关联问题的选项与已选标记
	public List<RpqExamQuestionItemVo> findExemQuestItemList(String questionId,String langCode,String examId){
		String hql = "select t.id,out.title,out.remark ";
			hql += " from RpqExamQuestItem t inner join "+this.getLangString("RpqExamQuestItem", langCode);
			hql += " out on t.id=out.id ";
			hql += " where t.quest.id=? order by t.orderBy ";
			List<RpqExamQuestionItemVo> voList = new ArrayList<RpqExamQuestionItemVo>();
			List list = this.baseDao.find(hql, new String[]{questionId}, false);
			if(!list.isEmpty()){
				for(int x=0;x<list.size();x++){
					RpqExamQuestionItemVo vo = new RpqExamQuestionItemVo();
					Object[] objs = (Object[])list.get(x);
					String itemId = objs[0]==null?"":objs[0].toString();
					if(!"".equals(itemId)){
						vo.setId(itemId);
						RpqExamQuestItem obj = (RpqExamQuestItem)this.baseDao.get(RpqExamQuestItem.class, itemId);
						vo.setOrderBy(obj.getOrderBy());
						vo.setRemark(obj.getRemark());
						vo.setScoreValue(obj.getScoreValue());
						vo.setTitle(objs[1]==null?"":objs[1].toString());
						vo.setRemark(objs[2]==null?"":objs[2].toString());
						String checkHql = "from RpqExamAnswer t where t.exam.id=? and t.quest.id=? and t.item.id=? ";
						List checkList  = this.baseDao.find(checkHql, new String[]{examId,questionId,itemId}, false);
						if(checkList.isEmpty()){
							vo.setCheckFlag("0");
						}else{
							vo.setCheckFlag("1");
						}
					}
					voList.add(vo);
				}
			}
			return voList;
		
	}

	/**
	 * 获取已填写的RPQExam
	 * @author scshi
	 * @date 20160811
	 * @param accountId 当前账户
	 * @param module_type 对应模块,O开户,I投资方案,K文档审查的rpq,其他类型自行扩展
	 * */
	@Override
	public List<RpqListVO> findRpqExamList(String accountId, String moduleType,String langCode) {
		// 返回的结果list
		// 确定原使用的问卷
		List<RpqExam> rpqExamList = this.findRpqExam(accountId,moduleType,langCode);
		List<RpqListVO> rpqListVOs = new ArrayList<RpqListVO>();
		if(null!=rpqExamList && !rpqExamList.isEmpty()){
			for(int x=0;x<rpqExamList.size();x++){
				RpqExam examVo = rpqExamList.get(x);
				RpqListVO rpqListVO = new RpqListVO();
				rpqListVO.setRpqPageid( examVo.getPageId() );
				rpqListVO.setRpqPageTitle( examVo.getTitle() );
				rpqListVO.setRpqPageRemark( examVo.getRemark() );
				rpqListVO.setType( examVo.getType() );
				rpqListVO.setDistributorId( examVo.getDistributor().getId() );
				rpqListVO.setIsCalScore(examVo.getIsCalScore());
				rpqListVO.setExamId(examVo.getId());
				
				//查找问题列表
				List<ExamQuestionAndItemsVo> questionVoList = new ArrayList<ExamQuestionAndItemsVo>();
				List<RpqExamQuest> questionList = this.findRpqExamQuest(examVo.getId(),langCode);
				for(int a=0;a<questionList.size();a++){
					RpqExamQuest question = questionList.get(a);
					ExamQuestionAndItemsVo questionVo = new ExamQuestionAndItemsVo();
					questionVo.setQuestId( question.getId() );
					questionVo.setQuestOrder( question.getOrderBy() );
					questionVo.setQuestTitle( (a+1)+"."+ question.getTitle() );
					questionVo.setQuestRemark( question.getRemark() );
					// 获取各条问题的选项与已选
					questionVo.setRqpExamItemList( this.findExemQuestItemList(question.getId(),langCode,examVo.getId()) );
					questionVoList.add(questionVo);
				}
				rpqListVO.setQuestionList(questionVoList);
				
				//问卷评级
				RpqPageLevel level = this.loadRpqLevelMsg(examVo.getId(),langCode);
				rpqListVO.setLevel(level);
				
				rpqListVOs.add(rpqListVO);
			}
		}
		
		return rpqListVOs;
	}
	/**
	 * 投资人获取问卷进行答卷（从rpgPage题库拷贝至exam）
	 * @author qgfeng
	 * @param memberId 投资人id
	 * @param distributorId 代理商id
	 * @param moduleType 类型  ：O:开户rpq I:投资方案的rpq,K:KYC rpq
	 */
	@Override
	public RpqExam saveExamFromPage(String distributorId,String clientType,
			String pageType,HttpServletRequest request) {
		String hql = "from RpqPageType t where t.distributor.id=? and t.clientType=? and t.pageType=? order by t.orderBy desc ";
		List<String> params = new ArrayList<String>();
		params.add(distributorId);
		params.add(clientType);
		params.add(pageType);
		List<RpqPageType> list = this.baseDao.find(hql, params.toArray(), false);
		RpqExam exam = null;
		if(null!=list && !list.isEmpty()){
			MemberBase loginUser = this.getLoginUser(request);
			//保存问卷
			RpqPageType rpqPageType = list.get(0);
			String accountId = request.getParameter("accountId");
			exam = this.saveOrUpdateRpqExam(rpqPageType,loginUser,accountId);
		}
		return exam;
	}
	
	@Override
	public RpqListVO getRpqExamVoById(String examId, String langCode) {
		//List<RpqExam> list = findRpqExam(examId, langCode);
		RpqExam rpqExam = (RpqExam) coreBaseService.get(RpqExam.class, examId);
		/*if(list!=null && !list.isEmpty()){
			rpqExam = list.get(0);
		}*/
		RpqListVO rpqListVO = null;
		if(rpqExam != null){
			String remark = null;
			String title = null;
			if(CommonConstants.LANG_CODE_SC.equals(langCode)){
				RpqExamSc examSc = (RpqExamSc) coreBaseService.get(RpqExamSc.class, rpqExam.getId());
				title = examSc.getTitle();
				remark = examSc.getRemark();
			}else if(CommonConstants.LANG_CODE_TC.equals(langCode)){
				RpqExamTc examTc = (RpqExamTc) coreBaseService.get(RpqExamTc.class, rpqExam.getId());
				title = examTc.getTitle();
				remark = examTc.getRemark();
			}else if(CommonConstants.LANG_CODE_EN.equals(langCode)){
				RpqExamEn examEn = (RpqExamEn) coreBaseService.get(RpqExamEn.class, rpqExam.getId());
				title = examEn.getTitle();
				remark = examEn.getRemark();
			}
			rpqListVO = new RpqListVO();
			rpqListVO.setRpqPageid( rpqExam.getPageId() );
			rpqListVO.setRpqPageTitle( title );
			rpqListVO.setRpqPageRemark( remark );
			rpqListVO.setType( rpqExam.getType() );
			rpqListVO.setDistributorId( rpqExam.getDistributor().getId() );
			rpqListVO.setIsCalScore(rpqExam.getIsCalScore());
			rpqListVO.setExamId(rpqExam.getId());
			rpqListVO.setRiskLevel(rpqExam.getUserRiskLevel());
			rpqListVO.setUserRiskLevel(rpqExam.getUserRiskLevel());
			rpqListVO.setTotalScore(rpqExam.getTotalScore());
			//查找问题列表
			List<ExamQuestionAndItemsVo> questionVoList = new ArrayList<ExamQuestionAndItemsVo>();
			List<RpqExamQuest> questionList = this.findRpqExamQuest(rpqExam.getId(),langCode);
			for(int a=0;a<questionList.size();a++){
				RpqExamQuest question = questionList.get(a);
				ExamQuestionAndItemsVo questionVo = new ExamQuestionAndItemsVo();
				questionVo.setQuestId( question.getId() );
				questionVo.setQuestOrder( question.getOrderBy() );
				questionVo.setQuestTitle( (a+1)+"."+ question.getTitle() );
				questionVo.setQuestRemark( question.getRemark() );
				// 获取各条问题的选项与已选
				questionVo.setRqpExamItemList( this.findExemQuestItemList(question.getId(),langCode,rpqExam.getId()) );
				questionVoList.add(questionVo);
			}
			rpqListVO.setQuestionList(questionVoList);
			//问卷评级
			RpqPageLevel level = this.loadRpqLevelMsg(rpqExam.getId(),langCode);
			rpqListVO.setLevel(level);
		}
		return rpqListVO;
	}
	
	/**
	 * 从模板复制问卷到rpqExam
	 * @author scshi
	 * @param distributorId 代理商
	 * @param clientType 适用客户,Individual,Corporate,FI多个之间用,分隔'
	 * @param pageType '此问题适用类型, O:开户rpq I:投资方案的rpq,K:KYC中使用的rpq,
	 * */
	public List<RpqListVO> copyExamFromPage(String distributorId,String clientType, String pageType,
			HttpServletRequest request,String accountId){
		String hql = "from RpqPageType t where t.distributor.id=? and t.clientType=? and t.pageType=? order by t.orderBy desc ";
		List<String> params = new ArrayList<String>();
		params.add(distributorId);
		params.add(clientType);
		params.add(pageType);
		List<RpqPageType> list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			MemberBase loginUser = this.getLoginUser(request);
			for(int y=0;y<list.size();y++){
				//保存问卷
				RpqPageType pageTypeMid = list.get(y);
				if(null == pageTypeMid)continue;
				this.saveOrUpdateRpqExam(pageTypeMid,loginUser,accountId);
			}
		}
		
		//读取rpq问卷与选项列表
		List<RpqListVO> voList = this.findRpqExamList(accountId, pageType, this.getLoginLangFlag(request));
		
		return voList;
	}	
	
	//保存问卷
	public RpqExam saveOrUpdateRpqExam(RpqPageType pageTypeMid,MemberBase loginUser,String accountId){
		RpqExam rpqExam = new RpqExam();
		rpqExam.setId(null);
		rpqExam.setCreateTime(new Date());
		rpqExam.setOrderBy(pageTypeMid.getOrderBy());
		rpqExam.setMember(loginUser);
		
		RpqPage rpqPage = pageTypeMid.getRpqPage();
		rpqExam.setPageId(rpqPage.getId());
		rpqExam.setModuleType(pageTypeMid.getPageType());
		rpqExam.setRelateId(accountId);
		rpqExam.setType(rpqPage.getType());
		rpqExam.setFromMember(loginUser);
		rpqExam.setDistributor(pageTypeMid.getDistributor());
		//rpqExam.setIsCalScore(rpqPage.getIsDef());
		
		rpqExam.setIsCalScore(rpqPage.getIsCalScore());
		rpqExam.setIsValid("1");
		//rpqExam.setStatus("1");
		this.saveOrUpdateRpqExam(rpqExam);
		
		//英文版保存
		RpqExamEn examEn = new RpqExamEn();
		RpqPageEn pageEn = this.getRpqPageEn(rpqPage.getId());
		examEn.setId(rpqExam.getId());
		examEn.setTitle(pageEn.getTitle());
		examEn.setRemark(pageEn.getRemark());
		this.saveOrUpdateExamEn(examEn);
		
		//繁体保存
		RpqExamTc examTc = new RpqExamTc();
		RpqPageTc pageTc = this.getRpqPageTc(rpqPage.getId());
		examTc.setId(rpqExam.getId());
		examTc.setTitle(pageTc.getTitle());
		examTc.setRemark(pageTc.getRemark());
		this.saveOrUpdateExamTc(examTc);
		
		//简体中文保存
		RpqExamSc examSc = new RpqExamSc();
		RpqPageSc pageSc = this.getRpqPageSc(rpqPage.getId());
		examSc.setId(rpqExam.getId());
		examSc.setTitle(pageSc.getTitle());
		examSc.setRemark(pageSc.getRemark());
		this.saveOrUpdateExamSc(examSc);
		
		//问卷问题保存
		List<RpqPageQuest> rpqPageQuestList = this.findRpqPageQuest( rpqPage.getId() );
		if(null!=rpqPageQuestList && !rpqPageQuestList.isEmpty()){
			for(int a=0;a<rpqPageQuestList.size();a++){
				RpqPageQuest pageQuestion = rpqPageQuestList.get(a);
				if(null == pageQuestion)continue;
				this.saveOrUpdateQuestion(rpqExam,pageQuestion);
			}
		}
		return rpqExam;
	}
	
	//保存问题
	public void saveOrUpdateQuestion(RpqExam rpqExam,RpqPageQuest pageQuestion){
		RpqExamQuest rpqExamQuest = new RpqExamQuest();
		rpqExamQuest.setId(null);
		rpqExamQuest.setExam(rpqExam);
		rpqExamQuest.setOrderBy( pageQuestion.getOrderBy() );
		this.saveOrUpdateRpqExamQuest(rpqExamQuest);
		
		//簡體保存rpqExamQuest
		RpqExamQuestSc examQuestionSc  =  new RpqExamQuestSc();
		RpqQuest rpqQuestionSc = this.loadRpqQuestionById( pageQuestion.getQuest().getId(), "sc");
		examQuestionSc.setId(rpqExamQuest.getId());
		examQuestionSc.setTitle(rpqQuestionSc.getTitle());
		examQuestionSc.setRemark(rpqQuestionSc.getRemark());
		this.saveOrUpdateRpqExamQuestSc(examQuestionSc);
		
		//繁體rqpExamQuestion 
		RpqExamQuestTc examQuestionTc = new RpqExamQuestTc();
		RpqQuest rpqQuestionTc= this.loadRpqQuestionById( pageQuestion.getQuest().getId(), "tc");
		examQuestionTc.setId(rpqExamQuest.getId());
		examQuestionTc.setTitle(rpqQuestionTc.getTitle());
		examQuestionTc.setRemark(rpqQuestionTc.getRemark());
		//RpqExamQuestTc examQuestion_tcEntity = 
			this.saveOrUpdateRpqExamQuestTc(examQuestionTc);
		//英文rqpExamQuestion保存
		RpqExamQuestEn examQuestionEn = new RpqExamQuestEn();
		RpqQuest rpqQuestionEn = this.loadRpqQuestionById(pageQuestion.getQuest().getId(), "en");
		examQuestionEn.setId(rpqExamQuest.getId());
		examQuestionEn.setTitle(rpqQuestionEn.getTitle());
		examQuestionEn.setRemark(rpqQuestionEn.getRemark());
		//RpqExamQuestEn examQuestion_enEntity = 
			this.saveOrUpdateExamQuestSc(examQuestionEn);
			
		//选项保存
		String itemHql = "from RpqQuestItem t where t.quest.id = ? order by t.orderBy "	;
		List<RpqQuestItem> rpqQuestItemList = this.baseDao.find(itemHql, new String[]{pageQuestion.getQuest().getId()}, false);
		//List<RpqQuestItem> rpqQuestItemList = this.findRpqQuestItemList(pageQuestion.getQuest().getId(), "sc");	
		for(int b=0;b<rpqQuestItemList.size();b++){
			RpqQuestItem pageQuestionItem = rpqQuestItemList.get(b);
			if(null == pageQuestionItem)continue;
			this.saveOrUpdateRpqExamQuestItem(pageQuestionItem,rpqExamQuest);
		}
	}
	
	//选项保存
	public void saveOrUpdateRpqExamQuestItem(RpqQuestItem pageQuestionItem,RpqExamQuest rpqExamQuest){
		RpqExamQuestItem examQuestItem = new RpqExamQuestItem();
		examQuestItem.setId(null);
		examQuestItem.setQuest(rpqExamQuest);
		examQuestItem.setType( pageQuestionItem.getType() );
		examQuestItem.setScoreValue( pageQuestionItem.getScoreValue() );
		examQuestItem.setOrderBy( pageQuestionItem.getOrderBy() );
		RpqExamQuestItem examQuestItemPersistent = this.saveOrUpdateRpqExamQuestItem(examQuestItem);
		
		//英文選項保存
		RpqQuestItemEn questionItemEn = this.getRpqQuestItemEn(pageQuestionItem.getId());
		RpqExamQuestItemEn examQuestItemEn = new RpqExamQuestItemEn();
		examQuestItemEn.setId(examQuestItemPersistent.getId());
		examQuestItemEn.setTitle(questionItemEn.getTitle());
		examQuestItemEn.setRemark(questionItemEn.getRemark());
		//RpqExamQuestItemEn examQuestItem_enEntity = 
			this.saveOrUpdateRpqExamQuestItemEn(examQuestItemEn);
		
		//繁體中文
		RpqQuestItemTc questionItemTc = this.getRpqQuestItemTc(pageQuestionItem.getId());
		RpqExamQuestItemTc examQuestItemTc = new RpqExamQuestItemTc();
		examQuestItemTc.setId(examQuestItemPersistent.getId());
		examQuestItemTc.setTitle(questionItemTc.getTitle());
		examQuestItemTc.setRemark(questionItemTc.getRemark());
		//RpqExamQuestItemTc questionItemTcEntity = 
			this.saveOrUpdateRpqExamQuestItemTc(examQuestItemTc);
		
		//簡體保存
		RpqQuestItemSc questionItemSc = this.getRpqQuestItemSc(pageQuestionItem.getId());
		RpqExamQuestItemSc examQuestItemSc = new RpqExamQuestItemSc();
		examQuestItemSc.setId(examQuestItemPersistent.getId());
		examQuestItemSc.setRemark(questionItemSc.getRemark());
		examQuestItemSc.setTitle(questionItemSc.getTitle());
		//RpqExamQuestItemSc examQuestItem_scEntity = 
			this.saveOrUpdateRpqExamQuestItemSc(examQuestItemSc);
	}
	
	public List<RpqExamAnswer> findRpqExamAnswer(String id, String examId, String questId, String itemId) {
		return null;
	}

	/*public List<RpqExamQuestItem> findRpqExamQuestItem(String id, String questId, String type, String title, int scoreValue, String remark) {
		return null;
	}*/

// find list -------------
	
// get entity -------------
	@Override
	public InvestorAccount getInvestorAccount(String id) {
		if (null == id) {
			return null;
		}
		return (InvestorAccount) this.baseDao.get(InvestorAccount.class, id);
	}
	
	public RpqExam getRpqExam(String id) {
		if (null == id) {
			return null;
		}
		return (RpqExam) this.baseDao.get(RpqExam.class, id);
	}
	
	public RpqExamAnswer getRpqExamAnswer(String id) {
		if (null == id) {
			return null;
		}
		return (RpqExamAnswer) this.baseDao.get(RpqExamAnswer.class, id);
	}

	public RpqExamQuest getRpqExamQuest(String id) {
		if (null == id) {
			return null;
		}
		return (RpqExamQuest) this.baseDao.get(RpqExamQuest.class, id);
	}

	public RpqExamQuestItem getRpqExamQuestItem(String id) {
		if (null == id) {
			return null;
		}
		return (RpqExamQuestItem) this.baseDao.get(RpqExamQuestItem.class, id);
	}
	
	@Override
	public RpqPage getRpqPage(String id) {
		if ( null == id ) {
			return null;
		}
		return (RpqPage)this.baseDao.get(RpqPage.class, id);
	}
	
	@Override
	public RpqPageLevel getRpqPageLevel(String id) {
		if ( null == id ) {
			return null;
		}
		return (RpqPageLevel)this.baseDao.get(RpqPageLevel.class, id);
	}
	
	@Override
	public RpqQuest getRpqQuest(String id) {
		if ( null == id ) {
			return null;
		}
		return (RpqQuest)this.baseDao.get(RpqQuest.class, id);
	}
	
	@Override
	public RpqQuestItem getRpqQuestItem(String id) {
		if ( null == id ) {
			return null;
		}
		return (RpqQuestItem)this.baseDao.get(RpqQuestItem.class, id);
	}
	
	@Override
	public MemberDistributor getMemberDistributor(String id) {
		if ( null == id ) {
			return null;
		}
		return (MemberDistributor)this.baseDao.get(MemberDistributor.class, id);
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	public AccessoryFile getAccessoryFile(String id) {
		if ( null == id ) {
			return null;
		}
		return (AccessoryFile)this.baseDao.get(AccessoryFile.class, id);
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	public DocTemplate getDocTemplate(String id) {
		if ( null == id ) {
			return null;
		}
		return (DocTemplate)this.baseDao.get(DocTemplate.class, id);
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	public InvestorAccountDeclaration getInvestorAccountDeclarationAgeas(String id) {
		if ( null == id ) {
			return null;
		}
		return (InvestorAccountDeclaration)this.baseDao.get(InvestorAccountDeclaration.class, id);
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	public InvestorDoc getInvestorDoc(String id) {
		if ( null == id ) {
			return null;
		}
		return (InvestorDoc)this.baseDao.get(InvestorDoc.class, id);
	}

	public RpqPageEn getRpqPageEn(String id) {
		return (RpqPageEn)this.baseDao.get(RpqPageEn.class, id);
	}

	public RpqPageSc getRpqPageSc(String id) {
		return (RpqPageSc)this.baseDao.get(RpqPageSc.class, id);
	}

	public RpqPageTc getRpqPageTc(String id) {
		return (RpqPageTc)this.baseDao.get(RpqPageTc.class,id);
	}

//saveORupdate entity 	
	public RpqExamEn saveOrUpdateExamEn(RpqExamEn examEn) {
		RpqExamEn obj = (RpqExamEn)this.baseDao.get(RpqExamEn.class, examEn.getId());
		if(null==obj){
			return (RpqExamEn)this.baseDao.create(examEn);
		}else{
			obj.setTitle(examEn.getTitle());
			obj.setRemark(examEn.getRemark());
			return (RpqExamEn)this.baseDao.saveOrUpdate(obj,false);
		}
	}

	public RpqExamTc saveOrUpdateExamTc(RpqExamTc examTc) {
		RpqExamTc obj = (RpqExamTc)this.baseDao.get(RpqExamTc.class, examTc.getId());
		if(null==obj){
			return (RpqExamTc)this.baseDao.create(examTc);
		}else{
			obj.setTitle(examTc.getTitle());
			obj.setRemark(examTc.getRemark());
			return (RpqExamTc)this.baseDao.saveOrUpdate(obj,false);
		}
	}

	public RpqExamSc saveOrUpdateExamSc(RpqExamSc examSc) {
		RpqExamSc obj = (RpqExamSc)this.baseDao.get(RpqExamSc.class, examSc.getId());
		if(null==obj){
			return (RpqExamSc)this.baseDao.create(examSc);
		}else{
			obj.setTitle(examSc.getTitle());
			obj.setRemark(examSc.getRemark());
			return (RpqExamSc)this.baseDao.saveOrUpdate(examSc,false);
		}
	}

	public RpqExamQuestSc saveOrUpdateRpqExamQuestSc(
			RpqExamQuestSc examQuestionSc) {
		RpqExamQuestSc obj = (RpqExamQuestSc)this.baseDao.get(RpqExamQuestSc.class, examQuestionSc.getId());
		if(null==obj){
			return (RpqExamQuestSc)this.baseDao.create(examQuestionSc);
		}else{
			obj.setTitle(examQuestionSc.getTitle());
			obj.setRemark(examQuestionSc.getRemark());
			return (RpqExamQuestSc)this.baseDao.saveOrUpdate(obj,false);
		}
	}

	public RpqExamQuestTc saveOrUpdateRpqExamQuestTc(
			RpqExamQuestTc examQuestionTc) {
		RpqExamQuestTc obj = (RpqExamQuestTc)this.baseDao.get(RpqExamQuestTc.class, examQuestionTc.getId());
		if(null==obj){
			return (RpqExamQuestTc)this.baseDao.create(examQuestionTc);
		}else{
			obj.setTitle(examQuestionTc.getTitle());
			obj.setRemark(examQuestionTc.getRemark());
			return (RpqExamQuestTc)this.baseDao.saveOrUpdate(obj,false);
		}
	}

	public RpqExamQuestEn saveOrUpdateExamQuestSc(RpqExamQuestEn examQuestionEn) {
		RpqExamQuestEn obj = (RpqExamQuestEn)this.baseDao.get(RpqExamQuestEn.class, examQuestionEn.getId());
		if(null == obj){
			return (RpqExamQuestEn)this.baseDao.create(examQuestionEn);
		}else{
			obj.setTitle(examQuestionEn.getTitle());
			obj.setRemark(examQuestionEn.getRemark());
			return (RpqExamQuestEn)this.baseDao.saveOrUpdate(obj,false);
		}
	}

	public RpqQuestItemEn getRpqQuestItemEn(String id) {
		return (RpqQuestItemEn)this.baseDao.get(RpqQuestItemEn.class, id);
	}

	public RpqQuestItemTc getRpqQuestItemTc(String id) {
		return (RpqQuestItemTc)this.baseDao.get(RpqQuestItemTc.class, id);
	}

	public RpqQuestItemSc getRpqQuestItemSc(String id) {
		return (RpqQuestItemSc)this.baseDao.get(RpqQuestItemSc.class, id);
	}

	public RpqExamQuestItemEn saveOrUpdateRpqExamQuestItemEn(
			RpqExamQuestItemEn examQuestItemEn) {
		RpqExamQuestItemEn obj = (RpqExamQuestItemEn)this.baseDao.get(RpqExamQuestItemEn.class, examQuestItemEn.getId());
		if(null==obj){
			return (RpqExamQuestItemEn)this.baseDao.create(examQuestItemEn);
		}else{
			obj.setTitle(examQuestItemEn.getTitle());
			obj.setRemark(examQuestItemEn.getRemark());
			return (RpqExamQuestItemEn)this.baseDao.saveOrUpdate(obj,false);
		}
		
	}

	public RpqExamQuestItemTc saveOrUpdateRpqExamQuestItemTc(
			RpqExamQuestItemTc examQuestItemTc) {
		RpqExamQuestItemTc obj = (RpqExamQuestItemTc)this.baseDao.get(RpqExamQuestItemTc.class,examQuestItemTc.getId());
		if(null == obj){
			return (RpqExamQuestItemTc)this.baseDao.create(examQuestItemTc);
		}else{
			obj.setTitle(examQuestItemTc.getTitle());
			obj.setRemark(examQuestItemTc.getRemark());
			return  (RpqExamQuestItemTc)this.baseDao.saveOrUpdate(obj,false);
		}
		
	}

	public RpqExamQuestItemSc saveOrUpdateRpqExamQuestItemSc(
			RpqExamQuestItemSc examQuestItemSc) {
		RpqExamQuestItemSc obj = (RpqExamQuestItemSc)this.baseDao.get(RpqExamQuestItemSc.class, examQuestItemSc.getId());
		if(null==obj){
			return (RpqExamQuestItemSc)this.baseDao.create(examQuestItemSc);
		}else{
			obj.setTitle(examQuestItemSc.getTitle());
			obj.setRemark(examQuestItemSc.getRemark());
			return (RpqExamQuestItemSc)this.baseDao.saveOrUpdate(obj,false);
		}
	}

	/*@Override
	public List<RpqExamQuest> findRpqExamQuest(String id, String exam,
			String title, String remark) {
		// TODO Auto-generated method stub
		return null;
	}*/

	public RpqPageLevel loadRpqLevelMsg(String examId, String loginLangFlag) {
		String hql = "select t.totalScore,t.riskLevel,out.riskResult ";
		hql += " from RpqExam t inner join "+this.getLangString("RpqExam", loginLangFlag);
		hql += " out on t.id=out.id where t.id=?";
		List list = this.baseDao.find(hql, new String[]{examId}, false);
		List<RpqPageLevel> levels = new ArrayList<RpqPageLevel>();
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				Object[] objs = (Object[])list.get(x);
				RpqPageLevel level = new RpqPageLevel();
				level.setRiskLevel(objs[1]==null?null:Integer.parseInt(objs[1].toString()));
				level.setResult(objs[2]==null?"":objs[2].toString());
				levels.add(level);
			}
			return levels.get(0);
		}
		return  null;
	}
	
	/**根据问卷和问题，返回原答案
	 * @param examId
	 * @param questionId
	 * @return
	 */
	@Override
	public RpqExamAnswer getRpqExamAns(String examId, String questionId) {
		String hql = " from RpqExamAnswer t where t.exam.id=? and t.quest.id=? ";
		List params = new ArrayList();
		params.add(examId);
		params.add(questionId);
		List<RpqExamAnswer> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			return list.get(0);
		}
		
		//首次提交，创建答卷-问题-选项关联表
		RpqExamAnswer ans = new RpqExamAnswer();
		ans.setId(null);
		RpqExam exam  = new RpqExam();
		exam.setId(examId);
		RpqExamQuest quest = new RpqExamQuest();
		quest.setId(questionId);
		ans.setExam(exam);
		ans.setQuest(quest);
		return ans;
	}

	@Override
	public RpqExam saveCompleteExam(String levelId, String userLevelId,
			String examId,int score) {
		//保存exam
		RpqPageLevel rpqLevel = (RpqPageLevel) this.baseDao.get(RpqPageLevel.class, levelId);
		RpqExam exam = (RpqExam) this.baseDao.get(RpqExam.class, examId);
		if(rpqLevel != null){
			exam.setRiskLevel(rpqLevel.getRiskLevel());
		}
		Calendar calendar = Calendar.getInstance();
		if(exam.getCreateTime() != null){
			calendar.setTime(exam.getCreateTime());
		}
		calendar.add(Calendar.YEAR, 1);
		exam.setExpireDate(calendar.getTime());
		exam.setTotalScore(score);
		exam.setStatus("1");
		RpqPageLevel userRpqLevel = null;
		if(StringUtils.isNotBlank(userLevelId)){
			userRpqLevel = (RpqPageLevel) this.baseDao.get(RpqPageLevel.class, userLevelId);
			exam.setUserRiskLevel(userRpqLevel.getRiskLevel());
		}else{
			exam.setUserRiskLevel(null);
		}
		baseDao.update(exam);
		
		//保存EN
		RpqPageLevelEn levelEn = (RpqPageLevelEn) baseDao.get(RpqPageLevelEn.class, levelId);
		RpqExamEn examEn = new RpqExamEn();
		examEn.setId(exam.getId());
		if(levelEn != null){
			examEn.setRiskResult(levelEn.getRemark());
			examEn.setRiskRemark(levelEn.getRemark());
		}
		if(userRpqLevel != null){
			RpqPageLevelEn levelEnUser = (RpqPageLevelEn) baseDao.get(RpqPageLevelEn.class, userLevelId);
			examEn.setUserRiskRemark(levelEnUser.getRemark());
			examEn.setUserRiskResult(levelEnUser.getResult());
		}
		baseDao.saveOrUpdate(examEn);
		
		//保存SC
		RpqPageLevelSc levelSC = (RpqPageLevelSc) baseDao.get(RpqPageLevelSc.class, levelId);
		RpqExamSc examSC = new RpqExamSc();
		examSC.setId(exam.getId());
		if(levelSC != null){
			examSC.setRiskResult(levelSC.getRemark());
			examSC.setRemark(levelSC.getRemark());
		}
		if(userRpqLevel != null){
			RpqPageLevelSc levelScUser = (RpqPageLevelSc) baseDao.get(RpqPageLevelSc.class, userLevelId);
			examSC.setUserRiskRemark(levelScUser.getRemark());
			examSC.setUserRiskResult(levelScUser.getResult());
		}
		baseDao.saveOrUpdate(examSC);
		//保存Tc
		RpqPageLevelTc levelTC = (RpqPageLevelTc) baseDao.get(RpqPageLevelTc.class, levelId);
		RpqExamTc examTc = new RpqExamTc();
		examTc.setId(exam.getId());
		if(levelTC != null){
			examTc.setRiskResult(levelTC.getRemark());
			examTc.setRemark(levelTC.getRemark());
		}
		if(userRpqLevel != null){
			RpqPageLevelTc levelTcUser = (RpqPageLevelTc) baseDao.get(RpqPageLevelTc.class, userLevelId);
			examTc.setUserRiskRemark(levelTcUser.getRemark());
			examTc.setUserRiskResult(levelTcUser.getResult());
		}
		//baseDao.saveOrUpdate(levelTC);
		baseDao.saveOrUpdate(examTc);
		return exam;
	}

	@Override
	public RpqExam getRpqExamRiskLevel(String id, String langFlag) {
		RpqExam exam = (RpqExam) this.baseDao.get(RpqExam.class, id);
		Integer userRiskId = exam.getUserRiskLevel();
		if(CommonConstants.LANG_CODE_EN.equals(langFlag)){
			RpqExamEn examEn = (RpqExamEn) baseDao.get(RpqExamEn.class,exam.getId());
			if(userRiskId!=null){
				exam.setRemark(examEn.getUserRiskRemark());
				exam.setResult(examEn.getUserRiskResult());
			}else{
				exam.setRemark(examEn.getRiskRemark());
				exam.setResult(examEn.getRiskResult());
			}
		}else if(CommonConstants.LANG_CODE_TC.equals(langFlag)){
			RpqExamTc examTc = (RpqExamTc) baseDao.get(RpqExamTc.class,exam.getId());
			if(userRiskId!=null){
				exam.setRemark(examTc.getUserRiskRemark());
				exam.setResult(examTc.getUserRiskResult());
			}else{
				exam.setRemark(examTc.getRiskRemark());
				exam.setResult(examTc.getRiskResult());
			}
		}else if(CommonConstants.LANG_CODE_SC.equals(langFlag)){
			RpqExamSc examSc = (RpqExamSc) baseDao.get(RpqExamSc.class,exam.getId());
			if(userRiskId!=null){
				exam.setRemark(examSc.getUserRiskRemark());
				exam.setResult(examSc.getUserRiskResult());
			}else{
				exam.setRemark(examSc.getRiskRemark());
				exam.setResult(examSc.getRiskResult());
			}
		}
		return exam;
	}

	/**
	 * 更新状态，只保留最新数据
	 */
	@Override
	public void updateStatus(String memberId, String moduleType, String examId) {
		String hql = " FROM RpqExam e WHERE e.status=1 AND e.member.id=? AND e.moduleType=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(moduleType);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				RpqExam objRead = (RpqExam)list.get(i);
				String id = objRead.getId();
				if(id!=examId){
					RpqExam exam = this.getRpqExam(id);
					exam.setStatus("0");
					this.baseDao.update(exam);
				}
			}
		}
	}

	/**
	 * 更新账号风险等级
	 * @param accountId 主账号
	 * @param rpqLevel
	 */
	@Override
	public void updateAccountRpqLevel(String accountId, Integer rpqLevel) {
		if (StringUtils.isNotBlank(accountId) && rpqLevel != null) {
			StringBuffer hql = new StringBuffer(""
					+ " UPDATE InvestorAccount a"
					+ " SET"
					+ " a.rpqLevel=?"
					+ " WHERE"
					+ " a.id=?"
					+ " OR"
					+ " (a.subFlag=1"
					+ " AND"
					+ " a.masterAccount.id=?)"
					+ "");
			coreBaseService.updateHql(hql.toString(), new Object[]{rpqLevel, accountId, accountId});
		}		
	}

	/**
	 * 根据风险等级获取结果
	 * @param rpqExamId 答卷Id
	 * @param langCode
	 * @param rpqLevel
	 */
	@Override
	public String getRiskLevelResult(String rpqExamId, Integer riskLevel, String langCode) {
		String riskLevelResult = null;
		if (riskLevel != null && StringUtils.isNotBlank(langCode)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " l.result"
					+ " FROM"
					+ " RpqPageLevel r"
					+ " LEFT JOIN"
					+ " RpqExam e"
					+ " ON"
					+ " e.pageId=r.page.id"
					+ " LEFT JOIN"
					+ " RpqPageLevel" + getLangFirstCharUpper(langCode) + " l"
					+ " ON"
					+ " r.id=l.id"
					+ " WHERE"
					+ " e.id=?"
					+ " AND"
					+ " r.riskLevel=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(rpqExamId);
			params.add(riskLevel);
			List<String> riskLevelResults = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(riskLevelResults != null && !riskLevelResults.isEmpty()){
				riskLevelResult = riskLevelResults.get(0);
			}
		}
		return riskLevelResult;
	}

}
