/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.member.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberBase;

/**
 * 
 * @author 黄模建 E-mail:mjhuang@fsll.cn
 * @version 1.0.0
 * Created On: 2016-7-1
 */
public class MemberSsoVO {
    private String id;
    private String sessionId;
    private String tokenId;
    private String tokenExpireTime;
	private String loginCode;//也等于app中的alias
	private String nickName;//昵称
	private String memberName;//中文名或者英文名,不同的环境会保存不同的名称
	private String gender;
	private String email;
	private String mobileCode;
	private String mobileNumber;
	private Integer memberType;
	private Integer subMemberType;
    private String defCurrency;
    private String defDisplayColor;
    private String dateFormat;
	private String iconUrl;
	private String langCode;
	private String status;
	private Date lastLoginTime;
	private String imAppKey;
	private String imUserId;
	private String imUserPwd;
	private String imNickName;
	private MemberBase baseInfo;
	
	private String companyId;
	private String individualId;//member_individual.id
	private String corporateId;//member_corporate.id
	private String fiId;//member_fi.id
	private String ifaId;//member_ifa.id
	private String ifafirmId;//member_ifafirm.id
	private String ifaCheckStatus;//ifafirm对ifa的审核状态 0待审批，1同意，2拒绝
	private String distributorId;//member_distributor.id
	private String groupCode;//也等于app中的aliasType
	private String device;//登录设备：web,app
	
    private String ret = "";//1成功,0失败
    private Integer failCount;//登录失败次数;//错误编码
    private String errorCode = "";//错误编码
    private String errorMsg = "";//错误信息
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTokenId() {
		return tokenId;
	}
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobileCode() {
		return mobileCode;
	}
	public void setMobileCode(String mobileCode) {
		this.mobileCode = mobileCode;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public Integer getMemberType() {
		return memberType;
	}
	public void setMemberType(Integer memberType) {
		this.memberType = memberType;
	}
	public Integer getSubMemberType() {
		return subMemberType;
	}
	public void setSubMemberType(Integer subMemberType) {
		this.subMemberType = subMemberType;
	}
	public String getDefCurrency() {
		return defCurrency;
	}
	public void setDefCurrency(String defCurrency) {
		this.defCurrency = defCurrency;
	}
	public String getDefDisplayColor() {
		return defDisplayColor;
	}
	public void setDefDisplayColor(String defDisplayColor) {
		this.defDisplayColor = defDisplayColor;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public String getTokenExpireTime() {
		return tokenExpireTime;
	}
	public void setTokenExpireTime(String tokenExpireTime) {
		this.tokenExpireTime = tokenExpireTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	public Date getLastLoginTime() {
		return lastLoginTime;
	}
	public MemberBase getBaseInfo() {
		return baseInfo;
	}
	public void setBaseInfo(MemberBase baseInfo) {
		this.baseInfo = baseInfo;
	}
	public String getIfaId() {
		return ifaId;
	}
	public void setIfaId(String ifaId) {
		this.ifaId = ifaId;
	}
	public String getIfafirmId() {
		return ifafirmId;
	}
	public void setIfafirmId(String ifafirmId) {
		this.ifafirmId = ifafirmId;
	}
	public String getDistributorId() {
		return distributorId;
	}
	public void setDistributorId(String distributorId) {
		this.distributorId = distributorId;
	}
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	public String getIndividualId() {
		return individualId;
	}
	public void setIndividualId(String individualId) {
		this.individualId = individualId;
	}
	public String getCorporateId() {
		return corporateId;
	}
	public void setCorporateId(String corporateId) {
		this.corporateId = corporateId;
	}
	public String getFiId() {
		return fiId;
	}
	public void setFiId(String fiId) {
		this.fiId = fiId;
	}
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getImAppKey() {
		return imAppKey;
	}
	public void setImAppKey(String imAppKey) {
		this.imAppKey = imAppKey;
	}
	public String getImUserId() {
		return imUserId;
	}
	public void setImUserId(String imUserId) {
		this.imUserId = imUserId;
	}
	public String getImUserPwd() {
		return imUserPwd;
	}
	public void setImUserPwd(String imUserPwd) {
		this.imUserPwd = imUserPwd;
	}
	public String getImNickName() {
		return imNickName;
	}
	public void setImNickName(String imNickName) {
		this.imNickName = imNickName;
	}
	public String getGender() {
    	return gender;
    }
	public void setGender(String gender) {
    	this.gender = gender;
    }
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getIfaCheckStatus() {
		return ifaCheckStatus;
	}
	public void setIfaCheckStatus(String ifaCheckStatus) {
		this.ifaCheckStatus = ifaCheckStatus;
	}
}
