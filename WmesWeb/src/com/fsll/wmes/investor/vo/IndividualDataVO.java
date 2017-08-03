package com.fsll.wmes.investor.vo;

import java.util.Date;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.MemberIndividual;

/**
 * Individual完整用户数据
 * @author michael
 * 2016-07-28
 */
public class IndividualDataVO {
 
	private MemberBase base;
	private MemberIndividual individual;
	
	public void setBase(MemberBase base) {
		this.base = base;
	}
	public MemberBase getBase() {
		return base;
	}
	
	public void setIndividual(MemberIndividual individual) {
		this.individual = individual;
	}
	public MemberIndividual getIndividual() {
		return individual;
	}
	
	
}
