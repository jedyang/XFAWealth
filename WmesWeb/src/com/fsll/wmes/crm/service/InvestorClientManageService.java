package com.fsll.wmes.crm.service;

import com.fsll.common.util.JsonPaging;
import com.fsll.wmes.investor.vo.AccountVO;

public interface InvestorClientManageService {

	/**
	 * 代理商客户列表json
	 * @date 20161223
	 * @author scshi_u330p
	 * */
	public JsonPaging findAccountList(JsonPaging jsonPaging, AccountVO newVo,String langCode);
	
//	/**
//	 * ifa客户列表
//	 * @author scshi_u330p
//	 * @date 20161223
//	 * */
//	public JsonPaging findIfaAccountList(JsonPaging jsonPaging,AccountVO newVo, String langCode);

}
