package com.fsll.wmes.ifa.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.fund.vo.GeneralDataVO;

/**
 * 发现朋友圈发表主题评论实体
 * @author 林文伟
 * @date 2016-10-12
 */
public class IfaCornerInfoReplyVO {
	private String id;
	private String replyNickName;
	private String replyMemberId;//评论人ID
	private String content;//评论内容
	private String emotionIcon;
	private Date replayTime;//评论日期
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReplyMemberId() {
		return replyMemberId;
	}
	public void setReplyMemberId(String replyMemberId) {
		this.replyMemberId = replyMemberId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getEmotionIcon() {
		return emotionIcon;
	}
	public void setEmotionIcon(String emotionIcon) {
		this.emotionIcon = emotionIcon;
	}
	public Date getReplayTime() {
		return replayTime;
	}
	public void setReplayTime(Date replayTime) {
		this.replayTime = replayTime;
	}
	public String getReplyNickName() {
		return replyNickName;
	}
	public void setReplyNickName(String replyNickName) {
		this.replyNickName = replyNickName;
	}
	
	
}
