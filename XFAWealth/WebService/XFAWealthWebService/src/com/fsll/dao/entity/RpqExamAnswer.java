package com.fsll.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "rpq_exam_answer")
public class RpqExamAnswer implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "exam_id")
	@JsonIgnore
	private RpqExam exam;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "quest_id")
	@JsonIgnore
	private RpqExamQuest quest;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "item_id")
	@JsonIgnore
	private RpqExamQuestItem item;
	
	@Column(name = "item_value")
	private String itemValue;

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

	public RpqExamQuest getQuest() {
		return quest;
	}

	public void setQuest(RpqExamQuest quest) {
		this.quest = quest;
	}

	public RpqExamQuestItem getItem() {
		return item;
	}

	public void setItem(RpqExamQuestItem item) {
		this.item = item;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

}