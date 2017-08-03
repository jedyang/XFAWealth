/**
 * 
 */
package com.fsll.wmes.fund.vo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.FundHouse;
import com.fsll.wmes.entity.FundInfo;
import com.fsll.wmes.entity.FundInfoEn;
import com.fsll.wmes.entity.FundInfoSc;
import com.fsll.wmes.entity.FundInfoTc;
import com.fsll.wmes.entity.FundReturn;
import com.fsll.wmes.entity.FundReturnEn;
import com.fsll.wmes.entity.FundReturnSc;
import com.fsll.wmes.entity.FundReturnTc;
import com.fsll.wmes.entity.MemberBase;

/**
 * 基金筛选条件
 * 
 * @author michael
 * @date 2016-6-20
 */
public class FundScreenerDataVO {
	private String langCode;
	
	// 基金信息
	private String fundID;	//允许多个，id用逗号分隔
	private String distributor;
	private String fundName;
	private String fundHouseIds;//允许多个，id用逗号分隔
	private String fundHouse;//作为查询关键字
	private String fundSizeFrom;//基金规模下限
	private String fundSizeTo;//基金规模上限
	private String fundSizeCurrency;
	private String domicile;
	private String currency;
	private String fundType;
//	private String type;
	private String sectorType;
	private String geoAllocation;
	private String riskRating;
	private MemberBase loginUser;
	private String toCurrency;//目标货币
	
	private String ratingOrg;//评级组织
	private String ratingLevel;//评级值
	
	// 基金回报信息
	//回报率下限
	private String riskReturnThreeYear;// 3年回报风险
	private String perfOneWeekFrom;// 1周
	private String perfOneMonthFrom;// 1个月
	private String perfThreeMonthFrom;// 3个月
	private String perfSixMonthFrom;// 6个月
	private String perfOneYearFrom;// 1年
	private String perfThreeYearFrom;// 3年
	private String perfFiveYearFrom;// 5年
	private String perfYTDFrom;// YTD
	private String perfLaunchFrom;// LANUCH
	//回报率上限
	private String perfOneWeekTo;// 1周
	private String perfOneMonthTo;// 1个月
	private String perfThreeMonthTo;// 3个月
	private String perfSixMonthTo;// 6个月
	private String perfOneYearTo;// 1年
	private String perfThreeYearTo;// 3年
	private String perfFiveYearTo;// 5年
	private String perfYTDTo;// YTD
	private String perfLaunchTo;// LANUCH
	private String beforeYears;// 获取的年度回报数量
	//评级
	private String lipperCrId;
	private String fitchId;
	private String citywireId;
	private String lipperCr;
	private String fitch;
	private String citywire;

	private String minInitialInv;
	private String mgtFee;
	private String keyword;
	
	private String minInitialInvFrom;
	private String minInitialInvTo;
	private String mgtFeeFrom;
	private String mgtFeeTo;
	private String inceptionDateFrom;
	private String inceptionDateTo;
	
	private String ifaFirmId;
	private String distributorId;
	
	public String getFundID() {
		return fundID;
	}
	public void setFundID(String fundID) {
		this.fundID = fundID;
	}
	private String strategyId;//策略id
	private String portfolioId;//组合id
	private String portfolioArenaId;//组合id
	
	public String getDistributor() {
		return distributor;
	}
	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}
	public String getFundHouse() {
		return fundHouse;
	}
	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}
	public String getFundSizeCurrency() {
		return fundSizeCurrency;
	}
	public void setFundSizeCurrency(String fundSizeCurrency) {
		this.fundSizeCurrency = fundSizeCurrency;
	}
	public String getDomicile() {
		return domicile;
	}
	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getGeoAllocation() {
		return geoAllocation;
	}
	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}
	public String getRiskRating() {
		return riskRating;
	}
	public void setRiskRating(String riskRating) {
		this.riskRating = riskRating;
	}
	public String getRiskReturnThreeYear() {
		return riskReturnThreeYear;
	}
	public void setRiskReturnThreeYear(String riskReturnThreeYear) {
		this.riskReturnThreeYear = riskReturnThreeYear;
	}
	
	public String getFitch() {
		return fitch;
	}
	public void setFitch(String fitch) {
		this.fitch = fitch;
	}
	public String getCitywire() {
		return citywire;
	}
	public void setCitywire(String citywire) {
		this.citywire = citywire;
	}
	public String getMinInitialInv() {
		return minInitialInv;
	}
	public void setMinInitialInv(String minInitialInv) {
		this.minInitialInv = minInitialInv;
	}
	public String getMgtFee() {
		return mgtFee;
	}
	public void setMgtFee(String mgtFee) {
		this.mgtFee = mgtFee;
	}
	public String getKeyword() {
		if (this.keyword == null) return "";
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public String getPerfOneWeekFrom() {
		return perfOneWeekFrom;
	}
	public void setPerfOneWeekFrom(String perfOneWeekFrom) {
		this.perfOneWeekFrom = perfOneWeekFrom;
	}
	public String getPerfOneMonthFrom() {
		return perfOneMonthFrom;
	}
	public void setPerfOneMonthFrom(String perfOneMonthFrom) {
		this.perfOneMonthFrom = perfOneMonthFrom;
	}
	public String getPerfThreeMonthFrom() {
		return perfThreeMonthFrom;
	}
	public void setPerfThreeMonthFrom(String perfThreeMonthFrom) {
		this.perfThreeMonthFrom = perfThreeMonthFrom;
	}
	public String getPerfSixMonthFrom() {
		return perfSixMonthFrom;
	}
	public void setPerfSixMonthFrom(String perfSixMonthFrom) {
		this.perfSixMonthFrom = perfSixMonthFrom;
	}
	public String getPerfOneYearFrom() {
		return perfOneYearFrom;
	}
	public void setPerfOneYearFrom(String perfOneYearFrom) {
		this.perfOneYearFrom = perfOneYearFrom;
	}
	public String getPerfThreeYearFrom() {
		return perfThreeYearFrom;
	}
	public void setPerfThreeYearFrom(String perfThreeYearFrom) {
		this.perfThreeYearFrom = perfThreeYearFrom;
	}
	public String getPerfYTDFrom() {
		return perfYTDFrom;
	}
	public void setPerfYTDFrom(String perfYTDFrom) {
		this.perfYTDFrom = perfYTDFrom;
	}
	public String getPerfLaunchFrom() {
		return perfLaunchFrom;
	}
	public void setPerfLaunchFrom(String perfLaunchFrom) {
		this.perfLaunchFrom = perfLaunchFrom;
	}
	public String getPerfOneWeekTo() {
		return perfOneWeekTo;
	}
	public void setPerfOneWeekTo(String perfOneWeekTo) {
		this.perfOneWeekTo = perfOneWeekTo;
	}
	public String getPerfOneMonthTo() {
		return perfOneMonthTo;
	}
	public void setPerfOneMonthTo(String perfOneMonthTo) {
		this.perfOneMonthTo = perfOneMonthTo;
	}
	public String getPerfThreeMonthTo() {
		return perfThreeMonthTo;
	}
	public void setPerfThreeMonthTo(String perfThreeMonthTo) {
		this.perfThreeMonthTo = perfThreeMonthTo;
	}
	public String getPerfSixMonthTo() {
		return perfSixMonthTo;
	}
	public void setPerfSixMonthTo(String perfSixMonthTo) {
		this.perfSixMonthTo = perfSixMonthTo;
	}
	public String getPerfOneYearTo() {
		return perfOneYearTo;
	}
	public void setPerfOneYearTo(String perfOneYearTo) {
		this.perfOneYearTo = perfOneYearTo;
	}
	public String getPerfThreeYearTo() {
		return perfThreeYearTo;
	}
	public void setPerfThreeYearTo(String perfThreeYearTo) {
		this.perfThreeYearTo = perfThreeYearTo;
	}
	public String getPerfYTDTo() {
		return perfYTDTo;
	}
	public void setPerfYTDTo(String perfYTDTo) {
		this.perfYTDTo = perfYTDTo;
	}
	public String getPerfLaunchTo() {
		return perfLaunchTo;
	}
	public void setPerfLaunchTo(String perfLaunchTo) {
		this.perfLaunchTo = perfLaunchTo;
	}

	public String getFundSizeFrom() {
		return fundSizeFrom;
	}
	public void setFundSizeFrom(String fundSizeFrom) {
		this.fundSizeFrom = fundSizeFrom;
	}
	public String getFundSizeTo() {
		return fundSizeTo;
	}
	public void setFundSizeTo(String fundSizeTo) {
		this.fundSizeTo = fundSizeTo;
	}
	public String getFundHouseIds() {
		return fundHouseIds;
	}
	public void setFundHouseIds(String fundHouseIds) {
		this.fundHouseIds = fundHouseIds;
	}
	public MemberBase getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(MemberBase loginUser) {
		this.loginUser = loginUser;
	}
	public String getToCurrency() {
		return toCurrency;
	}
	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}
	public String getRatingOrg() {
		return ratingOrg;
	}
	public void setRatingOrg(String ratingOrg) {
		this.ratingOrg = ratingOrg;
	}
	public String getRatingLevel() {
		return ratingLevel;
	}
	public void setRatingLevel(String ratingLevel) {
		this.ratingLevel = ratingLevel;
	}
	public String getBeforeYears() {
		return beforeYears;
	}
	public void setBeforeYears(String beforeYears) {
		this.beforeYears = beforeYears;
	}
	public String getPerfFiveYearFrom() {
		return perfFiveYearFrom;
	}
	public void setPerfFiveYearFrom(String perfFiveYearFrom) {
		this.perfFiveYearFrom = perfFiveYearFrom;
	}
	public String getPerfFiveYearTo() {
		return perfFiveYearTo;
	}
	public void setPerfFiveYearTo(String perfFiveYearTo) {
		this.perfFiveYearTo = perfFiveYearTo;
	}
	public String getStrategyId() {
		return strategyId;
	}
	public void setStrategyId(String strategyId) {
		this.strategyId = strategyId;
	}
	public String getPortfolioId() {
		return portfolioId;
	}
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}
	public String getPortfolioArenaId() {
		return portfolioArenaId;
	}
	public void setPortfolioArenaId(String portfolioArenaId) {
		this.portfolioArenaId = portfolioArenaId;
	}
	
	public String getMinInitialInvFrom() {
		return minInitialInvFrom;
	}
	public void setMinInitialInvFrom(String minInitialInvFrom) {
		this.minInitialInvFrom = minInitialInvFrom;
	}
	public String getMinInitialInvTo() {
		return minInitialInvTo;
	}
	public void setMinInitialInvTo(String minInitialInvTo) {
		this.minInitialInvTo = minInitialInvTo;
	}
	
	public String getMgtFeeFrom() {
		return mgtFeeFrom;
	}
	public void setMgtFeeFrom(String mgtFeeFrom) {
		this.mgtFeeFrom = mgtFeeFrom;
	}
	public String getMgtFeeTo() {
		return mgtFeeTo;
	}
	public void setMgtFeeTo(String mgtFeeTo) {
		this.mgtFeeTo = mgtFeeTo;
	}
	
	public String getInceptionDateFrom() {
		return inceptionDateFrom;
	}
	public void setInceptionDateFrom(String inceptionDateFrom) {
		this.inceptionDateFrom = inceptionDateFrom;
	}
	public String getInceptionDateTo() {
		return inceptionDateTo;
	}
	public void setInceptionDateTo(String inceptionDateTo) {
		this.inceptionDateTo = inceptionDateTo;
	}
	public String getLipperCrId() {
		return lipperCrId;
	}
	public void setLipperCrId(String lipperCrId) {
		this.lipperCrId = lipperCrId;
	}
	public String getFitchId() {
		return fitchId;
	}
	public void setFitchId(String fitchId) {
		this.fitchId = fitchId;
	}
	public String getCitywireId() {
		return citywireId;
	}
	public void setCitywireId(String citywireId) {
		this.citywireId = citywireId;
	}
	public String getLipperCr() {
		return lipperCr;
	}
	public void setLipperCr(String lipperCr) {
		this.lipperCr = lipperCr;
	}
	public String getFundType() {
		return fundType;
	}
	public void setFundType(String fundType) {
		this.fundType = fundType;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = (null==langCode?"":langCode);
	}
	public String getSectorType() {
		return sectorType;
	}
	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
	}
	public String getIfaFirmId() {
    	return ifaFirmId;
    }
	public void setIfaFirmId(String ifaFirmId) {
    	this.ifaFirmId = ifaFirmId;
    }
	public String getDistributorId() {
    	return distributorId;
    }
	public void setDistributorId(String distributorId) {
    	this.distributorId = distributorId;
    }
}
