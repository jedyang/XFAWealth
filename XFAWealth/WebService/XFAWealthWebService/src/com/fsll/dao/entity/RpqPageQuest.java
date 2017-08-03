package com.fsll.dao.entity;

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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "rpq_page_quest")
public class RpqPageQuest implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "page_id")
	@JsonIgnore
	private RpqPage page;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quest_id")
	@JsonIgnore
	private RpqQuest quest;
	
	@Column(name = "order_by")
	private Integer orderBy;
	
	@Transient
	private String title;
	@Transient
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RpqPage getPage() {
		return page;
	}

	public void setPage(RpqPage page) {
		this.page = page;
	}

	public RpqQuest getQuest() {
		return quest;
	}

	public void setQuest(RpqQuest quest) {
		this.quest = quest;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}