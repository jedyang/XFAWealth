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
@Table(name = "top_info_category")
public class TopInfoCategory implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "category_id")
	@JsonIgnore
	private TopCategory category;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	@JsonIgnore
	private TopInfo topInfo;
	
    @Column(name = "is_main")
	private String isMain;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public TopCategory getCategory() {
		return category;
	}

	public void setCategory(TopCategory category) {
		this.category = category;
	}

	public TopInfo getTopInfo() {
		return topInfo;
	}

	public void setTopInfo(TopInfo topInfo) {
		this.topInfo = topInfo;
	}

	public String getIsMain() {
		return isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}


}