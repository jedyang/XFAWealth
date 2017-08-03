/**
 * 
 */
package com.fsll.wmes.fund.vo;

import java.util.ArrayList;
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
import com.fsll.wmes.entity.OrderPlanProduct;
import com.fsll.wmes.entity.PortfolioHoldProduct;
import com.fsll.wmes.entity.ProductInfo;
import com.fsll.wmes.ifa.vo.RecommendInfoVO;
import com.fsll.wmes.investor.vo.InvestorAccountVO;

/**
 * 基金数据
 * 
 * @author michael
 * @date 2016-6-20
 */
public class FundInfoDataVO {
	// 基金基础信息
	private String fundId;
	private String productId;
	private FundInfo fundInfo;
	private FundInfoEn fundInfoEn;
	private FundInfoSc fundInfoSc;
	private FundInfoTc fundInfoTc;
	private String followStatus;//当前用户的关注状态
	private RecommendInfoVO recommendInfo;
	
	//金额相关信息，转换货币后的数值
	private String toCurrency;//转换货币
	private Double lastNav;
	private Double dayReturn;
	private Double issuePrice;
	private Double subscribeFee;
	private Double mgtFee;//%
	private Double minInitialAmount;
	private Double minSubscribeAmount;
	private Double minHoldingAmount;
	private Double minRedemptionAmount;
	private Double minRspAmount;
	
	private String toCurrencyName; //
	private Double rateOfReturn; //对比前一次净值，回报率
	
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}

	public String getFundId() {
		return fundId;
	}
	
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductId() {
		return productId;
	}
	
	public FundInfo getFundInfo() {
		return fundInfo;
	}

	public void setFundInfo(FundInfo fundInfo) {
		this.fundInfo = fundInfo;
	}

	public FundInfoEn getFundInfoEn() {
		return fundInfoEn;
	}

	public void setFundInfoEn(FundInfoEn fundInfoEn) {
		this.fundInfoEn = fundInfoEn;
	}

	public FundInfoSc getFundInfoSc() {
		return fundInfoSc;
	}

	public void setFundInfoSc(FundInfoSc fundInfoSc) {
		this.fundInfoSc = fundInfoSc;
	}

	public FundInfoTc getFundInfoTc() {
		return fundInfoTc;
	}

	public void setFundInfoTc(FundInfoTc fundInfoTc) {
		this.fundInfoTc = fundInfoTc;
	}
	

	public RecommendInfoVO getRecommendInfo() {
		return recommendInfo;
	}

	public void setRecommendInfo(RecommendInfoVO recommendInfo) {
		this.recommendInfo = recommendInfo;
	}


	// 基金附件信息
	private String fundName;
	private String fundNamePinyin;
	private String domicileCode;
	private String domicile;
	private String fundHouseId;
	private String fundHouse;
	private String fundManCo;
	private String fundManager;
	private String fundSize;
	private String fundCurrencyCode;
	private String fundCurrency;
	private String issueCurrencyCode;
	private String issueCurrency;
	private String fundTypeCode;
	private String fundType;
	private String sectorTypeCode;
	private String sectorType;
	private String geoAllocationCode;
	private String geoAllocation;
	private String invTarget;
	private String keyRisks;

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getDomicileCode() {
		return domicileCode;
	}

	public void setDomicileCode(String domicileCode) {
		this.domicileCode = domicileCode;
	}

	public String getDomicile() {
		return domicile;
	}

	public void setDomicile(String domicile) {
		this.domicile = domicile;
	}

	public String getFundHouseId() {
		return fundHouseId;
	}

	public void setFundHouseId(String fundHouseId) {
		this.fundHouseId = fundHouseId;
	}

	public String getFundHouse() {
		return fundHouse;
	}

	public void setFundHouse(String fundHouse) {
		this.fundHouse = fundHouse;
	}

	public String getFundManCo() {
		return fundManCo;
	}

	public void setFundManCo(String fundManCo) {
		this.fundManCo = fundManCo;
	}

	public String getFundManager() {
		return fundManager;
	}

	public void setFundManager(String fundManager) {
		this.fundManager = fundManager;
	}

	public String getFundSize() {
		return fundSize;
	}

	public void setFundSize(String fundSize) {
		this.fundSize = fundSize;
	}

	public String getFundCurrencyCode() {
		return fundCurrencyCode;
	}

	public void setFundCurrencyCode(String fundCurrencyCode) {
		this.fundCurrencyCode = fundCurrencyCode;
	}

	public String getFundCurrency() {
		return fundCurrency;
	}

	public void setFundCurrency(String fundCurrency) {
		this.fundCurrency = fundCurrency;
	}

	public String getIssueCurrencyCode() {
		return issueCurrencyCode;
	}

	public void setIssueCurrencyCode(String issueCurrencyCode) {
		this.issueCurrencyCode = issueCurrencyCode;
	}

	public String getIssueCurrency() {
		return issueCurrency;
	}

	public void setIssueCurrency(String issueCurrency) {
		this.issueCurrency = issueCurrency;
	}

	public String getFundTypeCode() {
		return fundTypeCode;
	}

	public void setFundTypeCode(String fundTypeCode) {
		this.fundTypeCode = fundTypeCode;
	}

	public String getFundType() {
		return fundType;
	}

	public void setFundType(String fundType) {
		this.fundType = fundType;
	}

	public String getSectorTypeCode() {
		return sectorTypeCode;
	}

	public void setSectorTypeCode(String sectorTypeCode) {
		this.sectorTypeCode = sectorTypeCode;
	}

	public String getSectorType() {
		return sectorType;
	}

	public void setSectorType(String sectorType) {
		this.sectorType = sectorType;
	}

	public String getGeoAllocationCode() {
		return geoAllocationCode;
	}

	public void setGeoAllocationCode(String geoAllocationCode) {
		this.geoAllocationCode = geoAllocationCode;
	}

	public String getGeoAllocation() {
		return geoAllocation;
	}

	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	public String getInvTarget() {
		return invTarget;
	}

	public void setInvTarget(String invTarget) {
		this.invTarget = invTarget;
	}

	public String getKeyRisks() {
		return keyRisks;
	}

	public void setKeyRisks(String keyRisks) {
		this.keyRisks = keyRisks;
	}

	// 基金回报信息
	private double fundReturnOneWeek;// 1周
	private double fundReturnOneMonth;// 1个月
	private double fundReturnThreeMonth;// 3个月
	private double fundReturnSixMonth;// 6个月
	private double fundReturnOneYear;// 1年
	private double fundReturnThreeYear;// 3年
	private double fundReturnFiveYear;// 5年
	private double fundReturnYTD;// YTD
	private double fundReturnLaunch;// LANUCH
	private String fundReturnYTDSmallImg;// YTD小图
	private String fundReturnYTDMiddleImg;// YTD中图

	public double getFundReturnOneMonth() {
		return fundReturnOneMonth;
	}

	public void setFundReturnOneMonth(double fundReturnOneMonth) {
		this.fundReturnOneMonth = fundReturnOneMonth;
	}

	public double getFundReturnThreeMonth() {
		return fundReturnThreeMonth;
	}

	public void setFundReturnThreeMonth(double fundReturnThreeMonth) {
		this.fundReturnThreeMonth = fundReturnThreeMonth;
	}

	public double getFundReturnOneYear() {
		return fundReturnOneYear;
	}

	public void setFundReturnOneYear(double fundReturnOneYear) {
		this.fundReturnOneYear = fundReturnOneYear;
	}

	public double getFundReturnOneWeek() {
		return fundReturnOneWeek;
	}

	public void setFundReturnOneWeek(double fundReturnOneWeek) {
		this.fundReturnOneWeek = fundReturnOneWeek;
	}

	public double getFundReturnSixMonth() {
		return fundReturnSixMonth;
	}

	public void setFundReturnSixMonth(double fundReturnSixMonth) {
		this.fundReturnSixMonth = fundReturnSixMonth;
	}

	public double getFundReturnThreeYear() {
		return fundReturnThreeYear;
	}

	public void setFundReturnThreeYear(double fundReturnThreeYear) {
		this.fundReturnThreeYear = fundReturnThreeYear;
	}

	public double getFundReturnYTD() {
		return fundReturnYTD;
	}

	public void setFundReturnYTD(double fundReturnYTD) {
		this.fundReturnYTD = fundReturnYTD;
	}

	public double getFundReturnLaunch() {
		return fundReturnLaunch;
	}

	public void setFundReturnLaunch(double fundReturnLaunch) {
		this.fundReturnLaunch = fundReturnLaunch;
	}

	public double getFundReturnFiveYear() {
		return fundReturnFiveYear;
	}

	public void setFundReturnFiveYear(double fundReturnFiveYear) {
		this.fundReturnFiveYear = fundReturnFiveYear;
	}
	
	public String getFundReturnYTDSmallImg() {
		return fundReturnYTDSmallImg;
	}

	public void setFundReturnYTDSmallImg(String fundReturnYTDSmallImg) {
		this.fundReturnYTDSmallImg = fundReturnYTDSmallImg;
	}

	public String getFundReturnYTDMiddleImg() {
		return fundReturnYTDMiddleImg;
	}

	public void setFundReturnYTDMiddleImg(String fundReturnYTDMiddleImg) {
		this.fundReturnYTDMiddleImg = fundReturnYTDMiddleImg;
	}
	
	//基金的其他信息列表
    private List<GeneralDataVO> fundFeesList;
    private List<GeneralDataVO> fundDocsList;
    private List<GeneralDataVO> fundBonusList;
    private List<GeneralDataVO> fundAnnoList;
    private List<GeneralDataVO> fundCumReturnList;
    private List<GeneralDataVO> fundYearReturnList;
    private List<GeneralDataVO> fundPriceList;
    private List<FundCompositionDataVO> fundPortfolioListByRegion;
    private List<FundCompositionDataVO> fundPortfolioListByIndustry;
    
	public List<GeneralDataVO> getFundFeesList() {
		return fundFeesList;
	}

	public void setFundFeesList(List<GeneralDataVO> fundFeesList) {
		this.fundFeesList = fundFeesList;
	}

	public List<GeneralDataVO> getFundDocsList() {
		return fundDocsList;
	}

	public void setFundDocsList(List<GeneralDataVO> fundDocsList) {
		this.fundDocsList = fundDocsList;
	}

	public List<GeneralDataVO> getFundBonusList() {
		return fundBonusList;
	}

	public void setFundBonusList(List<GeneralDataVO> fundBonusList) {
		this.fundBonusList = fundBonusList;
	}

	public List<GeneralDataVO> getFundAnnoList() {
		return fundAnnoList;
	}

	public void setFundAnnoList(List<GeneralDataVO> fundAnnoList) {
		this.fundAnnoList = fundAnnoList;
	}

	public List<GeneralDataVO> getFundCumReturnList() {
		return fundCumReturnList;
	}

	public void setFundCumReturnList(List<GeneralDataVO> fundCumReturnList) {
		this.fundCumReturnList = fundCumReturnList;
	}

	public List<GeneralDataVO> getFundYearReturnList() {
		return fundYearReturnList;
	}

	public void setFundYearReturnList(List<GeneralDataVO> fundYearReturnList) {
		this.fundYearReturnList = fundYearReturnList;
	}

	public List<GeneralDataVO> getFundPriceList() {
		return fundPriceList;
	}

	public void setFundPriceList(List<GeneralDataVO> fundPriceList) {
		this.fundPriceList = fundPriceList;
	}

	public List<FundCompositionDataVO> getFundPortfolioListByRegion() {
		return fundPortfolioListByRegion;
	}

	public void setFundPortfolioListByRegion(
			List<FundCompositionDataVO> fundPortfolioListByRegion) {
		this.fundPortfolioListByRegion = fundPortfolioListByRegion;
	}

	public List<FundCompositionDataVO> getFundPortfolioListByIndustry() {
		return fundPortfolioListByIndustry;
	}

	public void setFundPortfolioListByIndustry(
			List<FundCompositionDataVO> fundPortfolioListByIndustry) {
		this.fundPortfolioListByIndustry = fundPortfolioListByIndustry;
	}

	//年回报
//	private double fundReturn2010;
//	private double fundReturn2011;
//	private double fundReturn2012;
//	private double fundReturn2013;
//	private double fundReturn2014;
//	private double fundReturn2015;
//	private double fundReturn2016;

	private double fundReturnYear1;
	private double fundReturnYear2;
	private double fundReturnYear3;
	private double fundReturnYear4;
	private double fundReturnYear5;

	
//	public double getFundReturn2012() {
//		return fundReturn2012;
//	}
//
//	public void setFundReturn2012(double fundReturn2012) {
//		this.fundReturn2012 = fundReturn2012;
//	}
//
//	public double getFundReturn2013() {
//		return fundReturn2013;
//	}
//
//	public void setFundReturn2013(double fundReturn2013) {
//		this.fundReturn2013 = fundReturn2013;
//	}
//
//	public double getFundReturn2014() {
//		return fundReturn2014;
//	}
//
//	public void setFundReturn2014(double fundReturn2014) {
//		this.fundReturn2014 = fundReturn2014;
//	}
//
//	public double getFundReturn2015() {
//		return fundReturn2015;
//	}
//
//	public void setFundReturn2015(double fundReturn2015) {
//		this.fundReturn2015 = fundReturn2015;
//	}
//
//	public double getFundReturn2016() {
//		return fundReturn2016;
//	}
//
//	public void setFundReturn2016(double fundReturn2016) {
//		this.fundReturn2016 = fundReturn2016;
//	}
//
//	public double getFundReturn2010() {
//		return fundReturn2010;
//	}
//
//	public void setFundReturn2010(double fundReturn2010) {
//		this.fundReturn2010 = fundReturn2010;
//	}
//
//	public double getFundReturn2011() {
//		return fundReturn2011;
//	}
//
//	public void setFundReturn2011(double fundReturn2011) {
//		this.fundReturn2011 = fundReturn2011;
//	}

	public double getFundReturnYear1() {
		return fundReturnYear1;
	}

	public void setFundReturnYear1(double fundReturnYear1) {
		this.fundReturnYear1 = fundReturnYear1;
	}

	public double getFundReturnYear2() {
		return fundReturnYear2;
	}

	public void setFundReturnYear2(double fundReturnYear2) {
		this.fundReturnYear2 = fundReturnYear2;
	}

	public double getFundReturnYear3() {
		return fundReturnYear3;
	}

	public void setFundReturnYear3(double fundReturnYear3) {
		this.fundReturnYear3 = fundReturnYear3;
	}

	public double getFundReturnYear4() {
		return fundReturnYear4;
	}

	public void setFundReturnYear4(double fundReturnYear4) {
		this.fundReturnYear4 = fundReturnYear4;
	}

	public double getFundReturnYear5() {
		return fundReturnYear5;
	}

	public void setFundReturnYear5(double fundReturnYear5) {
		this.fundReturnYear5 = fundReturnYear5;
	}
	
	//基金评级信息
	private int lipperCR;
	private int fitch;
	private int citywire;

	public int getLipperCR() {
		return lipperCR;
	}

	public void setLipperCR(int lipperCR) {
		this.lipperCR = lipperCR;
	}

	public int getFitch() {
		return fitch;
	}

	public void setFitch(int fitch) {
		this.fitch = fitch;
	}

	public int getCitywire() {
		return citywire;
	}

	public void setCitywire(int citywire) {
		this.citywire = citywire;
	}


	public String getFollowStatus() {
		return followStatus;
	}

	public void setFollowStatus(String followStatus) {
		this.followStatus = followStatus;
	}

	/**
	 * 按语言设置基金信息
	 * @param langCode
	 */
	public void setDefaultInfoByLang(String langCode) {
		if ("en".equals(langCode)) {// 英文
			if (this.fundInfoEn != null) {
				fundName = this.fundInfoEn.getFundName();
				domicileCode = this.fundInfoEn.getDomicileCode();
				domicile = this.fundInfoEn.getDomicile();
				fundHouse = this.fundInfoEn.getFundHouse();
				fundManCo = this.fundInfoEn.getFundManCo();
				fundManager = this.fundInfoEn.getFundManager();
				fundSize = this.fundInfoEn.getFundSize();
				fundCurrencyCode = this.fundInfoEn.getFundCurrencyCode();
				fundCurrency = this.fundInfoEn.getFundCurrency();
				issueCurrencyCode = this.fundInfoEn.getIssueCurrencyCode();
				issueCurrency = this.fundInfoEn.getIssueCurrency();
				fundTypeCode = this.fundInfoEn.getFundTypeCode();
				fundType = this.fundInfoEn.getFundType();
				sectorTypeCode = this.fundInfoEn.getSectorTypeCode();
				sectorType = this.fundInfoEn.getSectorType();
				geoAllocationCode = this.fundInfoEn.getGeoAllocationCode();
				geoAllocation = this.fundInfoEn.getGeoAllocation();
				invTarget = this.fundInfoEn.getInvTarget();
				keyRisks = this.fundInfoEn.getKeyRisks();
			}
		} else if ("tc".equals(langCode)) {// 繁体
			if (this.fundInfoTc != null) {
				fundName = this.fundInfoTc.getFundName();
				domicileCode = this.fundInfoTc.getDomicileCode();
				domicile = this.fundInfoTc.getDomicile();
				fundHouse = this.fundInfoTc.getFundHouse();
				fundManCo = this.fundInfoTc.getFundManCo();
				fundManager = this.fundInfoTc.getFundManager();
				fundSize = this.fundInfoTc.getFundSize();
				fundCurrencyCode = this.fundInfoTc.getFundCurrencyCode();
				fundCurrency = this.fundInfoTc.getFundCurrency();
				fundTypeCode = this.fundInfoTc.getFundTypeCode();
				issueCurrencyCode = this.fundInfoTc.getIssueCurrencyCode();
				issueCurrency = this.fundInfoTc.getIssueCurrency();
				fundType = this.fundInfoTc.getFundType();
				sectorTypeCode = this.fundInfoTc.getSectorTypeCode();
				sectorType = this.fundInfoTc.getSectorType();
				geoAllocationCode = this.fundInfoTc.getGeoAllocationCode();
				geoAllocation = this.fundInfoTc.getGeoAllocation();
				invTarget = this.fundInfoTc.getInvTarget();
				keyRisks = this.fundInfoTc.getKeyRisks();
			}
		} else if ("sc".equals(langCode)){// 简体
			if (this.fundInfoSc != null) {
				fundName = this.fundInfoSc.getFundName();
				domicileCode = this.fundInfoSc.getDomicileCode();
				domicile = this.fundInfoSc.getDomicile();
				fundHouse = this.fundInfoSc.getFundHouse();
				fundManCo = this.fundInfoSc.getFundManCo();
				fundManager = this.fundInfoSc.getFundManager();
				fundSize = this.fundInfoSc.getFundSize();
				fundCurrencyCode = this.fundInfoSc.getFundCurrencyCode();
				fundCurrency = this.fundInfoSc.getFundCurrency();
				issueCurrencyCode = this.fundInfoSc.getIssueCurrencyCode();
				issueCurrency = this.fundInfoSc.getIssueCurrency();
				fundTypeCode = this.fundInfoSc.getFundTypeCode();
				fundType = this.fundInfoSc.getFundType();
				sectorTypeCode = this.fundInfoSc.getSectorTypeCode();
				sectorType = this.fundInfoSc.getSectorType();
				geoAllocationCode = this.fundInfoSc.getGeoAllocationCode();
				geoAllocation = this.fundInfoSc.getGeoAllocation();
				invTarget = this.fundInfoSc.getInvTarget();
				keyRisks = this.fundInfoSc.getKeyRisks();
			}
		} else {// 简体
			if (this.fundInfoSc != null) {
				fundName = this.fundInfoSc.getFundName();
				domicileCode = this.fundInfoSc.getDomicileCode();
				domicile = this.fundInfoSc.getDomicile();
				fundHouse = this.fundInfoSc.getFundHouse();
				fundManCo = this.fundInfoSc.getFundManCo();
				fundManager = this.fundInfoSc.getFundManager();
				fundSize = this.fundInfoSc.getFundSize();
				fundCurrencyCode = this.fundInfoSc.getFundCurrencyCode();
				fundCurrency = this.fundInfoSc.getFundCurrency();
				issueCurrencyCode = this.fundInfoSc.getIssueCurrencyCode();
				issueCurrency = this.fundInfoSc.getIssueCurrency();
				fundTypeCode = this.fundInfoSc.getFundTypeCode();
				fundType = this.fundInfoSc.getFundType();
				sectorTypeCode = this.fundInfoSc.getSectorTypeCode();
				sectorType = this.fundInfoSc.getSectorType();
				geoAllocationCode = this.fundInfoSc.getGeoAllocationCode();
				geoAllocation = this.fundInfoSc.getGeoAllocation();
				invTarget = this.fundInfoSc.getInvTarget();
				keyRisks = this.fundInfoSc.getKeyRisks();
			}
		}
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public Double getLastNav() {
		return lastNav;
	}

	public void setLastNav(Double lastNav) {
		this.lastNav = lastNav;
	}

	public Double getDayReturn() {
		return dayReturn;
	}

	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}

	public Double getIssuePrice() {
		return issuePrice;
	}

	public void setIssuePrice(Double issuePrice) {
		this.issuePrice = issuePrice;
	}

	public Double getSubscribeFee() {
		return subscribeFee;
	}

	public void setSubscribeFee(Double subscribeFee) {
		this.subscribeFee = subscribeFee;
	}

	public Double getMgtFee() {
		return mgtFee;
	}

	public void setMgtFee(Double mgtFee) {
		this.mgtFee = mgtFee;
	}

	public Double getMinInitialAmount() {
		return minInitialAmount;
	}

	public void setMinInitialAmount(Double minInitialAmount) {
		this.minInitialAmount = minInitialAmount;
	}

	public Double getMinSubscribeAmount() {
		return minSubscribeAmount;
	}

	public void setMinSubscribeAmount(Double minSubscribeAmount) {
		this.minSubscribeAmount = minSubscribeAmount;
	}

	public Double getMinHoldingAmount() {
		return minHoldingAmount;
	}

	public void setMinHoldingAmount(Double minHoldingAmount) {
		this.minHoldingAmount = minHoldingAmount;
	}

	public Double getMinRedemptionAmount() {
		return minRedemptionAmount;
	}

	public void setMinRedemptionAmount(Double minRedemptionAmount) {
		this.minRedemptionAmount = minRedemptionAmount;
	}

	public Double getMinRspAmount() {
		return minRspAmount;
	}

	public void setMinRspAmount(Double minRspAmount) {
		this.minRspAmount = minRspAmount;
	}
	
	/**
	 * 按兑换率设置金额信息
	 * @param exchangeRate
	 */
	public void setAmounts(double exchangeRate) {
		if (null!=fundInfo){
			if (exchangeRate<=0) exchangeRate=1;
			this.lastNav = getRate(fundInfo.getLastNav(),exchangeRate);
			this.dayReturn = getRate(fundInfo.getDayReturn(),exchangeRate);
			this.issuePrice = getRate(fundInfo.getIssuePrice(),exchangeRate);
			this.subscribeFee = getRate(fundInfo.getSubscribeFee(),exchangeRate);
			this.mgtFee = getRate(fundInfo.getMgtFee(),exchangeRate);
			this.minInitialAmount = getRate(fundInfo.getMinInitialAmount(),exchangeRate);
			this.minSubscribeAmount = getRate(fundInfo.getMinSubscribeAmount(),exchangeRate);
			this.minHoldingAmount = getRate(fundInfo.getMinHoldingAmount(),exchangeRate);
			this.minRedemptionAmount = getRate(fundInfo.getMinRedemptionAmount(),exchangeRate);
			this.minRspAmount = getRate(fundInfo.getMinRspAmount(),exchangeRate);
		}
	}
	
	private Double getRate(Double rate, double exchange){
		if (null!=rate) return rate*exchange;
		return rate;
	}

	public void setFundNamePinyin(String fundNamePinyin) {
		this.fundNamePinyin = fundNamePinyin;
	}

	public String getFundNamePinyin() {
		return fundNamePinyin;
	}

	//对应的产品持仓
	private PortfolioHoldProduct portfolioHoldProduct;

	public PortfolioHoldProduct getPortfolioHoldProduct() {
		return portfolioHoldProduct;
	}

	public void setPortfolioHoldProduct(PortfolioHoldProduct portfolioHoldProduct) {
		this.portfolioHoldProduct = portfolioHoldProduct;
	}

	//候选基金集合
	private List<FundInfoDataVO> spareFunds;

	public List<FundInfoDataVO> getSpareFunds() {
		return spareFunds;
	}

	public void setSpareFunds(List<FundInfoDataVO> spareFunds) {
		this.spareFunds = spareFunds;
	}

	public String getToCurrencyName() {
		return toCurrencyName;
	}

	public void setToCurrencyName(String toCurrencyName) {
		this.toCurrencyName = toCurrencyName;
	}

	public Double getRateOfReturn() {
		return rateOfReturn;
	}

	public void setRateOfReturn(Double rateOfReturn) {
		this.rateOfReturn = rateOfReturn;
	}
	
	
	private OrderPlanProduct orderPlanProduct;
	private List<InvestorAccountVO> investorAccounts = new ArrayList<InvestorAccountVO>();
	private Double productWeight;

	public OrderPlanProduct getOrderPlanProduct() {
		return orderPlanProduct;
	}

	public void setOrderPlanProduct(OrderPlanProduct orderPlanProduct) {
		this.orderPlanProduct = orderPlanProduct;
	}

	public List<InvestorAccountVO> getInvestorAccounts() {
		return investorAccounts;
	}

	public void setInvestorAccounts(List<InvestorAccountVO> investorAccounts) {
		this.investorAccounts = investorAccounts;
	}

	public Double getProductWeight() {
		return productWeight;
	}

	public void setProductWeight(Double productWeight) {
		this.productWeight = productWeight;
	}
	
	//供代理商查看基金列表使用 
	private String symbolCode; //指定代理商产品库中产品的编码
	private Double transactionFeeRate ; //交易费用比率,去掉百分号
	private String transactionFeeCur; //向oms下单的使用的交易货币
	private String transactionFeeCurCurrency; //向oms下单的使用的交易货币名称
	private Double transactionFeeMini; //最低交易费
	private String distributorLogofile; 

	public String getSymbolCode() {
		return symbolCode;
	}

	public void setSymbolCode(String symbolCode) {
		this.symbolCode = symbolCode;
	}

	public Double getTransactionFeeRate() {
		return transactionFeeRate;
	}

	public void setTransactionFeeRate(Double transactionFeeRate) {
		this.transactionFeeRate = transactionFeeRate;
	}

	public String getTransactionFeeCur() {
		return transactionFeeCur;
	}

	public void setTransactionFeeCur(String transactionFeeCur) {
		this.transactionFeeCur = transactionFeeCur;
	}

	public Double getTransactionFeeMini() {
		return transactionFeeMini;
	}

	public void setTransactionFeeMini(Double transactionFeeMini) {
		this.transactionFeeMini = transactionFeeMini;
	}

	public String getTransactionFeeCurCurrency() {
		return transactionFeeCurCurrency;
	}

	public void setTransactionFeeCurCurrency(String transactionFeeCurCurrency) {
		this.transactionFeeCurCurrency = transactionFeeCurCurrency;
	}

	public String getDistributorLogofile() {
		return distributorLogofile;
	}

	public void setDistributorLogofile(String distributorLogofile) {
		this.distributorLogofile = distributorLogofile;
	}
	
	
	
}
