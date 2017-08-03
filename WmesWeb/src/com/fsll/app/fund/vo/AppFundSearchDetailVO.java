package com.fsll.app.fund.vo;

import java.util.List;

/**
 * 基金查询条件初始化通用实体
 * @author zxtan
 * @date 2017-06-01
 */
public class AppFundSearchDetailVO {

	private List<AppFundSearchItemVO> fundTypeList; //基金类型
	private List<AppFundSearchItemVO> fundGeoList;//地域分布
	private List<AppFundSearchItemVO> fundSectorList;//投资主题
	private List<AppFundSearchItemVO> fundHouseList;//基金公司
	private List<AppFundSearchItemVO> fundSizeList;//基金规模
	private List<AppFundSearchItemVO> fundMgtFeeList;//基金管理费
	private List<AppFundSearchItemVO> fundMinInitialInvList;//最低认购额
	private List<AppFundSearchItemVO> distributorList;//代理商
	
	public List<AppFundSearchItemVO> getFundTypeList() {
		return fundTypeList;
	}
	public void setFundTypeList(List<AppFundSearchItemVO> fundTypeList) {
		this.fundTypeList = fundTypeList;
	}
	public List<AppFundSearchItemVO> getFundGeoList() {
		return fundGeoList;
	}
	public void setFundGeoList(List<AppFundSearchItemVO> fundGeoList) {
		this.fundGeoList = fundGeoList;
	}
	public List<AppFundSearchItemVO> getFundSectorList() {
		return fundSectorList;
	}
	public void setFundSectorList(List<AppFundSearchItemVO> fundSectorList) {
		this.fundSectorList = fundSectorList;
	}
	public List<AppFundSearchItemVO> getFundHouseList() {
		return fundHouseList;
	}
	public void setFundHouseList(List<AppFundSearchItemVO> fundHouseList) {
		this.fundHouseList = fundHouseList;
	}
	public List<AppFundSearchItemVO> getFundSizeList() {
		return fundSizeList;
	}
	public void setFundSizeList(List<AppFundSearchItemVO> fundSizeList) {
		this.fundSizeList = fundSizeList;
	}
	public List<AppFundSearchItemVO> getFundMgtFeeList() {
		return fundMgtFeeList;
	}
	public void setFundMgtFeeList(List<AppFundSearchItemVO> fundMgtFeeList) {
		this.fundMgtFeeList = fundMgtFeeList;
	}
	public List<AppFundSearchItemVO> getFundMinInitialInvList() {
		return fundMinInitialInvList;
	}
	public void setFundMinInitialInvList(
			List<AppFundSearchItemVO> fundMinInitialInvList) {
		this.fundMinInitialInvList = fundMinInitialInvList;
	}
	public List<AppFundSearchItemVO> getDistributorList() {
		return distributorList;
	}
	public void setDistributorList(List<AppFundSearchItemVO> distributorList) {
		this.distributorList = distributorList;
	}	
}
