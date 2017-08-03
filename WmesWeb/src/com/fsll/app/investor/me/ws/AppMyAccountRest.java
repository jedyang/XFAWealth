package com.fsll.app.investor.me.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.me.service.AppMyAccountService;
import com.fsll.app.investor.me.vo.AppMyAccountBaseInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountDocInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountRpqInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountVO;
import com.fsll.app.investor.me.vo.AppPortfolioMarketMessVo;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.app.investor.me.vo.AppRpqExamQuestVO;
import com.fsll.app.investor.me.vo.AppRpqExamResultVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 我的账户接口服务
 * @author zxtan
 * @date 2017-01-12
 */
@RestController
@RequestMapping("/service/myaccount")
public class AppMyAccountRest extends WmesBaseRest {
	@Autowired
	private AppMyAccountService myAccountService;
	
	/**
	 * 得到我的账户列表数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/findMyAccountList.r
	 * {"memberId":"40280a25559b852e01559b89c80f0004","toCurrency":"","langCode":"sc","openStatus":"" }
	 */
	@RequestMapping(value = "/findMyAccountList")
	@ResponseBody
	public ResultWS findMyAccountList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String memberId = jsonObject.optString("memberId", "");// 获得MemberBase ID
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得toCurrency
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		String openStatus = jsonObject.optString("openStatus", "");// 获得openStatus
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppMyAccountVO> list= myAccountService.findMyAccountList(memberId, langCode, toCurrency,openStatus);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到我的账户详情数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/findAccountInfo.r
	 * {"accountId":"40280a25559b852e01559b89c80f0004","toCurrency":"HKD","langCode":"sc" }
	 */
	@RequestMapping(value = "/findAccountInfo")
	@ResponseBody
	public ResultWS findAccountInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String accountId = jsonObject.optString("accountId", "");// 获得MemberBase ID
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得MemberBase ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		if ("".equals(accountId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppMyAccountInfoVO info = myAccountService.findAccountInfo(accountId, langCode, toCurrency);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/findAccountOrderHistoryList.r
	 * {"accountId":"40280a25559b852e01559b89c80f0004","toCurrency":"HKD","langCode":"sc" }
	 */
	@RequestMapping(value = "/findAccountOrderHistoryList")
	@ResponseBody
	public ResultWS findAccountOrderHistoryList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String accountId = jsonObject.optString("accountId", "");// 获得MemberBase ID
		String toCurrency = jsonObject.optString("toCurrency", "");// 获得MemberBase ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		if ("".equals(accountId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppPortfolioOrderHistoryVO> list = myAccountService.findAccountOrderHistoryList(accountId, toCurrency, langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/findAccountRPQInfo.r
	 * {"accountId":"40280a2e5980d5f2015981405b44007a","langCode":"sc" }
	 */
	@RequestMapping(value = "/findAccountRPQInfo")
	@ResponseBody
	public ResultWS findAccountRPQInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String accountId = jsonObject.optString("accountId", "");// 获得accountId
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		if ("".equals(accountId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppMyAccountRpqInfoVO rpq = myAccountService.findAccountRPQInfo(accountId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(rpq);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/findAccountDocCheckInfo.r
	 * {"accountId":"40280a2e5980d5f2015981405b44007a","langCode":"sc" }
	 */
	@RequestMapping(value = "/findAccountDocCheckInfo")
	@ResponseBody
	public ResultWS findAccountDocCheckInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String accountId = jsonObject.optString("accountId", "");// 获得accountId
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		if ("".equals(accountId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppMyAccountDocInfoVO> list = myAccountService.findAccountDocCheckInfo(accountId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/findAccountBaseInfo.r
	 * {"accountId":"40280a2e5980d5f2015981405b44007a","langCode":"sc" }
	 */
	@RequestMapping(value = "/findAccountBaseInfo")
	@ResponseBody
	public ResultWS findAccountBaseInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String accountId = jsonObject.optString("accountId", "");// 获得accountId
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		if ("".equals(accountId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppMyAccountBaseInfoVO info = myAccountService.findAccountBaseInfo(accountId, langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/createRpqExamQuest.r
	 * {"accountId":"40280a2e5980d5f2015981405b44007a","langCode":"sc" }
	 */
	@RequestMapping(value = "/createRpqExamQuest")
	@ResponseBody
	public ResultWS createRpqExamQuest(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String accountId = jsonObject.optString("accountId", "");// 获得accountId
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		if ("".equals(accountId)|| "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppRpqExamQuestVO> list = myAccountService.createRpqExamQuest(accountId, langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/myaccount/saveRpqExamAnswer.r
	 * {"langCode":"sc","answer":[{"questId":"q001","itemId":"q001ic"},{"questId":"q002","itemId":"q002ia"}] }
	 */
	@RequestMapping(value = "/saveRpqExamAnswer")
	@ResponseBody
	public ResultWS saveRpqExamAnswer(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		JSONArray answerArray = jsonObject.getJSONArray("answer");
		if (answerArray.isEmpty()|| "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppRpqExamResultVO resultVO = myAccountService.saveRpqExamAnswer(answerArray, langCode);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(resultVO);
		return result;
	}
}
