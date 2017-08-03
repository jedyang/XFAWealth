/**
 * 
 */
package com.fsll.app.investor.market.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.app.investor.market.service.AppPortfolioArenaMessService;
import com.fsll.app.investor.market.vo.AppIndexHotVO;
import com.fsll.app.investor.market.vo.AppIndexMarketDataVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationDetailVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaAllocationVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaCumperfVO;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMarketMessVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaMessVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaProductVo;
import com.fsll.app.investor.market.vo.AppPortfolioArenaReturnVO;
import com.fsll.app.watch.service.AppWatchMessService;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaCumperf;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.entity.PortfolioArenaReturn;

/**
 * 投资组合用于展示的接口服务接口类现实
 * @author zpzhou
 * @date 2016-9-18
 */
@Service("appPortfolioArenaMessService")
//@Transactional
public class AppPortfolioArenaMessServiceImpl extends BaseService implements AppPortfolioArenaMessService {
	@Autowired
	private AppWatchMessService watchMessService;
	/**
	 * 得到首页最佳组合更多列表信息
	 * @param jsonPaging 分页信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @return	JsonPaging	分页组合数据
	 */
	public JsonPaging findBestPortfolioList(JsonPaging jsonPaging,String memberId,String langCode,String periodCode){
		List<AppIndexHotVO> portfolioList=new ArrayList<AppIndexHotVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("select f.id,f.portfolioName,f.riskLevel,r.increase,f.returnChart,m.id,m.iconUrl ");
		hql.append(" from PortfolioArena f ");
		hql.append(" left join PortfolioArenaReturn r on r.portfolio.id=f.id and r.periodCode=? ");
		hql.append(" left join f.creator m ");
		hql.append(" where f.id in (select v.relateId from WebView v where  v.moduleType=? ");
		hql.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
		
		hql.append(" order by r.increase desc ");
		List params = new ArrayList();
		params.add(periodCode);
		params.add("portfolio_arena");
		params.add(memberId);
		jsonPaging= baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		MemberBase member = (MemberBase) baseDao.get(MemberBase.class, memberId);
		String displayColor = member==null?CommonConstants.DEF_DISPLAY_COLOR: member.getDefDisplayColor();
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexHotVO  vo = new AppIndexHotVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			vo.setIncrease(objs[3]==null?"":objs[3].toString());			
			String returnChart = PageHelper.getImgUrlForWS(getPerformanceChartImage(String.valueOf(objs[0]), CommonConstantsWeb.CHART_IMAGE_SIZE_SMALL, displayColor));
			vo.setReturnChart(returnChart);//objs[4]==null?"":objs[4].toString()
			String creatorId = objs[5]==null?"":objs[5].toString();
			String iconUrl = objs[6]==null?"":objs[6].toString();
			vo.setIfaName(getCommonMemberName(creatorId, langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl,null));
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			portfolioList.add(vo);
		}
		jsonPaging.setList(portfolioList);
		return jsonPaging;
	}
	
	/**
	 * 得到一个基金关联的组合列表信息
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @param langCode 显示语言
	 * @return	productId 产品ID
	 */
	public List<AppIndexHotVO> findProductPortfolioList(String productId,String langCode,String periodCode,int num){
		List<AppIndexHotVO> portfolioList=new ArrayList<AppIndexHotVO>();
		String hql = "select f.id,f.portfolioName,f.riskLevel,r.increase,f.returnChart,m.id,m.iconUrl ";
		hql += " from PortfolioArenaProduct k inner join k.portfolio f ";
		hql += " left join PortfolioArenaReturn r on r.portfolio.id=f.id and r.periodCode=? ";
		hql += " left join f.creator m where k.product.id=? ";
		hql += " order by f.createTime desc ";
		List params = new ArrayList();
		params.add(periodCode);
		params.add(productId);
		List list= baseDao.find(hql, params.toArray(),0,num,false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppIndexHotVO  vo = new AppIndexHotVO();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setName(objs[1]==null?"":objs[1].toString());
			vo.setRiskLevel(objs[2]==null?"":objs[2].toString());
			vo.setIncrease(objs[3]==null?"":objs[3].toString());
			vo.setReturnChart(objs[4]==null?"":objs[4].toString());
			
			String creatorId = objs[5]==null?"":objs[5].toString();
			String iconUrl = objs[6]==null?"":objs[6].toString();
			vo.setIfaName(getCommonMemberName(creatorId, langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(iconUrl, null));
			if(null==vo.getIncrease() || "".equals(vo.getIncrease())){
				vo.setIncrease(getFormatNumByPer(null));
			}else{
				vo.setIncrease(getFormatNumByPer(Double.parseDouble(vo.getIncrease())));
			}
			portfolioList.add(vo);
		}
		return portfolioList;
	}
	
	
	/**
	 * 得到我有权限查看的投资组合列表
	 * @param memberId 用户ID
	 * @param keyWord 搜索关键词
	 * @param periodCode 分析时间段编码：1WK：一周，1Mth：一个月...，1Yr：一年 ...
	 * @return JsonPaging 分页数据
	 */
	public JsonPaging findPortfolioList(JsonPaging jsonPaging,String memberId,String periodCode,String keyWord,String langCode){
		List<AppPortfolioArenaMessVo>  messList = new ArrayList<AppPortfolioArenaMessVo>();
		List params = new ArrayList();
		String hql = " from PortfolioArena s " +
		             " left join PortfolioArenaReturn r on r.portfolio.id=s.id and r.periodCode=? "+
		             " where s.id in (select v.relateId from WebView v where  v.moduleType=? and s.isValid=? " +
		             " and (v.scopeFlag=? or (v.scopeFlag=? and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?))))  ";
		
		params.add(periodCode);
		params.add("portfolio_arena");
		params.add("1");
		params.add("2");
		params.add("3");
		params.add(memberId);
		
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and s.portfolioName like ? ";
			params.add("%"+keyWord+"%");
		}
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
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
			vo.setCreator(getCommonMemberName(memberBase.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(memberBase.getIconUrl(), null));
			String ifFollow = watchMessService.findWebFollowCount(info.getId(), memberId, "portfolio_arena");
			vo.setIfFollow(ifFollow);
			messList.add(vo);
		}
		jsonPaging.setList(messList);
		return jsonPaging;
	}
	
	/**
	 * 某只组合的某段时间的行情数据
	 * @param portfolioId  组合ID
	 * @param startDate 开始时间
	 * @param endDate 结束时间
	 * @return
	 */
	private List<AppIndexMarketDataVO> findPortfolioMarketList(String portfolioId, String startDate,String endDate){
		List<AppIndexMarketDataVO> list=new ArrayList<AppIndexMarketDataVO>();
		String hql = "select t.valuationDate,t.cumpref_rate from PortfolioArenaCumperf t where 1=1 ";
		List params = new ArrayList();
		if(null!=portfolioId && !"".equals(portfolioId)){
			hql += "and t.portfolio.id=? ";
			params.add(portfolioId);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql += "and t.valuationDate>=? ";
			params.add(DateUtil.getDate(startDate));
		}
		if(null!=endDate && !"".equals(endDate)){
			hql += "and t.valuationDate<=? ";
			params.add(DateUtil.getDate(endDate));
		}
		hql += " order by t.portfolio.id,t.valuationDate ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				AppIndexMarketDataVO vo = new AppIndexMarketDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setReturnRate(String.valueOf(objs[0]));
				vo.setMarketDate(String.valueOf(objs[1]));
				list.add(vo);
			}
		}
		return list;
	}
	
	
	/**
	 * 得到某个投资组合行情数据
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param startDate 起始时间
	 * @return
	 */
	public List<AppPortfolioArenaCumperfVO> findPortfolioCumperfList(String portfolioId,String startDate){
		List<AppPortfolioArenaCumperfVO> cumperfList = new ArrayList<AppPortfolioArenaCumperfVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArenaCumperf t where 1=1 ");
		List params = new ArrayList();
		if(null!=portfolioId && !"".equals(portfolioId)){
			hql.append(" and t.portfolio.id=? ");
			params.add(portfolioId);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql.append( "and t.valuationDate>=? ");
			params.add(DateUtil.getDate(startDate));
		}
		
		hql.append(" order by t.valuationDate ");

		
		List<PortfolioArenaCumperf> list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			PortfolioArenaCumperf cumperf = list.get(i);
			AppPortfolioArenaCumperfVO vo = new AppPortfolioArenaCumperfVO();
			
			vo.setId(cumperf.getId());
			vo.setPortfolioId(cumperf.getPortfolio().getId());
			vo.setValuationDate(DateUtil.dateToDateString(cumperf.getValuationDate(),"yyyy-MM-dd"));
			if(null==cumperf.getCumprefRate() || "".equals(cumperf.getCumprefRate().toString())){
				vo.setCumprefRate(getFormatNum(null));
			}else{
				vo.setCumprefRate(getFormatNum(cumperf.getCumprefRate()));
			}
			if(null==cumperf.getTotalPl() || "".equals(cumperf.getTotalPl().toString())){
				vo.setTotalPl(getFormatNum(null));
			}else{
				vo.setTotalPl(getFormatNum(cumperf.getTotalPl()));
			}
			if(null==cumperf.getDayPl() || "".equals(cumperf.getDayPl().toString())){
				vo.setDayPl(getFormatNum(null));
			}else{
				vo.setDayPl(getFormatNum(cumperf.getDayPl()));
			}
			vo.setCreateTime(DateUtil.dateToDateString(cumperf.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(cumperf.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			cumperfList.add(vo);
		}
		return cumperfList;
	}
	
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @return
	 */
	public List<AppPortfolioArenaProductVo> findPortfolioProductList(String portfolioId,String langCode){
		List<AppPortfolioArenaProductVo>  productList = new ArrayList<AppPortfolioArenaProductVo>();
		StringBuilder hql = new StringBuilder();
//		hql.append("SELECT p.id,p.portfolio.id,p.product.id,p.allocationRate,f.id as fundId,l.fundName,l.fundType ,f.riskLevel");
		hql.append(" FROM PortfolioArenaProduct p");
		hql.append(" inner JOIN FundInfo f ON p.product.id = f.product.id ");
		hql.append(" inner JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");
		hql.append(" where p.portfolio.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioArenaProduct product = (PortfolioArenaProduct) objs[0];
			FundInfo fundInfo = (FundInfo) objs[1];
			FundInfoSc fundInfoSc = new FundInfoSc();
			BeanUtils.copyProperties(objs[2], fundInfoSc);
			AppPortfolioArenaProductVo  vo = new AppPortfolioArenaProductVo();
			
			vo.setId(product.getId());
			vo.setPortfolioId(product.getPortfolio().getId());
			vo.setProductId(product.getProduct().getId());
			vo.setAllocationRate(String.valueOf(product.getAllocationRate()));
			vo.setFundId(fundInfo.getId());
			vo.setFundName(fundInfoSc.getFundName());
			vo.setFundType(fundInfoSc.getFundType());
			vo.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));
			vo.setFundCurrency(fundInfoSc.getFundCurrency());
			vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
			vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
			vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());
			
			productList.add(vo);
		}
		return productList;
	}
	
	/**
	 * 得到投资组合的产品分类配置情况
	 * @author zxtan
	 * @date 2016-11-16
	 * @param portfolioId 组合ID
	 * @param langCode 语言
	 * @param groupType 统计分类
	 * @return
	 */
	public List<AppPortfolioArenaAllocationDetailVO> findPortfolioAllocationList(String portfolioId,String langCode,String groupType){
		List<AppPortfolioArenaAllocationDetailVO> allocationList = new ArrayList<AppPortfolioArenaAllocationDetailVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("SELECT l."+groupType+",sum(p.allocationRate)");
		hql.append(" FROM PortfolioArenaProduct p");
		hql.append(" LEFT JOIN FundInfo f ON p.product.id = f.product.id ");
		hql.append(" LEFT JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");
		hql.append(" where p.portfolio.id=? ");
		hql.append(" group by l."+groupType );
		
		List params = new ArrayList();
		params.add(portfolioId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			AppPortfolioArenaAllocationDetailVO  vo = new AppPortfolioArenaAllocationDetailVO();
			
			vo.setItemName(objs[0]==null?"":objs[0].toString());
			vo.setItemRate(objs[1]==null?"":objs[1].toString());
			if(null==vo.getItemRate() || "".equals(vo.getItemRate())){
				vo.setItemRate(getFormatNum(null));
			}else{
				vo.setItemRate(getFormatNum(Double.parseDouble(vo.getItemRate())));
			}
			allocationList.add(vo);
		}
		return allocationList;
	}
	
	/**
	 * 得到投资组合基本信息
	 * @author zxtan
	 * @date 2016-11-16
	 * @param memberId member Id
	 * @param portfolioId 组合Id
	 * @return
	 */
	public AppPortfolioArenaMessVo findPortfolioInfoMess(String memberId,String portfolioId,String langCode){
		AppPortfolioArenaMessVo vo = null;
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArena p where p.isValid='1' and p.id=? ");
		hql.append(" and p.id in (select v.relateId from WebView v where  v.moduleType='portfolio_arena' ");
        hql.append(" and (v.scopeFlag='2' or (v.scopeFlag='3' and  v.id in (select d.view.id from WebViewDetail d where d.toMember.id=?)))) ");
		List params = new ArrayList();
		params.add(portfolioId);
		params.add(memberId);
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			vo =new AppPortfolioArenaMessVo();
			PortfolioArena info=(PortfolioArena)list.get(0);
			vo.setId(info.getId());
			vo.setPortfolioName(info.getPortfolioName());
			vo.setInvestmentGoal(info.getInvestmentGoal());	
			vo.setReason(info.getReason());
			vo.setSuitability(info.getSuitability());
			vo.setDescription(info.getDescription());
			//数据格式处理
			if(null==info.getTotalReturn() || "".equals(info.getTotalReturn().toString())){
				vo.setTotalReturn(getFormatNum(null));
			}else{
				vo.setTotalReturn(getFormatNum(info.getTotalReturn()));
			}						
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));			
			vo.setCreator(getCommonMemberName(info.getCreator().getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
			vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			vo.setIconUrl(PageHelper.getUserHeadUrlForWS(info.getCreator().getIconUrl(),null));
			String ifFollow = watchMessService.findWebFollowCount(info.getId(), memberId, "portfolio_arena");
			vo.setIfFollow(ifFollow);
		}
		return vo;
	}
	
	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-18
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppPortfolioArenaReturnVO findPortfolioReturn(String portfolioId,String periodCode){
		AppPortfolioArenaReturnVO vo =new AppPortfolioArenaReturnVO();
		String hql = " from PortfolioArenaReturn p where p.portfolio.id=? and p.periodCode=? ";
		List params = new ArrayList();
		params.add(portfolioId);
		params.add(periodCode);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			PortfolioArenaReturn info=(PortfolioArenaReturn)list.get(0);
			vo.setPeriodCode(info.getPeriodCode());
			//数据格式处理
			if(null==info.getIncrease() || "".equals(info.getIncrease().toString())){
				vo.setIncrease(getFormatNum(null));
			}else{
				vo.setIncrease(getFormatNumByPer(info.getIncrease()));
			}	
		}		
		
		return vo;
	}
}
