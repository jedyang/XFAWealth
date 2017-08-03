/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

@Entity
///@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "member_base")
public class MemberBase {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
	
	@Column(name = "login_code")
	private String loginCode;
	
	@Column(name = "password")
	private String password;
	
	@Column(name = "nick_name")
	private String nickName;
	
	@Column(name = "email")
	private String email;
	
    @Column(name = "mobile_code")
	private String mobileCode;
    
    @Column(name = "mobile_number")
	private String mobileNumber;
	
	@Column(name = "member_type")
	private Integer memberType;
	
	@Column(name = "sub_member_type")
	private Integer subMemberType;
	
    @Column(name="last_login_ip")
    private String lastLoginIp;//登陆IP
    
    @Column(name="login_time")
    private Date loginTime;//登陆时间
    
    @Column(name="login_count")
    private Integer loginCount;//登录次数
	
    @Column(name="def_currency")
    private String defCurrency;
	
    @Column(name="def_display_color")
    private String defDisplayColor;
    
	@Column(name = "icon_url")
	private String iconUrl;
	
	@Column(name = "invest_style")
	private String investStyle;
	
	@Column(name = "invest_field")
	private String investField;
	
	@Column(name = "hobby_type")
	private String hobbyType;
	
	@Column(name = "language_spoken")
	private String languageSpoken;
	
	@Column(name = "live_region")
	private String liveRegion;
    
	@Column(name = "lang_code")
	private String langCode;
    
	@Column(name = "dateformat")
	private String dateFormat;
	
	@Column(name = "invite_code")
	private String inviteCode;
	
	@Column(name = "webchat_code")
	private String webchatCode;
	
	@Column(name = "linkedIn_code")
	private String linkedInCode;
	
	@Column(name = "facebook_code")
	private String facebookCode;
	
	@Column(name = "qq_code")
	private String qqCode;

	@Column(name = "weibo_code")
	private String weiboCode;
	
	@Column(name = "twitter_code")
	private String twitterCode;
	
	@Column(name = "privacy_setting")
	private String privacySetting;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_valid")
	private String isValid;

    @Column(name="fail_count")
    private Integer failCount;//登录失败次数
    
	@Column(name = "lock_status")
	private String lockStatus;
	
	@Column(name = "lock_date")
	private Date lockDate;
	
	@Column(name = "im_user_id")
	private String imUserId;
	
	@Column(name = "im_user_pwd")
	private String imUserPwd;
	
	@Column(name = "im_nick_name")
	private String imNickName;
	
	@Column(name = "im_icon_url")
	private String imNickUrl;
	
	@Column(name = "im_status")
	private String imStatus;
	
	@Column(name = "risk_declare_flag")
	private String riskDeclareFlag;
	
	@Column(name = "highlight")
	private String highlight;
	
	@Column(name = "is_system")
	private String isSystem;
	
	/****** 以下字段不存储数据库,仅用于页面显示 *******/
	@Transient
    private String rIds;//需要移除的id
	@Transient
    private String xh;//列表中显示序号字段
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLoginCode() {
		return loginCode;
	}
	public void setLoginCode(String loginCode) {
		this.loginCode = loginCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
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
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdate() {
		return lastUpdate;
	}
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLangCode() {
		return langCode;
	}
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}
	public String getIsValid() {
		return isValid;
	}
	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	public String getrIds() {
		return rIds;
	}
	public void setrIds(String rIds) {
		this.rIds = rIds;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
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
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public Date getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}
	public Integer getLoginCount() {
		return loginCount;
	}
	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
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
	public String getInvestStyle() {
		return investStyle;
	}
	public void setInvestStyle(String investStyle) {
		this.investStyle = investStyle;
	}
	public String getInvestField() {
		return investField;
	}
	public void setInvestField(String investField) {
		this.investField = investField;
	}
	public String getHobbyType() {
		return hobbyType;
	}
	public void setHobbyType(String hobbyType) {
		this.hobbyType = hobbyType;
	}
	public String getDateFormat() {
		return dateFormat;
	}
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	public String getInviteCode() {
		return inviteCode;
	}
	public void setInviteCode(String inviteCode) {
		this.inviteCode = inviteCode;
	}
	public String getWebchatCode() {
		return webchatCode;
	}
	public void setWebchatCode(String webchatCode) {
		this.webchatCode = webchatCode;
	}
	public String getFacebookCode() {
		return facebookCode;
	}
	public void setFacebookCode(String facebookCode) {
		this.facebookCode = facebookCode;
	}
	public String getQqCode() {
		return qqCode;
	}
	public void setQqCode(String qqCode) {
		this.qqCode = qqCode;
	}
	public String getPrivacySetting() {
		return privacySetting;
	}
	public void setPrivacySetting(String privacySetting) {
		this.privacySetting = privacySetting;
	}
	public String getLinkedInCode() {
		return linkedInCode;
	}
	public void setLinkedInCode(String linkedInCode) {
		this.linkedInCode = linkedInCode;
	}
	public String getWeiboCode() {
		return weiboCode;
	}
	public void setWeiboCode(String weiboCode) {
		this.weiboCode = weiboCode;
	}
	public String getTwitterCode() {
		return twitterCode;
	}
	public void setTwitterCode(String twitterCode) {
		this.twitterCode = twitterCode;
	}
	public String getLanguageSpoken() {
		return languageSpoken;
	}
	public void setLanguageSpoken(String languageSpoken) {
		this.languageSpoken = languageSpoken;
	}
	public String getLiveRegion() {
		return liveRegion;
	}
	public void setLiveRegion(String liveRegion) {
		this.liveRegion = liveRegion;
	}
	public Integer getFailCount() {
		return failCount;
	}
	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}
	public String getLockStatus() {
		return lockStatus;
	}
	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}
	public Date getLockDate() {
		return lockDate;
	}
	public void setLockDate(Date lockDate) {
		this.lockDate = lockDate;
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
	public String getImNickUrl() {
		return imNickUrl;
	}
	public void setImNickUrl(String imNickUrl) {
		this.imNickUrl = imNickUrl;
	}
	public String getRiskDeclareFlag() {
		return riskDeclareFlag;
	}
	public void setRiskDeclareFlag(String riskDeclareFlag) {
		this.riskDeclareFlag = riskDeclareFlag;
	}
	public String getImStatus() {
		return imStatus;
	}
	public void setImStatus(String imStatus) {
		this.imStatus = imStatus;
	}
	public String getHighlight() {
		return highlight;
	}
	public void setHighlight(String highlight) {
		this.highlight = highlight;
	}
	public String getIsSystem() {
		return isSystem;
	}
	public void setIsSystem(String isSystem) {
		this.isSystem = isSystem;
	}
	
}
