package com.fsll.wmes.community.vo;

import java.util.List;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.NewsInfo;

/**
 * @author wwlin
 *	首页贴子列表的每个贴子的显示实体
 */
public class FrontCommunityTopicVO {
	//贴子ID
	private String id;
	//贴子标题
	private String title;
	//贴子内容
	private String content;
	//所属板块ID
	private String sectionId;
	//所属板块名称
	private String sectionName;
	//创建时间格式化
	private String publishTimeFormat;
	//发布人头像
	private String userHeadUrl;
	//发布人ID
	private String memberId;
	//发布人类型
	private String memberType;
	//发布人昵称
	private String nickName;
	//阅读次数
	private String readCount;
	//评论次数
	private String commentCount;
	//点赞次数
	private String likeCount;
	//扔鸡蛋次数
	private String unlikeCount;
	//查看的人是否点赞了
	private int isLike;
	//查看的人是否鸡蛋了
	private int isUnlike;
	//查看的人是否收藏了
	private int isFavourite;
	
	private String isShare;//是否来自转载/分享
	private String sourceType;//装载/分享来源
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getPublishTimeFormat() {
		return publishTimeFormat;
	}
	public void setPublishTimeFormat(String publishTimeFormat) {
		this.publishTimeFormat = publishTimeFormat;
	}
	
	public String getUserHeadUrl() {
		return userHeadUrl;
	}
	public void setUserHeadUrl(String userHeadUrl) {
		this.userHeadUrl = userHeadUrl;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getReadCount() {
		return readCount;
	}
	public void setReadCount(String readCount) {
		this.readCount = readCount;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}
	public String getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(String likeCount) {
		this.likeCount = likeCount;
	}
	public String getUnlikeCount() {
		return unlikeCount;
	}
	public void setUnlikeCount(String unlikeCount) {
		this.unlikeCount = unlikeCount;
	}
	public String getMemberType() {
		return memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}
	public int getIsLike() {
		return isLike;
	}
	public void setIsLike(int isLike) {
		this.isLike = isLike;
	}
	public int getIsUnlike() {
		return isUnlike;
	}
	public void setIsUnlike(int isUnlike) {
		this.isUnlike = isUnlike;
	}
	public int getIsFavourite() {
		return isFavourite;
	}
	public void setIsFavourite(int isFavourite) {
		this.isFavourite = isFavourite;
	}
	public String getIsShare() {
		return isShare;
	}
	public void setIsShare(String isShare) {
		this.isShare = isShare;
	}
	public String getSourceType() {
		return sourceType;
	}
	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}
	
	
	
}

