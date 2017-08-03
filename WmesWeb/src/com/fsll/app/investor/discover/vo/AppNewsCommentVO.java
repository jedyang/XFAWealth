package com.fsll.app.investor.discover.vo;

import java.util.List;

/**
 * 新闻评论VO
 * @author zxtan
 * @date 2017-03-10
 */
public class AppNewsCommentVO {
	private String id;//评论Id
	private String newsInfoId;//新闻Id
	private String memberId;//评论人Id
	private String memberIconUrl;//评论人头像
	private String nickname;//评论人昵称

	private String replyMemberId;//被评论人Id
	private String replyMemberIconUrl;//被评论人头像
	private String replyNickname;//被评论人昵称
	
	private String comment;//评论内容
	private String good;//点赞数量
	private String bad;//踩的数量
	private String opTime;//评论时间
	private String ip;//IP地址
	private String fType;//评论类型 feedback,good,bad
	private String face;//表情
	private String checked;//审核标记，1：审核通过；0：审核不通过；-1：待审核
	private String behaviorType;//赞或踩
//	private List<AppNewsCommentVO> replyList;//评论回复列表
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNewsInfoId() {
		return newsInfoId;
	}
	public void setNewsInfoId(String newsInfoId) {
		this.newsInfoId = newsInfoId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberIconUrl() {
		return memberIconUrl;
	}
	public void setMemberIconUrl(String memberIconUrl) {
		this.memberIconUrl = memberIconUrl;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}	
	public String getReplyMemberId() {
		return replyMemberId;
	}
	public void setReplyMemberId(String replyMemberId) {
		this.replyMemberId = replyMemberId;
	}
	public String getReplyMemberIconUrl() {
		return replyMemberIconUrl;
	}
	public void setReplyMemberIconUrl(String replyMemberIconUrl) {
		this.replyMemberIconUrl = replyMemberIconUrl;
	}
	public String getReplyNickname() {
		return replyNickname;
	}
	public void setReplyNickname(String replyNickname) {
		this.replyNickname = replyNickname;
	}

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getGood() {
		return good;
	}
	public void setGood(String good) {
		this.good = good;
	}
	public String getBad() {
		return bad;
	}
	public void setBad(String bad) {
		this.bad = bad;
	}
	
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	public String getIP() {
		return ip;
	}
	public void setIP(String ip) {
		this.ip = ip;
	}
	public String getFType() {
		return fType;
	}
	public void setFType(String fType) {
		this.fType = fType;
	}
	public String getFace() {
		return face;
	}
	public void setFace(String face) {
		this.face = face;
	}
	public String getChecked() {
		return checked;
	}
	public void setChecked(String checked) {
		this.checked = checked;
	}	
	public String getBehaviorType() {
		return behaviorType;
	}
	public void setBehaviorType(String behaviorType) {
		this.behaviorType = behaviorType;
	}
//	public List<AppNewsCommentVO> getReplyList(){
//		return replyList;
//	}
//	public void setReplyList(List<AppNewsCommentVO> replyList ){
//		this.replyList = replyList;
//	}
}


