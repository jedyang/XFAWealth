package com.fsll.dao.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "insight_info")
public class InsightInfo implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
    @Column(name = "geo_allocation")
	private String geoAllocation;
    
    @Column(name = "sector")
	private String sector;
	
    @Column(name = "title")
	private String title;
    
    @Column(name = "channel")
	private String channel;
    
    @Column(name = "keyword")
	private String keyword;
    
    @Column(name = "thumbnail")
	private String thumbnail;
    
    @Column(name = "thumbnail_src")
	private String thumbnailSrc; // '略缩图来源，1：用户指定；2：自动取文章第一个配图'
	
	@Column(name = "content")
	private String content;

	@Column(name = "click")
	private Integer click;
	
	@Column(name = "up_counter")
	private Integer upCounter;
	
	@Column(name = "down_counter")
	private Integer downCounter;
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "creator_id")
	private MemberBase creator;
	
	@Column(name = "create_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	
	@Column(name = "last_update")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date lastUpdate;	

	@Column(name = "overhead")
	private Integer overhead;	

	@Column(name = "overhead_time")
	private Date overheadTime;

	@Column(name = "status")
	private Integer status;	

	@Column(name = "pub_date")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date pubDate;
	
	@Column(name = "reposted")
	private String reposted;
	
	@Column(name = "relate_id")
	private String relateId;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	private MemberBase author;
	
	@Column(name = "reposted_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date repostedTime;
	
	/**
	 * 以下属性只用作页面显示
	 * */
	@Transient
	private String thumbType;//缩略图来源 1.正文首张图片（如果有），0.自定义
	@Transient
	private String createTimeStr;
	@Transient
	private String overheadTimeStr;
	@Transient
	private String pubDateStr;
	@Transient
	private String repostedTimeStr;
	@Transient
	private Integer frendsCount;
	@Transient
	private InsightCount insightCount;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGeoAllocation() {
		return geoAllocation;
	}

	public void setGeoAllocation(String geoAllocation) {
		this.geoAllocation = geoAllocation;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getClick() {
		return click;
	}

	public void setClick(Integer click) {
		this.click = click;
	}

	public Integer getUpCounter() {
		return upCounter;
	}

	public void setUpCounter(Integer upCounter) {
		this.upCounter = upCounter;
	}

	public Integer getDownCounter() {
		return downCounter;
	}

	public void setDownCounter(Integer downCounter) {
		this.downCounter = downCounter;
	}

	public MemberBase getCreator() {
		return creator;
	}

	public void setCreator(MemberBase creator) {
		this.creator = creator;
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

	public Integer getOverhead() {
		return overhead;
	}

	public void setOverhead(Integer overhead) {
		this.overhead = overhead;
	}

	public Date getOverheadTime() {
		return overheadTime;
	}

	public void setOverheadTime(Date overheadTime) {
		this.overheadTime = overheadTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public String getReposted() {
		return reposted;
	}

	public void setReposted(String reposted) {
		this.reposted = reposted;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public MemberBase getAuthor() {
		return author;
	}

	public void setAuthor(MemberBase author) {
		this.author = author;
	}

	public Date getRepostedTime() {
		return repostedTime;
	}

	public void setRepostedTime(Date repostedTime) {
		this.repostedTime = repostedTime;
	}

	public String getThumbType() {
		return thumbType;
	}

	public void setThumbType(String thumbType) {
		this.thumbType = thumbType;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getOverheadTimeStr() {
		return overheadTimeStr;
	}

	public void setOverheadTimeStr(String overheadTimeStr) {
		this.overheadTimeStr = overheadTimeStr;
	}

	public String getPubDateStr() {
		return pubDateStr;
	}

	public void setPubDateStr(String pubDateStr) {
		this.pubDateStr = pubDateStr;
	}

	public String getRepostedTimeStr() {
		return repostedTimeStr;
	}

	public void setRepostedTimeStr(String repostedTimeStr) {
		this.repostedTimeStr = repostedTimeStr;
	}

	public Integer getFrendsCount() {
		return frendsCount;
	}

	public void setFrendsCount(Integer frendsCount) {
		this.frendsCount = frendsCount;
	}

	public InsightCount getInsightCount() {
		return insightCount;
	}

	public void setInsightCount(InsightCount insightCount) {
		this.insightCount = insightCount;
	}

	public String getThumbnailSrc() {
		return thumbnailSrc;
	}

	public void setThumbnailSrc(String thumbnailSrc) {
		this.thumbnailSrc = thumbnailSrc;
	}
	
	
}