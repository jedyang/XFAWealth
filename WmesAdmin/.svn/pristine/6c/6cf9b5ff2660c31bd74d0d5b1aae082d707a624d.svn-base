package com.fsll.wmes.entity;

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
@Table(name = "rpq_quest_item")
public class RpqQuestItem implements java.io.Serializable {
	private String id;
	private RpqQuest quest;
	private String type;
	@Transient
	private String title;
	private Integer scoreValue;
	private Integer orderBy;
	@Transient
	private String remark;

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quest_id")
	@JsonIgnore
	public RpqQuest getQuest() {
		return quest;
	}

	public void setQuest(RpqQuest quest) {
		this.quest = quest;
	}

	@Column(name = "type")
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	@Transient
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "score_value")
	public Integer getScoreValue() {
		return this.scoreValue;
	}

	public void setScoreValue(Integer scoreValue) {
		this.scoreValue = scoreValue;
	}

	@Column(name = "order_by")
	public Integer getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	
	@Transient
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}