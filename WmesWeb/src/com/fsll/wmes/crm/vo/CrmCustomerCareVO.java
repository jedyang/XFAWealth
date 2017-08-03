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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberDistributor;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.RpqExam;


public class CrmCustomerCareVO {
	private String id;
	private String ifaId;
	private String memberId;
	private String mobileCode;
	private String mobileNumber;
	private String email;
	private String nickName;
	private String groupName;
	
	private String birthdayRemind;
	private String appointmentRemind;	
	private String createTime;
	private String lastUpdate;	
	private String contactTimes;
	private String lastContact;	
	private String proposalInvAmount;
	private String proposalCurrency;
	private String proposalStatus;
	private String proposalLastUpdate;
	
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}


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



	public String getMobileCode() {
		return this.mobileCode;
	}

	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}


	public String getMobileNumber() {
		return this.mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	

	public String getBirthdayRemind() {
		return this.birthdayRemind;
	}

	public void setBirthdayRemind(String birthdayRemind) {
		this.birthdayRemind = birthdayRemind;
	}
	

	public String getAppointmentRemind() {
		return this.appointmentRemind;
	}

	public void setAppointmentRemind(String appointmentRemind) {
		this.appointmentRemind = appointmentRemind;
	}
	

	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(String lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getContactTimes() {
		return this.contactTimes;
	}

	public void setContactTimes(String contactTimes) {
		this.contactTimes = contactTimes;
	}

	public String getLastContact() {
		return this.lastContact;
	}

	public void setLastContact(String lastContact) {
		this.lastContact = lastContact;
	}

	public String getProposalInvAmount() {
		return this.proposalInvAmount;
	}

	public void setProposalInvAmount(String proposalInvAmount) {
		this.proposalInvAmount = proposalInvAmount;
	}
	
	public String getProposalCurrency() {
		return this.proposalCurrency;
	}

	public void setProposalCurrency(String proposalCurrency) {
		this.proposalCurrency = proposalCurrency;
	}

	public String getProposalStatus() {
		return this.proposalStatus;
	}

	public void setProposalStatus(String proposalStatus) {
		this.proposalStatus = proposalStatus;
	}

	public String getProposalLastUpdate() {
		return this.proposalLastUpdate;
	}

	public void setProposalLastUpdate(String proposalLastUpdate) {
		this.proposalLastUpdate = proposalLastUpdate;
	}
	
}
