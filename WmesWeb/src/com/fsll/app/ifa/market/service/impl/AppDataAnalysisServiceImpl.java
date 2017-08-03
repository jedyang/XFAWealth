package com.fsll.app.ifa.market.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.fsll.app.ifa.market.service.AppDataAnalysisService;
import com.fsll.app.ifa.market.vo.AppDataAnalysisVO;
import com.fsll.app.ifa.market.vo.AppPieChartItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.entity.CrmCustomer;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioHold;

/**
 * IFA市场——统计与分析
 * @author zxtan
 * @date 2017-04-01
 */
@Service("appIfaMarketDataAnalysisService")
public class AppDataAnalysisServiceImpl extends BaseService implements
		AppDataAnalysisService {


	/**
	 * 获取客户回报分析
	 * @author zxtan
	 * @date 2017-04-01
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public List<AppPieChartItemVO> findReturnAnalysis(String ifaMemberId,
			String langCode) {
		List<AppPieChartItemVO> voList = new ArrayList<AppPieChartItemVO>();
		String ltN20 = " < -20%";
		String betweenN20toN10 = "-20% ~ -10%";
		String betweenN10to0 = "-10% ~ 0%";
		String between0to10 = "0% ~ 10%";
		String between10to20 = "10% ~ 20%";
		String between20to50 = "20% ~ 50%";
		String gt50 = " > 50%";
				
		List<AppDataAnalysisVO> list = findDataAnalysisList(ifaMemberId,langCode);
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				double returnRate = list.get(i).getReturnRate();
				String mapKey = "";
				if(returnRate < -0.2){
					mapKey = ltN20;
				}else if (returnRate>=-0.2 && returnRate <-0.1) {
					mapKey = betweenN20toN10;
				}else if (returnRate>=-0.1 && returnRate <0) {
					mapKey = betweenN10to0;
				}else if (returnRate>=0 && returnRate <0.1) {
					mapKey = between0to10;
				}else if (returnRate>=0.1 && returnRate <0.2) {
					mapKey = between10to20;
				}else if (returnRate>=0.2 && returnRate <0.5) {
					mapKey = between20to50;
				}else if (returnRate>=0.5 ) {
					mapKey = gt50;
				}
				
				if(map.containsKey(mapKey)){
					map.put(mapKey, map.get(mapKey) + 1);	
				}else {
					map.put(mapKey, 1);
				}
			}
			
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				AppPieChartItemVO vo = new AppPieChartItemVO();
				vo.setItemId(null);
				vo.setItemName(entry.getKey());
				int itemValue = entry.getValue();
				vo.setItemValue(String.valueOf(itemValue));
				double weight = 1.0*itemValue/list.size();
				weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				vo.setItemWeight(String.valueOf(weight));
				voList.add(vo);
			}
		}
				
		return voList;
	}


	/**
	 * 获取客户资产分析
	 * @author zxtan
	 * @date 2017-04-01
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public List<AppPieChartItemVO> findAssetAnalysis(String ifaMemberId,
			String langCode) {
		List<AppPieChartItemVO> voList = new ArrayList<AppPieChartItemVO>();
		String lt10 = " < 100,000 ";
		String between10to100 = "100,000 ~ 1,000,000";
		String gt100 = " > 1,000,000";
				
		List<AppDataAnalysisVO> list = findDataAnalysisList(ifaMemberId,langCode);
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				double totalAsset = list.get(i).getTotalAsset();
				String mapKey = "";
				if(totalAsset < 100000){
					mapKey = lt10;
				}else if (totalAsset>=100000 && totalAsset <1000000) {
					mapKey = between10to100;
				}else if (totalAsset>=1000000 ) {
					mapKey = gt100;
				}
				
				if(map.containsKey(mapKey)){
					map.put(mapKey, map.get(mapKey) + 1);	
				}else {
					map.put(mapKey, 1);
				}
			}
			
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				AppPieChartItemVO vo = new AppPieChartItemVO();
				vo.setItemId(null);
				vo.setItemName(entry.getKey());
				int itemValue = entry.getValue();
				vo.setItemValue(String.valueOf(itemValue));
				double weight = 1.0*itemValue/list.size();
				weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				vo.setItemWeight(String.valueOf(weight));
				voList.add(vo);
			}
		}
				
		return voList;
	}


	/**
	 * 获取客户地域分析
	 * @author zxtan
	 * @date 2017-04-01
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	public List<AppPieChartItemVO> findGeoAnalysis(String ifaMemberId,
			String langCode) {
		List<AppPieChartItemVO> voList = new ArrayList<AppPieChartItemVO>();
					
		List<AppDataAnalysisVO> list = findDataAnalysisList(ifaMemberId,langCode);
		Map<String, Integer> map = new HashMap<String, Integer>();
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				String mapKey = list.get(i).getGeo();								
				if(map.containsKey(mapKey)){
					map.put(mapKey, map.get(mapKey) + 1);	
				}else {
					map.put(mapKey, 1);
				}
			}
			
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				AppPieChartItemVO vo = new AppPieChartItemVO();
				vo.setItemId(null);
				vo.setItemName(entry.getKey());
				int itemValue = entry.getValue();
				vo.setItemValue(String.valueOf(itemValue));
				double weight = 1.0*itemValue/list.size();
				weight = new BigDecimal(weight).setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
				vo.setItemWeight(String.valueOf(weight));
				voList.add(vo);
			}
		}
				
		return voList;
	}
	
	/**
	 * 获取客户的数据（总资产，总回报）
	 * @param ifaMemberId
	 * @param langCode
	 * @return
	 */
	private List<AppDataAnalysisVO> findDataAnalysisList(String ifaMemberId,String langCode) {
		List<AppDataAnalysisVO> voList = new ArrayList<AppDataAnalysisVO>();
		StringBuilder hql = new StringBuilder();
		hql.append("from CrmCustomer c ");
		hql.append(" inner join MemberIfa i on i.id=c.ifa.id ");
		hql.append(" inner join MemberIndividual d on d.member.id=c.member.id ");
		hql.append(" inner join SysCountry s on s.id=d.nationality.id ");
		hql.append(" where c.clientType='1' and c.isValid='1' and i.member.id=? ");
		List<Object> params = new ArrayList<Object>();
		params.add(ifaMemberId);
		
		List list = baseDao.find(hql.toString(), params.toArray(), false);
		if(null != list && !list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objs = (Object[]) list.get(i);
				CrmCustomer customer = (CrmCustomer) objs[0];
				MemberIfa ifa = (MemberIfa) objs[1];
				MemberBase member = customer.getMember();
				SysCountry country = (SysCountry) objs[3];
				
				String ifaId = ifa.getId();
				String memberId = member.getId();
				String baseCurrency = ifa.getMember().getDefCurrency();
				baseCurrency = ( baseCurrency==null || "".equals(baseCurrency))?CommonConstants.DEF_CURRENCY_HKD:baseCurrency;

				AppDataAnalysisVO vo = new AppDataAnalysisVO();
				vo.setMemberId(memberId);
				vo.setBaseCurrency(baseCurrency);
				String geo = country.getNameSc();
				if("en".equalsIgnoreCase(langCode)){
					geo = country.getNameEn();
				}else if ("tc".equalsIgnoreCase(langCode)) {
					geo = country.getNameTc();
				}
				vo.setGeo(geo);
				
				String hqlHold = "from PortfolioHold h where h.ifa.id=? and h.member.id=? ";
				List<Object> paramsHold = new ArrayList<Object>();
				paramsHold.add(ifaId);
				paramsHold.add(memberId);
				double totalAsset = 0;
				double totalReturnRate = 0;
				List<PortfolioHold> listHold = baseDao.find(hqlHold, paramsHold.toArray(), false);
				if(null != listHold && !listHold.isEmpty()){					
					for (int j = 0; j < listHold.size(); j++) {
						PortfolioHold hold = listHold.get(j);
						Double rate = getExchangeRate(hold.getBaseCurrency(), baseCurrency);
						Double asset = hold.getTotalAsset()==null?0:hold.getTotalAsset();
						if(null != rate){
							asset = asset*rate;
						}
						totalAsset += asset;
					}
					if(totalAsset>0){
						for (int j = 0; j < listHold.size(); j++) {
							PortfolioHold hold = listHold.get(j);
							Double returnRate = hold.getTotalReturnRate()==null?0:hold.getTotalReturnRate();
							Double asset = hold.getTotalAsset()==null?0:hold.getTotalAsset();
							
							totalReturnRate += returnRate*asset/totalAsset;
						}
					}
					
				}
				vo.setTotalAsset(totalAsset);
				vo.setReturnRate(totalReturnRate);
				
				voList.add(vo);
			}
		}
		return voList;
	}
	
	

}
