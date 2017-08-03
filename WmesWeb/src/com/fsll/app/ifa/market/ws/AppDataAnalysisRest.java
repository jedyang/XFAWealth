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

import com.fsll.app.ifa.market.service.AppDataAnalysisService;
import com.fsll.app.ifa.market.service.AppStrategyInfoService;
import com.fsll.app.ifa.market.vo.AppPieChartItemVO;
import com.fsll.app.ifa.schedule.service.AppScheduleService;
import com.fsll.app.ifa.schedule.vo.AppScheduleGroupVO;
import com.fsll.app.ifa.schedule.vo.AppScheduleItemVO;
import com.fsll.common.CommonConstants;
import com.fsll.common.util.DateUtil;
import com.fsll.common.util.JsonPaging;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 统计与分析接口
 * @author zxtan
 * @date 2017-04-01
 */
@RestController
@RequestMapping("/service/ifa/market/analysis")
public class AppDataAnalysisRest extends WmesBaseRest {

	@Autowired
	private AppDataAnalysisService dataAnalysisService;
		
	/**
	 * 获取回报分析统计
	 *@author zxtan
	 *@date 2017-04-01
	 * 调用示例:[地址]/service/ifa/market/analysis/findReturnAnalysis.r
	 * {"memberId":"40280ad455e327690155e3291c580000","langCode":"sc"}
	 */
	@RequestMapping(value = "/findReturnAnalysis")
	@ResponseBody
	public ResultWS findReturnAnalysis(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录IFA用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ("".equals(memberId) || "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppPieChartItemVO> list = dataAnalysisService.findReturnAnalysis(memberId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}	
	
	/**
	 * 获取回报分析统计
	 *@author zxtan
	 *@date 2017-04-01
	 * 调用示例:[地址]/service/ifa/market/analysis/findAssetAnalysis.r
	 * {"memberId":"40280ad455e327690155e3291c580000","langCode":"sc"}
	 */
	@RequestMapping(value = "/findAssetAnalysis")
	@ResponseBody
	public ResultWS findAssetAnalysis(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录IFA用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ("".equals(memberId) || "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppPieChartItemVO> list = dataAnalysisService.findAssetAnalysis(memberId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 获取回报分析统计
	 *@author zxtan
	 *@date 2017-04-01
	 * 调用示例:[地址]/service/ifa/market/analysis/findGeoAnalysis.r
	 * {"memberId":"40280ad455e327690155e3291c580000","langCode":"sc"}
	 */
	@RequestMapping(value = "/findGeoAnalysis")
	@ResponseBody
	public ResultWS findGeoAnalysis(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		
		String memberId = jsonObject.optString("memberId", "");// 获得当前登录IFA用户ID
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 获得当前语言
		
		if ("".equals(memberId) || "".equals(langCode)) {
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(WSConstants.CODE1002);
			result.setErrorMsg(WSConstants.MSG1002);
			return result;
		}
		
		List<AppPieChartItemVO> list = dataAnalysisService.findGeoAnalysis(memberId, langCode);

		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	

}
