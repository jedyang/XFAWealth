package com.fsll.wmes.web.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.core.vo.WebVisitCountVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.community.service.CommunityService;
import com.fsll.wmes.entity.BbsTopic;
import com.fsll.wmes.entity.BbsTopicCount;
import com.fsll.wmes.entity.CommunityBehavior;
import com.fsll.wmes.entity.CommunityQuestion;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.CornerInfo;
import com.fsll.wmes.entity.CornerInfoCount;
import com.fsll.wmes.entity.FundInfoCount;
import com.fsll.wmes.entity.InsightCount;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.LiveCount;
import com.fsll.wmes.entity.LiveInfo;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCount;
import com.fsll.wmes.entity.StrategyCount;
import com.fsll.wmes.entity.StrategyInfo;
import com.fsll.wmes.entity.WebVisitLog;
import com.fsll.wmes.web.service.WebVisitLogService;

/***
 * 业务接口实现类：访问记录
 * @author mqzou
 * @date 2016-12-22
 */
@Service("webVisitLogService")
//@Transactional
public class WebVisitLogServiceImpl  extends BaseService implements WebVisitLogService {

	@Autowired
	private CommunityService communityService;
	
	/**
	 * 保存访问记录
	 * @author mqzou
	 * @date 2016-12-22
	 * @param log
	 * @return
	 */
	@Override
	public WebVisitLog addLog(WebVisitLog log) {
		boolean isAdd=false;
		if(CommonConstantsWeb.WEB_VISIT_BBS_TOPIC.equals(log.getModuleType())){
			BbsTopic topic=(BbsTopic)this.baseDao.get(BbsTopic.class, log.getRelateId());
			if(topic.getAuthor().getId().equals(log.getMember().getId()))
				return log;
			BbsTopicCount count=(BbsTopicCount)this.baseDao.get(BbsTopicCount.class, log.getRelateId());
			if(null==count){
				count=new BbsTopicCount();
				isAdd=true;
			}
	        BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			if(isAdd)this.baseDao.create(count);
			else {
				this.baseDao.saveOrUpdate(count);
			}
		}else if (CommonConstantsWeb.WEB_VISIT_CORNER_INFO.equals(log.getModuleType())) {
			CornerInfo cornerInfo=(CornerInfo)this.baseDao.get(CornerInfo.class, log.getRelateId());
			if(cornerInfo.getAuthor().getId().equals(log.getMember().getId()))
				return log;
			CornerInfoCount count=(CornerInfoCount)this.baseDao.get(CornerInfoCount.class, log.getRelateId());
			if(null==count){
				count=new CornerInfoCount();
				isAdd=true;
			}
	        BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			if(isAdd)this.baseDao.create(count);
			else {
				this.baseDao.saveOrUpdate(count);
			}
		}else if (CommonConstantsWeb.WEB_VISIT_FUND.equals(log.getModuleType())) {
			
			FundInfoCount count=(FundInfoCount)this.baseDao.get(FundInfoCount.class, log.getRelateId());
			if(null==count){
				count=new FundInfoCount();
				isAdd=true;
			}
	        BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			if(isAdd)this.baseDao.create(count);
			else {
				this.baseDao.saveOrUpdate(count);
			}
		}else if (CommonConstantsWeb.WEB_VISIT_INSIGHT.equals(log.getModuleType())) {
			InsightInfo insightInfo=(InsightInfo)this.baseDao.get(InsightInfo.class, log.getRelateId());
			if(insightInfo.getAuthor().getId().equals(log.getMember().getId()))
				return log;
			InsightCount count=(InsightCount)this.baseDao.get(InsightCount.class, log.getRelateId());
			if(null==count){
				count=new InsightCount();
				isAdd=true;
			}
	        BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			if(isAdd)this.baseDao.create(count);
			else {
				this.baseDao.saveOrUpdate(count);
			}
		}else if (CommonConstantsWeb.WEB_VISIT_LIVE.equals(log.getModuleType())) {
			LiveInfo liveInfo=(LiveInfo)this.baseDao.get(LiveInfo.class, log.getRelateId());
			if(liveInfo.getAuthor().getId().equals(log.getMember().getId()))
				return log;
			
			LiveCount count=(LiveCount)this.baseDao.get(LiveCount.class, log.getRelateId());
			if(null==count){
				count=new LiveCount();
				isAdd=true;
			}
	        BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			if(isAdd)this.baseDao.create(count);
			else {
				this.baseDao.saveOrUpdate(count);
			}
		}else if (CommonConstantsWeb.WEB_VISIT_NEWS.equals(log.getModuleType())) {
			
			//NewsCount count=(NewsCount)this.baseDao.get(NewsCount.class, log.getRelateId());
			//if(null==count){
			//	count=new NewsCount();
			//	isAdd=true;
			//}
	        //BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			//if(isAdd)this.baseDao.create(count);
			//else {
			//	this.baseDao.saveOrUpdate(count);
			//}
		}else if (CommonConstantsWeb.WEB_VISIT_PORTFOLIO.equals(log.getModuleType())) {
			PortfolioArena arena=(PortfolioArena)this.baseDao.get(PortfolioArena.class, log.getRelateId());
			if(arena.getCreator().getId().equals(log.getMember().getId()))
				return log;
			
			PortfolioArenaCount count=(PortfolioArenaCount)this.baseDao.get(PortfolioArenaCount.class, log.getRelateId());
			if(null==count){
				count=new PortfolioArenaCount();
				isAdd=true;
			}
	        BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			if(isAdd)this.baseDao.create(count);
			else {
				this.baseDao.saveOrUpdate(count);
			}
		}else if (CommonConstantsWeb.WEB_VISIT_STRATEGY.equals(log.getModuleType())) {
			StrategyInfo strategyInfo=(StrategyInfo)this.baseDao.get(StrategyInfo.class, log.getRelateId());
			if(strategyInfo.getCreator().getId().equals(log.getMember().getId()))
				return log;
			
			StrategyCount count=(StrategyCount)this.baseDao.get(StrategyCount.class, log.getRelateId());
			if(null==count){
				count=new StrategyCount();
				isAdd=true;
			}
	        BeanUtils.copyProperties((WebVisitCountVO)getUpdateCount(count, log), count);
			if(isAdd)this.baseDao.create(count);
			else {
				this.baseDao.saveOrUpdate(count);
			}
		}else if(CommonConstantsWeb.WEB_VISIT_COMMUNITY_TOPIC.equals(log.getModuleType())){//社区--帖子
			CommunityBehavior behavior=new CommunityBehavior();
			behavior.setBehaviorType(CommonConstantsWeb.COMUNITY_BEHAVIOR_READ);
			behavior.setCreateTime(DateUtil.getCurDate());
			behavior.setCreator(log.getMember());
			behavior.setTarget(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_TOPIC);
			behavior.setTargetId(log.getRelateId());
			baseDao.saveOrUpdate(behavior);
			CommunityTopic topic=communityService.findCommunityTopicById(log.getRelateId());
		    int count=null!=topic.getReadCount()?topic.getReadCount():0;
		    topic.setReadCount(count+1);
		    baseDao.saveOrUpdate(topic);
		}else if(CommonConstantsWeb.WEB_VISIT_COMMUNITY_QUESTION.equals(log.getModuleType())){//社区--问题
			CommunityBehavior behavior=new CommunityBehavior();
			behavior.setBehaviorType(CommonConstantsWeb.COMUNITY_BEHAVIOR_READ);
			behavior.setCreateTime(DateUtil.getCurDate());
			behavior.setCreator(log.getMember());
			behavior.setTarget(CommonConstantsWeb.COMUNITY_BEHAVIOR_TARGET_QUESTION);
			behavior.setTargetId(log.getRelateId());
			baseDao.saveOrUpdate(behavior);
			CommunityQuestion question=communityService.findCommunityQuestion(log.getRelateId());
		    int count=null!=question.getReadCount()?question.getReadCount():0;
		    question.setReadCount(count+1);
		    baseDao.saveOrUpdate(question);
		}
		log=(WebVisitLog)this.baseDao.saveOrUpdate(log);
		return log;
	}
	
	/**
	 * 
	 * @param count
	 * @param log
	 * @return
	 */
	private WebVisitCountVO getUpdateCount(Object object,WebVisitLog log){
		WebVisitCountVO count=new WebVisitCountVO();
		if(null!=object)
			 BeanUtils.copyProperties(object, count);
		
		if(CommonConstantsWeb.WEB_VISIT_BUS_VIEW.equals(log.getBusType())){
			count.setViews(count.getViews()+1);
		}else if (CommonConstantsWeb.WEB_VISIT_BUS_DOWN.equals(log.getBusType())) {
			count.setDowns(count.getDowns()+1);
		}else if (CommonConstantsWeb.WEB_VISIT_BUS_UP.equals(log.getBusType())) {
			count.setUps(count.getUps()+1);
		}
		count.setId(log.getRelateId());
		return count;
	}
	
	

	/**
	 * 获取最新的一条访问记录（根据memberId或者浏览器标识）
	 * @author mqzou
	 * @date 2016-12-22
	 * @param log
	 * @return
	 */
	@Override
	public WebVisitLog findLastLog(WebVisitLog log) {
		StringBuilder hql=new StringBuilder();
		hql.append(" from WebVisitLog r where 1=1");
		List<Object> params=new ArrayList<Object>();
		if(null!=log){
			if(null!=log.getMember() && null!=log.getMember().getId() && !"".equals(log.getMember().getId())){
				hql.append(" and r.member.id=?");
				params.add(log.getMember().getId());
			}else if (null!=log.getBrowser() && !"".equals(log.getBrowser())) {
				hql.append(" and r.browser=?");
				params.add(log.getBrowser());
			}
			if(null!=log.getBusType() && !"".equals(log.getBusType())){
				hql.append(" and r.busType=?");
				params.add(log.getBusType());
			}
			if(null!=log.getModuleType() && !"".equals(log.getModuleType())){
				hql.append(" and r.moduleType=?");
				params.add(log.getModuleType());
			}
			if(null!=log.getRelateId() && !"".equals(log.getRelateId())){
				hql.append(" and r.relateId=?");
				params.add(log.getRelateId());
			}
		}
		hql.append(" order by r.vistiTime desc");
		List list=this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			WebVisitLog log2=(WebVisitLog)list.get(0);
			return log2;
		}
		return null;
	}

}
