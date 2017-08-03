package com.fsll.app.ifa.market.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

//import com.fsll.app.me.vo.AppPieChartItemVO;
import com.fsll.app.ifa.market.service.AppPortfolioHoldService;
import com.fsll.app.ifa.market.vo.AppHoldAllocationVO;
import com.fsll.app.ifa.market.vo.AppHoldCountVO;
import com.fsll.app.ifa.market.vo.AppHoldCumperfVO;
import com.fsll.app.ifa.market.vo.AppHoldFeeVO;
import com.fsll.app.ifa.market.vo.AppHoldItemVO;
import com.fsll.app.ifa.market.vo.AppHoldOrderPlanVO;
import com.fsll.app.ifa.market.vo.AppHoldProductCumperfVO;
import com.fsll.app.ifa.market.vo.AppHoldProductDetailVO;
import com.fsll.app.ifa.market.vo.AppHoldProductFundVO;
import com.fsll.app.ifa.market.vo.AppHoldProductReturnDetailVO;
import com.fsll.app.ifa.market.vo.AppHoldProductVO;
import com.fsll.app.ifa.market.vo.AppHoldReturnVO;
import com.fsll.app.ifa.market.vo.AppHoldVO;
import com.fsll.app.ifa.market.vo.AppOrderHistoryHoldItemVO;
import com.fsll.app.ifa.market.vo.AppOrderHistoryVO;
import com.fsll.app.ifa.market.vo.AppPieChartItemVO;
import com.fsll.app.investor.me.vo.AppPortfolioProductVo;
import com.fsll.app.investor.me.vo.AppProductInfoVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.BeanUtils;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundPortfolio;
import com.fsll.wmes.entity.FundPortfolioSc;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.OrderHistory;
import com.fsll.wmes.entity.OrderPlan;
import com.fsll.wmes.entity.PortfolioHold;
import com.fsll.wmes.entity.PortfolioHoldAccount;
import com.fsll.wmes.entity.PortfolioHoldCumperf;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.PortfolioHoldProductCumperf;
import com.fsll.wmes.entity.PortfolioHoldReturn;
import com.fsll.wmes.entity.ProductInfo;

/**
 * IFA-Market投资组合服务
 * @author zxtan
 * @date 2017-03-29
 */
@Service("appIfaMarketPortfolioHoldService")
public class AppPortfolioHoldServiceImpl extends BaseService implements
		AppPortfolioHoldService {


	/**
	 * 获取IFA客户持仓组合统计（盈利、亏损数）
	 * @author zxtan
	 * @date 2017-04-26
	 */
	public AppHoldCountVO findPortfolioHoldCount(JSONObject jsonObject) {
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String minReturn = jsonObject.optString("minReturn","");
		String maxReturn = jsonObject.optString("maxReturn","");
		String minAmount = jsonObject.optString("minAmount","");
		String maxAmount = jsonObject.optString("maxAmount","");
		String toCurrency = jsonObject.optString("toCurrency","");
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);

		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" inner join MemberIfa i on i.id=h.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=h.member.id and c.ifa.id=i.id ");
		hql.append(" left join PortfolioHoldReturn r on r.portfolioHold.id=h.id and r.periodCode=? ");
		hql.append(" where i.member.id=? ");
		hql.append(" and exists (select p.id from PortfolioHoldProduct p where p.portfolioHold.id=h.id ) ");
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		
		if(!"".equals(keyword)){
			hql.append(" and h.portfolioName like ?");
			params.add("%"+keyword+"%");
		}
		
		if(!"".equals(minReturn)){
			hql.append(" and r.increase >=?");
			params.add(Double.parseDouble(minReturn));
		}		
		if(!"".equals(maxReturn)){
			hql.append(" and r.increase <=?");
			params.add(Double.parseDouble(maxReturn));
		}
		
		if("".equals(toCurrency)){
			if(!"".equals(minAmount)){
				hql.append(" and h.totalAsset >=?");
				params.add(Double.parseDouble(minAmount));
			}
			
			if(!"".equals(maxAmount)){
				hql.append(" and h.totalAsset <=?");
				params.add(Double.parseDouble(maxAmount));
			}
		}else {
			if(!"".equals(minAmount)){
				hql.append(" and get_exchange_rate(h.baseCurrency,?,h.totalAsset) >=?");
				params.add(toCurrency);
				params.add(Double.parseDouble(minAmount));
			}
			
			if(!"".equals(maxAmount)){
				hql.append(" and get_exchange_rate(h.baseCurrency,?,h.totalAsset) <=?");
				params.add(toCurrency);
				params.add(Double.parseDouble(maxAmount));
			}
		}
		
		hql.append(" order by h.lastUpdate desc ");
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		AppHoldCountVO vo = new AppHoldCountVO();
		vo.setPeriodCode(periodCode);
		int profitNum = 0;
		int lossNum = 0;
		if(null != list &&!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				
				if(null != objs[3]){
					PortfolioHoldReturn holdReturn = (PortfolioHoldReturn) objs[3];
					Double increase = holdReturn.getIncrease();
					if(null != increase){
						if(increase > 0){
							profitNum++;
						}else if(increase < 0) {
							lossNum++;
						}
					}
				}							
			}			
		}
		
		vo.setProfitNum(String.valueOf(profitNum));
		vo.setLossNum(String.valueOf(lossNum));		
		return vo;
	}
	
	
	/**
	 * 获取IFA客户持仓组合列表
	 * @author zxtan
	 * @date 2017-03-24
	 */
	public JsonPaging findPortfolioHoldList(JsonPaging jsonPaging,JSONObject jsonObject) {
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String minReturn = jsonObject.optString("minReturn","");
		String maxReturn = jsonObject.optString("maxReturn","");
		String minAmount = jsonObject.optString("minAmount","");
		String maxAmount = jsonObject.optString("maxAmount","");
		String toCurrency = jsonObject.optString("toCurrency","");
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1M);
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);

		String sort = jsonObject.optString("sort","0");//排序字段		
		String order = jsonObject.optString("order","DESC");//方向	
		
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" inner join MemberIfa i on i.id=h.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=h.member.id and c.ifa.id=i.id ");
		hql.append(" left join PortfolioHoldReturn r on r.portfolioHold.id=h.id and r.periodCode=? ");
		hql.append(" where i.member.id=? ");
		hql.append(" and exists (select p.id from PortfolioHoldProduct p where p.portfolioHold.id=h.id ) ");
		List<Object> params = new ArrayList<Object>();
		params.add(periodCode);
		params.add(memberId);
		
		if(!"".equals(keyword)){
			hql.append(" and h.portfolioName like ?");
			params.add("%"+keyword+"%");
		}
		
		if(!"".equals(minReturn)){
			hql.append(" and r.increase >=?");
			params.add(Double.parseDouble(minReturn));
		}		
		if(!"".equals(maxReturn)){
			hql.append(" and r.increase <=?");
			params.add(Double.parseDouble(maxReturn));
		}
		
		if("".equals(toCurrency)){
			if(!"".equals(minAmount)){
				hql.append(" and h.totalAsset >=?");
				params.add(Double.parseDouble(minAmount));
			}
			
			if(!"".equals(maxAmount)){
				hql.append(" and h.totalAsset <=?");
				params.add(Double.parseDouble(maxAmount));
			}
		}else {
			if(!"".equals(minAmount)){
				hql.append(" and get_exchange_rate(h.baseCurrency,?,h.totalAsset) >=?");
				params.add(toCurrency);
				params.add(Double.parseDouble(minAmount));
			}
			
			if(!"".equals(maxAmount)){
				hql.append(" and get_exchange_rate(h.baseCurrency,?,h.totalAsset) <=?");
				params.add(toCurrency);
				params.add(Double.parseDouble(maxAmount));
			}
		}
		
		if(",0,1,2,".indexOf(sort)>-1){
			if("0".equals(sort)){
				sort = " get_exchange_rate(h.baseCurrency,?,h.totalAsset) ";//总资产
				params.add(toCurrency);
			}else if("1".equals(sort)){
				sort = " r.increase ";//回报率
			}else if("2".equals(sort)){
				sort = " h.createTime ";//投资天数
			}
			jsonPaging.setOrder(order);
			hql.append(" order by "+ sort+" " + order);
		}
		
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		List<AppHoldItemVO> voList = new ArrayList<AppHoldItemVO>();
		List list = jsonPaging.getList();
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list &&!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				PortfolioHold hold = (PortfolioHold) objs[0];
				MemberIfa ifa = (MemberIfa) objs[1];
				CrmCustomer customer = (CrmCustomer) objs[2];
				MemberBase member = customer.getMember();
				
				AppHoldItemVO vo = new AppHoldItemVO();
				vo.setHoldId(hold.getId());
				vo.setPortfolioName(hold.getPortfolioName());
				vo.setBaseCurrency(hold.getBaseCurrency());
				
				if("".equals(toCurrency)){
					vo.setToCurrency(getParamConfigName(langCode, hold.getBaseCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
					vo.setTotalAsset(getFormatNum(hold.getTotalAsset(),hold.getBaseCurrency()));
				}else {
					vo.setToCurrency(toCurrencyName);
					vo.setTotalAsset(getFormatNumByCurrency(hold.getTotalAsset(),hold.getBaseCurrency(),toCurrency));
				}
				vo.setRiskLevel(String.valueOf(hold.getRiskLevel()));
				double totalReturnRate = hold.getTotalReturnRate()==null?0:hold.getTotalReturnRate();
				double ifaMin = ifa.getPortfolioCriticalValue()==null?0:Double.parseDouble(ifa.getPortfolioCriticalValue())/100.0;
				double ifaMax = ifa.getPortfolioReturnValue()==null?0:Double.parseDouble(ifa.getPortfolioReturnValue())/100.0;
				if(totalReturnRate> ifaMax || totalReturnRate <ifaMin){
					vo.setAlertFlag("1");
				}else {
					vo.setAlertFlag("0");
				}
				
				try {
					int investDays = DateUtil.daysBetween(hold.getCreateTime(), DateUtil.getCurDate());
					vo.setInvestDays(String.valueOf(investDays));					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				vo.setMemberId(member.getId());
				vo.setClientName(getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null));
				
				if(null != objs[3]){
					PortfolioHoldReturn holdReturn = (PortfolioHoldReturn) objs[3];
					vo.setPeriodCode(holdReturn.getPeriodCode());
					vo.setIncrease(getFormatNumByPer(holdReturn.getIncrease()));
				}
				
				voList.add(vo);				
			}
		
			jsonPaging.setList(voList);
		}
		
		
		return jsonPaging;
	}
	
	
	/**
	 * 获取IFA客户组合进行中列表
	 * @author zxtan
	 * @date 2017-04-25
	 */
	public JsonPaging findPortfolioOrderPlanList(JsonPaging jsonPaging,JSONObject jsonObject) {
		String memberId = jsonObject.optString("memberId","");
		String keyword = jsonObject.optString("keyword","");
		String finishStatus = jsonObject.optString("finishStatus","");		
		String minAmount = jsonObject.optString("minAmount","");
		String maxAmount = jsonObject.optString("maxAmount","");
		String toCurrency = jsonObject.optString("toCurrency","");
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" inner join MemberIfa i on i.id=h.ifa.id ");
		hql.append(" inner join CrmCustomer c on c.member.id=h.member.id and c.ifa.id=i.id ");
		hql.append(" inner join OrderPlan o on o.portfolioHold.id=h.id ");
		hql.append(" where i.member.id=? ");
		hql.append(" and exists (select p.id from OrderPlan p where p.portfolioHold.id=h.id and ( FIND_IN_SET(p.finishStatus,'1,2,3,4')>0 or (FIND_IN_SET(p.finishStatus,'-1,0')>0 and p.creator.id=? ) ) ) ");
		List<Object> params = new ArrayList<Object>();
		params.add(memberId);
		params.add(memberId);
		
		if(!"".equals(keyword)){
			hql.append(" and h.portfolioName like ?");
			params.add("%"+keyword+"%");
		}
		
		if(!"".equals(finishStatus)){
			if ("0".equals(finishStatus)) {
				hql.append(" and ( o.finishStatus ='-1' or o.finishStatus='0' )");
			}else {
				hql.append(" and o.finishStatus =? ");
				params.add(finishStatus);
			}			
		}		
		
		if("".equals(toCurrency)){
			if(!"".equals(minAmount)){
				hql.append(" and o.totalBuy >=? ");
				params.add(Double.parseDouble(minAmount));
			}
			
			if(!"".equals(maxAmount)){
				hql.append(" and o.totalBuy <=? ");
				params.add(Double.parseDouble(maxAmount));
			}
		}else {
			if(!"".equals(minAmount)){
				hql.append(" and get_exchange_rate(o.baseCurrency,?,o.totalBuy) >=? ");
				params.add(toCurrency);
				params.add(Double.parseDouble(minAmount));
			}
			
			if(!"".equals(maxAmount)){
				hql.append(" and get_exchange_rate(o.baseCurrency,?,o.totalBuy) <=? ");
				params.add(toCurrency);
				params.add(Double.parseDouble(maxAmount));
			}
		}
		
		hql.append(" order by o.lastUpdate desc ");
		jsonPaging = baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		
		List list = jsonPaging.getList();
		String toCurrencyName = getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY);
		if(null != list &&!list.isEmpty()){
			List<AppHoldOrderPlanVO> voList = new ArrayList<AppHoldOrderPlanVO>();
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				PortfolioHold hold = (PortfolioHold) objs[0];
//				MemberIfa ifa = (MemberIfa) objs[1];
				CrmCustomer customer = (CrmCustomer) objs[2];
				MemberBase member = customer.getMember();
				OrderPlan plan = (OrderPlan) objs[3];
				
				AppHoldOrderPlanVO vo = new AppHoldOrderPlanVO();
				vo.setHoldId(hold.getId());
				vo.setPortfolioName(hold.getPortfolioName());
				vo.setRiskLevel(String.valueOf(hold.getRiskLevel()));								
				vo.setMemberId(member.getId());
				vo.setClientName(getCommonMemberName(member.getId(), langCode, CommonConstants.MEMBER_NAME_REAL_NAME));
				vo.setIconUrl(PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null));
				
				vo.setIfFirst(hold.getIfFirst());
				vo.setOrderPlanId(plan.getId());
				vo.setOrderPlanCreatorId(plan.getCreator().getId());
				vo.setOrderPlanCreateTime(DateUtil.dateToDateString(plan.getCreateTime(), CommonConstants.FORMAT_DATE_TIME));
				vo.setFinishStatus(plan.getFinishStatus());
				vo.setBaseCurrency(plan.getBaseCurrency());
								
				if("".equals(toCurrency)){
					vo.setToCurrency(getParamConfigName(langCode, plan.getBaseCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
					vo.setTotalBuy(getFormatNum(plan.getTotalBuy(),plan.getBaseCurrency()));
				}else {
					vo.setToCurrency(toCurrencyName);
					vo.setTotalBuy(getFormatNumByCurrency(plan.getTotalBuy(),plan.getBaseCurrency(),toCurrency));
				}
				
				String sql = "CALL pro_getOrderPlanCount(?)";
				List subParams = new ArrayList();
				subParams.add(hold.getId());
				List subList = this.springJdbcQueryManager.springJdbcQueryForList(sql,subParams.toArray());
				Iterator it = (Iterator) subList.iterator();
			    while (it.hasNext()) {
			    	Map map = (HashMap) it.next();
			    	
			    	if("1".equals(hold.getIfFirst())){
						//申购中					
						int productCount,successCount,pendCount,failCount;
						successCount = Integer.parseInt(getMapValue(map,"success_count"));
						pendCount = Integer.parseInt(getMapValue(map,"pend_count"));
						failCount = Integer.parseInt(getMapValue(map,"fail_count"));
						productCount = successCount+pendCount+failCount;
						
						vo.setProductCount(String.valueOf(productCount));
						vo.setSuccessCount(String.valueOf(successCount));
						vo.setPendCount(String.valueOf(pendCount));
						vo.setFailCount(String.valueOf(failCount));
						
					}else {
						//调整中
						int buyCount,sellCount;//,switchCount;
						buyCount = Integer.parseInt(getMapValue(map,"buy_count"));
						sellCount = Integer.parseInt(getMapValue(map,"sell_count"));
						
						vo.setBuyCount(String.valueOf(buyCount));
						vo.setSellCount(String.valueOf(sellCount));
					}
			    }
				
				voList.add(vo);				
			}
		
			jsonPaging.setList(voList);
		}
		
		
		return jsonPaging;
	}
		

	/**
	 * 得到投资组合基本信息
	 * @author zxtan
	 * @param portfolioHoldId 组合ID
	 * @param toCurrency 货币
	 * @return
	 */
	public AppHoldVO findPortfolioHold(String portfolioHoldId,String toCurrency,String langCode){
		AppHoldVO vo = new AppHoldVO();
		String hql = " from PortfolioHold h where h.id=? ";
		List params = new ArrayList();
		params.add(portfolioHoldId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			PortfolioHold hold = (PortfolioHold)list.get(0);
			vo.setId(hold.getId());
			
			vo.setCreateTime(DateUtil.dateToDateString(hold.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setPortfolioName(hold.getPortfolioName());
			//vo.setRiskLevel(String.valueOf(info.getRiskLevel()));			
			vo.setLastUpdate(DateUtil.dateToDateString(hold.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
		    vo.setTotalReturnRate( getFormatNumByPer(hold.getTotalReturnRate()));
		    if("".equals(toCurrency)){
		    	vo.setBaseCurrencyCode(hold.getBaseCurrency());
		    	vo.setBaseCurrency(getParamConfigName(langCode, hold.getBaseCurrency(), CommonConstants.SYS_PARAM_TYPE_CURRENCY));
			    vo.setTotalMarketValue(getFormatNum(hold.getTotalMarketValue(),hold.getBaseCurrency()));
			    vo.setTotalReturnValue(getFormatNum(hold.getTotalReturnValue(),hold.getBaseCurrency()));
			    vo.setTotalAsset(getFormatNum(hold.getTotalAsset(),hold.getBaseCurrency()));
			    vo.setTotalCash(getFormatNum(hold.getTotalCash(),hold.getBaseCurrency()));
		    }else {
		    	vo.setBaseCurrencyCode(toCurrency);
		    	vo.setBaseCurrency(getParamConfigName(langCode, toCurrency, CommonConstants.SYS_PARAM_TYPE_CURRENCY));
		    	Double rate = getExchangeRate(hold.getBaseCurrency(),toCurrency);
		    	if(rate != null){
		    		vo.setTotalMarketValue( getFormatNumByRate(hold.getTotalMarketValue(), rate,toCurrency));
		    		vo.setTotalReturnValue( getFormatNumByRate(hold.getTotalReturnValue(), rate,toCurrency));
		    		vo.setTotalAsset( getFormatNumByRate(hold.getTotalAsset(), rate,toCurrency));
		    		vo.setTotalCash( getFormatNumByRate(hold.getTotalCash(), rate,toCurrency));
		    	}else {
		    		vo.setTotalMarketValue( getFormatNum(null));
				    vo.setTotalReturnValue( getFormatNum(null));
				    vo.setTotalAsset( getFormatNum(null));
				    vo.setTotalCash( getFormatNum(null));
				}			    
			}
		    
		    MemberIfa ifa = hold.getIfa();
		    vo.setIfaId(ifa.getId());
		    vo.setIfaMemberId(ifa.getMember().getId());
		    String ifaName = getCommonMemberName(ifa.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME);
			vo.setIfaName(ifaName);	
			
			String hasOrderPlan = checkHoldOrderPlan(portfolioHoldId);
			vo.setHasOrderPlan(hasOrderPlan);
		}
		return vo;
	}
	
	/**
	 * 是否有进行中的计划
	 * @param holdId
	 * @return
	 */
	private String checkHoldOrderPlan(String holdId){
		String hql = "from OrderPlan t where t.portfolioHold.id=? and t.finishStatus in ('1','2','3') ";
		List<Object> params = new ArrayList<Object>();
		params.add(holdId);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null != list && !list.isEmpty()){
			return String.valueOf(list.size());
		}else {
			return "0";
		}
	}
	
	/**
	 * 得到投资组合回报
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param periodCode 回报周期
	 * @return
	 */
	public AppHoldReturnVO findPortfolioHoldReturn(String portfolioHoldId,String periodCode){
		AppHoldReturnVO vo =new AppHoldReturnVO();
		String hql = " from PortfolioHoldReturn p where p.portfolioHold.id=? and p.periodCode=? ";
		List params = new ArrayList();
		params.add(portfolioHoldId);
		params.add(periodCode);
		List list = baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			PortfolioHoldReturn info=(PortfolioHoldReturn)list.get(0);
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
	
	/**
	 * 得到某个投资组合行情数据
	 * @author zxtan
	 * @date 2016-11-30
	 * @param portfolioId 组合ID
	 * @param startDate 起始时间
	 * @return
	 */
	public List<AppHoldCumperfVO> findPortfolioHoldCumperfList(String portfolioHoldId,String startDate){
		List<AppHoldCumperfVO> cumperfList = new ArrayList<AppHoldCumperfVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldCumperf t inner join PortfolioHold h on t.portfolioHold.id=h.id where 1=1 ");
		List params = new ArrayList();
		if(null!=portfolioHoldId && !"".equals(portfolioHoldId)){
			hql.append(" and t.portfolioHold.id=? ");
			params.add(portfolioHoldId);
		}
		if(null!=startDate && !"".equals(startDate)){
			hql.append( "and t.valuationDate>=? ");
			params.add(DateUtil.getDate(startDate));
		}
		
		hql.append(" order by t.valuationDate ");

		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[]) list.get(i);
			PortfolioHoldCumperf cumperf = (PortfolioHoldCumperf) objs[0];
			PortfolioHold hold = (PortfolioHold) objs[1];
			AppHoldCumperfVO vo = new AppHoldCumperfVO();
			
			vo.setId(cumperf.getId());
			vo.setPortfolioId(hold.getId());
			vo.setValuationDate(DateUtil.dateToDateString(cumperf.getValuationDate(),"yyyy-MM-dd"));
			if(null==cumperf.getCumulativeRate() || "".equals(cumperf.getCumulativeRate().toString())){
				vo.setCumulativeRate(getFormatNum(null));
			}else{
				vo.setCumulativeRate(getFormatNum(cumperf.getCumulativeRate()));
			}
			if(null==cumperf.getCumulativePl() || "".equals(cumperf.getCumulativePl().toString())){
				vo.setCumulativePl(getFormatNum(null));
			}else{
				vo.setCumulativePl(getFormatNum(cumperf.getCumulativePl(),cumperf.getBaseCurrency()));
			}
			if(null==cumperf.getDayPl() || "".equals(cumperf.getDayPl().toString())){
				vo.setDayPl(getFormatNum(null));
			}else{
				vo.setDayPl(getFormatNum(cumperf.getDayPl(),cumperf.getBaseCurrency()));
			}
			vo.setCreateTime(DateUtil.dateToDateString(cumperf.getCreateTime(),"yyyy-MM-dd HH:mm:ss"));
			vo.setLastUpdate(DateUtil.dateToDateString(cumperf.getLastUpdate(),"yyyy-MM-dd HH:mm:ss"));
			cumperfList.add(vo);
		}
		return cumperfList;
	}
	
	/**
	 * 得到我的组合详情的基金产品信息列表
	 * @author zxtan
	 * @date 2017-03-27
	 * @param portfolioHoldId 组合Id
	 * @param productType 产品类型
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public List<AppHoldProductVO> findPortfolioHoldProductList(String portfolioHoldId,String productType,String langCode,String toCurrency){
		List<AppHoldProductVO>  voList = new ArrayList<AppHoldProductVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" Inner join PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" Inner join ProductInfo p on p.id=m.product.id ");
		hql.append(" where h.id=? and p.type=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		params.add(productType);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			double totalAmount = 0;	
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				AppHoldProductVO  vo = new AppHoldProductVO();
				PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];

				String productId = holdProduct.getProduct().getId();
				AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
				
				
				vo.setHoldProductId(holdProduct.getId());
				vo.setPortfolioHoldId(holdProduct.getPortfolioHold().getId());
				vo.setProductId(productId);
				vo.setProductName(productInfoVO.getProductName());	
				vo.setProductType(productType);
				vo.setBaseCurrency(toCurrency);
									
				double marketValue = 0;
				double refCost= 0;
				double returnValue = 0;
				double returnRate = 0;

				double lastNav = productInfoVO.getLastNav()==null?0:productInfoVO.getLastNav();
				double unit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
				refCost = holdProduct.getReferenceCost()==null?lastNav:holdProduct.getReferenceCost();
				
				Double rate = getExchangeRate(holdProduct.getBaseCurrency(), toCurrency);
				if(null != rate){				
					marketValue = lastNav * unit * rate;					
					returnValue = (lastNav - refCost)* unit * rate;
					returnRate = refCost>0?(lastNav - refCost)/refCost:0;					
				}
							
				totalAmount += marketValue;
				
				vo.setMarketValue(getFormatNum(marketValue,toCurrency));
				vo.setReturnValue(getFormatNum(returnValue,toCurrency));
				vo.setReturnRate(getFormatNumByPer(returnRate));				
				
							
				voList.add(vo);
			}
			
			for (int i = 0; i < voList.size(); i++) {
				AppHoldProductVO vo = voList.get(i);
				double marketValue = 0;
				if(null != vo.getMarketValue()){
					marketValue = Double.parseDouble(vo.getMarketValue().replace(",", ""));			
				}
				double weight = 0;
				if (totalAmount != 0){
					weight = marketValue / totalAmount;
				}
					
				vo.setWeight(getFormatNumByPer(weight));
				voList.set(i, vo);
			}
		}
		
		return voList;
	}
	
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppHoldAllocationVO> findPortfolioHoldAllocationList(String portfolioHoldId,String langCode,String toCurrency){
		List<AppHoldAllocationVO>  voList = new ArrayList<AppHoldAllocationVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join ProductInfo f on f.id=m.product.id ");		
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		List<AppHoldProductVO> productVOList = new ArrayList<AppHoldProductVO>();
		double fundTotal = 0;
		double bondTotal = 0;
		double stockTotal = 0;
		double insureTotal = 0;
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
			AppHoldProductVO  vo = new AppHoldProductVO();
			//vo.setId(product.getId());
			double holdingUnit = product.getHoldingUnit()==null?0:product.getHoldingUnit();
//			double availableUnit = product.getAvailableUnit()==null?0:product.getAvailableUnit();
			
//			vo.setHoldingUnit(product.getHoldingUnit());			
//			vo.setAvailableUnit(product.getAvailableUnit());
			

			ProductInfo info = (ProductInfo) objs[1];
			String productType = info.getType();
			vo.setProductType(productType);	

			String productId = info.getId();

			AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
			String fromCurrency = productInfoVO.getBaseCurrency();
			
			Double referenceCost;
			Double lastPrice;
			if(null != toCurrency && !"".equals(toCurrency)){
				vo.setBaseCurrency(toCurrency);
				referenceCost = getNumByCurrency(product.getReferenceCost(), product.getBaseCurrency(), toCurrency);
				lastPrice = getNumByCurrency(productInfoVO.getLastNav(), fromCurrency, toCurrency);
			}else {
				vo.setBaseCurrency(product.getBaseCurrency());
				referenceCost = product.getReferenceCost();
				lastPrice = getNumByCurrency(productInfoVO.getLastNav(), fromCurrency, product.getBaseCurrency());
			}
			lastPrice = lastPrice==null?0:lastPrice;
//			vo.setReferenceCost(referenceCost);
			
			Double totalMarketValue = lastPrice*holdingUnit;
			Double returnRate = (referenceCost == null || referenceCost == 0)?0:(lastPrice-referenceCost)/referenceCost;
			vo.setMarketValue(String.valueOf(totalMarketValue));
			vo.setReturnRate(String.valueOf(returnRate));
			
			if("fund".equalsIgnoreCase(productType)){
				fundTotal += totalMarketValue;
			}else if ("bond".equalsIgnoreCase(productType)) {
				bondTotal += totalMarketValue;
			}else if ("stock".equalsIgnoreCase(productType)) {
				stockTotal += totalMarketValue;
			}else if ("insure".equalsIgnoreCase(productType)) {
				insureTotal += totalMarketValue;
			}
						
			productVOList.add(vo);	
		}
		
		
		double fundRate = 0;
		double bondRate = 0;
		double stockRate = 0;
		double insureRate = 0;
		for (int i = 0; i < productVOList.size(); i++) {
			AppHoldProductVO  vo = productVOList.get(i);
			double marketValue = vo.getMarketValue()==null?0:Double.parseDouble(vo.getMarketValue());
			double returnRate = vo.getReturnRate()==null?0:Double.parseDouble(vo.getReturnRate());
			String productType = vo.getProductType();
			
			if("fund".equalsIgnoreCase(productType)&& fundTotal != 0){
					fundRate += marketValue * returnRate / fundTotal;
				
			}else if ("bond".equalsIgnoreCase(productType)&& bondTotal != 0){
					bondRate += marketValue * returnRate / bondTotal;
				
			}else if ("stock".equalsIgnoreCase(productType)&& stockTotal != 0){
					stockRate += marketValue * returnRate / stockTotal;
				
			}else if ("insure".equalsIgnoreCase(productType)&& insureTotal != 0){
					insureRate += marketValue * returnRate / insureTotal;
				
			}
		}
		
		double cashTotal = findPortfolioHoldCash(portfolioHoldId, toCurrency);
		double total = fundTotal+bondTotal+stockTotal+insureTotal+cashTotal;
		
		if(fundTotal>0){
			AppHoldAllocationVO vo = genPortfolioAllocation("fund", fundTotal, fundRate, total,toCurrency);
			voList.add(vo);
		}
		
		if(bondTotal>0){		
			AppHoldAllocationVO vo = genPortfolioAllocation("bond", bondTotal, bondRate, total,toCurrency);
			voList.add(vo);
		}
		
		if(stockTotal>0){
			AppHoldAllocationVO vo = genPortfolioAllocation("stock", stockTotal, stockRate, total,toCurrency);
			voList.add(vo);
		}

		if(insureTotal>0){
			AppHoldAllocationVO vo = genPortfolioAllocation("insure", insureTotal, insureRate, total,toCurrency);
			voList.add(vo);
		}
		
		if(cashTotal>0){
			AppHoldAllocationVO vo = genPortfolioAllocation("cash", cashTotal, 0, total,toCurrency);
			voList.add(vo);
		}
		
		return voList;
	}
	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId
	 * @param productId
	 * @return
	 */
	private double findPortfolioHoldCash(String portfolioHoldId,String toCurrency){
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldAccount m ");		
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		double totalCash = 0;
		List<PortfolioHoldAccount> list = baseDao.find(hql.toString(), params.toArray(),false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				PortfolioHoldAccount holdAccount = list.get(i);
				double cash = holdAccount.getTotalCash()==null?0:holdAccount.getTotalCash();
				Double rate = getExchangeRate(holdAccount.getBaseCurrency(), toCurrency);
				if(null != rate){
					totalCash += cash*rate;
				}
			}
		}
		return totalCash;
	}
		
	/**
	 * 生成组合配置情况
	 * @author zxtan
	 * @date 2016-12-27
	 * @param itemName
	 * @param itemValue
	 * @param itemIncrease
	 * @param total
	 * @param toCurrency
	 * @return
	 */
	private AppHoldAllocationVO genPortfolioAllocation(String itemName,double itemValue,double itemIncrease,double total,String toCurrency)
	{
		AppHoldAllocationVO vo = new AppHoldAllocationVO();
		vo.setItemName(itemName);
		vo.setItemValue(getFormatNum(itemValue));
		vo.setItemIncrease(getFormatNumByPer(itemIncrease));
		double rate = itemValue/total;
		vo.setItemRate(getFormatNum(rate));
		vo.setItemCurrency(toCurrency);
		return vo;
	}
	
	
	/**
	 * 得到投资组合的基金配置情况
	 * @author zxtan
	 * @date 2017-02-17
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppPieChartItemVO> findPortfolioFundAllocationList(String portfolioHoldId,String langCode,String toCurrency,String groupType){

		List<AppPieChartItemVO>  voList = new ArrayList<AppPieChartItemVO>();
		
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" INNER JOIN PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" LEFT JOIN ProductInfo p on p.id=m.product.id ");	
		hql.append(" LEFT JOIN FundInfo f ON f.product.id = m.product.id ");
		hql.append(" LEFT JOIN "+this.getLangString("FundInfo", langCode)+" l ON f.id = l.id ");	
		hql.append(" where p.type='fund' and m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		double total = 0;
		Map<String, Double> map = new HashMap<String, Double>();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
				PortfolioHold hold = (PortfolioHold) objs[1];
				FundInfo fundInfo = (FundInfo) objs[3];
				FundInfoSc fundInfoSc = new FundInfoSc();
				BeanUtils.copyProperties(objs[4], fundInfoSc);
				
				String keyString = fundInfo.getId();
							
				toCurrency = "".equals(toCurrency)? hold.getBaseCurrency():toCurrency;
				String fromCurrency = fundInfoSc.getFundCurrencyCode()==null?product.getBaseCurrency():fundInfoSc.getFundCurrencyCode();
				
				double holdingUnit = product.getHoldingUnit()==null?0:product.getHoldingUnit(); 
				double lastNav = fundInfo.getLastNav()==null?0:fundInfo.getLastNav();
				Double totalMarketValue = 0.0;
				totalMarketValue = lastNav * holdingUnit;
				totalMarketValue = getNumByCurrency(totalMarketValue, fromCurrency, toCurrency);
				totalMarketValue = totalMarketValue==null?0:totalMarketValue;
				total += totalMarketValue;
				if(map.containsKey(keyString)){
					map.put(keyString, map.get(keyString) + totalMarketValue);	
				}else {
					map.put(keyString, totalMarketValue);
				}
			}
		}

		Map<String, Double> mapFP = new HashMap<String, Double>();
		for (Map.Entry<String, Double> entry : map.entrySet()) {
			
			String fundId = entry.getKey();
			double weight = entry.getValue()/total;			
			
			hql = new StringBuilder();
			hql.append(" from FundInfo f ");	
			hql.append(" inner JOIN FundPortfolio p ON p.fund.id = f.id ");	
			hql.append(" inner JOIN "+this.getLangString("FundPortfolio", langCode)+" l ON l.id = p.id ");	
			hql.append(" where f.id=? and p.type=? ");
			
			params = new ArrayList();
			params.add(fundId);
			params.add(groupType);
			
			List listFP = baseDao.find(hql.toString(), params.toArray(), false);
			
			if(null != listFP && !listFP.isEmpty()){			

				for(int i=0;i<listFP.size();i++){
					Object[] objs = (Object[])listFP.get(i);				
//					FundInfo fundInfo = (FundInfo) objs[0];
					FundPortfolio fundPortfolio = (FundPortfolio) objs[1];
					FundPortfolioSc fundPortfolioSc = new FundPortfolioSc();
					BeanUtils.copyProperties(objs[2], fundPortfolioSc);
//					String productId = fundInfo.getProduct().getId();
					
					String keyString = fundPortfolioSc.getName();
					double rate = (fundPortfolio.getRate()==null)?0:fundPortfolio.getRate();
					
					if(mapFP.containsKey(keyString)){
						mapFP.put(keyString, mapFP.get(keyString) + weight*rate/100.0);	
					}else {
						mapFP.put(keyString, weight*rate/100.0);
					}
				}
			}
		}	
		
		for (Map.Entry<String, Double> entry : mapFP.entrySet()) {
			AppPieChartItemVO vo = new AppPieChartItemVO();
			vo.setItemId(null);
			vo.setItemName(entry.getKey());
			double weight = entry.getValue();
			weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
			vo.setItemWeight(String.valueOf(weight));
			voList.add(vo);
		}
		
		return voList;
	}
	
	
	/**
	 * 得到投资客户的交易记录列表
	 * @author zxtan
	 * @date 2017-03-29
	 * @return
	 */
	public JsonPaging findOrderHistoryList(JsonPaging jsonPaging,String ifaMemberId,String keyword,String langCode,int num){
		List<AppOrderHistoryHoldItemVO>  voHoldList = new ArrayList<AppOrderHistoryHoldItemVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHold h ");
		hql.append(" inner join MemberIfa i on i.id=h.ifa.id ");
		hql.append(" inner join MemberBase b on b.id=h.member.id ");
		hql.append(" where  i.member.id=? ");
		hql.append(" and exists ( select s.id from OrderHistory s where s.portfolioHold.id=h.id ) ");
		List params = new ArrayList();
		params.add(ifaMemberId);
		
		
		if(!"".equals(keyword)){
			hql.append(" and h.portfolioName like ? ");
			params.add("%"+keyword+"%");
		}		
		hql.append(" order by h.lastUpdate desc ");			
				
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql.toString(), params.toArray(), jsonPaging, false);
		
		List list = jsonPaging.getList();
		if(null != list && !list.isEmpty()){
			for(int i=0;i<list.size();i++){
				int rows = jsonPaging.getRows()==null?10:jsonPaging.getRows();
				int page = jsonPaging.getPage()==null?1:jsonPaging.getPage();
				long headId = rows*(page-1)+1;
				
				Object[] objs = (Object[])list.get(i);
				PortfolioHold hold = (PortfolioHold) objs[0];
				MemberBase member = (MemberBase) objs[2];
				AppOrderHistoryHoldItemVO holdItemVO = new AppOrderHistoryHoldItemVO();
				holdItemVO.setHoldId(hold.getId());
				holdItemVO.setPortfolioName(hold.getPortfolioName());
				holdItemVO.setMemberId(member.getId());
				holdItemVO.setClientName(getCommonMemberName(member.getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME));
				holdItemVO.setIconUrl( PageHelper.getUserHeadUrlForWS(member.getIconUrl(), null) );
				
				
				List<AppOrderHistoryVO>  voList = new ArrayList<AppOrderHistoryVO>();
				hql = new StringBuilder();
				hql.append(" from OrderHistory m ");
				hql.append(" inner join InvestorAccount a on a.id=m.account.id ");
				hql.append(" inner join FundInfo f on f.product.id=m.product.id ");		
				hql.append(" inner join "+this.getLangString("FundInfo", langCode)+ " s on s.id=f.id ");
				hql.append(" inner join PortfolioHold p on p.id=m.portfolioHold.id ");
				hql.append(" where  p.id=? ");
				hql.append(" order by m.updateTime desc ");	
				params = new ArrayList();
				params.add(hold.getId());
				List hisList = baseDao.find(hql.toString(), params.toArray(),false,num);
				if(null != hisList && !hisList.isEmpty()){					
					for (int j = 0; j < hisList.size(); j++) {
						Object[] subObjs = (Object[])hisList.get(j);
						OrderHistory history = (OrderHistory) subObjs[0];
						OrderPlan plan = history.getPlan();						
						InvestorAccount account = (InvestorAccount) subObjs[1];
						AppOrderHistoryVO vo = new AppOrderHistoryVO();
						FundInfoSc fundInfo = new FundInfoSc();				
						BeanUtils.copyProperties(subObjs[3], fundInfo);
						vo.setFundName(fundInfo.getFundName());			
						PortfolioHold portfolioHold = (PortfolioHold) subObjs[4];
						
						vo.setId(history.getId());
						if(null != plan){
							vo.setPlanId(plan.getId());
							if(j==0){
								holdItemVO.setOrderPlanStatus(plan.getFinishStatus());								
							}
							vo.setOrderPlanStatus(holdItemVO.getOrderPlanStatus());
						}
												
						vo.setHoldId(portfolioHold.getId());
						vo.setPortfolioName(portfolioHold.getPortfolioName());
						vo.setMemberId(history.getMember().getId());
						vo.setClientName(getCommonMemberName(history.getMember().getId(), langCode,CommonConstants.MEMBER_NAME_REAL_NAME));

						vo.setAccountNo(account.getAccountNo());
						vo.setCurrencyCode(history.getCurrencyCode());
						vo.setCommissionUnit(String.valueOf(history.getCommissionUnit()));
						vo.setCommissionPrice(getFormatNum(history.getCommissionPrice(),history.getCurrencyCode()));
						vo.setCommissionAmount(getFormatNum(history.getCommissionAmount(),history.getCurrencyCode()));
						vo.setTransactionAmount(getFormatNum(history.getTransactionAmount(),history.getCurrencyCode()));
						vo.setFee(getFormatNum(history.getFee(),history.getCurrencyCode()));
						vo.setTransactionUnit( String.valueOf(history.getTransactionUnit()));		
										
						vo.setOrderType(history.getOrderType());
						vo.setOrderDate(DateUtil.dateToDateString(history.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
						vo.setCloseTime(DateUtil.dateToDateString(history.getCloseTime(), "yyyy-MM-dd HH:mm:ss"));
						vo.setUpdateTime(DateUtil.dateToDateString(history.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
						
						String status = "-1";
						if(",3,".indexOf(","+history.getStatus()+",")>-1){
							//成功
							status = "3";
						}else if(",4,5,-1,".indexOf(history.getStatus())>-1){
							//失败
							status = "-1";
						}else {
							//处理中 1,2,-10,6,7
							status = "1";
						}
						vo.setStatus(status);
						vo.setIfAip(history.getIfAip());
						vo.setSwitchFlag(history.getSwitchFlag());
						vo.setHeadId(headId);
						voList.add(vo);
					}
				}
				

				holdItemVO.setOrderHistoryList(voList);
				voHoldList.add(holdItemVO);
			}
		}
		jsonPaging.setList(voHoldList);
		return jsonPaging;	
	}
	

	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param holdProductId 组合持仓产品表的主键ID
	 * @return
	 */
	public AppHoldProductDetailVO findPortfolioHoldProductDetail(String holdProductId,String langCode,String toCurrency){

		AppHoldProductDetailVO  vo = new AppHoldProductDetailVO();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" left join PortfolioHold p on p.id=m.portfolioHold.id ");
		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" left join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" left join InvestorAccount a on a.id=m.account.id ");	
		hql.append(" where m.id=? ");
		
		List params = new ArrayList();
		params.add(holdProductId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		if(null != list && !list.isEmpty()){
			Object[] objs = (Object[])list.get(0);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
			PortfolioHold hold = (PortfolioHold) objs[1];
			FundInfo fundInfo = (FundInfo) objs[2];
			FundInfoSc fundInfoSc = new FundInfoSc();
			if(null != objs[3]){
				BeanUtils.copyProperties(objs[3], fundInfoSc);
				vo.setFundId(fundInfoSc.getId());
				vo.setFundName(fundInfoSc.getFundName());
				vo.setFundType(fundInfoSc.getFundType());
			}
			
			vo.setId(product.getId());
			String productId = product.getProduct().getId();
			String portfolioHoldId = hold.getId();
			vo.setProductId(productId);
			vo.setPortfolioHoldId(portfolioHoldId);
			vo.setPortfolioName(hold.getPortfolioName());
			if(null != fundInfo.getRiskLevel()){
				vo.setRiskLevel(fundInfo.getRiskLevel().toString());
			}
			vo.setFundCurrency(fundInfoSc.getFundCurrency());
			vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
			vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
			vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());
			vo.setToCurrency(toCurrency);
			
			double lastNav = getNumByCurrency(fundInfo.getLastNav(), fundInfoSc.getFundCurrencyCode(), toCurrency);
			vo.setLastNav(getFormatNum(lastNav,toCurrency));
			vo.setLastNavUpdate(DateUtil.dateToDateString(fundInfo.getLastNavUpdate(), "yyyy-MM-dd"));
			vo.setDayReturn(getFormatNumByPer(fundInfo.getDayReturn()));
			Double cost = 0.0;
			if(null != product.getReferenceCost()){
				cost = getNumByCurrency(product.getReferenceCost(), product.getBaseCurrency(), toCurrency);
				vo.setReferenceCost( getFormatNum( cost,toCurrency ));
			}
			Double unit = 0.0;
			if(null != product.getHoldingUnit()){
				unit = product.getHoldingUnit();
				//Double unitPrice = cost / unit;
				vo.setHoldingUnit(String.valueOf(unit));
				//vo.setReferenceUnitPrice(getFormatNum(unitPrice));
			}
			if(null != product.getAvailableUnit()){
				vo.setAvailableUnit(product.getAvailableUnit().toString());
			}
			
			double totalMarketValue = lastNav*unit ;
			double totalPl = (lastNav-cost)*unit;
			Double cumperfRate = cost==0?null:1.0*(lastNav-cost)/cost;
							
			vo.setMarketValue(getFormatNum(totalMarketValue,toCurrency));
			vo.setReturnValue(getFormatNum(totalPl,toCurrency));
			vo.setCumperfRate(getFormatNumByPer(cumperfRate));	
			vo.setValuationDate(DateUtil.dateToDateString(fundInfo.getLastNavUpdate(), "yyyy-MM-dd"));
			
			double totalAmount = findPortfolioFundTotalMarketValue(portfolioHoldId,toCurrency);
			if(totalAmount > 0){
				double rate = totalMarketValue/totalAmount;
				vo.setAllocationRate(getFormatNumByPer(rate));
			}
			
			
			if(null != objs[4]){
				InvestorAccount account = (InvestorAccount) objs[4];
				vo.setDividend(account.getDividend());
				vo.setAccountId(account.getId());
			}
			
			
		}
				
		return vo;
	}
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	private double findPortfolioFundTotalMarketValue(String portfolioId,String toCurrency){
//		List<AppPortfolioProductVo>  messList = new ArrayList<AppPortfolioProductVo>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" inner join FundInfoEn s on s.id=f.id ");
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		double totalAmount = 0.0;
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];			
//			String productId = product.getProduct().getId();
			FundInfo fundInfo = (FundInfo) objs[1];
			FundInfoEn fundInfoEn = (FundInfoEn) objs[2];
			double unit = product.getHoldingUnit()==null?0.0:product.getHoldingUnit();
			double lastNav = fundInfo.getLastNav()==null?0.0:fundInfo.getLastNav();
			Double marketValue = getNumByCurrency(lastNav*unit, fundInfoEn.getFundCurrencyCode(), toCurrency);
//			marketValue = marketValue==null?0.0:marketValue;
			if(null != marketValue){
				totalAmount += marketValue;		
			}
		}
				
		return totalAmount;
	}
	
	/**
	 * 获取产品最新一条回报数据
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioId
	 * @param productId
	 * @return
	 */
	public List<AppHoldProductCumperfVO> findPortfolioHoldProductCumperfList(String portfolioHoldId,String productId,String startDate,String toCurrency){
		List<AppHoldProductCumperfVO>  voList = new ArrayList<AppHoldProductCumperfVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProductCumperf m ");		
		hql.append(" where m.portfolioHold.id=? and m.product.id=? ");
		hql.append(" and m.valuationDate > ? ");
		hql.append(" order by m.valuationDate ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		params.add(productId);
		params.add(DateUtil.getDate(startDate));
		
		List<PortfolioHoldProductCumperf> list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				PortfolioHoldProductCumperf cumperf = list.get(i);
				AppHoldProductCumperfVO vo = new AppHoldProductCumperfVO();
				
				vo.setId(cumperf.getId());
				vo.setCumperfRate(getFormatNum(cumperf.getCumperfRate()));
				if(null == toCurrency || "".equals(toCurrency)){
					vo.setBaseCurrency(cumperf.getBaseCurrency());
					vo.setTotalPl(getFormatNum(cumperf.getTotalPl(),cumperf.getBaseCurrency()));
					vo.setDayPl(getFormatNum(cumperf.getDayPl(),cumperf.getBaseCurrency()));
					vo.setTotalMarketValue(getFormatNum(cumperf.getTotalMarketValue(),cumperf.getBaseCurrency()));
				}else {
					vo.setBaseCurrency(toCurrency);
					Double rate=getExchangeRate(cumperf.getBaseCurrency(),toCurrency);
					vo.setTotalPl(getFormatNumByRate(cumperf.getTotalPl(), rate,toCurrency));
					vo.setDayPl(getFormatNumByRate(cumperf.getDayPl(), rate,toCurrency));
					vo.setTotalMarketValue(getFormatNumByRate(cumperf.getTotalMarketValue(), rate,toCurrency));
				}

				vo.setValuationDate(DateUtil.dateToDateString(cumperf.getValuationDate(),"yyyy-MM-dd"));
				vo.setCreateTime(DateUtil.dateToDateString(cumperf.getCreateTime(),"yyyy-MM-dd"));
				vo.setLastUpdate(DateUtil.dateToDateString(cumperf.getLastUpdate(),"yyyy-MM-dd"));
				
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2016-12-27
	 * @param portfolioHoldId 组合ID
	 * @param productId 产品ID
	 * @param toCurrency 显示货币
	 * @return
	 */
	public List<AppOrderHistoryVO> findPortfolioOrderHistoryList(String portfolioHoldId,String productId,String toCurrency,String langCode,String keyword){
		List<AppOrderHistoryVO>  messList = new ArrayList<AppOrderHistoryVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from OrderHistory m ");
		hql.append(" left join InvestorAccount a on a.id=m.account.id ");
		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" left join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" left join PortfolioHold p on p.id=m.portfolioHold.id ");
		hql.append(" where 1=1 ");

		List params = new ArrayList();
				
		if(!"".equalsIgnoreCase(portfolioHoldId)){
			hql.append(" and m.portfolioHold.id=? ");
			params.add(portfolioHoldId);
		}
		
		if(!"".equalsIgnoreCase(productId)){
			hql.append(" and m.product.id=? ");
			params.add(productId);
		}
		
		if(!"".equalsIgnoreCase(keyword)){
			hql.append(" and ( s.fundName like ? or p.portfolioName like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
		hql.append(" order by m.updateTime desc ");		
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		
		for(int i=0;i<list.size();i++){
			Object[] objs = (Object[])list.get(i);
			OrderHistory history = (OrderHistory) objs[0];
			OrderPlan plan = history.getPlan();
			InvestorAccount account = (InvestorAccount) objs[1];
			AppOrderHistoryVO vo = new AppOrderHistoryVO();
			FundInfoSc fundInfo = new FundInfoSc();
			
			if(null != objs[3]){
				BeanUtils.copyProperties(objs[3], fundInfo);
				vo.setFundName(fundInfo.getFundName());
			}
			if(null != objs[4]){
				PortfolioHold portfolioHold = (PortfolioHold) objs[4];
				vo.setHoldId(portfolioHold.getId());
				vo.setPortfolioName(portfolioHold.getPortfolioName());
			}
			
			vo.setId(history.getId());
			if(null != plan){
				vo.setPlanId(plan.getId());
			}
			//vo.setOrderNo(history.getOrderNo());
			vo.setAccountNo(account.getAccountNo());
			String currencyCode = history.getCurrencyCode();
			vo.setCurrencyCode(toCurrency);
			if(null != history.getCommissionUnit()){
				vo.setCommissionUnit(history.getCommissionUnit().toString());
			}
			
			if(null != history.getCommissionPrice()){
				vo.setCommissionPrice(getFormatNumByCurrency(history.getCommissionPrice(), currencyCode, toCurrency));
			}
			if(null != history.getCommissionAmount()){
				vo.setCommissionAmount(getFormatNumByCurrency(history.getCommissionAmount(),currencyCode,toCurrency));
			}
			
			if(null != history.getTransactionUnit()){
				vo.setTransactionUnit(history.getTransactionUnit().toString());
			}			
			if(null != history.getTransactionAmount()){
				vo.setTransactionAmount(getFormatNumByCurrency(history.getTransactionAmount(),currencyCode,toCurrency));
			}
			if(null != history.getFee()){
				vo.setFee(getFormatNumByCurrency(history.getFee(),currencyCode,toCurrency));
			}	
			vo.setOrderType(history.getOrderType());
			vo.setOrderDate(DateUtil.dateToDateString(history.getOrderDate(), "yyyy-MM-dd HH:mm:ss"));
			vo.setCloseTime(DateUtil.dateToDateString(history.getCloseTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setUpdateTime(DateUtil.dateToDateString(history.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
			vo.setStatus(history.getStatus());
			vo.setIfAip(history.getIfAip());
			vo.setSwitchFlag(history.getSwitchFlag());
			vo.setHeadId((long) 0);
			messList.add(vo);
		}
				
		return messList;
	}
	
	/**
	 * 获取组合的费用列表
	 * @author zxtan
	 * @date 2017-02-07
	 * @param holdId
	 * @param langCode
	 * @param toCurrency
	 * @return
	 */
	public List<AppHoldFeeVO> findPortfolioFeeList(String holdId,String langCode,String toCurrency){
		List<AppHoldFeeVO> voList = new ArrayList<AppHoldFeeVO>();
		StringBuilder hql = new StringBuilder();
		hql.append(" from OrderHistory o ");
		hql.append(" inner join FundInfo f on f.product.id=o.product.id ");
		hql.append(" inner join "+getLangString("FundInfo",langCode)+" l on l.id=f.id ");
		hql.append(" inner join PortfolioHold h on h.id=o.portfolioHold.id ");		
		hql.append(" where o.status in ('2','3') and h.id=? ");
		hql.append(" order by o.updateTime desc ");
		List<String> params = new ArrayList<String>();
		params.add(holdId);
		
		List list = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				AppHoldFeeVO vo = new AppHoldFeeVO();
				Object[] objs = (Object[]) list.get(i);
				OrderHistory orderHistory = (OrderHistory) objs[0];
				FundInfoSc fundInfo = new FundInfoSc();
				BeanUtils.copyProperties(objs[2], fundInfo);
				
				vo.setProductId(orderHistory.getProduct().getId());
				vo.setHoldId(holdId);
				vo.setFundName(fundInfo.getFundName());
				if(null != toCurrency && !"".equals(toCurrency)){
					vo.setCurrency(toCurrency);
					vo.setFee(getFormatNumByCurrency(orderHistory.getFee(), orderHistory.getCurrencyCode(), toCurrency));
				}else {
					vo.setCurrency(orderHistory.getCurrencyCode());
					vo.setFee(getFormatNum(orderHistory.getFee(),orderHistory.getCurrencyCode()));
				}
				vo.setCreateTime(DateUtil.dateToDateString(orderHistory.getUpdateTime(), "yyyy-MM-dd HH:mm:ss"));
				voList.add(vo);
			}
		}
				
		return voList;
	}
	
	
	/**
	 * 得到我的资产的产品回报信息
	 * @author zxtan
	 * @date 2017-03-03
	 * @param holdProductId 持仓产品Id
	 * @param langCode 语言
	 * @param toCurrency 货币
	 * @return
	 */
	public AppHoldProductReturnDetailVO findHoldProductReturnDetail(String holdProductId,String langCode,String toCurrency){
		StringBuilder hql = new StringBuilder();
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" Inner join PortfolioHold h on h.id=m.portfolioHold.id ");
		hql.append(" Inner join ProductInfo p on p.id=m.product.id ");
		hql.append(" where m.id=? ");
		
		List params = new ArrayList();
		params.add(holdProductId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);

		AppHoldProductReturnDetailVO  vo = new AppHoldProductReturnDetailVO();
		if(null != list && !list.isEmpty()){
			
			Object[] objs = (Object[])list.get(0);
			PortfolioHoldProduct holdProduct = (PortfolioHoldProduct) objs[0];

			String productId = holdProduct.getProduct().getId();
			AppProductInfoVO productInfoVO = findProductInfo(productId, langCode);
			
			
			vo.setHoldProductId(holdProduct.getId());
			vo.setPortfolioHoldId(holdProduct.getPortfolioHold().getId());
			vo.setProductId(productId);
			vo.setProductName(productInfoVO.getProductName());	
			vo.setProductCode(productInfoVO.getProductCode());
			vo.setProductType(holdProduct.getProduct().getType());
								
			double marketValue = 0;
			double refCost= 0;
			double returnValue = 0;
			double returnRate = 0;

			double availableUnit = holdProduct.getAvailableUnit()==null?0:holdProduct.getAvailableUnit();
			double unit = holdProduct.getHoldingUnit()==null?0:holdProduct.getHoldingUnit();
			double lastNav = productInfoVO.getLastNav()==null?0:productInfoVO.getLastNav();
			refCost = holdProduct.getReferenceCost()==null?lastNav:holdProduct.getReferenceCost();
			
			vo.setHoldingUnit(getFormatNum(unit));
			vo.setAvailableUnit(getFormatNum(availableUnit));
			
			if("".equals(toCurrency)){	
				vo.setLastPrice(getFormatNum(lastNav,productInfoVO.getBaseCurrency()));
				vo.setRefCost(getFormatNum(refCost,holdProduct.getBaseCurrency()));
				vo.setBaseCurrency(holdProduct.getBaseCurrency());
				marketValue = lastNav * unit;					
				returnValue = (lastNav - refCost)* unit;
				returnRate = refCost>0?(lastNav - refCost)/refCost:0;					
			}else {
				vo.setBaseCurrency(toCurrency);
				Double rate = getExchangeRate(holdProduct.getBaseCurrency(), toCurrency);
				if(null != rate){	
					vo.setLastPrice(getFormatNum(lastNav*rate,toCurrency));
					vo.setRefCost(getFormatNum(refCost*rate,toCurrency));
					marketValue = lastNav * unit * rate;					
					returnValue = (lastNav - refCost)* unit * rate;
					returnRate = refCost>0?(lastNav - refCost)/refCost:0;					
				}
			}
						
			vo.setMarketValue(getFormatNum(marketValue,vo.getBaseCurrency()));
			vo.setReturnValue(getFormatNum(returnValue,vo.getBaseCurrency()));
			vo.setReturnRate(getFormatNumByPer(returnRate));				
			vo.setAccountNo(holdProduct.getAccountNo());
		}
		
		return vo;
	}
	
	/**
	 * 得到投资组合的产品信息列表
	 * @author zxtan
	 * @date 2017-02-27
	 * @param portfolioId 组合ID
	 * @return
	 */
	public List<AppHoldProductFundVO> findPortfolioProductFundList(String portfolioHoldId,String langCode,String toCurrency){
		List<AppHoldProductFundVO>  messList = new ArrayList<AppHoldProductFundVO>();
		StringBuilder hql = new StringBuilder();
//		hql.append("select m.id,m.holdingUnit,s.fundName,m.baseCurrency,s.fundType,f.riskLevel,f.lastNav,m.referenceCost,m.product.id,m.availableUnit ");
//		hql.append(" from PortfolioHoldProduct m ");
//		hql.append(" left join FundInfo f on f.product.id=m.product.id ");		
//		hql.append(" left join "+this.getLangString("FundInfo", langCode));
//		hql.append(" s on s.id=f.id ");
//		hql.append(" where m.portfolioHold.id=? ");
		
		hql.append(" from PortfolioHoldProduct m ");
		hql.append(" inner join FundInfo f on f.product.id=m.product.id ");		
		hql.append(" inner join "+this.getLangString("FundInfo", langCode));
		hql.append(" s on s.id=f.id ");
		hql.append(" where m.portfolioHold.id=? ");
		
		List params = new ArrayList();
		params.add(portfolioHoldId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		double totalAmount = 0;
		if(null != list && !list.isEmpty()){
			String toCurrencyName = "";
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				AppHoldProductFundVO  vo = new AppHoldProductFundVO();
				PortfolioHoldProduct product = (PortfolioHoldProduct) objs[0];
				FundInfo fundInfo = (FundInfo) objs[1];
				FundInfoSc fundInfoSc = new FundInfoSc();
				BeanUtils.copyProperties(objs[2], fundInfoSc);
				
				vo.setId(product.getId());			
				vo.setFundId(fundInfo.getId());
				//vo.setAllocationRate(objs[1]==null?"":objs[1].toString());
				vo.setFundName(fundInfoSc.getFundName());
//				String fromCurrency = product.getBaseCurrency();
				
				if(null == toCurrency || "".equals(toCurrency)){
					toCurrency = product.getPortfolioHold().getBaseCurrency();
					toCurrencyName = getParamConfigName(langCode, toCurrency, "currency_type");
				}
				
				vo.setFundCurrency(fundInfoSc.getFundCurrency());
				vo.setFundCurrencyCode(fundInfoSc.getFundCurrencyCode());
				vo.setIssueCurrency(fundInfoSc.getIssueCurrency());
				vo.setIssueCurrencyCode(fundInfoSc.getIssueCurrencyCode());				
				vo.setFundType(fundInfoSc.getFundType());
				vo.setRiskLevel(String.valueOf(fundInfo.getRiskLevel()));

				double lastNav = fundInfo.getLastNav()==null?0:fundInfo.getLastNav();
				double refCost = product.getReferenceCost()==null?0:product.getReferenceCost();
				if(!"".equals(toCurrency)){
					vo.setToCurrency(toCurrencyName);
					lastNav = getNumByCurrency(lastNav, fundInfoSc.getFundCurrencyCode(), toCurrency);
					refCost = getNumByCurrency(refCost, product.getBaseCurrency(), toCurrency);
					vo.setLastNav(getFormatNum(lastNav,toCurrency));
				}else {
					vo.setToCurrency(getParamConfigName(langCode, fundInfoSc.getFundCurrencyCode(), "currency_type"));
					vo.setLastNav(getFormatNum(fundInfo.getLastNav(),fundInfoSc.getFundCurrencyCode()));					
				}
				
							
				String productId = product.getProduct() == null? "": product.getProduct().getId();
				double holdingUnit = product.getHoldingUnit()==null?0:product.getHoldingUnit();
				vo.setHoldingUnit(getFormatNum(product.getHoldingUnit()));
				vo.setAvailableUnit(getFormatNum(product.getAvailableUnit()));
				vo.setProductId(productId);
				vo.setPortfolioHoldId(portfolioHoldId);
				
				

				Double marketValue = lastNav*holdingUnit;					
				Double totalPl = (lastNav-refCost)*holdingUnit;				
				Double cumperfRate = (lastNav-refCost)/refCost;
				
				totalAmount += marketValue;
				
				vo.setMarketValue(getFormatNum(marketValue,toCurrency));
				vo.setReturnValue(getFormatNum(totalPl,toCurrency));
				vo.setCumperfRate(getFormatNumByPer(cumperfRate));	
				
				vo.setAccountId(product.getAccount()==null?null:product.getAccount().getId());
				vo.setAccountNo(product.getAccountNo());
							
				messList.add(vo);
			}
			
			for (int i = 0; i < messList.size(); i++) {
				AppHoldProductFundVO  vo = messList.get(i);
				double marketValue = 0;
				if(null != vo.getMarketValue()){
					marketValue = Double.parseDouble(vo.getMarketValue().replace(",", ""));			
				}
				double rate = 0;
				if (totalAmount != 0)
				{
					rate = marketValue / totalAmount;
				}
					
				vo.setAllocationRate(getFormatNumByPer(rate));
				messList.set(i, vo);
			}
		}
		
		return messList;
	}

}
