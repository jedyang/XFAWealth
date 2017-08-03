package com.fsll.wmes.portfolio.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.service.SysParamService;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.PortfolioArena;
import com.fsll.wmes.entity.PortfolioArenaProduct;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.portfolio.service.PortfolioArenaService;
import com.fsll.wmes.portfolio.vo.CoreFundNavVO;
import com.fsll.wmes.portfolio.vo.CorePortfolioVO;
import com.fsll.wmes.portfolio.vo.PortfolioArenaVO;
import com.fsll.wmes.portfolio.vo.PortfolioListVO;

/***
 * 业务接口实现类：投资组合信息管理接口实现类
 * 
 * @author mqzou
 * @version 1.0
 * @date 2017-06-02
 */
@Service("portfolioArenaService")
public class PortfolioArenaServiceImpl extends BaseService implements PortfolioArenaService {

	@Autowired
	private SysParamService sysParamService;

	/**
	 * 获取组合的列表分页
	 * 
	 * @author mqzou 2017-06-02
	 * @param jsonPaging
	 * @param keyWord
	 * @param riskLevel
	 * @param memberId
	 * @param langCode
	 * @return
	 */
	@Override
	public JsonPaging findPortfolioList(JsonPaging jsonPaging, String keyWord, String riskLevel, String langCode) {
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioArena r where r.isValid='1'");
		List<Object> params = new ArrayList<Object>();
		if (null != keyWord && !"".equals(keyWord)) {
			hql.append(" and r.portfolioName like ?");
			params.add("%" + keyWord + "%");
		}
		if (null != riskLevel && !"".equals(riskLevel)) {
			hql.append(" and r.riskLevel=?");
			params.add(riskLevel);
		}
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<PortfolioListVO> list = new ArrayList<PortfolioListVO>();
		if (null != jsonPaging && null != jsonPaging.getList() && !jsonPaging.getList().isEmpty()) {
			Iterator it = jsonPaging.getList().iterator();
			while (it.hasNext()) {
				PortfolioArena info = (PortfolioArena) it.next();
				PortfolioListVO vo = new PortfolioListVO();
				vo.setId(info.getId());
				vo.setPortfolioName(info.getPortfolioName());
				vo.setCreateTime(DateUtil.dateToDateString(info.getCreateTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT));
				String creator = getCommonMemberName(info.getCreator().getId(), langCode, "2");
				vo.setCreator(creator);
				String geoAllocation = sysParamService.findNameByCode(info.getGeoAllocation(), langCode);
				vo.setGeoAllocation(geoAllocation);
				String sector = sysParamService.findNameByCode(info.getSector(), langCode);
				vo.setSector(sector);
				vo.setRiskLevel(null != info.getRiskLevel() ? String.valueOf(info.getRiskLevel()) : "");
				vo.setTotalReturnRate(info.getTotalReturn());
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取组合详情
	 * 
	 * @author mqzou 2017-06-05
	 * @param id
	 * @return
	 */
	@Override
	public PortfolioArenaVO findPortfolioDetial(String id, String langCode) {
		PortfolioArena obj = findById(id);
		PortfolioArenaVO vo = new PortfolioArenaVO(obj);
		String geoAllocation = sysParamService.findNameByCode(obj.getGeoAllocation(), langCode);
		vo.setGeoAllocation(geoAllocation);
		String sector = sysParamService.findNameByCode(obj.getSector(), langCode);
		vo.setSector(sector);
		String creatorName = getCommonMemberName(obj.getCreator().getId(), langCode, "1");
		vo.setCreatorName(creatorName);
		return vo;
	}

	@Override
	public PortfolioArena findById(String id) {
		PortfolioArena vo = (PortfolioArena) baseDao.get(PortfolioArena.class, id);
		return vo;
	}

	/**
	 * 获取指定时间范围内的组合累计收益数据
	 * 
	 * @param portfolioId
	 *            组合id
	 * @param frequencyType
	 *            频率 1W=1周,2W=2周,1M=1月,3M=3月,6M=6月,YTD=年初至今,1Y=1年,3Y=3年,5Y=5年
	 * @param 货币
	 * @return
	 */
	public List<CorePortfolioVO> getPortfolioReturnRate(String portfolioId, String frequencyType, String currency) {

		List<CorePortfolioVO> list = new ArrayList<CorePortfolioVO>();

		list = findArenaReturnRate(portfolioId, frequencyType, currency);

		return list;
	}

	// 获取基金日期范围内累计收益
	public List<CorePortfolioVO> findArenaReturnRate(String portfolioId, String frequencyType, String currency) {
		List<CorePortfolioVO> voList = new ArrayList<CorePortfolioVO>();

		Map<Date, Date> marketDateMap = new TreeMap<Date, Date>(); // 日期
		Map<String, Map<Date, CoreFundNavVO>> dataMap = new HashMap<String, Map<Date, CoreFundNavVO>>();// 基金回报
		Date firstDate = null;
		Map<String, Double> firstDayNavMap = new HashMap<String, Double>();// 基金回报
		Map<String, Double> fundWeightMap = new HashMap<String, Double>();// 基金权重

		StringBuilder hql = new StringBuilder();
		List<Object> params = new ArrayList<Object>();

		Date beginDate;
		Date endDate;
		Date createDate;

		hql.append(" from FundMarket t ");
		hql.append(" inner join FundInfo f on f.id=t.fund.id ");
		hql.append(" inner join PortfolioArenaProduct p on p.product.id=f.product.id ");
		hql.append(" where t.marketDate is not null and p.portfolio.id=? ");
		hql.append(" order by t.marketDate desc ");
		params = new ArrayList<Object>();
		params.add(portfolioId);
		List list = baseDao.find(hql.toString(), params.toArray(), 0, 2, false);
		if (null != list && !list.isEmpty()) {
			Object[] objs = (Object[]) list.get(0);
			FundMarket market = (FundMarket) objs[0];
			PortfolioArenaProduct product = (PortfolioArenaProduct) objs[2];

			endDate = market.getMarketDate();
			createDate = product.getPortfolio().getCreateTime();
		} else {
			endDate = new Date();
			createDate = null;
		}

		beginDate = getBeginDate(endDate, frequencyType);
		if (null != createDate && beginDate.before(createDate)) {
			beginDate = createDate;
		}

		hql = new StringBuilder();
		hql.append(" from FundMarket t ");
		hql.append(" inner join FundInfo f on f.id=t.fund.id ");
		hql.append(" inner join PortfolioArenaProduct p on p.product.id=f.product.id ");
		hql.append(" where t.marketDate >? and p.portfolio.id=? ");
		hql.append(" order by t.marketDate ");
		params = new ArrayList<Object>();
		params.add(beginDate);
		params.add(portfolioId);
		List marketDataList = baseDao.find(hql.toString(), params.toArray(), false);
		if (null != marketDataList && !marketDataList.isEmpty()) {

			for (int i = 0; i < marketDataList.size(); i++) {
				Object[] objs = (Object[]) marketDataList.get(i);
				FundMarket market = (FundMarket) objs[0];
				FundInfo fundInfo = market.getFund();
				PortfolioArenaProduct product = (PortfolioArenaProduct) objs[2];

				// 日期
				marketDateMap.put(market.getMarketDate(), market.getMarketDate());
				if (null == firstDate) {
					firstDate = market.getMarketDate();
				}

				String fundId = fundInfo.getId();
				double nav = market.getNav() == null ? 0 : market.getNav();
				double rate = product.getAllocationRate() == null ? 0 : product.getAllocationRate();

				CoreFundNavVO fundNavVO = new CoreFundNavVO();
				fundNavVO.setFundId(fundId);
				fundNavVO.setMarketDate(market.getMarketDate());
				fundNavVO.setNav(nav);
				fundNavVO.setRate(rate);

				fundWeightMap.put(fundId, rate);

				Map<Date, CoreFundNavVO> subMap = new HashMap<Date, CoreFundNavVO>();
				if (dataMap.containsKey(fundId)) {
					subMap = dataMap.get(fundId);
				}
				subMap.put(market.getMarketDate(), fundNavVO);
				dataMap.put(fundId, subMap);
			}

			firstDate = findFundFirstNavDate(dataMap, firstDate);
			List<Date> removeList = new ArrayList<Date>();

			for (Map.Entry<Date, Date> entry : marketDateMap.entrySet()) {
				if (entry.getKey().before(firstDate)) {
					removeList.add(entry.getKey());
				}
			}

			for (Date date : removeList) {
				marketDateMap.remove(date);
			}

			firstDayNavMap = findFundFirstNavMap(dataMap, firstDate);

			for (Map.Entry<Date, Date> entry : marketDateMap.entrySet()) {
				CorePortfolioVO vo = new CorePortfolioVO();
				// 循环每一天
				Date marketDate = entry.getValue();
				double returnRate = 0;
				for (Map.Entry<String, Map<Date, CoreFundNavVO>> data : dataMap.entrySet()) {
					// 循环每一产品
					String fundId = data.getKey();
					Map<Date, CoreFundNavVO> fundDataList = data.getValue();
					double fundFirstNav = firstDayNavMap.get(fundId);
					double weight = fundWeightMap.get(fundId) / 100.0;
					double nav = 0;

					if (fundDataList.containsKey(marketDate)) {
						// 当天有数据
						nav = fundDataList.get(marketDate).getNav() == null ? 0 : fundDataList.get(marketDate).getNav();
					} else {
						// 当天没有数据，要补数据
						FundMarket market = getPrevFundMarketInfo(fundId, marketDate);
						if (null != market) {
							nav = market.getNav() == null ? 0 : market.getNav();
						}
					}

					double fundReturnRate = fundFirstNav > 0 ? weight * (nav - fundFirstNav) / fundFirstNav : 0;
					returnRate += fundReturnRate;
				}

				returnRate = new BigDecimal(returnRate).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				vo.setPortfolioId(portfolioId);
				vo.setMarketDate(marketDate);
				vo.setReturnRate(returnRate);
				voList.add(vo);
			}

		}

		return voList;
	}

	private Date getBeginDate(Date date, String frequencyType) {
		Date beginDate;
		if (StringUtils.isNotBlank(frequencyType)) {
			// 获取相关日期数
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			if ("1W".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -7);
			} else if ("2W".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -14);
			} else if ("1M".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -30);
			} else if ("3M".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -90);
			} else if ("6M".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -180);
			} else if ("YTD".equals(frequencyType)) {
				// 获取当年第一天
				Date firstDate = DateUtil.getCurrYearFirst();
				calendar.setTime(firstDate);
			} else if ("1Y".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -365);

			} else if ("3Y".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -3 * 365);

			} else if ("5Y".equals(frequencyType)) {
				calendar.add(Calendar.DATE, -5 * 365);
			}
			beginDate = calendar.getTime();
		} else {
			beginDate = new Date();
		}
		return beginDate;
	}

	private Date findFundFirstNavDate(Map<String, Map<Date, CoreFundNavVO>> dataMap, Date firstDate) {
		Date result = firstDate;
		for (Map.Entry<String, Map<Date, CoreFundNavVO>> data : dataMap.entrySet()) {
			String fundId = data.getKey();
			Map<Date, CoreFundNavVO> fundDataList = data.getValue();
			Date marketDate = null;
			if (fundDataList.containsKey(firstDate)) {
				marketDate = firstDate;
			} else {
				FundMarket market = getFundFirstMarketInfo(fundId, firstDate);
				if (null != market) {
					marketDate = market.getMarketDate();
				} else {
					marketDate = firstDate;
				}
			}
			if (marketDate.after(result)) {
				result = marketDate;
			}

		}

		return result;
	}

	private Map<String, Double> findFundFirstNavMap(Map<String, Map<Date, CoreFundNavVO>> dataMap, Date firstDate) {
		Map<String, Double> result = new HashMap<String, Double>();
		for (Map.Entry<String, Map<Date, CoreFundNavVO>> data : dataMap.entrySet()) {
			String fundId = data.getKey();
			Map<Date, CoreFundNavVO> fundDataList = data.getValue();
			double firstNav = 0;
			if (fundDataList.containsKey(firstDate)) {
				firstNav = fundDataList.get(firstDate).getNav();
			} else {
				FundMarket market = getFundFirstMarketInfo(fundId, firstDate);
				if (null != market) {
					firstNav = market.getNav() == null ? 1 : market.getNav();
				} else {
					firstNav = 1;
				}
			}
			result.put(fundId, firstNav);
		}

		return result;
	}

	/**
	 * 通过FundID,日期获取基金的行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getFundMarketInfo(String productId, Date date) {
		String sql = "from FundMarket r where r.fund.id=? and r.marketDate=?";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		params.add(date);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过FundID,获取基金的最新的一条行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getLatestFundMarketInfo(String fundId) {
		String sql = "from FundMarket r where r.fund.id=? order by r.marketDate desc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), 0, 2, false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过FundID,获取基金的第一条行情数据
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getFundFirstMarketInfo(String fundId) {
		String sql = "from FundMarket r where r.fund.id=? order by r.marketDate asc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过FundID,日期获取基金的行情数据，如果该日期无数据，则通过获取比它最早的有数据的那条记录
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private FundMarket getPrevFundMarketInfo(String fundId, Date date) {
		String sql = "from FundMarket r where r.fund.id=? and r.marketDate<? order by r.marketDate desc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		params.add(date);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), 0, 2, false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			return null;
		}
	}

	/**
	 * 通过FundID,日期获取基金的行情数据，如果该日期无数据，则通过获取比它最早的有数据的那条记录
	 * 
	 * @author zxtan 2017-05-26
	 * @return
	 */
	private FundMarket getFundFirstMarketInfo(String fundId, Date date) {
		String sql = "from FundMarket r where r.nav>0 and r.fund.id=? and r.marketDate<? order by r.marketDate desc";
		List<Object> params = new ArrayList<Object>();
		params.add(fundId);
		params.add(date);
		List<FundMarket> list = this.baseDao.find(sql, params.toArray(), 0, 2, false);
		if (null != list && !list.isEmpty()) {
			return list.get(0);
		} else {
			sql = "from FundMarket r where r.nav>0 and r.fund.id=? order by r.marketDate";
			params = new ArrayList<Object>();
			params.add(fundId);
			list = this.baseDao.find(sql, params.toArray(), 0, 2, false);
			if (null != list && !list.isEmpty()) {
				return list.get(0);
			} else {
				return null;
			}
		}
	}

	/**
	 * 通过ProductId获取FundID
	 * 
	 * @author wwlin 2017-03-02
	 * @return
	 */
	private String getFundInfoByProductId(String productId) {
		String sql = "from FundInfo r where r.product.id=? ";
		List<Object> params = new ArrayList<Object>();
		params.add(productId);
		List<FundInfo> list = this.baseDao.find(sql, params.toArray(), false);
		if (null != list && !list.isEmpty()) {
			return list.get(0).getId();
		} else {
			return "";
		}
	}
	
	/**
	 * 获取投资组合产品比重列表（用于图表显示）
	 * @author mqzou 
	 * @date 2016-09-13
	 * @param type
	 * @param portfolioId
	 * @return
	 */
	@Override
	public List findPortfolioProductRate(String type, String portfolioId,String langCode) {
		StringBuilder sql=new StringBuilder();
		sql.append("SELECT "+type+" name,SUM(p.`allocation_rate`) value FROM `portfolio_arena_product` p ");
		sql.append(" LEFT JOIN fund_info i on p.product_id=i.product_id ");
		sql.append(" LEFT JOIN `fund_info_"+langCode+"` f ON i.id=f.`id` WHERE p.`portfolio_id`='"+portfolioId+"'");
		sql.append(" GROUP BY "+type);
		List list=this.springJdbcQueryManager.springJdbcQueryForList(sql.toString());
		return list;
	}
}
