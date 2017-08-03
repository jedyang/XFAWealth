package com.fsll.wmes.news.vo;

import java.util.List;



public class NewsCategoryVO {

	private String id;
	private String code;
	private String name;
	private String parentId;
	private List<String> parent;
	private List<NewsCategoryVO> children;//=new ArrayList<NewsCategoryVO>();


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

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<NewsCategoryVO> getChildren() {
		return children;
	}

	public void setChildren(List<NewsCategoryVO> children) {
		this.children = children;
	}

	public List<String> getParent() {
		return parent;
	}

	public void setParent(List<String> parent) {
		this.parent = parent;
	}

	
	

}
