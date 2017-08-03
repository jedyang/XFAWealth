package com.fsll.app.investor.discover.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fsll.app.investor.discover.service.AppCornerInfoService;
import com.fsll.app.investor.discover.vo.AppCornerInfoVO;
import com.fsll.app.investor.discover.vo.AppCornerLikedVO;
import com.fsll.app.investor.discover.vo.AppCornerReplyVO;
import com.fsll.app.investor.discover.vo.AppFundInfoVO;
import com.fsll.app.investor.discover.vo.AppIfaInfoVO;
import com.fsll.app.investor.discover.vo.AppIfaPerformanceVO;
import com.fsll.app.investor.discover.vo.AppIfaRecommendedVO;
import com.fsll.app.investor.discover.vo.AppInsightInfoVO;
import com.fsll.app.investor.discover.vo.AppNewsItemVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppStrategyInfoVO;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.entity.CornerInfo;
import com.fsll.wmes.entity.CornerLiked;
import com.fsll.wmes.entity.CornerReply;
import com.fsll.wmes.entity.IfaExtInfo;
import com.fsll.wmes.entity.IfaHighlight;
import com.fsll.wmes.entity.InsightInfo;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsInfo;
import com.fsll.wmes.entity.NewsInfoCategory;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaReturn;

/**
 * 圈子信息接口现实
 * @author zxtan
 * @date 2016-11-21
 */
@Service("appCornerInfoService")
//@Transactional
public class AppCornerInfoServiceImpl extends BaseService implements
		AppCornerInfoService {


	/**
	 * 获取投资者的IFA圈子主题列表
	 * @param jsonPaging 分页数据
	 * @param memberId 投资者Id
	 * @param ifaMemberId IfaId
	 * @return
	 */
	public JsonPaging findCornerInfoList(JsonPaging jsonPaging, String memberId,String ifaMemberId) {
		// TODO Auto-generated method stub
		List<AppCornerInfoVO> voList = new ArrayList<AppCornerInfoVO>();
		StringBuilder hql = new StringBuilder(" from CornerInfo i ");
		hql.append(" LEFT JOIN WebFriend f ON i.author.id = f.toMember.id ");
		hql.append(" WHERE i.isValid=1 and f.checkResult='1' and f.fromMember.id=? ");

		List params = new ArrayList();
		params.add(memberId);
		
		if(null != ifaMemberId && !"".equals(ifaMemberId)){
			hql.append(" and i.author.id=? ");
			params.add(ifaMemberId);
		}
		hql.append(" ORDER BY i.createTime desc");
		
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		if(list != null && !list.isEmpty())
		{
			for(int i=0;i<list.size();i++)
			{
				Object[] objs = (Object[])list.get(i);
				CornerInfo info = (CornerInfo) objs[0];
				AppCornerInfoVO vo = new AppCornerInfoVO();
				vo.setId(info.getId());
				vo.setAuthor(info.getAuthor().getNickName());
				vo.setAuthorIconUrl(info.getAuthor().getIconUrl());
				vo.setEmotionIcon(info.getEmotionIcon());
				if(null != info.getReplyCount()){
					vo.setReplyCount(info.getReplyCount().toString());
				}else {
					vo.setReplyCount("0");
				}
				
				//分析点赞数据
				long likedCount = this.findCornerLikedCount(info.getId());
				vo.setLikedCount(Long.toString(likedCount));//点赞数量
								
				vo.setTopic(info.getTopic());
				vo.setContent(info.getContent());
				vo.setIsTransfer(info.getIsTransfer());
				vo.setIconUrl(info.getIconUrl());
				vo.setUrl(info.getUrl());
				vo.setCreateTime(DateUtil.dateToDateString( info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setLastUpdate(DateUtil.dateToDateString( info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
				
				voList.add(vo);
			}
			jsonPaging.setList(voList);
		}
		return jsonPaging;
	}


	/**
	 * 圈子主题信息
	 * @param cornerId
	 * @return
	 */
	public AppCornerInfoVO findCornerInfoById(String cornerId) {
		// TODO Auto-generated method stub
		StringBuilder hql = new StringBuilder(" from CornerInfo i ");
		hql.append(" WHERE i.isValid=1 and i.id=? ");
		
		List params = new ArrayList();
		params.add(cornerId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);

		AppCornerInfoVO vo = new AppCornerInfoVO();
		if(list != null && !list.isEmpty())
		{
			CornerInfo info = (CornerInfo)list.get(0);
			vo.setId(info.getId());
			vo.setAuthor(info.getAuthor().getNickName());
			vo.setAuthorIconUrl(info.getAuthor().getIconUrl());
			vo.setEmotionIcon(info.getEmotionIcon());
			if(null != info.getReplyCount()){
				vo.setReplyCount(info.getReplyCount().toString());
			}else {
				vo.setReplyCount("0");
			}
			
			long likedCount = this.findCornerLikedCount(cornerId);
			vo.setLikedCount(Long.toString(likedCount));//点赞数量
							
			vo.setTopic(info.getTopic());
			vo.setContent(info.getContent());
			vo.setIsTransfer(info.getIsTransfer());
			vo.setIconUrl(info.getIconUrl());
			vo.setUrl(info.getUrl());
			vo.setCreateTime(DateUtil.dateToDateString( info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString( info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			
		}
		return vo;
	}
	
	/**
	 * 获取每个主题的点赞列表数据
	 * @param cornerId
	 * @return
	 */
	public JsonPaging findCornerLikedList(JsonPaging jsonPaging, String cornerId) {
		List<AppCornerLikedVO> likedList = new ArrayList<AppCornerLikedVO>();
		List<String> params = new ArrayList<String>();
		String hql = " from CornerLiked t  ";
		hql += " where t.corner.id = ? order by t.createDate asc";
		params.add(cornerId);
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<CornerLiked> list = jsonPaging.getList(); //  this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for(CornerLiked item : list){ //循环每个主题，实体化一个IfaCornerInfoList
				AppCornerLikedVO vo = new AppCornerLikedVO();//实例化一个前端评论VO				
				vo.setId(item.getId());
				vo.setCreateDate(DateUtil.dateToDateString(item.getCreateDate(),"yyyy-MM-dd HH:mm:ss"));
				vo.setMemberId(item.getMember().getId());
				vo.setMemberIconUrl(item.getMember().getIconUrl());
				vo.setNickName(item.getMember().getNickName());
				likedList.add(vo);
			}
		}
		jsonPaging.setList(likedList);
		return jsonPaging;
	}
	

	
	/**
	 * 获取每个主题的点赞数量
	 * @param cornerId
	 * @return
	 */
	private long findCornerLikedCount(String cornerId) {
		List<String> params = new ArrayList<String>();
		String hql = "select count(*) from CornerLiked t  ";
		hql += " where t.corner.id = ? ";
		params.add(cornerId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		
		if (list.isEmpty()) {
			return 0;
		} else {
			Object o = list.get(0);
			if (o == null)
				return 0;
			if (o instanceof Integer)
				return ((Integer) o).longValue();
			else
				return ((Long) o).longValue();
		}
	}
	
	/**
	 * 获取每个主题的评论列表等
	 * @param cornerId
	 * @return
	 */
	public JsonPaging findCornerReplyList(JsonPaging jsonPaging, String cornerId) {
		List<AppCornerReplyVO> replyList = new ArrayList<AppCornerReplyVO>();
		List<String> params = new ArrayList<String>();
		String hql = " from CornerReply t  ";
		hql += " where t.corner.id = ? order by t.replayTime asc";
		params.add(cornerId);
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List<CornerReply> list = jsonPaging.getList(); //this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for(CornerReply item : list){ //循环每个主题，实体化一个IfaCornerInfoList
				AppCornerReplyVO vo = new AppCornerReplyVO();//实例化一个前端评论VO
				vo.setContent(item.getContent());
				vo.setEmotionIcon(item.getEmotionIcon());
				vo.setId(item.getId());
				vo.setReplayTime(DateUtil.dateToDateString(item.getReplayTime(),"yyyy-MM-dd HH:mm:ss"));
				vo.setMemberId(item.getReplyUser().getId());
				vo.setMemberIconUrl(item.getReplyUser().getIconUrl());
				vo.setNickName(item.getReplyUser().getNickName());
				replyList.add(vo);
			}
		}
		jsonPaging.setList(replyList);
		return jsonPaging;
	}

	/**
	 * 获取Ifa基本信息
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public AppIfaInfoVO findIfaInfo(String ifaMemberId,String langCode){
		AppIfaInfoVO ifaInfoVO = new AppIfaInfoVO();
		
		StringBuilder hql = new StringBuilder();
		hql.append("select b.id,b.nickName,f.companyName ");
//		hql.append(" ,fn_getconfigname(i.serviceRegion,'"+langCode+"') as serviceRegion,fn_getconfigname(i.expertiseType,'"+langCode+"') as expertiseType,fn_getconfigname(i.investStyle,'"+langCode+"') as investStyle ");
		hql.append(" from MemberBase b left join MemberIfa i on b.id = i.member.id and i.companyType='2' ");
		hql.append(" left join "+this.getLangString("MemberIfafirm", langCode));
		hql.append(" f on i.ifafirm.id = f.id ");
		hql.append(" where b.id = ? ");
		List<String> params = new ArrayList<String>();
		params.add(ifaMemberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[]) list.get(0);
			ifaInfoVO.setMemberId(objs[0] == null? "":objs[0].toString());
			ifaInfoVO.setNickName(objs[1] == null? "":objs[1].toString());
			ifaInfoVO.setCompanyName(objs[2] == null? "":objs[2].toString());
//			ifaInfoVO.setServiceRegion(objs[3] == null? "":objs[3].toString());
//			ifaInfoVO.setExpertiseType(objs[4] == null? "":objs[4].toString());
//			ifaInfoVO.setInvestStyle(objs[5] == null? "":objs[5].toString());
			
			IfaHighlight highlight = this.findIfaHighlight(ifaMemberId);
			if( null != highlight){
				ifaInfoVO.setHighlightTitle(highlight.getTitle());
				ifaInfoVO.setHighlightCreateTime(DateUtil.dateToDateString(highlight.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			}
			
			long articleCount = this.findIfaArticleCount(ifaMemberId);
			long fansCount = this.findIfaFansCount(ifaMemberId);
			
			ifaInfoVO.setArticleCount( Long.toString(articleCount));
			ifaInfoVO.setFansCount(Long.toString(fansCount));
		}
		
		return ifaInfoVO;
	}
	
	/**
	 * 获取Ifa最新说说
	 * @param ifaMemberId
	 * @return
	 */
	private IfaHighlight findIfaHighlight(String ifaMemberId){		
		StringBuilder hql = new StringBuilder();
		hql.append(" from IfaHighlight i  ");
		hql.append(" where i.ifa.member.id = ? ");
		List<String> params = new ArrayList<String>();
		params.add(ifaMemberId);
		List<IfaHighlight> list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			return list.get(0);			
		}
		
		return null;
	}

	/**
	 * 获取Ifa粉丝数量
	 * @param ifaMemberId
	 * @return
	 */
	private long findIfaFansCount(String ifaMemberId) {
		List<String> params = new ArrayList<String>();
		String hql = "select count(*) from WebFriend t  ";
		hql += " where t.fromMember.id = ? ";
		params.add(ifaMemberId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		
		if (list.isEmpty()) {
			return 0;
		} else {
			Object o = list.get(0);
			if (o == null)
				return 0;
			if (o instanceof Integer)
				return ((Integer) o).longValue();
			else
				return ((Long) o).longValue();
		}
	}
	
	/**
	 * 获取Ifa动态数量
	 * @param ifaMemberId
	 * @return
	 */
	private long findIfaArticleCount(String ifaMemberId) {
		List<String> params = new ArrayList<String>();
		String hql = "select count(*) from CornerInfo t  ";
		hql += " where t.author.id = ? ";
		params.add(ifaMemberId);
		
		List list = this.baseDao.find(hql, params.toArray(), false);
		
		if (list.isEmpty()) {
			return 0;
		} else {
			Object o = list.get(0);
			if (o == null)
				return 0;
			if (o instanceof Integer)
				return ((Integer) o).longValue();
			else
				return ((Long) o).longValue();
		}
	}
	
	
	public AppIfaPerformanceVO findIfaPerformance(String ifaMemberId,String toCurrency,String periodCode,int rows){
		AppIfaPerformanceVO vo = new AppIfaPerformanceVO();
		IfaExtInfo ifaExtInfo = this.findIfaExtInfo(ifaMemberId, toCurrency);
		if(null != ifaExtInfo){
			vo.setAum(ifaExtInfo.getAumNumber());
		}
		
		List<AppPortfolioArenaMessVo> portfolioList = this.findIfaPortfolioList(ifaMemberId, periodCode, rows);
		vo.setPortfolioList(portfolioList);
		
		return vo;
	}
	
	private IfaExtInfo findIfaExtInfo(String ifaMemberId,String toCurrency){
		String hql = " from IfaExtInfo i where i.ifa.member.id = ?";
		List<String> params = new ArrayList<String>();
		params.add(ifaMemberId);
		List<IfaExtInfo> list = this.baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			return list.get(0);
		}
		else {
			return null;
		}		
	}
	
	
	/**
	 * 得到Ifa的明星投资组合列表
	 * @param ifaMemberId 用户ID
	 * @param periodCode 分析时间段编码：fund_return_period_code_1W：一周，fund_return_period_code_1M：一个月...，fund_return_period_code_1Y：一年 ...
	 * @return rows 明星组合返回多少条
	 */
	private List<AppPortfolioArenaMessVo> findIfaPortfolioList(String ifaMemberId,String periodCode,int rows){
		List<AppPortfolioArenaMessVo>  messList = new ArrayList<AppPortfolioArenaMessVo>();
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArena s ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=s.id and r.periodCode=? ");
		hql.append(" where s.creator.id=? order by r.increase desc ");
		
		params.add(periodCode);
		params.add(ifaMemberId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioArena info=(PortfolioArena)objs[0];
			AppPortfolioArenaMessVo vo =new AppPortfolioArenaMessVo();
			vo.setId(info.getId());
			vo.setPortfolioName(info.getPortfolioName());
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
			vo.setInvestmentGoal(info.getInvestmentGoal());
			vo.setSuitability(info.getSuitability());
			vo.setReason(info.getReason());
			vo.setDescription(info.getDescription());
			vo.setTotalReturn(getFormatNumByPer(info.getTotalReturn()));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setOverhead(info.getOverhead());
			if(null!=objs[1]){
				PortfolioArenaReturn arenaReturn=(PortfolioArenaReturn)objs[1];
				vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
			}else{
				vo.setIncrease(getFormatNumByPer(null));
			}
			MemberBase memberBase=info.getCreator();
			vo.setCreator(memberBase.getNickName());
			vo.setIconUrl(memberBase.getIconUrl());
			messList.add(vo);
		}
		
		return messList;
	}
	
	/**
	 * 得到Ifa的推荐列表，包含组合，策略，基金，观点，新闻等内容。
	 * @param ifaMemberId 用户ID
	 * @param periodCode 分析时间段编码：fund_return_period_code_1W：一周，fund_return_period_code_1M：一个月...，fund_return_period_code_1Y：一年 ...
	 * @return rows 推荐组合返回多少条
	 */
	public AppIfaRecommendedVO findIfaRecommended(JsonPaging jsonPaging, String ifaMemberId,String langCode,String toCurrency,String periodCode){
		AppIfaRecommendedVO vo = new AppIfaRecommendedVO();
				
		List<AppPortfolioArenaMessVo> portfolioList = this.findIfaRecommendPortfolioList(jsonPaging, ifaMemberId, periodCode).getList();
		List<AppStrategyInfoVO> strategyList = this.findIfaRecommendStrategyList(jsonPaging, ifaMemberId, langCode).getList();
		List<AppFundInfoVO> fundList = this.findIfaRecommendFundList(jsonPaging, ifaMemberId, langCode, toCurrency, periodCode).getList();
		List<AppInsightInfoVO> insightList = this.findIfaRecommendInsightInfoList(jsonPaging, ifaMemberId).getList();
		List<AppNewsItemVo> newsList = this.findIfaRecommendNewsList(jsonPaging, ifaMemberId).getList();
		
		vo.setPortfolioList(portfolioList);
		vo.setStrategyList(strategyList);
		vo.setFundList(fundList);
		vo.setInsightList(insightList);
		vo.setNewsList(newsList);
		
		return vo;
	}
	
	/**
	 * 得到Ifa的推荐投资组合列表
	 * @param ifaMemberId 用户ID
	 * @param periodCode 分析时间段编码：fund_return_period_code_1W：一周，fund_return_period_code_1M：一个月...，fund_return_period_code_1Y：一年 ...
	 * @return rows 推荐组合返回多少条
	 */
	public JsonPaging findIfaRecommendPortfolioList(JsonPaging jsonPaging, String ifaMemberId,String periodCode){
		List<AppPortfolioArenaMessVo>  messList = new ArrayList<AppPortfolioArenaMessVo>();
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArena s ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=s.id and r.periodCode=? ");
		hql.append(" left join WebRecommended wr on s.id = wr.relateId ");
		hql.append(" where wr.moduleType='portfolio_arena' and wr.creator.id=? ");
		hql.append(" order by wr.overhead desc, s.createTime desc ");
		
		params.add(periodCode);
		params.add(ifaMemberId);
		
//		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioArena info=(PortfolioArena)objs[0];
			AppPortfolioArenaMessVo vo =new AppPortfolioArenaMessVo();
			vo.setId(info.getId());
			vo.setPortfolioName(info.getPortfolioName());
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setOverhead(info.getOverhead());
			if(null!=objs[1]){
				PortfolioArenaReturn arenaReturn=(PortfolioArenaReturn)objs[1];
				vo.setIncrease(getFormatNumByPer(arenaReturn.getIncrease()));
			}else{
				vo.setIncrease(getFormatNumByPer(null));
			}
			MemberBase memberBase=info.getCreator();
			vo.setCreator(memberBase.getNickName());
			vo.setIconUrl(memberBase.getIconUrl());
			messList.add(vo);
		}
		jsonPaging.setList(messList);
		return jsonPaging;
	}
	
	/**
	 * 得到Ifa的推荐投资组合列表
	 * @param ifaMemberId 用户ID
	 * @param periodCode 分析时间段编码：fund_return_period_code_1W：一周，fund_return_period_code_1M：一个月...，fund_return_period_code_1Y：一年 ...
	 * @return rows 推荐组合返回多少条
	 */
	public JsonPaging findIfaRecommendStrategyList(JsonPaging jsonPaging, String ifaMemberId,String langCode){
		List<AppStrategyInfoVO>  messList = new ArrayList<AppStrategyInfoVO>();
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" select i.id,fn_getconfigname(i.geoAllocation,'"+langCode+"') as geoAllocation,fn_getconfigname(i.sector,'"+langCode+"') as sector,i.strategyName,i.riskLevel,i.investmentGoal,i.reason,m.nickName,i.createTime,i.lastUpdate,m.iconUrl  from StrategyInfo i ");
		hql.append(" left join MemberBase m on m.id=i.creator.id");
		hql.append(" left join WebRecommended wr on i.id = wr.relateId ");
		hql.append(" where wr.moduleType='strategy' and wr.creator.id=? ");
		hql.append(" order by wr.overhead desc, i.createTime desc ");
		
		params.add(ifaMemberId);
		
//		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] obj = (Object[]) list.get(0);
			AppStrategyInfoVO vo =new AppStrategyInfoVO();
			vo.setId(obj[0]==null?"":obj[0].toString());
			vo.setGeoAllocation(obj[1]==null?"":obj[1].toString());
			vo.setSector(obj[2]==null?"":obj[2].toString());
			vo.setStrategyName(obj[3]==null?"":obj[3].toString());
			vo.setRiskLevel(obj[4]==null?"":obj[4].toString());
			vo.setInvestmentGoal(obj[5]==null?"":obj[5].toString());
			vo.setReason(obj[6]==null?"":obj[6].toString());
			vo.setCreator(obj[7]==null?"":obj[7].toString());
			vo.setCreateTime(DateUtil.dateToDateString( DateUtil.getDate(obj[8]==null?"":obj[8].toString()),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString( DateUtil.getDate(obj[9]==null?"":obj[9].toString()),"yyyy-MM-dd HH:mm:ss"));
			vo.setIconUrl(obj[10]==null?"":obj[10].toString());
			messList.add(vo);
		}
		jsonPaging.setList(messList);
		return jsonPaging;
	}
	
	/**
	 * 得到Ifa的推荐投资组合列表
	 * @param ifaMemberId 用户ID
	 * @param periodCode 分析时间段编码：fund_return_period_code_1W：一周，fund_return_period_code_1M：一个月...，fund_return_period_code_1Y：一年 ...
	 * @return rows 推荐组合返回多少条
	 */
	public JsonPaging findIfaRecommendFundList(JsonPaging jsonPaging, String ifaMemberId,String langCode, String toCurrency,String periodCode){
		List<AppFundInfoVO>  messList = new ArrayList<AppFundInfoVO>();
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" select f.id,f.product.id,x.fundName,x.fundCurrency,f.riskLevel, ");
		hql.append(" x.fundType,get_exchange_rate(x.fundCurrencyCode,'"+toCurrency+"',f.lastNav) as lastNav, ");
		hql.append(" date_format(f.lastNavUpdate,'%Y-%m-%d'),x.fundCurrencyCode,r.increase");
		hql.append(" from FundInfo f left join "+this.getLangString("FundInfo", langCode));
		hql.append(" x on f.id=x.id ");
		//股票时间类型:return_period_code_1W,return_period_code_1M,return_period_code_1Y...
		hql.append(" left join FundReturn r on r.fund.id=f.id and r.periodCode=? ");
		hql.append(" left join WebRecommended wr on f.id = wr.relateId ");
		hql.append(" where wr.moduleType='fund' and wr.creator.id=? ");
		hql.append(" order by wr.overhead desc, f.createTime desc ");
		
		params.add(periodCode);
		params.add(ifaMemberId);
		
//		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppFundInfoVO  vo = new AppFundInfoVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setProductId(objs[1]==null?"":objs[1].toString());
			vo.setFundName(objs[2]==null?"":objs[2].toString());
			vo.setFundCurrency(objs[3]==null?"":objs[3].toString());
			vo.setRiskLevel(objs[4]==null?"":objs[4].toString());
			vo.setFundType(objs[5]==null?"":objs[5].toString());
			vo.setLastNav(objs[6]==null?"":objs[6].toString());
			vo.setLastNavUpdate(objs[7]==null?"":objs[7].toString());
			vo.setFundCurrencyCode(objs[8]==null?"":objs[8].toString());
			vo.setIncrease(objs[9]==null?"":objs[9].toString());
			//数据格式处理
			if(null==vo.getLastNav() || "".equals(vo.getLastNav())){
				vo.setLastNav(getFormatNum(null));
			}else{
				vo.setLastNav(getFormatNum(Double.parseDouble(vo.getLastNav()),vo.getFundCurrencyCode()));
			}
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			messList.add(vo);
		}
		jsonPaging.setList(messList);
		return jsonPaging;
	}
	
	/**
	 * 得到Ifa的推荐投资组合列表
	 * @param ifaMemberId 用户ID
	 * @param periodCode 分析时间段编码：fund_return_period_code_1W：一周，fund_return_period_code_1M：一个月...，fund_return_period_code_1Y：一年 ...
	 * @return rows 推荐组合返回多少条
	 */
	public JsonPaging findIfaRecommendInsightInfoList(JsonPaging jsonPaging, String ifaMemberId){
		List<AppInsightInfoVO>  messList = new ArrayList<AppInsightInfoVO>();
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" from InsightInfo s ");
		hql.append(" left join WebRecommended wr on s.id = wr.relateId ");
		hql.append(" where wr.moduleType='insight' and wr.creator.id=? ");
		hql.append(" order by wr.overhead desc, s.createTime desc ");
		
		params.add(ifaMemberId);
		
//		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			InsightInfo info=(InsightInfo)objs[0];
			AppInsightInfoVO vo =new AppInsightInfoVO();
			vo.setId(info.getId());
			vo.setGeoAllocation(info.getGeoAllocation());
			vo.setSector(info.getSector());
			vo.setTitle(info.getTitle());
			vo.setThumbnail(info.getThumbnail());
			vo.setContent(info.getContent());
			vo.setUpCounter(info.getUpCounter()==null? "0":info.getUpCounter().toString());
			vo.setDownCounter(info.getDownCounter()==null? "0":info.getDownCounter().toString());
			vo.setClick(info.getClick()==null? "0":info.getClick().toString());
			if(null!=info.getCreator())vo.setCreator(info.getCreator().getNickName());
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(), "yyyy-MM-dd HH:mm:ss"));
			
			messList.add(vo);
		}
		jsonPaging.setList(messList);
		return jsonPaging;
	}
	
	/**
	 * 得到Ifa的推荐投资组合列表
	 * @param ifaMemberId 用户ID
	 * @param periodCode 分析时间段编码：fund_return_period_code_1W：一周，fund_return_period_code_1M：一个月...，fund_return_period_code_1Y：一年 ...
	 * @return rows 推荐组合返回多少条
	 */
	public JsonPaging findIfaRecommendNewsList(JsonPaging jsonPaging, String ifaMemberId){
		List<AppNewsItemVo>  messList = new ArrayList<AppNewsItemVo>();
		List params = new ArrayList();
		StringBuilder hql = new StringBuilder();
		hql.append(" from NewsInfo i ");
		hql.append(" left join NewsCount c on c.id=i.id ");
		hql.append(" left join NewsInfoCategory n on n.newsInfo.id=i.id ");
		hql.append(" left join WebRecommended wr on i.id = wr.relateId ");
		hql.append(" where wr.moduleType='news' and wr.creator.id=? ");
		hql.append(" order by wr.overhead desc, i.createTime desc ");
		
		params.add(ifaMemberId);
		
//		List list = this.baseDao.find(hql.toString(), params.toArray(), 0, rows, false);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List list = jsonPaging.getList();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			NewsInfo info=(NewsInfo)objs[0];
			//NewsCount count=(NewsCount)objs[1];
			NewsInfoCategory category=(NewsInfoCategory)objs[2];
			
			AppNewsItemVo vo =new AppNewsItemVo();
			vo.setId(info.getId());
			vo.setRegionType(info.getRegionType());
			vo.setSectionType(info.getSectionType());
			//vo.setXfaNewsId(info.getXfaNewsId());
			vo.setTitle(info.getTitle());
			//vo.setUrl(info.getUrl());
			//vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), "yyyy-MM-dd HH:mm:ss"));
			if(null!=category && null!=category.getCategory()){
				vo.setCategoryName(category.getCategory().getNameSc());//得到栏目名称
			}
			//if(null!=count){
			//	vo.setViews(count.getViews());
			//	vo.setComments(count.getComments());
			//	vo.setUps(count.getUps());
			//	vo.setDowns(count.getDowns());
			//}
			messList.add(vo);
		}
		jsonPaging.setList(messList);
		return jsonPaging;
	}

}
