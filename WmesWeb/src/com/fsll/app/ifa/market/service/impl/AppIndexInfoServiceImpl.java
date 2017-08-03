package com.fsll.app.ifa.market.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.app.ifa.market.service.AppIndexInfoService;
import com.fsll.app.ifa.market.vo.AppFollowArenaItemVO;
import com.fsll.app.ifa.market.vo.AppHotTopicItemVO;
import com.fsll.app.ifa.market.vo.AppNoticeItemVO;
import com.fsll.app.ifa.market.vo.AppStrategyInfoItemVO;
import com.fsll.app.ifa.market.vo.AppWatchFundItemVO;
import com.fsll.app.ifa.market.vo.AppWatchTypeVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.CommunitySection;
import com.fsll.wmes.entity.CommunityTopic;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.FundReturnSc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.MemberIndividual;
import com.fsll.wmes.entity.Notice;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaReturn;
import com.fsll.wmes.entity.WebWatchlistType;
import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

/**
 * IFA Market首页接口实现
 * @author zxtan
 * @date 2017-03-14
 *
 */
@Service("appIfaMarketIndexService")
public class AppIndexInfoServiceImpl extends BaseService implements
		AppIndexInfoService {

	/**
	 * 获取IFA 的自选分类及基金
	 * @param memberId Ifa member Id
	 * @param periodCode 周期
	 * @param langCode 语言
	 * @return
	 */
	public List<AppWatchTypeVO> findMyWatchTypeList(String memberId,String periodCode,String langCode) {
		List<AppWatchTypeVO> voList = new ArrayList<AppWatchTypeVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from WebWatchlistType t ");
		hql.append(" where t.isValid='1' and t.member.id=? ");
		hql.append(" and exists ( select s.id from WebWatchlist s where s.webWatchlistType.id=t.id )");
		hql.append(" order by t.orderBy ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List<WebWatchlistType> list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppWatchTypeVO vo = new AppWatchTypeVO();
				WebWatchlistType typeInfo = list.get(i);
				String typeId = typeInfo.getId();
				vo.setTypeId(typeId);
				vo.setTypeName(typeInfo.getName());
				List<AppWatchFundItemVO> fundList = findWatchFundList(memberId, typeId, periodCode, langCode);
				vo.setFundList(fundList);
				voList.add(vo);
			}
		}
		return voList;
	}
		
	
	/**
	 * 获取IFA 的自选分类基金
	 * @param memberId
	 * @param typeId
	 * @param periodCode
	 * @param langCode
	 * @return
	 */
	private List<AppWatchFundItemVO> findWatchFundList(String memberId,String typeId,String periodCode,String langCode){
		List<AppWatchFundItemVO> voList = new ArrayList<AppWatchFundItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from WebWatchlist m ");
		hql.append(" inner join FundInfo f on m.product.id=f.product.id ");
		hql.append(" inner join "+getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
		hql.append(" left join FundReturn r on r.fund.id=f.id and r.periodCode=?");
		hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
		hql.append(" where m.isValid='1' and m.member.id=? and m.webWatchlistType.id=? ");
		hql.append(" order by m.orderBy,m.createTime desc ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		params.add(typeId);
		List list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppWatchFundItemVO vo = new AppWatchFundItemVO();
				Object[] objs = (Object[]) list.get(i);
				FundInfo fundInfo = (FundInfo) objs[1];
				FundInfoSc fundInfoSc = new FundInfoSc();
				BeanUtils.copyProperties(objs[2], fundInfoSc);
				
				vo.setFundId(fundInfo.getId());
				vo.setProductId(fundInfo.getProduct().getId());
				vo.setFundName(fundInfoSc.getFundName());
				vo.setFundType(fundInfoSc.getFundType());
				vo.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));
				vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
				vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());
				
				if(null != objs[3]){
					FundReturn fundReturn = (FundReturn) objs[3];
					vo.setIncrease(getFormatNumByPer(fundReturn.getIncrease()));
				}
				if(null != objs[4]){
					FundReturnSc fundReturnSc = new FundReturnSc();
					BeanUtils.copyProperties(objs[4], fundReturnSc);
					vo.setPeriodName(fundReturnSc.getPeriodName());					
				}
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取IFA 感兴趣的基金
	 * @param memberId
	 * @param periodCode
	 * @param langCode
	 * @return
	 */
	public List<AppWatchFundItemVO> findVisitedFundList(String memberId,String periodCode,String langCode){
		List<AppWatchFundItemVO> voList = new ArrayList<AppWatchFundItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from FundInfo f ");
		hql.append(" inner join "+getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
		//时间类型:return_period_code_1W,return_period_code_1M,return_period_code_1Y...
		hql.append(" left join FundReturn r on r.fund.id=f.id  and r.periodCode=?");
		hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
		hql.append(" left join WebInvestorVisit v on f.id = v.relateId ");
		hql.append(" where v.moduleType='fund' and v.member.id=? ");
		hql.append(" order by v.vistiTime desc, r.increase desc ");
		
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
		
		if( null == list || list.isEmpty()){
			
			hql = new StringBuilder();
			params = new ArrayList();
			hql.append(" from FundInfo f ");
			hql.append(" inner join "+getLangString("FundInfo", langCode)+" fl on fl.id=f.id ");
			hql.append(" left join FundReturn r on r.fund.id=f.id ");	
			hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
			hql.append(" where r.periodCode=?  ");
			hql.append(" order by r.increase desc ");

			params.add(periodCode);
			
			list = this.baseDao.find(hql.toString(), params.toArray(), 0, 2, false);
			
		}
				
		
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppWatchFundItemVO vo = new AppWatchFundItemVO();
				Object[] objs = (Object[]) list.get(i);
				FundInfo fundInfo = (FundInfo) objs[0];
				FundInfoSc fundInfoSc = new FundInfoSc();
				BeanUtils.copyProperties(objs[1], fundInfoSc);
				
				vo.setFundId(fundInfo.getId());
				vo.setProductId(fundInfo.getProduct().getId());
				vo.setFundName(fundInfoSc.getFundName());
				vo.setFundType(fundInfoSc.getFundType());
				vo.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));
				vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
				vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());
				
				if(null != objs[2]){
					FundReturn fundReturn = (FundReturn) objs[2];
					vo.setIncrease(getFormatNumByPer(fundReturn.getIncrease()));
				}
				if(null != objs[3]){
					FundReturnSc fundReturnSc = new FundReturnSc();
					BeanUtils.copyProperties(objs[3], fundReturnSc);
					vo.setPeriodName(fundReturnSc.getPeriodName());					
				}
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取IFA 的自选组合列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppFollowArenaItemVO> findMyFollowPortfolioList(String memberId,String periodCode,String langCode){
		List<AppFollowArenaItemVO> voList = new ArrayList<AppFollowArenaItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArena m ");
		hql.append(" inner join MemberBase b on b.id = m.creator.id ");
		hql.append(" inner join MemberIfa i on i.member.id = b.id ");
		hql.append(" inner join WebFollow t on t.relateId = m.id ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=m.id and r.periodCode=? ");
		hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
		hql.append(" where t.member.id=? ");
		hql.append(" order by t.createTime desc ");
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppFollowArenaItemVO vo = new AppFollowArenaItemVO();
				Object[] objs = (Object[]) list.get(i);
				PortfolioArena arena = (PortfolioArena) objs[0];
				MemberBase creator = (MemberBase) objs[1];
				MemberIfa ifa = (MemberIfa) objs[2];
				
				
				vo.setArenaId(arena.getId());
				vo.setPortfolioName(arena.getPortfolioName());
				vo.setRiskLevel(String.valueOf(arena.getRiskLevel()));
				vo.setCreatorId(creator.getId());
				
				String userName = getCommonMemberName(ifa.getMember().getId(),langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setCreatorName(userName);				
				
				String gender = ifa.getGender();
				String iconUrl = creator.getIconUrl();
				iconUrl = PageHelper.getUserHeadUrlForWS(iconUrl, gender);
				vo.setCreatorIconUrl(iconUrl);
				
				if(null != objs[4]){
					PortfolioArenaReturn arenaReturn = (PortfolioArenaReturn) objs[4];
					vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
				}
				if(null != objs[5]){
					FundReturnSc fundReturnSc = new FundReturnSc();
					BeanUtils.copyProperties(objs[5], fundReturnSc);
					vo.setPeriodName(fundReturnSc.getPeriodName());					
				}
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取IFA 感兴趣的组合列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppFollowArenaItemVO> findVisitedPortfolioList(String memberId,String periodCode,String langCode){
		List<AppFollowArenaItemVO> voList = new ArrayList<AppFollowArenaItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArena m ");
		hql.append(" inner join MemberBase b on b.id = m.creator.id ");
		hql.append(" inner join MemberIfa i on i.member.id = b.id ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=m.id and r.periodCode=? ");
		hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
		hql.append(" inner join WebInvestorVisit v on v.relateId=m.id ");
		hql.append(" where v.moduleType='portfolio_arena' and v.member.id=? ");
		hql.append(" order by v.vistiTime desc, r.increase desc ");
						
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
		
		if( null == list || list.isEmpty()){			
			hql = new StringBuilder();
			params = new ArrayList();
			hql.append(" from PortfolioArena m ");
			hql.append(" inner join MemberBase b on b.id = m.creator.id ");
			hql.append(" inner join MemberIfa i on i.member.id = b.id ");
			hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=m.id and r.periodCode=? ");
			hql.append(" left join "+getLangString("FundReturn", langCode)+" rl on rl.periodCode=r.periodCode ");
			hql.append(" inner join WebView v on v.relateId=m.id ");
			hql.append(" where v.scopeFlag='2' ");
			hql.append(" order by r.increase desc ");

			params.add(periodCode);
			
			list = this.baseDao.find(hql.toString(), params.toArray(), 0, 2, false);
			
		}
		
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppFollowArenaItemVO vo = new AppFollowArenaItemVO();
				Object[] objs = (Object[]) list.get(i);
				PortfolioArena arena = (PortfolioArena) objs[0];
				MemberBase creator = (MemberBase) objs[1];
				MemberIfa ifa = (MemberIfa) objs[2];
				
				
				vo.setArenaId(arena.getId());
				vo.setPortfolioName(arena.getPortfolioName());
				vo.setRiskLevel(String.valueOf(arena.getRiskLevel()));
				vo.setCreatorId(creator.getId());
				
				String userName = getCommonMemberName(ifa.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setCreatorName(userName);				
				
				String gender = ifa.getGender();
				String iconUrl = creator.getIconUrl();
				iconUrl = PageHelper.getUserHeadUrlForWS(iconUrl, gender);
				vo.setCreatorIconUrl(iconUrl);
				
				if(null != objs[3]){
					PortfolioArenaReturn arenaReturn = (PortfolioArenaReturn) objs[3];
					vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
				}
				if(null != objs[4]){
					FundReturnSc fundReturnSc = new FundReturnSc();
					BeanUtils.copyProperties(objs[4], fundReturnSc);
					vo.setPeriodName(fundReturnSc.getPeriodName());					
				}
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取IFA 的自选策略列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppStrategyInfoItemVO> findMyFollowStrategyList(String memberId,String langCode){
		List<AppStrategyInfoItemVO> voList = new ArrayList<AppStrategyInfoItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("select m.id,m.strategyName,m.riskLevel,fn_getconfigname(m.geoAllocation,?),fn_getconfigname(m.sector,?), ");
		hql.append(" i.firstName,i.lastName,i.nameChn,i.gender,b.iconUrl,b.id ");
		hql.append(" from StrategyInfo m ");
		hql.append(" inner join MemberBase b on b.id = m.creator.id ");
		hql.append(" inner join MemberIfa i on i.member.id = b.id ");
		hql.append(" inner join WebFollow t on t.relateId = m.id ");
		hql.append(" where t.member.id=? ");
		hql.append(" order by t.createTime desc ");
		List<Object> params = new ArrayList<Object>();
		params.add(langCode);
		params.add(langCode);
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppStrategyInfoItemVO vo = new AppStrategyInfoItemVO();
				Object[] objs = (Object[]) list.get(i);
				vo.setStrategyId(String.valueOf(objs[0]));
				vo.setStrategyName(String.valueOf(objs[1]));
				vo.setRiskLevel(String.valueOf(objs[2]));
				vo.setGeoAllocation(String.valueOf(objs[3]));
				vo.setSector(String.valueOf(objs[4]));
				String userName = getCommonMemberName(String.valueOf(objs[10]), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setCreatorName(userName);
				String gender = objs[8] == null?"":objs[8].toString();
				String iconUrl = objs[9] == null?"":objs[9].toString();
				iconUrl = PageHelper.getUserHeadUrlForWS(iconUrl, gender);
				vo.setCreatorIconUrl(iconUrl);
				vo.setCreatorId(String.valueOf(objs[10]));
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取IFA 感兴趣的策略列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppStrategyInfoItemVO> findVisitedStrategyList(String memberId,String langCode){
		List<AppStrategyInfoItemVO> voList = new ArrayList<AppStrategyInfoItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("select m.id,m.strategyName,m.riskLevel,fn_getconfigname(m.geoAllocation,?),fn_getconfigname(m.sector,?), ");
		hql.append(" i.firstName,i.lastName,i.nameChn,i.gender,b.iconUrl,b.id ");
		hql.append(" from StrategyInfo m ");
		hql.append(" inner join MemberBase b on b.id = m.creator.id ");
		hql.append(" inner join MemberIfa i on i.member.id = b.id ");
		hql.append(" left join WebInvestorVisit v on v.relateId=m.id ");
		hql.append(" where v.moduleType='strategy' and v.member.id=? ");
		hql.append(" order by v.vistiTime desc, m.createTime desc ");
		List<Object> params = new ArrayList<Object>();
		params.add(langCode);
		params.add(langCode);
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(),0,2, false);
		
		if(null == list || list.isEmpty()){			
			params = new ArrayList();
			hql = new StringBuilder();
			hql.append("select m.id,m.strategyName,m.riskLevel,fn_getconfigname(m.geoAllocation,?),fn_getconfigname(m.sector,?), ");
			hql.append(" i.firstName,i.lastName,i.nameChn,i.gender,b.iconUrl,b.id ");
			hql.append(" from StrategyInfo m ");
			hql.append(" inner join MemberBase b on b.id = m.creator.id ");
			hql.append(" inner join MemberIfa i on i.member.id = b.id ");
			hql.append(" inner join WebView v on v.relateId=m.id ");
			hql.append(" where m.status='1' and v.scopeFlag='2' ");
			hql.append(" order by m.createTime desc ");
			params.add(langCode);
			params.add(langCode);		
			list = this.baseDao.find(hql.toString(), params.toArray(), 0, 2, false);			
		}
		
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppStrategyInfoItemVO vo = new AppStrategyInfoItemVO();
				Object[] objs = (Object[]) list.get(i);
				vo.setStrategyId(String.valueOf(objs[0]));
				vo.setStrategyName(String.valueOf(objs[1]));
				vo.setRiskLevel(String.valueOf(objs[2]));
				vo.setGeoAllocation(String.valueOf(objs[3]));
				vo.setSector(String.valueOf(objs[4]));
				String userName = getCommonMemberName(String.valueOf(objs[10]), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
				vo.setCreatorName(userName);
				String gender = objs[8] == null?"":objs[8].toString();
				String iconUrl = objs[9] == null?"":objs[9].toString();
				iconUrl = PageHelper.getUserHeadUrlForWS(iconUrl, gender);
				vo.setCreatorIconUrl(iconUrl);
				vo.setCreatorId(String.valueOf(objs[10]));
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 获取热门的话题列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppHotTopicItemVO> findHotTopicList(String memberId,String langCode){
		List<AppHotTopicItemVO> voList = new ArrayList<AppHotTopicItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CommunityTopic t ");
		hql.append(" inner join CommunitySection s on s.id = t.section.id ");
		hql.append(" inner join MemberBase b on b.id = t.creator.id ");
		hql.append(" left join MemberIfa i on i.member.id = b.id ");
		hql.append(" left join MemberIndividual d on d.member.id = b.id ");
		//TODO:增加过滤条件
		hql.append(" order by t.createTime desc ");
		List list = baseDao.find(hql.toString(), null,0,5, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppHotTopicItemVO vo = new AppHotTopicItemVO();
				Object[] objs = (Object[]) list.get(i);
				CommunityTopic topic = (CommunityTopic) objs[0];
				CommunitySection section = (CommunitySection) objs[1];
				MemberBase creator = (MemberBase) objs[2];
								
				vo.setTopicId(topic.getId());
				vo.setTopicTitle(topic.getTitle());
				if("en".equalsIgnoreCase(langCode)){
					vo.setSection(section.getSectionNameEn());
				}else if ("sc".equalsIgnoreCase(langCode)) {
					vo.setSection(section.getSectionNameSc());
				}else if ("tc".equalsIgnoreCase(langCode)) {
					vo.setSection(section.getSectionNameTc());
				}
				
				vo.setCreatorId(creator.getId());
				String userName,gender;
				userName = getCommonMemberName(creator.getId(), langCode,CommonConstants.MEMBER_NAME_NICKNAME);
				if(creator.getMemberType()==2 && creator.getSubMemberType()==21){
					vo.setIsIfa("1");
					MemberIfa ifa = (MemberIfa) objs[3];
					gender = ifa.getGender()==null?"":ifa.getGender();
				}else {
					vo.setIsIfa("0");
					MemberIndividual individual = (MemberIndividual) objs[4];
					gender = individual.getGender()==null?"":individual.getGender();
				}

				vo.setCreatorName(userName);
				
				String iconUrl = creator.getIconUrl();
				iconUrl = PageHelper.getUserHeadUrlForWS(iconUrl, gender);
				vo.setCreatorIconUrl(iconUrl);
				vo.setCreateTime(DateUtil.dateToDateString(topic.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**
	 * 获取公告列表
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	public List<AppNoticeItemVO> findNoticeList(String memberId){
		List<AppNoticeItemVO> voList = new ArrayList<AppNoticeItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from Notice t ");
		hql.append(" where t.isValid='1' ");
		hql.append(" and ( t.target='all' ");
		hql.append(" 	or (t.target='I1' and t.sourceType='ifafirm' ");
		hql.append("		and exists (select r.id from MemberIfaIfafirm r inner join MemberIfa i on i.id=r.ifa.id where r.ifafirm.id=t.source and i.member.id=? ) )");
		hql.append(" 	or (t.target='D1' and t.sourceType='distributor' ");
		hql.append("		and exists (select d.id from IfafirmDistributor d inner join MemberIfafirm f on f.id=d.ifafirm.id ");
		hql.append("				inner join MemberIfaIfafirm r on r.ifafirm.id=f.id inner join MemberIfa i on i.id=r.ifa.id where d.distributor.id=t.source and i.member.id=? ) )");
		hql.append(" )");
		hql.append(" order by t.releaseDate desc ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(memberId);
		
		List<Notice> list = baseDao.find(hql.toString(), params.toArray(),0,5, false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppNoticeItemVO vo = new AppNoticeItemVO();
//				Object[] objs = (Object[]) list.get(i);
				Notice notice = list.get(i);
				vo.setId(notice.getId());
				vo.setSubject(notice.getSubject());
				vo.setReleaseDate(DateUtil.dateToDateString(notice.getReleaseDate(), CommonConstants.FORMAT_DATE_TIME));
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	

}
