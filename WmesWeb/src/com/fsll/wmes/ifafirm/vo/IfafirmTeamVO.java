package com.fsll.wmes.ifafirm.vo;

import java.util.List;

import com.fsll.wmes.entity.IfafirmTeam;
import com.fsll.wmes.entity.MemberIfafirm;



public class IfafirmTeamVO {

    private String id;
    private String parentId;
    private IfafirmTeam parent;
    private String ifafirmId;
    private MemberIfafirm ifafirm;
    private String code;
    private String name;
    private int orderBy;
    private String reamrk;
    private int classLayer;
    private String isValid;
    private String supervisor;
    private List supervisorList;
    
	public String getId() {
    	return id;
    }
	public void setId(String id) {
    	this.id = id;
    }
	public String getParentId() {
    	return parentId;
    }
	public void setParentId(String parentId) {
    	this.parentId = parentId;
    }
	public IfafirmTeam getParent() {
    	return parent;
    }
	public void setParent(IfafirmTeam parent) {
    	this.parent = parent;
    }
	public String getIfafirmId() {
    	return ifafirmId;
    }
	public void setIfafirmId(String ifafirmId) {
    	this.ifafirmId = ifafirmId;
    }
	public MemberIfafirm getIfafirm() {
    	return ifafirm;
    }
	public void setIfafirm(MemberIfafirm ifafirm) {
    	this.ifafirm = ifafirm;
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
	public int getOrderBy() {
    	return orderBy;
    }
	public void setOrderBy(int orderBy) {
    	this.orderBy = orderBy;
    }
	public String getReamrk() {
    	return reamrk;
    }
	public void setReamrk(String reamrk) {
    	this.reamrk = reamrk;
    }
	public int getClassLayer() {
    	return classLayer;
    }
	public void setClassLayer(int classLayer) {
    	this.classLayer = classLayer;
    }
	public String getIsValid() {
    	return isValid;
    }
	public void setIsValid(String isValid) {
    	this.isValid = isValid;
    }
	public String getSupervisor() {
    	return supervisor;
    }
	public void setSupervisor(String supervisor) {
    	this.supervisor = supervisor;
    }
	public void setSupervisorList(List supervisorList) {
	    this.supervisorList = supervisorList;
    }
	public List getSupervisorList() {
	    return supervisorList;
    }
    
}
