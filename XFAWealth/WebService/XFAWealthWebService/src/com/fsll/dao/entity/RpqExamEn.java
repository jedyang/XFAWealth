package com.fsll.dao.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "rpq_exam_en")
public class RpqExamEn implements java.io.Serializable {
	private String id;
	private String title;
	private String remark;
	
	private String riskResult;
	private String riskRemark;
	private String userRiskResult;
	private String userRiskRemark;
	
	//id,title,remark,risk_result,risk_remark,user_risk_result,user_risk_remark

    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "assigned")
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "remark")
	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	@Column(name = "risk_result")
	public String getRiskResult() {
		return riskResult;
	}

	public void setRiskResult(String riskResult) {
		this.riskResult = riskResult;
	}

	@Column(name = "risk_remark")
	public String getRiskRemark() {
		return riskRemark;
	}

	public void setRiskRemark(String riskRemark) {
		this.riskRemark = riskRemark;
	}

	@Column(name = "user_risk_result")
	public String getUserRiskResult() {
		return userRiskResult;
	}

	public void setUserRiskResult(String userRiskResult) {
		this.userRiskResult = userRiskResult;
	}

	@Column(name = "user_risk_remark")
	public String getUserRiskRemark() {
		return userRiskRemark;
	}

	public void setUserRiskRemark(String userRiskRemark) {
		this.userRiskRemark = userRiskRemark;
	}
	
}