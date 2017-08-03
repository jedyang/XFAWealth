/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.investor.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.vo.SysParamVO;
import com.fsll.wmes.crm.vo.InvestorAccountDistributorVO;
import com.fsll.wmes.entity.DocList;
import com.fsll.wmes.entity.DocListEn;
import com.fsll.wmes.entity.DocListSc;
import com.fsll.wmes.entity.DocListTc;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountBank;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountContactAddr;
import com.fsll.wmes.entity.InvestorAccountDeclaration;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.InvestorDocEn;
import com.fsll.wmes.entity.InvestorDocSc;
import com.fsll.wmes.entity.InvestorDocTc;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderReturn;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqExamEn;
import com.fsll.wmes.entity.RpqExamSc;
import com.fsll.wmes.entity.RpqExamTc;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.WebFriend;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.IndividualDataVO;
import com.fsll.wmes.investor.vo.InvestorDistributorVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.BasicInfoCombVo;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.rpq.vo.RpqListVO;
/***
 * 业务接口实现类：开户管理
 * @author michael
 * @date 2016-7-7
 */
@Service("investorService")
//@Transactional
public class InvestorServiceImpl extends BaseService implements InvestorService {
	@Autowired
	private MemberBaseService memberBaseService;
	
//	@Autowired
//	private InvestorService investorService;
	
// saveOrUpdate -------------
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
	public InvestorAccountDeclaration saveOrUpdateInvestorAccountDeclarationAgeas(InvestorAccountDeclaration ageas,String accountId) {
		InvestorAccount investorAccount = new InvestorAccount();
		investorAccount.setId(accountId);
		
		ageas.setLastUpdate(new Date());
		ageas.setAccount(investorAccount);
		
		InvestorAccountDeclaration ageasPr = null;
		if ( null == ageas.getId() || ageas.getId().trim().length() < 1 ) {
			ageas.setId(null);
			ageas.setCreateTime(new Date());
			ageasPr = (InvestorAccountDeclaration)baseDao.create(ageas);
		}else{
			ageas.setLastUpdate(new Date());
			ageasPr = (InvestorAccountDeclaration)baseDao.update(ageas);
		}
		return ageasPr;
	}
	
	/**
	 * 保存开户基本信息
	 * @author michael
	 * @param account 开户基本信息实体
	 * @return InvestorAccount
	 */
	public InvestorAccount saveOrUpdateInvestorAccount(InvestorAccount account){
		return (InvestorAccount) baseDao.saveOrUpdate(account);
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
	 */
	@Override
	public List<RpqPageLevel> findRpqPageLevel(String pageId, int score, String status, String isValid,String langCode) {
		String hql = "select t.id,out.result,out.remark ";
		hql +=	" from RpqPageLevel t inner join "+this.getLangString("RpqPageLevel", langCode);
		hql += " out on t.id=out.id ";
		hql += 	" where "+score+" BETWEEN t.beginScore and t.endScore and t.page.id = ? and t.status = ? and t.isValid = ? ";
		List<String> params = new ArrayList<String>();
		params.add( pageId );
		params.add( status );
		params.add( isValid );
		List<RpqPageLevel> levels =new ArrayList();
		List list  = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				Object[] objs = (Object[])list.get(x);
				String levelId = objs[0]==null?"":objs[0].toString();
				if(!"".equals(levelId)){
					RpqPageLevel level = (RpqPageLevel)this.baseDao.get(RpqPageLevel.class, levelId);
					level.setResult(objs[1]==null?"":objs[1].toString());
					level.setRemark(objs[2]==null?"":objs[2].toString());
					levels.add(level);
				}
			}
		}
		return levels;
	}
	
	/**
	 * @author tejay zhu
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
		
		String docTemplateHql = "select dt.id,out.title ";
		docTemplateHql += " from DocTemplate dt inner join "+this.getLangString("DocTemplate", langCode);
		docTemplateHql += " out on dt.id=out.id ";
		docTemplateHql += " WHERE dt.isValid = 1 and dt.distributor.id = ? and dt.clientType = ? ";
		List<String> docTemplateParams = new ArrayList<String>();
		docTemplateParams.add(distributorId);
		docTemplateParams.add(clientType);
//		docTemplateParams.add(langCode);
		List voList = this.baseDao.find(docTemplateHql, docTemplateParams.toArray(), false);
		List<DocTemplate> docTemplatesList = new ArrayList<DocTemplate>();
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				Object[] objs = (Object[])voList.get(x);
				DocTemplate temp = new DocTemplate();
				String tempId = objs[0]==null?"":objs[0].toString();
				if(!"".equals(tempId)){
					temp = (DocTemplate)this.baseDao.get(DocTemplate.class, tempId);
					temp.setTitle(objs[1]==null?"":objs[1].toString());
				}
				docTemplatesList.add(temp);
			}
		}
		
		
		if ( null != docTemplatesList && !docTemplatesList.isEmpty() ) {
			String docListHql = "select t.id,out.docName,out.remark ";
			docListHql += " from DocList t inner join "+this.getLangString("DocList", langCode);
			docListHql += " out on t.id=out.id ";
			docListHql += " where t.template.id = ? ORDER BY t.id ";
			List<String> docListParams = new ArrayList<String>();
			docListParams.add( docTemplatesList.get(0).getId() );
			List docList = this.baseDao.find(docListHql, docListParams.toArray(), false);
//			docLists = 
			if(!docList.isEmpty()){
				for(int y=0;y<docList.size();y++){
					Object[] objs = (Object[])docList.get(y);
					DocList docObj= new DocList();
					String docId = objs[0]==null?"":objs[0].toString();
					if(!"".endsWith(docId)){
						docObj = (DocList)this.baseDao.get(DocList.class, docId);
						//docObj.setDocName(objs[1]==null?"":objs[1].toString());
						//docObj.setDesc(objs[2]==null?"":objs[2].toString());
					}
					docLists.add(docObj);
				}	
			}
			
			if ( null != docLists && !docLists.isEmpty() ) {
				// 转换是否为必须上传项显示内容为文字
				for (int i = 0; i < docLists.size(); i++) {

					String investorDocHql = "select t.id,out.docName ";
					investorDocHql += " from InvestorDoc t inner join "+this.getLangString("InvestorDoc", langCode);
					investorDocHql += " out on t.id=out.id ";
					investorDocHql += " where t.distributor.id = ? and t.docTemplate.id = ? ";
					investorDocHql	+= " and out.docName = ? and t.isNecessary = ? and t.isValid = ? ";

					List<String> investorDocParams = new ArrayList<String>();
					investorDocParams.add(distributorId);
					investorDocParams.add( docLists.get(i).getTemplate().getId() );
					//investorDocParams.add( docLists.get(i).getDocName() );
					investorDocParams.add( docLists.get(i).getIsNecessary() );
					investorDocParams.add("1");
					List investList = this.baseDao.find(investorDocHql, investorDocParams.toArray(), false);
					if(!investList.isEmpty()){
						for(int z=0;z<investList.size();z++){
							Object[] objs = (Object[])investList.get(z);
							InvestorDoc investDoc = new InvestorDoc();
							String investDocId = objs[0]==null?"":objs[0].toString();
							if(!"".equals(investDocId)){
								investDoc = (InvestorDoc)this.baseDao.get(InvestorDoc.class, investDocId);
								//invest_doc.setDocName(objs[1]==null?"":objs[1].toString());
							}
							investorDocList.add(investDoc);
						}
					}
					
					DocListVO docListVO = new DocListVO();
					docListVO.setId( docLists.get(i).getId() );
					docListVO.setTemplateId(docLists.get(i).getTemplate().getId());
					//docListVO.setDocName(docLists.get(i).getDocName());
					//docListVO.setDesc( docLists.get(i).getDesc() );
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
						docListVO.setFileList(files);
//						System.out.println( "** AccessoryFile files:"+files.size() + " <> investorDocList.get(0).getId():"+investorDocList.get(0).getId() );
//						if ( null != files && !files.isEmpty() ) {
//							docListVO.setAccessoryFileId( files.get(0).getId() );
//							docListVO.setAccessoryFileName( files.get(0).getFileName() );
//							docListVO.setAccessoryFileCreateTime( files.get(0).getCreateTime() );
//						}else{
//							docListVO.setAccessoryFileId("");
//							docListVO.setAccessoryFileName("");
//							docListVO.setAccessoryFileCreateTime(new Date());							
//						}
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
//		String hql = "from InvestorAccountDeclarationAgeas t where t.account.id = ?";
		String hql = "from InvestorAccountDeclaration t where t.account.id = ?";
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
	public List<RpqQuestItem> findRpqQuestItemList(String questId){
		String hql = "from RpqQuestItem t where t.quest.id = ? order by t.orderBy ";
		List<String> params = new ArrayList<String>();
		params.add(questId);
		
		List<RpqQuestItem> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 通过会员ID查询好友
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list 好友列表
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<WebFriend> findWebFriends(String fromMemberId){
		List<WebFriend> list = new ArrayList<WebFriend>();
		List<String> params = new ArrayList<String>();

		String hql = "from WebFriend t ";
		if (null!=fromMemberId && !"".equals(fromMemberId)){
			hql += " where fromMember.id = ? )";
			params.add(fromMemberId);
		}
		hql += " order by t.toMember ";
		
		list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}

	/**
	 * 通过会员ID查询IFA好友
	 * @author michael
	 * @param fromMemberId 会员ID
	 * @return list IFA好友列表
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<MemberIfa> findIfaFriends(String fromMemberId){
		List<MemberIfa> list = new ArrayList<MemberIfa>();
		List<String> params = new ArrayList<String>();

		String hql = "select m from MemberIfa m , WebFriend f where m.member.id = f.toMember.id  ";

		hql += " and f.fromMember.id = ? ";
		params.add(fromMemberId);

		hql += " order by m.firstName ";
		
		list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
	 * 通过会员ID查询IFA的客户列表
	 * @author michael
	 * @param memberId IFA会员ID
	 * @param clientType 客户类型：Buddy， Client， Advisor， Prospect
	 * @return list IFA好友列表
	 */
	public List<MemberIndividual> findClientByIFA(String memberId, String clientType){
		List<MemberIndividual> list = new ArrayList<MemberIndividual>();
		List<String> params = new ArrayList<String>();

		String hql = "select m from MemberIndividual m left join MemberBase b on m.member.id=b.id inner join WebFriend f on m.member.id = f.toMember.id where 1=1 ";
		hql += " and b.isValid='1' ";
		hql += " and f.fromMember.id = ? ";
		params.add(memberId);

		hql += " and f.relationships = ? ";
		params.add(clientType);
		
		hql += " order by m.firstName ";
		
		list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
	 * 通过IFA ID查询IFA关联的代理商
	 * @author michael
	 * @param ifaId IFA会员ID
	 * @return list IFA关联的代理商列表
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<MemberDistributor> findIfaDistributors(String ifaId){
		List<MemberDistributor> list = new ArrayList<MemberDistributor>();
		List<String> params = new ArrayList<String>();

		String hql = "select distinct m from MemberDistributor m inner join IfafirmDistributor f on m.id=f.distributor.id inner join MemberIfa i on  i.ifafirm.id = f.ifafirm.id where 1=1 ";
//		if (null!=ifaId && !"".equals(ifaId)){
			hql += " and i.member.id = ? ";
			params.add(ifaId);
//		}
		hql += " order by m.companyName ";
		
		list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
	 * 通过会员ID查询IFA的同事
	 * @author michael
	 * @param memberId 排除当前会员ID
	 * @param ifaFirmId IFA公司ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaColleagues(String memberId, String ifaFirmId){
		List<MemberIfa> list = new ArrayList<MemberIfa>();
		List<String> params = new ArrayList<String>();

		String hql = "select m from MemberIfa m where m.member.isValid='1' ";

		hql += " and m.ifafirm.id = ? ";
		params.add(ifaFirmId);
		hql += " and m.member.id <> ? ";
		params.add(memberId);

		hql += " order by m.firstName ";
		
		list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
	 * 通过会员ID查询IFA的组员
	 * @author michael
	 * @param memberId 会员ID
	 * @param ifaFirmId IFA公司ID
	 * @param ifaTeamId IFA组ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaTeamColleagues(String memberId, String ifaFirmId, String ifaTeamId){
		List<MemberIfa> list = new ArrayList<MemberIfa>();
		List<String> params = new ArrayList<String>();

		String hql = "select m from MemberIfa m where 1=1  ";

		hql += " and m.id in ( select t.ifa.id from IfafirmTeamIfa t where t.ifafirm.id = ? and t.team.id=? )";
		params.add(ifaFirmId);
		params.add(ifaTeamId);
		
		hql += " and m.member.id <> ? ";
		params.add(memberId);

		hql += " order by m.firstName ";
		
		list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<RpqPageQuest> findRpqPageQuest(String pageId) {
		String hql = "from RpqPageQuest t where t.page.id = ? order by t.orderBy ";
		List<String> params = new ArrayList<String>();
		params.add( pageId );
		
		List<RpqPageQuest> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	@Override
	//@Transactional(readOnly=true)
	public List<RpqPage> findRpqPage( String type, String distributorId, String clientType, String langCode, String isDef, String pageType ){
		String hql = "from RpqPage t where t.type = ? and t.distributor.id = ? and t.clientType = ? and t.langCode = ? and t.isDef = ? and t.pageType = ? ";
		List<String> params = new ArrayList<String>();
		params.add( type );
		params.add( distributorId );
		params.add( clientType );
		params.add( langCode );
		params.add( isDef );
		params.add( pageType );
		
		List<RpqPage> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * @author tejay zhu
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<RpqListVO> findRpqQuestList(String type, String distributorId, String clientType, String langCode, String isDef, String pageType) {
		
		// 返回的结果list
		List<RpqListVO> rpqListVOs = new ArrayList<RpqListVO>();
		
		// 确定使用的问卷
		List<RpqPage> rpqPageList = this.findRpqPage(type, distributorId, clientType, langCode, isDef, pageType);
		RpqPage rpqPage = null;
		
		// 确定问卷的问题列表
		List<RpqPageQuest> rpqPageQuestList = null;
		if( null != rpqPageList && !rpqPageList.isEmpty() ){
			rpqPage = rpqPageList.get(0);
//			System.out.println( "** 这里获得的问卷id："+rpqPage.getId()  );
			
			rpqPageQuestList = this.findRpqPageQuest( rpqPage.getId() );
			for (int i = 0; i < rpqPageQuestList.size(); i++) {
				RpqListVO rpqListVO = new RpqListVO();
				rpqListVO.setRpqPageid( rpqPage.getId() );
				rpqListVO.setRpqPageTitle( rpqPage.getTitle() );
				rpqListVO.setRpqPageRemark( rpqPage.getRemark() );
				rpqListVO.setType( rpqPage.getType() );
				rpqListVO.setDistributorId( rpqPage.getDistributor().getId() );
				rpqListVO.setIsValid( rpqPage.getIsValid() );
				rpqListVO.setQuestId( rpqPageQuestList.get(i).getQuest().getId() );
				rpqListVO.setQuestOrder( rpqPageQuestList.get(i).getOrderBy() );
				rpqListVO.setQuestTitle( (i+1)+"."+ rpqPageQuestList.get(i).getQuest().getTitle() );
				rpqListVOs.add(rpqListVO);
			}
		}else{
			return rpqListVOs;
		}

		// 获取各条问题的可选项
		RpqListVO rpqListVOForFillItem = null;
		if( null != rpqPageQuestList && !rpqPageQuestList.isEmpty() ){
			for (int i = 0; i < rpqListVOs.size(); i++) {
				rpqListVOForFillItem = rpqListVOs.get(i);
				rpqListVOs.get(i).setRpqQuestItemList( this.findRpqQuestItemList( rpqListVOForFillItem.getQuestId() ) );
			}
		}
		
		return rpqListVOs;
	}
// find list -------------
	
	
	
	
// get entity -------------
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

	/***
	 * 保存开户step-3基本信息
	 * @author scshi
	 * @param basicVo 基本信息总类VO
	 * @date 20160714
	 * */
	public void saveOrUpdateBasicInfo(BasicInfoCombVo basicVo) {
		InvestorAccount iAccount = basicVo.getiAccount();
		if("".equals(iAccount.getId())|| null== iAccount.getId()){
			iAccount.setId(null);
			iAccount.setCreateTime(new Date());
			iAccount.setIsValid("1");
			iAccount.setAccountNo("test");//测试
		}
		iAccount.setLastUpdate(new Date());
		iAccount.setCurStep("Basic");
		this.baseDao.saveOrUpdate(iAccount);//账号信息
		
		InvestorAccountContact iContact = basicVo.getiContact();
		
		if("".equals(iContact.getId()) || null==iContact.getId()){
			iContact.setId(null);
			iContact.setCreateTime(new Date());
		}
		
		iContact.setAccount(iAccount);
		iContact.setContactType("M");
		this.baseDao.saveOrUpdate(iContact);//主账户信息
		
		InvestorAccountContactAddr iContactAddrR = basicVo.getiContactAddrR();
		if(null!=iContactAddrR.getDistrict() && !"".equals(iContactAddrR.getDistrict())){//如果地区为空，不保存
			if(null==iContactAddrR.getId() || "".equals(iContactAddrR.getId())){
				iContactAddrR.setId(null);
				iContactAddrR.setCreateTime(new Date());
				iContactAddrR.setLastUpdate(new Date());
			}
			iContactAddrR.setAccount(iAccount);
			iContactAddrR.setContact(iContact);
			this.baseDao.saveOrUpdate(iContactAddrR);//居住地信息保存
		}
		
		InvestorAccountContactAddr iContactAddrP = basicVo.getiContactAddrP();
		if(null!=iContactAddrP.getDistrict() && !"".equals(iContactAddrP.getDistrict())){
			if(null==iContactAddrP.getId() || "".equals(iContactAddrP.getId())){
				iContactAddrP.setId(null);
				iContactAddrP.setCreateTime(new Date());
				iContactAddrP.setLastUpdate(new Date());
			}
			iContactAddrP.setAccount(iAccount);
			iContactAddrP.setContact(iContact);
			this.baseDao.saveOrUpdate(iContactAddrP);//永久地信息保存
		}
		
		InvestorAccountContactAddr iContactAddrC = basicVo.getiContactAddrC();
		if(null!=iContactAddrC.getDistrict() && !"".equals(iContactAddrC.getDistrict())){
			if(null==iContactAddrC.getId() || "".equals(iContactAddrC.getId())){
				iContactAddrC.setId(null);
				iContactAddrC.setCreateTime(new Date());
				iContactAddrC.setLastUpdate(new Date());
			}
			iContactAddrC.setAccount(iAccount);
			iContactAddrC.setContact(iContact);
			this.baseDao.saveOrUpdate(iContactAddrC);//通信地址保存
		}
		
//		if("J".equals(iAccount.getAccType())){
//			InvestorAccountContactAgeas iContactJoint = basicVo.getiContactJoint();
//			
//			InvestorAccountContactAddrAgeas iContactJointAddr_r = basicVo.getiContactJointAddr_r();
//			
//			InvestorAccountContactAddrAgeas iContactJointAddr_p = basicVo.getiContactJointAddr_p();
//			
//			InvestorAccountContactAddrAgeas iContactJointAddr_c = basicVo.getiContactJointAddr_c();
//			
//		}
		
		InvestorAccountBank iBank = basicVo.getiBank();
		if(null==iBank.getId() || "".equals(iBank.getId())){
			iBank.setId(null);
			iBank.setCreateTime(new Date());
			iBank.setLastUpdate(new Date());
		}
		iBank.setAccount(iAccount);
		this.baseDao.saveOrUpdate(iBank);//银行信息
		
	}
	
	/**
	 * 根据id获取InvestorAccount实体
	 * @param accountId
	 * @return
	 */
	public InvestorAccount findInvestorAccountById(String accountId){
		
		InvestorAccount iAccount = new InvestorAccount();
		if(null==accountId || "".equals(accountId))return iAccount;
		
		String hql = "from InvestorAccount t where t.id=?";
		List<InvestorAccount> list = this.baseDao.find(hql, new String[]{accountId}, false);
		if(list.isEmpty())return iAccount;
		return list.get(0);
	}
	
	/**
	 * 获取账户关联联系人
	 * @author scshi
	 * @param accountid 账户id
	 * @param contactType 联系人类型,M:帐户或者主联系人信息,J:联名帐户联系人信息
	 * */
	public InvestorAccountContact findIContactByType(InvestorAccount account, String contactType,String langCode) {
		InvestorAccountContact contact = new InvestorAccountContact();
		if(null==account || null==account.getId())return contact;

//		String hql = "from InvestorAccountContactAgeas t where t.account.id=? and t.contactType=?";
		String hql = "from InvestorAccountContact t where t.account.id=? and t.contactType=?";

		List params = new ArrayList();
		params.add(account.getId());
		params.add(contactType);
		List<InvestorAccountContact> list = this.baseDao.find(hql, params.toArray(), false);
		if(list.isEmpty()) return contact;
		
		contact = list.get(0);
		String sex = contact.getGender();
		if("F".equals(sex)){
			contact.setSexName("Female");
		}else if("M".equals(sex)){
			contact.setSexName("Male");
		}
		String eduLevel = contact.getLevelOfEducation();//教育程度code
		String occupation = contact.getOccupation();//职业code
		//String investH = contact.getInvestHorizon();//投资期限code
		
		if(null!=eduLevel && !"".equals(eduLevel)){
			String eduLevelName = findParamConfigName(eduLevel,langCode);
			contact.setEduLevelName(eduLevelName);
		}
		if(null!=occupation && !"".equals(occupation)){
			String occaName = findParamConfigName(occupation,langCode);
			contact.setOccupationName(occaName);
		}
//		if(null!=investH && !"".equals(investH)){
//			String investHname = finaParamConfigName(investH,langCode);
//			contact.setInvestHname(investHname);
//		}
		return contact;
		
	}

	/**
	 * 获取账户关联银行信息
	 * @author scshi
	 * @param account 账户
	 * */
	public InvestorAccountBank findBankAgeasByAccid(InvestorAccount iAccount) {
		InvestorAccountBank bank = new InvestorAccountBank();
		if(null==iAccount||null==iAccount.getId())return bank;

//		String hql = "from InvestorAccountBankAgeas t where t.account.id=? ";
		String hql = "from InvestorAccountBank t where t.account.id=? ";

		List<InvestorAccountBank> list = this.baseDao.find(hql, new String[]{iAccount.getId()}, false);
		if(list.isEmpty())return bank;
		return list.get(0);
	}
	
	/**
	 * 获取联系人地址信息
	 * @author scshi
	 * @param contact 联系人
	 * @param addrType R:Residential居住地址 P:Permanent永久地址,C:Correspondence通信地址
	 * */
	public InvestorAccountContactAddr findIContactAddr(InvestorAccountContact iContact, String addrType) {
		InvestorAccountContactAddr contactAddr = new InvestorAccountContactAddr();
		if(null==iContact || null==iContact.getId())return contactAddr;
		String hql = "from InvestorAccountContactAddr t where t.contact.id=? and t.addrType=?";
		List params = new ArrayList();
		params.add(iContact.getId());
		params.add(addrType);
		List<InvestorAccountContactAddr> list = this.baseDao.find(hql, params.toArray(), false);
		if(list.isEmpty())return contactAddr;
		return list.get(0);
	}
	
// get entity -------------
	/**
	 * 通过memberId获取信息
	 * @author michael
	 * @param memberId 成员ID
	 * @return MemberIfa
	 */
	public MemberIfa findIfaByMemberId(String memberId) {
		MemberIfa bank = new MemberIfa();
		String hql = "from MemberIfa t where t.member.id=? ";
		List<MemberIfa> list = this.baseDao.find(hql, new String[]{memberId}, false);
		if(list.isEmpty())return bank;
		return list.get(0);
	}

	 /***
     * 分页查询开户申请列表
     * @author michael
     * @param jsonPaging 分页参数
     * @param loginUser 当前登录用户
	 * @return
     */
	//@Transactional(readOnly = true)
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging,MemberBase loginUser, String queryType){
		return findInvestorAccountList(jsonPaging, loginUser, queryType, null, null, null, null);
	}
	
	 /**
	  * 分页查询开户申请列表
	  * @param jsonPaging 分页参数
	  * @param loginUser 当前登录用户
	  * @param submitSts 提交方：invest投资人提交,ifa提交
	  * @param openSts 开户状态:-2待推送,-1已推送等待结果,1成功开户,0失败
	  * @param finishSts 完成状态：0草稿,1已提交
	  * @param flowSts 流程状态：-1还未执行,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
	  * @param queryType 查询类型：application 申请， approval 审批
	  * @return
	  */
	//@Transactional(readOnly = true)
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging,MemberBase loginUser, String queryType, String submitSts, String openSts, String finishSts, String flowSts){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InvestorAccount a ");
		hql.append(" where a.isValid='1' ");
		
		if(loginUser != null && loginUser.getId() != null && !"".equals(loginUser.getId())){
			if (null!=queryType && "application".equalsIgnoreCase(queryType)){
				hql.append(" and a.member.id = ? ");
				params.add(loginUser.getId());
			}
			
			if (null!=queryType && "approval".equalsIgnoreCase(queryType)){
				//查todo表， flow_state 节点审批状态：0待处理，1已处理
				List<WfProcedureInstanseTodo> todoList = findTodoListByUser("0",loginUser.getId());
				if (null!=todoList && !todoList.isEmpty()){
					String idString = "";
					for (WfProcedureInstanseTodo x: todoList){
						if (!"".equals(idString)) idString += ",";
						idString += "'"+x.getBusinessId()+"'";
					}
					if (!"".equals(idString))
						hql.append(" and a.id in ("+idString+")");
				}else{
					hql.append(" and a.id ='xx' ");
				}
			}
		}
		
		if(null!=submitSts && !"".equals(submitSts)){
			hql.append(" and a.submitStatus = ? ");
			params.add(submitSts);
		}
		
		if(null!=openSts && !"".equals(openSts)){
			hql.append(" and a.openStatus = ? ");
			params.add(openSts);
		}
		
		if(null!=finishSts && !"".equals(finishSts)){
			hql.append(" and a.finishStatus = ? ");
			params.add(finishSts);
		}
		
		if(null!=flowSts && !"".equals(flowSts)){
			hql.append(" and a.flowStatus = ? ");
			params.add(flowSts);
		}
		
		if(jsonPaging.getSort() == null || "".equals(jsonPaging.getSort())){
			jsonPaging.setSort("a.lastUpdate");
			jsonPaging.setOrder("desc");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}
	
	@SuppressWarnings("unchecked")
	private List<WfProcedureInstanseTodo> findTodoListByUser(String flowState, String flowUserId){
		try {
			String hql = "from WfProcedureInstanseTodo where businessId is not null and flowState=? and flowUser.id = ? ";
			
			List params = new ArrayList();
			params.add(Integer.parseInt(flowState));
			params.add(flowUserId);
			List<WfProcedureInstanseTodo> voList = this.baseDao.find(hql, params.toArray(), false);
			
			return voList;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * 账号信息保存
	 * @author scshi
	 * @param account 账号信息
	 * */
	@Override
	public void saveInvestorAccount(InvestorAccount account) {
		InvestorAccount accountVo = (InvestorAccount)this.baseDao.get(InvestorAccount.class, account.getId());
		accountVo.setAccType(account.getAccType());
		accountVo.setInvestType(account.getInvestType());
		accountVo.setCies(account.getCies());
		accountVo.setDividend(account.getDividend());
		accountVo.setBaseCurrency(account.getBaseCurrency());
		accountVo.setPurpose(account.getPurpose());
		accountVo.setPurposeOther(account.getPurposeOther());
		accountVo.setLastUpdate(new Date());
		accountVo.setSentBy(account.getSentBy());
		this.baseDao.saveOrUpdate(accountVo);
	}
	
	/**
	 * 基本信息保存
	 * @author scshi
	 * @param iContact 主要联系人信息
	 * @param accountId 关联账号Id
	 * @param iContactId 主要联系人id
	 * */
	@Override
	public String saveOrUpdateMainContact(InvestorAccountContact iContact,String accountId) {
		if(null == iContact.getId() || "".equals(iContact.getId())){
			iContact.setId(null);
			iContact.setCreateTime(new Date());
		}
		InvestorAccount acc = (InvestorAccount)this.baseDao.get(InvestorAccount.class, accountId);
		iContact.setAccount(acc);
		iContact.setLastUpdate(new Date());
		
		String countryId = iContact.getCountry().getId();
		String nationlId = iContact.getNationality().getId();
		String esiId = iContact.getPrimaryResidence().getId();
		if("".equals(countryId))iContact.setCountry(null);
		if("".equals(nationlId))iContact.setNationality(null);
		if("".equals(esiId))iContact.setPrimaryResidence(null);
		
		this.baseDao.saveOrUpdate(iContact);//主账户信息
		return iContact.getId();
	}
	
	
	/**
	 * 联系人地址保存
	 * @author scshi
	 * @param iContactAddr 地址实体
	 * @param iContactId	关联联系人
	 * @param accountId	关联账号
	 * */
	public String saveOrUpdateIaddr(InvestorAccountContactAddr iContactAddr, String iContactId,String accountId){
		if(null == iContactAddr.getId() || "".equals(iContactAddr.getId())){
			iContactAddr.setId(null);
			iContactAddr.setCreateTime(new Date());
		}
		InvestorAccount acc = (InvestorAccount)this.baseDao.get(InvestorAccount.class, accountId);
		InvestorAccountContact iContact = (InvestorAccountContact)this.baseDao.get(InvestorAccountContact.class, iContactId);
		iContactAddr.setAccount(acc);
		iContactAddr.setContact(iContact);
		iContactAddr.setLastUpdate(new Date());
		
		String countryId = iContactAddr.getCountry().getId();
		if("".equals(countryId)) iContactAddr.setCountry(null);
		
		this.baseDao.saveOrUpdate(iContactAddr);
		return iContactAddr.getId();
	}
	
	/**
	 * 银行保存
	 * @author scshi
	 * @param iBank 银行信息实体
	 * @param iContactId	关联账户
	 * */
	public String saveOrUpdateIbank(InvestorAccountBank iBank, String accountId){
		if(null==iBank.getId() || "".equals(iBank.getId())){
			iBank.setId(null);
			iBank.setCreateTime(new Date());
		}
		InvestorAccount acc = (InvestorAccount)this.baseDao.get(InvestorAccount.class, accountId);
		iBank.setAccount(acc);
		iBank.setLastUpdate(new Date());
		
		this.baseDao.saveOrUpdate(iBank);
		return iBank.getId();
	}	
	
	/**
	 * 通过memberId获取信息
	 * @author michael
	 * @param memberId 成员ID
	 * @return MemberIndividual
	 */
	public MemberIndividual findIndividualByMemberId(String memberId) {
		MemberIndividual bank = new MemberIndividual();
		String hql = "from MemberIndividual t where t.member.id=? ";
		List<MemberIndividual> list = this.baseDao.find(hql, new String[]{memberId}, false);
		if(list.isEmpty())return bank;
		return list.get(0);
	}
	
	/**
	 * 通过memberId获取信息
	 * @author michael
	 * @param memberId 成员ID
	 * @return IndividualDataVO
	 */
	public IndividualDataVO findIndividualFullInfoByMemberId(String memberId){
		IndividualDataVO vo = new IndividualDataVO();
		vo.setBase(memberBaseService.findById(memberId));
		vo.setIndividual(findIndividualByMemberId(memberId));
		return vo;
	}
	
	/**根据基本数据的config_code获取基础数据多语言名称
	 * param configCode 基础数据的code值
	 * param langCode 当前语言
	 * return 基础数据多语言名称
	 * */
	private String findParamConfigName(String configCode,String langCode){
		String hql = "select c.id,c.configCode,c." + this.getLangString("name", langCode) + ",c." + this.getLangString("confValue", langCode);
		hql +=  " from SysParamConfig c where c.isValid='1' and c.configCode=? ";
		List list = this.baseDao.find(hql, new String[]{configCode}, false);
		if(list.isEmpty())return null;
		List<SysParamVO> voList = new ArrayList<SysParamVO>();
		for(int x=0;x<list.size();x++){
			SysParamVO vo=new SysParamVO();
			Object[] objs = (Object[])list.get(x);
			vo.setId(null==objs[0]?"":objs[0].toString());
			vo.setCode(null==objs[1]?"":objs[1].toString());
			vo.setName(null==objs[2]?"":objs[2].toString());
			vo.setValue(null==objs[3]?"":objs[3].toString());
			voList.add(vo);
		}
		return voList.get(0).getName();
	}
	
	/***
	 * 获取账户关联联系人id
	 * @param accountId 账户
	 * @param contactType 联系人类型M--主联系人，J--关联联系人
	 * @return contactId 
	 * */
	public String getAccountContactId(String accountId, String contactType) {

//		String hql = "from InvestorAccountContactAgeas t where t.account.id=? and t.contactType=? order by t.id ";
		String hql = "from InvestorAccountContact t where t.account.id=? and t.contactType=? order by t.id ";

		List params = new ArrayList();
		params.add(accountId);
		params.add(contactType);
		List<InvestorAccountContact> voList = this.baseDao.find(hql, params.toArray(), false);
		if(voList.isEmpty())return null;
		return voList.get(0).getId();
	}
	
	/***
	 * 获取需要检查的文档
	 * modify by mqzou 2016-10-24
	 * @param memberId 账号
	 * @param distrubuteId 代理商
	 * @param contactId 联系人
	 * @param langCode 当前语言
	 * */
	public List<DocListVO> findContactDocList(String memberId,String distrubuteId, String contactId,String langCode){
		String hql = " select t.id,out.docName,t.docTemplate.id,t.isNecessary,t.updateCycle,t.expireDate,t.submitDate,";
		hql += " t.checkStatus ";
		hql += " from InvestorDoc t inner join "+this.getLangString("InvestorDoc", langCode);
		hql += " out on t.id=out.id ";
		hql += " where t.member.id=? and t.distributor.id=? and t.contact.id=? ";
		
		List params = new ArrayList();
		params.add(memberId);
		params.add(distrubuteId);
		params.add(contactId);
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(voList.isEmpty())return null;
		
		List<DocListVO> list = new ArrayList<DocListVO>();
		for(int z=0;z<voList.size();z++){
			Object[] objs = (Object[])voList.get(z);
			DocListVO vo = new DocListVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setDocName(objs[1]==null?"":objs[1].toString());
			vo.setTemplateId(objs[2]==null?"":objs[2].toString());
			vo.setIsNecessary(objs[3]==null?"":objs[3].toString());
			vo.setUpdateCycle(objs[4]==null?0:Integer.parseInt(objs[4].toString()));
			vo.setCreateTime(objs[5]==null?new Date():DateUtil.StringToDate(objs[5].toString(), "yyyy-MM-dd"));
			vo.setEffectDate(objs[6]==null?new Date():DateUtil.StringToDate(objs[6].toString(), "yyyy-MM-dd"));
			vo.setCheckStatus(objs[7]==null?"":objs[7].toString());
			//查询已上传附件
			String fileHql = " from AccessoryFile t where t.relateId=? and t.moduleType=? order by t.createTime desc ";
			List fileParams = new ArrayList();
			fileParams.add(objs[0].toString());
			fileParams.add("investor_doc");
			List<AccessoryFile> fileList = this.baseDao.find(fileHql, fileParams.toArray(), false);
			vo.setFileList(fileList);
			list.add(vo);
		}
		return list;
	}
	
	/**
	 * 从模板复制doclist到invest
	 * @params distrubuteId 代理商
	 * @params clientType 开户类型
	 * @params loginLangFlag 当前语言
	 * @params memberId 当前登录用户
	 * @params mainContactId 联系人id
	 * */
	public void copyDocListfromTemp(String distrubuteId,String clientType, 
			String loginLangFlag, String memberId,String mainContactId){
		
		String docTemplateHql = "select dt.id,out.title ";
		docTemplateHql += " from DocTemplate dt inner join "+this.getLangString("DocTemplate", loginLangFlag);
		docTemplateHql += " out on dt.id=out.id ";
		docTemplateHql += " WHERE dt.isValid = 1 and dt.distributor.id = ? and dt.clientType = ? ";
		List<String> docTemplateParams = new ArrayList<String>();
		docTemplateParams.add(distrubuteId);
		docTemplateParams.add(clientType);
//		docTemplateParams.add(langCode);
		List voList = this.baseDao.find(docTemplateHql, docTemplateParams.toArray(), false);
		List<DocTemplate> docTemplatesList = new ArrayList<DocTemplate>();
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				Object[] objs = (Object[])voList.get(x);
				DocTemplate temp = new DocTemplate();
				String tempId = objs[0]==null?"":objs[0].toString();
				if(!"".equals(tempId)){
					temp = (DocTemplate)this.baseDao.get(DocTemplate.class, tempId);
					temp.setTitle(objs[1]==null?"":objs[1].toString());
				}
				docTemplatesList.add(temp);
			}
		}
		
		
		if(null!=docTemplatesList && !docTemplatesList.isEmpty()){
			String docListHql = " from DocList t where t.template.id = ? ORDER BY t.id ";
			List<String> docListParams = new ArrayList<String>();
			docListParams.add( docTemplatesList.get(0).getId() );
			List docList = this.baseDao.find(docListHql, docListParams.toArray(), false);
			if(!docList.isEmpty()){
				
				MemberBase member = new MemberBase();
				member.setId(memberId);
				
				MemberDistributor distributor = new MemberDistributor();
				distributor.setId(distrubuteId);
				
				DocTemplate docTemplate = docTemplatesList.get(0);
				for(int y=0;y<docList.size();y++){
					DocList docModel = (DocList)docList.get(y);
					String docModelId = docModel.getId();
					InvestorDoc investDoc = new InvestorDoc();
					
					investDoc.setId(null);
					investDoc.setCreateTime(new Date());
					investDoc.setDistributor(distributor);
					investDoc.setMember(member);
					investDoc.setDocTemplate(docTemplate);
					//investDoc.setContactId(mainContactId);
					investDoc.setIsNecessary(docModel.getIsNecessary());
					investDoc.setUpdateCycle(docModel.getUpdateCycle());
					investDoc.setCreateTime(new Date());
					investDoc.setLastUpdate(new Date());
					investDoc.setIsValid("1");
					this.baseDao.saveOrUpdate(investDoc);
					
					//简体中文版checklist保存
					InvestorDocSc investDocSc = new InvestorDocSc();
					DocListSc docModelSc = (DocListSc)this.baseDao.get(DocListSc.class,docModelId );
					investDocSc.setId(investDoc.getId());
					investDocSc.setDocName(docModelSc.getDocName());
					this.baseDao.create(investDocSc);
					
					//繁体
					InvestorDocTc investDocTc = new InvestorDocTc();
					DocListTc docModelTc = (DocListTc)this.baseDao.get(DocListTc.class, docModelId);
					investDocTc.setId(investDoc.getId());
					investDocTc.setDocName(docModelTc.getDocName());
					this.baseDao.create(investDocTc);
					
					//英文
					InvestorDocEn investDocEn = new InvestorDocEn();
					DocListEn docModelEn = (DocListEn)this.baseDao.get(DocListEn.class, docModelId);
					investDocEn.setId(investDoc.getId());
					investDocEn.setDocName(docModelEn.getDocName());
					this.baseDao.create(investDocEn);
				}
			}
		}
	}
	
	
	/**
	 * 搜索提交的文档列表
	 * @param moduleType
	 * @param relateId
	 * @return
	 */
	public List<AccessoryFile> findSubmitDocList(String moduleType, String relateId){
		//查询已上传附件
		String fileHql = " from AccessoryFile t where t.relateId=? and t.moduleType=? order by t.createTime desc ";
		List fileParams = new ArrayList();
		fileParams.add(relateId);
		fileParams.add(moduleType);
		List<AccessoryFile> fileList = this.baseDao.find(fileHql, fileParams.toArray(), false);
		return fileList;
	}
	
	/**
	 * 生成todo记录
	 * @param todo
	 * @return
	 */
	/*public WebToDo saveWebToDo(WebToDo todo) {
		return (WebToDo)baseDao.saveOrUpdate(todo);
	}*/
	
	/**
	 * 生成todo记录
	 * @param todo
	 * @param titleEn
	 * @param titleSc
	 * @param titleTc
	 * @return
	 */
	/*public WebToDo saveWebToDo(WebToDo todo, String titleEn, String titleSc, String titleTc){
		WebToDo newTodo = saveWebToDo(todo);
		
		WebToDoEn en = new WebToDoEn();
		en.setId(newTodo.getId());
		en.setTitle(titleEn);
		baseDao.create(en);
		
		WebToDoSc sc = new WebToDoSc();
		sc.setId(newTodo.getId());
		sc.setTitle(titleSc);
		baseDao.create(sc);

		WebToDoTc tc = new WebToDoTc();
		tc.setId(newTodo.getId());
		tc.setTitle(titleTc);
		baseDao.create(tc);

		return newTodo;
	}*/
	

	/**
	 * 获取Ifa管理的某个客户的投资账号列表
	 * @author zxtan
	 * @date 2016-09-06
	 */
	@Override
	public JsonPaging findInvestorAccountListForIfa(JsonPaging jsonPaging,String ifaMemberId, String customerMemberId){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InvestorAccount l ");
		hql.append(" where l.ifa.member.id=? and l.member.id=? ");
		params.add(ifaMemberId);
		params.add(customerMemberId);
			
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}
	
	/**
	 * 获取Ifa管理的某个客户的投资账号列表
	 * @author zxtan
	 * @date 2016-09-09
	 */
	@Override
	public List<InvestorAccount> findInvestorAccountListForIfa(String ifaMemberId, String customerMemberId,String distributorId){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InvestorAccount l ");
		hql.append(" where l.ifa.member.id=? and l.member.id=? and l.distributor.id=? ");
		params.add(ifaMemberId);
		params.add(customerMemberId);
		params.add(distributorId);
		
		List<InvestorAccount> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		return list;
	}
	
	/**
	 * 获取Ifa管理的某个客户的投资账号文档
	 * @author zxtan
	 * @date 2016-09-09
	 */
	@Override
	public InvestorDoc findInvestorDocForDistributor(String distributorId,String customerMemberId)
	{
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InvestorDoc l ");
		hql.append(" where l.distributor.id=? and l.member.id=? ");
		hql.append(" ORDER BY expireDate"); // IFNULL(expireDate,DATE_ADD(CURRENT_DATE(),INTERVAL 5 YEAR)) 
		params.add(distributorId);
		params.add(customerMemberId);
		
		List<InvestorDoc> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if ( null !=list && !list.isEmpty() ) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 获取Ifa管理的某个客户的投资账号列表
	 * @author zxtan
	 * @date 2016-09-09
	 */
	@Override
	public List<InvestorAccountDistributorVO> findInvestorDistributor(String ifaMemberId,String customerMemberId)
	{
		List<InvestorAccountDistributorVO> voList = new ArrayList<InvestorAccountDistributorVO>();
		StringBuilder hql = new StringBuilder("SELECT DISTINCT  ia.ifa_id,ia.member_id, ia.distributor_id, ");
		hql.append("(SELECT company_name FROM member_distributor WHERE id=ia.distributor_id) as distributor_name");
		hql.append(" FROM investor_account ia INNER JOIN member_ifa mi ON mi.id=ia.ifa_id WHERE mi.member_id='"+ifaMemberId+"' and ia.member_id='"+customerMemberId+"'"); // IFNULL(expireDate,DATE_ADD(CURRENT_DATE(),INTERVAL 5 YEAR)) 
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(customerMemberId);
//		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		List list = springJdbcQueryManager.springJdbcQueryForList(hql.toString());
		
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Map map = (HashMap) list.get(i);
				
				InvestorAccountDistributorVO vo= new InvestorAccountDistributorVO();
				String ifaId = map.get("ifa_id") == null? "" : map.get("ifa_id").toString();
				String memberId = map.get("member_id") == null? "" : map.get("member_id").toString();
				String distributorId = map.get("distributor_id") == null? "" : map.get("distributor_id").toString();
				String distributorName = map.get("distributor_name") == null? "" : map.get("distributor_name").toString();
				
				vo.setIfaId(ifaId);
				vo.setMemberId(memberId);
				vo.setDistributorId(distributorId);
				vo.setDistributorName(distributorName);

				InvestorDoc docObj = findInvestorDocForDistributor(distributorId, customerMemberId);
				Date expireDate = docObj == null ? null:docObj.getExpireDate();
				vo.setNextDocCheckDate(expireDate);
				String eTime = DateUtil.getDateStr(expireDate);
				String sTime = DateUtil.getCurDateStr();
				Long days = expireDate == null? null : DateUtil.getDaysOfTwoDate(sTime,eTime);
				vo.setNextDocCheckDays(days == null? null: days.intValue());
				
				List<InvestorAccount> accountList = findInvestorAccountListForIfa(ifaMemberId, customerMemberId,distributorId);
				vo.setAccountList(accountList);
				
				voList.add(vo);
			}	
		}
		return voList;
	}

	@Override
	public List<InvestorAccount> findAllAccountList(InvestorAccount account) {
		String hql=" from InvestorAccount r where   (r.openStatus='-1' or r.openStatus='1')";
		if(null!=account){
			if(null!=account.getMember() && null!=account.getMember().getId() && !"".equals(account.getMember().getId()) && !"null".equals(account.getMember().getId())){
				hql+=" and r.member.id='"+account.getMember().getId()+"'";
			}
			if(null!=account.getOpenStatus() && !"".equals(account.getOpenStatus())){
				hql+=" and r.openStatus in ("+account.getOpenStatus()+")";
			}
			if(null!=account.getBaseCurrency() && !"".equals(account.getBaseCurrency())){
				hql+=" and r.baseCurrency='"+account.getBaseCurrency()+"'";
			}
			if(null!=account.getIsValid() && !"".equals(account.getIsValid())){
				hql+=" and r.isValid='"+account.getIsValid()+"'";
			}else {
				hql+=" and r.isValid='1'";
			}
			if(null!=account.getIfa()){
				if(null!=account.getIfa().getId() && !"".equals(account.getIfa().getId())){
					hql+=" and r.ifa.id='"+account.getIfa().getId()+"'";
				}
				if(null!=account.getIfa().getIfafirm() && null!=account.getIfa().getIfafirm().getId() && !"".equals(account.getIfa().getIfafirm().getId())){
					hql+=" and r.ifa.ifafirm.id='"+account.getIfa().getIfafirm().getId()+"'";
				}
			}
			
			if(null!=account.getDistributor()){
				hql+=" and r.distributor.id='"+account.getDistributor().getId()+"'";
			}
			if(null!=account.getAccountNo() && !"".equals(account.getAccountNo())){
				hql+=" and r.member.loginCode='"+account.getAccountNo()+"'";
			}
		}
		hql+=" order by r.lastUpdate desc";
		List reList=this.baseDao.find(hql, null, false);
		List<InvestorAccount> list=new ArrayList<InvestorAccount>();
		Iterator it=reList.iterator();
		while (it.hasNext()) {
			InvestorAccount investorAccount = (InvestorAccount) it.next();
			list.add(investorAccount);
		}
		return list;
	}

	@Override
	public List findInvestorDistributor(InvestorAccount account) {

		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT DISTINCT r.`distributor_id`,m.`company_name`,p.expire_date rpqDate,d.expire_date docDate FROM investor_account r ");
		sql.append(" LEFT JOIN member_distributor m ON r.distributor_id=m.id");
		sql.append(" LEFT JOIN (SELECT * FROM  `rpq_exam`  ORDER BY rpq_exam.expire_date DESC LIMIT 1)p ON r.`member_id`=p.member_id AND r.`distributor_id`=p.distributor_id");
		sql.append(" LEFT JOIN (SELECT * FROM  `investor_doc` ORDER BY expire_date ASC LIMIT 1 )d ON r.`member_id`=d.member_id AND r.`distributor_id`=p.distributor_id");
		sql.append(" WHERE     (r.open_status='-1' or r.open_status='1') ");
		
		if(null!=account){
			if(null!=account.getMember()&& null!=account.getMember().getId() && !"".equals(account.getMember().getId())){
				sql.append(" and r.`member_id`='"+account.getMember().getId()+"'");
			}
			if(null!=account.getOpenStatus() && !"".equals(account.getOpenStatus())){
				sql.append(" and r.open_status in ("+account.getOpenStatus()+")");
			}
			if(null!=account.getBaseCurrency() && !"".equals(account.getBaseCurrency())){
				sql.append(" and r.base_currency='"+account.getBaseCurrency()+"'");
			}
			if(null!=account.getIsValid() && !"".equals(account.getIsValid())){
				sql.append(" and r.is_valid='"+account.getIsValid()+"'");
			}else {
				sql.append(" and r.`is_valid`='1' ");
			}
			if(null!=account.getIfa() && null!=account.getIfa().getId() && !"".equals(account.getIfa().getId())){
				sql.append(" and r.ifa_id='"+account.getIfa().getId()+"'");
			}
			if(null!=account.getDistributor() && null!=account.getDistributor().getId() && !"".equals(account.getDistributor().getId())){
				sql.append(" and r.distributor_id='"+account.getDistributor().getId()+"'");
			}
		}
		
		List reList=this.springJdbcQueryManager.springJdbcQueryForList(sql.toString());
		List list=new ArrayList();
		Iterator it=reList.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			InvestorDistributorVO vo=new InvestorDistributorVO();
			vo.setCompanyName(getMapObject(map, "company_name"));
			vo.setId(getMapObject(map, "distributor_id"));
			vo.setLogoUrl("");
			//vo.setMemberId(getMapObject(map, "member_id"));
			String rpqDate=getMapObject(map, "rpqDate");
			String dcDate=getMapObject(map, "docDate");
			if("".equals(rpqDate)){
				vo.setNextRPQDate("0");
			}
			if("".equals(dcDate)){
				vo.setNextDCDate("0");
			}
			list.add(vo);
		}
		
		return list;
	}
	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}

	@Override
	public List findAccountIfafirm(InvestorAccount account) {
		StringBuilder sql=new StringBuilder();
		sql.append(" SELECT DISTINCT  f.`id`,f.`company_name` FROM investor_account i LEFT JOIN member_ifa m ON i.`ifa_id`=m.`id`");
		sql.append(" LEFT JOIN member_ifafirm f ON m.`company_ifafirm_id`=f.`id` ");
		sql.append(" WHERE     (i.open_status='-1' or i.open_status='1') and  f.`company_name` IS NOT NULL ");
		if(null!=account.getMember()&& null!=account.getMember().getId() && !"".equals(account.getMember().getId())){
			sql.append(" and i.`member_id`='"+account.getMember().getId()+"'");
		}
		if(null!=account.getOpenStatus() && !"".equals(account.getOpenStatus())){
			sql.append(" and i.open_status in ("+account.getOpenStatus()+")");
		}
		if(null!=account.getBaseCurrency() && !"".equals(account.getBaseCurrency())){
			sql.append(" and i.base_currency='"+account.getBaseCurrency()+"'");
		}
		if(null!=account.getIsValid() && !"".equals(account.getIsValid())){
			sql.append(" and i.is_valid='"+account.getIsValid()+"'");
		}else {
			sql.append(" and i.`is_valid`='1' ");
		}
		
		List reList=this.springJdbcQueryManager.springJdbcQueryForList(sql.toString());
		List list=new ArrayList();
		Iterator it=reList.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			InvestorDistributorVO vo=new InvestorDistributorVO();
			vo.setCompanyName(getMapObject(map, "company_name"));
			vo.setId(getMapObject(map, "id"));
			vo.setLogoUrl("");
			
			list.add(vo);
		}
		return list;
	}
	@Override
	public List findAccountMember(InvestorAccount account) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<InvestorAccount> findSubAccountList(InvestorAccount account) {
		String hql=" from InvestorAccount r where  r.masterAccount.id='"+account.getId()+"' and r.isValid='1' and (r.openStatus='-1'  or r.openStatus='1')";
		List resultList=this.baseDao.find(hql, null, false);
		List<InvestorAccount> list=new ArrayList<InvestorAccount>();
		Iterator it=resultList.iterator();
		while (it.hasNext()) {
			InvestorAccount investorAccount = (InvestorAccount) it.next();
			list.add(investorAccount);
		}
		return list;
	}
	
	/*******************************投资人KYC,背景调查、培训*****************************************/
	/**
	 * 获取投资人背景调查
	 * @author wwlin
	 * @date 2016-09-18
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<InvestorBackground> findInvestorbackground( String memberId){
		String hql = "from InvestorBackground t where t.member.id = ?  ";
		List<String> params = new ArrayList<String>();
		params.add( memberId );
		
		List<InvestorBackground> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 保存投资人背景调查
	 * @author wwlin
	 * @param account 背景调查基本信息实体
	 * @return InvestorAccount
	 */
	@Override
	public InvestorBackground saveOrUpdateInvestorBackground(InvestorBackground model){
		return (InvestorBackground) baseDao.saveOrUpdate(model);
	}
	
	/***
     * 通过ID获取背景信息
     * @author 林文伟
     * @date 2016-09-19
     */
	@Override
	public InvestorBackground getInvestorBackground(String id) {
		Object obj = (InvestorBackground) baseDao.get(InvestorBackground.class, id);
		if(obj!=null)
		{
			return (InvestorBackground)obj;
		} else return null;
	}
	
	/***
     * 删除背景信息
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean delInvestorBackground(String id)
	{
		String hql1 = " delete InvestorBackground where  id =?  ";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		
		return rs>0?true:false;
	}
	
	/**
	 * 获取投资人培训信息
	 * @author wwlin
	 * @date 2016-09-18
	 */
	@Override
	//@Transactional(readOnly=true)
	public List<InvestorTraining> findInvestorTraining( String memberId){
		String hql = "from InvestorTraining t where t.member.id = ?  ";
		List<String> params = new ArrayList<String>();
		params.add( memberId );
		
		List<InvestorTraining> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 保存投资人培训信息
	 * @author wwlin
	 * @param account 培训信息基本信息实体
	 * @return InvestorAccount
	 */
	@Override
	public InvestorTraining saveOrUpdateInvestorTraining(InvestorTraining model){
		return (InvestorTraining) baseDao.saveOrUpdate(model);
	}

	@Override
	public List<WfProcedureInstanseTodo> findAccountWfHistory(String accountId) {
		String hql=" from WfProcedureInstanseTodo r where r.businessId='"+accountId+"' ";
		hql+=" order by r.createTime desc";
		List reList=this.baseDao.find(hql, null, false);
		List<WfProcedureInstanseTodo> list=new ArrayList<WfProcedureInstanseTodo>();
		Iterator it=reList.iterator();
		while (it.hasNext()) {
			WfProcedureInstanseTodo todo = (WfProcedureInstanseTodo) it.next();
			list.add(todo);
		}
		return list;
	}
	
	/***
     * 删除投资人培训信息
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean delInvestorTraining(String id)
	{
		String hql1 = " delete InvestorTraining where  id =?  ";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		int rs = this.baseDao.updateHql(hql1, params.toArray());
		
		return rs>0?true:false;
	}
	
	/***
     * IFA问卷列表查询的方法
     * @author 林文伟
     * @date 2016-09-25
     */
	@Override
	public List<RpqExam> findRpqExamByMemberId(String langCode,String memberId) {
		String hql=" from RpqExam r  ";
		hql +=" inner join "+this.getLangString("RpqExam", langCode);
		hql += " l on l.id=r.id ";
		hql += " where r.member.id = '"+memberId+"'";
		//List<Object> params=new ArrayList<Object>();
		hql += " order by r.createTime desc";
		//System.out.println("3333333333333"+hql);
		List<RpqExam> rpqExamList = new ArrayList<RpqExam>();
		List list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				//System.out.println("3333333333333");
				RpqExam vo = new RpqExam();
				 Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//RpqExam实体
				 Object questObj3 =(Object)each2[1];//多语言实体
				 if(questObj2 instanceof RpqExam)
				 {
					 vo = ((RpqExam) questObj2);
				 }
				 if(questObj3 instanceof RpqExamEn)
				 {
					 //System.out.println("5555555555555");
					 String title = ((RpqExamEn) questObj3).getTitle();
					 vo.setTitle(title);
				 }
				 if(questObj3 instanceof RpqExamSc)
				 {
					 //System.out.println("5555555555555");
					 String title = ((RpqExamSc) questObj3).getTitle();
					 vo.setTitle(title);
				 }
				 if(questObj3 instanceof RpqExamTc)
				 {
					 //System.out.println("5555555555555");
					 String title = ((RpqExamTc) questObj3).getTitle();
					 vo.setTitle(title);
				 }
				 rpqExamList.add(vo);
			}
		}
		
		return rpqExamList;
	}
	
	/***
     * 账号的交易历右记录
     * @author 林文伟
     * @date 2016-09-27
     */
	@Override
	public List<OrderReturn> findAccountOrderReturn(String dataValue,String accountId) {
		String hql=" from OrderReturn r  ";
		hql +=" inner join OrderHistory l";
		hql += " on l.id=r.order.id ";
		hql += " where l.account.id = '"+accountId+"'";
		if(""!=dataValue){
			if("today".equals(dataValue)){
				hql += " and TO_DAYS(r.transactionTime) = TO_DAYS(NOW()) "; //查询是今天的
			}
			else if("1w".equals(dataValue)){
				hql += " and DATEDIFF(NOW(),r.transactionTime)<7 and DATEDIFF(NOW(),r.transactionTime)>0   "; //查询1星期内
			}
			else if("1m".equals(dataValue)){
				//hql += " and r.transactionTime between DATE_SUB(now(),interval 1 month) and now()   "; //
				hql += " and DATE_FORMAT(r.transactionTime, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )";
			}
			else if("2m".equals(dataValue)){
				//hql += " and r.transactionTime between DATE_SUB(now(),interval 2 month) and now()   "; //
				hql += " and DATEDIFF(NOW(),r.transactionTime)<60 and DATEDIFF(NOW(),r.transactionTime)>0 ";
			}
			else if("3m".equals(dataValue)){
				hql += " and DATEDIFF(NOW(),r.transactionTime)<90 and DATEDIFF(NOW(),r.transactionTime)>0   "; //
			}
			else if("6m".equals(dataValue)){
				hql += " and DATEDIFF(NOW(),r.transactionTime)<180 and DATEDIFF(NOW(),r.transactionTime)>0  "; //
			}
			else if("1y".equals(dataValue)){
				hql += " and YEAR(r.transactionTime)=YEAR(NOW())  "; //
			}
		}
		hql += " order by r.transactionTime desc";
		List<OrderReturn> orderReturnList = new ArrayList<OrderReturn>();
		List list1 = this.baseDao.find(hql, null, false);
		if(!list1.isEmpty()){
			for(int x=0;x<list1.size();x++){
				OrderReturn vo = new OrderReturn();
				 Object[] each2 = (Object[])list1.get(x);
				 Object questObj2 =(Object)each2[0];//OrderReturn实体
				 Object questObj3 =(Object)each2[1];//OrderHistory实体
				 if(questObj2 instanceof OrderReturn)
				 {
					 vo = ((OrderReturn) questObj2);
				 }
				 if(questObj3 instanceof OrderHistory)
				 {
					 //System.out.println("5555555555555");
					 //String title = ((OrderHistory) questObj3).getTitle();
					 //vo.setTitle(title);
				 }
				
				 orderReturnList.add(vo);
			}
		}
		
		return orderReturnList;
	}
	
	@Override
	public List<MemberBase> findInvestorList(MemberBase memberBase,String noIfa) {
		String hql=" from MemberBase r where r.isValid='1' and r.subMemberType='11'";
		
		List params=new ArrayList();
		if(null!=memberBase){
			if(null!=memberBase.getInvestStyle() && !"".equals(memberBase.getInvestStyle())){
				hql+=" and r.investStyle like ?";
				params.add("%"+memberBase.getInvestStyle()+"%");
			}
			if(null!=memberBase.getHobbyType() && !"".equals(memberBase.getHobbyType())){
				hql+=" and r.hobbyType  like ?";
				params.add("%"+memberBase.getHobbyType()+"%");
			}
			if(null!=memberBase.getLangCode() && !"".equals(memberBase.getLangCode())){
				hql+=" and r.langCode=?";
				params.add(memberBase.getLangCode());
			}
			
		}
		if("1".equals(noIfa)){
			hql+=" and r.id not in (select i.member.id from InvestorAccount i where i.ifa is not null)";
		}
		List rList=this.baseDao.find(hql, params.toArray(), false);
		Iterator it=rList.iterator();
		List list=new ArrayList();
		while (it.hasNext()) {
			MemberBase object = (MemberBase) it.next();
			list.add(object);
			
		}
		return list;
	}
	
	
	/**
	 * 获取文档审核记录
	 * @author scshi
	 * @date 20160928
	 * @param docId
	 * */
	public List<InvestorDocCheck> findDocCheckList(String docId){
		StringBuffer strBuf = new StringBuffer("from InvestorDocCheck  t ");
		strBuf.append(" where t.docId=? order by t.checkTime desc ");
		List<InvestorDocCheck> list = this.baseDao.find(strBuf.toString(), new String[]{docId}, false);
		if(null!=list && !list.isEmpty())
			return list;
		return null;
		
	}
	
	/**
	 * 获取文档详细信息
	 * @author scshi_u330p
	 * @date 20161009
	 * @param docId 文档id
	 * @param langCode 语言设置
	 * */
	public InvestorDoc findDocInfoById(String docId,String langCode){
		StringBuffer strBuf = new StringBuffer("select t.id,out.docName,t.updateCycle,t.submitDate,t.expireDate,t.checkStatus ");
		strBuf.append("  from InvestorDoc t inner join "+this.getLangString("InvestorDoc", langCode));
		strBuf.append(" out on out.id=t.id ");
		strBuf.append(" where t.id=? order by t.lastUpdate desc ");
		
		List list = this.baseDao.find(strBuf.toString(),new String[]{docId},false);
		if(null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			InvestorDoc doc = new InvestorDoc();
			doc.setId(objs[0]==null?"":objs[0].toString());
			//doc.setDocName(objs[1]==null?"":objs[1].toString());
			doc.setUpdateCycle(objs[2]==null?0:Integer.parseInt(objs[2].toString()));
			doc.setSubmitDate(objs[3]==null?null:DateUtil.StringToDate(objs[3].toString(), "yyyy-MM-dd hh:mm:ss"));
			doc.setExpireDate(objs[4]==null?null:DateUtil.StringToDate(objs[4].toString(), "yyyy-MM-dd hh:mm:ss"));
			doc.setCheckStatus(objs[5]==null?"":objs[5].toString());
			return doc;
		}
		return null;
	}
	
	/**
	 * 获取Ifa管理的某个客户的投资账号信息
	 * @author wwluo
	 * @date 2016-10-14
	 */
	@Override
	public List<InvestorAccount> findInvestorAccounts(String ifaMemberId,String customerMemberId){
		List<InvestorAccount> investorAccounts = null;
		if (StringUtils.isNotBlank(ifaMemberId) && 
				StringUtils.isNotBlank(customerMemberId)) {
			StringBuffer hql = new StringBuffer(" from InvestorAccount where isValid=1 and ifa_id =? and member_id =?");
			List params = new ArrayList();
			params.add(ifaMemberId);
			params.add(customerMemberId);
			investorAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return investorAccounts;
	}
}
