package com.fsll.app.investor.market.vo;

/**
 * 首页社区主题回复VO
 * @author zpzhou
 * @date 2016-10-09
 */
public class AppIndexBbsReplyVO {
	
	private String id;
	private String typeName;
	private String topicid;
	private String replyUserName;
	private String replayTime;
	private String emotionIcon;
	private String content;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getTopicid() {
		return topicid;
	}
	public void setTopicid(String topicid) {
		this.topicid = topicid;
	}
	public String getReplyUserName() {
		return replyUserName;
	}
	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}
	public String getReplayTime() {
		return replayTime;
	}
	public void setReplayTime(String replayTime) {
		this.replayTime = replayTime;
	}
	public String getEmotionIcon() {
		return emotionIcon;
	}
	public void setEmotionIcon(String emotionIcon) {
		this.emotionIcon = emotionIcon;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
