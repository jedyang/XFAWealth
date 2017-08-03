package com.fsll.wmes.fund.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.StrUtils;
import com.fsll.wmes.fund.service.FundBasicService;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundBasicDataVO;
import com.fsll.wmes.fund.vo.FundBriefDataVO;

@Service("fundBasicService")
//@Transactional
public class FundBasicServiceImpl extends BaseService implements FundBasicService {
	@Autowired
	private FundInfoService fundInfoService;
	
	public JsonPaging initJsonPaging( JSONObject jsonObject ) {
		int page = 1;
		int size = 10;
		if ( jsonObject.containsKey("page") ) {
			page = jsonObject.getInt("page");
		}
		if( jsonObject.containsKey("rows") ){
			size = jsonObject.getInt("rows");
		}
		JsonPaging jsonPaging = new JsonPaging(page, size, "", "");
		return jsonPaging;
	}
	
	//@Transactional(readOnly=true)
	public JsonPaging getFundListData(JSONObject jsonObject, String sqlHead) {
		StringBuffer sqlBody = new StringBuffer(" from fund_info t INNER JOIN ");

		String langCode = "fund_info_";
		langCode += jsonObject.optString("langCode","sc");

		sqlBody.append( langCode+" tl ON t.id = tl.id WHERE t.is_valid = '1' " );
		
		if( jsonObject.containsKey("fundId") ){
			String fundId = jsonObject.getString("fundId");
			sqlBody.append(" and t.id = '"+fundId+"' ");
		}
		
		if (jsonObject.containsKey("fundName")) {
			String fundName = jsonObject.getString("fundName");
			sqlBody.append( " and tl.fund_name like '%"+fundName+"%' " );
		}

		if (jsonObject.containsKey("constraint")) {
			JSONObject constraint = jsonObject.getJSONObject("constraint");
			Iterator<String> iterator = constraint.keys();
			String key = null;
			while (iterator.hasNext()) {
				key = iterator.next();
				sqlBody.append( " and "+key+" = '"+ constraint.getString(key)+"' " );
			}
		}
		
		if( jsonObject.containsKey("orderBy") ){
			sqlBody.append( " ORDER BY "+jsonObject.getString("orderBy") + " " );
		}
		
		String sql = new StringBuffer(sqlHead).append( sqlBody ).toString();
		
		JsonPaging jsonPaging = this.initJsonPaging(jsonObject);
		String[] s = new String[0];
		jsonPaging = this.springJdbcQueryManager.springJdbcQueryForPaging( sql , s, jsonPaging);
		jsonPaging.setTotal( this.springJdbcQueryManager.springJdbcQueryForTotal(sqlBody.toString(), s).intValue() );
		
		return jsonPaging;
	}
	
	
	public JsonPaging fundList(JSONObject jsonObject) {
		String fundBasicDataSqlHead = new String( "SELECT t.*, tl.*, DATE_FORMAT( t.last_nav_update ,'%Y-%m-%d %T') as last_nav_update_str, " +
				"DATE_FORMAT( t.issue_date ,'%Y-%m-%d %T') as issue_date_str, DATE_FORMAT( t.create_time ,'%Y-%m-%d %T') as create_time_str, " +
				"DATE_FORMAT( t.last_update ,'%Y-%m-%d %T') as last_update_str " );
		
		JsonPaging paging = this.getFundListData(jsonObject, fundBasicDataSqlHead);
		List list=paging.getList();
		List listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			HashMap objs = (HashMap)list.get(i);
			FundBasicDataVO  vo = new FundBasicDataVO();
			vo.setFundId(StrUtils.getString(objs.get("ID")));
			vo.setFundName(StrUtils.getString(objs.get("FUND_NAME")));
			vo.setIsin(StrUtils.getString(objs.get("ISIN_CODE")));
			vo.setExchange(StrUtils.getString(objs.get("GEO_ALLOCATION")));//交易市场 ?
			vo.setFundHouse(StrUtils.getString(objs.get("FUND_HOUSE")));
			vo.setFundManCo(StrUtils.getString(objs.get("FUND_MAN_CO")));
			vo.setFundManager(StrUtils.getString(objs.get("FUND_MANAGER")));
			vo.setFundSize(StrUtils.getString(objs.get("FUND_SIZE")));
			vo.setFundCurrency(StrUtils.getString(objs.get("FUND_CURRENCY")));
			vo.setRiskLevel(StrUtils.getString(objs.get("RISK_LEVEL")));
			vo.setFundType(StrUtils.getString(objs.get("FUND_TYPE")));
			vo.setSector(StrUtils.getString(objs.get("SECTOR_TYPE")));
			vo.setGeoAllocation(StrUtils.getString(objs.get("GEO_ALLOCATION")));
			vo.setLastNAV(StrUtils.getString(objs.get("LAST_NAV")));
			vo.setLastNAVUpdate(StrUtils.getString(objs.get("LAST_NAV_UPDATE")));
			vo.setIssuePrice(StrUtils.getString(objs.get("ISSUE_PRICE")));
			vo.setIssueDate(StrUtils.getString(objs.get("ISSUE_DATE")));
			vo.setMgtFee(StrUtils.getString(objs.get("MGT_FEE")));
			vo.setMinInitAmount(StrUtils.getString(objs.get("MIN_INITIAL_AMOUNT")));
			vo.setMinSubscribeAmount(StrUtils.getString(objs.get("MIN_SUBSCRIBE_AMOUNT")));
			vo.setMinHoldingAmount(StrUtils.getString(objs.get("MIN_HOLDING_AMOUNT")));
			vo.setMinRedemptionAmount(StrUtils.getString(objs.get("MIN_REDEMPTION_AMOUNT")));
			vo.setMinRspAmount(StrUtils.getString(objs.get("MIN_RSP_AMOUNT")));
			vo.setInvTarget(StrUtils.getString(objs.get("INV_TARGET")));
			vo.setLastFundReturn(StrUtils.getString(objs.get("DAY_RETURN")));
			vo.setLangCode(jsonObject.optString("langCode","sc"));

			//活动兑换率
			double rate = 0;
			if (jsonObject.containsKey("toCurrency")) {
				String toCurrency = jsonObject.optString("toCurrency","");
				String fromCurrency = vo.getFundCurrency();
				if (!"".equals(fromCurrency) && !"".equals(toCurrency)){
					try {
						rate = fundInfoService.findExchangeRate(fromCurrency, toCurrency).getRate();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			if (rate>0){
				vo.setIssuePrice(String.valueOf(StrUtils.getDoubleVal(vo.getIssuePrice())*rate));
				vo.setLastNAV(String.valueOf(StrUtils.getDoubleVal(vo.getLastNAV())*rate));
				vo.setMinInitAmount(String.valueOf(StrUtils.getDoubleVal(vo.getMinInitAmount())*rate));
				vo.setMinSubscribeAmount(String.valueOf(StrUtils.getDoubleVal(vo.getMinSubscribeAmount())*rate));
				vo.setMinHoldingAmount(String.valueOf(StrUtils.getDoubleVal(vo.getMinHoldingAmount())*rate));
				vo.setMinRedemptionAmount(String.valueOf(StrUtils.getDoubleVal(vo.getMinRedemptionAmount())*rate));
				vo.setMinRspAmount(String.valueOf(StrUtils.getDoubleVal(vo.getMinRspAmount())*rate));
//				vo.setFundSize(String.valueOf(StrUtils.getDoubleVal(vo.getFundSize())*rate));
			}
			listTemp.add(vo);
    	}
		paging.setList(listTemp);
		return paging;
	}
	
	public JsonPaging fundBriefInfo(JSONObject jsonObject) {
		String fundBriefInfoSqlHead = new String( "SELECT t.id, t.isin_code, tl.fund_name, tl.fund_house, " +
				"tl.fund_man_co, tl.fund_manager, tl.fund_size, tl.fund_currency, t.risk_level, tl.fund_type, " +
				"tl.sector_type,tl.geo_allocation, t.last_nav, DATE_FORMAT( t.last_nav_update ,'%Y-%m-%d %T') as last_nav_update, " +
				"t.issue_price, DATE_FORMAT( t.issue_date ,'%Y-%m-%d %T') as issue_date,t.mgt_fee, " +
				"t.day_return, t.min_initial_amount, t.min_rsp_amount, t.min_subscribe_amount, " +
				"t.min_holding_amount, t.min_redemption_amount, tl.inv_target ");
		
		JsonPaging paging = this.getFundListData(jsonObject, fundBriefInfoSqlHead);
		List list=paging.getList();
		List listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			HashMap objs = (HashMap)list.get(i);
			FundBriefDataVO  vo = new FundBriefDataVO();
			vo.setFundId(StrUtils.getString(objs.get("ID")));
			vo.setFundName(StrUtils.getString(objs.get("FUND_NAME")));
			vo.setIsin(StrUtils.getString(objs.get("ISIN_CODE")));
			vo.setFundHouse(StrUtils.getString(objs.get("FUND_HOUSE")));
			vo.setFundCurrency(StrUtils.getString(objs.get("FUND_CURRENCY")));
			vo.setRiskLevel(StrUtils.getString(objs.get("RISK_LEVEL")));
			vo.setFundType(StrUtils.getString(objs.get("FUND_TYPE")));
			vo.setLastNAV(StrUtils.getString(objs.get("LAST_NAV")));
			vo.setLastNAVUpdate(StrUtils.getString(objs.get("LAST_NAV_UPDATE")));
			vo.setMgtFee(StrUtils.getString(objs.get("MGT_FEE")));
			vo.setLangCode(jsonObject.optString("langCode","sc"));

			//活动兑换率
			double rate = 0;
			if (jsonObject.containsKey("toCurrency")) {
				String toCurrency = jsonObject.optString("toCurrency","");
				String fromCurrency = vo.getFundCurrency();
				if (!"".equals(fromCurrency) && !"".equals(toCurrency)){
					try {
						rate = fundInfoService.findExchangeRate(fromCurrency, toCurrency).getRate();
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
			if (rate>0){
				vo.setLastNAV(String.valueOf(StrUtils.getDoubleVal(vo.getLastNAV())*rate));
			}
			
			listTemp.add(vo);
    	}
		paging.setList(listTemp);
		return paging;
	}

}
