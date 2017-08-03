/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.investor.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.util.UriEncoder;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysAdmin;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysParamVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.vo.CommunityNewsListVO;
import com.fsll.wmes.community.vo.FrontCommunityTopicVO;
import com.fsll.wmes.community.vo.TopicDetailVO;
import com.fsll.wmes.crm.vo.ClientSearchVO;
import com.fsll.wmes.crm.vo.InvestorAccountDistributorVO;
import com.fsll.wmes.entity.CommunityRule;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.DocList;
import com.fsll.wmes.entity.DocListEn;
import com.fsll.wmes.entity.DocListSc;
import com.fsll.wmes.entity.DocListTc;
import com.fsll.wmes.entity.DocTemplate;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.FundReturnEn;
import com.fsll.wmes.entity.FundReturnSc;
import com.fsll.wmes.entity.FundReturnTc;
import com.fsll.wmes.entity.IfaDistributor;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.InvestorAccountBank;
import com.fsll.wmes.entity.InvestorAccountContact;
import com.fsll.wmes.entity.InvestorAccountContactAddr;
import com.fsll.wmes.entity.InvestorAccountCurrency;
import com.fsll.wmes.entity.InvestorAccountDeclaration;
import com.fsll.wmes.entity.InvestorAccountStream;
import com.fsll.wmes.entity.InvestorBackground;
import com.fsll.wmes.entity.InvestorDoc;
import com.fsll.wmes.entity.InvestorDocCheck;
import com.fsll.wmes.entity.InvestorDocEn;
import com.fsll.wmes.entity.InvestorDocSc;
import com.fsll.wmes.entity.InvestorDocTc;
import com.fsll.wmes.entity.InvestorReport;
import com.fsll.wmes.entity.InvestorTraining;
import com.fsll.wmes.entity.MemberAccountSso;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberConsoleCheckRole;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.entity.MemberIfafirmEn;
import com.fsll.wmes.entity.MemberIfafirmSc;
import com.fsll.wmes.entity.MemberIfafirmTc;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.MyAsset;
import com.fsll.wmes.entity.MyAssetHis;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.RpqExam;
import com.fsll.wmes.entity.RpqExamEn;
import com.fsll.wmes.entity.RpqExamSc;
import com.fsll.wmes.entity.RpqExamTc;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageLevel;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.StrategyAllocation;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WfProcedureInstanseTodo;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundAnnoVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.ifa.vo.IfaClientAccountVO;
import com.fsll.wmes.ifa.vo.StrategyInfoVO;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.investor.vo.IndividualDataVO;
import com.fsll.wmes.investor.vo.InvestorAccountCurrencyVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;
import com.fsll.wmes.investor.vo.InvestorDistributorVO;
import com.fsll.wmes.investor.vo.InvestorHomeMostSelectedFundsVO;
import com.fsll.wmes.investor.vo.InvestorHomeTopPerformanceFundsVO;
import com.fsll.wmes.investor.vo.InvestorHomeTopPerformancePortfolioVO;
import com.fsll.wmes.investor.vo.InvestorMyPortfolioVO;
import com.fsll.wmes.investor.vo.InvestorMyPortfolios;
import com.fsll.wmes.investor.vo.MyAssetChartDataVO;
import com.fsll.wmes.investor.vo.PortfolioAssetsVO;
import com.fsll.wmes.investor.vo.WebFriendVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.member.vo.BasicInfoCombVo;
import com.fsll.wmes.member.vo.DocListVO;
import com.fsll.wmes.member.vo.IfaVO;
import com.fsll.wmes.news.vo.NewsInfoVO;
import com.fsll.wmes.notice.vo.NoticeVO;
import com.fsll.wmes.portfolio.vo.AssetsTotalVo;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;
import com.fsll.wmes.rpq.vo.RpqListVO;
import com.fsll.wmes.strategy.vo.CharDataVO;
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
	@Autowired
	private FundInfoService fundInfoService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	@Autowired
	private SysParamService sysParamService;
	/*@Autowired
	private PortfolioHoldService portfolioHoldService;*/
	
	@Autowired
	private IfaMigrateHistService ifaMigrateHistService;
	
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
					} else {
						docListVO.setIsNecessary("NO");
					}
					if (null != investorDocList && !investorDocList.isEmpty()) {
						String accessoryFileHql = "from AccessoryFile t where t.moduleType = ? and t.relateId = ?";
						List<String> accessoryFileParams = new ArrayList<String>();
						accessoryFileParams.add("investor_doc");
						accessoryFileParams.add(investorDocList.get(0).getId());
						List<AccessoryFile> files = this.baseDao.find(accessoryFileHql, accessoryFileParams.toArray(), false);
						docListVO.setFileList(files);
						// System.out.println(
						// "** AccessoryFile files:"+files.size() +
						// " <> investorDocList.get(0).getId():"+investorDocList.get(0).getId()
						// );
						// if ( null != files && !files.isEmpty() ) {
						// docListVO.setAccessoryFileId( files.get(0).getId() );
						// docListVO.setAccessoryFileName(
						// files.get(0).getFileName() );
						// docListVO.setAccessoryFileCreateTime(
						// files.get(0).getCreateTime() );
						// }else{
						// docListVO.setAccessoryFileId("");
						// docListVO.setAccessoryFileName("");
						// docListVO.setAccessoryFileCreateTime(new Date());
						// }
					} else {
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
	//@Transactional(readOnly = true)
	public InvestorAccountDeclaration findInvestorAccountDeclarationAgeasForAccount(String accountId) {
		// String hql =
		// "from InvestorAccountDeclarationAgeas t where t.account.id = ?";
		String hql = "from InvestorAccountDeclaration t where t.account.id = ?";
		List<String> params = new ArrayList<String>();
		params.add(accountId);
		List<InvestorAccountDeclaration> ageas = this.baseDao.find(hql, params.toArray(), false);
		if (null != ageas && !ageas.isEmpty()) {
			return ageas.get(0);
		} else {
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
	//@Transactional(readOnly = true)
	public List<RpqQuestItem> findRpqQuestItemList(String questId) {
		String hql = "from RpqQuestItem t where t.quest.id = ? order by t.orderBy ";
		List<String> params = new ArrayList<String>();
		params.add(questId);

		List<RpqQuestItem> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}


	/**
	 * 通过会员ID查询IFA的客户列表(通过webfriend表关联)
	 * 
	 * @author michael
	 * @param memberId IFA会员ID
	 * @param clientType 客户类型：Buddy， Client， Advisor， Prospect
	 * @return list IFA好友列表
	 */
	public List<MemberIndividual> findClientByIFA(String memberId, String clientType) {
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
	 * 通过会员ID查询IFA的客户列表(通过CrmCustomer表关联)
	 * 
	 * @author michael
	 * @param ifaId IFA会员ID
	 * @return list IFA客户列表
	 */
	public List<MemberIndividual> findCustomerByIFA(String ifaId) {
		List<MemberIndividual> list = new ArrayList<MemberIndividual>();
		List<String> params = new ArrayList<String>();

		String hql = "select m from MemberIndividual m left join MemberBase b on m.member.id=b.id inner join CrmCustomer f on m.member.id = f.member.id where 1=1 ";
		hql += " and b.isValid='1' ";
		hql += " and f.ifa.id = ? ";
		params.add(ifaId);

		hql += " order by m.firstName ";

		list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}

	/**
	 * 通过IFA ID查询IFA关联的代理商
	 * 
	 * @author michael
	 * @param ifaId IFA会员ID
	 * @return list IFA关联的代理商列表
	 */
	@Override
	//@Transactional(readOnly = true)
	public List<MemberDistributor> findIfaDistributors(String ifaId) {
		List<MemberDistributor> list = new ArrayList<MemberDistributor>();
		List<String> params = new ArrayList<String>();

		String hql = "select distinct m from MemberDistributor m inner join IfafirmDistributor f on m.id=f.distributor.id inner join MemberIfa i on  i.ifafirm.id = f.ifafirm.id where 1=1 ";
		// if (null!=ifaId && !"".equals(ifaId)){
		hql += " and i.member.id = ? ";
		params.add(ifaId);
		// }
		hql += " order by m.companyName ";

		list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}

	/**
	 * 通过会员ID查询IFA的同事
	 * 
	 * @author michael
	 * @param memberId
	 *            排除当前会员ID
	 * @param ifaFirmId
	 *            IFA公司ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaColleagues(String memberId, String ifaFirmId) {
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
	 * 
	 * @author michael
	 * @param memberId
	 *            会员ID
	 * @param ifaFirmId
	 *            IFA公司ID
	 * @param ifaTeamId
	 *            IFA组ID
	 * @return list IFA好友列表
	 */
	public List<MemberIfa> findIfaTeamColleagues(String memberId, String ifaFirmId, String ifaTeamId) {
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
	//@Transactional(readOnly = true)
	public List<RpqPageQuest> findRpqPageQuest(String pageId) {
		String hql = "from RpqPageQuest t where t.page.id = ? order by t.orderBy ";
		List<String> params = new ArrayList<String>();
		params.add(pageId);

		List<RpqPageQuest> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}

	@Override
	//@Transactional(readOnly = true)
	public List<RpqPage> findRpqPage(String type, String distributorId, String clientType, String langCode, String isDef, String pageType) {
		String hql = "from RpqPage t where t.type = ? and t.distributor.id = ? and t.clientType = ? and t.langCode = ? and t.isDef = ? and t.pageType = ? ";
		List<String> params = new ArrayList<String>();
		params.add(type);
		params.add(distributorId);
		params.add(clientType);
		params.add(langCode);
		params.add(isDef);
		params.add(pageType);

		List<RpqPage> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	//@Transactional(readOnly = true)
	public List<RpqListVO> findRpqQuestList(String type, String distributorId, String clientType, String langCode, String isDef, String pageType) {

		// 返回的结果list
		List<RpqListVO> rpqListVOs = new ArrayList<RpqListVO>();

		// 确定使用的问卷
		List<RpqPage> rpqPageList = this.findRpqPage(type, distributorId, clientType, langCode, isDef, pageType);
		RpqPage rpqPage = null;

		// 确定问卷的问题列表
		List<RpqPageQuest> rpqPageQuestList = null;
		if (null != rpqPageList && !rpqPageList.isEmpty()) {
			rpqPage = rpqPageList.get(0);
			// System.out.println( "** 这里获得的问卷id："+rpqPage.getId() );

			rpqPageQuestList = this.findRpqPageQuest(rpqPage.getId());
			for (int i = 0; i < rpqPageQuestList.size(); i++) {
				RpqListVO rpqListVO = new RpqListVO();
				rpqListVO.setRpqPageid(rpqPage.getId());
				rpqListVO.setRpqPageTitle(rpqPage.getTitle());
				rpqListVO.setRpqPageRemark(rpqPage.getRemark());
				rpqListVO.setType(rpqPage.getType());
				rpqListVO.setDistributorId(rpqPage.getDistributor().getId());
				rpqListVO.setIsValid(rpqPage.getIsValid());
				rpqListVO.setQuestId(rpqPageQuestList.get(i).getQuest().getId());
				rpqListVO.setQuestOrder(rpqPageQuestList.get(i).getOrderBy());
				rpqListVO.setQuestTitle((i + 1) + "." + rpqPageQuestList.get(i).getQuest().getTitle());
				rpqListVOs.add(rpqListVO);
			}
		} else {
			return rpqListVOs;
		}

		// 获取各条问题的可选项
		RpqListVO rpqListVOForFillItem = null;
		if (null != rpqPageQuestList && !rpqPageQuestList.isEmpty()) {
			for (int i = 0; i < rpqListVOs.size(); i++) {
				rpqListVOForFillItem = rpqListVOs.get(i);
				rpqListVOs.get(i).setRpqQuestItemList(this.findRpqQuestItemList(rpqListVOForFillItem.getQuestId()));
			}
		}

		return rpqListVOs;
	}

	// find list -------------

	// get entity -------------
	@Override
	public RpqPage getRpqPage(String id) {
		if (null == id) {
			return null;
		}
		return (RpqPage) this.baseDao.get(RpqPage.class, id);
	}

	@Override
	public RpqPageLevel getRpqPageLevel(String id) {
		if (null == id) {
			return null;
		}
		return (RpqPageLevel) this.baseDao.get(RpqPageLevel.class, id);
	}

	@Override
	public RpqQuest getRpqQuest(String id) {
		if (null == id) {
			return null;
		}
		return (RpqQuest) this.baseDao.get(RpqQuest.class, id);
	}

	@Override
	public MemberDistributor getMemberDistributor(String id) {
		if (null == id) {
			return null;
		}
		return (MemberDistributor) this.baseDao.get(MemberDistributor.class, id);
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	public AccessoryFile getAccessoryFile(String id) {
		if (null == id) {
			return null;
		}
		return (AccessoryFile) this.baseDao.get(AccessoryFile.class, id);
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	public DocTemplate getDocTemplate(String id) {
		if (null == id) {
			return null;
		}
		return (DocTemplate) this.baseDao.get(DocTemplate.class, id);
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	public InvestorAccountDeclaration getInvestorAccountDeclarationAgeas(String id) {
		if (null == id) {
			return null;
		}
		return (InvestorAccountDeclaration) this.baseDao.get(InvestorAccountDeclaration.class, id);
	}

	/**
	 * @author tejay zhu
	 */
	@Override
	public InvestorDoc getInvestorDoc(String id) {
		if (null == id) {
			return null;
		}
		return (InvestorDoc) this.baseDao.get(InvestorDoc.class, id);
	}

	/***
	 * 保存开户step-3基本信息
	 * 
	 * @author scshi
	 * @param basicVo
	 *            基本信息总类VO
	 * @date 20160714
	 * */
	public void saveOrUpdateBasicInfo(BasicInfoCombVo basicVo) {
		InvestorAccount iAccount = basicVo.getiAccount();
		if ("".equals(iAccount.getId()) || null == iAccount.getId()) {
			iAccount.setId(null);
			iAccount.setCreateTime(new Date());
			iAccount.setIsValid("1");
			iAccount.setAccountNo("test");// 测试
		}
		iAccount.setLastUpdate(new Date());
		iAccount.setCurStep("Basic");
		this.baseDao.saveOrUpdate(iAccount);// 账号信息

		InvestorAccountContact iContact = basicVo.getiContact();

		if ("".equals(iContact.getId()) || null == iContact.getId()) {
			iContact.setId(null);
			iContact.setCreateTime(new Date());
		}

		iContact.setAccount(iAccount);
		iContact.setContactType("M");
		this.baseDao.saveOrUpdate(iContact);// 主账户信息

		InvestorAccountContactAddr iContactAddrR = basicVo.getiContactAddrR();
		if (null != iContactAddrR.getDistrict() && !"".equals(iContactAddrR.getDistrict())) {// 如果地区为空，不保存
			if (null == iContactAddrR.getId() || "".equals(iContactAddrR.getId())) {
				iContactAddrR.setId(null);
				iContactAddrR.setCreateTime(new Date());
				iContactAddrR.setLastUpdate(new Date());
			}
			iContactAddrR.setAccount(iAccount);
			iContactAddrR.setContact(iContact);
			this.baseDao.saveOrUpdate(iContactAddrR);// 居住地信息保存
		}

		InvestorAccountContactAddr iContactAddrP = basicVo.getiContactAddrP();
		if (null != iContactAddrP.getDistrict() && !"".equals(iContactAddrP.getDistrict())) {
			if (null == iContactAddrP.getId() || "".equals(iContactAddrP.getId())) {
				iContactAddrP.setId(null);
				iContactAddrP.setCreateTime(new Date());
				iContactAddrP.setLastUpdate(new Date());
			}
			iContactAddrP.setAccount(iAccount);
			iContactAddrP.setContact(iContact);
			this.baseDao.saveOrUpdate(iContactAddrP);// 永久地信息保存
		}

		InvestorAccountContactAddr iContactAddrC = basicVo.getiContactAddrC();
		if (null != iContactAddrC.getDistrict() && !"".equals(iContactAddrC.getDistrict())) {
			if (null == iContactAddrC.getId() || "".equals(iContactAddrC.getId())) {
				iContactAddrC.setId(null);
				iContactAddrC.setCreateTime(new Date());
				iContactAddrC.setLastUpdate(new Date());
			}
			iContactAddrC.setAccount(iAccount);
			iContactAddrC.setContact(iContact);
			this.baseDao.saveOrUpdate(iContactAddrC);// 通信地址保存
		}

		// if("J".equals(iAccount.getAccType())){
		// InvestorAccountContactAgeas iContactJoint =
		// basicVo.getiContactJoint();
		//
		// InvestorAccountContactAddrAgeas iContactJointAddr_r =
		// basicVo.getiContactJointAddr_r();
		//
		// InvestorAccountContactAddrAgeas iContactJointAddr_p =
		// basicVo.getiContactJointAddr_p();
		//
		// InvestorAccountContactAddrAgeas iContactJointAddr_c =
		// basicVo.getiContactJointAddr_c();
		//
		// }

		InvestorAccountBank iBank = basicVo.getiBank();
		if (null == iBank.getId() || "".equals(iBank.getId())) {
			iBank.setId(null);
			iBank.setCreateTime(new Date());
			iBank.setLastUpdate(new Date());
		}
		iBank.setAccount(iAccount);
		this.baseDao.saveOrUpdate(iBank);// 银行信息

	}

	/**
	 * 根据id获取InvestorAccount实体
	 * 
	 * @param accountId
	 * @return
	 */
	public InvestorAccount findInvestorAccountById(String accountId) {

		InvestorAccount iAccount = new InvestorAccount();
		if (null == accountId || "".equals(accountId))
			return iAccount;

		String hql = "from InvestorAccount t where t.id=?";
		List<InvestorAccount> list = this.baseDao.find(hql, new String[] { accountId }, false);
		if (list.isEmpty())
			return iAccount;
		return list.get(0);
	}

	/**
	 * 获取账户关联联系人
	 * 
	 * @author scshi
	 * @param accountid
	 *            账户id
	 * @param contactType
	 *            联系人类型,M:帐户或者主联系人信息,J:联名帐户联系人信息
	 * */
	public InvestorAccountContact findIContactByType(InvestorAccount account, String contactType, String langCode) {
		InvestorAccountContact contact = new InvestorAccountContact();
		if (null == account || null == account.getId())
			return contact;

		// String hql =
		// "from InvestorAccountContactAgeas t where t.account.id=? and t.contactType=?";
		String hql = "from InvestorAccountContact t where t.account.id=? and t.contactType=?";

		List params = new ArrayList();
		params.add(account.getId());
		params.add(contactType);
		List<InvestorAccountContact> list = this.baseDao.find(hql, params.toArray(), false);
		if (list.isEmpty())
			return contact;

		contact = list.get(0);
		String sex = contact.getGender();
		if ("F".equals(sex)) {
			contact.setSexName("Female");
		} else if ("M".equals(sex)) {
			contact.setSexName("Male");
		}
		String eduLevel = contact.getLevelOfEducation();// 教育程度code
		String occupation = contact.getOccupation();// 职业code
		// String investH = contact.getInvestHorizon();//投资期限code

		if (null != eduLevel && !"".equals(eduLevel)) {
			String eduLevelName = findParamConfigName(eduLevel, langCode);
			contact.setEduLevelName(eduLevelName);
		}
		if (null != occupation && !"".equals(occupation)) {
			String occaName = findParamConfigName(occupation, langCode);
			contact.setOccupationName(occaName);
		}
		// if(null!=investH && !"".equals(investH)){
		// String investHname = finaParamConfigName(investH,langCode);
		// contact.setInvestHname(investHname);
		// }
		return contact;

	}

	/**
	 * 获取账户关联银行信息
	 * 
	 * @author scshi
	 * @param account
	 *            账户
	 * */
	public InvestorAccountBank findBankAgeasByAccid(InvestorAccount iAccount) {
		InvestorAccountBank bank = new InvestorAccountBank();
		if (null == iAccount || null == iAccount.getId())
			return bank;

		// String hql = "from InvestorAccountBankAgeas t where t.account.id=? ";
		String hql = "from InvestorAccountBank t where t.account.id=? ";

		List<InvestorAccountBank> list = this.baseDao.find(hql, new String[] { iAccount.getId() }, false);
		if (list.isEmpty())
			return bank;
		return list.get(0);
	}

	/**
	 * 获取联系人地址信息
	 * 
	 * @author scshi
	 * @param contact
	 *            联系人
	 * @param addrType
	 *            R:Residential居住地址 P:Permanent永久地址,C:Correspondence通信地址
	 * */
	public InvestorAccountContactAddr findIContactAddr(InvestorAccountContact iContact, String addrType) {
		InvestorAccountContactAddr contactAddr = new InvestorAccountContactAddr();
		if (null == iContact || null == iContact.getId())
			return contactAddr;
		String hql = "from InvestorAccountContactAddr t where t.contact.id=? and t.addrType=?";
		List params = new ArrayList();
		params.add(iContact.getId());
		params.add(addrType);
		List<InvestorAccountContactAddr> list = this.baseDao.find(hql, params.toArray(), false);
		if (list.isEmpty())
			return contactAddr;
		return list.get(0);
	}

	// get entity -------------
	/**
	 * 通过memberId获取信息
	 * 
	 * @author michael
	 * @param memberId
	 *            成员ID
	 * @return MemberIfa
	 */
	public MemberIfa findIfaByMemberId(String memberId) {
		MemberIfa bank = new MemberIfa();
		String hql = "from MemberIfa t where t.member.id=? ";
		List<MemberIfa> list = this.baseDao.find(hql, new String[] { memberId }, false);
		if (list.isEmpty())
			return bank;
		return list.get(0);
	}

	/***
	 * 分页查询开户申请列表
	 * 
	 * @author michael
	 * @param jsonPaging
	 *            分页参数
	 * @param loginUser
	 *            当前登录用户
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging, MemberBase loginUser, String queryType) {
		return findInvestorAccountList(jsonPaging, loginUser, queryType, null, null, null, null);
	}

	/**
	 * 分页查询开户申请列表
	 * 
	 * @param jsonPaging
	 *            分页参数
	 * @param loginUser
	 *            当前登录用户
	 * @param submitSts
	 *            提交方：invest投资人提交,ifa提交
	 * @param openSts
	 *            开户状态:-2待推送,-1已推送等待结果,1成功开户,0失败
	 * @param finishSts
	 *            完成状态：0草稿,1已提交
	 * @param flowSts
	 *            流程状态：-1还未执行,0流程进行中,1流程审核通过结束返回,2流程审核不通过结束返回
	 * @param queryType
	 *            查询类型：application 申请， approval 审批
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging findInvestorAccountList(JsonPaging jsonPaging, MemberBase loginUser, String queryType, String submitSts, String openSts, String finishSts, String flowSts) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InvestorAccount a ");
		hql.append(" where a.isValid='1' ");

		if (loginUser != null && loginUser.getId() != null && !"".equals(loginUser.getId())) {
			//investor申请
			if (null != queryType && "application".equalsIgnoreCase(queryType)) {
				hql.append(" and a.member.id = ? ");
				params.add(loginUser.getId());
			}
			//ifa草拟开户申请
			if (null != queryType && "ifaDraft".equalsIgnoreCase(queryType)) {
				MemberIfa ifa = findIfaByMemberId(loginUser.getId());
				hql.append(" and a.ifa.id = ? ");
				params.add(ifa.getId());
			}
			//审批
			if (null != queryType && "approval".equalsIgnoreCase(queryType)) {
				// 查todo表， flow_state 节点审批状态：0待处理，1已处理
				List<WfProcedureInstanseTodo> todoList = findTodoListByUser("0", loginUser.getId());
				if (null != todoList && !todoList.isEmpty()) {
					String idString = "";
					for (WfProcedureInstanseTodo x : todoList) {
						if (!"".equals(idString))
							idString += ",";
						idString += "'" + x.getBusinessId() + "'";
					}
					if (!"".equals(idString))
						hql.append(" and a.id in (" + idString + ")");
				} else {
					hql.append(" and a.id ='xx' ");
				}
			}
		}

		if (null != submitSts && !"".equals(submitSts)) {
			hql.append(" and a.submitStatus = ? ");
			params.add(submitSts);
		}

		if (null != openSts && !"".equals(openSts)) {
			hql.append(" and a.openStatus = ? ");
			params.add(openSts);
		}

		if (null != finishSts && !"".equals(finishSts)) {
			hql.append(" and a.finishStatus = ? ");
			params.add(finishSts);
		}

		if (null != flowSts && !"".equals(flowSts)) {
			hql.append(" and a.flowStatus = ? ");
			params.add(flowSts);
		}

		if (jsonPaging.getSort() == null || "".equals(jsonPaging.getSort())) {
			jsonPaging.setSort("a.lastUpdate");
			jsonPaging.setOrder("desc");
		}

		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);

		return jsonPaging;
	}

	@SuppressWarnings("unchecked")
	private List<WfProcedureInstanseTodo> findTodoListByUser(String flowState, String flowUserId) {
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
	 * 
	 * @author scshi
	 * @param account
	 *            账号信息
	 * */
	@Override
	public void saveInvestorAccount(InvestorAccount account) {
		InvestorAccount accountVo = (InvestorAccount) this.baseDao.get(InvestorAccount.class, account.getId());
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
	 * 
	 * @author scshi
	 * @param iContact
	 *            主要联系人信息
	 * @param accountId
	 *            关联账号Id
	 * @param iContactId
	 *            主要联系人id
	 * */
	@Override
	public String saveOrUpdateMainContact(InvestorAccountContact iContact, String accountId) {
		if (null == iContact.getId() || "".equals(iContact.getId())) {
			iContact.setId(null);
			iContact.setCreateTime(new Date());
		}
		InvestorAccount acc = (InvestorAccount) this.baseDao.get(InvestorAccount.class, accountId);
		iContact.setAccount(acc);
		iContact.setLastUpdate(new Date());

		String countryId = iContact.getCountry().getId();
		String nationlId = iContact.getNationality().getId();
	//	String esiId = iContact.getPrimaryResidence().getId();
		if ("".equals(countryId))
			iContact.setCountry(null);
		if ("".equals(nationlId))
			iContact.setNationality(null);
		/*if ("".equals(esiId))
			iContact.setPrimaryResidence(null);*/

		this.baseDao.saveOrUpdate(iContact);// 主账户信息
		return iContact.getId();
	}

	/**
	 * 联系人地址保存
	 * 
	 * @author scshi
	 * @param iContactAddr
	 *            地址实体
	 * @param iContactId
	 *            关联联系人
	 * @param accountId
	 *            关联账号
	 * */
	public String saveOrUpdateIaddr(InvestorAccountContactAddr iContactAddr, String iContactId, String accountId) {
		if (null == iContactAddr.getId() || "".equals(iContactAddr.getId())) {
			iContactAddr.setId(null);
			iContactAddr.setCreateTime(new Date());
		}
		InvestorAccount acc = (InvestorAccount) this.baseDao.get(InvestorAccount.class, accountId);
		InvestorAccountContact iContact = (InvestorAccountContact) this.baseDao.get(InvestorAccountContact.class, iContactId);
		iContactAddr.setAccount(acc);
		iContactAddr.setContact(iContact);
		iContactAddr.setLastUpdate(new Date());

		String countryId = iContactAddr.getCountry().getId();
		if ("".equals(countryId))
			iContactAddr.setCountry(null);

		this.baseDao.saveOrUpdate(iContactAddr);
		return iContactAddr.getId();
	}

	/**
	 * 银行保存
	 * 
	 * @author scshi
	 * @param iBank
	 *            银行信息实体
	 * @param iContactId
	 *            关联账户
	 * */
	public String saveOrUpdateIbank(InvestorAccountBank iBank, String accountId) {
		if (null == iBank.getId() || "".equals(iBank.getId())) {
			iBank.setId(null);
			iBank.setCreateTime(new Date());
		}
		InvestorAccount acc = (InvestorAccount) this.baseDao.get(InvestorAccount.class, accountId);
		iBank.setAccount(acc);
		iBank.setLastUpdate(new Date());

		this.baseDao.saveOrUpdate(iBank);
		return iBank.getId();
	}

	/**
	 * 通过memberId获取信息
	 * 
	 * @author michael
	 * @param memberId
	 *            成员ID
	 * @return MemberIndividual
	 */
	public MemberIndividual findIndividualByMemberId(String memberId) {
		MemberIndividual bank = new MemberIndividual();
		String hql = "from MemberIndividual t where t.member.id=? ";
		List<MemberIndividual> list = this.baseDao.find(hql, new String[] { memberId }, false);
		if (list.isEmpty())
			return bank;
		return list.get(0);
	}

	/**
	 * 通过memberId获取信息
	 * 
	 * @author michael
	 * @param memberId
	 *            成员ID
	 * @return IndividualDataVO
	 */
	public IndividualDataVO findIndividualFullInfoByMemberId(String memberId) {
		IndividualDataVO vo = new IndividualDataVO();
		vo.setBase(memberBaseService.findById(memberId));
		vo.setIndividual(findIndividualByMemberId(memberId));
		return vo;
	}

	/**
	 * 根据基本数据的config_code获取基础数据多语言名称 param configCode 基础数据的code值 param langCode
	 * 当前语言 return 基础数据多语言名称
	 * */
	private String findParamConfigName(String configCode, String langCode) {
		String hql = "select c.id,c.configCode,c." + this.getLangString("name", langCode) + ",c." + this.getLangString("confValue", langCode);
		hql += " from SysParamConfig c where c.isValid='1' and c.configCode=? ";
		List list = this.baseDao.find(hql, new String[] { configCode }, false);
		if (list.isEmpty())
			return null;
		List<SysParamVO> voList = new ArrayList<SysParamVO>();
		for (int x = 0; x < list.size(); x++) {
			SysParamVO vo = new SysParamVO();
			Object[] objs = (Object[]) list.get(x);
			vo.setId(null == objs[0] ? "" : objs[0].toString());
			vo.setCode(null == objs[1] ? "" : objs[1].toString());
			vo.setName(null == objs[2] ? "" : objs[2].toString());
			vo.setValue(null == objs[3] ? "" : objs[3].toString());
			voList.add(vo);
		}
		return voList.get(0).getName();
	}

	/***
	 * 获取账户关联联系人id
	 * 
	 * @param accountId
	 *            账户
	 * @param contactType
	 *            联系人类型M--主联系人，J--关联联系人
	 * @return contactId
	 * */
	public String getAccountContactId(String accountId, String contactType) {

		// String hql =
		// "from InvestorAccountContactAgeas t where t.account.id=? and t.contactType=? order by t.id ";
		String hql = "from InvestorAccountContact t where t.account.id=? and t.contactType=? order by t.id ";

		List params = new ArrayList();
		params.add(accountId);
		params.add(contactType);
		List<InvestorAccountContact> voList = this.baseDao.find(hql, params.toArray(), false);
		if (voList.isEmpty())
			return null;
		return voList.get(0).getId();
	}

	/***
	 * 获取需要检查的文档 modify by mqzou 2016-10-24
	 * 
	 * @param memberId
	 *            账号
	 * @param distrubuteId
	 *            代理商
	 * @param contactId
	 *            联系人
	 * @param langCode
	 *            当前语言
	 * */
	public List<DocListVO> findContactDocList(String memberId, String distrubuteId, String contactId, String langCode,String accountId) {
		String hql = " select t.id,out.docName,t.docTemplate.id,t.isNecessary,t.updateCycle,t.expireDate,t.submitDate,";
		hql += " t.checkStatus ,t.isValid,t.docListId ";
		hql += " from InvestorDoc t inner join " + this.getLangString("InvestorDoc", langCode);
		hql += " out on t.id=out.id ";
		hql += " where t.member.id=? and t.distributor.id=? ";

		
		List params = new ArrayList();
		params.add(memberId);
		params.add(distrubuteId);
		if(StringUtils.isNotBlank(accountId)){
			hql += " and t.account.id=?";
			params.add(accountId);
		}else{
			hql += " and t.contact.id=? ";
			params.add(contactId);
		}
		hql += " order by t.isValid desc,t.createTime desc ";
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if (voList.isEmpty())
			return null;

		List<DocListVO> list = new ArrayList<DocListVO>();
		for (int z = 0; z < voList.size(); z++) {
			Object[] objs = (Object[]) voList.get(z);
			DocListVO vo = new DocListVO();
			vo.setId(objs[0] == null ? "" : objs[0].toString());
			vo.setDocName(objs[1] == null ? "" : objs[1].toString());
			vo.setTemplateId(objs[2] == null ? "" : objs[2].toString());
			vo.setIsNecessary(objs[3] == null ? "" : objs[3].toString());
			vo.setUpdateCycle(objs[4] == null ? 0 : Integer.parseInt(objs[4].toString()));
			vo.setEffectDate(objs[5] == null ? null : DateUtil.StringToDate(objs[5].toString(), "yyyy-MM-dd hh:mm:ss"));
			vo.setCreateTime(objs[6] == null ? new Date() : DateUtil.StringToDate(objs[6].toString(), "yyyy-MM-dd hh:mm:ss"));
			vo.setCheckStatus(objs[7] == null ? "" : objs[7].toString());
			String isValid = objs[8]==null?"1":objs[8].toString();
			if("0".equals(isValid)){
				vo.setCheckStatus("3");//文档失效
			}
			vo.setDocListId(objs[9]==null?"":objs[9].toString());
			if(null!=vo.getEffectDate()){
				String expireStatus = vo.getEffectDate().after(new Date())?"0":"1";
				vo.setExpireStatus(expireStatus);
				Calendar betweenDay = Calendar.getInstance();
				betweenDay.setTime(vo.getEffectDate());
				int dayEff = betweenDay.get(Calendar.DAY_OF_YEAR);
				betweenDay.setTime(new Date());
				int dayNow = betweenDay.get(Calendar.DAY_OF_YEAR);
				vo.setEffectDateBetween(dayNow-dayEff);
			}
			
			
			// 查询已上传附件
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
	 * @author scshi_u330p
	 * @params distrubuteId 代理商
	 * @params clientType 开户类型
	 * @params loginLangFlag 当前语言
	 * @params memberId 当前登录用户
	 * @params mainContactId 联系人id
	 * */
	public void copyDocListfromTemp(String distrubuteId,String clientType, 
			String loginLangFlag, String memberId,String contactId,InvestorAccount account){
		
		String docTemplateHql = "select dt.id,out.title ";
		docTemplateHql += " from DocTemplate dt inner join " + this.getLangString("DocTemplate", loginLangFlag);
		docTemplateHql += " out on dt.id=out.id ";
		docTemplateHql += " WHERE dt.isValid = 1 and dt.distributor.id = ? and dt.clientType = ? ";
		docTemplateHql += " and dt.isDefault=1 and dt.status='using' ";
		List<String> docTemplateParams = new ArrayList<String>();
		docTemplateParams.add(distrubuteId);
		docTemplateParams.add(clientType);
		// docTemplateParams.add(langCode);
		List voList = this.baseDao.find(docTemplateHql, docTemplateParams.toArray(), false);
		List<DocTemplate> docTemplatesList = new ArrayList<DocTemplate>();
		if (!voList.isEmpty()) {
			for (int x = 0; x < voList.size(); x++) {
				Object[] objs = (Object[]) voList.get(x);
				DocTemplate temp = new DocTemplate();
				String tempId = objs[0] == null ? "" : objs[0].toString();
				if (!"".equals(tempId)) {
					temp = (DocTemplate) this.baseDao.get(DocTemplate.class, tempId);
					if(null!=temp){
						temp.setTitle(objs[1] == null ? "" : objs[1].toString());	
					}
				}
				if(null!=temp)
				  docTemplatesList.add(temp);
			}
		}

		if (null != docTemplatesList && !docTemplatesList.isEmpty()) {
			String docListHql = " from DocList t where t.template.id = ? ORDER BY t.id ";
			List<String> docListParams = new ArrayList<String>();
			docListParams.add(docTemplatesList.get(0).getId());
			List docList = this.baseDao.find(docListHql, docListParams.toArray(), false);
			if (!docList.isEmpty()) {

				MemberBase member = new MemberBase();
				member.setId(memberId);

				MemberDistributor distributor = new MemberDistributor();
				distributor.setId(distrubuteId);

	
				InvestorAccountContact contact = new InvestorAccountContact();
				contact.setId(contactId);
				
				DocTemplate docTemplate = docTemplatesList.get(0);
				for (int y = 0; y < docList.size(); y++) {
					DocList docModel = (DocList) docList.get(y);
					String docModelId = docModel.getId();
					InvestorDoc investDoc = new InvestorDoc();

					investDoc.setId(null);
					investDoc.setCreateTime(new Date());
					investDoc.setDistributor(distributor);
					investDoc.setMember(member);
					investDoc.setDocTemplate(docTemplate);
					if(StringUtils.isNotBlank(contactId)){
						investDoc.setContact(contact);
					}
					investDoc.setIsNecessary(docModel.getIsNecessary());
					investDoc.setUpdateCycle(docModel.getUpdateCycle());
					investDoc.setCreateTime(new Date());
					investDoc.setLastUpdate(new Date());
					investDoc.setIsValid("1");
					investDoc.setAccount(account);
					investDoc.setDocListId(docModel.getId());
					this.baseDao.saveOrUpdate(investDoc);

					// 简体中文版checklist保存
					InvestorDocSc investDocSc = new InvestorDocSc();
					DocListSc docModelSc = (DocListSc) this.baseDao.get(DocListSc.class, docModelId);
					investDocSc.setId(investDoc.getId());
					investDocSc.setDocName(docModelSc.getDocName());
					this.baseDao.create(investDocSc);

					// 繁体
					InvestorDocTc investDocTc = new InvestorDocTc();
					DocListTc docModelTc = (DocListTc) this.baseDao.get(DocListTc.class, docModelId);
					investDocTc.setId(investDoc.getId());
					investDocTc.setDocName(docModelTc.getDocName());
					this.baseDao.create(investDocTc);

					// 英文
					InvestorDocEn investDocEn = new InvestorDocEn();
					DocListEn docModelEn = (DocListEn) this.baseDao.get(DocListEn.class, docModelId);
					investDocEn.setId(investDoc.getId());
					investDocEn.setDocName(docModelEn.getDocName());
					this.baseDao.create(investDocEn);
				}
			}
		}
	}

	/**
	 * 搜索提交的文档列表
	 * 
	 * @param moduleType
	 * @param relateId
	 * @return
	 */
	public List<AccessoryFile> findSubmitDocList(String moduleType, String relateId) {
		// 查询已上传附件
		String fileHql = " from AccessoryFile t where t.relateId=? and t.moduleType=? order by t.createTime desc ";
		List fileParams = new ArrayList();
		fileParams.add(relateId);
		fileParams.add(moduleType);
		List<AccessoryFile> fileList = this.baseDao.find(fileHql, fileParams.toArray(), false);
		return fileList;
	}


	 

	/**
	 * 获取Ifa管理的某个客户的投资账号列表
	 * 
	 * @author zxtan
	 * @date 2016-09-06
	 */
	@Override
	public JsonPaging findInvestorAccountListForIfa(JsonPaging jsonPaging, String ifaMemberId, String customerMemberId) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InvestorAccount l ");
		hql.append(" where l.ifa.member.id=? and l.member.id=? ");
		params.add(ifaMemberId);
		params.add(customerMemberId);

		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);

		return jsonPaging;
	}

	/**
	 * 获取Ifa管理的某个客户的投资账号列表
	 * 
	 * @author zxtan
	 * @date 2016-09-09
	 */
	@Override
	public List<InvestorAccount> findInvestorAccountListForIfa(String ifaMemberId, String customerMemberId, String distributorId) {
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
	 * 
	 * @author zxtan
	 * @date 2016-09-09
	 */
	@Override
	public InvestorDoc findInvestorDocForDistributor(String distributorId, String customerMemberId) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from InvestorDoc l ");
		hql.append(" where l.distributor.id=? and l.member.id=? ");
		hql.append(" ORDER BY expireDate"); // IFNULL(expireDate,DATE_ADD(CURRENT_DATE(),INTERVAL
											// 5 YEAR))
		params.add(distributorId);
		params.add(customerMemberId);

		List<InvestorDoc> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 获取Ifa管理的某个客户的投资账号列表
	 * 
	 * @author zxtan
	 * @date 2016-09-09
	 */
	@Override
	public List<InvestorAccountDistributorVO> findInvestorDistributor(String ifaMemberId, String customerMemberId) {
		List<InvestorAccountDistributorVO> voList = new ArrayList<InvestorAccountDistributorVO>();
		StringBuilder hql = new StringBuilder("SELECT DISTINCT  ia.ifa_id,ia.member_id, ia.distributor_id, ");
		hql.append("(SELECT company_name FROM member_distributor WHERE id=ia.distributor_id) as distributor_name,");
		hql.append("(SELECT logofile FROM member_distributor WHERE id=ia.distributor_id) as icon_url");
		hql.append(" FROM investor_account ia INNER JOIN member_ifa mi ON mi.id=ia.ifa_id WHERE mi.member_id='" + ifaMemberId + "' and ia.member_id='" + customerMemberId + "'"); // IFNULL(expireDate,DATE_ADD(CURRENT_DATE(),INTERVAL
																																													// 5
																																													// YEAR))
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(customerMemberId);
		// List list = this.baseDao.find(hql.toString(), params.toArray(),
		// false);
		List list = springJdbcQueryManager.springJdbcQueryForList(hql.toString());

		if (!list.isEmpty()) {
			for (int i = 0; i < list.size(); i++) {
				Map map = (HashMap) list.get(i);

				InvestorAccountDistributorVO vo = new InvestorAccountDistributorVO();
				String ifaId = map.get("ifa_id") == null ? "" : map.get("ifa_id").toString();
				String memberId = map.get("member_id") == null ? "" : map.get("member_id").toString();
				String distributorId = map.get("distributor_id") == null ? "" : map.get("distributor_id").toString();
				String distributorName = map.get("distributor_name") == null ? "" : map.get("distributor_name").toString();
				String iconUrl=map.get("icon_url")== null ? "" : map.get("icon_url").toString();

				vo.setIfaId(ifaId);
				vo.setMemberId(memberId);
				vo.setDistributorId(distributorId);
				vo.setDistributorName(distributorName);
				vo.setIconUrl(iconUrl);

				InvestorDoc docObj = findInvestorDocForDistributor(distributorId, customerMemberId);
				Date expire_date = docObj == null ? null : docObj.getExpireDate();
				vo.setNextDocCheckDate(expire_date);
				String eTime = DateUtil.getDateStr(expire_date);
				String sTime = DateUtil.getCurDateStr();
				Long days = expire_date == null ? null : DateUtil.getDaysOfTwoDate(sTime, eTime);
				vo.setNextDocCheckDays(days == null ? null : days.intValue());

				List<InvestorAccount> accountList = findInvestorAccountListForIfa(ifaMemberId, customerMemberId, distributorId);
				vo.setAccountList(accountList);

				voList.add(vo);
			}
		}
		return voList;
	}

	@Override
	public List<AccountVO> findAllAccountList(InvestorAccount account) {
		String hql = " from InvestorAccount r where r.isValid='1'  ";
		if (null != account) {
			if (null != account.getMember() && null != account.getMember().getId() && !"".equals(account.getMember().getId()) && !"null".equals(account.getMember().getId())) {
				hql += " and r.member.id='" + account.getMember().getId() + "'";
			}
			if (null != account.getOpenStatus() && !"".equals(account.getOpenStatus())) {
				hql += " and r.openStatus in (" + account.getOpenStatus() + ")";
			}
			/*if (null != account.getBaseCurrency() && !"".equals(account.getBaseCurrency())) {
				hql += " and r.baseCurrency='" + account.getBaseCurrency() + "'";
			}*/
			/*if (null != account.getIsValid() && !"".equals(account.getIsValid())) {
				hql += " r.openStatus='0''";
			} else {
				hql += "and  r.openStatus<>'0'";
			}*/
			if (null != account.getIfa()) {
				if (null != account.getIfa().getId() && !"".equals(account.getIfa().getId())) {
					hql += " and r.ifa.id='" + account.getIfa().getId() + "'";
				}
				if (null != account.getIfa().getIfafirm() && null != account.getIfa().getIfafirm().getId() && !"".equals(account.getIfa().getIfafirm().getId())) {
					hql += " and r.ifa.ifafirm.id='" + account.getIfa().getIfafirm().getId() + "'";
				}
			}

			if (null != account.getDistributor()) {
				hql += " and r.distributor.id='" + account.getDistributor().getId() + "'";
			}
			if (null != account.getAccountNo() && !"".equals(account.getAccountNo())) {
				hql += " and r.member.loginCode='" + account.getAccountNo() + "'";
			}
		}
		hql += " order by r.subFlag asc, r.openStatus desc";
		List reList = this.baseDao.find(hql, null, false);
		List<AccountVO> list = new ArrayList<AccountVO>();
		Iterator it = reList.iterator();
		while (it.hasNext()) {
			InvestorAccount investorAccount = (InvestorAccount) it.next();
			AccountVO vo=new AccountVO();
			vo.setId(investorAccount.getId());
			vo.setAccountNo(investorAccount.getAccountNo());
			vo.setAccType("I".equals(investorAccount.getAccType())?"Individual":"Joint");
			vo.setBaseCurrency(investorAccount.getBaseCurrency());
			
			vo.setCies(null!=investorAccount.getCies() && "1".equals(investorAccount.getCies())?"CIES":"");
			vo.setDistributorId(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getId():"");
			vo.setFromType(investorAccount.getFromType());
			vo.setMemberId(investorAccount.getMember().getId());
			vo.setOpenStatus(investorAccount.getOpenStatus());
			if(CommonConstantsWeb.WEB_ACCCOUNT_OPEN_COMPLETE.equals(investorAccount.getOpenStatus())){
				String accountNo="";
				String subAccountNo="";
				accountNo=investorAccount.getAccountNo();
				/*if("1".equals(investorAccount.getSubFlag())){
					subAccountNo=investorAccount.getAccountNo();
				}else {
					accountNo=investorAccount.getAccountNo();
				}*/
				List<CharDataVO> chartList=findAccountCurrency(accountNo, subAccountNo,account.getBaseCurrency());
				double totalAssets=0;
				double cash=0;
				double market=0;
				if(null!=chartList){
					Iterator iterator=chartList.iterator();
					while (iterator.hasNext()) {
						CharDataVO object = (CharDataVO) iterator.next();
						if("Market Value".equals(object.getName())){
							vo.setProductValue(object.getValue()>0?String.valueOf(object.getValue()):"0");
							market=object.getValue();
						}else if ("Cash".equals(object.getName())) {
							vo.setCash(object.getValue()>0?String.valueOf(object.getValue()):"0");
							cash=object.getValue();
						}
						totalAssets+=object.getValue();
					}
				}
				vo.setTotalAssest(totalAssets>0?String.valueOf(totalAssets):"0");
				String chartStr=JsonUtil.toJson(chartList).replace("\"", "'");
				vo.setChartData(chartStr);
				if(totalAssets>0){
					vo.setCashPercent(String.valueOf(cash/totalAssets));
					vo.setProductValuePercent(String.valueOf(market/totalAssets));
				}else {
					vo.setCashPercent("0");
					vo.setProductValuePercent("0");
				}
				
			}
			vo.setSubFlag(investorAccount.getSubFlag());
			vo.setFlowStatus(investorAccount.getFlowStatus());
			
			list.add(vo);
		}
		return list;
	}

	@Override
	public List findInvestorDistributor(InvestorAccount account) {

		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT DISTINCT r.`distributor_id`,m.logofile,m.`company_name`, MAX(IFNULL( p.expire_date,NOW())) rpqDate,MIN(IFNULL(d.expire_date, NOW())) docDate  FROM investor_account r ");
		sql.append(" LEFT JOIN member_distributor m ON r.distributor_id=m.id");
		sql.append(" LEFT JOIN (SELECT * FROM  `rpq_exam` " );
		if(null!=account && null!=account.getMember() && null!=account.getMember().getId() && !"".equals(account.getMember().getId())){
			sql.append(" where rpq_exam.member_id='"+account.getMember().getId()+"'");
		}
		sql.append("  ORDER BY rpq_exam.expire_date DESC LIMIT 1 )p ON r.`member_id`=p.member_id AND r.`distributor_id`=p.distributor_id");
		sql.append(" LEFT JOIN (SELECT * FROM  `investor_doc` ORDER BY ifnull(expire_date,now()) ASC LIMIT 1 )d ON r.`member_id`=d.member_id AND r.`distributor_id`=p.distributor_id");
		sql.append(" WHERE  1=1 ");

		if (null != account) {
			if (null != account.getMember() && null != account.getMember().getId() && !"".equals(account.getMember().getId())) {
				sql.append(" and r.`member_id`='" + account.getMember().getId() + "'");
			}
			if (null != account.getOpenStatus() && !"".equals(account.getOpenStatus())) {
				sql.append(" and r.open_status in (" + account.getOpenStatus() + ")");
			}
			if (null != account.getIsValid() && !"".equals(account.getIsValid())) {
				sql.append(" and r.is_valid='" + account.getIsValid() + "'");
			} else {
				sql.append(" and r.`is_valid`='1' ");
			}
			if (null != account.getIfa() && null != account.getIfa().getId() && !"".equals(account.getIfa().getId())) {
				sql.append(" and r.ifa_id='" + account.getIfa().getId() + "'");
			}
			if (null != account.getDistributor() && null != account.getDistributor().getId() && !"".equals(account.getDistributor().getId())) {
				sql.append(" and r.distributor_id='" + account.getDistributor().getId() + "'");
			}
		}

		sql.append("  GROUP BY distributor_id");
		List reList = this.springJdbcQueryManager.springJdbcQueryForList(sql.toString());
		List list = new ArrayList();
		Iterator it = reList.iterator();
		while (it.hasNext()) {
			HashMap map = (HashMap) it.next();
			InvestorDistributorVO vo = new InvestorDistributorVO();
			vo.setCompanyName(getMapObject(map, "company_name"));
			vo.setId(getMapObject(map, "distributor_id"));
			String logofile=getMapObject(map, "logofile");
			
			vo.setLogoUrl(PageHelper.getLogoUrl(logofile, "D"));
			String rpqDate = getMapObject(map, "rpqDate");
			String dcDate = getMapObject(map, "docDate");
			if ("".equals(rpqDate)) {
				vo.setNextRPQDate("0");
			}else {
			  	
			   vo.setNextRPQDate(String.valueOf(DateUtil.getDaysOfTwoDate(DateUtil.getCurDateTime(), rpqDate)+1));
			}
			if ("".equals(dcDate)) {
				vo.setNextDCDate("0");
			}else {
				vo.setNextDCDate(String.valueOf(DateUtil.getDaysOfTwoDate(DateUtil.getCurDateTime(), dcDate)+1));
			}
			
			
			list.add(vo);
		}

		return list;
	}

	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}

	@Override
	public List findAccountIfafirm(InvestorAccount account, String langCode) {
		/*
		 * StringBuilder sql=new StringBuilder(); sql.append(
		 * " SELECT DISTINCT  f.`id`,f.`company_name` FROM investor_account i LEFT JOIN member_ifa m ON i.`ifa_id`=m.`id`"
		 * ); sql.append(
		 * " LEFT JOIN member_ifafirm f ON m.`company_ifafirm_id`=f.`id` ");
		 * sql.append(
		 * " WHERE     (i.open_status='-1' or i.open_status='1') and  f.`company_name` IS NOT NULL "
		 * ); if(null!=account.getMember()&& null!=account.getMember().getId()
		 * && !"".equals(account.getMember().getId())){
		 * sql.append(" and i.`member_id`='"+account.getMember().getId()+"'"); }
		 * if(null!=account.getOpenStatus() &&
		 * !"".equals(account.getOpenStatus())){
		 * sql.append(" and i.open_status in ("+account.getOpenStatus()+")"); }
		 * if(null!=account.getBaseCurrency() &&
		 * !"".equals(account.getBaseCurrency())){
		 * sql.append(" and i.base_currency='"+account.getBaseCurrency()+"'"); }
		 * if(null!=account.getIsValid() && !"".equals(account.getIsValid())){
		 * sql.append(" and i.is_valid='"+account.getIsValid()+"'"); }else {
		 * sql.append(" and i.`is_valid`='1' "); }
		 */

		StringBuilder hql = new StringBuilder();
		hql.append("select f.id,f.companyName from InvestorAccount i left join MemberIfa m on i.ifa.id=m.id");
		hql.append(" left join " + getLangString("MemberIfafirm", langCode) + " f on m.ifafirm.id=f.id");
		hql.append(" where (i.openStatus='-1' or i.openStatus='1') ");
		List params = new ArrayList();
		if (null != account.getMember() && null != account.getMember().getId() && !"".equals(account.getMember().getId())) {
			hql.append(" and i.`member_id`=?");
			params.add(account.getMember().getId());
		}
		/*
		 * if(null!=account.getOpenStatus() &&
		 * !"".equals(account.getOpenStatus())){
		 * sql.append(" and i.open_status in ("+account.getOpenStatus()+")"); }
		 * if(null!=account.getBaseCurrency() &&
		 * !"".equals(account.getBaseCurrency())){
		 * sql.append(" and i.base_currency='"+account.getBaseCurrency()+"'"); }
		 * if(null!=account.getIsValid() && !"".equals(account.getIsValid())){
		 * sql.append(" and i.is_valid='"+account.getIsValid()+"'"); }else {
		 * sql.append(" and i.`is_valid`='1' "); }
		 */

		List reList = this.baseDao.find(hql.toString(), params.toArray(), false);
		// List
		// reList=this.springJdbcQueryManager.springJdbcQueryForList(sql.toString());
		List list = new ArrayList();
		Iterator it = reList.iterator();
		List<String> idList=new ArrayList<String>(); 
		while (it.hasNext()) {
			// HashMap map = (HashMap) it.next();
			Object[] objects = (Object[]) it.next();
			InvestorDistributorVO vo = new InvestorDistributorVO();
			vo.setCompanyName(objects[1].toString());
			vo.setId(objects[0].toString());
			vo.setLogoUrl("");
			if(!idList.contains(vo.getId())){
				list.add(vo);
				idList.add(vo.getId());
			}
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
		String hql = " from InvestorAccount r where  r.masterAccount.id='" + account.getId() + "' and r.isValid='1' and (r.openStatus='-1'  or r.openStatus='1')";
		List resultList = this.baseDao.find(hql, null, false);
		List<InvestorAccount> list = new ArrayList<InvestorAccount>();
		Iterator it = resultList.iterator();
		while (it.hasNext()) {
			InvestorAccount investorAccount = (InvestorAccount) it.next();
			list.add(investorAccount);
		}
		return list;
	}

	/******************************* 投资人KYC,背景调查、培训 *****************************************/
	/**
	 * 获取投资人背景调查
	 * 
	 * @author wwlin
	 * @date 2016-09-18
	 */
	@Override
	//@Transactional(readOnly = true)
	public List<InvestorBackground> findInvestorbackground(String memberId) {
		String hql = "from InvestorBackground t where t.member.id = ?  ";
		List<String> params = new ArrayList<String>();
		params.add(memberId);

		List<InvestorBackground> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}

	/**
	 * 保存投资人背景调查
	 * 
	 * @author wwlin
	 * @param account
	 *            背景调查基本信息实体
	 * @return InvestorAccount
	 */
	@Override
	public InvestorBackground saveOrUpdateInvestorBackground(InvestorBackground model) {
		return (InvestorBackground) baseDao.saveOrUpdate(model);
	}

	/***
	 * 通过ID获取背景信息
	 * 
	 * @author 林文伟
	 * @date 2016-09-19
	 */
	@Override
	public InvestorBackground getInvestorBackground(String id) {
		Object obj = (InvestorBackground) baseDao.get(InvestorBackground.class, id);
		if (obj != null) {
			return (InvestorBackground) obj;
		} else
			return null;
	}

	/***
     * 通过ID获取培训信息
     * @author 林文伟
     * @date 2016-09-19
     */
	@Override
	public InvestorTraining getInvestorTraining(String id) {
		Object obj = (InvestorTraining) baseDao.get(InvestorTraining.class, id);
		if(obj!=null)
		{
			return (InvestorTraining)obj;
		} else return null;
	}
	
	/***
     * 删除背景信息
     * @author 林文伟
     * @date 2016-09-18
     */
	@Override
	public Boolean delInvestorBackground(String id) {
		String hql1 = " delete InvestorBackground where  id =?  ";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		int rs = this.baseDao.updateHql(hql1, params.toArray());

		return rs > 0 ? true : false;
	}

	/**
	 * 获取投资人培训信息
	 * 
	 * @author wwlin
	 * @date 2016-09-18
	 */
	@Override
	//@Transactional(readOnly = true)
	public List<InvestorTraining> findInvestorTraining(String memberId) {
		String hql = "from InvestorTraining t where t.member.id = ?  ";
		List<String> params = new ArrayList<String>();
		params.add(memberId);

		List<InvestorTraining> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}

	/**
	 * 保存投资人培训信息
	 * 
	 * @author wwlin
	 * @param account
	 *            培训信息基本信息实体
	 * @return InvestorAccount
	 */
	@Override
	public InvestorTraining saveOrUpdateInvestorTraining(InvestorTraining model) {
		return (InvestorTraining) baseDao.saveOrUpdate(model);
	}
	
	@Override
	public AccessoryFile saveOrUpdateAccessoryFile(AccessoryFile model) {
		return (AccessoryFile) baseDao.saveOrUpdate(model);
	}


	@Override
	public List<WfProcedureInstanseTodo> findAccountWfHistory(String accountId) {
		String hql = " from WfProcedureInstanseTodo r where r.businessId='" + accountId + "' ";
		hql += " order by r.createTime desc,r.flowCode desc";
		List reList = this.baseDao.find(hql, null, false);
		List<WfProcedureInstanseTodo> list = new ArrayList<WfProcedureInstanseTodo>();
		Iterator it = reList.iterator();
		while (it.hasNext()) {
			WfProcedureInstanseTodo todo = (WfProcedureInstanseTodo) it.next();
			list.add(todo);
		}
		return list;
	}

	/***
	 * 删除投资人培训信息
	 * 
	 * @author 林文伟
	 * @date 2016-09-18
	 */
	@Override
	public Boolean delInvestorTraining(String id) {
		String hql1 = " delete InvestorTraining where  id =?  ";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		int rs = this.baseDao.updateHql(hql1, params.toArray());

		return rs > 0 ? true : false;
	}

	/***
	 * IFA问卷列表查询的方法
	 * 
	 * @author 林文伟
	 * @date 2016-09-25
	 */
	@Override
	public List<RpqExam> findRpqExamByMemberId(String langCode, String memberId) {
		String hql = " from RpqExam r  ";
		hql += " inner join " + this.getLangString("RpqExam", langCode);
		hql += " l on l.id=r.id ";
		hql += " where r.status='1' and r.member.id = '" + memberId + "'";
		// List<Object> params=new ArrayList<Object>();
		hql += " order by r.createTime desc";
		// System.out.println("3333333333333"+hql);
		List<RpqExam> rpqExamList = new ArrayList<RpqExam>();
		List list1 = this.baseDao.find(hql, null, false);
		if (!list1.isEmpty()) {
			for (int x = 0; x < list1.size(); x++) {
				// System.out.println("3333333333333");
				RpqExam vo = new RpqExam();
				Object[] each2 = (Object[]) list1.get(x);
				Object questObj2 = (Object) each2[0];// RpqExam实体
				Object questObj3 = (Object) each2[1];// 多语言实体
				if (questObj2 instanceof RpqExam) {
					vo = ((RpqExam) questObj2);
				}
				if (questObj3 instanceof RpqExamEn) {
					// System.out.println("5555555555555");
					String title = ((RpqExamEn) questObj3).getTitle();
					vo.setTitle(title);
				}
				if (questObj3 instanceof RpqExamSc) {
					// System.out.println("5555555555555");
					String title = ((RpqExamSc) questObj3).getTitle();
					vo.setTitle(title);
				}
				if (questObj3 instanceof RpqExamTc) {
					// System.out.println("5555555555555");
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
	 * 
	 * @author 林文伟
	 * @date 2016-09-27
	 */
	@Override
	public JsonPaging findAccountOrderReturn(JsonPaging jsonPaging,String dataValue, String accountId,String transactionType) {
		String hql = " from InvestorAccountStream r  ";
		//hql += " inner join OrderHistory l";
		//hql += " on l.id=r.order.id ";
		hql += " where r.account.id = '" + accountId + "'";
		if(StringUtils.isNotBlank(transactionType)){
			hql += "and r.transactionType = '"+ transactionType +"' ";
		}
		if ("" != dataValue) {
			if ("today".equals(dataValue)) {
				hql += " and TO_DAYS(r.recordDate) = TO_DAYS(NOW()) "; // 查询是今天的
			} else if ("1w".equals(dataValue)) {
				hql += " and DATEDIFF(NOW(),r.recordDate)<7 and DATEDIFF(NOW(),r.recordDate)>0   "; // 查询1星期内
			} else if ("1m".equals(dataValue)) {
				// hql +=
				// " and r.transactionTime between DATE_SUB(now(),interval 1 month) and now()   ";
				// //
				hql += " and DATE_FORMAT(r.recordDate, '%Y%m' ) = DATE_FORMAT( CURDATE( ) , '%Y%m' )";
			} else if ("2m".equals(dataValue)) {
				// hql +=
				// " and r.transactionTime between DATE_SUB(now(),interval 2 month) and now()   ";
				// //
				hql += " and DATEDIFF(NOW(),r.recordDate)<60 and DATEDIFF(NOW(),r.recordDate)>0 ";
			} else if ("3m".equals(dataValue)) {
				hql += " and DATEDIFF(NOW(),r.recordDate)<90 and DATEDIFF(NOW(),r.recordDate)>0   "; //
			} else if ("6m".equals(dataValue)) {
				hql += " and DATEDIFF(NOW(),r.recordDate)<180 and DATEDIFF(NOW(),r.recordDate)>0  "; //
			} else if ("1y".equals(dataValue)) {
				hql += " and YEAR(r.recordDate)=YEAR(NOW())  "; //
			}
		}
		hql += " order by r.recordDate desc";
		//List<InvestorAccountStream> orderReturnList = new ArrayList<InvestorAccountStream>();
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), null, jsonPaging , true);
		//List<InvestorAccountStream> list1 = jsonPaging.getList();////每个列表都是一个2维数组，每个数组有2个实体
		//List<InvestorAccountStream> list1 = this.baseDao.find(hql, null, false);
//		if (!list1.isEmpty()) {
//			for (int x = 0; x < list1.size(); x++) {
//				OrderReturn vo = new OrderReturn();
//				Object[] each2 = (Object[]) list1.get(x);
//				Object questObj2 = (Object) each2[0];// OrderReturn实体
//				Object questObj3 = (Object) each2[1];// OrderHistory实体
//				if (questObj2 instanceof OrderReturn) {
//					vo = ((OrderReturn) questObj2);
//				}
//				if (questObj3 instanceof OrderHistory) {
//					// System.out.println("5555555555555");
//					// String title = ((OrderHistory) questObj3).getTitle();
//					// vo.setTitle(title);
//				}
//
//				orderReturnList.add(vo);
//			}
//		}

		return jsonPaging;
	}

	@Override
	public List<MemberBase> findInvestorList(MemberBase memberBase, String noIfa) {
		String hql = " from MemberBase r where r.isValid='1' and r.subMemberType='11'";

		List params = new ArrayList();
		if (null != memberBase) {
			if (null != memberBase.getInvestStyle() && !"".equals(memberBase.getInvestStyle())) {
				hql += " and r.investStyle like ?";
				params.add("%" + memberBase.getInvestStyle() + "%");
			}
			if (null != memberBase.getHobbyType() && !"".equals(memberBase.getHobbyType())) {
				hql += " and r.hobbyType  like ?";
				params.add("%" + memberBase.getHobbyType() + "%");
			}
			if (null != memberBase.getLangCode() && !"".equals(memberBase.getLangCode())) {
				hql += " and r.langCode=?";
				params.add(memberBase.getLangCode());
			}

		}
		if ("1".equals(noIfa)) {
			hql += " and r.id not in (select i.member.id from InvestorAccount i where i.ifa is not null)";
		}
		List rList = this.baseDao.find(hql, params.toArray(), false);
		Iterator it = rList.iterator();
		List list = new ArrayList();
		while (it.hasNext()) {
			MemberBase object = (MemberBase) it.next();
			list.add(object);

		}
		return list;
	}

	/**
	 * KYC获取文档审核记录
	 * @author scshi
	 * @date 20160928
	 * @param docId
	 * */
	public List<InvestorDocCheck> findDocCheckList(String docId) {
		StringBuffer strBuf = new StringBuffer("from InvestorDocCheck  t ");
		strBuf.append(" where t.docId=? order by t.checkTime desc ");
		List<InvestorDocCheck> list = this.baseDao.find(strBuf.toString(), new String[] { docId }, false);
		if (null != list && !list.isEmpty())
			return list;
		return null;

	}

	/**
	 * 获取Ifa管理的某个客户的投资账号信息
	 * 
	 * @author wwluo
	 * @date 2016-10-14
	 */
	@Override
	public List<InvestorAccount> findInvestorAccounts(String ifaMemberId, String customerMemberId) {
		List<InvestorAccount> investorAccounts = null;
		if (StringUtils.isNotBlank(ifaMemberId) && StringUtils.isNotBlank(customerMemberId)) {
			StringBuffer hql = new StringBuffer(" from InvestorAccount where isValid=1 and ifa_id =? and member_id =?");
			List params = new ArrayList();
			params.add(ifaMemberId);
			params.add(customerMemberId);
			investorAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return investorAccounts;
	}

	/**
	 * 获取投资人账户列表（IFA）
	 * 
	 * @author mqzou 2016-11-10
	 * @param jsonPaging
	 * @param accountVO
	 * @return
	 */
	@Override
	public JsonPaging findAccountList(JsonPaging jsonPaging, AccountVO accountVO,String langCode) {
		List params = new ArrayList();

		StringBuilder sql=new StringBuilder();
		sql.append(" call pro_getaccountlist(?,?,?,?,?,?,?,?,?,?,?);");
		params.add("");
		params.add(accountVO.getIfaId());
		params.add(accountVO.getDistributorId());
		params.add(accountVO.getOpenStatus());
		params.add(accountVO.getIfafirmId());
		
		params.add(DateUtil.getCurDateStr());
		params.add(accountVO.getNextRPQDate());
		params.add(accountVO.getBaseCurrency());
		params.add(jsonPaging.getOrderStr());
		params.add((jsonPaging.getPage()-1)*jsonPaging.getRows());
		params.add(jsonPaging.getRows());
		//params.add(total);
		List resultList=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		params=new ArrayList();
		sql=new StringBuilder();
		sql.append(" call pro_getaccountlisttotal(?,?,?,?,?,?,?);");
		params.add(accountVO.getIfaId());
		params.add(accountVO.getDistributorId());
		params.add(accountVO.getOpenStatus());
		//params.add(accountVO.getIsValid());
		params.add(accountVO.getIfafirmId());
		params.add(accountVO.getNextRPQDate());
		params.add(DateUtil.getCurDateStr());
		params.add(accountVO.getBaseCurrency());
		List totalList=springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		if(null!=totalList && !totalList.isEmpty()){
			HashMap map=(HashMap)totalList.get(0);
			if(map.containsKey("count(*)")){
				Object total=map.get("count(*)");
				if(null!=total){
					jsonPaging.setTotal(Integer.valueOf(total.toString()));
				}
			}
			
		}
		List<AccountVO> list=new ArrayList<AccountVO>();
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				Object id=getMapObject(map, "id");
				Object nextDoc=getMapObject(map, "docDate");
			    Object nextRpq=getMapObject(map, "rpqDate");
			    Object riskLevel=getMapObject(map, "risk_level");
			    //Object marketValueObj=getMapObject(map, "marketValue");
			    //Object cashObj=getMapObject(map, "cash");
			    //Object totalAssetsObj=getMapObject(map, "totalAssets");
			    Object nickName=getMapObject(map, "nick_name");
			  /*  double marketValue=null!=marketValueObj&&!"".equals(marketValueObj.toString()) ?Double.valueOf(marketValueObj.toString()):0;
			    double cash=null!=cashObj&& !"".equals(cashObj.toString())?Double.valueOf(cashObj.toString()):0;
			    double totalAssets=null!=totalAssetsObj&& !"".equals(totalAssetsObj.toString())?Double.valueOf(totalAssetsObj.toString()):0;*/
			    
			    
			    AccountVO vo=new AccountVO();
			    if(null==id )
			    	continue;
			    InvestorAccount  investorAccount=findInvestorAccountById(id.toString());
			    nickName=getCommonMemberName(investorAccount.getMember().getId(), langCode, "2");
			    vo.setCustomerName(null!=nickName?nickName.toString():"");
				vo.setId(investorAccount.getId());
				vo.setAccountNo(investorAccount.getAccountNo());
				vo.setAccType("I".equals(investorAccount.getAccType())?"Indiviual":"Joint");
				vo.setBaseCurrency(null!=investorAccount.getBaseCurrency()?investorAccount.getBaseCurrency():"");
				vo.setBaseCurName(sysParamService.findNameByCode(vo.getBaseCurrency(), langCode));
				vo.setCies(null!=investorAccount.getCies() && "1".equals(investorAccount.getCies())?"CIES":"");
				vo.setDistributorId(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getId():"");
				vo.setFromType(investorAccount.getFromType());
				vo.setMemberId(null!=investorAccount.getMember()? investorAccount.getMember().getId():"");
				vo.setOpenStatus(investorAccount.getOpenStatus());
				if("-1".equals(vo.getOpenStatus())){
					//approvalCount++;
				}
				if("3".equals(investorAccount.getOpenStatus())){
					
					List<CharDataVO> chartList=findAccountCurrency(investorAccount.getAccountNo(), "",accountVO.getBaseCurrency());
					double totalAssets=0;
					double cash=0;
					double market=0;
					if(null!=chartList){
						Iterator iterator=chartList.iterator();
						while (iterator.hasNext()) {
							CharDataVO object = (CharDataVO) iterator.next();
							if("Market Value".equals(object.getName())){
								vo.setProductValue(object.getValue()>0?String.valueOf(object.getValue()):"0");
								market=object.getValue();
							}else if ("Cash".equals(object.getName())) {
								vo.setCash(object.getValue()>0?String.valueOf(object.getValue()):"0");
								cash=object.getValue();
							}
							totalAssets+=object.getValue();
						}
					}
					vo.setTotalAssest(totalAssets>0?String.valueOf(totalAssets):"0");
					
				/*	vo.setTotalAssest(StrUtils.getNumberString(totalAssets,"#,##0.00"));
					vo.setProductValue(StrUtils.getNumberString(marketValue,"#,##0.00"));
					vo.setCash(StrUtils.getNumberString(cash,"#,##0.00"));*/

					try {
						if (null != nextDoc && !"".equals(nextDoc.toString())) {
							vo.setNextDCDate(String.valueOf(DateUtil.daysBetween(DateUtil.getCurDate(), DateUtil.StringToDate(nextDoc.toString(), DateUtil.DEFAULT_DATE_FORMAT))+1));
						} else {
							vo.setNextDCDate("0");
						}
						if (null != nextRpq && !"".equals(nextRpq.toString())) {
							vo.setNextRPQDate(String.valueOf(daysBetween(DateUtil.getCurDate(),DateUtil.StringToDate(nextRpq.toString(), DateUtil.DEFAULT_DATE_FORMAT))+1));
						} else {
							vo.setNextRPQDate("0");
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
						
				}
				
				
				vo.setFlowStatus(investorAccount.getFlowStatus());
				vo.setFaca(null!=investorAccount.getFaca() && "1".equals(investorAccount.getFaca())?"FACA":"");
				vo.setSubFlag(null!=investorAccount.getSubFlag()?investorAccount.getSubFlag():"");
				vo.setLoginCode(null!=investorAccount.getMember()? investorAccount.getMember().getLoginCode():"");
				vo.setRiskLevel(null!=riskLevel?riskLevel.toString():"0");
				vo.setDistributor(null!=investorAccount.getDistributor()?investorAccount.getDistributor().getCompanyName():"");
				vo.setDistributorIcon(null!=investorAccount.getDistributor()?PageHelper.getLogoUrl(investorAccount.getDistributor().getLogofile(), "D"):"");
				vo.setIsValid(investorAccount.getIsValid());
				vo.setIfafirmId(null!=investorAccount.getIfa()&& null!=investorAccount.getIfa().getIfafirm()? investorAccount.getIfa().getIfafirm().getId():"");
				MemberIfafirmBaseVO ifafirmBaseVO=ifafirmManageService.findIfafirmBase(vo.getIfafirmId(), langCode);
				vo.setIfafirmName(null!=ifafirmBaseVO?ifafirmBaseVO.getIfafirmName():"");
				vo.setIfafirmIcon(null!=ifafirmBaseVO?ifafirmBaseVO.getIconUrl():"");
				vo.setIfaId(null!=investorAccount.getIfa()?investorAccount.getIfa().getId():"");
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	
    /**
     * 获取帐号的收益图表数据
     * modify by mqzou 2017-01-06 
     */
	@Override
	public List<CharDataVO> findAccountCurrency(String accountNo, String subAccountNo, String currency) {
		List<CharDataVO> list = new ArrayList<CharDataVO>();
		CharDataVO marketVo = new CharDataVO();
		CharDataVO cashVo = new CharDataVO();
		marketVo.setName("Market Value");
		marketVo.setValue(0);
		cashVo.setName("Cash");
		cashVo.setValue(0);

		StringBuilder hql = new StringBuilder();
		//hql.append(" from InvestorAccountCurrency r where 1=1");
		hql.append(" from PortfolioHoldAccount r where 1=1");
		List params = new ArrayList();
		hql.append(" and r.accountNo=?");
		params.add(accountNo);
		
		/*
		if (null != accountNo && !"".equals(accountNo)) {
			hql.append(" and r.accountNo=?");
			params.add(accountNo);
		} else {
			hql.append(" and r.subAccountNo=?");
			params.add(subAccountNo);
		}*/

		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		/*
		 * System.out.println("accountNo:"+accountNo+" subAccountNo:"+subAccountNo
		 * +" size:"+String.valueOf(resultList.size()));
		 * System.out.println(hql.toString());
		 */
		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				PortfolioHoldAccount obj = (PortfolioHoldAccount) it.next();
				double market =null!=obj.getMarketValue()? obj.getMarketValue():0;
				double cashHold=null!=obj.getCashHold()?obj.getCashHold():0;
				double cashAvailable=null!=obj.getCashAvailable()?obj.getCashAvailable():0;
				double cash = cashHold + cashAvailable;
				String cur = obj.getBaseCurrency();
				double rate = 1;
				if (!currency.equals(cur)) {
					WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(cur, currency);
					if (null != exchangeRate) {
						rate = exchangeRate.getRate();
					}
				}
				marketVo.setValue(market * rate + marketVo.getValue());
				cashVo.setValue(cash * rate + cashVo.getValue());
			}
		}
		list.add(marketVo);
		list.add(cashVo);
		return list;
	}

	/**
	 * Accounts Wait for Process
	 * @author wwluo
	 * @date 2016-10-14
	 */
	@Override
	public JsonPaging getAccountsWaitforProcess(JsonPaging jsonPaging,
			MemberBase loginUser ,String distributorId,String ifaFirmId,String keyWord,String lang) {
		StringBuffer hql = new StringBuffer("" +
				" from InvestorAccount i where id in(" +
				" select businessId from" +
				" WfProcedureInstanseTodo" +
				" where" +
				" isValid=1 and" +
				" flowState=0 and" +
				" flowUser=? and" +
				" instanse.invokeCode=?)" +
				" and isValid=1");
		List params = new ArrayList();
		params.add(loginUser);
		params.add("Investor_open_account");
		
		if (StringUtils.isNotBlank(keyWord)) {
			hql.append(" and (i.member.nickName like ? ");
			hql.append(" or i.ifa.id in (select ifa.id from MemberIfa ifa where ifa.firstName like ? or ifa.lastName like ? or ifa.nameChn like ? ) ");
			hql.append(" ) ");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		
		if (StringUtils.isNotBlank(distributorId)) {
			hql.append(" and distributor_id=?");
			params.add(distributorId);
		}else if (StringUtils.isNotBlank(ifaFirmId)) {
			MemberIfafirm ifafirm = new MemberIfafirm();
			ifafirm.setId(ifaFirmId);
			hql.append(" and i.ifa.ifafirm=?");
			params.add(ifafirm);
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(jsonPaging != null && !jsonPaging.getList().isEmpty()){
			List<InvestorAccountVO> investorAccountVOs = new ArrayList<InvestorAccountVO>();
			List<InvestorAccount> investorAccounts = jsonPaging.getList();
			for (InvestorAccount investorAccount : investorAccounts) {
				InvestorAccountVO investorAccountVO = new InvestorAccountVO();
				BeanUtils.copyProperties(investorAccount, investorAccountVO);
				if(investorAccount.getMember() != null){
					investorAccountVO.setNickName(investorAccount.getMember().getNickName());
					investorAccountVO.setMobileNumber(investorAccount.getMember().getMobileNumber());
				}
				if(investorAccount.getIfa() != null && investorAccount.getIfa().getMember() != null){
					investorAccountVO.setIfaName(investorAccount.getIfa().getMember().getNickName());
					investorAccountVO.setIfaIconUrl(investorAccount.getIfa().getMember().getIconUrl());
				}
				if(investorAccount.getDistributor() != null){
					investorAccountVO.setDistributorId(investorAccount.getDistributor().getId());
					investorAccountVO.setDistributorName(investorAccount.getDistributor().getCompanyName());
				}
				if(investorAccount.getIfa() != null){
					MemberIfafirm firm = investorAccount.getIfa().getIfafirm();
					if(CommonConstants.LANG_CODE_SC.equals(lang)){
						MemberIfafirmSc firmSc = (MemberIfafirmSc) this.baseDao.get(MemberIfafirmSc.class, firm.getId());
						investorAccountVO.setIfaFirmId(firmSc.getId());
						investorAccountVO.setIfaFirmName(firmSc.getCompanyName());
					}else if(CommonConstants.LANG_CODE_TC.equals(lang)){
						MemberIfafirmTc firmTc = (MemberIfafirmTc) this.baseDao.get(MemberIfafirmTc.class, firm.getId());
						investorAccountVO.setIfaFirmId(firmTc.getId());
						investorAccountVO.setIfaFirmName(firmTc.getCompanyName());
					}else if(CommonConstants.LANG_CODE_EN.equals(lang)){
						MemberIfafirmEn firmEn = (MemberIfafirmEn) this.baseDao.get(MemberIfafirmEn.class, firm.getId());
						investorAccountVO.setIfaFirmId(firmEn.getId());
						investorAccountVO.setIfaFirmName(firmEn.getCompanyName());
					}
					investorAccountVO.setIfafirm(firm);
				}
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
				String time = dateFormat.format(investorAccount.getCreateTime());
				investorAccountVO.setTime(time);
				investorAccountVOs.add(investorAccountVO);
			}
			jsonPaging.getList().clear();
			jsonPaging.getList().addAll(investorAccountVOs);
			jsonPaging.setTotal(jsonPaging.getList().size());
		}
		return jsonPaging;
	}

	/**
	 * 获取用户的资产信息
	 * @author mqzou
	 * @date 2016-11-16
	 * modify by mqozu 2017-01-06 用户资产表更改
	 * @param memberId
	 * @return
	 */
	@Override
	public InvestorAccountCurrencyVO findInvestorCurrency(String memberId,String currency,String ifaId) {
		InvestorAccountCurrencyVO vo=new InvestorAccountCurrencyVO();
		vo.setMemberId(memberId);
		StringBuilder hql=new StringBuilder();
	    /*hql.append(" select c from PortfolioHoldAccount c ");
	    hql.append("  where c.account.member.id=?");*/
		hql.append(" select c from PortfolioHold h left join PortfolioHoldAccount c on h.id=c.portfolioHold.id");
		hql.append(" where h.member.id=? and c !=null and h.ifFirst='0'");
	    List params=new ArrayList();
	    params.add(memberId);
	    if(null!=ifaId && !"".equals(ifaId)){
	    	hql.append(" and h.ifa.id=?");
	    	params.add(ifaId);
	    }
	    List list=this.baseDao.find(hql.toString(), params.toArray(), false);
	    if(null!=list){
	    	Iterator it=list.iterator();
	    	while (it.hasNext()) {
				//InvestorAccountCurrency accountCurrency = (InvestorAccountCurrency) it.next();
	    		PortfolioHoldAccount obj = (PortfolioHoldAccount) it.next();
				double market = null!=obj.getMarketValue()? obj.getMarketValue():0;
				double cash = (null!=obj.getCashHold()? obj.getCashHold():0) + (null!=obj.getCashAvailable()?obj.getCashAvailable():0);
				String cur = obj.getBaseCurrency();
				double rate = 1;
				if (!currency.equals(cur)) {
					WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(cur, currency);
					if (null != exchangeRate) {
						rate = exchangeRate.getRate();
					}
				}
				vo.setMarketValue(market * rate + vo.getMarketValue());
				vo.setCashValue(cash * rate + vo.getCashValue());
			}
	    	  vo.setTotalAssets(vo.getMarketValue()+vo.getCashValue());
	    }
	 
		return vo;
	}
	
	/**
	 * 获取账户的资产信息
	 * @author mqzou
	 * @date 2016-11-24
	 * @param accountId
	 * @param currency
	 * @return
	 */
	@Override
	public InvestorAccountCurrencyVO findAccountCurrency(String accountNo, String currency) {
		InvestorAccountCurrencyVO vo=new InvestorAccountCurrencyVO();
		String hql=" from PortfolioHoldAccount r where r.accountNo=? or r.subAccountNo=? ";
		List params=new ArrayList();
		params.add(accountNo);
		params.add(accountNo);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Iterator it=list.iterator();
			while (it.hasNext()) {
				PortfolioHoldAccount accountCurrency = (PortfolioHoldAccount) it.next();
				String cur = accountCurrency.getBaseCurrency();
				double rate = 1;
				if (!currency.equals(cur)) {
					WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(cur, currency);
					if (null != exchangeRate) {
						rate = exchangeRate.getRate();
					}
				}
				double cashAvailable=null!=accountCurrency.getCashAvailable()?accountCurrency.getCashAvailable():0;
				double cashHold=null!=accountCurrency.getCashHold()?accountCurrency.getCashHold():0;
				double cashWithdrawal=null!=accountCurrency.getCashWithdrawal()?accountCurrency.getCashWithdrawal():0;
				double marketValue=null!=accountCurrency.getMarketValue()?accountCurrency.getMarketValue():0;
				
				vo.setCashAvailable(cashAvailable*rate+vo.getCashAvailable());
				vo.setCashHold(cashHold*rate+vo.getCashHold());
				vo.setCashValue((cashHold + cashAvailable)*rate+vo.getCashValue());
				vo.setCashWithdrawal(cashWithdrawal*rate+vo.getCashWithdrawal());
				vo.setMarketValue(marketValue*rate+vo.getMarketValue());
				vo.setTotalAssets(vo.getCashValue()+vo.getMarketValue()+vo.getTotalAssets());
			}
			
		}
		return vo;
	}
	
	/**
	 * 获取账户的所有资产列表
	 * @author mqzou
	 * @date 2016-11-24
	 * @param accountNo
	 * @return
	 */
	@Override
	public List<InvestorAccountCurrencyVO> findAccountCurrencyList(String accountNo) {
		List<InvestorAccountCurrencyVO> list=new ArrayList<InvestorAccountCurrencyVO>();
		String hql=" from PortfolioHoldAccount r where r.accountNo=? or r.subAccountNo=?";
		List params=new ArrayList();
		params.add(accountNo);
		params.add(accountNo);
		List resultList=this.baseDao.find(hql, params.toArray(), false);
		if(null!=resultList){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				PortfolioHoldAccount object = (PortfolioHoldAccount) it.next();
				InvestorAccountCurrencyVO vo=new InvestorAccountCurrencyVO();
				double cashAvailable=StrUtils.getDoubleVal(object.getCashAvailable());
				double cashHold=StrUtils.getDoubleVal(object.getCashHold());
				double cashwithdrawal=StrUtils.getDoubleVal(object.getCashWithdrawal());
				double marketValue=StrUtils.getDoubleVal(object.getMarketValue());
				vo.setCashAvailable(cashAvailable);
				vo.setCashHold(cashHold);
				vo.setCashValue(cashHold+cashAvailable);
				vo.setCashWithdrawal(cashwithdrawal);
				vo.setMarketValue(marketValue);
				vo.setTotalAssets(vo.getCashValue()+vo.getMarketValue());
				vo.setCurrency(object.getBaseCurrency());
				vo.setAccountNo(object.getAccountNo());
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 获取投资人rpq中的最高风险系数
	 * @author mqzou
	 * @date 2016-12-16
	 * @param memberId
	 * @return
	 */
	@Override
	public String findInvestorMaxRiskLevel(String memberId) {
		String hql="select max(riskLevel) from RpqExam r where r.member.id=? and r.isValid='1'";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=(Object)list.get(0);
			if(null!=object)
				return object.toString();
		}
		return "";
	}

	/**
	 * 检查投资人是否FACA或CIES
	 * @author mqzou
	 * @date 2016-12-16
	 * @param memberId
	 * @return
	 */
	@Override
	public boolean checkFacaOrCies(String memberId,String type) {
		String hql=" from InvestorAccount r where r.member.id=? and r."+type+"='1' and r.isValid='1'";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}
		return false;
	}

	
	
	/**
	 * 获取账户信息
	 * @author wwluo
	 * @date 2016-12-13
	 * @param 
	 * @return
	 */
	@Override
	public List<InvestorAccountVO> findInvestorByMember(MemberBase member,
			MemberIfa memberIfa,String langCode,String toCurrency) {
		List<InvestorAccountVO> accounts = null;
		if(member != null && memberIfa != null){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" InvestorAccount a" +
					" WHERE" +
					" a.isValid=1" +
					" AND" +
					" a.ifa=?" +
					" AND" +
					" a.member=?");
			List<Object> params = new ArrayList<Object>();
			params.add(memberIfa);
			params.add(member);
			List<InvestorAccount> investorAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
			accounts = new ArrayList<InvestorAccountVO>();
			if(investorAccounts != null && !investorAccounts.isEmpty()){
				for (InvestorAccount investorAccount : investorAccounts) {
					InvestorAccountVO target = new InvestorAccountVO();
					BeanUtils.copyProperties(investorAccount, target);
					MemberDistributor distributor = investorAccount.getDistributor();
					target.setDistributorIconUrl(PageHelper.getLogoUrl(distributor.getLogofile(), "D"));
					target.setDistributorName(distributor.getCompanyName());
					target.setDistributorId(distributor.getId());
					String baseCurrency = this.getParamConfigName(langCode, investorAccount.getBaseCurrency(), CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
					target.setBaseCurrency(baseCurrency);
					InvestorAccountCurrencyVO accountCurrency = this.findAccountCurrency(investorAccount.getAccountNo(),toCurrency);
					Double assets = accountCurrency.getTotalAssets();
					target.setTotalValue(assets.toString());
					Double cash = accountCurrency.getCashValue();
					target.setCashValue(cash.toString());
					accounts.add(target);
				}
			}
		}
		return accounts;
	}
	
	/**
	 * 获取ifa的客户的账户列表
	 * @author mqzou
	 * @date 2016-12-27
	 * @param ifaMemberId
	 * @return
	 */
	@Override
	public JsonPaging findCrmAccountList(JsonPaging jsonPaging,String ifaMemberId, String distributorId,String keyWord,String currency,String langCode) {
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append(" from InvestorAccount a left join MemberIfa i on a.ifa.id=i.id");
		hql.append(" left join CrmCustomer c on c.ifa.id=i.id and a.member.id=c.member.id");
		hql.append(" where a.isValid='1' and a.openStatus='3'");
		if(null!=ifaMemberId && !"".equals(ifaMemberId)){
			hql.append(" and i.member.id=?");
			params.add(ifaMemberId);
		}
		if(null!=distributorId && !"".equals(distributorId)){
			hql.append(" and a.distributor.id=?");
			params.add(distributorId);
		}
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and (a.accountNo like ? or c.nickName like ?)");
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(),jsonPaging, false);
		List<IfaClientAccountVO> list=new ArrayList<IfaClientAccountVO>();
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				Object[] object = (Object[]) it.next();
				InvestorAccount account=(InvestorAccount)object[0];
				CrmCustomer customer=(CrmCustomer)object[2];
				IfaClientAccountVO vo=new IfaClientAccountVO();
				vo.setAccountNo(account.getAccountNo());
				//vo.setNickName(customer.getNickName());
				vo.setBaseCurrency(account.getBaseCurrency());
				vo.setAccountId(account.getId());
				vo.setCreateDate(DateUtil.getDateStr(account.getCreateTime()));
				vo.setCrmId(customer.getId());
				vo.setDiscretionary(account.getAuthorized());
				vo.setDistriburotUrl(PageHelper.getLogoUrl(account.getDistributor().getLogofile(), "D"));
				vo.setDistributorId(account.getDistributor().getId());
				vo.setDistributorName(account.getDistributor().getCompanyName());
				vo.setIconUrl(PageHelper.getUserHeadUrl(account.getMember().getIconUrl(), ""));
				vo.setMemberId(account.getMember().getId());
				vo.setMemberType(String.valueOf(account.getMember().getSubMemberType()));
				List<CharDataVO> chartList=findAccountCurrency(account.getAccountNo(), "",currency);
				double totalAssets=0;
				//double cash=0;
				//double market=0;
				if(null!=chartList){
					Iterator iterator=chartList.iterator();
					while (iterator.hasNext()) {
						CharDataVO charDataVO = (CharDataVO) iterator.next();
						
						totalAssets+=charDataVO.getValue();
					}
				}
				vo.setTotalAccountValue(totalAssets);
				vo.setTotalAccountValueStr(StrUtils.getNumberString(totalAssets, "#,##0.00"));
				String nickName=getCommonMemberName(vo.getMemberId(), langCode, "2");
				vo.setNickName(nickName);
			    list.add(vo);	
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 根据投资人id获取它的所有InvestorAccount账号下的ifa数据
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public List<MemberIfa> findInvestorAccountListByInvestorId(String investorId) {
		//InvestorAccount iAccount = new InvestorAccount();
		List<MemberIfa> ifaList = new ArrayList<MemberIfa>();
		String hql = "FROM InvestorAccount a WHERE a.member.id=? and a.openStatus='3' ";
		List<InvestorAccount> list = this.baseDao.find(hql, new String[] { investorId }, false);
		if (!list.isEmpty()){
			for(InvestorAccount each : list){
				MemberIfa ifa = each.getIfa();
				if(null!=ifa){
					if(!ifaList.contains(ifa)){
						ifaList.add(ifa);
					}
				}
			}
		}
		return ifaList;
	}
	
	/**
	 * Investor投资首页，Most Selected Funds 从portfolio_hold_product跟portfolio_arena_produc中进行产品统计
	 * @author mqzou
	 * @date 2016-11-26
	 * @param ifa
	 * @return
	 */
	@Override
	public List<InvestorHomeMostSelectedFundsVO> getMostSelectedFunds(String langCode) {
		List<InvestorHomeMostSelectedFundsVO> fundsList=new ArrayList<InvestorHomeMostSelectedFundsVO>();
		List<Object> params=new ArrayList<Object>();
		StringBuilder hql=new StringBuilder();
		hql.append(" call pro_mostselectedfunds() ");

		List list=this.springJdbcQueryManager.springJdbcQueryForList(hql.toString(), null);
		
		int titleNum = 0;
		Iterator it = (Iterator) list.iterator();
	    while (it.hasNext()) {
	    	Map map = (HashMap) it.next();
	    	String productId = getMapObject(map, "product_id");
	    	String portfoliocount = getMapObject(map, "portfoliocount");
	    	FundInfo fundInfo = fundInfoService.getFundInfoByProduct(productId);
	    	if(null!=fundInfo){
	    		InvestorHomeMostSelectedFundsVO vo = new InvestorHomeMostSelectedFundsVO();
	    		String  fundId = fundInfo.getId();
	    		vo.setFundId(fundId);
	    		if("en".equals(langCode)){
		    		FundInfoEn info = (FundInfoEn) baseDao.get(FundInfoEn.class, fundId);
		    		if(null!=info)vo.setFundName(info.getFundName());
		    	}
		    	else if("sc".equals(langCode)){
		    		FundInfoSc info = (FundInfoSc) baseDao.get(FundInfoSc.class, fundId);
		    		if(null!=info)vo.setFundName(info.getFundName());
		    	}
		    	else if("en".equals(langCode)){
		    		FundInfoTc info = (FundInfoTc) baseDao.get(FundInfoTc.class, fundId);
		    		if(null!=info)vo.setFundName(info.getFundName());
		    	}
	    		vo.setRiskValue(fundInfo.getRiskLevel());
	    		titleNum++;
		    	vo.setTitleNum(titleNum);
		    	vo.setRankingNum(portfoliocount);
		    	fundsList.add(vo);
	    	}
	    	//System.out.println(portfoliocount);
	    }

		return fundsList;
	}
	
	/**
	 * Top Performance Portfolio
	 * author:林文伟
	 * modify by mqzou 2017-05-10 添加权限控制
	 * @param investorId
	 * @return
	 */
	@Override
	public List<InvestorHomeTopPerformancePortfolioVO> findTopPerformancePortfolio(String langCode,String periodType, String displayColor,String memberId) {
		List<InvestorHomeTopPerformancePortfolioVO> newList = new ArrayList<InvestorHomeTopPerformancePortfolioVO>();
		//List<MemberIfa> ifaList = new ArrayList<MemberIfa>();
		StringBuilder hql=new StringBuilder();
		hql.append(  " select distinct b from PortfolioArena b ");
		hql.append(" left join  WebView v on b.id=v.relateId");
		hql.append(" left join WebViewDetail d on v.id=d.view.id");
		hql.append(" left join MemberIfa i on b.creator.id=i.member.id");
		hql.append(" left join CrmCustomer u on i.id=u.ifa.id");
		hql.append("  left join WebFriend f on b.creator.id=f.fromMember.id ");
		hql.append(" left join IfafirmTeamIfa t on i.id=t.ifa.id");
		hql.append("  AND ((v.scopeFlag='3'  AND (");
		//hql.append(" AND  (( b.creator.id=? )");
		//hql.append("  OR (v.scopeFlag='3'  AND (");
		hql.append(" (v.clientFlag='1' AND u.clientType='1'  AND u.member.id=?) ");
		hql.append(" OR  (v.clientFlag='0' AND 1=0)");
		hql.append("  OR  (v.clientFlag='-1' AND d.toMember.id=?)");
		hql.append(" OR (v.prospectFlag='1' AND u.clientType='0'  AND u.member.id=?) ");
		hql.append(" OR  (v.prospectFlag='0' AND 1=0)");
		hql.append(" OR  (v.prospectFlag='-1' AND  d.toMember.id=?)");
		hql.append(" OR (v.buddyFlag='1' AND  f.toMember.id=?)");
		hql.append(" OR  (v.buddyFlag='0' AND 1=0)");
		hql.append(" OR  (v.buddyFlag='-1' AND   d.toMember.id=?)");
		hql.append(" OR (v.colleagueFlag='1' AND t.team.id IN  (SELECT b.team.id FROM MemberIfa a INNER JOIN IfafirmTeamIfa b ON a.id=b.ifa.id  AND a.member.id=? ))");
		hql.append(" OR  (v.colleagueFlag='0' AND 1=0)");
		hql.append("  OR  (v.colleagueFlag='-1' AND   d.toMember.id=?)");
		hql.append(" ))OR v.scopeFlag='2')");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		params.add(memberId);
		hql.append( " WHERE  (b.creator.id=? OR b.isPublic='1') AND b.totalReturn IS NOT NULL AND b.isValid=1 ORDER BY b.totalReturn DESC");
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		int titleNum=0;
		if(list!=null && !list.isEmpty()){
			for(int x=0;x<list.size();x++){
				titleNum++;
				if(x>4)break;
				InvestorHomeTopPerformancePortfolioVO vo = new InvestorHomeTopPerformancePortfolioVO();
				 PortfolioArena portfolioArena = (PortfolioArena)list.get(x);
					if(null!=portfolioArena){
						String name = portfolioArena.getPortfolioName();
						vo.setPortfolioId(portfolioArena.getId());
						vo.setPortfolioName(name);
						if (portfolioArena.getRiskLevel() != null) {
							vo.setRiskLevel(String.valueOf(portfolioArena.getRiskLevel()));
						}else{
							vo.setRiskLevel(""); // 置空，用于默认基金风险级别
						}
						vo.setIncreaseImagePath(this.getPerformanceChartImage(portfolioArena.getId(), "middle", displayColor));
						vo.setIncreasePercent(portfolioArena.getTotalReturn());
					}
				 vo.setTitleNum(titleNum);
				 newList.add(vo);
			}
		}
		return newList;
	}
	
	/**
	 * Top Performance Funds
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public List<InvestorHomeTopPerformanceFundsVO> findTopPerformanceFunds(String langCode,String periodType, String displayColor) {
		List<InvestorHomeTopPerformanceFundsVO> newList = new ArrayList<InvestorHomeTopPerformanceFundsVO>();
		//List<MemberIfa> ifaList = new ArrayList<MemberIfa>();
		String hql = "FROM FundReturn a";
		hql += " LEFT JOIN  "+this.getLangString("FundInfo", langCode)+" d ON d.id=a.fund.id";
		hql += " LEFT JOIN  "+this.getLangString("FundReturn", langCode)+" c ON a.periodCode=c.periodCode";
		
		hql += " WHERE 1=1 ";
		if(StringUtils.isNotBlank(periodType)){
			hql += " AND c.periodCode='"+periodType+"'";
		}
		hql += "  AND a.isValid=1 ORDER BY a.increase DESC";
		//hql += " LIMIT 0,5";
		List list = this.baseDao.find(hql, null, false);
		
		if(list!=null && !list.isEmpty()){
			for(int x=0;x<list.size();x++){
				if(x>4)break;
				InvestorHomeTopPerformanceFundsVO vo = new InvestorHomeTopPerformanceFundsVO();
				 Object[] each2 = (Object[])list.get(x);
				 Object questObj0 =(Object)each2[0];//FundReturn实体
				 Object questObj1 =(Object)each2[1];//FundInfoTc实体
				 Object questObj2 =(Object)each2[2];//FundReturnTc实体
				 if(questObj0 instanceof FundReturn){
					 FundReturn fundReturn = (FundReturn)questObj0;
					if(null!=fundReturn){
						FundInfo fund = fundReturn.getFund();
						if(null!=fund&&""!=fund.getId()){
							double increase = (fundReturn.getIncrease());
							vo.setIncrease(increase);
							String percentString = PageHelper.getPercentString(increase);
							vo.setIncreasePercentStr(percentString);
							vo.setFundId(fundReturn.getFund().getId());
							FundInfo info=fundReturn.getFund();
							vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
							vo.setIncreaseImagePath(getPerformanceChartImage(fund.getId(), "middle", displayColor));
							if(questObj1 instanceof FundInfoEn){
								 FundInfoEn fundInfoEn = (FundInfoEn)questObj1;
									if(null!=fundInfoEn){
										String name = fundInfoEn.getFundName();
										vo.setFundName(name);
										
									}
							 }
							 else if(questObj1 instanceof FundInfoSc){
								 FundInfoSc fundInfoEn = (FundInfoSc)questObj1;
									if(null!=fundInfoEn){
										String name = fundInfoEn.getFundName();
										vo.setFundName(name);
									}
							 }
							 else if(questObj1 instanceof FundInfoTc){
								 FundInfoTc fundInfoEn = (FundInfoTc)questObj1;
									if(null!=fundInfoEn){
										String name = fundInfoEn.getFundName();
										vo.setFundName(name);
									}
							 }
							 
							 if(questObj2 instanceof FundReturnSc){
								 FundReturnSc fundReturnSc = (FundReturnSc)questObj2;
									if(null!=fundReturnSc){
										String name = fundReturnSc.getPeriodName();
										vo.setPeriod(name);
									}
							 }
							 else if(questObj2 instanceof FundReturnTc){
								 FundReturnTc fundReturnSc = (FundReturnTc)questObj2;
									if(null!=fundReturnSc){
										String name = fundReturnSc.getPeriodName();
										vo.setPeriod(name);
									}
							 }
							 else  if(questObj2 instanceof FundReturnEn){
								 FundReturnEn fundReturnSc = (FundReturnEn)questObj2;
									if(null!=fundReturnSc){
										String name = fundReturnSc.getPeriodName();
										vo.setPeriod(name);
									}
							 }
							 //vo.setTitleNum(x++);
							 newList.add(vo);
						}
						
					}
				 }
				 
			}
		}
		return newList;
	}
	
	/**
	 * My zone My All Portfolio Detail 
	 * author:林文伟
	 * modify by mqzou 2017-01-10 增加需要转换的货币参数
	 * @param investorId
	 * @return
	 */
	@Override
	public List<InvestorMyPortfolioVO> findMyAllPortfolio(String memberId,String langCode,String currency) {
		List<InvestorMyPortfolioVO> newList = new ArrayList<InvestorMyPortfolioVO>();
		StringBuilder hql=new StringBuilder();
	    hql.append(" from PortfolioHold h where h.member.id=? and h.ifFirst='0'");
	    List<Object> params=new ArrayList<Object>();
	    params.add(memberId);
	    InvestorMyPortfolioVO totalVo=new InvestorMyPortfolioVO();
	    List  list=this.baseDao.find(hql.toString(), params.toArray(), false);
	    if(null!=list && !list.isEmpty()){
	    	Iterator it=list.iterator();
	    	int index=1;
	    	while (it.hasNext()) {
				PortfolioHold hold = (PortfolioHold) it.next();
				InvestorMyPortfolioVO vo=new InvestorMyPortfolioVO();
				String baseCurrency=hold.getBaseCurrency();
				 vo.setPortfolioId(hold.getId());
				 vo.setPortfolioName(hold.getPortfolioName());
				 vo.setReturnRate(null!=hold.getTotalReturnRate()?hold.getTotalReturnRate():0);
				 totalVo.setReturnRate(totalVo.getReturnRate()+vo.getReturnRate());
				 double returnValue=null!=hold.getTotalReturnValue()?hold.getTotalReturnValue():0;
				 if(currency.equals(baseCurrency)){
					 vo.setReturnValue(getExchangeRate(baseCurrency, currency)*returnValue);
					 
				 }else {
					vo.setReturnValue(returnValue);
				}
				 totalVo.setReturnValue(totalVo.getReturnValue()+vo.getReturnValue());
				 AssetsTotalVo assetsTotalVo=tradeCoreService.getPortfolioTotalAssets(hold.getId(), currency);
				 if(null!=assetsTotalVo){
					 vo.setMarketValue(assetsTotalVo.getTotalMarketValue());
					 vo.setTotalCash(assetsTotalVo.getTotalCash());
					 vo.setTotalAssets(assetsTotalVo.getTotalAssets());
					 totalVo.setMarketValue(totalVo.getMarketValue()+vo.getMarketValue());
					 totalVo.setTotalCash(totalVo.getTotalCash()+vo.getTotalCash());
					 totalVo.setTotalAssets(totalVo.getTotalAssets()+vo.getTotalAssets());
				 }
				 vo.setSort(index);
				 index++;
				 newList.add(vo);
			}
	    }
	    //如果有组合，则添加一个总计算
	    if(null!=newList && !newList.isEmpty()){
	    	  totalVo.setPortfolioName("All Portfolio");
	  	    totalVo.setSort(0);
	  		newList.add(0, totalVo);//插入第一个
	    }
		//List<MemberIfa> ifaList = new ArrayList<MemberIfa>();
	//	String currency = "";
		//
		/*List<String> holdArray = new ArrayList<String>();
		String hql1 = "FROM PortfolioHoldProduct a WHERE a.account.id IN (SELECT b.id FROM  InvestorAccount b WHERE b.member.id=?)";
		List<PortfolioHoldProduct> list1 = this.baseDao.find(hql1, new String[]{memberId}, false);
		for(PortfolioHoldProduct each : list1){
			PortfolioHold hold = each.getPortfolioHold();
			if(hold!=null&&!"".equals(hold.getId())){
				if(!holdArray.contains(hold.getId()))holdArray.add(hold.getId());
			} else{
				holdArray.add("");
			}
		}
		
		int i=1;
		for(String holdId :holdArray){
			
			double totalAssets = 0;
			double totalAccountCashAvailable = 0;
			double totalMarketValue = 0;
			String hql = "";
			List<String> params = new ArrayList<String>();
			
			if("".equals(holdId)){
				i++;
				hql = "FROM PortfolioHoldProduct a WHERE (a.portfolioHold.id = '' or a.portfolioHold.id is null)  and  a.account.id IN (SELECT b.id FROM  InvestorAccount b WHERE b.member.id=?)";
				params.add( memberId );
			}
			else{
				hql = "FROM PortfolioHoldProduct a WHERE a.portfolioHold.id = ? and  a.account.id IN (SELECT b.id FROM  InvestorAccount b WHERE b.member.id=?)";
				params.add( holdId );
				params.add( memberId );
			}
			
			List<PortfolioHoldProduct> list = this.baseDao.find(hql, params.toArray(), false);
			
			InvestorMyPortfolioVO vo = new InvestorMyPortfolioVO();
			vo.setPortfolioName("portfolioName");
			for(PortfolioHoldProduct each : list){
				if(null!=each){
					
					
					if(null!=each.getPortfolioHold()){
						String portfolioName =	each.getPortfolioHold().getPortfolioName();
						vo.setPortfolioName(portfolioName);
					} else{
						//获取该账号下可用现金
						InvestorAccount account = each.getAccount();
						String accountId = account.getId();
						String nickName = account.getMember().getNickName();
						vo.setPortfolioName( nickName + " Portfolio " + i);
					}
					

					double holdingUnit = each.getHoldingUnit();//持有份额
					double referenceCost = each.getReferenceCost();//参考成本
					double eachTotalAssets = holdingUnit*referenceCost;
					currency = each.getBaseCurrency();
					vo.setCurrency(currency);
					
					totalAssets += eachTotalAssets;
					
					
				}
			}
			
			//
			String hql2=" from PortfolioHoldAccount r where r.portfolioHold.id = ? and r.member.id=? ";
			List<PortfolioHoldAccount> resultList=this.baseDao.find(hql2,new String[]{holdId,memberId}, false);
			if(null!=resultList){
				Iterator it=resultList.iterator();
				while (it.hasNext()) {
					PortfolioHoldAccount object = (PortfolioHoldAccount) it.next();
					if(null!=object.getCashAvailable()){
						double cashAvailable = object.getCashAvailable();//可用现金
						totalAccountCashAvailable += cashAvailable;
						totalAssets += totalAccountCashAvailable;
					}
				}
			}
			
			//
			if(StringUtils.isNotBlank(holdId)){
				List<PortfolioHoldCumperf> cumperfList = this.getTheLatestPortfolioHoldCumperf(holdId);
				if(null!=cumperfList&&!cumperfList.isEmpty()){
					PortfolioHoldCumperf cumperf = cumperfList.get(0);
					double cumulativePl = cumperf.getCumulativePl();
					double cumulativRrate = cumperf.getCumulativeRate();
					totalMarketValue+=cumulativePl;
					vo.setMarketValue(cumulativePl);
					vo.setReturnRate(cumulativRrate);
					vo.setCurrency(currency);
				}
			}
			
			
			vo.setTotalCash(totalAccountCashAvailable);
			vo.setTotalAssets(totalAssets);
			vo.setSort(i);
			newList.add(vo);
		}
		
		
		
		
		if(null!=newList){
			if(newList.size()>0){
				double allAssets = 0;
				double allAccountCashAvailable = 0;
				double totalMarketValue = 0;
				for(InvestorMyPortfolioVO each : newList){
					allAssets += each.getTotalAssets();
					allAccountCashAvailable += each.getTotalCash();
					if(each.getMarketValue()!=null){
						totalMarketValue+= each.getMarketValue();
					}
					
				}
				InvestorMyPortfolioVO allVO = new InvestorMyPortfolioVO();
				allVO.setPortfolioName("My All Portfolio");
				allVO.setTotalCash(allAccountCashAvailable);
				allVO.setTotalAssets(allAssets);
				allVO.setSort(0);
				allVO.setMarketValue(totalMarketValue);
				newList.add(allVO);
			}
		}
		
		Collections.sort(newList, new Comparator() {
            public int compare(Object a, Object b) {
                int one = ((InvestorMyPortfolioVO) a).getSort();
                int two = ((InvestorMyPortfolioVO) b).getSort();
                return one - two;
            }
        });*/
		
		return newList;
	}
	
	/*******************************************************************************************/
	/**
	 * 登录成功后获取SSO实体
	 * author:林文伟
	 * @param accountCode
	 * @return
	 */
	@Override
	public  MemberAccountSso  getMemberAccountSso(String accountCode) {
		String hql="from MemberAccountSso r where r.accountCode='"+accountCode+"' and (sessionId is not null or sessionId<>'') ";
		List<MemberAccountSso> listSSO = baseDao.find(hql,null, false);
		if(null!=listSSO&&!listSSO.isEmpty())return listSSO.get(0);
		else return null;
	}
	
	/**
	 * 根据投资人id获取它的所有InvestorAccount账号
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public List<InvestorAccount> findInvestorAccountListByMemberId(String memberId) {
		String hql = "FROM InvestorAccount a WHERE a.member.id=?";
		List<InvestorAccount> list = this.baseDao.find(hql, new String[] { memberId }, false);
		
		return list;
	}
	
	/**
	 * 根据查询条件获取投资人的投资账户
	 * @author michael
	 * @param memberId
	 * @param criterias
	 * @return
	 */
	public List<InvestorAccount> findInvestorAccountList(String memberId, InvestorAccount criterias) {
		List<String> params = new ArrayList<String>();
		String hql = "FROM InvestorAccount a WHERE a.member.id=? ";
		params.add(memberId);
		if (null!=criterias){
			if (!StrUtils.isEmpty(criterias.getIsValid())){
				hql += " and a.isValid=?";
				params.add(criterias.getIsValid());
			}
			if (!StrUtils.isEmpty(criterias.getFinishStatus())){
				hql += " and a.finishStatus=?";
				params.add(criterias.getFinishStatus());
			}
			if (!StrUtils.isEmpty(criterias.getFlowStatus())){
				hql += " and a.flowStatus=?";
				params.add(criterias.getFlowStatus());
			}
			if (!StrUtils.isEmpty(criterias.getOpenStatus())){
				hql += " and a.openStatus=?";
				params.add(criterias.getOpenStatus());
			}
			if (!StrUtils.isEmpty(criterias.getSubmitStatus())){
				hql += " and a.submitStatus=?";
				params.add(criterias.getSubmitStatus());
			}
			if (!StrUtils.isEmpty(criterias.getAccType())){
				hql += " and a.accType=?";
				params.add(criterias.getAccType());
			}
			if (!StrUtils.isEmpty(criterias.getFromType())){
				hql += " and a.fromType=?";
				params.add(criterias.getFromType());
			}
			if (!StrUtils.isEmpty(criterias.getSubFlag())){
				if ("1".equals(StrUtils.getString(criterias.getSubFlag()))){
					hql += " and a.subFlag=?";
					params.add(criterias.getSubFlag());
				}else{
					hql += " and ( a.subFlag is null or a.subFlag='0' )";
				}
			}
		}
		List<InvestorAccount> list = this.baseDao.find(hql, params.toArray(), false);
		
		return list;
	}
	
	/**
	 * 根据投资人accountNo获取它的所有InvestorAccount账号
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public List<InvestorAccount> findInvestorAccountListByAccountNo(String accountNo) {
		String hql = "FROM InvestorAccount a WHERE a.accountNo=?";
		List<InvestorAccount> list = this.baseDao.find(hql, new String[] { accountNo }, false);
		
		return list;
	}
	
	/**
	 * 获取审批角色下的所有MemberBase列表
	 * @author michael
	 * @date 2016-12-30
	 * @param companyId 运营公司id
	 * @param accountId 账户id
	 * @param roleType 审批角色类型（表member_console_check_role）
	 */
	public List findMemberByRole(String companyId, String accountId, String roleType){
		List<String> params = new ArrayList<String>();
		String hql="from MemberBase m where m.id in (select c.member.id from MemberConsoleCheckRole c where c.roleType=? ";
		params.add( roleType );
		if (!StrUtils.isEmpty(companyId)){
			hql+=" and c.company.id=?";
			params.add( companyId );
		}
		hql+=" )";
//		if (!StrUtils.isEmpty(accountId)){
//			if (roleType.startsWith("I")){
//				hql+=" and m.id in (select c.member.id from MemberAdmin c left join MemberIfaIfafirm i on c.ifafirm.id=i.ifafirm.id left join InevstorAccount a on i.ifa.id=a.ifa.id " +
//						"where c.type='1' and c.ifafirm.id is not null and i.ifafirm.id is not null and i.ifa.id is not null and a.ifa.id is not null and a.id=?)";
//				
//			}else if (roleType.startsWith("D")){
//				hql+=" and m.id in (select c.member.id from MemberAdmin c left join InevstorAccount a on c.distributor.id=a.distributor.id " +
//						"where c.type='2' and c.distributor.id is not null and a.distributor.id is not null and a.id=?)";
//			}else{
//				hql+=" and m.id=? ";//不返回结果
//			}
//			params.add( accountId );
//		}
		List<MemberConsoleCheckRole> list = baseDao.find(hql,params.toArray(), false);
		return list;
	}
	
	public  List<PortfolioHoldCumperf>  getTheLatestPortfolioHoldCumperf(String holdId) {
		String hql="from PortfolioHoldCumperf r where r.portfolioHold.id='"+holdId+"' order by valuation_date desc ";
		List<PortfolioHoldCumperf> list = baseDao.find(hql,null, false);
		return list;
	}
	
	/**
	 * 根据holdId获取它的所有PortfolioHoldCumperf data
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public List<PortfolioHoldCumperf> findPortfolioHoldCumperfListByHoldId(String holdId) {
		String hql = "FROM PortfolioHoldCumperf a WHERE a.portfolioHold.id=?";
		List<PortfolioHoldCumperf> list = this.baseDao.find(hql, new String[] { holdId }, false);
		
		return list;
	}

	/**
	 * PortfolioHoldAccount
	 * @author wwluo
	 * @date 2017-01-08
	 * @param distributorId
	 * @param ifaId
	 * @return
	 */
	@Override
	public PortfolioHoldAccount findPortfolioHoldAccount(String accountNo,
			String ifaId, String langCode, Object toCurrency) {
		PortfolioHoldAccount portfolioHoldAccount = null;
		if(StringUtils.isNotBlank(accountNo) && StringUtils.isNotBlank(ifaId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldAccount a" +
					" WHERE" +
					" a.isValid=1" +
					" AND" +
					" a.accountNo=?" +
					" AND a.ifa.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(accountNo);
			params.add(ifaId);
			List<PortfolioHoldAccount> accounts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(accounts != null&& !accounts.isEmpty()){
				portfolioHoldAccount = accounts.get(0);
			}
		}
		return portfolioHoldAccount;
	}

	/**
	 * InvestorAccountVO
	 * @author wwluo
	 * @date 2017-01-08
	 * @param investorAccount
	 * @return
	 */
	@Override
	public InvestorAccountVO findPortfolioHoldAccount(
			InvestorAccount investorAccount, String langCode) {
		InvestorAccountVO target = null;
		if(investorAccount != null){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" PortfolioHoldAccount a" +
					" WHERE" +
					" a.isValid=1" +
					" AND" +
					" a.account=?" +
					" AND" +
					" (a.portfolioHold IS NULL" +
					" OR" +
					" NOT EXISTS" +
					" (FROM" +
					" PortfolioHoldProduct p" +
					" WHERE" +
					" p.portfolioHold.id=a.portfolioHold.id)" +
					" )" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(investorAccount);
			List<PortfolioHoldAccount> portfolioHoldAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(portfolioHoldAccounts != null && !portfolioHoldAccounts.isEmpty()){
				for (PortfolioHoldAccount portfolioHoldAccount : portfolioHoldAccounts) {
					//PortfolioHold hold = portfolioHoldAccount.getPortfolioHold();
					target = new InvestorAccountVO();
					BeanUtils.copyProperties(portfolioHoldAccount, target);
					InvestorAccount account = portfolioHoldAccount.getAccount();
					if(account != null){
						target.setId(account.getId());
						MemberDistributor distributor = account.getDistributor();
						target.setDistributorIconUrl(PageHelper.getLogoUrl(distributor.getLogofile(), "D"));
						target.setDistributorName(distributor.getCompanyName());
						target.setDistributorId(distributor.getId());
						target.setAccountNo(account.getAccountNo());
					}
					String currencyName = this.getParamConfigName(langCode, portfolioHoldAccount.getBaseCurrency(), CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
					target.setBaseCurrency(portfolioHoldAccount.getBaseCurrency());
					target.setCurrency(currencyName);
					Double cashAvailable = portfolioHoldAccount.getCashAvailable();
					Double cashHold = portfolioHoldAccount.getCashHold();
					Double cashValue = cashAvailable + cashHold;
					Double totalValue = cashValue + portfolioHoldAccount.getMarketValue();
					target.setCashValue(cashValue.toString());
					target.setTotalValue(totalValue.toString());
				}
			}
		}
		return target;
	}

	/**
	 * 获取投资者的可用账户
	 * @author wwluo
	 * @date 2017-01-09
	 */
	@Override
	public InvestorAccount findPortfolioHoldAccount(String distributorId,
			String ifaId) {
		InvestorAccount account = null;
		if(StringUtils.isNotBlank(distributorId) && StringUtils.isNotBlank(ifaId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" InvestorAccount a" +
					" WHERE" +
					" a.isValid=1" +
					" AND" +
					" a.distributor.id=?" +
					" AND" +
					" a.ifa.id=?" +
					" AND" +
					" (a.portfolioHold IS NULL" +
					" OR" +
					" NOT EXISTS" +
					" (FROM" +
					" PortfolioHoldProduct p" +
					" WHERE" +
					" p.portfolioHold.id=a.portfolioHold.id)" +
					" )" +
					" ");
			List<Object> params = new ArrayList<Object>();
			params.add(distributorId);
			params.add(ifaId);
			List<InvestorAccount> investorAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(investorAccounts!=null && !investorAccounts.isEmpty()){
				account = investorAccounts.get(0);
			}
		}
		return account;
	}
	
	/**
	 * GET IFA AE CODE
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public IfaDistributor findIfaDistributorByIfaId(String ifaId) {
		String hql = "FROM IfaDistributor a WHERE a.ifa.id=?";
		List<IfaDistributor> list = this.baseDao.find(hql, new String[] { ifaId }, false);
		if(null!=list&&!list.isEmpty())return list.get(0);
		return null;
	}
	/**
	 * 获取投资人的持仓账户
	 * @author mqzou
	 * @date 2017-01-09
	 */
	@Override
	public List<String> findInvestorHoldAccount(String memberId, String portfolioHoldId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from PortfolioHoldAccount r where 1=1");
		List<Object> params=new ArrayList<Object>();
		if(null!=memberId && !"".equals(memberId)){
			hql.append(" and r.portfolioHold.member.id=?");
			params.add(memberId);
		}
		if(null!=portfolioHoldId && !"".equals(portfolioHoldId)){
			hql.append(" and r.portfolioHold.id=?");
		}
		List resultList=this.baseDao.find(hql.toString(), params.toArray(), false);
		List<String> list=new ArrayList<String>();
		if(null!=resultList && !resultList.isEmpty()){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				PortfolioHoldAccount account = (PortfolioHoldAccount) it.next();
				list.add(account.getAccountNo());
			}
		}
		return list;
	}
	
	/**
	 * 通过账号ID获取该账号所持有的PortfolioHoldProduct列表
	 * author:林文伟
	 * @param investorId
	 * @return
	 */
	@Override
	public List<PortfolioHoldProduct> findPortfolioHoldProductByAccountId(String accountId) {
		String hql = "FROM PortfolioHoldProduct a WHERE a.account.id=?";
		List<PortfolioHoldProduct> list = this.baseDao.find(hql, new String[] { accountId }, false);
		return list;
	}
	
	/**
	 * 获取投资人的ifa
	 * @author mqzou
	 * @date 2017-01-11
	 * @param memberId
	 * @return
	 */
	@Override
	public List<MemberIfa> findInvestorIfa(String memberId) {
		List<MemberIfa> list=new ArrayList<MemberIfa>();
		StringBuilder hql=new StringBuilder();
		//hql.append(" from CrmCustomer r where r.member.id=?");
		hql.append(" select distinct r.ifa from InvestorAccount r where r.member.id=? and r.openStatus='3'");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List resultList =this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=resultList && !resultList.isEmpty()  ){
			Iterator it=resultList.iterator();
			while (it.hasNext()) {
				MemberIfa crm = (MemberIfa) it.next();
				list.add(crm);
			}
		}
		return list;
	}
	
	/**
	 * 获取投资人的好友
	 * @author mqzou
	 * @date 2017-01-11
	 * @param memberId
	 * @param count
	 * @return
	 */
	@Override
	public List<WebFriendVO> findInvestorBuddyList(String memberId, int count) {
		List<WebFriendVO> list=new ArrayList<WebFriendVO>();
		StringBuilder hql=new StringBuilder();
		hql.append(" select i from WebFriend r left join MemberIfa i on r.toMember.id=i.member.id");
		hql.append(" where r.fromMember.id=? and r.checkResult='1' and i!=null");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		JsonPaging jsonPaging=new JsonPaging();
		jsonPaging.setRows(count);
		jsonPaging =this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		if(null!=jsonPaging && null!=jsonPaging.getList()){
			Iterator it=jsonPaging.getList().iterator();
			while (it.hasNext()) {
				MemberIfa ifa = (MemberIfa) it.next();
				WebFriendVO vo=new WebFriendVO();
				vo.setMemberId(ifa.getMember().getId());
				vo.setFirstName(ifa.getFirstName());
				vo.setIconUrl(ifa.getMember().getIconUrl());
				vo.setLastName(ifa.getLastName());
				vo.setNickName(ifa.getMember().getNickName());
				vo.setNameChn(ifa.getNameChn());
				vo.setGender(ifa.getGender());
				list.add(vo);
			}
		}
		return list;
	}
	
	/**
	 * 根据条件搜索代理商
	 * @param searchVo
	 * @return
	 */
	public List findDistributorByCriteria(ClientSearchVO searchVo) {

		StringBuilder sql = new StringBuilder();
		//sql.append(" SELECT DISTINCT r.`distributor_id`,m.logofile,m.`company_name`,p.expire_date rpqDate,ifnull(d.expire_date,now()) docDate FROM investor_account r ");
		sql.append(" SELECT DISTINCT r.distributor.id ,m.logofile,m.companyName FROM MemberDistributor m ");
		sql.append(" LEFT JOIN InvestorAccount r ON r.distributor.id=m.id");
		//sql.append(" LEFT JOIN (SELECT * FROM  `rpq_exam`  ORDER BY rpq_exam.expire_date DESC LIMIT 1)p ON r.`member_id`=p.member_id AND r.`distributor_id`=p.distributor_id");
		//sql.append(" LEFT JOIN (SELECT * FROM  `investor_doc` ORDER BY ifnull(expire_date,now()) ASC LIMIT 1 )d ON r.`member_id`=d.member_id AND r.`distributor_id`=p.distributor_id");
		sql.append(" WHERE  1=1 ");

		if (!StrUtils.isEmpty(searchVo.getClientId())) {
			sql.append(" and r.member.id='" + searchVo.getClientId() + "'");
		}
		if (!StrUtils.isEmpty(searchVo.getOpenStatus())) {
			sql.append(" and r.openStatus in (" + searchVo.getOpenStatus() + ")");
		}
		if (!StrUtils.isEmpty(searchVo.getCurrency())) {
			sql.append(" and r.baseCurrency='" + searchVo.getCurrency() + "'");
		}
		if (!StrUtils.isEmpty(searchVo.getIsValid())) {
			sql.append(" and r.isValid='" + searchVo.getIsValid() + "'");
		} else {
			sql.append(" and r.isValid='1' ");
		}
		if (!StrUtils.isEmpty(searchVo.getIfaId())) {
			sql.append(" and r.ifa_.id='" + searchVo.getIfaId() + "'");
		}
		if (!StrUtils.isEmpty(searchVo.getIfaFirmId())) {
			sql.append(" and r.ifa.id in (select ifa.id from MemberIfaIfafirm where ifafirm.id='" + searchVo.getIfaFirmId() + "')");
			sql.append(" and r.distributor.id in (select distributor.id from IfafirmDistributor where ifafirm.id='" + searchVo.getIfaFirmId() + "')");
		}
		if (!StrUtils.isEmpty(searchVo.getDistributorId())) {
			sql.append(" and r.distributor.id='" + searchVo.getDistributorId() + "'");
		}

//		List reList = this.springJdbcQueryManager.springJdbcQueryForList(sql.toString());
		List reList = this.baseDao.find(sql.toString(),null,false);
		List list = new ArrayList();
		Iterator it = reList.iterator();
		while (it.hasNext()) {
			Object[] map = (Object[]) it.next();
			InvestorDistributorVO vo = new InvestorDistributorVO();
			vo.setId(StrUtils.getString(map[0]));
			String logofile= StrUtils.getString(map[1]);
			vo.setCompanyName(StrUtils.getString(map[2]));
			
			vo.setLogoUrl(PageHelper.getLogoUrl(logofile, "D"));
//			String rpqDate = getMapObject(map, "rpqDate");
//			String dcDate = getMapObject(map, "docDate");
//			if ("".equals(rpqDate)) {
//				vo.setNextRPQDate("0");
//			}
//			if ("".equals(dcDate)) {
//				vo.setNextDCDate("0");
//			}
			list.add(vo);
		}

		return list;
	}
	
	/**
	 * 检查是否已有账户
	 * @param distributorId
	 * @param memberId
	 * @return
	 */
	public boolean checkAccount(String distributorId, String memberId) {
		String hql = " from InvestorAccount r where r.isValid='1'  ";

		hql += " and r.member.id='" + memberId + "'";
		hql += " and r.distributor.id='" + distributorId + "'";
		hql += " and r.finishStatus='1' ";//已提交
		hql += " and r.openStatus not in ('-1','4') ";//非 退回、开户失败 状态
		List reList = this.baseDao.find(hql, null, false);
		if (!reList.isEmpty()) return true;
		return false;
	}

	/**
	 * 检查是否已有账户号
	 * @param accountId
	 * @param accountNo
	 * @return
	 */
	public boolean checkAccountNo(String accountId, String accountNo) {
		String hql = " from InvestorAccount r where r.isValid='1'  ";
		if (!StrUtils.isEmpty(accountId))
			hql += " and r.id <> '" + accountId + "'";
		hql += " and r.accountNo='" + accountNo + "'";
//		hql += " and r.finishStatus='1' ";//已提交
//		hql += " and r.openStatus not in ('-1','4') ";//非 退回、开户失败 状态
		List reList = this.baseDao.find(hql, null, false);
		if (null!=reList && !reList.isEmpty()) return true;
		return false;
	}
	
	/**
	 * 获取InvestorAccount
	 * @param distributorId
	 * @param memberId
	 * @return
	 */
	@Override
	public List<InvestorAccount> findInvestorAccountsByDistributorMember(String distributorId,
			String memberId) {
		List<InvestorAccount> accounts = null;
		if (StringUtils.isNotBlank(distributorId) && StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" InvestorAccount i" +
					" WHERE" +
					" i.isValid=1" +
					" AND" +
					" i.member.id=?" +
					" AND" +
					" i.distributor.id=?" +
					//" AND" +
					//" i.subFlag<>1" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			params.add(distributorId);
			accounts = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return accounts;
	}


	/**
	 * 复制IFA的客户投资账户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaCustomerAccount(String fromMemberId,String toMemberId,MemberBase createBy) {
		List<InvestorAccount> list = findInvestorAccountListForIfa(fromMemberId, null, null);
		MemberIfa ifa = memberBaseService.findIfaMember(toMemberId);
		if (null!=list && !list.isEmpty() && null!=ifa && !StrUtils.isEmpty(ifa.getId())){
			for (InvestorAccount f: list){
				boolean status = false;
				try{
					//客户类型client_type有没要求，已存在的不加？
					f.setIfa(ifa);
					this.baseDao.update(f);
					
					status = true;
	            } catch (Exception e) {
	                // TODO: handle exception
	            }
	            
				//保存日志
				IfaMigrateHist hist = new IfaMigrateHist();
				hist.setCreateBy(createBy);
				hist.setCreateTime(new Date());
				hist.setFromMember(memberBaseService.findById(fromMemberId));
				hist.setToMember(ifa.getMember());
				hist.setCusMember(f.getMember());
				hist.setDataType("InvestorAccount");
				hist.setStatus(status?"1":"0");
				ifaMigrateHistService.saveOrUpdate(hist);
			
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 获取Ifa管理的客户列表
	 */
	@Override
	public JsonPaging findInvestorListForIfa(JsonPaging jsonPaging,	String ifaMemberId) {
		List<Object> params = new ArrayList<Object>();
		String hql = " FROM InvestorAccount l "
			+ " WHERE l.fromType='ifa' "
			+ " AND l.ifa.member.id=? ";
		params.add(ifaMemberId);
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging , false);
		return jsonPaging;
	}

	/**
	 * 获取我的IFA信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<IfaVO> getMyIfas(String memberId,String langCode) {
		List<IfaVO> vos = null;
		if (StringUtils.isNotBlank(memberId) && StringUtils.isNotBlank(langCode)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " DISTINCT"
					+ " b"
					+ " FROM"
					+ " InvestorAccount i"
					
					+ " LEFT JOIN"
					+ " MemberIfa b"
					+ " ON"
					+ " i.ifa.id=b.id"
					
					+ " WHERE"
					+ " i.isValid=1"
					+ " AND"
					+ " i.member.id=?"
					+ " AND"
					+ " i.openStatus=3" // 开户成功
					+ " ORDER BY"
					+ " i.lastUpdate"
					+ " DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			List<MemberIfa> ifas = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(ifas != null && !ifas.isEmpty()){
				vos = new ArrayList<IfaVO>();
				for (MemberIfa memberIfa : ifas) {
					if(memberIfa != null){
						IfaVO vo = new IfaVO(memberIfa, langCode);
						vo.setIconUrl(PageHelper.getUserHeadUrl(vo.getIconUrl(), vo.getGender()));
						vos.add(vo);
					}
				}
			}
		}
		return vos;
	}

	/**
	 * 获取推荐的组合
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<PortfolioArenaVO> getRecommendedPortfolios(String memberId, String isPublic, String langCode) {
		List<PortfolioArenaVO> arenas = null;
		String defPrefixHql = " SELECT a,r.increase FROM PortfolioArena a LEFT JOIN WebView v ON v.relateId=a.id";
		String othPrefixHql = " SELECT a,r.increase FROM WebView v LEFT JOIN PortfolioArena a ON v.relateId=a.id";
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(""
				+ " LEFT JOIN"
				+ " WebViewDetail d"
				+ " ON"
				+ " d.view.id=v.id"
				
				+ " LEFT JOIN"
				+ " CrmCustomer c"
				+ " ON"
				+ " d.toMember.id=c.member.id"
				
				+ " LEFT JOIN"
				+ " MemberIfa i"
				+ " ON"
				+ " i.id=c.ifa.id"
				
				+ " LEFT JOIN"
				+ " PortfolioArenaReturn r"        
				+ " ON"
				+ " r.portfolio.id=a.id"
				
				+ " WHERE"
				+ " v.moduleType=?"
				+ " AND"
				+ " a.isValid=1"
				+ " AND"
				+ " a.status=1"
				+ " AND"
				+ " r.periodCode=?"
				+ " ");
		params.add(CommonConstantsWeb.WEB_VIEW_MODULE_PORTFOLIO_ARENA);
		params.add(CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE + "1M");
		if (StringUtils.isNotBlank(isPublic) && StringUtils.isNotBlank(memberId)) {
			hql.insert(0, defPrefixHql);
			hql.append(" AND (a.isPublic=1 OR d.toMember.id=?)");
			params.add(memberId);
		}else if(StringUtils.isNotBlank(isPublic)){
			hql.insert(0, defPrefixHql);
			hql.append(" AND a.isPublic=1");
		}else if(StringUtils.isNotBlank(memberId)){
			hql.insert(0, othPrefixHql);
			hql.append(" AND d.toMember.id=?");
			params.add(memberId);
		}else{
			return arenas;
		}
		hql.append(" GROUP BY a ORDER BY v.lastUpdate DESC");
		List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(objs != null && !objs.isEmpty()){
			arenas = new ArrayList<PortfolioArenaVO>();
			for (Object[] objects : objs) {
				PortfolioArena arena = (PortfolioArena) objects[0];
				Double increase = (Double) objects[1];
				PortfolioArenaVO vo = new PortfolioArenaVO(arena);
				vo.setIncrease(increase);
				String region = arena.getGeoAllocation();
				if (StringUtils.isNotBlank(region)) {
					vo.setGeoAllocationDisplay(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, region, langCode));
				}
				String sector = arena.getSector();
				if (StringUtils.isNotBlank(sector)) {
					vo.setSectorDisplay(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, sector, langCode));
				}
				arenas.add(vo);
			}
		}
		return arenas;
	}

	/**
	 * 获取arean浏览记录
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<PortfolioArenaVO> getInvestorVisitArena(String memberId, String langCode) {
		List<PortfolioArenaVO> arenas = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " a,r.increase"
					+ " FROM"
					+ " WebInvestorVisit v"
					
					+ " LEFT JOIN"
					+ " PortfolioArena a"
					+ " ON"
					+ " v.relateId=a.id"
					
					+ " LEFT JOIN"
					+ " PortfolioArenaReturn r"        
					+ " ON"
					+ " r.portfolio.id=a.id"
					
					+ " WHERE"
					+ " v.moduleType=?"
					+ " AND"
					+ " v.member.id=?"
					+ " AND"
					+ " r.periodCode=?"
					+ " GROUP BY"
					+ " a"
					+ " ORDER BY"
					+ " v.vistiTime"
					+ " DESC"
					+ " ");
			List<Object> params = new ArrayList<Object>();
			params.add(CommonConstantsWeb.WEB_VISIT_PORTFOLIO_ARENA);
			params.add(memberId);
			params.add(CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE + "1M");
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objs != null && !objs.isEmpty()){
				arenas = new ArrayList<PortfolioArenaVO>();
				for (Object[] objects : objs) {
					PortfolioArena arena = (PortfolioArena) objects[0];
					Double increase = (Double) objects[1];
					PortfolioArenaVO vo = new PortfolioArenaVO(arena);
					vo.setIncrease(increase);
					String region = arena.getGeoAllocation();
					if (StringUtils.isNotBlank(region)) {
						vo.setGeoAllocationDisplay(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, region, langCode));
					}
					String sector = arena.getSector();
					if (StringUtils.isNotBlank(sector)) {
						vo.setSectorDisplay(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, sector, langCode));
					}
					arenas.add(vo);
				}
			}
		}
		return arenas;
	}

	/**
	 * 获取基础数据
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	private String getParamConfig(String type, String configCode, String langCode) {
		String name = null;
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(configCode)) {
			if (StringUtils.isBlank(langCode)) {
				langCode = CommonConstants.LANG_CODE_EN;
			}
			langCode = langCode.substring(0, 1).toUpperCase()+langCode.substring(1, 2);
			String[] objs = null;
			if(configCode.indexOf(",") > -1){
				objs = configCode.split(",");
			}else{
				objs = new String[]{configCode};
			}
			for (int i = 0; i < objs.length; i++) {
				StringBuffer hql = new StringBuffer("" +
						" SELECT" +
						" c.name" + langCode +
						" FROM" +
						" SysParamConfig c" +
						" WHERE" +
						" c.type.typeCode=?" +
						" AND" +
						" c.configCode=?");
				List<Object> params = new ArrayList<Object>();
				params.add(type);
				params.add(objs[i]);
				List<String> sysParamConfigs = this.baseDao.find(hql.toString(), params.toArray(), false);
				if(sysParamConfigs != null && !sysParamConfigs.isEmpty()){
					name = sysParamConfigs.get(0) + ",";
				}
			}
		}
		return StrUtils.reHeavy(name);
	}

	/**
	 * 获取推荐的策略
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<StrategyInfoVO> getRecommendedStrategies(String memberId,
			String isPublic, String langCode) {
		List<StrategyInfoVO> strategies = null;
		String defPrefixHql = " SELECT DISTINCT i FROM StrategyInfo i LEFT JOIN WebView v ON v.relateId=i.id";
		String othPrefixHql = " SELECT DISTINCT i FROM WebView v LEFT JOIN StrategyInfo i ON v.relateId=i.id";
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(""
				+ " LEFT JOIN"
				+ " WebViewDetail d"
				+ " ON"
				+ " d.view.id=v.id"
				
				+ " LEFT JOIN"
				+ " CrmCustomer c"
				+ " ON"
				+ " d.toMember.id=c.member.id"
				
				+ " LEFT JOIN"
				+ " MemberIfa m"
				+ " ON"
				+ " m.id=c.ifa.id"
				
				+ " WHERE"
				+ " v.moduleType=?"
				+ " AND"
				+ " i.isValid=1"
				+ " AND"
				+ " i.status=1"
				+ " ");
		params.add(CommonConstantsWeb.WEB_VIEW_MODULE_STRATEGY);
		if (StringUtils.isNotBlank(isPublic) && StringUtils.isNotBlank(memberId)) {
			hql.insert(0, defPrefixHql);
			hql.append(" AND (i.isPublic=1 OR v.toMember.id=?)");
			params.add(memberId);
		}else if(StringUtils.isNotBlank(isPublic)){
			hql.insert(0, defPrefixHql);
			hql.append(" AND i.isPublic=1");
		}else if(StringUtils.isNotBlank(memberId)){
			hql.insert(0, othPrefixHql);
			hql.append(" AND d.toMember.id=?");
			params.add(memberId);
		}else{
			return strategies;
		}
		hql.append(" ORDER BY v.lastUpdate DESC");
		List<StrategyInfo> strategyInfos = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(strategyInfos != null && !strategyInfos.isEmpty()){
			strategies = new ArrayList<StrategyInfoVO>();
			for (StrategyInfo strategyInfo : strategyInfos) {
				StrategyInfoVO strategy = new StrategyInfoVO();
				BeanUtils.copyProperties(strategyInfo, strategy);
				String region = strategyInfo.getGeoAllocation();
				if (StringUtils.isNotBlank(region)) {
					strategy.setGeoAllocationName(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, region, langCode));
				}
				String sector = strategyInfo.getSector();
				if (StringUtils.isNotBlank(sector)) {
					strategy.setSectorName(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, sector, langCode));
				}
				List<StrategyAllocation> strategyAllocations = getStrategyAllocation(strategy.getId());
				strategy.setStrategyAllocations(strategyAllocations);
				if(strategyAllocations != null && !strategyAllocations.isEmpty()){
					List<Map<String, Object>> allocationObj = new ArrayList<Map<String, Object>>();
					Integer itemWeightFund = 0;
					Integer itemWeightStock = 0;
					Integer itemWeightBond = 0;
					for (StrategyAllocation strategyAllocation : strategyAllocations) {
						Integer weight = strategyAllocation.getItemWeight();
						String type = strategyAllocation.getType();
						if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUND.equals(type) && weight != null){
							itemWeightFund += weight;
						}else if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_BOND.equals(type) && weight != null){
							itemWeightBond += weight;
						}else if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_STOCK.equals(type) && weight != null){
							itemWeightStock += weight;
						}
					}
					Map<String, Object> fundMap = new HashMap<String, Object>();
					fundMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUND);
					fundMap.put("value", itemWeightFund);
					if(itemWeightFund > 0){
						allocationObj.add(fundMap);
					}
					Map<String, Object> bondMap = new HashMap<String, Object>();
					bondMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_BOND);
					bondMap.put("value", itemWeightBond);
					if(itemWeightBond > 0){
						allocationObj.add(bondMap);
					}
					Map<String, Object> stockMap = new HashMap<String, Object>();
					stockMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_STOCK);
					stockMap.put("value", itemWeightStock);
					if(itemWeightStock > 0){
						allocationObj.add(stockMap);
					}
					strategy.setStrategyAllocationsObj(allocationObj);
				}
				strategies.add(strategy);
			}
		}
		return strategies;
	}

	/**
	 * 获取策略分配比例
	 * @author wwluo
	 * @data 2016-10-25
	 * @param strategyId 策略id
	 * @return 
	 */
	private List<StrategyAllocation> getStrategyAllocation(String strategyId) {
		List<StrategyAllocation> strategyAllocations = new ArrayList<StrategyAllocation>();
		if(StringUtils.isNotBlank(strategyId)){
			StringBuffer hql = new StringBuffer(" from StrategyAllocation a where a.layerLevel=1 and a.strategy.id=?");
			List params = new ArrayList();
			params.add(strategyId);
			strategyAllocations = this.baseDao.find(hql.toString(), params.toArray(), false);
		}
		return strategyAllocations;
	}
	
	/**
	 * 获取strategy浏览记录
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<StrategyInfoVO> getInvestorVisitStrategies(String memberId, String langCode) {
		List<StrategyInfoVO> strategies = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " DISTINCT"
					+ " i"
					+ " FROM"
					+ " WebInvestorVisit v"
					
					+ " LEFT JOIN"
					+ " StrategyInfo i"
					+ " ON"
					+ " v.relateId=i.id"
					
					+ " WHERE"
					+ " v.moduleType=?"
					+ " AND"
					+ " v.member.id=?"
					+ " ORDER BY"
					+ " v.vistiTime"
					+ " DESC"
					+ " ");
			List<Object> params = new ArrayList<Object>();
			params.add(CommonConstantsWeb.WEB_VISIT_STRATEGY);
			params.add(memberId);
			List<StrategyInfo> strategyInfos = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(strategyInfos != null && !strategyInfos.isEmpty()){
				strategies = new ArrayList<StrategyInfoVO>();
				for (StrategyInfo strategyInfo : strategyInfos) {
					StrategyInfoVO strategy = new StrategyInfoVO();
					if(null==strategyInfo)
						continue;
					BeanUtils.copyProperties(strategyInfo, strategy);
					String region = strategyInfo.getGeoAllocation();
					if (StringUtils.isNotBlank(region)) {
						strategy.setGeoAllocationName(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, region, langCode));
					}
					String sector = strategyInfo.getSector();
					if (StringUtils.isNotBlank(sector)) {
						strategy.setSectorName(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, sector, langCode));
					}
					List<StrategyAllocation> strategyAllocations = getStrategyAllocation(strategy.getId());
					strategy.setStrategyAllocations(strategyAllocations);
					if(strategyAllocations != null && !strategyAllocations.isEmpty()){
						List<Map<String, Object>> allocationObj = new ArrayList<Map<String, Object>>();
						Integer itemWeightFund = 0;
						Integer itemWeightStock = 0;
						Integer itemWeightBond = 0;
						for (StrategyAllocation strategyAllocation : strategyAllocations) {
							Integer weight = strategyAllocation.getItemWeight();
							String type = strategyAllocation.getType();
							if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUND.equals(type) && weight != null){
								itemWeightFund += weight;
							}else if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_BOND.equals(type) && weight != null){
								itemWeightBond += weight;
							}else if(CommonConstantsWeb.WEB_ALLOCATION_TYPE_STOCK.equals(type) && weight != null){
								itemWeightStock += weight;
							}
						}
						Map<String, Object> fundMap = new HashMap<String, Object>();
						fundMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_FUND);
						fundMap.put("value", itemWeightFund);
						if(itemWeightFund > 0){
							allocationObj.add(fundMap);
						}
						Map<String, Object> bondMap = new HashMap<String, Object>();
						bondMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_BOND);
						bondMap.put("value", itemWeightBond);
						if(itemWeightBond > 0){
							allocationObj.add(bondMap);
						}
						Map<String, Object> stockMap = new HashMap<String, Object>();
						stockMap.put("name", CommonConstantsWeb.WEB_ALLOCATION_TYPE_STOCK);
						stockMap.put("value", itemWeightStock);
						if(itemWeightStock > 0){
							allocationObj.add(stockMap);
						}
						strategy.setStrategyAllocationsObj(allocationObj);
					}
					strategies.add(strategy);
				}
			}
		}
		return strategies;
	}

	/**
	 * 获取推荐的基金
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<FundInfoDataVO> getRecommendedFunds(String memberId, String langCode) {
		List<FundInfoDataVO> funds = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " f,r.increase"
					+ " FROM"
					+ " WebRecommended v"
					
					+ " LEFT JOIN"
					+ " FundInfo f"
					+ " ON"
					+ " v.relateId=f.id"
					
					+ " LEFT JOIN"
					+ " MemberIfa i"
					+ " ON"
					+ " i.member.id=v.creator.id"
					
					+ " LEFT JOIN"
					+ " CrmCustomer c"
					+ " ON"
					+ " i.id=c.ifa.id"
					
					+ " LEFT JOIN"
					+ " FundReturn r"        
					+ " ON"
					+ " r.fund.id=f.id"
					
					+ " WHERE"
					+ " v.moduleType=?"
					+ " AND"
					+ " f.isValid=1"
					+ " AND"
					+ " c.member.id=?"
					+ " AND"
					+ " r.periodCode=?"
					+ " GROUP BY f"
					+ " ");
			hql.append(" ORDER BY v.recommendTime DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(CommonConstantsWeb.WEB_RECOMMENDED_MODULE_TYPE_FUND);
			params.add(memberId);
			params.add(CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE + "1M");
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objs != null && !objs.isEmpty()){
				funds = new ArrayList<FundInfoDataVO>();
				for (Object[] objects : objs) {
					FundInfoDataVO vo = new FundInfoDataVO();
					FundInfo fundInfo = (FundInfo) objects[0];
					Double increase = (Double) objects[1];
					vo.setFundInfo(fundInfo);
					vo.setFundInfoSc((FundInfoSc) this.baseDao.get(FundInfoSc.class, fundInfo.getId()));
					vo.setFundInfoTc((FundInfoTc) this.baseDao.get(FundInfoTc.class, fundInfo.getId()));
					vo.setFundInfoEn((FundInfoEn) this.baseDao.get(FundInfoEn.class, fundInfo.getId()));
					vo.setDefaultInfoByLang(langCode);
					vo.setFundReturnOneMonth(increase);
					String region = vo.getGeoAllocationCode();
					if (StringUtils.isNotBlank(region)) {
						vo.setGeoAllocation(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, region, langCode));
					}
					String sector = vo.getSectorTypeCode();
					if (StringUtils.isNotBlank(sector)) {
						vo.setSectorType(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, sector, langCode));
					}
					funds.add(vo);
				}
			}
		}
		return funds;
	}

	/**
	 * 获取fund浏览记录
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<FundInfoDataVO> getInvestorVisitFunds(String memberId, String langCode) {
		List<FundInfoDataVO> funds = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " f,r.increase"
					+ " FROM"
					+ " WebInvestorVisit v"
					
					+ " LEFT JOIN"
					+ " FundInfo f"
					+ " ON"
					+ " v.relateId=f.id"
					
					+ " LEFT JOIN"
					+ " FundReturn r"        
					+ " ON"
					+ " r.fund.id=f.id"
					
					+ " WHERE"
					+ " v.moduleType=?"
					+ " AND"
					+ " v.member.id=?"
					+ " AND"
					+ " r.periodCode=?"
					+ " GROUP BY"
					+ " f"
					+ " ORDER BY"
					+ " v.vistiTime"
					+ " DESC"
					+ " ");
			List<Object> params = new ArrayList<Object>();
			params.add(CommonConstantsWeb.WEB_VISIT_FUND);
			params.add(memberId);
			params.add(CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE + "1M");
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(objs != null && !objs.isEmpty()){
				funds = new ArrayList<FundInfoDataVO>();
				for (Object[] objects : objs) {
					FundInfo fundInfo = (FundInfo) objects[0];
					Double increase = (Double) objects[1];
					FundInfoDataVO vo = new FundInfoDataVO();
					vo.setFundInfoSc((FundInfoSc) this.baseDao.get(FundInfoSc.class, fundInfo.getId()));
					vo.setFundInfoTc((FundInfoTc) this.baseDao.get(FundInfoTc.class, fundInfo.getId()));
					vo.setFundInfoEn((FundInfoEn) this.baseDao.get(FundInfoEn.class, fundInfo.getId()));
					vo.setFundInfo(fundInfo);
					vo.setDefaultInfoByLang(langCode);
					vo.setFundReturnOneMonth(increase);
					String region = vo.getGeoAllocationCode();
					if (StringUtils.isNotBlank(region)) {
						vo.setGeoAllocation(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_GEOGRAPHICAL, region, langCode));
					}
					String sector = vo.getSectorTypeCode();
					if (StringUtils.isNotBlank(sector)) {
						vo.setSectorType(this.getParamConfig(CommonConstantsWeb.SYS_PARM_TYPE_SECTOR, sector, langCode));
					}
					funds.add(vo);
				}
			}
		}
		return funds;
	}
	
	/**
	 * 获取持仓基金公告
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @return
	 */
	@Override
	public List<FundAnnoVO> getHoldFundNotice(MemberBase loginUser, String langCode, Integer maxResults) {
		List<FundAnnoVO> vos = null;
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " n"
				+ " FROM"
				+ " FundAnno n"
				
				+ " LEFT JOIN"
				+ " FundInfo f"
				+ " ON"
				+ " f.id=n.fund.id"
				
				+ " LEFT JOIN"
				+ " PortfolioHoldProduct p"
				+ " ON"
				+ " f.product.id=p.product.id"
				
				+ " LEFT JOIN"
				+ " PortfolioHold h"
				+ " ON"
				+ " h.id=p.portfolioHold.id"
				
				+ " WHERE"
				+ " h.member.id=?"
				+ " AND"
				+ " n.isValid=1"
				+ " AND"
				+ " n.langCode=?"
				+ " ORDER BY"
				+ " n.lastUpdate"
				+ " DESC");
		List<Object> params = new ArrayList<Object>();
		params.add(loginUser.getId());
		params.add(langCode);
		List<FundAnno> fundAnnos = this.baseDao.find(hql.toString(), params.toArray(), true, maxResults);
		if(fundAnnos != null && !fundAnnos.isEmpty()){
			vos = new ArrayList<FundAnnoVO>();
			for (FundAnno fundAnno : fundAnnos) {
				FundAnnoVO vo = new FundAnnoVO();
				BeanUtils.copyProperties(fundAnno, vo);
				vo.setAnnoContent(UriEncoder.encode(new String(fundAnno.getAnnoContent())));
				String dateFormat = loginUser.getDateFormat();
				if (StringUtils.isBlank(dateFormat)) {
					dateFormat = CommonConstants.FORMAT_DATE;
				}
				if(vo.getAnnoDate() != null){
					vo.setAnnoDateFormat(DateUtil.dateToDateString(vo.getAnnoDate(), dateFormat));
				}
				vos.add(vo);
			}
		}
		return vos;
	}

	/**
	 * 获取指定时间内，指定好友类型发布的insight
	 * @author wwluo
	 * @date 2017-03-28
	 * @param type 关系类型：Buddy Client Advisor Prospect
	 * @param 时间类型 天：D，周：W，月：M，年：Y （默认D）
	 * @param period （默认7）
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public List<FrontCommunityTopicVO> getRecommendedInsights(MemberBase loginUser, String type,
			String periodType, Integer period, String memberId, String langCode, Integer maxResults) {
		String dateFormat = loginUser.getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		List<FrontCommunityTopicVO> vos = null;
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " c,a.content"
				+ " FROM"
				+ " CommunityTopic c"
				+ " LEFT JOIN"
				+ " CommunityContent a"
				+ " ON"
				+ " a.id=c.id"
				+ "");
		if (CommonConstantsWeb.WEB_FRIEND_ADVISOR.equals(type)) {
			hql.append(" LEFT JOIN"
					+ " MemberIfa i"
					+ " ON"
					+ " i.member.id=c.creator.id"
					+ " LEFT JOIN"
					+ " CrmCustomer b"
					+ " ON"
					+ " b.ifa.id=i.id"
					+ " WHERE"
					+ " b.member.id=?"
					+ " AND"
					+ " b.isValid=1"
					+ " AND"
					+ " c.status=1"
					+ " AND"
					+ " c.isInsight=1"
					+ " AND "
					+ " (c.visitor='all'"
					+ " OR"
					+ " c.visitor='client')"
					+ "");
			if (StringUtils.isNotBlank(memberId)) {
				params.add(memberId);
			}else{
				return null;
			}
		}else if (CommonConstantsWeb.WEB_FRIEND_BUDDY.equals(type)) {
			hql.append(" LEFT JOIN"
					+ " WebFriend i"
					+ " ON"
					+ " i.toMember.id=c.creator.id"
					+ " WHERE"
					+ " i.fromMember.id=?"
					+ " AND"
					+ " i.checkResult=1"
					+ " AND"
					+ " i.relationships='Buddy'"
					+ " AND"
					+ " c.status=1"
					+ " AND"
					+ " c.isInsight=1"
					+ " AND "
					+ " (c.visitor='all'"
					+ " OR"
					+ " c.visitor='friend')"
					+ "");
			if (StringUtils.isNotBlank(memberId)) {
				params.add(memberId);
			}else{
				return null;
			}
		}else{
			hql.append(""
					+ " WHERE"
					+ " c.status=1"
					+ " AND"
					+ " c.isInsight=1"
					+ " AND "
					+ " (c.visitor='all'"
					+ " OR"
					+ " c.visitor='client')"
					+ "");
		}
		Calendar calendar = Calendar.getInstance();
		if (StringUtils.isNotBlank(periodType) && period != null) {
			if("D".equals(periodType)){
				calendar.add(Calendar.DATE, -period);
			}else if("W".equals(periodType)){
				calendar.add(Calendar.WEEK_OF_YEAR, -period);
			}else if("M".equals(periodType)){
				calendar.add(Calendar.MONTH, -period);
			}else if("Y".equals(periodType)){
				calendar.add(Calendar.YEAR, -period);
			}
		}else{
			calendar.add(Calendar.DATE, -7);
		}
		List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false, maxResults);
		if(objs != null && !objs.isEmpty()){
			vos = new ArrayList<FrontCommunityTopicVO>();
			for (Object[] objects : objs) {
				FrontCommunityTopicVO vo = new FrontCommunityTopicVO();
				CommunityTopic communityTopic = (CommunityTopic) objects[0];
				String content = (String) objects[1];
				BeanUtils.copyProperties(communityTopic, vo);
				if(communityTopic.getLikeCount() != null){
					vo.setLikeCount(communityTopic.getLikeCount().toString());
				}else{
					vo.setLikeCount("0");
				}
				if(communityTopic.getUnlikeCount() != null){
					vo.setUnlikeCount(communityTopic.getUnlikeCount().toString());
				}else{
					vo.setUnlikeCount("0");
				}
				if(communityTopic.getCommentCount() != null){
					vo.setCommentCount(communityTopic.getCommentCount().toString());
				}else{
					vo.setCommentCount("0");
				}
				if(communityTopic.getReadCount() != null){
					vo.setReadCount(communityTopic.getReadCount().toString());
				}else{
					vo.setReadCount("0");
				}
				if(communityTopic.getCreateTime() != null){
					vo.setPublishTimeFormat(DateUtil.dateToDateString(communityTopic.getCreateTime(), dateFormat));
				}
				if(communityTopic.getCreator() != null){
					MemberBase creator = communityTopic.getCreator();
					if(creator != null){
						vo.setMemberId(creator.getId());
						if(creator.getMemberType() != null){
							vo.setMemberType(creator.getMemberType().toString());
						}
						vo.setUserHeadUrl(PageHelper.getUserHeadUrl(creator.getIconUrl(), null));
						vo.setNickName(getCommonMemberName(creator.getId(), langCode, "2"));
					}else{
						vo.setUserHeadUrl(PageHelper.getUserHeadUrl(null, null));
					}
				}
				vo.setContent(content);
				vos.add(vo);
			}
		}
		return vos;
	}

	/**
	 * 获取Hot Topic
	 * @author wwluo
	 * @date 2017-03-28
	 * @param rule
	 * @param maxResults 获取数量
	 * @return
	 */
	@Override
	public List<FrontCommunityTopicVO> getHotTopics(MemberBase loginUser, String ruleKey,
			Integer maxResults, String langCode) {
		String dateFormat = loginUser.getDateFormat();
		if (StringUtils.isBlank(dateFormat)) {
			dateFormat = CommonConstants.FORMAT_DATE_TIME;
		}
		List<FrontCommunityTopicVO> vos = null;
		List<Object> params = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " c,a.content"
				+ " FROM"
				+ " CommunityTopic c"
				+ " LEFT JOIN"
				+ " CommunityContent a"
				+ " ON"
				+ " a.id=c.id"
				+ " WHERE"
				+ " c.status=1"
				+ " AND"
				+ " (c.visitor='all'"
				+ " OR"
				+ " c.visitor='client')");
		if (StringUtils.isNotBlank(ruleKey)) {
			CommunityRule rule = getCommunityRule(ruleKey);
			if(rule != null && StringUtils.isNotBlank(rule.getValue())){
				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -Integer.valueOf(rule.getValue()));
				hql.append(" AND c.createTime BETWEEN ? AND CURDATE()");
				params.add(calendar.getTime());
			}
		}
		hql.append(" ORDER BY c.commentCount DESC");
		List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false, maxResults);
		if(objs != null && !objs.isEmpty()){
			vos = new ArrayList<FrontCommunityTopicVO>();
			for (Object[] objects : objs) {
				FrontCommunityTopicVO vo = new FrontCommunityTopicVO();
				CommunityTopic communityTopic = (CommunityTopic) objects[0];
				String content = (String) objects[1];
				BeanUtils.copyProperties(communityTopic, vo);
				if(communityTopic.getLikeCount() != null){
					vo.setLikeCount(communityTopic.getLikeCount().toString());
				}else{
					vo.setLikeCount("0");
				}
				if(communityTopic.getUnlikeCount() != null){
					vo.setUnlikeCount(communityTopic.getUnlikeCount().toString());
				}else{
					vo.setUnlikeCount("0");
				}
				if(communityTopic.getCommentCount() != null){
					vo.setCommentCount(communityTopic.getCommentCount().toString());
				}else{
					vo.setCommentCount("0");
				}
				if(communityTopic.getReadCount() != null){
					vo.setReadCount(communityTopic.getReadCount().toString());
				}else{
					vo.setReadCount("0");
				}
				if(communityTopic.getCreateTime() != null){
					vo.setPublishTimeFormat(DateUtil.dateToDateString(communityTopic.getCreateTime(), dateFormat));
				}
				if(communityTopic.getCreator() != null){
					MemberBase creator = communityTopic.getCreator();
					if(creator != null){
						vo.setMemberId(creator.getId());
						if(creator.getMemberType() != null){
							vo.setMemberType(creator.getMemberType().toString());
						}
						vo.setUserHeadUrl(PageHelper.getUserHeadUrl(creator.getIconUrl(), null));
						vo.setNickName(getCommonMemberName(creator.getId(), langCode, "2"));
					}else{
						vo.setUserHeadUrl(PageHelper.getUserHeadUrl(null, null));
					}
				}
				vo.setContent(content);
				vos.add(vo);
			}
		}
		return vos;
	}
	
	/**
	 * 帖子规则获取
	 * @author wwluo
	 * @date 2017-03-28
	 * @param key
	 * @return
	 */
	private CommunityRule getCommunityRule(String key){
		CommunityRule rule = null;
		if (StringUtils.isNotBlank(key)) {
			StringBuffer hql = new StringBuffer(""
					+ " FROM"
					+ " CommunityRule r"
					+ " WHERE"
					+ " r.key=?"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(key);
			List<CommunityRule> rules = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(rules != null && !rules.isEmpty()){
				rule = rules.get(0);
			}
		}
		return rule;
	}

	/**
	 * 获取投资者账单信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param type 报告类型,w周报,m月报,q季报,h半年报,y年报
	 * @param maxResults 获取数量
	 * @return
	 */
	@Override
	public List<InvestorReport> getInvestorReports(String memberId, String type,
			Integer maxResults) {
		List<InvestorReport> reports = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " FROM"
					+ " InvestorReport i"
					+ " WHERE"
					+ " i.member.id=?"
					+ " ");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			if (StringUtils.isNotBlank(type)) {
				hql.append(" AND i.type=?");
				params.add(type);
			}
			hql.append(" ORDER BY i.lastUpdate DESC");
			reports = this.baseDao.find(hql.toString(), params.toArray(), false, maxResults);
		}
		return reports;
	}

	/**
	 * 获取我的资产信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param currencyCode
	 * @return
	 */
	@Override
	public MyAsset getMyAsset(String memberId, String currencyCode) {
		MyAsset myAsset = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(" FROM MyAsset a WHERE a.member.id=?");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			List<MyAsset> myAssets = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(myAssets != null && !myAssets.isEmpty()){
				myAsset = myAssets.get(0);
				if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(myAsset.getCurrencyType()) && !currencyCode.equals(myAsset.getCurrencyType())) {
					Double exchangeRate = this.getExchangeRate(myAsset.getCurrencyType(), currencyCode);
					if(exchangeRate != null){
						if(myAsset.getDayPl() != null){
							myAsset.setDayPl(myAsset.getDayPl() * exchangeRate);
						}
						if(myAsset.getTotalAsset() != null){
							myAsset.setTotalAsset(myAsset.getTotalAsset() * exchangeRate);
						}
						if(myAsset.getTotalCash() != null){
							myAsset.setTotalCash(myAsset.getTotalCash() * exchangeRate);
						}
						if(myAsset.getTotalMarket() != null){
							myAsset.setTotalMarket(myAsset.getTotalMarket() * exchangeRate);
						}
						if(myAsset.getTotalPl() != null){
							myAsset.setTotalPl(myAsset.getTotalPl() * exchangeRate);
						}
					}
				}
			}
		}
		return myAsset;
	}
	
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
	@Override
	public List<PortfolioAssetsVO> getMyPortfolioTotalAssets(String memberId,
			String currencyCode, String periodType, String period) {
		List<PortfolioAssetsVO> vos = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " a"
					+ " FROM"
					+ " PortfolioHoldAccount a"
					+ " WHERE"
					+ " a.member.id=?"
					+ " AND"
					+ " a.isValid=1"
					+ " AND"
					+ " a.portfolioHold"
					+ " IS NOT NULL"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			List<PortfolioHoldAccount> portfolioHoldAccounts = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(portfolioHoldAccounts != null && !portfolioHoldAccounts.isEmpty()){
				vos = new ArrayList<PortfolioAssetsVO>();
				for (PortfolioHoldAccount portfolioHoldAccount : portfolioHoldAccounts) {
					PortfolioAssetsVO vo = new PortfolioAssetsVO();
					vo.setHoldId(portfolioHoldAccount.getPortfolioHold().getId());
					vo.setPortfolioName(StringEscapeUtils.unescapeHtml(portfolioHoldAccount.getPortfolioHold().getPortfolioName()));
					Double exchangeRate = null;
					if (StringUtils.isNotBlank(currencyCode)) {
						exchangeRate = this.getExchangeRate(portfolioHoldAccount.getPortfolioHold().getBaseCurrency(), currencyCode);
					}
					if(exchangeRate == null){
						exchangeRate = 1.00;
					}
					vo.setAccReturn(portfolioHoldAccount.getPortfolioHold().getTotalReturnRate());
					if(portfolioHoldAccount.getPortfolioHold().getTotalAsset() != null){
						vo.setTotalAsset(portfolioHoldAccount.getPortfolioHold().getTotalAsset() * exchangeRate);
					}
					if(portfolioHoldAccount.getPortfolioHold().getTotalCash() != null){
						vo.setTotalCash(portfolioHoldAccount.getPortfolioHold().getTotalCash() * exchangeRate);
					}
					List<Object> cumperfParams = new ArrayList<Object>();
					StringBuffer cumperfHql = new StringBuffer(""
							+ " FROM"
							+ " PortfolioHoldCumperf p"
							+ " WHERE"
							+ " p.portfolioHold.id=?"
							+ "");
					cumperfParams.add(portfolioHoldAccount.getPortfolioHold().getId());
					if (StringUtils.isNotBlank(periodType) && StringUtils.isNotBlank(period)) {
						Calendar calendar = Calendar.getInstance();
						if("M".equals(periodType)){
							calendar.add(Calendar.MONTH, -Integer.valueOf(period));
						}else if("Y".equals(periodType)){
							calendar.add(Calendar.YEAR, -Integer.valueOf(period));
						}else if("YTD".equals(periodType)){
							calendar.set(calendar.get(Calendar.YEAR), 0, 2); 
						}
						cumperfHql.append(" AND p.valuationDate BETWEEN ? AND CURDATE()");
						cumperfParams.add(calendar.getTime());
					}else{
					// 默认YTD
						Calendar calendar = Calendar.getInstance();
						calendar.set(calendar.get(Calendar.YEAR), 0, 2); 
						cumperfHql.append(" AND p.valuationDate BETWEEN ? AND CURDATE()");
						cumperfParams.add(calendar.getTime());
					}
					cumperfHql.append(" ORDER BY p.valuationDate ASC");
					List<PortfolioHoldCumperf> cumperfs = this.baseDao.find(cumperfHql.toString(), cumperfParams.toArray(), false);
					vo.setHoldCumperfs(cumperfs);
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * 计算全部持仓组合资产信息
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param currencyCode
	 * @return
	 */
	@Override
	public InvestorMyPortfolios getAllPortfolioAssets(String memberId,
			String currencyCode) {
		InvestorMyPortfolios portfolios = new InvestorMyPortfolios();
		if (StringUtils.isNotBlank(memberId)) {
			/*StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " a"
					+ " FROM"
					+ " PortfolioHoldAccount a"
					
					+ " LEFT JOIN"
					+ " PortfolioHold h"
					+ " ON"
					+ " a.portfolioHold.id=h.id"
					
					+ " LEFT JOIN"
					+ " PortfolioHold h"
					+ " ON"
					+ " a.portfolioHold.id=h.id"
					
					+ " WHERE"
					+ " a.isValid=1"
					+ " AND"
					+ " h.member.id=?"
					+ " AND"
					+ " a.portfolioHold"
					+ " IS NOT NULL"
					+ "");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			List<PortfolioHoldAccount> holdAccounts = this.baseDao.find(hql.toString(), params.toArray(), true);
			if(holdAccounts != null && !holdAccounts.isEmpty()){
				Double totalAsset = 0.00;
				Double totalCash = 0.00;
				Double accReturn = 0.00;
				Double totalReturn=0.00;
				List<String> portfolioIds=new ArrayList<String>();
				for (PortfolioHoldAccount portfolioHoldAccount : holdAccounts) {
					Double excahngeRate = null;
					if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(portfolioHoldAccount.getBaseCurrency())) {
						excahngeRate = this.getExchangeRate(portfolioHoldAccount.getBaseCurrency(), currencyCode);
					}
					if(excahngeRate == null){
						excahngeRate = 1.00;
					}
					if(portfolioHoldAccount.getTotalAsset() != null){
						totalAsset += portfolioHoldAccount.getTotalAsset() * excahngeRate;
					}
					if(portfolioHoldAccount.getTotalCash() != null){
						totalCash += portfolioHoldAccount.getTotalCash() * excahngeRate;
					}
					
					Double cumperfRate = getHoldCumperfRate(portfolioHoldAccount.getPortfolioHold().getId());
					if(cumperfRate != null){
						accReturn += cumperfRate;
					}
					if(!portfolioIds.contains(portfolioHoldAccount.getPortfolioHold().getId())){
						if(null!=portfolioHoldAccount.getPortfolioHold().getTotalReturnValue()){
							totalReturn+=getNumByCurrency(portfolioHoldAccount.getPortfolioHold().getTotalReturnValue(), portfolioHoldAccount.getPortfolioHold().getBaseCurrency(), currencyCode);
						}
						if(null!=portfolioHoldAccount.getPortfolioHold().getTotalReturnRate()){
							accReturn+=portfolioHoldAccount.getPortfolioHold().getTotalReturnRate();
						}
						portfolioIds.add(portfolioHoldAccount.getPortfolioHold().getId());
					}
				}
				portfolios.setTotalAsset(totalAsset);
				portfolios.setTotalCash(totalCash);
				portfolios.setAccReturn(accReturn);
				portfolios.setTotalReturn(totalReturn);
			}*/
			
			StringBuilder hql=new StringBuilder();
			hql.append(" from MyAsset r where r.member.id=?");
			List<Object> params=new ArrayList<Object>();
			params.add(memberId);
			List list=baseDao.find(hql.toString(), params.toArray(), false);
			if(null!=list && !list.isEmpty()){
				MyAsset myAsset=(MyAsset)list.get(0);
				Double totalAsset = myAsset.getTotalAsset();
				Double totalCash = myAsset.getTotalCash();
				Double accReturn = myAsset.getAccReturn();
				Double totalReturn=myAsset.getTotalPl();
				/*double excahngeRate = 1;
				if (StringUtils.isNotBlank(currencyCode) && StringUtils.isNotBlank(myAsset.getCurrencyType())) {
					excahngeRate = this.getExchangeRate(myAsset.getCurrencyType(), currencyCode);
				}*/
				if(null!=accReturn){
					portfolios.setAccReturn(accReturn*100);
				}
				
				if(null!=totalAsset){
					portfolios.setTotalAsset(getNumByCurrency(totalAsset, myAsset.getCurrencyType(), currencyCode));
				}
				if(null!=totalCash){
					portfolios.setTotalCash(getNumByCurrency(totalCash, myAsset.getCurrencyType(), currencyCode));
				}
				if(null!=totalReturn){
					portfolios.setTotalReturn(getNumByCurrency(totalReturn, myAsset.getCurrencyType(), currencyCode));
				}
			}
		}
		return portfolios;
	}
	
	/**
	 * 获取持仓组合最新累计收益率
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param currencyCode
	 * @return
	 */
	private Double getHoldCumperfRate(String holdId){
		Double cumperfRate = null;
		if (StringUtils.isNotBlank(holdId)) {
			StringBuffer hql = new StringBuffer(" FROM PortfolioHoldCumperf c WHERE c.portfolioHold.id=? ORDER BY c.valuationDate DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(holdId);
			List<PortfolioHoldCumperf> cumperfs = this.baseDao.find(hql.toString(), params.toArray(), true, 1);
			if(cumperfs != null && !cumperfs.isEmpty()){
				PortfolioHoldCumperf cumperf = cumperfs.get(0);
				cumperfRate = cumperf.getCumulativeRate();
			}
		}
		return cumperfRate;
	}

	/**
	 * My Favourites News
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param maxResults
	 * @return
	 */
	@Override
	public List<NewsInfoVO> getMyFavouritesNews(String memberId, String langCode, Integer maxResults) {
		List<NewsInfoVO> vos = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " i.id,i.title,i.pubDate,t.typeName,f.id" 
					//+ this.getLangString("t.name", langCode)
					+ " FROM"
					+ " WebFollow f"
					
					+ " LEFT JOIN"
					+ " NewsInfo i"
					+ " ON"
					+ " i.id=f.relateId"
					
					+ " LEFT JOIN"
					+ " NewsXfaType t"
					+ " ON"
					+ " i.xfaTypeId=t.id"
					
					+ " WHERE"
					+ " f.isValid=1"
					+ " AND"
					+ " f.moduleType=?"
					+ " AND"
					+ " f.member.id=?"
					+ " ORDER BY"
					+ " f.createTime"
					+ " DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(CommonConstantsWeb.WEB_FOLLOW_TYPE_NEWS);
			params.add(memberId);
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), true, maxResults);
			if(objs != null && !objs.isEmpty()){
				vos = new ArrayList<NewsInfoVO>();
				for (Object[] objects : objs) {
					NewsInfoVO vo = new NewsInfoVO();
					vo.setId((String) objects[0]);
					vo.setTitle((String) objects[1]);
					vo.setPubDate((Date) objects[2]);
					vo.setSectionType((String) objects[3]);
					vo.setFavoritesId((String) objects[4]);
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * My Favourites Topics
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param maxResults
	 * @return
	 */
	@Override
	public List<TopicDetailVO> getMyFavouritesTopics(String memberId, String langCode,
			Integer maxResults) {
		List<TopicDetailVO> vos = null;
		if (StringUtils.isNotBlank(memberId)) {
			StringBuffer hql = new StringBuffer(""
					+ " SELECT"
					+ " i.id,i.title,i.createTime," + this.getLangString("t.sectionName", langCode) + ",f.id"
					+ " FROM"
					+ " WebFollow f"
					
					+ " LEFT JOIN"
					+ " CommunityTopic i"
					+ " ON"
					+ " i.id=f.relateId"
					+ " AND"
					+ " i.status=1"
					
					+ " LEFT JOIN"
					+ " CommunitySection t"
					+ " ON"
					+ " i.section.id=t.id"
					
					+ " WHERE"
					+ " f.isValid=1"
					+ " AND"
					+ " f.moduleType=?"
					+ " AND"
					+ " f.member.id=?"
					+ " ORDER BY"
					+ " f.createTime"
					+ " DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(CommonConstantsWeb.WEB_FOLLOW_TYPE_TOPIC);
			params.add(memberId);
			List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), true, maxResults);
			if(objs != null && !objs.isEmpty()){
				vos = new ArrayList<TopicDetailVO>();
				for (Object[] objects : objs) {
					TopicDetailVO vo = new TopicDetailVO();
					vo.setId((String) objects[0]);
					vo.setTitle((String) objects[1]);
					Date createDate = (Date) objects[2];
					if(createDate != null){
						vo.setCreateDate(DateUtil.dateToDateString(createDate, CommonConstants.FORMAT_DATE_TIME));
					}
					vo.setSection((String) objects[3]);
					vo.setFavoritesId((String) objects[4]);
					vos.add(vo);
				}
			}
		}
		return vos;
	}

	/**
	 * 删除我的收藏信息（逻辑删除）
	 * @author wwluo
	 * @date 2017-03-28
	 * @param memberId
	 * @param maxResults
	 * @return
	 */
	@Override
	public Boolean deleteMyWebFollow(String followId) {
		Boolean flag = false;
		if (StringUtils.isNotBlank(followId)) {
			WebFollow follow = (WebFollow) this.baseDao.get(WebFollow.class, followId);
			if(follow != null){
				follow.setIsValid("0");
				this.baseDao.saveOrUpdate(follow);
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 获取 Statement
	 * @author wwluo
	 * @data 2017-03-16
	 * @param jsonPaging
	 * @param memberId
	 * @param type 报告类型,w周报,m月报,q季报,h半年报,y年报
	 * @return
	 */	
	@Override
	public JsonPaging getInvestorReports(JsonPaging jsonPaging,
			String memberId, String type) {
		if (StringUtils.isNotBlank(memberId)) {
			List<InvestorReport> reports = null;
			StringBuffer hql = new StringBuffer(""
					+ " FROM"
					+ " InvestorReport i"
					+ " WHERE"
					+ " i.member.id=?"
					+ " ");
			List<Object> params = new ArrayList<Object>();
			params.add(memberId);
			if (StringUtils.isNotBlank(type)) {
				hql.append(" AND i.type=?");
				params.add(type);
			}
			hql.append(" ORDER BY i.lastUpdate DESC");
			jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, true);
		}
		return jsonPaging;
	}

	/**
	 * 获取 推荐的基金公告
	 * @author wwluo
	 * @data 2017-03-16
	 * @param memberId
	 * @param langCode
	 * @param maxResults
	 * @return
	 */	
	@Override
	public List<FundAnnoVO> getRecommendedFundAnnos(MemberBase loginUser,
			String langCode, Integer maxResults) {
		List<FundAnnoVO> vos = null;
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " n"
				+ " FROM"
				+ " FundAnno n"
				
				+ " LEFT JOIN"
				+ " WebRecommended f"
				+ " ON"
				+ " f.relateId=n.fund.id"
				
				+ " LEFT JOIN"
				+ " MemberIfa i"
				+ " ON"
				+ " i.member.id=f.creator.id"
				
				+ " LEFT JOIN"
				+ " CrmCustomer p"
				+ " ON"
				+ " p.ifa.id=i.id"
				+ " AND"
				+ " p.isValid=1"
				
				+ " WHERE"
				+ " p.member.id=?"
				+ " AND"
				+ " n.isValid=1"
				+ " AND"
				+ " n.langCode=?"
				+ " ORDER BY"
				+ " n.lastUpdate"
				+ " DESC");
		List<Object> params = new ArrayList<Object>();
		params.add(loginUser.getId());
		params.add(langCode);
		List<FundAnno> fundAnnos = this.baseDao.find(hql.toString(), params.toArray(), true, maxResults);
		if(fundAnnos != null && !fundAnnos.isEmpty()){
			vos = new ArrayList<FundAnnoVO>();
			for (FundAnno fundAnno : fundAnnos) {
				FundAnnoVO vo = new FundAnnoVO();
				BeanUtils.copyProperties(fundAnno, vo);
				vo.setAnnoContent(UriEncoder.encode(new String(fundAnno.getAnnoContent())));
				String dateFormat = loginUser.getDateFormat();
				if (StringUtils.isBlank(dateFormat)) {
					dateFormat = CommonConstants.FORMAT_DATE;
				}
				if(vo.getAnnoDate() != null){
					vo.setAnnoDateFormat(DateUtil.dateToDateString(vo.getAnnoDate(), dateFormat));
				}
				vos.add(vo);
			}
		}
		return vos;
	}

	/**
	 * 获取新闻
	 * @author wwluo
	 * @data 2017-03-16
	 * @param maxResults
	 * @return
	 */	
	@Override
	public List<CommunityNewsListVO> getNews(Integer maxResults) {
		List<CommunityNewsListVO> vos = null;
		StringBuffer hql = new StringBuffer(""
				+ " SELECT"
				+ " n,m.content"
				+ " FROM"
				+ " NewsInfo n"
				+ " LEFT JOIN"
				+ " CommunityTopic c"
				+ " ON"
				+ " n.id=c.sourceId"
				+ " AND"
				+ " c.sourceType='news'"
				+ " AND"
				+ " c.status=1"
				+ " LEFT JOIN"
				+ " CommunityContent m"
				+ " ON"
				+ " m.id=c.id"
				+ " ORDER BY"
				+ " n.pubDate"
				+ " DESC");
		List<Object> params = new ArrayList<Object>();
		List<Object[]> objects = this.baseDao.find(hql.toString(), null, true, maxResults);
		if(objects != null && !objects.isEmpty()){
			vos = new ArrayList<CommunityNewsListVO>();
			for (Object[] obj : objects) {
				CommunityNewsListVO vo = new CommunityNewsListVO();
				NewsInfo info = (NewsInfo) obj[0];
				String content = (String) obj[1];
				vo.setContent(content);
				vo.setNewsId(info.getId());
				vo.setNewsUrl(info.getLitPic());
				vo.setTitle(info.getTitle());
				vo.setDescription(info.getDescription());
				if(info.getPubDate() != null){
					vo.setDateTime(DateUtil.dateToDateString(info.getPubDate(), CommonConstants.FORMAT_DATE_TIME));
				}
				vos.add(vo);
			}
		}
		return vos;
	}

	/**
	 * 获取账户的资产列表
	 * @author mqzou  2017-04-25
	 * @param accountId
	 * @return
	 */
	@Override
	public List<InvestorAccountCurrency> findAccountCurrency(String accountId) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from InvestorAccountCurrency r where r.account.id=?");
		List<Object> params=new ArrayList<Object>();
		params.add(accountId);
		List list=baseDao.find(hql.toString(), params.toArray(), false);
		
		return list;
	}

	/**
	 * 删除投资帐号
	 * @author mqzou 2017-05-16
	 * @param account
	 */
	@Override
	public void deleteAccount(InvestorAccount account) {
		if(null!=account){
			account.setIsValid("0");
			baseDao.saveOrUpdate(account);
		}
	}
	
	/**
	 * 获取账户详情
	 * @author mqzou 2017-05-16
	 * @param accountId
	 * @param langCode
	 * @return
	 */
	@Override
	public InvestorAccountVO findAccountDetail(String accountId, String langCode,String currency) {
        InvestorAccount account = findInvestorAccountById(accountId);
		if (null != account) {
			if (null == currency || "".equals(currency))
				currency = account.getBaseCurrency();
			InvestorAccountVO vo = new InvestorAccountVO();
			vo.setIfa(account.getIfa());
			vo.setAccountId(account.getId());
			vo.setAccountNo(account.getAccountNo());
			vo.setAccType(account.getAccType());
			vo.setAuthorized(account.getAuthorized());
			vo.setBaseCurrency(account.getBaseCurrency());
			vo.setSourceFrom(account.getSourceFrom());
			String currencyName=sysParamService.findNameByCode(account.getBaseCurrency(), langCode);
			vo.setCurrency(currencyName);
			if (null!=account.getMember())
				vo.setIconUrl(account.getMember().getIconUrl());
			vo.setCies(account.getCies());
			
			if (null!=account.getDistributor()){
				vo.setDistributorId(account.getDistributor().getId());
				vo.setDistributorName(account.getDistributor().getCompanyName());
				vo.setDistributorIconUrl(PageHelper.getLogoUrl(account.getDistributor().getLogofile(), "D"));
			}
			vo.setFaca(account.getFaca());
			
			if (null!=account.getMember()){
				vo.setLoginCode(account.getMember().getLoginCode());
				String nickName=getCommonMemberName(account.getMember().getId(), langCode, "2");
				vo.setNickName(nickName);
				MemberIndividual individual = memberBaseService.findIndividualMember(account.getMember().getId());
				if (null != individual) {
					vo.setGender(individual.getGender());
					vo.setFirstName(individual.getFirstName());
					vo.setLastName(individual.getLastName());
				}
			}
			
			vo.setId(account.getId());

			InvestorAccountCurrencyVO currencyVO = findAccountCurrency(vo.getAccountNo(), currency);
			if (null != currency) {
				vo.setTotalValue(String.valueOf(currencyVO.getTotalAssets()));
				vo.setCashAvailable(String.valueOf(currencyVO.getCashAvailable()));
				vo.setCashForPendingTran(String.valueOf(currencyVO.getCashHold()));
				vo.setCashValue(String.valueOf(currencyVO.getCashValue()));
				vo.setMarketValue(currencyVO.getMarketValue());
				vo.setCashWithdrawal(String.valueOf(currencyVO.getCashWithdrawal()));
				vo.setCashRatio(StrUtils.getNumberString(currencyVO.getCashValue() / currencyVO.getTotalAssets() * 100));

				vo.setMarketRatio(StrUtils.getNumberString(currencyVO.getMarketValue() / currencyVO.getTotalAssets() * 100));
			} else {
				vo.setCashRatio("0");
				vo.setMarketRatio("0");
			}
			vo.setSubFlag(account.getSubFlag());
			vo.setMemberId(account.getMember().getId());
			return vo;
		}
		return null;
	}

	/**
	 * 获取投资人的资产收益图表数据
	 * @author mqzou  2017-06-06
	 * @param memberId
	 * @param beginDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<MyAssetChartDataVO> findAssetCumperfsData(String memberId, String beginDate, String endDate,String dateFormat,String currency) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from MyAssetHis r where r.member.id=? and r.valuationDate between ? and ?");
		hql.append(" order by r.valuationDate asc");
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		params.add(DateUtil.StringToDate(beginDate, DateUtil.DEFAULT_DATE_FORMAT));
		params.add(DateUtil.StringToDate(endDate, DateUtil.DEFAULT_DATE_FORMAT));
		List resultList=baseDao.find(hql.toString(), params.toArray(), false);
		List<MyAssetChartDataVO> list=new ArrayList<MyAssetChartDataVO>();
		if(null!=resultList && !resultList.isEmpty()){
			double baseValue=0;
			for (int i = 0; i < resultList.size(); i++) {
				MyAssetHis his=(MyAssetHis)resultList.get(i);
				MyAssetChartDataVO vo=new MyAssetChartDataVO();
				vo.setDate(DateUtil.dateToDateString(his.getValuationDate(), dateFormat));
				if(i==0){
					baseValue=null!=his.getAccReturn()?his.getAccReturn():0;
					vo.setAccReturn(0);
				}else {
					double accReturn=null!=his.getAccReturn()?his.getAccReturn():0;
					vo.setAccReturn(accReturn-baseValue);
				}
				double dayPl=null!=his.getDayPl()?his.getDayPl():0;
				if(null!=his.getCurrencyType() && !"".equals(his.getCurrencyType())){
					vo.setDatePl(getNumByCurrency(dayPl, his.getCurrencyType(), currency));
				}else {
					vo.setDatePl(0);
				}
				
				list.add(vo);
			}
			
		}
		return list;
	}


	
	
	
	
	
	
	
	
}
