package com.fsll.wmes.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
////@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "ifafirm_team_ifa")
public class IfafirmTeamIfa implements java.io.Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator = "paymentableGenerator")        
    @GenericGenerator(name = "paymentableGenerator", strategy = "uuid.hex")
    private String id;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifafirm_id")
	@JsonIgnore
	private MemberIfafirm ifafirm;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "team_id")
	@JsonIgnore
	private IfafirmTeam team;
    
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ifa_id")
	@JsonIgnore
	private MemberIfa ifa;
    
    @Column(name = "is_supervisor")
	private String isSupervisor;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MemberIfafirm getIfafirm() {
		return ifafirm;
	}

	public void setIfafirm(MemberIfafirm ifafirm) {
		this.ifafirm = ifafirm;
	}

	public IfafirmTeam getTeam() {
		return team;
	}

	public void setTeam(IfafirmTeam team) {
		this.team = team;
	}

	public MemberIfa getIfa() {
		return ifa;
	}

	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}

	public String getIsSupervisor() {
		return isSupervisor;
	}

	public void setIsSupervisor(String isSupervisor) {
		this.isSupervisor = isSupervisor;
	}
	
}

