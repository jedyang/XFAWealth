
package com.fsll.wmes.rpq.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
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
import com.fsll.wmes.entity.RpqQuestEn;
import com.fsll.wmes.entity.RpqQuestItem;
import com.fsll.wmes.entity.RpqQuestItemEn;
import com.fsll.wmes.entity.RpqQuestItemSc;
import com.fsll.wmes.entity.RpqQuestItemTc;
import com.fsll.wmes.entity.RpqQuestSc;
import com.fsll.wmes.entity.RpqQuestTc;
import com.fsll.wmes.rpq.service.RpqManageService;
import com.fsll.wmes.rpq.vo.RpqListVO;
import com.fsll.wmes.rpq.vo.RpqPageLangVO;
import com.fsll.wmes.rpq.vo.RpqPageLevelLangVO;
import com.fsll.wmes.rpq.vo.RpqPageQuestItemView;
import com.fsll.wmes.rpq.vo.RpqPageQuestView;
import com.fsll.wmes.rpq.vo.RpqPageView;
import com.fsll.wmes.rpq.vo.RpqQuestItemVO;


/***
 * 业务接口实现类：RPQ管理接口类
 * @author 林文伟
 * @date 2016-06-22
 */
@Service("rpqManageService")
//@Transactional
public class RpqManageServiceImpl extends BaseService implements RpqManageService {

	/***
     * 问卷列表查询的方法
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findPageAll(JsonPaging jsonpaging, RpqPage info,String langCode,String typeSql,String pageType,String clientType) {
		String hql=" select r.id,l.title,t.clientType,t.pageType,r.status from RpqPage r  ";
		hql += " LEFT join "+this.getLangString("RpqPage", langCode);
		hql += " l on l.id=r.id ";
		hql += " LEFT JOIN RpqPageType t ON t.rpqPage.id=r.id ";
		hql += " where 1=1 "+typeSql;
		List<Object> params=new ArrayList<Object>();
		
		//add 列表查询条件    wwluo 160826
		if(StringUtils.isNotBlank(info.getTitle())){
			hql += " and l.title like ?";
			params.add("%"+info.getTitle()+"%");
		}
		//开户类型筛选
		if(StringUtils.isNotBlank(pageType)){
			hql += " AND t.pageType = ?";
			params.add(pageType); 
		}
		if(StringUtils.isNotBlank(clientType)){
			hql += " AND t.clientType = ?";
			params.add(clientType); 
		}
		//end
		hql += " order by r.createTime desc";
		
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
		List list = jsonpaging.getList();
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
		jsonpaging.setList(voList);
		return jsonpaging;
	}
	
	/***
     * 设置问卷与题库关联的方法
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public void setPageQuest(String pageId,List<RpqPageQuest> pageQuestList) {
		this.delPageQuestItemByPageId(pageId);//先删除所有关联
		for(RpqPageQuest item : pageQuestList)
		{
			this.baseDao.saveOrUpdate(item,true);
		}
	}
	
	/***
     * 删除问卷下下的所有题目quest
     * @author 林文伟
     * @date 2016-0-20
     */
	@Override
	public void delPageQuestItemByPageId(String pageId) {
		String hql = " delete RpqPageQuest where  page.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(pageId);
		this.baseDao.updateHql(hql, params.toArray());
	}
	
	/***
     * 获取问卷下的所有题目
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public List<RpqQuest> getQuestByPageId(String pageId,String langCode) {
		String hql = " FROM RpqPageQuest a INNER JOIN RpqQuest b  ON a.quest.id=b.id ";
		hql +=" inner join "+this.getLangString("RpqQuest", langCode);
		hql += " l on l.id=b.id ";
		hql += " WHERE  a.page.id=? order by a.orderBy asc";
		List<Object> params1=new ArrayList<Object>();
		params1.add(pageId);

		List<RpqQuest> list = this.baseDao.find(hql, params1.toArray(), false);

		return list;
	}
	
	/***
     * 通过ID获取问卷信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPage getPage(String id) {
		Object obj = (RpqPage) baseDao.get(RpqPage.class, id);
		if(obj!=null)
		{
			return (RpqPage)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取问卷SC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageSc getPageSc(String id) {
		Object obj = (RpqPageSc) baseDao.get(RpqPageSc.class, id);
		if(obj!=null)
		{
			return (RpqPageSc)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取问卷TC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageTc getPageTc(String id) {
		Object obj = (RpqPageTc) baseDao.get(RpqPageTc.class, id);
		if(obj!=null)
		{
			return (RpqPageTc)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取问卷EN信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageEn getPageEn(String id) {
		Object obj = (RpqPageEn) baseDao.get(RpqPageEn.class, id);
		if(obj!=null)
		{
			return (RpqPageEn)obj;
		} else return null;
	}
	
	/***
     * 保存
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPage saveOrUpdate(RpqPage rpqPage,RpqPageLangVO vo, boolean isAdd) {
		rpqPage=(RpqPage)this.baseDao.saveOrUpdate(rpqPage,isAdd);
		String pageId = rpqPage.getId();
		//更新多语言
		if(isAdd)
		{
			//SC
			RpqPageSc sc = new RpqPageSc();
			sc.setId(pageId);
			sc.setTitle(vo.getTitleSc());
			sc.setRemark(vo.getRemarkSc());
			this.baseDao.create(sc);
			//TC
			RpqPageTc tc = new RpqPageTc();
			tc.setId(pageId);
			tc.setTitle(vo.getTitleTc());
			tc.setRemark(vo.getRemarkTc());
			this.baseDao.create(tc);
			//EN
			RpqPageEn en = new RpqPageEn();
			en.setId(pageId);
			en.setTitle(vo.getTitleEn());
			en.setRemark(vo.getRemarkEn());
			this.baseDao.create(en);
		} 
		else
		{
			//SC
			RpqPageSc sc = new RpqPageSc();
			sc.setId(pageId);
			sc.setTitle(vo.getTitleSc());
			sc.setRemark(vo.getRemarkSc());
			this.baseDao.saveOrUpdate(sc,false);
			//TC
			RpqPageTc tc = new RpqPageTc();
			tc.setId(pageId);
			tc.setTitle(vo.getTitleTc());
			tc.setRemark(vo.getRemarkTc());
			this.baseDao.saveOrUpdate(tc,false);
			//EN
			RpqPageEn en = new RpqPageEn();
			en.setId(pageId);
			en.setTitle(vo.getTitleEn());
			en.setRemark(vo.getRemarkEn());
			this.baseDao.saveOrUpdate(en,false);
		}
		
		return rpqPage;
	}
	
	/**************************等级 管理******************************/
	/***
     * 获取问卷的所有等级数据列表
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public List<RpqPageLevel> getLevelByPageId(String pageId,String langCode) {
		String hql = "select a,l.result,l.remark FROM RpqPageLevel a  ";
		hql +=" inner join "+this.getLangString("RpqPageLevel", langCode);
		hql += " l on l.id=a.id ";
		hql += " WHERE  a.page.id=? order by a.beginScore asc";
		List<Object> params1=new ArrayList<Object>();
		params1.add(pageId);

		List list = this.baseDao.find(hql, params1.toArray(), false);
		List<RpqPageLevel> result = new ArrayList<RpqPageLevel>();
		if (null!=list && !list.isEmpty()){
			for (int i=0 ;i<list.size();i++){
				Object[] obj = (Object[])list.get(i);
				RpqPageLevel level = (RpqPageLevel)obj[0];
				level.setResult(StrUtils.getString(obj[1]));
				level.setRemark(StrUtils.getString(obj[2]));
				result.add(level);
			}
		}
		return result;
	}
	
	/***
     * 通过ID获取等级信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageLevel getPageLevel(String id) {
		Object obj = (RpqPageLevel) baseDao.get(RpqPageLevel.class, id);
		if(obj!=null)
		{
			return (RpqPageLevel)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取等级SC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageLevelSc getPageLevelSc(String id) {
		Object obj = (RpqPageLevelSc) baseDao.get(RpqPageLevelSc.class, id);
		if(obj!=null)
		{
			return (RpqPageLevelSc)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取等级TC信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageLevelTc getPageLevelTc(String id) {
		Object obj = (RpqPageLevelTc) baseDao.get(RpqPageLevelTc.class, id);
		if(obj!=null)
		{
			return (RpqPageLevelTc)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取等级EN信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageLevelEn getPageLevelEn(String id) {
		Object obj = (RpqPageLevelEn) baseDao.get(RpqPageLevelEn.class, id);
		if(obj!=null)
		{
			return (RpqPageLevelEn)obj;
		} else return null;
	}
	
	/**************************题库 管理******************************/
	/***
     *题库列表查询的方法
     * @author 林文伟
     * @date 2016-06-29
     */
	@Override
	//@Transactional(readOnly=true)
	public JsonPaging findQuestAll(JsonPaging jsonpaging, RpqQuest info,String langCode,String typeSql) {
		String hql=" from RpqQuest r ";
		hql +=" inner join "+this.getLangString("RpqQuest", langCode);
		hql += " l on l.id=r.id ";
		hql += " where 1=1 ";
		if (StringUtils.isNoneBlank(typeSql))
			hql += typeSql;
		List<Object> params=new ArrayList<Object>();
		if(null!=info.getTitle()&&!"".equals(info.getTitle())){
			hql+=" and l.title like '%"+info.getTitle()+"%'";
		}
		if(null!=info.getStatus()&&!"".equals(info.getStatus())){
			hql+=" and r.status = '"+info.getStatus()+"'";
		}
		if(StringUtils.isNoneBlank(info.getQuestType())){
			hql += " AND r.questType = '" + info.getQuestType() + "'";
		}
		
		jsonpaging=this.baseDao.selectJsonPaging(hql, params.toArray(), jsonpaging, false);
//		List list = new ArrayList();
//		Iterator it = jsonpaging.getList().iterator();
//		Integer indexNumber = (jsonpaging.getPage()-1)*jsonpaging.getRows();
//		Integer index = 0;
//    	while(it.hasNext()){
//			//index++;
//    		Object obj = (Object)it.next();
//    		Object vo = new Object();
//			BeanUtils.copyProperties(obj,vo);//拷贝信息;
//			
//			list.add(vo);
//		}
//    	jsonpaging.setList(list);
		return jsonpaging;
	}
	
	/***
     * 保存题库的方法
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqQuest save(RpqQuest rpqQuest,RpqQuestSc rpqQuestSc,RpqQuestTc rpqQuestTc,RpqQuestEn rpqQuestEn,List<RpqQuestItemVO> itemList,boolean isAdd) {
		rpqQuest=(RpqQuest)this.baseDao.saveOrUpdate(rpqQuest,isAdd);
		
		if(isAdd)
		{
			rpqQuestSc.setId(rpqQuest.getId());
			this.baseDao.create(rpqQuestSc);
			
			rpqQuestTc.setId(rpqQuest.getId());
			this.baseDao.create(rpqQuestTc);
			
			rpqQuestEn.setId(rpqQuest.getId());
			this.baseDao.create(rpqQuestEn);

		} else
		{
			rpqQuestSc.setId(rpqQuest.getId());
			rpqQuestTc.setId(rpqQuest.getId());
			rpqQuestEn.setId(rpqQuest.getId());
			this.baseDao.saveOrUpdate(rpqQuestSc,isAdd);
			this.baseDao.saveOrUpdate(rpqQuestTc,isAdd);
			this.baseDao.saveOrUpdate(rpqQuestEn,isAdd);
		}
		
		//管理选项
		this.delRpqQuestItemByQuestId(rpqQuest.getId());//全部删除后重新添加
		for(RpqQuestItemVO item : itemList)
		{
			RpqQuestItem realItem = new RpqQuestItem();
			realItem.setId(null);
			realItem.setOrderBy(item.getOrderBy());
			realItem.setQuest(rpqQuest);
			//realItem.setRemark(item.getRemark());
			realItem.setScoreValue(item.getScoreValue());
			//realItem.setTitle(item.getTitle());
			realItem.setType(item.getType());
			
			realItem = (RpqQuestItem)this.baseDao.create(realItem);//添加主选项
			//添加多语言项
			RpqQuestItemSc itemSc = new RpqQuestItemSc();
			itemSc.setId(realItem.getId());
			itemSc.setTitle(item.getTitleSc());
			itemSc.setRemark(item.getRemarkSc());
			this.baseDao.create(itemSc);
			
			RpqQuestItemTc itemTc = new RpqQuestItemTc();
			itemTc.setId(realItem.getId());
			itemTc.setTitle(item.getTitleTc());
			itemTc.setRemark(item.getRemarkTc());
			this.baseDao.create(itemTc);
			
			RpqQuestItemEn itemEn= new RpqQuestItemEn();
			itemEn.setId(realItem.getId());
			itemEn.setTitle(item.getTitleEn());
			itemEn.setRemark(item.getRemarkEn());
			this.baseDao.create(itemEn);
		}
		
		return rpqQuest;
	}
	
	/***
     * 通过ID获取信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqQuest getQuest(String id) {
		Object obj = (RpqQuest) baseDao.get(RpqQuest.class, id);
		if(obj!=null)
		{
			return (RpqQuest)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取简体中文信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqQuestSc getQuestSc(String id) {
		Object obj = (RpqQuestSc) baseDao.get(RpqQuestSc.class, id);
		if(obj!=null)
		{
			return (RpqQuestSc)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取繁体中文信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqQuestTc getQuestTc(String id) {
		Object obj = (RpqQuestTc) baseDao.get(RpqQuestTc.class, id);
		if(obj!=null)
		{
			return (RpqQuestTc)obj;
		} else return null;
	}
	
	/***
     * 通过ID获取繁体英文信息
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqQuestEn getQuestEn(String id) {
		Object obj = (RpqQuestEn) baseDao.get(RpqQuestEn.class, id);
		if(obj!=null)
		{
			return (RpqQuestEn)obj;
		} else return null;
	}
	
	/***
     * 获取题目下的所有选项，同时包含简繁英数据
     * @author 林文伟
     * @date 2016-0-20
     */
	@Override
	public List<RpqQuestItem> getRpqQuestItemByQuestId(String questId) {
		String hql = " FROM RpqQuestItem  r";
		hql +=" inner join RpqQuestItemSc s on s.id=r.id";
		hql +=" inner join RpqQuestItemTc t on t.id=r.id";
		hql +=" inner join RpqQuestItemEn e on e.id=r.id";
		hql += " WHERE r.quest.id=? order by r.orderBy asc";
		List<Object> params = new ArrayList<Object>();
		params.add(questId);
		List<RpqQuestItem> list = this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/***
     * 删除quest下的所有item
     * @author 林文伟
     * @date 2016-0-20
     */
	@Override
	public void delRpqQuestItemByQuestId(String questId) {
		//先获取取所有选项
		String hql = " FROM RpqQuestItem  WHERE quest.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(questId);
		List<RpqQuestItem> list = this.baseDao.find(hql, params.toArray(), false);
		for(RpqQuestItem each :list)
		{
			String itemId = each.getId();
			List<Object> params1 = new ArrayList<Object>();
			params1.add(itemId);
			
			hql = " delete RpqQuestItemSc where  id=? ";
			this.baseDao.updateHql(hql, params1.toArray());
			
			hql = " delete RpqQuestItemTc where  id=? ";
			this.baseDao.updateHql(hql, params1.toArray());
			
			hql = " delete RpqQuestItemEn where  id=? ";
			this.baseDao.updateHql(hql, params1.toArray());
			
//			System.out.println(itemId);
//			RpqQuestItemSc sc = new RpqQuestItemSc();
//			sc.setId(itemId);
//			System.out.println(sc);
//			System.out.println(sc.getId());
//			System.out.println(sc.getRemark());
//			System.out.println(null == sc);
//			if(null!=sc)this.baseDao.delete(sc);
//			
//			RpqQuestItemTc tc = new RpqQuestItemTc();
//			tc.setId(itemId);
//			if(null!=tc)this.baseDao.delete(tc);
//			
//			RpqQuestItemEn en = new RpqQuestItemEn();
//			en.setId(itemId);
//			if(null!=en)this.baseDao.delete(en);
		}
		
		hql = " delete RpqQuestItem where  quest.id=? ";
		this.baseDao.updateHql(hql, params.toArray());
	}
	
	/***
     * 删除quest
     * @author 林文伟
     * @date 2016-0-20
     */
	@Override
	public void delRpqQuest(String questId) {
		//先获取取所有选项
		delRpqQuestItemByQuestId(questId);
		
		RpqQuestSc sc = new RpqQuestSc();
		sc.setId(questId);
		this.baseDao.delete(sc);
		
		RpqQuestTc tc = new RpqQuestTc();
		tc.setId(questId);
		this.baseDao.delete(tc);
		
		RpqQuestEn en = new RpqQuestEn();
		en.setId(questId);
		this.baseDao.delete(en);
		
//		RpqQuest info = new RpqQuest();
//		info.setId(questId);
//		if(null!=info)this.baseDao.delete(info);
		
		String hql = " delete RpqQuestItem where  quest.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(questId);
		this.baseDao.updateHql(hql, params.toArray());
	}
	
	/***
     * 删除page
     * @author 林文伟
     * @date 2016-0-20
     */
	@Override
	public void delRpqPage(String pageId) {

		RpqPageSc sc = new RpqPageSc();
		sc.setId(pageId);
		this.baseDao.delete(sc);
		
		RpqPageTc tc = new RpqPageTc();
		tc.setId(pageId);
		this.baseDao.delete(tc);
		
		RpqPageEn en = new RpqPageEn();
		en.setId(pageId);
		this.baseDao.delete(en);
		
//		RpqQuest info = new RpqQuest();
//		info.setId(questId);
//		if(null!=info)this.baseDao.delete(info);
		
		String hql = " delete RpqPage where  id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(pageId);
		this.baseDao.updateHql(hql, params.toArray());
	}
	
	/***
     * 保存等级数据
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageLevel savePageLevel(RpqPageLevel level ,RpqPageLevelLangVO vo,boolean isAdd) {
		
		level=(RpqPageLevel)this.baseDao.saveOrUpdate(level,isAdd);

		//更新多语言
		if(isAdd)
		{
			//SC
			RpqPageLevelSc sc = new RpqPageLevelSc();
			sc.setId(level.getId());
			sc.setResult(vo.getTitleSc());
			sc.setRemark(vo.getRemarkSc());
			this.baseDao.create(sc);
			//TC
			RpqPageLevelTc tc = new RpqPageLevelTc();
			tc.setId(level.getId());
			tc.setResult(vo.getTitleTc());
			tc.setRemark(vo.getRemarkTc());
			this.baseDao.create(tc);
			//EN
			RpqPageLevelEn en = new RpqPageLevelEn();
			en.setId(level.getId());
			en.setResult(vo.getTitleEn());
			en.setRemark(vo.getRemarkEn());
			this.baseDao.create(en);
		} 
		else
		{
			//SC
			RpqPageLevelSc sc = new RpqPageLevelSc();
			sc.setId(level.getId());
			sc.setResult(vo.getTitleSc());
			sc.setRemark(vo.getRemarkSc());
			this.baseDao.saveOrUpdate(sc,false);
			//TC
			RpqPageLevelTc tc = new RpqPageLevelTc();
			tc.setId(level.getId());
			tc.setResult(vo.getTitleTc());
			tc.setRemark(vo.getRemarkTc());
			this.baseDao.saveOrUpdate(tc,false);
			//EN
			RpqPageLevelEn en = new RpqPageLevelEn();
			en.setId(level.getId());
			en.setResult(vo.getTitleEn());
			en.setRemark(vo.getRemarkEn());
			this.baseDao.saveOrUpdate(en,false);
		}
		
		return level;
	}
	
	/***
     * 删除Level
     * @author 林文伟
     * @date 2016-0-20
     */
	@Override
	public void delRpqPageLevel(String levelId) {
		String hql = " delete RpqPageLevel where  id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(levelId);
		this.baseDao.updateHql(hql, params.toArray());
	}
	
	/***
     * 预览问卷
     * @author 林文伟
     * @date 2016-06-20
     */
	@Override
	public RpqPageView viewPage(String pageId,String langCode) {
		//要返回的实体
		RpqPageView view = new RpqPageView();
		List<RpqPageQuestView> rpqPageQuestView = new ArrayList<RpqPageQuestView>();
		//List<Object>  list2 = new ArrayList<Object>();
		//1.先获取问卷基础信息
		String hql = "SELECT p,a.title,a.remark FROM RpqPage p left join " + this.getLangString("RpqPage", langCode) + " a on p.id=a.id ";
		hql += " WHERE  a.id=? ";
		List<Object> params1=new ArrayList<Object>();
		params1.add(pageId);
		List list = this.baseDao.find(hql, params1.toArray(), false);
		
		if (null!=list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			
			view.setPaper((RpqPage)objs[0]);
			view.setId(view.getPaper().getId());
			view.setPageName(StrUtils.getString(objs[1]));
			view.setRemark(StrUtils.getString(objs[2]));
			
			//2.获取题目
			String hql2 = "SELECT b,c.title,c.remark FROM RpqQuest b  ";
			hql2 += " inner join RpqPageQuest a on a.quest.id = b.id ";
			hql2 += " inner join "+this.getLangString("RpqQuest", langCode)+" c on c.id = b.id ";
			hql2 += " where a.page.id=? order by a.orderBy asc";
			List<Object> params3=new ArrayList<Object>();
			params3.add(pageId);
			List list1 = this.baseDao.find(hql2, params3.toArray(), false);//每个LIST都是一个二维数组，每个数组有三个实体
			//System.out.print(hql);System.out.print(pageId);
			//Object[] s = (Object[])list1.toArray();//3个实体
			for(int i=0;i<list1.size();i++){//循环每道题目
				Object[] each = (Object[])list1.get(i);
				RpqPageQuestView questView = new RpqPageQuestView();
				questView.setQuest((RpqQuest)each[0]);
				questView.setId(questView.getQuest().getId());
				questView.setPageId(view.getId());
				questView.setTitleName(StrUtils.getString(each[1]));
				questView.setRemark(StrUtils.getString(each[2]));
				
				String questId = questView.getQuest().getId();

				//3.每个题目的所有选项
				 String hql1 = "SELECT a,b.title,b.remark FROM RpqQuestItem  a ";
				 hql1 += " inner join "+this.getLangString("RpqQuestItem", langCode)+" b on b.id = a.id ";
				 hql1 += " where a.quest.id=? order by a.orderBy asc";
				 List<Object> params2=new ArrayList<Object>();
				 params2.add(questId);
				 List  list3 = this.baseDao.find(hql1, params2.toArray(), false);//每个LIST都是一个二维数组，每个数组有2个实体
	
				 List<RpqPageQuestItemView> rpqPageQuestItemView = new ArrayList<RpqPageQuestItemView>();
				 for(int k=0;k<list3.size();k++)
				 {
					 Object[] each2 = (Object[])list3.get(k);
					 RpqPageQuestItemView questItemView = new RpqPageQuestItemView();
					 questItemView.setItem((RpqQuestItem)each2[0]);
					 questItemView.setId(questItemView.getItem().getId());
					 questItemView.setPageId(view.getId());
					 questItemView.setQuestId(questId);
					 questItemView.setType(questItemView.getItem().getType());
					 questItemView.setScoreValue(questItemView.getItem().getScoreValue());
					 
					 questItemView.setTitle(StrUtils.getString(each2[1]));
					 questItemView.setRemark(StrUtils.getString(each2[2]));
					 rpqPageQuestItemView.add(questItemView);
				 }
				 questView.setItemList(rpqPageQuestItemView);
				 rpqPageQuestView.add(questView);
			 }
	
			 view.setQuestList(rpqPageQuestView);
		}
		return view;
	}
	
	/**
	 * 工作台问卷类型列表列表
	 * @author scshi_u330p
	 * @date 20161212
	 * */
	public void consoleSaveOrUpdateRpqPagetType(RpqPage info,String clientType, String isDefault, String pageType,MemberAdmin admin){
		List params = new ArrayList();
		StringBuffer sb = new StringBuffer("from RpqPageType t where 1=1");
		sb.append(" and t.rpqPage.id=? ");
		params.add(info.getId());
		List<RpqPageType> typeList = this.baseDao.find(sb.toString(), params.toArray(), false);
		if(typeList.isEmpty()){
			RpqPageType type = new RpqPageType();
			type.setId(null);
			type.setClientType(clientType);
			type.setCreateTime(new Date());
			if("2".equals(admin.getType())){//代理商
				type.setDistributor(admin.getDistributor());
			}else if ("1".equals(admin.getType())){//ifaFirm
				type.setIfafirm(admin.getIfafirm());
			}
			type.setIsDef(isDefault);
			type.setLastUpdate(new Date());
			//type.setOrderBy()
			type.setPageType(pageType);
			type.setRpqPage(info);
			this.baseDao.saveOrUpdate(type);
		}else{
			for(int x=0;x<typeList.size();x++){
				RpqPageType typeObj = typeList.get(x);
				typeObj.setClientType(clientType);
				typeObj.setIsDef(isDefault);
				typeObj.setPageType(pageType);
				typeObj.setLastUpdate(new Date());
				this.baseDao.saveOrUpdate(typeObj);
			}
		}
	}
	
	/**
	 * 工作台获取问卷信息
	 * @author scshi_u330p
	 * @date 20161212
	 * */
	public RpqListVO getPageVoById(String id, String language){
		StringBuffer sb = new StringBuffer(" select r.id,l.title,t.clientType,t.pageType,r.status,t.isDef,r.isCalScore ");
		sb.append(" from RpqPage r inner join "+this.getLangString("RpqPage", language));
		sb.append(" l on l.id=r.id LEFT JOIN RpqPageType t ON t.rpqPage.id=r.id  where r.id=? ");
		sb.append(" order by r.createTime desc ");
		List list = this.baseDao.find(sb.toString(), new String[]{id}, false);
		if(!list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			RpqListVO vo = new RpqListVO();
			vo.setRpqPageid(objs[0]==null?"":objs[0].toString());
			vo.setRpqPageTitle(objs[1]==null?"":objs[1].toString());
			vo.setClientType(objs[2]==null?"":objs[2].toString());
			vo.setPageType(objs[3]==null?"":objs[3].toString());
			vo.setStatus(objs[4]==null?"":objs[4].toString());
			vo.setIsDef(objs[5]==null?"":objs[5].toString());
			vo.setIsCalScore(objs[6]==null?"":objs[6].toString());
			return vo;
		}
		return null;
	}
}


