package com.fsll.app.investor.me.vo;

/**
 * 帖子观点实体类VO
 * @author zxtan
 * @date 2017-03-20
 */
public class AppCommunityTopicItemVO {
	private String topicId;//话题 Id
	private String topicTitle;//话题标题
	private String section;//版块
	private String createTime;//创建时间
	private String isRecommand;//是否推荐，1是，0否
	private String recommandReason;//推荐理由
	private String commentCount;//评论数
	private String readCount;//阅读数
	private String likeCount;//点赞数
	private String unlikeCount;//踩的数
	private String transferCount;//转发次数
	private String favoriteCount;//收藏次数
	private String suportComment;//是否支持评论，1是，0否
	private String content;//内容
	
	
	public String getTopicId() {
		return topicId;
	}
	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	public String getTopicTitle() {
		return topicTitle;
	}
	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		this.section = section;
	}	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}	
	public String getIsRecommand() {
		return isRecommand;
	}
	public void setIsRecommand(String isRecommand) {
		this.isRecommand = isRecommand;
	}	
	public String getRecommandReason() {
		return recommandReason;
	}
	public void setRecommandReason(String recommandReason) {
		this.recommandReason = recommandReason;
	}
	public String getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}	
	public String getReadCount() {
		return readCount;
	}
	public void setReadCount(String readCount) {
		this.readCount = readCount;
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
	public String getTransferCount() {
		return transferCount;
	}
	public void setTransferCount(String transferCount) {
		this.transferCount = transferCount;
	}
	public String getFavoriteCount() {
		return favoriteCount;
	}
	public void setFavoriteCount(String favoriteCount) {
		this.favoriteCount = favoriteCount;
	}
	public String getSuportComment() {
		return suportComment;
	}
	public void setSuportComment(String suportComment) {
		this.suportComment = suportComment;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
