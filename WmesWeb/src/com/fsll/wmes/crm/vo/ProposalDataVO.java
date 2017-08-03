package com.fsll.wmes.crm.vo;

import java.util.ArrayList;
import java.util.List;

import com.fsll.wmes.entity.CrmProposal;
import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIfa;
import com.fsll.wmes.entity.PortfolioInfo;
import com.fsll.wmes.entity.PortfolioInfoAip;

/**
 * proposal 相关所有信息，PDF页面展示
 *
 */
public class ProposalDataVO {
	
	private CrmProposal proposal;
	private PortfolioInfo portfolio;
	private PortfolioInfoAip aip;
	private MemberBase client;
	private MemberIfa ifa;
	//产品信息，包含备选产品信息
	private List<PortfolioInfoProductVO> products = new ArrayList<PortfolioInfoProductVO>();
	
	public CrmProposal getProposal() {
		return proposal;
	}
	public void setProposal(CrmProposal proposal) {
		this.proposal = proposal;
	}
	public PortfolioInfo getPortfolio() {
		return portfolio;
	}
	public void setPortfolio(PortfolioInfo portfolio) {
		this.portfolio = portfolio;
	}
	public PortfolioInfoAip getAip() {
		return aip;
	}
	public void setAip(PortfolioInfoAip aip) {
		this.aip = aip;
	}
	public MemberBase getClient() {
		return client;
	}
	public void setClient(MemberBase client) {
		this.client = client;
	}
	public MemberIfa getIfa() {
		return ifa;
	}
	public void setIfa(MemberIfa ifa) {
		this.ifa = ifa;
	}
	public List<PortfolioInfoProductVO> getProducts() {
		return products;
	}
	public void setProducts(List<PortfolioInfoProductVO> products) {
		this.products = products;
	}
	
	
}
