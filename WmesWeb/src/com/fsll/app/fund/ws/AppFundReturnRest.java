package com.fsll.app.fund.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.fund.service.AppFundMarketService;
import com.fsll.app.fund.service.AppFundReturnService;
import com.fsll.app.fund.vo.AppChartDataVO;
import com.fsll.app.fund.vo.AppFundCumulativePerformanceDataVO;
import com.fsll.app.fund.vo.AppFundYearPerformanceDataVO;
import com.fsll.buscore.fund.service.CoreFundService;
import com.fsll.buscore.fund.vo.CoreFundNavVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.StrUtils;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * @author scshi
 *资金回报接口
 *@date 20160615
 */
@RestController
@RequestMapping("/service/fundReturn")
public class AppFundReturnRest  extends WmesBaseRest{
	@Autowired
	private AppFundReturnService fundReturnService;
//	@Autowired
//	private AppFundMarketService fundMarketService;
	
	@Autowired
	private CoreFundService coreFundService;
	
	/**
	 * 获取基金净值图表数据， 通过body传递参数
	 * 调用示例:[地址]/service/fundReturn/getFundChartData.r
	   {"fundId":"8F1E79BAAFA643A6818C364413ED0901","dataType":"NAV","cycle":"D","period":"1Mth","toCurrency":"USD","addiDate":"0"}
	 */
	@RequestMapping(value = "/getFundChartData")
	@ResponseBody
	public ResultWS getFundChartData(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		result = parseParam(request,jsonObject,result,"list");//参数验证
		if(WSConstants.FAIL.equals(result.getRet())){
			return result;
		}
		String fundId = jsonObject.optString("fundId","");
//		String cycle = jsonObject.optString("cycle","D");
		String periodCode = jsonObject.optString("periodCode",CommonConstants.RETURN_PERIOD_CODE_1M);
		String toCurrency = jsonObject.optString("toCurrency",CommonConstants.DEF_CURRENCY);
//		String addiDate = jsonObject.optString("addiDate", "250");// 额外返回的数据周期，可空 -- 默认250
//		String dataType = jsonObject.optString("dataType", "NAV");// 数据类型，目前固定为NAV
		String frequencyType = periodCode.replace("return_period_code_", "");
		
//		AppChartDataVO list = fundMarketService.fundChartData(fundId,dataType,cycle,periodCode,addiDate,toCurrency,langCode);

		List<CoreFundNavVO> list = coreFundService.getFundNav(fundId, frequencyType, toCurrency);
		
		if(list==null){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2001);
			result.setErrorMsg(WSConstants.MSG2001);
			return result;
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(list);
			result.setTotal(list.size());
		}
		return result;
	}
	

	/**
	 * get 获取基金累积表现信息， 通过body传递参数
	 * 调用示例:[地址]/service/fundReturn/getFundCumulativePerformanceInfo.r
	   {"fundId":"1679091C5A880FAF6FB5E6087EB1B2DC","period":"return_period_code_1M","langCode":"sc"}
	 */
	@RequestMapping(value = "/getFundCumulativePerformanceInfo")
	@ResponseBody
	public ResultWS getFundCumulativePerformanceInfo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}	
		String fundId = jsonObject.optString("fundId","");
		String periodCode = jsonObject.optString("periodCode","");
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		if("".equals(fundId) ){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fundId”"+WSConstants.MSG1002);
		}

		List<AppFundCumulativePerformanceDataVO> list = fundReturnService.fundCumulativePerformanceInfo(fundId, periodCode, langCode);
		
		if(list.isEmpty()){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2001);
			result.setErrorMsg(WSConstants.MSG2001);
			return result;
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(list);
			result.setTotal(list.size());
		}
		return result;
	}
	
	/**
	 * get 获取基金年度表现信息， 通过body传递参数
	 * 调用示例:[地址]/service/fundReturn/getFundYearPerformanceInfo.r
	   {"fundId":"1679091C5A880FAF6FB5E6087EB1B2DC","period":"return_period_code_1M","langCode":"sc"}
	 */
	@RequestMapping(value = "/getFundYearPerformanceInfo")
	@ResponseBody
	public ResultWS getFundYearPerformanceInfo(HttpServletRequest request,@RequestBody String body){
		JSONObject jsonObject = JSONObject.fromObject(body); 
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}	
		String fundId = jsonObject.optString("fundId","");
		String year = jsonObject.optString("year","");//指定获取年度
		String years = jsonObject.optString("lastYears","");//上n年
		String langCode=jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		int lastYears = StrUtils.getInt(years);
		if("".equals(fundId) ){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fundId”"+WSConstants.MSG1002);
		}
		
		List<AppFundYearPerformanceDataVO> list = fundReturnService.fundYearPerformanceInfo(fundId,year,lastYears,langCode);
		if(list.isEmpty()){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE2001);
			result.setErrorMsg(WSConstants.MSG2001);
			return result;
		}else{
			result.setRet(WSConstants.SUCCESS);
			result.setErrorCode("");
			result.setErrorMsg("");
			result.setData(list);
			result.setTotal(list.size());
		}
		return result;
	}
	
	
	
	/**
	 * 分析参数,必需参数是否为空
	 * @param request
	 * @param jsonObject
	 * @param result
	 * @param oper add mod
	 * @return
	 */
	private ResultWS parseParam(HttpServletRequest request,JSONObject jsonObject,ResultWS result,String oper){
		result.setRet(WSConstants.SUCCESS);//假定通过验证
		String fundId = jsonObject.getString("fundId")==null?"":jsonObject.getString("fundId");
		if("list".equals(oper)){
//			String cycle = jsonObject.getString("cycle")==null?"":jsonObject.getString("cycle");
			String period = jsonObject.getString("periodCode")==null?"":jsonObject.getString("periodCode");
			if("".equals(fundId)){//参数不能为空
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“fundId”"+WSConstants.MSG1002);
			}
//			if("".equals(cycle)){//参数不能为空
//				result.setRet(WSConstants.FAIL);
//				result.setErrorCode(WSConstants.CODE1002);
//				result.setErrorMsg("“cycle”"+WSConstants.MSG1002);
//			}
			if("".equals(period)){//参数不能为空
				result.setRet(WSConstants.FAIL);
				result.setErrorCode(WSConstants.CODE1002);
				result.setErrorMsg("“period”"+WSConstants.MSG1002);
			}
		}
		
		if("heap".endsWith(oper)&& "".equals(fundId) ){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg("“fundId”"+WSConstants.MSG1002);
		}
		
		return result;
	}
	
}
