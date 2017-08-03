package com.fsll.app.investor.me.ws;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.me.service.AppCrmProposalMessService;
import com.fsll.app.investor.me.vo.AppCrmProposalMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioMessVo;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 我的客户的投资方案接口服务
 * @author zpzhou
 * @date 2016-9-22 
 */
@RestController
@RequestMapping("/service/crmProposal")
public class AppCrmProposalMessRest extends WmesBaseRest {

	@Autowired
	private AppCrmProposalMessService crmProposalMessService;

	/**
	 * 得到我的投资方案列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/crmProposal/findMyProposalList.r
	 * {"memberId":"40280a25559b852e01559bb299b7000a","keyWord":"","toCurrency":"HKD"}
	 */
	@RequestMapping(value = "/findMyProposalList")
	@ResponseBody
	public ResultWS findMyProposalList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency","");
		String keyWord = jsonObject.optString("keyWord", "");// 获得查询关键词
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppCrmProposalMessVo> list= crmProposalMessService.findMyProposalList(memberId,null,keyWord,toCurrency,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到投资方案基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/crmProposal/findMyProposalMess.r
	 * {"proposalId":"40280a705aa182a8015aa194f87c000a","langCode":"sc","toCurrency":"HKD" }
	 */
	@RequestMapping(value = "/findMyProposalMess")
	@ResponseBody
	public ResultWS findMyProposalMess(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String proposalId = jsonObject.optString("proposalId", "");// 获得方案ID
		String toCurrency = jsonObject.optString("toCurrency","");
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		if ("".equals(proposalId)||"".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		AppCrmProposalMessVo vo= crmProposalMessService.findMyProposalMess(proposalId,toCurrency,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}
	
	/**
	 * 得到某个方案下的投资组合列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/crmProposal/findProposalPortfolioList.r
	 * {"proposalId":"40280ad456e09b900156e0a3b08f0003" }
	 */
	@RequestMapping(value = "/findProposalPortfolioList")
	@ResponseBody
	public ResultWS findProposalPortfolioList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String proposalId = jsonObject.optString("proposalId", "");// 获得当前登录用户ID
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		if ("".equals(proposalId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		List<AppPortfolioMessVo> list= crmProposalMessService.findProposalPortfolioList(proposalId,toCurrency,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到某个方案下的投资组合列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/crmProposal/updateProposalConfirm.r
	 * {"proposalId":"40280a70592455bb015925919389001f","status":"2","content":"abc" }
	 */
	@RequestMapping(value = "/updateProposalConfirm")
	@ResponseBody
	public ResultWS updateProposalConfirm(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String proposalId = jsonObject.optString("proposalId", "");// 方案ID
		String status = jsonObject.optString("status", "2");// 确认方案，2同意  -1退回
		String content = jsonObject.optString("content", "");// 确认方案填写的内容
		String ip = getRemoteHost(request);// IP地址
		
		if ("".equals(proposalId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		boolean flag = crmProposalMessService.updateProposalConfirm(proposalId,status,content,ip);
		Map<String, Object> resultData = new HashMap<String, Object>();
		resultData.put("flag", flag);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(resultData);
		return result;
	}
	
}
