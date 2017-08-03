package com.fsll.wmes.web.service;

import com.fsll.wmes.entity.MemberBase;
import com.fsll.wmes.entity.WebInvestorVisit;

/**
 * @author Yan
 * @date 2016-01-07
 * @param log
 * @return
 */
public interface WebInvestorVisitService {
	
	/**
	 * 保存访问记录
	 */
	public WebInvestorVisit addLog(WebInvestorVisit log);
	
	/**
	 * 根据条件获取访问记录
	 * @author mqzou 2017-02-06
	 * @param visit
	 * @return
	 */
	public WebInvestorVisit findInvestorVisit(String moduleType,String relateId,String memberId);
	
	/**
	 * 保存访问记录
	 * @author mqzou 2017-02-06
	 * @param moduleType
	 * @param relateId
	 * @param memberId
	 * @return
	 */
	public WebInvestorVisit checkAndSaveVisit(String moduleType,String relateId, MemberBase member);
}
