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

import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "live_info")
public class LiveInfo implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;

	@Column(name = "title")
    private String title;

	@Column(name = "content")
    private String content;

	@Column(name = "time_length")
	private Integer timeLength;
	
	@Column(name = "overhead_by")
    private Integer overheadBy;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "author_id")
	private MemberBase author;
	
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "issued_time")
	private Date issuedTime;
	
	@Column(name = "create_time")
	private Date createTime;
	
	@Column(name = "last_update")
	private Date lastUpdate;
	
	@Column(name = "is_valid")
	private String isValid;

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

	public Integer getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(Integer timeLength) {
		this.timeLength = timeLength;
	}

	public Integer getOverheadBy() {
		return overheadBy;
	}

	public void setOverheadBy(Integer overheadBy) {
		this.overheadBy = overheadBy;
	}

	public MemberBase getAuthor() {
		return author;
	}

	public void setAuthor(MemberBase author) {
		this.author = author;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getIssuedTime() {
		return issuedTime;
	}

	public void setIssuedTime(Date issuedTime) {
		this.issuedTime = issuedTime;
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

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}
	
	
	
}
