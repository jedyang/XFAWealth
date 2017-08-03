package com.fsll.wmes.rpq.vo;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.RpqQuest;

public class RpqQuestItemVO {

	private String type;
	private String title;
	private Integer scoreValue;
	private Integer orderBy;
	private String remark;
	
	private String titleSc;
	private String remarkSc;
	
	private String titleTc;
	private String remarkTc;
	
	private String titleEn;
	private String remarkEn;


	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getScoreValue() {
		return this.scoreValue;
	}

	public void setScoreValue(Integer scoreValue) {
		this.scoreValue = scoreValue;
	}

	public Integer getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	public String getTitleTc() {
		return this.titleTc;
	}

	public void setTitleTc(String titleTc) {
		this.titleTc = titleTc;
	}
	
	public String getTitleSc() {
		return this.titleSc;
	}

	public void setTitleSc(String titleSc) {
		this.titleSc = titleSc;
	}
	
	public String getTitleEn() {
		return this.titleEn;
	}

	public void setTitleEn(String titleEn) {
		this.titleEn = titleEn;
	}
	
	public String getRemarkSc() {
		return this.remarkSc;
	}

	public void setRemarkSc(String remarkSc) {
		this.remarkSc = remarkSc;
	}
	
	public String getRemarkTc() {
		return this.remarkTc;
	}

	public void setRemarkTc(String remarkTc) {
		this.remarkTc = remarkTc;
	}
	
	public String getRemarkEn() {
		return this.remarkEn;
	}

	public void setRemarkEn(String remarkEn) {
		this.remarkEn = remarkEn;
	}
}
