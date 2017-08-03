package com.fsll.app.investor.discover.ws;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.discover.service.AppCornerInfoService;
import com.fsll.app.investor.discover.vo.AppCornerInfoVO;
import com.fsll.app.investor.discover.vo.AppIfaInfoVO;
import com.fsll.app.investor.discover.vo.AppIfaPerformanceVO;
import com.fsll.app.investor.discover.vo.AppIfaRecommendedVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 圈子信息接口
 * @author zxtan
 * @date 2016-11-21
 */
@RestController
@RequestMapping("/service/cornerInfo")
public class AppCornerInfoRest extends WmesBaseRest {
	@Autowired
	private AppCornerInfoService appCornerInfoService;
	
	/**
	 * 根据参数得到相应的主题列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/cornerInfo/getCornerInfoList.r
	 * {"page":1, "rows":15,"memberId":"40280ad455e327690155e3291c580000"}
	 */
	@RequestMapping(value = "/getCornerInfoList")
	@ResponseBody
	public ResultWS getCornerInfoList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录用户ID
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得当前登录用户ID
		
		if ("".equals(memberId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = appCornerInfoService.findCornerInfoList(jsonPaging, memberId,ifaMemberId);

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
	 * 根据参数得到相应的主题回复列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/cornerInfo/getCornerReplyList.r
	 * {"page":1, "rows":15,"cornerId":"1"}
	 */
	@RequestMapping(value = "/getCornerReplyList")
	@ResponseBody
	public ResultWS getCornerReplyList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String cornerId = jsonObject.optString("cornerId", "");// 获得当前登录用户ID
		
		if ("".equals(cornerId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = appCornerInfoService.findCornerReplyList(jsonPaging, cornerId);

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
	 * 根据参数得到相应的主题回复列表接口， 通过body传递参数 
	 * 调用示例:[地址]/service/cornerInfo/getCornerLikedList.r
	 * {"page":1, "rows":15,"cornerId":"1"}
	 */
	@RequestMapping(value = "/getCornerLikedList")
	@ResponseBody
	public ResultWS getCornerLikedList(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		int page = jsonObject.optInt("page", 1);// 获得当前页数
		int rows = jsonObject.optInt("rows", 10);// 获得每页记录数
		String cornerId = jsonObject.optString("cornerId", "");// 获得当前登录用户ID
		
		if ("".equals(cornerId)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(page);
		jsonPaging.setRows(rows);

		jsonPaging = appCornerInfoService.findCornerLikedList(jsonPaging, cornerId);

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
	 * 得到某个圈子主题详情接口， 通过body传递参数 
	 * 调用示例:[地址]/service/cornerInfo/getCornerInfo.r
	 * {"cornerId":"1"}
	 */
	@RequestMapping(value = "/getCornerInfo")
	@ResponseBody
	public ResultWS getCornerInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String cornerId = jsonObject.optString("cornerId", "");// 获得圈子主题ID
		
		if ( "".equals(cornerId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppCornerInfoVO info = appCornerInfoService.findCornerInfoById(cornerId);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 * 得到某个圈子主题详情接口， 通过body传递参数 
	 * 调用示例:[地址]/service/cornerInfo/getIfaInfo.r
	 * {"cornerId":"1"}
	 */
	@RequestMapping(value = "/getIfaInfo")
	@ResponseBody
	public ResultWS getIfaInfo(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得IfaMemberID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langCode
		
		if ( "".equals(ifaMemberId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppIfaInfoVO info = appCornerInfoService.findIfaInfo(ifaMemberId, langCode);
				
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(info);
		return result;
	}
	
	/**
	 * 得到某个圈子主题详情接口， 通过body传递参数 
	 * 调用示例:[地址]/service/cornerInfo/findIfaPerformance.r
	 * {"ifaMemberId":"40280ad65601004c0156010188390005","toCurrency":"USD","periodCode":"return_period_code_1M","rows":2}
	 */
	@RequestMapping(value = "/findIfaPerformance")
	@ResponseBody
	public ResultWS findIfaPerformance(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得IfaMemberID
		String toCurrency = jsonObject.optString("toCurrency", CommonConstants.DEF_CURRENCY);// 获得periodCode
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1Y);// 获得periodCode
		int rows = jsonObject.optInt("rows", 2);// 获得periodCode
		
		
		if ( "".equals(ifaMemberId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		AppIfaPerformanceVO performanceVO = appCornerInfoService.findIfaPerformance(ifaMemberId, toCurrency, periodCode, rows);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(performanceVO);
		return result;
	}
	
	/**
	 * 得到某个圈子主题详情接口， 通过body传递参数 
	 * 调用示例:[地址]/service/cornerInfo/findIfaRecommended.r
	 * {"ifaMemberId":"40280ad65601004c0156010188390005","langCode":"sc","toCurrency":"USD","periodCode":"return_period_code_1M","rows":2}
	 */
	@RequestMapping(value = "/findIfaRecommended")
	@ResponseBody
	public ResultWS findIfaRecommended(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String ifaMemberId = jsonObject.optString("ifaMemberId", "");// 获得IfaMemberID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得langCode
		String toCurrency = jsonObject.optString("toCurrency", CommonConstants.DEF_CURRENCY);// 获得periodCode
		String periodCode = jsonObject.optString("periodCode", CommonConstants.RETURN_PERIOD_CODE_1Y);// 获得periodCode
		int rows = jsonObject.optInt("rows", 2);// 获得periodCode
		
		
		if ( "".equals(ifaMemberId) ) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}

		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(1);
		jsonPaging.setRows(rows);
		
		AppIfaRecommendedVO recommendedVO  = appCornerInfoService.findIfaRecommended(jsonPaging, ifaMemberId, langCode, toCurrency, periodCode);
		
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(recommendedVO);
		return result;
	}

}
