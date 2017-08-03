package com.fsll.wmes.fund.vo;

import java.util.Date;
import com.fsll.wmes.entity.FundHouseEn;
import com.fsll.wmes.entity.FundHouseSc;
import com.fsll.wmes.entity.FundHouseTc;

public class FundHouseDataVO {
	
    private String id;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	
	private FundHouseEn fundHouseEn;
	private FundHouseSc fundHouseSc;
	private FundHouseTc fundHouseTc;
	
	private String houseName;
	private String pinYin;
	private String logoUrl;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public FundHouseEn getFundHouseEn() {
		return fundHouseEn;
	}
	public void setFundHouseEn(FundHouseEn fundHouseEn) {
		this.fundHouseEn = fundHouseEn;
	}
	public FundHouseSc getFundHouseSc() {
		return fundHouseSc;
	}
	public void setFundHouseSc(FundHouseSc fundHouseSc) {
		this.fundHouseSc = fundHouseSc;
	}
	public FundHouseTc getFundHouseTc() {
		return fundHouseTc;
	}
	public void setFundHouseTc(FundHouseTc fundHouseTc) {
		this.fundHouseTc = fundHouseTc;
	}
	public String getLogoUrl() {
		return logoUrl;
	}
	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		this.houseName = houseName;
	}
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	
}
