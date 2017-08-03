package com.fsll.wmes.news.vo;

import java.util.List;

import com.fsll.common.util.DateUtil;
import com.fsll.wmes.entity.NewsComment;


public class NewsCommentVO {

	private String id;
	private String infoId;
	private String memberId;
	private String memberName;
	private String memberUrl;
	private String comment;
	private String replyMemberName;//被回复的人的名
	private int  good;
	private int bad;
	private String opTime;
	private int replyCount;
	private String replyId;
	private List<NewsCommentVO> replylist;
	private boolean hasAppend=false;
	private String isLike;
	private String isUnlike;
	public NewsCommentVO () {
		
	}
	
	public NewsCommentVO(NewsComment commentVO){
		this.id=commentVO.getId();
		this.bad=null!=commentVO.getBad()?commentVO.getBad():0;
		this.good=null!=commentVO.getGood()?commentVO.getGood():0;
		this.comment=null!=commentVO.getComment()?commentVO.getComment():"";
		this.infoId=commentVO.getNewsInfo().getId();
		this.memberId=commentVO.getMember().getId();
		this.memberName=commentVO.getMember().getNickName();
		this.memberUrl=commentVO.getMember().getIconUrl();
		this.opTime=DateUtil.dateToDateString(commentVO.getOpTime(), DateUtil.DEFAULT_DATE_TIME_FORMAT);
		this.replyId=null!=commentVO.getReplyComment()?commentVO.getReplyComment().getId():"";
		this.replyMemberName=null!=commentVO.getReplyComment()?commentVO.getReplyComment().getMember().getNickName():"";
		this.replyCount=null!=commentVO.getTotalPost()?commentVO.getTotalPost():0;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInfoId() {
		return infoId;
	}
	public void setInfoId(String infoId) {
		this.infoId = infoId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getReplyMemberName() {
		return replyMemberName;
	}
	public void setReplyMemberName(String replyMemberName) {
		this.replyMemberName = replyMemberName;
	}
	public int getGood() {
		return good;
	}
	public void setGood(int good) {
		this.good = good;
	}
	public int getBad() {
		return bad;
	}
	public void setBad(int bad) {
		this.bad = bad;
	}
	public String getOpTime() {
		return opTime;
	}
	public void setOpTime(String opTime) {
		this.opTime = opTime;
	}
	
	public List<NewsCommentVO> getReplylist() {
		return replylist;
	}
	public void setReplylist(List<NewsCommentVO> replylist) {
		this.replylist = replylist;
	}
	public String getMemberUrl() {
		return memberUrl;
	}
	public void setMemberUrl(String memberUrl) {
		this.memberUrl = memberUrl;
	}
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	public String getReplyId() {
		return replyId;
	}
	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public boolean isHasAppend() {
		return hasAppend;
	}

	public void setHasAppend(boolean hasAppend) {
		this.hasAppend = hasAppend;
	}

	public String getIsLike() {
		return isLike;
	}

	public void setIsLike(String isLike) {
		this.isLike = isLike;
	}

	public String getIsUnlike() {
		return isUnlike;
	}

	public void setIsUnlike(String isUnlike) {
		this.isUnlike = isUnlike;
	}
	
	
	
}
