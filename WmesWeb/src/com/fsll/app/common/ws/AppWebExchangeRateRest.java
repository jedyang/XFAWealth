package com.fsll.app.common.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;
import com.fsll.wmes.entity.WebExchangeRate;
import com.fsll.wmes.fund.service.FundInfoService;

/**
 * 控制器:基础数据
 * 
 * @author michael
 * @date 2016年7月15日
 */
@RestController
@RequestMapping("/service/exchangeRate")
public class AppWebExchangeRateRest extends WmesBaseRest{	
	@Autowired
	private FundInfoService fundInfoService;
	
	/**
	 * 获得汇率列表 通过body传递参数
	 * 调用示例:[地址]/service/exchangeRate/getExchangeRateList.r
	   POST_BODY: {"page":1, "rows":10}
	 */
	@RequestMapping(value = "/getExchangeRateList")
	@ResponseBody
	public ResultWS getExchangeRateList(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}

		String page = jsonObject.optString("page", "1");// 获得当前页数
		String rows = jsonObject.optString("rows", "");// 获得每页记录数
		
		JsonPaging jsonPaging = new JsonPaging();
		jsonPaging.setPage(Integer.parseInt(page));
		jsonPaging.setRows(Integer.parseInt(rows));
		jsonPaging = fundInfoService.findExchangeList(jsonPaging);
			
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
	 * 获得汇率列表 通过body传递参数
	 * 调用示例:[地址]/service/exchangeRate/getExchangeRate.r
	   POST_BODY: {"fromCurrency":'CNY', "toCurrency":"USD"}
	 */
	@RequestMapping(value = "/getExchangeRate")
	@ResponseBody
	public ResultWS getExchangeRate(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
//		result.setData(null);
//		String checkPowerResult = checkPower(jsonObject);
//		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
//			result.setRet(WSConstants.FAIL);
//			result.setErrorCode(checkPowerResult);
//			return result;
//		}
		String fromCurrency = jsonObject.optString("fromCurrency","");
		String toCurrency = jsonObject.optString("toCurrency","");
		if("".equals(fromCurrency) || "".equals(toCurrency)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fromCurrency or toCurrency”"+WSConstants.MSG1002);
			return result;
		}
		WebExchangeRate exchangeRate = fundInfoService.findExchangeRate(fromCurrency, toCurrency);
		if(exchangeRate == null ){//无数据
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2002);
			result.setErrorMsg(WSConstants.MSG2002);
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(exchangeRate);
		}
		return result;
	}
	
	/**
	 * 获得汇率列表 通过body传递参数
	 * 调用示例:[地址]/service/exchangeRate/getCurrencyList.r
	   POST_BODY: {"fromCurrency":'CNY', "toCurrency":"USD"}
	 */
	@RequestMapping(value = "/getCurrencyList")
	@ResponseBody
	public ResultWS getCurrencyList(HttpServletRequest request,@RequestBody String body) {
		ResultWS result = new ResultWS();
		result.setData(null);
		List<String> list = fundInfoService.getCurrencyList();
		if(list.isEmpty()){//无数据
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2002);
			result.setErrorMsg(WSConstants.MSG2002);
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(list);
		}
		return result;
	}
}
