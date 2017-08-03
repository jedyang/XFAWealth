package com.fsll.dao.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ifafirm_team")
public class IfafirmTeam implements java.io.Serializable {
	@Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "parent_id")
	@NotFound(action=NotFoundAction.IGNORE) 
	private IfafirmTeam parent;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifafirm_id")
	private MemberIfafirm ifafirm;
    
    @Column(name = "code")
	private String code;
    
    @Column(name = "name")
	private String name;
    
    @Column(name = "order_by")
	private Integer orderBy;
    
    @Column(name = "reamrk")
	private String reamrk;
    
    @Column(name = "class_layer")
	private Integer classLayer;
    
    @Column(name = "is_valid")
	private String isValid;

	@OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.EAGER, mappedBy = "parent")
	////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
	@OrderBy("orderBy asc")
	@JsonIgnore
	private Set<IfafirmTeam> childSet = new HashSet<IfafirmTeam>(0);
	
	@Transient
	private String parentId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public IfafirmTeam getParent() {
		return parent;
	}

	public void setParent(IfafirmTeam parent) {
		this.parent = parent;
	}

	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}

	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(Integer orderBy) {
		this.orderBy = orderBy;
	}

	public String getReamrk() {
		return reamrk;
	}

	public void setReamrk(String reamrk) {
		this.reamrk = reamrk;
	}

	public Integer getClassLayer() {
		return classLayer;
	}

	public void setClassLayer(Integer classLayer) {
		this.classLayer = classLayer;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public Set<IfafirmTeam> getChildSet() {
		return childSet;
	}

	public void setChildSet(Set<IfafirmTeam> childSet) {
		this.childSet = childSet;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	
}
