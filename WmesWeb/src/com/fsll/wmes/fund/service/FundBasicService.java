package com.fsll.wmes.fund.service;

import javax.servlet.http.HttpServletRequest;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.wmes.fund.vo.FundBaseVo;

import net.sf.json.JSONObject;

public interface FundBasicService {
	
	public JsonPaging fundList( JSONObject jsonObject );
	
	public JsonPaging fundBriefInfo( JSONObject jsonObject );
	
	
}
