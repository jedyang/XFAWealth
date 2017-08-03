package com.fsll.wmes.discover.vo;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebDiscussReply;

public class WebDiscussReplyVO extends WebDiscussReply{

	// D:天    H：小时
	private String timeType;
	// 2小时前   TimeNum：2，TimeType：H
	private Integer timeNum;
	// 被回复人
	private String beRepliedTo;
	// 回复人
	private String nickName;
	private String iconUrl;
	
	public String getTimeType() {
		return timeType;
	}
	public void setTimeType(String timeType) {
		this.timeType = timeType;
	}
	public Integer getTimeNum() {
		return timeNum;
	}
	public void setTimeNum(Integer timeNum) {
		this.timeNum = timeNum;
	}
	public String getBeRepliedTo() {
		return beRepliedTo;
	}
	public void setBeRepliedTo(String beRepliedTo) {
		this.beRepliedTo = beRepliedTo;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	
}
