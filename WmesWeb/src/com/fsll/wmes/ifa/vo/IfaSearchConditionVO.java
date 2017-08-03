package com.fsll.wmes.ifa.vo;

import java.util.ArrayList;
import java.util.List;

import com.fsll.core.vo.SysParamVO;


/**
 * IFA搜索条件实体类
 * 
 * @author zpzhou
 * @date 2016-7-25
 */
public class IfaSearchConditionVO {
	List<IfaSearchItemVO> seList = new ArrayList<IfaSearchItemVO>();
	List<IfaSearchItemVO> exList = new ArrayList<IfaSearchItemVO>();
	List<IfaSearchItemVO> coList = new ArrayList<IfaSearchItemVO>();
	List<IfaSearchItemVO> disList = new ArrayList<IfaSearchItemVO>();
	
	public List<IfaSearchItemVO> getSeList() {
		return seList;
	}
	public void setSeList(List<IfaSearchItemVO> seList) {
		this.seList = seList;
	}
	public List<IfaSearchItemVO> getExList() {
		return exList;
	}
	public void setExList(List<IfaSearchItemVO> exList) {
		this.exList = exList;
	}
	public List<IfaSearchItemVO> getCoList() {
		return coList;
	}
	public void setCoList(List<IfaSearchItemVO> coList) {
		this.coList = coList;
	}
	public List<IfaSearchItemVO> getDisList() {
		return disList;
	}
	public void setDisList(List<IfaSearchItemVO> disList) {
		this.disList = disList;
	}
}
