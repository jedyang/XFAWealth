package com.fsll.wmes.crm.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.InvestorAccount;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.RpqExam;


public class InvestorAccountDistributorVO {
	private String ifaId;
	private String memberId;
	private String distributorId;
	private String distributorName;
	private Date nextDocCheckDate;
	private Integer nextDocCheckDays;
	
	private List<InvestorAccount> accountList;


	public String getIfaId() {
		return this.ifaId;
	}

	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}


	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	

	public String getDistributorId() {
		return this.distributorId;
	}

	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributorName() {
		return this.distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}
	
	public Date getNextDocCheckDate() {
		return this.nextDocCheckDate;
	}

	public void setNextDocCheckDate(Date nextDocCheckDate) {
		this.nextDocCheckDate = nextDocCheckDate;
	}
	
	public Integer getNextDocCheckDays() {
		return this.nextDocCheckDays;
	}

	public void setNextDocCheckDays(Integer nextDocCheckDays) {
		this.nextDocCheckDays = nextDocCheckDays;
	}
	

	public List<InvestorAccount> getAccountList() {
		return this.accountList;
	}

	public void setAccountList(List<InvestorAccount> accountList) {
		this.accountList = accountList;
	}
}
