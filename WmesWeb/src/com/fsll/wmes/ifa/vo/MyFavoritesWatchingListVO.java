package com.fsll.wmes.ifa.vo;

import java.util.List;

import com.fsll.wmes.fund.vo.FundInfoDataVO;

/**
 * 显示我自选的自选信息，包含基金数据列表
 * @author 林文伟
 * @date 2016-9-28
 */
public class MyFavoritesWatchingListVO {
	private String watchingId;
	private String remark;
	FundInfoDataVO fundInfo; //一条自选对应一条基金信息
	public String getWatchingId() {
		return watchingId;
	}
	public void setWatchingId(String watchingId) {
		this.watchingId = watchingId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public FundInfoDataVO getFundInfo() {
		return fundInfo;
	}
	public void setFundInfo(FundInfoDataVO fundInfo) {
		this.fundInfo = fundInfo;
	}
	
}
