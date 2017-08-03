package com.fsll.app.investor.market.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fsll.app.common.service.AppSysParamService;
import com.fsll.app.common.vo.AppSysParamVO;
import com.fsll.app.investor.market.service.AppIndexService;
import com.fsll.app.investor.market.vo.AppIndexBbsTopicVO;
import com.fsll.app.investor.market.vo.AppIndexHotVO;
import com.fsll.app.investor.market.vo.AppIndexNoMissVo;
import com.fsll.app.investor.market.vo.AppIndexTopCategoryVO;
import com.fsll.app.investor.market.vo.AppNoticeItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.BbsTopic;
import com.fsll.wmes.entity.BbsTopicCount;
import com.fsll.wmes.entity.BbsType;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaReturn;
import com.fsll.wmes.entity.StrategyInfo;

/**
 * 首页接口服务现实
 * @author zpzhou
 * @date 2016-10-31
 */
@Service("appIndexService")
//@Transactional
public class AppIndexServiceImpl extends BaseService implements AppIndexService {
	@Autowired
	private AppSysParamService sysParamService ;
	/**
	 * 得到首页组合中用到最多的基金的行情图表数据
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>AppIndexHotVO	基金数据
	 */
	public List<AppIndexHotVO> findIndexHotFund(String langCode,String periodCode,int num){
		List<AppIndexHotVO> fundList=new ArrayList<AppIndexHotVO>();
		String hql = "select f.id,s.fundName,f.riskLevel,r.increase,count(*) as num ";
		hql += " from ProductInfo p";
		hql += " right join PortfolioArenaProduct t on t.product.id=p.id ";
		hql += " left join FundInfo f on f.product.id=p.id ";
		hql += " left join "+this.getLangString("FundInfo", langCode)+" s on s.id=f.id ";
		hql += " left join FundReturn r on r.fund.id=f.id and r.periodCode=? ";
		hql += " where p.type=? and p.isValid=? GROUP BY t.product.id order by num desc ";
		List params = new ArrayList();
		params.add(periodCode);
		params.add("fund");
		params.add("1");
		List list = baseDao.find(hql, params.toArray(),0,num, false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexHotVO  vo = new AppIndexHotVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			vo.setIncrease(objs[3]==null?"":objs[3].toString());
			vo.setNum(objs[4]==null?"":objs[4].toString());
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			fundList.add(vo);
		}
		return fundList;
	}
	
	/**
	 * 得到首页热门投资中收益最多的组合
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @param num 返回条数
	 * @return	<List>AppIndexHotVO	组合数据
	 */
	public List<AppIndexHotVO> findIndexHotPortfolio(String memberId,String langCode,String periodCode,int num){
		List<AppIndexHotVO> portfolioList=new ArrayList<AppIndexHotVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id,f.portfolioName,f.riskLevel,r.increase,m.id,m.iconUrl ");
		hql.append(" from PortfolioArena f ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=f.id and r.periodCode=? ");
		hql.append(" left join f.creator m ");
		hql.append(" where f.id in (select v.relateId from WebView v where  v.moduleType=? ");
		hql.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
		
		hql.append(" order by f.totalReturn desc ");
		List params = new ArrayList();
		params.add(periodCode);
		params.add("portfolio_arena");
		params.add(memberId);
		
		List list = baseDao.find(hql.toString(), params.toArray(),0,num, false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexHotVO  vo = new AppIndexHotVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			vo.setIncrease(objs[3]==null?"":objs[3].toString());
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			String creatorId = objs[4]==null?"":objs[4].toString();
			String iconUrl = objs[5]==null?"":objs[5].toString();
			vo.setIfaName(getCommonMemberName(creatorId, langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl,null));
			portfolioList.add(vo);
		}
		return portfolioList;
	}
	
	/**
	 * 得到首页不容错过信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 语言
	 * @return
	 */
	public List<AppIndexNoMissVo> findIndexNoMiss(String memberId,String langCode,String periodCode) {
		List<AppIndexNoMissVo> noMissList = new ArrayList<AppIndexNoMissVo>();

		AppIndexNoMissVo noMissStrategy = findIndexNoMissStrategy(memberId, langCode);
		AppIndexNoMissVo noMissPortfolio = findIndexNoMissPortfolio(memberId, periodCode,langCode);
		AppIndexNoMissVo noMissFund = findIndexNoMissFund(memberId, langCode, periodCode);
		
		noMissList.add(noMissStrategy);
		noMissList.add(noMissPortfolio);
		noMissList.add(noMissFund);
		
		return noMissList;
	}
	
	
	/**
	 * 得到首页不容错过信息
	 * @param langCode 语言
	 * @return
	 */
	private AppIndexNoMissVo findIndexNoMissStrategy(String memberId,String langCode) {
		AppIndexNoMissVo vo = new AppIndexNoMissVo();
		//IFA推荐的
		StringBuilder hqlStrategy = new StringBuilder();
		hqlStrategy.append(" from StrategyInfo s ");
		hqlStrategy.append(" inner join WebView v on v.relateId=s.id ");
		hqlStrategy.append(" where s.isValid='1' and v.moduleType=? ");
		hqlStrategy.append(" and ( v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?))) ");
		hqlStrategy.append(" order by v.createTime desc");

		List paramsStrategy = new ArrayList();
		paramsStrategy.add("strategy");
		paramsStrategy.add(memberId);
		List listStrategy = baseDao.find(hqlStrategy.toString(), paramsStrategy.toArray(),1,2, false);
		
//		if(null==listStrategy || listStrategy.isEmpty()){
//			//访问过的
//			hqlStrategy = new StringBuilder();
//			hqlStrategy.append(" from StrategyInfo s where s.isValid='1' ");
//			hqlStrategy.append(" and s.id in (select v.relateId from WebView v where  v.moduleType=? ");
//			hqlStrategy.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
//			hqlStrategy.append(" and exists (select r.id from WebInvestorVisit r where r.relateId=s.id and r.member.id=? ) ");
//			hqlStrategy.append(" order by s.overheadTime desc");
//			paramsStrategy = new ArrayList();
//			paramsStrategy.add("strategy");
//			paramsStrategy.add(memberId);
//			paramsStrategy.add(memberId);
//			listStrategy = baseDao.find(hqlStrategy.toString(), paramsStrategy.toArray(), false);			
//		}
//		
//		if(null==listStrategy || listStrategy.isEmpty()){
//			//有权限访问的
//			hqlStrategy = new StringBuilder();
//			hqlStrategy.append(" from StrategyInfo s where s.isValid='1' ");
//			hqlStrategy.append(" and s.id in (select v.relateId from WebView v where  v.moduleType=? ");
//			hqlStrategy.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
//			hqlStrategy.append(" order by s.overheadTime desc");
//			paramsStrategy = new ArrayList();
//			paramsStrategy.add("strategy");
//			paramsStrategy.add(memberId);
//			listStrategy = baseDao.find(hqlStrategy.toString(), paramsStrategy.toArray(), false);			
//		}
		
		if(null!=listStrategy && !listStrategy.isEmpty()){
			Object[] objs = (Object[]) listStrategy.get(0);
			StrategyInfo info=(StrategyInfo)objs[0];
			vo.setId(info.getId());
			vo.setName(info.getStrategyName());
			vo.setReason(info.getReason());
			vo.setRiskLevel(info.getRiskLevel());
			vo.setIfaName(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(),null));
			vo.setType("1");
			String classify="";
			if(null!=info.getSector() && !"".equals(info.getSector())){
				String [] sectorArray=info.getSector().split(",");
				for(String code:sectorArray){
					AppSysParamVO paramVO=sysParamService.findParamByCode(langCode,code);
					classify+=paramVO.getName()+"/";
				}
				if(!"".equals(classify)){
					classify=classify.substring(0, classify.length()-1);
				}
			}
			vo.setClassify(classify);
		}
		
		return vo;		
	}
	
	/**
	 * 得到首页不容错过信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 语言
	 * @return
	 */
	private AppIndexNoMissVo findIndexNoMissPortfolio(String memberId,String periodCode,String langCode) {
		AppIndexNoMissVo vo = new AppIndexNoMissVo();
		//IFA推荐的
		StringBuilder hqlPortfolio = new StringBuilder();	
		hqlPortfolio.append(" from PortfolioArena s ");
		hqlPortfolio.append(" left join PortfolioArenaReturn r on r.portfolio.id=s.id and r.periodCode=? ");
		hqlPortfolio.append(" inner join WebView v on v.relateId=s.id ");
		hqlPortfolio.append(" where s.isValid='1' and v.moduleType=? ");
		hqlPortfolio.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?))) ");
		hqlPortfolio.append(" order by v.createTime desc ");
		
		
		
		List paramsPortfolio = new ArrayList();
		paramsPortfolio.add(periodCode);
		paramsPortfolio.add("portfolio_arena");
		paramsPortfolio.add(memberId);
		List listPortfolio = baseDao.find(hqlPortfolio.toString(), paramsPortfolio.toArray(), false);
		
//		if(null==listPortfolio || listPortfolio.isEmpty()){
//			//访问过的
//			hqlPortfolio = new StringBuilder();
//			hqlPortfolio.append("select s.id,s.portfolioName,s.reason,s.riskLevel,m.id,r.increase,m.iconUrl from PortfolioArena s ");
//			hqlPortfolio.append(" left join PortfolioArenaReturn r on r.portfolio.id=s.id and r.periodCode=? ");
//			hqlPortfolio.append(" left join s.creator m ");
//			hqlPortfolio.append(" where s.isValid='1' ");
//			hqlPortfolio.append(" and s.id in (select v.relateId from WebView v where  v.moduleType=? ");
//			hqlPortfolio.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
//			hqlPortfolio.append(" order by s.overheadTime desc");
//			paramsPortfolio = new ArrayList();
//			paramsPortfolio.add(periodCode);
//			paramsPortfolio.add("portfolio_arena");
//			paramsPortfolio.add(memberId);
//			listPortfolio = baseDao.find(hqlPortfolio.toString(), paramsPortfolio.toArray(), false);			
//		}
//		
//		if(null==listPortfolio || listPortfolio.isEmpty()){
//			//有权限访问的
//			hqlPortfolio = new StringBuilder();
//			hqlPortfolio.append("select s.id,s.portfolioName,s.reason,s.riskLevel,m.id,r.increase,m.iconUrl from PortfolioArena s ");
//			hqlPortfolio.append(" left join PortfolioArenaReturn r on r.portfolio.id=s.id and r.periodCode=? ");
//			hqlPortfolio.append(" left join s.creator m ");
//			hqlPortfolio.append(" where s.isValid='1' ");
//			hqlPortfolio.append(" and s.id in (select v.relateId from WebView v where  v.moduleType=? ");
//			hqlPortfolio.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
//			hqlPortfolio.append(" and exists (select r.id from WebInvestorVisit r where r.relateId=s.id and r.member.id=? ) ");
//			hqlPortfolio.append(" order by s.overheadTime desc");
//			paramsPortfolio = new ArrayList();
//			paramsPortfolio.add(periodCode);
//			paramsPortfolio.add("portfolio_arena");
//			paramsPortfolio.add(memberId);
//			paramsPortfolio.add(memberId);
//			listPortfolio = baseDao.find(hqlPortfolio.toString(), paramsPortfolio.toArray(), false);			
//		}
		
		if(null!=listPortfolio && !listPortfolio.isEmpty()){
			Object[] objs = (Object[])listPortfolio.get(0);
			PortfolioArena arena = (PortfolioArena) objs[0];
			MemberBase member = arena.getCreator();
			
			vo.setId(arena.getId());
			vo.setName(arena.getPortfolioName());
			vo.setReason(arena.getReason());
			vo.setRiskLevel(String.valueOf(arena.getRiskLevel()));
			
			if(null != member){
				vo.setIfaName(getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null));
			}
			vo.setType("2");
			if(null==objs[1]){
				vo.setReturnRate(getFormatNumByPer(null));
			}else{
				PortfolioArenaReturn paReturn = (PortfolioArenaReturn) objs[1]; 
				vo.setReturnRate(getFormatNumByPer(paReturn.getIncrease()));
			}
		}
		
		return vo;
	}
	
	/**
	 * 得到首页不容错过信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 语言
	 * @return
	 */
	private AppIndexNoMissVo findIndexNoMissFund(String memberId,String langCode,String periodCode) {
		AppIndexNoMissVo vo = new AppIndexNoMissVo();
		//IFA推荐的
		StringBuilder hqlFund = new StringBuilder();
		hqlFund.append(" from FundInfo s ");
		hqlFund.append(" inner join " +this.getLangString("FundInfo", langCode)+" l on l.id=s.id ");
		hqlFund.append(" left join FundReturn r on r.fund.id=s.id and r.periodCode=? ");
		hqlFund.append(" inner join WebRecommended w on w.relateId=s.id ");
		hqlFund.append(" inner join MemberIfa i on i.member.id=w.creator.id ");
		hqlFund.append(" where s.isValid='1' ");// s.overhead='1' and
		hqlFund.append(" and exists (select ia.id from InvestorAccount ia where ia.ifa.id=i.id and ia.member.id=? ) ");
		hqlFund.append(" order by w.overheadTime desc,w.recommendTime desc");
				
		List paramsFund = new ArrayList();
		paramsFund.add(periodCode);
		paramsFund.add(memberId);
		List listFund = baseDao.find(hqlFund.toString(), paramsFund.toArray(), false);
		
		if(null == listFund || listFund.isEmpty()){
			//访问过的
			hqlFund = new StringBuilder();
			hqlFund.append(" from FundInfo s ");
			hqlFund.append(" inner join " +this.getLangString("FundInfo", langCode)+" a on s.id=a.id ");
			hqlFund.append(" left join FundReturn r on r.fund.id=s.id and r.periodCode=? ");
			hqlFund.append(" inner join WebInvestorVisit v on v.relateId=s.id ");
			hqlFund.append(" where s.isValid='1' and v.member.id=? ");
			hqlFund.append(" order by v.vistiTime desc");
			
			paramsFund = new ArrayList();
			paramsFund.add(periodCode);
			paramsFund.add(memberId);
			listFund = baseDao.find(hqlFund.toString(), paramsFund.toArray(), false);
		}
		
		if(null == listFund || listFund.isEmpty()){
			//有权限访问的
			hqlFund = new StringBuilder();
			hqlFund.append(" from FundInfo s ");
			hqlFund.append(" inner join " +this.getLangString("FundInfo", langCode)+" a on s.id=a.id ");
			hqlFund.append(" left join FundReturn r on r.fund.id=s.id and r.periodCode=? ");
			hqlFund.append(" where s.isValid='1' ");// s.overhead='1' and
			
			hqlFund.append(" and exists( SELECT pc.id FROM ProductCompany pc ");
			hqlFund.append(" INNER JOIN MemberCompany mc ON mc.company.id=pc.company.id ");
			hqlFund.append(" WHERE pc.product.id=s.product.id AND mc.member.id=? )");
		
			hqlFund.append(" order by s.lastUpdate desc");
			
			paramsFund = new ArrayList();
			paramsFund.add(periodCode);
			paramsFund.add(memberId);
			listFund = baseDao.find(hqlFund.toString(), paramsFund.toArray(), false);
		}
		
		if(null!=listFund && !listFund.isEmpty()){
			Object[] objs = (Object[])listFund.get(0);
			FundInfo info = (FundInfo) objs[0];
			FundInfoEn infoEn = new FundInfoEn();
			BeanUtils.copyProperties(objs[1], infoEn);
			
			vo.setId(info.getId());
			vo.setName(infoEn.getFundName());
			vo.setReason("");
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
			vo.setIfaName("");
			vo.setIconUrl("");
			vo.setType("3");
			
			if(null==objs[2]){
				vo.setReturnRate(getFormatNumByPer(null));
			}else{
				FundReturn fundReturn = (FundReturn) objs[2];
				vo.setReturnRate(getFormatNumByPer(fundReturn.getIncrease()));
			}
		}
		return vo;
	}
	
	
	/**
	 * 你的选择
	 * @param langCode  语言
	 * @return
	 */
	public List<AppIndexTopCategoryVO> findIndexTopCategory(String langCode){
		List<AppIndexTopCategoryVO> topList=new ArrayList<AppIndexTopCategoryVO>();
		String hql = "select t.id,t.name,t.iconUrl from TopCategory t order by t.orderBy desc";
		List list = baseDao.find(hql, null,false);
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexTopCategoryVO  vo = new AppIndexTopCategoryVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setIconUrl(objs[2]==null?"":objs[2].toString());
			topList.add(vo);
    	}
		return topList;
	}
	
	/**
	 * 得到首页热门话题列表信息
	 * @param typeId 类型ID  0:最热门  1：推荐  2:模块
	 * @param moduleId 模块ID  当typeId=2时不能为空
	 * @param num 返回条数
	 * @return
	 */
	public List<AppIndexBbsTopicVO> findIndexTopicList(String typeId,String moduleId,int num){
		List<AppIndexBbsTopicVO> topicList=new ArrayList<AppIndexBbsTopicVO>();
		List params = new ArrayList();
		String hql = " from BbsTopic b ";
		hql += " left join b.type t  ";
		hql += " left join b.author m  ";
		hql += " left join BbsTopicCount c on c.id=b.id  ";
		if("0".equals(typeId)){//最热门
			hql += " where b.isValid=? ";
			hql += " order by b.replyCount desc ";
			params.add("1");
		}else if("1".equals(typeId)){//推荐
			hql += " where b.isValid=? and b.isRecommend=?";
			hql += " order by b.orderBy desc";
			params.add("1");
			params.add("1");
		}else{//按模块
			hql += " where b.isValid=? and b.type.id=?";
			hql += " order by b.orderBy desc";
			params.add("1");
			params.add(moduleId);
		}
		List list = baseDao.find(hql, params.toArray(), 0, num, false);
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			BbsTopic info=(BbsTopic)objs[0];
			BbsType type=(BbsType)objs[1];
			MemberBase author=(MemberBase)objs[2];
			BbsTopicCount count=(BbsTopicCount)objs[3];
			AppIndexBbsTopicVO vo=new AppIndexBbsTopicVO();
			vo.setId(info.getId());
			vo.setContent(info.getContent());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setIsRecommend(info.getIsRecommend());
			vo.setIsTop(info.getIsTop());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setReplyCount(info.getReplyCount());
			vo.setTopic(info.getTopic());
			if(null!=type)vo.setTypeName(type.getName());
			if(null!=author){
				vo.setAuthorName(author.getNickName());
				vo.setIconUrl(author.getIconUrl());
			}
			if(null!=count)vo.setViews(count.getViews());
			topicList.add(vo);
    	}
		return topicList;
	}
	
	/**
	 * 获取公告列表
	 * @author zxtan
	 * @date 2017-04-07
	 * @param memberId
	 * @return
	 */
	public List<AppNoticeItemVO> findNoticeList(String memberId,int num){
		List<AppNoticeItemVO> voList = new ArrayList<AppNoticeItemVO>();
		StringBuilder hql = new StringBuilder();
//		hql.append("select t.id,t.subject,DATE_FORMAT(t.releaseDate,'%Y-%m-%d %H:%i:%s') from Notice t ");
		hql.append(" from Notice t ");
		hql.append(" where t.isValid='1' ");
		hql.append(" and ( t.target='all' ");
		hql.append(" 	or (t.target='I2' and t.sourceType='ifafirm' ");
		hql.append("		and exists (select r.id from MemberIfaIfafirm r inner join MemberIfa i on i.id=r.ifa.id inner join InvestorAccount a on a.ifa.id=i.id where r.ifafirm.id=t.source and a.member.id=? ) )");
		hql.append(" 	or (t.target='D2' and t.sourceType='distributor' ");
		hql.append("		and exists (select d.id from IfafirmDistributor d inner join InvestorAccount a on a.distributor.id=d.distributor.id where d.distributor.id=t.source and a.member.id=? ) )");
		hql.append(" )");
		hql.append(" order by t.releaseDate desc ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(memberId);
		
		List list = baseDao.find(hql.toString(), params.toArray(),0,num, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppNoticeItemVO vo = new AppNoticeItemVO();
//				Object[] objs = (Object[]) list.get(i);
				Notice notice = (Notice) list.get(i);
				vo.setId(notice.getId());
				vo.setSubject(notice.getSubject());
				vo.setReleaseDate(DateUtil.dateToDateString(notice.getReleaseDate(), CommonConstants.FORMAT_DATE_TIME));

//				Object[] objs = (Object[]) list.get(i);
//				vo.setId(objs[0].toString());
//				vo.setSubject(objs[1].toString());
//				vo.setReleaseDate(objs[2].toString());
				voList.add(vo);
			}
		}
		return voList;
	}
}
