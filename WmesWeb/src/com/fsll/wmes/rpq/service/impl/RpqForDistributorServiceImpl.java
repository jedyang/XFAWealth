/**
 * 
 */
package com.fsll.wmes.rpq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.RpqPage;
import com.fsll.wmes.entity.RpqPageEn;
import com.fsll.wmes.entity.RpqPageQuest;
import com.fsll.wmes.entity.RpqPageSc;
import com.fsll.wmes.entity.RpqPageTc;
import com.fsll.wmes.entity.RpqPageType;
import com.fsll.wmes.entity.RpqQuest;
import com.fsll.wmes.entity.RpqQuestEn;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.RpqQuestItemEn;
import com.fsll.wmes.entity.RpqQuestItemSc;
import com.fsll.wmes.entity.RpqQuestItemTc;
import com.fsll.wmes.entity.RpqQuestSc;
import com.fsll.wmes.entity.RpqQuestTc;
import com.fsll.wmes.rpq.service.RpqForDistributorService;
import com.fsll.wmes.rpq.vo.RpqListVO;

/**
 * @author scshi
 *	代理商问卷调查实现类
 */
@Service("rpqForDistributorService")
public class RpqForDistributorServiceImpl extends BaseService implements RpqForDistributorService {
	
	/**
	 *查询RPQ问卷
	 *@author scshi
	 *@date 20170612
	 * */
	public JsonPaging queryPageList(JsonPaging jsonPaging, String language,RpqPage info, String typeSql){
		StringBuffer pageHql = new StringBuffer(" select r.id,l.title,t.clientType,t.pageType,r.status ");
		pageHql.append(" from RpqPage r ");
		pageHql.append(" LEFT join "+this.getLangString("RpqPage", language) );
		pageHql.append(" l on l.id=r.id ");
		pageHql.append(" LEFT JOIN RpqPageType t ON t.rpqPage.id=r.id ");
		pageHql.append("where 1=1 "+typeSql );
		List<Object> params=new ArrayList<Object>();
		if(StringUtils.isNotBlank(info.getTitle())){
			pageHql.append(" and l.title like ? ") ;
			params.add("%"+info.getTitle()+"%");
		}
		//实用类型，O=开户，K=KYC
		if(StringUtils.isNotBlank(info.getPageType())){
			pageHql.append(" AND t.pageType = ? ") ;
			params.add(info.getPageType()); 
		}
		
		//客户类型
		if(StringUtils.isNotBlank(info.getClientType())){
			pageHql.append(" AND t.clientType = ? ") ;
			params.add(info.getClientType()); 
		}
		pageHql.append( " order by r.createTime desc ");
		jsonPaging=this.baseDao.selectJsonPaging(pageHql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		List<RpqListVO> voList = new ArrayList();
		if(!list.isEmpty()){
			for(int y=0;y<list.size();y++){
				Object[] objs = (Object[])list.get(y);
				RpqListVO vo = new RpqListVO();
				vo.setRpqPageid(objs[0]==null?"":objs[0].toString());
				vo.setRpqPageTitle(objs[1]==null?"":objs[1].toString());
				vo.setClientType(objs[2]==null?"":objs[2].toString());
				vo.setPageType(objs[3]==null?"":objs[3].toString());
				vo.setStatus(objs[4]==null?"":objs[4].toString());
				voList.add(vo);
			}
		}
		jsonPaging.setList(voList);
		return jsonPaging;
	}
	
	
	/**
	 * 代理商题库管理
	 * @author scshi
	 * @date 20170612
	 * */
	public JsonPaging queryQuestionsList(JsonPaging jsonPaging,String language, RpqQuest info){
		StringBuffer pageHql = new StringBuffer(" FROM RpqQuest r ");
		pageHql.append(" INNER JOIN "+this.getLangString("RpqQuest", language));
		pageHql.append(" l ON l.id=r.id ");
		pageHql.append(" WHERE (r.distributor.id is null or r.distributor.id=?) ");
		List<Object> params=new ArrayList<Object>();
		params.add(info.getDistributor().getId());
		
		if(null!=info.getTitle()&&!"".equals(info.getTitle())){
			String title = info.getTitle();
			pageHql.append( " AND l.title LIKE '%"+title+"%'" );
		}
		if(null!=info.getStatus()&&!"".equals(info.getStatus())){
			pageHql.append(" AND r.status = '"+info.getStatus()+"'");
		}
		if(null!=info.getQuestType()&&!"".equals(info.getQuestType())){
			pageHql.append(" AND r.questType = '"+info.getQuestType()+"'");
		}
		jsonPaging=this.baseDao.selectJsonPaging(pageHql.toString(), params.toArray(), jsonPaging, false);
		return jsonPaging;
	}
	
	/**
	 * 保存来自模板的问卷
	 * @author scshi
	 * @date 20170613
	 * */
	public void saveTemplateUnionDistributor(MemberDistributor memberDistributor, String tempIds){
		String[] pageIdArray = tempIds.split(",");
		for(String pageId:pageIdArray){
			RpqPage page = (RpqPage)this.baseDao.get(RpqPage.class, pageId);
			RpqPageTc pageTc = (RpqPageTc)this.baseDao.get(RpqPageTc.class, pageId);
			RpqPageSc pageSc = (RpqPageSc)this.baseDao.get(RpqPageSc.class, pageId);
			RpqPageEn pageEn = (RpqPageEn)this.baseDao.get(RpqPageEn.class, pageId);
			
			RpqPage tempPage = new RpqPage();
			RpqPageTc tempPageTc = new RpqPageTc();
			RpqPageSc tempPageSc = new RpqPageSc();
			RpqPageEn tempPageEn = new RpqPageEn();
			
			BeanUtils.copyProperties(page, tempPage);
			tempPage.setStatus("draft");
			tempPage.setId(null);
			this.baseDao.saveOrUpdate(tempPage);
			
			tempPageTc.setId(tempPage.getId());
			tempPageTc.setTitle("(Template)"+pageTc.getTitle());
			tempPageTc.setRemark(pageTc.getRemark());
			this.baseDao.create(tempPageTc);
			
			tempPageSc.setId(tempPage.getId());
			tempPageSc.setTitle("(Template)"+pageSc.getTitle());
			tempPageSc.setRemark(pageSc.getRemark());
			this.baseDao.create(tempPageSc);
			
			tempPageEn.setId(tempPage.getId());
			tempPageEn.setTitle("(Template)"+pageEn.getTitle());
			tempPageEn.setRemark(pageEn.getRemark());
			this.baseDao.create(tempPageEn);
			
			//查找pageType并copy关联当前代理商
			StringBuffer pageTypeHql = new StringBuffer("from RpqPageType t where t.rpqPage.id=? ");
			List<RpqPageType> pageTypeList = this.baseDao.find(pageTypeHql.toString(), new String[]{pageId}, false);
			if(!pageTypeList.isEmpty()){
				RpqPageType pageType = pageTypeList.get(0);
				RpqPageType pageTypeTemp = new RpqPageType();
				BeanUtils.copyProperties(pageType, pageTypeTemp);
				pageTypeTemp.setId(null);
				pageTypeTemp.setDistributor(memberDistributor);
				pageTypeTemp.setCreateTime(new Date());
				pageTypeTemp.setRpqPage(tempPage);
				this.baseDao.saveOrUpdate(pageTypeTemp);
				
				//查找问题并copy关联当前代理商
				StringBuffer pageQuestionHql = new StringBuffer("from RpqPageQuest t where t.page.id=? order by t.orderBy ");
				List<RpqPageQuest> pageQuestList = this.baseDao.find(pageQuestionHql.toString(), new String[]{pageId}, false);
				if(!pageQuestList.isEmpty()){
					for(RpqPageQuest pageQuest:pageQuestList){
						RpqQuest question = pageQuest.getQuest();
						RpqQuestTc questionTc = (RpqQuestTc)this.baseDao.get(RpqQuestTc.class, question.getId());
						RpqQuestEn questionEn = (RpqQuestEn)this.baseDao.get(RpqQuestEn.class, question.getId());
						RpqQuestSc questionSc = (RpqQuestSc)this.baseDao.get(RpqQuestSc.class, question.getId());
						
						RpqQuest questionTemp = new RpqQuest();
						RpqQuestTc questionTcTemp = new RpqQuestTc();
						RpqQuestEn questionEnTemp = new RpqQuestEn();
						RpqQuestSc questionScTemp = new RpqQuestSc();
						BeanUtils.copyProperties(question, questionTemp);
						questionTemp.setId(null);
						questionTemp.setCreateTime(new Date());
						this.baseDao.saveOrUpdate(questionTemp);
						
						questionTcTemp.setId(questionTemp.getId());
						questionTcTemp.setTitle(questionTc.getTitle());
						questionTcTemp.setRemark(questionTc.getRemark());
						this.baseDao.create(questionTcTemp);
						
						questionEnTemp.setId(questionTemp.getId());
						questionEnTemp.setTitle(questionEn.getTitle());
						questionEnTemp.setRemark(questionEn.getRemark());
						this.baseDao.create(questionEnTemp);
						
						questionScTemp.setId(questionTemp.getId());
						questionScTemp.setTitle(questionSc.getTitle());
						questionScTemp.setRemark(questionSc.getRemark());
						this.baseDao.create(questionScTemp);
						
						//问题与问卷关联表
						RpqPageQuest pageQuestTemp = new RpqPageQuest();
						pageQuestTemp.setId(null);
						pageQuestTemp.setPage(tempPage);
						pageQuestTemp.setQuest(questionTemp);
						pageQuestTemp.setOrderBy(pageQuest.getOrderBy());
						this.baseDao.saveOrUpdate(pageQuestTemp);
						
						//查询模板选项
						StringBuffer itemsHql = new StringBuffer("from RpqQuestItem t where t.quest.id=? order by t.orderBy ");
						List<RpqQuestItem> itemList = this.baseDao.find(itemsHql.toString(), new String[]{question.getId()}, false);
						if(!itemList.isEmpty()){
							for(RpqQuestItem questItem:itemList){
								
								RpqQuestItemSc itemSc = (RpqQuestItemSc)this.baseDao.get(RpqQuestItemSc.class, questItem.getId());
								RpqQuestItemTc itemTc = (RpqQuestItemTc)this.baseDao.get(RpqQuestItemTc.class, questItem.getId());
								RpqQuestItemEn itemEn = (RpqQuestItemEn)this.baseDao.get(RpqQuestItemEn.class, questItem.getId());
								
								RpqQuestItem itemTemp = new RpqQuestItem();
								BeanUtils.copyProperties(questItem, itemTemp);
								itemTemp.setId(null);
								itemTemp.setQuest(questionTemp);
								this.baseDao.saveOrUpdate(itemTemp);
								
								RpqQuestItemSc itemScTemp = new RpqQuestItemSc();
								RpqQuestItemTc itemTcTemp = new RpqQuestItemTc();
								RpqQuestItemEn itemEnTemp = new RpqQuestItemEn();
								
								itemScTemp.setId(itemTemp.getId());
								itemScTemp.setRemark(itemSc.getRemark());
								itemScTemp.setTitle(itemSc.getTitle());
								this.baseDao.create(itemScTemp);
								
								itemTcTemp.setId(itemTemp.getId());
								itemTcTemp.setRemark(itemTc.getRemark());
								itemTcTemp.setTitle(itemTc.getTitle());
								this.baseDao.create(itemTcTemp);
								
								itemEnTemp.setId(itemTemp.getId());
								itemEnTemp.setRemark(itemEn.getRemark());
								itemEnTemp.setTitle(itemEn.getTitle());
								this.baseDao.create(itemEnTemp);
								
							}
						}//item if end
						
					}
				}//pageQuestionlist if end
			}//pageType if end			
		}
	}
	
}
