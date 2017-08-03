package com.fsll.wmes.web.vo;

import javax.persistence.Column;

import com.fsll.wmes.entity.MemberDistributor;

public class MemberDistributorExtend extends MemberDistributor {
	
	private String incorporationPlaceName;
	
	private String distributorLogo;
	
	private String IfafirmDistributorid;
	
	
	
	public String getIfafirmDistributorid() {
		return IfafirmDistributorid;
	}

	public void setIfafirmDistributorid(String ifafirmDistributorid) {
		IfafirmDistributorid = ifafirmDistributorid;
	}

	public String getIncorporationPlaceName() {
		return incorporationPlaceName;
	}

	public void setincorporationPlaceName(String incorporationPlaceName) {
		this.incorporationPlaceName = incorporationPlaceName;
	}

	public String getDistributorLogo() {
		return distributorLogo;
	}

	public void setDistributorLogo(String distributorLogo) {
		this.distributorLogo = distributorLogo;
	}
}
