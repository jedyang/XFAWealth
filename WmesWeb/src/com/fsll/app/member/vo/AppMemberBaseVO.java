/*
 * Copyright (c) 2016-2019 by fsll
 * All rights reserved.
 */
package com.fsll.app.member.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberBase;

/**
 * 
 * @author zxtan
 * @version 1.0.0
 * Created On: 2017-02-20
 */
public class AppMemberBaseVO {
    private String id;//会员Id
	private String loginCode;//登录账号
	private String nickName;//昵称
	private String email;//邮箱
	private String mobileCode;//手机区号
	private String mobileNumber;//手机号码
	private String iconUrl;//头像Url
	private String gender;//性别
	private String hobby;//兴趣爱好
	private String defDisplayColor;//默认颜色 1:绿涨红跌;2红涨绿跌
	private String defCurrency;//默认货币
	private String defCurrencyCode;//默认货币Code
	private String dateformat;//日期格式
	
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

	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getDefDisplayColor() {
		return defDisplayColor;
	}
	public void setDefDisplayColor(String defDisplayColor) {
		this.defDisplayColor = defDisplayColor;
	}
	public String getDefCurrency() {
		return defCurrency;
	}
	public void setDefCurrency(String defCurrency) {
		this.defCurrency = defCurrency;
	}
	public String getDefCurrencyCode() {
		return defCurrencyCode;
	}
	public void setDefCurrencyCode(String defCurrencyCode) {
		this.defCurrencyCode = defCurrencyCode;
	}
	public String getDateformat() {
		return dateformat;
	}
	public void setDateformat(String dateformat) {
		this.dateformat = dateformat;
	}
}
