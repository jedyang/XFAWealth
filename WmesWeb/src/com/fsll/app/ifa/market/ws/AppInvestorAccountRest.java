package com.fsll.app.ifa.market.ws;

import java.util.Date;
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

import com.fsll.app.ifa.market.service.AppInvestorAccountService;
import com.fsll.app.ifa.market.service.AppStrategyInfoService;
import com.fsll.app.ifa.market.vo.AppAccountBaseInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountDocInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountInfoVO;
import com.fsll.app.ifa.market.vo.AppAccountRpqInfoVO;
import com.fsll.app.ifa.market.vo.AppOrderHistoryVO;
import com.fsll.app.ifa.schedule.service.AppScheduleService;
import com.fsll.app.ifa.schedule.vo.AppScheduleGroupVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleItemVO;
import com.fsll.app.investor.me.vo.AppMyAccountBaseInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountDocInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountInfoVO;
import com.fsll.app.investor.me.vo.AppMyAccountRpqInfoVO;
import com.fsll.app.investor.me.vo.AppPortfolioOrderHistoryVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
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
@RequestMapping("/service/ifa/market/account")
public class AppInvestorAccountRest extends WmesBaseRest {

	@Autowired
	private AppInvestorAccountService investorAccountService;
	

	
	/**
	 * 获取账户列表
	 *@author zxtan
	 *@date 2017-03-23
	 * 调用示例:[地址]/service/ifa/market/account/findInvestorAccountList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c582000","openStatus":""}
	 */
	@RequestMapping(value = "/findInvestorAccountList")
	@ResponseBody
	public ResultWS findInvestorAccountList(HttpServletRequest request,@RequestBody String body) {
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
		Integer rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String memberId = jsonObject.optString("memberId", "");// 获得ifa member id
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		String openStatus = jsonObject.optString("openStatus", "");//开户状态，2：申请中；3：成功；4：失败
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = investorAccountService.findAccountList(jsonPaging, memberId, openStatus,langCode);

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
	 * 得到我的账户详情数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/account/findAccountInfo.r
	 * {"accountId":"40280a25559b852e01559b89c80f0004","langCode":"sc" }
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
//		String toCurrency = jsonObject.optString("toCurrency", "");// 获得MemberBase ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langcode
		if ("".equals(accountId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppAccountInfoVO info = investorAccountService.findAccountInfo(accountId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/account/findAccountOrderHistoryList.r
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
		
		List<AppOrderHistoryVO> list = investorAccountService.findAccountOrderHistoryList(accountId, toCurrency, langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/account/findAccountRPQInfo.r
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
		
		AppAccountRpqInfoVO rpq = investorAccountService.findAccountRPQInfo(accountId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(rpq);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/account/findAccountDocCheckInfo.r
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
		
		List<AppAccountDocInfoVO> list = investorAccountService.findAccountDocCheckInfo(accountId,langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到账户交易信息接口， 通过body传递参数 
	 * 调用示例:[地址]/service/ifa/market/account/findAccountBaseInfo.r
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
		
		AppAccountBaseInfoVO info = investorAccountService.findAccountBaseInfo(accountId, langCode);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
}
