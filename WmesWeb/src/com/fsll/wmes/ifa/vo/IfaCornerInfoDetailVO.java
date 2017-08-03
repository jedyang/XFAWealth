package com.fsll.wmes.ifa.vo;

import java.util.Date;
import java.util.List;

/**
 * 发现朋友圈发表主题详细信息实体
 * @author 林文伟
 * @date 2016-10-12
 */
public class IfaCornerInfoDetailVO {
	private String id;
	private String emotionIcon;
	private Integer replycount;//评论数
	private String topic;//主题
	private String content;//内容
	private String iconUrl;
	private String url;
	private Date lastReplayTime;
	private Date createTime;
	private String createTimeFormat;

	private Integer likedCount;//点赞数
	private List<IfaCornerInfoLikedVO> ifaCornerInfoLikedList;//点赞会员列表
	private List<IfaCornerInfoReplyVO> ifaCornerInfoReplyList;//评论列表
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmotionIcon() {
		return emotionIcon;
	}
	public void setEmotionIcon(String emotionIcon) {
		this.emotionIcon = emotionIcon;
	}
	public Integer getReplycount() {
		return replycount;
	}
	public void setReplycount(Integer replycount) {
		this.replycount = replycount;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Date getLastReplayTime() {
		return lastReplayTime;
	}
	public void setLastReplayTime(Date lastReplayTime) {
		this.lastReplayTime = lastReplayTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public Integer getLikedCount() {
		return likedCount;
	}
	public void setLikedCount(Integer likedCount) {
		this.likedCount = likedCount;
	}
	
	public List<IfaCornerInfoLikedVO> getIfaCornerInfoLikedList() {
		return ifaCornerInfoLikedList;
	}
	public void setIfaCornerInfoLikedList(
			List<IfaCornerInfoLikedVO> ifaCornerInfoLikedList) {
		this.ifaCornerInfoLikedList = ifaCornerInfoLikedList;
	}
	public List<IfaCornerInfoReplyVO> getIfaCornerInfoReplyList() {
		return ifaCornerInfoReplyList;
	}
	public void setIfaCornerInfoReplyList(
			List<IfaCornerInfoReplyVO> ifaCornerInfoReplyList) {
		this.ifaCornerInfoReplyList = ifaCornerInfoReplyList;
	}
	public String getCreateTimeFormat() {
		return createTimeFormat;
	}
	public void setCreateTimeFormat(String createTimeFormat) {
		this.createTimeFormat = createTimeFormat;
	}
	

	
}
