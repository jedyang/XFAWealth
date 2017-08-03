package com.fsll.wmes.es.model;

import java.util.Date;

//@Document(indexName = "discover_topic", type = "discover_topic", shards = 1, replicas = 0, refreshInterval = "-1")
public class EsDiscoverTopic {
	//@Id
	private String id;
	
	//@Field
	private String typeId;
	
    //@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String author;
    
	//@Field
	private Long replyCount;
    
	//@Field
	private Long clickCoount;
	
	//@Field
	private Long orderBy;
	
	//@Field
	private String isRecommend;
	
	//@Field
	private String isTop;
    
	//@Field(type = FieldType.String, searchAnalyzer="ik", store = true)
	private String topic;
	
    //@Field(type = FieldType.String, searchAnalyzer="ik", store = true)
    private String content;

    //@Field(type = FieldType.String, index = FieldIndex.not_analyzed, store = true)
    private String lastReply;
    
	//@Field
	private Date lastReplyTime;
	
	//@Field
	private Date createTime;
	
	//@Field
	private Date lastUpdate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Long getReplyCount() {
		return replyCount;
	}

	public void setReplyCount(Long replyCount) {
		this.replyCount = replyCount;
	}

	public Long getClickCoount() {
		return clickCoount;
	}

	public void setClickCoount(Long clickCoount) {
		this.clickCoount = clickCoount;
	}

	public Long getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}

	public String getIsTop() {
		return isTop;
	}

	public void setIsTop(String isTop) {
		this.isTop = isTop;
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

	public String getLastReply() {
		return lastReply;
	}

	public void setLastReply(String lastReply) {
		this.lastReply = lastReply;
	}

	public Date getLastReplyTime() {
		return lastReplyTime;
	}

	public void setLastReplyTime(Date lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
}
