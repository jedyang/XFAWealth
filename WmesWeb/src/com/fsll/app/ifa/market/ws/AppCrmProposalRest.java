package com.fsll.app.ifa.market.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.ifa.market.service.AppCrmProposalService;
import com.fsll.app.ifa.market.vo.AppCrmProposalVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 日程接口
 * @author zxtan
 * @date 2017-03-23
 */
@RestController
@RequestMapping("/service/ifa/market/proposal")
public class AppCrmProposalRest extends WmesBaseRest {

	@Autowired
	private AppCrmProposalService proposalService;
	
	
	/**
	 * 获取交易记录
	 *@author zxtan
	 *@date 2017-03-29
	 * 调用示例:[地址]/service/ifa/market/proposal/findMyProposalList.r
	 * {"page":1, "rows":10,"memberId":"40280ad455e327690155e3291c582000","keyword":"","status":"","minAmount":"","maxAmount":""}
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
		Integer page = jsonObject.optInt("page", 1);// 获得当前页数
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数isRead
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
//		String clientMemberId = jsonObject.optString("clientMemberId", "");// 获得当前登录用户ID
		String keyword = jsonObject.optString("keyword", "");// 搜索关键字
		String status = jsonObject.optString("status", "");// 状态
		String minAmount = jsonObject.optString("minAmount", "");// 初始投资额查询下限
		String maxAmount = jsonObject.optString("maxAmount", "");// 初始投资额查询上限
		String langCode = jsonObject.optString("langCode",CommonConstants.DEF_LANG_CODE);
		String toCurrency = jsonObject.optString("toCurrency","");
		
		if ( "".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = proposalService.findMyProposalList(jsonPaging,memberId,keyword, status, minAmount, maxAmount,toCurrency,langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(jsonPaging.getList());
		result.setCurPage(jsonPaging.getPage());
		result.setPageSize(jsonPaging.getRows());
		result.setTotal(jsonPaging.getTotal());
		return result;
	}
	
	/**
	 * 得到投资方案基本信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/proposal/findProposal.r
	 * {"proposalId":"40280a705aa182a8015aa194f87c000a","langCode":"sc","toCurrency":"HKD" }
	 */
	@RequestMapping(value = "/findProposal")
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
		AppCrmProposalVO vo= proposalService.findProposal(proposalId,toCurrency,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(vo);
		return result;
	}

}
