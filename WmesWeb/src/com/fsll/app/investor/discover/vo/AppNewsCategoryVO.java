package com.fsll.app.investor.discover.vo;

import java.util.List;

/**
 * 新闻栏目VO
 * @author zxtan
 * @date 2017-03-09
 */
public class AppNewsCategoryVO {
	private String id;//栏目Id
	private String code;//栏目编码
	private String name;//栏目名称
	private String iconUrl;//栏目图标
	private String orderBy;//栏目排序
	private List<AppNewsCategoryVO> subCategoryList;//子栏目列表
		
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	
	public String getOrderBy() {
		return orderBy;
	}
	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public List<AppNewsCategoryVO> getSubCategoryList(){
		return subCategoryList;
	}
	public void setSubCategoryList(List<AppNewsCategoryVO> subCategoryList){
		this.subCategoryList = subCategoryList;
	}
}
