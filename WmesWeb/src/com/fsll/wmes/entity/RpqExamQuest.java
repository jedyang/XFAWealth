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
@Table(name = "rpq_exam_quest")
public class RpqExamQuest implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "exam_id")
	@JsonIgnore
	private RpqExam exam;
	
	@Column(name = "quest_type")
	private String questType;
	
	@Column(name = "score_value")
	private Integer scoreValue;
	
	@Transient
	private String title;
	@Transient
	private String remark;
	
	@Column(name = "order_by")
	private Integer orderBy;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RpqExam getExam() {
		return exam;
	}

	public void setExam(RpqExam exam) {
		this.exam = exam;
	}

	public Integer getScoreValue() {
		return scoreValue;
	}

	public void setScoreValue(Integer scoreValue) {
		this.scoreValue = scoreValue;
	}
	@Transient
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Transient
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}
	
	public String getQuestType() {
		return questType;
	}

	public void setQuestType(String questType) {
		this.questType = questType;
	}
}