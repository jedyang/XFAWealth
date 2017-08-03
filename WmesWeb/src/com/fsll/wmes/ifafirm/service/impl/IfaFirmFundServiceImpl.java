package com.fsll.wmes.ifafirm.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.service.SysParamService;
import com.fsll.core.vo.SysCountryVO;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.distributor.service.DistributorService;
import com.fsll.wmes.distributor.vo.AccountFitlerVO;
import com.fsll.wmes.entity.DistributorOrg;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.IfafirmDistributor;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberAdmin;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfafirm;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.ifafirm.service.IfaFirmFundService;
import com.fsll.wmes.ifafirm.service.IfafirmManageService;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmBaseVO;
import com.fsll.wmes.ifafirm.vo.MemberIfafirmVO;
import com.fsll.wmes.investor.service.InvestorService;
import com.fsll.wmes.investor.vo.AccountVO;
import com.fsll.wmes.strategy.vo.CharDataVO;
import com.fsll.wmes.web.service.CountryService;


/***
 * 业务接口实现类：工作流管理接口类
 * @author 林文伟
 * @date 2016-06-22
 */
@Service("ifafirmFundService")
//@Transactional
public class IfaFirmFundServiceImpl extends BaseService implements IfaFirmFundService {
	
	@Autowired
	private CountryService countryService;

	@Autowired
	private InvestorService investorService;
	@Autowired
	private SysParamService sysParamService;
	@Autowired
	private IfafirmManageService ifafirmManageService;
	@Autowired
	private FundInfoService fundInfoService;

	
	
	/**
	 * 获取代理商基金列表(Fund Screener页面)
	 * modify by wwlin
	 * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging findFundInfoList(JsonPaging jsonPaging, FundScreenerDataVO filters){
		StringBuffer hql = new StringBuffer("SELECT DISTINCT i.id, i.isinCode, i.riskLevel, i.lastNav, i.lastNavUpdate,i.dayReturn, i.issuePrice, i.issueDate, " +
				"i.mgtFee, i.minInitialAmount, i.minSubscribeAmount, i.minHoldingAmount, i.minRedemptionAmount, " +
				"i.minRspAmount, i.createTime, i.lastUpdate, i.isValid, f.id, f.fundName, f.domicileCode, f.domicile, f.fundHouse, " +
				"f.fundManCo, f.fundManager, f.fundSize, f.fundCurrencyCode, f.fundCurrency, f.fundTypeCode, f.fundType, f.sectorTypeCode, " +
				"f.sectorType, f.geoAllocationCode, f.geoAllocation, f.invTarget, f.keyRisks,i.product.id,p.symbolCode,p.transactionFeeRate,p.transactionFeeCur,p.transactionFeeMini,md.logofile FROM FundInfo i ");
		List<Object> params = new ArrayList<Object>();

		hql.append(" LEFT JOIN FundReturn r ON i.id=r.fund.id AND r.periodCode='return_period_code_3M' ");
		hql.append(" LEFT JOIN FundInfoCount c ON i.id=c.id ");//基金按人气排序时使用
		hql.append(" LEFT JOIN ProductDistributor p ON p.product.id=i.product.id ");//代理商关联的产品信息
		hql.append(" LEFT JOIN MemberDistributor md ON p.distributor.id=md.id ");//代理商
		
		hql.append(" left join "+this.getLangString("FundInfo", filters.getLangCode()));
		hql.append(" f on i.id=f.id where i.isValid='1' ");//and p.distributor.id='40280a2958b401690158b415a4290003' 
		
		if( null != filters.getFundID() && filters.getFundID().indexOf(",")>-1 ){
			String[] fundIds = filters.getFundID().split(",");
			hql.append(" and ( 1=2 ");
			for (int i = 0; i < fundIds.length; i++) {
				hql.append( " or i.id = '"+fundIds[i]+"' " );
			}
			hql.append( "  ) " );
		}
		
		String idString = StrUtils.seperateWithQuote(filters.getFundHouseIds(), "'");
		if(idString != null && !"".equals(idString)){
			hql.append(" and f.fundHouseId.id in ( "+idString+" ) ");
//			params.add(idString);
		}
		if(filters.getFundHouse() != null && !"".equals(filters.getFundHouse())){
			hql.append(" and f.fundHouse like ? ");
			params.add("%"+filters.getFundHouse()+"%");
		}
		
		if(filters.getFundSizeFrom() != null && !"".equals(filters.getFundSizeFrom())){
			hql.append(" and f.fundSize >= ? ");
			params.add(filters.getFundSizeFrom());
		}
		if(filters.getFundSizeTo() != null && !"".equals(filters.getFundSizeTo())){
			hql.append(" and f.fundSize <= ? ");
			params.add(filters.getFundSizeTo());
		}
		
		if(filters.getDomicile() != null && !"".equals(filters.getDomicile())){
			hql.append(" and f.domicile = ? ");
			params.add(filters.getDomicile());
		}
		
		if(filters.getCurrency() != null && !"".equals(filters.getCurrency())){
			hql.append(" and f.fundCurrency = ? ");
			params.add(filters.getCurrency());
		}
		
		if(filters.getFundType() != null && !"".equals(filters.getFundType())){
			hql.append(" and f.fundType = ? ");
//			params.add(filters.getFundType());
			params.add(filters.getFundType());
		}
		
		if(filters.getSectorType() != null && !"".equals(filters.getSectorType())){
			hql.append(" and f.sectorType = ? ");
			params.add(filters.getSectorType());
		}
		
		if(filters.getGeoAllocation() != null && !"".equals(filters.getGeoAllocation())){
			hql.append(" and f.geoAllocation = ? ");
			params.add(filters.getGeoAllocation());
		}
		
		if(filters.getRiskRating() != null && !"".equals(filters.getRiskRating())){
			hql.append(" and i.riskLevel = ? ");
			params.add(filters.getRiskRating());
		}
		
		if(filters.getMgtFee() != null && !"".equals(filters.getMgtFee())){
			hql.append(" and i.mgtFee is not null and i.mgtFee < ? ");
			params.add(StrUtils.getDouble(filters.getMgtFee()));
		}
	
		if(filters.getMinInitialInv() != null && !"".equals(filters.getMinInitialInv())){
			hql.append(" and i.minInitialAmount >= ? ");
			params.add(StrUtils.getDouble(filters.getMinInitialInv()));
		}
		
		//评级筛选条件
		StringBuffer ratingHql = new StringBuffer();
		if(filters.getLipperCr() != null && !"".equals(filters.getLipperCr())){
			ratingHql.append(" and ( en.orgId.id=? and r.level= ? ) ");
			params.add(filters.getRatingOrg());
			params.add(StrUtils.getInteger(filters.getLipperCr()));
		}
		if(filters.getFitch() != null && !"".equals(filters.getFitch())){
			ratingHql.append(" and ( en.orgId.id=? and r.level= ? ) ");
			params.add(filters.getRatingOrg());
			params.add(StrUtils.getInteger(filters.getFitch()));
		}
		if(filters.getCitywire() != null && !"".equals(filters.getCitywire())){
			ratingHql.append(" and ( en.orgId.id=? and r.level= ? ) ");
			params.add(filters.getRatingOrg());
			params.add(StrUtils.getInteger(filters.getCitywire()));
		}
		if (ratingHql.length()>0){
			ratingHql = new StringBuffer("select r.fund.id from FundRatingLevel r LEFT JOIN FundRatingLevelEn en on r.id = en.id where r.isValid='1' and r.fund.id is not null").append(ratingHql.toString());
//			hql.append(" and i.id in ( "+ratingHql.toString()+"  order by r.pubDate desc  ) ");
			hql.append(" and i.id in ( "+ratingHql.toString()+" ) ");
		}
		
		//基金回报筛选条件
		StringBuffer returnHql = new StringBuffer();
		if(filters.getPerfLaunchFrom() != null && !"".equals(filters.getPerfLaunchFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_LAUNCH");
			params.add(Double.parseDouble(filters.getPerfLaunchFrom()));
		}
		if(filters.getPerfLaunchTo() != null && !"".equals(filters.getPerfLaunchTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_LAUNCH");
			params.add(Double.parseDouble(filters.getPerfLaunchTo()));
		}
		if(filters.getPerfYTDFrom() != null && !"".equals(filters.getPerfYTDFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_YTD");
			params.add(StrUtils.getDouble(filters.getPerfYTDFrom()));
		}
		if(filters.getPerfYTDTo() != null && !"".equals(filters.getPerfYTDTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_YTD");
			params.add(StrUtils.getDouble(filters.getPerfYTDTo()));
		}
		if(filters.getPerfOneWeekFrom() != null && !"".equals(filters.getPerfOneWeekFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_1W");
			params.add(StrUtils.getDouble(filters.getPerfOneWeekFrom()));
		}
		if(filters.getPerfOneWeekTo() != null && !"".equals(filters.getPerfOneWeekTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_1W");
			params.add(Double.parseDouble(filters.getPerfOneWeekTo()));
		}
		if(filters.getPerfOneMonthFrom() != null && !"".equals(filters.getPerfOneMonthFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_1M");
			params.add(StrUtils.getDouble(filters.getPerfOneMonthFrom()));
		}
		if(filters.getPerfOneMonthTo() != null && !"".equals(filters.getPerfOneMonthTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_1M");
			params.add(StrUtils.getDouble(filters.getPerfOneMonthTo()));
		}
		if(filters.getPerfThreeMonthFrom() != null && !"".equals(filters.getPerfThreeMonthFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_3M");
			params.add(Double.parseDouble(filters.getPerfThreeMonthFrom()));
		}
		if(filters.getPerfThreeMonthTo() != null && !"".equals(filters.getPerfThreeMonthTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_3M");
			params.add(StrUtils.getDouble(filters.getPerfThreeMonthTo()));
		}
		if(filters.getPerfSixMonthFrom() != null && !"".equals(filters.getPerfSixMonthFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_6M");
			params.add(Double.parseDouble(filters.getPerfSixMonthFrom()));
		}
		if(filters.getPerfSixMonthTo() != null && !"".equals(filters.getPerfSixMonthTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_6M");
			params.add(StrUtils.getDouble(filters.getPerfSixMonthTo()));
		}
		if(filters.getPerfOneYearFrom() != null && !"".equals(filters.getPerfOneYearFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_1Y");
			params.add(StrUtils.getDouble(filters.getPerfOneYearFrom()));
		}
		if(filters.getPerfOneYearTo() != null && !"".equals(filters.getPerfOneYearTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_1Y");
			params.add(StrUtils.getDouble(filters.getPerfOneYearTo()));
		}
		if(filters.getPerfThreeYearFrom() != null && !"".equals(filters.getPerfThreeYearFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_3Y");
			params.add(StrUtils.getDouble(filters.getPerfThreeYearFrom()));
		}
		if(filters.getPerfThreeYearTo() != null && !"".equals(filters.getPerfThreeYearTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_3Y");
			params.add(StrUtils.getDouble(filters.getPerfThreeYearTo()));
		}
		if(filters.getPerfFiveYearFrom() != null && !"".equals(filters.getPerfFiveYearFrom())){
			returnHql.append(" and ( r.periodCode=? and r.increase >= ? ) ");
			params.add("return_period_code_5Y");
			params.add(StrUtils.getDouble(filters.getPerfFiveYearFrom()));
		}
		if(filters.getPerfFiveYearTo() != null && !"".equals(filters.getPerfFiveYearTo())){
			returnHql.append(" and ( r.periodCode=? and r.increase <= ? ) ");
			params.add("return_period_code_5Y");
			params.add(StrUtils.getDouble(filters.getPerfFiveYearTo()));
		}
		if (returnHql.length()>0){
//			returnHql = new StringBuffer("select i.fund.id from FundReturn i LEFT JOIN FundReturnEn en on i.id = en.id where i.isValid='1' and i.fund.id is not null ").append(returnHql.toString());
			returnHql = new StringBuffer("select r.fund.id from FundReturn r LEFT JOIN FundReturnEn en on r.id = en.id where r.isValid='1' and r.fund.id is not null ").append(returnHql.toString());
			hql.append(" and i.id in ( "+returnHql.toString()+" ) ");
		}
		
		//		if(filters.getRiskReturnThreeYear() != null && !"".equals(filters.getRiskReturnThreeYear())){
//			hql.append(" and f.fundSize = ? ");
//			params.add(filters.getRiskReturnThreeYear());
//		}
		
		if(filters.getKeyword() != null && !"".equals(filters.getKeyword())){
			hql.append(" and (f.fundName like ? or i.isinCode like ? or p.symbolCode like ? ) ");
			params.add("%"+filters.getKeyword()+"%");
			params.add("%"+filters.getKeyword()+"%");
			params.add("%"+filters.getKeyword()+"%");
			//params.add("%"+filters.getKeyword()+"%");
		}
		
		//限制在distributor及company下
		if(filters.getDistributorId() != null && !"".equals(filters.getDistributorId())){
			String[] disArray = filters.getDistributorId().split(",");
			String distributorSql = "";
			for(String distributorId : disArray){
				distributorSql += "'" + distributorId + "',";
			}
			distributorSql = distributorSql.substring(0, distributorSql.length()-1);
			
			hql.append(" and i.product.id in ( select pd.product.id from ProductDistributor pd where pd.distributor.id in ("+distributorSql+") ) ");
			//params.add(distributorSql);
		} else{
			hql.append(" and i.product.id in ( select pd.product.id from ProductDistributor pd where pd.distributor.id in ('') ) ");
		}

		//限制在运营公司下
		if(null!=filters.getLoginUser() && !"".equals(filters.getLoginUser().getId())){
			hql.append(" and exists ( SELECT pc.id FROM ProductCompany pc ");
			hql.append(" INNER JOIN MemberCompany mc ON mc.company.id=pc.company.id ");
			hql.append(" WHERE pc.product.id=i.product.id AND mc.member.id=? )");
			params.add(filters.getLoginUser().getId());
		}

		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , true);
//		String totolSQL	 = "select count(1) as cnt from ("+hql.toString()+") a";
		List totalList = this.baseDao.find(hql.toString(), params.toArray(), false);//(totolSQL, params.toArray(), false);
		jsonPaging.setTotal(totalList==null?0:totalList.size());
		List voList = jsonPaging.getList();
		List list = new ArrayList();
//		Iterator it = jsonPaging.getList().iterator();
		
		if(!voList.isEmpty()){
			String memberId = null;
			String displayColor = CommonConstants.DEF_DISPLAY_COLOR;
			if(null!=filters.getLoginUser() && !"".equals(filters.getLoginUser())){
				memberId = filters.getLoginUser().getId();
				displayColor = filters.getLoginUser().getDefDisplayColor();
			}
			
			for(int x=0;x<voList.size();x++){
				int idx = 0;
				FundInfoDataVO vo = new FundInfoDataVO();
				Object[] objs = (Object[])voList.get(x);
				//基金基本信息
				vo.setFundInfo(new FundInfo());
				vo.getFundInfo().setId(StrUtils.getString(objs[idx++]));
				vo.getFundInfo().setIsinCode(StrUtils.getString(objs[idx++]));
				vo.getFundInfo().setRiskLevel(StrUtils.getInteger(objs[idx++]));
				vo.getFundInfo().setLastNav(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setLastNavUpdate(DateUtil.getDate(objs[idx++]));
				vo.getFundInfo().setDayReturn(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setIssuePrice(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setIssueDate(DateUtil.getDate(objs[idx++]));
				vo.getFundInfo().setMgtFee(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setMinInitialAmount(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setMinSubscribeAmount(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setMinHoldingAmount(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setMinRedemptionAmount(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setMinRspAmount(StrUtils.getDouble(objs[idx++]));
				vo.getFundInfo().setCreateTime(DateUtil.getDate(objs[idx++]));
				vo.getFundInfo().setLastUpdate(DateUtil.getDate(objs[idx++]));
				vo.getFundInfo().setIsValid(StrUtils.getString(objs[idx++]));
	
				//基金附加信息（多语言）
				vo.setFundId(StrUtils.getString(objs[idx++]));
				vo.setFundName(StrUtils.getString(objs[idx++]));
				vo.setDomicileCode(StrUtils.getString(objs[idx++]));
				vo.setDomicile(StrUtils.getString(objs[idx++]));
//				vo.setFundHouseId(StrUtils.getString(objs[idx++]));
				vo.setFundHouse(StrUtils.getString(objs[idx++]));
				vo.setFundManCo(StrUtils.getString(objs[idx++]));
				vo.setFundManager(StrUtils.getString(objs[idx++]));
				vo.setFundSize(StrUtils.getString(objs[idx++]));
				vo.setFundCurrencyCode(StrUtils.getString(objs[idx++]));
				vo.setFundCurrency(StrUtils.getString(objs[idx++]));
				vo.setFundTypeCode(StrUtils.getString(objs[idx++]));
				vo.setFundType(StrUtils.getString(objs[idx++]));
				vo.setSectorTypeCode(StrUtils.getString(objs[idx++]));
				vo.setSectorType(StrUtils.getString(objs[idx++]));
				vo.setGeoAllocationCode(StrUtils.getString(objs[idx++]));
				vo.setGeoAllocation(StrUtils.getString(objs[idx++]));
				vo.setInvTarget(StrUtils.getString(objs[idx++]));
				vo.setKeyRisks(StrUtils.getString(objs[idx++]));
				
				vo.setProductId(StrUtils.getString(objs[idx++]));
				vo.setSymbolCode(StrUtils.getString(objs[idx++]));
				vo.setTransactionFeeRate( StrUtils.getDouble(StrUtils.getString(objs[idx++])) );
				String transactionFeeCur = StrUtils.getString(objs[idx++]);
				vo.setTransactionFeeCur(transactionFeeCur);
				vo.setTransactionFeeMini(StrUtils.getDouble(StrUtils.getString(objs[idx++])));
				String currencyName = sysParamService.findNameByCode(transactionFeeCur, filters.getLangCode());
				vo.setTransactionFeeCurCurrency(currencyName);
				
				String logoFile = StrUtils.getString(objs[idx++]);
				logoFile = PageHelper.getLogoUrl(logoFile, "D");
				vo.setDistributorLogofile(logoFile);
				//vo.setDistributorIcon(null!=investorAccount.getDistributor()?PageHelper.getLogoUrl(investorAccount.getDistributor().getLogofile(), "D"):"");
//				vo.setTransactionFeeRate( StrUtils.getDouble(StrUtils.getString(objs[idx++])) );
//				String transactionFeeCur = StrUtils.getString(objs[idx++]);
//				vo.setTransactionFeeCur(transactionFeeCur);
//				vo.setTransactionFeeMini(StrUtils.getDouble(StrUtils.getString(objs[idx++])));
//				String currencyName = sysParamService.findNameByCode(transactionFeeCur, filters.getLangCode());
//				vo.setTransactionFeeCurCurrency(currencyName);
				//设置回报及评级信息
				vo = fundInfoService.setFundInfos(vo,filters.getLangCode(),memberId,null,filters.getBeforeYears());
				
				String smallImgUrl = getPerformanceChartImage(vo.getFundId(), CommonConstantsWeb.CHART_IMAGE_SIZE_SMALL, displayColor);
				String middleImgUrl = getPerformanceChartImage(vo.getFundId(), CommonConstantsWeb.CHART_IMAGE_SIZE_MIDDL, displayColor);
				vo.setFundReturnYTDSmallImg(smallImgUrl);
				vo.setFundReturnYTDMiddleImg(middleImgUrl);
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 根据ifafirmId查找所有关系的代理商
	 * @author wwlin
	 * @param memberId 人员id
	 */
	public List<IfafirmDistributor> getIfaFirmDistributorByifaFirmid(String ifafirmId) {
		if(StringUtils.isBlank(ifafirmId)){
			return null;
		}
		String hql = " from IfafirmDistributor r where  r.ifafirm.id=?";
		List<String> params = new ArrayList<String>();
		params.add(ifafirmId);
		List<IfafirmDistributor> list = baseDao.find(hql, params.toArray(),false);
		return list;
	}
}
