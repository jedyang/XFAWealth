package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE) 
@Table(name = "investor_doc_sc")
public class InvestorDocSc implements java.io.Serializable {
	private String id;
	private String docName;
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
	@Column(name = "doc_name")
	public String getDocName() {
		return this.docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}
}