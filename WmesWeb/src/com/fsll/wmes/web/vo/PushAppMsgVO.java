package com.fsll.wmes.web.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PushAppMsgVO implements java.io.Serializable {
	private String _app_code_;
	private String _app_psw_;
	private String _app_server_ip_;
	private String msgType;
	
	private String id;
//	private FPushApp app;
	private String type;
	private String deviceTokens;
	private String aliasType;
	private String alias;
	private String fileId;
	private String filter;
	private String displayType;
	private String ticker;
	private String title;
	private String text;
	private String icon;
	private String largeIcon;
	private String img;
	private String androidSound;
	private Integer builderId;
	private String playVibrate;
	private String playLights;
	private String playSound;
	private String afterOpen;
	private String url;
	private String activity;
	private String custom;
	private String alert;
	private Integer badge;
	private String iosSound;
	private String contentAvailable;
	private Integer category;
	private String extraField;
	private String startTime;
	private String expireTime;
	private Integer maxSendNum;
	private String productionMode;
	private String description;
	private String thirdpartyId;
	private String createUser;
	private Date createTime;
	private String sendFlag;
	private Integer sendCount;
	private Date sendTime;
	private String sendResult;
	private String errorCode;
	
	
	public String getId() {
    	return id;
    }
	public void setId(String id) {
    	this.id = id;
    }
	public String getMsgType() {
    	return msgType;
    }
	public void setMsgType(String msgType) {
    	this.msgType = msgType;
    }
	public String getType() {
    	return type;
    }
	public void setType(String type) {
    	this.type = type;
    }
	public String getDeviceTokens() {
    	return deviceTokens;
    }
	public void setDeviceTokens(String deviceTokens) {
    	this.deviceTokens = deviceTokens;
    }
	public String getAliasType() {
    	return aliasType;
    }
	public void setAliasType(String aliasType) {
    	this.aliasType = aliasType;
    }
	public String getAlias() {
    	return alias;
    }
	public void setAlias(String alias) {
    	this.alias = alias;
    }
	public String getFileId() {
    	return fileId;
    }
	public void setFileId(String fileId) {
    	this.fileId = fileId;
    }
	public String getFilter() {
    	return filter;
    }
	public void setFilter(String filter) {
    	this.filter = filter;
    }
	public String getDisplayType() {
    	return displayType;
    }
	public void setDisplayType(String displayType) {
    	this.displayType = displayType;
    }
	public String getTicker() {
    	return ticker;
    }
	public void setTicker(String ticker) {
    	this.ticker = ticker;
    }
	public String getTitle() {
    	return title;
    }
	public void setTitle(String title) {
    	this.title = title;
    }
	public String getText() {
    	return text;
    }
	public void setText(String text) {
    	this.text = text;
    }
	public String getIcon() {
    	return icon;
    }
	public void setIcon(String icon) {
    	this.icon = icon;
    }
	public String getLargeIcon() {
    	return largeIcon;
    }
	public void setLargeIcon(String largeIcon) {
    	this.largeIcon = largeIcon;
    }
	public String getImg() {
    	return img;
    }
	public void setImg(String img) {
    	this.img = img;
    }
	public String getAndroidSound() {
    	return androidSound;
    }
	public void setAndroidSound(String androidSound) {
    	this.androidSound = androidSound;
    }
	public Integer getBuilderId() {
    	return builderId;
    }
	public void setBuilderId(Integer builderId) {
    	this.builderId = builderId;
    }
	public String getPlayVibrate() {
    	return playVibrate;
    }
	public void setPlayVibrate(String playVibrate) {
    	this.playVibrate = playVibrate;
    }
	public String getPlayLights() {
    	return playLights;
    }
	public void setPlayLights(String playLights) {
    	this.playLights = playLights;
    }
	public String getPlaySound() {
    	return playSound;
    }
	public void setPlaySound(String playSound) {
    	this.playSound = playSound;
    }
	public String getAfterOpen() {
    	return afterOpen;
    }
	public void setAfterOpen(String afterOpen) {
    	this.afterOpen = afterOpen;
    }
	public String getUrl() {
    	return url;
    }
	public void setUrl(String url) {
    	this.url = url;
    }
	public String getActivity() {
    	return activity;
    }
	public void setActivity(String activity) {
    	this.activity = activity;
    }
	public String getCustom() {
    	return custom;
    }
	public void setCustom(String custom) {
    	this.custom = custom;
    }
	public String getAlert() {
    	return alert;
    }
	public void setAlert(String alert) {
    	this.alert = alert;
    }
	public Integer getBadge() {
    	return badge;
    }
	public void setBadge(Integer badge) {
    	this.badge = badge;
    }
	public String getIosSound() {
    	return iosSound;
    }
	public void setIosSound(String iosSound) {
    	this.iosSound = iosSound;
    }
	public String getContentAvailable() {
    	return contentAvailable;
    }
	public void setContentAvailable(String contentAvailable) {
    	this.contentAvailable = contentAvailable;
    }
	public Integer getCategory() {
    	return category;
    }
	public void setCategory(Integer category) {
    	this.category = category;
    }
	public String getExtraField() {
    	return extraField;
    }
	public void setExtraField(String extraField) {
    	this.extraField = extraField;
    }
	public String getStartTime() {
    	return startTime;
    }
	public void setStartTime(String startTime) {
    	this.startTime = startTime;
    }
	public String getExpireTime() {
    	return expireTime;
    }
	public void setExpireTime(String expireTime) {
    	this.expireTime = expireTime;
    }
	public Integer getMaxSendNum() {
    	return maxSendNum;
    }
	public void setMaxSendNum(Integer maxSendNum) {
    	this.maxSendNum = maxSendNum;
    }
	public String getProductionMode() {
    	return productionMode;
    }
	public void setProductionMode(String productionMode) {
    	this.productionMode = productionMode;
    }
	public String getDescription() {
    	return description;
    }
	public void setDescription(String description) {
    	this.description = description;
    }
	public String getThirdpartyId() {
    	return thirdpartyId;
    }
	public void setThirdpartyId(String thirdpartyId) {
    	this.thirdpartyId = thirdpartyId;
    }
	public String getCreateUser() {
    	return createUser;
    }
	public void setCreateUser(String createUser) {
    	this.createUser = createUser;
    }
	public Date getCreateTime() {
    	return createTime;
    }
	public void setCreateTime(Date createTime) {
    	this.createTime = createTime;
    }
	public String getSendFlag() {
    	return sendFlag;
    }
	public void setSendFlag(String sendFlag) {
    	this.sendFlag = sendFlag;
    }
	public Integer getSendCount() {
    	return sendCount;
    }
	public void setSendCount(Integer sendCount) {
    	this.sendCount = sendCount;
    }
	public Date getSendTime() {
    	return sendTime;
    }
	public void setSendTime(Date sendTime) {
    	this.sendTime = sendTime;
    }
	public String getSendResult() {
    	return sendResult;
    }
	public void setSendResult(String sendResult) {
    	this.sendResult = sendResult;
    }
	public String getErrorCode() {
    	return errorCode;
    }
	public void setErrorCode(String errorCode) {
    	this.errorCode = errorCode;
    }
	public String get_app_code_() {
    	return _app_code_;
    }
	public void set_app_code_(String _app_code_) {
    	this._app_code_ = _app_code_;
    }
	public String get_app_psw_() {
    	return _app_psw_;
    }
	public void set_app_psw_(String _app_psw_) {
    	this._app_psw_ = _app_psw_;
    }
	public String get_app_server_ip_() {
    	return _app_server_ip_;
    }
	public void set_app_server_ip_(String _app_server_ip_) {
    	this._app_server_ip_ = _app_server_ip_;
    }


}