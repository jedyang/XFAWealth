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

@Entity
@Table(name = "live_info_category")
public class LiveInfoCategory implements java.io.Serializable {
	private String id;
	private LiveCategory category;
	private LiveInfo live;
	private String isMain;
	
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
	@JoinColumn(name = "category_id")
	public LiveCategory getCategory() {
		return category;
	}

	public void setCategory(LiveCategory category) {
		this.category = category;
	}

    @ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "info_id")
	public LiveInfo getLive() {
		return live;
	}

	public void setLive(LiveInfo live) {
		this.live = live;
	}

	@Column(name = "is_main")
	public String getIsMain() {
		return this.isMain;
	}

	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}

}