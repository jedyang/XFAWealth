package com.fsll.wmes.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "web_queue_his")
public class WebQueueHis implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	private String id;
    
    @Column(name = "queue_id")
	private String queueId;
    
    @Column(name = "module_type")
	private String moduleType;
    
    @Column(name = "relate_id")
	private String relateId;
    
    @Column(name = "handle_flag")
	private String handleFlag;
    
    @Column(name = "handle_result")
	private String handleResult;
	
	@Column(name = "handle_time")
	private Date handleTime;

	@Column(name = "create_by")
	private String createBy;
	
	@Column(name = "create_time")
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getModuleType() {
		return moduleType;
	}

	public void setModuleType(String moduleType) {
		this.moduleType = moduleType;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) {
		this.relateId = relateId;
	}

	public String getHandleFlag() {
		return handleFlag;
	}

	public void setHandleFlag(String handleFlag) {
		this.handleFlag = handleFlag;
	}

	public String getHandleResult() {
		return handleResult;
	}

	public void setHandleResult(String handleResult) {
		this.handleResult = handleResult;
	}

	public Date getHandleTime() {
		return handleTime;
	}

	public void setHandleTime(Date handleTime) {
		this.handleTime = handleTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getQueueId() {
		return queueId;
	}

	public void setQueueId(String queueId) {
		this.queueId = queueId;
	}
	
}