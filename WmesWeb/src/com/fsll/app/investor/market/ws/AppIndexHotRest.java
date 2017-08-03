package com.fsll.app.investor.market.ws;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fsll.app.investor.market.service.AppIndexHotService;
import com.fsll.app.investor.market.vo.AppIndexChartVO;
import com.fsll.common.CommonConstants;
import com.fsll.core.ResultWS;
import com.fsll.core.WSConstants;
import com.fsll.wmes.base.WmesBaseRest;

/**
 * 首页热门投资接口服务
 * @author zpzhou
 * @date 2016-9-26
 */
@RestController
@RequestMapping("/service/indexHot")
public class AppIndexHotRest extends WmesBaseRest{
	@Autowired
	private AppIndexHotService indexHotService;
	
	/**
	 * 得到首页组合中用到最多的基金的行情图表数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexHot/findFundIndexChart.r
	 *  {"langCode":"sc","period":"return_period_code_1M","addiDate":"0",num:5}
	 */
	@RequestMapping(value = "/findFundIndexChart")
	@ResponseBody
	public ResultWS findFundIndexChart(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String period = jsonObject.optString("period",CommonConstants.RETURN_PERIOD_CODE_1M);
		String addiData = jsonObject.optString("addiData", "0");// 额外返回的数据周期，可空 -- 默认0
		int num = jsonObject.optInt("num", 5);//返回条数
		List<AppIndexChartVO> list= indexHotService.findFundIndexChart( period, addiData,langCode,num);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
	
	/**
	 * 得到首页组合中收益最多的行情图表数据接口， 通过body传递参数 
	 * 调用示例:[地址]/service/indexHot/findPortfolioIndexChart.r
	 *  {"langCode":"sc","period":"return_period_code_1M","addiDate":"0",num:5}
	 */
	@RequestMapping(value = "/findPortfolioIndexChart")
	@ResponseBody
	public ResultWS findPortfolioIndexChart(HttpServletRequest request,@RequestBody String body) {
		JSONObject jsonObject = JSONObject.fromObject(body);
		ResultWS result = new ResultWS();
		result.setData(null);
		String checkPowerResult = checkPower(jsonObject);
		if(!checkPowerResult.equals(WSConstants.SUCCESS)){
			result.setRet(WSConstants.FAIL);
			result.setErrorCode(checkPowerResult);
			return result;
		}
		String langCode = jsonObject.optString("langCode", CommonConstants.DEF_LANG_CODE);// 语言编码
		String period = jsonObject.optString("period",CommonConstants.RETURN_PERIOD_CODE_1M);
		String addiData = jsonObject.optString("addiData", "0");// 额外返回的数据周期，可空 -- 默认0
		int num = jsonObject.optInt("num", 5);//返回条数
		List<AppIndexChartVO> list= indexHotService.findPortfolioIndexChart( period, addiData,langCode,num);
		result.setRet(WSConstants.SUCCESS);
		result.setErrorCode("");
		result.setErrorMsg("");
		result.setData(list);
		return result;
	}
}
