package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "wf_procedure_info")
public class WfProcedureInfo implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
	private String id;
    
    @Column(name = "procedure_name")
	private String procedureName;
    
    @Column(name = "invok_code")
	private String invokCode;
    
    @Column(name = "approval_type")
	private Integer approvalType;
    
    @Column(name = "is_del")
	private String isDel;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcedureName() {
		return procedureName;
	}

	public void setProcedureName(String procedureName) {
		this.procedureName = procedureName;
	}

	public String getInvokCode() {
		return invokCode;
	}

	public void setInvokCode(String invokCode) {
		this.invokCode = invokCode;
	}

	public Integer getApprovalType() {
		return approvalType;
	}

	public void setApprovalType(Integer approvalType) {
		this.approvalType = approvalType;
	}

	public String getIsDel() {
		return isDel;
	}

	public void setIsDel(String isDel) {
		this.isDel = isDel;
	}
}