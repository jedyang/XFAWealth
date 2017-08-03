package com.fsll.wmes.crm.vo;

import java.util.Date;

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
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.RpqExam;


public class InvestorAccountVO {
	private String id;
	private MemberBase member;
	private String accountNo;
	private String totalFlag;
	private String fromType;
	private String accType;
	private String investType;
	private String cies;
	private String dividend;
	private String baseCurrency;
	private String purpose;
	private String purposeOther;
	private String sentBy;
	private String discFlag;
	private MemberDistributor distributor;
	private MemberIfa ifa;
	private RpqExam rpqExam;
	private String submitStatus;
	private String openStatus;
	private String curStep;
	private String finishStatus;
	private String flowId;
	private String flowStatus;
	private MemberBase createBy;
	private Date createTime;
	private Date lastUpdate;
	private String isValid;
	
	
	private Date nextDocCheckDate;
	private Integer nextDocCheckDays;


	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public MemberBase getMember() {
		return this.member;
	}

	public void setMember(MemberBase member) {
		this.member = member;
	}


	public String getAccountNo() {
		return this.accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}


	public String getTotalFlag() {
		return this.totalFlag;
	}

	public void setTotalFlag(String totalFlag) {
		this.totalFlag = totalFlag;
	}


	public String getFromType() {
		return this.fromType;
	}

	public void setFromType(String fromType) {
		this.fromType = fromType;
	}


	public String getAccType() {
		return this.accType;
	}

	public void setAccType(String accType) {
		this.accType = accType;
	}


	public String getInvestType() {
		return this.investType;
	}

	public void setInvestType(String investType) {
		this.investType = investType;
	}


	public String getCies() {
		return this.cies;
	}

	public void setCies(String cies) {
		this.cies = cies;
	}


	public String getDividend() {
		return this.dividend;
	}

	public void setDividend(String dividend) {
		this.dividend = dividend;
	}


	public String getBaseCurrency() {
		return this.baseCurrency;
	}

	public void setBaseCurrency(String baseCurrency) {
		this.baseCurrency = baseCurrency;
	}


	public String getPurpose() {
		return this.purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}


	public String getPurposeOther() {
		return this.purposeOther;
	}

	public void setPurposeOther(String purposeOther) {
		this.purposeOther = purposeOther;
	}


	public String getSentBy() {
		return this.sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}


	public String getDiscFlag() {
		return this.discFlag;
	}

	public void setDiscFlag(String discFlag) {
		this.discFlag = discFlag;
	}


	public MemberDistributor getDistributor() {
		return distributor;
	}

	public void setDistributor(MemberDistributor distributor) {
		this.distributor = distributor;
	}


	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}


	public RpqExam getRpqExam() {
		return rpqExam;
	}

	public void setRpqExam(RpqExam rpqExam) {
		this.rpqExam = rpqExam;
	}


	public String getOpenStatus() {
		return this.openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}


	public String getCurStep() {
		return this.curStep;
	}

	public void setCurStep(String curStep) {
		this.curStep = curStep;
	}


	public String getFinishStatus() {
		return this.finishStatus;
	}

	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}


	public String getFlowId() {
		return this.flowId;
	}

	public void setFlowId(String flowId) {
		this.flowId = flowId;
	}


	public String getFlowStatus() {
		return flowStatus;
	}

	public void setFlowStatus(String flowStatus) {
		this.flowStatus = flowStatus;
	}


	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}


	public String getIsValid() {
		return this.isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}


	public String getSubmitStatus() {
		return submitStatus;
	}

	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}


	public MemberBase getCreateBy() {
		return createBy;
	}

	public void setCreateBy(MemberBase createBy) {
		this.createBy = createBy;
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
}
