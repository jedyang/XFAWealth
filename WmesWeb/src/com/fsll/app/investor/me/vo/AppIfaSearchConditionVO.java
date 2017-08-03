package com.fsll.app.investor.me.vo;

import java.util.ArrayList;
import java.util.List;


/**
 * IFA搜索条件实体类
 * 
 * @author zpzhou
 * @date 2016-7-25
 */
public class AppIfaSearchConditionVO {
	List<AppIfaSearchItemVO> seList = new ArrayList<AppIfaSearchItemVO>();
	List<AppIfaSearchItemVO> exList = new ArrayList<AppIfaSearchItemVO>();
	List<AppIfaSearchItemVO> coList = new ArrayList<AppIfaSearchItemVO>();
	List<AppIfaSearchItemVO> disList = new ArrayList<AppIfaSearchItemVO>();
	public List<AppIfaSearchItemVO> getSeList() {
		return seList;
	}
	public void setSeList(List<AppIfaSearchItemVO> seList) {
		this.seList = seList;
	}
	public List<AppIfaSearchItemVO> getExList() {
		return exList;
	}
	public void setExList(List<AppIfaSearchItemVO> exList) {
		this.exList = exList;
	}
	public List<AppIfaSearchItemVO> getCoList() {
		return coList;
	}
	public void setCoList(List<AppIfaSearchItemVO> coList) {
		this.coList = coList;
	}
	public List<AppIfaSearchItemVO> getDisList() {
		return disList;
	}
	public void setDisList(List<AppIfaSearchItemVO> disList) {
		this.disList = disList;
	}
	
	
}
