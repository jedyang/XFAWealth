/**
 * 
 */
package com.fsll.wmes.fund.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.fsll.common.CommonConstants;
import com.fsll.common.base.service.BaseService;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.common.util.JsonUtil;
import com.fsll.common.util.PageHelper;
import com.fsll.common.util.StrUtils;
import com.fsll.core.CoreConstants;
import com.fsll.core.entity.AccessoryFile;
import com.fsll.core.entity.SysCountry;
import com.fsll.wmes.common.CommonConstantsWeb;
import com.fsll.wmes.entity.BondInfo;
import com.fsll.wmes.entity.FundAnno;
import com.fsll.wmes.entity.FundBonus;
import com.fsll.wmes.entity.FundDoc;
import com.fsll.wmes.entity.FundDocDetail;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.FundMarket;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.entity.StockInfo;
import com.fsll.wmes.entity.WebBusLog;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.entity.WebFollow;
import com.fsll.wmes.entity.WebInvestorVisit;
import com.fsll.wmes.entity.WebWatchlist;
import com.fsll.wmes.fund.service.FundInfoService;
import com.fsll.wmes.fund.vo.FoundFollowVo;
import com.fsll.wmes.fund.vo.FundBasicDataVO;
import com.fsll.wmes.fund.vo.FundHouseDataVO;
import com.fsll.wmes.fund.vo.FundInfoDataVO;
import com.fsll.wmes.fund.vo.FundProductVO;
import com.fsll.wmes.fund.vo.FundScreenerDataVO;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * 基金信息查询服务接口实现
 * @author michael
 * @date 2016-6-20
 */
@Service("fundInfoService")
//@Transactional
public class FundInfoServiceImpl extends BaseService implements FundInfoService {

	/**
	 * 通过ID查找一条基金基本信息
	 * @param id
	 * @return
	 */
	//@Transactional(readOnly = true)
	public FundInfo findFundInfoById(String id){
		FundInfo info = (FundInfo) baseDao.get(FundInfo.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条基金附加英文信息
	 * @param id
	 * @return
	 */
	public FundInfoEn findFundInfoEnById(String id){
		FundInfoEn info = (FundInfoEn) baseDao.get(FundInfoEn.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条基金附加简体中文信息
	 * @param id
	 * @return
	 */
	public FundInfoSc findFundInfoScById(String id){
		FundInfoSc info = (FundInfoSc) baseDao.get(FundInfoSc.class, id);
		return info;
	}
	
	/**
	 * 通过ID查找一条基金附加繁体中文信息
	 * @param id
	 * @return
	 */
	public FundInfoTc findFundInfoTcById(String id){
		FundInfoTc info = (FundInfoTc) baseDao.get(FundInfoTc.class, id);
		return info;
	}
	
	/**
	 * 获取基金列表(Fund Screener页面)
	 * modify by mqzou 2016-10-18 增加查询字段
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
				"f.sectorType, f.geoAllocationCode, f.geoAllocation, f.invTarget, f.keyRisks,i.product.id FROM FundInfo i ");
		List<Object> params = new ArrayList<Object>();

		hql.append(" LEFT JOIN FundReturn r ON i.id=r.fund.id AND r.periodCode='return_period_code_3M' ");
		hql.append(" LEFT JOIN FundInfoCount c ON i.id=c.id ");//基金按人气排序时使用
		
		hql.append(" left join "+this.getLangString("FundInfo", filters.getLangCode()));
		hql.append(" f on i.id=f.id where i.isValid='1' ");
		
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
			hql.append(" and (f.fundName like ? or i.isinCode like ? ) ");
			params.add("%"+filters.getKeyword()+"%");
			params.add("%"+filters.getKeyword()+"%");
			//params.add("%"+filters.getKeyword()+"%");
		}
		
		//限制在distributor及company下
		if(filters.getDistributorId() != null && !"".equals(filters.getDistributorId())){
			hql.append(" and i.product.id in ( select pd.product.id from ProductDistributor pd where pd.distributor.id=? ) ");
			params.add(filters.getDistributorId());
		}
		
		//限制在distributor及company下
		if(filters.getIfaFirmId() != null && !"".equals(filters.getIfaFirmId())){
			hql.append(" and i.product.id in ( select pd.product.id from ProductIfafirmDistributor pd where pd.ifafirm.id=? ");
			params.add(filters.getIfaFirmId());
			if(filters.getDistributorId() != null && !"".equals(filters.getDistributorId())){
				hql.append(" and pd.distributor.id=? ");
				params.add(filters.getDistributorId());
			}
			hql.append(" ) ");
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
			Iterator it=voList.iterator();
			while (it.hasNext()) {
				Object[] objs = (Object[]) it.next();
				int idx = 0;
				FundInfoDataVO vo = new FundInfoDataVO();
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
				//设置回报及评级信息
				vo = setFundInfos(vo,filters.getLangCode(),memberId,null,filters.getBeforeYears());
				
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
	 * 获取基金列表(Fund Screener页面)新版
	 * @author zxtan
	 * @date 2016-10-17
	 * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging findFundInfoListData(JsonPaging jsonPaging, FundScreenerDataVO filters,String dataFlag){
		StringBuffer hql = new StringBuffer("SELECT i.id, i.isinCode, i.riskLevel, i.lastNav, i.lastNavUpdate,i.dayReturn, i.issuePrice, i.issueDate, " +
				"i.mgtFee, i.minInitialAmount, i.minSubscribeAmount, i.minHoldingAmount, i.minRedemptionAmount, " +
				"i.minRspAmount, i.createTime, i.lastUpdate, i.isValid, f.id, f.fundName, f.domicileCode, f.domicile, f.fundHouse, " +
				"f.fundManCo, f.fundManager, f.fundSize, f.fundCurrencyCode, f.fundCurrency, f.fundTypeCode, f.fundType, f.sectorTypeCode, " +
				"f.sectorType, f.geoAllocationCode, f.geoAllocation, f.invTarget, f.keyRisks,i.product.id FROM FundInfo i ");
		List params = new ArrayList();

		hql.append(" LEFT JOIN FundReturn r ON i.id=r.fund.id AND r.periodCode='return_period_code_3M' ");
//		hql.append(" LEFT JOIN FundInfoCount c ON i.id=c.id ");
		hql.append(" left join "+this.getLangString("FundInfo", filters.getLangCode()));
		hql.append(" f on i.id=f.id where i.isValid='1' ");
		
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
		

		String currencyString = StrUtils.seperateWithQuote(filters.getCurrency(), "'");
		if(filters.getCurrency() != null && !"".equals(filters.getCurrency())){
			hql.append(" and f.fundCurrencyCode in ( "+currencyString+" ) ");
		}

		String fundtypeString = StrUtils.seperateWithQuote(filters.getFundType(), "'");
		if(filters.getFundType() != null && !"".equals(filters.getFundType())){			
			hql.append(" and f.fundTypeCode in ( "+fundtypeString+" ) ");
		}

		String sectorString = StrUtils.seperateWithQuote(filters.getSectorType(), "'");
		if(filters.getSectorType() != null && !"".equals(filters.getSectorType())){
			hql.append(" and f.sectorTypeCode in ( "+sectorString+" ) ");
		}

		String geoString = StrUtils.seperateWithQuote(filters.getGeoAllocation(), "'");
		if(filters.getGeoAllocation() != null && !"".equals(filters.getGeoAllocation())){
			hql.append(" and f.geoAllocationCode in ( "+geoString+" ) ");
		}
		
//		String distributorString = StrUtils.seperateWithQuote(filters.getDistributor(), "'");
//		if(filters.getDistributor() != null && !"".equals(filters.getDistributor())){
//			hql.append(" AND EXISTS ( SELECT * FROM ProductDistributor d WHERE d.product.id = l.product.id AND d.distributor.id in ('1','2','3','4') ) ");
//		}
		
		String inceptionDateFrom = filters.getInceptionDateFrom();
		if( inceptionDateFrom != null && !"".equals( inceptionDateFrom) ){
			int unit = Integer.parseInt(inceptionDateFrom);
			Date fromDate = DateUtil.getInternalDateByYear(new Date(), -unit) ;
			
			hql.append(" AND i.issueDate >= ? ");
			
			params.add(fromDate);
		}
		
		String inceptionDateTo = filters.getInceptionDateTo();
		if(inceptionDateTo != null && !"".equals( inceptionDateTo) ){
			int unit = Integer.parseInt(inceptionDateTo);
			Date toDate = DateUtil.getInternalDateByYear(new Date(), -unit) ;
			
			hql.append(" AND i.issueDate <= ? ");
			params.add(toDate);
		}
		
		
		if(filters.getRiskRating() != null && !"".equals(filters.getRiskRating())){
			hql.append(" and i.riskLevel = ? ");			
			params.add( StrUtils.getInt(filters.getRiskRating()));
		}
		
		if(filters.getMgtFeeFrom() != null && !"".equals(filters.getMgtFeeFrom())){
			hql.append(" and i.mgtFee >= ? ");
			params.add(StrUtils.getDouble(filters.getMgtFeeFrom()));
		}
		
		if(filters.getMgtFeeTo() != null && !"".equals(filters.getMgtFeeTo())){
			hql.append(" and i.mgtFee <= ? ");
			params.add(StrUtils.getDouble(filters.getMgtFeeTo()));
		}
	
		if(filters.getMinInitialInvFrom() != null && !"".equals(filters.getMinInitialInvFrom())){
			hql.append(" and i.minInitialAmount >= ? ");
			params.add(StrUtils.getDouble(filters.getMinInitialInvFrom()));
		}
		
		if(filters.getMinInitialInvTo() != null && !"".equals(filters.getMinInitialInvTo())){
			hql.append(" and i.minInitialAmount <= ? ");
			params.add(StrUtils.getDouble(filters.getMinInitialInvTo()));
		}
		
		
		//基金回报筛选条件
		StringBuffer returnHql = new StringBuffer();
		if(filters.getPerfLaunchFrom() != null && !"".equals(filters.getPerfLaunchFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_LAUNCH");
			params.add(Double.parseDouble(filters.getPerfLaunchFrom()));
		}
		if(filters.getPerfLaunchTo() != null && !"".equals(filters.getPerfLaunchTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_LAUNCH");
			params.add(Double.parseDouble(filters.getPerfLaunchTo()));
		}
		if(filters.getPerfYTDFrom() != null && !"".equals(filters.getPerfYTDFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_YTD");
			params.add(StrUtils.getDouble(filters.getPerfYTDFrom()));
		}
		if(filters.getPerfYTDTo() != null && !"".equals(filters.getPerfYTDTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_YTD");
			params.add(StrUtils.getDouble(filters.getPerfYTDTo()));
		}
		if(filters.getPerfOneWeekFrom() != null && !"".equals(filters.getPerfOneWeekFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_1W");
			params.add(StrUtils.getDouble(filters.getPerfOneWeekFrom()));
		}
		if(filters.getPerfOneWeekTo() != null && !"".equals(filters.getPerfOneWeekTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_1W");
			params.add(Double.parseDouble(filters.getPerfOneWeekTo()));
		}
		if(filters.getPerfOneMonthFrom() != null && !"".equals(filters.getPerfOneMonthFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_1M");
			params.add(StrUtils.getDouble(filters.getPerfOneMonthFrom()));
		}
		if(filters.getPerfOneMonthTo() != null && !"".equals(filters.getPerfOneMonthTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_1M");
			params.add(StrUtils.getDouble(filters.getPerfOneMonthTo()));
		}
		if(filters.getPerfThreeMonthFrom() != null && !"".equals(filters.getPerfThreeMonthFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_3M");
			params.add(Double.parseDouble(filters.getPerfThreeMonthFrom()));
		}
		if(filters.getPerfThreeMonthTo() != null && !"".equals(filters.getPerfThreeMonthTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_3M");
			params.add(StrUtils.getDouble(filters.getPerfThreeMonthTo()));
		}
		if(filters.getPerfSixMonthFrom() != null && !"".equals(filters.getPerfSixMonthFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_6M");
			params.add(Double.parseDouble(filters.getPerfSixMonthFrom()));
		}
		if(filters.getPerfSixMonthTo() != null && !"".equals(filters.getPerfSixMonthTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_6M");
			params.add(StrUtils.getDouble(filters.getPerfSixMonthTo()));
		}
		if(filters.getPerfOneYearFrom() != null && !"".equals(filters.getPerfOneYearFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_1Y");
			params.add(StrUtils.getDouble(filters.getPerfOneYearFrom()));
		}
		if(filters.getPerfOneYearTo() != null && !"".equals(filters.getPerfOneYearTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_1Y");
			params.add(StrUtils.getDouble(filters.getPerfOneYearTo()));
		}
		if(filters.getPerfThreeYearFrom() != null && !"".equals(filters.getPerfThreeYearFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_3Y");
			params.add(StrUtils.getDouble(filters.getPerfThreeYearFrom()));
		}
		if(filters.getPerfThreeYearTo() != null && !"".equals(filters.getPerfThreeYearTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_3Y");
			params.add(StrUtils.getDouble(filters.getPerfThreeYearTo()));
		}
		if(filters.getPerfFiveYearFrom() != null && !"".equals(filters.getPerfFiveYearFrom())){
			returnHql.append(" and ( i.periodCode=? and i.increase >= ? ) ");
			params.add("return_period_code_5Y");
			params.add(StrUtils.getDouble(filters.getPerfFiveYearFrom()));
		}
		if(filters.getPerfFiveYearTo() != null && !"".equals(filters.getPerfFiveYearTo())){
			returnHql.append(" and ( i.periodCode=? and i.increase <= ? ) ");
			params.add("return_period_code_5Y");
			params.add(StrUtils.getDouble(filters.getPerfFiveYearTo()));
		}
		if (returnHql.length()>0){
			returnHql = new StringBuffer("select i.fund.id from FundReturn i LEFT JOIN FundReturnEn en on i.id = en.id where i.isValid='1' and i.fund.id is not null ").append(returnHql.toString());
			hql.append(" and i.id in ( "+returnHql.toString()+" ) ");
		}
		
		
		if(filters.getKeyword() != null && !"".equals(filters.getKeyword())){
			hql.append(" and (f.fundName like ?) ");
			params.add("%"+filters.getKeyword()+"%");
			//params.add("%"+filters.getKeyword()+"%");
		}
		
		//限制在运营公司下
		if(null!=filters.getLoginUser() && !"".equals(filters.getLoginUser().getId())){
			hql.append(" and exists ( SELECT pc.id FROM ProductCompany pc ");
			hql.append(" INNER JOIN MemberCompany mc ON mc.company.id=pc.company.id ");
			hql.append(" WHERE pc.product.id=i.product.id AND mc.member.id=? )");
			params.add(filters.getLoginUser().getId());
		}
		
		//hql.append(" order by f.fundName ");
		
//		List voList = this.baseDao.find(hql.toString(), params.toArray(), false);
//		System.out.println( "" );System.out.println( "" );
//		System.out.println( "** FundInfoServiceImpl findFundInfoList hql:\n"+hql.toString() );
//		System.out.println( "" );System.out.println( "" );
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		List voList = jsonPaging.getList();
		List list = new ArrayList();
//		Iterator it = jsonPaging.getList().iterator();
		if("all".equalsIgnoreCase(dataFlag)){
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
					
					//设置回报及评级信息
					vo = setFundInfos(vo,filters.getLangCode(),memberId,null,filters.getBeforeYears());
	
					String smallImgUrl = getPerformanceChartImage(vo.getFundId(), CommonConstantsWeb.CHART_IMAGE_SIZE_SMALL, displayColor);
					String middleImgUrl = getPerformanceChartImage(vo.getFundId(), CommonConstantsWeb.CHART_IMAGE_SIZE_MIDDL, displayColor);
					vo.setFundReturnYTDSmallImg(smallImgUrl);
					vo.setFundReturnYTDMiddleImg(middleImgUrl);
					
					list.add(vo);
				}
			}
			jsonPaging.setList(list);
		}
		return jsonPaging;
	}
	
	
	/**
	 * 获取基金列表(Fund Screener页面)新版
	 * @author zxtan
	 * @date 2016-10-17
	 * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging findFundInfoCount(JsonPaging jsonPaging,String langCode){
		StringBuffer hql = new StringBuffer("SELECT i.id, i.isinCode, i.riskLevel, i.lastNav, i.lastNavUpdate, i.issuePrice, i.issueDate, " +
				"i.mgtFee, i.minInitialAmount, i.minSubscribeAmount, i.minHoldingAmount, i.minRedemptionAmount, " +
				"i.minRspAmount, i.createTime, i.lastUpdate, i.isValid, f.id, f.fundName, f.domicileCode, f.domicile, f.fundHouse, " +
				"f.fundManCo, f.fundManager, f.fundSize, f.fundCurrencyCode, f.fundCurrency, f.fundTypeCode, f.fundType, f.sectorTypeCode, " +
				"f.sectorType, f.geoAllocationCode, f.geoAllocation, f.invTarget, f.keyRisks FROM FundInfo i ");
		List params = new ArrayList();

		hql.append(" left join "+this.getLangString("FundInfo", langCode));
		
		hql.append(" f on i.id=f.id where i.isValid='1' ");
				
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}
	
	
	
	
	
	 /***
     * 分页查询记录
     * @param jsonPaging 分页参数
     * @param fundInfo 查询参数
	 * @return
     */
	//@Transactional(readOnly = true)
	public JsonPaging findAllFundInfo(JsonPaging jsonPaging,String keyword,String distributorId,String language){
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from FundInfo l ");
		hql.append(" LEFT join "+this.getLangString("FundInfo", language));
		hql.append(" r on l.id=r.id ");
		hql.append(" where l.isValid='1' ");

		if(keyword != null && !"".equals(keyword)){
			hql.append(" and ( l.isinCode like ?  or r.fundName like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}					
		if(jsonPaging.getSort() == null || "".equals(jsonPaging.getSort())){
			jsonPaging.setSort("l.issueDate");
		}
		if(StringUtils.isNotBlank(distributorId)){
			hql.append(" AND l.product.id IN (SELECT c.product.id FROM ProductDistributor c,MemberDistributor d ");
			hql.append(" WHERE c.distributor.id=d.id AND d.id=?)");
			params.add(distributorId);
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);

		return jsonPaging;
	}

	/**
	 * 设置基金信息vo中的回报及评级信息
	 * @param vo
	 * @param langCode 查看语言
	 * @param memberId 用户ID
	 * @param toCurrency 转换货币
	 * @return
	 */
	private FundInfoDataVO setFundInfos(FundInfoDataVO vo, String langCode, String memberId, String toCurrency){
		return setFundInfos(vo, langCode, memberId, toCurrency, null);
	}
	
	/**
	 * 设置基金信息vo中的回报及评级信息
	 * @param vo
	 * @param langCode 查看语言
	 * @param memberId 用户ID
	 * @param toCurrency 转换货币
	 * @return
	 */
	public FundInfoDataVO setFundInfos(FundInfoDataVO vo, String langCode, String memberId, String toCurrency, String returnYears){
		try {
			
			//基金关注状态
			if (StringUtils.isNotEmpty(memberId)){//非null和空串
				String followStatus = getWebWatchStatus(vo.getProductId(), memberId, ""); // getWebFollowStatus(vo.getFundId(),memberId,"fund");
				vo.setFollowStatus(followStatus);
			}
			
			//累积回报
			List<GeneralDataVO> fundCumReturnList;
			fundCumReturnList = findFundReturnList(vo.getFundId(), "heap", "", langCode);
			
			if (null!=fundCumReturnList && !fundCumReturnList.isEmpty()){
				for (GeneralDataVO r: fundCumReturnList){
					if (null!=r && null!=r.getItemCode() && !"".equals(r.getItemCode())){
						try {
							if ("return_period_code_1W".equals(r.getItemCode()))
								vo.setFundReturnOneWeek(r.getPercent());
							else if ("return_period_code_1M".equals(r.getItemCode()))
								vo.setFundReturnOneMonth(r.getPercent());
							else if ("return_period_code_3M".equals(r.getItemCode()))
								vo.setFundReturnThreeMonth(r.getPercent());
							else if ("return_period_code_6M".equals(r.getItemCode()))
								vo.setFundReturnSixMonth(r.getPercent());
							else if ("return_period_code_1Y".equals(r.getItemCode()))
								vo.setFundReturnOneYear(r.getPercent());
							else if ("return_period_code_3Y".equals(r.getItemCode()))
								vo.setFundReturnThreeYear(r.getPercent());
							else if ("return_period_code_5Y".equals(r.getItemCode()))
								vo.setFundReturnFiveYear(r.getPercent());
							else if ("return_period_code_YTD".equals(r.getItemCode()))
								vo.setFundReturnYTD(r.getPercent());
							else if ("return_period_code_LAUNCH".equals(r.getItemCode()))
								vo.setFundReturnLaunch(r.getPercent());
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}
			
			//年回报
			fundCumReturnList = findFundReturnList(vo.getFundId(), "year", "", langCode, returnYears);
			if (null!=fundCumReturnList && !fundCumReturnList.isEmpty()){
				for (int i = 0;i<fundCumReturnList.size();i++){
					GeneralDataVO r = fundCumReturnList.get(i);
//					if (null!=r.getItemCode() && !"".equals(r.getItemCode())){
//						if ("fund_return_2010".equals(r.getItemCode()))
//							vo.setFundReturn2010(r.getPercent());
//						else if ("fund_return_2011".equals(r.getItemCode()))
//							vo.setFundReturn2011(r.getPercent());
//						else if ("fund_return_2012".equals(r.getItemCode()))
//							vo.setFundReturn2012(r.getPercent());
//						else if ("fund_return_2013".equals(r.getItemCode()))
//							vo.setFundReturn2013(r.getPercent());
//						else if ("fund_return_2014".equals(r.getItemCode()))
//							vo.setFundReturn2014(r.getPercent());
//						else if ("fund_return_2015".equals(r.getItemCode()))
//							vo.setFundReturn2015(r.getPercent());
//						else if ("fund_return_2016".equals(r.getItemCode()))
//							vo.setFundReturn2016(r.getPercent());
//					}
					switch (i) {//取5条记录
					case 0:
						vo.setFundReturnYear1(r.getPercent());
						break;
					case 1:
						vo.setFundReturnYear2(r.getPercent());
						break;
					case 2:
						vo.setFundReturnYear3(r.getPercent());
						break;
					case 3:
						vo.setFundReturnYear4(r.getPercent());
						break;
					case 4:
						vo.setFundReturnYear5(r.getPercent());
						break;
					default:
						break;
					}
				}
			}
			
			//基金评级，获取英语名称(同一评级公司只取最新的评级记录）
			List<GeneralDataVO> levels = findFundRatingLevelList(vo.getFundId(), CommonConstants.LANG_CODE_EN);
			List<String> orgs = new ArrayList<String>();
			if (null!=levels && !levels.isEmpty()){
				for (GeneralDataVO r: levels){
					if (null!=r.getItem() && !"".equals(r.getItem())){
						if (orgs.contains(r.getItem())) continue;//过滤重复项
						orgs.add(r.getItem());
						if ("Lipper CR".equals(r.getItem()))
							vo.setLipperCR(r.getLevel());
						else if ("Fitch".equals(r.getItem()))
							vo.setFitch(r.getLevel());
						else if ("Citywire".equals(r.getItem()))
							vo.setCitywire(r.getLevel());
					}
				}
			}
			
			//货币转换
			if (null!=toCurrency && !"".equals(toCurrency)){
				double rate = 0;
				String fromCurrency = null;
				if(vo.getFundInfoEn() != null){
					fromCurrency = StrUtils.getString(vo.getFundInfoEn().getFundCurrencyCode());
				}
				try {
					if (!"".equals(fromCurrency) && !"".equals(toCurrency) && !fromCurrency.equals(toCurrency)){
							rate = findExchangeRate(fromCurrency, toCurrency).getRate();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				//如果不能获取兑换率，则重置toCurrency
				if (rate<=0) vo.setToCurrency(fromCurrency);
				vo.setAmounts(rate);
			}
			
			//货币信息
			String toCurrencyName = this.getParamConfigName(langCode, toCurrency,CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
			vo.setToCurrency(toCurrency);
			vo.setToCurrencyName(toCurrencyName);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return vo;
	}
	/**
	 * 通过isinCode查找一条信息
	 * @param isinCode 编码
	 * @param password 用户密码
	 * @return
	 */
	public FundInfo findFundInfoByCode(String isinCode,String password){
		return null;
	}
	
	/**
	 * 通过ID查找基金全部信息（含多语言）
	 * @param id
	 * @return
	 */
	public FundInfoDataVO findFundFullInfoById(String id){
		FundInfoDataVO result = new FundInfoDataVO();
		FundInfo info = findFundInfoById(id);
		if(info != null){
			result.setFundInfo(info);
			result.setFundInfoEn(findFundInfoEnById(id));
			result.setFundInfoSc(findFundInfoScById(id));
			result.setFundInfoTc(findFundInfoTcById(id));
			if(info.getProduct() != null){
				result.setProductId(info.getProduct().getId());
			}
			result.setSubscribeFee(info.getSubscribeFee());
			result.setMgtFee(info.getMgtFee());
		}
		return result;
	}
	
	/**
	 * 通过ID查找基金全部信息
	 * @param id
	 * @param langCode 语言
	 * @param memberId 登陆用户
	 * @param toCurrency 转换货币
	 * @return
	 */
	public FundInfoDataVO findFundFullInfoById(String id, String langCode,String memberId,String toCurrency){
		FundInfoDataVO result = findFundFullInfoById(id);
		result.setDefaultInfoByLang(langCode);
		//String memberId = "ALOQ_JPUwJgGqbZRsQD4qw5xyJKnqwpIOKRh";//	测试 
		//设置回报及评级信息
		result.setFundId(id);
		result = setFundInfos(result, langCode, memberId, toCurrency);
		
		return result;
	}

	/**
	 * 获取基金累积表现信息
	 * @param fundId 基金id
	 * @param period 时间段编码
	 * @param langCode 语言
	 * @return	GeneralDataVO	基金累积表现数据
	 */
	//@Transactional(readOnly=true)
	public GeneralDataVO findFundCumReturn(String fundId, String period, String langCode) {
		List params = new ArrayList();

		String hql = "select t.fund.id,t.increase,out.periodName from FundReturn t ";
		hql += " left join "+this.getLangString("FundReturn", langCode);
		hql += " out on t.id=out.id where t.isValid='1' and t.type='heap' ";
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		
		
		if(null!=period && !"".equals(period)){
			hql += "and out.periodCode=? ";
			params.add(period);
		}
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(objs[0]==null?"":objs[0].toString());
				vo.setPercent(StrUtils.getDouble(objs[1]==null?"":objs[1].toString()));
				vo.setItem(objs[2]==null?"":objs[2].toString());
				return vo;
			}
		}
		
		return null;
	}
	
	/**
	 * 获取基金费用信息
	 * @param fundId 基金id
	 * @param period 时间段编码
	 * @param langCode 语言
	 * @return	GeneralDataVO	基金费用数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundFees(String fundId, String langCode) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List params = new ArrayList();

		String hql = "select t.fund.id,out.feesItem,out.fees from FundFeesItem t ";
		hql += " left join "+this.getLangString("FundFeesItem", langCode);
		hql += " out on t.id=out.id where t.isValid='1' ";
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		hql += " order by t.fund.id,t.lastUpdate desc ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(objs[0]==null?"":objs[0].toString());
				vo.setItem(objs[1]==null?"":objs[1].toString());
				vo.setDescrp(objs[2]==null?"":objs[2].toString());
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取基金文档信息
	 * @param id 基金
	 */
	//@Transactional(readOnly=true)
	public FundDoc findFundDoc(String id){
		FundDoc info = (FundDoc)this.baseDao.get(FundDoc.class, id);
		return info;
	}
	
	/**
	 * 获取基金文档信息
	 * @param fundId 基金id
	 * @param langCode 语言
	 */
	//@Transactional(readOnly=true)
	public JsonPaging findFundDocList(JsonPaging jsonPaging,String fundId) {
		String hql = " FROM FundDoc t "
			//+ " INNER JOIN AccessoryFile a ON t.id=a.relateId "
			//+ " INNER JOIN SysAdmin s ON a.createBy=s.loginCode "
			+ " WHERE t.isValid='1' ";
		List params = new ArrayList();
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		//hql += " order by t.fund.id,t.lastUpdate desc ";
		
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		
		return jsonPaging;
	}
	
	/**
	 * 获取基金文档信息
	 * @param fundId 基金id
	 * @param period 时间段编码
	 * @param langCode 语言
	 * @return	GeneralDataVO	基金文档数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundDocs(String fundId, String langCode) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
//		String hql = "select t.fund.id,t.documentName,t.accessory.fileName,t.accessory.filePath from FundDoc t where t.isValid='1' ";
		String hql = "select t.id,t.fund.id,t.documentName from FundDoc t where t.isValid='1' ";
//		hql +="left join t.accessory  where 1=1 ";
		List params = new ArrayList();

		if(null!=langCode && !"".equals(langCode)){
			hql += "and t.langCode=? ";
			params.add(langCode);
		}
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		hql += " order by t.fund.id,t.lastUpdate desc ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setId(StrUtils.getString(objs[0]));
				vo.setFundId(StrUtils.getString(objs[1]));
				vo.setItem(StrUtils.getString(objs[2]).replace("點擊這裏查看", "").replace("点击这里查看", "").replace("Click here for", ""));
				//每个记录还对应3个多语言版本
				List<FundDocDetail> fundDocDetailList = this.findDocDetail(vo.getId());
				for(int i=0;i<fundDocDetailList.size();i++){
//					if("sc".equals(langCode)){
//						if("sc".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("简体");
//						else if("en".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("英文");
//						else if("tc".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("繁体");
//					}
//					else if("tc".equals(langCode)){
//						if("sc".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("簡體");
//						else if("en".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("英文");
//						else if("tc".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("繁體");
//					}
//					else if("en".equals(langCode)){
//						if("sc".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("SC");
//						else if("en".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("EN");
//						else if("tc".equals(fundDocDetailList.get(i).getLangCode()))fundDocDetailList.get(i).setLangCode("TC");
//					}
				}
				vo.setDocDetailList(fundDocDetailList);
//				vo.setFileName(objs[2]==null?"":objs[2].toString());
//				vo.setUrl(objs[3]==null?"":objs[3].toString());
				//查找附件
				List<AccessoryFile> files = findFileAttr(vo.getId(),"fund_doc");
				if (!files.isEmpty()){
					AccessoryFile file = files.get(0);
					vo.setFileName(file.getFileName());
					vo.setUrl(file.getFilePath());
				}
				list.add(vo);
			}
		}
		
		return list;
	}
	
	public List<FundDocDetail> findDocDetail(String docId){
		String sql = "from FundDocDetail t where t.fundDoc.id = '"+docId+"' ORDER BY CASE WHEN lang_code ='繁体' THEN 1 WHEN  lang_code ='簡體' THEN 2 WHEN  lang_code ='英文' THEN 3 WHEN lang_code ='繁体' THEN 1 WHEN  lang_code ='简体' THEN 2 WHEN  lang_code ='英文' THEN 3 WHEN lang_code ='tc' THEN 1 WHEN  lang_code ='sc' THEN 2 WHEN  lang_code ='en' THEN 3  END ASC";
		List<FundDocDetail> list = this.baseDao.find(sql, null, false);
		return list;
	}
	
	/**
	 * 获取基金分红派息信息
	 * @param id 基金
	 */
	//@Transactional(readOnly=true)
	public FundBonus findFundBonusById(String id){
		FundBonus info = (FundBonus)this.baseDao.get(FundBonus.class, id);
		return info;
	}
	
	/**
	 * 获取基金分红派息信息
	 * @param fundId 基金id
	 * @param langCode 语言
	 */
	//@Transactional(readOnly=true)
	public JsonPaging findFundBonusList(JsonPaging jsonPaging,String fundId) {
		String hql = "FROM FundBonus t ";
		hql +=" WHERE t.isValid='1' ";
		List params = new ArrayList();
		if(null!=fundId && !"".equals(fundId)){
			hql += "AND t.fund.id=? ";
			params.add(fundId);
		}
		hql += " ORDER BY t.fund.id,t.exDividendDate DESC";
		
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		
		return jsonPaging;
	}
	
	/**
	 * 获取基金分红派息信息Dividend
	 * @param fundId 基金id
	 * @param period 时间段编码
	 * @return	GeneralDataVO	基金分红数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundBonus(String fundId) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		String hql = "select t.fund.id,t.exDividendDate,t.dividendPerUnit,t.annualDividendYield from FundBonus t ";
		List params = new ArrayList();
		
		hql += "where t.isValid='1' ";
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		hql += " order by t.fund.id,t.exDividendDate desc ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(objs[0]==null?"":objs[0].toString());
				vo.setUpdateTime(DateUtil.getDate(objs[1]==null?"":objs[1].toString(),CoreConstants.DATE_FORMAT));
				vo.setPrice(StrUtils.getDouble(objs[2]==null?"":objs[2].toString()));
				vo.setPercent(StrUtils.getDouble(objs[3]==null?"":objs[3].toString()));
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取基金通告信息
	 * @param id 基金
	 */
	//@Transactional(readOnly=true)
	public FundAnno findFundAnno(String id){
		FundAnno info = (FundAnno)this.baseDao.get(FundAnno.class, id);
		return info;
	}
	
	/**
	 * 获取基金通告信息
	 * @param fundId 基金id
	 * @param langCode 语言
	 */
	//@Transactional(readOnly=true)
	public JsonPaging findFundAnnoList(JsonPaging jsonPaging,String fundId, String langCode) {
		String hql = "FROM FundAnno t ";
		hql +=" WHERE t.isValid='1' ";
		List params = new ArrayList();

		if(null!=langCode && !"".equals(langCode)){
			hql += "and t.langCode=? ";
			params.add(langCode);
		}
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		hql += " order by t.fund.id,t.annoDate desc ";
		
		this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		
		return jsonPaging;
	}
	
	/**
	 * 获取基金通告信息
	 * @param fundId 基金id
	 * @param period 时间段编码
	 * @param langCode 语言
	 * @return	GeneralDataVO	基金通告数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundAnnouncement(String fundId, String langCode) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		String hql = "select t.fund.id,t.annoName,t.annoDate,t.id from FundAnno t ";
		hql +=" where t.isValid='1' ";
		List params = new ArrayList();

		if(null!=langCode && !"".equals(langCode)){
			hql += "and t.langCode=? ";
			params.add(langCode);
		}
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		hql += " order by t.fund.id,t.annoDate desc ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(objs[0]==null?"":objs[0].toString());
				vo.setItem(objs[1]==null?"":objs[1].toString());
				vo.setUpdateTime(DateUtil.getDate(objs[2]==null?"":objs[2].toString(),CoreConstants.DATE_FORMAT));
				vo.setId(objs[1]==null?"":objs[3].toString());
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取基金表现信息列表
	 * @param fundId 基金id
	 * @param type 类型：heap - 累积表现, year - 年度表现
	 * @param period 时间段编码
	 * @param langCode 语言
	 * @return	GeneralDataVO	基金表现数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundReturnList(String fundId, String type, String period, String langCode) {
		return findFundReturnList(fundId, type, period, langCode, null);
	}
	
	/**
	 * 获取基金表现信息列表
	 * @param fundId 基金id
	 * @param type 类型：heap - 累积表现, year - 年度表现
	 * @param period 时间段编码
	 * @param langCode 语言
	 * @param years 获取的年数
	 * @return	GeneralDataVO	基金表现数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundReturnList(String fundId, String type, String period, String langCode, String years) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List params = new ArrayList();

		String hql = "select t.fund.id,t.increase,out.periodCode,out.periodName from FundReturn t ";
		hql += " left join "+this.getLangString("FundReturn", langCode);
		hql += " out on t.periodCode=out.periodCode where t.isValid = '1' ";
		//hql += " out on t.id=out.id where t.isValid = '1' ";//modify by linwenwei 2017-04-12
		
		if(null!=type && !"".equals(type)){
			hql += "and t.type=? ";
			params.add(type);
		}
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		
		if(null!=period && !"".equals(period)){
			hql += "and out.periodCode=? ";
			params.add(period);
		}
		
		hql += " order by t.fund.id,out.id asc ";
		
		
		JsonPaging paging = new JsonPaging();
		paging.setPage(0);
		if(null!=years && !"".equals(years) && null!=type && "year".equals(type)){
			paging.setRows(Integer.parseInt(years));
		}else{
			paging.setRows(null);
		}
		paging = this.baseDao.selectJsonPaging(hql, params.toArray(), paging , false);
//		List voList = this.baseDao.find(hql, params.toArray(), false);
		List voList = paging.getList();
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(objs[0]==null?"":objs[0].toString());
				vo.setPercent(StrUtils.getDouble(objs[1]));
				vo.setItemCode(objs[2]==null?"":objs[2].toString());
				vo.setItem(objs[3]==null?"":objs[3].toString());
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取基金价格行情信息列表
	 * @param fundId 基金id
	 * @param period 时间段类型： day, week, month
	 * @param startDate 开始时间： yyyy-MM-dd
	 * @param endDate 结束时间： yyyy-MM-dd
	 * @param langCode 语言
	 * @return	GeneralDataVO	价格行情数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundMarketList(String fundId, String period, String startDate, String endDate, String langCode) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		String tableName = "FundMarket";//日行情
		if (null!=period){
			if ("week".equals(period))
				tableName = "FundMarketWeek";//周行情
			else if ("month".equals(period))
				tableName = "FundMarketMonth";//月行情
		}
		
		String hql = "select t.fund.id,t.openPrice,t.lowPrice,t.hightPrice,t.closePrice,t.nav,t.accNav,t.returnRate,t.marketDate,i.fundName from "+tableName+" t ";
		hql += " left join "+this.getLangString("FundInfo", langCode);		
		hql += " i ON i.id=t.fund.id where t.isValid='1' ";
		List params = new ArrayList();
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}
		
		if(null!=startDate && !"".equals(startDate)){
			hql += "and t.marketDate>=? ";
			params.add(DateUtil.getDate(startDate));
		}
		if(null!=endDate && !"".equals(endDate)){
			hql += "and t.marketDate<=? ";
			params.add(DateUtil.getDate(endDate));
		}
		
		hql += " order by t.fund.id,t.marketDate ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				vo.setOpenPrice(StrUtils.getDouble(objs[1]));
				vo.setLowPrice(StrUtils.getDouble(objs[2]));
				vo.setHightPrice(StrUtils.getDouble(objs[3]));
				vo.setClosePrice(StrUtils.getDouble(objs[4]));
				vo.setNav(StrUtils.getDouble(objs[5]));
				vo.setAccNav(StrUtils.getDouble(objs[6]));
				vo.setDayReturn(StrUtils.getDouble(objs[7]));
				vo.setUpdateTime(DateUtil.getDate(objs[8]==null?"":objs[8].toString(), CoreConstants.DATE_FORMAT));
				vo.setName(StrUtils.getString(objs[9]));
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取基金公司列表
	 * @param langCode
	 * @return
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundHouseList(String langCode) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List params = new ArrayList();

		String hql = "select t.id,out.houseName from FundHouse t ";
		hql += " left join "+this.getLangString("FundHouse", langCode);
		hql += " out on t.id=out.id where t.isValid='1' ";
//		hql += " order by out.houseName ";
		hql += " order by convert(out.houseName ,'gbk') ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setId(objs[0]==null?"":objs[0].toString());
				vo.setName(objs[1]==null?"":objs[1].toString());
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 获取基金评级信息列表
	 * @param fundId 基金id
	 * @param langCode 语言
	 * @return	GeneralDataVO	基金表现数据
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundRatingLevelList(String fundId, String langCode) {
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List params = new ArrayList();
		
		String hql = "select t.id,t.level,out.orgName,t.fund.id from FundRatingLevel t ";
		hql += " left join "+this.getLangString("FundRatingLevel", langCode);
		hql += " out on t.id=out.id where t.isValid='1' ";
		
		if(null!=fundId && !"".equals(fundId)){
			hql += "and t.fund.id=? ";
			params.add(fundId);
		}

		hql += " order by t.fund.id,out.orgName desc,t.lastUpdate desc ";
		
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setId(objs[0]==null?"":objs[0].toString());
				vo.setLevel(StrUtils.getInt(StrUtils.getString(objs[1])));
				vo.setItem(objs[2]==null?"":objs[2].toString());
				vo.setFundId(objs[3]==null?"":objs[3].toString());
				list.add(vo);
			}
		}
		
		return list;
	}

	/**
	 * 搜索基金信息
	 * @param houseId 基金公司ID
	 * @param keyWord 搜索关键词
	 * @param langCode 语言
	 * @return
	 */
	//@Transactional(readOnly=true)
	public List<GeneralDataVO> findFundListByHouse(String houseId, String keyWord, String langCode){
		List<GeneralDataVO> list = new ArrayList<GeneralDataVO>();
		List<Object> params = new ArrayList<Object>();
		String hql = "select f.id ,x.fundName from FundInfo f ";
		hql += " left join "+this.getLangString("FundInfo", langCode);
		hql += " x on f.id=x.id  ";
		hql += " where f.isValid = '1' ";
		
		if(null!=houseId && !"".equals(houseId)){
			hql += " and (x.fundHouseId.id =  ?)";
			params.add(houseId);
		}
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and (x.fundName like ? or x.fundNamePinyin like ?)";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		hql += " order by x.fundName ";
		List voList = this.baseDao.find(hql, params.toArray(), false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				GeneralDataVO vo = new GeneralDataVO();
				Object[] objs = (Object[])voList.get(x);
				vo.setFundId(StrUtils.getString(objs[0]));
				vo.setId(vo.getFundId());
				vo.setName(StrUtils.getString(objs[1]));
				
				list.add(vo);
			}
		}
		
		return list;
	}
	
	/**
	 * 根据基金类型统计
	 * @param fundId 同类基金ID
	 * @param typeCode 基金类型编码
	 * @param typeName 基金类型名称
	 * @return
	 */
	public int findFundTotalByType(String fundId, String typeCode, String typeName){
		List<Object> params = new ArrayList<Object>();
	    String hql = "select t.id from FundInfoEn t where 1=1 ";

		if(null!=fundId && !"".equals(fundId)){
			hql += " and t.fundTypeCode = (select max(i.fundTypeCode) from FundInfoEn i where i.fundTypeCode is not null and id=?) ";
			params.add(fundId);
		}
		if(null!=typeCode && !"".equals(typeCode)){
			hql += " and t.fundTypeCode = ?";
			params.add(typeCode);
		}
		if(null!=typeName && !"".equals(typeName)){
			hql += " and t.fundType like ?";
			params.add("%"+typeName+"%");
		}
		List list = null;
		try {
		    list = this.baseDao.find(hql, params.toArray(), false);
		} catch (Exception e) {
			return 0;
		}
	    return (list==null?0:list.size());  
	}
	
	/**
	 * 分页查询基金列表信息
	 * @param jsonPaging 分页参数
	 * @param keyWord 搜索关键词
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param fundType 基金类型
	 * @param dateType 基金回报类型
	 * @param toCurrency 转换货币
	 * @return
	 */
	public JsonPaging findAllFundListForApp(JsonPaging jsonPaging,String keyWord,String langCode,String userId,String fundType,String dateType, String toCurrency){
		
		List<Object> params = new ArrayList<Object>();
		String sql = "select f.id as id,x.fund_name as fundName,x.fund_currency as fundCurrency,f.risk_level as riskLevel, " +
				" x.fund_type as fundType,f.last_nav as lastNav,date_format(f.last_nav_update,'%c-%d') as lastNavUpdate, " +
				" x.fund_currency_code as fundCurrencyCode,r.increase as increase ";
				
		if(null!=userId && !"".equals(userId)){
			sql +=" , w.follow_flag as followFlag ";
		}else{
			sql +=" , '0' as followFlag ";
		}
		sql +=" from fund_info f ";
		sql += " left join "+this.getLangString("fund_info_", langCode).toLowerCase();
		sql +=" x on f.id=x.id  ";
		if(null!=userId && !"".equals(userId)){
			sql +=" left join web_follow w on w.relate_id=f.id and w.module_type='fund' and w.member_id=? ";
			params.add(userId);
		}
		
		//股票时间类型:return_period_code_1W,return_period_code_1M,return_period_code_1Y...
		sql +=" left join fund_return r on r.fund_id=f.id and r.period_code=? ";
		params.add(dateType);
		
		sql +=" where f.is_valid='1' ";
		
		//基金类型编码,对应基础参数表中fund_type_??
		if(null!=fundType && !"".equals(fundType)){
			sql += " and x.fund_type_code=? ";
			params.add(fundType);
		}
		
		if(null!=keyWord && !"".equals(keyWord)){
			sql += " and x.fund_name like ? ";
			params.add("%"+keyWord+"%");
		}
		if ("last_nav".equals(jsonPaging.getSort()) && !"".equals(toCurrency)){
			jsonPaging.setSort("");
			sql += "order by getExchangeRate(x.fund_currency_code,'"+toCurrency+"',last_nav) "+jsonPaging.getOrder();
		}
		jsonPaging = springJdbcQueryManager.springJdbcQueryForPaging(sql, params.toArray(), jsonPaging);
		long total=springJdbcQueryManager.springJdbcQueryForTotal(sql, params.toArray());
		jsonPaging.setTotal((int)total);
		return jsonPaging;
	}
	
	/**
	 * 搜索基金信息
	 * @param jsonPaging 分页参数
	 * @param keyWord 搜索关键词
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @return
	 */
	public JsonPaging searchFundListForApp(JsonPaging jsonPaging,String keyWord,String langCode,String userId){
		List<Object> params = new ArrayList<Object>();
		String sql = "select f.id as id,x.fund_name as fundName,f.isin_code as isinCode " ;
		if(null!=userId && !"".equals(userId)){
			sql +=" , w.follow_flag as followFlag ";
		}else{
			sql +=" , '0' as followFlag ";
		}
		sql +=" from fund_info f ";
		sql += " left join "+this.getLangString("fund_info_", langCode).toLowerCase();
		sql +=" x on f.id=x.id  ";
		if(null!=userId && !"".equals(userId)){
			sql +=" left join web_follow w on w.relate_id=f.id and w.module_type='fund' and w.member_id=? ";
			params.add(userId);
		}
		sql +=" where f.is_valid='1' ";
		if(null!=keyWord && !"".equals(keyWord)){
			sql += " and (x.fund_name like ? or f.isin_code like ?)";
			params.add("%"+keyWord+"%");
			params.add("%"+keyWord+"%");
		}
		//long total=springJdbcQueryManager.springJdbcQueryForTotal(sql, params.toArray());
		//jsonPaging.setTotal((int)total);
		jsonPaging = springJdbcQueryManager.springJdbcQueryForPaging(sql, params.toArray(), jsonPaging);
		return jsonPaging;
	}
	
	/**
	 * 获取我关注的基金名单列表
	 * @param jsonPaging
	 * @param langCode 语言
	 * @param userId 用户ID
	 * @param dateType 基金回报类型
	 * @param toCurrency 转换货币
	 * @return
	 */
	public JsonPaging getFundFollowListForApp(JsonPaging jsonPaging,String langCode,String userId,String dateType, String toCurrency) {
		
		String hql = "select f.id,s.fundName,w.followFlag,s.fundCurrency,s.fundType,f.riskLevel,f.lastNav,date_format(f.lastNavUpdate,'%c-%d') as lastNavUpdate,r.increase,s.fundCurrencyCode ";
		hql += " from WebFollow w ";
		hql += " left join FundInfo f on f.id=w.relateId ";
		
		hql += " left join "+this.getLangString("FundInfo", langCode);
		hql += " s on s.id=w.relateId ";

		hql += " left join FundReturn r on r.fund.id=w.relateId and r.periodCode=? ";
		hql += " where w.moduleType='fund' and w.isValid='1' and w.followFlag='1' and w.member.id=? ";
		List params = new ArrayList();
		params.add(dateType);
		params.add(userId);
		
		if ("last_nav".equals(jsonPaging.getSort())){
			jsonPaging.setSort("");
			hql += "order by getExchangeRate(s.fundCurrencyCode,'"+toCurrency+"',f.lastNav) "+jsonPaging.getOrder();
		}
		
		jsonPaging = baseDao.selectJsonPagingNoTotal(hql, params.toArray(), jsonPaging, false);
		List list=jsonPaging.getList();
		List listTemp=new ArrayList();
		for(int i=0;i<list.size(); i++){
			Object[] objs = (Object[])list.get(i);
			FoundFollowVo  vo = new FoundFollowVo();
			vo.setId(objs[0]==null?"":objs[0].toString());
			vo.setFundName(objs[1]==null?"":objs[1].toString());
			vo.setFollowFlag(objs[2]==null?"":objs[2].toString());
			vo.setFundCurrency(objs[3]==null?"":objs[3].toString());
			vo.setFundType(objs[4]==null?"":objs[4].toString());
			vo.setRiskLevel(objs[5]==null?"":objs[5].toString());
			vo.setLastNav(objs[6]==null?"":objs[6].toString());
			vo.setLastNavUpdate(objs[7]==null?"":objs[7].toString());
			vo.setIncrease(objs[8]==null?"":objs[8].toString());
			vo.setFundCurrencyCode(objs[9]==null?"":objs[9].toString());
			listTemp.add(vo);
    	}
		jsonPaging.setList(listTemp);
		return jsonPaging;
	}
	
	
	/**
	 *获取fundhourseList
	 * */
	public List<FundHouseDataVO> loadFundHouseList(String langCode) {
		
		String hql = "select out.id,out.houseName,out.pinYin,t.logoUrl from FundHouse t ";
		hql += " inner join "+this.getLangString("FundHouse", langCode);
		hql += " out on t.id=out.id where t.isValid='1' order by t.createTime desc";
		List list = this.baseDao.find(hql, null, false);
		List<FundHouseDataVO> voList = new ArrayList();
		if(!list.isEmpty()){
			for(int i=0;i<list.size();i++){
				Object[] objs = (Object[])list.get(i);
				FundHouseDataVO vo = new FundHouseDataVO();
				String id = objs[0]==null?"":objs[0].toString();
				String houseName = objs[1]==null?"":objs[1].toString();
				String pinYin = objs[2]==null?"":objs[2].toString();
				String logoUrl = objs[3]==null?"":objs[3].toString();
				vo.setId(id);
				vo.setHouseName(houseName);
				if(CommonConstants.LANG_CODE_EN.equals(langCode) && StringUtils.isBlank(pinYin)){
					vo.setPinYin(houseName);
				}else{
					vo.setPinYin(pinYin);
				}
				vo.setLogoUrl(PageHelper.getLogoUrl(logoUrl, "F"));
				voList.add(vo);
			}
		}
		return voList;
	}
	
	
	/**3.1.14	获取基金关注状态
	 * @author scshi
	 * @param relateId 对应类型id
	 * @param memberId 网站会员ID
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	//@Transactional(readOnly=true)
	public String getWebFollowStatus(String relateId, String memberId,String moduleType) {
		String hql = "from WebFollow t where t.relateId=? and t.member.id=? and t.moduleType=? and t.isValid='1' ";
		List params = new ArrayList();
		params.add(relateId);
		params.add(memberId);
		params.add(moduleType);
		try {
			List<WebFollow> list = this.baseDao.find(hql, params.toArray(), false);
			if(!list.isEmpty()){
//				WebFollow obj = list.get(0);
//				return obj.getFollowFlag();
				return "1";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "0";
	}
	

	/**	获取基金收藏状态
	 * @author zxtan
	 * @param productId 对应产品id
	 * @param memberID 网站会员ID
	 * @param typeId 对应模块
	 */

	private String getWebWatchStatus(String productId, String memberId,String typeId) {
		String hql = "from WebWatchlist t where t.product.id=? and t.member.id=? and t.isValid='1' ";
		List params = new ArrayList();
		params.add(productId);
		params.add(memberId);
		List<WebWatchlist> list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			//WebSelf obj = list.get(0);
			return "1";
		}
		return null;
	}
	
	/**
	 * 获取兑换率列表
	 * @return
	 * */
	//@Transactional(readOnly=true)
	public List<WebExchangeRate> loadExchangeList() {
//		String hql = "from WebExchangeRate t where t.isValid='1' order by t.lastUpdate desc ";
//		List<WebExchangeRate> list = this.baseDao.find(hql, null, false);
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(null);
		jsonPaging = findExchangeList(jsonPaging);
		return jsonPaging.getList();
	}
	
	/**
	 * 获取兑换率列表分页
	 * @param jsonPaging 
	 * @return
	 * */
	//@Transactional(readOnly=true)
	public JsonPaging findExchangeList(JsonPaging jsonPaging) {
		String hql = "from WebExchangeRate t where t.isValid='1' order by t.lastUpdate desc ";
//		List<WebExchangeRate> list = this.baseDao.find(hql, null, false);
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), null, jsonPaging , true);
		return jsonPaging;
	}

	/**
	 * 获取兑换率
	 * @author michael
	 * @param fromCurrency
	 * @param toCurrency
	 * @return
	 */
	//@Transactional(readOnly=true)
	public WebExchangeRate findExchangeRate(String fromCurrency, String toCurrency) {
		String hql = "from WebExchangeRate t where t.isValid='1' and fromCurrency=? and toCurrency=? order by t.fromCurrency,t.toCurrency  ";
		List params = new ArrayList();
		params.add(fromCurrency);
		params.add(toCurrency);
		List<WebExchangeRate> list = this.baseDao.find(hql, params.toArray(), false);
		if (null!=list && !list.isEmpty()) return list.get(0);
		return null;
	}
	
	/**
	 * 获取货币列表
	 * @author michael
	 * @return
	 */
	//@Transactional(readOnly=true)
	public List<String> getCurrencyList() {
		String hql = "select distinct t.toCurrency from WebExchangeRate t where t.isValid='1' order by t.toCurrency ";
		List<String> list = new ArrayList<String>();
		List voList = this.baseDao.find(hql, null, false);
		if(!voList.isEmpty()){
			for(int x=0;x<voList.size();x++){
				String code = (String)voList.get(x);
				list.add(code);
			}
		}
		return list;
	}
	
	/**3.1.16	基金快速选择接口
	 * @author scshi
	 * @param keyWord 搜索关键字
	 * @param lang_code 语言
	 * @return	String 	搜索结果项目，多项以逗号分隔
	 */
	@Override
	//@Transactional(readOnly=true)
	public String myFavFundList(String keyWord, String langCode) {
		List params = new ArrayList();
		String hql = "select out.fundName from FundInfo t ";
		hql +=" left join  "+this.getLangString("FundInfo", langCode);
		hql +=" out on t.id=out.id where t.isValid='1' ";
		if(null!=keyWord && !"".equals(keyWord)){
			hql += " and ( ";
			hql += " out.fundName like ? ";
			params.add("%"+keyWord+"%");
			hql += " or out.fundHouse like ? ";
			params.add("%"+keyWord+"%");
			hql += " or out.fundHousePinyin like ? ";
			params.add("%"+keyWord+"%");
			hql += " or t.isinCode like ? ";
			params.add("%"+keyWord+"%");
			hql += " ) ";
		}
		String fundName = "";
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(!list.isEmpty()){
			for(int x=0;x<list.size();x++){
				fundName +=list.get(x);
				if(x!=list.size()-1){
					fundName += ",";
				}
			}
			return fundName;
		}
		return null;
	}
	
	
	/**3.1.15	我关注的基金名单列表
	 * @author scshi
	 * @param relateID 对应类型id
	 * @param memberID 网站会员ID
	 * @param jsonPaging 
	 * @param moduleType 对应模块,fund基金关注,ifa关注,article文章关注
	 */
	public JsonPaging getWebFollowList(String memberID,String moduleType,String lang,JsonPaging jsonPaging) {
		
		String hql = "SELECT i.id, i.isinCode, i.riskLevel, i.lastNav, i.lastNavUpdate, i.issuePrice, i.issueDate, " +
				"i.mgtFee, i.minInitialAmount, i.minSubscribeAmount, i.minHoldingAmount, i.minRedemptionAmount, " +
				"i.minRspAmount, i.createTime, i.lastUpdate, i.isValid, f.id, f.fundName, f.domicileCode, f.domicile, f.fundHouse, " +
				"f.fundManCo, f.fundManager, f.fundSize, f.fundCurrencyCode, f.fundCurrency, f.fundTypeCode, f.fundType, f.sectorTypeCode, " +
				"f.sectorType, f.geoAllocationCode, f.geoAllocation, f.invTarget, f.keyRisks ";
		hql += "from FundInfo i ";
		hql += "inner join "+this.getLangString("FundInfo",lang);
		hql +=" f on i.id=f.id ";
		hql += " inner join WebFollow w on i.id=w.relateId and w.member.id=? and w.moduleType=? and w.isValid='1' and w.followFlag='1'";
		List params = new ArrayList();
		params.add(memberID);
		params.add(moduleType);
		jsonPaging = this.baseDao.selectJsonPaging(hql, params.toArray(), jsonPaging, false);
		List voList = jsonPaging.getList();
		List list = new ArrayList();
		if(!voList.isEmpty()){
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
				
				//设置回报及评级信息
				vo = setFundInfos(vo,lang,memberID,null);
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}
	
	/**
	 * 得到基金基本信息
	 * @param fundId 基金ID
	 * @param langCode  语言
	 * @param userId 用户ID
	 * @param toCurrency 要转换的货币类型
	 * @return
	 */
	public FundBasicDataVO getFundBasicDataMess(String fundId, String langCode, String userId, String toCurrency){
		FundBasicDataVO dataVO = new FundBasicDataVO();
		List params = new ArrayList();
		String hql = "from "+this.getLangString("FundInfo", langCode)+" t where t.id=?";
		params.add(fundId);
		List list = this.baseDao.find(hql, params.toArray(), false);
		if(null!=list && !list.isEmpty()){
			Object object=list.get(0);
			BeanUtils.copyProperties(object,dataVO);//拷贝信息
		}
		FundInfo info = (FundInfo) baseDao.get(FundInfo.class, fundId);
		
		dataVO.setFundId(info.getId());
		dataVO.setIsin(info.getIsinCode());
		dataVO.setRiskLevel(StrUtils.getString(info.getRiskLevel()));
		dataVO.setLastNAV(StrUtils.getString(info.getLastNav()));
		dataVO.setLastNAVUpdate(DateUtil.dateToDateString(info.getLastNavUpdate(),CoreConstants.DATE_TIME_FORMAT));
		dataVO.setIssuePrice(StrUtils.getString(info.getIssuePrice()));
		dataVO.setIssueDate(DateUtil.dateToDateString(info.getIssueDate(),CoreConstants.DATE_TIME_FORMAT));
		dataVO.setMgtFee(StrUtils.getString(info.getMgtFee()));
		dataVO.setMinInitAmount(StrUtils.getString(info.getMinInitialAmount()));
		dataVO.setMinSubscribeAmount(StrUtils.getString(info.getMinSubscribeAmount()));
		dataVO.setMinHoldingAmount(StrUtils.getString(info.getMinHoldingAmount()));
		dataVO.setMinRedemptionAmount(StrUtils.getString(info.getMinRedemptionAmount()));
		dataVO.setMinRspAmount(StrUtils.getString(info.getMinRspAmount()));
		dataVO.setLastFundReturn(StrUtils.getString(info.getDayReturn()));
		dataVO.setLangCode(langCode);
		//基金关注状态
		String followStatus = getWebFollowStatus(fundId,userId,"fund");
		dataVO.setFollowStatus(followStatus);
		
		if(null!=toCurrency && !"".equals(toCurrency)){
			//货币转换--兑换率
			double rate = 0;
			String fromCurrency = dataVO.getFundCurrencyCode();
			if (!"".equals(fromCurrency) && !"".equals(toCurrency) && !fromCurrency.equals(toCurrency)){
				rate = findExchangeRate(fromCurrency, toCurrency).getRate();
			}
			if (rate>0){
				dataVO.setIssuePrice(String.valueOf(StrUtils.getDoubleVal(dataVO.getIssuePrice())*rate));
				dataVO.setLastNAV(String.valueOf(StrUtils.getDoubleVal(dataVO.getLastNAV())*rate));
				//dataVO.setMinInitAmount(String.valueOf(StrUtils.getDoubleVal(dataVO)*rate));
				//dataVO.setMinSubscribeAmount(String.valueOf(StrUtils.getDoubleVal(dataVO.getMinSubscribeAmount())*rate));
				//dataVO.setMinHoldingAmount(String.valueOf(StrUtils.getDoubleVal(dataVO.getMinHoldingAmount())*rate));
				//dataVO.setMinRedemptionAmount(String.valueOf(StrUtils.getDoubleVal(dataVO.getMinRedemptionAmount())*rate));
				//dataVO.setMinRspAmount(String.valueOf(StrUtils.getDoubleVal(dataVO.getMinRspAmount())*rate));
//				dataVO.setFundSize(String.valueOf(StrUtils.getDoubleVal(dataVO.getFundSize())*rate));
			}else dataVO.setToCurrency(fromCurrency);//获取兑换率失败，不转换货币	
		}else {
			dataVO.setIssuePrice(String.valueOf(StrUtils.getDoubleVal(dataVO.getIssuePrice())));
			dataVO.setLastNAV(String.valueOf(StrUtils.getDoubleVal(dataVO.getLastNAV())));
			 dataVO.setToCurrency(dataVO.getFundCurrencyCode());
		}
		
		
		return dataVO;
	}
	
	/**
	 * 获取基金基础数据列表
	 * @return
	 */
	public List<FundProductVO> getFundProductVoList(String langCode){
		List<FundProductVO> voList = new ArrayList<FundProductVO>();
		List params = new ArrayList();
		String hql = " FROM FundInfo r "
			+ " INNER JOIN " + this.getLangString("FundInfo", langCode)+" i ON r.id=i.id ";
		List<Object> list = this.baseDao.find(hql, null, false);
		if(!list.isEmpty()){
			for (int i = 0; i < list.size(); i++) {
				Object[] objRead = (Object[])list.get(i);
				FundInfo info = (FundInfo)objRead[0];
				FundProductVO vo = new FundProductVO();
				vo.setId(info.getId());
				if(!"".equals(info.getProduct()) && null!=info.getProduct()){
					vo.setProductId(info.getProduct().getId());
				}
				if("sc".equals(langCode)){
					FundInfoSc sc = (FundInfoSc)objRead[1];
					vo.setName(sc.getFundName());
				} else if("tc".equals(langCode)){
					FundInfoTc tc = (FundInfoTc)objRead[1];
					vo.setName(tc.getFundName());
				} else if("en".equals(langCode)){
					FundInfoEn en = (FundInfoEn)objRead[1];
					vo.setName(en.getFundName());
				}
				voList.add(vo);
			}
		}
		return voList;
	}
	
	/**
	 * 获取基金列表(Allocation页面)
	 * @author michael
	 * @param jsonPaging 分页参数
	 * @param filters 过滤条件
	 * @return
	 */
	//@Transactional(readOnly = true)
	public JsonPaging findFundInfoListForAllocation(JsonPaging jsonPaging, FundScreenerDataVO filters){
		StringBuffer hql = new StringBuffer("SELECT i.id, i.isinCode, i.riskLevel, i.lastNav, i.lastNavUpdate, i.issuePrice, i.issueDate, " +
				"i.mgtFee, i.minInitialAmount, i.minSubscribeAmount, i.minHoldingAmount, i.minRedemptionAmount, " +
				"i.minRspAmount, i.createTime, i.lastUpdate, i.isValid, f.id, f.fundName, f.fundNamePinyin, f.domicileCode, f.domicile, f.fundHouse, " +
				"f.fundManCo, f.fundManager, f.fundSize, f.fundCurrencyCode, f.fundCurrency, f.fundTypeCode, f.fundType, f.sectorTypeCode, " +
				"f.sectorType, f.geoAllocationCode, f.geoAllocation, f.invTarget, f.keyRisks, i.product,p.symbolCode FROM FundInfo i ");
		List params = new ArrayList();
		hql.append(" left join "+this.getLangString("FundInfo", filters.getLangCode()));
		hql.append(" f on i.id=f.id  ");
		hql.append(" LEFT JOIN ProductDistributor p ON p.product.id=i.product.id ");//代理商关联的产品信息
		hql.append(" LEFT JOIN MemberDistributor md ON p.distributor.id=md.id ");//代理商
		
		hql.append("   where i.isValid='1' ");
		String idString = StrUtils.seperateWithQuote(filters.getFundHouseIds(), "'");
		if(idString != null && !"".equals(idString)){
			hql.append(" and f.fundHouseId.id in ( "+idString+" ) ");
//			params.add(idString);
		}
		if(filters.getFundHouse() != null && !"".equals(filters.getFundHouse())){
			hql.append(" and f.fundHouse like ? ");
			params.add("%"+filters.getFundHouse()+"%");
		}
		
		if(filters.getCurrency() != null && !"".equals(filters.getCurrency())){
			hql.append(" and f.fundCurrency = ? ");
			params.add(filters.getCurrency());
		}
		
		if(null != filters.getFundType() && !"".equals(filters.getFundType())){//可多选，用逗号分隔
			String[] idArrStrings = filters.getFundType().split(",");
			hql.append(" and ( " );
			if (idArrStrings!=null && idArrStrings.length>0){
            	for(int k=0;k<idArrStrings.length;k++){
            		String cd = idArrStrings[k].toString();
            		if (!StrUtils.isEmpty(cd)){
	            		hql.append("f.fundTypeCode like '%"+ cd +"%'");
	            		if (k<idArrStrings.length-1)  hql.append(" or ");
            		}
            	}
			}
			hql.append(" )");
		}
		
		if(null != filters.getSectorType() && !"".equals(filters.getSectorType())){//可多选，用逗号分隔
			String[] idArrStrings = filters.getSectorType().split(",");
			hql.append(" and ( " );
			if (idArrStrings!=null && idArrStrings.length>0){
            	for(int k=0;k<idArrStrings.length;k++){
            		String cd = idArrStrings[k].toString();
            		if (!StrUtils.isEmpty(cd)){
	            		hql.append("f.sectorTypeCode like '%"+ cd +"%'");
	            		if (k<idArrStrings.length-1)  hql.append(" or ");
            		}
            	}
			}
			hql.append(" )");
		}

		if(null != filters.getGeoAllocation() && !"".equals(filters.getGeoAllocation())){//可多选，用逗号分隔
			String[] idArrStrings = filters.getGeoAllocation().split(",");
			hql.append(" and ( " );
			if (idArrStrings!=null && idArrStrings.length>0){
            	for(int k=0;k<idArrStrings.length;k++){
            		String cd = idArrStrings[k].toString();
            		if (!StrUtils.isEmpty(cd)){
	            		hql.append("f.geoAllocationCode like '%"+ cd +"%'");
	            		if (k<idArrStrings.length-1)  hql.append(" or ");
            		}
            	}
			}
			hql.append(" )");
		}
		
		if(filters.getRiskRating() != null && !"".equals(filters.getRiskRating())){
			hql.append(" and i.riskLevel = ? ");
			params.add(filters.getRiskRating());
		}
		
		if(filters.getKeyword() != null && !"".equals(filters.getKeyword())){
			hql.append(" and (f.fundName like ? or f.fundNamePinyin like ?) ");
			params.add("%"+filters.getKeyword()+"%");
			params.add("%"+filters.getKeyword()+"%");
		}
		
		if(filters.getStrategyId() != null && !"".equals(filters.getStrategyId())){
			hql.append(" and i.product.id in (select p.product.id from StrategyProduct p where p.product.id is not null and p.strategy.id=?) ");
			params.add(filters.getStrategyId());
		}

		if(filters.getPortfolioArenaId() != null && !"".equals(filters.getPortfolioArenaId())){
			hql.append(" and i.product.id in (select p.product.id from PortfolioArenaProduct p where p.product.id is not null and p.portfolio.id=?) ");
			params.add(filters.getPortfolioArenaId());
		}

		if(filters.getPortfolioId() != null && !"".equals(filters.getPortfolioId())){
			hql.append(" and i.product.id in (select p.product.id from PortfolioInfoProduct p where p.product.id is not null and p.portfolio.id=?) ");
			params.add(filters.getPortfolioId());
		}
		
		//distributor
		if(filters.getDistributorId() != null && !"".equals(filters.getDistributorId())){
			String[] disArray = filters.getDistributorId().split(",");
			String distributorSql = "";
			for(String distributorId : disArray){
				distributorSql += "'" + distributorId + "',";
			}
			distributorSql = distributorSql.substring(0, distributorSql.length()-1);
			
			hql.append(" and i.product.id in ( select pd.product.id from ProductDistributor pd where pd.distributor.id in ("+distributorSql+") ) ");
			//params.add(distributorSql);
		} 
//		else{
//			hql.append(" and i.product.id in ( select pd.product.id from ProductDistributor pd where pd.distributor.id in ('') ) ");
//		}
		
		hql.append(" order by convert(f.fundName,'gbk') ");
		jsonPaging.setOrder("");
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , true);
		List voList = jsonPaging.getList();
		List list = new ArrayList();
		
		if(!voList.isEmpty()){
			String memberId = null;
			if(null!=filters.getLoginUser() && !"".equals(filters.getLoginUser()))
				memberId = filters.getLoginUser().getId();
			
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
				vo.setFundNamePinyin(StrUtils.getString(objs[idx++]));
				vo.setDomicileCode(StrUtils.getString(objs[idx++]));
				vo.setDomicile(StrUtils.getString(objs[idx++]));
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
				
				//获取产品信息
				ProductInfo product = (ProductInfo)objs[idx++];
				vo.setSymbolCode(StrUtils.getString(objs[idx++]));
				if (null!=product) vo.setProductId(product.getId());
				
				//设置回报及评级信息
				vo = setFundInfos(vo,filters.getLangCode(),memberId,null,filters.getBeforeYears());
				
				list.add(vo);
			}
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}


	/**
	 * 保存基金基本信息
	 * @author michael
	 * @param info
	 * @return
	 */
	public FundInfo saveOrUpdate(FundInfo info){
		return (FundInfo)this.baseDao.saveOrUpdate(info);
	}
	
	/**
	 * 根据 id 获取产品信息
	 * @author wwluo
	 * @param productId 产品ID
	 * @return
	 */
	public ProductInfo findProductById(String productId){
		return (ProductInfo) this.baseDao.get(ProductInfo.class, productId);
	}
	
	/**
	 * 根据产品ID获取基金信息
	 * @author wwluo
	 * @param productId 产品ID
	 * @return
	 */
	public FundInfo getFundInfoByProduct(String productId){
		FundInfo fund = null;
		if (StringUtils.isNotBlank(productId)) {
			StringBuffer hql = new StringBuffer(" from FundInfo where isValid=1 and product_id=?");
			List params = new ArrayList();
			params.add(productId);
			List<FundInfo> list = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(null != list && !list.isEmpty()){
				fund = list.get(0);
			}
		}
		return fund;
	}

	/**
	 * 保存一条浏览记录   WebInvestorVisit
	 * @author wwluo
	 * @date 2016-11-16
	 * @param WebInvestorVisit 
	 * @return
	 */
	@Override
	public WebInvestorVisit saveWebInvestorVisit(WebInvestorVisit investorVisit) {
		return (WebInvestorVisit) this.baseDao.saveOrUpdate(investorVisit);
	}

	/**
	 * 获取基金浏览历史
	 * @author wwluo
	 * @date 2016-11-16
	 * @param MemberBase 当前用户 
	 * @return
	 */
	@Override
	public List<String> getFundBrowserHistory(MemberBase loginUser, Integer maxResults) {
		List<String> relateIds = null;
		if(loginUser != null){
			StringBuffer hql = new StringBuffer("" +
					" select" +
					" w.relateId" +
					" from" +
					" WebInvestorVisit w" +
					" where" +
					" w.member=? and" +
					" w.moduleType='fund'" +
					" order by w.vistiTime desc" +
					"");
			List<Object> params = new ArrayList<Object>();
			params.add(loginUser);
			relateIds = this.baseDao.find(hql.toString(), params.toArray(), false, 5);
		}
		return relateIds;
	}

	/**
	 * 获取表现最好的基金
	 * @author wwluo
	 * @date 2016-11-16
	 * @param
	 * @return
	 */
	@Override
	public List<FundReturn> getBestFundReturn(String period,Integer count) {
		StringBuffer hql = new StringBuffer("" +
				" from" +
				" FundReturn" +
				" where" +
				" isValid=1 and" +
				" periodCode=?" +
				//" limit 0 offset 8" +
				" order by increase desc");
		List params = new ArrayList();
		params.add(period);
		//params.add(count);
		List<FundReturn> fundReturns = this.baseDao.find(hql.toString(), params.toArray(),0,count, false);
		return fundReturns;
	}

	/**
	 * 查找当前用户某个区间的浏览历史
	 * @author wwluo
	 * @date 2016-11-16
	 * @param loginUser 当前用户 
	 * @param fundId 对应relateId 
	 * @param period 查找区间的历史
	 * @return
	 */
	@Override
	public List<WebInvestorVisit> getWebInvestorVisit(MemberBase loginUser,
			String relateId, Date fromTime) {
		StringBuffer hql = new StringBuffer("" +
				" from" +
				" WebInvestorVisit w" +
				" where" +
				" w.relateId=? and" +
				" w.member=? and" +
				" w.vistiTime" +
				" between ? and ?");
		List params = new ArrayList();
		params.add(relateId);
		params.add(loginUser);
		params.add(fromTime);
		params.add(new Date());
		List<WebInvestorVisit> webInvestorVisits = this.baseDao.find(hql.toString(), params.toArray(), false);
		return webInvestorVisits;
	}
	
	/**
	 * 获取ifa推荐的基金列表
	 *  @author mqzou	
	 * @date 2016-11-30
	 * @param jsonPaging
	 * @param langCode
	 * @param keyWord
	 * @param ifaMemberId
	 * @return
	 */
	@Override
	public JsonPaging findByIfaRecommend(JsonPaging jsonPaging, String langCode, String keyWord, String ifaMemberId) {
		StringBuilder hql=new StringBuilder();
		List<Object> params=new ArrayList<Object>();
		hql.append(" from WebRecommended r left join FundInfo f on r.relateId=f.id  ");
		hql.append(" left join "+getLangString("FundInfo", langCode)+" l on f.id=l.id");
		hql.append(" where r.creator.id=? and r.moduleType='fund'");
		params.add(ifaMemberId);
		if(null!=keyWord && !"".equals(keyWord)){
			hql.append(" and l.fundName like ?");
			params.add("%"+keyWord+"%");
		}
		jsonPaging=this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging, false);
		Iterator it=jsonPaging.getList().iterator();
		List list=new ArrayList();
		while (it.hasNext()) {
			FundInfoDataVO vo = new FundInfoDataVO();
			Object[] object = (Object[]) it.next();
			//WebRecommended recommended=(WebRecommended)object[0];
			FundInfo info=(FundInfo)object[1];
			
			
			//基金基本信息
			vo.setFundInfo(new FundInfo());
			vo.getFundInfo().setId(info.getId());
			vo.getFundInfo().setIsinCode(info.getIsinCode());
			vo.getFundInfo().setRiskLevel(info.getRiskLevel());
			vo.getFundInfo().setLastNav(info.getLastNav());
			vo.getFundInfo().setLastNavUpdate(info.getLastNavUpdate());
			vo.getFundInfo().setIssuePrice(info.getIssuePrice());
			vo.getFundInfo().setIssueDate(info.getIssueDate());
			vo.getFundInfo().setMgtFee(info.getMgtFee());
			vo.getFundInfo().setMinInitialAmount(info.getMinInitialAmount());
			vo.getFundInfo().setMinSubscribeAmount(info.getMinSubscribeAmount());
			vo.getFundInfo().setMinHoldingAmount(info.getMinHoldingAmount());
			vo.getFundInfo().setMinRedemptionAmount(info.getMinRedemptionAmount());
			vo.getFundInfo().setMinRspAmount(info.getMinRspAmount());
			vo.getFundInfo().setCreateTime(info.getCreateTime());
			vo.getFundInfo().setLastUpdate(info.getLastUpdate());
			vo.getFundInfo().setIsValid(info.getIsValid());
			
			//基金附加信息（多语言）
			if(CommonConstants.LANG_CODE_EN.equals(langCode)){
				FundInfoEn en=(FundInfoEn)object[2];
				vo.setFundId(info.getId());
				vo.setFundName(en.getFundName());
				vo.setDomicileCode(en.getDomicileCode());
				vo.setDomicile(en.getDomicile());
				vo.setFundHouse(en.getFundHouse());
				vo.setFundManCo(en.getFundManCo());
				vo.setFundManager(en.getFundManager());
				vo.setFundSize(en.getFundSize());
				vo.setFundCurrencyCode(en.getFundCurrencyCode());
				vo.setFundCurrency(en.getFundCurrency());
				vo.setFundTypeCode(en.getFundTypeCode());
				vo.setFundType(en.getFundType());
				vo.setSectorTypeCode(en.getSectorTypeCode());
				vo.setSectorType(en.getSectorType());
				vo.setGeoAllocationCode(en.getGeoAllocationCode());
				vo.setGeoAllocation(en.getGeoAllocation());
				vo.setInvTarget(en.getInvTarget());
				vo.setKeyRisks(en.getKeyRisks());
			}else if(CommonConstants.LANG_CODE_SC.equals(langCode)) {
				FundInfoSc sc=(FundInfoSc)object[2];
				vo.setFundId(info.getId());
				vo.setFundName(sc.getFundName());
				vo.setDomicileCode(sc.getDomicileCode());
				vo.setDomicile(sc.getDomicile());
				vo.setFundHouse(sc.getFundHouse());
				vo.setFundManCo(sc.getFundManCo());
				vo.setFundManager(sc.getFundManager());
				vo.setFundSize(sc.getFundSize());
				vo.setFundCurrencyCode(sc.getFundCurrencyCode());
				vo.setFundCurrency(sc.getFundCurrency());
				vo.setFundTypeCode(sc.getFundTypeCode());
				vo.setFundType(sc.getFundType());
				vo.setSectorTypeCode(sc.getSectorTypeCode());
				vo.setSectorType(sc.getSectorType());
				vo.setGeoAllocationCode(sc.getGeoAllocationCode());
				vo.setGeoAllocation(sc.getGeoAllocation());
				vo.setInvTarget(sc.getInvTarget());
				vo.setKeyRisks(sc.getKeyRisks());
			}else if(CommonConstants.LANG_CODE_TC.equals(langCode)) {
				FundInfoTc tc=(FundInfoTc)object[2];
				vo.setFundId(info.getId());
				vo.setFundName(tc.getFundName());
				vo.setDomicileCode(tc.getDomicileCode());
				vo.setDomicile(tc.getDomicile());
				vo.setFundHouse(tc.getFundHouse());
				vo.setFundManCo(tc.getFundManCo());
				vo.setFundManager(tc.getFundManager());
				vo.setFundSize(tc.getFundSize());
				vo.setFundCurrencyCode(tc.getFundCurrencyCode());
				vo.setFundCurrency(tc.getFundCurrency());
				vo.setFundTypeCode(tc.getFundTypeCode());
				vo.setFundType(tc.getFundType());
				vo.setSectorTypeCode(tc.getSectorTypeCode());
				vo.setSectorType(tc.getSectorType());
				vo.setGeoAllocationCode(tc.getGeoAllocationCode());
				vo.setGeoAllocation(tc.getGeoAllocation());
				vo.setInvTarget(tc.getInvTarget());
				vo.setKeyRisks(tc.getKeyRisks());
			}
		
			//设置回报及评级信息
			vo = setFundInfos(vo,langCode,ifaMemberId,null,"5");
			list.add(vo);
		}
		jsonPaging.setList(list);
		return jsonPaging;
	}

	/**
	 * 获取基金信息集合
	 * @author wwluo	
	 * @date 2016-12-06
	 * @return
	 */
	@Override
	public List<FundInfoDataVO> getFundDataByFunds(MemberBase loginUser,
			String langCode, List<Map> fundMaps, String toCurrency) {
		List<FundInfoDataVO> fundInfoList = new ArrayList<FundInfoDataVO>();
		if(fundMaps != null){
			for (Map map : fundMaps) {
				String fund = (String) map.get("fund");
				Double weight = null;
				if(map.get("weight") instanceof String){
					String weightStr = (String) map.get("weight");
					weight = Double.valueOf(weightStr);
				}else if(map.get("weight") instanceof Integer){
					Integer weightInt = (Integer) map.get("weight");
					weight = Double.valueOf(weightInt);
				}else{
					
				}
				List<String> spareFunds = (List<String>) map.get("spareFunds");
				FundInfoDataVO fundInfoVO = getFundData(loginUser, langCode, fund, toCurrency);
				List<FundInfoDataVO> spareFundList = new ArrayList<FundInfoDataVO>();
				List<String> checkList = new ArrayList<String>();
				if(spareFunds != null && !spareFunds.isEmpty() ){
					//多个基金
		    		for (String fundId : spareFunds) {
			    		if(fundId!=null && fundId.length()>0 && !checkList.contains(fundId)){
			    			checkList.add(fundId);
			    			FundInfoDataVO spareFundVO = getFundData(loginUser, langCode, fundId,toCurrency);
			    			String toCurrencyName = this.getParamConfigName(langCode, toCurrency,CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
			    			spareFundVO.setToCurrencyName(toCurrencyName);
			    			spareFundList.add(spareFundVO);
			    		}
					}
		    	}
		    	fundInfoVO.setSpareFunds(spareFundList);
				String toCurrencyName = this.getParamConfigName(langCode, toCurrency,CommonConstantsWeb.SYS_PARM_TYPE_CURRENCY_TYPE);
				fundInfoVO.setToCurrencyName(toCurrencyName);
				if(weight != null){
					fundInfoVO.setProductWeight(weight*100);
				}
				fundInfoList.add(fundInfoVO);
			}
		}
		return fundInfoList;
	}


	/**
	 * 获取基金信息
	 * @author wwluo	
	 * @date 2016-12-06
	 * @return
	 */
	public FundInfoDataVO getFundData(MemberBase loginUser, String langCode,
			String fundIds, String toCurrency) {
		FundInfoDataVO fundInfoVO = this.findFundFullInfoById(fundIds);
		//货币转换
		if (StringUtils.isNotBlank(toCurrency) && fundInfoVO != null) {
			Double rate = null;
			String fromCurrency = null;
			if (fundInfoVO.getFundInfoEn() != null && StringUtils.isNotBlank(fundInfoVO.getFundInfoEn().getFundCurrencyCode())) {
				fromCurrency = fundInfoVO.getFundInfoEn().getFundCurrencyCode();
			}else if (fundInfoVO.getFundInfoTc() != null && StringUtils.isNotBlank(fundInfoVO.getFundInfoTc().getFundCurrencyCode())) {
				fromCurrency = fundInfoVO.getFundInfoTc().getFundCurrencyCode();
			}else if (fundInfoVO.getFundInfoSc() != null && StringUtils.isNotBlank(fundInfoVO.getFundInfoSc().getFundCurrencyCode())) {
				fromCurrency = fundInfoVO.getFundInfoSc().getFundCurrencyCode();
			}
			if (StringUtils.isNotBlank(fromCurrency) && !fromCurrency.equals(toCurrency)){
				WebExchangeRate exchangeRate =	this.findExchangeRate(fromCurrency, toCurrency);
				if(exchangeRate != null){
					rate = this.findExchangeRate(fromCurrency, toCurrency).getRate();
				}	
			}
			if(rate == null){
				rate = 1.00;
			}
			if (rate <= 0.0) fundInfoVO.setToCurrency(fromCurrency);
			//fundInfoVO.setAmounts(rate);
			if(fundInfoVO.getFundInfo() != null && fundInfoVO.getFundInfo().getLastNav() != null){
				fundInfoVO.getFundInfo().setLastNav(fundInfoVO.getFundInfo().getLastNav() * rate);
			}
		}
		fundInfoVO.setFundId(fundIds);
		fundInfoVO.setToCurrency(toCurrency);
		fundInfoVO.setDefaultInfoByLang(langCode);
		//fundInfoVO = this.setFundInfos(fundInfoVO,langCode,loginUser.getId(),toCurrency,null);
		return fundInfoVO;
	}
	
	/**
	 * 获取最新净值收益率
	 * @author wwluo	
	 * @date 2016-12-06
	 * @return
	 */
	public Double getLastNavRateOfReturn(String fundId) {
		Double rateOfReturn = null;
		if(StringUtils.isNotBlank(fundId)){
			StringBuffer hql = new StringBuffer("" +
					" FROM" +
					" FundMarket m" +
					" WHERE" +
					" m.fund.id=?" +
					" ORDER BY" +
					" m.marketDate" +
					" DESC");
			List<Object> params = new ArrayList<Object>();
			params.add(fundId);
			List<FundMarket> fundMarkets = this.baseDao.find(hql.toString(), params.toArray(), false);
			if(fundMarkets != null && fundMarkets.size()>2){
				Double lastNav = fundMarkets.get(0).getNav();
				Double lastTimeNav = fundMarkets.get(1).getNav();
				rateOfReturn = new BigDecimal((lastNav-lastTimeNav)/lastTimeNav).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
			}
		}
		return rateOfReturn;
	}
	
	/**
	 * 获取基金信息集合
	 * @author wwluo	
	 * @return 
	 * @date 2016-12-06
	 * @return
	 */
	@Override
	public WebBusLog saveWebBusLog(WebBusLog busLog) {
		return (WebBusLog) this.baseDao.saveOrUpdate(busLog);
	}
	
	/**
     * 更新/保存方法
     */
	@Override
	public FundInfo save(FundInfo fundInfo, FundInfoSc fundInfoSc, FundInfoTc fundInfoTc
			, FundInfoEn fundInfoEn, boolean isAdd) {
		fundInfo = (FundInfo)this.baseDao.saveOrUpdate(fundInfo,isAdd);	
		if(isAdd) {
			fundInfoSc.setId(fundInfo.getId());
			this.baseDao.create(fundInfoSc);		
			fundInfoTc.setId(fundInfo.getId());
			this.baseDao.create(fundInfoTc);
			fundInfoEn.setId(fundInfo.getId());
			this.baseDao.create(fundInfoEn);
		} else {
			fundInfoSc.setId(fundInfo.getId());
			this.baseDao.saveOrUpdate(fundInfoSc,isAdd);
			fundInfoTc.setId(fundInfo.getId());
			this.baseDao.saveOrUpdate(fundInfoTc,isAdd);
			fundInfoEn.setId(fundInfo.getId());
			this.baseDao.saveOrUpdate(fundInfoEn,isAdd);
		}
		return fundInfo;
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteById(String id){
		if (id == null) {
			return false;
		}else{
			FundInfo info = findFundInfoById(id);
			if(info != null){
				deleteRelate(id);
				baseDao.delete(info);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 删除其他关联记录
	 * @param id
	 */
	private void deleteRelate(String id){
		FundInfoSc infoSc = findFundInfoScById(id);
		if(infoSc != null){
			baseDao.delete(infoSc);
		}
		FundInfoTc infoTc = findFundInfoTcById(id);
		if(infoTc != null){
			baseDao.delete(infoTc);
		}
		FundInfoEn infoEn = findFundInfoEnById(id);
		if(infoEn != null){
			baseDao.delete(infoEn);
		}
	}
	
	/**
	 * 通过ID更改一条记录状态
	 * @param id
	 * @return
	 */
	@Override
	public boolean updateIsValid(String id){
		if (id == null) {
			return false;
		}else{
			FundInfo info = findFundInfoById(id);
			if(info != null){
				info.setIsValid("0");
				baseDao.update(info);
				return true;
			}else{
				return false;
			}
		}
	}
	
	/**
	 * 获取注册地国家列表
	 * @return
	 */
	@Override
	public List<SysCountry> getCountryList() {
		String hql = " FROM SysCountry ";
		List<SysCountry> list = (List<SysCountry>)this.baseDao.find(hql, null, false);
		return list;
	}	

	/***
     * 更新/保存方法
     */
	@Override
	public FundAnno saveAnno(FundAnno info, boolean isAdd){
		info = (FundAnno)this.baseDao.saveOrUpdate(info, isAdd);
		return info;
	}
	
	/***
     * 更新/保存方法
     */
	@Override
	public FundBonus saveBonus(FundBonus info, boolean isAdd){
		info = (FundBonus)this.baseDao.saveOrUpdate(info, isAdd);
		return info;
	}
	
	/***
     * 更新/保存方法
     */
	@Override
	public FundDoc saveDoc(FundDoc info, boolean isAdd){
		info = (FundDoc)this.baseDao.saveOrUpdate(info, isAdd);
		return info;
	}
	
	/**
	 * 通过ID删除一条记录
	 * @param id
	 * @return
	 */
	@Override
	public boolean updateFileValidById(String id, String type){
		boolean flag = false;
		if(StringUtils.isNotBlank(id) && "anno".equals(type)) {
			FundAnno info = findFundAnno(id);
			if(info != null){
				info.setIsValid("0");
				baseDao.update(info);
				flag = true;
			}
		} else if(StringUtils.isNotBlank(id) && "bonus".equals(type)) {
			FundBonus info = findFundBonusById(id);
			if(info != null){
				info.setIsValid("0");
				baseDao.update(info);
				flag = true;
			}
		} else if(StringUtils.isNotBlank(id) && "doc".equals(type)) {
			FundDoc info = findFundDoc(id);
			if(info != null){
				info.setIsValid("0");
				baseDao.update(info);
				flag = true;
			}
		} 
		return flag;
	}
	
	/**
	 * 查找关联的文件
	 * @param relateId
	 * @return
	 */
	@Override
	public List<AccessoryFile> findAccessoryList(String relateId) {
		List<AccessoryFile> list = new ArrayList<AccessoryFile>();
		String hql = " FROM AccessoryFile f "
			//+ " INNER JOIN SysAdmin s ON f.createBy=s.loginCode "
			+ " WHERE 1=1 ";
		List<String> params = new ArrayList<String>();
		if(StringUtils.isNotBlank(relateId)){
			hql += " AND f.relateId = ? ";
			params.add(relateId);
		}
		list = this.baseDao.find(hql, params.toArray(), false);

		return list;
	}
	
	/**
	 * 根据产品ID获取基金信息（基金的基表信息与基金的多语言信息）
	 * @author 林文伟
	 * @param productId 产品ID
	 * @return
	 */
	@Override
	public FundInfo getFundInfoByProductId(String productId,String langCode){
		FundInfo fund = new FundInfo();
		if (StringUtils.isNotBlank(productId)) {
			String hql = "select a.lastNav,b.fundName,b.id  from FundInfo a left join "+this.getLangString("FundInfo", langCode)+" b on a.id=b.id  where a.isValid=1 and a.product.id='"+productId+"'";
			List params = new ArrayList();
			params.add(productId);
			List  list = this.baseDao.find(hql.toString(), null, false);
			if(!list.isEmpty()){
				Object[] objs = (Object[])list.get(0);
				String lastNav = objs[0]==null?"0":objs[0].toString();
				String fundName = objs[1]==null?"":objs[1].toString();
				String fundId = objs[2]==null?"":objs[2].toString();
				fund.setLastNav(StrUtils.getDouble(lastNav));
				fund.setTempFundName(fundName);
				fund.setId(fundId);
			}
		}
		return fund;
	}
	
	/**
	 * 根据产品ID获取股票信息（股票的基表信息与股票的多语言信息）
	 * @author 林文伟
	 * @param productId 产品ID
	 * @return
	 */
	@Override
	public StockInfo getStockInfoByProductId(String productId,String langCode){
		StockInfo stock = new StockInfo();
		if (StringUtils.isNotBlank(productId)) {
			String hql = "select a.lastNav,b.stockName  from StockInfo a left join "+this.getLangString("StockInfo", langCode)+" b on a.id=b.id  where a.isValid=1 and a.product.id='"+productId+"'";
			List params = new ArrayList();
			params.add(productId);
			List  list = this.baseDao.find(hql.toString(), null, false);
			if(!list.isEmpty()){
				Object[] objs = (Object[])list.get(0);
				String lastNav = objs[0]==null?"0":objs[0].toString();
				String fundName = objs[1]==null?"":objs[1].toString();
				//stock.setLastNav(StrUtils.getDouble(lastNav));
				//stock.setTempFundName(fundName);
			}
		}
		return stock;
	}
	
	/**
	 * 根据产品ID获取债劵信息（债劵的基表信息与债劵的多语言信息）
	 * @author 林文伟
	 * @param productId 产品ID
	 * @return
	 */
	@Override
	public BondInfo getBondInfoByProductId(String productId,String langCode){
		BondInfo bond = new BondInfo();
		if (StringUtils.isNotBlank(productId)) {
			String hql = "select a.exchangeCode,b.bondName  from BondInfo a left join "+this.getLangString("BondInfo", langCode)+" b on a.id=b.id  where  a.product.id='"+productId+"'";
			List params = new ArrayList();
			params.add(productId);
			List  list = this.baseDao.find(hql.toString(), null, false);
			if(!list.isEmpty()){
				Object[] objs = (Object[])list.get(0);
				String exchange = objs[0]==null?"0":objs[0].toString();
				String bondName = objs[1]==null?"":objs[1].toString();
				bond.setExchangeCode(exchange);
				bond.setBondName(bondName);
			}
		}
		return bond;
	}

	/**
     * 汇总基金组合（Ecahrt饼图）
     * @author wwluo
     * @date 2017/2/2/23
     * @param request
     * @param response
     * @param model
     * @return
     */
	@Override
	public Map<String, Double> getfundPortfolioWeight(String funds,String type,String langCode) {
		Map<String, Double> result = new TreeMap<String, Double>();
		if (StringUtils.isNotBlank(funds)) {
			List<Map> fundMaps = JsonUtil.toListMap(funds);
			if(fundMaps != null && !fundMaps.isEmpty()){
				Double total = 0.00;
				Map<String, Double> map = new TreeMap<String, Double>();
				for (Map fundMap : fundMaps) {
					String fundId = (String) fundMap.get("fundId");
					String weightStr = (String) fundMap.get("weight");
					Double weight = null;
					if (StringUtils.isNotBlank(weightStr)) {
						weight = Double.parseDouble(weightStr)/100;
					}
					if(weight == null){
						weight = 0.00;
					}
					if(StringUtils.isNotBlank(fundId)){
						StringBuffer hql = new StringBuffer("" +
								" SELECT l.name,p.rate FROM" +
								" FundPortfolio p" +
								" LEFT JOIN" +
								" FundPortfolio" + this.getLangFirstCharUpper(langCode) + " l" +
								" ON" +
								" l.id=p.id" +
								" WHERE" +
								" p.fund.id=?" +
								" AND" +
								" p.type=?" +
								" AND" +
								" p.isValid=1");
						List<Object> params = new ArrayList<Object>();
						params.add(fundId);
						params.add(type);
						List<Object[]> objs = this.baseDao.find(hql.toString(), params.toArray(), false);
						if(objs != null && !objs.isEmpty()){
							for (Object[] objects : objs) {
								String name = (String) objects[0];
								Double rate = (Double) objects[1];
								total = total + rate * weight;
								if(name != null){
									if(map.get(name) == null){
										rate = rate * weight;
										map.put(name, rate);
									}else{
										Double value = map.get(name);
										value = value + rate * weight;
										map.put(name, value);
									}
								}
							}
							for (String key : map.keySet()) {
								Double rate = map.get(key);
								if(total > 0){
									result.put(key, rate/total*100);
								}
							}
						}
					}
				}
			}
		}
		if(result != null && result.isEmpty()){
			result = null;
		}
		return result;
	}

	/**
     * 获取基金集合
     * @author wwluo
     * @date 2017/2/23
     * @return
     */
	@Override
	public List<FundInfo> findAllFunds(Integer maxResults) {
		StringBuffer hql = new StringBuffer(" FROM FundInfo i WHERE i.isValid=1 ORDER BY i.lastUpdate DESC");
		List<FundInfo> fundInfos = this.baseDao.find(hql.toString(), null, true, maxResults);
		return fundInfos;
	}
	
	
	//代理商-业务管理-投资顾问公司管理
	
	/**
	 * 获取代理商下的投资顾问公司的代理产品(基金)
     * @author rqwang
     * @date 2017/06/14
	 * @return
	 */
	@Override
	public JsonPaging findDisIfafirmFundInfo(JsonPaging jsonPaging,
			String keyword, String disId, String ifaFirmId, String langCode) {
		
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from FundInfo l ");
		hql.append(" LEFT join "+this.getLangString("FundInfo", langCode));
		hql.append(" r on l.id=r.id ");
		hql.append(" left join ProductInfo p on l.product.id=p.id ");
		hql.append(" left join ProductDistributor pd on l.product.id=pd.product.id ");
		hql.append(" left join ProductIfafirmDistributor pid on l.product.id=pid.product.id ");
		hql.append(" where l.isValid='1'");
		hql.append(" and p.type='fund'");
		hql.append(" and pd.distributor.id=?");
		hql.append(" and pid.ifafirm.id=?");
		params.add(disId);
		params.add(ifaFirmId);
		
		if(keyword != null && !"".equals(keyword)){
			hql.append(" and ( l.isinCode like ?  or r.fundName like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		
		return jsonPaging;
	}
	
	/**
	 * 获取代理商下的投资顾问公司的代理产品(基金)添加页面列表数据
     * @author rqwang
     * @date 2017/06/15
	 * @return
	 */
	@Override
	public JsonPaging findDisIfafirmFundInfoInput(JsonPaging jsonPaging,
			String keyword, String disId, String ifaFirmId, String langCode) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder("from FundInfo l ");
		hql.append(" LEFT join "+this.getLangString("FundInfo", langCode));
		hql.append(" r on l.id=r.id ");
		hql.append(" left join ProductInfo p on l.product.id=p.id ");
		hql.append(" left join ProductDistributor pd on l.product.id=pd.product.id ");
		hql.append(" where l.isValid='1'");
		hql.append(" and p.type='fund'");
		hql.append(" and pd.distributor.id=?");
		hql.append(" and pd.product.id not in" );
		hql.append(" (select pp.product.id from ProductIfafirmDistributor pp ");
		hql.append(" where pp.ifafirm.id=? and pp.distributor.id=?)");
		
		params.add(disId);
		params.add(ifaFirmId);
		params.add(disId);
		
		if(keyword != null && !"".equals(keyword)){
			hql.append(" and ( l.isinCode like ?  or r.fundName like ? )");
			params.add("%"+keyword+"%");
			params.add("%"+keyword+"%");
		}
		
		jsonPaging = this.baseDao.selectJsonPaging(hql.toString(), params.toArray(), jsonPaging , false);
		
		return jsonPaging;
	}

	/**
     * 基金公司下拉列表框显示
     * @author rqawng
     * @date 2017-06-15
	 * @return
	 */
	@Override
	public List<String> findRiskLevelList(String disId,String langCode) {
		List<Object> params = new ArrayList<Object>();
		StringBuilder hql = new StringBuilder();
		
		hql.append(" select  DISTINCT(lang.fundHouse)");
		hql.append(" as fundHouse from FundInfo t");
		hql.append(" LEFT JOIN "+this.getLangString("FundInfo", langCode) + " lang");
		hql.append(" 	on t.id=lang.id");
		hql.append(" LEFT JOIN ProductInfo p");
		hql.append(" 	on t.ProductInfo.id=p.id");
		hql.append(" LEFT JOIN ProductDistributor pd");
		hql.append(" 	on t.ProductInfo.id=pd.product.id");
		hql.append(" where lang.fundHouse is not null and lang.fundHouse!=''");
		hql.append(" and t.isValid='1'");
		hql.append(" and p.type='fund'");
		hql.append(" and pd.distributor.id=?");
		
		params.add(disId);
		
		List<String> fundHouseName = this.baseDao.find(hql.toString(), params.toArray(), false);
		if(null != fundHouseName){
			return fundHouseName;
		}
		return null;
	}

	
}
