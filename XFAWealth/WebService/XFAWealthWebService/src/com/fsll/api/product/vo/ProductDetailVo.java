/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.api.product.vo;

import java.util.ArrayList;
import java.util.List;


/**
 * P(页面)使用的产品详情虚拟对象
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2017-6-20
 */
public class ProductDetailVo {
	//共用字段
	private String searchListType = "";//页面来源,如/api/product/list.r
	private String searchListCond = "";//如果从列表点击进来的,记录的是列表的搜索条件,json格式:{"code":"F2013213",........} 
	//这里的分页固定每页为一条记录
	private Integer searchPage = 0;//当前页page,此处的当前页是来源列表中第几条记录 等于 page*rows + index + 1
	private String searchSort = "";//排序字段
	private String searchOrder = "";//asc或者dec
	
	private String searchPreId = "";//上一个标识
	private String searchPreName = "";//上一个名称
	private String searchNextId = "";//下一个标识
	private String searchNextName = "";//下一个名称
	
	//共用属性
	private ProductBasicDataVO vo = new ProductBasicDataVO();//产品基本信息数据
	
	//特殊字段：1.以下对基金有效
	private List<FundMarketDataVO> navList = new ArrayList<FundMarketDataVO>();//基金行情数据
	private List<FundFeesItemVO> feesList = new ArrayList<FundFeesItemVO>();//基金费用数据
	private List<FundDocDataVO> docList = new ArrayList<FundDocDataVO>();//基金文档数据
	private List<FundReturnDataVO> returnList = new ArrayList<FundReturnDataVO>();//基金回报数据
	private List<FundDividendDataVO> dividendList = new ArrayList<FundDividendDataVO>();//基金分红数据
	private List<FundCumulativePerformanceDataVO> ljList = new ArrayList<FundCumulativePerformanceDataVO>();//基金累积表现
	private List<FundYearPerformanceDataVO> ndList = new ArrayList<FundYearPerformanceDataVO>();//基金年度表现
	
	//特殊字段：2.以下对股票有效
	//...
	
	//特殊字段：3.以下对债券有效
	//...
	
	//特殊字段：4.以下对保险有效
	//...
	
	public ProductBasicDataVO getVo() {
		return vo;
	}
	public void setVo(ProductBasicDataVO vo) {
		this.vo = vo;
	}
	public List<FundMarketDataVO> getNavList() {
		return navList;
	}
	public void setNavList(List<FundMarketDataVO> navList) {
		this.navList = navList;
	}
	public List<FundFeesItemVO> getFeesList() {
		return feesList;
	}
	public void setFeesList(List<FundFeesItemVO> feesList) {
		this.feesList = feesList;
	}
	public List<FundDocDataVO> getDocList() {
		return docList;
	}
	public void setDocList(List<FundDocDataVO> docList) {
		this.docList = docList;
	}
	public List<FundReturnDataVO> getReturnList() {
		return returnList;
	}
	public void setReturnList(List<FundReturnDataVO> returnList) {
		this.returnList = returnList;
	}
	public List<FundDividendDataVO> getDividendList() {
		return dividendList;
	}
	public void setDividendList(List<FundDividendDataVO> dividendList) {
		this.dividendList = dividendList;
	}
	public List<FundCumulativePerformanceDataVO> getLjList() {
		return ljList;
	}
	public void setLjList(List<FundCumulativePerformanceDataVO> ljList) {
		this.ljList = ljList;
	}
	public List<FundYearPerformanceDataVO> getNdList() {
		return ndList;
	}
	public void setNdList(List<FundYearPerformanceDataVO> ndList) {
		this.ndList = ndList;
	}
	public String getSearchListType() {
		return searchListType;
	}
	public void setSearchListType(String searchListType) {
		this.searchListType = searchListType;
	}
	public String getSearchListCond() {
		return searchListCond;
	}
	public void setSearchListCond(String searchListCond) {
		this.searchListCond = searchListCond;
	}
	public Integer getSearchPage() {
		return searchPage;
	}
	public void setSearchPage(Integer searchPage) {
		this.searchPage = searchPage;
	}
	public String getSearchSort() {
		return searchSort;
	}
	public void setSearchSort(String searchSort) {
		this.searchSort = searchSort;
	}
	public String getSearchOrder() {
		return searchOrder;
	}
	public void setSearchOrder(String searchOrder) {
		this.searchOrder = searchOrder;
	}
	public String getSearchPreId() {
		return searchPreId;
	}
	public void setSearchPreId(String searchPreId) {
		this.searchPreId = searchPreId;
	}
	public String getSearchPreName() {
		return searchPreName;
	}
	public void setSearchPreName(String searchPreName) {
		this.searchPreName = searchPreName;
	}
	public String getSearchNextId() {
		return searchNextId;
	}
	public void setSearchNextId(String searchNextId) {
		this.searchNextId = searchNextId;
	}
	public String getSearchNextName() {
		return searchNextName;
	}
	public void setSearchNextName(String searchNextName) {
		this.searchNextName = searchNextName;
	}
}
