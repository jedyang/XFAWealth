/**
 * 
 */
package com.fsll.wmes.fund.vo;

import java.util.Date;
import java.util.List;

import com.fsll.wmes.entity.FundDocDetail;

/**
 * 通用显示对象（集合一些常用的页面显示字段）
 * @author michael
 * @version 1.0.0 Created On: 2016-6-20
 *	
 */
public class GeneralDataVO {
	//适于sysparameter列表
	private String id;
	private String name;
	private String value;
	private String key;
	
	
	private String fundId;//基金ID
	private String itemCode;//编码
	private String item;//名称
	private String descrp;//说明
	private int level;//价钱
	private Double price;//价钱
	private String unit;//单位
	private Double percent;//价钱
	private String fileName;//文件名
	private String url;//链接地址
	private Date updateTime;//更新时间
	
	private String currency;//源货币
	private Double openPrice;//开市价钱
	private Double lowPrice;//最低价钱
	private Double hightPrice;//最高价钱
	private Double closePrice;//收市价钱
	private Double nav;//基金净值
	private Double accNav;//累计净值
	private Double dayReturn;//日回报
	
	//适应一个文档记录信息对应N个多语言文档
	private List<FundDocDetail> docDetailList;
	
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getItemCode() {
		return itemCode;
	}
	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getDescrp() {
		return descrp;
	}
	public void setDescrp(String descrp) {
		this.descrp = descrp;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Double getPercent() {
		return percent;
	}
	public void setPercent(Double percent) {
		this.percent = percent;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public Double getOpenPrice() {
		return openPrice;
	}
	public void setOpenPrice(Double openPrice) {
		this.openPrice = openPrice;
	}
	public Double getLowPrice() {
		return lowPrice;
	}
	public void setLowPrice(Double lowPrice) {
		this.lowPrice = lowPrice;
	}
	public Double getHightPrice() {
		return hightPrice;
	}
	public void setHightPrice(Double hightPrice) {
		this.hightPrice = hightPrice;
	}
	public Double getClosePrice() {
		return closePrice;
	}
	public void setClosePrice(Double closePrice) {
		this.closePrice = closePrice;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Double getNav() {
		return nav;
	}
	public void setNav(Double nav) {
		this.nav = nav;
	}
	public Double getAccNav() {
		return accNav;
	}
	public void setAccNav(Double accNav) {
		this.accNav = accNav;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Double getDayReturn() {
		return dayReturn;
	}
	public void setDayReturn(Double dayReturn) {
		this.dayReturn = dayReturn;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}
	public List<FundDocDetail> getDocDetailList() {
		return docDetailList;
	}
	public void setDocDetailList(List<FundDocDetail> docDetailList) {
		this.docDetailList = docDetailList;
	}
	
}
