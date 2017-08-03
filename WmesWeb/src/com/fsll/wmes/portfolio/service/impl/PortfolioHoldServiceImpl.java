package com.fsll.wmes.portfolio.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.crm.vo.ProposalNumberVO;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.BondInfoEn;
import com.fsll.wmes.entity.BondInfoSc;
import com.fsll.wmes.entity.BondInfoTc;
import com.fsll.wmes.entity.BondMarketDay;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.IfaMigrateHist;
import com.fsll.wmes.entity.InsureInfo;
import com.fsll.wmes.entity.InsureInfoEn;
import com.fsll.wmes.entity.InsureInfoSc;
import com.fsll.wmes.entity.InsureInfoTc;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.PortfolioHoldReturn;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.ProductType;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.StockInfoEn;
import com.fsll.wmes.entity.StockInfoSc;
import com.fsll.wmes.entity.StockInfoTc;
import com.fsll.wmes.entity.StockMarketDay;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.fund.vo.FundIncomDetailVO;
import com.fsll.wmes.ifa.service.IfaMigrateHistService;
import com.fsll.wmes.ifa.vo.IfaPortfolioVO;
import com.fsll.wmes.ifa.vo.IfaSpaceVisitVO;
import com.fsll.wmes.investor.vo.AccountNumberVO;
import com.fsll.wmes.member.service.MemberBaseService;
import com.fsll.wmes.portfolio.service.PortfolioHoldService;
import com.fsll.wmes.portfolio.vo.AssetsVO;
import com.fsll.wmes.portfolio.vo.FavouryPortfolioVO;
import com.fsll.wmes.portfolio.vo.FavouryWatchlistsVO;
import com.fsll.wmes.portfolio.vo.HoldProductVO;
import com.fsll.wmes.portfolio.vo.InvestorPortfolioDataVO;
import com.fsll.wmes.portfolio.vo.PortfolioAssetsVO;
import com.fsll.wmes.portfolio.vo.PortfolioHoldCumperfSimpleVO;
import com.fsll.wmes.portfolio.vo.PortfolioProductListVO;
import com.fsll.wmes.portfolio.vo.PortfolioProductPLVO;
import com.fsll.wmes.strategy.vo.CharDataVO;
import com.fsll.wmes.strategy.vo.FavouryStrategyVO;

@Service("portfolioHoldService")
// @Transactional
public class PortfolioHoldServiceImpl extends BaseService implements PortfolioHoldService {
	@Autowired
	private MemberBaseService memberBaseService;
	@Autowired
	private IfaMigrateHistService ifaMigrateHistService;
	/**
	 * 获取投资人的资产
	 * 
	 * @author mqzou
	 * @date 2016-10-09
	 * @param memberId
	 * @return
	 */
	@Override
	public List<AssetsVO> getMyAssets(String memberId, String portfolioId, String ifaId) {
		/*
		 * StringBuilder sql = new StringBuilder(); sql.append(
		 * "SELECT SUM(IFNULL(h.`total_market_value`,0)) total_market_value,SUM(IFNULL(h.`total_return_rate`,0)) total_return_rate,SUM(IFNULL(h.`total_return_value`,0)) total_return_value, "
		 * ); sql.append(
		 * " ,p.`base_currency` FROM `portfolio_info` p LEFT JOIN `portfolio_hold` h ON p.`id`=h.`portfolio_id`"
		 * ); sql.append(" WHERE p.`member_id`=? "); List params = new
		 * ArrayList(); params.add(memberId);
		 * 
		 * if (null != portfolioId && !"".equals(portfolioId)) {
		 * sql.append(" and p.id=?"); params.add(portfolioId); }
		 * sql.append(" GROUP BY p.`base_currency`;");
		 */

		StringBuilder hql = new StringBuilder();
		hql.append(" select SUM(h.totalMarketValue) ,SUM(h.totalReturnRate) ,SUM(h.totalReturnValue) ,h.baseCurrency from  ");
		hql.append("  PortfolioHold h  ");
		hql.append(" where  h.ifFirst='0'");

		List params = new ArrayList();

		if (null != memberId && !"".equals(memberId)) {
			hql.append(" and h.member.id=? ");
			params.add(memberId);
		}
		if (null != portfolioId && !"".equals(portfolioId)) {
			hql.append(" and h.id=?");
			params.add(portfolioId);
		}
		if (null != ifaId && !"".equals(ifaId)) {
			hql.append(" and h.ifa.id=?");
			params.add(ifaId);
		}
		hql.append(" GROUP BY h.baseCurrency");
		AssetsVO vo = new AssetsVO();
		vo.setMemberId(memberId);
		// List returList =
		// this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(),
		// params.toArray());
		List returList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<AssetsVO> list = new ArrayList<AssetsVO>();
		if (null != returList && !returList.isEmpty()) {
			Iterator it = returList.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				/*
				 * HashMap map = (HashMap) it.next(); Object marketValue =
				 * map.get("total_market_value"); Object returnRate =
				 * map.get("total_return_rate"); Object returnValue =
				 * map.get("total_return_value"); Object baseCurr =
				 * map.get("base_currency");
				 */
				Object marketValue = objects[0];
				Object returnRate = objects[1];
				Object returnValue = objects[2];
				Object baseCurr = objects[3];
				vo.setTotalMarketValue(null != marketValue ? Double.valueOf(marketValue.toString()) : 0);
				vo.setTotalReturnRate(null != returnRate ? Double.valueOf(returnRate.toString()) : 0);
				vo.setTotalReturnValue(null != returnValue ? Double.valueOf(returnValue.toString()) : 0);

				vo.setBaseCurrency(null != baseCurr ? baseCurr.toString() : "");
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 获取统计某段时间内的资产收益报表
	 * 
	 * @author mqzou
	 * @date 2016-10-09
	 * @param memberId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public List<PortfolioHoldCumperf> getCountPortfolioHoldCumperfs(String memberId, String startDate, String endDate, String portfolioId) {
		/*
		 * StringBuilder sql = new StringBuilder(); sql.append(
		 * "SELECT c.`valuation_date`,SUM(c.`cumulative_pl`) cumulative_pl,SUM(c.`day_pl`) daily_pl ,SUM(c.`cumulative_rate`) cumulative_rate"
		 * ); sql.append(
		 * " FROM `portfolio_hold_cumperf`  c LEFT JOIN  `portfolio_info` p  ON p.id=c.`portfolio_id`"
		 * );
		 * sql.append(" WHERE p.`member_id`=? and c.valuation_date between ? and ?"
		 * );
		 * 
		 * List params = new ArrayList(); params.add(memberId);
		 * params.add(startDate); params.add(endDate); if (null != portfolioId
		 * && !"".equals(portfolioId)) { sql.append(" and p.id=?");
		 * params.add(portfolioId); }
		 * sql.append(" GROUP BY c.`valuation_date` ORDER BY valuation_date;");
		 * List resultList =
		 * this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(),
		 * params.toArray());
		 */

		StringBuilder hql = new StringBuilder();
		hql.append(" select c.valuationDate,SUM(c.cumulativePl),SUM(c.dayPl) ,SUM(c.cumulativeRate)");
		hql.append(" from PortfolioHoldCumperf c left join PortfolioHold h on c.portfolioHold.id=h.id");
		hql.append(" where h.member.id=? and c.valuationDate between ? and ?");
		List params = new ArrayList();
		params.add(memberId);
		params.add(DateUtil.getDate(startDate));
		params.add(DateUtil.getDate(endDate));
		if (null != portfolioId && !"".equals(portfolioId)) {
			hql.append(" and h.id=?");
			params.add(portfolioId);
		}
		hql.append(" GROUP BY c.valuationDate ORDER BY c.valuationDate");
		List<PortfolioHoldCumperf> list = new ArrayList<PortfolioHoldCumperf>();
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);

		if (null != resultList && !resultList.isEmpty()) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				Object[] objects = (Object[]) it.next();
				int index = 0;
				PortfolioHoldCumperf voCumperf = new PortfolioHoldCumperf();
				voCumperf.setValuationDate(null != objects[index] ? DateUtil.getDate(objects[index].toString()) : null);
				voCumperf.setCumulativePl(null != objects[index++] ? Double.valueOf(objects[index].toString()) : 0);
				voCumperf.setDayPl(null != objects[index++] ? Double.valueOf(objects[index].toString()) : 0);

				voCumperf.setCumulativeRate(null != objects[index++] ? Double.valueOf(objects[index].toString()) : 0);

				list.add(voCumperf);
			}
		}
		return list;
	}

	/**
	 * 获取投资组合的资产情况
	 * 
	 * @author mqzou
	 * @date 2016-10-09
	 * @param memberId
	 * @return
	 */
	@Override
	public List<PortfolioAssetsVO> getPortfolioAssets(String memberId, String portfolioId, String ifaId,String period,String currency) {
		

		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		/*hql.append(" select h.id,h.portfolioName,count(*),h.totalReturnValue ,h.totalMarketValue ,h.totalReturnRate,h.baseCurrency");
		hql.append(" from  PortfolioHold h    left join PortfolioHoldProduct r  on  r.portfolioHold.id=h.id ");
		
		hql.append(" where h.member.id=? and h.ifFirst='0'");*/
		hql.append(" from PortfolioHold h left join PortfolioHoldReturn r on h.id=r.portfolioHold.id and r.periodCode=?");
		hql.append(" where h.member.id=? and h.ifFirst='0'  ");
		hql.append(" and exists (select p from PortfolioHoldProduct p where p.portfolioHold.id=h.id )");
		
		params.add(CommonConstantsWeb.WEB_RETURN_PERIOD_CODE_PRE+period);
		params.add(memberId);
		
		if (null != portfolioId && !"".equals(portfolioId)) {
			hql.append(" and h.id=?");
			params.add(portfolioId);
		}
		if (null != ifaId && !"".equals(ifaId)) {
			hql.append(" and h.ifa.id=?");
			params.add(ifaId);
		}
		//hql.append(" group by h.id,h.portfolioName");
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<PortfolioAssetsVO> list = new ArrayList<PortfolioAssetsVO>();
		if (null != resultList && !resultList.isEmpty()) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				// HashMap map = (HashMap) it.next();
				//int index = 0;
				Object[] objects = (Object[]) it.next();
				PortfolioHold hold=(PortfolioHold)objects[0];
				if(hold==null)
					continue;
				PortfolioHoldReturn holdReturn=(PortfolioHoldReturn)objects[1];
				String baseCurrency=hold.getBaseCurrency();
				if(null==baseCurrency || "".equals(baseCurrency))
					baseCurrency=CommonConstants.DEF_CURRENCY_HKD;
				double totalAsset=null!=hold.getTotalAsset()?hold.getTotalAsset():0;
				totalAsset=getNumByCurrency(totalAsset, baseCurrency, currency);
				PortfolioAssetsVO vo = new PortfolioAssetsVO();
				vo.setTotalAssets(String.valueOf(totalAsset));
				if(null!=holdReturn && null!=holdReturn.getIncrease()){
					double returnRate=holdReturn.getIncrease();
					vo.setTotalReturnValue(totalAsset*returnRate);
					vo.setTotalReturnRate(returnRate);
				}
				if(null==period || "".equals(period)){
					vo.setTotalReturnRate(hold.getTotalReturnRate());
					vo.setTotalReturnValue(getNumByCurrency(hold.getTotalReturnValue(), baseCurrency, currency));
				}
				vo.setPortfolioId(hold.getId());
				vo.setPortfolioName(hold.getPortfolioName());
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 获取用户的账户统计数量
	 * 
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	@Override
	public AccountNumberVO findAccountNumberOfMember(String memberId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(0) allCount,COUNT(IF(a.open_status='-1' OR a.open_status='3' ,TRUE,NULL)) auditedCount ");
		sql.append(" FROM investor_account a WHERE a.`member_id`=? AND a.`is_valid`='1';");
		List params = new ArrayList();
		params.add(memberId);
		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		AccountNumberVO vo = new AccountNumberVO();
		vo.setMemberId(memberId);
		if (null != resultList && !resultList.isEmpty()) {
			HashMap map = (HashMap) resultList.get(0);
			Object total = map.get("allCount");
			Object audited = map.get("auditedCount");
			vo.setTotalCount(null != total ? Integer.valueOf(total.toString()) : 0);
			vo.setAuditedCount(null != audited ? Integer.valueOf(audited.toString()) : 0);

		} else {
			vo.setTotalCount(0);
			vo.setAuditedCount(0);
		}
		return vo;
	}

	/**
	 * 获取用户的方案统计数量
	 * 
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	@Override
	public ProposalNumberVO findProposalNumberOfMember(String memberId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(0) allCount,COUNT(IF(c.`status`='1',TRUE,NULL)) toConfirm  FROM `crm_proposal` c WHERE c.`member_id`=? and c.`is_valid`='1' ");
		sql.append(" and c.status!='-2' and c.status!='0'");
		List params = new ArrayList();
		params.add(memberId);
		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		ProposalNumberVO vo = new ProposalNumberVO();
		vo.setMemberId(memberId);
		if (null != resultList && !resultList.isEmpty()) {
			HashMap map = (HashMap) resultList.get(0);
			Object total = map.get("allCount");
			Object toConfirm = map.get("toConfirm");
			vo.setTotalCount(null != total ? Integer.valueOf(total.toString()) : 0);
			vo.setToConfirmCount(null != toConfirm ? Integer.valueOf(toConfirm.toString()) : 0);

		} else {
			vo.setTotalCount(0);
			vo.setToConfirmCount(0);
		}
		return vo;
	}

	@Override
	public FavouryPortfolioVO findFavouryPortfolio(String memberId) {
		/*
		 * StringBuilder sql = new StringBuilder(); sql.append(
		 * " SELECT p.`id`,p.`portfolio_name`,SUM(h.`total_return_rate`)*100 total_return_rate,h.`last_update`"
		 * ); sql.append(
		 * "  FROM `web_follow` f  LEFT JOIN `portfolio_info` p ON f.`relate_id`=p.`id`"
		 * );
		 * sql.append(" LEFT JOIN `portfolio_hold` h ON p.`id`=h.`portfolio_id`"
		 * );
		 * sql.append(" WHERE f.`member_id`=? AND f.`module_type`='portfolio_arena'"
		 * ); sql.append(
		 * " GROUP BY p.`id`,p.`portfolio_name` ORDER BY h.`last_update` DESC LIMIT 1;"
		 * ); List params = new ArrayList(); params.add(memberId); List
		 * resultList =
		 * this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(),
		 * params.toArray());
		 */

		StringBuilder hql = new StringBuilder();
		hql.append(" from WebFollow f left join PortfolioArena a on  f.relateId=a.id");
		hql.append(" where f.moduleType='portfolio_arena' and f.member.id=? ");
		hql.append(" order by f.createTime desc ");
		List params = new ArrayList();
		params.add(memberId);
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);

		FavouryPortfolioVO vo = new FavouryPortfolioVO();
		vo.setMemberId(memberId);
		if (null != resultList && !resultList.isEmpty()) {
			Object[] obj = (Object[]) resultList.get(0);
			WebFollow webFollow = (WebFollow) obj[0];
			PortfolioArena arena = (PortfolioArena) obj[1];
			if (null != arena) {
				vo.setPortfolioName(arena.getPortfolioName());
				vo.setReturnRate(String.valueOf(arena.getTotalReturn()));
				vo.setLastUpdate(DateUtil.getDateStr(webFollow.getCreateTime()));
				vo.setAllCount(resultList.size());
				vo.setPortfolioId(arena.getId());
				/*
				 * HashMap map = (HashMap) resultList.get(0); Object name =
				 * map.get("portfolio_name"); Object rate =
				 * map.get("total_return_rate"); Object date =
				 * map.get("last_update"); vo.setPortfolioName(null != name ?
				 * name.toString() : ""); vo.setReturnRate(null != rate ?
				 * rate.toString() : "0"); vo.setLastUpdate(date.toString());
				 */
				vo.setRiskLevel(String.valueOf(arena.getRiskLevel()));
			}
		} else {
			vo.setPortfolioName("");
			vo.setReturnRate("0");
			vo.setLastUpdate("");
			vo.setAllCount(0);
		}
		/*
		 * String sql1 =
		 * " SELECT COUNT(0) allCount FROM `web_follow` f LEFT JOIN `portfolio_info` p ON f.`relate_id`=p.`id` WHERE f.`member_id`='"
		 * + memberId +
		 * "' AND f.`module_type`='portfolio_arena' AND p.`is_valid`='1'"; List
		 * countList = this.springJdbcQueryManager.springJdbcQueryForList(sql1);
		 * if (null != countList && !countList.isEmpty()) { HashMap map =
		 * (HashMap) countList.get(0); Object count = map.get("allCount");
		 * vo.setAllCount(null != count ? Integer.valueOf(count.toString()) :
		 * 0); } else { vo.setAllCount(0); }
		 */
		return vo;
	}

	@Override
	public FavouryStrategyVO findFavouryStrategy(String memberId) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT s.`id`, s.`strategy_name`,(SELECT COUNT(0) FROM `strategy_product` p WHERE p.strategy_id=s.id) fundCount,s.last_update");
		sql.append(" FROM `web_follow` f  LEFT JOIN `strategy_info` s ON f.`relate_id`=s.`id`");
		sql.append(" WHERE f.`member_id`=? AND f.`module_type`='strategy' AND s.`is_valid`='1'");
		sql.append(" GROUP BY s.`id`,s.`strategy_name` LIMIT 1;");
		List params = new ArrayList();
		params.add(memberId);
		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(), params.toArray());
		FavouryStrategyVO vo = new FavouryStrategyVO();
		if (resultList != null && !resultList.isEmpty()) {
			HashMap map = (HashMap) resultList.get(0);
			Object name = map.get("strategy_name");
			Object count = map.get("fundCount");
			Object lastUpdate = map.get("last_update");
			Object id = map.get("id");
			vo.setStrategyName(null != name ? name.toString() : "");
			vo.setFundCount(null != count ? Integer.valueOf(count.toString()) : 0);
			vo.setLastUpdate(lastUpdate.toString());
			vo.setStrategyId(id.toString());
		} else {
			vo.setFundCount(0);
			vo.setStrategyName("");
		}
		String sql1 = "SELECT COUNT(0) allCount FROM `web_follow` f  LEFT JOIN `strategy_info` s ON f.`relate_id`=s.`id` WHERE f.`member_id`='" + memberId
				+ "' AND f.`module_type`='strategy' AND s.`is_valid`='1'";
		List countList = this.springJdbcQueryManager.springJdbcQueryForList(sql1);
		if (null != countList && !countList.isEmpty()) {
			HashMap map = (HashMap) countList.get(0);
			Object count = map.get("allCount");
			vo.setAllCount(null != count ? Integer.valueOf(count.toString()) : 0);
		} else {
			vo.setAllCount(0);
		}
		return vo;
	}

	/**
	 * 获取用户收藏的Watchlists
	 * 
	 * @author mqzou
	 * @date 2016-10-21
	 * @param memberId
	 * @return
	 */
	@Override
	public FavouryWatchlistsVO findFavouryWatchlists(String memberId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" SELECT b.`name`,max(a.`last_update`) last_update,COUNT(0) fundsCount   ");
		hql.append(" FROM `web_watchlist`  a LEFT JOIN `web_watchlist_type` b  ON a.`type_id`=b.`id`");
		hql.append(" WHERE a.`member_id`=? GROUP BY b.`name` ORDER BY max(a.`last_update`) DESC");
		List params = new ArrayList();
		params.add(memberId);
		List resultList = this.springJdbcQueryManager.springJdbcQueryForList(hql.toString(), params.toArray());
		FavouryWatchlistsVO vo = new FavouryWatchlistsVO();
		if (null != resultList && !resultList.isEmpty()) {
			HashMap map = (HashMap) resultList.get(0);
			Object name = map.get("name");
			Object lastUpdate = map.get("last_update");
			Object fundsCount = map.get("fundsCount");
			vo.setFundsCount(null != fundsCount ? fundsCount.toString() : "0");
			vo.setLastUpdate(null != lastUpdate ? lastUpdate.toString() : "");
			vo.setTypeName(null != name ? name.toString() : "");
			vo.setAllCount(String.valueOf(resultList.size()));
		}
		return vo;
	}

	/**
	 * 获取用户查看过空间的IFA
	 * 
	 * @author mqzou
	 * @date 2016-10-10
	 * @param memberId
	 * @return
	 */
	@Override
	public List<IfaSpaceVisitVO> findFollowingSpace(String memberId) {
		String hql = " select   v.vistiTime,b.loginCode,b.iconUrl,b.id,b.iconUrl,m from  WebInvestorVisit v left join MemberIfa m on v.relateId=m.id left join MemberBase b on m.member.id=b.id ";
		hql += "where v.member.id='" + memberId + "' and v.moduleType='ifa' order by v.vistiTime desc";
		List list = this.baseDao.find(hql, null, false);
		List<IfaSpaceVisitVO> visitList = new ArrayList<IfaSpaceVisitVO>();
		if (null != list && !list.isEmpty()) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				IfaSpaceVisitVO vo = new IfaSpaceVisitVO();
				Object[] objs = (Object[]) it.next();
				String visitTime = objs[0] == null ? "" : objs[0].toString();
				String name = objs[1] == null ? "" : objs[1].toString();
				String url = objs[2] == null ? "" : objs[2].toString();
				String id = objs[3] == null ? "" : objs[3].toString();
				String iconUrl = objs[4] == null ? "" : objs[4].toString();
				MemberIfa ifa = (MemberIfa) objs[5];
				Calendar cal = Calendar.getInstance();
				cal.setTime(DateUtil.getDate(visitTime));

				vo.setVisitDateTime(DateUtil.StringToDate(visitTime, DateUtil.DEFAULT_DATE_TIME_FORMAT));
				vo.setMemberHeadImg(url);
				vo.setMemberId(id);
				vo.setName(name);
				vo.setIconUrl(iconUrl);
				if (null != ifa) {
					vo.setFirstName(ifa.getFirstName());
					vo.setLastName(ifa.getLastName());
					vo.setNameChn(ifa.getNameChn());
					vo.setGender(ifa.getGender());
					vo.setNickName(ifa.getMember().getNickName());
				}

				visitList.add(vo);

			}
		}
		return visitList;
	}

	/**
	 * 获取组合的产品列表
	 * 
	 * @author mqzou
	 * @date 2016-10-10
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List<PortfolioProductListVO> findPortfolioProductList(String portfolioId, String langCode, String currency) {
		/*
		 * StringBuilder sql = new StringBuilder(); sql.append(
		 * " SELECT p.id,i.`risk_level`,p.`available_unit`,p.`holding_unit`,p.`product_id`,p.`reference_cost` ,f.`fund_name`,"
		 * ); sql.append(
		 * " SUM(h.`total_market_value`) total_market_value,SUM(c.`cumulative_pl`) cumulative_pl,SUM(c.`cumulative_rate`)*100 cumulative_rate,i.`base_currency`,"
		 * ); sql.append(
		 * " CASE WHEN IFNULL(p.account_no,'')='' THEN a.account_no ELSE p.`account_no` END account_no,p.account_id"
		 * ); sql.append(
		 * " FROM `portfolio_info` i LEFT JOIN `portfolio_hold` h ON i.`id`=h.`portfolio_id`"
		 * ); sql.append(
		 * " LEFT JOIN `portfolio_hold_product` p ON i.`id`=p.`portfolio_id`");
		 * sql.append(
		 * " LEFT JOIN `portfolio_hold_cumperf` c ON i.`id`=c.`portfolio_id`");
		 * sql.append(" LEFT JOIN `fund_info_" + langCode +
		 * "` f ON p.`product_id`=f.`id` LEFT JOIN `investor_account` a ON p.account_id=a.id"
		 * ); sql.append(" WHERE i.`id`=?"); sql.append(
		 * " GROUP BY i.`risk_level`,p.`account_no`,p.`available_unit`,p.`holding_unit`,p.`product_id`,p.`reference_cost`"
		 * ); List params = new ArrayList(); params.add(portfolioId); List
		 * resultList =
		 * this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(),
		 * params.toArray());
		 */

		StringBuilder hql = new StringBuilder();
		hql.append(" select p.id,f.riskLevel,p.availableUnit,p.holdingUnit,i.id,p.referenceCost,l.fundName,");
		hql.append(" p.baseCurrency,f.lastNav ,l.fundCurrencyCode,h.baseCurrency,p.accountNo");
		hql.append(" from PortfolioHoldProduct p left join PortfolioHold h  on h.id=p.portfolioHold.id");
		hql.append(" left join PortfolioHoldCumperf c on h.id=c.portfolioHold.id");
		hql.append(" left join ProductInfo i on p.product.id=i.id");
		hql.append(" left join FundInfo f on i.id=f.product.id");
		hql.append(" left join " + getLangString("FundInfo", langCode) + " l on f.id=l.id");
		// hql.append(" left join PortfolioInfo o on h.portfolio.id=o.id");
		hql.append(" WHERE h.id=? ");
		hql.append(" GROUP BY i.id");
		List params = new ArrayList();
		params.add(portfolioId);
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<PortfolioProductListVO> list = new ArrayList<PortfolioProductListVO>();
		if (null != resultList && !resultList.isEmpty()) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				PortfolioProductListVO vo = new PortfolioProductListVO();
				// HashMap map = (HashMap) it.next();
				Object[] objects = (Object[]) it.next();
				int index = 0;
				Object id = objects[index++];
				Object riskLevel = objects[index++];
				Object availableUnit = objects[index++];
				Object holdingUnit = objects[index++];
				Object productId = objects[index++];
				Object referenceCost = objects[index++];
				Object fundName = objects[index++];
				Object productCurrency = objects[index++];
				Object lastNav = objects[index++];
				Object fundCurrencyCode = objects[index++];
				Object baseCurrency = objects[index++];
				Object accountId = "";
				Object accountNo = objects[index++];

				double cost = null != referenceCost ? Double.valueOf(referenceCost.toString()) : 0;
				double nav = null != lastNav ? Double.valueOf(lastNav.toString()) : 0;
				double hold = null != holdingUnit ? Double.valueOf(holdingUnit.toString()) : 0;
				
				cost = getNumByCurrency(cost, productCurrency.toString(), currency);
				nav = getNumByCurrency(nav, fundCurrencyCode.toString(), currency);
				double marketValue = nav * hold;
				double cumulativePl = (nav - cost) * hold;
				double cumulativeRate = (nav - cost) / nav;

				vo.setId(null != id ? id.toString() : "");
				vo.setAccountNo(null != accountNo ? accountNo.toString() : "");
				vo.setAvailableUnit(null != availableUnit ? availableUnit.toString() : "0");
				vo.setCurrency(null != baseCurrency ? baseCurrency.toString() : "");
				vo.setHoldingUnit(null != holdingUnit ? holdingUnit.toString() : "0");
				vo.setMarketValue(String.valueOf(marketValue));
				vo.setPl(String.valueOf(cumulativePl));
				vo.setProductId(null != productId ? productId.toString() : "");
				vo.setProductName(null != fundName ? fundName.toString() : "");
				vo.setReferenceCost(null != referenceCost ? referenceCost.toString() : "0");
				vo.setRiskLevel(null != riskLevel ? riskLevel.toString() : "");
				vo.setTotalRate(String.valueOf(cumulativeRate));
				vo.setAccountId(null != accountId ? accountId.toString() : "");
				list.add(vo);
			}
		}
		return list;
		// return null;
	}

	/**
	 * 获取组合产品关联表（持仓）
	 * 
	 * @author mqzou
	 * @date 2016-10-12
	 * @param id
	 * @return
	 */
	@Override
	public PortfolioHoldProduct findPortfolioHoldProductById(String id) {
		PortfolioHoldProduct vo = (PortfolioHoldProduct) this.baseDao.get(PortfolioHoldProduct.class, id);
		return vo;
	}

	/**
	 * 获取持仓产品的收益详情
	 * 
	 * @author mqzou
	 * @date 2016-10-12
	 * @param id
	 *            组合产品关联表id
	 * @return
	 */
	@Override
	public FundIncomDetailVO findFundIncomDetail(String id, String langCode) {
		/*
		 * StringBuilder sql = new StringBuilder(); sql.append(
		 * " SELECT p.`available_unit`,p.`holding_unit`,c.`cumperf_rate`,c.`total_pl`,f.`last_nav`,f.`last_nav_update`,a.`fund_name`,a.`fund_currency_code`,f.`risk_level`"
		 * ); sql.append(
		 * " FROM`portfolio_hold_product` p LEFT JOIN (SELECT * FROM `portfolio_hold_product_cumperf` ORDER BY valuation_date DESC LIMIT 1) c "
		 * ); sql.append(
		 * " ON p.`hold_id` = c.hold_id  AND (p.`account_id` = c.account_id  OR p.`account_no` = c.account_no ) AND p.`product_id` = c.product_id "
		 * );
		 * sql.append(" LEFT JOIN `fund_info` f ON p.`product_id`=f.`product_id`"
		 * ); sql.append(" LEFT JOIN `fund_info_" + langCode +
		 * "` a ON f.`id`=a.`id`"); sql.append(" WHERE p.`id`=?"); List params =
		 * new ArrayList(); params.add(id); List list =
		 * this.springJdbcQueryManager.springJdbcQueryForList(sql.toString(),
		 * params.toArray());
		 */

		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct h left join ProductInfo p on h.product.id=p.id");
		hql.append(" left join FundInfo f on p.id=f.product.id");
		hql.append(" left join " + getLangString("FundInfo", langCode) + " i on f.id=i.id");
		hql.append(" where h.id=?");
		List params = new ArrayList();
		params.add(id);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		FundIncomDetailVO vo = new FundIncomDetailVO();
		if (null != list && !list.isEmpty()) {
			Object[] objects = (Object[]) list.get(0);
			PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objects[0];
			FundInfo info = (FundInfo) objects[2];
			if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
				FundInfoEn fundInfoEn = (FundInfoEn) objects[3];
				vo.setFundName(fundInfoEn.getFundName());
				vo.setCurrency(fundInfoEn.getFundCurrencyCode());
			} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
				FundInfoSc fundInfoSc = (FundInfoSc) objects[3];
				vo.setFundName(fundInfoSc.getFundName());
				vo.setCurrency(fundInfoSc.getFundCurrencyCode());
			} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
				FundInfoTc fundInfoTc = (FundInfoTc) objects[3];
				vo.setFundName(fundInfoTc.getFundName());
				vo.setCurrency(fundInfoTc.getFundCurrencyCode());
			}

			vo.setAvailableUnit(String.valueOf(holdProduct.getAvailableUnit()));
			vo.setHoldingUnit(String.valueOf(holdProduct.getHoldingUnit()));
			vo.setLatestNAVPrice(info.getLastNav());
			vo.setLastUpdate(DateUtil.dateToDateString(info.getLastNavUpdate(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
			vo.setId(id);
			vo.setRiskLevel(String.valueOf(info.getRiskLevel()));
			PortfolioHoldProductCumperf cumperf = getLastCumperf(holdProduct.getPortfolioHold().getId(), holdProduct.getProduct().getId());
			if (null != cumperf) {
				vo.setTotalPl(String.valueOf(cumperf.getTotalPl()));
				vo.setCumperfRate(String.valueOf(cumperf.getCumperfRate()));
			} else {
				vo.setTotalPl("0");
				vo.setCumperfRate("0");
			}

			/*
			 * Iterator it = list.iterator(); while (it.hasNext()) { HashMap map
			 * = (HashMap) it.next(); Object availableUnit =
			 * map.get("available_unit"); Object holdingUnit =
			 * map.get("holding_unit"); Object cumperfRate =
			 * map.get("cumperf_rate"); Object totalPl = map.get("total_pl");
			 * Object lastNav = map.get("last_nav"); Object lastNavUpdate =
			 * map.get("last_nav_update"); Object fundName =
			 * map.get("fund_name"); Object fundCurrencyCode =
			 * map.get("fund_currency_code"); Object riskLevel =
			 * map.get("risk_level"); vo.setAvailableUnit(null != availableUnit
			 * ? availableUnit.toString() : "0"); vo.setCumperfRate(null !=
			 * cumperfRate ? cumperfRate.toString() : "0"); vo.setCurrency(null
			 * != fundCurrencyCode ? fundCurrencyCode.toString() : "");
			 * vo.setFundName(null != fundName ? fundName.toString() : "");
			 * vo.setHoldingUnit(null != holdingUnit ? holdingUnit.toString() :
			 * "0"); vo.setLastUpdate(null != lastNavUpdate ?
			 * lastNavUpdate.toString() : ""); vo.setLatestNAVPriceStr(null !=
			 * lastNav ? lastNav.toString() : "0"); vo.setRiskLevel(null !=
			 * riskLevel ? riskLevel.toString() : ""); vo.setTotalPl(null !=
			 * totalPl ? totalPl.toString() : "0"); }
			 */
			return vo;
		}
		return null;

	}

	/**
	 * 获取组合中的产品的收益
	 * 
	 * @author mqzou 2017-01-17
	 * @param holdId
	 * @param productId
	 * @return
	 */
	private PortfolioHoldProductCumperf getLastCumperf(String holdId, String productId) {
		String hql = " select sum(r.cumperfRate),sum(r.totalPl) from PortfolioHoldProductCumperf r where r.portfolioHold.id=? and r.product.id=?";
		hql += " group by r.valuationDate order by r.valuationDate desc";
		List<Object> params = new ArrayList<Object>();
		params.add(holdId);
		params.add(productId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			Object[] objects = (Object[]) list.get(0);
			Object cumperfRate = objects[0];
			Object totalPl = objects[1];
			PortfolioHoldProductCumperf vo = new PortfolioHoldProductCumperf();
			vo.setCumperfRate(null != cumperfRate ? Double.valueOf(cumperfRate.toString()) : 0);
			vo.setTotalPl(null != totalPl ? Double.valueOf(totalPl.toString()) : 0);
			return vo;
		}

		return null;
	}

	/**
	 * 获取统计某段时间内的产品收益报表
	 * 
	 * @author mqzou
	 * @date 2016-10-12
	 * @return
	 */
	@Override
	public List<PortfolioHoldProductCumperf> findPortfolioHoldProductCumperf(String pofolioId, String productId, String accountNo, String startDate, String endDate) {
		StringBuilder hql = new StringBuilder();
		hql.append("  from PortfolioHoldProductCumperf r left join InvestorAccount a on r.account.id=a.id  where 1=1 ");
		hql.append("and  r.portfolioHold.id=? and r.product.id=? and (r.accountNo=?  or a.accountNo=? )");
		hql.append(" and r.valuationDate between ? and ?");
		hql.append(" order by r.valuationDate");
		List params = new ArrayList();
		params.add(pofolioId);
		params.add(productId);
		params.add(accountNo);
		params.add(accountNo);
		params.add(DateUtil.getDate(startDate));
		params.add(DateUtil.getDate(endDate));
		List resultList = this.baseDao.find(hql.toString(), params.toArray(), false);
		List<PortfolioHoldProductCumperf> list = new ArrayList<PortfolioHoldProductCumperf>();
		if (null != resultList && !resultList.isEmpty()) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				PortfolioHoldProductCumperf cumperf = (PortfolioHoldProductCumperf) obj[0];

				list.add(cumperf);
			}
		}
		return list;
	}

	/**
	 * 获取投资组合的盈亏
	 * 
	 * @author mqzou 2016-10-24
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List<PortfolioProductPLVO> findPortfolioPL(String portfolioId, String langCode) {
		String hql = "select r,f from  PortfolioHoldProductCumperf r left join FundInfo i on r.product.id=i.product.id";
		hql += " left join " + getLangString("FundInfo", langCode);
		hql += " f on i.id=f.id where r.portfolioHold.id=? ";
		hql += " order by r.product,r.valuationDate desc";
		List params = new ArrayList();
		params.add(portfolioId);
		List resultList = this.baseDao.find(hql, params.toArray(), false);
		List<PortfolioProductPLVO> list = new ArrayList<PortfolioProductPLVO>();
		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				Object[] obj = (Object[]) it.next();
				PortfolioHoldProductCumperf cumperf = (PortfolioHoldProductCumperf) obj[0];
				PortfolioProductPLVO vo = new PortfolioProductPLVO();
				if (CommonConstants.LANG_CODE_EN.equals(langCode)) {
					FundInfoEn fundInfoEn = (FundInfoEn) obj[1];
					vo.setProductName(fundInfoEn.getFundName());
				} else if (CommonConstants.LANG_CODE_SC.equals(langCode)) {
					FundInfoSc fundInfoSc = (FundInfoSc) obj[1];
					vo.setProductName(fundInfoSc.getFundName());
				} else if (CommonConstants.LANG_CODE_TC.equals(langCode)) {
					FundInfoTc fundInfoTc = (FundInfoTc) obj[1];
					vo.setProductName(fundInfoTc.getFundName());
				}
				vo.setDayPlValue(cumperf.getDayPl());
				vo.setTotalPlValue(cumperf.getTotalPl());
				vo.setValuationDate(DateUtil.getDateStr(cumperf.getValuationDate()));
				list.add(vo);
			}
		}
		return list;
	}

	/**
	 * 获取IFA负责的组合列表
	 * 
	 * @author mqzou 2016-11-28
	 * @param ifaId
	 * @return
	 */
	@Override
	public List<PortfolioInfo> findPortfolioByIFA(String ifaId) {
		List<PortfolioInfo> list = new ArrayList<PortfolioInfo>();
		String hql = " from  PortfolioHold r where r.portfolio.memberIfa.id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(ifaId);
		List resultList = this.baseDao.find(hql, params.toArray(), false);
		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				PortfolioHold hold = (PortfolioHold) it.next();
				list.add(hold.getPortfolio());
			}
		}
		return list;
	}

	/**
	 * 获取投资者的的组合列表
	 * 
	 * @author zxtan 2017-02-21
	 * @param memberId
	 * @return
	 */
	@Override
	public List<PortfolioHold> findMyPortfolioHoldList(String memberId) {
		String hql = " from PortfolioHold r where r.member.id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List<PortfolioHold> list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}

	/**
	 * 获取投资人的组合产品类型分配
	 * 
	 * @author mqzou
	 * @date 2016-12-16
	 * @param memberId
	 * @return
	 */
	@Override
	public List findPortfolioAllocation(String memberId) {
		StringBuilder hql = new StringBuilder();
		/*hql.append(" select p.product.type,sum(p.holdingUnit) from PortfolioHoldProduct p");
		hql.append(" where p.portfolioHold.member.id=?");
		hql.append(" group by p.product.type");*/
		
		hql.append(" select i.type,sum(p.holdingUnit),t.displayColor from PortfolioHoldProduct p left join ProductInfo i on p.product.id=i.id");
		hql.append(" left join ProductType t on i.type=t.id");
		hql.append(" left join PortfolioHold h on p.portfolioHold.id=h.id");
		hql.append(" where h.member.id=?");
		hql.append(" group by t.orderBy");
		
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		return list;
	}

	/**
	 * 获取IFA需要进行回顾的组合
	 * 
	 * @author mqzou
	 * @date 2016-12-16
	 * @param critical
	 * @param ifaId
	 * @param period
	 * @return
	 */
	@Override
	public List findCriticalPortfolioByIfa(String critical, String ifaId, String period) {
		StringBuilder hql = new StringBuilder();
		hql = new StringBuilder();
		hql.append("select distinct c from CrmCustomer c ");
		// hql.append(" left join PortfolioInfo i on c.member.id=i.member.id");
		hql.append(" left join PortfolioHold h on c.ifa.id=h.ifa.id and c.member.id=h.member.id");
		hql.append(" left join PortfolioHoldReturn r on r.portfolioHold.id=h.id");
		hql.append(" where c.ifa.id=? and h.id in (select h.id from PortfolioHold h )");
		hql.append(" and r.periodCode=? and r.increase<?");
		List<Object> params = new ArrayList<Object>();
		params.add(ifaId);
		params.add(period);
		params.add(Double.parseDouble(critical));
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		return list;
	}

	/**
	 * 计算组合的总资产
	 * 
	 * @author mqzou 2016-12-21
	 */
	@Override
	public double findPortfolioAuM(String portfolioId, String currency) {
		double aum = 0;
		StringBuilder hql = new StringBuilder();
		/*
		 * hql.append(
		 * "select  c from PortfolioHoldProduct p left join InvestorAccount i on p.account.id=i.id"
		 * ); hql.append(
		 * " left join InvestorAccountCurrency c on i.accountNo=c.accountNo");
		 * hql.append(" where p.portfolioHold.id=?");
		 */
		hql.append(" select c from PortfolioHold h left join PortfolioHoldAccount c on h.id=c.portfolioHold.id ");
		hql.append(" where h.id=? and c!=null and h.ifFirst='0'");
		List<Object> params = new ArrayList<Object>();
		params.add(portfolioId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if (null != list) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				PortfolioHoldAccount accountCurrency = (PortfolioHoldAccount) it.next();
				if (null == accountCurrency)
					continue;
				double money = 0;
				money = (null != accountCurrency.getCashAvailable() ? accountCurrency.getCashAvailable() : 0) + (null != accountCurrency.getCashHold() ? accountCurrency.getCashHold() : 0)
						+ (null != accountCurrency.getMarketValue() ? accountCurrency.getMarketValue() : 0);
				if (currency.equals(accountCurrency.getBaseCurrency())) {
					aum += money;
				} else {
					aum += getExchangeRate(accountCurrency.getBaseCurrency(), currency) * money;
				}

			}
		}
		return aum;
	}

	/**
	 * 获取ifa创建的portfolio
	 * 
	 * @author mqzou
	 * @date 2016-12-27
	 * @param jsonPaging
	 * @param ifaMemberId
	 * @param currency
	 * @param keyWord
	 * @return
	 */
	@Override
	public JsonPaging findPortfolioForIfa(JsonPaging jsonPaging, String ifaMemberId, String currency, String keyWord) {
		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		hql.append(" from PortfolioHold h  ");
		hql.append(" left join MemberIfa f on h.ifa.id=f.id ");
		hql.append(" left join CrmCustomer c on h.member.id=c.member.id and c.ifa.id=f.id");
		hql.append(" where 1=1");
		if (null != ifaMemberId && !"".equals(ifaMemberId)) {
			hql.append(" and f.member.id=?");
			params.add(ifaMemberId);
		}
		if (null != keyWord && !"".equals(keyWord)) {
			hql.append(" and h.portfolioName like ?");
			params.add("%" + keyWord + "%");
		}
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it = jsonPaging.getList().iterator();
		List<IfaPortfolioVO> list = new ArrayList<IfaPortfolioVO>();
		while (it.hasNext()) {
			Object[] object = (Object[]) it.next();
			PortfolioHold hold = (PortfolioHold) object[0];
			CrmCustomer customer = (CrmCustomer) object[2];
			IfaPortfolioVO vo = new IfaPortfolioVO();
			vo.setId(hold.getId());
			vo.setLastUpdate(null != hold.getLastUpdate() ? DateUtil.getDateStr(hold.getLastUpdate()) : "");
			if (null != customer)
				vo.setNickName(customer.getNickName());
			vo.setPortfolioName(hold.getPortfolioName());

			vo.setRiskLevel(String.valueOf(hold.getRiskLevel()));
			vo.setTotalReturn(null != hold.getTotalReturnRate() ? hold.getTotalReturnRate() * 100 : 0);
			vo.setTotalReturnStr(StrUtils.getNumberString(vo.getTotalReturn()));
			vo.setTotalAsset(findPortfolioAuM(vo.getId(), currency));
			vo.setTotalAssetStr(StrUtils.getNumberString(vo.getTotalAsset(), "#,##0.00"));
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取PortfolioHold实体
	 * 
	 * @author mqzou
	 * @date 2017-01-03
	 * @param id
	 * @return
	 */
	@Override
	public PortfolioHold findById(String id) {
		PortfolioHold hold = (PortfolioHold) this.baseDao.get(PortfolioHold.class, id);
		return hold;
	}

	/**
	 * 查询组合是否定投
	 * 
	 * @author mqzou
	 * @date 2017-01-04
	 * @param id
	 * @return
	 */
	@Override
	public boolean checkPortfolioAip(String id) {
		String hql = "from OrderAip r where r.portfolioHold.id=?";
		List<Object> params = new ArrayList<Object>();
		params.add(id);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return true;
		}
		return false;
	}

	@Override
	public PortfolioHoldAccount saveHoldAccount(PortfolioHoldAccount holdAccount) {
		holdAccount = (PortfolioHoldAccount) this.baseDao.saveOrUpdate(holdAccount);
		return holdAccount;
	}

	/**
	 * 获取持仓组合的收益数据
	 * 
	 * @author mqzou 2017-01-10
	 * @param holdId
	 * @param currency
	 * @param strCondition
	 * @return
	 */
	@Override
	public InvestorPortfolioDataVO getPortfolioHoldCumperf(String holdId, String currency, String strCondition, String memberId, String dateFormat) {
		InvestorPortfolioDataVO dataVO = new InvestorPortfolioDataVO();
		List<PortfolioHoldCumperfSimpleVO> list = new ArrayList<PortfolioHoldCumperfSimpleVO>();
		List<String> dateList = new ArrayList<String>();
		String sql = "CALL pro_getportfoliocumperf(?,?,?,?);";
		List<Object> params = new ArrayList<Object>();
		params.add(holdId);
		params.add(currency);
		params.add(strCondition);
		params.add(memberId);
		List resultList = springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
		if (null != resultList) {
			Iterator it = resultList.iterator();
			while (it.hasNext()) {
				HashMap map = (HashMap) it.next();
				PortfolioHoldCumperfSimpleVO vo = new PortfolioHoldCumperfSimpleVO();
				Object valuationDate = getMapObject(map, "valuation_date");
				Object dayPl = getMapObject(map, "day_pl");
				Object cumulativePl = getMapObject(map, "cumulative_pl");
				Object id = getMapObject(map, "id");
				Object name = getMapObject(map, "portfolio_name");
				Object rate=getMapObject(map, "cumulative_rate");
				vo.setCumulativePl(null != cumulativePl ? Double.parseDouble(cumulativePl.toString()) : 0);
				vo.setDayPl(null != dayPl ? Double.parseDouble(dayPl.toString()) : 0);
				vo.setValuationDate(null != valuationDate ? DateUtil.getDateStr(valuationDate.toString(), DateUtil.DEFAULT_DATE_FORMAT, dateFormat) : "");
				vo.setHoldId(null != id ? id.toString() : "");
				vo.setPortfolioName(null != name ? name.toString() : "");
				vo.setCumulativeRate(null!=rate&& !"".equals(rate)?Double.parseDouble(rate.toString()):0);
				list.add(vo);
				dateList.add(null != valuationDate ? DateUtil.getDateStr(valuationDate.toString(), DateUtil.DEFAULT_DATE_FORMAT, dateFormat) : "");
			}
		}
		dataVO.setMyPortfolioList(list);
		dataVO.setDateList(dateList);
		return dataVO;
	}

	private String getMapObject(Map map, String key) {
		return map.get(key) == null ? "" : map.get(key).toString();
	}

	/**
	 * 获取组合的风险值
	 * 
	 * @author mqzou 2017-01-12
	 * @param portfolioHoldId
	 * @return
	 */
	@Override
	public int getPortfolioHoldRisk(String portfolioHoldId) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct p left join ProductInfo i on p.product.id=i.id");
		hql.append(" left join  FundInfo f on i.id=f.product.id");
		hql.append(" where p.portfolioHold.id=?");
		List<Object> params = new ArrayList<Object>();
		params.add(portfolioHoldId);
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);

		hql = new StringBuilder();
		hql.append(" select sum(p.holdingUnit) from PortfolioHoldProduct p where p.portfolioHold.id=?");
		List sumList = this.baseDao.find(hql.toString(), params.toArray(), false);
		double holdUnit = 0;
		if (null != sumList && !sumList.isEmpty()) {
			Object sum = sumList.get(0);
			if (null != sum && !"".equals(sum.toString())) {
				holdUnit = Double.valueOf(sum.toString());
			}
		}
		if (holdUnit == 0)
			return 0;

		double risk = 0;
		if (null != list) {
			Iterator it = list.iterator();
			while (it.hasNext()) {
				Object[] object = (Object[]) it.next();
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) object[0];
				ProductInfo product = (ProductInfo) object[1];
				FundInfo info = (FundInfo) object[2];
				int riskLevel = 0;
				if (CommonConstantsWeb.WEB_PRODUCT_TYPE_FUND.equals(product.getType())) {
					// 获取基金的风险值
					riskLevel = info.getRiskLevel();
				} else if (CommonConstantsWeb.WEB_PRODUCT_TYPE_BOND.equals(product.getType())) {
					// 债券的风险值默认1
					riskLevel = 1;
				} else if (CommonConstantsWeb.WEB_PRODUCT_TYPE_STOCK.equals(product.getType())) {
					// 股票的风险值默认9
					riskLevel = 9;
				}
				risk += riskLevel * ((null != holdProduct.getHoldingUnit() ? holdProduct.getHoldingUnit() : 0) / holdUnit);

			}
		}

		return (int) Math.round(risk); // Integer.parseInt(String.valueOf(risk));
	}


	
	/**
	 * 根据ifa的memberId查找客户组合持仓
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<PortfolioHold> findPortfolioHoldByIfa(String memberId) {
		String hql=" from PortfolioHold r where r.ifa.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List<PortfolioHold> list= (List<PortfolioHold>)this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 检测客户组合持仓是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistPortfolioHold(String ifaMemberId, String memberId) {
		String hql=" from PortfolioHold r where r.isValid='1' and r.ifa.member.id=? and r.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改IFA的客户组合持仓到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaPortfolioHold(String fromMemberId,String toMemberId,MemberBase createBy) {
		List<PortfolioHold> list = findPortfolioHoldByIfa(fromMemberId);
		MemberIfa ifa = memberBaseService.findIfaMember(toMemberId);
		if (null!=list && !list.isEmpty()){
			for (PortfolioHold f: list){
				//已存在的不加？
				if (!checkIfExistPortfolioHold(toMemberId, f.getIfa().getMember().getId())){
					boolean status = false;
					try{
						//更新方式
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
					hist.setToMember(memberBaseService.findById(toMemberId));
					hist.setCusMember(f.getMember());
					hist.setDataType("PortfolioHold");
					hist.setStatus(status?"1":"0");
					ifaMigrateHistService.saveOrUpdate(hist);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 根据ifa的memberId查找客户组合持仓账户
	 * @author michael
	 * @param memberId
	 * @return
	 */
	@SuppressWarnings("unchecked")
    public List<PortfolioHoldAccount> findPortfolioHoldAccountByIfa(String memberId) {
		String hql=" from PortfolioHoldAccount r where r.ifa.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		List<PortfolioHoldAccount> list= (List<PortfolioHoldAccount>)this.baseDao.find(hql, params.toArray(), false);
		return list;
	}
	
	/**
	 * 检测客户组合持仓账户是否存在
	 * @author michael
	 * @param ifaMemberId
	 * @param memberId
	 * @return
	 */
	public boolean checkIfExistPortfolioHoldAccount(String ifaMemberId, String memberId) {
		String hql=" from PortfolioHoldAccount r where r.isValid='1' and r.ifa.member.id=? and r.member.id=? ";
		List<Object> params=new ArrayList<Object>();
		params.add(ifaMemberId);
		params.add(memberId);
		List list=this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改IFA的客户组合持仓账户到另一IFA
	 * @author michael
	 * @date 2017-3-1
	 * @param fromMemberId 源IFA
	 * @param toMemberId 目标IFA
	 * @return Boolean
	 */
	public Boolean migrateIfaPortfolioHoldAccount(String fromMemberId,String toMemberId,MemberBase createBy) {
		List<PortfolioHoldAccount> list = findPortfolioHoldAccountByIfa(fromMemberId);
		MemberIfa ifa = memberBaseService.findIfaMember(toMemberId);
		if (null!=list && !list.isEmpty()){
			for (PortfolioHoldAccount f: list){
				//已存在的不加？
				if (!checkIfExistPortfolioHoldAccount(toMemberId, f.getIfa().getMember().getId())){
					boolean status = false;
					try{
						//更新方式
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
					hist.setToMember(memberBaseService.findById(toMemberId));
					hist.setCusMember(f.getMember());
					hist.setDataType("PortfolioHoldAccount");
					hist.setStatus(status?"1":"0");
					ifaMigrateHistService.saveOrUpdate(hist);
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 获取产品类型表数据
	 * @author mqzou 2017-03-24
	 * @return
	 */
	@Override
	public List<ProductType> findProductType() {
	    String hql=" from ProductType  r order by r.orderBy";
	    List<ProductType> list=(List<ProductType>)baseDao.find(hql, null, false);
		return list;
	}
	
	/**
	 * 获取产品比例图表数据
	 * @author mqzou 2017-03-31
	 * @param memberId 组合持仓所属的投资人id
	 * @param ifaId 组合持仓所属的ifaId
	 * @param holdId 组合持仓id
	 * @param currency 货币
	 * @return
	 */
	@Override
	public List getProductChartData(String memberId, String ifaId, String holdId, String currency) {
		String sql="CALL pro_getchartdata(?,?,?,?)";
		List<Object> params=new ArrayList<Object>();
		params.add(memberId);
		params.add(ifaId);
		params.add(holdId);
		params.add(currency);
		List resultList=springJdbcQueryManager.springJdbcQueryForList(sql, params.toArray());
	    List<CharDataVO> list=new ArrayList<CharDataVO>();
		if(null!=resultList && !resultList.isEmpty()){
		    Iterator it=resultList.iterator();
		    while (it.hasNext()) {
				HashMap map=(HashMap)it.next();
				String type=getMapObject(map, "type");
				String marketValue=getMapObject(map, "market_value");
				String displayColor=getMapObject(map, "display_color");
				if(null==type || "".equals(type) || null==marketValue || "".equals(marketValue)){
					continue;
				}
				CharDataVO vo=new CharDataVO();
				vo.setDisplayColor(displayColor);
				vo.setName(type);
				vo.setValue(Double.valueOf(marketValue));
				list.add(vo);
			}
			
		}
		return list;
	}
}
